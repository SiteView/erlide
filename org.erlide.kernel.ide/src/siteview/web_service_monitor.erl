-module (web_service_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> atomic_monitor .

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

web_service_monitor(Self, Name)->
	?SETVALUE(wsdlurl,[]),
	?SETVALUE(methodname,[]),
	?SETVALUE(serverurl,[]),
	?SETVALUE(argnames,[]),
	?SETVALUE(matchstring,[]),
	?SETVALUE(contenttype,[]),
	?SETVALUE(useragent,[]),
	?SETVALUE(usedotnetsoap,[]),
	?SETVALUE(schema,[]),
	?SETVALUE(methodns,[]),
	?SETVALUE(actionuri,[]),
	?SETVALUE(ntlmdomain,[]),
	?SETVALUE(username,[]),
	?SETVALUE(password,[]),
	?SETVALUE(proxyuser,[]),
	?SETVALUE(proxypassword,[]),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

web_service_monitor_(Self)->eresye:stop(?VALUE(name)).

init(Self,EventType,Pattern,State) ->
	object:do(Self,waiting).

get_max() -> 10.  %max number of this type of monitor can be run in parallel

do_pong(Self,EventType,Pattern,State) ->
	object:do(Self,start).

update(Self,EventType,Pattern,State) ->
  	Start = erlang:now(),
	object:do(Self,running),


%% 	simulated random data
	?SETVALUE(matchvalue,[]),
	?SETVALUE(status,[]),
	?SETVALUE(roundtriptime,[]),

	Diff = timer:now_diff(erlang:now(), Start)/1000000,
	?SETVALUE(?MEASUREMENTTIME,Diff),
	?SETVALUE(?LASTUPDATE,erlang:now()),


	resource_pool:release(?VALUE(name)), %%trigging the release_resource_pattern in resource_pool module
	object:super(Self, post_updating,[]),	
	object:do(Self,logging).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " already existed, choose a new name"
	end.
