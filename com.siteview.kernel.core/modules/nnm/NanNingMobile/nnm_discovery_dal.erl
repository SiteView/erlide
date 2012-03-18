-module(nnm_discovery_dal).
-compile(export_all).

-include("nnm_define.hrl").
-include("monitor.hrl").
-include("nnm_discovery_topo.hrl").
-include("monitor.hrl").

-define(ACTIVETIME,15*60).

%% DB operate
readFromDB(TableName,ColList,Where,_Order) when is_atom(Where) ->
	case erlcmdb:find_ci(TableName,Where) of
		[] ->
			[];
		[Data|_] ->
			[{X,proplists:get_value(X,Data,"")} || X <- ColList]
	end;
readFromDB(TableName,ColList,Where,Order) ->
%% 	nnm_discovery_util:writeFile([TableName,ColList,Where,Order],"db.txt",write),
	case erlcmdb:find_ci(TableName,Where,0,9999,Order) of
		[] ->
			[];
		Data ->
			[[{Y,proplists:get_value(Y,X,"")} || Y <- ColList] || X <- Data]
	end.

writeToDB(_,[]) -> [];
writeToDB(TableName,[H|Data]) ->
	case proplists:get_value(id,H) of
		undefined ->
			case erlcmdb:create_ci(TableName,H) of
				{ok,V} ->
					[proplists:get_value(id,V)|writeToDB(TableName,Data)];
				_ ->
					writeToDB(TableName,Data)
			end;
		Id ->
			erlcmdb:update_ci(TableName,Id,H),
			[Id|writeToDB(TableName,Data)]
	end.

deleteInDB(TableName,IdList) ->
	[erlcmdb:delete_ci(TableName,X) || X <- IdList],
	ok.

deleteInDB(TableName) ->
	Data = readFromDB(TableName,[id],[],""),
	IdList = [proplists:get_value(id,X) || X <- Data],
	deleteInDB(TableName,IdList).

%% scan config operate
getScanConfig([]) ->
	case erlcmdb:find_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,[]) of
		[] ->
			[];
		Data ->
			[[{id,proplists:get_value(id,X)},{name,proplists:get_value(name,X,"")}] || X <- Data]
	end;
getScanConfig(Param) ->
	Id = proplists:get_value(id, Param),
	Name = proplists:get_value(name, Param, "default"),
	Data = 
		case Id of
			undefined ->
				erlcmdb:find_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,["name="++Name]);
			_ ->
				erlcmdb:find_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,Id)
		end,
	case Data of
		[] -> 
			[];
		[ScanConfig|_] ->
			SId = proplists:get_value(id, ScanConfig),
			SName = proplists:get_value(name, ScanConfig, ""),
			DefaultSnmpParam = proplists:get_value(defaultSnmpParam, ScanConfig, []),
			DefineSnmpParam = proplists:get_value(defineSnmpParam, ScanConfig, []),
			ScanParam = proplists:get_value(scanParam, ScanConfig ,[]),
	
			[{id,SId},{name,SName},{defaultSnmpParam,DefaultSnmpParam},{defineSnmpParam,DefineSnmpParam},{scanParam,ScanParam}]
	end.

addOrUpdateScanConfig(ScanConfig) ->
%% 	io:format("ScanConfig:~p~n",[ScanConfig]),
	case proplists:get_value(id, ScanConfig) of
		undefined ->
			case erlcmdb:create_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,ScanConfig) of
				{ok,V} ->
					{ok,proplists:get_value(id,V)};
				_ ->
					{error,"fail"}
			end;
		Id ->
			erlcmdb:update_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,Id,ScanConfig),
			{ok,Id}
	end.

deleteScanConfig([]) ->
	Data = erlcmdb:find_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,[]),
	[erlcmdb:delete_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,proplists:get_value(id, X)) || X <- Data],
	ok;
deleteScanConfig(Param) ->
	Id = proplists:get_value(id,Param),
	Name = proplists:get_value(name,Param,""),
	case Id of
		undefined ->
			case Name =:= "" orelse Name =:= "default" of
				true -> 
					{error,"name is empty or name is default"};
				false ->
					Data = erlcmdb:find_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,["name="++Name]),
					erlcmdb:delete_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,proplists:get_value(id,Data)),
					ok
			end;
		_ ->
			erlcmdb:delete_ci(?NNM_DISCOVERY_SCAN_CONFIG_TABLE,Id),
			ok
	end.

