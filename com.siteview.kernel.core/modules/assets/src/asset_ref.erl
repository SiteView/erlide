-module(asset_ref).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").

-define(REFTYPE, ["DependsOn","ConnectedTo","InstalledOn","PointsTo","BelongsTo"]).
%reference, ["id","source","target","refType", "ref"]
%source是单一的，target是多个的

create_ref(Ref) ->
    NR = case proplists:get_value(id, Ref) of
        undefined ->
            Id = asset_util:get_id(?REF),
            [{id, Id}|Ref];
        _ ->
            Ref
    end,
    dbcs_asset_ref:create_ref(NR).
    
update_ref(Ref) ->
    Id = proplists:get_value(id, Ref),
    case get_ref(Id) of
        {ok, _OldRef} ->
            dbcs_asset_ref:update_ref(Ref);
        _ ->
            {error, ref_not_found}
    end.
    
remove_ref(Id) ->
    case get_ref(Id) of
        {ok, _} ->
            dbcs_asset_ref:remove_ref(Id);
        _ ->
            {error, ref_not_found}
    end.
    
get_ref(Id) ->
    dbcs_asset_ref:get_ref(Id).
    
get_all() ->
    dbcs_asset_ref:get_all().
    
remove_all() ->
    [dbcs_asset_ref:remove_ref(proplists:get_value(id, X)) || X<- get_all()].
    
get_ref_by_source(AssetId) when is_list(AssetId) ->
    dbcs_asset_ref:get_ref_match("my.source="++AssetId);
get_ref_by_source(_) ->
    {error, params_error}.
    
get_ref_by_source(AssetId, TempAlias, Index, Count, Sort, SortType) ->
    Where =#query_condition_where{
    where=[{"my.source","=","'"++AssetId++"'","&"},{"my.ref","=","'"++TempAlias++"'","&"}]
    },
    Query_Condition=#query_condition1{
            where=Where,
            index= Index,
            count=Count,
            sort=Sort,
            sortType=SortType
    },
    dbcs_asset_ref:get_refWhere(Query_Condition).
    
get_target_by_ref(RefId, Index, Count, Sort, SortType) ->
    case asset_ref:get_ref(RefId) of
        {error, _} ->
            {error, ref_not_found};
        Ref ->
            Assets = lists:flatten([proplists:get_value(target, Ref)]),
            asset:get_assets(Assets, Index, Count, Sort, SortType)
    end.