-module (perfmon_monitorbase).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> atomic_monitor.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

perfmon_monitorbase(Self, Name)->

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

perfmon_monitorbase_(Self)->eresye:stop(?VALUE(name)).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
