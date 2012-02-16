%% @doc  the analysing engine to process the logs
%%  it can be used to analyze the measurement logs and can also be used to analyze the 
%% 	internal state transition and monitoring health condition.  The log are asserted into the engine as facts
%% rules can be set to take action for the facts.
%%  fields: name, state, timestampe, sessions
%%  rules: if session complete, delete,
%%  actions: delete log, alert, corrective action
%% 
%%  monitor state machine:
%%  get the exago recognize exat's facts


-module (log_analyzer).
-compile ([export_all]).

-include("object.hrl").
-include("../exago/exago_state_machine.hrl").

extends () -> nil .

%% 	eresye:assert(log_analyzer, {?VALUE(name),Session,erlang:now(),object:stateof(Self),resource_pool:get_counter(?VALUE(name)),resource_pool:get_queue_length(?VALUE(name))}),
%% name, session, timestamp, state,counter,queueLen

?PATTERN(monitor_timeout_pattern) -> {?LOGNAME,read,{'_', '_', fun(X)-> Diff = timer:now_diff(erlang:now(), X)/1000000, Diff > 100 end, running,'_','_'}};
?PATTERN(finished_pattern) -> {?LOGNAME, read, {'_','_','_',finished, '_','_'}}.

?EVENT(finished_event)-> {eresye,finished_pattern}.

?ACTION(start) -> {finished_event,finished_action}.
%%TODO: check for the long running monitor

%% @doc check the completeness of monitoring sequence
finished_action(Self,EventType,Pattern,State) -> 
	{Name,Session,_,_,_,_} = Pattern,
	Pattern1 = {Name,Session,'_','_','_','_'},
	MonitorStateList = eresye:query_kb(?LOGNAME, Pattern1),
%% 	io:format("[~w:~w] MonitorStateList=~w~n",[?MODULE,?LINE,MonitorStateList]),
	eresye:retract(?LOGNAME, MonitorStateList),
	%% TODO: check for completeness: feed the MonitorStateList into exago
	%% convert MonitorState to event format
	%% TODO: check for counter and queue
	
	RowFormat = monitor_row_format(),
	MonitorStateTest = [{"ping1","1111","2010-10-12 16:50:20:0868702","frequency","0","0"},{"ping1","1111","2010-10-12 16:50:25:0868702","resource_allocated","0","0"},
						{"ping1","1111","2010-10-12 16:50:30:0868702","update","0","0"},{"ping1","1111","2010-10-12 16:50:35:0868702","logging","0","0"}],
	
    EventSource  = exago_event:new_source("monitor_state_log", MonitorStateTest, RowFormat),
%% 	io:format("[~w:~w] MonitorStateList = ~w~n", [?MODULE,?LINE,MonitorStateList]),
%% 	io:format("[~w:~w] EventSource = ~w~n", [?MODULE,?LINE,EventSource]),
%%     EventSource = 
%% 	exago_event:new_source("monitor_state_log", MonitorStateList, RowFormat, 
%% 			       {?MODULE, monitor_input_filter},
%% 			       {?MODULE, monitor_input_modifier, 
%% 				[transition_input, "Counter", "QueueLen"]}),

    StateMachine = monitor_state_machine(),
%% 	io:format(EventSource),

    Result = exago_state_machine:analyse_event_source(EventSource, StateMachine),
	%% error alert based on the Result
	exago_printer:print_result(Result),


	Len = length(MonitorStateList),
	lists:foreach(
	  fun(MonitorState) -> 
			  {Name1,Session1,Timestamp,State1, Counter,QueueLen} = MonitorState
%% 			  io:format("[~w:~w] Name=~w,Session=~w,Timestamp=~w,State=~w,Counter=~w,QueueLen=~w~n",
%% 						[?MODULE,?LINE, Name1,Session1,Timestamp,State1, Counter,QueueLen])
	  end, MonitorStateList),
ok.

%%@doc timeout monitoring
monitor_timeout_action (Self,EventType,Pattern,State) ->
	ok.  %%TODO

