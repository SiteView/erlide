-module(cmdb_query_handler).
-include("erl_cmdb.hrl").

-export([execute/1, execute_filter/1]).

execute(_GraphQuery) ->[].

execute_filter(#ci_filter{item_offspring_selector=Selectors}) when is_list(Selectors) ->
    [];
execute_filter(_) ->
    {error, error_params}.
    

