-module (network_switch_if_monitor).
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
network_switch_if_monitor(Self, Name)->
	?SETVALUE(ip,"localhost"),
	?SETVALUE(port,161),
	?SETVALUE(snmpVer,"v2"),
	?SETVALUE(getCommunity,"public"),
	?SETVALUE(timeout,5000),
	?SETVALUE(name,Name),	
	object:super(Self, [Name]).

network_switch_if_monitor_(Self)->eresye:stop(?VALUE(name)).

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
%% 	SpecialOidValueList = getSpecialOid(SysobjectId,[ifIndex,ifDescr,ifType,ifMtu,ifMac,ifSpeed,ifAdminStatus,ifOperStatus,ifLastChange,
%% 													 ifInOctets,ifInUcastPkts,ifInNUcastPkts,ifInDiscards,ifInErrors,ifInUnknownProtos,
%% 													 ifOutOctets,ifOutUcastPkts,ifOutNUcastPkts,ifOutDiscards,ifOutErrors,ifOutQLen,
%% 													 ifAlia]),
	SpecialOidValueList = [],
	IfIndex = getTreeValue(SnmpParam,?IFINDEX, proplists:get_value(ifIndex, SpecialOidValueList, "")),
	IfDescr = getTreeValue(SnmpParam,?IFDESCR, proplists:get_value(ifDescr, SpecialOidValueList, "")),
	IfType = getTreeValue(SnmpParam,?IFTYPE, proplists:get_value(ifType, SpecialOidValueList, "")),
	IfMtu = getTreeValue(SnmpParam,?IFMTU, proplists:get_value(ifMtu, SpecialOidValueList, "")),
	IfSpeed = getTreeValue(SnmpParam,?IFSPEED,proplists:get_value(ifMac, SpecialOidValueList, "")),
	IfMac = getTreeValue(SnmpParam,?IFMAC,proplists:get_value(ifSpeed, SpecialOidValueList, "")),
	IfAdminStatus = getTreeValue(SnmpParam,?IFADMINSTATUS,proplists:get_value(ifAdminStatus, SpecialOidValueList, "")),
	IfOperStatus = getTreeValue(SnmpParam,?IFOPERSTATUS,proplists:get_value(ifOperStatus, SpecialOidValueList, "")),
	IfLastChange = getTreeValue(SnmpParam,?IFLASTCHANGE,proplists:get_value(ifLastChange, SpecialOidValueList, "")),
	IfInOctets = getTreeValue(SnmpParam,?IFINOCTETS,proplists:get_value(ifInOctets, SpecialOidValueList, "")),
	IfInUcastPkts = getTreeValue(SnmpParam,?IFINUCASTPKTS,proplists:get_value(ifInUcastPkts, SpecialOidValueList, "")),
	IfInNUcastPkts = getTreeValue(SnmpParam,?IFINNUCASTPKTS,proplists:get_value(ifInNUcastPkts, SpecialOidValueList, "")),
	IfInDiscards = getTreeValue(SnmpParam,?IFINDISCARDS,proplists:get_value(ifInDiscards, SpecialOidValueList, "")),
	IfInErrors = getTreeValue(SnmpParam,?IFINERRORS,proplists:get_value(ifInErrors, SpecialOidValueList, "")),
	IfInUnknownProtos = getTreeValue(SnmpParam,?IFINUNKNOWNPROTOS,proplists:get_value(ifInUnknownProtos, SpecialOidValueList, "")),
	IfOutOctets = getTreeValue(SnmpParam,?IFOUTOCTETS,proplists:get_value(ifOutOctets, SpecialOidValueList, "")),
	IfOutUcastPkts = getTreeValue(SnmpParam,?IFOUTUCASTPKTS,proplists:get_value(ifOutUcastPkts, SpecialOidValueList, "")),
	IfOutNUcastPkts = getTreeValue(SnmpParam,?IFOUTNUCASTPKTS,proplists:get_value(ifOutNUcastPkts, SpecialOidValueList, "")),
	IfOutDiscards = getTreeValue(SnmpParam,?IFOUTDISCARDS,proplists:get_value(ifOutDiscards, SpecialOidValueList, "")),
	IfOutErrors = getTreeValue(SnmpParam,?IFOUTERRORS,proplists:get_value(ifOutErrors, SpecialOidValueList, "")),
	IfOutQLen = getTreeValue(SnmpParam,?IFOUTQLEN,proplists:get_value(ifOutQLen, SpecialOidValueList, "")),
	IfAlias = getTreeValue(SnmpParam,?IFALIAS,proplists:get_value(ifAlias, SpecialOidValueList, "")),
			
	Result = connectInterfaceTable(IfIndex,IfDescr,IfType,IfMtu,IfSpeed,IfMac,IfAdminStatus,IfOperStatus,IfLastChange,IfInOctets,IfInUcastPkts,
						  	IfInNUcastPkts,IfInDiscards,IfInErrors,IfInUnknownProtos,IfOutOctets,IfOutUcastPkts,IfOutNUcastPkts,
							IfOutDiscards,IfOutErrors,IfOutQLen,IfAlias),
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

