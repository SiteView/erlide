%%建立资产与标签的连接
-module(asset_label_manage).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").
-behaviour(gen_server).
%% gen_server callbacks
 -export([init/1, handle_call/3, handle_cast/2, handle_info/2, code_change/3, terminate/2]).

%%%%%%%%%%%%%%% user_api %%%%%%%%%%%%%%%%%%%% 
start_link()->
    gen_server:start_link({local, ?MODULE}, ?MODULE, [], []).

init([])->
    create_root(),%%启动ecc时，初始化系统标签
    create_sysLabel(),
	{ok,[]}.

stop() ->
    gen_server:cast(?MODULE, stop).

call(Info)->
    case whereis(?MODULE) of
        undefined   ->  start_link();
        _   ->  ok
    end,
     gen_server:call(?MODULE,Info).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%删除标签和删除资产的时候，应该一起删除它们对应的连接关系，避免冗余
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% @spec set_asset2label(Labelid,AssetIds) -> {ok,Result} | {error,Reason}
%% where
%% Labelid=string()
%% AssetIds = list()
%% @doc 将多个资产打到一个标签组,Labelid 标签id ,  AssetIds 资产id 列表
set_asset2label(Labelid,AssetIds)->
%%
    N = erlang:length(AssetIds),
    case dbcs_asset_label:get_LabelById(Labelid) of
    []->{error,"unknow label"};
    {error,Err}->{error,Err};
    Label->
        NEWLable = lists:keyreplace(index ,1,Label,{index ,proplists:get_value(index ,Label) + N}),
         case dbcs_asset_label:update_label(NEWLable) of
         {error,Err}->{error,Err};
         _->
         Result = [call({create_relation,{Labelid,Assetid}})||Assetid<-AssetIds],
            case proplists:get_value(error, Result) of
                undefined->{ok,"create sucess"};
                Err->{error,Err}
            end
         end
     end.
     
%% @spec remove_assetFromLabel(Labelid,Assetids) -> {ok,"remove sucess"} | {error,Reason}
%% where
%% Labelid=string()
%% Assetids = list()
%% @doc 将多个资产从一个标签中删除,Labelid 标签id ,  AssetIds 资产id 列表
%%
remove_assetFromLabel(Labelid,Assetids)->
     N = erlang:length(Assetids),
     case dbcs_asset_label:get_LabelById(Labelid) of
     []->{error,"unknow label"};
     {error,Err}->{error,Err};
     Label->
    %%将新增的设别的数加到标签中的设备数标记
        NLable = lists:keyreplace(index,1,Label,{index,proplists:get_value(index,Label)-N}),
         case dbcs_asset_label:update_label(NLable) of
             {error,Err}->{error,Err};
             _->
                call({remove_labelByasset,{Labelid,Assetids}})%%删除联系      
        end
    end.

%% @spec get_assetByLabel(Labelid) -> Result | {error,Reason}
%% where
%% Labelid = list()
%% @doc 获得标签对应的资产,Labelid 标签id
%%
get_assetByLabel(Labelid,Index,Count,Sort,SortType)->
    R = call({get_assetBylabel,{Labelid,Index,Count,Sort,SortType}}),
    case R of
    {ok,Aids}->
        case length(Aids)>0 of
        true->
            get_assetByLabel_t(Aids,Index,Count,Sort,SortType);
        _->[]
        end;
    Err->Err
    end.
%%标签列表  
get_assetByLabels([],_Index,_Count,_Sort,_SortType)->[];
get_assetByLabels([Labelid|Next],Index,Count,Sort,SortType)->
    case call({get_assetBylabel,{Labelid,0,0,"",""}}) of
    {ok,Result}->
    Aids = Result++get_assetByLabels(Next,Index,Count,Sort,SortType),
    get_assetByLabel_t(Aids,Index,Count,Sort,SortType);
    Err->Err
    end.

