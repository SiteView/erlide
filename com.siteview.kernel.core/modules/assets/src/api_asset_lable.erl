-module(api_asset_lable).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").

-export([create_labeltreeNode/2,update_labeltreeNode/1,remove_labeltreeNode/1,get_user_label_root/0,get_user_sys_root/0,get_label_children/1
,set_asset2label/2,remove_assetFromLabel/2,create_sysLabel/2,get_assetByLabel/5,get_labelByasset/5,get_label_byId/1,get_user_label/0,get_sys_label/0]).

%% @spec create_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label=list()
%% @doc 创建标签树上的一个节点,Label 标签实体
%%
create_labeltreeNode(Parentid,Label)->
    asset_label_manage:create_labeltreeNode(Parentid,Label).

%% @spec update_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label=list()
%% @doc 更新标签树的节点,Label 标签实体
%%
update_labeltreeNode(Label)->
    asset_label_manage:update_labeltreeNode([Label]).
    
%% @spec update_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Labelids=string()
%% @doc 删除树节点，同时删除子节点
%%
remove_labeltreeNode(Labelid)->
    asset_label_manage:remove_labeltreeNode([Labelid]).

%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc 查询用户标签的根节点
get_user_label_root()->
    asset_label_manage:get_user_label_root().

%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc 查询系统标签的根节点
get_user_sys_root()->
    asset_label_manage:get_syslabel().

%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc 查询标签子节点
get_label_children(Lid)->
    asset_label_manage:get_childrenNode(Lid).

%% @spec set_asset2label(Labelid,AssetIds) -> {ok,Result} | {error,Reason}
%% where
%% Labelid=string()
%% AssetIds = list()
%% @doc 将多个资产打到一个标签组,Labelid 标签id ,  AssetIds 资产id 列表
set_asset2label(Labelid,AssetIds)->
    asset_label_manage:set_asset2label(Labelid,AssetIds).

%% @spec remove_assetFromLabel(Labelid,Assetids) -> {ok,"remove sucess"} | {error,Reason}
%% where
%% Labelid=string()
%% Assetids = list()
%% @doc 将多个资产从一个标签中删除,Labelid 标签id ,  AssetIds 资产id 列表
%%
remove_assetFromLabel(Labelid,Assetids)->
    asset_label_manage:remove_assetFromLabel(Labelid,Assetids).

%% @spec get_assetByLabel(Labelid) -> Result | {error,Reason}
%% where
%% Labelid = list()
%% @doc 获得标签对应的资产,Labelid 标签id
%%
get_assetByLabel(Labelid,Index,Count,Sort,SortType)->
    asset_label_manage:get_allAssetbylabeltreeNode(Labelid,Index,Count,Sort,SortType).

%% @spec get_labelByAsset(Assetid) -> Result | {error,Reason}
%% where
%% Assetid 资产id
%% @doc 获得一个资产对应的标签
%%
get_labelByasset(Assetid,Index, Count, Sort, SortType)->
    asset_label_manage:get_labelByasset(Assetid,Index, Count, Sort, SortType).

%% @spec create_sysLabel_L([Template])
%% where
%% Template = tuple()
%% @doc 添加一个系统标签,Parentid父亲标签的id,Template={Id,Template}
%%
create_sysLabel(Template,Parentid)->
    asset_label_manage:create_sysLabel([Template],Parentid).

%% @spec get_label_byId(ID) -> Result | {error,Reason}
%% where
%% ID = string()
%% @doc 通过ID获得标签的实体,ID 标签id
%%
get_label_byId(ID)->
    asset_label_manage:get_label_byId(ID).

%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc 查询全部用户标签
get_user_label()->
    asset_label_manage:get_user_label(). 

%% @spec asset_label_manage() -> Result | {error,Reason}
%% where
%% 
%% @doc 查询全部系统标签
get_sys_label()->
    asset_label_manage:get_syslabel(). 