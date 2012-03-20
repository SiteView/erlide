%% @author Cheng kaiyang <kaiyang.cheng@dragonflow.com>
%% @copyright 2010 dragonflow, Inc.
%% @version 1.0
%% @doc API Asset.
%% 
%% Description: Defined functions with asset
-module(api_asset).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").

-export([create/1, update/1, delete/1, get_asset/1,remove_assets/1 ,get_assets/5, get_all_assets/0, get_all/4, get_asset_by_template/5, get_all_ids/0, get_offspring_by_template/5]).

%% @spec create(Asset) -> [Asset | {error, Reason}]
%% where
%% Asset = list()
%% Reason = atom()
%% @doc create asset.
create(Asset) when is_list(Asset) ->
    Attrs = proplists:get_value(attrs, Asset),
    Drived = proplists:get_value(derived, Asset),
    case asset_template:get_template_by_alias(Drived) of
        {error, _} ->
            {error, template_not_found};
        Template ->
            TempId = proplists:get_value(id, Template),
            case asset_template:get_template_attribute(TempId) of
                {error, _} ->
                    {error, attribute_not_found};
                {Attributes, _, _} ->
                    io:format("attrs:~p~nattributes:~p~n", [Attrs, Attributes]),
                    NewAttrs = parse_attribute(Attrs, Attributes, []),
                    LogResult = asset:create_asset(lists:keyreplace(attrs, 1, Asset, {attrs, NewAttrs})),
                    asset_log:log(?CREATE_ASSET, proplists:get_value(id, Asset), LogResult),
                    LogResult;
                _Other ->
                    {error, error_params}
            end
    end;
create(_) ->
    {error, params_error}.
    
%% @spec update(Asset) -> [Asset | {error, Reason}]
%% where
%% Asset = list()
%% Reason = atom()
%% @doc update asset.
update(Asset) when is_list(Asset) ->
    Id = proplists:get_value(id, Asset),
    if
        Id==undefined ->
            {error, asset_not_found};
        true ->
            Attrs = proplists:get_value(attrs, Asset),
            Drived = proplists:get_value(derived, Asset),
            case asset_template:get_template_by_alias(Drived) of
                {error, _} ->
                    {error, template_not_found};
                Template ->
                    TempId = proplists:get_value(id, Template),
                    case asset_template:get_template_attribute(TempId) of
                        {error, _} ->
                            {error, attribute_not_found};
                        {Attributes, _, _} ->
                            NewAttrs = parse_attribute(Attrs, Attributes, []),
                            LogResult = asset:update_asset(lists:keyreplace(attrs, 1, Asset, {attrs, NewAttrs})),
                            asset_log:log(?UPDATE_ASSET, Id, LogResult),
                            LogResult;
                        _Other ->
                            {error, error_params}
                    end
            end
    end;
update(_) ->
    {error, params_error}.
    
%% @spec delete(Id) -> [{ok, finish} | {error, Reason}]
%% where
%% Id = list()
%% Reason = atom()
%% @doc delete asset by id.
delete(Id) when is_list(Id)->
    LogResult = asset:remove_asset(Id),
    asset_log:log(?DELETE_ASSET, Id, LogResult),
    LogResult;
delete(_) ->
    {error, id_not_list}.
    
%% @spec remove_assets(Id) -> [{ok, "remove sucess"} | {error, Reason}]
%% where
%% Id = list()
%% Reason = atom()
%% @doc delete asset by some ids.
remove_assets(Ids) when is_list(Ids)-> 
    LogResult = asset:remove_assets(Ids),
    asset_log:log(?DELETE_ASSET, Ids, LogResult),
    LogResult;
remove_assets(_)->{error,"argument format error"}.

%% @spec delete_asset_by_template(TempAlias) -> [{ok, finish} | {error, Reason}]
%% where
%% TempAlias = list()
%% Reason = atom()
%% @doc delete assets by his template.
delete_asset_by_template(TempAlias) ->
    asset:remove_asset_by_template(TempAlias).
    
%% @spec get_asset(Id) -> [Asset | {error, Reason}]
%% where
%% Id = string()
%% Asset = list()
%% Reason = atom()
%% @doc get asset by id.
get_asset(Id) ->
    asset:get_asset(Id).
    
%% @spec get_assets(Ids, Index, Count, Sort, SortType) -> [Assets | {error, Reason}]
%% where
%% Index = integer()
%% Count = Integer()
%% Sort = string()
%% SortType = string()
%% Ids = string()
%% Assets = [Asset()]
%% Reason = atom()
%% @doc get assets by ids.
get_assets(Ids, Index, Count, Sort, SortType) ->
    asset:get_assets(Ids, Index, Count, Sort, SortType).
    
