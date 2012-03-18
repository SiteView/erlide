-module(nnm_discovery_scan).

-behaviour(gen_server).
-export([start_link/0]).
%% gen_server callbacks
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
terminate/2, code_change/3]).

-export([start_scan/1,get_scan_info/1,stop_scan/0]).

-include("nnm_discovery_setting.hrl").
-include("nnm_discovery_topo.hrl").
-include("nnm_define.hrl").

-define(SERVER, nnm_discovery_scan_server).
-define(THREAD, 10).
-define(TIMEOUT, 7200000).
-define(SNMP_PING_OID, [1,3,6,1,2,1,1,2,0]).

-record(state, 
		{status="free", scanpid=[], scanInfo=[], scanResult=[], scanParam=[], 
		 sysobjectIdList=[], specialOidList=[], defaultSnmpParam=[], definedSnmpParam=[], 
		 telnetList=[], telnetParam=[]}
	   ).

start_link() -> gen_server:start_link({local, ?SERVER}, ?MODULE, [], []).

init([]) -> 
	{ok, #state{status=start}}.

start_scan(Param) ->
	start_link(),
	ConfigId = proplists:get_value(configId, Param),
	ParentPid = self(),
	case gen_server:call(?SERVER, {start_scan, ConfigId, ParentPid}) of
		{ok, ReqId} ->
			{ok,ReqId};
%% 			receive
%% 				{error, Reason} ->
%% %% 					gen_server:cast(?SERVER, {removeR2PState, ReqId}),
%% 					{error, Reason};
%% 				{ReqId, Return} ->
%% 					change_server_state(free),
%% 					{ok, Return}
%% 			after ?TIMEOUT ->
%% %% 					gen_server:cast(?SERVER, {removeR2PState, ReqId}), 
%% 					{error, timeout}
%% 			end;
		{error, Reason} ->
			{error, Reason}
	end.

print_scan_info(Info) ->
	gen_server:call(?SERVER, {print_scan_info, Info}).

get_scan_info(Param) ->
	gen_server:call(?SERVER, {get_scan_info,Param}).

stop_scan() ->
	nnm_discovery_util:writeFile(["asdfsdf"],"stop.txt",write),
	io:format("stop scan."),
	nnm_monitor:start_monitor(),
	gen_server:call(?SERVER, {stop_scan}).

handle_call({start_scan, ConfigId, _ParentPid}, _From, State) -> 
	case State#state.status of
		"busy" ->
			{reply, {error, "busy"}, State};
		_ ->
			nnm_monitor:stop_monitor(),
			{ok,SysobjectIdList} = file:consult(?NNM_DISCOVERY_SYSOBJECTPATH),
			{ok,SpecialOidList} = file:consult(?NNM_DISCOVERY_SPECIALOIDLISTPATH),
			{ok,TelnetList} = file:consult(?NNM_DISCOVERY_TELNETLISTPATH),
			{ok,TelnetParam} = file:consult(?NNM_DISCOVERY_TELNETPARAMPATH),
			io:format("start"),
			ScanConfig = nnm_discovery_dal:getScanConfig([{id,ConfigId}]),
			DefaultSnmpParam = proplists:get_value(defaultSnmpParam, ScanConfig, []),
			DefineSnmpParam = proplists:get_value(defineSnmpParam, ScanConfig, []),
			ScanParam = proplists:get_value(scanParam, ScanConfig, []),
			
			NState = 
				State#state{
							sysobjectIdList=SysobjectIdList,
							specialOidList=SpecialOidList,
							scanParam=ScanParam,
							defaultSnmpParam=DefaultSnmpParam,
							definedSnmpParam=DefineSnmpParam,
							telnetList=TelnetList,
							telnetParam=TelnetParam
							},
			Pid = spawn(fun() -> scan_manager(NState) end),
			on_scan_exit(Pid),
			{reply, {ok, ConfigId}, NState#state{status="busy",scanpid=Pid}}
	end;
handle_call({stop_scan}, _From, State) ->
%% 	io:format("State:~p~n",[State]),
	case State#state.scanpid of
		[] -> {reply,error,State};
		Pid -> 
			erlang:exit(Pid, stop),
			{reply,ok,State#state{status="free", scanpid=[], scanInfo=[], scanResult=[], scanParam=[], 
		 							sysobjectIdList=[], specialOidList=[], defaultSnmpParam=[], definedSnmpParam=[], 
		 							telnetList=[], telnetParam=[]}}
	end;
handle_call({print_scan_info, Info}, _From, State) ->
%% 	io:format("Info:~p~n",[Info]),
	ScanInfo = State#state.scanInfo ++ [Info],
	{reply, [], State#state{scanInfo=ScanInfo}};
handle_call({get_scan_info,Param}, _From, State) ->
	Ind = proplists:get_value(index,Param),
	Index = 
		if
			Ind =< 0 ->
				0;
			true ->
				Ind
		end,
	ScanInfo = State#state.scanInfo,
	Len = length(ScanInfo)-Index+1,
	if
		Len =< 0 ->
		  {reply, [], State};
		true ->
			Info = lists:sublist(ScanInfo, Index, Len),
			{reply, Info, State}
	end.

handle_cast(_Msg, State) -> {noreply, State}.
handle_info(_Info, State) -> {noreply, State}.
terminate(_Reason, _State) -> ok.
code_change(_OldVsn, State, _Extra) -> {ok, State}.

on_scan_exit(Pid) ->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						io:format("why:~p~n",[Why]),
%%  						print_scan_info(ProfileId,Why),
						print_scan_info("end scan")
				end
				end).

scan_manager(State) -> 
	print_scan_info("start scan..."),
	ScanParam = State#state.scanParam,
	ScanType = proplists:get_value(scanType, ScanParam, "snmpping"),
	
	{NetworkDeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo} =
		case proplists:get_value(includeIp, ScanParam, []) of
			[] ->
				case ScanType of
					"nmap" ->
						scan_manager_nmap(State);
					"arp" ->
						scan_manager_arp(State);
					"file" ->
						scan_manager_file(State);
					_ ->
						scan_manager_normal(State)
				end;
			IncludeIp ->
				scan_manager_ip_segment(State,IncludeIp)
		end,
	
 	nnm_discovery_util:writeFile(AftInfo,"AftInfo.txt",write),
 	nnm_discovery_util:writeFile(ArpInfo,"ArpInfo.txt",write),
 	nnm_discovery_util:writeFile(NetworkDeviceInfo,"NetworkDeviceInfo.txt",write),
	nnm_discovery_util:writeFile(RouteInfo,"RouteInfo.txt",write),
	nnm_discovery_util:writeFile(InterfaceInfo,"InterfaceInfo.txt",write),
	
	FNetworkDeviceInfo = nnm_discovery_dal:saveDeviceInfo(NetworkDeviceInfo),
	EdgeInfo = analyseScanInfo(FNetworkDeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo),
	LogicEdgeInfo = analyseScanInfo(FNetworkDeviceInfo,InterfaceInfo,RouteInfo,ArpInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo),
	nnm_discovery_util:writeFile(EdgeInfo,"EdgeInfo.txt",write),
	nnm_discovery_util:writeFile(LogicEdgeInfo,"LogicEdgeInfo.txt",write),
	
	FEdgeInfo = formatEdge(EdgeInfo,FNetworkDeviceInfo),
	FLogicEdgeInfo = formatEdge(LogicEdgeInfo,FNetworkDeviceInfo),

%% 	coordinate compute
	CoordinateDevice = calculateCoordinate(FEdgeInfo,FNetworkDeviceInfo),
%% 	update device
	nnm_discovery_dal:savaCoordinateDevice(CoordinateDevice),
