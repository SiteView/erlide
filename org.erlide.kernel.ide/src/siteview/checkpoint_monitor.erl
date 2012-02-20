-module (checkpoint_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> atomic_monitor .

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

checkpoint_monitor(Self, Name)->
	?SETVALUE(host,[]),
	?SETVALUE(counters,[{"1.3.6.1.4.1.2620.1.1.5",
                             "Rejected:1.3.6.1.4.1.2620.1.1.5"},
                            {"1.3.6.1.4.1.2620.1.1.6",
                             "Dropped:1.3.6.1.4.1.2620.1.1.6"},
                            {"1.3.6.1.4.1.2620.1.1.7",
                             "Logged:1.3.6.1.4.1.2620.1.1.7"},
                            {"1.3.6.1.4.1.2620.1.1.8",
                             "Major:1.3.6.1.4.1.2620.1.1.8"},
                            {"1.3.6.1.4.1.2620.1.1.9",
                             "Minor:1.3.6.1.4.1.2620.1.1.9"},
                            {"1.3.6.1.4.1.2620.1.1.10",
                             "Product:1.3.6.1.4.1.2620.1.1.10"},
                            {"1.3.6.1.4.1.2620.1.1.11",
                             "PointEvent:1.3.6.1.4.1.2620.1.1.11"},
                            {"1.3.6.1.4.1.2620.1.1.1",
                             "ModuleState:1.3.6.1.4.1.2620.1.1.1"}]),
	?SETVALUE(index,0),
	?SETVALUE(community,public),
	?SETVALUE(timeout,5),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

checkpoint_monitor_(Self)->eresye:stop(?VALUE(name)).

init(Self,EventType,Pattern,State) ->
	object:do(Self,waiting).

get_max() -> 10.  %max number of this type of monitor can be run in parallel

do_pong(Self,EventType,Pattern,State) ->
	object:do(Self,start).

update(Self,EventType,Pattern,State) ->
  	Start = erlang:now(),
	object:do(Self,running),


%% 	simulated random data
	?SETVALUE(countersInError,[]),
	?SETVALUE(status,[]),

	Diff = timer:now_diff(erlang:now(), Start)/1000000,
	?SETVALUE(?MEASUREMENTTIME,Diff),
	?SETVALUE(?LASTUPDATE,erlang:now()),


	resource_pool:release(?VALUE(name)), %%trigging the release_resource_pattern in resource_pool module
	object:super(Self, post_updating,[]),	
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
