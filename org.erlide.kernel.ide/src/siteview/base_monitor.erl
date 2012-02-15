-module (base_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").
-include("../../include/monitor_template.hrl").

%
% monitor life cycle management:
%% start, waiting, waiting-for-resource, updating, logging,waiting
%% waiting, disable, waiting
%% waiting, disable
%
extends () -> nil .

?PATTERN(resource_allocated_pattern)-> {?VALUE(name), get, {'_',resource_allocated}}; 
?PATTERN(wakeup_pattern)-> {?VALUE(name), get, {wakeup}};
?PATTERN(logging_pattern)-> {?VALUE(name), get, {'_',logging}};
?PATTERN(refresh_pattern)-> {?VALUE(name), get, {refresh}};
?PATTERN(enable)-> [{?VALUE(name), get, {enable}},{?VALUE(name), get, {enable,fun(Time)-> Time >= 0 end}}];
?PATTERN(disable_pattern)-> {?VALUE(name), get, {disable,'_'}};
?PATTERN(frequency_pattern) -> ?VALUE(?FREQUENCY)*1000;
?PATTERN(disable_time) -> ?VALUE(disable_time)*1000.
%% ?PATTERN(disable_pattern)-> [{?VALUE(name), get, {disable}},{?VALUE(name), get, {disable,fun(Time)-> Time >= 0 end}}];

?EVENT(wakeup_event)-> {eresye,wakeup_pattern};
?EVENT(disable_event)-> {eresye,disable_pattern};
?EVENT(enable_event)-> {eresye,enable};
?EVENT(frequency_event) -> {timeout,frequency_pattern};
?EVENT(resource_allocated_event)-> {eresye,resource_allocated_pattern};
?EVENT(logging_event)-> {eresye,logging_pattern};
?EVENT(refresh_event)-> {eresye,refresh_pattern};
?EVENT(timed_enable_event) -> {timeout,disable_time}.

?ACTION(start) -> [{wakeup_event,init_action}];
?ACTION(disabled) -> [{timed_enable_event,enable},{enable_event, enable_action}];
?ACTION(logging) -> {logging_event, logging_action};
?ACTION(waiting) -> [
					{disable_event,disable_action},
					{refresh_event,request_refresh_resource_action},
					{frequency_event,request_resource_action}
					];
?ACTION(running) -> {frequency_event,request_resource_action};
?ACTION(waiting_for_resource) -> [{resource_allocated_event,update_action}].
init_action(Self,EventType,Pattern,State) ->
	io:format ( "[~w]:Type=~w,Action=init,State=~w,Event=~w,Pattern=~w\n",	[?VALUE(name),?MODULE,State,EventType,Pattern]),
	object:do(Self,waiting).

update_action(Self,EventType,Pattern,State) ->
	io:format ( "[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n",	[?VALUE(name),?MODULE,State,EventType,Pattern]),
	object:do(Self,waiting).

%%@doc the constructor
base_monitor (Self,Name) ->
	?SETVALUE(?NAME,monitor),
	?SETVALUE(?FREQUENCY,3),
	?SETVALUE(?LASTUPDATE,0),
	?SETVALUE(disable_time,0),
	?SETVALUE(?MEASUREMENTTIME,0),
	?SETVALUE(?DISABLED,false),
	?SETVALUE(?VERFIY_ERROR,true),
	?SETVALUE(?ERROR_FREQUENCY,60),
	?SETVALUE(?DEPENDS_ON,none),
	?SETVALUE(?DEPENDS_CONDITION,error),	
	?SETVALUE(name,Name),	
	eresye:start(Name).

%%@doc the destructor
base_monitor_(Self)-> 
	eresye:stop(?VALUE(name)).

disable_action(Self,EventType,Pattern,State) ->
	io:format ( "[~w:~w]Action: disable, [State]:~w, [Event type]:~w, [Pattern]: ~w '\n",	[?MODULE,?LINE,State,EventType,Pattern]),
	object:do(Self,disabled).

%%@doc request resource from the resource pool, once got the resource, then run
%%    or put the resource request into a queue
request_resource_action(Self,EventType,Pattern,State) ->
%% 	io:format ( "[~w:~w] ~w-1 Counter=~w, Action: request_resource_action, State=~w, Event type=~w, Pattern=~w '\n",	[?MODULE,?LINE,?VALUE(name),resource_pool:get_counter(?VALUE(name)),State,EventType,Pattern]),
%% 	resource_pool:request(?VALUE(name), frequency),
	{Mega,Sec,MilliSec} = erlang:now(),
%% 	Session = list_to_atom(atom_to_list(?VALUE(name)) ++ integer_to_list(MilliSec)),
	Session = Mega+Sec+MilliSec,
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),State,resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name))}),
	spawn(resource_pool,request,[?VALUE(name), Session,frequency]),
%% 	object:do(Self,waiting).
	object:do(Self,waiting_for_resource).

request_refresh_resource_action(Self,EventType,Pattern,State) ->
%% 	io:format ( "Action: request_refresh_resource, [State]:~w, [Event type]:~w, [Pattern]: ~w '\n",	[State,EventType,Pattern]),
%% 	{_,_,Ms} = erlang:now(),
%% 	Session = ?VALUE(name) ++ integer_to_list(Ms),
%% 	eresye:assert(log_analyzer, {?VALUE(name),Session,erlang:now(),State}),
%% 	eresye:assert(resource_pool,{?VALUE(name),request_refresh_resource}), %%trigging the request_refresh_resource_pattern in resource_pool module
	resource_pool:request(?VALUE(name), refresh),
	object:do(Self,waiting_for_resource).

enable_action(Self,EventType,Pattern,State) ->
	io:format ( "Action: enable, [State]:~w, [Event type]:~w, [Pattern]: ~w '\n",	[State,EventType,Pattern]),
	object:do(Self,waiting).

%%@doc post run processing, e.g. releasing resources and changing the state 
post_run(Self) -> 
	resource_pool:release(?VALUE(name)), 	
	eresye:assert(?VALUE(name), {logging}),
	object:do(?VALUE(name),waiting).
%% 	object:do(?VALUE(name),logging).

%%@doc logging the measurement into database
logging_action(Self,EventType,Pattern,State) -> 
	%TODO: logging data to database
	{Session,_} = Pattern,
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),State,resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name))}),
	timer:sleep(random:uniform(2)*1000),  % simulate the logging time
	io:format ( "[~w:~w] Action: logging, [State]:~w, [Event type]:~w, [Pattern]: ~w '\n",	[?MODULE,?LINE,State,EventType,Pattern]),
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),finished,resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name))}),	
	object:do(Self,waiting).

allocate_resource(Self) ->
	object:add_fact(Self,{resource_allocated}).

%%@doc get the measurement now, not by frequency
refresh(Self) ->
	eresye:assert(?VALUE(name), {refresh}).

on_starting(Self) ->
	io:format("This [~w] ~w object is starting \n",[?VALUE(name),?MODULE]).

on_stopping(Self) ->
	io:format("This [~w] ~w object is stopping \n",[?VALUE(name),?MODULE]).


start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				object:add_fact(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " not available, choose a new name"
	end.