%% 	save edge
	Version = [{name,"topoChart"},{type,"total"},{time,nnm_discovery_util:getLocalTime()}],
	nnm_discovery_dal:saveEdgeInfo(FEdgeInfo,Version),
	ok.
	

%%----------------------------------------------------------------------------------------------
%% use nmap scan 
%%----------------------------------------------------------------------------------------------
scan_manager_nmap(State) -> 
	ScanParam = State#state.scanParam,
	ExcludeIpList = getIpList(proplists:get_value(excludeIp, ScanParam, [])),
	Threads = proplists:get_value(threads, ScanParam, 50),
	Depth = proplists:get_value(depth, ScanParam, 3),
	
	ScanIpList = getIpList(proplists:get_value(scanSeed, ScanParam, [])),
	NetworkDeviceList =	nmap_scanNext(Depth+1,Threads,State,ScanIpList,ExcludeIpList,[]),
%% 	io:format("NetworkDeviceList:~p~n,NetworkSnmpList:~p~n",[NetworkDeviceList,NetworkSnmpList]),
	NetworkDeviceInfo = get_networkDeviceInfo(State,NetworkDeviceList,Threads,[],0),
	{InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo} = 
		mergeScanResult(NetworkDeviceInfo,[],[],[],[],[],[],[]),
%% 	io:format("NetworkDeviceInfo:~p~nInterfaceInfo:~p~n",[NetworkDeviceList,InterfaceInfo]),
	FNetworkDeviceList = [[{infs,proplists:get_value(inrec,proplists:get_value(proplists:get_value(ip,Y,""),InterfaceInfo,[]),[])}|Y]
						 || Y <- NetworkDeviceList],
	{FNetworkDeviceList,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo}.