get_assetByLabel_t(Aids,Index,Count,Sort,SortType)->
    Where =#query_condition_where{
        where=[{"id","in",textutils:listToStr(Aids),"&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= Index,
                     count=Count,
                     sort=Sort,
                     sortType=SortType
    },
    dbcs_asset:get_assetWhere(Query_Condition).

%% @spec get_labelByAsset(Assetid) -> Result | {error,Reason}
%% where
%% Assetid 资产id
%% @doc 获得一个资产对应的标签
%%
get_labelByasset(Assetid,Index, Count, Sort, SortType)->
    R = call({get_labelByasset,{Assetid,0, 0, "", ""}}),
    case R of
    {ok,Lids}->
        case length(Lids)>0 of
        true->
            Where =#query_condition_where{
            where=[{"id","in",textutils:listToStr(Lids),"&"}]
            },
            Query_Condition=#query_condition1{
                            where=Where,
                             index= Index,
                             count=Count,
                             sort=Sort,
                             sortType=SortType
            },
            dbcs_asset_label:get_labelWhere(Query_Condition);
     _->[]
     end;
   Err->Err
   end.

create_root()->
    Root = [{id,?LABEL_ROOT},{app_,dbcs_base:get_app()},{name,?LABEL_ROOT},{index,0},{isSyslabel,"false"},{templateid,""},   
    {hide,"false"},{value,?LABEL_ROOT},{icon,""},{treeindex,?LABEL_ROOT_INDEX},{maxchild,"0:2"}],  %%总根节点
     User_Root = [{id,?USER_ROOT},{app_,dbcs_base:get_app()},{name,?USER_ROOT},{index,0},{isSyslabel,"false"},{templateid,""},   
        {hide,"false"},{value,?USER_ROOT},{icon,""},{treeindex,?USER_ROOT_INDEX},{maxchild,"0:2:0"}],  %%用户标签根节点
    case get_label_byId(?LABEL_ROOT) of
        {error,_}-> create_label(Root);
        []->create_label(Root);
        _->[]
    end,
    case get_label_byId(?USER_ROOT) of
        {error,_}-> create_label(User_Root);
        []->create_label(User_Root);
        _->[]
    end.
    
%% @spec create_sysLabel()
%% @doc 创建全部系统标签
%%
create_sysLabel()->
        dbcs_asset_label:remove_AllSysLabel(),
        Template_Root = [{id,?TEMPLATE_ROOT},{app_,dbcs_base:get_app()},{name,?TEMPLATE_ROOT},{index,0},{isSyslabel,"true"},{templateid,""},   
        {hide,"false"},{value,?TEMPLATE_ROOT},{icon,""},{treeindex,?TEMPLATE_ROOT_INDEX},{maxchild,"0:1:0"}],  %%用户标签根节点
        create_label(Template_Root),
        case asset_template:get_root_templates() of
        {error, Reason} ->
            {error, Reason};
        Templates ->
                create_sysLabel_children(Templates,"root")
        end.
        
create_sysLabel_children([],_Type)->{ok,"Create ok"};
create_sysLabel_children([Template|Next],Type)->
    case proplists:get_value(id,Template) of
    undefined->
        create_sysLabel_children(Next,Type),
        {error,"template id error"};
    ID->
        case Type of
         "root"->
            create_sysLabel_L([Template],[]);
          PID->
            create_sysLabel_L([Template],PID)
        end,
        case asset_template:get_child_template(ID,0,0,"","") of
        {error,_}->
            create_sysLabel_children(Next,Type);
        T->
            create_sysLabel_children(T,ID),
            create_sysLabel_children(Next,Type)
        end
    end.
