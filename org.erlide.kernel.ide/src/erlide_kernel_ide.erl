-module(erlide_kernel_ide).

-export([
		 init/0
		]).

init() ->	
	spawn(fun()->
				  erlide_scanner_listener:start(),
				  object:start(),
				  content_store:start(), %TODO: set the db directory
				  application:start(quickstart_mochiweb),
				  application:start(svecc),
				  application:start(crypto),
				  application:start(ssh),
				  application:start(gettext),
				  gettext:recreate_db(),
				  license:start(),	
				  wmic:start(),
				  extension_sup:start(),
				  nnm_start:start(),
				  %%TODO: start java-node seperately
				  ok
		  end),
	ok.

init1(L) when is_list(L) ->
	[init1(X) || X<-L];
init1(execute) ->
	ok;
init1(ide) ->
	ok;
init1(build) ->
	erlide_xref:start(),
	ok;
init1(monitor) ->
	%watch_eclipse(node(JRex)),
	ok.	