%% scan version operate
getScanVersion() ->
	case erlcmdb:find_ci(?NNM_DISCOVERY_SCAN_VERSION_TABLE,[]) of
		[] ->
			[];
		ScanVersion ->
			[[{id,proplists:get_value(id,X)},{info,proplists:get_value(info,X)}] || X <- ScanVersion]
	end.
  
addOrUpdateScanVersion(ScanVersion) ->
	Id = proplists:get_value(id,ScanVersion,""),
	case Id of
		"" ->
			case erlcmdb:create_ci(?NNM_DISCOVERY_SCAN_VERSION_TABLE,ScanVersion) of
				{ok,[H|_]} ->
					{id,SId} = H,
					{ok,SId};
				_ ->
					{error,"fail"}
			end;
		_ ->
			erlcmdb:update_ci(?NNM_DISCOVERY_SCAN_VERSION_TABLE,Id,ScanVersion),
			{ok,Id}
	end.

%% topo chart operate
submitTopoChart(TopoData) ->
%% 	io:format("topoData:~p~n",[TopoData]),
	case proplists:get_value(id, TopoData) of
		undefined ->
			Type = proplists:get_value(type,proplists:get_value(version,TopoData,[])),
			io:format("name:~p~n",[Type]),
			case Type =:= "total" of
				true ->
					Data = erlcmdb:find_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,[]),
					[erlcmdb:delete_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,proplists:get_value(id,X)) || X <- Data];
				false ->
					io:format("sub topo chart")
			end,
			case erlcmdb:create_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,TopoData) of
				{ok,[H|_]} ->
					{id,Id1} = H,
					{ok,Id1};
				_ ->
					{error,"fail"}
			end;
		Id ->
			erlcmdb:update_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,Id,TopoData),
			{ok,Id}
	end.

getTopoChart([]) ->
	case erlcmdb:find_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,[]) of
		[] ->
			[];
		Data ->
			[[{id,proplists:get_value(id,X)},{version,proplists:get_value(version,X)}] || X <- Data]
	end;
getTopoChart(Id) ->
%% 	nnm_discovery_util:writeFile(Id,"Id.txt"),
	case erlcmdb:find_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,Id) of
		[] -> 
			[];
		[Data|_] ->
			[{id,proplists:get_value(id,Data)},
			 {version,proplists:get_value(version,Data)},
			 {edge,proplists:get_value(edge,Data,[])},
			 {topoSet,proplists:get_value(topoSet,Data,[])}]
	end.

deleteTopoChart([]) ->
	Data = erlcmdb:find_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,[]),
	[erlcmdb:delete_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,proplists:get_value(id, X)) || X <- Data],
	ok;
deleteTopoChart(Id) ->
	erlcmdb:delete_ci(?NNM_DISCOVERY_TOPOCHART_TABLE,Id),
	ok.

%% add device to machine
addNetworkDevice(Device) ->
	Name = case proplists:get_value(name,Device) of
			   undefined -> proplists:get_value(sysName,Device,"");
			   N -> N
		   end,
	Host = proplists:get_value(ip,Device,""),
	Type = string:to_upper(proplists:get_value(devTypeName,Device,"")),
	Method = case proplists:get_value(snmpFlag,Device) of
				 "1" -> "Snmp";
				 _ -> case proplists:get_value(telnetFlag,Device) of
						  "1" -> "Telnet";
						  _ -> ""
					  end
			 end,
	
	TelnetFlag = case proplists:get_value(telnetFlag,Device) of
					 undefined -> "0";
					 T -> T
				 end,
	TelnetParam = case proplists:get_value(telnetParam,Device) of
					  undefined -> "1";
					  Tp -> Tp
				  end,
	
	Other = lists:keydelete(devTypeName, 1, lists:keydelete(ip, 1, Device)), 
	
	Data = #machine{
					name = Name,
					host = Host,
					type = Type,
					method = Method,
					status = "Connection Successful",
					os = "unknown OS",
					other = lists:append(Other, [{telnetFlag,TelnetFlag},{telnetParam,TelnetParam}]) 
					},
	%--io:format("===============add Data:~p============~n",[Data]),
	case api_machine:create_machine(Data) of
		{ok,Id} -> Id;
		{error,Reason} -> io:format("======addNetworkDevice error Reason:~n~p",[Reason]),Reason
	end.

