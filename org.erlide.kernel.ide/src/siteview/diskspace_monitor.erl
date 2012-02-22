-module (diskspace_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> atomic_monitor .

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

diskspace_monitor(Self, Name)->
	?SETVALUE(machine,[]),
	?SETVALUE(disk,[]),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

diskspace_monitor_(Self)->eresye:stop(?VALUE(name)).

get_resource_type() -> ?MODULE .

init_action(Self,EventType,Pattern,State) ->
	object:do(Self,waiting).

get_max() -> 10.  %max number of this type of monitor can be run in parallel

%% do_pong(Self,EventType,Pattern,State) ->
%% 	object:do(Self,start).

update_action(Self,EventType,Pattern,State) ->
	{Session,_} = Pattern,  %%resource_allocated
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),object:stateof(Self),resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name))}),
  	Start = erlang:now(),
	object:do(Self,running),


%% 	simulated random data
	?SETVALUE(percentFull,[]),
	?SETVALUE(freeSpace,[]),

	timer:sleep(5*1000),
	Diff = timer:now_diff(erlang:now(), Start)/1000000,
	
	?SETVALUE(?MEASUREMENTTIME,Diff),
	?SETVALUE(?LASTUPDATE,erlang:now()),
	
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),object:stateof(Self),resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name))}),	
	io:format("diskspace_monitor:~p~n", ["ddd"]),
	resource_pool:release(?VALUE(name),Session), 	
	eresye:assert(?VALUE(name), {Session,logging}),
%% 	resource_pool:release(?VALUE(name)), %%trigging the release_resource_pattern in resource_pool module
%% 	object:super(Self, post_updating,[]),	
	object:do(Self,logging).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				resource_pool:register(object:getClass(Name)),
				eresye:assert(Name,{wakeup}),
				Name;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
