-module(asset).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").
%ci["id", "class", "app_", "derived", "display", "created", "modified", "description", "icon", "attrs","total"]
%asset的继承关系用模板alias标示

create_asset(Asset) ->
    Derived = proplists:get_value(derived, Asset),
    case asset_template:get_asset_template_by_alias(Derived) of
        {error, _} ->
            {error, template_not_found};
        Template ->
            Id = case proplists:get_value(id, Asset) of
                undefined ->
                    asset_util:get_id(?ASSET);
                OId ->
                    OId
            end,
            NewAsset = [{id, Id}, {derived, Derived}, {display, asset_util:get_display(Asset, Template)}, 
                {created, asset_util:get_time()}, {modified, asset_util:get_time()}, {description, proplists:get_value(description, Asset, "")}, 
                {icon, proplists:get_value(icon, Asset, "")}, {attrs, proplists:get_value(attrs, Asset, [])}],
            dbcs_asset:create_asset(NewAsset)
    end.

update_asset(Asset) ->
    Id = proplists:get_value(id, Asset),
    case get_asset(Id) of
        {error, _} ->
            {error, asset_not_found};
        OldAsset ->
            Derived = proplists:get_value(derived, Asset),
            case asset_template:get_template_by_alias(Derived) of
                {error, _} ->
                    {error, template_not_found};
                Template ->
                    OldDisplay = proplists:get_value(display, OldAsset, ""),
                    NewDisplay = proplists:get_value(display, Asset, ""),
                    NewDis = if
                        OldDisplay==NewDisplay ->
                            asset_util:get_display(Asset, Template);
                        true ->
                            NewDisplay
                    end,
                    NewAsset = [{id, Id}, {derived, Derived}, {display, NewDis}, 
                        {created, proplists:get_value(created, Asset, "")}, {modified, asset_util:get_time()}, {description, proplists:get_value(description, Asset, "")}, 
                        {icon, proplists:get_value(icon, Asset, "")}, {attrs, proplists:get_value(attrs, Asset, [])}],
                    dbcs_asset:update_asset(NewAsset)
            end
    end.
    
remove_asset(Id) ->
   case dbcs_asset:remove_asset(Id) of
   {ok,_}->asset_label_manage:remove_asset_relation(Id);
   Err->Err
   end.

remove_assets(Ids) ->
    Result = [dbcs_asset:remove_asset(Id) || Id<- Ids],
    case proplists:get_value(error,Result) of
    undefined->
        Result = [asset_label_manage:remove_asset_relation(Id) || Id<- Ids],
        case proplists:get_value(error,Result) of
            undefined->{ok,"remove sucess"};
            _->{error,"remove fail"}
        end;
    _->{error,"remove fail"}
    end.

%删除当前模板下的资产
remove_asset_by_template(Alias) ->
    case get_asset_by_template(Alias) of
        {error, Reason} ->
            {error, Reason};
        Assets ->
            [remove_asset(proplists:get_value(id, X))||X<- Assets],
            {ok, finish}
    end.
    
remove_all_assets() ->
    dbcs_asset:remove_all().
    
get_asset(Id) ->
    dbcs_asset:get_asset(Id).
    
get_assets(Ids, Index, Count, Sort, SortType) ->
    Where =#query_condition_where{
    where=[{"id","in",textutils:listToStr(Ids),"&"}]
    },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= Index,
                     count=Count,
                     sort=Sort,
                     sortType=SortType
    },
    dbcs_asset:get_assetWhere(Query_Condition).

%取得当前模板下的所有资产
get_asset_by_template(Alias) ->
    get_asset_by_template(Alias, 0, 10000, "", "").

get_asset_by_template(Alias, Index, Count, Sort, SortType) ->
    case asset_template:get_template_by_alias(Alias) of
        {error, _} ->
            {error, template_not_found};
        _ ->
            Where =#query_condition_where{
            where=[{"my.derived","=","'"++Alias++"'","&"}]
            },
            Query_Condition=#query_condition1{
                    where=Where,
                    index= Index,
                    count=Count,
                    sort=Sort,
                    sortType=SortType
            },
            dbcs_asset:get_assetWhere(Query_Condition)
    end.
    
get_offspring_by_template(Alias, Index, Count, Sort, SortType) ->
    case asset_template:get_template_by_alias(Alias) of
        {error, _} ->
            {error, template_not_found};
        Current ->
            Childs = asset_template:get_all_children(proplists:get_value(id, Current)),
            Temps = [proplists:get_value(alias,X)||X<- [Current|Childs]],
            Where =#query_condition_where{
            where=[{"my.derived","in",textutils:listToStr(Temps),"&"}]
            },
            Query_Condition=#query_condition1{
                    where=Where,
                    index= Index,
                    count=Count,
                    sort=Sort,
                    sortType=SortType
            },
            dbcs_asset:get_assetWhere(Query_Condition)
    end.
    
get_all_assets() ->
    dbcs_asset:get_all().
    
get_all(Index, Count, Sort, SortType) ->
    WhereCondition=
        #query_condition_where{
                where=[]
            },
    Query_Condition=
        #query_condition1{
            where=WhereCondition,
            index=Index,
            count=Count,
            sort=Sort,
            sortType=SortType
        },
    dbcs_asset:get_assetWhere(Query_Condition).

clear() ->
    application:start(asset),
    asset_template:remove_all_templates(),
    asset:remove_all_assets().
    
test() ->
    application:start(asset),
    asset_machine:import_all().
    