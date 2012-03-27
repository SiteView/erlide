-module(nnm_snmp_api).
-compile(export_all).

-include_lib("snmp/include/snmp_types.hrl").
% -include_lib("nnm_discovery_setting.hrl").
-include("nnm_define.hrl").

-define(SNMP_PING_OID_1, [1,3,6,1,2,1,1,2,0]).
-define(SNMP_PING_OID_2, [1,3,6,1,2,1,1,2]).

getSnmpParam(SnmpParam) ->
	Ip = proplists:get_value(ip, SnmpParam, ""),
	Port = proplists:get_value(port, SnmpParam, 161),
	SnmpVer = proplists:get_value(snmpVer, SnmpParam, "v1"),
	GetCommunity = proplists:get_value(getCommunity, SnmpParam, "public"),
	SetCommunity = proplists:get_value(setCommunity, SnmpParam, "private"),
	AuthType = proplists:get_value(authType, SnmpParam, ""),
	User = proplists:get_value(user, SnmpParam, ""),
	Passwd = proplists:get_value(passwd, SnmpParam, ""),
	PrivPasswd = proplists:get_value(privPasswd, SnmpParam, ""),
	ContextId = proplists:get_value(contextId, SnmpParam, ""),
	ContextName = proplists:get_value(contextName, SnmpParam, ""),
	Timeout = proplists:get_value(timeout, SnmpParam, 500),
	BindPort = proplists:get_value(bindPort,SnmpParam,163),
	
	{ok,Path} = file:get_cwd(),
	Dir = proplists:get_value(dir,SnmpParam,Path ++ "/templates.nnm/"),

	{list_to_tuple(nnm_discovery_util:stringToIntegerList(Ip,".")), 
	 Port, SnmpVer, GetCommunity, SetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,BindPort,Dir}.

setMibObject(SnmpParam,VarsAndVals) ->
	{Ip, Port, SnmpVer, _GetCommunity, SetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,BindPort,Dir} =
		getSnmpParam(SnmpParam),
	Opts = [{port,BindPort},{dir,Dir}],
	Session = snmp_session:new(Ip, Port, SnmpVer, SetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,Opts),
	Session:start_config(),% add by qicheng.ai
	case Session:s(VarsAndVals) of
		{ok,{noError,_,_},_} ->
			ok;
		{ok,{R,_,_},_} ->
			{error,erlang:atom_to_list(R)};
		{error,timeout} ->
			{error,"timeout"};
		{error,{Ei,_}} ->
			{error,erlang:atom_to_list(Ei)};
		_ ->
			{error,"unknown error"}
	end.
		

