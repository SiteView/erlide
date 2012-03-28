-module (oracle9i_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> urlcontent_base.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

oracle9i_monitor(Self, Name)->
	?SETVALUE(url,[]),
	?SETVALUE(timeout,60),
	?SETVALUE(userName,[]),
	?SETVALUE(password,[]),
	?SETVALUE(counters,[{"1","Up/Down Time(up/down)"},
                            {"2","Completed Requests(number/sec)"},
                            {"3","Completed Requests(max/sec)"},
                            {"4","Completed Requests(avg/sec)"},
                            {"5","Completed Requests(total)"},
                            {"6","Latency(avg this interval)"},
                            {"7","Latency(avg since start)"},
                            {"8","Load(now)"},
                            {"9","Load(max)"},
                            {"10","Active Sessions(now)"}]),
	?SETVALUE(ntlm,[]),
	?SETVALUE(proxyUserName,[]),
	?SETVALUE(proxyPassword,[]),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

oracle9i_monitor_(Self)->eresye:stop(?VALUE(name)).

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