nmap_scanNext(0,_,_,_,_,NetworkDeviceList) -> NetworkDeviceList;
nmap_scanNext(_,_,_,[],_,NetworkDeviceList) -> NetworkDeviceList;
nmap_scanNext(Depth,Threads,State,ScanIpList,ExcludeIpList,NetworkDeviceList) ->
	io:format("scan next"),
	FilterIpList = lists:filter(fun(X) -> not lists:member(X, ExcludeIpList) end, ScanIpList),
	SnmpParamList = get_snmpParamList(FilterIpList,State#state.defaultSnmpParam,State#state.definedSnmpParam),
	
	NetworkList = snmp_ping(State,SnmpParamList,Threads,[],0),
	
	DeviceIps = lists:append([[proplists:get_value(ipAdEntAddr, Y) || Y <- proplists:get_value(ipAddr,X,[])] || X <- NetworkList]),
	NExcludeIpList = lists:append([DeviceIps,ScanIpList, ExcludeIpList]),
	{NNExcludeIpList,NScanIpList} = nmap_getNextScanIpList(NetworkList,NExcludeIpList,[]),
	nnm_discovery_util:writeFile(NScanIpList,"snmpdevice.txt",append),
	nmap_scanNext(Depth-1,Threads,State,NScanIpList,NNExcludeIpList,lists:append(NetworkList,NetworkDeviceList)).

nmap_getNextScanIpList([],ExcludeIpList,Result) -> {ExcludeIpList,Result};
nmap_getNextScanIpList([H|NetworkList],ExcludeIpList,Result) ->
	Type = proplists:get_value(devType,H,""),
 	case ((Type =:= ?NNM_DEVICE_TYPE_ROUTE_SWITCH) orelse (Type =:= ?NNM_DEVICE_TYPE_SWITCH) orelse (Type =:= ?NNM_DEVICE_TYPE_ROUTE)) of
		true ->
			IpAddr = proplists:get_value(ipAddr,H,[]),
			{NExcludeIpList,Iplist} = namp_getNextScanIpList_one(IpAddr,ExcludeIpList,Result),
			nmap_getNextScanIpList(NetworkList,NExcludeIpList,Iplist);
		false ->
			nmap_getNextScanIpList(NetworkList,ExcludeIpList,Result)
	end.

namp_getNextScanIpList_one([],ExcludeIpList,Result) -> {ExcludeIpList,Result};
namp_getNextScanIpList_one([H|IpAddr],ExcludeIpList,Result) ->
	IpAdEntAddr = proplists:get_value(ipAddress,H),
	IpAdEntNetMask = proplists:get_value(ipMask,H),
	if
		IpAdEntAddr =:= "10.254.255.2" ->
			namp_getNextScanIpList_one(IpAddr,ExcludeIpList,Result);
		true ->
	io:format("scan sub net:~p/~p~n",[IpAdEntAddr,IpAdEntNetMask]),
	{NExcludeIpList,IpList} = 
		if
			IpAdEntNetMask =:= "255.255.255.255" ->
				{ExcludeIpList,[]};
			IpAdEntAddr =:= "127.0.0.1" ->
				{ExcludeIpList,[]};
			true ->
				[I1,I2,I3,I4] = nnm_discovery_util:stringToIntegerList(IpAdEntAddr, "."),
				[M1,M2,M3,M4] = nnm_discovery_util:stringToIntegerList(IpAdEntNetMask, "."),
				<<Ip:4/big-integer-unit:8>> = <<I1,I2,I3,I4>>,
				<<Mask:4/big-integer-unit:8>> = <<M1,M2,M3,M4>>,
				<<All:4/big-integer-unit:8>> = <<255,255,255,255>>,
				Subnet = Ip band Mask,
				MinIp = Subnet + 1,
				MaxIp = ((All - Mask) bor Subnet) - 1,
				
				<<S1,S2,S3,S4>> = <<MinIp:4/big-integer-unit:8>>,
				<<E1,E2,E3,E4>> = <<MaxIp:4/big-integer-unit:8>>,
				
				StartIp = nnm_discovery_util:integerListToString([S1,S2,S3,S4],10, "."),
				EndIp = nnm_discovery_util:integerListToString([E1,E2,E3,E4],10, "."),
				SEIp = getIpList(StartIp ++ "-" ++ EndIp),
				case lists:filter(fun(X) -> lists:member(X, ExcludeIpList) end, SEIp) =:= SEIp of
					true ->
						{ExcludeIpList,[]};
					false ->
						SnmpIpList = nmapScanMIpSegment(S1,S2,S3,S4,E1,E2,E3,E4),
%% 							case S3 =:= S4 of
%% 								true -> 
%% 									IpSegment = StartIp ++ "-" ++ integer_to_list(E4),
%% 									getIpListUseNmap(IpSegment);
%% 								_ -> 
%% 									nmapScanMIpSegment(S1,S2,S3,S4,E1,E2,E3,E4)
%% 							end,
%% 						SnmpIpList = getIpListUseNmap(IpSegment),
						NE = lists:subtract(SEIp, SnmpIpList),
						{lists:append(NE, ExcludeIpList),SnmpIpList}
				end
		end,
	ScanIpList = nnm_discovery_util:mergerList(Result,IpList),
	namp_getNextScanIpList_one(IpAddr,NExcludeIpList,ScanIpList)
	end.

nmapScanMIpSegment(S1,S2,S3,S4,_E1,_E2,S3,E4) -> 
	IpSegment = nnm_discovery_util:integerListToString([S1,S2,S3,S4],10, ".") ++ "-" ++ integer_to_list(E4),
	getIpListUseNmap(IpSegment);
nmapScanMIpSegment(S1,S2,S3,S4,E1,E2,E3,E4) ->
	IpSegment = nnm_discovery_util:integerListToString([S1,S2,S3,S4],10, ".") ++ "-254",
	lists:append(getIpListUseNmap(IpSegment),nmapScanMIpSegment(S1,S2,S3+1,S4,E1,E2,E3,E4)).

getIpListUseNmap(IpSegment) ->
	io:format("Ip:~p~n",[IpSegment]),
	case nmap_scan:scan(IpSegment,["-sU -p 161"]) of
		[] -> [];
		R ->
			io:format("discovery ip:~p~n",[R]),
			[proplists:get_value(ip,X) 
			|| X <- R,Y<-[proplists:get_value(service,proplists:get_value(port,proplists:get_value(ports,X,[]),[]))],Y=:="snmp"]
	end.
			

%%----------------------------------------------------------------------------------------------
%% use arp scan 
%%----------------------------------------------------------------------------------------------
scan_manager_arp(State) -> 
	ScanParam = State#state.scanParam,
	ExcludeIpList = getIpList(proplists:get_value(excludeIp, ScanParam, "")),
	Depth = proplists:get_value(depth, ScanParam, 3),
	ScanIpList = getIpList(proplists:get_value(scanSeed, ScanParam, [])),
	{NetworkDeviceList,NetworkDeviceInfo} = arp_scanNext(Depth+1,State,ScanIpList,ExcludeIpList,{[],[]}),
	{InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo} = 
		mergeScanResult(NetworkDeviceInfo,[],[],[],[],[],[],[]),
	{NetworkDeviceList,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo}.

%%---------------------------------------------------------------------------------------------
%% arp scan next hope
%%---------------------------------------------------------------------------------------------
arp_scanNext(0,_,_,_,{NetworkDeviceList,NetworkDeviceInfo}) -> {NetworkDeviceList,NetworkDeviceInfo};
arp_scanNext(_,_,[],_,{NetworkDeviceList,NetworkDeviceInfo}) -> {NetworkDeviceList,NetworkDeviceInfo};
arp_scanNext(Depth,State,ScanIpList,ExcludeIpList,{NetworkDeviceList,NetworkDeviceInfo}) ->
	ScanParam = State#state.scanParam,
	Threads = proplists:get_value(threads, ScanParam, 50),
	
	FilterIpList = lists:filter(fun(X) -> not lists:member(X, ExcludeIpList) end, ScanIpList),
	SnmpParamList = get_snmpParamList(FilterIpList,State#state.defaultSnmpParam,State#state.definedSnmpParam),
 	NetworkList = snmp_ping(State,SnmpParamList,Threads,[],0),
	NetworkInfo = get_networkDeviceInfo(State,NetworkList,Threads,[],0),
	
	DeviceIps = lists:append([[proplists:get_value(ipAdEntAddr, Y) || Y <- proplists:get_value(ipAddr,X,[])] || X <- NetworkList]),
	NScanIpList = arp_getNextScanIpList(NetworkInfo,[]),
	arp_scanNext(Depth-1,State,NScanIpList,lists:append([DeviceIps,ScanIpList, ExcludeIpList]),
				 {lists:append(NetworkList,NetworkDeviceList),lists:append(NetworkInfo, NetworkDeviceInfo)}).
	
%%----------------------------------------------------------------------------------------------
%% normal scan
%%----------------------------------------------------------------------------------------------
scan_manager_normal(State) -> 
	ScanParam = State#state.scanParam,
	ExcludeIpList = getIpList(proplists:get_value(excludeIp, ScanParam, [])),
	Threads = proplists:get_value(threads, ScanParam, 50),
	Depth = proplists:get_value(depth, ScanParam, 3),
	
	ScanIpList = getIpList(proplists:get_value(scanSeed, ScanParam, [])),
	NetworkDeviceList =	normal_scanNext(Depth+1,Threads,State,ScanIpList,ExcludeIpList,[]),
%% 	io:format("NetworkDeviceList:~p~n,NetworkSnmpList:~p~n",[NetworkDeviceList,NetworkSnmpList]),
	NetworkDeviceInfo = get_networkDeviceInfo(State,NetworkDeviceList,Threads,[],0),
	{InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo} = 
		mergeScanResult(NetworkDeviceInfo,[],[],[],[],[],[],[]),
%% 	io:format("NetworkDeviceInfo:~p~nInterfaceInfo:~p~n",[NetworkDeviceList,InterfaceInfo]),
	FNetworkDeviceList = [[{infs,proplists:get_value(inrec,proplists:get_value(proplists:get_value(ip,Y,""),InterfaceInfo,[]),[])}|Y]
						 || Y <- NetworkDeviceList],
	{FNetworkDeviceList,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo}.
	

%%---------------------------------------------------------------------------------------------
%% normal scan next hope
%%---------------------------------------------------------------------------------------------
normal_scanNext(0,_,_,_,_,NetworkDeviceList) -> NetworkDeviceList;
normal_scanNext(_,_,_,[],_,NetworkDeviceList) -> NetworkDeviceList;
normal_scanNext(Depth,Threads,State,ScanIpList,ExcludeIpList,NetworkDeviceList) ->
	FilterIpList = lists:filter(fun(X) -> not lists:member(X, ExcludeIpList) end, ScanIpList),
	SnmpParamList = get_snmpParamList(FilterIpList,State#state.defaultSnmpParam,State#state.definedSnmpParam),
	NetworkList = snmp_ping(State,SnmpParamList,Threads,[],0),
	
	DeviceIps = lists:append([[proplists:get_value(ipAdEntAddr, Y) || Y <- proplists:get_value(ipAddr,X,[])] || X <- NetworkList]),
	NScanIpList = normal_getNextScanIpList(NetworkList,[]),
	normal_scanNext(Depth-1,Threads,State,NScanIpList,lists:append([DeviceIps,ScanIpList, ExcludeIpList]),
					lists:append(NetworkList,NetworkDeviceList)).

%% ------------------------------------------------------------------------------------------------
%% scan ip segment
%% ------------------------------------------------------------------------------------------------
scan_manager_ip_segment(State,IncludeIp) ->
	ScanParam = State#state.scanParam,
	ExcludeIpList = getIpList(proplists:get_value(excludeIp, ScanParam, "")),
	Threads = proplists:get_value(threads, ScanParam, 50),
	
	IncludeIpList = getIpList(IncludeIp),
	ScanIpList = lists:filter(fun(X) -> not lists:member(X, ExcludeIpList) end, IncludeIpList),
	SnmpParamList = get_snmpParamList(ScanIpList,State#state.defaultSnmpParam,State#state.definedSnmpParam),
	NetworkList = snmp_ping(State,SnmpParamList,Threads,[],0),
	NetworkDeviceList =	filter_networkdevice(NetworkList),
	NetworkDeviceInfo = get_networkDeviceInfo(State,NetworkDeviceList,Threads,[],0),
	{InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo} = 
		mergeScanResult(NetworkDeviceInfo,[],[],[],[],[],[],[]),
%% 	io:format("NetworkDeviceInfo:~p~nInterfaceInfo:~p~n",[NetworkDeviceList,InterfaceInfo]),
	FNetworkDeviceList = [[{infs,proplists:get_value(inrec,proplists:get_value(proplists:get_value(ip,Y,""),InterfaceInfo,[]),[])}|Y]
						 || Y <- NetworkDeviceList],
	{FNetworkDeviceList,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo}.

%%---------------------------------------------------------------------------------------------
%% one device has many ips,delete the same device
%%---------------------------------------------------------------------------------------------
filter_networkdevice([]) -> [];
filter_networkdevice([H|T]) ->
	Ip = proplists:get_value(ip,H,""),
	case lists:filter(fun(X) -> lists:member(Ip, [proplists:get_value(ipAdEntAddr,Y) || Y <- proplists:get_value(ipAddr,X,[])]) end, T) of
		[] -> [H|filter_networkdevice(T)];
		R -> [H|filter_networkdevice(lists:subtract(T, R))]
	end.


%%----------------------------------------------------------------------------------------------
%% use snmp find network device 
%%----------------------------------------------------------------------------------------------
snmp_ping(_,[],_,NetworkDeviceList,0) -> NetworkDeviceList;
snmp_ping(State,[],Threads,NetworkDeviceList,C) ->
	receive
		{ping_done,_Why} ->
			snmp_ping(State,[],Threads,NetworkDeviceList,C-1);
		{ping_result,NetworkDevice} ->
			snmp_ping(State,[],Threads,[NetworkDevice|NetworkDeviceList],C);
		_ ->
			snmp_ping(State,[],Threads,NetworkDeviceList,C)
	end;
snmp_ping(State,[H|T],Threads,NetworkDeviceList,C) ->
	receive
		{ping_done,_Why} ->
			start_ping(H,State),
			snmp_ping(State,T,Threads,NetworkDeviceList,C);
		{ping_result,NetworkDevice} ->
			snmp_ping(State,[H|T],Threads,[NetworkDevice|NetworkDeviceList],C);
		_ ->
			snmp_ping(State,[H|T],Threads,NetworkDeviceList,C)
	after 10 ->
		if
			C < Threads ->
					start_ping(H,State),
					snmp_ping(State,T,Threads,NetworkDeviceList,C+1);
				true->
					snmp_ping(State,[H|T],Threads,NetworkDeviceList,C)
		end
	end.

start_ping(SnmpParam,State) ->
	ParentId = self(),
	Pid = spawn(fun()->do_ping(SnmpParam,ParentId,State) end),
	on_ping_exit(Pid,self()).

do_ping(SnmpParam,ParentId,State) ->
	SpecialOidList = State#state.specialOidList,
	SysobjectIdList= State#state.sysobjectIdList,
	SnmpReadObject = nnm_discovery_snmp_read:new(SnmpParam,SpecialOidList,SysobjectIdList),
	
	case SnmpReadObject:readDeviceInfo() of
		[] ->
			ParentId ! false;
		NetworkDevice ->
			io:format("find device:~p,~p,~p~n", 
					  [proplists:get_value(ip,NetworkDevice,""),proplists:get_value(devTypeName,NetworkDevice,""),
					   proplists:get_value(devFactory,NetworkDevice,"")]),
			print_scan_info("find device:" ++ proplists:get_value(ip,NetworkDevice,"") ++ " " ++ proplists:get_value(devTypeName,NetworkDevice,"") 
						   ++ " " ++ proplists:get_value(devFactory,NetworkDevice,"")),
			ParentId ! {ping_result,NetworkDevice}
	end.

on_ping_exit(Pid,ParentId) ->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						io:format("Ping Pid:~p,Exit:~p~n",[Pid,Why]),
						ParentId ! {ping_done,Why}
				end
				end).
%%----------------------------------------------------------------------------------------------


%%----------------------------------------------------------------------------------------------
%% use snmp get device info 
%%----------------------------------------------------------------------------------------------
get_networkDeviceInfo(_,[],_,Result,0) -> Result;
get_networkDeviceInfo(State,[],Threads,Result,C)->
	receive
		{scan_done,_Why}->
			%%io:format("C:~p~n",[C]),
			get_networkDeviceInfo(State,[],Threads,Result,C-1);
		{scan_result,Value} ->
			get_networkDeviceInfo(State,[],Threads,[Value|Result],C);
		_->
			get_networkDeviceInfo(State,[],Threads,Result,C)
	end;
get_networkDeviceInfo(State,[H|T],Threads,Result,C)->
	receive
		{scan_done,_Why}->
			start_scaner(H,State),
			get_networkDeviceInfo(State,T,Threads,Result,C);
		{scan_result,Value} ->
			get_networkDeviceInfo(State,[H|T],Threads,[Value|Result],C);
		_->
			get_networkDeviceInfo(State,[H|T],Threads,Result,C)
	after 20 ->
		Max = case Threads of
				Scount when is_integer(Scount)->
					Scount;
				_->
					?THREAD
			end,
		if
			C < Max -> % limit max scaner
				start_scaner(H,State),
				get_networkDeviceInfo(State,T,Threads,Result,C+1);
			true->
				get_networkDeviceInfo(State,[H|T],Threads,Result,C)
		end
	end.
	
start_scaner(DeviceEntity,State)->
	ParentId = self(),
	Pid = spawn(fun()->do_scan_host(DeviceEntity,State,ParentId) end),
	on_scaner_exit(Pid,self()).
	
do_scan_host(DeviceEntity,State,ParentId)->
	Result = getScanInfo(DeviceEntity,State),
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

getScanInfo(DeviceEntity,State) ->
	Ip = proplists:get_value(ip,DeviceEntity,""),
	SysobjectId = proplists:get_value(sysobjectId,DeviceEntity,""),
	DevType = proplists:get_value(devType,DeviceEntity,""),
	SnmpFlag = proplists:get_value(snmpFlag,DeviceEntity,""),
	SnmpParam = proplists:get_value(snmpParam,DeviceEntity,[]),
	TelnetFlag = proplists:get_value(telnetFlag,DeviceEntity,""),
	TelnetParam = proplists:get_value(telentParam,DeviceEntity,[]),
	SpecialOidList = State#state.specialOidList,
	TelnetList = State#state.telnetList,
	
	ScanObject = nnm_discovery_bll:new(SpecialOidList,TelnetList),
	Param = [{ip,Ip},{sysobjectId,SysobjectId},{snmpFlag,SnmpFlag},{snmpParam,SnmpParam},{telnetFlag,TelnetFlag},{telnetParam,TelnetParam}],
	
%% 	print_scan_info("start read device " ++ SnmpParam#nnm_snmpPara.ip ++ " info..."),
	case DevType of
		?NNM_DEVICE_TYPE_SWITCH ->
			io:format("start read device ~p info...~n", [Ip]),
			print_scan_info("start read device " ++ Ip),
			AftInfo = ScanObject:getScanValue(Param,?AFTTABLE),
			InterfaceInfo = ScanObject:getScanValue(Param,?INTERFACETABLE),
			io:format("end read device ~p info...~n", [Ip]),
			print_scan_info("end read device " ++ Ip),
			{InterfaceInfo,[],AftInfo,[],[],[],[]};
		?NNM_DEVICE_TYPE_ROUTE ->
			io:format("start read device ~p info...~n", [Ip]),
			print_scan_info("start read device " ++ Ip),
			InterfaceInfo = ScanObject:getScanValue(Param,?INTERFACETABLE),
			RouteInfo = ScanObject:getScanValue(Param,?ROUTETABLE),
			ArpInfo = ScanObject:getScanValue(Param,?ARPTABLE),
			OspfInfo = ScanObject:getScanValue(Param,?OSPFTABLE),
			BgpInfo = ScanObject:getScanValue(Param,?BGPTABLE),
			DirectInfo = ScanObject:getScanValue(Param,?DIRECTTABLE),
			io:format("end read device ~p info...~n", [Ip]),
			print_scan_info("end read device " ++ Ip),
			{InterfaceInfo,RouteInfo,[],ArpInfo,OspfInfo,BgpInfo,DirectInfo};
		?NNM_DEVICE_TYPE_ROUTE_SWITCH ->
			io:format("start read device ~p info...~n", [Ip]),
			print_scan_info("start read device " ++ Ip),
			InterfaceInfo = ScanObject:getScanValue(Param,?INTERFACETABLE),
			RouteInfo = ScanObject:getScanValue(Param,?ROUTETABLE),
			ArpInfo = ScanObject:getScanValue(Param,?ARPTABLE),
			OspfInfo = ScanObject:getScanValue(Param,?OSPFTABLE),
			BgpInfo = ScanObject:getScanValue(Param,?BGPTABLE),
			DirectInfo = ScanObject:getScanValue(Param,?DIRECTTABLE),
			AftInfo = ScanObject:getScanValue(Param,?AFTTABLE),
			io:format("end read device ~p info...~n", [Ip]),
			print_scan_info("end read device " ++ Ip),
			{InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo};
		_ ->
			{[],[],[],[],[],[],[]}
	end.
%%----------------------------------------------------------------------------------------------

%%----------------------------------------------------------------------------------------------
%% get ip from string split of ',' and '-'
%%----------------------------------------------------------------------------------------------
getIpList(IpStr) ->
	case string:rstr(IpStr, ",") of 
		0 ->
			case string:rstr(IpStr, "-") of
				0 ->
					[IpStr];
				_ ->
					[Ip1, Ip2] = string:tokens(IpStr, "-"),
					[I1,I2,I3,I4] = nnm_discovery_util:stringToIntegerList(Ip1, "."),
					[P1,P2,P3,P4] = nnm_discovery_util:stringToIntegerList(Ip2, "."),
					<<Int1:4/big-integer-unit:8>> = <<I1,I2,I3,I4>>,
					<<Int2:4/big-integer-unit:8>> = <<P1,P2,P3,P4>>,
					getIpListFromRange(Int1, Int2)
			end;
		_ ->
			RangeList = string:tokens(IpStr, ","),
			[getIpList(X) || X <- RangeList]
	end.

getIpListFromRange(Int1, Int2) when Int1 =:= Int2 + 1 -> [];
getIpListFromRange(Int1, Int2) -> 
	<<V1,V2,V3,V4>> = <<Int1:4/big-integer-unit:8>>,
	Ip = nnm_discovery_util:integerListToString([V1,V2,V3,V4],10, "."),
	[Ip|getIpListFromRange(Int1+1,Int2)].
	

%%----------------------------------------------------------------------------------------------
%% snmp Assignment
%%----------------------------------------------------------------------------------------------
get_snmpParamList(FilterIpList,DefaultSnmpParam,DefinedSnmpParam) ->
 	DefinedSnmpParamList = [lists:keyreplace(ip, 1, X, {ip,getIpList(proplists:get_value(ip, X, []))}) || X <- DefinedSnmpParam],
%% 	DefinedSnmpParamList = [X#nnm_deviceLoginType{ip=getIpList(X#nnm_deviceLoginType.ip)} || X <- DefinedSnmpParam],
%% 	io:format("DefinedSnmpParamList:~p~n",[DefinedSnmpParamList]),
	[put_snmpParamList(X,DefaultSnmpParam,DefinedSnmpParamList) || X <- FilterIpList].

put_snmpParamList(Ip,DefaultSnmpParam,[]) ->
	[{ip,Ip}|DefaultSnmpParam];
put_snmpParamList(Ip,DefaultSnmpParam,[H|DefinedSnmpParamList]) ->
	case lists:member(Ip, proplists:get_value(ip, H, [])) of
		true ->
			lists:keyreplace(ip, 1, H, {ip,Ip});
		false ->
			put_snmpParamList(Ip,DefaultSnmpParam,DefinedSnmpParamList)
	end.

%%----------------------------------------------------------------------------------------------
%% get next scan ip list from subnet
%%----------------------------------------------------------------------------------------------
normal_getNextScanIpList([],Result) -> Result;
normal_getNextScanIpList([H|NetworkList],Result) ->
	Type = proplists:get_value(devType,H,""),
 	case ((Type =:= ?NNM_DEVICE_TYPE_ROUTE_SWITCH) orelse (Type =:= ?NNM_DEVICE_TYPE_SWITCH) orelse (Type =:= ?NNM_DEVICE_TYPE_ROUTE)) of
		true ->
			IpAddr = proplists:get_value(ipAddr,H,[]),
			Iplist = normal_getNextScanIpList_one(IpAddr,Result),
			normal_getNextScanIpList(NetworkList,Iplist);
		false ->
			normal_getNextScanIpList(NetworkList,Result)
	end.

normal_getNextScanIpList_one([],Result) -> Result;
normal_getNextScanIpList_one([H|IpAddr],Result) ->
	IpAdEntAddr = proplists:get_value(ipAddress,H),
	IpAdEntNetMask = proplists:get_value(ipMask,H),
	io:format("scan sub net:~p/~p~n",[IpAdEntAddr,IpAdEntNetMask]),
	IpList = 
		if
			IpAdEntNetMask =:= "255.255.255.255" ->
				[];
			IpAdEntAddr =:= "127.0.0.1" ->
				[];
			true ->
				[I1,I2,I3,I4] = nnm_discovery_util:stringToIntegerList(IpAdEntAddr, "."),
				[M1,M2,M3,M4] = nnm_discovery_util:stringToIntegerList(IpAdEntNetMask, "."),
				<<Ip:4/big-integer-unit:8>> = <<I1,I2,I3,I4>>,
				<<Mask:4/big-integer-unit:8>> = <<M1,M2,M3,M4>>,
				<<All:4/big-integer-unit:8>> = <<255,255,255,255>>,
				Subnet = Ip band Mask,
				MinIp = Subnet + 1,
				MaxIp = ((All - Mask) bor Subnet) - 1,
				getIpListFromRange(MinIp,MaxIp)
		end,
	ScanIpList = nnm_discovery_util:mergerList(Result,IpList),
	normal_getNextScanIpList_one(IpAddr,ScanIpList).
%%----------------------------------------------------------------------------------------------

%%----------------------------------------------------------------------------------------------
%% get next scan ip list from arp
%%----------------------------------------------------------------------------------------------
arp_getNextScanIpList([],Result) -> Result;
arp_getNextScanIpList([H|NScanResult],Result) ->
	{_,_,_,ArpInfo,_,_,_} = H,
	case ArpInfo of
		[] -> arp_getNextScanIpList(NScanResult,Result);
		_ ->
			{_,Arp} = ArpInfo,
			IpList = [Y || X <- Arp,Y <- [proplists:get_value(ip,X)],(not lists:member(Y, Result))],
			arp_getNextScanIpList(NScanResult,lists:append(IpList, Result))
	end.

%%----------------------------------------------------------------------------------------------
%%Merge the same information.
%%----------------------------------------------------------------------------------------------
mergeScanResult([],InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo) -> 
	{InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo};
mergeScanResult([H|ScanResult],InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo) ->
	{V1,V2,V3,V4,V5,V6,V7} = H,
	F = fun(NV,OV) ->
				case NV =:= [] of
					true ->
						OV;
					false ->
						[NV|OV]
				end
		end,
				
	mergeScanResult(ScanResult,
				   F(V1,InterfaceInfo),
				   F(V2,RouteInfo),
				   F(V3,AftInfo),
				   F(V4,ArpInfo),
				   F(V5,OspfInfo),
				   F(V6,BgpInfo),
				   F(V7,DirectInfo)).  

writeEdgeFile([],S) -> file:close(S);
writeEdgeFile([H|Edge],S) ->
	{LeftDeviceId,_,_,_,_,RightDeviceId,_,_,_,_} = H,
	io:format(S, "{~s,~s}~n",
			  [LeftDeviceId,RightDeviceId]),
	writeEdgeFile(Edge,S).

writeDeviceFile([],S) -> file:close(S);
writeDeviceFile([H|Device],S) ->
	Id = proplists:get_value(id,H),
	io:format(S,"~s~n",[Id]),
	writeDeviceFile(Device,S).

%%calulate coordinate and generate topo chart
calculateCoordinate(FEdgeInfo,FNetworkDeviceInfo) ->
	{ok,Path} = file:get_cwd(),
	{ok,DeviceFile} = file:open(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/DeviceInfo.txt", write),
	{ok,EdgeFile} = file:open(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/EdgeInfo.txt", write),
	writeEdgeFile(FEdgeInfo,EdgeFile),
	writeDeviceFile(FNetworkDeviceInfo,DeviceFile),
	Cmd = Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/ComputeTopo ",
	os:cmd(Cmd),
	{ok,CoordinateDevice} = file:consult(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/target.txt"),
	CoordinateDevice.


formatEdge([],_) -> [];
formatEdge([H|EdgeInfo],DeviceList) ->
%% 	io:format("H:~p~n",[H]),
	{IpLeft,PtLeft,IfLeft,DescrLeft,AliasLeft,IpRight,PtRight,IfRight,DescrRight,AliasRight} = H,
	{LeftDeviceId,RightDeviceId} = findNodeId(DeviceList,IpLeft,IpRight,[],[]),
	case LeftDeviceId =/= [] andalso RightDeviceId =/= [] of
		true ->
			[{LeftDeviceId,IfLeft,PtLeft,DescrLeft,AliasLeft,RightDeviceId,IfRight,PtRight,DescrRight,AliasRight}|formatEdge(EdgeInfo,DeviceList)];
		false ->
			formatEdge(EdgeInfo,DeviceList)
	end.

findNodeId([],_,_,LeftDeviceId,RightDeviceId) -> {LeftDeviceId,RightDeviceId};
findNodeId([H|DeviceList],IpLeft,IpRight,LeftDeviceId,RightDeviceId) ->
	case LeftDeviceId =/= [] andalso RightDeviceId =/= [] of
		true ->
			{LeftDeviceId,RightDeviceId};
		false ->
			Ip = proplists:get_value(ip,H,""),
			if
				Ip =:= IpLeft ->
					findNodeId(DeviceList,IpLeft,IpRight,atom_to_list(proplists:get_value(id,H)),RightDeviceId);
				Ip =:= IpRight ->
					findNodeId(DeviceList,IpLeft,IpRight,LeftDeviceId,atom_to_list(proplists:get_value(id,H)));
				true ->
					findNodeId(DeviceList,IpLeft,IpRight,LeftDeviceId,RightDeviceId)
			end
	end.
			
%% -----------------------------------------------------------------------------------------------
%% scan from file 
scan_manager_file(State) ->
	{ok,Path} = file:get_cwd(), 
	Interface = readInterface(Path),
	Device = readDevice(Path),
	Route = readRoute(Path),
	Aft = readAft(Path),
	Arp = readArp(Path),
	
	{Device,Interface,Route,Aft,Arp,[],[],[]}.

readInterface(Path) ->
	{ok,S} = file:open(Path ++ "/log/InfProps.txt", read),
	Data = readInterface(S,[]),
	file:close(S),
	Data.

readInterface(S,Result) ->
	case io:get_line(S, '') of
		eof -> Result;
		R ->
			D = string:strip(R, both, $\n),
			L1 = nnm_discovery_util:tokens(D,"[:::]",true),
			L2 = nnm_discovery_util:tokens(lists:nth(3, L1),"[::]",true),
			F = fun(M,N) -> if length(N) < M -> ""; true -> lists:nth(M,N) end end, 
			Inrec = lists:map(fun(X) ->
%% 						io:format("ifIndex:~p~n",[X]),
						Y = nnm_discovery_util:tokens(X,"[:]",true),
						[{ifIndex,F(1, Y)},
					   	{ifType,F(2, Y)},
					   	{ifDescr,F(5, Y)},
					   	{ifMac,F(3, Y)},
					   	{ifSpeed,F(6,Y)},
					   	{ifPort,F(4, Y)},
					   	{ifAlias,""}] 
						end, L2),
			readInterface(S,[{lists:nth(1, L1),[{ifAmount,lists:nth(2, L1)},{inrec,Inrec}]}|Result])
	end.
	
readDevice(Path) ->
	{ok,S} = file:open(Path ++ "/log/DeviceInfos.txt", read),
	Data = readDevice(S,[]),
	file:close(S),
	Data.

readDevice(S,Result) ->
	case io:get_line(S, '') of
		eof -> Result;
		R ->
			F = fun(M,N) -> if length(N) < M -> ""; true -> lists:nth(M,N) end end, 
			D = string:strip(R, both, $\n),
			L1 = nnm_discovery_util:tokens(D,"[::]",true),
			L2 = nnm_discovery_util:tokens(F(13, L1),"[:]",true),
			L3 = nnm_discovery_util:tokens(F(14, L1),"[:]",true),
			
			IpAddr = lists:map(fun(X) -> 
								Y = nnm_discovery_util:tokens(X,"/",true),
								[{ipAddress,F(1,Y)},{ipMask,F(2,Y)},{ipIfIndex,F(3,Y)}]
								end, L2),
			DevType = F(6, L1),
			Device = [{ip,F(1, L1)},{baseMac,F(10, L1)},{snmpFlag,F(2, L1)},{devType,DevType},{devFactory,F(7, L1)},
						{devModel,F(8, L1)},{devTypeName,type(DevType)},{sysDescr,""},{sysobjectId,F(5, L1)},{sysSvcs,F(12, L1)},
						{sysName,F(11, L1)},{ipAddr,IpAddr},{macs,L3}],
%% 			Device = #nnm_device{
%% 								 devIp=F(1, L1),
%% 								 baseMac=F(10, L1),
%% 								 snmpFlag=F(2, L1),
%% 								 devType=DevType,
%% 								 devFactory=F(7, L1),
%% 								 devModel=F(8, L1),
%% 								 devTypeName=type(DevType),
%% 								 sysDescr="",
%% 								 sysobjectId=F(5, L1),
%% 								 sysSvcs=F(12, L1),
%% 								 sysName=F(11, L1),
%% 								 ipAddr=IpAddr,
%% 								 macs=L3
%% 								 },
			readDevice(S,[Device|Result])
	end.

type(X) ->
	case X of
		?NNM_DEVICE_TYPE_ROUTE_SWITCH -> "ROUTE_SWITCH";
		?NNM_DEVICE_TYPE_SWITCH -> "SWITCH";
		?NNM_DEVICE_TYPE_ROUTE -> "ROUTE";
		?NNM_DEVICE_TYPE_FIREWALL -> "FIREWALL";
		?NNM_DEVICE_TYPE_SERVER -> "SERVER";
		?NNM_DEVICE_TYPE_HOST -> "HOST";
		?NNM_DEVICE_TYPE_OTHER -> "OTHER"
	end.
	
readRoute(Path) ->
	{ok,S} = file:open(Path ++ "/log/Route_ORG.txt", read),
	Data = readRoute(S,[]),
	file:close(S),
	Data.

readRoute(S,Result) ->
	case io:get_line(S, '') of
		eof -> Result;
		R ->
			D = string:strip(R, both, $\n),
			L1 = nnm_discovery_util:tokens(D,"::",true),
			L2 = nnm_discovery_util:tokens(lists:nth(2,L1),";",true),
			F = fun(M,N) -> if length(N) < M -> ""; true -> lists:nth(M,N) end end, 
			
			Route = lists:map(fun(X) ->
							Y = nnm_discovery_util:tokens(X,"/",true),
							[{routeDest,string:sub_string(F(1, Y), string:str(F(1, Y), ":"))},
					  	{routeIfIndex,string:sub_string(F(1, Y), 1, string:str(F(1, Y), ":")-1)},
					  	{routeNextHop,F(2, Y)},
					  	{routeType,""},
					  	{routeMask,F(3, Y)}]
								 end, L2),
			readRoute(S, [{lists:nth(1, L1),Route}|Result])
	end.

readAft(Path) ->
	{ok,S} = file:open(Path ++ "/log/Aft_ORG.txt", read),
	Data = readAft(S,[]),
	file:close(S),
	Data.

readAft(S,Result) ->
	case io:get_line(S, '') of
		eof -> Result;
		R ->
			D = string:strip(R, both, $\n),
			L1 = nnm_discovery_util:tokens(D,"::",true),
			L2 = nnm_discovery_util:tokens(lists:nth(2,L1),";",true),
			
			Aft = lists:append([readAft_a(X) || X <- L2]),
			readAft(S, [{lists:nth(1, L1),Aft}|Result])
	end.

readAft_a(Aft) ->
	F = fun(M,N) -> if length(N) < M -> ""; true -> lists:nth(M,N) end end, 
	L1 = nnm_discovery_util:tokens(Aft,":",true),
	L2 = F(1, L1),
	L3 = nnm_discovery_util:tokens(F(2, L1),",",true),
	[[{port,L2},{mac,X}] || X <- L3].
	
readArp(Path) ->
	{ok,S} = file:open(Path ++ "/log/Arp_ORG.txt", read),
	Data = readArp(S,[]),
	file:close(S),
	Data.

readArp(S,Result) ->
	case io:get_line(S, '') of
		eof -> Result;
		R ->
			D = string:strip(R, both, $\n),
			L1 = nnm_discovery_util:tokens(D,"::",true),
			L2 = nnm_discovery_util:tokens(lists:nth(2,L1),";",true),
			
			Arp = lists:append([readArp_a(X) || X <- L2]),
			readArp(S, [{lists:nth(1,L1),Arp}|Result])
	end.

readArp_a(Arp) ->
	L1 = nnm_discovery_util:tokens(Arp,":",true),
	L2 = lists:nth(1,L1),
	L3 = nnm_discovery_util:tokens(lists:nth(2,L1),",",true),
	F = fun(M,N) -> if length(N) < M -> ""; true -> lists:nth(M,N) end end, 
	
	[[{ifIndex,L2},{ip,F(1,nnm_discovery_util:tokens(X,"-",true))},
	  {mac,F(2,nnm_discovery_util:tokens(X,"-",true))}] || X <- L3].
	
%% ----------------------------------------------------------------------------------------------- 	

analyseScanInfo(DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo) ->
%% 	{FDeviceInfo,FInterfaceInfo,FRouteInfo,FAftInfo,FArpInfo,FOspfInfo,FBgpInfo,FDirectInfo} = 
%% 		analyseFormat(DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo),
%% 
%% 	nnm_discovery_util:writeFile(FDeviceInfo,"FDeviceData.txt",write),
%% 	nnm_discovery_util:writeFile(FInterfaceInfo,"FInterfaceData.txt",write),
%% 	nnm_discovery_util:writeFile(FRouteInfo,"FRouteData.txt",write),
%% 	nnm_discovery_util:writeFile(FAftInfo,"FAftData.txt",write),
%% 	nnm_discovery_util:writeFile(FArpInfo,"FArpData.txt",write),
%% 	nnm_discovery_util:writeFile(FOspfInfo,"FOspfData.txt",write),
%% 	nnm_discovery_util:writeFile(FBgpInfo,"FBgpData.txt",write),
%% 	nnm_discovery_util:writeFile(FDirectInfo,"FDirectData.txt",write),	
%% 	
%% 	{DeviceData,InterfaceData,RouteData,AftData,ArpData,OspfData,BgpData,DirectData} = 
%% 		analyseEncode(FDeviceInfo,FInterfaceInfo,FRouteInfo,FAftInfo,FArpInfo,FOspfInfo,FBgpInfo,FDirectInfo),
%% 	
%% 	nnm_discovery_util:writeFile(DeviceData,"DeviceData.txt",write),
%% 	nnm_discovery_util:writeFile(InterfaceData,"InterfaceData.txt",write),
%% 	nnm_discovery_util:writeFile(RouteData,"RouteData.txt",write),
%% 	nnm_discovery_util:writeFile(AftData,"AftData.txt",write),
%% 	nnm_discovery_util:writeFile(ArpData,"ArpData.txt",write),
%% 	nnm_discovery_util:writeFile(OspfData,"OspfData.txt",write),
%% 	nnm_discovery_util:writeFile(BgpData,"BgpData.txt",write),
%% 	nnm_discovery_util:writeFile(DirectData,"DirectData.txt",write),
%% 	
%% 	Data = [list_to_tuple(DeviceData),list_to_tuple(InterfaceData),list_to_tuple(RouteData),list_to_tuple(AftData),list_to_tuple(ArpData),
%% 			list_to_tuple(OspfData),list_to_tuple(BgpData),list_to_tuple(DirectData)],	
	%%io:format("Data:~p~n",[Data]),
	io:format("start analyse"),
%% 	Pid = nnm_discovery_analyse:start("Analyse"),
%% 	Value = nnm_discovery_analyse:call_port(Pid,Data),
%% 	io:format("edge:~p~n",[Value]),
	nnm_discovery_analyse:start_link(),
%% 	Value = nnm_discovery_analyse:analyse(Data),
	Value = nnm_discovery_analyse:analyse({DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo}),
	io:format("value:~p~n",[Value]),
	Result =
		case Value of
			<<"error">> ->
				[];
			{error,timeout} ->
				[];
			_ ->
				tuple_to_list(binary_to_term(Value))
		end,
%% 	case Value =:= "error" of
%% 		false ->
%% %% 			EdgeInfo = binary_to_term(list_to_binary(Value)),
%% 			EdgeInfoList = tuple_to_list(Value),
%% 			EdgeInfoList;
%% 		true ->
%% 			[]
%% 	end,
%% 	nnm_discovery_analyse:stop(),
	Result.

analyseFormat(DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo) ->
	IpMacList = lists:append([[{proplists:get_value(mac,Y),proplists:get_value(ip,Y)} || Y <- X] || {_,X} <- ArpInfo]),
	
	FDeviceInfo = analyseFormatDevice(DeviceInfo),
	FInterfaceInfo = analyseFormatInterface(InterfaceInfo),
	FRouteInfo = analyseFormatRoute(RouteInfo),
	FAftInfo = analyseFormatAft(AftInfo,IpMacList,InterfaceInfo),
	FArpInfo = analyseFormatArp(ArpInfo),
	FOspfInfo = analyseFormatOspf(OspfInfo),
	FBgpInfo = analyseFormatBgp(BgpInfo),
	FDirectInfo = analyseFormatDirect(DirectInfo),
	
	{FDeviceInfo,FInterfaceInfo,FRouteInfo,FAftInfo,FArpInfo,FOspfInfo,FBgpInfo,FDirectInfo}.

analyseFormatDevice([]) -> [];
analyseFormatDevice([H|DeviceInfo]) ->
	IpAddr = H#nnm_device.ipAddr,
	FIpAddr = lists:filter(fun(X) -> 
									(proplists:get_value(ipAddress,X) =/= "127.0.0.1") andalso 
										(proplists:get_value(ipMask, X) =/= "255.0.0.0") 
							end, IpAddr),
	[H#nnm_device{ipAddr=FIpAddr}|analyseFormatDevice(DeviceInfo)].

analyseFormatInterface(InterfaceInfo) ->
	InterfaceInfo.

analyseFormatRoute([]) -> [];
analyseFormatRoute([H|RouteInfo]) ->
	{SourceIp,Info} = H,
	FInfo = analyseFormatRoute(Info,[]),
	[{SourceIp,FInfo}|analyseFormatRoute(RouteInfo)].

analyseFormatRoute([],Result) -> Result;
analyseFormatRoute([H|Info],Result) ->
	IfIndex = proplists:get_value(routeIfIndex,H),
	Dest = proplists:get_value(routeDest,H),
	NextHop = proplists:get_value(routeNextHop,H),
	Mask = proplists:get_value(routeMask,H),
	
	case proplists:get_value(IfIndex,Result) of
		undefined ->
			analyseFormatRoute(Info,[{IfIndex,[{Dest,NextHop,Mask}]}|Result]);
		R ->
			V = [{Dest,NextHop,Mask}|R],
			analyseFormatRoute(Info,lists:keyreplace(IfIndex, 1, Result, {IfIndex,V}))
	end.

analyseFormatOspf(OspfInfo) ->
	OspfInfo.

analyseFormatBgp(BgpInfo) ->
	BgpInfo.

analyseFormatDirect(DirectInfo) ->
	DirectInfo.

analyseFormatArp([]) -> [];
analyseFormatArp([H|ArpInfo]) ->
	{SourceIp,IfIpMac} = H,
	FIfIpMac = analyseFormatArp(IfIpMac,SourceIp,[]),
	[{SourceIp,FIfIpMac}|analyseFormatArp(ArpInfo)].

analyseFormatArp([],_,Result) -> Result;
analyseFormatArp([H|IfIpMac],SourceIp,Result) ->
	IfIndex = proplists:get_value(ifIndex,H),
	Ip = proplists:get_value(ip,H),
	Str = lists:nth(4,string:tokens(Ip,".")),
	case Ip =:= SourceIp orelse Str =:= "255" orelse Str =:= "0" of
		true ->
			analyseFormatArp(IfIpMac,SourceIp,Result);
		false ->
			case proplists:get_value(IfIndex,Result) of
				undefined ->
					analyseFormatArp(IfIpMac,SourceIp,[{IfIndex,[Ip]}|Result]);
				R ->
					V = [Ip|R],
					analyseFormatArp(IfIpMac,SourceIp,lists:keyreplace(IfIndex, 1, Result, {IfIndex,V}))
			end
	end.

analyseFormatAft([],_,_) -> [];
analyseFormatAft([H|AftInfo],IpMacList,InterfaceInfo) ->
	{SourceIp,IpMac} = H,
	IfInfo = proplists:get_value(inrec,proplists:get_value(SourceIp,InterfaceInfo,[]),[]),
	PortIf = [{proplists:get_value(ifPort,X),proplists:get_value(ifIndex,X)} || X <- IfInfo],
	IfIp = analyseFormatAft(IpMac,SourceIp,IpMacList,PortIf,[]),
	[{SourceIp,IfIp}|analyseFormatAft(AftInfo,IpMacList,InterfaceInfo)].

analyseFormatAft([],_,_,_,Result) -> Result;
analyseFormatAft([H|IpMac],SourceIp,IpMacList,PortIf,Result) ->
	Mac = proplists:get_value(mac,H),
	Port = proplists:get_value(port,H),
	Ip = proplists:get_value(Mac,IpMacList),
	IfIndex = proplists:get_value(Port,PortIf),
	
	case Ip =:= undefined orelse Ip =:= SourceIp orelse IfIndex =:= undefined of
		true ->
			analyseFormatAft(IpMac,SourceIp,IpMacList,PortIf,Result);
		false ->
			case proplists:get_value(IfIndex, Result) of
				undefined ->
					analyseFormatAft(IpMac,SourceIp,IpMacList,PortIf,[{IfIndex,[Ip]}|Result]);
				R ->
					V = [Ip|R],
					analyseFormatAft(IpMac,SourceIp,IpMacList,PortIf,lists:keyreplace(IfIndex, 1, Result, {IfIndex,V}))
			end
	end.

analyseEncode(DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo) ->
	DeviceData = analyseEncodeDevice(DeviceInfo),
	InterfaceData = analyseEncodeInterface(InterfaceInfo),
	RouteData = analyseEncodeRoute(RouteInfo),
	AftData = analyseEncodeAft(AftInfo),
	ArpData = analyseEncodeAft(ArpInfo),
	OspfData = analyseEncodeOspf(OspfInfo),
	BgpData = analyseEncodeBgp(BgpInfo),
	DirectData = analyseEncodeDirect(DirectInfo),
	
	{DeviceData,InterfaceData,RouteData,AftData,ArpData,OspfData,BgpData,DirectData}.

analyseEncodeDevice([]) -> [];
analyseEncodeDevice([H|DeviceInfo]) ->
	IpAddr = lists:append(H#nnm_device.ipAddr),
	IpAdEntAddr = proplists:get_all_values(ipAddress, IpAddr),
	IpAdEntIfIndex = proplists:get_all_values(ipIfIndex, IpAddr),
	IpAdEntNetMask = proplists:get_all_values(ipMask, IpAddr),
	SnmpParam = H#nnm_device.snmpParam,
	DevIp = H#nnm_device.devIp,
	BaseMac = H#nnm_device.baseMac,
	SnmpFlag = H#nnm_device.snmpFlag,
	CommunityGet = proplists:get_value(getCommunity,SnmpParam,""),
	CommunitySet = proplists:get_value(setCommunity,SnmpParam,""),
	DevType = H#nnm_device.devType,
	DevFactory = H#nnm_device.devFactory,
	DevModel = H#nnm_device.devModel,
	DevTypeName = H#nnm_device.devTypeName,
	SysobjectId = H#nnm_device.sysobjectId,
	SysSvcs = H#nnm_device.sysSvcs,
	SysName = H#nnm_device.sysName,
	Macs = H#nnm_device.macs,
	[{DevIp,BaseMac,SnmpFlag,CommunityGet,CommunitySet,DevType,DevFactory,DevModel,DevTypeName,SysobjectId,SysSvcs,SysName,
	 	list_to_tuple(IpAdEntAddr),list_to_tuple(IpAdEntIfIndex),list_to_tuple(IpAdEntNetMask),list_to_tuple(Macs)}
	|analyseEncodeDevice(DeviceInfo)].



analyseEncodeInterface([]) -> [];
analyseEncodeInterface([H|InterfaceInfo]) ->
	{SourceIp,Info} = H,
	Amount = proplists:get_value(ifAmount,Info,""),
	Inrec = proplists:get_value(inrec,Info,[]),
	
	FInrec = [{proplists:get_value(ifIndex,X,""),
			   proplists:get_value(ifType,X,""),
			   proplists:get_value(ifDescr,X,""),
			   proplists:get_value(ifMac,X,""),
			   proplists:get_value(ifPort,X,""),
			   proplists:get_value(ifSpeed,X,""),
			   proplists:get_value(ifAlias,X,"")} || X <- Inrec],
	
	[{SourceIp,Amount,list_to_tuple(FInrec)}|analyseEncodeInterface(InterfaceInfo)].

analyseEncodeRoute([]) -> [];
analyseEncodeRoute([H|RouteInfo]) ->
	{SourceIp,Info} = H,
	FInfo = [{X,list_to_tuple(Y)} || {X,Y} <- Info],
	[{SourceIp,list_to_tuple(FInfo)}|analyseEncodeRoute(RouteInfo)].

analyseEncodeAft([]) -> [];
analyseEncodeAft([H|AftInfo]) ->
	{SourceIp,Info} = H,
	FInfo = [{X,list_to_tuple(Y)} || {X,Y} <- Info],
	[{SourceIp,list_to_tuple(FInfo)}|analyseEncodeAft(AftInfo)].
	
analyseEncodeOspf([]) -> [];
analyseEncodeOspf(OspfInfo) ->
	[].
	
analyseEncodeBgp([]) -> [];
analyseEncodeBgp(BgpInfo) ->
	[].
	
analyseEncodeDirect([]) -> [];
analyseEncodeDirect(DirectInfo) ->
	[].

