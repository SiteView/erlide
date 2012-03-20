-module(layout).

-compile(export_all).


test(Node)->
	rpc:call(Node, layout,do,[['1300:166480:547003','1300:166479:516007'],[{'1300:166480:547003','1300:166479:516007'}],circular,{32.0, 32.0},[]]).

