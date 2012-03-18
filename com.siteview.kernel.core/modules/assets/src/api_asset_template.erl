%% @author Cheng kaiyang <kaiyang.cheng@dragonflow.com>
%% @copyright 2010 dragonflow, Inc.
%% @version 1.0
%% @doc API Asset Template.
%% 
%% Description: Defined functions with asset template
-module(api_asset_template).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").

-export([create/1, update/1, delete/1, get_template/1, get_template_by_alias/1, get_asset_template/1, get_child/1, get_offspring/1, get_parent/1, get_template_by_asset/1, get_all_templates/4, get_root_templates/4, import_template/1, import_all/0, get_attributes/1]).

%% @spec create(Template) -> [{ok, Template}| {error, Reason}]
%% where
%% Asset = list()
%% Reason = atom()
%% @doc create asset template, Template should include: alias, derived, attrs at least.
create(Template) when is_list(Template) ->
    Alias = proplists:get_value(alias, Template),
    case asset_template:get_template_by_alias(Alias) of
        {error, _} ->
            LogResult = asset_template:create_template(Template),
            % create label
            case LogResult of
                {error, _} ->
                    nothing;
                {ok, Temp} ->
                    asset_label_manage:create_sysLabel_L(Temp)
            end,
            asset_log:log(?CREATE_TEMPLATE, Alias, LogResult),
            LogResult;
        _ ->
            {error, alias_existed}
    end;
create(_) ->
    {error, params_error}.
    
%% @spec update(Template) -> [{ok, Template}| {error, Reason}]
%% where
%% Asset = list()
%% Reason = atom()
%% @doc update asset template, Template should include: id, alias, derived, attrs at least.
update(Template) when is_list(Template) ->
    Id = proplists:get_value(id, Template),
    if
        Id==undefined ->
            {error, id_not_found};
        true ->
            case asset_template:get_template(Id) of
                {error, _} ->
                    {error, template_not_found};
                _ ->
                    LogResult = asset_template:update_template(Template),
                    asset_log:log(?UPDATE_TEMPLATE, Id, LogResult),
                    asset_label_manage:update_sysLabel(Template),
                    % 是否同步现有继承于此模板的实例
                    sync_ci(Template),
                    LogResult
            end
    end;
update(_) ->
    {error, params_error}.
    
%% @spec delete(Id) -> [{ok, finish}| {error, Reason}]
%% where
%% Id = string()
%% Reason = atom()
%% @doc delete current template, his cis, his children template and them cis.
delete(Id) ->
    asset_label_manage:remove_labeltreeNode(["temp-"++Id]),
    %找到最下层的template,清除继承它的所有ci和template自身,在寻找上面的
    LogResult = case asset_template:get_template(Id) of
        {error,_} ->
            {error, template_not_found};
        Temp ->
            delete_each([Temp])
    end,
    asset_log:log(?DELETE_TEMPLATE, Id, LogResult),
    LogResult.
    
delete_each([]) ->{ok, finish};
delete_each([F|R]) ->
    Id = proplists:get_value(id, F),
    Alias = proplists:get_value(alias, F),
    Childs = case asset_template:get_child_template(Id) of
        {error, _} ->
            [];
        Other ->
            Other
    end,
    if
        length(Childs)==0 ->
            asset:remove_asset_by_template(Alias),
            asset_template:remove_template(Id),
            delete_each(R);
        true ->
            delete_each(Childs++[F]++R)
    end.
    
%% @spec get_template(Id) -> [Template | {error, Reason}]
%% where
%% Id = string()
%% Template = list()
%% Reason = atom()
%% @doc get template by id.
get_template(Id) ->
    asset_template:get_template(Id).
    
%% @spec get_template_by_alias(Alias) -> [Template | {error, Reason}]
%% where
%% Alias = string()
%% Template = list()
%% Reason = atom()
%% @doc get template by alias.
get_template_by_alias(Alias) ->
    asset_template:get_template_by_alias(Alias).
    
%% @spec get_asset_template(Id) -> [Template | {error, Reason}]
%% where
%% Id = string()
%% Template = list()
%% Reason = atom()
%% @doc get template which attributes include all attributes derived from parent.
get_asset_template(Id) ->
    asset_template:get_asset_template(Id).
    
