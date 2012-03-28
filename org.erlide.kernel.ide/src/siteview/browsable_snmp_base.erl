-module (browsable_snmp_base).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> browsable_base.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

browsable_snmp_base(Self, Name)->
	?SETVALUE(browse,[]),
	?SETVALUE(server,[]),
	?SETVALUE(mibfile,[]),
	?SETVALUE(snmpversion,[]),
	?SETVALUE(community,public),
	?SETVALUE(snmpv3authtype,[]),
	?SETVALUE(snmpv3username,[]),
	?SETVALUE(snmpv3authpassword,[]),
	?SETVALUE(snmpv3privpassword,[]),
	?SETVALUE(contextEngineID,[]),
	?SETVALUE(contextName,[]),
	?SETVALUE(timeout,5),
	?SETVALUE(port,161),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

browsable_snmp_base_(Self)->eresye:stop(?VALUE(name)).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
