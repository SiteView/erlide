%% @doc  the discovery engine to discover everything in the data center
%% 	0. obtaining the initial IP address list: seed or an IP range
%%  1. find the active nodes: ICMP, SNMPPing, NMap, each discovery method is a 'monitor'
%%  2. detect the type of the node and create the typed object: sysoid, nmap identification result
%%  3. processing each type which will generate more nodes to be discovered 
%% 
%%  related monitors: 
%% 		Active nodes discovery: fastping, SNMPPing, 
%% 		NMap: NMap host, NMap ports, 
%% 		SNMP:
%% [SWITCH]
%% 	AftInfo
%% 	InterfaceInfo
%% [ROUTE]
%% 	InterfaceInfo
%% 	RouteInfo
%% 	ArpInfo
%% 	OspfInfo
%% 	BgpInfo
%% 	DirectInfo
%% [ROUTE_SWITCH]
%% 	InterfaceInfo
%% 	RouteInfo
%% 	ArpInfo
%% 	OspfInfo
%% 	BgpInfo
%% 	DirectInfo
%% 	AftInfo
%% [gpon]
%% 	InterfaceInfo
%% 	GponOltInfo
%% [FIREWALL]
%% 	InterfaceInfo
%% 	RouteInfo
%% 	ArpInfo
%% 	OspfInfo
%% 	BgpInfo
%% 	DirectInfo
%% 
%% %% 
%% 
%% 
%% 

-module (discovery_engine).
-compile ([export_all]).

-include("object.hrl").

extends () -> nil .

?PATTERN(finished_pattern) -> {?LOGNAME, read, {'_','_','_',log}}. %% name, session, timestamp, state

?EVENT(finished_event)-> {eresye,finished_pattern}.

?ACTION(start) -> {finished_event,finished_action}.

start()->
	Discover = object:new(?MODULE),
	object:start(Discover),
	eresye:start(discover_rule_engine),
	Discover.

discovery_engine(Self)->
	?SETVALUE(name,discover_object),
	eresye:start(?VALUE(name)).

discovery_engine_(Self)->eresye:stop(?VALUE(name)).