%% @doc The monitor state machine model
-spec(monitor_state_machine/0 :: () -> #state_machine{}).
monitor_state_machine() ->
    StateMachine =
	#state_machine{
      states= %%state, adding finish state
	  [#state{number=0, name="waiting"},
	   #state{number=1, name="disabled"},
	   #state{number=2, name="waiting_for_resource"},
	   #state{number=3, name="running"},
	   #state{number=4, name="logging"},
	   #state{number=5, name="finished"}],
      transitions= %%pattern
	  [#transition{from=0, to=2, input="frequency"},
	   #transition{from=0, to=1, input="disable"},
	   #transition{from=1, to=0, input="enable"},
	   #transition{from=2, to=3, input="resource_allocated"},
	   #transition{from=3, to=4, input="update"},
	   #transition{from=4, to=5, input="logging"}],
      start=0,
      accept=[0,1,5]},
    StateMachine.

%% @doc Each log file should have a row format which specifies the required
%% fields (timestamp, transition_input and group_id), and any other
%% information. For example in the following row format, one row looks
%% like:  
%%     {"2010-10-12 16:50:03:0423338","38","close","1"} or
%%     {"2010-10-12 16:49:56:0753614","14","reset","2","closed","1"}
%%
%% As you can see the fields vary in size, and the input data is
%% reflected precisely in the definitions of the field parsers below.
%%
%% Annotations are useful especially in the input modifier, since they
%% can be used to modify the input in any way that you like.
%% As an example of this, the input modifier for this example uses
%% the "floor1" and "floor2" annotations to construct a transition
%% input.
-spec(monitor_row_format/0 :: () -> list()).
monitor_row_format() ->
    [
     exago_field:parser(annotation, "Name"),
     exago_field:parser(group_id),%% session
%% 	 exago_field:parser(timestamp,[]), %%time 
	 exago_field:parser(timestamp, "yyyy-MM-dd hh:mm:ss:fffffff"), %%time 
     exago_field:parser(transition_input), %%state
     exago_field:parser(annotation, "Counter"),
     exago_field:parser(annotation, "QueueLen")
	].
%% name, session, timestamp, state,counter,queueLen

%% @doc In this example, we wish to modify the input to the transitions 
%% in the state machine, so that they better reflect the state machine 
%% model. This step is not necessary if your inputs already fit the model.
-spec(monitor_input_modifier/1 :: (list()) -> tuple()).
monitor_input_modifier(Fields) ->
    TransitionInput = proplists:get_value(transition_input, Fields),
    case TransitionInput of
	"reset"       ->
	    FloorN = proplists:get_value("floor2", Fields),
	    {transition_input, "reset_to_" ++ FloorN};
	"approaching" ->
	    FloorN = proplists:get_value("floor1", Fields),
	    {transition_input, "approaching_" ++ FloorN};
	"stopped_at"  ->
	    FloorN = proplists:get_value("floor1", Fields),
	    {transition_input, "stopped_at_" ++ FloorN};
	_             ->
	    {transition_input, TransitionInput}
    end.

%% @doc This is another optional function which is used to filter out
%% unwanted transition inputs. In this example we are only interested
%% in keeping the specified inputs (those that evaluate to true), and
%% so we return false to filter any unnecessary inputs. 
-spec(monitor_input_filter/1 :: (list()) -> true | false).
monitor_input_filter(Input) ->
    case Input of
	"frequency"       -> true;
	"disable"             -> true;
	"enable"            -> true;
	"resource_allocated" -> true;
	"update"  -> true;
	"logging"   -> true;
	_                  -> false
    end.

start()->
	Logger = object:new(?MODULE),
	object:start(Logger),
	eresye:start(?LOGNAME),
	Logger.

log_analyzer(Self)->
	?SETVALUE(name,?LOGOBJ),
	eresye:start(?VALUE(name)).

log_analyzer_(Self)->eresye:stop(?VALUE(name)).

