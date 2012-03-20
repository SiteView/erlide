-module(app).
-compile(export_all).

-include("dbcs_common.hrl").
-include("config.hrl").

all() ->
    case rpc:call(?DBName,ets,tab2list,[application]) of
          Apps when is_list(Apps)  ->
               F = fun(X) -> X#application.id end,
               lists:map(F,Apps);
          _  -> []    
    end.
    