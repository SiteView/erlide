-module(nmsSession_NmsSession_I_impl).
-export([init/1,terminate/2,ping/0]).

init(_En) ->
	{ok,_En}.

terminate(_,_) ->
	ok.

ping() -> ok.