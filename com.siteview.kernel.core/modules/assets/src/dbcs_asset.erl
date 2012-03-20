%%
%% @doc device database module
%%
%%
-module(dbcs_asset).
-compile(export_all).
-include("../../core/include/dbcs_common.hrl").
-include("asset.hrl").
-include("../../core/include/monitor.hrl").
-define(Table,"asset").
-define(AssetLabel, "assetLabel").

base_property(id)->
	true;
base_property(app_)->
	true;
base_property(_)->
	false.

build_asset([], _, Result) ->lists:reverse(Result);
build_asset([F|R], Count, Result) ->
    NF = case F of
        {_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}, {total, Count}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end,
    build_asset(R, Count, [NF|Result]).
    
asset_to_db(Asset)->
	Advance = [dbcs_base:term2db(K,V)||{K,V}<- Asset, base_property(K)=:=false],
	case proplists:get_value(id, Asset) of
		Id when is_list(Id)->
			{content,list_to_atom(?Table), list_to_atom(Id), <<"asset">>,null,null,null,null,?Author,null,null,null,null,null,Advance};
		_->
			{}
	end.

db_to_asset(AssetData)->
	case AssetData of
		{_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end.

create_asset(Asset)->
	case db_ecc:insert_data(?DBName,?Table,asset_to_db(Asset)) of
		{ok,Ret}->
			{ok,db_to_asset(Ret)};
		Err->
			Err
	end.

%% @doc 万能数据库查询操作
get_asset_where(Where, Order)->     
    Ret = db_ecc:get_data_stat(?DBName, ?Table, Where,Order),
    case Ret of
        {_Start, _End, Count, Contents} ->
            build_asset(Contents, Count, []);
        _ ->
            []
    end.

remove_all()->
    Assets = get_all(),
    [remove_asset(proplists:get_value(id, X))||X<- Assets],
    {ok, finish}.

%%查询标签By Where Condition
get_assetWhere(Condition=#query_condition1{}) ->
    %%HostName = erlang:list_to_atom(Hostname),
    BeamCondition = asset_util:build_condition(Condition),
    Where = BeamCondition#query_beam_condition1.where,
    Order = BeamCondition#query_beam_condition1.order,
    get_asset_where(Where, Order).
    
get_asset(Id) ->
    Ret = if
        is_atom(Id) ->
            db_ecc:get_data(?DBName,?Table,"type=asset & id="++atom_to_list(Id));
        is_list(Id) ->
            db_ecc:get_data(?DBName,?Table,"type=asset & id="++Id);
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
					[Asset|_] = Ret,
					db_to_asset(Asset)
			end
	end.

update_asset(Asset)->
	case proplists:get_value(id, Asset) of
        undefined ->
            {error,not_found_id};
		Id ->
			db_ecc:update_data(?DBName,?Table,"id=" ++ Id,asset_to_db(Asset))
    end.

get_all()->
    case db_ecc:get_data(?DBName,?Table,"") of
		{error,_}->
			[];
		Ret ->
			[db_to_asset(X)||X <- Ret]
	end.


remove_asset(Id) when is_atom(Id)->
	db_ecc:delete_data(?DBName,?Table,"id="++atom_to_list(Id));
remove_asset(Id) when is_list(Id)->
	db_ecc:delete_data(?DBName,?Table,"id="++Id);
remove_asset(_)->{error,parameter_error}.


get_asset_match(Where) when is_list(Where)->
	Ret = db_ecc:get_data(?DBName,?Table,Where),
	case Ret of
		{error,_}->
			[];
		_->
			[db_to_asset(X)||X <- Ret]
	end;
get_asset_match(_)->{error,where_should_be_list}.