-module(nnm_start_sup).
-behaviour(supervisor).

-export([start_link/1,init/1,start/0]).

start() ->
	spawn(fun() ->
		supervisor:start_link({global,?MODULE}, ?MODULE, _Arg = [])
	end).
                                            
start_link(Args) ->
    supervisor:start_link({global,?MODULE},?MODULE, Args).

init([]) ->
    {ok, {{one_for_one, 1, 60},
          [
			{nnm_monitor, {nnm_monitor, start_link, []},permanent, 10000, worker, [nnm_monitor]}
		  ]}}.