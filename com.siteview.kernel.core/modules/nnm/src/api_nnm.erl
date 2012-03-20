-module(api_nnm).
-compile(export_all).

-export([snmp_set/2,snmp_get/2,snmp_get/3,snmp_get_next/2,snmp_get_tree/2,snmp_get_tree_term/2,snmp_get_bulk/4]).
-export([scan_start/1,scan_stop/1,scan_info_get/1]).
-export([scan_config_get/1,scan_config_submit/1,scan_config_delete/1]).
-export([topochart_submit/1,topochart_get/1,topochart_delete/1]).
-export([topochildchart_submit/1,topochildchart_get/1,topochildchart_delete/1]).
-export([line_level_submit/1,line_level_get/1,line_level_delete/1]).
-export([realtime_target_get/2]).
-export([monitor_set_interval/1,monitor_get_interval/0,monitor_get_device_data/2,monitor_get_port_data/2]).
-export([db_read/4,db_write/2,db_delete/2]).
-export([telnet_connect/1,telnet_send/2,telnet_close/1]).
-export([calculateCoordinate/5,reLayout/2]).
-export([device_refresh/1,device_submit/1,ipDistribution/2]).

-include("nnm_discovery_topo.hrl").
-include("nnm_define.hrl").
-include("../../core/include/monitor.hrl").

getDeviceBascInfo(SysObjectID) ->
	{ok,SysObjectIDList} = file:consult(?NNM_DISCOVERY_SYSOBJECTPATH),
	Device = nnm_discovery_device:new(),
	Device:getsysObjectInfo(SysObjectID,SysObjectIDList,#nnm_device{}).

%% @spec snmp_set(SnmpParam,VarsAndVals) -> (ok | {error,Reason})
%% where
%% 		SnmpParam = list()
%% 		VarsAndVals = [{oid(),type(),value()}]
%%		Reason = string()
%%		oid() = string()
%% 		type() = o ('OBJECT IDENTIFIER') | 
%%                i ('INTEGER') | 
%%                u ('Unsigned32') | 
%%                g ('Unsigned32') | 
%%                s ('OCTET SRING') | 
%%                b ('BITS') | 
%%                ip ('IpAddress') | 
%%                op ('Opaque') | 
%%                c32 ('Counter32') | 
%%                c64 ('Counter64') | 
%%                tt ('TimeTicks')
%% 		value() = term()
%% @doc snmp set
%%	<br />
%%  <dl>
%% 	<dt><em>example: </em></dt>
%%  <dd><pre> SnmpParam = [{ip,"192.168.0.254"},{port,161},{snmpVer,"v1"},{getCommunity,"public1"},{setCommunity,"private1"},{authType,""},
%% 	  	{user,""},{passwd,""},{privPasswd,""},{contextId,""},{contextName,""},{timeout,500}]
%%VarsAndVals = [{"1,3,6,1,2,1,2,2,1,7,1026",i,2},{"1,3,6,1,2,1,2,2,1,7,1026",i,2}]
%% 
%% </pre></dd>
%% 	</dl>
%% @end
snmp_set(SnmpParam,VarsAndVals) ->
	FVarsAndVals = [{nnm_discovery_util:stringToIntegerList(Oid,"."),Type,Value} || {Oid,Type,Value} <- VarsAndVals],
	nnm_snmp_api:setMibObject(SnmpParam,FVarsAndVals).

%% @spec snmp_get(SnmpParam,Oid)-> ({ok,{Oid,Value}} | {error,Reason})
%% where
%% 		SnmpParam = list()
%% 		Oid = string()
%%		Value = binary()
%%		Reason = string()
%% @doc snmp get
%%	<br />
%%  <dl>
%% 	<dt><em>example: </em></dt>
%%  <dd><pre> SnmpParam = [{ip,"192.168.0.254"},{port,161},{snmpVer,"v1"},{getCommunity,"public1"},{setCommunity,"private"},{authType,""},
%% 	  	{user,""},{passwd,""},{privPasswd,""},{contextId,""},{contextName,""},{timeout,500}]
%%Oid = "1.3.6.1.2.1.1.1"
%% 
%%return:{ok,{"1.3.6.1.2.1.1.1",&lt;&lt;131,107,0,173,72,117,97,119,101,105,32,86,101,114,115, 97,116,105,108,101,32,82,111,117,116,...&gt;&gt;>}}
%% </pre></dd>
%% 	</dl>
%% @end
snmp_get(SnmpParam,Oid) ->
	io:format("+++++++++++++++++++++++snmp_get(SnmpParam,Oid)"),
%% 	io:format("~p~n", SnmpParam),
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),
	case nnm_snmp_api:getMibObject(SnmpParam,FOid) of
		{ok,{ROid,Value}} -> {ok,{nnm_discovery_util:integerListToString(ROid,10,"."),Value}};
		R -> R
	end.


snmp_get(Node,SnmpParam,Oid) ->
%% 	io:format("+++++++++++++++++++++++~n"),
%% 	io:format("+++++++++++++++++++++++     ~p~n", Node),
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),
%% 	case rpc:call('monitorproxy@windy', nnm_snmp_api, getMibObject, [SnmpParam,FOid]) of
	case rpc:call(Node, nnm_snmp_api, getMibObject, [SnmpParam,FOid]) of
	%% 	case nnm_snmp_api:getMibObject(SnmpParam,FOid) of
		{ok,{ROid,Value}} -> {ok,{nnm_discovery_util:integerListToString(ROid,10,"."),Value}};
		R -> R
	end.

