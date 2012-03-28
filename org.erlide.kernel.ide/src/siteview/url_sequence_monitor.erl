-module (url_sequence_monitor).
-compile(export_all).
-include("../../include/object.hrl").
-include("../../include/monitor.hrl").

extends () -> atomic_monitor.

?SUPERCLAUSE(action).
?SUPERCLAUSE(event).
?SUPERCLAUSE(pattern).

url_sequence_monitor(Self, Name)->
	?SETVALUE(referenceType1,[]),
	?SETVALUE(reference1,[]),
	?SETVALUE(encoding1,[]),
	?SETVALUE(contentMatch1,[]),
	?SETVALUE(errorContent1,[]),
	?SETVALUE(userName1,[]),
	?SETVALUE(password1,[]),
	?SETVALUE(domain1,[]),
	?SETVALUE(whenToAuthenticate1,[]),
	?SETVALUE(stepDelay1,0),
	?SETVALUE(stepName1,[]),
	?SETVALUE(postData1,[]),
	?SETVALUE(encodePostData1,[]),
	?SETVALUE(referenceType2,[]),
	?SETVALUE(reference2,[]),
	?SETVALUE(encoding2,[]),
	?SETVALUE(contentMatch2,[]),
	?SETVALUE(errorContent2,[]),
	?SETVALUE(userName2,[]),
	?SETVALUE(password2,[]),
	?SETVALUE(domain2,[]),
	?SETVALUE(whenToAuthenticate2,[]),
	?SETVALUE(stepDelay2,0),
	?SETVALUE(stepName2,[]),
	?SETVALUE(postData2,[]),
	?SETVALUE(encodePostData2,[]),
	?SETVALUE(referenceType3,[]),
	?SETVALUE(reference3,[]),
	?SETVALUE(encoding3,[]),
	?SETVALUE(contentMatch3,[]),
	?SETVALUE(errorContent3,[]),
	?SETVALUE(userName3,[]),
	?SETVALUE(password3,[]),
	?SETVALUE(domain3,[]),
	?SETVALUE(whenToAuthenticate3,[]),
	?SETVALUE(stepDelay3,0),
	?SETVALUE(stepName3,[]),
	?SETVALUE(postData3,[]),
	?SETVALUE(encodePostData3,[]),
	?SETVALUE(referenceType4,[]),
	?SETVALUE(reference4,[]),
	?SETVALUE(encoding4,[]),
	?SETVALUE(contentMatch4,[]),
	?SETVALUE(errorContent4,[]),
	?SETVALUE(userName4,[]),
	?SETVALUE(password4,[]),
	?SETVALUE(domain4,[]),
	?SETVALUE(whenToAuthenticate4,[]),
	?SETVALUE(stepDelay4,0),
	?SETVALUE(stepName4,[]),
	?SETVALUE(postData4,[]),
	?SETVALUE(encodePostData4,[]),
	?SETVALUE(referenceType5,[]),
	?SETVALUE(reference5,[]),
	?SETVALUE(encoding5,[]),
	?SETVALUE(contentMatch5,[]),
	?SETVALUE(errorContent5,[]),
	?SETVALUE(userName5,[]),
	?SETVALUE(password5,[]),
	?SETVALUE(domain5,[]),
	?SETVALUE(whenToAuthenticate5,[]),
	?SETVALUE(stepDelay5,0),
	?SETVALUE(stepName5,[]),
	?SETVALUE(postData5,[]),
	?SETVALUE(encodePostData5,[]),
	?SETVALUE(referenceType6,[]),
	?SETVALUE(reference6,[]),
	?SETVALUE(encoding6,[]),
	?SETVALUE(contentMatch6,[]),
	?SETVALUE(errorContent6,[]),
	?SETVALUE(userName6,[]),
	?SETVALUE(password6,[]),
	?SETVALUE(domain6,[]),
	?SETVALUE(whenToAuthenticate6,[]),
	?SETVALUE(stepDelay6,0),
	?SETVALUE(stepName6,[]),
	?SETVALUE(postData6,[]),
	?SETVALUE(encodePostData6,[]),
	?SETVALUE(referenceType7,[]),
	?SETVALUE(reference7,[]),
	?SETVALUE(encoding7,[]),
	?SETVALUE(contentMatch7,[]),
	?SETVALUE(errorContent7,[]),
	?SETVALUE(userName7,[]),
	?SETVALUE(password7,[]),
	?SETVALUE(domain7,[]),
	?SETVALUE(whenToAuthenticate7,[]),
	?SETVALUE(stepDelay7,0),
	?SETVALUE(stepName7,[]),
	?SETVALUE(postData7,[]),
	?SETVALUE(encodePostData7,[]),
	?SETVALUE(referenceType8,[]),
	?SETVALUE(reference8,[]),
	?SETVALUE(encoding8,[]),
	?SETVALUE(contentMatch8,[]),
	?SETVALUE(errorContent8,[]),
	?SETVALUE(userName8,[]),
	?SETVALUE(password8,[]),
	?SETVALUE(domain8,[]),
	?SETVALUE(whenToAuthenticate8,[]),
	?SETVALUE(stepDelay8,0),
	?SETVALUE(stepName8,[]),
	?SETVALUE(postData8,[]),
	?SETVALUE(encodePostData8,[]),
	?SETVALUE(referenceType9,[]),
	?SETVALUE(reference9,[]),
	?SETVALUE(encoding9,[]),
	?SETVALUE(contentMatch9,[]),
	?SETVALUE(errorContent9,[]),
	?SETVALUE(userName9,[]),
	?SETVALUE(password9,[]),
	?SETVALUE(domain9,[]),
	?SETVALUE(whenToAuthenticate9,[]),
	?SETVALUE(stepDelay9,0),
	?SETVALUE(stepName9,[]),
	?SETVALUE(postData9,[]),
	?SETVALUE(encodePostData9,[]),
	?SETVALUE(referenceType10,[]),
	?SETVALUE(reference10,[]),
	?SETVALUE(encoding10,[]),
	?SETVALUE(contentMatch10,[]),
	?SETVALUE(errorContent10,[]),
	?SETVALUE(userName10,[]),
	?SETVALUE(password10,[]),
	?SETVALUE(domain10,[]),
	?SETVALUE(whenToAuthenticate10,[]),
	?SETVALUE(stepDelay10,0),
	?SETVALUE(stepName10,[]),
	?SETVALUE(postData10,[]),
	?SETVALUE(encodePostData10,[]),
	?SETVALUE(referenceType11,[]),
	?SETVALUE(reference11,[]),
	?SETVALUE(encoding11,[]),
	?SETVALUE(contentMatch11,[]),
	?SETVALUE(errorContent11,[]),
	?SETVALUE(userName11,[]),
	?SETVALUE(password11,[]),
	?SETVALUE(domain11,[]),
	?SETVALUE(whenToAuthenticate11,[]),
	?SETVALUE(stepDelay11,0),
	?SETVALUE(stepName11,[]),
	?SETVALUE(postData11,[]),
	?SETVALUE(encodePostData11,[]),
	?SETVALUE(referenceType12,[]),
	?SETVALUE(reference12,[]),
	?SETVALUE(encoding12,[]),
	?SETVALUE(contentMatch12,[]),
	?SETVALUE(errorContent12,[]),
	?SETVALUE(userName12,[]),
	?SETVALUE(password12,[]),
	?SETVALUE(domain12,[]),
	?SETVALUE(whenToAuthenticate12,[]),
	?SETVALUE(stepDelay12,0),
	?SETVALUE(stepName12,[]),
	?SETVALUE(postData12,[]),
	?SETVALUE(encodePostData12,[]),
	?SETVALUE(referenceType13,[]),
	?SETVALUE(reference13,[]),
	?SETVALUE(encoding13,[]),
	?SETVALUE(contentMatch13,[]),
	?SETVALUE(errorContent13,[]),
	?SETVALUE(userName13,[]),
	?SETVALUE(password13,[]),
	?SETVALUE(domain13,[]),
	?SETVALUE(whenToAuthenticate13,[]),
	?SETVALUE(stepDelay13,0),
	?SETVALUE(stepName13,[]),
	?SETVALUE(postData13,[]),
	?SETVALUE(encodePostData13,[]),
	?SETVALUE(referenceType14,[]),
	?SETVALUE(reference14,[]),
	?SETVALUE(encoding14,[]),
	?SETVALUE(contentMatch14,[]),
	?SETVALUE(errorContent14,[]),
	?SETVALUE(userName14,[]),
	?SETVALUE(password14,[]),
	?SETVALUE(domain14,[]),
	?SETVALUE(whenToAuthenticate14,[]),
	?SETVALUE(stepDelay14,0),
	?SETVALUE(stepName14,[]),
	?SETVALUE(postData14,[]),
	?SETVALUE(encodePostData14,[]),
	?SETVALUE(referenceType15,[]),
	?SETVALUE(reference15,[]),
	?SETVALUE(encoding15,[]),
	?SETVALUE(contentMatch15,[]),
	?SETVALUE(errorContent15,[]),
	?SETVALUE(userName15,[]),
	?SETVALUE(password15,[]),
	?SETVALUE(domain15,[]),
	?SETVALUE(whenToAuthenticate15,[]),
	?SETVALUE(stepDelay15,0),
	?SETVALUE(stepName15,[]),
	?SETVALUE(postData15,[]),
	?SETVALUE(encodePostData15,[]),
	?SETVALUE(referenceType16,[]),
	?SETVALUE(reference16,[]),
	?SETVALUE(encoding16,[]),
	?SETVALUE(contentMatch16,[]),
	?SETVALUE(errorContent16,[]),
	?SETVALUE(userName16,[]),
	?SETVALUE(password16,[]),
	?SETVALUE(domain16,[]),
	?SETVALUE(whenToAuthenticate16,[]),
	?SETVALUE(stepDelay16,0),
	?SETVALUE(stepName16,[]),
	?SETVALUE(postData16,[]),
	?SETVALUE(encodePostData16,[]),
	?SETVALUE(referenceType17,[]),
	?SETVALUE(reference17,[]),
	?SETVALUE(encoding17,[]),
	?SETVALUE(contentMatch17,[]),
	?SETVALUE(errorContent17,[]),
	?SETVALUE(userName17,[]),
	?SETVALUE(password17,[]),
	?SETVALUE(domain17,[]),
	?SETVALUE(whenToAuthenticate17,[]),
	?SETVALUE(stepDelay17,0),
	?SETVALUE(stepName17,[]),
	?SETVALUE(postData17,[]),
	?SETVALUE(encodePostData17,[]),
	?SETVALUE(referenceType18,[]),
	?SETVALUE(reference18,[]),
	?SETVALUE(encoding18,[]),
	?SETVALUE(contentMatch18,[]),
	?SETVALUE(errorContent18,[]),
	?SETVALUE(userName18,[]),
	?SETVALUE(password18,[]),
	?SETVALUE(domain18,[]),
	?SETVALUE(whenToAuthenticate18,[]),
	?SETVALUE(stepDelay18,0),
	?SETVALUE(stepName18,[]),
	?SETVALUE(postData18,[]),
	?SETVALUE(encodePostData18,[]),
	?SETVALUE(referenceType19,[]),
	?SETVALUE(reference19,[]),
	?SETVALUE(encoding19,[]),
	?SETVALUE(contentMatch19,[]),
	?SETVALUE(errorContent19,[]),
	?SETVALUE(userName19,[]),
	?SETVALUE(password19,[]),
	?SETVALUE(domain19,[]),
	?SETVALUE(whenToAuthenticate19,[]),
	?SETVALUE(stepDelay19,0),
	?SETVALUE(stepName19,[]),
	?SETVALUE(postData19,[]),
	?SETVALUE(encodePostData19,[]),
	?SETVALUE(referenceType20,[]),
	?SETVALUE(reference20,[]),
	?SETVALUE(encoding20,[]),
	?SETVALUE(contentMatch20,[]),
	?SETVALUE(errorContent20,[]),
	?SETVALUE(userName20,[]),
	?SETVALUE(password20,[]),
	?SETVALUE(domain20,[]),
	?SETVALUE(whenToAuthenticate20,[]),
	?SETVALUE(stepDelay20,0),
	?SETVALUE(stepName20,[]),
	?SETVALUE(postData20,[]),
	?SETVALUE(encodePostData20,[]),
	?SETVALUE(timeout,60),
	?SETVALUE(timeoutPerStep,[]),
	?SETVALUE(proxyUserName,[]),
	?SETVALUE(proxyPassword,[]),
	?SETVALUE(resumeStep,[]),
	?SETVALUE(resumeRemainingSteps,[]),
	?SETVALUE(httpVersion10,[]),
	?SETVALUE(acceptAllUntrustedCerts,[]),
	?SETVALUE(acceptInvalidCerts,[]),

	?SETVALUE(name,Name),
	object:super(Self, [Name]).

url_sequence_monitor_(Self)->eresye:stop(?VALUE(name)).

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
	?SETVALUE(status,[]),
	?SETVALUE(roundTripTime,[]),
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
