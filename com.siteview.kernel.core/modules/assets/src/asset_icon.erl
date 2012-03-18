-module(asset_icon).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").
%ci["id", "alias", "url", "size", "user", "total"]
%Í¼±ê¹ÜÀí

create_icon(Icon) ->
    Alias = proplists:get_value(alias, Icon),
    case asset_icon:get_icon_by_alias(Alias) of
        {error, _} ->
            NR = case proplists:get_value(id, Icon) of
                undefined ->
                    Id = asset_util:get_id(?IMG),
                    [{id, Id}|Icon];
                _ ->
                    Icon
            end,
            dbcs_asset_icon:create_icon(NR);
        _ ->
            {error, alias_existed}
    end.
    
update_icon(Icon) ->
    Id = proplists:get_value(id, Icon),
    case get_icon(Id) of
        {ok, _OldIcon} ->
            dbcs_asset_icon:update_icon(Icon);
        _ ->
            {error, icon_not_found}
    end.
    
remove_icon(Id) ->
    case get_icon(Id) of
        {error,_} ->
            {error, icon_not_found};
        _ ->
            dbcs_asset_icon:remove_icon(Id)
    end.
    
get_icon(Id) ->
    dbcs_asset_icon:get_icon(Id).
    
get_all() ->
    dbcs_asset_icon:get_all().
    
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
    dbcs_asset_icon:get_iconWhere(Query_Condition).
    
remove_all() ->
    [dbcs_asset_icon:remove_icon(proplists:get_value(id, X)) || X<- get_all()].
    
get_icon_by_alias("") ->{error, icon_not_found};
get_icon_by_alias(Alias) when is_list(Alias) ->
    case dbcs_asset_icon:get_icon_match("my.alias="++Alias) of
        [] ->
            {error, icon_not_found};
        Icon ->
            hd(Icon)
    end;
get_icon_by_alias(_) ->{error, params_error}.