%% @spec create_sysLabel_L([Template])
%% where
%% Template = list()
%% @doc 添加一个系统标签,Parentid父亲标签的id
%%
create_sysLabel_L([],_)->{ok,"Create ok"};
create_sysLabel_L([Template|Next],Parentid)->
    case proplists:get_value(id,Template) of
    undefined->
        create_sysLabel_L(Next,Parentid),
        {error,"template id error"};
    ID->
        PID = case Parentid of
        []-> ?TEMPLATE_ROOT;
        _->
           "temp-"++Parentid
        end,
        Name= proplists:get_value(alias,Template),
        Icon = proplists:get_value(icon, Template, "template"),
        case get_label_byId(PID) of
        {error, _} ->
            create_sysLabel_L(Next,Parentid),
            {error, label_not_found};
        []->create_sysLabel_L(Next,Parentid),
            {error, label_not_found};
        PLabel->
            TreeIndex =case proplists:get_value(maxchild,PLabel) of
            undefined->proplists:get_value(treeindex,PLabel) ++ ":1";
            Maxchild ->
                case string:substr(Maxchild,string:rstr(Maxchild,":")+1,string:len(Maxchild)) of
                []-> proplists:get_value(treeindex,PLabel) ++ ":1";
                M-> proplists:get_value(treeindex,PLabel)++":"++ integer_to_list(list_to_integer(M)+1)
                end
            end,
            NPLabel = lists:keyreplace(maxchild,1,PLabel,{maxchild,TreeIndex}),
            case update_label(NPLabel) of
            {error,_}->
                    create_sysLabel_L(Next,Parentid),
                    {error,"update parent error"};
            _->
                 Label= [{id,"temp-"++ID},{app_,dbcs_base:get_app()},{name,Name},{index,0},{isSyslabel,"true"},{templateid,ID},   %%如果是系统标签，对应的资产模板id
                         {hide,"false"},{value,ID},{icon, Icon},{treeindex,TreeIndex},{maxchild,TreeIndex++":0"}],
                create_label(Label),
                create_sysLabel_L(Next,Parentid)
            end
        end
    end.

create_sysLabel_L(Template) when is_list(Template)->
    case proplists:get_value(id,Template) of
    undefined->
        {error,"template id error"};
    ID->
        Name = proplists:get_value(alias,Template),
        Icon = proplists:get_value(icon,Template),
        PAlias =  proplists:get_value(derived,Template),
        Pid =
        if  PAlias =:= ?ROOT ->
                ?TEMPLATE_ROOT;
            true->
                case asset_template:get_template_by_alias([PAlias]) of
                {error,_}->?TEMPLATE_ROOT;
                PTemplate->
                Parentid = proplists:get_value(id,PTemplate),
                    "temp-"++Parentid
                end
        end,
        case get_label_byId(Pid) of
        {error, _} ->   {error, label_not_found};
        []->{error, label_not_found};
         PLabel->
            TreeIndex =case proplists:get_value(maxchild,PLabel) of
            undefined->proplists:get_value(treeindex,PLabel) ++ ":1";
            Maxchild ->
                case string:substr(Maxchild,string:rstr(Maxchild,":")+1,string:len(Maxchild)) of
                []-> proplists:get_value(treeindex,PLabel) ++ ":1";
                M-> proplists:get_value(treeindex,PLabel)++":"++ integer_to_list(list_to_integer(M)+1)
                end
            end,
            NPLabel = lists:keyreplace(maxchild,1,PLabel,{maxchild,TreeIndex}),
            case update_label(NPLabel) of
            {error,_}->
                    {error,"update parent error"};
            _->
                 Label= [{id,"temp-"++ID},{app_,dbcs_base:get_app()},{name,Name},{index,0},{isSyslabel,"true"},{templateid,ID},   %%如果是系统标签，对应的资产模板id
                         {hide,"false"},{value,ID},{icon, Icon},{treeindex,TreeIndex},{maxchild,TreeIndex++":0"}],
                create_label(Label)
            end
        end
    end.
        
remove_sysLabel(TempId) when is_list(TempId) ->
    remove_label("temp-"++TempId);
remove_sysLabel(_) ->
    {error, params_error}.
    
