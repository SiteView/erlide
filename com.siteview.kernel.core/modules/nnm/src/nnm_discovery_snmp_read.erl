-module(nnm_discovery_snmp_read,[SnmpParam,SpecialOidList,SysObjectIdList]).
-compile(export_all).

-include("nnm_discovery_setting.hrl").
-include("nnm_define.hrl").
-include("nnm_discovery_topo.hrl").

-define(OTHER,"OTHER").

new(SnmpParam,SpecialOidList,SysObjectIdList) ->
	{?MODULE,SnmpParam,SpecialOidList,SysObjectIdList}.

new(SnmpParam,SpecialOidList) ->
	{?MODULE,SnmpParam,SpecialOidList,[]}.

new(SnmpParam) ->
	{ok,SysobjectIdList} = file:consult(?NNM_DISCOVERY_SYSOBJECTPATH),
	{ok,SpecialOidList} = file:consult(?NNM_DISCOVERY_SPECIALOIDLISTPATH),
	{?MODULE,SnmpParam,SpecialOidList,SysobjectIdList}.

getValue(O1,O2) ->
	case nnm_snmp_api:readOneValue(SnmpParam,O1) of
		[] ->
			case O2 of
				"" -> [];
				_ -> nnm_snmp_api:readOneValue(SnmpParam,O2)
			end;
		V ->
			V
	end.

getTreeValue(O1,O2) ->
	case nnm_snmp_api:readTreeValue(SnmpParam,O1) of
		[] ->
			case O2 of
				"" -> [];
				_ -> nnm_snmp_api:readTreeValue(SnmpParam,O2)
			end;
		V ->
			V
	end.

getSpecialOid(SysobjectId,OidNameList) ->
 	%%io:format("SysobjectId:~p~nSpecialOidList~p~n",[SysobjectId,SpecialOidList]),
	if
		length(SysobjectId) < 7 ->
		    [{X,""} || X <- OidNameList];
		true ->
			case proplists:get_value(SysobjectId, SpecialOidList) of
				undefined ->
					SysOid = lists:sublist(SysobjectId, 1, length(SysobjectId)-1),
					getSpecialOid(SysOid,OidNameList);
				V ->
					[{X,proplists:get_value(X, V, "")} || X <- OidNameList]
			end
	end.

readDeviceInfo() ->
	case nnm_snmp_api:snmpPing(SnmpParam) of
		[] -> 
			[];
		SysobjectId ->
			SpecialOidValueList = getSpecialOid(SysobjectId,[sysName,sysUpTime,sysServices,sysDescr,baseMac,ifMac]),

			SysName = getValue(?SYSNAME, proplists:get_value(sysName, SpecialOidValueList, "")),
			SysUpTime = getValue(?SYSUPTIME, proplists:get_value(sysUpTime, SpecialOidValueList, "")),
			SysSvcs = getValue(?SYSSERVICES, proplists:get_value(sysServices, SpecialOidValueList, "")),
			SysDescr = getValue(?SYSDESCR, proplists:get_value(sysDescr, SpecialOidValueList, "")),
			BaseMac = getValue(?BASEMAC, proplists:get_value(baseMac, SpecialOidValueList, "")),
			Macs = getTreeValue(?IFMAC,proplists:get_value(ifMac, SpecialOidValueList, "")),
			
			FormatBaseMac = nnm_discovery_util:toMac(BaseMac),
			FormatMacs = formatMacs(Macs,[]),
			
			FMacs = case FormatBaseMac =:= [] orelse lists:member(FormatBaseMac, FormatMacs) of
						true ->
							FormatMacs;
						false ->
							FormatMacs ++ [FormatBaseMac]
					end,
			FBaseMac = case FormatBaseMac of
						   "" -> 
							   case FMacs of
								   [] -> "";
								   _ -> lists:nth(1, FMacs)
							   end;
						   _ -> FormatBaseMac
					   end,
			FSysUpTime = case SysUpTime of
							 "" -> "0";
							 _ -> 
								 {Days,{Hours,Minutes,Seconds}} = calendar:seconds_to_daystime(SysUpTime div 100),
								 erlang:integer_to_list(Days) ++ "days " ++ 
									 integer_to_list(Hours) ++ "hours " ++
									 integer_to_list(Minutes) ++ "minutes " ++ 
									 integer_to_list(Seconds) ++ "seconds"
						 end,
			
			IpTable = readIp(SysobjectId),
			
%% 			io:format("ipTable:~p~n",[IpTable]),
			Subnet = [nnm_discovery_util:subnet(SubIp,Mask) || 
						X <- IpTable,
						Mask <- [proplists:get_value(ipMask,X,"")],
						SubIp <- [proplists:get_value(ipAddress,X,"")],
						(SubIp =/= "" andalso SubIp =/= "127.0.0.1" andalso SubIp =/= "0.0.0.0"),
						(Mask =/= "" andalso Mask =/= "0.0.0.0" andalso Mask =/= "255.255.255.255")],
	
			DeviceSys = getsysObjectInfo(SysSvcs,SysobjectId,SysObjectIdList),
			
			[{ip,proplists:get_value(ip,SnmpParam)},
			 {snmpFlag,"1"},
			 {snmpParam,lists:keydelete(ip, 1, SnmpParam)},
			 {baseMac,FBaseMac},
			 {sysDescr,SysDescr},
			 {sysSvcs,nnm_discovery_util:integerToString(SysSvcs)},
			 {sysName,SysName},
			 {sysobjectId,nnm_discovery_util:integerListToString(SysobjectId, 10, ".")},
			 {sysUpTime,FSysUpTime},
			 {ipAddr,IpTable},
			 {subnet,Subnet},
			 {macs,FMacs}] ++ DeviceSys
			
%% 			#nnm_device{
%% 						devIp = proplists:get_value(ip,SnmpParam),
%% 						baseMac = FBaseMac,
%% 	  				    snmpFlag = "1",
%% 						snmpParam = lists:keydelete(ip, 1, SnmpParam),
%% 						devType = proplists:get_value(devType,DeviceSys),
%% 						devFactory = proplists:get_value(devFactory,DeviceSys),
%% 						devModel = proplists:get_value(devModel,DeviceSys),
%% 						devTypeName = proplists:get_value(devTypeName,DeviceSys),
%% 						sysDescr = SysDescr,
%% 						sysobjectId = nnm_discovery_util:integerListToString(SysobjectId, 10, "."),
%% 						sysSvcs = nnm_discovery_util:integerToString(SysSvcs),
%% 						sysName = SysName,
%% 						ipAddr = IpTable,
%% 						macs = FMacs
%% 						}
	end.
	
formatMacs([],Result) -> Result;
formatMacs([H|DeviceMacs],Result) ->
	{_,Value} = H,
	case (Value=:=[]) orelse (Value=:=[0,0,0,0,0,0]) orelse (Value=:=[256,256,256,256,256,256]) of
		true ->
			formatMacs(DeviceMacs,Result);
		false ->
			Mac = nnm_discovery_util:toMac(Value),
			case lists:member(Mac, Result) of
				true ->
					formatMacs(DeviceMacs,Result);
				false ->
					formatMacs(DeviceMacs,[Mac|Result])
			end
	end.

readDeviceInfo(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[sysName,sysServices,sysDescr,baseMac,ifMac]),

	SysName = getValue(?SYSNAME, proplists:get_value(sysName, SpecialOidValueList, "")),
	SysSvcs = getValue(?SYSSERVICES, proplists:get_value(sysServices, SpecialOidValueList, "")),
	SysDescr = getValue(?SYSDESCR, proplists:get_value(sysDescr, SpecialOidValueList, "")),
	BaseMac = getValue(?BASEMAC, proplists:get_value(baseMac, SpecialOidValueList, "")),
	Macs = getTreeValue(?IFMAC,proplists:get_value(ifMac, SpecialOidValueList, "")),
			
	FBaseMac = nnm_discovery_util:toMac(BaseMac),
	FMacs = 
		case formatMacs(Macs,[]) of
			[] ->
				[FBaseMac];
			R ->
				case lists:member(FBaseMac, R) of
					true ->
						R;
					false ->
						R ++ [FBaseMac]
				end
		end,
			
	IpTable = readIp(SysobjectId),
	DeviceSys = getsysObjectInfo(SysSvcs,SysobjectId,SysObjectIdList),
	[{baseMac,FBaseMac},
	 {sysDescr,SysDescr},
	 {sysSvcs,nnm_discovery_util:integerToString(SysSvcs)},
	 {sysName,SysName},
	 {ipAddr,IpTable},
	 {macs,FMacs}] ++ DeviceSys.

