-module(erlide_kernel_ide).

-export([
		 init/0
		]).

init() ->	
	erlide_log:log("*******************erlide_kernel_ide*************************"),
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

ecc_init() ->	
	erlide_log:log("*******************erlide_kernel_ide begin*************************"),
	erlide_scanner_listener:start(),
	erlide_log:log("*******************erlide_scanner_listener*************************"),
	object:start(),
	erlide_log:log("*******************object*************************"),
	content_store:start(), %TODO: set the db directory
	erlide_log:log("*******************content_store*************************"),
	application:start(quickstart_mochiweb),
	erlide_log:log("*******************quickstart_mochiweb*************************"),
	application:start(svecc),
	erlide_log:log("*******************svecc*************************"),
	application:start(crypto),
	erlide_log:log("*******************crypto*************************"),
	application:start(ssh),
	erlide_log:log("*******************ssh*************************"),
	application:start(gettext),
	erlide_log:log("*******************gettext*************************"),	
%% 	extension_sup:start(),
%% 	nnm_start:start(),
%% 	erlide_log:log("*******************nnm_start*************************"),
%% 	wmic:start(),
%% 	erlide_log:log("*******************wmic*************************"),
	gettext:recreate_db(),
	erlide_log:log("*******************recreate_db*************************"),
%% 	license:start(),
%% 	erlide_log:log("*******************license*************************"),
	%%TODO: start java-node seperately
	
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

