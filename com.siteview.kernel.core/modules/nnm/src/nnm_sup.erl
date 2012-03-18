%% ---
%%
%%
%%---
-module(nnm_sup).
-behaviour(supervisor).

-export([start_link/1,init/1,start/0]).

start() ->
	spawn(fun() ->
		supervisor:start_link({local,?MODULE}, ?MODULE, _Arg = [])
	end).

start_link(Args) ->
    supervisor:start_link({local,?MODULE},?MODULE, Args).

init(_Args) ->
    {ok, {{one_for_one, 1, 60},
          [
			{nnm_poller_loader, {nnm_poller_loader, start_link, []},transient, brutal_kill, worker, [nnm_poller_loader]}
		  ]}}.

