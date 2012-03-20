-module(nnm_discovery_bll,[SpecialOidList,TelnetList]).
-compile(export_all).

-include("nnm_define.hrl").
-include("nnm_discovery_topo.hrl").

-define(OCTETS,octets).
-define(UCASTPKTS,ucastPkts).
-define(NUCASTPKTS,nUcastPkts).
-define(DISCARDS,discards).
-define(ERRORS,errors).
-define(BANDWIDTHRATE,bandwidthRate).
-define(ADMINSTATUS,adminStatus).
-define(OPERSTATUS,operStatus).


new(SpecialOidList,TelnetList) ->
	{?MODULE,SpecialOidList,TelnetList}.

new() ->
	{ok,TelnetList} = file:consult(?NNM_DISCOVERY_TELNETLISTPATH),
	{ok,SpecialOidList} = file:consult(?NNM_DISCOVERY_SPECIALOIDLISTPATH),
	{?MODULE,SpecialOidList,TelnetList}.

getParam(Param,Target) ->
%% 	io:format("Param:~p~n",[Param]),
	SnmpFlag = proplists:get_value(snmpFlag,Param,"0"),
	SnmpParam = proplists:get_value(snmpParam,Param,[]),
	TelnetFlag = proplists:get_value(telnetFlag,Param,"0"),
	TelnetParam = proplists:get_value(telnetParam,Param,[]),
	Ip = proplists:get_value(ip, Param, ""),
	SysobjectId = nnm_discovery_util:stringToIntegerList(proplists:get_value(sysobjectId,Param,[]),"."),
	
	TemplateId = proplists:get_value(Target,TelnetParam,""),
	if
		TelnetFlag =:= "1" andalso TemplateId =/= "" ->
			{nnm_discovery_telnet_read:new([{ip,Ip}|TelnetParam],TelnetList),TemplateId};
		SnmpFlag =:= "1" ->
			io:format("Ip:~p,Target:~p~n",[Ip,Target]),
			case lists:sublist(SysobjectId, 7) of
				[1,3,6,1,4,1,9] -> 
					Len = length(proplists:get_value(getCommunity,SnmpParam,[])),
					LogicalCommunity = nnm_snmp_api:readTreeValue([{ip,Ip}|SnmpParam],?ENTLOGICALCOMMUNITY),
					FLogicalCommunity = [V || 
										 {_,V} <- LogicalCommunity,
										 string:substr(V, Len+1)=/="@1001",
										 string:substr(V, Len+1)=/="@1002",
										 string:substr(V, Len+1)=/="@1003",
										 string:substr(V, Len+1)=/="@1004",
										 string:substr(V, Len+1)=/="@1005"],
					{nnm_discovery_snmp_read_cisco:new([{ip,Ip}|SnmpParam],SpecialOidList,FLogicalCommunity),SysobjectId};
				_ ->
					{nnm_discovery_snmp_read:new([{ip,Ip}|SnmpParam],SpecialOidList),SysobjectId}
			end;
		true ->
			[]
	end.

getTargetValue(Param,Target) ->
	case getParam(Param,Target) of
		[] ->
			[];
		{Object,TemplateId} ->
			case Target of
				?IPTABLE ->
					Object:readIpTable(TemplateId);
				?INTERFACETABLE ->
					Object:readInterfaceTable(TemplateId);
				?ROUTETABLE ->
					Object:readRouteTable(TemplateId);
				?AFTTABLE ->
					Object:readAftTable(TemplateId);
				?ARPTABLE ->
					Object:readArpTable(TemplateId);
				?CPUTABLE ->
					Object:readCpuTable(TemplateId);
				?MEMORYTABLE ->
					Object:readMemoryTable(TemplateId);
				?DEVICE ->
					Object:readDeviceInfo();
				?BGPTABLE ->
					Object:readBgpTable(TemplateId);
				?OSPFTABLE ->
					Object:readOspfTable(TemplateId);
				?DIRECTTABLE ->
					Object:readDirectTable(TemplateId);
				_ ->
					[]
			end
	end.

getMonitorValue(Param,Target) ->
	case getParam(Param,Target) of
		[] ->
			{proplists:get_value(ip, Param),[]};
		{Object,TemplateId} ->
			case Target of
				?IPTABLE ->
					Object:readIpTable(TemplateId);
				?FLOWTABLE ->
					Indexs = proplists:get_value(indexs,Param),
					Object:readPortFlow(TemplateId,Indexs);
				?AFTTABLE ->
					Object:readAftTable(TemplateId);
				?ARPTABLE ->
					Object:readArpTable(TemplateId);
				?CPUTABLE ->
					Object:readCpuTable(TemplateId);
				?MEMORYTABLE ->
					Object:readMemoryTable(TemplateId);
				_ ->
					[]
			end
	end.

getScanValue(Param,Target) ->
	case getParam(Param,Target) of
		[] ->
			[];
		{Object,TemplateId} ->
			case Target of
				?AFTTABLE ->
					Object:readAft(TemplateId);
				?ARPTABLE ->
					Object:readArp(TemplateId);
				?ROUTETABLE ->
					Object:readRoute(TemplateId);
				?INTERFACETABLE ->
					Object:readInterface(TemplateId);
				_ ->
					[]
			end
	end.

%% getPortValue(Param,Target) ->
%% 	case getParam(Param,Target) of
%% 		[] ->
%% 			[];
%% 		{Object,TemplateId} ->
%% 			Indexs = proplists:get_value(index,Param),
%% 			case Target of
%% 				?ADMINSTATUS ->
%% 					Object:readAdminStatus(TemplateId,Indexs);
%% 				?OPERSTATUS ->
%% 					Object:readOperStatus(TemplateId,Indexs);
%% 				?OCTETS ->
%% 					Object:readOctets(TemplateId,Indexs);
%% 				?UCASTPKTS ->
%% 					Object:readUcastPkts(TemplateId,Indexs);
%% 				?NUCASTPKTS ->
%% 					Object:readNUcastPkts(TemplateId,Indexs);
%% 				?DISCARDS ->
%% 					Object:readDiscards(TemplateId,Indexs);
%% 				?ERRORS ->
%% 					Object:readErrors(TemplateId,Indexs);
%% 				?BANDWIDTHRATE ->
%% 					Object:readBandWidthRate(TemplateId,Indexs);
%% 				_ ->
%% 					[]
%% 			end
%% 	end.
				



	
	




























