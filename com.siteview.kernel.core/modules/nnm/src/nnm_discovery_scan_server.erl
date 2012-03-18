-module(nnm_discovery_scan_server).

-behaviour(gen_server).
-export([start_link/0,start_scan/5,stop/0,snmp_ping/3]).
%% gen_server callbacks
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
terminate/2, code_change/3]).

-define(SCANER_THREAD,10).
-define(SERVER,   'nnm_scan_server').

-record(state, {scan_state,scan_result}).

start_link() -> gen_server:start_link({local, ?MODULE}, ?MODULE, [], []).

init([]) -> {ok, #state{}}.

start_scan(Params,Threads,ProfileID,OidList,SysObjectIDList) ->
	start_link(),
	gen_server:call(?MODULE,{start_scan,Params,Threads,ProfileID,OidList,SysObjectIDList},600000).

snmp_ping(Params,Threads,OidList) ->
	start_link(),
	gen_server:call(?MODULE, {snmp_ping,Params,Threads,OidList}, 600000).

stop() -> 
	gen_server:call(?MODULE, stop).

handle_call({start_scan,Params,Threads,ProfileID,OidList,SysObjectIDList},_From,Tab) ->
	Reply = scaner_manager(Params,Threads,ProfileID,OidList,SysObjectIDList,[],0),
	%%io:format("Reply"),
	{reply,Reply,Tab};
handle_call({snmp_ping,Params,Threads,OidList},_From,Tab) ->
	Reply = snmp_ping_manager(Params,Threads,OidList,{[],[]},0),
	{reply,Reply,Tab};
handle_call(stop, _From, Tab) ->
	io:format("stop"),
	{stop, normal, stopped, Tab}.

handle_cast(_Msg, State) -> {noreply, State}.
handle_info(_Info, State) -> {noreply, State}.
terminate(_Reason, _State) -> ok.
code_change(_OldVsn, State, Extra) -> {ok, State}.

snmp_ping_manager([],_,_,{PCList,NetWorkList},0) -> {PCList,NetWorkList};
snmp_ping_manager([],Threads,OidList,{PCList,NetWorkList},C) ->
	receive
		{ping_done,Why} ->
			snmp_ping_manager([],Threads,OidList,{PCList,NetWorkList},C-1);
		{ping_result_pc,PC} ->
			snmp_ping_manager([],Threads,OidList,{[PC|PCList],NetWorkList},C);
		{ping_result_netWork,NetWork} ->
			snmp_ping_manager([],Threads,OidList,{PCList,[NetWork|NetWorkList]},C);
		_ ->
			snmp_ping_manager([],Threads,OidList,{PCList,NetWorkList},C)
	end;
snmp_ping_manager([H|T],Threads,OidList,{PCList,NetWorkList},C) ->
	receive
		{ping_done,Why} ->
			start_ping(H,OidList),
			snmp_ping_manager(T,Threads,OidList,{PCList,NetWorkList},C);
		{ping_result_pc,PC} ->
			snmp_ping_manager([H]++T,Threads,OidList,{[PC|PCList],NetWorkList},C);
		{ping_result_netWork,NetWork} ->
			snmp_ping_manager([H]++T,Threads,OidList,{PCList,[NetWork|NetWorkList]},C);
		_ ->
			snmp_ping_manager([H]++T,Threads,OidList,{PCList,NetWorkList},C)
	after 40 ->
		Max = case Threads of
				Scount when is_integer(Scount)->
					Scount;
				_->
					?SCANER_THREAD
			end,
		if
			C < Max ->
					start_ping(H,OidList),
					snmp_ping_manager(T,Threads,OidList,{PCList,NetWorkList},C+1);
				true->
					snmp_ping_manager([H]++T,Threads,OidList,{PCList,NetWorkList},C)
		end
	end.

start_ping(Param,OidList) ->
	ParentId = self(),
	Pid = spawn(fun()->do_ping_host(Param,OidList,ParentId) end),
	on_ping_exit(Pid,self()).

do_ping_host(Param,OidList,ParentId) ->
	Result = nnm_snmpDG:snmpPing(Param,OidList,"sysObjectID"),
	case Result of
		{error,_} ->
			ParentId ! {ping_result_pc,Param};
		{ok,_} ->
			ParentId ! {ping_result_netWork,Param}
	end.

on_ping_exit(Pid,ParentId) ->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						%%io:format("Ping Pid:~p,Exit:~p~n",[Pid,Why]),
						ParentId ! {ping_done,Why}
				end
				end).

scaner_manager([],_,_,_,_,Result,0) -> Result;
scaner_manager([],Threads,ProfileID,OidList,SysObjectIDList,Result,C)->
	receive
		{scan_done,Why}->
			%%io:format("C:~p~n",[C]),
			scaner_manager([],Threads,ProfileID,OidList,SysObjectIDList,Result,C-1);
		{scan_result,Value} ->
			scaner_manager([],Threads,ProfileID,OidList,SysObjectIDList,[Value|Result],C);
		_->
			scaner_manager([],Threads,ProfileID,OidList,SysObjectIDList,Result,C)
	end;
scaner_manager([H|T],Threads,ProfileID,OidList,SysObjectIDList,Result,C)->
	receive
		{scan_done,Why}->
			start_scaner(H,ProfileID,OidList,SysObjectIDList),
			scaner_manager(T,Threads,ProfileID,OidList,SysObjectIDList,Result,C);
		{scan_result,Value} ->
			scaner_manager([H]++T,Threads,ProfileID,OidList,SysObjectIDList,[Value|Result],C);
		_->
			scaner_manager([H]++T,Threads,ProfileID,OidList,SysObjectIDList,Result,C)
	after 40->
		Max = case Threads of
				Scount when is_integer(Scount)->
					Scount;
				_->
					?SCANER_THREAD
			end,
		if
			C < Max -> % limit max scaner
				start_scaner(H,ProfileID,OidList,SysObjectIDList),
				scaner_manager(T,Threads,ProfileID,OidList,SysObjectIDList,Result,C+1);
			true->
				scaner_manager([H]++T,Threads,ProfileID,OidList,SysObjectIDList,Result,C)
		end
	end.
	
start_scaner(Param,ProfileID,OidList,SysObjectIDList)->
	ParentId = self(),
	Pid = spawn(fun()->do_scan_host(Param,ProfileID,OidList,SysObjectIDList,ParentId) end),
	on_scaner_exit(Pid,self()).
	
do_scan_host(Param,ProfileID,OidList,SysObjectIDList,ParentId)->
	Result = nnm_discovery_scan:getScanInfo(Param,ProfileID,OidList,SysObjectIDList),
	ParentId!{scan_result,Result}.


on_scaner_exit(Pid,ParentId)->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						%%io:format("scaner Pid:~p,Exit:~p~n",[Pid,Why]),
						ParentId ! {scan_done,Why}
				end
				end).
	

