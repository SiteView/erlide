-module(api_asset_icon).
-compile(export_all).
-include("asset.hrl").
-define(ICON_DIR,"templates.asset/default_icons.conf").

create(Icon) ->
    asset_icon:create_icon(Icon).
    
update(Icon) ->
    asset_icon:update_icon(Icon).
    
delete(Icon) ->
    asset_icon:remove_icon(Icon).
    
get_all() ->
    asset_icon:get_all().
    
get_all(Index, Count, Sort, SortType) ->
    asset_icon:get_all(Index, Count, Sort, SortType).
    
import_all() ->
    Icons = case file:consult(?ICON_DIR) of
		{ok,Data}->
			Data;
		_->
			[]
	end,
    if
        length(Icons)==0 ->
            {error, default_icons_not_found};
        true ->
            [asset_icon:create_icon([{alias, K}, {url, V}, {user, "all"}])||{K, V}<- Icons],
            {ok, finish}
    end.
    
get_icon(Id) ->
    asset_icon:get_icon(Id).
    
get_icon_by_alias(Alias) ->
    asset_icon:get_icon_by_alias(Alias).