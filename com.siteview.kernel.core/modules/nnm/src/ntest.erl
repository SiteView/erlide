-module(ntest).
-compile(export_all).
-include("../../core/include/monitor.hrl").

snmp_test(Ip,Community) ->
	IpList = nnm_discovery_util:getIpList(Ip),
	SnmpParam = [[{ip,X},{getCommunity,Community},{timeout,2000}] || X <- IpList],
	{ok,File} = file:open("c:/snp.txt", [append]),
	snmp_ping(SnmpParam,50,0,File).
%% 	lists:foreach(fun(X) -> 
%% 						  SnmpParam = [{ip,X},{getCommunity,Community},{timeout,2000}],
%% 						  io:format("~p:~p~n",[X,nnm_snmp_api:readOneValue(SnmpParam,[1,3,6,1,2,1,1,2])]) 
%% 				  end, IpList).

snmp_ping([],_,0,File) -> file:close(File);
snmp_ping([],Threads,C,File) ->
	receive
		{ping_done,_Why} ->
			snmp_ping([],Threads,C-1,File);
		_ ->
			snmp_ping([],Threads,C,File)
	end;
snmp_ping([H|T],Threads,C,File) ->
	receive
		{ping_done,_Why} ->
			start_snmp_ping(H,File),
			snmp_ping(T,Threads,C,File);
		_ ->
			snmp_ping([H|T],Threads,C,File)
	after 10 ->
		if
			C < Threads ->
					start_snmp_ping(H,File),
					snmp_ping(T,Threads,C+1,File);
				true->
					snmp_ping([H|T],Threads,C,File)
		end
	end.

start_snmp_ping(SnmpParam,File) ->
	Pid = spawn(fun()->do_snmp_ping(SnmpParam,File) end),
	on_ping_exit(Pid,self()).

do_snmp_ping(SnmpParam,File) ->
	case nnm_snmp_api:readOneValue(SnmpParam,[1,3,6,1,2,1,1,2]) of
		[] -> [];
		Value -> io:format(File,"~p,~p~n",[proplists:get_value(ip,SnmpParam),Value])
	end.
	
	

on_ping_exit(Pid,ParentId) ->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
%% 						io:format("Ping Pid:~p,Exit:~p~n",[Pid,Why]),
						ParentId ! {ping_done,Why}
				end
				end).

calculateCoordinate() ->
	EdgeInfo = 
		case api_nnm:topochart_get([]) of
			[] ->
				[];
			[R1|_] ->
				Id = proplists:get_value(id,R1),
				case api_nnm:topochart_get(Id) of
					[] -> [];
					R2 ->
						proplists:get_value(edge,R2,[])
				end
		end,
	
	FEdgeInfo = lists:map(fun(X) ->
								  LeftDeviceId = proplists:get_value(leftDeviceId,X,""),
								  IfLeft = proplists:get_value(leftIndex,X,""),
								  PtLeft = proplists:get_value(leftPort,X,""),
								  DescrLeft = proplists:get_value(leftDescr,X,""),
								  AliasLeft = proplists:get_value(leftAlias,X,""),
								  RightDeviceId = proplists:get_value(rightDeviceId,X,""),
								  IfRight = proplists:get_value(rightIndex,X,""),
								  PtRight = proplists:get_value(rightPort,X,""),
								  DescrRight = proplists:get_value(rightDescr,X,""),
								  AliasRight = proplists:get_value(rightAlias,X,""),
								  {LeftDeviceId,IfLeft,PtLeft,DescrLeft,AliasLeft,RightDeviceId,IfRight,PtRight,DescrRight,AliasRight} end
						 , EdgeInfo),
	
	DeviceInfo = api_nnm:db_read("machine",[id,host],[],""),
	
	
	CoordinateDevice = calculateCoordinate(FEdgeInfo,DeviceInfo),
	
%% 	update device
	nnm_discovery_dal:savaCoordinateDevice(CoordinateDevice).

calculateCoordinate(FEdgeInfo,FNetworkDeviceInfo) ->
	Line = [{erlang:list_to_atom(LeftDeviceId),list_to_atom(RightDeviceId)} || {LeftDeviceId,_,_,_,_,RightDeviceId,_,_,_,_} <- FEdgeInfo],
	{Point1,Point2} = calculateCoordinate(FNetworkDeviceInfo,[],[]),
	io:format("Point1:~p~n,Point2:~p~n,Line:~p~n",[Point1,Point2,Line]),
	nnm_discovery_util:writeFile([Point1],"point.txt",write),
	nnm_discovery_util:writeFile([Line],"line.txt",write),
	case nnm_calculateCoordinate:calculate(Point1,Line,circular,{32.0,32.0},[{exclude,Point2}]) of
		{ok,R} -> R;
		_ -> []
	end.

calculateCoordinate([],Point1,Point2) -> {Point1,Point2};
calculateCoordinate([H|T],Point1,Point2) ->
	Id = proplists:get_value(id,H),
	LX = proplists:get_value(x,H,""),
	LY = proplists:get_value(y,H,""),
	if
		LX =/= "" andalso LY =/= "" ->
			Point = {Id,{LX,LY}},
			calculateCoordinate(T,Point1,[Point|Point2]);
		true ->
			calculateCoordinate(T,[Id|Point1],Point2)
	end.

submitTopo() ->
	Topochart = 
		case api_nnm:topochart_get([]) of
			[] ->
				[];
			[R1|_] ->
				Id = proplists:get_value(id,R1),
				case api_nnm:topochart_get(Id) of
					[] -> [];
					R2 ->
						R2
				end
		end,
	EdgeInfo = proplists:get_value(edge,Topochart,[]),

	DD = [[{leftDeviceId,"1301:385987:328008"},
				  {leftIndex,"99"},
				  {leftPort,"99"},
				  {leftDescr,"GigabitEthernet7/1"},
				  {leftAlias,""},
				  {rightDeviceId,"1301:385987:359027"},
				  {rightIndex,"3"},
				  {rightPort,"3"},
				  {rightDescr,"GigabitEthernet1/2"},
				  {rightAlias,""}],
		  [{leftDeviceId,"1301:385987:328004"},
				  {leftIndex,"99"},
				  {leftPort,"99"},
				  {leftDescr,"GigabitEthernet7/1"},
				  {leftAlias,""},
				  {rightDeviceId,"1301:385987:359027"},
				  {rightIndex,"3"},
				  {rightPort,"3"},
				  {rightDescr,"GigabitEthernet1/2"},
				  {rightAlias,""}]],
	FEdgeInfo = lists:append(DD, EdgeInfo),
	
	Data = lists:keyreplace(edge, 1, Topochart, {edge,FEdgeInfo}),
	nnm_discovery_dal:submitTopoChart(Data).


updateMethod() ->
	Machine = api_machine:getAllMachine(0,0,"",""),
	FMachine = [X#machine{method="Snmp"} || X <- Machine],
	[api_machine:update_machine(Y) || Y <- FMachine].
	

		













