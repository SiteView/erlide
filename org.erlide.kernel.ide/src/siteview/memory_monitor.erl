-module (memory_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> server_monitor .

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

memory_monitor(Self, Name)->
	?SETVALUE(machine,[]),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

memory_monitor_(Self)->eresye:stop(?VALUE(name)).

init_action(Self,EventType,Pattern,State) ->
	object:do(Self,waiting).

get_max() -> 10.  %max number of this type of monitor can be run in parallel

%%@doc the type of resource consumpted from the system, e.g. mem, cpu, network, diskio etc.
get_resource_type() -> ?MODULE.

do_pong(Self,EventType,Pattern,State) ->
	object:do(Self,start).

run(Host,LastPageFaults,LastMeasurement)->
	{Status,ResultList} = platform:getMemoryFull(Host,LastPageFaults,LastMeasurement),
	io:format("__________platform:getMemoryFull____________:~p~n",[{Status,ResultList}]),
	{Status,ResultList} .
	
update_action(Self,EventType,Pattern,State) ->
	
	{Session,_} = Pattern,  %%resource_allocated
	eresye:wait(?LOGNAME, {?VALUE(name),Session,'_',allocate_resource}),
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),  	
	Start = erlang:now(),
	object:do(Self,running),

%% 	simulated random data
	?SETVALUE(pPercentFull,[]),
	?SETVALUE(pFreeSpace,[]),
	?SETVALUE(pPageFaultsPerSecond,[]),	
%% 	run(?VALUE(machine), 0,0),
	
	Diff = timer:now_diff(erlang:now(), Start)/1000000,
	?SETVALUE(?MEASUREMENTTIME,Diff),
	?SETVALUE(?LASTUPDATE,erlang:now()),

	io:format("---------------------module: ~p update_action : ~p ~n", [?MODULE, ?VALUE(name)]),	   
	resource_pool:release(?VALUE(name)), %%trigging the release_resource_pattern in resource_pool module
	eresye:assert(?VALUE(name), {Session,logging}),
	object:do(Self,logging).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
