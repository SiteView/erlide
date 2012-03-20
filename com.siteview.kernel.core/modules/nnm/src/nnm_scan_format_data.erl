-module(nnm_scan_format_data).
-compile(export_all).

dataFormat(DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo) ->
	{FDeviceInfo,FInterfaceInfo,FRouteInfo,FAftInfo,FArpInfo,FOspfInfo,FBgpInfo,FDirectInfo} = 
		analyseFormat(DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo),
	{DeviceData,InterfaceData,RouteData,AftData,ArpData,OspfData,BgpData,DirectData} = 
		analyseEncode(FDeviceInfo,FInterfaceInfo,FRouteInfo,FAftInfo,FArpInfo,FOspfInfo,FBgpInfo,FDirectInfo),
	[list_to_tuple(DeviceData),list_to_tuple(InterfaceData),list_to_tuple(RouteData),list_to_tuple(AftData),list_to_tuple(ArpData),
	 list_to_tuple(OspfData),list_to_tuple(BgpData),list_to_tuple(DirectData)].

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
	IpAddr = proplists:get_value(ipAddr,H,[]),
	FIpAddr = lists:filter(fun(X) -> 
									(proplists:get_value(ipAddress,X) =/= "127.0.0.1") andalso 
										(proplists:get_value(ipMask, X) =/= "255.0.0.0") 
							end, IpAddr),
	[lists:keyreplace(ipAddr, 1, H, {ipAddr,FIpAddr})|analyseFormatDevice(DeviceInfo)].

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
%% 	io:format("H:~p~n",[H]),
	IfIndex = proplists:get_value(ifIndex,H),
	Ip = proplists:get_value(ip,H,""),
	Str = 
		case string:tokens(Ip,".") of
			[] -> "";
			VV -> lists:nth(4, VV)
		end,
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
	IpAddr = lists:append(proplists:get_value(ipAddr,H,[])),
	IpAdEntAddr = proplists:get_all_values(ipAddress, IpAddr),
	IpAdEntIfIndex = proplists:get_all_values(ipIfIndex, IpAddr),
	IpAdEntNetMask = proplists:get_all_values(ipMask, IpAddr),
	SnmpParam = proplists:get_value(snmpParam,H,[]),
	Ip = proplists:get_value(ip,H,""),
	BaseMac = proplists:get_value(baseMac,H,""),
	SnmpFlag = proplists:get_value(snmpFlag,H,"0"),
	CommunityGet = proplists:get_value(getCommunity,SnmpParam,""),
	CommunitySet = proplists:get_value(setCommunity,SnmpParam,""),
	DevType = proplists:get_value(devType,H,""),
	DevFactory = proplists:get_value(devFactory,H,""),
	DevModel = proplists:get_value(devModel,H,""),
	DevTypeName = proplists:get_value(devTypeName,H,""),
	SysobjectId = proplists:get_value(sysobjectId,H,""),
	SysSvcs = proplists:get_value(sysSvcs,H,""),
	SysName = proplists:get_value(sysName,H,""),
	Macs = proplists:get_value(macs,H,""),
	[{Ip,BaseMac,SnmpFlag,CommunityGet,CommunitySet,DevType,DevFactory,DevModel,DevTypeName,SysobjectId,SysSvcs,SysName,
	 	list_to_tuple(IpAdEntAddr),list_to_tuple(IpAdEntIfIndex),list_to_tuple(IpAdEntNetMask),list_to_tuple(Macs)}|analyseEncodeDevice(DeviceInfo)].

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