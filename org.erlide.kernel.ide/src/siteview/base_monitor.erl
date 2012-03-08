-module (base_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").
<<<<<<< HEAD
%% -include("../../include/log.hrl").
-include("../../include/classifierstring.hrl").

=======
-include("../../include/monitor_template.hrl").
-include("../../include/classifierstring.hrl").
>>>>>>> pre_classifier_version
%
% monitor life cycle management:
%% 0. ping_monitor:start: initializing
%% 1. base_monitor:action(Self,waiting): triggerred by frequency base_monitor:request_resource_action()
%% 2. waiting-for-resource:
%% 3. running:
%% 4. base_monitor:logging
%% 5. waiting
%% waiting, disable, waiting
%% waiting, disable
%
%% TODO: add period based frequency
<<<<<<< HEAD
%% classifier: when meet a pattern, set the category value, the classifier condition should be a pattern within the object
=======
%% TODO: schedule optimization: grouping and seperating
%% 		grouping: using the same resource e.g. SSH coonection can be grouped togather to re-use the connection before close the connection 
%% 		seperating: seperating the monitors to reduce the parallel execution
>>>>>>> pre_classifier_version
 
extends () -> nil .

?PATTERN(resource_allocated_pattern)-> {?VALUE(name), get, {'_',resource_allocated}}; %%triggerred in resource_pool:do_request
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
?ACTION(disabled) -> [{timed_enable_event,enable_action},{enable_event, enable_action}];
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
	?SETVALUE(?FREQUENCY,5),
	?SETVALUE(?LASTUPDATE,0),
	?SETVALUE(disable_time,0),
	?SETVALUE(?MEASUREMENTTIME,0),
	?SETVALUE(wait_time,0),
	?SETVALUE(?DISABLED,false),
	?SETVALUE(?VERFIY_ERROR,true),
	?SETVALUE(?ERROR_FREQUENCY,60),
	?SETVALUE(?DEPENDS_ON,none),
	?SETVALUE(?DEPENDS_CONDITION,error),
<<<<<<< HEAD
	?SETVALUE(?LAST_CATEGORY,nodata),
	?SETVALUE(?CATEGORY,nodata),
	?SETVALUE(?STATE_STRING,""),
	?SETVALUE(?RULES,[]),	
	?SETVALUE(name,Name),	
=======
    ?SETVALUE(?LAST_CATEGORY,nodata),
	?SETVALUE(?CATEGORY,nodata),
	?SETVALUE(?STATE_STRING,""),
	?SETVALUE(?RULES,[]),	
	?SETVALUE(name,Name),
	?SETVALUE(data_logger,"localhost"),  %%the data logger used in log_action
>>>>>>> pre_classifier_version
	eresye:start(Name). %%TODO: need evaluate whether start a rule engine for each monitor or one rule engine for all monitor ?

%%@doc the destructor
base_monitor_(Self)-> 
	eresye:stop(?VALUE(name)).

disable_action(Self,EventType,Pattern,State) ->
	io:format ( "[~w:~w]Action: disable, [State]:~w, [Event type]:~w, [Pattern]: ~w '\n",	[?MODULE,?LINE,State,EventType,Pattern]),
	object:do(Self,disabled).

%%@doc request resource from the resource pool, once got the resource, then run
%%    or put the resource request into a queue
request_resource_action(Self,EventType,Pattern,State) ->
	%%TODO: insert schedule processing here.
	
%% 	io:format ( "[~w:~w] ~w-1 Counter=~w, Action: request_resource_action, State=~w, Event type=~w, Pattern=~w '\n",	[?MODULE,?LINE,?VALUE(name),resource_pool:get_counter(?VALUE(name)),State,EventType,Pattern]),
	{Mega,Sec,MilliSec} = erlang:now(),
%% 	TODO: using PID as session, can be used to communicate with the update action
	Session = Mega+Sec+MilliSec,
	resource_pool:request(?VALUE(name), Session,frequency_request),
	object:do(Self,waiting_for_resource).

request_refresh_resource_action(Self,EventType,Pattern,State) ->
%% 	io:format ( "Action: request_refresh_resource, [State]:~w, [Event type]:~w, [Pattern]: ~w '\n",	[State,EventType,Pattern]),
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

%%@doc logging the measurement into database by inform the data_logger to pull the data from monitor object
logging_action(Self,EventType,Pattern,State) -> 
	{Session,_} = Pattern,
	resource_pool:release(?VALUE(name), Session),
	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),log}),	
	%TODO: logging data to database
	timer:sleep(random:uniform(2)*1000),  % simulate the logging time
