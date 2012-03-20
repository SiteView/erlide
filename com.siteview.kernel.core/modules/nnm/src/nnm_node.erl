%% 
%% @doc api of nnm nodes
%% @version{1.0}
%% @copyright 2010 dragonflow.com
%% @author Shi xianfang <xianfang.shi@dragonflow.com>

-module(nnm_node).
-compile(export_all).
-include_lib("snmp/include/snmp_types.hrl").
-include("nnm_discovery_topo.hrl").


-define(IF_INDEX,[1,3,6,1,2,1,2,2,1,1]).
-define(IF_DESCR,[1,3,6,1,2,1,2,2,1,2]).
-define(IF_ADMINSTATUS,[1,3,6,1,2,1,2,2,1,7]).
-define(IF_OPERSTATUS,[1,3,6,1,2,1,2,2,1,8]).

find_interface(Params)->
	{Type,Pwd} = case proplists:get_value(snmpv3_priv_key_ispwd,Params) of
			true->
				{proplists:get_value(snmpv3_priv_method,Params),proplists:get_value(snmpv3_priv_key,Params)};
			_->
				case proplists:get_value(snmpv3_auth_key_ispwd,Params) of
					true->
						{proplists:get_value(snmpv3_auth_method,Params),proplists:get_value(snmpv3_auth_key,Params)};
					_->
						{"no",""}
				end
		end,
	Port = case proplists:get_value(agent_port,Params) of
		undefined->161;
		V1->V1
		end,
	Community = case proplists:get_value(community,Params) of
			undefined->"public";V2->V2 
			end,
		
	case proplists:get_value(ip_address,Params) of
		undefined->
			{error,not_spefic_ip};
		Ip->
				
			S = snmp_session:new(ipstr2tuple(Ip),
								Port,
								proplists:get_value(snmp_version,Params),
								Community,
								Type,
								proplists:get_value(snmpv3_username,Params),
								Pwd,
								proplists:get_value(snmpv3_priv_key,Params),
								proplists:get_value(engine_id,Params),
								proplists:get_value(snmpv3_context,Params),
								10000
								),
			find_if(S,?IF_INDEX)
	end.
	
ipstr2tuple(Ipstr) when is_list(Ipstr)->			
	list_to_tuple([list_to_integer(X) || X <- string:tokens(Ipstr,".")]);
ipstr2tuple(_) ->{0,0,0,0}.			
	
find_if(S,Oid)->
	% io:format("find_if:~p~n",[S]),
	case S:gn(Oid) of
		{ok,{noError,_,[VB|_]},_}->
			case VB#varbind.variabletype of
				'NULL'->
					[];
				_->
					case lists:prefix(?IF_INDEX,VB#varbind.oid) of
						true->
							Index2 = lists:nth(length(VB#varbind.oid),VB#varbind.oid),
							[
							[{interface_index,VB#varbind.value}] ++
							case S:g(?IF_DESCR++[Index2]) of
								{ok,{noError,_,[VB2|_]},_}->
									[{interface_name,VB2#varbind.value}];
								_->
									[{interface_name,undefined}]
							end ++
							case S:g(?IF_ADMINSTATUS++[Index2]) of
								{ok,{noError,_,[VB3|_]},_}->
									[{admin_status,VB3#varbind.value}];
								_->
									[{admin_status,undefined}]
							end ++
							case S:g(?IF_OPERSTATUS++[Index2]) of
								{ok,{noError,_,[VB3|_]},_}->
									[{oper_status,VB3#varbind.value}];
								_->
									[{oper_status,undefined}]
							end
							] ++ find_if(S,VB#varbind.oid);
						_->
							[]
					end
			end;
		_->
			[]
	end.
