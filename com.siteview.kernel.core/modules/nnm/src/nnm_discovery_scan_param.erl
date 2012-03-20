-module(nnm_discovery_scan_param).
-compile(export_all).

-include("nnm_discovery_setting.hrl").
-include("nnm_define.hrl").

addOrUpdateScanparam(Scanparam) ->
	Data = [
			{scanseed,Scanparam#nnm_scan_param.scanSeed},
			{includeip,Scanparam#nnm_scan_param.includeIp},
			{excludeip,Scanparam#nnm_scan_param.excludeIp},
			{threads,Scanparam#nnm_scan_param.threads},
			{depth,Scanparam#nnm_scan_param.depth},
			{scantype,Scanparam#nnm_scan_param.scanType}
			],
	Value = erlcmdb:find_ci(?NNM_DISCOVERY_SCANPARAM_TABLE,[]),
	io:format("Data:~p~n",[Data]),
	io:format("Value:~p~n",[Value]),
	case Value =:= [] of
		true ->
			erlcmdb:create_ci(?NNM_DISCOVERY_SCANPARAM_TABLE,Data);
		false ->
			[V|_] = Value,
			ID = proplists:get_value(id,V),
			erlcmdb:update_ci(?NNM_DISCOVERY_SCANPARAM_TABLE,ID,Data)
	end.

findScanparam() ->
	Data = erlcmdb:find_ci(?NNM_DISCOVERY_SCANPARAM_TABLE,[]),
	case Data =/= [] of
		true ->
			[H|_] = Data,
			ScanSeed = proplists:get_value(scanseed,H),
			IncludeIp = proplists:get_value(includeip, H),
			ExcludeIp = proplists:get_value(excludeip, H),
			Threads = proplists:get_value(threads,H),
			Depth = proplists:get_value(depth,H),
			ScanType = proplists:get_value(scantype, H),
			#nnm_scan_param{scanSeed=ScanSeed,includeIp=IncludeIp,excludeIp=ExcludeIp,threads=Threads,depth=Depth,scanType=ScanType};
		false ->
			#nnm_scan_param{}
	end.

deleteAllScanparam() ->
	Data = erlcmdb:find_ci(?NNM_DISCOVERY_SCANPARAM_TABLE,[]),
	IDList = getDeleteIDList(Data),
	deleteScanparam(IDList).

getDeleteIDList([]) -> [];
getDeleteIDList([H|ScanParamList]) ->
	ID = proplists:get_value(id,H),
	[ID|getDeleteIDList(ScanParamList)].

deleteScanparam([]) -> [];
deleteScanparam([H|IDList]) ->
	erlcmdb:delete_ci(?NNM_DISCOVERY_SCANPARAM_TABLE,H),
	deleteScanparam(IDList).

test() ->
	Data = #nnm_scan_param{
					scanSeed="192.168.0.254",
					includeIp = "192.168.0.1-192.168.0.254",
					excludeIp = "192.168.0.254-192.168.0.254",
					threads = "50",
					depth = "4",
					scanType = "snmpping"
					},
	addOrUpdateScanparam(Data).