%% @spec snmp_get_next(SnmpParam,Oid)-> {ok,{Oid,Value}} | {error,Reason}
%% where
%% 		SnmpParam = list()
%% 		Oid = string()
%%		Value = binary()
%%		Reason = string()
%% @doc snmp get next
%%	<br />
%%  <dl>
%% 	<dt><em>example: </em></dt>
%%  <dd><pre> SnmpParam = [{ip,"192.168.0.254"},{port,161},{snmpVer,"v1"},{getCommunity,"public1"},{setCommunity,"private"},{authType,""},
%% 	  	{user,""},{passwd,""},{privPasswd,""},{contextId,""},{contextName,""},{timeout,500}]
%%Oid = "1.3.6.1.2.1.1.1"
%% 
%%return:{ok,{"1.3.6.1.2.1.1.1",&lt;&lt;131,107,0,173,72,117,97,119,101,105,32,86,101,114,115, 97,116,105,108,101,32,82,111,117,116,...&gt;&gt;>}}
%% </pre></dd>
%% 	</dl>
%% @end
snmp_get_next(SnmpParam,Oid) ->
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),
	case nnm_snmp_api:getNextMibObject(SnmpParam,FOid) of
		{ok,{ROid,Value}} -> {ok,{nnm_discovery_util:integerListToString(ROid,10,"."),Value}};
		R -> R
	end.

%% @spec snmp_get_tree(SnmpParam,Oid)-> {ok,Data} | {error,[]}
%% where
%% 		SnmpParam = list()
%% 		Oid = string()
%%		Data = list()
%% @doc 
%% snmp get col
%%	<br />
%%  <dl>
%% 	<dt><em>example: </em></dt>
%%  <dd><pre> SnmpParam = [{ip,"192.168.0.254"},{port,161},{snmpVer,"v1"},{getCommunity,"public1"},{setCommunity,"private"},{authType,""},
%% 	  	{user,""},{passwd,""},{privPasswd,""},{contextId,""},{contextName,""},{timeout,500}]
%%Oid = "1.3.6.1.2.1.1.1"
%% 
%%return: {ok,[{"1.3.6.1.2.1.1.1.0",
%%       &lt;&lt;131,107,0,173,72,117,97,119,101,105,32,86,101,114,115,
%%         97,116,105,108,101,32,82,111,117,...&gt;&gt;},
%%      {"1.3.6.1.2.1.1.2.0",
%%       &lt;&lt;131,108,0,0,0,10,97,1,97,3,97,6,97,1,97,4,97,1,98,0,0,
%%         7,219,...&gt;&gt;},
%%      {"1.3.6.1.2.1.1.3.0",&lt;&lt;131,98,1,30,167,152&gt;&gt;},
%%      {"1.3.6.1.2.1.1.4.0",
%%       &lt;&lt;131,107,0,41,82,38,68,32,66,101,105,106,105,110,103,44,
%%         32,72,117,97,119,...&gt;&gt;},
%%      {"1.3.6.1.2.1.1.5.0",
%%       &lt;&lt;131,107,0,7,81,117,105,100,119,97,121&gt;&gt;},
%%      {"1.3.6.1.2.1.1.6.0",
%%       &lt;&lt;131,107,0,13,66,101,105,106,105,110,103,32,67,104,105,
%%         110,97&gt;&gt;},
%%      {"1.3.6.1.2.1.1.7.0",&lt;&lt;131,97,78&gt;&gt;}]}
%% </pre></dd>
%% 	</dl>
%% @end
snmp_get_tree(SnmpParam,Oid) ->
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),

	case nnm_snmp_api:getTreeMibObject(SnmpParam,FOid) of
		{ok,Data} -> 
			{ok,[{nnm_discovery_util:integerListToString(ROid,10,"."),Value} || {ROid,Value} <- Data]};
			
		R -> R
	end.
	
snmp_get_tree_term(SnmpParam,Oid) -> 
%%	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),   %use "," 
	
	case nnm_snmp_api:getTreeMibObject(SnmpParam,Oid) of
		{ok,Data} -> 
			{ok,[{nnm_discovery_util:integerListToString(ROid,10,"."),binary_to_term(Value)} || {ROid,Value} <- Data]};
			
		R -> R
	end.
	
%% @spec snmp_get_bulk(SnmpParam,Oid,NRep,MaxRep)-> {ok,Data} | {error,[]}
%% where
%% 		SnmpParam = list()
%% 		Oid = string()
%% 		NRep = integer()
%% 		MaxRep = integer()
%%		Data = list()
%% @doc 
%% snmp get bulk
snmp_get_bulk(SnmpParam,Oid,NRep,MaxRep) ->
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),
	nnm_snmp_api:getBulkMibObject(SnmpParam,FOid,NRep,MaxRep).

%% @spec scan_start(Param)-> ok | {error,Reason}
%% where
%% 		Scan = [{configId,atom()}]
%%		Reason = string()
%% @doc 
%% start scan
scan_start(Param) ->
%% 	nnm_discovery_scan:start_scan(Param).
	io:format("start_scan:Parem: ~p~n" ,[Param]),
	nnm_scan:start_scan(Param).

%% @spec scan_stop(_Param)-> ok | error
%% @doc 
%% stop scan
scan_stop(_Param) ->
%% 	nnm_discovery_scan:stop_scan().
	nnm_scan:stop_scan().

