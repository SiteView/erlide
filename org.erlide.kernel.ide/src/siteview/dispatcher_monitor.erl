-module (dispatcher_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> browsable_base.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

dispatcher_monitor(Self, Name)->
	?SETVALUE(browse,[]),
	?SETVALUE(server,[]),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

dispatcher_monitor_(Self)->eresye:stop(?VALUE(name)).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