readIp(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ipAddress,ipMask,ipIfIndex]),
	IpAddress = getTreeValue(?IPADDRESS,proplists:get_value(ipAddress, SpecialOidValueList, "")),
	IpMask = getTreeValue(?IPMASK,proplists:get_value(ipMask, SpecialOidValueList, "")),
	IpIfIndex = getTreeValue(?IPIFINDEX,proplists:get_value(ipIfIndex, SpecialOidValueList, "")),
	
	[[{ipAddress,nnm_discovery_util:integerListToString(Ip, 10, ".")},
	  {ipMask,nnm_discovery_util:integerListToString(Mask, 10, ".")},
	  {ipIfIndex,nnm_discovery_util:integerToString(IfIndex)}] 
	|| {K1,Ip} <- IpAddress, {K2,Mask} <- IpMask, {K3,IfIndex} <- IpIfIndex, (K1 =:= K2 andalso K1 =:= K3)].

readIpTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ipAddress,ipMask,ipIfIndex]),
	IpAddress = getTreeValue(?IPADDRESS,proplists:get_value(ipAddress, SpecialOidValueList, "")),
	IpMask = getTreeValue(?IPMASK,proplists:get_value(ipMask, SpecialOidValueList, "")),
	IpIfIndex = getTreeValue(?IPIFINDEX,proplists:get_value(ipIfIndex, SpecialOidValueList, "")),
	
	Result = 
		[{nnm_discovery_util:integerListToString(Ip, 10, "."),
		   nnm_discovery_util:integerToString(IfIndex),
		   nnm_discovery_util:integerListToString(Mask, 10, ".")} 
		|| {K1,Ip} <- IpAddress, {K2,Mask} <- IpMask, {K3,IfIndex} <- IpIfIndex, (K1 =:= K2 andalso K1 =:= K3)],

	{proplists:get_value(ip,SnmpParam),Result}.

getsysObjectInfo(SysSvcs,_,[]) -> 
	{DevType,DevTypeName} = 
		if
			SysSvcs =:= 0 ->
				{?NNM_DEVICE_TYPE_HOST,"HOST"};
			SysSvcs band 6 =:= 6 ->
				{?NNM_DEVICE_TYPE_ROUTE_SWITCH,"ROUTE_SWITCH"};
			SysSvcs band 4 =:= 4 ->
				{?NNM_DEVICE_TYPE_ROUTE,"ROUTE"};
			SysSvcs band 2 =:= 2 ->
				{?NNM_DEVICE_TYPE_SWITCH,"SWITCH"};
			true ->
				{?NNM_DEVICE_TYPE_OTHER,"OTHER"}
		end,
	[
	 {devType,DevType},
	 {devTypeName,DevTypeName},
	 {devModel,""},
	 {devFactory,""}
	 ];
getsysObjectInfo(SysSvcs,Result,[H|IdList]) ->
	{SysObjectID,Len,Type,TypeName,Model,Enterprises} = H,
	case ((length(Result)>=Len) andalso (lists:sublist(Result, Len)==SysObjectID)) of
		true ->
			[{devType,nnm_discovery_util:integerToString(Type)},
			 {devTypeName,TypeName},
			 {devModel,Model},
			 {devFactory,Enterprises}];
		false ->
			getsysObjectInfo(SysSvcs,Result,IdList)
	end.

readInterface(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ifNumber,ifIndex,ifType,ifDescr,ifMac,ifSpeed,ifAlia,ifPort]),
	IfNumber = getValue(?IFNUMBER, proplists:get_value(ifNumber, SpecialOidValueList, "")),
	IfIndex = getTreeValue(?IFINDEX, proplists:get_value(ifIndex, SpecialOidValueList, "")),
	IfDescr = getTreeValue(?IFDESCR, proplists:get_value(ifDescr, SpecialOidValueList, "")),
	IfType = getTreeValue(?IFTYPE, proplists:get_value(ifType, SpecialOidValueList, "")),
	IfSpeed = getTreeValue(?IFSPEED,proplists:get_value(ifMac, SpecialOidValueList, "")),
	IfMac = getTreeValue(?IFMAC,proplists:get_value(ifSpeed, SpecialOidValueList, "")),
	IfAlias = getTreeValue(?IFALIAS,proplists:get_value(ifAlias, SpecialOidValueList, "")),
	IfPort = getTreeValue(?IFPORT,proplists:get_value(ifPort, SpecialOidValueList, "")),
	
%% GPON ifDescr are all the same,use "ifDescr" converted ,such as 0/0/1 
	IndexDescrTypeSpeedMacAliasPort = 
		case lists:sublist(SysobjectId, 9) of
			GPON when GPON =:= [1,3,6,1,4,1,2011,2,115]; GPON =:= [1,3,6,1,4,1,2011,2,133] ; 
			    GPON =:= [1,3,6,1,4,1,2011,2,135]; GPON =:= [1,3,6,1,4,1,2011,2,186] ->
			    io:format("GPON"),
			    gponConnectIndexDescrTypeSpeedMacAliasPort(IfIndex,IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort);

			_-> io:format("other"),
			    connectIndexDescrTypeSpeedMacAliasPort(IfIndex,IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort)
		end,
	%~ IndexDescrTypeSpeedMacAliasPort = 
	     %~ connectIndexDescrTypeSpeedMacAliasPort(IfIndex,IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort),

	io:format("~n~n~n IndexDescrTypeSpeedMacAliasPort = ~p~n~n~n",[IndexDescrTypeSpeedMacAliasPort]),
	{proplists:get_value(ip,SnmpParam,""),[{ifAmount,nnm_discovery_util:integerToString(IfNumber)},{inrec,IndexDescrTypeSpeedMacAliasPort}]}.


connectIndexDescrTypeSpeedMacAliasPort([],_,_,_,_,_,_) -> [];
connectIndexDescrTypeSpeedMacAliasPort([H|IfIndex],IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort) ->
	{Key,Index} = H,
	Descr = proplists:get_value(Key, IfDescr, ""),
	Type = proplists:get_value(Key, IfType, ""),
	Speed = proplists:get_value(Key, IfSpeed, ""),
	Mac = proplists:get_value(Key, IfMac, ""),
	Alias = proplists:get_value(Key, IfAlias, ""),
	Port =
		case lists:filter(fun(X) -> {_,V}=X,V=:=Index end, IfPort) of
			[] -> Index;
			[{[P],_}] -> P
		end,
		
	%~ [[{ifIndex,nnm_discovery_util:integerToString(ifIndexSmallThanZero(Index))},
	  %~ {ifType,nnm_discovery_util:integerToString(Type)},
	  %~ {ifDescr, Descr},
	  %~ {ifMac,nnm_discovery_util:toMac(Mac)},
	  %~ {ifSpeed,nnm_discovery_util:integerToString(Speed)},
	  %~ {ifPort,nnm_discovery_util:integerToString(ifIndexSmallThanZero(Port))},
	  %~ {ifAlias,Alias}]|
		 %~ connectIndexDescrTypeSpeedMacAliasPort(IfIndex,IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort)].


	[[{ifIndex,nnm_discovery_util:integerToString(Index)},
	  {ifType,nnm_discovery_util:integerToString(Type)},
	  {ifDescr, Descr},
	  {ifMac,nnm_discovery_util:toMac(Mac)},
	  {ifSpeed,nnm_discovery_util:integerToString(Speed)},
	  {ifPort,nnm_discovery_util:integerToString(Port)},
	  {ifAlias,Alias}]|
		 connectIndexDescrTypeSpeedMacAliasPort(IfIndex,IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort)].


readInterfaceTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ifIndex,ifDescr,ifType,ifMtu,ifMac,ifSpeed,ifAdminStatus,ifOperStatus,ifLastChange,
													 ifInOctets,ifInUcastPkts,ifInNUcastPkts,ifInDiscards,ifInErrors,ifInUnknownProtos,
													 ifOutOctets,ifOutUcastPkts,ifOutNUcastPkts,ifOutDiscards,ifOutErrors,ifOutQLen,
													 ifAlia]),
	IfIndex = getTreeValue(?IFINDEX, proplists:get_value(ifIndex, SpecialOidValueList, "")),
	IfDescr = getTreeValue(?IFDESCR, proplists:get_value(ifDescr, SpecialOidValueList, "")),
	IfType = getTreeValue(?IFTYPE, proplists:get_value(ifType, SpecialOidValueList, "")),
	IfMtu = getTreeValue(?IFMTU, proplists:get_value(ifMtu, SpecialOidValueList, "")),
	IfSpeed = getTreeValue(?IFSPEED,proplists:get_value(ifMac, SpecialOidValueList, "")),
	IfMac = getTreeValue(?IFMAC,proplists:get_value(ifSpeed, SpecialOidValueList, "")),
	IfAdminStatus = getTreeValue(?IFADMINSTATUS,proplists:get_value(ifAdminStatus, SpecialOidValueList, "")),
	IfOperStatus = getTreeValue(?IFOPERSTATUS,proplists:get_value(ifOperStatus, SpecialOidValueList, "")),
	IfLastChange = getTreeValue(?IFLASTCHANGE,proplists:get_value(ifLastChange, SpecialOidValueList, "")),
	IfInOctets = getTreeValue(?IFINOCTETS,proplists:get_value(ifInOctets, SpecialOidValueList, "")),
	IfInUcastPkts = getTreeValue(?IFINUCASTPKTS,proplists:get_value(ifInUcastPkts, SpecialOidValueList, "")),
	IfInNUcastPkts = getTreeValue(?IFINNUCASTPKTS,proplists:get_value(ifInNUcastPkts, SpecialOidValueList, "")),
	IfInDiscards = getTreeValue(?IFINDISCARDS,proplists:get_value(ifInDiscards, SpecialOidValueList, "")),
	IfInErrors = getTreeValue(?IFINERRORS,proplists:get_value(ifInErrors, SpecialOidValueList, "")),
	IfInUnknownProtos = getTreeValue(?IFINUNKNOWNPROTOS,proplists:get_value(ifInUnknownProtos, SpecialOidValueList, "")),
	IfOutOctets = getTreeValue(?IFOUTOCTETS,proplists:get_value(ifOutOctets, SpecialOidValueList, "")),
	IfOutUcastPkts = getTreeValue(?IFOUTUCASTPKTS,proplists:get_value(ifOutUcastPkts, SpecialOidValueList, "")),
	IfOutNUcastPkts = getTreeValue(?IFOUTNUCASTPKTS,proplists:get_value(ifOutNUcastPkts, SpecialOidValueList, "")),
	IfOutDiscards = getTreeValue(?IFOUTDISCARDS,proplists:get_value(ifOutDiscards, SpecialOidValueList, "")),
	IfOutErrors = getTreeValue(?IFOUTERRORS,proplists:get_value(ifOutErrors, SpecialOidValueList, "")),
	IfOutQLen = getTreeValue(?IFOUTQLEN,proplists:get_value(ifOutQLen, SpecialOidValueList, "")),
	IfAlias = getTreeValue(?IFALIAS,proplists:get_value(ifAlias, SpecialOidValueList, "")),
			
	Result = connectInterfaceTable(IfIndex,IfDescr,IfType,IfMtu,IfSpeed,IfMac,IfAdminStatus,IfOperStatus,IfLastChange,IfInOctets,IfInUcastPkts,
						  	IfInNUcastPkts,IfInDiscards,IfInErrors,IfInUnknownProtos,IfOutOctets,IfOutUcastPkts,IfOutNUcastPkts,
							IfOutDiscards,IfOutErrors,IfOutQLen,IfAlias),
	{proplists:get_value(ip,SnmpParam),Result}.

connectInterfaceTable([],_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_) -> [];
connectInterfaceTable([H|IfIndex],IfDescr,IfType,IfMtu,IfSpeed,IfMac,IfAdminStatus,IfOperStatus,IfLastChange,IfInOctets,IfInUcastPkts,
					 	IfInNUcastPkts,IfInDiscards,IfInErrors,IfInUnknownProtos,IfOutOctets,IfOutUcastPkts,IfOutNUcastPkts,
						IfOutDiscards,IfOutErrors,IfOutQLen,IfAlias) ->
	{Key,Value} = H,
	Index = nnm_discovery_util:integerToString(Value),
	Descr = proplists:get_value(Key, IfDescr, ""),
	Type = nnm_discovery_util:integerToString(proplists:get_value(Key, IfType, "")),
	Mtu = proplists:get_value(Key, IfMtu, ""),
	Speed = nnm_discovery_util:integerToString(proplists:get_value(Key, IfSpeed, "")),
	Mac = nnm_discovery_util:toMac(proplists:get_value(Key, IfMac, "")),
	AdminStatus = nnm_discovery_util:integerToString(proplists:get_value(Key, IfAdminStatus, "")),
	OperStatus = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOperStatus, "")),
	LastChange = nnm_discovery_util:integerToString(proplists:get_value(Key, IfLastChange, "")),
	InOctets = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInOctets, "")),
	InUcastPkts = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInUcastPkts, "")),
	InNUcastPkts = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInNUcastPkts, "")),
	InDiscards = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInDiscards, "")),
	InErrors = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInErrors, "")),
	InUnknownProtos = nnm_discovery_util:integerToString(proplists:get_value(Key, IfInUnknownProtos, "")),
	OutOctets = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutOctets, "")),
	OutUcastPkts = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutUcastPkts, "")),
	OutNUcastPkts = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutNUcastPkts, "")),
	OutDiscards = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutDiscards, "")),
	OutErrors = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutErrors, "")),
	OutQLen = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOutQLen, "")),
	Alias = proplists:get_value(Key, IfAlias, ""),
	
	[{Index,Descr,Type,Mtu,Speed,Mac,AdminStatus,OperStatus,LastChange,InOctets,InUcastPkts,InNUcastPkts,InDiscards,InErrors,InUnknownProtos,
	  OutOctets,OutUcastPkts,OutNUcastPkts,OutDiscards,OutErrors,OutQLen,Alias}|
		 connectInterfaceTable(IfIndex,IfDescr,IfType,IfMtu,IfSpeed,IfMac,IfAdminStatus,IfOperStatus,IfLastChange,IfInOctets,IfInUcastPkts,
					 	IfInNUcastPkts,IfInDiscards,IfInErrors,IfInUnknownProtos,IfOutOctets,IfOutUcastPkts,IfOutNUcastPkts,
						IfOutDiscards,IfOutErrors,IfOutQLen,IfAlias)].
													   

readRoute(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[routeDest,routeIfIndex,routeNextHop,routeType,routeMask]),
	RouteDest = getTreeValue(?ROUTEDEST, proplists:get_value(routeDest, SpecialOidValueList, "")),
	RouteIfIndex = getTreeValue(?ROUTEIFINDEX, proplists:get_value(routeIfIndex, SpecialOidValueList, "")),
	RouteNextHop = getTreeValue(?ROUTENEXTHOP, proplists:get_value(routeNextHop, SpecialOidValueList, "")),
	RouteType = getTreeValue(?ROUTETYPE, proplists:get_value(routeType, SpecialOidValueList, "")),
	RouteMask = getTreeValue(?ROUTEMASK, proplists:get_value(routeMask, SpecialOidValueList, "")),
			
	DestIfIndexNextHopTypeMask = connectDestIfIndexNextHopTypeMask(RouteDest,RouteIfIndex,RouteNextHop,RouteType,RouteMask),
	{proplists:get_value(ip,SnmpParam,""),DestIfIndexNextHopTypeMask}.

connectDestIfIndexNextHopTypeMask([],_,_,_,_) -> [];
connectDestIfIndexNextHopTypeMask([H|RouteDest],RouteIfIndex,RouteNextHop,RouteType,RouteMask) ->
	{Key,Dest} = H,
	IfIndex = proplists:get_value(Key, RouteIfIndex, ""),
	NextHop = proplists:get_value(Key, RouteNextHop, ""),
	Type = proplists:get_value(Key, RouteType, ""),
	Mask = proplists:get_value(Key, RouteMask, ""),
	
	[[{routeDest,nnm_discovery_util:integerListToString(Dest,10,".")},
	  {routeIfIndex,nnm_discovery_util:integerToString(IfIndex)},
	  {routeNextHop,nnm_discovery_util:integerListToString(NextHop,10,".")},
	  {routeType,nnm_discovery_util:integerToString(Type)},
	  {routeMask,nnm_discovery_util:integerListToString(Mask,10,".")}]|
		 connectDestIfIndexNextHopTypeMask(RouteDest,RouteIfIndex,RouteNextHop,RouteType,RouteMask)].

readRouteTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[routeDest,routeIfIndex,routeNextHop,routeType,routeProto,routeMask]),
	RouteDest = getTreeValue(?ROUTEDEST, proplists:get_value(routeDest, SpecialOidValueList, "")),
	RouteIfIndex = getTreeValue(?ROUTEIFINDEX, proplists:get_value(routeIfIndex, SpecialOidValueList, "")),
	RouteNextHop = getTreeValue(?ROUTENEXTHOP, proplists:get_value(routeNextHop, SpecialOidValueList, "")),
	RouteType = getTreeValue(?ROUTETYPE, proplists:get_value(routeType, SpecialOidValueList, "")),
	RouteProto = getTreeValue(?ROUTEPROTO, proplists:get_value(routeProto, SpecialOidValueList, "")),
	RouteMask = getTreeValue(?ROUTEMASK, proplists:get_value(routeMask, SpecialOidValueList, "")),
	
	Result = connectRouteTable(RouteDest,RouteIfIndex,RouteNextHop,RouteType,RouteProto,RouteMask),
	{proplists:get_value(ip,SnmpParam),Result}.

connectRouteTable([],_,_,_,_,_) -> [];
connectRouteTable([H|RouteDest],RouteIfIndex,RouteNextHop,RouteType,RouteProto,RouteMask) ->
	{Key,Value} = H,
	Dest = nnm_discovery_util:integerListToString(Value,10,"."),
	IfIndex = nnm_discovery_util:integerToString(proplists:get_value(Key, RouteIfIndex)),
	NextHop = nnm_discovery_util:integerListToString(proplists:get_value(Key, RouteNextHop),10,"."),
	Type = nnm_discovery_util:integerToString(proplists:get_value(Key, RouteType)),
	Proto = proplists:get_value(Key, RouteProto),
	Mask = nnm_discovery_util:integerListToString(proplists:get_value(Key, RouteMask),10,"."),
	
	[{Dest,IfIndex,NextHop,Type,Proto,Mask}|connectRouteTable(RouteDest,RouteIfIndex,RouteNextHop,RouteType,RouteProto,RouteMask)].
	

readAft(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[dtpFdbPort,qtpFdbPort]),
	io:format("readAft SpecialOidValueList: ~p~n" ,[SpecialOidValueList]),
	DtpFdbPort = getTreeValue(?DTPFDBPORT, proplists:get_value(dtpFdbPort, SpecialOidValueList, "")),
	QtpFdbPort = getTreeValue(?QTPFDBPORT, proplists:get_value(qtpFdbPort, SpecialOidValueList, "")),
	
	FDtpFdbPort = [{nnm_discovery_util:integerToString(Value),nnm_discovery_util:toMac(lists:sublist(Key, 1, 6))} || 
				{Key,Value} <- DtpFdbPort,(length(Key) >= 6)],
	FQtpFdbPort = [{nnm_discovery_util:integerToString(Value),nnm_discovery_util:toMac(lists:sublist(Key, length(Key)-5, 6))} || 
				{Key,Value} <- QtpFdbPort,(length(Key) >= 6)],
	DtpQtp = nnm_discovery_util:mergerList(FDtpFdbPort,FQtpFdbPort),
	{proplists:get_value(ip,SnmpParam,""),[[{port,Port},{mac,Mac}] || {Port,Mac} <- DtpQtp]}.

readAftTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[dtpFdbAddress,dtpFdbPort,dtpFdbStatus,qtpFdbAddress,qtpFdbPort,qtpFdbStatus]),
	DtpFdbAddress = getTreeValue(?DTPFDBADDRESS, proplists:get_value(dtpFdbAddress, SpecialOidValueList, "")),
	DtpFdbPort = getTreeValue(?DTPFDBPORT, proplists:get_value(dtpFdbPort, SpecialOidValueList, "")),
	DtpFdbStatus = getTreeValue(?DTPFDBSTATUS, proplists:get_value(dtpFdbStatus, SpecialOidValueList, "")),
	QtpFdbAddress = getTreeValue(?QTPFDBADDRESS, proplists:get_value(qtpFdbAddress, SpecialOidValueList, "")),
	QtpFdbPort = getTreeValue(?QTPFDBPORT, proplists:get_value(qtpFdbPort, SpecialOidValueList, "")),
	QtpFdbStatus = getTreeValue(?QTPFDBSTATUS, proplists:get_value(qtpFdbStatus, SpecialOidValueList, "")),
	
	DtpFdb = connectAftTable(DtpFdbPort,DtpFdbAddress,DtpFdbStatus),
	QtpFdb = connectAftTable(QtpFdbPort,QtpFdbAddress,QtpFdbStatus),
%% 	io:format("DtpFdb:~p~n,QtpFdb:~p~n",[DtpFdb,QtpFdb]),
	Result = nnm_discovery_util:mergerList(DtpFdb,QtpFdb),
	{proplists:get_value(ip,SnmpParam),Result}.

connectAftTable([],_,_) -> [];
connectAftTable([H|FdbPort],FdbAddress,FdbStatus) ->
	{Key,Value} = H,
	BackAddress = lists:sublist(Key, length(Key)-5, 6),
	Port = nnm_discovery_util:integerToString(Value),
	Address = nnm_discovery_util:toMac(proplists:get_value(Key, FdbAddress, BackAddress)),
	Status = nnm_discovery_util:integerToString(proplists:get_value(Key, FdbStatus, "")),
	
	[{Port,Address,Status}|connectAftTable(FdbPort,FdbAddress,FdbStatus)].
	

readArp(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[arpMac]),
	ArpMac = getTreeValue(?ARPMAC, proplists:get_value(arpMac, SpecialOidValueList, "")),
	FArpMac = fArpMac(ArpMac),
	{proplists:get_value(ip,SnmpParam,""),FArpMac}.

fArpMac([]) -> [];
fArpMac([H|ArpMac]) ->
	{Key,Mac} = H,
	{[IfIndex],Ip} = lists:split(1, Key),
	[[{ifIndex,nnm_discovery_util:integerToString(IfIndex)},
	  {ip,nnm_discovery_util:integerListToString(Ip,10,".")},
	  {mac,nnm_discovery_util:toMac(Mac)}]|fArpMac(ArpMac)].
	
readArpTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[arpIfIndex,arpMac,arpIp,arpType]),
	ArpIfIndex = getTreeValue(?ARPIFINDEX, proplists:get_value(arpIfIndex, SpecialOidValueList, "")),
	ArpMac = getTreeValue(?ARPMAC, proplists:get_value(arpMac, SpecialOidValueList, "")),
	ArpIp = getTreeValue(?ARPIP, proplists:get_value(arpIp, SpecialOidValueList, "")),
	ArpType = getTreeValue(?ARPTYPE, proplists:get_value(arpType, SpecialOidValueList, "")),
	Result = connectArpTable(ArpIfIndex,ArpMac,ArpIp,ArpType),
	{proplists:get_value(ip,SnmpParam),Result}.

connectArpTable([],_,_,_) -> [];
connectArpTable([H|ArpIfIndex],ArpMac,ArpIp,ArpType) ->
	{Key,Value} = H,
	IfIndex = nnm_discovery_util:integerToString(Value),
	Mac = nnm_discovery_util:toMac(proplists:get_value(Key, ArpMac)),
	Ip = nnm_discovery_util:integerListToString(proplists:get_value(Key, ArpIp), 10, "."),
	Type = nnm_discovery_util:integerToString(proplists:get_value(Key, ArpType)),
	[{IfIndex,Mac,Ip,Type}|connectArpTable(ArpIfIndex,ArpMac,ArpIp,ArpType)].


readFlowTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ifIndex,ifSpeed,ifAdminStatus,ifOperStatus,
													 ifInOctets,ifInUcastPkts,ifInNUcastPkts,ifInDiscards,ifInErrors,
													 ifOutOctets,ifOutUcastPkts,ifOutNUcastPkts,ifOutDiscards,ifOutErrors]),
	IfIndex = getTreeValue(?IFINDEX, proplists:get_value(ifIndex, SpecialOidValueList, "")),
	IfSpeed = getTreeValue(?IFSPEED,proplists:get_value(ifMac, SpecialOidValueList, "")),
	IfAdminStatus = getTreeValue(?IFADMINSTATUS,proplists:get_value(ifAdminStatus, SpecialOidValueList, "")),
	IfOperStatus = getTreeValue(?IFOPERSTATUS,proplists:get_value(ifOperStatus, SpecialOidValueList, "")),
	IfInOctets = getTreeValue(?IFINOCTETS,proplists:get_value(ifInOctets, SpecialOidValueList, "")),
	IfInUcastPkts = getTreeValue(?IFINUCASTPKTS,proplists:get_value(ifInUcastPkts, SpecialOidValueList, "")),
	IfInNUcastPkts = getTreeValue(?IFINNUCASTPKTS,proplists:get_value(ifInNUcastPkts, SpecialOidValueList, "")),
	IfInDiscards = getTreeValue(?IFINDISCARDS,proplists:get_value(ifInDiscards, SpecialOidValueList, "")),
	IfInErrors = getTreeValue(?IFINERRORS,proplists:get_value(ifInErrors, SpecialOidValueList, "")),
	IfOutOctets = getTreeValue(?IFOUTOCTETS,proplists:get_value(ifOutOctets, SpecialOidValueList, "")),
	IfOutUcastPkts = getTreeValue(?IFOUTUCASTPKTS,proplists:get_value(ifOutUcastPkts, SpecialOidValueList, "")),
	IfOutNUcastPkts = getTreeValue(?IFOUTNUCASTPKTS,proplists:get_value(ifOutNUcastPkts, SpecialOidValueList, "")),
	IfOutDiscards = getTreeValue(?IFOUTDISCARDS,proplists:get_value(ifOutDiscards, SpecialOidValueList, "")),
	IfOutErrors = getTreeValue(?IFOUTERRORS,proplists:get_value(ifOutErrors, SpecialOidValueList, "")),
			
	Result = connectFlow(IfIndex,IfSpeed,IfAdminStatus,IfOperStatus,IfInOctets,IfInUcastPkts,
						  	IfInNUcastPkts,IfInDiscards,IfInErrors,IfOutOctets,IfOutUcastPkts,IfOutNUcastPkts,
							IfOutDiscards,IfOutErrors),
	timer:sleep(2000),
	SIfInOctets = getTreeValue(?IFINOCTETS,proplists:get_value(ifInOctets, SpecialOidValueList, "")),
	SIfInUcastPkts = getTreeValue(?IFINUCASTPKTS,proplists:get_value(ifInUcastPkts, SpecialOidValueList, "")),
	SIfInNUcastPkts = getTreeValue(?IFINNUCASTPKTS,proplists:get_value(ifInNUcastPkts, SpecialOidValueList, "")),
	SIfInDiscards = getTreeValue(?IFINDISCARDS,proplists:get_value(ifInDiscards, SpecialOidValueList, "")),
	SIfInErrors = getTreeValue(?IFINERRORS,proplists:get_value(ifInErrors, SpecialOidValueList, "")),
	SIfOutOctets = getTreeValue(?IFOUTOCTETS,proplists:get_value(ifOutOctets, SpecialOidValueList, "")),
	SIfOutUcastPkts = getTreeValue(?IFOUTUCASTPKTS,proplists:get_value(ifOutUcastPkts, SpecialOidValueList, "")),
	SIfOutNUcastPkts = getTreeValue(?IFOUTNUCASTPKTS,proplists:get_value(ifOutNUcastPkts, SpecialOidValueList, "")),
	SIfOutDiscards = getTreeValue(?IFOUTDISCARDS,proplists:get_value(ifOutDiscards, SpecialOidValueList, "")),
	SIfOutErrors = getTreeValue(?IFOUTERRORS,proplists:get_value(ifOutErrors, SpecialOidValueList, "")),
	SResult = connectFlow(IfIndex,IfSpeed,IfAdminStatus,IfOperStatus,SIfInOctets,SIfInUcastPkts,
						  	SIfInNUcastPkts,SIfInDiscards,SIfInErrors,SIfOutOctets,SIfOutUcastPkts,SIfOutNUcastPkts,
							SIfOutDiscards,SIfOutErrors),
%%  			io:format("Result:~p~n",[Result]),
%%  			io:format("SResult:~p~n",[SResult]),
	Flow = computeFlow(Result,SResult),
	
	{proplists:get_value(ip,SnmpParam),Flow}.

connectFlow([],_,_,_,_,_,_,_,_,_,_,_,_,_) -> [];
connectFlow([H|IfIndex],IfSpeed,IfAdminStatus,IfOperStatus,IfInOctets,IfInUcastPkts,IfInNUcastPkts,IfInDiscards,IfInErrors,IfOutOctets,
			IfOutUcastPkts,IfOutNUcastPkts,IfOutDiscards,IfOutErrors) ->
	{Key,Value} = H,
	Index = nnm_discovery_util:integerToString(Value),
	Speed = nnm_discovery_util:integerToString(proplists:get_value(Key, IfSpeed, "")),
	AdminStatus = nnm_discovery_util:integerToString(proplists:get_value(Key, IfAdminStatus, "")),
	OperStatus = nnm_discovery_util:integerToString(proplists:get_value(Key, IfOperStatus, "")),
	InOctets = proplists:get_value(Key, IfInOctets, 0),
	InUcastPkts = proplists:get_value(Key, IfInUcastPkts, 0),
	InNUcastPkts = proplists:get_value(Key, IfInNUcastPkts, 0),
	InDiscards = proplists:get_value(Key, IfInDiscards, 0),
	InErrors = proplists:get_value(Key, IfInErrors, 0),
	OutOctets = proplists:get_value(Key, IfOutOctets, 0),
	OutUcastPkts = proplists:get_value(Key, IfOutUcastPkts, 0),
	OutNUcastPkts = proplists:get_value(Key, IfOutNUcastPkts, 0),
	OutDiscards = proplists:get_value(Key, IfOutDiscards, 0),
	OutErrors = proplists:get_value(Key, IfOutErrors, 0),
	
	[{Index,[Speed,AdminStatus,OperStatus,InOctets,InUcastPkts,InNUcastPkts,InDiscards,InErrors,OutOctets,OutUcastPkts,OutNUcastPkts,
	 OutDiscards,OutErrors]}|
		 connectFlow(IfIndex,IfSpeed,IfAdminStatus,IfOperStatus,IfInOctets,IfInUcastPkts,IfInNUcastPkts,IfInDiscards,IfInErrors,
					 IfOutOctets,IfOutUcastPkts,IfOutNUcastPkts,IfOutDiscards,IfOutErrors)].

computeFlow([],_) -> [];
computeFlow([H|Result],SResult) ->
	{Index,[Speed,AdminStatus,OperStatus,InOctets,InUcastPkts,InNUcastPkts,InDiscards,InErrors,OutOctets,OutUcastPkts,OutNUcastPkts,
	 OutDiscards,OutErrors]} = H,
	[_,_,_,SInOctets,SInUcastPkts,SInNUcastPkts,SInDiscards,SInErrors,SOutOctets,SOutUcastPkts,SOutNUcastPkts,SOutDiscards,SOutErrors] =
		proplists:get_value(Index, SResult),
	F = fun(X) ->
				if 
					X < 0 -> 
						"0";
					true -> 
						nnm_discovery_util:floatToString(X,0)
				end
		end,
	Octets = ((SInOctets - InOctets) + (SOutOctets - OutOctets)) / 2,
	TInOctets = (SInOctets - InOctets) /2,
	TOutOctets = (SOutOctets - OutOctets) /2,
%%  	io:format("Octets:~p~n",[Octets]),
	UcastPkts = ((SInUcastPkts - InUcastPkts) + (SOutUcastPkts - OutUcastPkts)) / 2,
	TInUcastPkts = (SInUcastPkts - InUcastPkts) /2,
	TOutUcastPkts = (SOutUcastPkts - OutUcastPkts) /2,
	NUcastPkts = ((SInNUcastPkts - InNUcastPkts) + (SOutNUcastPkts - OutNUcastPkts)) / 2,
	TInNUcastPkts = (SInNUcastPkts - InNUcastPkts) /2,
	TOutNUcastPkts = (SOutNUcastPkts - OutNUcastPkts) /2,
	Discards = ((SInDiscards - InDiscards) + (SOutDiscards - OutDiscards)) / 2,
	TInDiscards = (SInDiscards - InDiscards) /2,
	TOutDiscards = (SOutDiscards - OutDiscards) /2,
	Errors = ((SInErrors - InErrors) + (SOutErrors - OutErrors)) / 2,
	TInErrors = (SInErrors - InErrors) /2,
	TOutErrors = (SOutErrors - OutErrors) /2,
%%  	io:format("Speed:~p~n",[Speed]),
	BandwidthRate = 
		case Speed of
			[] -> 0.00;
			"0" -> 0.00;
			_ -> 
				if
					Octets < 0 -> 0.0;
					true -> Octets / erlang:list_to_integer(Speed)
				end
		end,
%%  	io:format("BandwidthRate:~p~n",[BandwidthRate]),
	
	[[{index,Index},{adminStatus,AdminStatus},{operStatus,OperStatus},
	  {octets,[F(Octets),F(TInOctets),F(TOutOctets)]},
	  {ucastPkts,[F(UcastPkts),F(TInUcastPkts),F(TOutUcastPkts)]},
	  {nUcastPkts,[F(NUcastPkts),F(TInNUcastPkts),F(TOutNUcastPkts)]},
	  {discards,[F(Discards),F(TInDiscards),F(TOutDiscards)]},
	  {errors,[F(Errors),F(TInErrors),F(TOutErrors)]},
	  {bandwidthRate,nnm_discovery_util:floatToString(BandwidthRate,4)}]|computeFlow(Result,SResult)].

readCpuTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[cpuDutyUsed,cpuDutyUnUsed]),
%% 	io:format("SpecialOidValueList:~p~n",[SpecialOidValueList]),
	CpuDutyUsed = 
		case proplists:get_value(cpuDutyUsed,SpecialOidValueList,"") of
			"" ->
				case proplists:get_value(cpuDutyUnUsed,SpecialOidValueList,"") of
					"" ->
						[];
					Oid ->
						Value = getTreeValue(Oid, ""),
						100 - Value
				end;
			Oid ->
				getTreeValue(Oid, "")
		end,
%% 	io:format("CpuDutyUsed:~p~n",[CpuDutyUsed]),				
	Result = [nnm_discovery_util:integerToString(Value) || {_,Value} <- CpuDutyUsed],
	{proplists:get_value(ip,SnmpParam),Result}.
	

readMemoryTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[memorySize,memoryFree,memoryFreeRate,memoryUsed,memoryUsedRate]),
	MemorySizeOid = proplists:get_value(memorySize,SpecialOidValueList,""),
	MemoryFreeOid = proplists:get_value(memoryFree,SpecialOidValueList,""),
	MemoryFreeRateOid = proplists:get_value(memoryFreeRate,SpecialOidValueList,""),
	MemoryUsedOid = proplists:get_value(memoryUsed,SpecialOidValueList,""),
	MemoryUsedRateOid = proplists:get_value(memoryUsedRate,SpecialOidValueList,""),
	
	F = fun(X) ->
				case X of
					"" ->
						[];
					_ ->
						getTreeValue(X,"")
				end
		end,
	MemorySize = F(MemorySizeOid),
	MemoryFree = F(MemoryFreeOid),
	MemoryFreeRate = F(MemoryFreeRateOid),
	MemoryUsed = F(MemoryUsedOid),
	MemoryUsedRate = F(MemoryUsedRateOid),
	
%	io:format("Result:~p~p~p~p~p~n",[MemorySize,MemoryFree,MemoryFreeRate,MemoryUsed,MemoryUsedRate]),
	Result = computeMemory(MemorySize,MemoryFree,MemoryFreeRate,MemoryUsed,MemoryUsedRate),
%	io:format("Result:~p~n",[Result]),
	
	{proplists:get_value(ip,SnmpParam),Result}.


computeMemory(MemorySize,MemoryFree,MemoryFreeRate,MemoryUsed,MemoryUsedRate) ->
 %%	io:format("MemorySize:~p,MemoryFree:~p,MemoryFreeRate:~p,MemoryUsed:~p,MemoryUsedRate:~p~n",[MemorySize,MemoryFree,MemoryFreeRate,MemoryUsed,MemoryUsedRate]),
	F1 = fun(M1,M2) -> lists:foreach(fun(X) -> 
							  				{K,V} = X,
							  				if 
								  				V =:= 0 ->
									  				{K,V};
								  				true ->
									  				{K, (proplists:get_value(K,M1) * 100) / V}
							  				end
					  				end, M2)
		end,
	
	Size = 
		if
			MemorySize =/= [] ->
				MemorySize;
			MemoryFree =/= [] andalso MemoryFreeRate =/= [] ->
				if
					length(MemoryFree) =:= length(MemoryFreeRate) ->
						F1(MemoryFree,MemoryFreeRate);
					true ->
						[]
				end;
			MemoryUsed =/= [] andalso MemoryUsedRate =/= [] ->
				if
					length(MemoryUsed) =:= length(MemoryUsedRate) ->
						F1(MemoryUsed,MemoryUsedRate);
					true ->
						[]
				end;
			MemoryFree =/= [] andalso MemoryUsed =/= [] ->
				if
					length(MemoryFree) =:= length(MemoryUsed) ->
						[{K,V + proplists:get_value(K,MemoryUsed)} || {K,V} <- MemoryFree];
					true ->
						[]
				end;
			true ->
				[]
		end,
	UsedRate = 
		if
			MemoryUsedRate =/= [] ->
				MemoryUsedRate;
			MemorySize =/= [] andalso MemoryUsed =/= [] ->
				if
					length(MemorySize) =:= length(MemoryUsed) ->
						[{K,proplists:get_value(K,MemoryUsed) / V} || {K,V} <- MemorySize];
					true ->
						[]
				end;
			MemoryFreeRate =/= [] ->
				[{K,(100 - V) / 100} || {K,V} <- MemoryFreeRate];
			MemorySize =/= [] andalso MemoryFree =/= [] ->
				if
					length(MemorySize) =:= length(MemoryFree) ->
						[{K,(V - proplists:get_value(K,MemoryFree)) * 100 / V} || {K,V} <- MemorySize];
					true ->
						[]
				end;
			MemoryFree =/= [] andalso MemoryUsed =/= [] ->
				if
					length(MemoryFree) =:= length(MemoryUsed) ->
						[{K,V / (V + proplists:get_value(K,MemoryFree))} || {K,V} <- MemoryUsed];
					true ->
						[]
				end;
			true ->
				[]
		end,
	[{nnm_discovery_util:floatToString(V,0),nnm_discovery_util:floatToString(proplists:get_value(K,UsedRate,0.0),4)} || {K,V} <- Size].
	

readOspfTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ospfNbrAddressLessIndex,ospfNbrIpAddr]),
	OspNbrAddressLessIndex = getTreeValue(?OSPFNBRADDRESSLESSINDEX, proplists:get_value(ospfNbrAddressLessIndex, SpecialOidValueList, "")),
	OspfNbrIpAddr = getTreeValue(?OSPFNBRIPADDR, proplists:get_value(ospfNbrIpAddr, SpecialOidValueList, "")),
	
	Result = [{nnm_discovery_util:toString(Index),
			   nnm_discovery_util:integerListToString(IpAddr,10,".")} || {K1,IpAddr} <- OspfNbrIpAddr, {K2,Index} <- OspNbrAddressLessIndex, (K1 =:= K2)],
	{proplists:get_value(ip,SnmpParam),Result}.

readOspf(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ospfNbrAddressLessIndex,ospfNbrIpAddr]),
	OspNbrAddressLessIndex = getTreeValue(?OSPFNBRADDRESSLESSINDEX, proplists:get_value(ospfNbrAddressLessIndex, SpecialOidValueList, "")),
	OspfNbrIpAddr = getTreeValue(?OSPFNBRIPADDR, proplists:get_value(ospfNbrIpAddr, SpecialOidValueList, "")),
	
	Result = [[{index,nnm_discovery_util:toString(Index)},
			   {ip,nnm_discovery_util:integerListToString(IpAddr,10,".")}] || {K1,IpAddr} <- OspfNbrIpAddr, {K2,Index} <- OspNbrAddressLessIndex, (K1 =:= K2)],
	{proplists:get_value(ip,SnmpParam),Result}.

readBgpTable(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[bgpPeerLocalAddr,bgpPeerLocalPort,bgpPeerRemoteAddr,bgpPeerRemotePort]),
	BgpPeerLocalAddr = getTreeValue(?BGPPEERLOCALADDR, proplists:get_value(bgpPeerLocalAddr, SpecialOidValueList, "")),
	BgpPeerLocalPort = getTreeValue(?BGPPEERLOCALPORT, proplists:get_value(bgpPeerLocalPort, SpecialOidValueList, "")),
	BgpPeerRemoteAddr = getTreeValue(?BGPPEERREMOTEADDR, proplists:get_value(bgpPeerRemoteAddr, SpecialOidValueList, "")),
	BgpPeerRemotePort = getTreeValue(?BGPPEERREMOTEPORT, proplists:get_value(bgpPeerRemotePort, SpecialOidValueList, "")),
	
	Result = [{nnm_discovery_util:integerListToString(LocalAddr,10,"."),
			   nnm_discovery_util:toString(LocalPort),
			   nnm_discovery_util:integerListToString(RemoteAddr,10,"."),
			   nnm_discovery_util:toString(RemotePort)} || 
			   {K1,LocalAddr} <- BgpPeerLocalAddr, 
			   {K2,LocalPort} <- BgpPeerLocalPort, 
			   {K3,RemoteAddr} <- BgpPeerRemoteAddr, 
			   {K4,RemotePort} <- BgpPeerRemotePort,
			   (K1 =:= K2 andalso K1 =:= K3 andalso K1 =:= K4)],
	{proplists:get_value(ip,SnmpParam),Result}.