%% update device to machine
updateNetworkDevice(Device,Machine) ->
	Other = Machine#machine.other,
	
	Host = case proplists:get_value(ip,Device) of
			   undefined -> Machine#machine.host;
			   D0 -> D0
		   end,
	Name = case proplists:get_value(name,Device) of
			   undefined -> Machine#machine.name;
			   D1 -> D1
		   end,
	Type = case proplists:get_value(devTypeName,Device) of
			   undefined -> Machine#machine.type;
			   D2 -> D2
		   end,
	
	FDevice = lists:keydelete(devTypeName, 1, lists:keydelete(ip, 1, Device)),
%%------yi.duan add 'devType' in dumb's other-------
	FOther = case Type of
				"OTHER" ->
					case lists:keyfind(devType,1,FDevice) of 
						false -> lists:append(nnm_discovery_util:mergerProplists(Other,FDevice), [{devType,"6"}]); 
						_-> nnm_discovery_util:mergerProplists(Other,FDevice)
					end;
				_-> nnm_discovery_util:mergerProplists(Other,FDevice)
			 end,
%%------yi.duan add 'devType' in dumb's other-------

%	FOther = nnm_discovery_util:mergerProplists(Other,FDevice), %%old
	Method = case proplists:get_value(snmpFlag,Device) of
				 "1" -> "Snmp";
				 _ -> case proplists:get_value(telnetFlag,Device) of
						  "1" -> "Telnet";
						  _ -> Machine#machine.method
					  end
			 end,		

	Data = Machine#machine{
						   host = Host,
						   type = Type,
						   name = Name,
						   method = Method,
						   other = FOther
						  },

	%--io:format("===============update Data:~p============~n",[Data]),
	case api_machine:update_machine(Data) of
		{ok,_} -> Machine#machine.id;
		{error,Reason} -> Reason
	end.

%% scan over save device
saveDeviceInfo(DeviceInfo) ->
	Machine = api_machine:getAllMachine(0,0,"",""),
	%io:format("============Machine =~p~n",[Machine]),
	saveDeviceInfo(DeviceInfo,Machine,[]).