%% 	io:format ( "[~w:~w] Action: logging, [State]:~w, [Event type]:~w, [Pattern]: ~w '\n",	[?MODULE,?LINE,State,EventType,Pattern]),
	object:do(Self,waiting).

allocate_resource(Self) ->
	eresye:assert(?VALUE(name),{resource_allocated}).

%%@doc get the measurement now, not by frequency
refresh(Self) ->
	eresye:assert(?VALUE(name), {refresh}).

on_starting(Self) ->
	io:format("This [~w] ~w object is starting \n",[?VALUE(name),?MODULE]).

on_stopping(Self) ->
	io:format("This [~w] ~w object is stopping \n",[?VALUE(name),?MODULE]).

<<<<<<< HEAD
=======
run_classifier(Self,Classifier) ->
	io:format("[~w:~w] name=~w, Classifier=~w~n", [?MODULE,?LINE,?VALUE(name),Classifier]).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

>>>>>>> pre_classifier_version
%% @spec get_classifier(_)->list()
%% @doc get classifier information
%%
get_classifier(_)->[]. 

%% @spec classifier(This,[R|T])-> (true | false)
%% @doc classify monitor's category
%%
% classifier(_,[])->false;
% classifier(This,[R|T])->
	% case object:call(Self, classifier_item(This,R) of
		% false ->
			% object:call(Self, classifier(This,T);
		% _->
			% true
	% end.
	
preclassifier(_,[],R)->R;
preclassifier(_,['and'],R)->R;
preclassifier(_,['or'],R)->R;
preclassifier(Self,['and'|T],R)->
	preclassifier(Self,T,R ++ ['and']);
preclassifier(Self,['or'|T],R)->
	preclassifier(Self,T,R ++ ['or']);
preclassifier(Self,[I|T],R)->
	preclassifier(Self,T,R++[object:call(Self, classifier_item, [I])]).
	
classifier(Self,Conds)->
%% 	io:format("Exp11:~p~n",[Conds]),
	Exp = preclassifier(Self,Conds,[]),
%% 	io:format("Exp2:~p~n",[Exp]),
	bool_exp:parse(Exp).

%% @spec str2num(Str)->(float|integer)
%% @doc convert string to float or integer
%%
str2num(Str)->
	case string:to_float(Str) of
		{F,[]}->
			F;
		_->
			case string:to_integer(Str) of
				{I,[]}->
					I;
				_->
					Str
			end
	end.

%% @spec num2str(Num)->string()
%% @doc convert number to string
%%
num2str(Num)->
	if
		is_float(Num)->
			float_to_list(Num);
		is_integer(Num)->
			integer_to_list(Num);
		true ->
			Num
	end.

%% @spec classifier_item(Self,R)->(true|false)
%% @doc caculate a classifier item's value
%%
classifier_item(Self,R)->
	case element(1,R) of
		always ->
			true;
		alwaysfalse ->
			false;
		P->
			%io:format("classifier_item:~p~n",[P]),
			case ?VALUE(P) of
%% 				{ok,{P,"n/a"}}->
 				"n/a"->
					%io:format("value is n/a~n"),
					true;
%% 				{ok,{P,V}}->
				V->
					case element(2,R) of
						'>'->
							if							
								(not is_number(V)) andalso (not is_number(element(3,R)))->
									str2num(V) > str2num(element(3,R));
								(not is_number(V))->
									str2num(V) > element(3,R);
								(not is_number(element(3,R)))->
									V >str2num(element(3,R));
								true ->
									V>element(3,R)
							end;
						'>='->
							if
								(not is_number(V)) andalso (not is_number(element(3,R)))->
									str2num(V) >= str2num(element(3,R));
								(not is_number(V))->
									str2num(V) >= element(3,R);
								(not is_number(element(3,R)))->
									V >=str2num(element(3,R));
								true ->
									V>=element(3,R)
							end;
							%V>=element(3,R);
						'<'->
							if
								(not is_number(V)) andalso (not is_number(element(3,R)))->
									str2num(V) < str2num(element(3,R));
								(not is_number(V))->
									str2num(V) < element(3,R);
								(not is_number(element(3,R)))->
									V <str2num(element(3,R));
								true ->
									V<element(3,R)
							end;
							%V < element(3,R);
						'<='->
							if
								(not is_number(V)) andalso (not is_number(element(3,R)))->
									str2num(V) =< str2num(element(3,R));
								(not is_number(V))->
									str2num(V) =< element(3,R);
								(not is_number(element(3,R)))->
									V =<str2num(element(3,R));
								true ->
									V=<element(3,R)
							end;
							%L =  element(3,R),
							%(V< L) or (V =:= L);
						'!='->
							if
								is_list(V) andalso is_list(element(3,R))->
									V=/= element(3,R);
								is_number(V) andalso is_number(element(3,R))->
									V=/= element(3,R);
								is_number(V) ->
									V=/=str2num(element(3,R));
								is_number(element(3,R))->
									str2num(V)=/= element(3,R);
								true ->
									V=/= element(3,R)
							end;
						'=='->
							if
								is_list(V) andalso is_list(element(3,R))->
									V== element(3,R);
								is_number(V) andalso is_number(element(3,R))->
									V== element(3,R);
								is_number(V) ->
									V==str2num(element(3,R));
								is_number(element(3,R))->
									str2num(V)== element(3,R);
								true ->
									V== element(3,R)
							end;
							%V =:= element(3,R);
						'contains'->
							if
								is_number(V) andalso is_number(element(3,R))->
									string:rstr(num2str(V),num2str(element(3,R)))>0;
								is_number(V)->
									string:rstr(num2str(V),element(3,R))>0;
								is_number(element(3,R))->
									string:rstr(V,num2str(element(3,R)))>0;
								true ->
									string:rstr(V,element(3,R)) > 0
							end;
						'!contains'->
							if
								is_number(V) andalso is_number(element(3,R))->
									string:rstr(num2str(V),num2str(element(3,R)))=<0;
								is_number(V)->
									string:rstr(num2str(V),element(3,R))=<0;
								is_number(element(3,R))->
									string:rstr(V,num2str(element(3,R)))=<0;
								true ->
									string:rstr(V,element(3,R)) =< 0
							end;
							%string:rstr(V,element(3,R)) =< 0;
						_->
							false
					end;
				_->
					false
			end
	end.


compute_classifier(_, [], Operate, Ret)->
  Ret;
compute_classifier(Classifier, [H|T], Operate, Ret)->
	case erlang:is_tuple(H) of
		true ->
%% 		   case size(H) of
%% 			5->
%% 				{PropName, Operation, Value, StartTime, EndTime} = H,
				case Classifier =:= H of
					true->
						case Operate of
							'and'->
%% 								io:format("--------classifier_outoftime-and------------------------------:~p~n", [H]),
								compute_classifier(Classifier, T, Operate ,{always});
							'or'->
%% 								io:format("--------classifier_outoftime--or-----------------------------:~p~n", [H]),
								compute_classifier(Classifier, T, Operate ,{alwaysfalse});
							_->
%% 								io:format("--------classifier_outoftime--???-----------------------------:~p~n", [H]),
								compute_classifier(Classifier, T, Operate ,Ret)
						end;
					_->
						compute_classifier(Classifier, T, Operate ,Ret)
				end;
%% 			_->
%% 				classifier_outoftime(In, T, Operate, Ret)
%% 		   end;
		_->
%% 			io:format("--------classifier_outoftime4-------------------------------:~p~n", [H]),
			compute_classifier(Classifier, T, H, Ret)
  	end.

get_firstrelation(Classifier, Ret)->
	case erlang:length(Ret) > 2 of
		true->
			H = lists:nth(1, Ret),
			case erlang:is_tuple(H) of 
				true->
					case  Classifier=:= H of
						true->
							Operate= lists:nth(2, Ret),
							Operate;
						_->
							no
					end;
				_->
					no
			end;
		_->
			no
	end.

proc_valid_classifier([], RetSrc, Ret)->
	Ret;
proc_valid_classifier([H|T], RetSrc, Ret)->
	Relation = get_firstrelation(H, RetSrc),
%% 	io:format("get_firstrelation: ~p~n", [Relation]),
	TrueOrFalse = compute_classifier(H, RetSrc, Relation, {alwaysfalse}),
	F = fun(X,Acc1)->
		{X1, Value} = Acc1,
		case X1 =:= X of
			true->
				{Value, Acc1};
			_->
				{X, Acc1}
			end
	end,	
	{RetReplace, _} = lists:mapfoldl(F, {H, TrueOrFalse}, Ret),
	proc_valid_classifier(T, RetSrc, RetReplace).

get_default_firstrelation(Classifier, Ret)->
	case erlang:length(Ret) > 2 of
		true->
			H = lists:nth(1, Ret),
			case erlang:is_tuple(H) of 
				true->
					case (element(1, Classifier) =:= element(1, H)) and (element(4, Classifier) =:= element(4, H)) of
						true->
							Operate= lists:nth(2, Ret),
							Operate;
						_->
							no
					end;
				_->
					no
			end;
		_->
			no
	end.
	
compute_default_classifier(_, [], Operate, Ret)->
  Ret;
compute_default_classifier(Classifier, [H|T], Operate, Ret)->
	case erlang:is_tuple(H) of
		true ->
		   case size(H) of
			4->
%% 				{Name, Operation, Value, Scheudle} = H,
				case (element(1, Classifier) =:= element(1, H)) and (element(4, Classifier) =:= element(4, H)) of
					true->
						case Operate of
							'and'->
%% 								io:format("--------classifier_outoftime-and------------------------------:~p~n", [H]),
								compute_default_classifier(Classifier, T, Operate ,{always});
							'or'->
%% 								io:format("--------classifier_outoftime--or-----------------------------:~p~n", [H]),
								compute_default_classifier(Classifier, T, Operate ,{alwaysfalse});
							_->
%% 								io:format("--------classifier_outoftime--???-----------------------------:~p~n", [H]),
								compute_default_classifier(Classifier, T, Operate ,Ret)
						end;
					_->
						compute_default_classifier(Classifier, T, Operate ,Ret)
				end;
			_->
				compute_default_classifier(Classifier, T, Operate, Ret)
		   end;
		_->
%% 			io:format("--------classifier_outoftime4-------------------------------:~p~n", [H]),
			compute_default_classifier(Classifier, T, H, Ret)
  	end.

proc_default_classifier([], RetSrc, Ret)->
	Ret;
proc_default_classifier([H|T], RetSrc, Ret)->
	Relation = get_default_firstrelation(H, RetSrc),
%% 	io:format("get_default_firstrelation: ~p~n", [Relation]),	
	TrueOrFalse = compute_default_classifier(H, RetSrc, Relation, {alwaysfalse}),
	F = fun(X,Acc1)->
		{X1, Value} = Acc1,
%% 		case X1 =:= X of
		case erlang:is_tuple(X) of
			true ->
		   		case size(X) of
	 			  4->		
					case (element(1, X1) =:= element(1, X)) and (element(4, X1) =:= element(4, X)) of		
						true->
							{Value, Acc1};
						_->
							{X, Acc1}
					end;
				  _->
					  {X, Acc1}
				end;
			_->
				{X, Acc1}
		end
	end,	
	{RetReplace, _} = lists:mapfoldl(F, {H, TrueOrFalse}, Ret),
	proc_default_classifier(T, RetSrc, RetReplace).

confict_Classifier([], RetSrc, Ret)->
	Ret;
confict_Classifier([H|T], RetSrc, Ret)->
%% 	{Name, Operation, Value, Schedule} = H,
%% 	case schedule_property:is_enabled(Schedule) of
%% 		true->
%% 			ok;
%% 		_->
%% 			ok
%% 	end,
%% 	io:format("--------confict_Classifier--in time-----------------------------:~p~n", [H]),
	{Name, Operation, Value, Schedule} = H,
	
%% 	{{Year, Month, Day}, {Hour, Minutes, Seconds}} = calendar:local_time(),
%% 	case  (Hour >= StartTime) and (Hour =< EndTime) of
	case schedule_property:measurement_is_enabled(schedule_manager:get_measurement_schedule_filter(Schedule)) of
		true->
%% 			io:format("--------confict_Classifier--in time-----------------------------:~p~n", [H]),
%% 			case lists:keysearch(Name, 1, Ret) of
%% 				false ->
%% 					computeClassifier(T, RetSrc, Ret ++ [{Name, Operation, Value}]);
%% 				_ ->
%% 					computeClassifier(T, RetSrc, lists:keyreplace(Name, 1, Ret, {Name, Operation, Value}))
%% 			end;			
			case Schedule of
				"7x24"->
					Conditions= [{Name, Operation, Value, " "}];
%% 				" "->
%% 					Conditions= [{Name, Operation, Value}, {Name, Operation, Value, " "}, {Name, Operation, Value, "7x24"}];
				_->
					Conditions= [{Name, Operation, Value, " "}, {Name, Operation, Value, "7x24"}]
			end,
			RetReplace = proc_default_classifier(Conditions, RetSrc, Ret),
			confict_Classifier(T, RetSrc, RetReplace);
		_->	
%% 			io:format("--------confict_Classifier-out time In------------------------------:~p~n", [H]),
%% 			TrueOrFalse = classifier_outoftime(H, RetSrc, 'or', {alwaysfalse}),
%% 			F = fun(X,Acc1)->
%% 				{X1, T1} = Acc1,
%% 				case X1 =:= X of
%% 					true->
%% 						{T1, Acc1};
%% 					_->
%% 						{X, Acc1}
%% 				end
%% 			end,	
%% 			{RetReplace, _} = lists:mapfoldl(F, {H, TrueOrFalse}, Ret),
			Conditions = [H],
			RetReplace = proc_valid_classifier(Conditions, RetSrc, Ret),
%% 			io:format("--------confict_Classifier-out time------------------------------:~p~n", [RetReplace]),
			confict_Classifier(T, RetSrc, RetReplace)
	end.

time_Classifier([], RetSrc, Ret)->
	Ret;
time_Classifier([H|T], RetSrc, Ret)->
	{Name, Operation, Value, Schedule} = H,	
	F = fun(X,Acc1)->
		{X1, T1} = Acc1,
		case X1 =:= X of
			true->
				{T1, Acc1};
			_->
				{X, Acc1}
		end
	end,	
	{RetReplace, _} = lists:mapfoldl(F, {H, {Name, Operation, Value}}, Ret),
	time_Classifier(T, RetSrc, RetReplace).

%% @spec classifier_all(Self,[C|T])->(true|false)
%% @doc caculate all classifier's value
%%
classifier_all(Self,[])->?SETVALUE(?CATEGORY,good);
classifier_all(Self,[C|T])->
	%% call xxxx_monitor:get_classifier/1, e.g. ping_monitor:get_classifier(_), diskspace_monitor:get_classifier(_) etc.
%%  	io:format("Self: ~p----C: ~p---------classifier_all--------:~p~n", [Self, C, get_classifier_start]),
	R = object:call(Self, get_classifier, [C]),
%% 	io:format("Self: ~p ---------------classifier_all--------:~p~n", [Self, R]),
%% 	R = lists:umerge(object:call(Self, get_classifier(C)),

	%%cxy 2011/04/22 Time + Measurement Filter
%% 	{RetTime, Ret} = splitClassifier(R, [], []),
	
	F = fun(X) ->
		case erlang:is_tuple(X) of
			true ->
		   		case size(X) of
					4->
						{Name, Operation, Value, Schedule} = X,
						case Schedule of
							" "->
%% 								io:format("---------------classifier_all kkkkk Schedule--------:~p~n", [X]),
								false;
							_->
								true
						end;
					_->
						false
				end;
			_->
				false
		   end
	end,
	RetTime = lists:filter(F, R),
%% 	io:format("Self: ~p---------------classifier_all ffff --------:~p~n", [Self, R]),
%% 	RFilter = confict_Classifier(RetTime, R, R),
	Confict_Filter = confict_Classifier(RetTime, R, R),	
	RFilter = time_Classifier(RetTime, R, Confict_Filter),	
	case RetTime of
		[]->
			ok;
		_->
			io:format("---------------classifier_all-R-------:~p~n", [R]),
			io:format("---------------classifier_all-RetTime-------:~p~n", [RetTime]),
			io:format("---------------classifier_all-RFilter-------:~p~n", [RFilter])
	end,
	
	%%io:format("classifier_all:~p~n",[R]),
%% 	io:format("---------------classifier_all RFilter 1111 --------:~p~n", [RFilter]),
%% 	case classifier(Self,RFilter) of
	case object:call(Self, classifier, [RFilter]) of		
		true->
%% 			io:format("---------------classifier_all RFilter -------- :~p~n", [RFilter]),		
			?SETVALUE(?CATEGORY,C),			
			ok;
		false->
%% 			classifier_all(Self,[T])
			object:call(Self, classifier_all,[T]) 
	end.

<<<<<<< HEAD
%% @spec runClassifiers(This)->({ok,Result}|{error,Reason})
%% @doc run monitor's classifiers
%% 
runClassifiers(Self, This)->	
%% 	io:format("runClassifiers111111: [~w] ~w \n",[?VALUE(name),?MODULE]),
	object:call(Self, classifier_all, [[error,warning,good]]),
=======
%%@doc called by java to set the classifier
set_classifier(Self,[]) -> ok;
set_classifier(Self,[{Type,Classifier}|T]) -> 
%% 	?SETVALUE(?VALUE(Type),  (#erlv8_fun_invocation{}, []) ->  ?QUEVALUE(round_trip_time) > 100 end}])),
	?SETVALUE(?VALUE(Type),  Classifier),
	set_classifier(Self,T).
	

%%@doc execute the js, set the ?CATEGORY value based on the result of executing the js
%% 		
%% 
%%  fun (#erlv8_fun_invocation{}, [String]) -> lists:reverse(String) end}])
%% 
runClassifier2(Self) ->
	{ok, VM} = erlv8_vm:start(),
	Global = erlv8_vm:global(VM),
	Global:set_value("classifier",erlv8_object:new([{"error",?VALUE(error_classifier)}] )),
	Global:set_value("classifier",erlv8_object:new([{"major",?VALUE(major_classifier)}] )),
	Global:set_value("classifier",erlv8_object:new([{"warning",?VALUE(warning_classifier)}] )),
	Global:set_value("classifier",erlv8_object:new([{"minor",?VALUE(minor_classifier)}] )),
	Global:set_value("classifier",erlv8_object:new([{"ok",?VALUE(ok_classifier)}] )),
	{ok,Error} = erlv8_vm:run(VM,"classifier.error()"),
	{ok,Major} = erlv8_vm:run(VM,"classifier.major()"),
	{ok,Warning} = erlv8_vm:run(VM,"classifier.warning()"),
	{ok,Minor} = erlv8_vm:run(VM,"classifier.minor()"),
	{ok,OK} = erlv8_vm:run(VM,"classifier.ok()"),
	if Error -> ?SETQUEVALUE(?CATEGORY,error);
	   Major -> ?SETQUEVALUE(?CATEGORY,major);
	   Warning -> ?SETQUEVALUE(?CATEGORY,warning);
	   Minor -> ?SETQUEVALUE(?CATEGORY,minor);
	   OK -> ?SETQUEVALUE(?CATEGORY,ok);
	   true -> un_classified
	end,
	ok.

%% @spec runClassifiers(This)->({ok,Result}|{error,Reason})
%% @doc run monitor's classifiers
%% 
runClassifiersold(Self, This)->	
%% 	io:format("runClassifiers111111: [~w] ~w \n",[?VALUE(name),?MODULE]),
%% 	object:call(Self, classifier_all, [[error,warning,good]]),
	classifier_all(Self, [[error,warning,good]]),
>>>>>>> pre_classifier_version
%% 	io:format("runClassifiers2222: [~w] ~w \n",[?VALUE(name),?MODULE]),
	% object:call(Self, classifier_by_statestring(This),
	object:call(Self, runClassifiers, [?VALUE(?CATEGORY),?VALUE(?LAST_CATEGORY)]).
	
classifier_by_statestring(Self)->
	case ?VALUE(?STATE_STRING) of
%% 		{ok,{_,State}}->
		State->
			StateString = string:strip(State),
			F = fun(X)->re:run(StateString,X) =/= nomatch end,
			
			case lists:any(F,?NODATA_STRINGS) of
				true->
					?SETVALUE(?CATEGORY,?NO_DATA);
				_->
					case lists:any(F,?ERROR_STRINGS) of
						true->
							?SETVALUE(?CATEGORY,error);
						_->
							case lists:any(F,?WARNING_STRINGS) of
								true->
									?SETVALUE(?CATEGORY,warning);
								_->
									pass
							end
					end
			end;
		_->
			pass
	end.

%% @spec runClassifiers(Self,{ok,{?CATEGORY,C}},{ok,{?LAST_CATEGORY,LC}})->({ok,Result}|{error,Reason})
%% @doc run monitor's classifiers
%%
%% runClassifiers(Self,{ok,{?CATEGORY,C}},{ok,{?LAST_CATEGORY,LC}})->
 runClassifiers(Self,C, LC)->
	case C of
		LC->
%% 			object:call(Self, inc_attribute, [list_to_atom(atom_to_list(C) ++ "Count")]);
			ok;
		_->
			?SETVALUE(errorCount,0),
			?SETVALUE(warningCount,0),
			?SETVALUE(normalCount,0),
			?SETVALUE(?LAST_CATEGORY,C),
			?SETVALUE(list_to_atom(atom_to_list(C) ++ "Count"),1)
	end;
	
%% 	object:call(Self, setParentCategory(Self,C);

runClassifiers(_,_,_)->{error,object_state_wrong}.
<<<<<<< HEAD
%%test
=======
>>>>>>> pre_classifier_version

%% %% @spec setParentCategory(Self,Category)->({ok,Result}|{error,Reason})
%% %% @doc set parent's category
%% %%
%% setParentCategory(Self,Category)->
%% 	case object:call(Self, get_owner() of
%% 		{ok,{parent,Parent}}->
%% 			case Parent:inc_attribute(list_to_atom(atom_to_list(Category) ++ "Count")) of
%% 				{error,attribute_not_found}->
%% 					Parent:set_attribute(list_to_atom(atom_to_list(Category) ++ "Count"),1);
%% 				_->
%% 					pass
%% 			end,
%% 			Childs = Parent:get_childs(),
%% 			Stat = lists:map(fun(X)->
%% 								case X:get_attribute(?CATEGORY) of
%% 									{ok,{?CATEGORY,Categ}}->
%% 										Categ;
%% 									_->
%% 										?ERROR_LOG2("no category:~p~n",[X:get_property(id)]),
%% 										nodata
%% 								end
%% 							end,Childs),
%% 			case lists:member(error,Stat) of
%% 				true ->
%% 					Parent:set_attribute(?CATEGORY,error);
%% 				_->
%% 					case lists:member(warning,Stat) of
%% 						true ->
%% 							Parent:set_attribute(errorCount,0),
%% 							Parent:set_attribute(?CATEGORY,warning);							
%% 						_->
%% 							case lists:member(good,Stat) of
%% 								true ->
%% 									Parent:set_attribute(errorCount,0),
%% 									Parent:set_attribute(warningCount,0),
%% 									Parent:set_attribute(?CATEGORY,good);
%% 								_->
%% 									Parent:set_attribute(warningCount,0),
%% 									Parent:set_attribute(errorCount,0),
%% 									Parent:set_attribute(good,0),
%% 									Parent:set_attribute(?CATEGORY,nodata)
%% 							end
%% 					end
%% 			end,
%% 
%% 			Parent:setParentCategory(Parent,Category);
%% 		_->
%% 			{error,not_found_parent}
%% 	end.
<<<<<<< HEAD
		
%% @spec get_counter_attribute()->[]
%% @doc get counter attribute
%%
get_counter_attribute()->[].

=======

set_classifier([{Type,Classifier}|T]) -> ok.
	

%%@doc execute the js, set the ?CATEGORY value based on the result of executing the js
%% 		
%% 
%% 
%% 
runClassifiers(Self, This)->
	{ok, VM} = erlv8_vm:start(),
	io:format("---------------runClassifiers VM------:~p~n", [VM]),
	Global = erlv8_vm:global(VM),
	Global:set_value("classifier",erlv8_object:new([{"error",?VALUE(error_classifier)}] )),
%% 	Global:set_value("classifier",erlv8_object:new([{"major",?VALUE(major_classifier)}] )),
	Global:set_value("classifier",erlv8_object:new([{"warning",?VALUE(warning_classifier)}] )),
%% 	Global:set_value("classifier",erlv8_object:new([{"minor",?VALUE(minor_classifier)}] )),
	Global:set_value("classifier",erlv8_object:new([{"ok",?VALUE(ok_classifier)}] )),
	{ok,Error} = erlv8_vm:run(VM,"classifier.error()"),
%% 	{ok,Major} = erlv8_vm:run(VM,"classifier.major()"),
	{ok,Warning} = erlv8_vm:run(VM,"classifier.warning()"),
%% 	{ok,Minor} = erlv8_vm:run(VM,"classifier.minor()"),
	{ok,OK} = erlv8_vm:run(VM,"classifier.ok()"),
	if Error -> ?SETVALUE(?CATEGORY,error);
%% 	   Major -> ?SETVALUE(?CATEGORY,major);
	   Warning -> ?SETVALUE(?CATEGORY,warning);
%% 	   Minor -> ?SETVALUE(?CATEGORY,minor);
	   OK -> ?SETVALUE(?CATEGORY,ok);
	   true -> un_classified
	end,
	ok.
>>>>>>> pre_classifier_version

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				object:add_fact(Name,{wakeup}),
				X;
		_ -> atom_to_list(Name) ++ " not available, choose a new name"
	end.