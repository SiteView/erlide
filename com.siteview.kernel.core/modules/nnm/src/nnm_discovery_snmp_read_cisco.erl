-module(nnm_discovery_snmp_read_cisco,[BASE,SnmpParam,SpecialOidList,LogicalCommunity]).
-extends(nnm_discovery_snmp_read).
-compile(export_all).

-include("nnm_define.hrl").

new(SnmpParam,SysobjectIdList,SpecialOidList,LogicalCommunity) ->
	B = ?BASE_MODULE:new(SnmpParam,SysobjectIdList,SpecialOidList),
	{?MODULE,B,SnmpParam,SysobjectIdList,SpecialOidList,LogicalCommunity}.

new(SnmpParam,SpecialOidList,LogicalCommunity) ->
	B = nnm_discovery_snmp_read:new(SnmpParam,SpecialOidList),
	{?MODULE,B,SnmpParam,SpecialOidList,LogicalCommunity}.

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

getTreeValue(O1,O2,Param) ->
	case nnm_snmp_api:readTreeValue(Param,O1) of
		[] ->
			case O2 of
				"" -> [];
				_ -> nnm_snmp_api:readTreeValue(Param,O2)
			end;
		V ->
			V
	end.

getSpecialOid(SysobjectId,OidNameList) ->
%% 	io:format("SysobjectId:~p~nSpecialOidList~p~n",[SysobjectId,SpecialOidList]),
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

readInterface(SysobjectId) ->
	io:format("SysobjectId:~p~nSnmpParam:~p~n",[SysobjectId,SnmpParam]),
	SpecialOidValueList = getSpecialOid(SysobjectId,[ifNumber,ifIndex,ifType,ifDescr,ifMac,ifSpeed,ifAlia,ifPort]),
	SnmpParamList = [lists:keyreplace(getCommunity, 1, SnmpParam, {getCommunity,X}) || X <- LogicalCommunity],
	IfNumber = getValue(?IFNUMBER, proplists:get_value(ifNumber, SpecialOidValueList, "")),
	IfIndex = getTreeValue(?IFINDEX, proplists:get_value(ifIndex, SpecialOidValueList, "")),
	IfDescr = getTreeValue(?IFDESCR, proplists:get_value(ifDescr, SpecialOidValueList, "")),
	IfType = getTreeValue(?IFTYPE, proplists:get_value(ifType, SpecialOidValueList, "")),
	IfSpeed = getTreeValue(?IFSPEED,proplists:get_value(ifMac, SpecialOidValueList, "")),
	IfMac = getTreeValue(?IFMAC,proplists:get_value(ifSpeed, SpecialOidValueList, "")),
	IfAlias = getTreeValue(?IFALIAS,proplists:get_value(ifAlias, SpecialOidValueList, "")),
	IfPort = lists:append([getTreeValue(?IFPORT,proplists:get_value(ifPort, SpecialOidValueList, ""),X) || X <- SnmpParamList]),
	
	IndexDescrTypeSpeedMacAliasPort = 
		connectIndexDescrTypeSpeedMacAliasPort(IfIndex,IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort),
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
			[] -> "";
			[{[P],_}|_] -> P
		end,
		
	[[{ifIndex,nnm_discovery_util:integerToString(Index)},
	  {ifType,nnm_discovery_util:integerToString(Type)},
	  {ifDescr,Descr},
	  {ifMac,nnm_discovery_util:toMac(Mac)},
	  {ifSpeed,nnm_discovery_util:integerToString(Speed)},
	  {ifPort,nnm_discovery_util:integerToString(Port)},
	  {ifAlias,Alias}]|
		 connectIndexDescrTypeSpeedMacAliasPort(IfIndex,IfDescr,IfType,IfSpeed,IfMac,IfAlias,IfPort)].

