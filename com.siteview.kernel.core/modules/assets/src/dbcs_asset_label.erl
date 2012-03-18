%% ---
%%资产标签数据库存储模块
%% [{id,ID},{app_,App},{name,Name},{index,Index},{isSyslabel,IsSyslabel},{templateid,TemplateId},{hide,Hide},{value,Value},{icon, Icon},{treeindex,TreeIndex},{maxchild,MaxChild}]
%%
%%---
-module(dbcs_asset_label).
-compile(export_all).
-include("../../core/include/dbcs_common.hrl").
-include("asset.hrl").
-include("../../core/include/monitor.hrl").

build_label([],R)->lists:reverse(R);
build_label([F|R], Result) ->
    NF = case F of
        {_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end,
    build_label(R, [NF|Result]).

base_property(id)->
	true;
base_property(app_)->
	true;
base_property(_)->
	false.

%% @doc 万能数据库查询操作
get_label_where(Where, Order)->      
   case db_ecc:get_data_stat(?DBName,atom_to_list(?MODULE), Where,Order) of
    {_Start, _End, _Count, Contents} ->
            build_label(Contents, []);
     _->[]
     end.

%% 获取所有标签
get_all_label() ->
    Ret = db_ecc:get_data(?DBName,  atom_to_list(?MODULE), ""),
	case is_list(Ret) of
		false ->
			[];
		true ->
			[db_to_data(Data) || Data <- Ret] 			
	end.

get_labelWhere(Condition=#query_condition1{}) ->
    BeamCondition = asset_util:build_condition(Condition),
    Where = BeamCondition#query_beam_condition1.where,
    Order = BeamCondition#query_beam_condition1.order,
    get_label_where(Where, Order).


%% @doc 根据id获取标签
get_LabelById(Id) when is_list(Id)->
    Where = "id="++Id,
    Ret = db_ecc:get_data(?DBName, atom_to_list(?MODULE), Where),
	case is_list(Ret) of
		false ->
			[];
		true ->
        case Ret of 
        []->[];
        _->   [Data|_] 	=Ret,
            db_to_data(Data)
        end
	end;
get_LabelById(Id) when is_atom(Id)->
    Where = "id="++atom_to_list(Id),
    Ret = db_ecc:get_data(?DBName, atom_to_list(?MODULE), Where),
	case is_list(Ret) of
		false -> 
			[];
		true ->
        case Ret of 
            []->[];
            _->   [Data|_] =Ret,
            db_to_data(Data)
        end
	end;
get_LabelById(_)->{error,"id format error"}.
    
%% @doc 新建一个标签
create_label(Label)->
	Id = erlang:list_to_atom(proplists:get_value(id,Label)),
	Content = data_to_db(Label),
	db_ecc:insert_data(?DBName,atom_to_list(?MODULE), Content).
    
%% @doc 新建一个标签
update_label(Label) ->
    Id = proplists:get_value(id,Label),
	Where = "id = " ++ Id,
	NewRecord = data_to_db(Label),
  db_ecc:update_data(?DBName, atom_to_list(?MODULE), Where, NewRecord).
    
%% @doc 根据id删除标签一个标签
remove_label(Id) when is_atom(Id)->
	db_ecc:delete_data(?DBName, atom_to_list(?MODULE),"id='"++atom_to_list(Id));
remove_label(Id) when is_list(Id)->
	db_ecc:delete_data(?DBName, atom_to_list(?MODULE),"id='"++Id);
remove_label(_)->   {error,"id format error"}.

%% @doc 万能数据库删除操作
remove_label_where(Where)->     
    db_ecc:delete_data(?DBName,atom_to_list(?MODULE), Where).

%%删除标签By Where
remove_labelWhere(Condition=#query_condition1{}) ->
    %%HostName = erlang:list_to_atom(Hostname),
    BeamCondition = asset_util:build_condition(Condition),
    Where = BeamCondition#query_beam_condition1.where,
    remove_label_where(Where).

remove_all()->
    db_ecc:delete_data(?DBName,  atom_to_list(?MODULE), "").

remove_AllSysLabel() ->
    Maches = get_all_label(),
    remove_AllSysLabel(Maches).
remove_AllSysLabel([]) ->
    {ok, "remove_label_ok"};
remove_AllSysLabel([Mach|T]) ->
    case proplists:get_value(isSyslabel,Mach)  of
    "true"->
        remove_label(proplists:get_value(id,Mach));
    _->[]
    end,
    remove_AllSysLabel(T).

%% @doc 删除所有标签
remove_AllLabel() ->
    Maches = get_all_label(),
    remove_AllLabel_t(Maches).
remove_AllLabel_t([]) ->
    {ok, "remove_label_ok"};
remove_AllLabel_t([Mach|T]) ->
        remove_label(proplists:get_value(id,Mach)),
    remove_AllLabel_t(T).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

data_to_db(Data)->
    {value,{id,ID}} = lists:keysearch(id,1,Data),
	Advance = [dbcs_base:term2db(K,V)||{K,V}<- Data,base_property(K) =:= false],
	case [is_list(ID)] of
		[true]->
            {content,?MODULE, list_to_atom(ID), <<"dbcs_asset_label">>,null,null,null,null,?Author,null,null,null,null,null,Advance};
		_->
			{content,?MODULE, ID, <<"dbcs_asset_label">>,null,null,null,null,?Author,null,null,null,null,null,Advance}
	end.

db_to_data(Data)->
    case Data of
		 {_,App,Id,_,_,_,_,_,_,_,_,_,_,_,Adv}->
			[{id,atom_to_list(Id)},{app_,atom_to_list(App)}] ++ [dbcs_base:db2term(K,T,V)||{K,T,V}<- Adv];
		_->
			[]
	end.
    