%% @spec scan_info_get(Param)-> [] | Info
%% where
%% 		Param = [{index,integer()}]
%%		Info = list()
%% @doc 
%% if return list contain "end scan",scan overy.
%% <br />
%% 	<dl>
%% 	<dt><em>example: </em></dt>	
%% 	<dd><pre>return:
%% ["start scan...",
%%  "find device:192.168.0.248 ROUTE_SWITCH Cisco",
%%  "find device:192.168.9.134 Host Microsoft",
%%  "find device:192.168.9.116 Host Microsoft",
%%  "find device:192.168.9.63 Host Microsoft",
%%  "find device:192.168.9.16 Host Microsoft",
%%  "find device:192.168.0.238 Host Microsoft",
%%  "find device:192.168.0.251 SWITCH Cisco",
%%  "find device:192.168.0.181 SERVER Linux",
%%  "find device:192.168.0.176 Host Microsoft",
%%  "find device:192.168.0.254 ROUTE_SWITCH Huawei",
%%  "find device:192.168.0.162 Host CheckPoint",
%%  "find device:192.168.0.52 Host Microsoft",
%%  "find device:192.168.0.50 Host Microsoft",
%%  "find device:192.168.0.51 Host Microsoft",
%%  "find device:192.168.0.16 SERVER Linux","end scan"]
%% 	</pre></dd>
%% 	</dl>
%% @end
scan_info_get(Param) ->
	nnm_scan:get_scan_info(Param).

%% @spec scan_config_get(Param)-> [] | Data
%% where
%%      Param = [] | [{id,atom()}] | [{name,string()}]
%% 		Data = list()
%% @doc 
%% get scan config
%% <br />
%% <dl>
%% <dt><em>example:</em></dt>
%% 	<dd><pre>Param = [{name,"default"}] | [{id,'5940A1A4283A7495D4C956C4648045FB055BA3E7'}]
%% 
%% return: [{id,'5940A1A4283A7495D4C956C4648045FB055BA3E7'},
%%  {name,"default"},
%%  {defaultSnmpParam,[{snmpVer,"v2"},
%%                     {port,161},
%%                     {getCommunity,"public"},
%%                     {timeout,500},
%%                     {authType,[]},
%%                     {user,[]},
%%                     {passwd,[]},
%%                     {privPasswd,[]},
%%                     {contextId,[]},
%%                     {contextName,[]}]},
%%  {defineSnmpParam,[[{ip,"192.168.0.248"},
%%                     {snmpVer,"v2"},
%%                     {port,161},
%%                     {getCommunity,"dragon"},
%%                     {timeout,500}],
%%                    [{ip,"192.168.0.251"},
%%                     {snmpVer,"v2"},
%%                     {port,161},
%%                     {getCommunity,"public1"},
%%                     {timeout,500}],
%%                    [{ip,"192.168.0.254"},
%%                     {snmpVer,"v2"},
%%                     {port,161},
%%                     {getCommunity,"public1"},
%%                     {timeout,500}]]},
%%  {scanParam,[{scanSeed,"192.168.0.248"},
%%              {threads,30},
%%              {depth,3},
%%              {scanType,"snmpping"}]}]
%% 	</pre></dd>
%% </dl>
%% @end
scan_config_get(Param) ->
	nnm_discovery_dal:getScanConfig(Param).

%% @spec scan_config_delete(Param) -> ok | {error,Reason}
%% where
%%      Param = [] | [{id,atom()}] | [{name,string()}]
%% 		Id = atom()
%% 		Reason = string()
%% @doc 
%% delete scan config
%% <br />
%% 	<pre>
%% when Param is [],delete all config except default.default config can not be deleted
%% </pre>
%% @end
scan_config_delete(Param) ->
	nnm_discovery_dal:deleteScanConfig(Param).

%% @spec scan_config_submit(ScanConfig) -> {error,Reason} | {ok,Id}
%% where
%% 		ScanConfig = list()
%%		Id = atom()
%%		Reason = string()
%% @doc 
%% add or update scan config.ScanConfig contain {id,''} is update,not is add
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>DefaultSnmpParam = [{snmpVer,"v1"},{port,161},{getCommunity,"public"},{timeout,500},{authType,""},
%% 	  	{user,""},{passwd,""},{privPasswd,""},{contextId,""},{contextName,""}]
%% DefineSnmpParam = [[{ip,"192.168.0.249"},{snmpVer,"v1"},{port,161},{getCommunity,"public"},{timeout,500}],[{ip,"192.168.0.254"},{snmpVer,"v1"},{port,161},{getCommunity,"public1"},{timeout,500}]]
%% ScanParam = [{scanSeed,"192.168.0.249"},{includeIp,"192.168.1.0-192.168.1.11"},{excludeIp,"192.168.1.2-192.168.1.5"},{threads,50},{depth,3},{scanType,"snmpping"}]
%% Name = "default"
%% ScanConfig = [{name,Name},{defaultSnmpParam,DefaultSnmpParam},{defineSnmpParam,DefineSnmpParam},{scanParam,ScanParam}] 
%% 
%% return: {ok,'FBDA2E26073DF6FEF5770E1AC554717F979BF411'} 
%% 
%% scanType: "snmpping" | "nmap" | "arp" 
%% snmpVer: "v1" | "v2" | "v3"
%% 	</pre></dd>
%% 	</dl>
%% @end
scan_config_submit(ScanConfig) ->
	DefaultParam = proplists:get_value(defaultSnmpParam,ScanConfig,[]),
	DefineParam = proplists:get_value(defineSnmpParam,ScanConfig,[]),
	Port1 = nnm_discovery_util:toInteger(proplists:get_value(port,DefaultParam,161),161),