getMibObject(SnmpParam,Oid) ->
	{Ip, Port, SnmpVer, GetCommunity, _SetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,BindPort,Dir} =
		getSnmpParam(SnmpParam),
	Opts = [{port,BindPort},{dir,Dir}],
	Session = snmp_session:new(Ip, Port, SnmpVer, GetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,Opts),
	Session:start_config(),% add by qicheng.ai
	case Session:gt(Oid) of
		{ok,{noSuchName,_,_},_}->
			{error, "noSuchName"};
		{ok,{noError,_,[Vb|_]},_} ->
			case Vb#varbind.variabletype of
				'NULL' ->
					{error,erlang:atom_to_list(Vb#varbind.value)};
				_ ->
					case lists:prefix(Oid, Vb#varbind.oid) of
						true -> 
							%io:format("nnm_snmp_api:getMibObject value:~p~n", [Vb#varbind.value]),
							{ok,{Oid,erlang:term_to_binary(Vb#varbind.value)}};
						false ->
							{error,"noSuchObject"}
					end
			end;
		{error,timeout} ->
			{error,"timeout"};
		{error,{Ei,_}} ->
			{error,erlang:atom_to_list(Ei)};
		_ ->
			{error,"unknown error"}
	end.

getNextMibObject(SnmpParam,Oid) ->
	{Ip, Port, SnmpVer, GetCommunity, _SetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,BindPort,Dir} =
		getSnmpParam(SnmpParam),
	Opts = [{port,BindPort},{dir,Dir}],
	Session = snmp_session:new(Ip, Port, SnmpVer, GetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,Opts),
	Session:start_config(),% add by qicheng.ai
	case Session:gn(Oid) of
		{ok,{noSuchName,_,_},_}->
			{error, "noSuchName"};
		{ok,{noError,_,[Vb|_]},_} ->
			case Vb#varbind.variabletype of
				'NULL' ->
					{error,erlang:atom_to_list(Vb#varbind.value)};
				_ ->
					case lists:prefix(Oid, Vb#varbind.oid) of
						true ->
							{ok,{Oid,erlang:term_to_binary(Vb#varbind.value)}};
						false ->
							{error,"noSuchObject"}
					end
			end;
		{error,timeout} ->
			{error,"timeout"};
		{error,{Ei,_}} ->
			{error,erlang:atom_to_list(Ei)};
		_ ->
			{error,"unknown error"}
	end.
		

getTreeMibObject(SnmpParam,Oid) ->
	{Ip, Port, SnmpVer, GetCommunity, _SetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,BindPort,Dir} =
		getSnmpParam(SnmpParam),
	Opts = [{port,BindPort},{dir,Dir}],
	Session = snmp_session:new(Ip, Port, SnmpVer, GetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,Opts),
	Session:start_config(),% add by qicheng.ai
	case Session:get_table_col(Oid) of
		[] ->
			{error,[]};
		Result ->
			Info = get_tree_info(Oid,Result),
			{ok,Info}
	end.

get_tree_info(_,[]) -> [];
get_tree_info(Oid,[H|Result]) ->
	case H of
		{varbind,Oid2,_,Value,_} ->
			[{Oid2,erlang:term_to_binary(Value)}|get_tree_info(Oid,Result)];
		_Ex ->
			[{Oid,""}|get_tree_info(Oid,Result)]
	end.

getBulkMibObject(SnmpParam,Oid,NRep,MaxRep) ->
	{Ip, Port, SnmpVer, GetCommunity, _SetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,BindPort,Dir} =
		getSnmpParam(SnmpParam),
	Opts = [{port,BindPort},{dir,Dir}],
	Session = snmp_session:new(Ip, Port, SnmpVer, GetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,Opts),
	Session:start_config(),% add by qicheng.ai
%% 	Session:gb(NRep,MaxRep,[Oid]).
	Session:get_table_col_item_bulk(Oid, NRep, MaxRep).
	

readOneValue(SnmpParam,Oid) ->
	case getMibObject(SnmpParam,Oid) of
		{error,_} ->
			Oid_b = 
				case lists:last(Oid) of
					0 ->
						lists:sublist(Oid, length(Oid)-1);
					_ ->
						Oid
				end,
			case getNextMibObject(SnmpParam,Oid_b) of
				{error,_} ->
					[];
				{ok,{_,Value}} ->
					erlang:binary_to_term(Value)
			end;
		{ok,{_,Value}} ->
			erlang:binary_to_term(Value);
		_ ->
			[]
	end.

readTreeValue(SnmpParam,Oid) ->
	io:format("ip:~p,oid:~p~n",[proplists:get_value(ip,SnmpParam),Oid]),
	case getTreeMibObject(SnmpParam,Oid) of
		{error,[]} ->
			[];
		{ok,Result} ->
			[{Key--Oid,erlang:binary_to_term(Value)} || {Key,Value} <- Result];
		_ ->
			[]
	end.

snmpPing(SnmpParam) ->
	{Ip, Port, SnmpVer, GetCommunity, _SetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, Timeout,BindPort,Dir} =
		getSnmpParam(SnmpParam),
	Opts = [{port,BindPort},{dir,Dir}],
	%%change by qicheng.ai 
	%%change the ping timeout value
	Session = snmp_session:new(Ip, Port, SnmpVer, GetCommunity, AuthType, User, Passwd, PrivPasswd, ContextId, ContextName, 5000,Opts),
	Session:start_config(),% add by qicheng.ai
	case Session:gt(?SNMP_PING_OID_1) of
		{ok,{noError,_,[Vb|_]},_} ->
			case Vb#varbind.variabletype of
				'NULL' ->
					case Session:gn(?SNMP_PING_OID_2) of
						{ok,{noError,_,[Vb|_]},_} ->
							case Vb#varbind.variabletype of
								'NULL' ->
									[];
								_ ->
									case lists:prefix(?SNMP_PING_OID_2, Vb#varbind.oid) of
										true ->
%% 											io:format("oid2~n"),
											Vb#varbind.value;
										false ->
											[]
									end
							end;
						_ ->
							[]
					end;
				_ ->
					case lists:prefix(?SNMP_PING_OID_1, Vb#varbind.oid) of
						true ->
%% 							io:format("oid1~n"),
							Vb#varbind.value;
						false ->
							[]
					end
			end;
		_ ->
			[]
	end.

test()-> "nnm snmp api".




	



			