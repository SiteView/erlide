-module(nnm_scan_snmp_plugin).
-compile(export_all).

-include("nnm_define.hrl").

-define(THREADS,30).

writeLog(Log) ->
	case file:open("scanArgs.txt",[append]) of
		{ok,File} -> io:format(File,"~n~p~n~n",[Log]),
		file:close(File);
		_ -> ok
	end.
	
%% read config file
nnm_scan_init_ErlangPlugin(Args) ->
	io:format("Args:~p~n",[Args]),
	
	writeLog("Args:"),
	writeLog(Args),
	
	List = ets:all(),
	case lists:member(?MODULE, List) of
		true ->
			ets:delete_all_objects(?MODULE);
		false ->
			ets:new(?MODULE, [set,public,named_table])
	end,
	ScanParam = proplists:get_value(scanParam, Args, []),
	DefaultSnmpParam = proplists:get_value(defaultSnmpParam, Args, []),
	DefineSnmpParam = proplists:get_value(defineSnmpParam, Args, []),
	
	{ok,SysobjectIdList} = file:consult(?NNM_DISCOVERY_SYSOBJECTPATH),
	{ok,SpecialOidList} = file:consult(?NNM_DISCOVERY_SPECIALOIDLISTPATH),
	Threads = proplists:get_value(threads,ScanParam,?THREADS),
	ScanType = proplists:get_value(scanType,ScanParam,"snmpping"),
	
	ets:insert(?MODULE, {sysobjectIdList,SysobjectIdList}),
	ets:insert(?MODULE, {specialOidList,SpecialOidList}),
	ets:insert(?MODULE, {threads,Threads}),
	ets:insert(?MODULE, {scanType,ScanType}),
	ets:insert(?MODULE, {defaultSnmpParam,DefaultSnmpParam}),
	ets:insert(?MODULE, {defineSnmpParam,DefineSnmpParam}).

%% find device
nnm_scan_findIp_ErlangPlugin(Args) ->
	Threads = ets:lookup_element(?MODULE, threads, 2),
	ScanType = ets:lookup_element(?MODULE, scanType, 2),
	DefaultSnmpParam = ets:lookup_element(?MODULE, defaultSnmpParam, 2),
	DefineSnmpParam = ets:lookup_element(?MODULE, defineSnmpParam, 2),
	
	case ScanType of
		"nmap" ->
			R1 = nmap_discovery(Args,[]),
			R2 = lists:filter(fun(X) -> proplists:get_value(snmpFlag,X) =:= "1" end, R1),
			SnmpParamList = get_snmpParamList(R2,DefaultSnmpParam,DefineSnmpParam),
			R3 = snmp_ping(SnmpParamList,Threads,[],0),
			lists:map(fun(X) ->
							  I1 = proplists:get_value(ip,X),
							  case lists:filter(fun(Y) -> proplists:get_value(ip, Y) =:= I1 end, R3) of
								  [] -> X;
								  [I2|_] -> I2
							  end
					  end, R1);
		_ ->
			SnmpParamList = get_snmpParamList(Args,DefaultSnmpParam,DefineSnmpParam),
			snmp_ping(SnmpParamList,Threads,[],0)
	end.

%% use nmap discovery exist ip
nmap_discovery(IpList,Result) when length(IpList) =< 250 ->
	IpSegment = string:join(IpList, " "),
	Data = nmap_scan:scan(IpSegment,["-sU -T5 -p 161"]),
	RData = Result ++ Data,
	lists:foreach(fun(X) ->
						  Ip = proplists:get_value(ip,X,""),
						  SnmpFlag = 
							  case proplists:get_value(service,proplists:get_value(port,proplists:get_value(ports,X,[]),[])) of
								  "snmp" ->
									  "1";
								  _ ->
									  "0"
							  end,
						  [{ip,Ip},{snmpFlag,SnmpFlag}]
				  end, RData);
nmap_discovery(IpList,Result) ->
	{List1, List2} = lists:split(250, IpList),
	IpSegment = string:join(List1, " "),
	Data = nmap_scan:scan(IpSegment,["-sU -T5 -p 161"]),
	nmap_discovery(List2,Result++Data).

