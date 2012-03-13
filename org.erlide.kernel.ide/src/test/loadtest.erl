-module(loadtest).
-compile(export_all).

extends () -> nil .

action(Self,start) -> {silent_evt,do_action}.
event(Self,silent_evt) -> {silent,nil }.

loadtest ( Self,Name ) ->
 	object:start ( Self ),
	object : set ( Self , 'name' , Name),
	eresye:start(Name).

loadtest_(Self)->
	io:format("Name:~w~n", [object:get(Self,name)]),
	eresye:stop(object:get(Self,name)).

do_action(Self, _, _, _) ->
 	C = object:get(Self,counter),
	ping_monitor:start(list_to_atom("monitor" ++ integer_to_list(C))),
	timer:sleep(100),
%% 	atomic_monitor:test(list_to_atom("monitor" ++ integer_to_list(C))),
%% 	base_monitor:test(list_to_atom("monitor" ++ integer_to_list(C))),
%% 	P = point:test(list_to_atom("point" ++ integer_to_list(C))),
%% 	object:set(P, 'X', C * random:uniform(10)),object:set(P, 'Y', C * random:uniform(10)),
%% 	relativepoint:test(list_to_atom("relativepoint" ++ integer_to_list(C))),
 	io:format("[~w ] step ~w\n",[Self, C]),
	Max = object:get(Self,max),
	Start = object:get(Self,start),
 if
 	C < Max-Start -> object:set(Self,counter, C+1) ,
 								object : do ( Self , start );
 	true -> object : stop ( Self )
 end .

start(Name,CounterStart,Max) ->
	case object:isValidName(Name) of
		false -> X = object:new(?MODULE,[Name]),
				Start = erlang:now(),
				object:set(X,max,Max),
 				object:set ( X , counter , CounterStart) ,
				 object:set ( X , start , CounterStart) ,
				object:start(X),
				%% 	io:format("~w~n_", [object:getAttributes(X)]),
%% 				eresye:assert(Mind,{wakeup}),
 				io:format("Waiting ...\n"),
				object:join(X),
				io:format(" total number of objects:~w in ~w seconds.\n",[length(object:get_all()),timer:now_diff(erlang:now(), Start)/1000000]),
%% 				object:delete (X );
				X;					 
		true -> atom_to_list(Name) ++ " already exist, choose a new name"
	end.