-module (weblogic5x_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> snmp_base.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

weblogic5x_monitor(Self, Name)->
	?SETVALUE(host,[]),
	?SETVALUE(index,0),
	?SETVALUE(community,public),
	?SETVALUE(counters,[{"1.3.6.1.4.1.140.600.20.1.20",
                             "ServerState:1.3.6.1.4.1.140.600.20.1.20"},
                            {"1.3.6.1.4.1.140.600.20.1.25",
                             "LoginEnabled:1.3.6.1.4.1.140.600.20.1.25"},
                            {"1.3.6.1.4.1.140.600.20.1.60",
                             "MaxHEapSpace:1.3.6.1.4.1.140.600.20.1.60"},
                            {"1.3.6.1.4.1.140.600.20.1.65",
                             "HeapUsedPct:1.3.6.1.4.1.140.600.20.1.65"},
                            {"1.3.6.1.4.1.140.600.20.1.70",
                             "QueueLength:1.3.6.1.4.1.140.600.20.1.70"},
                            {"1.3.6.1.4.1.140.600.20.1.75",
                             "QueueThroughput:1.3.6.1.4.1.140.600.20.1.75"},
                            {"1.3.6.1.4.1.140.600.20.1.80",
                             "NumEJBDeployment:1.3.6.1.4.1.140.600.20.1.80"},
                            {"1.3.6.1.4.1.140.600.20.1.85",
                             "NumEJBBeansDeployed:1.3.6.1.4.1.140.600.20.1.85"}]),
	?SETVALUE(timeout,5),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

weblogic5x_monitor_(Self)->eresye:stop(?VALUE(name)).

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
	?SETVALUE(status,[]),

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
