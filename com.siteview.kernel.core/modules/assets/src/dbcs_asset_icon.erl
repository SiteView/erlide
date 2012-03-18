-module(dbcs_asset_icon).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/dbcs_common.hrl").
-include("../../core/include/monitor.hrl").
-define(Table,"asset_icon").

base_property(id)->
	true;
base_property(app_)->
	true;
base_property(_)->
	false.
    
    
icon_to_db(Icon)->
	Advance = [dbcs_base:term2db(K,V)||{K,V}<- Icon,base_property(K)=:=false],
	case proplists:get_value(id, Icon) of
		Id when is_list(Id) ->
			{content,list_to_atom(?Table), list_to_atom(Id), <<"asset_icon">>,null,null,null,null,?Author,null,null,null,null,null,Advance};
		_->
			{}
	end.

db_to_icon(IconData)->
	case IconData of
		{_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end.
    
build_icon([], _, Result) ->lists:reverse(Result);
build_icon([F|R], Count, Result) ->
    NF = case F of
        {_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}, {total, Count}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end,
    build_icon(R, Count, [NF|Result]).

create_icon(Icon) ->
	case db_ecc:insert_data(?DBName,?Table,icon_to_db(Icon)) of
		{ok,Ret}->
			{ok,db_to_icon(Ret)};
		Err->
			Err
	end.

get_icon(Id) ->
    Ret = if
        is_atom(Id) ->
            db_ecc:get_data(?DBName,?Table,"type=asset_icon & id="++atom_to_list(Id));
        is_list(Id) ->
            db_ecc:get_data(?DBName,?Table,"type=asset_icon & id="++Id);
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
					[Icon|_] = Ret,
					db_to_icon(Icon)
			end
	end.

update_icon(Icon)->
	case proplists:get_value(id, Icon) of
        undefined ->
            {error,not_found_id};
		Id when is_list(Id) ->
			db_ecc:update_data(?DBName,?Table,"id=" ++ Id,icon_to_db(Icon))
    end.

get_all()->
    case db_ecc:get_data(?DBName,?Table,"") of
		{error,_}->
			[];
		Ret ->
			[db_to_icon(X)||X <- Ret]
	end.


remove_icon(Id) when is_atom(Id)->
	db_ecc:delete_data(?DBName,?Table,"id="++atom_to_list(Id));
remove_icon(Id) when is_list(Id)->
	db_ecc:delete_data(?DBName,?Table,"id="++Id);
remove_icon(_)->{error,parameter_error}.

%%查询标签By Where Condition
get_iconWhere(Condition=#query_condition1{}) ->
    %%HostName = erlang:list_to_atom(Hostname),
    BeamCondition = asset_util:build_condition(Condition),
    Where = BeamCondition#query_beam_condition1.where,
    Order = BeamCondition#query_beam_condition1.order,
    get_icon_where(Where, Order).
    
%% @doc 万能数据库查询操作
get_icon_where(Where, Order)->     
    Ret = db_ecc:get_data_stat(?DBName, ?Table, Where,Order),
    case Ret of
        {_Start, _End, Count, Contents} ->
            build_icon(Contents, Count, []);
        _ ->
            []
    end.

get_icon_match(Where) when is_list(Where)->
	Ret = db_ecc:get_data(?DBName,?Table,Where),
	case Ret of
		{error,_}->
			[];
		_->
			[db_to_icon(X)||X <- Ret]
	end;
get_icon_match(_)->{error,where_should_be_list}.