%% ---
%% rule
%%
%%---
-module(log).
-define(LOGDIR,"logs").
-compile(export_all).

get_datetime()->
	case erlang:localtime() of
		{{Y,M,D},{HH,MM,SS}}->
			lists:flatten(io_lib:format("~4.4.0w-~2.2.0w-~2.2.0w ~2.2.0w:~2.2.0w:~2.2.0w",[Y,M,D,HH,MM,SS]));
		_->
			""
	end.

trace(Module,Line,Msg)->
	case file:open(?LOGDIR ++ "/debug.log", [append]) of
		{ok,File}->
			io:format(File,"~s,~p(~p),~s~n",[get_datetime(),Module,Line,Msg]),
			file:close(File);
		_->
			ok
	end.

trace(Module,Line,Format,Params)->
	case file:open(?LOGDIR ++ "/debug.log", [append]) of
		{ok,File}->
			io:format(File,"~s,~p(~p),"++Format++"~n",[get_datetime(),Module,Line]++Params),
			file:close(File);
		_->
			ok
	end.	

error(Module,Line,Msg)->
	case file:open(?LOGDIR ++ "/error.log", [append]) of
		{ok,File}->
			io:format(File,"~s,~p(~p),~s~n",[get_datetime(),Module,Line,Msg]),
			file:close(File);
		_->
			ok
	end.

error(Module,Line,Format,Params)->
	case file:open(?LOGDIR ++ "/error.log", [append]) of
		{ok,File}->
			io:format(File,"~s,~p(~p),"++Format++"~n",[get_datetime(),Module,Line]++Params),
			file:close(File);
		_->
			ok
	end.

logdb(X,Y)->
	case file:open(?LOGDIR ++ "/dblog.log", [append]) of
		{ok,File}->
			io:format(File,get_datetime() ++ " : " ++ X, Y),
			file:close(File);
		_->
			ok
	end.