%%----------------------------------------------------------------------------------------------
%% snmp Assignment
%%----------------------------------------------------------------------------------------------
get_snmpParamList(FilterIpList,DefaultSnmpParam,DefinedSnmpParam) ->
	io:format("FilterIpList:~p~n",[FilterIpList]),
 	DefinedSnmpParamList = 
		[lists:keyreplace(ip, 1, X, {ip,nnm_discovery_util:getIpList(proplists:get_value(ip, X, []))}) || X <- DefinedSnmpParam],
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
%% use snmp find network device 
%%----------------------------------------------------------------------------------------------
snmp_ping([],_,NetworkDeviceList,0) -> NetworkDeviceList;
snmp_ping([],Threads,NetworkDeviceList,C) ->
	receive
		{ping_done,_Why} ->
			snmp_ping([],Threads,NetworkDeviceList,C-1);
		{ping_result,NetworkDevice} ->
			snmp_ping([],Threads,[NetworkDevice|NetworkDeviceList],C);
		_ ->
			snmp_ping([],Threads,NetworkDeviceList,C)
	end;
snmp_ping([H|T],Threads,NetworkDeviceList,C) ->
	receive
		{ping_done,_Why} ->
			start_snmp_ping(H),
			snmp_ping(T,Threads,NetworkDeviceList,C);
		{ping_result,NetworkDevice} ->
			snmp_ping([H|T],Threads,[NetworkDevice|NetworkDeviceList],C);
		_ ->
			snmp_ping([H|T],Threads,NetworkDeviceList,C)
	after 10 ->
		if
			C < Threads ->
					start_snmp_ping(H),
					snmp_ping(T,Threads,NetworkDeviceList,C+1);
				true->
					snmp_ping([H|T],Threads,NetworkDeviceList,C)
		end
	end.

start_snmp_ping(SnmpParam) ->
	ParentId = self(),
	Pid = spawn(fun()->do_snmp_ping(SnmpParam,ParentId) end),
	on_ping_exit(Pid,self()).

do_snmp_ping(SnmpParam,ParentId) ->
	SpecialOidList = ets:lookup_element(?MODULE, specialOidList, 2),
	SysobjectIdList= ets:lookup_element(?MODULE, sysobjectIdList, 2),
	SnmpReadObject = nnm_discovery_snmp_read:new(SnmpParam,SpecialOidList,SysobjectIdList),
%% 	io:format("SnmpParam:~p~n",[SnmpParam]),
	case SnmpReadObject:readDeviceInfo() of
		[] ->
			ParentId ! false;
		Device ->
			io:format("find device:~p,~p,~p~n", 
					  [proplists:get_value(ip,Device,""),proplists:get_value(devTypeName,Device,""),
					   proplists:get_value(devFactory,Device,"")]),
			nnm_scan:print_scan_info("find device:" ++ proplists:get_value(ip,Device,"") ++ " " ++ proplists:get_value(devTypeName,Device,"") 
									++ " " ++ proplists:get_value(devFactory,Device,"")),
			ParentId ! {ping_result,Device}
%% 		SysobjectId ->
%% 			ParentId ! [{ip,proplists:get_value(ip,SnmpParam)},{sysobjectId,SysobjectId},{snmpParam,lists:keydelete(ip, 1, SnmpParam)}]
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

%% read device info 
nnm_scan_readInfo_ErlangPlugin(Params) ->
	Threads = ets:lookup_element(?MODULE, threads, 2),
	Info = get_deviceInfo(Params,Threads,[],0),
	Data = lists:append(Info),
 	%~ io:format("Data:~p~n",[Data]),
	DeviceInfo = proplists:get_all_values(deviceInfo, Data),
	InterfaceInfo = proplists:get_all_values(interfaceInfo, Data),
	RouteInfo = proplists:get_all_values(routeInfo, Data),
	ArpInfo = proplists:get_all_values(arpInfo, Data),
	AftInfo = proplists:get_all_values(aftInfo, Data),
	OspfInfo = proplists:get_all_values(ospfInfo, Data),
	BgpInfo = proplists:get_all_values(bgpInfo, Data),