%% 	Port2 = nnm_discovery_util:toInteger(proplists:get_value(port,DefineParam,161),161),
	Timeout1 = nnm_discovery_util:toInteger(proplists:get_value(timeout,DefaultParam,500),500),
%% 	Timeout2 = nnm_discovery_util:toInteger(proplists:get_value(timeout,DefineParam,500),500),
	
	FDefineParam = lists:map(fun(X) ->
									 Port2 = nnm_discovery_util:toInteger(proplists:get_value(port,X,161),161),
									 Timeout2 = nnm_discovery_util:toInteger(proplists:get_value(timeout,X,500),500),
									 lists:keyreplace(timeout, 1, lists:keyreplace(port, 1, X, {port,Port2}), {timeout,Timeout2})
							 end, DefineParam),
	
	FDefaultParam = lists:keyreplace(timeout, 1, lists:keyreplace(port, 1, DefaultParam, {port,Port1}), {timeout,Timeout1}),
%% 	FDefineParam = lists:keyreplace(timeout, 1, lists:keyreplace(port, 1, DefineParam, {port,Port2}), {timeout,Timeout2}),
	FScanConfig = lists:keyreplace(defineSnmpParam, 1, lists:keyreplace(defaultSnmpParam, 1, ScanConfig, {defaultSnmpParam,FDefaultParam}), 
								   {defineSnmpParam,FDefineParam}), 
				
	nnm_discovery_dal:addOrUpdateScanConfig(FScanConfig).

%% @spec topochart_submit(TopoData)-> {error,Reason} | {ok,Id}
%% where
%% 		TopoData = topochart_get(Id)
%% 		Id = atom()
%%		Reason = string()
%% @doc 
%% add or update topo chart
%% @end
topochart_submit(TopoData) ->
	nnm_discovery_dal:submitTopoChart(TopoData).

%% @spec topochart_get(Id)-> [] | Data
%% where
%% 		Id = [] | atom()
%%		Data = list()
%% @doc 
%% get topo chart
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>return:[{id,'9F21278AA33393597AD5332724DEA0D0DF0E6A0D'},
%%  {version,[{name,""},{time,""}]},
%%  {edge,[[{leftIndex,"16"},
%%          {leftPort,"16"},
%%          {leftDescr,"FastEthernet0/16"},
%%          {leftAlias,[]},
%%          {leftDeviceId,[]},
%%          {rightIndex,"0"},
%%          {rightPort,"PX"},
%%          {rightDescr,[]},
%%          {rightAlias,[]},
%%          {rightDeviceId,[]}],
%%         [{leftIndex,"21"},
%%          {leftPort,"21"},
%%          {leftDescr,"FastEthernet0/21"},
%%          {leftAlias,[]},
%%          {leftDeviceId,[]},
%%          {rightIndex,"0"},
%%          {rightPort,"PX"},
%%          {rightDescr,[]},
%%          {rightAlias,[]},
%%          {rightDeviceId,[]}],
%%         [{leftIndex,"0"},{leftPort,[...]},{leftDescr,...},{...}|...],
%%         [{leftIndex,[...]},{leftPort,...},{...}|...],
%%         [{leftIndex,...},{...}|...],
%%         [{...}|...],
%%         [...]|...]}]
%% 	</pre></dd>
%% 	</dl>
%% @end
topochart_get(Id) ->
	nnm_discovery_dal:getTopoChart(Id).

%% @spec topochart_delete(Id) -> ok | {error,Reason}
%% where
%% 		Id = [] | atom()
%%		Reason = string()
%% @doc 
%% delete topo chart
%% <br />
%% 	when Id is [],delete all topo chart
%% @end
topochart_delete(Id) ->
	nnm_discovery_dal:deleteTopoChart(Id).

