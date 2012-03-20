%% ---
%%
%%
%%---
-module(asset_sup).
-behaviour(supervisor).

-export([start_link/1,init/1,start/0]).

start() ->
	spawn(fun() ->
		supervisor:start_link({local,?MODULE}, ?MODULE, _Arg = [])
	end).

start_link(Args) ->
    supervisor:start_link({local,?MODULE},?MODULE, Args).

%Ë³Ğò²»Òª¸Ä
init(_Args) ->
    {ok, {{one_for_one, 1, 60},
          [
            {server_conf, {server_conf,start_link,[]},transient, brutal_kill, worker, [server_conf]},
            {asset_default_template,{asset_default_template,start_link,[]},transient, brutal_kill, worker, [asset_default_template]},
            {asset_label_manage,{asset_label_manage,start_link,[]},transient, brutal_kill, worker, [asset_label_manage]},
            {asset_log,{asset_log,start_link,[]},transient, brutal_kill, worker, [asset_log]}
		  ]}}.