saveDeviceInfo([],Machine,Data) -> 
	D = lists:map(fun(X) ->
						  Other = X#machine.other,
						  LX = proplists:get_value(x,Other,""),
						  LY = proplists:get_value(y,Other,""),
						  [{id,X#machine.id},{ip,X#machine.host},{typeName,X#machine.type},{x,LX},{y,LY}]
				  end, Machine),
	Data ++ D;
saveDeviceInfo([H|DeviceInfo],Machine,Data) ->
	IpAddr = proplists:get_value(ipAddr,H,[]),
	Ips = [proplists:get_value(ipAddress, X) || X <- IpAddr] ++ [proplists:get_value(ip,H,"")],
	Info = [{discoverTime,nnm_discovery_util:getLocalTime()},{activeTime,nnm_discovery_util:getLocalTime()}],
	case lists:filter(fun(X) -> lists:member(X#machine.host, Ips) end, Machine) of
		[] ->
			FMachine = Machine,
			Id = addNetworkDevice(lists:append(H, Info));
%% 			addNetworkMonitor(H);
		[M|_] ->
			FMachine = lists:delete(M, Machine),
			Id = updateNetworkDevice(lists:append(H, Info),M)
	end,
	saveDeviceInfo(DeviceInfo,FMachine,[[{id,Id}|H]|Data]).

%% auto add network monitor
addNetworkMonitor(Device) ->
	Ip = proplists:get_value(ip,Device),
	DevType = proplists:get_value(devType,Device),
	if
		(DevType =:= ?NNM_DEVICE_TYPE_ROUTE_SWITCH orelse DevType =:= ?NNM_DEVICE_TYPE_SWITCH orelse DevType =:= ?NNM_DEVICE_TYPE_ROUTE orelse DevType =:= ?NNM_DEVICE_TYPE_FIREWALL) ->
			case api_siteview:get_nodes() of
				[] ->
					[];
				[H|_] ->
					{ServerId, ServerName} = H,
					Childs = api_siteview:get_childs(ServerId),
					NNMChilds = lists:filter(fun(X) -> 
													 proplists:get_value(name,X) =:= "NetDevice"
											 end, Childs),
					NNMId = 
						case NNMChilds of
							[] ->
								GroupData = [{parent,'0'},{name,"NetDevice"},{class,group},{frequency,0},{depends_on,"none"},
											 {depends_condition,"good"},{category,good},{last,[]},{state_string,"0 in group,0 in error"}],
								case api_group:create(ServerId, GroupData) of
									{ok,R1} ->
										proplists:get_value(id,R1);
									_ ->
										[]
								end;
							[NH|_] ->
								proplists:get_value(id,NH)
						end,
					DeviceGroupData = [{parent,NNMId},{name,Ip},{class,group},{frequency,0},{depends_on,"none"},
										{depends_condition,"good"},{category,good},{last,[]},{state_string,"0 in group,0 in error"}],
					DeviceGroupId = 
						case api_group:create(NNMId,DeviceGroupData) of
							{ok,R2} -> proplists:get_value(id,R2);
							_ -> []
						end,
					addNetworkMonitor(DeviceGroupId,Device)
			end;
		true ->
			[]
	end.
							
addNetworkMonitor(DeviceGroupId,Device) ->
	MonitorData = [],
	api_monitor:create(DeviceGroupId, MonitorData).

%% save coordinate
savaCoordinateDevice(CoordinateDevice) ->
	Machine = api_machine:getAllMachine(0,0,"",""),
	savaCoordinateDevice(Machine,CoordinateDevice).
			
savaCoordinateDevice([],_) -> [];
savaCoordinateDevice([H|Machine],CoordinateDevice) ->
%% 	io:format("machine:~p~n",[H]),
	Id = H#machine.id,
	case proplists:get_value(Id,CoordinateDevice) of
		undefined ->
			savaCoordinateDevice(Machine,CoordinateDevice);
		{X,Y} ->
			Other = H#machine.other,
			FOther = lists:keydelete(y, 1, lists:keydelete(x, 1, Other)),
			api_machine:update_machine(H#machine{other=lists:append([{x,X},{y,Y}], FOther)}),
			savaCoordinateDevice(Machine,CoordinateDevice)
	end.

%% scan over save edge
saveEdgeInfo(EdgeInfo,Version,TopoSet) ->
	Edge = [[{leftIndex,LeftIndex},{leftPort,LeftPort},{leftDescr,LeftDescr},{leftAlias,LeftAlias},{leftDeviceId,LeftDeviceId},
			 {rightIndex,RightIndex},{rightPort,RightPort},{rightDescr,RightDescr},{rightAlias,RightAlias},{rightDeviceId,RightDeviceId}] 
		   || {LeftDeviceId,LeftIndex,LeftPort,LeftDescr,LeftAlias,RightDeviceId,RightIndex,RightPort,RightDescr,RightAlias} <- EdgeInfo],
	Data = [{version,Version},{edge,Edge},{topoSet,TopoSet}],
	submitTopoChart(Data).
		
%% monior interval operate
getMonitorInterval() ->
	case erlcmdb:find_ci(?NNM_MONITOR_INTERVAL_TABLE,[]) of
		[] -> [];
		[Data|_] ->
			proplists:get_value(info,Data,[])
	end.

setMonitorInterval(Interval) ->
	case erlcmdb:find_ci(?NNM_MONITOR_INTERVAL_TABLE,[]) of
		[] -> 
			erlcmdb:create_ci(?NNM_MONITOR_INTERVAL_TABLE,[{info,Interval}]);
		Data ->
			[erlcmdb:delete_ci(?NNM_MONITOR_INTERVAL_TABLE,proplists:get_value(id, X)) || X <- Data],
			erlcmdb:create_ci(?NNM_MONITOR_INTERVAL_TABLE,[{info,Interval}])
	end.

%% add or update one device
submitOneDevice(Device) ->
	case proplists:get_value(id,Device) of
		undefined ->
			addNetworkDevice(Device);
		Id ->
			Machine = api_machine:get_machine(Id),
			updateNetworkDevice(Device,Machine)
	end.
			
%% ip disstribution
ipDistribution(StartIp, EndIp) ->
	Machine = api_machine:getAllMachine(0,0,"",""),
	Pc = readFromDB("nnm_discovery_pcList",[id,ip,mac,name,type,discoverTime,activeTime],"",""),
	MachineIp = ipDistribution_filter_machine(Machine),
	IpList = nnm_discovery_util:getIpList(StartIp ++ "-" ++ EndIp),
	{{Y,M,D},{H,MM,S}} = calendar:gregorian_seconds_to_datetime(calendar:datetime_to_gregorian_seconds(calendar:local_time()) - ?ACTIVETIME),
	F = fun(X) ->
				V = integer_to_list(X),
				if
					length(V) < 2 ->
						"0" ++ V;
					true ->
						V
				end
		end,
	
	Time = integer_to_list(Y) ++ "-" ++ F(M) ++ "-" ++ F(D) ++ " " ++ 	F(H) ++ ":" ++ F(MM) ++ ":" ++ F(S),
	io:format("Device:~p~n",[lists:append(Pc, MachineIp)]),
	ipDistribution(IpList,lists:append(Pc, MachineIp),Time,[],[],[]).

ipDistribution([],_,_,Active,Inactive,Invalid) -> [{active,Active},{inactive,Inactive},{invalid,Invalid}];
ipDistribution([H|IpList],DeviceList,Time,Active,Inactive,Invalid) ->
	case lists:filter(fun(X) -> 
							  Ip = proplists:get_value(ip,X),
							  Ip =:= H orelse lists:member(H, Ip)
					  end, DeviceList) of
		[] ->
			ipDistribution(IpList,DeviceList,Time,Active,Inactive,
						   [H|Invalid]);
		[Device|_] ->
			ActiveTime = proplists:get_value(activeTime,Device,""),
			FDevice = lists:keyreplace(ip, 1, Device, {ip,H}),
			if
				ActiveTime < Time ->
					ipDistribution(IpList,DeviceList,Time,Active,[FDevice|Inactive],Invalid);
				true ->
					ipDistribution(IpList,DeviceList,Time,[FDevice|Active],Inactive,Invalid)
			end
	end.

ipDistribution_filter_machine([]) -> [];
ipDistribution_filter_machine([H|Machine]) ->
	Id = H#machine.id,
	Ip = H#machine.host,
	Type = H#machine.type,
	Other = H#machine.other,
	ActiveTime = proplists:get_value(activeTime,Other,""),
	DiscoverTime = proplists:get_value(discoverTime,Other,""),
	Name = proplists:get_value(sysName,Other,""),
	Mac = proplists:get_value(baseMac,Other,""),
	
	IpList = [proplists:get_value(ipAddress,X) || X <- proplists:get_value(ipAddr,Other,[])],
	[[{id,Id},{ip,IpList++[Ip]},{mac,Mac},{name,Name},{type,Type},
	{discoverTime,DiscoverTime},{activeTime,ActiveTime}]|ipDistribution_filter_machine(Machine)].

%% relayout
reLayout(TopochartId,Shape) ->
	Machine = api_machine:getAllMachine(0,0,"",""),
	Topochart = getTopoChart(TopochartId),
 	Point = [X#machine.id || X <- Machine],
	Line = [{list_to_atom(proplists:get_value(leftDeviceId,Y)),
			 list_to_atom(proplists:get_value(rightDeviceId,Y))} 
		   || Y <- proplists:get_value(edge,Topochart)],
	io:format("Point1:~p~n,Point2:~p~n,Line:~p~n",[Point,[],Line]),
	nnm_discovery_util:writeFile([Point],"point1.txt",write),
	nnm_discovery_util:writeFile([[]],"point2.txt",write),
	nnm_discovery_util:writeFile([Line],"line.txt",write),
	case nnm_calculateCoordinate:calculate(Point,Line,Shape,{32.0,32.0},[{exclude,[]}]) of
		{ok,R} -> 
			savaCoordinateDevice(R),
			true;
		_ -> false
	end.
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


		