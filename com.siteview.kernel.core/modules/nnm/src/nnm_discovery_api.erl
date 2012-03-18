-module(nnm_discovery_api).
-compile(export_all).

-include("nnm_discovery_setting.hrl").
-include("nnm_discovery_topo.hrl").
-include("nnm_define.hrl").


getDeviceBascInfo(SysObjectID) ->
	{ok,SysObjectIDList} = file:consult(?NNM_DISCOVERY_SYSOBJECTPATH),
	Device = nnm_discovery_device:new(),
	Device:getsysObjectInfo(SysObjectID,SysObjectIDList,#nnm_device{}).

getMibObject(SnmpParam,Oid) ->
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),
	case nnm_snmp_api:getMibObject(SnmpParam,FOid) of
		{ok,{ROid,Value}} -> {ok,{nnm_discovery_util:integerListToString(ROid,10,"."),Value}};
		R -> R
	end.

getNextMibObject(SnmpParam,Oid) ->
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),
	case nnm_snmp_api:getNextMibObject(SnmpParam,FOid) of
		{ok,{ROid,Value}} -> {ok,{nnm_discovery_util:integerListToString(ROid,10,"."),Value}};
		R -> R
	end.

getTreeMibObject(SnmpParam,Oid) ->
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),
	case nnm_snmp_api:getTreeMibObject(SnmpParam,FOid) of
		{ok,Data} -> {ok,[{nnm_discovery_util:integerListToString(ROid,10,"."),Value} || {ROid,Value} <- Data]};
		R -> R
	end.
			

getBulkMibObject(SnmpParam,Oid,NRep,MaxRep) ->
	FOid = nnm_discovery_util:stringToIntegerList(Oid,"."),
	getBulkMibObject(SnmpParam,FOid,NRep,MaxRep).

startScan(VersionId) ->
	nnm_discovery_scan:start_link(),
	nnm_discovery_scan:start_scan(VersionId).

stopScan(VersionId) ->
	nnm_discovery_scan:stop_scan(VersionId).

getScanInfo(VersionId) ->
	nnm_discovery_scan:get_scan_info(VersionId).

getScanConfig() ->
	nnm_discovery_dal:getScanConfig().

addOrUpdateScanConfig(ScanConfig) ->
	nnm_discovery_dal:addOrUpdateScanConfig(ScanConfig).

getScanVersion() ->
	nnm_discovery_dal:getScanVersion().

addOrUpdateScanVersion(ScanVersion) ->
	nnm_discovery_dal:addOrUpdateScanVersion(ScanVersion).




























