-module(nnm_discovery_telnet_read,[TelnetParam,TelnetList]).
-compile(export_all).

-include("nnm_define.hrl").
-include("nnm_discovery_topo.hrl").

new(TelnetParam,TelnetList) ->
	{?MODULE,TelnetParam,TelnetList}.

telnetValue(TelnetId,Target) ->
	case proplists:get_value(Target,proplists:get_value(TelnetId,TelnetList,""),"") of
		"" ->
			[];
		Rule ->
			case nnm_telnet_server:connect(TelnetParam) of
				{error,_} ->
					[];
				{ok,Pid} ->
					Return = 
						case proplists:get_value(cmd,Rule,"") of
							"" ->
								[];
							Cmd ->
								case execmd(Cmd,Pid) of
									"" ->
										"";
									Result ->
										io:format("Result:~p~n",[Result]),
										io:format("Rule:~p~n",[Rule]),
										case proplists:get_value(type,Rule,"value") of
											"table" ->
												getTableValue(Rule,Result);
											_ ->
												getValue(Rule,Result)
										end
								end
						end,
					nnm_telnet_server:close(Pid),
					Return
			end
	end.

execmd([],_) -> "";
execmd([H|Cmd],Pid) ->
	case nnm_telnet_server:send(Pid,H) of
		{ok,Result} ->
			string:concat(Result, execmd(Cmd,Pid));
		{error,_} ->
			execmd(Cmd,Pid)
	end.
	

getTableValue(Rule,Result) ->
	StartLine = proplists:get_value(startLine,Rule,""),
	StartLineOffset = proplists:get_value(startLineOffset,Rule,"0"),
	EndLine = proplists:get_value(endLine,Rule,""),
	EndLineOffset = proplists:get_value(endLineOffset,Rule,"0"),
	TableColumns = proplists:get_value(tableColumns,Rule,"0"),
	TableSeparator = proplists:get_value(tableSeparator,Rule," "),
	TableFormula = proplists:get_value(tableFormula,Rule,""),
	
	R1 = string:tokens(Result, "\n"),
	R2 = getTableValue_start(R1,StartLine,erlang:list_to_integer(StartLineOffset)),
	io:format("R2:~p~n",[R2]),
	R3 = getTableValue_end(R2,EndLine,erlang:list_to_integer(EndLineOffset)),
	io:format("R3:~p~n",[R3]),
	getTableValue(R3,erlang:list_to_integer(TableColumns),TableSeparator,TableFormula).

getTableValue_start([],_,_) -> [];
getTableValue_start([H|T],StartLine,StartLineOffset) ->
	case regexp:match(H, StartLine) of
		{match,_,_} ->
			case StartLineOffset of
				0 -> [H|T];
				_ ->
					lists:sublist(T, StartLineOffset, length(T)-StartLineOffset+1)
			end;
		_ ->
			getTableValue_start(T,StartLine,StartLineOffset)
	end.

getTableValue_end([],_,_) -> [];
getTableValue_end(R,EndLine,EndLineOffset) ->
	V = lists:last(R),
	case regexp:match(V, EndLine) of
		{match,_,_} ->
			lists:sublist(R, 1, length(R)-EndLineOffset);
		_ ->
			getTableValue_end(R--V,EndLine,EndLineOffset)
	end.

getTableValue([],_,_,_) -> [];
getTableValue([H|T],TableColumns,TableSeparator,TableFormula) ->
	V = string:tokens(H, TableSeparator),
	case length(V) =:= TableColumns of
		true ->
			case TableFormula of
				"" ->
					[list_to_tuple(V)|getTableValue(T,TableColumns,TableSeparator,TableFormula)];
				_ ->
					OneLine = lists:map(fun(X) -> 
						   						if
							   						X > length(V) ->
								   						"";
							   						true ->
								   						string:strip(lists:nth(X, V), both, $\r)
						   						end
				   						end, TableFormula),
					[list_to_tuple(OneLine)|getTableValue(T,TableColumns,TableSeparator,TableFormula)]
			end;
		false ->
			getTableValue(T,TableColumns,TableSeparator,TableFormula)
	end.
	
	
getValue(Rule,Result) -> 
%% 	StartLine = proplists:get_value(startLine,Rule,""),
%% 	StartLineOffset = proplists:get_value(startLineOffset,Rule,"0"),
%% 	EndLine = proplists:get_value(endLine,Rule,""),
%% 	EndLineOffset = proplists:get_value(endLineOffset,Rule,"0"),
	ValueRegexp = proplists:get_value(valueRegexp,Rule,""),
	ValueRegexpRule = proplists:get_value(valueRegexpRule,Rule,[]),
	
	Return = nnm_discovery_util:matches(Result,ValueRegexp,ValueRegexpRule),
	io:format("return:~p~n",[Return]),
	case proplists:get_value(valueFormula,Rule,[]) of
		"" ->
			Return;
		ValueFormula ->
			getValueFormat(Return,ValueFormula)
	end.

getValueFormat([],_) -> [];
getValueFormat([H|T],Formula) ->
	V1 = erlang:tuple_to_list(H),
	V2 = lists:map(fun(X) -> 
						   if
							   X > length(V1) ->
								   "";
							   true ->
								   lists:nth(X, V1)
						   end
				   end, Formula),
	[list_to_tuple(V2)|getValueFormat(T,Formula)].
	
%% read aft table
%% result format: {portIndex,mac,state}
readAft(TelnetId) ->
	AftInfo = telnetValue(TelnetId,aftTable),
	io:format("AftInfo:~p~n",[AftInfo]),
	{proplists:get_value(ip,TelnetParam,""),[[{port,Port},{mac,Mac}] || {Port,Mac,_} <- AftInfo]}.
	

%% read arp table
%% result format: {ip,mac,vlanId,portDescr,type}
readArp(TelnetId) ->
	ArpInfo = telnetValue(TelnetId,arpTable),
	io:format("ArpInfo:~p~n",[ArpInfo]),
	{proplists:get_value(ip,TelnetParam,""),[[{ifIndex,PortDescr},{ip,Ip},{mac,Mac}] || {Ip,Mac,_,PortDescr,_} <- ArpInfo]}.

%% read interface table
%% result format: {portDescr,mac,state,mtu,speed}
readInterface(TelnetId) ->
	InterfaceInfo = telnetValue(TelnetId,interfaceTable),
	io:format("InterfaceInfo~p~n",[InterfaceInfo]).
					
%% read route table
%% result format: {ip,interface,type,nexthop}
readRoute(TelnetId) ->
	RouteInfo = telnetValue(TelnetId,routeTable),
	io:format("RouteInfo:~p~n",[RouteInfo]).
									
 			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
	