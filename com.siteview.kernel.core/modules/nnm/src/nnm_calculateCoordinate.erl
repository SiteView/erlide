-module(nnm_calculateCoordinate).
-behaviour(gen_server).
-export([start_link/0]).
%% gen_server callbacks
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
terminate/2, code_change/3]).

-export([calculate/5]).

-include("nnm_define.hrl").

-define(SERVER, calculateCoordinate_server).
-define(TIMEOUT, 30000).
-define(LAYOUTCONF,"templates.nnm\\nnm.conf").

start_link() -> gen_server:start_link({local, ?SERVER}, ?MODULE, [], []).

init([]) -> 
	LayoutNode =
		case file:consult(?LAYOUTCONF) of
			{ok,File} -> 
				proplists:get_value(layoutNode,File);
			{error,_} -> 'layout@jiang'
		end,
	io:format("layout conf:~p~n",[LayoutNode]),
	
	nnm_cache:set(nnm_cache, nnm_calculateCoordinate, LayoutNode),
%% 	erlang:put(nnm_calculateCoordinate, LayoutNode),
	{ok, []}.

calculate(Point,Line,Shape,Size,Options) ->
%% 	start_link(),
	ParentPid = self(),
	case gen_server:call(?SERVER,{calculate,Point,Line,Shape,Size,Options,ParentPid},?TIMEOUT) of
		{ok,ReqId} ->
			receive
				{ReqId,Return} ->
					{ok,Return};
				{error,Reason} ->
					erlang:exit(ReqId, Reason),
					{error,Reason}
			after ?TIMEOUT ->
					{error,timeout}
			end;
		{error,Reason} ->
			{error,Reason}
	end.

handle_call({calculate,Point,Line,Shape,Size,Options,ParentPid}, _From,State) ->
	Pid = spawn(fun() -> do(Point,Line,Shape,Size,Options,ParentPid) end),
	on_exit(Pid),
	{reply,{ok,Pid},State}.

handle_cast(_Msg, State) -> {noreply, State}.
handle_info(_Info, State) -> {noreply, State}.
terminate(_Reason, _State) -> ok.
code_change(_OldVsn, State, _Extra) -> {ok, State}.

do(Point,Line,Shape,Size,Options,ParentPid) ->
	Pid = self(),
	io:format("layoutNode:~p~n",[nnm_cache:get(nnm_cache, nnm_calculateCoordinate)]),
	CoordinateDevice = 
		case rpc:call(nnm_cache:get(nnm_cache, nnm_calculateCoordinate),layout,do,[Point, Line, Shape, Size, Options]) of
			{ok,Result} ->
%% 				io:format("cResult:~p~n",[Result]),
				case proplists:get_value(devices,Result,[]) of
					[] -> 
						EPoint = proplists:get_value(exclude,Options,[]),
%% 						io:format("epoint:~p~n",[EPoint]),
						P1 = lists:subtract(Point, EPoint),
						P2 = [{X,{"0","0"}} || X <- P1],
						lists:append(EPoint,P2);
					L -> [{D,{nnm_discovery_util:floatToString(X,2),nnm_discovery_util:floatToString(Y,2)}} || {D,{X,Y}} <- L]
				end;
			_ ->
				EPoint = proplists:get_value(exclude,Options,[]),
				P1 = lists:subtract(Point, EPoint),
				P2 = [{X,{"0","0"}} || X <- P1],
				lists:append(EPoint,P2)
		end,
	ParentPid ! {Pid,CoordinateDevice}.

%% do(Point,Line,Shape,ParentPid) ->
%% 	Pid = self(),
%% 	Fn = string:sub_string(erlang:pid_to_list(Pid), 2),
%% 	FileName = string:substr(Fn, 1, length(Fn)-1),
%% 	{ok,Path} = file:get_cwd(),
%% 	writeFile(Point,FileName ++ "DeviceInfo.txt"),
%% 	writeFile(Line,FileName ++ "EdgeInfo.txt"),
%% 	Cmd = 
%% 		case Shape of
%% 			circular ->
%% 				Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/ComputeTopo " ++ FileName;
%% 			_ ->
%% 				Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/ComputeTopo " ++ FileName
%% 		end,
%% 	os:cmd(Cmd),
%% 	{ok,CoordinateDevice} = file:consult(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/"++ FileName ++"target.txt"),
%% 	file:delete(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/" ++ FileName ++ "DeviceInfo.txt"),
%% 	file:delete(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/" ++ FileName ++ "EdgeInfo.txt"),
%% 	file:delete(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/" ++ FileName ++ "target.txt"),
%% 	ParentPid ! {Pid,CoordinateDevice}.
%% 
%% writeFile(Data,FileName) ->
%% 	io:format("Data:~p~nFileName:~p~n",[Data,FileName]),
%% 	{ok,Path} = file:get_cwd(), 
%% 	{ok,File} = file:open(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/" ++ FileName, write),
%% 	lists:foreach(fun(X) -> io:format(File, "~p~n", [X]) end, Data),
%% 	file:close(File).

on_exit(Pid) ->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						io:format("why:~p~n",[Why])
				end
		  end).






	

