-module(beamjs_mod_sys).
-export([exports/1,init/1]).
-behaviour(beamjs_module).
-include_lib("../include/erlv8.hrl"). 

init(_VM) ->
	ok.

exports(_VM) ->
	?V8Obj([{"print", fun print/2},
			{"inspect", fun inspect/2},
			{"run_jseval", fun run_jseval/2}]).



print(#erlv8_fun_invocation{ vm = VM} = _Invocation, [Expr]) ->
	io:format("~s",[erlv8_vm:to_detail_string(VM,Expr)]),
%% 	io:format("~s",[Expr]),
	undefined.

inspect(#erlv8_fun_invocation{ vm = VM} = _Invocation, [Expr]) ->
	lists:flatten(io_lib:format("~s",[beamjs_js_formatter:format(VM, Expr)])).

run_jseval(#erlv8_fun_invocation{ vm = VM} = _Invocation, [Js]) ->
	Result = erlv8_vm:run(VM, erlv8_context:get(VM), Js, {"(command line)",0,0}),
%% 	io:format("jseval: ~p~n", [Result]),
	Result.