readAft(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[dtpFdbPort,qtpFdbPort]),
	io:format("cisco readAft SpecialOidValueList: ~p~n" ,[SpecialOidValueList]),
	SnmpParamList = [lists:keyreplace(getCommunity, 1, SnmpParam, {getCommunity,X}) || X <- LogicalCommunity],
	DtpFdbPort = 
		lists:append([getTreeValue(?DTPFDBPORT, proplists:get_value(dtpFdbPort, SpecialOidValueList, ""), X) || X <- SnmpParamList]),
	QtpFdbPort = 
		lists:append([getTreeValue(?QTPFDBPORT, proplists:get_value(qtpFdbPort, SpecialOidValueList, ""), X) || X <- SnmpParamList]),
	io:format("readAft DtpFdbPort: ~p~n" ,[DtpFdbPort]),
	io:format("readAft QtpFdbPort: ~p~n" ,[QtpFdbPort]),
	
	FDtpFdbPort = [{nnm_discovery_util:integerToString(Value),nnm_discovery_util:toMac(lists:sublist(Key, length(Key)-5, 6))} || 
				{Key,Value} <- DtpFdbPort],
	FQtpFdbPort = [{nnm_discovery_util:integerToString(Value),nnm_discovery_util:toMac(lists:sublist(Key, length(Key)-5, 6))} || 
				{Key,Value} <- QtpFdbPort],
	DtpQtp = nnm_discovery_util:mergerList(FDtpFdbPort,FQtpFdbPort),
	{proplists:get_value(ip,SnmpParam,""),[[{port,Port},{mac,Mac}] || {Port,Mac} <- DtpQtp]}.

readAftTable(SysobjectId) ->
	io:format("Cisco"),
	SpecialOidValueList = getSpecialOid(SysobjectId,[dtpFdbAddress,dtpFdbPort,dtpFdbStatus,qtpFdbAddress,qtpFdbPort,qtpFdbStatus]),
	SnmpParamList = [lists:keyreplace(getCommunity, 1, SnmpParam, {getCommunity,X}) || X <- LogicalCommunity],
	Result = readAftTable(SnmpParamList,SpecialOidValueList,[]),
	
	{proplists:get_value(ip,SnmpParam),Result}.

readAftTable([],_,Result) -> Result;
readAftTable([H|SnmpParamList],SpecialOidValueList,Result) ->
	DtpFdbAddress = getTreeValue(?DTPFDBADDRESS, proplists:get_value(dtpFdbAddress, SpecialOidValueList, ""),H),
	DtpFdbPort = getTreeValue(?DTPFDBPORT, proplists:get_value(dtpFdbPort, SpecialOidValueList, ""),H),
	DtpFdbStatus = getTreeValue(?DTPFDBSTATUS, proplists:get_value(dtpFdbStatus, SpecialOidValueList, ""),H),
	QtpFdbAddress = getTreeValue(?QTPFDBADDRESS, proplists:get_value(qtpFdbAddress, SpecialOidValueList, ""),H),
	QtpFdbPort = getTreeValue(?QTPFDBPORT, proplists:get_value(qtpFdbPort, SpecialOidValueList, ""),H),
	QtpFdbStatus = getTreeValue(?QTPFDBSTATUS, proplists:get_value(qtpFdbStatus, SpecialOidValueList, ""),H),
	
	DtpFdb = connectAftTable(DtpFdbPort,DtpFdbAddress,DtpFdbStatus),
	QtpFdb = connectAftTable(QtpFdbPort,QtpFdbAddress,QtpFdbStatus),
%% 	io:format("DtpFdb:~p~n,QtpFdb:~p~n",[DtpFdb,QtpFdb]),
	
	readAftTable(SnmpParamList,SpecialOidValueList,nnm_discovery_util:mergerList(Result,lists:append(DtpFdb,QtpFdb))).

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
	SnmpParamList = [lists:keyreplace(getCommunity, 1, SnmpParam, {getCommunity,X}) || X <- LogicalCommunity],
	ArpMac = 
		lists:append([getTreeValue(?ARPMAC, proplists:get_value(arpMac, SpecialOidValueList, ""), X) || X <- SnmpParamList]),
	FArpMac = fArpMac(ArpMac,[]),
	{proplists:get_value(ip,SnmpParam,""),FArpMac}.

