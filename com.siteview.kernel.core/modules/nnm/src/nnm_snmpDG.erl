-module(nnm_snmpDG).
-compile(export_all).

-include_lib("snmp/include/snmp_types.hrl").
-include_lib("nnm_discovery_setting.hrl").

getMibObject(SnmpPara,Oid) ->
	Session = snmp_session:new(SnmpPara#nnm_snmpPara.ip,SnmpPara#nnm_snmpPara.port,SnmpPara#nnm_snmpPara.snmpver,
							   SnmpPara#nnm_snmpPara.getCommunity,SnmpPara#nnm_snmpPara.authType,SnmpPara#nnm_snmpPara.user,
							   SnmpPara#nnm_snmpPara.passwd,SnmpPara#nnm_snmpPara.privPasswd,SnmpPara#nnm_snmpPara.contextID,
							   SnmpPara#nnm_snmpPara.contextName,SnmpPara#nnm_snmpPara.timeout),
	Result = Session:gt(Oid),
	ResultValue = 
	case Result of
		{ok,{_,_,[{_,Oid2,_,Value,_}|_]},_} ->
			case lists:prefix(Oid,Oid2) of
				true ->
					{ok,{Oid,Value}};
				false ->
					{error,{Oid,Value}}
			end;
        Ex -> 
%%             io:format("Error info:~p~n", [Ex]),
            {error,Ex}
    end,
	ResultValue.

getMibNextObject(SnmpPara,Oid) ->
	Session = snmp_session:new(SnmpPara#nnm_snmpPara.ip,SnmpPara#nnm_snmpPara.port,SnmpPara#nnm_snmpPara.snmpver,
							   SnmpPara#nnm_snmpPara.getCommunity,SnmpPara#nnm_snmpPara.authType,SnmpPara#nnm_snmpPara.user,
							   SnmpPara#nnm_snmpPara.passwd,SnmpPara#nnm_snmpPara.privPasswd,SnmpPara#nnm_snmpPara.contextID,
							   SnmpPara#nnm_snmpPara.contextName,SnmpPara#nnm_snmpPara.timeout),
	Result = Session:gn(Oid),
	ResultValue = 
	case Result of
		{ok,{_,_,[{_,Oid2,_,Value,_}|_]},_} ->
			case lists:prefix(Oid,Oid2) of
				true ->
					{ok,{Oid,Value}};
				false ->
					{error,{Oid,Value}}
			end;
        Ex -> 
%%             io:format("Error info:~p~n", [Ex]),
            {error,Ex}
    end,
	ResultValue.

getMibTable(SnmpPara,Oid) ->
	Session = snmp_session:new(SnmpPara#nnm_snmpPara.ip,SnmpPara#nnm_snmpPara.port,SnmpPara#nnm_snmpPara.snmpver,
							   SnmpPara#nnm_snmpPara.getCommunity,SnmpPara#nnm_snmpPara.authType,SnmpPara#nnm_snmpPara.user,
							   SnmpPara#nnm_snmpPara.passwd,SnmpPara#nnm_snmpPara.privPasswd,SnmpPara#nnm_snmpPara.contextID,
							   SnmpPara#nnm_snmpPara.contextName,SnmpPara#nnm_snmpPara.timeout),
	Result = Session:get_table_col(Oid),
	ResultValue = 
	case Result of
		[] ->
			{error,{Oid,[]}};
		_ ->
			Info = getTableInfo(Oid,Result),
			{ok,Info}
	end,
	ResultValue.

getTableInfo(_,[]) -> [];
getTableInfo(Oid,[H|Result]) ->
	case H of
		{varbind,Oid2,_,Value,_} ->
			[{Oid2,Value}|getTableInfo(Oid,Result)];
		Ex ->
			[{Oid,Ex}|getTableInfo(Oid,Result)]
	end.

readObject(SnmpPara,Object) ->
	case Object =:= undefined of
		true -> [];
		false -> 
			{Oid,Fun} = Object,
			FunName = list_to_atom(Fun),
			ResultValue = nnm_snmpDG:FunName(SnmpPara,Oid),
			case ResultValue of
				{ok,{_,Value}} ->
					Value;
				_ ->
					[]
			end
	end.

readObject(SnmpPara,OidList,OidName) ->
	case proplists:get_value(list_to_atom(OidName), OidList) of
		undefined -> [];
		{Oid,Fun} ->
			FunName = list_to_atom(Fun),
			ResultValue = nnm_snmpDG:FunName(SnmpPara,Oid),
			case ResultValue of
				{ok,{_,Value}} ->
					Value;
				_ ->
					[]
			end
	end.

readTree(SnmpPara,Object) ->
	case Object =:= undefined of
		true -> [];
		false ->
			{Oid,Fun} = Object,
			FunName = list_to_atom(Fun),
			ResultValue = nnm_snmpDG:FunName(SnmpPara,Oid),
			case ResultValue of
				{ok,Result} ->
					[{Key--Oid,Value} || {Key,Value} <- Result];
				_ ->
					[]
			end
	end.

readTree(SnmpPara,OidList,OidName) ->
	case proplists:get_value(list_to_atom(OidName), OidList) of
		undefined -> [];
		{Oid,Fun} ->
			FunName = list_to_atom(Fun),
			ResultValue = nnm_snmpDG:FunName(SnmpPara,Oid),
			case ResultValue of
				{ok,Result} ->
					[{Key--Oid,Value} || {Key,Value} <- Result];
				_ ->
					[]
			end
	end.
	

			

snmpPing(SnmpPara,OidList,OidName) ->
	case proplists:get_value(list_to_atom(OidName), OidList) of
		undefined -> {error,"can't find oid!"};
		{Oid,Fun} ->
			FunName = list_to_atom(Fun),
			ResultValue = nnm_snmpDG:FunName(SnmpPara,Oid),
			case ResultValue of
				{ok,{_,Value}} ->
					{ok,Value};
				_ ->
					{error,"can't get value!"}
			end
	end.

test() ->
	SnmpPara = #nnm_snmpPara{
		ip = {192,168,0,254},
		port = 161,
		getCommunity = "public1",
		timeout = 5000,
		retry = 2,
		snmpver = "v1",
		authType = "",
		user = "",
		passwd = "",
		privPasswd = "",
		contextID = "",
		contextName = ""					 
	},
	%%Result1 = getMibObject(SnmpPara,[1,3,6,1,2,1,1,1]),
	%%io:format("Result1:~p~n", [Result1]),
	%%Result2 = getMibTable(SnmpPara,[1,3,6,1,2,1,1]),
	%%io:format("Result2:~p~n", [Result2]),
	%%IPMask = getMibTable(SnmpPara,[1,3,6,1,2,1,4,20,1,3]),
	%%io:format("IPMask:~p~n", [IPMask]),
	%%IPInfInx = getMibTable(SnmpPara,[1,3,6,1,2,1,4,20,1,2]),
	%%io:format("IPInfInx:~p~n", [IPInfInx]),
	%%IpMacs = getMibTable(SnmpPara,[1,3,6,1,2,1,2,2,1,6]),
	%%io:format("IPMacs:~p~n", [IpMacs]).
	%%IfTable = getMibNextObject(SnmpPara,[1,3,6,1,2,1,2,2]).
	%%io:format("IPMacs:~p~n", [IfTable]).
	%%TestValue = getMibTable(SnmpPara,[1,1000,1000,100]),
	%%io:format("TestValue:~p~n",[TestValue]),
	%%BgpVersion = getMibTable(SnmpPara,[1,3,6,1,2,1,15,1]),
	%%io:format("BgpVersion:~p~n",[BgpVersion]),
	%%OspfStubAreaTable = getMibObject(SnmpPara,[1,3,6,1,2,1,14,3]),
	%%io:format("(getMibObject)OspfStubAreaTable:~p~n", [OspfStubAreaTable]),
	%%OspfStubAreaTable = getMibTable(SnmpPara,[1,3,6,1,2,1,14,3]),
	%%io:format("(getMibTable)OspfStubAreaTable:~p~n", [OspfStubAreaTable]),
	Route = getMibTable(SnmpPara,[1,3,6,1,2,1,4,21,1]),
	io:format("Route:~p~n",[Route]).




	



			