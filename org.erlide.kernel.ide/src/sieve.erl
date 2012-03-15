-module(sieve).
-compile ([export_all]).

remove_multiple ( Engine , {X} , {Y}) when ((X rem Y ) == 0) and (X =/= Y)->
	eresye : retract ( Engine , {X }).

final_rule ( Engine , {is , started } = X)
 when not ["{is , finished }"]; true ->
 eresye : retract ( Engine , X),
 eresye : assert ( Engine , {is , finished }).

start () ->
 eresye:start(sieve),
 eresye:assert(sieve,[{X}||X <- lists:seq(2,100)]),
 eresye:assert(sieve, {is,started}),
 eresye:add_rule(sieve,{sieve,remove_multiple},2),
 eresye:add_rule(sieve,{sieve,final_rule},1),
 eresye:wait_and_retract( sieve , { is , finished }),
 io:format("~p~n",[eresye:get_kb(sieve)]) ,
 eresye:stop(sieve),
 ok.