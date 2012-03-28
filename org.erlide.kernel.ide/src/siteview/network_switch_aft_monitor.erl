-module (network_switch_aft_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").
-include_lib("../include/erlv8.hrl").
-include("nnm_define.hrl").

extends () -> atomic_monitor .

%%@doc the action, event and pattern are specificed in base_monitor
?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

%%@doc the constructor, specific the input parameters into the monitor
network_switch_aft_monitor(Self, Name)->
	?SETVALUE(ip,"localhost"),
	?SETVALUE(port,161),
	?SETVALUE(snmpVer,"v2"),
	?SETVALUE(getCommunity,"public"),
	?SETVALUE(timeout,5000),
	?SETVALUE(aft,[]),
	?SETVALUE(name,Name),	
	object:super(Self, [Name]).

network_switch_aft_monitor_(Self)->eresye:stop(?VALUE(name)).

%%@doc called as start up
init_action(Self,EventType,Pattern,State) ->
%% 	io:format ( "[~w]:Type=~w,Action=update\n",	[?VALUE(name),?MODULE]),
%% 	io:format ( "[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n",	[?VALUE(name),?MODULE,State,EventType,Pattern]),
	object:do(Self,waiting).

%%doc max number of this type of monitor can be run in parallel, need be set per the system resource
get_max() -> 20.  

%%@doc the type of resource consumpted from the system, e.g. mem, cpu, network, diskio etc.
get_resource_type() -> ?MODULE.


%% @doc run the monitor without logging and classifier, for run standalone and testing
run(SnmpParam) ->
	%% SpecialOidValueList = ets:lookup_element(?MODULE, specialOidList, 2),
	SpecialOidValueList = [],
	DtpFdbAddress = getTreeValue(SnmpParam,?DTPFDBADDRESS, proplists:get_value(dtpFdbAddress, SpecialOidValueList, "")),
	DtpFdbPort = getTreeValue(SnmpParam,?DTPFDBPORT, proplists:get_value(dtpFdbPort, SpecialOidValueList, "")),
	DtpFdbStatus = getTreeValue(SnmpParam,?DTPFDBSTATUS, proplists:get_value(dtpFdbStatus, SpecialOidValueList, "")),
	QtpFdbAddress = getTreeValue(SnmpParam,?QTPFDBADDRESS, proplists:get_value(qtpFdbAddress, SpecialOidValueList, "")),
	QtpFdbPort = getTreeValue(SnmpParam,?QTPFDBPORT, proplists:get_value(qtpFdbPort, SpecialOidValueList, "")),
	QtpFdbStatus = getTreeValue(SnmpParam,?QTPFDBSTATUS, proplists:get_value(qtpFdbStatus, SpecialOidValueList, "")),
	
	DtpFdb = connectAftTable(DtpFdbPort,DtpFdbAddress,DtpFdbStatus),
	QtpFdb = connectAftTable(QtpFdbPort,QtpFdbAddress,QtpFdbStatus),
	Result = nnm_discovery_util:mergerList(DtpFdb,QtpFdb),
	{proplists:get_value(ip,SnmpParam),Result}.

getTreeValue(SnmpParam,O1,O2) ->
	case nnm_snmp_api:readTreeValue(SnmpParam,O1) of
		[] ->
			case O2 of
				"" -> [];
				_ -> nnm_snmp_api:readTreeValue(SnmpParam,O2)
			end;
		V ->
			V
	end.

connectAftTable([],_,_) -> [];
connectAftTable([H|FdbPort],FdbAddress,FdbStatus) ->
	{Key,Value} = H,
	BackAddress = lists:sublist(Key, length(Key)-5, 6),
	Port = nnm_discovery_util:integerToString(Value),
	Address = nnm_discovery_util:toMac(proplists:get_value(Key, FdbAddress, BackAddress)),
	Status = nnm_discovery_util:integerToString(proplists:get_value(Key, FdbStatus, "")),
	
	[{Port,Address,Status}|connectAftTable(FdbPort,FdbAddress,FdbStatus)].


%%@doc the main update action to collect the data
update_action(Self,EventType,Pattern,State) ->
	{Session,_} = Pattern,  %%resource_allocated
	eresye:wait(?LOGNAME, {?VALUE(name),Session,'_',allocate_resource}),
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),

%% 	io:format ( "[~w:~w] ~w-2 Counter=~w,Action=update_action,State=~w,Event=~w,Pattern=~w\n",	[?MODULE,?LINE,?VALUE(name),resource_pool:get_counter(?VALUE(name)),State,EventType,Pattern]),
  	Start = erlang:now(),
	object:do(Self,running),
	
    SnmpParam = [{ip,?VALUE(ip)}, 
				{port,?VALUE(port)}, 
				{snmpVer,?VALUE(snmpversion)}, 
				{getCommunity,?VALUE(getCommunity)}, 
				{timeout,5000},
				{bindPort,161}],
	
	Aft = run(SnmpParam),
	?SETVALUE(aft,Aft),
	
%% 
%% 	%cycle: connecting -> connected -> retriving data -> data received -> processing -> done
	
	Diff = timer:now_diff(erlang:now(), Start)/1000000,
	?SETVALUE(?MEASUREMENTTIME,Diff),
	?SETVALUE(?LASTUPDATE,erlang:now()),	
%%  	io:format("[~w:~w] ~w finish in ~w s, Counter=~w,return: RoundTripTime:~w, PacketsGood:~w\n", [?MODULE,?LINE,?VALUE(name),Diff,resource_pool:get_counter(?VALUE(name)),?VALUE(round_trip_time),?VALUE(packetsgood)]),
%% TODO: run classifier	
%% 	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),

	set_classifier(Self),
%% 	io:format("~p Classifier: Error_classifier:~p, Warning_classifier:~p, Ok_classifier:~p, round_trip_time:~p, packetsgood:~p ~n", 
%% 			  [?VALUE(name), ?VALUE(error_classifier), ?VALUE(warning_classifier), ?VALUE(ok_classifier), ?QUEVALUE(round_trip_time),?QUEVALUE(packetsgood)]),

%% 	runClassifiersJs(Self),
	
%% 	resource_pool:release(?VALUE(name),Session), 	
 	io:format("[~w:~w] ~w ~w,Counter=~w,Queue=~w,update time=~w,wait_time=~w,return:RoundTripTime:~w,PacketsGood:~w\n", 
			  [?MODULE,?LINE,?VALUE(name),calendar:local_time(),
										resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name)),
			   							Diff,?VALUE(wait_time),?QUEVALUE(round_trip_time),?QUEVALUE(packetsgood)]),

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
				Name;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