%% @spec realtime_target_get(Id,Target) -> {Ip, Value} | []
%% where
%% 		Id = atom()
%% 		Target = ipTable|interfaceTable|routeTable|aftTable|arpTable|cpuTable|memoryTable|directTable|ospfTable|bgpTable
%% 		Ip = string()
%%		Value = list()
%% @doc 
%% get (ipTable,interfaceTable,routeTable,arpTable,aftTable,cpuTable,memoryTable) real-time 
%% <br />
%% <dl>
%% <dt><em>return data format: {ip,data}</em></dt>
%% 	<dd><pre>target:
%% ipTable:[{ip,ifIndex,mask}]
%% interfaceTable:[{ifIndex,ifDescr,ifType,ifMtu,ifSpeed,ifMac,ifAdminStatus,ifOperStatus,ifLastChange,ifInOctets,ifInUcastPkts,
%% 						  	ifInNUcastPkts,ifInDiscards,ifInErrors,ifInUnknownProtos,ifOutOctets,ifOutUcastPkts,ifOutNUcastPkts,
%% 							ifOutDiscards,ifOutErrors,ifOutQLen,ifAlias}]
%% routeTable:[{destIp,ifIndex,nextHop,type,proto,mask}]
%% aftTable:[{portIndex,mac,status}]
%% arpTable:[{ifIndex,mac,ip,type}]
%% cpuTable:[used]
%% memoryTable:[{size,used}]
%% directTable:[{address,ifIndex,version,deviceId,devicePort,platform,vtpMgmtDomain,nativeVLAN,duplex,mtu}]
%% ospfTable:[{index,ipAddr}]
%% bgpTable:[{localAddr,localPort,remoteAddr,remotePort}]
%% 	</pre></dd>
%% 	</dl>
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>Id = '1288:93026:981001'
%%   Target = ipTable
%% 
%% return:{"192.168.0.254",
%%  [{"127.0.0.1","128","255.0.0.0"},
%%   {"192.168.0.254","4358","255.255.255.0"},
%%   {"192.168.3.1","3846","255.255.255.0"},
%%   {"192.168.4.1","3974","255.255.255.0"}]}
%% 
%% Target = routeTable
%%   
%%   return:{"192.168.0.254",
%%  [{"0.0.0.0","4358","192.168.0.1","4",2,"0.0.0.0"},
%%   {"192.168.0.0","4358","192.168.0.254","3",2,"255.255.255.0"},
%%   {"192.168.0.254","128","127.0.0.1","3",2,"255.255.255.255"},
%%   {"192.168.1.0","4358","192.168.0.249","4",2,"255.255.255.0"},
%%   {"192.168.3.0","4358","192.168.0.249","4",2,"255.255.255.0"},
%%   {"192.168.4.0","4358","192.168.0.249","4",2,"255.255.255.0"},
%%   {"192.168.5.0","4358","192.168.0.249","4",2,"255.255.255.0"},
%%   {"192.168.7.0","4358","192.168.4.3","4",2,"255.255.255.0"}]}
%% 	</pre></dd>
%% 	</dl>
%% @end
realtime_target_get(Id,Target) ->
	Device = api_machine:get_machine(Id),
	Ip = Device#machine.host,
	Other = Device#machine.other,
	
	SnmpFlag = proplists:get_value(snmpFlag, Other, "0"),
	SnmpParam = proplists:get_value(snmpParam, Other, []),
	TelnetFlag = proplists:get_value(telnetFlag, Other, "0"),
	TelnetParam = proplists:get_value(telnetParam, Other, []),
	SysobjectId = proplists:get_value(sysobjectId, Other, ""),

	Param = [{ip,Ip},{sysobjectId,SysobjectId},{snmpFlag,SnmpFlag},{snmpParam,SnmpParam},{telnetFlag,TelnetFlag},{telnetParam,TelnetParam}],
	
%% 	io:format("Param:~p~n",[Param]),
	GetObject = nnm_discovery_bll:new(),
	GetObject:getTargetValue(Param,Target).
	
%% @spec monitor_set_interval(Time)-> ok
%% where
%% 		Time = list()
%% @doc 
%% set monitor interval time.
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>Time = [{flowTable,600},{arpTable,180},{aftTable,300}]
%%   
%%   return:ok
%% 	</pre></dd>
%% 	</dl>
%% @end
monitor_set_interval(Time) ->
	nnm_discovery_dal:setMonitorInterval(Time).

%% @spec monitor_get_interval()-> [] | Data
%% where
%% 		Data = list()
%% @doc 
%% get monitor interval time
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>
%%   return:[{flowTable,600},{arpTable,180},{aftTable,300}]
%% 	</pre></dd>
%% 	</dl>
%% @end
monitor_get_interval() ->
	nnm_discovery_dal:getMonitorInterval().

%% @spec monitor_get_device_data(Target,Param)-> Data
%% where
%% 		Target = aftTable|arpTable|cpuTable|memoryTable|linkchart
%% 		Param = {id,list()} | {ip,list()} | {mac,list()}
%%		Data = list()
%% @doc 
%% get (ipTable,arpTable,aftTable,cpuTable,memoryTable,linkchart) from monitor service 
%% <br />
%% <dl>
%% <dt><em>return data format: [{id,ip,data}]</em></dt>
%% 	<dd><pre>target:
%% aftTable:[{portIndex,mac,ip,status}]
%% arpTable:[{ifIndex,mac,ip,type}]
%% cpuTable:[used]
%% memoryTable:[{size,used}]
%% 
%% linkchart:[{leftDeviceId,leftPort,leftIf,leftDescr,leftAlias,rightDeviceId,rightPort,rightIf,rightDescr,rightAlias}]
%% 	</pre></dd>
%% 	</dl>
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>Id = ['1288:93026:981001','1288:93026:934000']
%%   Target = ipTable
%% 
%% return:[{'1288:93026:981001',"192.168.0.254",
%%  [{"127.0.0.1","128","255.0.0.0"},
%%   {"192.168.0.254","4358","255.255.255.0"},
%%   {"192.168.3.1","3846","255.255.255.0"},
%%   {"192.168.4.1","3974","255.255.255.0"}]},
%% {'1288:93026:934000',"192.168.0.252",
%%  [{"127.0.0.1","128","255.0.0.0"},
%%   {"192.168.0.252","3718","255.255.255.0"}]}]
%% 	</pre></dd>
%% 	</dl>
%% @end
monitor_get_device_data(Target,Param) ->
	nnm_monitor:get_device_data(Target,Param).

%% @spec monitor_get_port_data(Target,PortList) -> Data
%% where
%% 		Target = octets|ucastPkts|nUcastPkts|discards|errors|bandwidthRate|operStatus
%% 		PortList = list()
%%		Data = list()
%% @doc 
%% get flow from monitor service 
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>PortList = [{'1290:674274:109002',["10004","10005"]}]
%%   Target = octets
%% 
%% return:[{'1290:674274:109002',"192.168.0.248",
%%                        [{"10004",["0","0","0"]},
%%                         {"10005",["864296","818399","45897"]}]}]
%% 
%% target = bandwidthRate
%% return:[{'1290:674274:109002',"192.168.0.248",
%%                        [{"10004","0.00"},{"10005","0.01"}]}]
%% 	</pre></dd>
%% 	</dl>
%% @end
monitor_get_port_data(Target,IfList) ->
	nnm_monitor:get_port_data(Target,IfList).