connectInterfaceTable([],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_) -> [];
connectInterfaceTable([H|IfIndex],IfDescr,IfType,IfMtu,IfSpeed,IfMac,IfAdminStatus,IfOperStatus,IfLastChange,IfInOctets,IfInUcastPkts,
					 	IfInNUcastPkts,IfInDiscards,IfInErrors,IfInUnknownProtos,IfOutOctets,IfOutUcastPkts,IfOutNUcastPkts,
						IfOutDiscards,IfOutErrors,IfOutQLen,IfAlias) ->
	{Key,Value} = H,
	Index = nnm_discovery_util:integerToString(Value),
	Descr = proplists:get_value(Key, IfDescr, ""),
	Type = nnm_discovery_util:integerToString(proplists:get_value(Key, IfType, "")),
	Mtu = proplists:get_value(Key, IfMtu, ""),
	Speed = nnm_discovery_util:integerToString(proplists:get_value(Key, IfSpeed, "")),
	Mac = nnm_discovery_util:toMac(proplists:get_value(Key, IfMac, "")),
	AdminStatus = nnm_discovery_util:integerToString(proplists:get_value(Key, IfAdminStatus, "")),
	OperStatus = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOperStatus, "")),
	LastChange = nnm_discovery_util:integerToString(proplists:get_value(Key, IfLastChange, "")),
	InOctets = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInOctets, "")),
	InUcastPkts = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInUcastPkts, "")),
	InNUcastPkts = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInNUcastPkts, "")),
	InDiscards = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInDiscards, "")),
	InErrors = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInErrors, "")),
	InUnknownProtos = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInUnknownProtos, "")),
	OutOctets = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutOctets, "")),
	OutUcastPkts = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutUcastPkts, "")),
	OutNUcastPkts = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutNUcastPkts, "")),
	OutDiscards = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutDiscards, "")),
	OutErrors = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutErrors, "")),
	OutQLen = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutQLen, "")),
	Alias = proplists:get_value(Key, IfAlias, ""),
	
	[{Index,Descr,Type,Mtu,Speed,Mac,AdminStatus,OperStatus,LastChange,InOctets,InUcastPkts,InNUcastPkts,InDiscards,InErrors,InUnknownProtos,
	  OutOctets,OutUcastPkts,OutNUcastPkts,OutDiscards,OutErrors,OutQLen,Alias}|
		 connectInterfaceTable(IfIndex,IfDescr,IfType,IfMtu,IfSpeed,IfMac,IfAdminStatus,IfOperStatus,IfLastChange,IfInOctets,IfInUcastPkts,
					 	IfInNUcastPkts,IfInDiscards,IfInErrors,IfInUnknownProtos,IfOutOctets,IfOutUcastPkts,IfOutNUcastPkts,
						IfOutDiscards,IfOutErrors,IfOutQLen,IfAlias)].
	
%%@doc the main update action to collect the data
update_action(Self,EventType,Pattern,State) ->
	{Session,_} = Pattern,  %%resource_allocated
	eresye:wait(?LOGNAME, {?VALUE(name),Session,'_',allocate_resource}),
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),

  	Start = erlang:now(),
	object:do(Self,running),

     SnmpParam = [{ip,?VALUE(ip)}, 
				{port,?VALUE(port)}, 
				{snmpVer,?VALUE(snmpversion)}, 
				{getCommunity,?VALUE(getCommunity)}, 
				{timeout,5000},
				{bindPort,161}],
	
	InterfaceTB = run(SnmpParam),
	?SETVALUE(interfaceTB,InterfaceTB),
	
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
