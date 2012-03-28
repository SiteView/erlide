-module (cisco_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> browsable_snmp_base.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

cisco_monitor(Self, Name)->
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

cisco_monitor_(Self)->eresye:stop(?VALUE(name)).

init_action(Self,EventType,Pattern,State) ->
	object:do(Self,waiting).

get_max() -> 10.  %max number of this type of monitor can be run in parallel
get_resource_type() -> ?MODULE.  %the type of resource consumpted from the system, e.g. mem, cpu, network, diskio etc.

do_pong(Self,EventType,Pattern,State) ->
	object:do(Self,start).

update_action(Self,EventType,Pattern,State) ->
	{Session,_} = Pattern,  %%resource_allocated  
	eresye:wait(?LOGNAME, {?VALUE(name),Session,'_',allocate_resource}), 
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),  
   	Start = erlang:now(),
	 object:do(Self,running),


%% 	simulated random data
	?SETVALUE(countersInError,[]),

	Diff = timer:now_diff(erlang:now(), Start)/1000000,
	?SETVALUE(?MEASUREMENTTIME,Diff),
	?SETVALUE(?LASTUPDATE,erlang:now()),

 	io:format("---------------------module: ~p update_action :  ~p ~n", [?MODULE, ?VALUE(name)]),

	resource_pool:release(?VALUE(name)), %%trigging the release_resource_pattern in resource_pool module
	eresye:assert(?VALUE(name), {Session,logging}),	
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