fArpMac([],Result) -> Result;
fArpMac([H|ArpMac],Result) ->
	{Key,Mac} = H,
	{[IfIndex],Ip} = lists:split(1, Key),
	V = [{ifIndex,nnm_discovery_util:integerToString(IfIndex)},
	  	 {ip,nnm_discovery_util:integerListToString(Ip,10,".")},
	  	 {mac,nnm_discovery_util:toMac(Mac)}],
	case lists:member(V, Result) of
		true ->
			fArpMac(ArpMac,Result);
		false ->
			fArpMac(ArpMac,[V|Result])
	end.
	
readArpTable(SysobjectId) ->
%% 	io:format("Cisco"),
	SpecialOidValueList = getSpecialOid(SysobjectId,[arpIfIndex,arpMac,arpIp,arpType]),
	SnmpParamList = [lists:keyreplace(getCommunity, 1, SnmpParam, {getCommunity,X}) || X <- LogicalCommunity],
	Result = readArpTable(SnmpParamList,SpecialOidValueList,[]),
	{proplists:get_value(ip,SnmpParam),Result}.

readArpTable([],_,Result) -> Result;
readArpTable([H|SnmpParamList],SpecialOidValueList,Result) ->
	ArpIfIndex = getTreeValue(?ARPIFINDEX, proplists:get_value(arpIfIndex, SpecialOidValueList, ""),H),
	ArpMac = getTreeValue(?ARPMAC, proplists:get_value(arpMac, SpecialOidValueList, ""),H),
	ArpIp = getTreeValue(?ARPIP, proplists:get_value(arpIp, SpecialOidValueList, ""),H),
	ArpType = getTreeValue(?ARPTYPE, proplists:get_value(arpType, SpecialOidValueList, ""),H),
	Arp = connectArpTable(ArpIfIndex,ArpMac,ArpIp,ArpType),
	readArpTable(SnmpParamList,SpecialOidValueList,nnm_discovery_util:mergerList(Result,Arp)).

connectArpTable([],_,_,_) -> [];
connectArpTable([H|ArpIfIndex],ArpMac,ArpIp,ArpType) ->
	{Key,Value} = H,
	IfIndex = nnm_discovery_util:integerToString(Value),
	Mac = nnm_discovery_util:toMac(proplists:get_value(Key, ArpMac)),
	Ip = nnm_discovery_util:integerListToString(proplists:get_value(Key, ArpIp), 10, "."),
	Type = nnm_discovery_util:integerToString(proplists:get_value(Key, ArpType)),
	[{IfIndex,Mac,Ip,Type}|connectArpTable(ArpIfIndex,ArpMac,ArpIp,ArpType)].

readDirectTable(SysobjectId) ->
	SpecialOidValueList = 
		getSpecialOid(SysobjectId,[cdpCacheAddress,cdpCacheVersion,cdpCacheDeviceId,cdpCacheDevicePort,cdpCachePlatform,cdpCacheVTPMgmtDomain,
								   cdpCacheNativeVLAN,cdpCacheDuplex,cdpCacheMTU]),
	CdpCacheAddress = getTreeValue(?CDPCACHEADDRESS, proplists:get_value(cdpCacheAddress, SpecialOidValueList, "")),
	CdpCacheVersion = getTreeValue(?CDPCACHEVERSION, proplists:get_value(cdpCacheVersion, SpecialOidValueList, "")),
	CdpCacheDeviceId = getTreeValue(?CDPCACHEDEVICEID, proplists:get_value(cdpCacheDeviceId, SpecialOidValueList, "")),
	CdpCacheDevicePort = getTreeValue(?CDPCACHEDEVICEPORT, proplists:get_value(cdpCacheDevicePort, SpecialOidValueList, "")),
	CdpCachePlatform = getTreeValue(?CDPCACHEPLATFORM, proplists:get_value(cdpCachePlatform, SpecialOidValueList, "")),
	CdpCacheVTPMgmtDomain = getTreeValue(?CDPCACHEVTPMGMTDOMAIN, proplists:get_value(cdpCacheVTPMgmtDomain, SpecialOidValueList, "")),
	CdpCacheNativeVLAN = getTreeValue(?CDPCACHENATIVEVLAN, proplists:get_value(cdpCacheNativeVLAN, SpecialOidValueList, "")),
	CdpCacheDuplex = getTreeValue(?CDPCACHEDUPLEX, proplists:get_value(cdpCacheDuplex, SpecialOidValueList, "")),
	CdpCacheMTU = getTreeValue(?CDPCACHEMTU, proplists:get_value(cdpCacheMTU, SpecialOidValueList, "")),
	
	CdpTable = connectCdpTable(CdpCacheAddress,CdpCacheVersion,CdpCacheDeviceId,CdpCacheDevicePort,CdpCachePlatform,CdpCacheVTPMgmtDomain,
							   CdpCacheNativeVLAN,CdpCacheDuplex,CdpCacheMTU),
	
	{proplists:get_value(ip,SnmpParam),CdpTable}.