%% @spec db_read(TableName,ColList,Where,Order) -> [] | Data
%% where
%% 		TableName = string()
%% 		ColList = list()
%% 		Where = list()
%% 		Order = list()
%% 		Data = list()
%% @doc 
%% get data from db 
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>api_nnm:db_read("machine",[id,host],[],"") 
%% 
%% 	[[{id,'1290:674274:47002'},{host,"192.168.9.16"}],
%%  [{id,'1290:674274:62002'},{host,"192.168.9.47"}],
%%  [{id,'1290:674274:78002'},{host,"192.168.9.116"}],
%%  [{id,'1290:674274:93000'},{host,"192.168.9.134"}],
%%  [{id,'1290:674274:109002'},{host,"192.168.0.248"}],
%%  [{id,'1290:674274:31003'},{host,"192.168.0.238"}],
%%  [{id,'1290:674274:15002'},{host,"192.168.0.251"}],
%%  [{id,'1290:674274:2'},{host,"192.168.0.176"}],
%%  [{id,'1290:674273:984005'},{host,"192.168.0.162"}],
%%  [{id,'1290:674273:984001'},{host,"192.168.0.52"}],
%%  [{id,'1290:674273:937001'},{host,"192.168.0.50"}],
%%  [{id,'1290:674273:968002'},{host,"192.168.0.51"}],
%%  [{id,'1290:674273:937000'},{host,"192.168.0.16"}]]
%% 	</pre></dd>
%% 	</dl>
%% @end
db_read(TableName,ColList,Where,Order) ->
	nnm_discovery_dal:readFromDB(TableName,ColList,Where,Order).

%% @spec db_write(TableName,Data) -> [] | IdList
%% where
%% 		TableName = string()
%% 		Data = list()
%%		IdList = list()
%% @doc 
%% add or update data to db 
%% @end
db_write(TableName,Data) ->
	io:format("db_write TableName = ~p~nData = ~p~n",[TableName,Data]),
	nnm_discovery_dal:writeToDB(TableName,Data).

%% @spec db_delete(TableName,IdList) -> ok | error
%% where
%% 		TableName = string()
%%		IdList = list()
%% @doc 
%% delete data in db 
%% @end
db_delete(TableName,IdList) ->
	nnm_discovery_dal:deleteInDB(TableName,IdList).

%% @spec telnet_connect(Param) -> {ok,Pid} | {error,Reason}
%% where
%% 		Param = list()
%%		Pid = pid()
%%		Reason = string() 
%% 
%% @doc 
%% init telnet connection
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% <dd><pre>Param = [{host,"192.168.0.254"},{port,23}, {user,""}, {passwd,""}, {superUser,""}, {superPasswd,""}, 
%% 					{userPrompt,""}, {passwdPrompt,""}, {prompt,""}, {superPrompt,""}, {timeout,5000}]
%% 
%% </pre></dd>
%% </dl>
%% @end
telnet_connect(Param) ->
	nnm_telnet_server:connect(Param).

%% @spec telnet_send(Pid,Cmd) -> {ok,Result} | {error,Reason}
%% where
%% 		Cmd = string()
%%		Pid = pid()
%%		Result = string() 
%% 		Reason = string() 
%% @doc 
%% send telnet command
%% @end
telnet_send(Pid,Cmd) ->
	nnm_telnet_server:send(Pid,Cmd).

%% @spec telnet_close(Pid) -> {ok,close} | {error,Reason}
%% where
%%		Pid = pid()
%% 		Reason = atom()
%% @doc 
%% close telnet connect
%% @end
telnet_close(Pid) ->
	nnm_telnet_server:close(Pid).

%% @spec calculateCoordinate(Point,Line,Shape,Size,Options) -> {ok,Data} | {error,Reason}
%% where
%% 		Point = list()
%% 		Line = list()
%% 		Shape = atom()
%%		Size = tuple()
%%		Options = list() 
%% 		Data = list()
%% 		Reason = atom()
%% @doc 
%% Calculate the coordinates
%% Point: [Id,...]
%% Line: [{LeftId,RightId},...]
%% Shape: organic | circular | rectangular | vertical
%% Size:{width,height}
%% Options:[{exclude,Point}]
%% Data: [{Id1,{X1,Y1}},{Id2,{X2,Y2}},...]
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre> api_nnm:calculateCoordinate(['1','2','3','4'],[{'1','3'},{'2','4'},{'1','2'},{'1','4'}],circular,{32.0,32.0},[{exclude,[{'1',{1,3}},{'2',{3,4}}]}]).
%% 	return: {ok,[{'1',"15","15"},
%%      {'2',"15","90"},
%%      {'3',"90","15"},
%%      {'4',"90","90"}]}
%% 	</pre></dd>
%% 	</dl>
%% @end
calculateCoordinate(Point,Line,Shape,Size,Options) ->
	nnm_calculateCoordinate:calculate(Point,Line,Shape,Size,Options).

%% @spec reLayout(TopochartId,Shape) -> true | false
%% where
%% 		TopochartId = atom()
%% 		Shape = atom()
%% @doc 
%% Shape: organic | circular | rectangular | vertical
reLayout(TopochartId,Shape) ->
	nnm_discovery_dal:reLayout(TopochartId,Shape).

