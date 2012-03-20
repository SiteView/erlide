-module(nnm_start).
-behaviour(application).
-export([start/0,start/2,stop/1]).

start() -> 
	application:load(nnm_start),
	application:start(nnm_start).

start(_Type, StartArgs) ->
	nnm_cache:create(nnm_cache),
	remoteMachineManager:start_link(),
	server_conf:start_link(),
	nnm_calculateCoordinate:start_link(),
 	nnm_start_sup:start_link(StartArgs).

stop(_State) ->
	ok.

	