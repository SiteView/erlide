-module (logMonitorBase).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> server_monitor.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

logMonitorBase(Self, Name)->
	?SETVALUE(machine,[]),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

logMonitorBase_(Self)->eresye:stop(?VALUE(name)).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