connectCdpTable([],_,_,_,_,_,_,_,_) -> [];
connectCdpTable([H|CdpCacheAddress],CdpCacheVersion,CdpCacheDeviceId,CdpCacheDevicePort,CdpCachePlatform,CdpCacheVTPMgmtDomain,CdpCacheNativeVLAN,
				CdpCacheDuplex,CdpCacheMTU) -> 
	{Key,Value} = H,
	Address = nnm_discovery_util:integerListToString(Value, 10, "."),
	IfIndex = nnm_discovery_util:integerToString(lists:nth(1, Key)),
	Version = proplists:get_value(Key,CdpCacheVersion,""),
	DeviceId = proplists:get_value(Key,CdpCacheDeviceId,""),
	DevicePort = proplists:get_value(Key,CdpCacheDevicePort,""),
	Platform = proplists:get_value(Key,CdpCachePlatform,""),
	VTPMgmtDomain = proplists:get_value(Key,CdpCacheVTPMgmtDomain,""),
	NativeVLAN = nnm_discovery_util:toString(proplists:get_value(Key,CdpCacheNativeVLAN)),
	Duplex = 
		case proplists:get_value(Key,CdpCacheDuplex,"") of
			1 ->
				"unknow(1)";
			2 ->
				"halfduplex(2)";
			3 ->
				"fullduplex(3)";
			_ ->
				""
		end,
	MTU = nnm_discovery_util:toString(proplists:get_value(Key,CdpCacheMTU)),
	
	[{Address,IfIndex,Version,DeviceId,DevicePort,Platform,VTPMgmtDomain,NativeVLAN,Duplex,MTU}
	|connectCdpTable(CdpCacheAddress,CdpCacheVersion,CdpCacheDeviceId,CdpCacheDevicePort,CdpCachePlatform,CdpCacheVTPMgmtDomain,
					 CdpCacheNativeVLAN,CdpCacheDuplex,CdpCacheMTU)].

readDirect(SysobjectId) ->
	SpecialOidValueList = getSpecialOid(SysobjectId,[cdpCacheAddress,cdpCacheDevicePort]),
	CdpCacheAddress = getTreeValue(?CDPCACHEADDRESS, proplists:get_value(cdpCacheAddress, SpecialOidValueList, "")),
	CdpCacheDevicePort = getTreeValue(?CDPCACHEDEVICEPORT, proplists:get_value(cdpCacheDevicePort, SpecialOidValueList, "")),
	
	CdpInfo = connectCdpInfo(CdpCacheAddress,CdpCacheDevicePort),
	{proplists:get_value(ip,SnmpParam,""),CdpInfo}.

connectCdpInfo([],_) -> [];
connectCdpInfo([H|CdpCacheAddress],CdpCacheDevicePort) ->
	{Key,Value} = H,
	Address = nnm_discovery_util:integerListToString(Value, 10, "."),
	IfIndex = nnm_discovery_util:integerToString(lists:nth(1, Key)),
	DevicePort = proplists:get_value(Key,CdpCacheDevicePort,""),
	
	[[{localPortIfIndex,IfIndex},
	  {localPortDescr,""},
	  {peerId,""},
	  {peerIp,Address},
	  {peerPortDescr,DevicePort},
	  {peerPortIndex,""}] |connectCdpInfo(CdpCacheAddress,CdpCacheDevicePort)].