%-------Gpon	
	GponOltInfo = proplists:get_all_values(gponOltInfo, Data), 
	GponOnuInfo = proplists:get_all_values(gponOnuInfo, Data), 
%-------Gpon	

	DirectInfo = proplists:append_values(directInfo, Data),
 
	%~ io:format("GponInfo Data:~p~n",[GponInfo]),
%old	
%	[{deviceInfo,DeviceInfo},{interfaceInfo,InterfaceInfo},{routeInfo,RouteInfo},{arpInfo,ArpInfo},{ospfInfo,OspfInfo},{bgpInfo,BgpInfo},
%	 {directInfo,DirectInfo},{aftInfo,AftInfo}].
%old	
	[{deviceInfo,DeviceInfo},{interfaceInfo,InterfaceInfo},{routeInfo,RouteInfo},{arpInfo,ArpInfo},{ospfInfo,OspfInfo},{bgpInfo,BgpInfo},
	 {directInfo,DirectInfo},{aftInfo,AftInfo},{gponOltInfo,GponOltInfo},{gponOnuInfo,GponOnuInfo}].
	

get_deviceInfo([],_,Result,0) -> Result;
get_deviceInfo([],Threads,Result,C)->
	receive
		{scan_done,_Why}->
			%%io:format("C:~p~n",[C]),
			get_deviceInfo([],Threads,Result,C-1);
		{scan_result,Value} ->
			get_deviceInfo([],Threads,[Value|Result],C);
		_->
			get_deviceInfo([],Threads,Result,C)
	end;
get_deviceInfo([H|T],Threads,Result,C)->
	receive
		{scan_done,_Why}->
			start_scaner(H),
			get_deviceInfo(T,Threads,Result,C);
		{scan_result,Value} ->
			get_deviceInfo([H|T],Threads,[Value|Result],C);
		_->
			get_deviceInfo([H|T],Threads,Result,C)
	after 20 ->
		Max = case Threads of
				Scount when is_integer(Scount)->
					Scount;
				_->
					?THREADS
			end,
		if
			C < Max -> % limit max scaner
				start_scaner(H),
				get_deviceInfo(T,Threads,Result,C+1);
			true->
				get_deviceInfo([H|T],Threads,Result,C)
		end
	end.
	
start_scaner(Param)->
	ParentId = self(),
	Pid = spawn(fun()->do_scan_host(Param,ParentId) end),
	on_scaner_exit(Pid,self()).
	
do_scan_host(Param,ParentId)->
	case getScanInfo(Param) of
		[] ->
			[];
		Result ->
			ParentId ! {scan_result,Result}
	end.
	
on_scaner_exit(Pid,ParentId)->
	spawn(fun()->process_flag(trap_exit,true),
				link(Pid),
				receive
					{'EXIT',Pid,Why}->
						%%io:format("scaner Pid:~p,Exit:~p~n",[Pid,Why]),
						ParentId ! {scan_done,Why}
				end
				end).
writefile(Log) ->
	case file:open("InterfaceInfo.log",[append]) of
		{ok,File} -> io:format(File,"~n~p~n~n",[Log]),
			file:close(File);
		_ -> ok
	end.
	
getScanInfo(Param) ->
	%~ io:format("getScanInfo param:~p~n",[Param]),
	case proplists:get_value(sysobjectId,Param,"") of
		"" ->
			[{deviceInfo,Param}];
		R ->
			SnmpParam = proplists:get_value(snmpParam,Param,[]),
			SysobjectIdList = ets:lookup_element(?MODULE, sysobjectIdList, 2),
			SpecialOidList = ets:lookup_element(?MODULE, specialOidList, 2),
			Ip = proplists:get_value(ip,Param,""),
			SysobjectId = nnm_discovery_util:stringToIntegerList(R,"."),
