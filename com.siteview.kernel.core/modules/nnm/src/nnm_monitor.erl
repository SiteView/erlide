-module(nnm_monitor).

-behaviour(gen_server).
-export([start_link/0]).
%% gen_server callbacks
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
terminate/2, code_change/3]).

-export([start_monitor/0,stop_monitor/0,get_device_data/2,get_port_data/2]).

-include("nnm_define.hrl").
-include("monitor.hrl").

-define(SERVER, nnm_monitor_server).
-define(THREAD, 5).
%-define(THREAD, 10).
-define(INTERVAL, 5000).
-define(FLOWTIME, 120).
-define(ARPTIME, 180).
-define(AFTTIME, 1200).

-define(ROUTE,"ROUTE").
-define(SWITCH,"SWITCH").
-define(ROUTE_SWITCH,"ROUTE_SWITCH").
-define(FIREWALL,"FIREWALL").

-define(LINKCHART,linkchart).
-define(OCTETS,octets).
-define(UCASTPKTS,ucastPkts).
-define(NUCASTPKTS,nUcastPkts).
-define(DISCARDS,discards).
-define(ERRORS,errors).
-define(BANDWIDTHRATE,bandwidthRate).
-define(OPERSTATUS,operStatus).

-define(PATH,"nnm_discovery\\").%\\

-record(state, 
		{
		 status = free,
		 refreshTime = [{flowTable,[]},{arpTable,[]},{aftTable,[]}],
		 pid = [],
		 specialOidList = [],
		 telnetList = [],
		 flowTableData = [],
		 flowTable=[],
		 aftTable = [],
		 arpTable = [],
		 cpuTable = [],
		 memoryTable = [],
		 linkchart = []
		 }
	   ).

start_link() -> gen_server:start_link({local, ?SERVER}, ?MODULE, [], []).