%% @spec get_child(Id) -> [Templates | {error, Reason}]
%% where
%% Id = string()
%% Templates = [Template()]
%% Reason = atom()
%% @doc get templates which derived from current template.
get_child(Id) ->
    asset_template:get_child_template(Id).
    
%% @spec get_offspring(Id) -> [Templates | {error, Reason}]
%% where
%% Id = string()
%% Templates = [Template()]
%% Reason = atom()
%% @doc get all templates which derived from current template or his children template.
get_offspring(Id) ->
    asset_template:get_all_children(Id).
    
%% @spec get_parent(Id) -> [Template | {error, Reason}]
%% where
%% Id = string()
%% Template = list()
%% Reason = atom()
%% @doc get parent template.
get_parent(Id) ->
    asset_template:get_parent_template(Id).
    
%% @spec get_template_by_asset(Id) -> [Template | {error, Reason}]
%% where
%% Id = string()
%% Template = list()
%% Reason = atom()
%% @doc get template by asset.
get_template_by_asset(Id) ->
    case asset:get_asset(Id) of
        {error, _} ->
            {error, asset_not_found};
        Asset ->
            asset_template:get_template_by_alias(proplists:get_value(derived, Asset))
    end.
    
%% @spec get_all_templates(Index, Count, Sort, SortType) -> [Templates | {error, Reason}]
%% where
%% Index = integer()
%% Count = Integer()
%% Sort = string()
%% SortType = string()
%% Templates = [Template()]
%% Reason = atom()
%% @doc get all templates.
get_all_templates(Index, Count, Sort, SortType) ->
    asset_template:get_templates(Index, Count, Sort, SortType).
    
%% @spec get_root_templates(Index, Count, Sort, SortType) -> [Templates | {error, Reason}]
%% where
%% Index = integer()
%% Count = Integer()
%% Sort = string()
%% SortType = string()
%% Templates = [Template()]
%% Reason = atom()
%% @doc get all root templates.
get_root_templates(Index, Count, Sort, SortType) ->
    asset_template:get_root_templates(Index, Count, Sort, SortType).
    
%% @spec import_template(Alias) -> [{ok, Template} | {error, Reason}]
%% where
%% Alias = string()
%% Template = list()
%% Reason = atom()
%% @doc import current template defined in templates.asset/default_templates.conf.
import_template(Alias) ->
    Result = asset_default_template:import_template(Alias),
    case Result of
        {ok, _} ->
            asset_label_manage:create_sysLabel();
        _ ->
            nothing
    end,
    Result.

%% @spec import_all() -> [{ok, finish} | {error, Reason}]
%% where
%% Reason = atom()
%% @doc import all template defined in templates.asset/default_templates.conf.
import_all() ->
    asset_default_template:import_all(),
    asset_label_manage:create_sysLabel().
    
%% @spec refresh_default_template() -> [{ok, finish} | {error, Reason}]
%% where
%% Reason = atom()
%% @doc refresh data with templates.asset/default_templates.conf.
refresh_default_template() ->
    asset_default_template:reload().
    
%% @spec get_default_template_info(Alias) -> Templates
%% where
%% Alias = list()
%% Templates = list()
%% @doc get default template include.
get_default_template_info(Alias) ->
    asset_default_template:get_info(Alias).
    
%% @spec get_imported_template() -> Templates
%% where
%% Templates = [template()]
%% @doc get imported templates.
get_imported_template() ->
    asset_default_template:get_imported_templates().
    
%% @spec get_default_template() -> Templates
%% where
%% Templates = list()
%% @doc get default templates.
get_default_template() ->
    asset_default_template:get_default_templates().
    
%% @spec get_attributes(Id) -> [Attribute | {error, Reason}]
%% where
%% Id = list()
%% Attribute = list()
%% Reason = atom()
%% @doc get current template's attributes, not include himself attrs but also his parents'.
get_attributes(Id) ->
    asset_template:get_template_attribute(Id).
    
sync_ci(_temp) ->ok.