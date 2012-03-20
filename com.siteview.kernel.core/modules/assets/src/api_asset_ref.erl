-module(api_asset_ref).
-compile(export_all).
-include("asset.hrl").

create(Ref) ->
    asset_ref:create_ref(Ref).
    
update(Ref) ->
    asset_ref:update_ref(Ref).
    
delete(Ref) ->
    asset_ref:remove_ref(Ref).
    
get_all() ->
    asset_ref:get_all().
    
get_ref(Id) ->
    asset_ref:get_ref(Id).
    
get_ref_by_source(AssetId) ->
    asset_ref:get_ref_by_source(AssetId).
    
get_target_by_ref(RefId, Index, Count, Sort, SortType) ->
    asset_ref:get_target_by_ref(RefId, Index, Count, Sort, SortType).