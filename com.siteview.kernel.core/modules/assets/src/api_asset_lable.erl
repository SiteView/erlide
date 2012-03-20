-module(api_asset_lable).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").

-export([create_labeltreeNode/2,update_labeltreeNode/1,remove_labeltreeNode/1,get_user_label_root/0,get_user_sys_root/0,get_label_children/1
,set_asset2label/2,remove_assetFromLabel/2,create_sysLabel/2,get_assetByLabel/5,get_labelByasset/5,get_label_byId/1,get_user_label/0,get_sys_label/0]).

%% @spec create_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label=list()
%% @doc ������ǩ���ϵ�һ���ڵ�,Label ��ǩʵ��
%%
create_labeltreeNode(Parentid,Label)->
    asset_label_manage:create_labeltreeNode(Parentid,Label).

%% @spec update_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Label=list()
%% @doc ���±�ǩ���Ľڵ�,Label ��ǩʵ��
%%
update_labeltreeNode(Label)->
    asset_label_manage:update_labeltreeNode([Label]).
    
%% @spec update_labeltreeNode(Label) -> {ok,Result} | {error,Reason}
%% where
%% Labelids=string()
%% @doc ɾ�����ڵ㣬ͬʱɾ���ӽڵ�
%%
remove_labeltreeNode(Labelid)->
    asset_label_manage:remove_labeltreeNode([Labelid]).

%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc ��ѯ�û���ǩ�ĸ��ڵ�
get_user_label_root()->
    asset_label_manage:get_user_label_root().

%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc ��ѯϵͳ��ǩ�ĸ��ڵ�
get_user_sys_root()->
    asset_label_manage:get_syslabel().

%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc ��ѯ��ǩ�ӽڵ�
get_label_children(Lid)->
    asset_label_manage:get_childrenNode(Lid).

%% @spec set_asset2label(Labelid,AssetIds) -> {ok,Result} | {error,Reason}
%% where
%% Labelid=string()
%% AssetIds = list()
%% @doc ������ʲ���һ����ǩ��,Labelid ��ǩid ,  AssetIds �ʲ�id �б�
set_asset2label(Labelid,AssetIds)->
    asset_label_manage:set_asset2label(Labelid,AssetIds).

%% @spec remove_assetFromLabel(Labelid,Assetids) -> {ok,"remove sucess"} | {error,Reason}
%% where
%% Labelid=string()
%% Assetids = list()
%% @doc ������ʲ���һ����ǩ��ɾ��,Labelid ��ǩid ,  AssetIds �ʲ�id �б�
%%
remove_assetFromLabel(Labelid,Assetids)->
    asset_label_manage:remove_assetFromLabel(Labelid,Assetids).

%% @spec get_assetByLabel(Labelid) -> Result | {error,Reason}
%% where
%% Labelid = list()
%% @doc ��ñ�ǩ��Ӧ���ʲ�,Labelid ��ǩid
%%
get_assetByLabel(Labelid,Index,Count,Sort,SortType)->
    asset_label_manage:get_allAssetbylabeltreeNode(Labelid,Index,Count,Sort,SortType).

%% @spec get_labelByAsset(Assetid) -> Result | {error,Reason}
%% where
%% Assetid �ʲ�id
%% @doc ���һ���ʲ���Ӧ�ı�ǩ
%%
get_labelByasset(Assetid,Index, Count, Sort, SortType)->
    asset_label_manage:get_labelByasset(Assetid,Index, Count, Sort, SortType).

%% @spec create_sysLabel_L([Template])
%% where
%% Template = tuple()
%% @doc ���һ��ϵͳ��ǩ,Parentid���ױ�ǩ��id,Template={Id,Template}
%%
create_sysLabel(Template,Parentid)->
    asset_label_manage:create_sysLabel([Template],Parentid).

%% @spec get_label_byId(ID) -> Result | {error,Reason}
%% where
%% ID = string()
%% @doc ͨ��ID��ñ�ǩ��ʵ��,ID ��ǩid
%%
get_label_byId(ID)->
    asset_label_manage:get_label_byId(ID).

%% @spec get_user_label_root() -> Result | {error,Reason}
%% where
%% 
%% @doc ��ѯȫ���û���ǩ
get_user_label()->
    asset_label_manage:get_user_label(). 

%% @spec asset_label_manage() -> Result | {error,Reason}
%% where
%% 
%% @doc ��ѯȫ��ϵͳ��ǩ
get_sys_label()->
    asset_label_manage:get_syslabel(). 