%% 			io:format("SysobjectId:~p~n",[SysobjectId]),
			ScanObject = 
				case lists:sublist(SysobjectId, 7) of
					[1,3,6,1,4,1,9] -> 
						Len = length(proplists:get_value(getCommunity,SnmpParam)),
						LogicalCommunity = nnm_snmp_api:readTreeValue([{ip,Ip}|SnmpParam],?ENTLOGICALCOMMUNITY),
						FLogicalCommunity = [V || 
										 	{_,V} <- LogicalCommunity,
										 	string:substr(V, Len+1)=/="@1001",
										 	string:substr(V, Len+1)=/="@1002",
										 	string:substr(V, Len+1)=/="@1003",
										 	string:substr(V, Len+1)=/="@1004",
										 	string:substr(V, Len+1)=/="@1005"],
						nnm_discovery_snmp_read_cisco:new([{ip,Ip}|SnmpParam],SpecialOidList,FLogicalCommunity);
					_ ->
						nnm_discovery_snmp_read:new([{ip,Ip}|SnmpParam],SpecialOidList,SysobjectIdList)
				end,
			
%% 			DeviceInfo = ScanObject:readDeviceInfo(SysobjectId) ++ 
%% 							 lists:keyreplace(sysobjectId, 1, Param, {sysobjectId,nnm_discovery_util:integerListToString(SysobjectId, 10, ".")}),
			DeviceInfo = Param,
			DevType = proplists:get_value(devType,DeviceInfo),
			
			
			Data = 
				case DevType of
					?NNM_DEVICE_TYPE_SWITCH ->
						io:format("start read  SWITCH device ~p SysobjectId:~p info...~n", [Ip,SysobjectId]),
					%% debug HuaWei
						%~ case lists:sublist(SysobjectId, 9) of

							%~ [1,3,6,1,4,1,25506,1,23] -> 
								%~ io:format("test Gpon~n"),	
								%~ InterfaceInfo = ScanObject:readInterface(SysobjectId),
								%~ GponSnInfo = ScanObject:readGponOlt(SysobjectId),
								%~ [{interfaceInfo,InterfaceInfo},{gponOltInfo,GponSnInfo}];
							%~ _-> io:format("other"),
						
					%%---------------------------	
						nnm_scan:print_scan_info("start read device " ++ Ip),
						AftInfo = ScanObject:readAft(SysobjectId),
						InterfaceInfo = ScanObject:readInterface(SysobjectId),
						io:format("start read  SWITCH device ~p SysobjectId:~p info...~n", [Ip,SysobjectId]),
						nnm_scan:print_scan_info("end read SWITCH device " ++ Ip),
						[{interfaceInfo,InterfaceInfo},{aftInfo,AftInfo}];
						 %~ end;
					%%---------------------------	
					
					
					?NNM_DEVICE_TYPE_ROUTE ->
						io:format("start read device ~p info...~n", [Ip]),
						nnm_scan:print_scan_info("start read device " ++ Ip),
						io:format("start read interface..."),
						InterfaceInfo = ScanObject:readInterface(SysobjectId),
						RouteInfo = ScanObject:readRoute(SysobjectId),
						ArpInfo = ScanObject:readArp(SysobjectId),
						OspfInfo = ScanObject:readOspf(SysobjectId),
						BgpInfo = ScanObject:readBgp(SysobjectId),
						DirectInfo = ScanObject:readDirect(SysobjectId),
						io:format("end read device ~p info...~n", [Ip]),
						nnm_scan:print_scan_info("end read device " ++ Ip),
						[{interfaceInfo,InterfaceInfo},{routeInfo,RouteInfo},{arpInfo,ArpInfo},{ospfInfo,OspfInfo},{bgpInfo,BgpInfo},
					 	{directInfo,DirectInfo}];
					?NNM_DEVICE_TYPE_ROUTE_SWITCH ->
			
					    io:format("start read  ROUTE_SWITCH device ~p SysobjectId:~p info...~n", [Ip,SysobjectId]),

					%% gpon--------------
						case lists:sublist(SysobjectId, 9) of
						%%gpon OLT
						OLT when OLT =:= [1,3,6,1,4,1,2011,2,115] ; OLT =:= [1,3,6,1,4,1,2011,2,133] ->
							%~ [1,3,6,1,4,1,2011,2,115] or [1,3,6,1,4,1,2011,2,133]->
							io:format("test Gpon OLT"),
							InterfaceInfo = ScanObject:readInterface(SysobjectId),
							GponOltInfo = ScanObject:readGponOlt(SysobjectId),
							[{interfaceInfo,InterfaceInfo},{gponOltInfo,GponOltInfo}];
						%%gpon ONU
						ONU when ONU =:= [1,3,6,1,4,1,2011,2,135] ; ONU =:= [1,3,6,1,4,1,2011,2,186] ->
							io:format("test Gpon ONU"),	
							InterfaceInfo = ScanObject:readInterface(SysobjectId),	
							GponOnuInfo = ScanObject:readGponOnu(SysobjectId),
							[{interfaceInfo,InterfaceInfo},{gponOnuInfo,GponOnuInfo}];
						_-> io:format("other"),
		
					%%---------------------------	
					
						io:format("start read device ~p info...~n", [Ip]),
						nnm_scan:print_scan_info("start read device " ++ Ip),
						InterfaceInfo = ScanObject:readInterface(SysobjectId),
						RouteInfo = ScanObject:readRoute(SysobjectId),
						ArpInfo = ScanObject:readArp(SysobjectId),
						OspfInfo = ScanObject:readOspf(SysobjectId),
						BgpInfo = ScanObject:readBgp(SysobjectId),
						DirectInfo = ScanObject:readDirect(SysobjectId),
						io:format("start read aft"),
						AftInfo = ScanObject:readAft(SysobjectId),
						io:format("end read device ~p info...~n", [Ip]),
						nnm_scan:print_scan_info("end read device " ++ Ip),
						[{interfaceInfo,InterfaceInfo},{routeInfo,RouteInfo},{arpInfo,ArpInfo},{ospfInfo,OspfInfo},{bgpInfo,BgpInfo},
					 	{directInfo,DirectInfo},{aftInfo,AftInfo}]
						
						end;
					%%---------------------------	
	
					?NNM_DEVICE_TYPE_FIREWALL ->
						io:format("start read device ~p info...~n", [Ip]),
						nnm_scan:print_scan_info("start read device " ++ Ip),
						InterfaceInfo = ScanObject:readInterface(SysobjectId),
						RouteInfo = ScanObject:readRoute(SysobjectId),
						ArpInfo = ScanObject:readArp(SysobjectId),
						OspfInfo = ScanObject:readOspf(SysobjectId),
						BgpInfo = ScanObject:readBgp(SysobjectId),
						DirectInfo = ScanObject:readDirect(SysobjectId),
