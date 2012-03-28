-module (mail_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> atomic_monitor.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

mail_monitor(Self, Name)->
	?SETVALUE(action,[]),
	?SETVALUE(smtpServer,[]),
	?SETVALUE(sendFromAddr,[]),
	?SETVALUE(sendToAddr,[]),
	?SETVALUE(receivingProtocol,"POP3/IMAP4"),
	?SETVALUE(pop3Server,[]),
	?SETVALUE(pop3Account,[]),
	?SETVALUE(pop3Password,[]),
	?SETVALUE(contentMatch,[]),
	?SETVALUE(timeout,300),
	?SETVALUE(checkEvery,10),
	?SETVALUE(attachment,[]),
	?SETVALUE(smtpUser,[]),
	?SETVALUE(smtpPassword,[]),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

mail_monitor_(Self)->eresye:stop(?VALUE(name)).

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
	?SETVALUE(roundTripTime,[]),
	?SETVALUE(status,[]),
	?SETVALUE(smtpTime,[]),
	?SETVALUE(receiveTime,[]),
	?SETVALUE(matchValue,[]),

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