init([]) -> 
	{ok,TelnetList} = file:consult(?NNM_DISCOVERY_TELNETLISTPATH),
	{ok,SpecialOidList} = file:consult(?NNM_DISCOVERY_SPECIALOIDLISTPATH),
	
	Nstate =
		#state{
					specialOidList = SpecialOidList,
					telnetList = TelnetList
					},
	io:format("start..."),
	Pid = spawn(fun() -> monitor_manage(Nstate) end),
	on_monitor_exit(Pid),
	{ok, Nstate#state{status=busy,pid=Pid}}.

start_monitor() ->
	%%start_link(),
	case gen_server:call(?SERVER, {startMonitor}) of
		ok -> {ok,start};
		{error,R} -> {error,R}
	end.

stop_monitor() ->
	gen_server:call(?SERVER, {stop_monitor}).

set_monitor_refresh_time(Time) ->
	gen_server:call(?SERVER, {set_monitor_refresh_time,Time}).

get_monitor_refresh_time() ->
	gen_server:call(?SERVER, {get_monitor_refresh_time}).

set_monitor_data(Data) ->
	gen_server:call(?SERVER, {set_monitor_data,Data}, 120000).

get_device_data(Target,Param) ->
	gen_server:call(?SERVER, {get_device_data,Target,Param}).

get_port_data(Target,PortList) ->
	gen_server:call(?SERVER, {get_port_data,Target,PortList}).

handle_call({startMonitor}, _From, State) ->
	case State#state.status of
		busy -> 
			{reply,{error,busy},State};
		_ ->
			{ok,TelnetList} = file:consult(?NNM_DISCOVERY_TELNETLISTPATH),
			{ok,SpecialOidList} = file:consult(?NNM_DISCOVERY_SPECIALOIDLISTPATH),
			Nstate =
				State#state{
							specialOidList = SpecialOidList,
							telnetList = TelnetList
							},
			io:format("start..."),
			Pid = spawn(fun() -> monitor_manage(Nstate) end),
			on_monitor_exit(Pid),
			{reply,ok,Nstate#state{status=busy,pid=Pid}}
	end;
handle_call({stop_monitor}, _From, State) ->
	case State#state.pid of
		[] ->
			[];
		_ ->
			erlang:exit(State#state.pid, stop)
	end,
	{reply,ok,State#state{
						  status = free,
		 				  refreshTime = [{flowTable,[]},{arpTable,[]},{aftTable,[]}],
		 				  pid = [],
		 				  specialOidList = [],
		 				  telnetList = [],
		 				  flowTable = [],
						  flowTableData = [],
		 				  aftTable = [],
		 				  arpTable = [],
		 				  cpuTable = [],
		 				  memoryTable = [],
						  linkchart = []
						  }};
handle_call({set_monitor_refresh_time,Time}, _From, State) ->
	FlowTime = 
		case proplists:lookup(flowTable,Time) of
			none -> proplists:lookup(flowTable, State#state.refreshTime);
			R1 -> R1
		end,
	ArpTime = 
		case proplists:lookup(arpTable,Time) of
			none -> proplists:lookup(arpTable, State#state.refreshTime);
			R2 -> R2
		end,
	AftTime =
		case proplists:lookup(aftTable,Time) of
			none -> proplists:lookup(aftTable, State#state.refreshTime);
			R3 -> R3
		end,
	{reply,ok,State#state{refreshTime=[FlowTime,ArpTime,AftTime]}};
handle_call({get_monitor_refresh_time}, _From, State) ->
	{reply,State#state.refreshTime,State};
handle_call({set_monitor_data,Data}, _From, State) ->
	{K,V} = Data,
	io:format("NNM==>K:~p~n,NNM==>V:~p~n",[K,V]),
 	ofbiz:call_topology(K,V),
	if
		K =:= ?FLOWTABLE ->
			io:format("end refresh..."),
			{X,Y,Z} = now(),
			Time = integer_to_list(X*1000000+Y),
%% 			nnm_discovery_util:writeFile(V,Time++"new.txt" ,write),
%% 			io:format("old data:~p~n",[State#state.flowTableData]),
%% 			nnm_discovery_util:writeFile(State#state.flowTableData,Time++"old.txt",write),
			FV = computeFlow(V,State#state.flowTableData),
			NState = State#state{flowTable=FV,flowTableData=V},
			nnm_discovery_util:writeFile([NState],"monitorDevice.txt",write),
			{reply,ok,NState};
		K =:= ?AFTTABLE ->
			ArpTable = State#state.arpTable,
			AftTable = 
				case V of
					[] -> State#state.aftTable;
					_ -> formatAft(ArpTable,V)
				end,
			{reply,ok,State#state{aftTable=AftTable}};
		K =:= ?ARPTABLE ->
			store_ipmac(V),
			store_new_device(V),
			{reply,ok,State#state{arpTable=V}};
		K =:= ?CPUTABLE ->
			{reply,ok,State#state{cpuTable=V}};
		K =:= ?MEMORYTABLE ->
			{reply,ok,State#state{memoryTable=V}};
		K =:= ?LINKCHART ->
			Linkchart = computeLinkchart(State),
			{reply,ok,State#state{linkchart=Linkchart}};
		true ->
			{reply,ok,State}
	end;
handle_call({get_device_data,Target,Param}, _From, State) ->
	IdList = proplists:get_value(id,[Param],[]),
	F = fun(X) ->
				case IdList of
					[] -> true;
					_ ->
						{Id,_,_} = X,
						lists:member(Id, IdList)
				end
		end,
	Data = 
		case Target of
			?ARPTABLE ->
				lists:filter(F, State#state.arpTable);
			?AFTTABLE ->
				lists:filter(F, State#state.aftTable);
			?CPUTABLE ->
				lists:filter(F, State#state.cpuTable);
			?MEMORYTABLE ->
				lists:filter(F, State#state.memoryTable);
			?LINKCHART ->
				filterDeviceTopo([Param],State#state.linkchart);
			_ ->
				[]
		end,
	{reply,Data,State};
handle_call({get_port_data,Target,IfList}, _From, State) ->
	FlowTable = State#state.flowTable,
%% 	io:format("FlowTable:~p~n",[FlowTable]),
	Data = filterFlowTable(FlowTable,Target,IfList),
	{reply,Data,State}.

handle_cast(_Msg, State) -> {noreply, State}.
handle_info(_Info, State) -> {noreply, State}.
terminate(_Reason, _State) -> ok.
code_change(_OldVsn, State, _Extra) -> {ok, State}.

monitor_manage(State) ->
	RefreshTime = get_monitor_refresh_time(),
	Interval = nnm_discovery_dal:getMonitorInterval(),
%% 	io:format("lun xun..."),
%% 	io:format("RefreshTime:~p~nInterval~p~n",[RefreshTime,Interval]),
	
	Type = [?ROUTE,?SWITCH,?ROUTE_SWITCH,?FIREWALL],
	NetworkDevice = api_machine:get_DeviceByType(Type,0,0,"",""),
	SpecialOidList = State#state.specialOidList,
	TelnetList = State#state.telnetList,
	
	RefreshFlowTime = 
		case proplists:get_value(flowTable,RefreshTime) of
			[] -> calendar:local_time();
			Time1 -> Time1
		end,
	RefreshArpTime = 
		case proplists:get_value(arpTable,RefreshTime) of
			[] -> calendar:local_time();
			Time2 -> Time2
		end,
	RefreshAftTime =
		case proplists:get_value(aftTable,RefreshTime) of
			[] -> calendar:local_time();
			Time3 -> Time3
		end,
	
	FlowTimeInterval = proplists:get_value(flowTable,Interval,?FLOWTIME),
	ArpTimeInterval = proplists:get_value(arpTable,Interval,?ARPTIME),
	AftTimeInterval = proplists:get_value(aftTable,Interval,?AFTTIME),
	
%% 	arp
	case RefreshArpTime =< calendar:local_time() of
		true ->
			io:format("start read arp table~n"),
			ArpTable = monitor_manage_proc(NetworkDevice,SpecialOidList,TelnetList,0,arpTable,[]),
			Seconds2 = calendar:datetime_to_gregorian_seconds(calendar:local_time()) + ArpTimeInterval,
			set_monitor_refresh_time([{arpTable,calendar:gregorian_seconds_to_datetime(Seconds2)}]),
			set_monitor_data({arpTable,ArpTable});
		false ->
			[]
	end,
%% 	aft
	case RefreshAftTime =< calendar:local_time() of
		true ->
			io:format("start read aft table~n"),
			AftTable = monitor_manage_proc(NetworkDevice,SpecialOidList,TelnetList,0,aftTable,[]),
			Seconds3 = calendar:datetime_to_gregorian_seconds(calendar:local_time()) + AftTimeInterval,
			set_monitor_refresh_time([{aftTable,calendar:gregorian_seconds_to_datetime(Seconds3)}]),
			set_monitor_data({aftTable,AftTable});
		false ->
			[]
	end,	
%% 	linkchart
	case RefreshAftTime =< calendar:local_time() of
		true ->
			io:format("start read linkchart~n"),
			set_monitor_data({linkchart,[]});
		false ->
			[]
	end,
%% 	cpu
	case RefreshFlowTime =< calendar:local_time() of
		true ->
			io:format("start read cpu table~n"),
			CpuTable = monitor_manage_proc(NetworkDevice,SpecialOidList,TelnetList,0,cpuTable,[]),
			set_monitor_data({cpuTable,CpuTable});
		false ->
			[]
	end,
%% 	memory
	case RefreshFlowTime =< calendar:local_time() of
		true ->
			io:format("start read memory table~n"),
			MemoryTable = monitor_manage_proc(NetworkDevice,SpecialOidList,TelnetList,0,memoryTable,[]),
			set_monitor_data({memoryTable,MemoryTable});
		false ->
			[]
	end,
%% 	flow
	case RefreshFlowTime =< calendar:local_time() of
		true ->
			io:format("start read flow table~n"),
			TopoChart = 
				case nnm_discovery_dal:readFromDB(?NNM_DISCOVERY_TOPOCHART_TABLE,[edge],"","") of
					[] -> [];
					[H|_] -> get_monitor_port(proplists:get_value(edge,H,[]))
				end,
			io:format("topochart:~p~n",[TopoChart]),
			nnm_cache:set(nnm_cache, nnm_monitor, {topoChart,TopoChart}),			
			
			FlowTable = monitor_manage_proc(NetworkDevice,SpecialOidList,TelnetList,0,flowTable,[]),
			Seconds1 = calendar:datetime_to_gregorian_seconds(calendar:local_time()) + FlowTimeInterval,
			set_monitor_refresh_time([{flowTable,calendar:gregorian_seconds_to_datetime(Seconds1)}]),
			set_monitor_data({flowTable,FlowTable});
		false ->
			[]
	end,
%% 	io:format("end refresh..."),
	timer:sleep(?INTERVAL),
	monitor_manage(State).
	

monitor_manage_proc([],_,_,0,_,Result) -> Result; 
monitor_manage_proc([],SpecialOidList,TelnetList,C,Target,Result) ->
	receive
		{monitor_done,_Why} ->
			monitor_manage_proc([],SpecialOidList,TelnetList,C-1,Target,Result);
		{monitor_data,Value} ->
			monitor_manage_proc([],SpecialOidList,TelnetList,C,Target,[Value|Result]);
		_ ->
			monitor_manage_proc([],SpecialOidList,TelnetList,C,Target,Result)
	end;
monitor_manage_proc([H|T],SpecialOidList,TelnetList,C,Target,Result) ->
	receive
		{monitor_done,_Why} ->
			start_monitor_proc(H,SpecialOidList,TelnetList,Target),
			monitor_manage_proc(T,SpecialOidList,TelnetList,C,Target,Result);
		{monitor_data,Value} ->
			monitor_manage_proc([H|T],SpecialOidList,TelnetList,C,Target,[Value|Result]);
		_ ->
			monitor_manage_proc([H|T],SpecialOidList,TelnetList,C,Target,Result)
	after 20 ->
			if
				C < ?THREAD ->
					start_monitor_proc(H,SpecialOidList,TelnetList,Target),
					monitor_manage_proc(T,SpecialOidList,TelnetList,C+1,Target,Result);
				true ->
					monitor_manage_proc([H|T],SpecialOidList,TelnetList,C,Target,Result)
			end
	end.
			
start_monitor_proc(Device,SpecialOidList,TelnetList,Target) ->
	ParentId = self(),
	Pid = spawn(fun() -> do_monitor_proc(ParentId,Device,SpecialOidList,TelnetList,Target) end),
	on_porc_exit(Pid,self()).

do_monitor_proc(ParentId,Device,SpecialOidList,TelnetList,Target) ->
	Id = Device#machine.id,
	Ip = Device#machine.host,
	Other = Device#machine.other,
	
	DevType = proplists:get_value(devType,Other,""),
	SnmpFlag = proplists:get_value(snmpFlag,Other,""),
	SnmpParam = proplists:get_value(snmpParam,Other,[]),
	TelnetFlag = proplists:get_value(telnetFlag,Other,""),
	TelnetParam = proplists:get_value(telnetFlag,Other,[]),
	SysobjectId = proplists:get_value(sysobjectId, Other, ""),
	
	Param = [{ip,Ip},{sysobjectId,SysobjectId},{snmpFlag,SnmpFlag},{snmpParam,SnmpParam},{telnetFlag,TelnetFlag},{telnetParam,TelnetParam}],
 	%%io:format("Param:~p~n",[Param]),
	GetObject = nnm_discovery_bll:new(SpecialOidList,TelnetList),
	
	Data =
		case Target of
			flowTable ->
				io:format("read ~p flowTable~n",[Ip]),
				Cache = nnm_cache:get(nnm_cache, nnm_monitor),
%% 				io:format("cache:~p~n",[Cache]),
				case proplists:get_value(atom_to_list(Id),proplists:get_value(topoChart,[Cache],[]),[]) of
					[] -> {Ip,[]};
					Indexs -> 
						GetObject:getMonitorValue([{indexs,Indexs}|Param],?FLOWTABLE)
				end;
			arpTable ->
				io:format("read ~p arpTable~n",[Ip]),
				GetObject:getMonitorValue(Param,?ARPTABLE);
			aftTable ->
				io:format("read ~p aftTable~n",[Ip]),
				case DevType of
					?NNM_DEVICE_TYPE_ROUTE ->
						{Ip,[]};
					_  ->
						GetObject:getMonitorValue(Param,?AFTTABLE)
				end;
			cpuTable ->
				io:format("read ~p cpuTable~n",[Ip]),
				TemData = GetObject:getMonitorValue(Param,?CPUTABLE),
			%%--------------just want core-----------------------------------------
				%%io:format("TemData = ~p~n SysobjectId= ~p~n", [TemData,SysobjectId]),
				{IP, CPU} = TemData,
				case CPU of       %是否取到了数据
				    [] -> TemData;
				    _ -> 
					if length(CPU) =:=   %是否为一个CPU，或者为简单变量能直接取到Core CPU
					    1 ->  TemData;
					    true ->
						  if
							SysobjectId =:= "1.3.6.1.4.1.2011.2.115"	->  
							    [_, _, CPU3 | _] = CPU, {IP, [CPU3]}; 
							SysobjectId =:= "1.3.6.1.4.1.2011.2.133"	->  
							    [_, _, CPU3 | _] = CPU, {IP, [CPU3]}; 
							SysobjectId =:= "1.3.6.1.4.1.2011.2.135"	->  
							    [_, _, CPU3 | _] = CPU, {IP, [CPU3]};  
							true	->   
							    [CPU1 | _] = CPU,   {IP, [CPU1]}   			
						   end  
					end   
				end;
			%%------------------------------------------------------	
			memoryTable ->
				io:format("read ~p memoryTable~n",[Ip]),
			
				TemData = GetObject:getMonitorValue(Param,?MEMORYTABLE),
			%%--------------just want core-----------------------------------------
				
				io:format("read ~p memoryTable ~p~n",[Ip,TemData]),
				{IP, Mem} = TemData,
				case TemData of
					{_,[]} -> TemData; %是否取到了数据
					{_,Mem} when length(Mem) > 0  
						-> {IP,[lists:nth(1,Mem)]};
						_-> TemData
				end;
			%~ %%---------------------------------------------------------
			_ ->
				{Ip,[]}
		end,
%% 	nnm_discovery_util:writeFile([Data],"monitorData.log",append),
  %      io:format("~n~n~n======read Data ~p======~n~n~n",[Data]),
	{V,VV} = Data,
	ParentId ! {monitor_data,{Id,V,VV}}.

on_monitor_exit(Pid) ->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						io:format("why:~p~n",[Why])
				end
		  end).

on_porc_exit(Pid,ParentId) ->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						ParentId ! {monitor_done,Why}
				end
		  end).

formatAft(ArpTable,AftTable) -> 
	IpMacList = getIpMacList(ArpTable,[]),
%% 	io:format("IpMacList:~p~n",[IpMacList]),
	fAft(IpMacList,AftTable).

getIpMacList([],IpMacList) -> IpMacList;
getIpMacList([H|ArpTable],IpMacList) ->
	{_,_,V} = H,
	D = [{Mac,Ip} || {_,Mac,Ip,_} <- V, (not lists:member({Mac,Ip}, IpMacList))],
	getIpMacList(ArpTable,lists:append(D, IpMacList)).

fAft(_,[]) -> [];
fAft(IpMacList,[H|AftTable]) ->
	{Id,Ip,V} = H,
	D = [{Port,Mac,proplists:get_value(Mac, IpMacList, ""),Status} || {Port,Mac,Status} <- V],
	[{Id,Ip,D}|fAft(IpMacList,AftTable)].

filterFlowTable([],_,_) -> [];
filterFlowTable([H|FlowTable],Target,IfList) ->
	{Id,Ip,Info} = H,
	case proplists:get_value(Id,IfList) of
		undefined ->
			filterFlowTable(FlowTable,Target,IfList);
		FIfList ->
			FInfo = filterFlow(Info,Target,FIfList),
			io:format("================ filterFlowTable for ofbiz begin==============="),
			Data = {Id,Ip,FInfo,Target},
			ofbiz:call_topology("portData",[Data]),
			
			[{Id,Ip,FInfo}|filterFlowTable(FlowTable,Target,IfList)]
	end.


filterFlow([],_,_) -> [];
filterFlow([H|Info],Target,FIfList) ->
	IfIndex = proplists:get_value(index,H),
	case FIfList =:= [] orelse lists:member(IfIndex, FIfList) of
		false ->
			filterFlow(Info,Target,FIfList);
		true ->
			case Target of
				?OCTETS ->
					[{IfIndex,proplists:get_value(octets,H)}|filterFlow(Info,Target,FIfList)];
				?UCASTPKTS ->
					[{IfIndex,proplists:get_value(ucastPkts,H)}|filterFlow(Info,Target,FIfList)];
				?NUCASTPKTS ->
					[{IfIndex,proplists:get_value(nUcastPkts,H)}|filterFlow(Info,Target,FIfList)];
				?DISCARDS ->
					[{IfIndex,proplists:get_value(discards,H)}|filterFlow(Info,Target,FIfList)];
				?ERRORS ->
					[{IfIndex,proplists:get_value(errors,H)}|filterFlow(Info,Target,FIfList)];
				?BANDWIDTHRATE ->
					[{IfIndex,proplists:get_value(bandwidthRate,H)}|filterFlow(Info,Target,FIfList)];
				?OPERSTATUS ->
					[{IfIndex,proplists:get_value(operStatus,H)}|filterFlow(Info,Target,FIfList)];
				_ ->
					[]
			end
	end.

computeLinkchart(State) ->
%% 	nnm_discovery_util:writeFile([State],"monitorDevice.txt",write),
	DeviceList = api_machine:getAllMachine(0,0,"",""),
	PcList = nnm_discovery_dal:readFromDB(?NNM_DISCOVERY_PCLIST_TABLE,[id,ip,mac,discoverTime,activeTime,info],[],""),
	ArpTable = State#state.arpTable,
	ADeviceList = getDeviceFromArp(ArpTable,[]),

	IpIdList = [findIpId(X,DeviceList,PcList) || X <- ADeviceList],
	
	AftTable = State#state.aftTable,
	InterfaceTable = [{X#machine.host,proplists:get_value(infs,X#machine.other,[])} || X <- DeviceList], 
%% 	io:format("interface:~p~n",[InterfaceTable]),
	{DeviceData,ArpData,AftData,InterfaceData} = formatData(DeviceList,ArpTable,AftTable,InterfaceTable),
%% 	{FDeviceList,FArpTable,FAftTable,FInterfaceTable} =
%% 		analyseFormat(DeviceList,ArpTable,AftTable,InterfaceTable),
%% 	
%% 	{DeviceData,ArpData,AftData,InterfaceData} =
%% 		analyseEncode(FDeviceList,FArpTable,FAftTable,FInterfaceTable),
%% 	
%% 	RouteData = [],
%% 	OspfData = [],
%% 	BgpData = [],
%% 	DirectData = [],
%% 	
%% 	Data = [list_to_tuple(DeviceData),list_to_tuple(InterfaceData),list_to_tuple(RouteData),list_to_tuple(AftData),list_to_tuple(ArpData),
%% 			list_to_tuple(OspfData),list_to_tuple(BgpData),list_to_tuple(DirectData)],	
	
	nnm_discovery_analyse:start_link(),
%% 	Value = nnm_discovery_analyse:analyse({DeviceData,InterfaceData,[],AftData,ArpData,[],[],[]}),
	filelib:ensure_dir(?PATH),
	Value = nnm_discovery_analyse:analyse({DeviceData,InterfaceData,[],AftData,ArpData,[],[],[]},?PATH),
%% 	io:format("value:~p~n",[Value]),
	Result =
	case Value of
		<<"error">> ->
			[];
		{error,timeout} ->
			[];
		_ ->
			tuple_to_list(binary_to_term(Value))
	end,
	
%% 	io:format("Result:~p~n",[Result]),
	formatEdge(Result,IpIdList). 

analyseFormat(DeviceList,ArpTable,AftTable,InterfaceTable) ->
	FDeviceList = analyseFormatDevice(DeviceList),
	FArpTable = analyseFormatArpTable(ArpTable),
	FAftTable = analyseFormatAftTable(AftTable,InterfaceTable),
	FInterfaceTable = analyseFormatInterfaceTable(InterfaceTable),
	
	{FDeviceList,FArpTable,FAftTable,FInterfaceTable}.

analyseFormatDevice([]) -> [];
analyseFormatDevice([H|DeviceList]) ->
	Other = H#machine.other,
	IpAddr = proplists:get_value(ipAddr,Other,[]),
	
	FIpAddr = 
		case IpAddr of
			[] -> [];
			_ -> lists:filter(fun(X) -> 
									(proplists:get_value(ipAddress,X) =/= "127.0.0.1") andalso 
										(proplists:get_value(ipMask, X) =/= "255.0.0.0") 
							end, IpAddr)
		end,
	FOther = lists:keyreplace(ipAddr, 1, Other, {ipAddr,FIpAddr}),
	[H#machine{other=FOther}|analyseFormatDevice(DeviceList)].

analyseFormatInterfaceTable(InterfaceTable) ->
	InterfaceTable.

analyseFormatArpTable([]) -> [];
analyseFormatArpTable([H|ArpTable]) ->
	{_,SourceIp,Info} = H,
	FInfo = analyseFormatArp(Info,SourceIp,[]),
	[{SourceIp,FInfo}|analyseFormatArpTable(ArpTable)].

analyseFormatArp([],_,Result) -> Result;
analyseFormatArp([H|Info],SourceIp,Result) ->
	{IfIndex,_,Ip,_} = H,
	if 
		Ip =:= [] ->
			analyseFormatArp(Info,SourceIp,Result);
		Ip =:= SourceIp ->
			analyseFormatArp(Info,SourceIp,Result);
		true ->
			Str = lists:nth(4,string:tokens(Ip,".")),
			case Ip =:= SourceIp orelse Str =:= "255" orelse Str =:= "0" of
				true ->
					analyseFormatArp(Info,SourceIp,Result);
				false ->
					case proplists:get_value(IfIndex,Result) of
						undefined ->
							analyseFormatArp(Info,SourceIp,[{IfIndex,[Ip]}|Result]);
						R ->
							V = [Ip|R],
							analyseFormatArp(Info,SourceIp,lists:keyreplace(IfIndex, 1, Result, {IfIndex,V}))
					end
			end
	end.
	
analyseFormatAftTable([],_) -> [];
analyseFormatAftTable([H|AftTable],InterfaceTable) ->
	{_,SourceIp,Info} = H,
	Interface = proplists:get_value(SourceIp,InterfaceTable),
	PortIf = [{Port,Index} || {Index,_,_,_,_,_,Port} <- Interface],
	FInfo = analyseFormatAftTable(Info,SourceIp,PortIf,[]),
	[{SourceIp,FInfo}|analyseFormatAftTable(AftTable,InterfaceTable)].

analyseFormatAftTable([],_,_,Result) -> Result;
analyseFormatAftTable([H|Info],SourceIp,PortIf,Result) ->
	{Port,_,Ip,_} = H,
	if
		Ip =:= [] ->
			analyseFormatAftTable(Info,SourceIp,PortIf,Result);
		Ip =:= SourceIp ->
			analyseFormatAftTable(Info,SourceIp,PortIf,Result);
		true ->
			Str = lists:nth(4,string:tokens(Ip,".")),
			case Str =:= "255" orelse Str =:= "0" of
				true ->
					analyseFormatAftTable(Info,SourceIp,PortIf,Result);
				false ->
					IfIndex = proplists:get_value(Port,PortIf,"0"),
					case proplists:get_value(IfIndex,Result) of
						undefined ->
							analyseFormatAftTable(Info,SourceIp,PortIf,[{IfIndex,[Ip]}|Result]);
						R ->
							V = [Ip|R],
							analyseFormatAftTable(Info,SourceIp,PortIf,lists:keyreplace(IfIndex, 1, Result, {IfIndex,V}))
					end
			end
	end.

analyseEncode(DeviceList,ArpTable,AftTable,InterfaceTable) ->
	DeviceData = analyseEncodeDevice(DeviceList),
	ArpData = analyseEncodeAft(ArpTable),
	AftData = analyseEncodeAft(AftTable),
	InterfaceData = analyseEncodeInterface(InterfaceTable),
	
	{DeviceData,ArpData,AftData,InterfaceData}.

analyseEncodeDevice([]) -> [];
analyseEncodeDevice([H|DeviceList]) ->
	Other = H#machine.other,
	IpAddr = proplists:get_value(ipAddr,Other,[]),
	IpAdEntAddr = case IpAddr of [] -> []; _ -> proplists:get_all_values(ipAddress, IpAddr) end,
	IpAdEntIfIndex = case IpAddr of [] -> []; _ -> proplists:get_all_values(ipIfIndex, IpAddr) end,
	IpAdEntNetMask = case IpAddr of [] -> []; _ -> proplists:get_all_values(ipMask, IpAddr) end,
	SnmpParam = proplists:get_value(snmpParam,Other,[]),
	Ip = H#machine.host,
	BaseMac = proplists:get_value(baseMac,Other,""),
	SnmpFlag = proplists:get_value(snmpFlag,Other,""),
	CommunityGet = proplists:get_value(getCommunity,SnmpParam,""),
	CommunitySet = proplists:get_value(setCommunity,SnmpParam,""),
	DevType = proplists:get_value(devType,Other,""),
	DevFactory = proplists:get_value(devFactory,Other,""),
	DevModel = proplists:get_value(devModel,Other,""),
	DevTypeName = H#machine.type,
	SysobjectId = proplists:get_value(sysobjectId,Other,""),
	SysSvcs = proplists:get_value(sysSvcs,Other,""),
	SysName = H#machine.name,
	Macs = proplists:get_value(macs,Other,[]),
	
	[{Ip,BaseMac,SnmpFlag,CommunityGet,CommunitySet,DevType,DevFactory,DevModel,DevTypeName,SysobjectId,SysSvcs,SysName,
	 	list_to_tuple(IpAdEntAddr),list_to_tuple(IpAdEntIfIndex),list_to_tuple(IpAdEntNetMask),list_to_tuple(Macs)}
	|analyseEncodeDevice(DeviceList)].

analyseEncodeInterface([]) -> [];
analyseEncodeInterface([H|InterfaceTable]) ->
	{SourceIp,Info} = H,
	Amount = "0",
	Inrec = [{IfIndex,IfType,IfDescr,IfMac,IfPort,IfSpeed,IfAlias} || {IfIndex,IfType,IfDescr,IfMac,IfSpeed,IfAlias,IfPort} <- Info],
	
	[{SourceIp,Amount,list_to_tuple(Inrec)}|analyseEncodeInterface(InterfaceTable)].
	
analyseEncodeAft([]) -> [];
analyseEncodeAft([H|AftTable]) ->
	{SourceIp,Info} = H,
	FInfo = [{X,list_to_tuple(Y)} || {X,Y} <- Info],
	[{SourceIp,list_to_tuple(FInfo)}|analyseEncodeAft(AftTable)].

formatEdge([],_) -> [];
formatEdge([H|EdgeList],IpIdList) ->
	{IpLeft,PtLeft,IfLeft,DescrLeft,AliasLeft,IpRight,PtRight,IfRight,DescrRight,AliasRight} = H,
	LeftDeviceId = proplists:get_value(IpLeft,IpIdList,""),
	RightDeviceId = proplists:get_value(IpRight,IpIdList,""),
	
	[{LeftDeviceId,PtLeft,IfLeft,DescrLeft,AliasLeft,RightDeviceId,PtRight,IfRight,DescrRight,AliasRight} 
	| formatEdge(EdgeList,IpIdList)].

getDeviceFromArp([],Result) -> Result;
getDeviceFromArp([H|ArpTable],Result) ->
	{_,_,Info} = H,
	IpMacList = [{Ip,Mac} || {_,Mac,Ip,_} <- Info,(not lists:member({Ip,Mac},Result)),(Ip =/= ""),(Mac =/= "")],
	getDeviceFromArp(ArpTable,lists:append(IpMacList, Result)).
  
findIpId(IpMac,DeviceList,PcList) ->
	case findIpIdFromDevice(IpMac,DeviceList) of
		[] ->
			findIpIdFromPc(IpMac,PcList);
		R ->
			R
	end.

findIpIdFromDevice(_,[]) -> [];
findIpIdFromDevice(IpMac,[H|DeviceList]) ->
	{Ip,_} = IpMac,
	Host = H#machine.host,
	Other = H#machine.other,
	IpList = [proplists:get_value(ipAddress,X) || X <- proplists:get_value(ipAddr,Other,[])],
	Ips = [Host|IpList],
	case lists:member(Ip, Ips) of
		true ->
			FOther = lists:keyreplace(activeTime, 1, Other, {activeTime,nnm_discovery_util:getLocalTime()}),
			api_machine:update_machine(H#machine{other=FOther}),
			{Ip,H#machine.id};
		false ->
			findIpIdFromDevice(IpMac,DeviceList)
	end.

findIpIdFromPc(IpMac,[]) -> 
	{Ip,Mac} = IpMac,
	Data = [{ip,Ip},{mac,Mac},{info,[]},{discoverTime,nnm_discovery_util:getLocalTime()},{activeTime,nnm_discovery_util:getLocalTime()}],
	[Id|_] = nnm_discovery_dal:writeToDB(?NNM_DISCOVERY_PCLIST_TABLE,[Data]),
	{Ip,Id};
findIpIdFromPc(IpMac,[H|PcList]) -> 
	{Ip,Mac} = IpMac,
	Ip1 = proplists:get_value(ip,H),
	Mac1 = proplists:get_value(mac,H),
	if
		Ip =:= Ip1 andalso Mac =:= Mac1 ->
			Info = proplists:get_value(info,H),
			Id = proplists:get_value(id,H),
			DiscoverTime = 
				case proplists:get_value(discoverTime,H) of
					undefined ->
						nnm_discovery_util:getLocalTime();
					R ->
						R
				end,
			ActiveTime = nnm_discovery_util:getLocalTime(),
			nnm_discovery_dal:writeToDB(?NNM_DISCOVERY_PCLIST_TABLE,[[{id,Id},{ip,Ip},{mac,Mac},{discoverTime,DiscoverTime},{activeTime,ActiveTime},{info,Info}]]),
			{Ip,Id};
		true ->
			findIpIdFromPc(IpMac,PcList)
	end.
  
filterDeviceTopo(Param,Linkchart) ->
	IdList = proplists:get_value(id,Param,[]),
	IpList = proplists:get_value(ip,Param,[]),
	MacList = proplists:get_value(mac,Param,[]),
  
	FilterIdList = 
		if
			IdList =/= [] ->
				IdList;
			IpList =/= [] ->
				DeviceList = api_machine:getAllMachine(0,0,"",""),
				PcList = nnm_discovery_dal:readFromDB(?NNM_DISCOVERY_PCLIST_TABLE,[id,ip],[],""),
				[Y || X <- IpList,Y <- [findLinkchartIdbyIp(X,DeviceList,PcList)],Y =/= []];
			MacList =/= [] ->
				DeviceList = api_machine:getAllMachine(0,0,"",""),
				PcList = nnm_discovery_dal:readFromDB(?NNM_DISCOVERY_PCLIST_TABLE,[id,mac],[],""),
				[Y || X <- MacList,Y <-[findLinkchartIpByMac(X,DeviceList,PcList)],Y=/=[]];
			true ->
				[]
		end,
	
	if
		FilterIdList =:= [] ->
			[{"","",Linkchart}];
		true ->
			filterLinkChart(FilterIdList,Linkchart)
	end.
  
findLinkchartIdbyIp(Ip,DeviceList,PcList) ->
	case findLinkchartIdbyIpFromDevice(Ip,DeviceList) of
		[] ->
			findLinkchartIdbyIpFromPc(Ip,PcList);
		R ->
			R
	end.

findLinkchartIdbyIpFromDevice(_,[]) -> [];
findLinkchartIdbyIpFromDevice(Ip,[H|DeviceList]) ->
	Host = H#machine.host,
	Other = H#machine.other,
	IpList = [proplists:get_value(ipAddress,X) || X <- proplists:get_value(ipAddr,Other,[])],
	Ips = [Host|IpList],
	case lists:member(Ip,Ips) of
		true ->
			H#machine.id;
		false ->
			findLinkchartIdbyIpFromDevice(Ip,DeviceList)
	end.

findLinkchartIdbyIpFromPc(_,[]) -> [];
findLinkchartIdbyIpFromPc(Ip,[H|PcList]) ->
	case proplists:get_value(ip,H) =:= Ip of
		true ->
			proplists:get_value(id,H);
		false ->
			findLinkchartIdbyIpFromPc(Ip,PcList)
	end.


findLinkchartIpByMac(Mac,DeviceList,PcList) ->
	case findLinkchartIpByMacFromDevice(Mac,DeviceList) of
		[] ->
			findLinkchartIpByMacFromPcList(Mac,PcList);
		R ->
			R
	end.

findLinkchartIpByMacFromDevice(_,[]) -> [];
findLinkchartIpByMacFromDevice(Mac,[H|DeviceList]) ->
	Other = H#machine.other,
	MacList = proplists:get_value(macs,Other,[]),
	case lists:member(Mac, MacList) of
		true ->
			H#machine.id;
		false ->
			findLinkchartIpByMacFromDevice(Mac,DeviceList)
	end.

findLinkchartIpByMacFromPcList(_,[]) -> [];
findLinkchartIpByMacFromPcList(Mac,[H|PcList]) ->
	case proplists:get_value(mac,H) =:= Mac of
		true ->
			proplists:get_value(id,H);
		false ->
			findLinkchartIpByMacFromPcList(Mac,PcList)
	end.

filterLinkChart([],_) -> [];
filterLinkChart([H|FilterIdList],Linkchart) ->
	Result = 
		lists:filter(fun(X) -> 
						 	 {LeftDeviceId,_,_,_,_,RightDeviceId,_,_,_,_} = X, 
						 	 LeftDeviceId =:= H orelse RightDeviceId =:= H
				 	 end, Linkchart),
	[{H,"",Result}|filterLinkChart(FilterIdList,Linkchart)].
						
%% ip-mac change
store_ipmac(ArpTable) ->
	ActiveIpMac = get_active_ipmac(ArpTable,[]),
	BaseIpMac = get_base_ipmac(),
	ChangeIpMac = get_change_ipmac(),
	compare_ipmac(BaseIpMac,ActiveIpMac,ChangeIpMac),
	ActiveIpMac1 = [{Ip,Mac} || {Mac,Ip} <- ActiveIpMac],
%% 	ChangeIpMac1 = get_change_ipmac(),
	compare_macip(BaseIpMac,ActiveIpMac1,ChangeIpMac).

get_active_ipmac([],Data) -> Data;
get_active_ipmac([H|T],Data) ->
	{_,_,ArpTable} = H,
	MacIp = [{Mac,Ip} || {_,Mac,Ip,_} <- ArpTable],
	get_active_ipmac(T,nnm_discovery_util:mergerList(Data,MacIp)).

get_base_ipmac() ->
	Data = nnm_discovery_dal:readFromDB(?NNM_BASEIPMAC_TABLE,[info],[],""),
	[erlang:list_to_tuple(proplists:get_value(info, X, [])) || X <- Data].

get_change_ipmac() ->
	nnm_discovery_dal:readFromDB(?NNM_CHANGEIPMAC_TABLE,[id,oldIp,oldMac,newIp,newMac,discoverTime,activeTime,count,info],[],"").

compare_ipmac([],_,_) -> [];
compare_ipmac([H|T],ActiveIpMac,ChangeIpMac) ->
	{Mac,Ip} = H,
	case proplists:get_value(Mac,ActiveIpMac,[]) of
		[] ->
			compare_ipmac(T,ActiveIpMac,ChangeIpMac);
		Ip ->
			compare_ipmac(T,ActiveIpMac,ChangeIpMac);
		Ip1 ->
			IpChangeTime = get_activeTime(newMac,Mac,ChangeIpMac,[]),
			case find_ipmac_change(Ip1,Mac,IpChangeTime,ChangeIpMac) of
				find -> [];
				_ ->
					Data = [{oldIp,Ip},{oldMac,Mac},{newIp,Ip1},{newMac,Mac},{discoverTime,nnm_discovery_util:getLocalTime()},
							{activeTime,nnm_discovery_util:getLocalTime()},{count,1},{info,[]}],
					nnm_discovery_dal:writeToDB(?NNM_CHANGEIPMAC_TABLE,[Data])
			end,
			compare_ipmac(T,ActiveIpMac,ChangeIpMac)
	end.

get_activeTime(_,_,[],Time) -> Time;
get_activeTime(Type,D,[H|ChangeIpMac],Time) ->
	D1 = proplists:get_value(Type,H,""),
	case D =:= D1 of
		true ->
			ActiveTime = proplists:get_value(activeTime,H,""),
			if
				ActiveTime > Time ->
					get_activeTime(Type,D,ChangeIpMac,ActiveTime);
				true ->
					get_activeTime(Type,D,ChangeIpMac,Time)
			end;
		false ->
			get_activeTime(Type,D,ChangeIpMac,Time)
	end.
				

find_ipmac_change(_,_,_,[]) -> notfind;
find_ipmac_change(Ip,Mac,Time,[H|ChangeIpMac]) ->
	Ip1 = proplists:get_value(newIp,H,""),
	Mac1 = proplists:get_value(newMac,H,""),
	ActiveTime = proplists:get_value(activeTime,H,""),
	
	if
		Ip1 =:= Ip andalso Mac1 =:= Mac ->
			if
				ActiveTime =:= Time ->
					nnm_discovery_dal:writeToDB(?NNM_CHANGEIPMAC_TABLE,[lists:keyreplace(activeTime, 1, H, {activeTime,nnm_discovery_util:getLocalTime()})]);
				true ->
					Data = lists:keyreplace(activeTime, 1, H, {activeTime,nnm_discovery_util:getLocalTime()}),
					Count = proplists:get_value(count,Data,0),
					nnm_discovery_dal:writeToDB(?NNM_CHANGEIPMAC_TABLE,[lists:keyreplace(count, 1, H, {count,Count + 1})])
			end,
			find;
		true ->
			find_ipmac_change(Ip,Mac,Time,ChangeIpMac)
	end.
		
compare_macip([],_,_) ->[];
compare_macip([H|T],ActiveIpMac,ChangeIpMac) ->
	{Mac,Ip} = H,
	case proplists:get_value(Ip,ActiveIpMac,[]) of
		[] ->
			compare_macip(T,ActiveIpMac,ChangeIpMac);
		Mac ->
			compare_macip(T,ActiveIpMac,ChangeIpMac);
		Mac1 ->
			MacChangeTime = get_activeTime(newIp,Ip,ChangeIpMac,[]),
			case find_ipmac_change(Ip,Mac1,MacChangeTime,ChangeIpMac) of
				find -> [];
				_ ->
					Data = [{oldIp,Ip},{oldMac,Mac},{newIp,Ip},{newMac,Mac1},{discoverTime,nnm_discovery_util:getLocalTime()},
							{activeTime,nnm_discovery_util:getLocalTime()},{count,1},{info,[]}],
					nnm_discovery_dal:writeToDB(?NNM_CHANGEIPMAC_TABLE,[Data])
			end,
			compare_macip(T,ActiveIpMac,ChangeIpMac)
	end.

%% find new ip,update activeTime
store_new_device(ArpTable) ->
	DeviceList = api_machine:getAllMachine(0,0,"",""),
	PcList = nnm_discovery_dal:readFromDB(?NNM_DISCOVERY_PCLIST_TABLE,[id,ip,mac,discoverTime,activeTime,info],[],""),
	ArpDeviceList = getDeviceFromArp(ArpTable,[]),
	[findIpId(X,DeviceList,PcList) || X <- ArpDeviceList].

formatData(DeviceList,ArpTable,AftTable,InterfaceTable) -> 
	Device = formatData_device(DeviceList),
	Arp = formatData_arp(ArpTable),
	Aft = formatData_aft(AftTable),
	Interface = formatData_interface(InterfaceTable),
	
	{Device,Arp,Aft,Interface}.

formatData_device([]) -> [];
formatData_device([H|T]) ->
	Other = H#machine.other,
	Ip = H#machine.host,
	DevTypeName = H#machine.type,
	
	[lists:append(Other, [{ip,Ip},{devTypeName,DevTypeName}]) | formatData_device(T)].

formatData_arp([]) -> [];
formatData_arp([H|T]) ->
	{_,Ip,Arp} = H,
	FArp = [[{ifIndex,IfIndex},{ip,AIp},{mac,Mac}] || {IfIndex,Mac,AIp,_Type} <- Arp],
	[{Ip,FArp}|formatData_arp(T)].

formatData_aft([]) -> [];
formatData_aft([H|T]) ->
	{_,Ip,Aft} = H,
	FAft = [[{port,Port},{mac,Address}] || {Port,Address,_Status} <- Aft],
	[{Ip,FAft} | formatData_aft(T)].

formatData_interface([]) -> [];
formatData_interface([H|T]) ->
	{Ip,Interface} = H,
	FInterface = [[{ifIndex,IfIndex},{ifDescr,IfDescr},{ifMac,IfMac},{ifSpeed,IfSpeed},{ifPort,IfPort},{ifAlias,IfAlias}] 
				 || {IfIndex,IfDescr,IfMac,IfSpeed,IfAlias,IfPort} <- Interface],
	[{Ip,FInterface} | formatData_interface(T)].
		
get_monitor_port(Edges) ->
	L = get_monitor_port(Edges,[]),
%% 	io:format("L~p~n",[L]),
	K = proplists:get_keys(L),
	[{X,proplists:get_all_values(X, L)} || X <- K].

get_monitor_port([],Result) ->	Result;
get_monitor_port([H|T],Result) -> 
	LeftId = proplists:get_value(leftDeviceId,H),
	LeftIndex = proplists:get_value(leftIndex,H),
	RightId = proplists:get_value(rightDeviceId,H),
	RightIndex = proplists:get_value(rightIndex,H),
	
	case lists:member({LeftId,LeftIndex}, Result) of
		true -> 
			case RightIndex =:= "0" orelse lists:member({RightId,RightIndex}, Result) of
				true -> get_monitor_port(T,Result);
				false -> get_monitor_port(T,[{RightId,RightIndex}|Result])
			end;
		false ->
			TR = 
				case LeftIndex =:= "0" of
					true -> Result;
					false -> [{LeftId,LeftIndex}|Result]
				end,
			case RightIndex =:= "0" orelse lists:member({RightId,RightIndex}, Result) of
				true -> get_monitor_port(T,TR);
				false -> get_monitor_port(T,[{RightId,RightIndex}|TR])
			end
	end.

computeFlow(Flow1,[]) ->
	lists:map(fun(X) ->
%% 					  io:format("X:~p~n",[X]),
					  {Id,Ip,Info} = X,
					  Result = 
						  lists:map(fun(Y) ->
											Index = proplists:get_value(ifIndex,Y,""),
											{AdminStatus,_} = proplists:get_value(ifAdminStatus,Y),
											{OperStatus,_} = proplists:get_value(ifOperStatus,Y),
											[{index,Index},{adminStatus,AdminStatus},{operStatus,OperStatus},
	  											{octets,["0","0","0"]},{ucastPkts,["0","0","0"]},{nUcastPkts,["0","0","0"]},
	  											{discards,["0","0","0"]},{errors,["0","0","0"]},{bandwidthRate,"0"}]
									end, Info),
					  {Id,Ip,Result} 
			  end, Flow1);
computeFlow([],_) -> [];
computeFlow([H|T],Flow2) ->
	{Id0,Ip0,D0} = H,
	FL = lists:filter(fun(X) -> 
							  {L1,_,_} = X,
							  L1 =:= Id0
					  end, Flow2),
	Result = 
		case FL of
			[] ->
				lists:map(fun(Y) ->
									Index = proplists:get_value(ifIndex,Y,""),
									{AdminStatus,_} = proplists:get_value(adminStatus,Y),
									{OperStatus,_} = proplists:get_value(operStatus,Y),
									[{index,Index},{adminStatus,AdminStatus},{operStatus,OperStatus},
	  									{octets,["0","0","0"]},{ucastPkts,["0","0","0"]},{nUcastPkts,["0","0","0"]},
	  									{discards,["0","0","0"]},{errors,["0","0","0"]},{bandwidthRate,"0"}]
						  end, D0);
			[{_Id1,_Ip1,D1}|_] ->
				computeFlow_I(D0,D1)
		end,
	[{Id0,Ip0,Result}|computeFlow(T,Flow2)].
	
computeFlow_I([],_) -> [];
computeFlow_I([H|D0],D1) -> 
%% 	io:format("H~p~n",[H]),
	Index = proplists:get_value(ifIndex,H),
	FD = lists:filter(fun(X) ->
						   proplists:get_value(ifIndex,X) =:= Index
				   end, D1),
	{AdminStatus,_} = proplists:get_value(ifAdminStatus,H),
	{OperStatus,_} = proplists:get_value(ifOperStatus,H),
	{Speed,_} = proplists:get_value(ifSpeed,H),
%% 	io:format("FD:~p~n",[FD]),
	case FD of
		[] ->
			[[{index,Index},{adminStatus,AdminStatus},{operStatus,OperStatus},
	  		 {octets,["0","0","0"]},{ucastPkts,["0","0","0"]},{nUcastPkts,["0","0","0"]},
	  		 {discards,["0","0","0"]},{errors,["0","0","0"]},{bandwidthRate,"0"}] |
			computeFlow_I(D0,D1)];
		[R|_] ->
			{InOctets0,T1} = proplists:get_value(ifInOctets,H),
			{InUcastPkts0,T2} = proplists:get_value(ifInUcastPkts,H),
			{InNUcastPkts0,T3} = proplists:get_value(ifInNUcastPkts,H),
			{InDiscards0,T4} = proplists:get_value(ifInDiscards,H),
			{InErrors0,T5} = proplists:get_value(ifInErrors,H),
			{OutOctets0,T6} = proplists:get_value(ifOutOctets,H),
			{OutUcastPkts0,T7} = proplists:get_value(ifOutUcastPkts,H),
			{OutNUcastPkts0,T8} = proplists:get_value(ifOutNUcastPkts,H),
			{OutDiscards0,T9} = proplists:get_value(ifOutDiscards,H),
			{OutErrors0,T10} = proplists:get_value(ifOutErrors,H),
	
			{InOctets1,T11} = proplists:get_value(ifInOctets,R),
			{InUcastPkts1,T12} = proplists:get_value(ifInUcastPkts,R),
			{InNUcastPkts1,T13} = proplists:get_value(ifInNUcastPkts,R),
			{InDiscards1,T14} = proplists:get_value(ifInDiscards,R),
			{InErrors1,T15} = proplists:get_value(ifInErrors,R),
			{OutOctets1,T16} = proplists:get_value(ifOutOctets,R),
			{OutUcastPkts1,T17} = proplists:get_value(ifOutUcastPkts,R),
			{OutNUcastPkts1,T18} = proplists:get_value(ifOutNUcastPkts,R),
			{OutDiscards1,T19} = proplists:get_value(ifOutDiscards,R),
			{OutErrors1,T20} = proplists:get_value(ifOutErrors,R),
									   
			F = fun(X1,X2,X3,X4) ->
						if
							(X3 - X4) =< 0 ->
								0;
							X2 =:= 0 ->
								0;
							true ->
								(X1 - X2) div (X3 - X4)
						end
				end,
%% 			io:format("time:~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p~n",
%% 					  [T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20]),
%% 			io:format("jiankongshuju:~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p~n",
%% 					  [InOctets0,OutOctets0,InUcastPkts0,OutUcastPkts0,InNUcastPkts0,OutNUcastPkts0,InDiscards0,OutDiscards0,InErrors0,OutErrors0,
%% 					   InOctets1,OutOctets1,InUcastPkts1,OutUcastPkts1,InNUcastPkts1,OutNUcastPkts1,InDiscards1,OutDiscards1,InErrors1,OutErrors1]),
			
			InOctets = F(InOctets0,InOctets1,T1,T11) * 8,
			InUcastPkts = F(InUcastPkts0,InUcastPkts1,T2,T12),
			InNUcastPkts = F(InNUcastPkts0,InNUcastPkts1,T3,T13),
			InDiscards = F(InDiscards0,InDiscards1,T4,T14),
			InErrors = F(InErrors0,InErrors1,T5,T15),
			OutOctets = F(OutOctets0,OutOctets1,T6,T16) * 8,
			OutUcastPkts = F(OutUcastPkts0,OutUcastPkts1,T7,T17),
			OutNUcastPkts = F(OutNUcastPkts0,OutNUcastPkts1,T8,T18),
			OutDiscards = F(OutDiscards0,OutDiscards1,T9,T19),
			OutErrors = F(OutErrors0,OutErrors1,T10,T20),
	
			Octets = InOctets + OutOctets,
			UcastPkts = InUcastPkts + OutUcastPkts,
			NUcastPkts = InNUcastPkts + OutNUcastPkts,
			Discards = InDiscards + OutDiscards,
			Errors = InErrors + OutErrors,
%% 			io:format("speed:~p~n",[Speed]),
			BandwidthRate = 
				case Speed of
					[] -> 0.00;
					0 -> 0.00;
					_ -> 
						if
							Octets =< 0 -> 0.0;
							true -> Octets / Speed
						end
				end,
			F1 = fun(X) ->
						if 
							X =< 0 -> 
								"0";
							true -> 
								nnm_discovery_util:floatToString(X,0)
						end
				end,
%% 			io:format("data:~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p,~p~n",
%% 					  [Octets,InOctets,OutOctets,UcastPkts,InUcastPkts,OutUcastPkts,NUcastPkts,InNUcastPkts,OutNUcastPkts,
%% 								   Discards,InDiscards,OutDiscards,Errors,InErrors,OutErrors]),
			Result = [{index,Index},{adminStatus,AdminStatus},{operStatus,OperStatus},
	  			{octets,[F1(Octets),F1(InOctets),F1(OutOctets)]},
	  			{ucastPkts,[F1(UcastPkts),F1(InUcastPkts),F1(OutUcastPkts)]},
	  			{nUcastPkts,[F1(NUcastPkts),F1(InNUcastPkts),F1(OutNUcastPkts)]},
	  			{discards,[F1(Discards),F1(InDiscards),F1(OutDiscards)]},
	  			{errors,[F1(Errors),F1(InErrors),F1(OutErrors)]},
	  			{bandwidthRate,nnm_discovery_util:floatToString(BandwidthRate,4)}],
			
			[Result|computeFlow_I(D0,D1)]
	end.
	
									   
	
	
	

	
	
	









  
