-module(dbcs_asset_ref).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/dbcs_common.hrl").
-include("../../core/include/monitor.hrl").
-define(Table,"asset_ref").

base_property(id)->
	true;
base_property(app_)->
	true;
base_property(_)->
	false.
    
build_ref([], _, Result) ->lists:reverse(Result);
build_ref([F|R], Count, Result) ->
    NF = case F of
        {_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}, {total, Count}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end,
    build_ref(R, Count, [NF|Result]).
    
ref_to_db(Ref)->
	Advance = [dbcs_base:term2db(K,V)||{K,V}<- Ref,base_property(K)=:=false],
	case proplists:get_value(id, Ref) of
		Id when is_list(Id) ->
			{content,list_to_atom(?Table), list_to_atom(Id), <<"asset_ref">>,null,null,null,null,?Author,null,null,null,null,null,Advance};
		_->
			{}
	end.

db_to_ref(RefData)->
	case RefData of
		{_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end.

create_ref(Ref) ->
	case db_ecc:insert_data(?DBName,?Table,ref_to_db(Ref)) of
		{ok,Ret}->
			{ok,db_to_ref(Ret)};
		Err->
			Err
	end.

get_ref(Id) ->
    Ret = if
        is_atom(Id) ->
            db_ecc:get_data(?DBName,?Table,"type=asset_ref & id="++atom_to_list(Id));
        is_list(Id) ->
            db_ecc:get_data(?DBName,?Table,"type=asset_ref & id="++Id);
        true ->
            []
    end,
	case Ret of
		{error,Reason}->
			{error,Reason};
		_->
			case length(Ret) of
				0 ->
					{error,not_found};
				_ ->
					[Ref|_] = Ret,
					db_to_ref(Ref)
			end
	end.

%% @doc 万能数据库查询操作
get_ref_where(Where, Order)->     
    Ret = db_ecc:get_data_stat(?DBName, ?Table, Where,Order),
    case Ret of
        {_Start, _End, Count, Contents} ->
            build_ref(Contents, Count, []);
        _ ->
            []
    end.

%%查询标签By Where Condition
get_refWhere(Condition=#query_condition1{}) ->
    %%HostName = erlang:list_to_atom(Hostname),
    BeamCondition = asset_util:build_condition(Condition),
    Where = BeamCondition#query_beam_condition1.where,
    Order = BeamCondition#query_beam_condition1.order,
    get_ref_where(Where, Order).

update_ref(Ref)->
	case proplists:get_value(id, Ref) of
        undefined ->
            {error,not_found_id};
		Id when is_list(Id) ->
			db_ecc:update_data(?DBName,?Table,"id=" ++ Id,ref_to_db(Ref))
    end.

get_all()->
    case db_ecc:get_data(?DBName,?Table,"") of
		{error,_}->
			[];
		Ret ->
			[db_to_ref(X)||X <- Ret]
	end.


remove_ref(Id) when is_atom(Id)->
	db_ecc:delete_data(?DBName,?Table,"id="++atom_to_list(Id));
remove_ref(Id) when is_list(Id)->
	db_ecc:delete_data(?DBName,?Table,"id="++Id);
remove_ref(_)->{error,parameter_error}.


get_ref_match(Where) when is_list(Where)->
	Ret = db_ecc:get_data(?DBName,?Table,Where),
	case Ret of
		{error,_}->
			[];
		_->
			[db_to_ref(X)||X <- Ret]
	end;
get_ref_match(_)->{error,where_should_be_list}.