%% 						io:format("start read aft"),
%% 						AftInfo = ScanObject:readAft(SysobjectId),
						io:format("end read device ~p info...~n", [Ip]),
						nnm_scan:print_scan_info("end read device " ++ Ip),
						[{interfaceInfo,InterfaceInfo},{routeInfo,RouteInfo},{arpInfo,ArpInfo},{ospfInfo,OspfInfo},{bgpInfo,BgpInfo},
					 	{directInfo,DirectInfo}];
					_ ->
						[]
				end,
 			io:format("Data:~p~n",[Data]),
			case proplists:get_value(interfaceInfo,Data,[]) of
				[] ->
					[{deviceInfo,DeviceInfo} | Data];
				{_,Infs} ->
					Inrec = proplists:get_value(inrec,Infs,[]),
					FInfs = [{proplists:get_value(ifIndex, X),
			  				proplists:get_value(ifType, X),
			  				proplists:get_value(ifDescr, X),
			  				proplists:get_value(ifMac, X),
			  				proplists:get_value(ifSpeed, X),
			  				proplists:get_value(ifAlias, X),
			  				proplists:get_value(ifPort, X)}
							|| X <- Inrec],
					
					%io:format("FInfs:~p~n",[FInfs]),
					[{deviceInfo,[{infs,FInfs}|DeviceInfo]} | Data]
			end
	end.
















	
	