update_sysLabel(Temp) when is_list(Temp) ->
    ID = proplists:get_value(id, Temp),
    case get_label_byId("temp-"++ID) of
        {error, _} ->
            {error, label_not_found};
        Label ->
            Name = proplists:get_value(alias, Temp, ""),
            Icon = proplists:get_value(icon, Temp, "template"),
            L1 = lists:keyreplace(name, 1, Label, {name, Name}),
            L2 = case proplists:get_value(icon, L1) of
                undefined ->
                    [{icon, Icon}|L1];
                _ ->
                    lists:keyreplace(icon, 1, Label, {icon, Icon})
            end,
            update_label(L2)
    end.
    
%% @spec create_label(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label 标签实体
%% @doc 创建一个新的label
%%
create_label(Label)->
    dbcs_asset_label:create_label(Label).

%% @spec get_label_byId(ID) -> Result | {error,Reason}
%% where
%% ID = list()
%% @doc 通过ID获得标签的实体,ID 标签id
%%
get_label_byId(ID)->
    dbcs_asset_label:get_LabelById(ID).

%% @spec update_label(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label = list()
%% @doc 更新一个Label,Label 新的标签实体
%%
update_label(Label)->
    dbcs_asset_label:update_label(Label).
    
%% @spec remove_label(Id) -> {ok,Result} | {error,Reason}
%% where
%% Id = string()
%% @doc 删除一个标签,Id 标签id
%%
remove_label(Id)->
    case dbcs_asset_label:remove_label(Id)of
    {ok,_}->
        %%删除与标签相关联的全部联系
        call({remove_labelandassetall,{Id}});
    Err->Err
    end.

remove_asset_relation(Assetid)->
     call({remove_assetandlabelall,{Assetid}}).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  构造标签树方法
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

getLabelLikeIndex(Index)->
    Where =#query_condition_where{
        where=[{"my.treeindex","like",""++Index++"","&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= 0,
                     count=0,
                     sort="",
                     sortType=""
    },
    Result = dbcs_asset_label:get_labelWhere(Query_Condition),
    [Label||Label<-Result,string:str(proplists:get_value(treeindex,Label),Index)=:=1].


%% @spec create_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label=list()
%% @doc 创建标签树上的一个节点,Label 标签实体
%%
create_labeltreeNode(Parentid,Label)->
   PLabelid = 
   case Parentid of 
        []->   ?USER_ROOT;
         _-> Parentid
    end,
    case get_label_byId(PLabelid) of 
    []->    {error,"error parentid"};
    {error,Err}->{error,Err};
    PLabel->
        io:format("~n PLabel:~p",[PLabel]),
        Maxchild = proplists:get_value(maxchild,PLabel),
        io:format("~n Maxchild:~p",[Maxchild]),
        TreeIndex = 
        case string:substr(Maxchild,string:rstr(Maxchild,":")+1,string:len(Maxchild)) of
        []->  proplists:get_value(treeindex,PLabel) ++ ":1";
        M-> proplists:get_value(treeindex,PLabel)++":"++ integer_to_list(list_to_integer(M)+1)
        end,
        io:format("~n TreeIndex:~p",[TreeIndex]),
        NPLabel = lists:keyreplace(maxchild,1,PLabel,{maxchild,TreeIndex}),
        case update_label(NPLabel) of
        {error,_}->
                {error,"update parent error"};
        _->
            NLabel1 = lists:keyreplace(treeindex,1,Label,{treeindex,TreeIndex}),
            NLabel2 = lists:keyreplace(maxchild,1,NLabel1,{maxchild,TreeIndex++":0"}),
            create_label(NLabel2)
        end  
    end.

%% @spec update_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label=list()
%% @doc 更新标签树的节点,Label 标签实体
%%
update_labeltreeNode([])->{ok,"update sucess"};
update_labeltreeNode([Label|Next])->
    case  update_label(Label) of
    {ok,_}->    update_labeltreeNode(Next);
    Err->  Err
    end;
update_labeltreeNode(_)->{error,"unknow format"}.

%% @spec update_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label 标签实体
%% @doc 删除树节点，同时删除子节点
%%
remove_labeltreeNode([])->{ok,"remove label succeed "};
remove_labeltreeNode("[]")->{ok,"ok"};
remove_labeltreeNode([Lid])->
    %%先删除子节点，在删除自己.
    case get_label_byId(Lid) of 
    []->    {error,"error parentid"};
    {error,Err}->{error,Err};
    Label->
        Labels = getLabelLikeIndex(proplists:get_value(treeindex,Label)),
        [remove_label(proplists:get_value(id,L)) || L<-Labels,string:str(proplists:get_value(treeindex,L),proplists:get_value(treeindex,Label))=:=1]
    end.
    


%% @spec update_labeltreeNode(Lid, Index, Count, Sort, SortType) -> Result | {error,Reason}
%% where
%% Lid 标签id
%% @doc 查询一个标签下的全部资产.
get_allAssetbylabeltreeNode(Lid, Index, Count, Sort, SortType)->
%%  读根节点
    Labelid = 
        case is_list(Lid) of
        true->  Lid;
        _->atom_to_list(Lid)
        end,
    case get_label_byId(Labelid) of
    []->[];
    {error,_}->[];
    Label->
        case proplists:get_value(isSyslabel,Label) =:="true" of
        true->      
            %%如果是系统标签，没有子标签，直接统计其下面的设备
            Templateid = proplists:get_value(templateid, Label),
            io:format("~n Templateid:~p",[Templateid]),
            case asset_template:get_template(Templateid) of
                {error,_} ->
                    {error, template_not_found};
                Temp ->
                    asset:get_offspring_by_template([proplists:get_value(alias, Temp)],0,0,"","")
            end;
        false->
            %%用户标签，循环递归查询结果
            Labels = getLabelLikeIndex(proplists:get_value(treeindex,Label)),
            Labelids = [proplists:get_value(id,L) || L<-Labels],
            get_assetByLabels(Labelids,Index, Count, Sort, SortType)
        end
    end.

%% @spec get_syslabel() -> Result | {error,Reason}
%% where
%% 
%% @doc 查询全部系统标签
get_syslabel()->
    Where =#query_condition_where{
        where=[{"my.isSyslabel","=",""++"true"++"","&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= 0,
                     count=0,
                     sort="",
                     sortType=""
    },
    Result = dbcs_asset_label:get_labelWhere(Query_Condition),
    NLables = [Label1||Label1<-Result,proplists:get_value(id,Label1)=/=?TEMPLATE_ROOT,proplists:get_value(id,Label1)=/=?LABEL_ROOT],
    R = set_CountForLabel(NLables),
    io:format("~n  R :~p",[R]),
    R.
    
%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc 查询用户标签的根节点
get_user_label_root()->
    Where =#query_condition_where{
        where=[{"my.isSyslabel","=",""++"false"++"","&"},
                        {"my.parentid","=",""++"root"++"","&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= 0,
                     count=0,
                     sort="",
                     sortType=""
    },
    dbcs_asset_label:get_labelWhere(Query_Condition).

%% @spec get_user_label() -> Result | {error,Reason}
%% where
%% 
%% @doc 查询全部用户标签
get_user_label()->
    Where =#query_condition_where{
        where=[{"my.isSyslabel","=",""++"false"++"","&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= 0,
                     count=0,
                     sort="",
                     sortType=""
    },
    Result = dbcs_asset_label:get_labelWhere(Query_Condition),
    NLables = [Label1||Label1<-Result,proplists:get_value(id,Label1)=/=?USER_ROOT,proplists:get_value(id,Label1)=/=?LABEL_ROOT],
    set_CountForLabel(NLables).

set_CountForLabel([])->[];
set_CountForLabel([Label|Next])->
    Lid = proplists:get_value(id,Label),
    Assets = get_allAssetbylabeltreeNode(Lid,0,0,"",""),
    Total = case length(Assets)>0 of 
                    true->
                       Asset = lists:nth(1,Assets),
                       proplists:get_value(total,Asset);
                    _-> 0
                end,
     IconUrl = case proplists:get_value(icon,Label) of
        undefined->[];
        IconName->
            case asset_icon:get_icon_by_alias(IconName) of
                {error,_}->IconName;
                Icon->
                   case proplists:get_value(url,Icon) of
                   undefined->[];
                   Url->Url
                   end
             end
    end,
    Label1 = lists:keyreplace(index,1,Label,{index,Total}),
    Label2 = lists:keyreplace(icon,1,Label1,{icon,IconUrl}),
    [Label2]++set_CountForLabel(Next).

%% @spec get_childrenNode(Labelid) -> Result | {error,Reason}
%% where
%% Labelid 标签id
%% @doc 查询用户标签的子节点
get_childrenNode(Labelid)->
    case get_label_byId(Labelid) of
    {error,_}->{error,"unkoow labelid"};
    Label->
         Where =#query_condition_where{
            where=[{"my.treeindex","like",""++proplists:get_value(treeindex,Label)++":"++"","&"}]
            },
        Query_Condition=#query_condition1{
                        where=Where,
                         index= 0,
                         count=0,
                         sort=0,
                         sortType=0
        },
        Result = dbcs_asset_label:get_labelWhere(Query_Condition),
        [Label1||Label1<-Result,string:str(proplists:get_value(treeindex,Label1),proplists:get_value(treeindex,Label))=:=1]
    end.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% 工具方法
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%给每个资产标记上关联的label
statlabelForAsset([])->[];
statlabelForAsset([CI|T]) ->
    Id = proplists:get_value(id,CI),%%CI#ci.id,
    case get_labelByasset(Id,0,0,"","") of
        {ok, Result} ->
            Val = [Lid||{Lid,_Aid}<-Result],
            Labels = textutils:listEveryToString(Val),
            [CI++[{label,Labels}]];
        _ ->
            [CI++[{label,[]}]]
    end++
    statlabelForAsset(T).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 
%% gen_server api
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%创建一个标签与资产的联系
handle_call({create_relation,{Labelid,Assetid}},_FROM,State)->
    case dbcs_asset_label_to_asset:create( [{id,asset_util:get_id(?LABELRELATION)},{labelid,Labelid},{assetid,Assetid}]) of
    {error,Err}->{reply,{error,Err},State};
    _->{reply,{ok,"create sucess"},State}
    end;
%%通过标签的id获得与其相关的资产的id
handle_call({get_assetBylabel,{Labelid,Index,Count,Sort,SortType}},_FROM,State)->
    Where =#query_condition_where{
        where=[{"my.labelid","=",""++Labelid++"","&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= Index,
                     count=Count,
                     sort=Sort,
                     sortType=SortType
    },
    case dbcs_asset_label_to_asset:get_Where(Query_Condition) of
    {error,Err}->{reply,{error,Err},State};
    Result->
        case length(Result)>0 of
        true->
            {reply,{ok,[Assetid||  [{id,_},{app_,_},{labelid,_},{assetid,Assetid}]<-Result]},State};
        _->
            {reply,{ok,[]},State}
        end    
    end;
%%通过资产的id获得与其相关的标签的id 
handle_call({get_labelByasset,{Assetid,Index,Count,Sort,SortType}},_FROM,State)->
    Where =#query_condition_where{
        where=[{"my.assetid","=",""++Assetid++"","&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= Index,
                     count=Count,
                     sort=Sort,
                     sortType=SortType
    },
    case dbcs_asset_label_to_asset:get_Where(Query_Condition) of
    {error,Err}->{reply,{error,Err},State};
    Result->
    case length(Result)>0 of
        true->
            {reply,{ok,[Labelid||  [{id,_},{app_,_},{labelid,Labelid},{assetid,_}]<-Result]},State};
        _->
            {reply,{ok,[]},State}
        end    
    end;
%%删除与一个标签相关联的某些资产的联系
handle_call({remove_labelByasset,{Labelid,Assetids}},_From,State)->
    Where =#query_condition_where{
        where=[{"my.labelid","=",""++Labelid++"","&"},
                        {"my.assetid","in",textutils:listToStr(Assetids),"&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= 0,
                     count=0,
                     sort="",
                     sortType=""
    },
    case dbcs_asset_label_to_asset:get_Where(Query_Condition) of
    {error,Err}->{reply,{error,Err},State};
    Result->
    case length(Result)>0 of
    true->
        Result2 = [dbcs_asset_label_to_asset:remove(ID)||  [{id,ID},{app_,_},{labelid,_},{assetid,_}]<-Result],
        case proplists:get_value(error,Result2) of
                undefined->{reply,{ok,"remove sucess"},State};
                Err->{reply,{error,Err},State}
       end;
   _->
        {reply,{error,"not found any label"},State}
   end
   end;
%%删除与一个资产相关的指定标签的联系
handle_call({remove_assetBylabel,{Assetid,Labelids}},_FROM,State)->
    Where =#query_condition_where{
        where=[{"my.assetid","=",""++Assetid++"","&"},
                        {"my.labelid","in",textutils:listToStr(Labelids),"&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= 0,
                     count=0,
                     sort="",
                     sortType=""
    },
    case dbcs_asset_label_to_asset:get_Where(Query_Condition) of
    {error,Err}->{reply,{error,Err},State};
    Result->
        case length(Result)>0 of
        true->
            Result2 = [dbcs_asset_label_to_asset:remove(ID)||  [{id,ID},{app_,_},{labelid,_},{assetid,_}]<-Result],
            case proplists:get_value(error,Result2) of
                    undefined->{reply,{ok,"remove sucess"},State};
                    Err->{reply,{error,Err},State}
           end;
        _->
            {reply,{error,"not found any asset"},State}
        end
   end;
%%删除与一个标签相关联的全部资产的联系
handle_call({remove_labelandassetall,{Labelid}},_FROM,State)->
   Where =#query_condition_where{
        where=[{"my.labelid","=",""++Labelid++"","&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= 0,
                     count=0,
                     sort="",
                     sortType=""
    },
    case dbcs_asset_label_to_asset:get_Where(Query_Condition) of
    {error,Err}->{reply,{error,Err},State};
    Result->
    case length(Result)>0 of
        true->
            Result2 = [dbcs_asset_label_to_asset:remove(ID)||  [{id,ID},{app_,_},{labelid,_},{assetid,_}]<-Result],
            case proplists:get_value(error,Result2) of
                    undefined->{reply,{ok,"remove sucess"},State};
                    Err->{reply,{error,Err},State}
           end;
        _->{reply,{error,"nothing can be delete"},State}
        end
   end;
%%删除与一个资产相关的全部标签的联系
handle_call({remove_assetandlabelall,{Assetid}},_FROM,State)->
    Where =#query_condition_where{
        where=[{"my.assetid","=",""++Assetid++"","&"}]
        },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= 0,
                     count=0,
                     sort="",
                     sortType=""
    },
    case dbcs_asset_label_to_asset:get_Where(Query_Condition) of
    {error,Err}->{reply,{error,Err},State};
    Result->
    case length(Result)>0 of
        true->
            Result2 = [dbcs_asset_label_to_asset:remove(ID)||  [{id,ID},{app_,_},{labelid,_},{assetid,_}]<-Result],
            case proplists:get_value(error,Result2) of
                    undefined->{reply,{ok,"remove sucess"},State};
                    Err->{reply,{error,Err},State}
           end;
        _->
            {reply,{error,"nothing can be delete"},State}
       end
   end;
handle_call(state, _, State)->
	{reply,{ok,State},State};
handle_call(_Req, _, State) ->
    {reply, {error,unknown_request}, State}.

handle_cast(stop, S) ->
    {stop, normal, S};

handle_cast(_, State) ->
    {noreply, State}.

handle_info(_, State) ->
    {noreply, State}.

code_change(_Vsn, State, _Extra) ->
    {ok, State}.

terminate(_Reason, _State) ->
    ok.
    