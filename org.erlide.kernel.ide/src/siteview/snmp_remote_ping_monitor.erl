-module (snmp_remote_ping_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends() -> atomic_monitor.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

snmp_remote_ping_monitor(Self, Name) ->
	?SETVALUE(oid,"1.3.6.1.2.1.1.1"),
	?SETVALUE(host,"192.168.0.248"),
	?SETVALUE(community,"dragon"),
	?SETVALUE(timeout,5),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

snmp_remote_ping_monitor_(Self)->eresye:stop(?VALUE(name)).

init_action(Self,EventType,Pattern,State) ->
	io:format("----init-----~p~n"),
	object:do(Self,waiting).

get_resource_type() -> ?MODULE.

update_action(Self,EventType,Pattern,State) ->
  	{Session,_} = Pattern,
	io:format("---update00-----~p~n"),
	io:format("-------------------------aqc----~p~n"),
	Host = ?VALUE(host),
	Community = ?VALUE(community),
	Timeout = ?VALUE(timeout),
	Session = snmp_session:new(Host, 161, "V2", Community, "", "", "", "", "", "", Timeout*1000),
	Session:test_snmp(),
	
	eresye:assert(?VALUE(name), {Session,logging}),
	object:do(Self,logging).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),				
				%%resource_pool:register(object:getClass(Name)),
				eresye:assert(Name,{wakeup}),
				Name;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
