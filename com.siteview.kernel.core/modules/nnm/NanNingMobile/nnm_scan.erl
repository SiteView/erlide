-module(nnm_scan).

-behaviour(gen_server).
-export([start_link/0]).
%% gen_server callbacks
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
terminate/2, code_change/3]).

-export([start_scan/1,get_scan_info/1,stop_scan/0,print_scan_info/1]).

-include("nnm_discovery_topo.hrl").
-include("nnm_define.hrl").

-define(SERVER, nnm_scan_server).
-define(THREAD, 20).
-define(TIMEOUT, 7200000).
-define(SNMP_PING_OID, [1,3,6,1,2,1,1,2,0]).
-define(PATH,"nnm_discovery\\").

writeLog(Log) ->
	case file:open("gponInfo.txt",[append]) of
		{ok,File} -> io:format(File,"~n~p~n~n",[Log]),
		file:close(File);
		_ -> ok
	end.
	
-record(state, 
		{status="free", scanpid=[], scanInfo=[], scanConfig=[], ipList=[], incrScan=true}
	   ).

start_link() -> gen_server:start_link({local, ?SERVER}, ?MODULE, [], []).

init([]) -> 
	ets:new(?MODULE,[set,public,named_table]),
	{ok, #state{status=start}}.

start_scan(Param) ->
	case erlang:whereis(?SERVER) of
		undefined -> start_link();
		_ -> []
	end,
	clear_ets(),
	case proplists:get_value(configId, Param, "") of
		"" ->
			{error,"config id is null"};
		_ConfigId ->
			case gen_server:call(?SERVER, {start_scan, Param}) of
				{ok, ReqId} ->
					{ok,ReqId};
				{error, Reason} ->
					{error, Reason}
			end
	end.

clear_ets() ->
	gen_server:call(?SERVER, {clear_ets}).

print_scan_info(Info) ->
	gen_server:call(?SERVER, {print_scan_info, Info}).

get_scan_info(Param) ->
	gen_server:call(?SERVER, {get_scan_info,Param}).

stop_scan() ->
	io:format("stop scan."),
	nnm_monitor:start_monitor(),
	gen_server:call(?SERVER, {stop_scan}).

handle_call({clear_ets}, _From, _State) ->
	ets:delete_all_objects(?MODULE),
	{reply, ok, #state{status=start}};
handle_call({start_scan, Param}, _From, State) -> 
	case State#state.status of
		"busy" ->
			{reply, {error, "busy"}, State};
		_ ->
			nnm_monitor:stop_monitor(),
			ConfigId = proplists:get_value(configId,Param),
			{ok,IpList} = file:consult(?NNM_DISCOVERY_IPLISTSPATH),
			ScanConfig = nnm_discovery_dal:getScanConfig([{id,ConfigId}]),
%% 			ScanParam = proplists:get_value(scanParam, ScanConfig, []),
			
			NState = 
				State#state{
							scanConfig=ScanConfig,
							ipList=IpList,
							incrScan=proplists:get_value(incrScan,Param,true)
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
			{reply,ok,State#state{status="free", scanpid=[], scanInfo=[], scanConfig=[], ipList=[]}}
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

scan_other(State) ->
	ScanConfig = State#state.scanConfig,
	ScanParam = proplists:get_value(scanParam,ScanConfig,[]),
	ScanSeed = getIpList(proplists:get_value(scanSeed, ScanParam, "")),
	Depth = proplists:get_value(depth,ScanParam,3),
	IncludeIp = getIpList(proplists:get_value(includeIp, ScanParam, [])),
	ExcludeIp = getIpList(proplists:get_value(excludeIp, ScanParam, [])),
	ets:insert(?MODULE, {includeIp,IncludeIp}),
	ets:insert(?MODULE, {excludeIp,ExcludeIp}),
	
	nnm_scan_init_ErlangExtension(ScanConfig),
	STATE = case State#state.ipList of
		[] ->
			case IncludeIp of
				[] ->
					scan_seed(ScanSeed,Depth);
				_ ->
					scan_ip_list(IncludeIp)
			end;
		IpList ->
			scan_ip_list(IpList)
	end,
	%~ io:format("~n~n~n scan_other = ~p~n~n~n",[STATE]),
	STATE.

scan_manager(State) -> 
	print_scan_info("start scan..."),
	ScanConfig = State#state.scanConfig,
	ConfigId = proplists:get_value(id,ScanConfig),
	ScanParam = proplists:get_value(scanParam,ScanConfig,[]),
	ScanType = proplists:get_value(scanType, ScanParam, "snmpping"),
	ets:insert(?MODULE, {scanType,ScanType}),
	
	init_device_info(),
	
	case ScanType of
		"file" ->
			scan_file(ConfigId);
		_ ->
			scan_other(State)
	end,
	
	Path = ?PATH ++ erlang:atom_to_list(ConfigId) ++ "\\",
	filelib:ensure_dir(Path),

%%----gpon-----------------------
	GponOltInfo = ets:lookup_element(?MODULE, gponOltInfo, 2),
 	io:format("analyse scanInfo GponOltInfo:~p~n",[GponOltInfo]),
	
	GponOnuInfo = ets:lookup_element(?MODULE, gponOnuInfo, 2),
 	io:format("analyse scanInfo GponOnuInfo:~p~n",[GponOnuInfo]),

	GponEdgeInfo = analyseGpon(GponOltInfo,GponOnuInfo),
	io:format("~n~n EdgeInfo GponInfo: ~p~n~n", [GponEdgeInfo]),
	save_scanInfo_gpon_edge(GponEdgeInfo), %% sava gponedge.txt
%%----gpon----------------------------	

%%	EdgeInfoMain = analyse_scanInfo(ScanType,Path),
%%	EdgeInfo = lists:append(EdgeInfoMain,GponEdgeInfo),
%%	io:format("~n~n EdgeInfo: ~p~n~n", [EdgeInfo]),
	EdgeInfo = analyse_scanInfo(ScanType,Path),
	io:format("EdgeInfo = ~n~p", [EdgeInfo]),
	
	DeviceInfo = ets:lookup_element(?MODULE, deviceInfo, 2),
	{ok, DumbInfo} = file:consult("nnm_discovery\\DumbInfo.dat"),
	io:format("DumbInfo = ~n~p", [DumbInfo]),
	DeviceInfo2 = lists:append(DeviceInfo,DumbInfo),
	
	%%io:format("~n=========DeviceInfo2:=========~n~p", [DeviceInfo2]),
	FDeviceInfo = nnm_discovery_dal:saveDeviceInfo(DeviceInfo2),
	%%-io:format("~n=========FDeviceInfo:=========~n~p", [FDeviceInfo]),
	
	FEdgeInfo = 
		case State#state.incrScan of
			true ->
				formatEdge(EdgeInfo,FDeviceInfo);
			false ->
				io:format("incr scan..."),
				incrAnalyse(EdgeInfo,FDeviceInfo)
		end,
%% 	coordinate compute
	CoordinateDevice = calculateCoordinate(FEdgeInfo,FDeviceInfo,State#state.incrScan),
%% 	update device
	nnm_discovery_dal:savaCoordinateDevice(CoordinateDevice),
%% 	save edge
	Version = [{name,"topoChart"},{type,"total"},{time,nnm_discovery_util:getLocalTime()}],
	TopoSet = [{octets,[1024*1024,1024*1024*1024,1024*1024*1024*10,1024*1024*1024*100]},
			   {ucastPkts,[10,100,1000,10000]},
			   {nUcastPkts,[10,100,1000,10000]},
			   {discards,[1,10,100,1000]},
			   {errors,[1,10,100,1000]},
			   {bandwidthRate,[0.10,0.30,0.50,0.70]}],
	nnm_discovery_dal:saveEdgeInfo(FEdgeInfo,Version,TopoSet),
	ok.
	
nnm_scan_init_ErlangExtension(Args) ->
	extension:call_plugins(?MODULE,init,Args).

scan_ip_list(IpList) ->
	ExistIpList = find_exist_ip(IpList),
 	  io:format("ExistIpList:~p~n",[ExistIpList]),
	FExistIpList = filter_exist_ip(ExistIpList,[]),
	 io:format("FExistIpList:~p~n",[FExistIpList]),
	 read_ip_info(ExistIpList).
	%~ read_ip_info(FExistIpList).

%% delete same device ip 
%% filter_exist_ip([]) -> [];
%% filter_exist_ip([H|T]) ->
%% 	Ip = proplists:get_value(ip,H),
%% 	case lists:filter(fun(X) -> lists:member(Ip, [proplists:get_value(ipAdEntAddr,Y) || Y <- proplists:get_value(ipAddr,X,[])]) end, T) of
%% 		[] -> [H|filter_exist_ip(T)];
%% 		R -> 
%% 			Ips = [proplists:get_value(ip,X) || X <- R],
%% 			ExcludeIp = ets:lookup_element(?MODULE, excludeIp, 2),
%% 			ets:update_element(?MODULE, excludeIp, {2,lists:append(ExcludeIp, Ips)}),
%% 			[H|filter_exist_ip(lists:subtract(T, R))]
%% 	end.
filter_exist_ip([],_) -> [];
filter_exist_ip([H|T],Ips) ->
	Ip = proplists:get_value(ip,H,""),
	IpAddr = proplists:get_value(ipAddr,H,[]),
	FilterIps = [proplists:get_value(ipAddress,X,"") || X <- IpAddr],
	case lists:member(Ip, FilterIps) of
		true -> 
 			io:format("FilterIps:~p~n",[FilterIps]),
			case FilterIps =/= [] andalso lists:member(FilterIps, Ips) of
				true -> filter_exist_ip(T,Ips);
				false -> [H|filter_exist_ip(T,[FilterIps|Ips])]
			end;
		false ->
			filter_exist_ip(T,Ips)
	end.

%% find exist ip and read ip config param
find_exist_ip(IpList) ->
	ExcludeIp = ets:lookup_element(?MODULE, excludeIp, 2),
	FIpList = lists:filter(fun(X) -> not lists:member(X, ExcludeIp) end, IpList),
	ets:update_element(?MODULE, excludeIp, {2,lists:append(ExcludeIp, FIpList)}),
	Return = nnm_scan_findIp_ErlangExtension(FIpList),
%% 	io:format("existIp:~p~n",[Return]),
	analyse_scanIpListReturn(Return,[]).

nnm_scan_findIp_ErlangExtension(Args) ->
	extension:call_plugins(?MODULE,findIp,Args).

analyse_scanIpListReturn([],Result) -> Result;
analyse_scanIpListReturn([H|Data],Result) ->
	case H of
		{_,_,D} ->
			if
				Result =:= [] ->
					analyse_scanIpListReturn(Data,D);
				true ->
					analyse_scanIpListReturn_a(D,Result)
			end;
		_ ->
			analyse_scanIpListReturn(Data,Result)
	end.
			
analyse_scanIpListReturn_a([],Result) -> Result;
analyse_scanIpListReturn_a([H|T],Result) ->
	Ip = proplists:get_value(ip, H),
	{L,K} = analyse_scanIpListReturn_a(Result,Ip,[],[]),
	case L of
		[] ->
			analyse_scanIpListReturn_a(T,[H|Result]);
		_ ->
			analyse_scanIpListReturn_a(T,[nnm_discovery_util:mergerList(H,L)|K])
	end.

analyse_scanIpListReturn_a([],_,L,K) ->
	{L,K};
analyse_scanIpListReturn_a([H|T],Ip,L,K) ->
	case proplists:get_value(ip,H) of
		Ip ->
			analyse_scanIpListReturn_a(T,Ip,H,K);
		_ ->
			analyse_scanIpListReturn_a(T,Ip,L,[H|K])
	end.
			
%% read device info
read_ip_info(IpList) ->
	Return = nnm_scan_readInfo_ErlangExtension(IpList),
 %%	io:format("read_ip_info:~p~n",[Return]),
	
	[{_,_,Data}] = Return,


	GponOltInfo = proplists:get_all_values(gponOltInfo,Data),
	GponOnuInfo = proplists:get_all_values(gponOnuInfo,Data),

	io:format("read_ip_info GponOltInfo:~p~n",[GponOltInfo]),
	ets:insert(?MODULE, {gponOltInfo,GponOltInfo}),
	
	io:format("read_ip_info GponOnuInfo:~p~n",[GponOnuInfo]),
	ets:insert(?MODULE, {gponOnuInfo,GponOnuInfo}),

	%~ writeLog("read_ip_info:"),
	%~ writeLog(GponOltInfo),
	%~ writeLog(GponOnuInfo),
	
	analyse_readInfo(Return,[]).


nnm_scan_readInfo_ErlangExtension(IpList) ->
	extension:call_plugins(?MODULE,readInfo,IpList).

%% connect plugin data 
analyse_readInfo([],Result) -> 
	DeviceInfo = ets:lookup_element(?MODULE, deviceInfo, 2),
	InterfaceInfo = ets:lookup_element(?MODULE, interfaceInfo, 2),
	RouteInfo = ets:lookup_element(?MODULE, routeInfo, 2),
	ArpInfo = ets:lookup_element(?MODULE, arpInfo, 2),
	AftInfo = ets:lookup_element(?MODULE, aftInfo, 2),
	OspfInfo = ets:lookup_element(?MODULE, ospfInfo, 2),
	BgpInfo = ets:lookup_element(?MODULE, bgpInfo, 2),
	DirectInfo = ets:lookup_element(?MODULE, directInfo, 2),
	
	ets:update_element(?MODULE, deviceInfo, {2,lists:append(DeviceInfo, proplists:get_value(deviceInfo,Result,[]))}),
	ets:update_element(?MODULE, interfaceInfo, {2,lists:append(InterfaceInfo, proplists:get_value(interfaceInfo,Result,[]))}),
	ets:update_element(?MODULE, routeInfo, {2,lists:append(RouteInfo, proplists:get_value(routeInfo,Result,[]))}),
	ets:update_element(?MODULE, arpInfo, {2,lists:append(ArpInfo, proplists:get_value(arpInfo,Result,[]))}),
	ets:update_element(?MODULE, aftInfo, {2,lists:append(AftInfo, proplists:get_value(aftInfo,Result,[]))}),
	ets:update_element(?MODULE, ospfInfo, {2,lists:append(OspfInfo, proplists:get_value(ospfInfo,Result,[]))}),
	ets:update_element(?MODULE, bgpInfo, {2,lists:append(BgpInfo, proplists:get_value(bgpInfo,Result,[]))}),
	ets:update_element(?MODULE, directInfo, {2,lists:append(DirectInfo, proplists:get_value(directInfo,Result,[]))});
	
analyse_readInfo([H|T],Result) ->
	case H of
		{_,_,Data} ->
			DeviceInfo_1 = proplists:get_value(deviceInfo,Data,[]),
			InterfaceInfo_1 = proplists:get_value(interfaceInfo,Data,[]),
			RouteInfo_1 = proplists:get_value(routeInfo,Data,[]),
			ArpInfo_1 = proplists:get_value(arpInfo,Data,[]),
			AftInfo_1 = proplists:get_value(aftInfo,Data,[]),
			OspfInfo_1 = proplists:get_value(ospfInfo,Data,[]),
			BgpInfo_1 = proplists:get_value(bgpInfo,Data,[]),
			DirectInfo_1 = proplists:get_value(directInfo,Data,[]),
			
			DeviceInfo_2 = proplists:get_value(deviceInfo,Result,[]),
			InterfaceInfo_2 = proplists:get_value(interfaceInfo,Result,[]),
			RouteInfo_2 = proplists:get_value(routeInfo,Result,[]),
			ArpInfo_2 = proplists:get_value(arpInfo,Result,[]),
			AftInfo_2 = proplists:get_value(aftInfo,Result,[]),
			OspfInfo_2 = proplists:get_value(ospfInfo,Result,[]),
			BgpInfo_2 = proplists:get_value(bgpInfo,Result,[]),
			DirectInfo_2 = proplists:get_value(directInfo,Result,[]),
			
			DeviceInfo = connect_device(DeviceInfo_1,DeviceInfo_2),
			InterfaceInfo = connect_interface(InterfaceInfo_1,InterfaceInfo_2),
			RouteInfo = connect_route(RouteInfo_1,RouteInfo_2),
			ArpInfo = connect_arp(ArpInfo_1,ArpInfo_2),
			AftInfo = connect_aft(AftInfo_1,AftInfo_2),
			OspfInfo = connect_ospf(OspfInfo_1,OspfInfo_2),
			BgpInfo = connect_bgp(BgpInfo_1,BgpInfo_2),
			DirectInfo = connect_direct(DirectInfo_1,DirectInfo_2),
			
			R = [{deviceInfo,DeviceInfo},{interfaceInfo,InterfaceInfo},{routeInfo,RouteInfo},{arpInfo,ArpInfo},{ospfInfo,OspfInfo},{bgpInfo,BgpInfo},{directInfo,DirectInfo},{aftInfo,AftInfo}],
			analyse_readInfo(T,R);
		_ ->
			analyse_readInfo(T,Result)
	end.

connect_device(D1,[]) -> D1;
connect_device([],D2) -> D2;
connect_device([H|D1],D2) ->
	Ip = proplists:get_value(ip,H),
	case lists:filter(fun(X) -> Ip =:= proplists:get_value(ip,X) end, D2) of
		[] ->
			connect_device(D1,[H|D2]);
		[DD|_] ->
			connect_device(D1,[nnm_discovery_util:mergerList(H,DD)|lists:delete(DD, D2)])
	end.
	
connect_interface(D1,[]) -> D1;
connect_interface([],D2) -> D2;
connect_interface([H|D1],D2) ->
	{Ip,Info} = H,
	case proplists:get_value(Ip,D2,[]) of
		[] ->
			connect_interface(D1,[H|D2]);
		Info1 ->
			connect_interface(D1,[{Ip,lists:append(Info1, Info)}|D2])
	end.
			
connect_route(D1,[]) -> D1;
connect_route([],D2) -> D2;
connect_route([H|D1],D2) ->
	{Ip,Info} = H,
	case proplists:get_value(Ip,D2,[]) of
		[] ->
			connect_route(D1,[H|D2]);
		Info1 ->
			connect_route(D1,[{Ip,lists:append(Info1, Info)}|D2])
	end.		

connect_arp(D1,[]) -> D1;
connect_arp([],D2) -> D2;
connect_arp([H|D1],D2) ->
	{Ip,Info} = H,
	case proplists:get_value(Ip,D2,[]) of
		[] ->
			connect_arp(D1,[H|D2]);
		Info1 ->
			connect_arp(D1,[{Ip,lists:append(Info1, Info)}|D2])
	end.	

connect_aft(D1,[]) -> D1;
connect_aft([],D2) -> D2;
connect_aft([H|D1],D2) ->
	{Ip,Info} = H,
	case proplists:get_value(Ip,D2,[]) of
		[] ->
			connect_aft(D1,[H|D2]);
		Info1 ->
			connect_aft(D1,[{Ip,lists:append(Info1, Info)}|D2])
	end.

connect_ospf(D1,[]) -> D1;
connect_ospf([],D2) -> D2;
connect_ospf([H|D1],D2) ->
	{Ip,Info} = H,
	case proplists:get_value(Ip,D2,[]) of
		[] ->
			connect_ospf(D1,[H|D2]);
		Info1 ->
			connect_ospf(D1,[{Ip,lists:append(Info1, Info)}|D2])
	end.
			
connect_bgp(D1,[]) -> D1;
connect_bgp([],D2) -> D2;
connect_bgp([H|D1],D2) ->
	{Ip,Info} = H,
	case proplists:get_value(Ip,D2,[]) of
		[] ->
			connect_bgp(D1,[H|D2]);
		Info1 ->
			connect_bgp(D1,[{Ip,lists:append(Info1, Info)}|D2])
	end.

connect_direct(D1,[]) -> D1;
connect_direct([],D2) -> D2;
connect_direct([H|D1],D2) ->
	{Ip,Info} = H,
	case proplists:get_value(Ip,D2,[]) of
		[] ->
			connect_direct(D1,[H|D2]);
		Info1 ->
			connect_direct(D1,[{Ip,lists:append(Info1, Info)}|D2])
	end.

%% scan from file
scan_file(ConfigId) ->
	{ok,Path} = file:get_cwd(), 
	{ok,S} = file:open(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/" ++ erlang:atom_to_list(ConfigId) ++ "/DeviceInfos.txt", read),
	Data = readDevice(S,[]),
	file:close(S),
	ets:update_element(?MODULE, deviceInfo, {2,Data}).

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
	

init_device_info() ->
	ets:insert(?MODULE, {deviceInfo,[]}),
	ets:insert(?MODULE, {interfaceInfo,[]}),
	ets:insert(?MODULE, {routeInfo,[]}),
	ets:insert(?MODULE, {arpInfo,[]}),
	ets:insert(?MODULE, {aftInfo,[]}),
	ets:insert(?MODULE, {ospfInfo,[]}),
	ets:insert(?MODULE, {bgpInfo,[]}),
	ets:insert(?MODULE, {directInfo,[]}).

%% scan from seed
scan_seed([],_) -> [];
scan_seed(_,0) -> [];
scan_seed(ScanSeed,Depth) ->
%% 	io:format("scanseed:~p~n",[ScanSeed]),
	scan_ip_list(ScanSeed),
	ScanType = ets:lookup_element(?MODULE, scanType, 2),
	DeviceInfo = ets:lookup_element(?MODULE, deviceInfo, 2),
	DeviceIps = lists:append([[proplists:get_value(ipAdEntAddr, Y) || Y <- proplists:get_value(ipAddr,X,[])] || X <- DeviceInfo]),
	ExcludeIp = ets:lookup_element(?MODULE, excludeIp, 2),
	ets:update_element(?MODULE, excludeIp, {2,lists:append(DeviceIps, ExcludeIp)}),
	IpList = 
		case ScanType of
			"arp" ->
				ArpInfo = ets:lookup_element(?MODULE, arpInfo, 2),
				arp_get_next_ipList(ArpInfo,[]);
			_ ->
				normal_get_next_ipList(DeviceInfo,[])
		end,
	scan_seed(IpList,Depth-1).


%% normal get next ip list from ip table 
normal_get_next_ipList([],Result) -> Result;
normal_get_next_ipList([H|DeviceList],Result) ->
	Type = proplists:get_value(devType,H),
 	case ((Type =:= ?NNM_DEVICE_TYPE_ROUTE_SWITCH) orelse (Type =:= ?NNM_DEVICE_TYPE_SWITCH) orelse (Type =:= ?NNM_DEVICE_TYPE_ROUTE)) of
		true ->
			IpAddr = proplists:get_value(ipAddr,H,[]),
			Iplist = normal_get_next_ipList_1(IpAddr,Result),
			normal_get_next_ipList(DeviceList,Iplist);
		false ->
			normal_get_next_ipList(DeviceList,Result)
	end.

normal_get_next_ipList_1([],Result) -> Result;
normal_get_next_ipList_1([H|IpAddr],Result) ->
	IpAdEntAddr = proplists:get_value(ipAddress,H),
	IpAdEntNetMask = proplists:get_value(ipMask,H),
	io:format("scan sub net:~p/~p~n",[IpAdEntAddr,IpAdEntNetMask]),
	IpList = 
		if
			(IpAdEntNetMask =:= "255.255.255.255") orelse (IpAdEntNetMask =:= "255.0.0.0") ->
				[];
			(IpAdEntAddr =:= "0.0.0.0") orelse (IpAdEntAddr =:= "127.0.0.1") ->
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
	normal_get_next_ipList_1(IpAddr,ScanIpList).

%% get next ip list from arp table
arp_get_next_ipList([],Result) -> Result;
arp_get_next_ipList([H|ArpList],Result) ->
	{_,Arp} = H,
	IpList = [Y || X <- Arp,Y <- [proplists:get_value(ip,X)],(not lists:member(Y, Result))],
	arp_get_next_ipList(ArpList,lists:append(IpList, Result)).

%%----------------------------------------------------------------------------------------------
%% get ip from string split of ',' and '-'
%%----------------------------------------------------------------------------------------------
getIpList("") -> [];
getIpList(IpStr) ->
	io:format("ipStr:~p~n",[IpStr]),
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
			IpSeg = string:tokens(IpStr, ","),
			lists:append([getIpList(X) || X <- IpSeg])
	end.

getIpListFromRange(Int1, Int2) when Int1 =:= Int2 + 1 -> [];
getIpListFromRange(Int1, Int2) -> 
	<<V1,V2,V3,V4>> = <<Int1:4/big-integer-unit:8>>,
	Ip = nnm_discovery_util:integerListToString([V1,V2,V3,V4],10, "."),
	[Ip|getIpListFromRange(Int1+1,Int2)].

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
%% calculateCoordinate(FEdgeInfo,FNetworkDeviceInfo) ->
%% 	{ok,Path} = file:get_cwd(),
%% 	{ok,DeviceFile} = file:open(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/DeviceInfo.txt", write),
%% 	{ok,EdgeFile} = file:open(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/EdgeInfo.txt", write),
%% 	writeEdgeFile(FEdgeInfo,EdgeFile),
%% 	writeDeviceFile(FNetworkDeviceInfo,DeviceFile),
%% 	Cmd = Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/ComputeTopo ",
%% 	os:cmd(Cmd),
%% 	CoordinateDevice = 
%% 		case file:consult(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/target.txt") of
%% 			{ok,V} -> V;
%% 			{error,_Reason} -> []
%% 		end,
%% %% 	{ok,CoordinateDevice} = file:consult(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH ++ "/target.txt"),
%% 	CoordinateDevice.

calculateCoordinate(FEdgeInfo,FNetworkDeviceInfo,Type) ->
	Line = [{erlang:list_to_atom(LeftDeviceId),list_to_atom(RightDeviceId)} || {LeftDeviceId,_,_,_,_,RightDeviceId,_,_,_,_} <- FEdgeInfo],
	{Point1,Point2} = 
		case Type of
			true -> 
				{[proplists:get_value(id,X) || X <- FNetworkDeviceInfo],[]};
			false ->
				calculateCoordinate_excludePoint(FNetworkDeviceInfo,[],[])
		end,
	io:format("Point1:~p~n,Point2:~p~n,Line:~p~n",[Point1,Point2,Line]),
	nnm_discovery_util:writeFile([Point1],"point1.txt",write),
	nnm_discovery_util:writeFile([Point2],"point2.txt",write),
	nnm_discovery_util:writeFile([Line],"line.txt",write),
	case nnm_calculateCoordinate:calculate(Point1,Line,circular,{32.0,32.0},[{exclude,Point2}]) of
		{ok,R} -> R;
		_ -> []
	end.

calculateCoordinate_excludePoint([],Point1,Point2) -> {Point1,Point2};
calculateCoordinate_excludePoint([H|T],Point1,Point2) ->
	Id = proplists:get_value(id,H),
	LX = proplists:get_value(x,H,""),
	LY = proplists:get_value(y,H,""),
	if
		LX =/= "" andalso LY =/= "" ->
			Point = {Id,{LX,LY}},
			calculateCoordinate_excludePoint(T,Point1,[Point|Point2]);
		true ->
			calculateCoordinate_excludePoint(T,[Id|Point1],Point2)
	end.
	

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
			
%% analyse edge
analyse_scanInfo(ScanType,Path) ->
	nnm_discovery_analyse:start_link(),
	DeviceInfo = ets:lookup_element(?MODULE, deviceInfo, 2),
	InterfaceInfo = ets:lookup_element(?MODULE, interfaceInfo, 2),
	RouteInfo = ets:lookup_element(?MODULE, routeInfo, 2),
	ArpInfo = ets:lookup_element(?MODULE, arpInfo, 2),
	AftInfo = ets:lookup_element(?MODULE, aftInfo, 2),
	OspfInfo = ets:lookup_element(?MODULE, ospfInfo, 2),
	BgpInfo = ets:lookup_element(?MODULE, bgpInfo, 2),
	DirectInfo = ets:lookup_element(?MODULE, directInfo, 2),

	Value = 
		case ScanType of
			"file" -> nnm_discovery_analyse:analyse([],Path);
			_ -> nnm_discovery_analyse:analyse({DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo},Path)
		end,
%% 	io:format("value:~p~n",[Value]),
	case Value of
		<<"error">> ->
			[];
		{error,timeout} ->
			[];
		_ ->
			tuple_to_list(binary_to_term(Value))
	end.

%% Incremental Analysis
incrAnalyse(EdgeInfo,DeviceInfo) ->
	io:format("incrAnalyse..."),
	NewEdgeInfo = formatEdge(EdgeInfo,DeviceInfo),
	ED0 = nnm_discovery_dal:getTopoChart([]),
	case lists:filter(fun(X) -> proplists:get_value(type,proplists:get_value(version,X,[])) =:= "total" end, ED0) of
		[] ->
			NewEdgeInfo;
		[H|_] ->
			OldEdgeInfo = proplists:get_value(edge,nnm_discovery_dal:getTopoChart(proplists:get_value(id,H)),[]),
			FOldEdgeInfo = [{proplists:get_value(leftDeviceId,X),
							 proplists:get_value(leftIndex,X,""),
							 proplists:get_value(leftPort,X,""),
							 proplists:get_value(leftDescr,X,""),
							 proplists:get_value(leftAlias,X,""),
							 proplists:get_value(rightDeviceId,X),
							 proplists:get_value(rightIndex,X,""),
							 proplists:get_value(rightPort,X,""),
							 proplists:get_value(rightDescr,X,""),
							 proplists:get_value(rightAlias,X,"")} || X <- OldEdgeInfo],
			incrAnalyse(NewEdgeInfo,FOldEdgeInfo,DeviceInfo)
	end.

incrAnalyse([],OldEdgeInfo,_) -> OldEdgeInfo;
incrAnalyse([H|T],OldEdgeInfo,DeviceInfo) ->
	io:format("edge:~p~n",[H]),
	EdgeInfo = incrAnalyse_1(OldEdgeInfo,H,DeviceInfo),
	incrAnalyse(T,EdgeInfo,DeviceInfo).
			
%% LeftDeviceId,IfLeft,PtLeft,DescrLeft,AliasLeft,RightDeviceId,IfRight,PtRight,DescrRight,AliasRight
incrAnalyse_1([],Edge,_) -> [Edge];
incrAnalyse_1([H|T],Edge,DeviceInfo) ->
	{IdLeft_o,IfLeft_o,PtLeft_o,DescrLeft_o,AliasLeft_o,IdRight_o,IfRight_o,PtRight_o,DescrRight_o,AliasRight_o} = H,
	{IdLeft_n,IfLeft_n,PtLeft_n,DescrLeft_n,AliasLeft_n,IdRight_n,IfRight_n,PtRight_n,DescrRight_n,AliasRight_n} = Edge,
	if
		IdLeft_o =:= IdLeft_n andalso IdRight_o =:= IdRight_n ->
			R = incrAnalyse_2(H,Edge),
			lists:append(R, T);
		IdLeft_o =:= IdRight_n andalso IdRight_o =:= IdLeft_n ->
			R = incrAnalyse_2({IdLeft_o,IfLeft_o,PtLeft_o,DescrLeft_o,AliasLeft_o,IdRight_o,IfRight_o,PtRight_o,DescrRight_o,AliasRight_o},
							  {IdRight_n,IfRight_n,PtRight_n,DescrRight_n,AliasRight_n,IdLeft_n,IfLeft_n,PtLeft_n,DescrLeft_n,AliasLeft_n}),
			lists:append(R, T);
		IdLeft_o =:= IdLeft_n andalso IdRight_o =/= IdRight_n ->
			R = incrAnalyse_3(H,Edge,DeviceInfo),
			lists:append(R, T);
		IdRight_o =:= IdRight_n andalso IdLeft_o =/= IdLeft_n ->
			R = incrAnalyse_3({IdRight_o,IfRight_o,PtRight_o,DescrRight_o,AliasRight_o,IdLeft_o,IfLeft_o,PtLeft_o,DescrLeft_o,AliasLeft_o},
							  {IdRight_n,IfRight_n,PtRight_n,DescrRight_n,AliasRight_n,IdLeft_n,IfLeft_n,PtLeft_n,DescrLeft_n,AliasLeft_n},
							  DeviceInfo),
			lists:append(R, T);
		IdLeft_o =:= IdRight_n andalso IdRight_o =/= IdLeft_n ->
			R = incrAnalyse_3({IdLeft_o,IfLeft_o,PtLeft_o,DescrLeft_o,AliasLeft_o,IdRight_o,IfRight_o,PtRight_o,DescrRight_o,AliasRight_o},
							  {IdRight_n,IfRight_n,PtRight_n,DescrRight_n,AliasRight_n,IdLeft_n,IfLeft_n,PtLeft_n,DescrLeft_n,AliasLeft_n},
							  DeviceInfo),
			lists:append(R, T);
		IdRight_o =:= IdLeft_n andalso IdLeft_o =/= IdRight_n ->
			R = incrAnalyse_3({IdRight_o,IfRight_o,PtRight_o,DescrRight_o,AliasRight_o,IdLeft_o,IfLeft_o,PtLeft_o,DescrLeft_o,AliasLeft_o},
							  {IdLeft_n,IfLeft_n,PtLeft_n,DescrLeft_n,AliasLeft_n,IdRight_n,IfRight_n,PtRight_n,DescrRight_n,AliasRight_n},
							  DeviceInfo),
			lists:append(R, T);
		true ->
			[H|incrAnalyse_1(T,Edge,DeviceInfo)]
	end.

incrAnalyse_2(O,N) ->
	{IdLeft_o,IfLeft_o,PtLeft_o,DescrLeft_o,AliasLeft_o,IdRight_o,IfRight_o,PtRight_o,DescrRight_o,AliasRight_o} = O,
	{IdLeft_n,IfLeft_n,PtLeft_n,DescrLeft_n,AliasLeft_n,IdRight_n,IfRight_n,PtRight_n,DescrRight_n,AliasRight_n} = N,
	
	if 
		PtLeft_o =:= "PX" andalso PtRight_o =:= "PX" ->
			[N];
		PtLeft_n =:= "PX" andalso PtRight_n =:= "PX" ->
			[O];
		PtLeft_o =/= "PX" andalso PtLeft_n =/= "PX" andalso PtLeft_o =/= PtLeft_n ->
			[O,N];
		PtRight_o =/= "PX" andalso PtRight_n =/= "PX" andalso PtRight_o =/= PtRight_n ->
			[O,N];
		PtLeft_o =/= "PX" andalso PtRight_n =/= "PX" ->
			[{IdLeft_o,IfLeft_o,PtLeft_o,DescrLeft_o,AliasLeft_o,IdRight_n,IfRight_n,PtRight_n,DescrRight_n,AliasRight_n}];
		PtLeft_n =/= "PX" andalso PtRight_o =/= "PX" ->
			[{IdLeft_n,IfLeft_n,PtLeft_n,DescrLeft_n,AliasLeft_n,IdRight_o,IfRight_o,PtRight_o,DescrRight_o,AliasRight_o}];
		true ->
			[O]
	end.

incrAnalyse_3(O,N,DeviceInfo) ->
	{_,_,PtLeft_o,_,_,IdRight_o,_,_,_,_} = O,
	{_,_,PtLeft_n,_,_,IdRight_n,_,_,_,_} = N,
	
	if 
		PtLeft_o =:= PtLeft_n ->
			case incrAnalyse_type(IdRight_o,DeviceInfo) of
				true ->
					[O];
				false ->
					case incrAnalyse_type(IdRight_n,DeviceInfo) of
						true ->
							[N];
						false ->
							[O,N]
					end
			end;
		true ->
			[O,N]
	end.
	
incrAnalyse_type(_,[]) -> false;
incrAnalyse_type(Id,[H|T]) ->
	case proplists:get_value(id,H) of
		Id -> 
			Type = proplists:get_value(typeName,H),
			if
				Type =:= "SWITCH" orelse Type =:= "ROUTE_SWITCH" orelse Type =:= "ROUTE" orelse Type =:= "FIREWALL" ->
					true;
				true ->
					false
			end;
		_ ->
			incrAnalyse_type(Id,T)
	end.


analyseGpon([],[]) ->	
		[];
analyseGpon(OLT,ONU) ->
	
	[OltInfo] = OLT,
	[OnuInfo] = ONU,

	FindIP = fun([],_,ACC) -> ACC;
		([Item|Items],Self,ACC) ->
			case Item of
				{Oip,[[{ifIndex,Oid},{ifDescr,Ods},_,_]]} ->   
					Lip = nnm_discovery_util:stringToIntegerList(Oip,"."),

					OltFun = fun([],_,ROLT) -> ROLT;
						([Olt|Olts],Solt,ROLT) ->
							case Olt of
								{MatIP,MatData} ->
								    MatFun = fun([],_,RMAT) -> RMAT;
									([Mat|Mats],SMat,RMAT) ->
										case Mat of
											[{ifIndex,Matid},{ifDescr,Matds},{ifIp,Lip}] ->
												%SMat(Mats,SMat,[{Oip,Oid,Oid,Ods,[],MatIP,Matid,Matid,Matds,[]}|RMAT]);
												SMat(Mats,SMat,[{MatIP,Matid,Matid,Matds,[],Oip,Oid,Oid,Ods,[]}|RMAT]);
											_ -> SMat(Mats,SMat,RMAT)
										end
									end,
								   Solt(Olts,Solt,MatFun(MatData,MatFun,[]) ++ ROLT);
								_ -> Solt(Olts,Solt,ROLT)
							end
						end,
					Self(Items,Self,OltFun(OltInfo,OltFun,[]) ++ ACC);
				_ -> Self(Items,Self,ACC)
			end
		end,

	TEM = FindIP(OnuInfo,FindIP,[]),
	io:format("~n FindIP = ~p~n",[TEM]),
	TEM.
	
%%------------------------------------------------------------------------------	
save_scanInfo_gpon_edge(EdgeInfo) ->
	EdgeInfo1 = [{"172.20.7.10","4261413120","4261413120",
                      "Huawei-MA5600-V800R305-GPON",[],"172.20.7.11",
                      "4194312192","4194312192",
                      "Huawei-MA5600-V800R006-GPON_UNI",[]},
           {"172.20.7.10","4261413120","4261413120",
                      "Huawei-MA5600-V800R305-GPON",[],"172.20.7.12",
                      "4194312192","4194312192",
                      "Huawei-MA5600-V800R006-GPON_UNI",[]}],
					  
	case file:open("nnm_discovery\\GponEdge.txt", [write]) of
		{ok,File} -> 
					io:format("save GponEdge.txt ok"), 
					save_scanInfo_gpon_edge_w(EdgeInfo,File),
					file:close(File);
		_-> []
	end.

save_scanInfo_gpon_edge_w([],_) -> [];
save_scanInfo_gpon_edge_w([H|T],File) ->
 	io:format("edge:~p~n",[H]),

	%[IpLeft,PortLeft,IndexLeft,DescLeft,AliasLeft,IpRight,PortRight,IndexRight,DescRight,AliasRight] = [H],
	
	io:format(File, "~s~n", [string:join(tuple_to_list(H), "[::]")]),
	save_scanInfo_gpon_edge_w(T,File).
%%-----------------------------------------------------------------------------------

				
	
							
					
					