%% @spec get_assets(Ids, Index, Count, Sort, SortType) -> [Assets | {error, Reason}]
%% where
%% Assets = [Asset()]
%% Reason = atom()
%% @doc get all asset.
get_all_assets() ->
    asset:get_all_assets().
    
%% @spec get_all(Index, Count, Sort, SortType) -> [Assets | {error, Reason}]
%% where
%% Index = integer()
%% Count = Integer()
%% Sort = string()
%% SortType = string()
%% Assets = [Asset()]
%% Reason = atom()
%% @doc get assets.
get_all(Index, Count, Sort, SortType) ->
    asset:get_all(Index, Count, Sort, SortType).
    
%% @spec get_asset_by_template(Id, Index, Count, Sort, SortType) -> [Assets | {error, Reason}]
%% where
%% Id = string()
%% Index = integer()
%% Count = Integer()
%% Sort = string()
%% SortType = string()
%% Assets = [Asset()]
%% Reason = atom()
%% @doc get assets by template.
get_asset_by_template(TempAlias, Index, Count, Sort, SortType) ->
    asset:get_asset_by_template(TempAlias, Index, Count, Sort, SortType).
    
%% @spec get_asset_by_template(Id, Index, Count, Sort, SortType) -> [Assets | {error, Reason}]
%% where
%% Id = string()
%% Index = integer()
%% Count = Integer()
%% Sort = string()
%% SortType = string()
%% Assets = [Asset()]
%% Reason = atom()
%% @doc get assets by template.
get_offspring_by_template(TempAlias, Index, Count, Sort, SortType) ->
    asset:get_offspring_by_template(TempAlias, Index, Count, Sort, SortType).
    
%% @spec get_all_ids() -> [Ids | {error, Reason}]
%% where
%% Ids = list()
%% Reason = atom()
%% @doc get all assets ids.
get_all_ids() ->
    L = [proplists:get_value(id, X)||X<- asset:get_all_assets()],
    lists:filter(fun(Y)->if Y==undefined -> false;true ->true end end, L).
    
get_asset_by_label(Label, Index, Count, Sort, SortType) ->
    asset_label_manage:get_assetByLabel(Label,Index, Count, Sort, SortType).

get_source_asset(RefId) ->
    Assets = lists:flatten([V||{_T,V}<- asset_ref:get_source(RefId)]),
    asset:get_assets(Assets, 0, 1000, "", "").
    
get_target_asset(RefId) ->
    Assets = lists:flatten([V||{_T,V}<- asset_ref:get_target(RefId)]),
    asset:get_assets(Assets, 0, 1000, "", "").

get_log_by_asset(Id) ->
    case asset_log:get_log() of
        {ok, Logs} ->
            [X||X<- Logs, proplists:get_value(target, X)==Id];
        _ ->
            []
    end.

%% @spec import_scan(IpAddress, User) -> {ok, finish}
%% where
%% IpAddress = string()
%% User = integer()
%% @doc import auto discovery to cmdb.
import_scan(IpAddress, User) ->
    asset_discovery:import_scan_result(IpAddress, User).

%在对资产做出调整时会自动匹配模板信息
parse_attribute(_, [], Result) ->Result;
parse_attribute(Attrs, [F|R], Result) ->
    Id = proplists:get_value(id, F),
    NR = case proplists:get_value(Id, Attrs) of
        undefined ->
            [{Id, get_default_value(F)}|Result];
        V ->
            [{Id, get_value(F, V)}|Result]
    end,
    parse_attribute(Attrs, R, NR).

get_value(Attribute, Value) ->
    Type = proplists:get_value(type, Attribute),
    if
        Type=="numeric" ->
            asset_util:to_numeric(Value);
        ((Type=="password") or (Type=="text") or (Type=="textArea")) ->
            asset_util:to_string(Value);
        Type=="bool" ->
            asset_util:to_bool(Value);
        true ->
            Value
    end.
    
    
get_default_value(Attribute) ->
    Default = proplists:get_value(default, Attribute),
    if
        Default/=undefined ->
            Default;
        true ->
            Type = proplists:get_value(type, Attribute),
            if
                Type=="numeric" ->
                    0;
                ((Type=="password") or (Type=="text") or (Type=="textArea")) ->
                    "";
                Type=="ci_reference" ->
                    undefined;
                Type=="bool" ->
                    false;
                true ->
                    ""
            end
    end.
    

