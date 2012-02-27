-module (ping_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> atomic_monitor .

%%@doc the action, event and pattern are specificed in base_monitor
?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

%%@doc the constructor, specific the input parameters into the monitor
ping_monitor(Self, Name)->
	?SETVALUE(timeout,1000),
	?SETVALUE(size,4),
	?SETVALUE(name,Name),	
	object:super(Self, [Name]).

ping_monitor_(Self)->eresye:stop(?VALUE(name)).

%%@doc called as start up
init_action(Self,EventType,Pattern,State) ->
%% 	io:format ( "[~w]:Type=~w,Action=update\n",	[?VALUE(name),?MODULE]),
%% 	io:format ( "[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n",	[?VALUE(name),?MODULE,State,EventType,Pattern]),
	object:do(Self,waiting).

%%doc max number of this type of monitor can be run in parallel, need be set per the system resource
get_max() -> 10.  

%%@doc the type of resource consumpted from the system, e.g. mem, cpu, network, diskio etc.
get_resource_type() -> ?MODULE.

%%@doc the main update action to collect the data
update_action(Self,EventType,Pattern,State) ->
	{Session,_} = Pattern,  %%resource_allocated
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),

%% 	io:format ( "[~w:~w] ~w-2 Counter=~w,Action=update_action,State=~w,Event=~w,Pattern=~w\n",	[?MODULE,?LINE,?VALUE(name),resource_pool:get_counter(?VALUE(name)),State,EventType,Pattern]),
  	Start = erlang:now(),
	object:do(Self,running),
	
%% 	io:format("[~w:~w] Running: Hostname:~w, Timeout:~w, Size:~w\n", [?MODULE,?LINE,?VALUE(name),?VALUE(timeout),?VALUE(size)]),
%% 	Cmd = "ping -n 4 -l " ++ object:get(Size,'value') ++ " -w " ++ object:get(Timeout,'value') ++ "  " ++ object:get(Hostname,'value'),

%% 	simulated random data

%% 	?SETVALUE(round_trip_time,100 * random:uniform(10)),
%% 	?SETVALUE(packetsgood,random:uniform(4)),
	?SETQUEVALUE(round_trip_time,100 * random:uniform(10)),
	?SETQUEVALUE(packetsgood,random:uniform(4)),
%% 	timer:sleep(random:uniform(10)*1000),
	timer:sleep(3*1000),
	
	Diff = timer:now_diff(erlang:now(), Start)/1000000,
	?SETVALUE(?MEASUREMENTTIME,Diff),
	?SETVALUE(?LASTUPDATE,erlang:now()),	
%%  	io:format("[~w:~w] ~w finish in ~w s, Counter=~w,return: RoundTripTime:~w, PacketsGood:~w\n", [?MODULE,?LINE,?VALUE(name),Diff,resource_pool:get_counter(?VALUE(name)),?VALUE(round_trip_time),?VALUE(packetsgood)]),
%% 	object:super(Self, post_run,[]),
%% TODO: run classifier	
%% 	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),
 	io:format("[~w:~w] ~w Counter=~w,Queue=~w,update time=~w,wait_time=~w,return:RoundTripTime:~w,PacketsGood:~w\n", 
			  [?MODULE,?LINE,?VALUE(name),resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name)),Diff,?VALUE(wait_time),?QUEVALUE(round_trip_time),?QUEVALUE(packetsgood)]),
%% 	resource_pool:release(?VALUE(name),Session), 	
	eresye:assert(?VALUE(name), {Session,logging}),
%% 	object:do(Self,waiting).
	object:do(Self,logging).


%%@doc startup, the name must be unique
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

