-module (ping_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").
-include_lib("../include/erlv8.hrl"). 

extends () -> atomic_monitor .

%%@doc the action, event and pattern are specificed in base_monitor
?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

%%@doc the constructor, specific the input parameters into the monitor
ping_monitor(Self, Name)->
	?SETVALUE(hostname,"localhost"),
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
	
    {Osfamily, Osname} = os:type(),

	
%% 	io:format("[~w:~w] Running: Hostname:~w, Timeout:~w, Size:~w\n", [?MODULE,?LINE,?VALUE(name),?VALUE(timeout),?VALUE(size)]),
%% 	Cmd = "ping -n 4 -l " ++ ?VALUE(size) ++ " -w " ++ ?VALUE(timeout) ++ "  " ++ ?VALUE(hostname),
%% 
%% 	%cycle: connecting -> connected -> retriving data -> data received -> processing -> done
%% 	case Osfamily of 
%% 		win32 -> 
%% 			    Cmd = "ping -n 4 -l " ++ integer_to_list(?VALUE(size)) ++ " -w " ++ ?VALUE(timeout) ++ "  " ++ ?VALUE(hostname),
%%                 Data = os:cmd(Cmd);
%% 		%@TODO: using erlv8 to processing the Data, using the string function in javascript, which is much more user friendly
%%         unix ->
%% 			    Cmd = "ping -c 4 -s " ++ integer_to_list(?VALUE(size))  ++ "  "  ++ ?VALUE(hostname),
%%                 Data =  os:cmd(Cmd)
%% 		%@TODO: using erlv8 to processing the Data
%%         end,
	
%% 	simulated random data

%% 	?SETVALUE(round_trip_time,100 * random:uniform(10)),
%% 	?SETVALUE(packetsgood,random:uniform(4)),
	?SETQUEVALUE(round_trip_time,10 * random:uniform(10)),
>>>>>>> b3d1e5a5b102281cba637efbf7b554d9d7a79732

	?SETQUEVALUE(round_trip_time,10 * random:uniform(10)),
	?SETQUEVALUE(packetsgood,random:uniform(4)),
	timer:sleep(1*1000),
	
	Diff = timer:now_diff(erlang:now(), Start)/1000000,
	?SETVALUE(?MEASUREMENTTIME,Diff),
	?SETVALUE(?LASTUPDATE,erlang:now()),	
%%  	io:format("[~w:~w] ~w finish in ~w s, Counter=~w,return: RoundTripTime:~w, PacketsGood:~w\n", [?MODULE,?LINE,?VALUE(name),Diff,resource_pool:get_counter(?VALUE(name)),?VALUE(round_trip_time),?VALUE(packetsgood)]),
%% TODO: run classifier	
%% 	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),
 	io:format("[~w:~w] ~w Counter=~w,Queue=~w,update time=~w,wait_time=~w,return:RoundTripTime:~w,PacketsGood:~w\n", 
			  [?MODULE,?LINE,?VALUE(name),resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name)),Diff,?VALUE(wait_time),?QUEVALUE(round_trip_time),?QUEVALUE(packetsgood)]),

	set_classifier(Self),
	io:format("~p Classifier: Error_classifier:~p, Warning_classifier:~p, Ok_classifier:~p, round_trip_time:~p, packetsgood:~p ~n", 
			  [?VALUE(name), ?VALUE(error_classifier), ?VALUE(warning_classifier), ?VALUE(ok_classifier), ?QUEVALUE(round_trip_time),?QUEVALUE(packetsgood)]),
	runClassifiersJs(Self),
	
%% 	resource_pool:release(?VALUE(name),Session), 	

	eresye:assert(?VALUE(name), {Session,logging}),
	object:do(Self,logging).

set_classifier(Self) ->
	Error_classifier = "Value('round_trip_time') > 80 || Value('packetsgood') < 3",
	Warning_classifier = "Value('round_trip_time') < 80 && Value('packetsgood') > 30",
	Ok_classifier = "Value('packetsgood') > 1",
%% 	io:format("---------------set_classifier------:~p~n", [[{error_classifier, Error_classifier},
%% 	{warning_classifier, Warning_classifier}, {ok_classifier, Ok_classifier}]]),
	set_classifier(Self, [{error_classifier, Error_classifier},
	{warning_classifier, Warning_classifier}, {ok_classifier, Ok_classifier}]).

set_classifier(Self, []) ->
	ok;
set_classifier(Self, [{Type,Classifier}|T]) -> 
	?SETVALUE(Type,Classifier),
	set_classifier(Self, T).

runClassifiersJs(Self)->
	{ok, VM} = erlv8_vm:start(),
	Global = erlv8_vm:global(VM),

%% 	Global:set_value("Value", 
%% 		 fun (#erlv8_fun_invocation{}, [String]) -> io:format("----~p~n", [String]), ?QUEVALUE(erlang:list_to_atom(erlang:binary_to_list(String))) end),

	Global:set_value("Value", 
		 fun (#erlv8_fun_invocation{}, [String]) -> ?QUEVALUE(erlang:list_to_atom(erlang:binary_to_list(String))) end),

	{ok,Error} = erlv8_vm:run(VM,?VALUE(error_classifier)),
	{ok,Warning} = erlv8_vm:run(VM,?VALUE(warning_classifier)),
	{ok,Ok} = erlv8_vm:run(VM,?VALUE(ok_classifier)),
	
	io:format("---------------~p runClassifiers result:  Ok: ~p , Warning ~p,  Error: ~p~n", [?VALUE(name), Ok, Warning, Error]),
	if Error -> ?SETVALUE(?CATEGORY,error);
%% 	   Major -> ?SETVALUE(?CATEGORY,major);
	   Warning -> ?SETVALUE(?CATEGORY,warning);
%% 	   Minor -> ?SETVALUE(?CATEGORY,minor);
	   Ok -> ?SETVALUE(?CATEGORY,ok);
	   true -> un_classified
	end,
 	erlv8_vm:stop(VM).

%%@doc startup, the name must be unique
start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),				
				resource_pool:register(object:getClass(Name)),
				eresye:assert(Name,{wakeup}),
%% 				object:call(X, set_classifier, []),
				Name;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
