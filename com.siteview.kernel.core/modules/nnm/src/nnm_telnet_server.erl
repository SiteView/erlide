-module(nnm_telnet_server).

-behaviour(gen_server).
-export([start_link/0]).
%% gen_server callbacks
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
terminate/2, code_change/3]).

-export([connect/1,send/2,send/3,close/1]).

-define(SERVER,nnm_telnet_server).
-define(TIMEOUT,5000).
-define(PORT,23).

-record(state, {ip, port, user, passwd, superUser, superPasswd, userPrompt, passwdPrompt, prompt, superPrompt, timeout, status, socket}).

start_link() -> gen_server:start_link({local, ?SERVER}, ?MODULE, [], []).

init([]) -> 
	{ok, []}.

connect(Param) ->
	start_link(),
	ParentPid = self(),
	case gen_server:call(?SERVER, {connect,Param,ParentPid}, ?TIMEOUT*2) of
		{ok,ReqId} ->
			io:format("self:~p~n,ReqId:~p~n",[self(),ReqId]),
			receive
				{error,Reason} ->
					erlang:exit(ReqId, Reason),
					{error,Reason};
				{ReqId,_} ->
					{ok,ReqId}
			after ?TIMEOUT * 2 ->
					{error,timeout}
			end;
		{error,Reason} ->
			{error,Reason}
	end.

send(ReqId,Cmd) -> 
	gen_server:call(?SERVER, {send,ReqId,Cmd}, ?TIMEOUT*2).

send(ReqId,Cmd,Prompt) ->
	gen_server:call(?SERVER, {send,ReqId,Cmd,Prompt}, ?TIMEOUT*2).
													
close(ReqId) -> 
	gen_server:call(?SERVER,{close,ReqId}, ?TIMEOUT*2).

handle_call({connect,Param,ParentPid}, _From, List) ->
	Ip = proplists:get_value(ip,Param),
	Port = proplists:get_value(port,Param,?PORT),
	User = proplists:get_value(user,Param,""),
	Passwd = proplists:get_value(passwd,Param,""),
	SuperUser = proplists:get_value(superUser,Param,""),
	SuperPasswd = proplists:get_value(superPasswd,Param,""),
	UserPrompt = proplists:get_value(userPrompt,Param,"ogin:"),
	PasswdPrompt = proplists:get_value(passwdPrompt,Param,"assword:"),
	Prompt = proplists:get_value(prompt,Param,""),
	SuperPrompt = proplists:get_value(superPrompt,Param,""),
	Timeout = proplists:get_value(timeout,Param,?TIMEOUT),
	
	State = #state{ip=Ip,port=Port,user=User,passwd=Passwd,superUser=SuperUser,superPasswd=SuperPasswd,
				   userPrompt=UserPrompt,passwdPrompt=PasswdPrompt,prompt=Prompt,superPrompt=SuperPrompt,
				   timeout=Timeout,status=init},
	io:format("State:~p~n",[State]),
	Pid = spawn(fun() -> connectToServer(State,ParentPid) end),
	on_exit(Pid),
	{reply,{ok,Pid},List};
handle_call({send,ReqId,Cmd}, _From, List) ->
	ReqId ! {send,self(),Cmd},
	Result = 
		receive
			R ->
				R
		end,
	io:format("Result:~p~n",[Result]),
	{reply,Result,List};
handle_call({send,ReqId,Cmd,Prompt}, _From, List) ->
	ReqId ! {send,self(),Cmd,Prompt},
	Result = 
		receive
			R ->
				R
		end,
	{reply,Result,List};
handle_call({close,ReqId}, _From, List) ->
	ReqId ! {close,self()},
	Result = 
		receive
			R ->
				R
		end,
	{reply,Result,List}.

handle_cast(_Msg, List) -> {noreply, List}.
handle_info(_Info, List) -> {noreply, List}.
terminate(_Reason, _List) -> ok.
code_change(_OldVsn, List, _Extra) -> {ok, List}.