readBgp(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[bgpPeerLocalAddr,bgpPeerLocalPort,bgpPeerRemoteAddr,bgpPeerRemotePort]),
	BgpPeerLocalAddr = getTreeValue(?BGPPEERLOCALADDR, proplists:get_value(bgpPeerLocalAddr, SpecialOidValueList, "")),
	BgpPeerLocalPort = getTreeValue(?BGPPEERLOCALPORT, proplists:get_value(bgpPeerLocalPort, SpecialOidValueList, "")),
	BgpPeerRemoteAddr = getTreeValue(?BGPPEERREMOTEADDR, proplists:get_value(bgpPeerRemoteAddr, SpecialOidValueList, "")),
	BgpPeerRemotePort = getTreeValue(?BGPPEERREMOTEPORT, proplists:get_value(bgpPeerRemotePort, SpecialOidValueList, "")),
	
	Result = [[{localAddr,nnm_discovery_util:integerListToString(LocalAddr,10,".")},
			   {localPort,nnm_discovery_util:toString(LocalPort)},
			   {remoteAddr,nnm_discovery_util:integerListToString(RemoteAddr,10,".")},
			   {remotePort,nnm_discovery_util:toString(RemotePort)}] || 
			   {K1,LocalAddr} <- BgpPeerLocalAddr, 
			   {K2,LocalPort} <- BgpPeerLocalPort, 
			   {K3,RemoteAddr} <- BgpPeerRemoteAddr, 
			   {K4,RemotePort} <- BgpPeerRemotePort,
			   (K1 =:= K2 andalso K1 =:= K3 andalso K1 =:= K4)],
	{proplists:get_value(ip,SnmpParam),Result}.

readDirect(_SysobjectId) ->
	{proplists:get_value(ip,SnmpParam),[]}.

readDirectTable(_SysobjectId) ->
	{proplists:get_value(ip,SnmpParam),[]}.
				
				
%% read port data
%% ifSpeed,ifAdminStatus,ifOperStatus,
%% ifInOctets,ifInUcastPkts,ifInNUcastPkts,ifInDiscards,ifInErrors,
%% ifOutOctets,ifOutUcastPkts,ifOutNUcastPkts,ifOutDiscards,ifOutErrors
readPort(Indexs,Target,SOid,DefaultValue,Type) ->
%% 	io:format("param:~p~n",[[Indexs,Target,SOid,DefaultValue,Type]]),
	Oid =
		case Target of
			ifSpeed -> ?IFSPEED;
			ifAdminStatus -> ?IFADMINSTATUS;
			ifOperStatus -> ?IFOPERSTATUS;
			ifInOctets -> ?IFHCINOCTETS;
			ifInUcastPkts -> ?IFINUCASTPKTS;
			ifInNUcastPkts -> ?IFINNUCASTPKTS;
			ifInDiscards -> ?IFINDISCARDS;
			ifInErrors -> ?IFINERRORS;
			ifOutOctets -> ?IFHCOUTOCTETS;
			ifOutUcastPkts -> ?IFOUTUCASTPKTS;
			ifOutNUcastPkts -> ?IFOUTNUCASTPKTS;
			ifOutDiscards -> ?IFOUTDISCARDS;
			ifOutErrors -> ?IFOUTERRORS;
			_ -> []
		end,
	
	Result = 
		lists:map(fun(X) ->
						  Ioid = case X of
									 "" -> [];
									 _ -> [erlang:list_to_integer(X)]
								 end,
						  O1 = Oid ++ Ioid,
						  O2 = case SOid of "" -> []; _ -> SOid ++ Ioid end,
%% 						  io:format("O1:~p~n,O2:~p~n",[O1,O2]),
						  {T1,T2,_} = now(),
						  Time = T1 * 1000000 + T2,
						  Value = 
							  case getValue(O1,O2) of
								  "" -> DefaultValue;
								  R -> Type(R)
							  end,
						  {X,{Target,{Value,Time}}}
				  end, Indexs),
	Result.
		
readPortFlow(SysobjectId,Indexs) ->
	SpecialOidValueList = 
		getSpecialOid(SysobjectId,[ifSpeed,ifAdminStatus,ifOperStatus,
									ifInOctets,ifInUcastPkts,ifInNUcastPkts,ifInDiscards,ifInErrors,
									ifOutOctets,ifOutUcastPkts,ifOutNUcastPkts,ifOutDiscards,ifOutErrors]),
	F1 = fun(X) -> nnm_discovery_util:integerToString(X) end,
	F2 = fun(X) -> X end,
	
	IfSpeed = readPort(Indexs,ifSpeed,proplists:get_value(ifSpeed, SpecialOidValueList, ""),0,F2),
%% 	io:format("ifspeed:~p~n",[IfSpeed]),
	IfAdminStatus = readPort(Indexs,ifAdminStatus,proplists:get_value(ifAdminStatus, SpecialOidValueList, ""),"",F1),
	IfOperStatus = readPort(Indexs,ifOperStatus,proplists:get_value(ifOperStatus, SpecialOidValueList, ""),"",F1),
	IfInOctets = readPort(Indexs,ifInOctets,proplists:get_value(ifInOctets, SpecialOidValueList, ""),0,F2),
	IfInUcastPkts = readPort(Indexs,ifInUcastPkts,proplists:get_value(ifInUcastPkts, SpecialOidValueList, ""),0,F2),
	IfInNUcastPkts = readPort(Indexs,ifInNUcastPkts,proplists:get_value(ifInNUcastPkts, SpecialOidValueList, ""),0,F2),
	IfInDiscards = readPort(Indexs,ifInDiscards,proplists:get_value(ifInDiscards, SpecialOidValueList, ""),0,F2),
	IfInErrors = readPort(Indexs,ifInErrors,proplists:get_value(ifInErrors, SpecialOidValueList, ""),0,F2),
	IfOutOctets = readPort(Indexs,ifOutOctets,proplists:get_value(ifOutOctets, SpecialOidValueList, ""),0,F2),
	IfOutUcastPkts = readPort(Indexs,ifOutUcastPkts,proplists:get_value(ifOutUcastPkts, SpecialOidValueList, ""),0,F2),
	IfOutNUcastPkts = readPort(Indexs,ifOutNUcastPkts,proplists:get_value(ifOutNUcastPkts, SpecialOidValueList, ""),0,F2),
	IfOutDiscards = readPort(Indexs,ifOutDiscards,proplists:get_value(ifOutDiscards, SpecialOidValueList, ""),0,F2),
	IfOutErrors = readPort(Indexs,ifOutErrors,proplists:get_value(ifOutErrors, SpecialOidValueList, ""),0,F2),
	
%% 	io:format("hah:~p~n",[[IfSpeed,IfAdminStatus,IfOperStatus,IfInOctets,IfInUcastPkts,IfInNUcastPkts,IfInDiscards,IfInErrors,IfOutOctets,
%%					  IfOutUcastPkts,IfOutNUcastPkts,IfOutDiscards,IfOutErrors]]),
	L = lists:append([IfSpeed,IfAdminStatus,IfOperStatus,IfInOctets,IfInUcastPkts,IfInNUcastPkts,IfInDiscards,IfInErrors,IfOutOctets,
					  IfOutUcastPkts,IfOutNUcastPkts,IfOutDiscards,IfOutErrors]),
	
	Result = [[{ifIndex,X}|proplists:get_all_values(X, L)] || X <- Indexs],
	
	{proplists:get_value(ip,SnmpParam),Result}.

%%----------add for Gpon Edge---------------------------------------
%% binary 	
%%  AA = <<4194394880:32>>.
%% <<_:7,D:6,E:6,F:5,_:8>> = AA.
%% D = 0
%% E = 11
%% F = 3

connectGponIndexDescrIpSn([],_,_,_) -> [];
connectGponIndexDescrIpSn([H|IfIndex],IfDescr,[Hi|IpAddr],[Ho|OntID]) ->
	{Key,Index} = H,
	Descr = proplists:get_value(Key, IfDescr, ""),
	{_,Ip} = Hi,
	{_,Sn} = Ho,
	
	
	[[{ifIndex,nnm_discovery_util:integerToString(ifIndexSmallThanZero(Index))},
	  {ifDescr,Descr},
	  {ifIp,Ip},
	  {ifSn,Sn}]| connectGponIndexDescrIpSn(IfIndex,IfDescr,IpAddr,OntID)].

connectIndexDescr([],_) -> [];
connectIndexDescr([H|IfIndex],IfDescr) ->
	{Key,Index} = H,
	Descr = proplists:get_value(Key, IfDescr, ""),
	
	[[{ifIndex,nnm_discovery_util:integerToString(ifIndexSmallThanZero(Index))},
	  {ifDescr,Descr}]| connectIndexDescr(IfIndex,IfDescr)].
	  
