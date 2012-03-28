%% Author: cxy
%% Created: 2012-3-23
%% Description: TODO: Add description to machineobj
-module (machineobj).
-compile(export_all).
-include("../../include/object.hrl").

extends () -> nil .

?ACTION(start) -> {wakeup_event,init};
?ACTION(running) -> {wakeup_event,update}. 
?EVENT(wakeup_event)-> {eresye,wakeupPattern}. 
?PATTERN(wakeupPattern)-> {?VALUE(name), get, {wakeup}}.

init(Self,EventType,Pattern,State) ->
	io:format ( "[~w:~w]:Action=init,State=~w,Event=~w,Pattern=~w,State=~w\n",[?MODULE,?VALUE(name),State,EventType,Pattern,State]),
	object:do(Self,running).

update(Self,EventType,Pattern,State) ->
	io:format ( "[~w:~w]:Action=update,State=~w,Event=~w,Pattern=~w,State=~w\n",[?MODULE,?VALUE(name),State,EventType,Pattern,State]),
	object:do(Self,start).

machineobj (Self,Name) ->
	?SETVALUE(id,Name),
	?SETVALUE(name,Name),
	?SETVALUE(host,""),
	?SETVALUE(login,""),
	?SETVALUE(passwd,""),
	?SETVALUE(trace,0),
	?SETVALUE(os,"nt"),
	?SETVALUE(status,"unknown"),
	?SETVALUE(method,""),
	?SETVALUE(prompt,"#"),
	?SETVALUE(loginprom,"login"),
	?SETVALUE(passwdprom,"password"),
	?SETVALUE(secondprom,""),
	?SETVALUE(secondresp,""),
	?SETVALUE(initshell,""),
	?SETVALUE(remoteencoding,""),
	?SETVALUE(sshcommand,""),
	?SETVALUE(sshclient,"interJavalib"),
	?SETVALUE(sshport,22),
	?SETVALUE(disableconncaching,""),
	?SETVALUE(connlimit,3),
	?SETVALUE(version,""),
	?SETVALUE(keyfile,""),
	?SETVALUE(sshauthmethod,""),
	?SETVALUE(label,""),
	?SETVALUE(total,0),	
	?SETVALUE(type,"SERVER"),
	?SETVALUE(other,[]),
	?SETVALUE(pwdmode,"other"),
	eresye:start(Name).

machineobj_(Self)-> 
%% 	io:format("machineobj destructor."),
	eresye:stop(?VALUE(name)).

get_resource_type() -> ?MODULE.

print(Self,P)  ->
	io:format("X:~w,Y:~w,P:~s~n", [object:get(Self,'X'),object:get(Self,'Y'),P]).

print(Self)  ->
	io:format("printing in [~w:~w]~n",[?MODULE,?VALUE(name)]).

on_starting(Self) ->
	io:format("This [~w] ~w object is starting \n",[?VALUE(name),?MODULE]).

on_stopping(Self) ->
	io:format("This [~w] ~w object is stopping \n",[?VALUE(name),?MODULE]).

start(Name) ->
	case object:get_by_name(Name) of
		[] -> 
				X = object:new(?MODULE,[Name]),
				object:start(X),
				eresye:assert(Name,{wakeup}),
				X;
		X -> {existed, X, atom_to_list(Name) ++ " already existed, choose a new name"}
	end.