connectToServer(State,ParentPid) ->
	NState = 
		case State#state.status of
			init ->		
				case gen_tcp:connect(State#state.ip,State#state.port,[list, {packet, raw}, {active, false}]) of
					{ok,Socket} ->
						case loginServer(State,Socket) of
							{ok,S} ->
								Pid = self(),
								io:format("ParentPid:~p~n,Pid:~p~n",[ParentPid,Pid]),
								ParentPid ! {Pid,S},
								S#state{socket=Socket};
							{error,Reason} ->
								ParentPid ! {error,Reason},
								State
						end;
					{error,Reason} ->
						ParentPid ! {error,Reason},
						State
				end;
			_ ->
				State
	end,
	receive
		{send,From,Cmd} ->
			Result = executeCommand(NState,Cmd),
			From ! Result,
			connectToServer(NState,ParentPid);
		{send,From,Cmd,Prompt} ->
			Result = executeCommand(NState,Cmd,Prompt),
			From ! Result,
			connectToServer(NState,ParentPid);
		{close,From} ->
			gen_tcp:close(NState#state.socket),
			From ! {ok,close}
	end.
	
loginServer(State,Socket) ->
	case gen_tcp:recv(Socket, 0, State#state.timeout) of
		{ok, List} ->
			io:format("Status:~p~n",[State#state.status]),
			io:format("List:~p~n",[List]),
%% 			Data1 = parse_iac(Data ++ List, "", Socket),
			Status = loginMatch(State,List),
			case Status of
				user ->
					gen_tcp:send(Socket, State#state.user ++ "\r\n"),
					loginServer(State#state{status=user},Socket);
				passwd ->
					gen_tcp:send(Socket, State#state.passwd ++ "\r\n"),
					loginServer(State#state{status=passwd},Socket);
				superUser ->
					case State#state.superUser of
						"" ->
							{ok,State#state{status=superUser}};
						_ ->
							gen_tcp:send(Socket, State#state.superUser ++ "\r\n"),
							loginServer(State#state{status=superUser},Socket)
					end;
				superPasswd ->
					gen_tcp:send(Socket, State#state.superPasswd ++ "\r\n"),
					loginServer(State#state{status=superPasswd},Socket);
				success ->
					{ok,State#state{status=success}};
				goon ->
					loginServer(State,Socket);
				'prompt error' ->
					{error,'prompt error'};
				_ ->
					{error,'login fail'}
			end;
		{error,Reason} ->
			{error,Reason}
	end.

loginMatch(State,List) ->
	case regexp:match(List, State#state.userPrompt ++ " *$") of
		{match,_,_} ->
			case State#state.status of
				init ->
					user;
				_ ->
					fail
			end;
		nomatch ->
			case regexp:match(List, State#state.passwdPrompt ++ " *$") of
				{match,_,_} ->
					case State#state.status of
						init ->
							passwd;
						user ->
							passwd;
						superUser ->
							superPasswd;
						_ ->
							fail
					end;
				nomatch ->
					case regexp:match(List, State#state.prompt ++ " *$") of
						{match,_,_} ->
							case State#state.status of
								passwd ->
									superUser;
								superPasswd ->
									success;
								_ ->
									fail
							end;
						nomatch ->
							case regexp:match(List, State#state.superPrompt ++ " *$") of
								{match,_,_} ->
									case State#state.status of
										superPasswd ->
											success;
										_ ->
											fail
									end;
								nomatch ->
									goon;
								_ ->
									'prompt error'
							end;
						_ ->
							'prompt error'
					end;
				_ ->
					'prompt error'
			end;
		_ ->
			'prompt error'
	end.



executeCommand(State,Cmd) ->
	Socket = State#state.socket,
	Prompt = 
		case State#state.superPrompt of
			"" ->
				State#state.prompt ++ " *$";
			_ ->
				State#state.superPrompt ++ " *$"
		end,
	Timeout = State#state.timeout,
	io:format("socket2:~p~n",[Socket]),
	gen_tcp:send(Socket, Cmd ++ " \r\n"),
	getCmdResult(Socket,Prompt,Timeout,"").

getCmdResult(Socket,Prompt,Timeout,Data) ->
	case gen_tcp:recv(Socket, 0, Timeout) of
		{ok,List} ->
			io:format("RList:~p~n",[List]),
			case regexp:match(List, Prompt ++ " *$") of
				nomatch ->
					case regexp:match(List, "\n") of
						{match,_,_} ->
							gen_tcp:send(Socket, " "),
							getCmdResult(Socket,Prompt,Timeout,string:concat(Data, List));
						_ ->
							getCmdResult(Socket,Prompt,Timeout,string:concat(Data, List))
					end;
				{match,_,_} ->
					Result = string:concat(Data, List),
					io:format("Data:~p~n",[Result]),
					Start = string:str(Result, "\n"),
					if 
						Start =:= 0 ->
							{error,'result error'};
						true ->
							End = string:rstr(Result, "\n"),
							if
								End =:= 0 ->
									{error,'result error'};
								true ->
									{ok, string:substr(Result, Start+1, End-Start)}
							end
					end;
				_ ->
					{error,'prompt error'}
			end;
		{error,Reason} ->
			{error,Reason}
	end.

executeCommand(State,Cmd,Prompt) ->
	Socket = State#state.socket,
	Timeout = State#state.timeout,
	io:format("socket2:~p~n",[Socket]),
	gen_tcp:send(Socket, Cmd ++ " \r\n"),
	getCmdResult(Socket,Prompt,Timeout,"").


on_exit(Pid) ->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						io:format("why:~p~n",[Why])
				end
				end).
	

















