%% Author: cxy
%% Created: 2012-3-2
%% Description: TODO: Add description to objtest
-module(objtest).

%%
%% Include files
%%

%%
%% Exported Functions
%%
-export([]).

%%
%% API Functions
%%

%% rr("../include/erlv8.hrl"), %% for use in shell only
-include_lib("../include/erlv8.hrl").
test()->
	{ok, VM} = erlv8_vm:start(),
	Global = erlv8_vm:global(VM),
	Global:set_value("greeting",  erlv8_object:new([{"English", fun (#erlv8_fun_invocation{}, []) ->
                                                                                                  "Hello!" end}])),
	erlv8_vm:run(VM,"greeting.English()").

test2()->
	{ok, VM} = erlv8_vm:start(),
	Global = erlv8_vm:global(VM),
	Global:set_value("greeting", erlv8_object:new([{"English", 
						       fun  (#erlv8_fun_invocation{}=Invocation, []) -> 
							    case Invocation:is_construct_call() of  
								    true ->
									 This = Invocation:this(),
									 This:set_value("greeting","Hello!");
								    false ->
									 {throw, {error, "This should always be used as a constructor!"}}
							    end
							   end}])),
	erlv8_vm:run(VM,"greeting.English()"), %% first run
	{ok, Obj} = erlv8_vm:run(VM,"new greeting.English()"), Obj:proplist(). %% second run

%%
%% Local Functions
%%