%~ connectGponIpSn([],_) -> [];
%~ connectGponIpSn([Hi|IpAddr],[Ho|OntID]) ->
	%~ {_,Ip} = Hi,
	%~ {_,Sn} = Ho,

	%~ [[{ifIp,Ip},{ifSn,Sn}]| connectGponIpSn(IpAddr,OntID)].

writeLog(Log) ->
	case file:open("gponInfo.txt",[append]) of
		{ok,File} -> io:format(File,"~n~p~n~n",[Log]),
		file:close(File);
		_ -> ok
	end.

readGponOlt(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ifIndex,ifDescr,ipAddr,ontID]),
	IfIndex = getTreeValue(?IFINDEX, proplists:get_value(ifIndex, SpecialOidValueList, "")),
	IfDescr = getTreeValue(?IFDESCR, proplists:get_value(ifDescr, SpecialOidValueList, "")),
% test [10]
	IpAddr = getTreeValue([10],proplists:get_value(ipAddr, SpecialOidValueList, "")),
	%~ OntID = getTreeValue([10],proplists:get_value(ontID, SpecialOidValueList, "")),
	
 	%io:format("=======================~n IfIndex:~p ~n IpAddr: ~p~n",[IfIndex,IpAddr]),
		
  	%~ Result = connectGponIndexDescrIpSn(IfIndex,IfDescr,IpAddr,OntID),

	IfIndexIfDescr = connectIndexDescr(IfIndex,IfDescr),
	Result = ip_for_index(IfIndexIfDescr,IpAddr),
		
 	Re = {proplists:get_value(ip,SnmpParam),Result},
	%io:format("======~n ResultOlt:~p ~n======",[Re]),
	Re.


ip_for_index(IfIndex,IpAddr) ->
	IIFun = fun([],_,ACC) -> ACC;
		([I|Is],Self,ACC) ->
			case I of
				{[Index,_],IP} ->
					IDFun = fun([],_,IDD) -> IDD;
						([D|Ds],IDF,IDD) ->
							TS = integer_to_list(Index),
							case D of
								[{ifIndex,TS},_] ->
								     IDF(Ds,IDF,[D ++ [{ifIp,IP}]] ++ IDD);
								_ -> IDF(Ds,IDF,IDD)
							end
					end,
					Self(Is,Self, IDFun(IfIndex,IDFun,[]) ++ ACC);
				_ -> Self(Is,Self,ACC)
			end
	end,
	IIFun(IpAddr,IIFun,[]).			
	

connectIndexDescrTypeOperstatus([],_,_,_) -> [];
connectIndexDescrTypeOperstatus([H|IfIndex],IfDescr,IfType,IfOperStatus) ->
	{Key,Index} = H,
	Descr = proplists:get_value(Key, IfDescr, ""),
	Type = proplists:get_value(Key, IfType, ""),
	OperStatus = proplists:get_value(Key, IfOperStatus, ""),
	
	%~ IndexFinaly = ifIndexSmallThanZero(Index),

	[[{ifIndex,nnm_discovery_util:integerToString(ifIndexSmallThanZero(Index))},
	  {ifDescr,Descr},
	  {ifType,nnm_discovery_util:integerToString(Type)},
	  {ifOperStatus,nnm_discovery_util:integerToString(OperStatus)}] | connectIndexDescrTypeOperstatus(IfIndex,IfDescr,IfType,IfOperStatus)].

readGponOnu(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[ifIndex,ifDescr,ifType,ifOperStatus]),
	IfIndex = getTreeValue(?IFINDEX, proplists:get_value(ifIndex, SpecialOidValueList, "")),
	IfDescr = getTreeValue(?IFDESCR, proplists:get_value(ifDescr, SpecialOidValueList, "")),
	IfType = getTreeValue(?IFTYPE, proplists:get_value(ifType, SpecialOidValueList, "")),
	IfOperStatus = getTreeValue(?IFOPERSTATUS,proplists:get_value(ifOperStatus, SpecialOidValueList, "")),

	Result = connectIndexDescrTypeOperstatus(IfIndex,IfDescr,IfType,IfOperStatus),
	
	Re = {proplists:get_value(ip,SnmpParam),Result},
	%~ io:format("======~n readGponOnu:~p ~n======",[Re]),
	analyseOnu(Re).

			
%%获取onu上的pon口 只有一个pon口
analyseOnu(ResultOnu)->	 %get Pon port  add in nnm_discovery_snmp_read:readGponOnu

	{IP,Data} = ResultOnu,
	IFFun = fun([],_) -> [];
		   ([Item|Items],Self) ->
			case Item of
			    [{ifIndex,IfIndex},{ifDescr,IfDescr},{ifType,IfType},{ifOperStatus,IfOperStatus}] -> IfType,
			        case IfType of
			           "117" ->   io:format("IfType:~p~n", [IfType]),  %%onu Pon port IFtype 117
					    [[{ifIndex,IfIndex},{ifDescr,IfDescr},{ifType,IfType},{ifOperStatus,IfOperStatus}]];
				       _->  Self(Items,Self)
				end;
			    _ -> Self(Items,Self)
			end
		end,
		
	Descr = IFFun(Data,IFFun),	
	io:format("Descr:~p ~n", [Descr]),
	{IP, Descr}.

	%~ case Descr of
	   %~ [] -> ResultOnu;
	   %~ _  ->
		%~ [[{ifIndex,Ifndex},{ifDescr,IfDescr},{ifType,IfType},{ifOperStatus,IfOperStatus}]] = Descr,

		%~ IfIndexInt =list_to_integer(Ifndex),
		%~ IfIndexFinaly = ifIndexSmallThanZero(IfIndexInt),

		%~ IfInfo = [[{ifIndex,IfIndexFinaly},{ifDescr,IfDescr},{ifType,IfType},{ifOperStatus,IfOperStatus}]],
		%~ Re = {IP, IfInfo},
		%~ io:format("~n analyseOnu:~p ~n",[Re]),
		%~ Re
	%~ end.
	
%% GPON "ifIndex" is big than 2^32, disappear snmp value "ifIndex" is small than 0
ifIndexSmallThanZero(Number) ->
	case Number<0  of
	       false  -> 
		   Number;
	       true -> 
	           X = 4294967296 + Number,
	           X
	end.
			

gponConnectIndexDescrTypeSpeedMacAliasPort([],_,_,_,_,_,_) -> [];
gponConnectIndexDescrTypeSpeedMacAliasPort([H|IfIndex],IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort) ->
	{Key,Index} = H,
	Descr = proplists:get_value(Key, IfDescr, ""),
	Type = proplists:get_value(Key, IfType, ""),
	Speed = proplists:get_value(Key, IfSpeed, ""),
	Mac = proplists:get_value(Key, IfMac, ""),
	Alias = proplists:get_value(Key, IfAlias, ""),
	Port =
		case lists:filter(fun(X) -> {_,V}=X,V=:=Index end, IfPort) of
			[] -> Index;
			[{[P],_}] -> P
		end,
		
	 FinalyIndex = ifIndexSmallThanZero(Index),
	 FinalyPort = ifIndexSmallThanZero(Port),

	 % {ifDescr, indexConvertDescr(FinalyIndex)},
	[[{ifIndex,nnm_discovery_util:integerToString(Index)},
	  {ifType,nnm_discovery_util:integerToString(Type)},
	  {ifDescr, Descr},
	  {ifMac,nnm_discovery_util:toMac(Mac)},
	  {ifSpeed,nnm_discovery_util:integerToString(Speed)},
	  {ifPort,nnm_discovery_util:integerToString(Port)},
	  {ifAlias,Alias}]|
		 gponConnectIndexDescrTypeSpeedMacAliasPort(IfIndex,IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort)].



indexConvertDescr(Ifindex) ->
	Temp = <<Ifindex:32>>,
	%Huawei-MA5600-V800R305-ETHERNET 0/0/1(板卡/模块/接口 )
	<<_:7,I_board:6,I_model:6,I_interface:5,_:8>> = Temp,
	S_board = integer_to_list(I_board),
	S_model = integer_to_list(I_model),
	S_interface = integer_to_list(I_interface),
   
	io:format("D:~p,E:~p,F:~p ~n",[I_board,I_model,I_interface]),
	Temp1 = lists:append(S_board,"/"),
	Temp2 = lists:append(Temp1,S_model),
	Temp3 = lists:append(Temp2,"/"),
	Temp4 = lists:append(Temp3,S_interface),

	io:format("Temp4:~p~n",[Temp4]),
	Temp4.	

















