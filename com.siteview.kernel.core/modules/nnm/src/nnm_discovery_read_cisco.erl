-module(nnm_discovery_read_cisco).
-compile(export_all).

readInterface(SnmpPara,SpecialOidValueList) -> 
	[].

readInterfaceTable(SnmpParam,SpecialOidList) ->
	[].

readFlow(SnmpParam,SpecialOidList) ->
	{proplists:get_value(ip,SnmpParam),[]}.

readArpTable(SnmpParam,SpecialOidList) ->
	{proplists:get_value(ip,SnmpParam),[]}.

readArp(SnmpParam,SpecialOidList) ->
	[].

readAftTable(SnmpParam,SpecialOidList) ->
	{proplists:get_value(ip,SnmpParam),[]}.

readAft(SnmpParam,SpecialOidList) ->
	[].

