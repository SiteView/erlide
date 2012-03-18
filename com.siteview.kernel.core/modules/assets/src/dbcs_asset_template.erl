%%
%% @doc device database module
%%
%%
-module(dbcs_asset_template).
-compile(export_all).
-include("../../core/include/dbcs_common.hrl").
-include("asset.hrl").
-include("../../core/include/monitor.hrl").
-define(Table,"asset_template").
-define(AssetLabel, "assetLabel").

base_property(id)->
	true;
base_property(app_)->
	true;
base_property(_)->
	false.

build_template([], _, Result) ->lists:reverse(Result);
build_template([F|R], Count, Result) ->
    NF = case F of
        {_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)},{total,Count}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end,
    build_template(R, Count, [NF|Result]).
    
template_to_db(Temp)->
	Advance = [dbcs_base:term2db(K,V)||{K,V}<- Temp, base_property(K)=:=false],
	case proplists:get_value(id, Temp) of
		Id when is_list(Id)->
			{content,list_to_atom(?Table), list_to_atom(Id), <<"asset_template">>,null,null,null,null,?Author,null,null,null,null,null,Advance};
		_->
			{}
	end.

db_to_template(TempData)->
	case TempData of
		{_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end.

create_template(Temp)->
	case db_ecc:insert_data(?DBName,?Table,template_to_db(Temp)) of
		{ok,Ret}->
			{ok,db_to_template(Ret)};
		Err->
			Err
	end.

%% @doc 万能数据库查询操作
get_template_where(Where, Order)->     
    Ret = db_ecc:get_data_stat(?DBName, ?Table, Where,Order),
    case Ret of
        {_Start, _End, Count, Contents} ->
            build_template(Contents, Count, []);
        _ ->
            []
    end.

remove_all()->
    Temps = get_all(),
    [remove_template(proplists:get_value(id, X))||X<- Temps],
    {ok, finish}.

%%查询标签By Where Condition
get_templateWhere(Condition=#query_condition1{}) ->
    %%HostName = erlang:list_to_atom(Hostname),
    BeamCondition = asset_util:build_condition(Condition),
    Where = BeamCondition#query_beam_condition1.where,
    Order = BeamCondition#query_beam_condition1.order,
    get_template_where(Where, Order).
    
get_template(Id) ->
    Ret = if
        is_atom(Id) ->
            db_ecc:get_data(?DBName,?Table,"type=asset_template & id="++atom_to_list(Id));
        is_list(Id) ->
            db_ecc:get_data(?DBName,?Table,"type=asset_template & id="++Id);
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
					[Temp|_] = Ret,
					db_to_template(Temp)
			end
	end.

update_template(Temp)->
	case proplists:get_value(id, Temp) of
        undefined ->
            {error,not_found_id};
		Id ->
			db_ecc:update_data(?DBName,?Table,"id=" ++ Id,template_to_db(Temp))
    end.

get_all()->
    case db_ecc:get_data(?DBName,?Table,"") of
		{error,_}->
			[];
		Ret ->
			[db_to_template(X)||X <- Ret]
	end.


remove_template(Id) when is_atom(Id)->
	db_ecc:delete_data(?DBName,?Table,"id="++atom_to_list(Id));
remove_template(Id) when is_list(Id)->
	db_ecc:delete_data(?DBName,?Table,"id="++Id);
remove_template(_)->{error,parameter_error}.


get_template_match(Where) when is_list(Where)->
	Ret = db_ecc:get_data(?DBName,?Table,Where),
	case Ret of
		{error,_}->
			[];
		_->
			[db_to_template(X)||X <- Ret]
	end;
get_template_match(_)->{error,where_should_be_list}.