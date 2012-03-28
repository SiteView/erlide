-module (browsable_urlcontent_base).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> browsable_base.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

browsable_urlcontent_base(Self, Name)->
	?SETVALUE(browse,[]),
	?SETVALUE(proxyUserName,[]),
	?SETVALUE(proxyPassword,[]),
	?SETVALUE(timeout,60),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

browsable_urlcontent_base_(Self)->eresye:stop(?VALUE(name)).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