%% @spec device_refresh(Param) -> Data | []
%% where
%%		Param = list()
%% 		Data = machine()
%% @doc 
%% get device info
%% @end
device_refresh(Params) ->
%% 	Param = [{ip,proplists:get_value(ip,Params,"")},{snmpFlag,"1"},{snmpParam,lists:keydelete(ip, 1, Params)}],
	Object = nnm_discovery_snmp_read:new(Params),
	Device = Object:readDeviceInfo(),
%% 	io:format("device:~p~n",[Device]),
	case  api_nnm:db_read("machine",[id],["host='"++ proplists:get_value(ip,Device,"") ++"'"],"") of
		[] -> 
			Id = nnm_discovery_dal:addNetworkDevice(Device),
			api_machine:get_machine(Id);
		[R|_] ->
			Id = proplists:get_value(id,R),
			Machine = api_machine:get_machine(Id),
			nnm_discovery_dal:updateNetworkDevice(Device,Machine),
			Machine;
		_ ->
			[]
	end.

%% @spec device_submit(Device) -> Id 
%% where
%% 		Device = list()
%%		Id = atom()
%% @doc 
%% add or update device info
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre> Device = [{id,''},{name,""},{sysName,""},{ip,""},{devTypeName,""},{baseMac,""},{snmpFlag,""},{snmpParam,[]},{telnetFlag,[]},{telnetParam,[]},
%% {devType,""},{devFactory,""},{devModel,""},{sysDescr,""},{sysobjectId,""},{sysSvcs,""},{ipAddr,[]},{macs,[]},{infs,[]}],
%% 	</pre></dd>
%% 	</dl>
%% @end
device_submit(Device) ->
	io:format("device_submit Device = ~p~n", [Device]),
	nnm_discovery_dal:submitOneDevice(Device).

%% @spec ipDistribution(StartIp, EndIp) -> Data | [] 
%% where
%% 		StartIp = string()
%%		EndIp = string()
%% 		Data = list()
%% @doc 
%% <br />
%% 	<dl>
%% 	<dt><em>example:</em></dt>
%% 	<dd><pre>api_nnm:ipDistribution("192.168.0.1", "192.168.0.10")
%% return:[{active,[[{id,'98587B2AF9799CAAB5EC2DE6157C0AEB4B606E57'},
%%            {ip,"192.168.0.8"},
%%            {mac,"5AAC.B43C.6862"},
%%            {name,[]},
%%            {type,[]},
%%            {discoverTime,"2011-03-07 11:38:17"},
%%            {activeTime,"2011-04-18 10:44:29"}],
%%           [{id,'A5FF1A858A6824B0E8E645FD281B3C7156A83E4C'},
%%            {ip,"192.168.0.5"},
%%            {mac,"000C.29C5.3389"},
%%            {name,[]},
%%            {type,[]},
%%            {discoverTime,"2011-03-07 11:38:17"},
%%            {activeTime,"2011-04-18 10:44:29"}],
%%           [{id,'2231C977487E9396587C353ADAC2AF68DAB730C7'},
%%            {ip,"192.168.0.3"},
%%            {mac,"0014.784E.0143"},
%%            {name,[]},
%%            {type,[]},
%%            {discoverTime,"2011-03-07 11:38:17"},
%%            {activeTime,"2011-04-18 10:44:29"}],
%%           [{id,'BED19489B29BA57C635239456F61A649941BE1C8'},
%%            {ip,"192.168.0.1"},
%%            {mac,"0014.784D.2851"},
%%            {name,[]},
%%            {type,[]},
%%            {discoverTime,"2011-03-07 11:38:17"},
%%            {activeTime,"2011-04-18 10:44:29"}]]},
%%  {inactive,[]},
%%  {invalid,["192.168.0.10","192.168.0.9","192.168.0.7",
%%            "192.168.0.6","192.168.0.4","192.168.0.2"]}]
%% 	</pre></dd>
%% 	</dl>
%% @end
ipDistribution(StartIp, EndIp) ->
	nnm_discovery_dal:ipDistribution(StartIp, EndIp).
	

%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%% oldhand add
%%-export([topochildchart_submit/1,topochildchart_get/1,topochildchart_delete/1]).
topochildchart_submit([]) -> io:format("topochildchart_submit[]"),[];
topochildchart_submit([Head|Data]) ->
 	io:format("topochildchart_submit Head:~p~n",[Head]),
	case proplists:get_value(id, Head) of
		undefined ->	
			io:format("topochildchart_submit id undefined Head = ~p~n",[Head]),
			case erlcmdb:create_ci(?NNM_DISCOVERY_TOPOCHILDCHART_TABLE,Head) of
				{ok,[H|_]} ->
					{id,Id1} = H,
					{ok,Id1};
				ErrorReason -> ErrorReason
				%~ _ ->
					%~ {error,"fail"}
			end;
		Id ->
			io:format("Id:~p~n",[Id]),
			Id1 = 
				case is_list(Id) of
				 false->
					Id;
				 true->
                    list_to_atom(Id) 
				end,
			io:format("Id1:~p~n",[Id1]),
			erlcmdb:update_ci(?NNM_DISCOVERY_TOPOCHILDCHART_TABLE,Id1,Head),
			
			io:format("topochildchart_submit Data:~p~n",[Data]),
			[Id|topochildchart_submit(Data)]
	end.

