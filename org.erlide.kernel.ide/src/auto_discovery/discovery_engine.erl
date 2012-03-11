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

?PATTERN(newnode_pattern) -> {discover_rule_engine, get, {newnode,'_','_','_','_'}}. %% node,port, discovered_by, timestamp

?EVENT(newnode_event)-> {eresye,newnode_pattern}.

?ACTION(start) -> {newnode_event,newnode_action}.

start()->
	Discover = object:new(?MODULE),
	object:start(Discover),
	eresye:start(discover_rule_engine),
	Discover.

discovery_engine(Self)->
	?SETVALUE(name,discover_object),
	eresye:start(?VALUE(name)).

discovery_engine_(Self)->eresye:stop(?VALUE(name)).

%%@doc whenever a new node added, discover the node type and then create it and hand over the further discover processing to the object 
newnode_action(Self,EventType,Pattern,State) -> 
	{newnode,Node,Port,By,Timestamp} = Pattern,
	AccessMethods = accessmethod_monitor:run(Node) ,  %snmp,nmap,telnet,ssh,wmi,jdbc,tr069,packet,manual_input
	%%create one object, and assign these DiscoverTypes to this object
	NodeType = discover_type(AccessMethods,Node),
	ok.

%%@doc discover the node type with the Access Methods, in a sequence, if discoverred, then exit
%% rules: is  
discover_type(AccessMethods,Node) ->
	ok.
%% SNMPPing: if yes, get sysoid, if not using nmap
%% id the type:sysoid and nmap
%% sysoid: if known create the model, if not try nmap: if yes create the generic model, if not create the generic model and put in for-manual-input
%% nmap: certain: create the model
%% 		possibility: prompt for-manual-input
%% create and start the typed object

