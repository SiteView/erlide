-module (db_monitor_base).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> atomic_monitor.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

db_monitor_base(Self, Name)->
	?SETVALUE(database,[]),
	?SETVALUE(query_property,[]),
	?SETVALUE(db_user,[]),
	?SETVALUE(db_password,[]),
	?SETVALUE(driver,[]),
	?SETVALUE(connect_timeout,60),
	?SETVALUE(content_match,[]),
	?SETVALUE(query_file_path,[]),
	?SETVALUE(query_timeout,60),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

db_monitor_base_(Self)->eresye:stop(?VALUE(name)).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