topochildchart_get([]) ->
	case erlcmdb:find_ci(?NNM_DISCOVERY_TOPOCHILDCHART_TABLE,[]) of
		[] ->
			[];
		Data ->
			[[{id,proplists:get_value(id,X)},{edge,proplists:get_value(edge,X,[])},
			{topochildname,proplists:get_value(topochildname,X,[])},{devicelist,proplists:get_value(devicelist,X,[])},
			{firstipseg,proplists:get_value(firstipseg,X,[])},{lastipseg,proplists:get_value(lastipseg,X,[])},
			{keydevice,proplists:get_value(keydevice,X,[])},{layout,proplists:get_value(layout,X,[])},
			{location,proplists:get_value(location,X,[])},{permission,proplists:get_value(permission,X,[])},
			{states,proplists:get_value(states,X,[])},{devtype,proplists:get_value(devtype,X,[])}
			] || X <- Data]
	end;
topochildchart_get(Id) ->
%% 	nnm_discovery_util:writeFile(Id,"Id.txt"),
	case erlcmdb:find_ci(?NNM_DISCOVERY_TOPOCHILDCHART_TABLE,Id) of
		[] -> 
			[];
		[Data|_] ->
			[{id,proplists:get_value(id,Data)},
			 {edge,proplists:get_value(edge,Data,[])},
			 {topochildname,proplists:get_value(topochildname,Data,[])},
			 {devicelist,proplists:get_value(devicelist,Data,[])},
			 {firstipseg,proplists:get_value(firstipseg,Data,[])},
			 {lastipseg,proplists:get_value(lastipseg,Data,[])},
			 {keydevice,proplists:get_value(keydevice,Data,[])},
			 {layout,proplists:get_value(layout,Data,[])},
			 {location,proplists:get_value(location,Data,[])},
			 {permission,proplists:get_value(permission,Data,[])},
			 {states,proplists:get_value(states,Data,[])},
			 {devtype,proplists:get_value(devtype,Data,[])}
			 ]
	end.

topochildchart_delete([]) ->
	Data = erlcmdb:find_ci(?NNM_DISCOVERY_TOPOCHILDCHART_TABLE,[]),
	[erlcmdb:delete_ci(?NNM_DISCOVERY_TOPOCHILDCHART_TABLE,proplists:get_value(id, X)) || X <- Data],
	ok;
topochildchart_delete(Id) ->
	erlcmdb:delete_ci(?NNM_DISCOVERY_TOPOCHILDCHART_TABLE,Id),
	ok.
%%%%%%%%%%%%%%%	
	
line_level_submit(TopoData) ->
%% 	io:format("topoData:~p~n",[TopoData]),
	case proplists:get_value(id, TopoData) of
		undefined ->			
			case erlcmdb:create_ci(?NNM_DISCOVERY_LINE_LEVEL,TopoData) of
				{ok,[H|_]} ->
					{id,Id1} = H,
					{ok,Id1};
				ErrorReason -> ErrorReason
				%~ _ ->
					%~ {error,"fail"}
			end;
		Id ->
			erlcmdb:update_ci(?NNM_DISCOVERY_LINE_LEVEL,Id,TopoData),
			{ok,Id}
	end.

line_level_get([]) ->
	case erlcmdb:find_ci(?NNM_DISCOVERY_LINE_LEVEL,[]) of
		[] ->
			[];
		Data ->
			[[{id,proplists:get_value(id,X)},
			{lid,proplists:get_value(lid,X,[])},
			{edge,proplists:get_value(edge,X,[])},
			{levelset,proplists:get_value(levelset,X,[])}
			] || X <- Data]
	end;
line_level_get(Id) ->
%% 	nnm_discovery_util:writeFile(Id,"Id.txt"),
	case erlcmdb:find_ci(?NNM_DISCOVERY_LINE_LEVEL,Id) of
		[] -> 
			[];
		[Data|_] ->
			[
			 {id,proplists:get_value(id,Data)},
			 {lid,proplists:get_value(lid,Data)},
			 {edge,proplists:get_value(edge,Data)},
			 {levelset,proplists:get_value(levelset,Data,[])}
			 ]
	end.
	
line_level_getbylid([]) ->
	case erlcmdb:find_ci(?NNM_DISCOVERY_LINE_LEVEL,[]) of
		[] ->
			[];
		Data ->
			[[{id,proplists:get_value(id,X)},{edge,proplists:get_value(edge,X,[])},
			{lid,proplists:get_value(lid,X,[])},
			{levelset,proplists:get_value(levelset,X,[])}
			] || X <- Data]
	end;
line_level_getbylid(Id) ->
%% 	nnm_discovery_util:writeFile(Id,"Id.txt"),
	case erlcmdb:find_ci(?NNM_DISCOVERY_LINE_LEVEL,[]) of
		[] -> 
			[];
		Data ->
			Newdata = [[{id,proplists:get_value(id,X)},{edge,proplists:get_value(edge,X,[])},
			{lid,proplists:get_value(lid,X,[])},
			{levelset,proplists:get_value(levelset,X,[])}
			] || X <- Data],
			line_level_getbylid(Newdata,Id)
			
	end.	
	
line_level_getbylid([L|R],Id) ->
        case lists:keyfind(lid, 1, L) of
	      {lid,Id} -> L;
	      _ -> line_level_getbylid(R,Id) 
	end;
line_level_getbylid([],_) -> [].

line_level_delete([]) ->
	Data = erlcmdb:find_ci(?NNM_DISCOVERY_LINE_LEVEL,[]),
	[erlcmdb:delete_ci(?NNM_DISCOVERY_LINE_LEVEL,proplists:get_value(id, X)) || X <- Data],
	ok;
line_level_delete(Id) ->
	erlcmdb:delete_ci(?NNM_DISCOVERY_LINE_LEVEL,Id),
	ok.





















	





















