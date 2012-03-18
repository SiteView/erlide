%% ---
%% node poller
%%
%%---
-module(nnm_interface_poller,[BASE]).
-compile(export_all).
-extends(nnm_poller).

-include("../../core/include/monitor.hrl").
-include("../../core/include/monitor_template.hrl").
-include("nnm_discovery_setting.hrl").
-include("nnm_discovery_topo.hrl").
-include_lib("snmp/include/snmp_types.hrl").

new()->
	Base = nnm_poller:new(),
	{?MODULE,Base}.
	
% get_action_id()->
%	{Id,_}=Interface,
%	Id.
	
update()-> 
	
	{ok,{_,Id}} = THIS:get_property(id),
	% io:format("=interface plloer execute:~p~n",[Id]),
	{ok,{_,NodeId}} = THIS:get_property(node_id),
	{ok,{_,App}} = THIS:get_property(app_),
	dbcs_base:set_app(App,true),
	% {Id,NodeId} = Interface,
	Flds = [ip_address,community,agent_port,snmp_version,snmpv3_username,snmpv3_context,
			snmpv3_priv_method,snmpv3_priv_key,snmpv3_priv_key_ispwd,
			snmpv3_auth_method,snmpv3_auth_key,snmpv3_auth_key_ispwd],
			
	case nnm_dbcs_node:get_node(NodeId,Flds) of
		{error,Err}->
			{error,Err};
		Node->
			Param = #nnm_snmpPara{
				ip = ipstr2tuple(proplists:get_value(ip_address,Node)),
				port = case proplists:get_value(agent_port,Node) of undefined->161;P1->P1 end,
				getCommunity = proplists:get_value(community,Node),
				timeout = 2000,
				retry = 2,
				snmpver = proplists:get_value(snmp_version,Node),
				authType = proplists:get_value(snmpv3_auth_method,Node),
				user = proplists:get_value(snmpv3_username,Node),
				passwd = proplists:get_value(snmpv3_auth_key,Node),
				privPasswd = proplists:get_value(snmpv3_priv_key,Node),
				contextID = proplists:get_value(snmpv3_context_id,Node),
				contextName = proplists:get_value(snmpv3_context,Node)
				},
				
			Session = snmp_session:new(Param#nnm_snmpPara.ip,Param#nnm_snmpPara.port,Param#nnm_snmpPara.snmpver,
							   Param#nnm_snmpPara.getCommunity,Param#nnm_snmpPara.authType,Param#nnm_snmpPara.user,
							   Param#nnm_snmpPara.passwd,Param#nnm_snmpPara.privPasswd,Param#nnm_snmpPara.contextID,
							   Param#nnm_snmpPara.contextName,Param#nnm_snmpPara.timeout),
			
			case nnm_snmpDG:getMibObject(Param,[1,3,6,1,2,1,1,2]) of
				{ok,{_,Soid}}->
					Fields = [interface_index,poll_interval,interface_speed],
					
					case nnm_dbcs_interface:get_interface(Id,Fields) of
						{error,Error2}->
							THIS:set_attribute(status,"error"),
							THIS:set_attribute(?STATE_STRING,"error"),
							{error,Error2};
						Interface->
							case proplists:get_value(interface_index,Interface) of
								undefined->
									THIS:set_attribute(status,"error"),
									THIS:set_attribute(?STATE_STRING,"interface index error"),
									{error,interface_index_error};
								Index->
									PollInterval = proplists:get_value(poll_interval,Interface),
									
									Oids1 = [
											[1,3,6,1,2,1,2,2,1,2,Index],
											[1,3,6,1,2,1,2,2,1,3,Index],
											[1,3,6,1,2,1,2,2,1,4,Index],
											[1,3,6,1,2,1,2,2,1,5,Index],
											[1,3,6,1,2,1,2,2,1,6,Index],
											[1,3,6,1,2,1,2,2,1,7,Index],
											[1,3,6,1,2,1,2,2,1,8,Index],
											[1,3,6,1,2,1,2,2,1,9,Index],
											[1,3,6,1,2,1,2,2,1,10,Index],
											[1,3,6,1,2,1,2,2,1,11,Index],
											[1,3,6,1,2,1,2,2,1,12,Index],
											[1,3,6,1,2,1,2,2,1,13,Index],
											[1,3,6,1,2,1,2,2,1,14,Index],
											[1,3,6,1,2,1,2,2,1,16,Index],
											[1,3,6,1,2,1,2,2,1,17,Index],
											[1,3,6,1,2,1,2,2,1,18,Index],
											[1,3,6,1,2,1,2,2,1,19,Index],
											[1,3,6,1,2,1,2,2,1,20,Index]
											],
									case Session:gm(Oids1) of
										{ok,{noError,_,Vbs},_}->
											IfDescr =  THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,2,Index],Vbs),
											IfType = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,3,Index],Vbs),
											IfMTU = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,4,Index],Vbs),
											IfSpeed = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,5,Index],Vbs),
											IfMac = mac2string(THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,6,Index],Vbs)),
											IfAdminStatus = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,7,Index],Vbs),
											IfOperStatus = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,8,Index],Vbs),
											IfLastChange = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,9,Index],Vbs),
											
											IfInOctets1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,10,Index],Vbs),
											IfInUcastPkts1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,11,Index],Vbs),
											IfInNUcastPkts1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,12,Index],Vbs),
											IfInDiscards1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,13,Index],Vbs),
											IfInErrors1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,14,Index],Vbs),
											
											IfOutOctets1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,16,Index],Vbs),
											IfOutUcastPkts1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,17,Index],Vbs),
											IfOutNUcastPkts1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,18,Index],Vbs),
											IfOutDiscards1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,19,Index],Vbs),
											IfOutErrors1 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,20,Index],Vbs),
											
											IfTypeDescr = nnm_mib_tool:get_iftype_descr(IfType),
											
											sleep(500),
											Oids2 =
												[
												[1,3,6,1,2,1,2,2,1,10,Index],
												[1,3,6,1,2,1,2,2,1,11,Index],
												[1,3,6,1,2,1,2,2,1,12,Index],
												[1,3,6,1,2,1,2,2,1,16,Index],
												[1,3,6,1,2,1,2,2,1,17,Index],
												[1,3,6,1,2,1,2,2,1,18,Index]
												],
											
											
											THIS:set_attribute(status,"ok"),
											THIS:set_attribute(?STATE_STRING,"ok"),
											
											case Session:gm(Oids2) of
												{ok,{noError,_,Vbs2},_}->
													IfInOctets2 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,10,Index],Vbs2),
													IfInUcastPkts2 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,11,Index],Vbs2),
													IfInNUcastPkts2 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,12,Index],Vbs2),
													IfOutOctets2 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,16,Index],Vbs2),
													IfOutUcastPkts2 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,17,Index],Vbs2),
													IfOutNUcastPkts2 = THIS:get_vbs_value([1,3,6,1,2,1,2,2,1,18,Index],Vbs2),
											
									
													In_bps = cacl_speed(IfInOctets2,IfInOctets1,0.5),
													Out_bps = cacl_speed(IfOutOctets2,IfOutOctets1,0.5),
													In_pps = cacl_speed((IfInUcastPkts2 + IfInNUcastPkts2),(IfInUcastPkts1 + IfInNUcastPkts1),0.5),
													Out_pps = cacl_speed((IfOutUcastPkts2 + IfOutNUcastPkts2),(IfOutUcastPkts1 + IfOutNUcastPkts1),0.5),
													In_percent_util =  case IfSpeed of
																			undefined->
																				0;
																			0->
																				0;
																			_->
																				float_round(In_bps / IfSpeed)
																		end,
													Out_percent_util = case IfSpeed of
																		undefined->
																			0;
																		0->
																			0;
																		_->
																			float_round(Out_bps / IfSpeed)
																		end,
													
													Out_ucast_pps = cacl_speed(IfOutUcastPkts2,IfOutUcastPkts1, 0.5),
													Out_mcast_pps = cacl_speed(IfOutNUcastPkts2 , IfOutNUcastPkts1, 0.5),
													
													In_ucast_pps = cacl_speed(IfInUcastPkts2 ,IfInUcastPkts1, 0.5),
													In_mcast_pps = cacl_speed(IfInNUcastPkts2 , IfInNUcastPkts1,0.5),
													
													THIS:set_attribute(in_bps,In_bps),
													THIS:set_attribute(out_bps,Out_bps),
													THIS:set_attribute(in_pps,In_pps),
													THIS:set_attribute(out_pps,Out_pps),
													THIS:set_attribute(in_percent_util,In_percent_util),
													THIS:set_attribute(out_percent_util,Out_percent_util),
													THIS:set_attribute(out_ucast_pps,Out_ucast_pps),
													THIS:set_attribute(out_mcast_pps,Out_mcast_pps),
													THIS:set_attribute(in_ucast_pps,In_ucast_pps),
													THIS:set_attribute(in_mcast_pps,In_mcast_pps),
													
									
													case nnm_dbcs_interface:update_interface([{id,Id}] ++ 
																							[
																							{interface_name,IfDescr},
																							{interface_type,IfType},
																							{interface_type_name,IfTypeDescr},
																							{interface_mtu,IfMTU},
																							{interface_speed,IfSpeed},
																							{physical_address,IfMac},
																							{admin_status,IfAdminStatus},
																							{oper_status,IfOperStatus},
																							{interface_last_change,IfLastChange},
																							{in_bps,In_bps},
																							{out_bps,Out_bps},
																							{in_pps,In_pps},
																							{out_pps,Out_pps},
																							{in_percent_util,In_percent_util},
																							{out_percent_util,Out_percent_util},
																							{out_ucast_pps,Out_ucast_pps},
																							{out_mcast_pps,Out_mcast_pps},
																							{in_ucast_pps,In_ucast_pps},
																							{in_mcast_pps,In_mcast_pps},
																							{in_discards_thishour,IfInDiscards1},
																							{out_discards_thishour,IfOutDiscards1},
																							{in_errors_thishour,IfInErrors1},
																							{out_errors_thishour,IfOutErrors1},
																							{next_poll,sv_datetime:now()+PollInterval*1000}
																							]) of
														{ok,_}->
															{ok,Id};
														{error,Err}->
															{error,Err};
														Else->
															{error,Else}
													end;
												_->
													case nnm_dbcs_interface:update_interface([{id,Id}] ++ 
																							[
																							{interface_name,IfDescr},
																							{interface_type,IfType},
																							{interface_type_name,IfTypeDescr},
																							{interface_mtu,IfMTU},
																							{interface_speed,IfSpeed},
																							{physical_address,IfMac},
																							{admin_status,IfAdminStatus},
																							{oper_status,IfOperStatus},
																							
																							{next_poll,sv_datetime:now()+PollInterval*1000}
																							]) of
														{ok,_}->
															{ok,Id};
														{error,Err}->
															{error,Err};
														Else->
															{error,Else}
													end
											end;
										_->
											THIS:set_attribute(status,"error"),
											THIS:set_attribute(?STATE_STRING,"error"),
											{error,snmp_get_error}
									end
							end
						end;
				_->
					THIS:set_attribute(status,"error"),
					THIS:set_attribute(?STATE_STRING,"error"),
					nnm_dbcs_interface:update_interface([{id,Id},
														{admin_status,2},
														{oper_status,2}
														]),
					error
			end
			
	end.
			
sleep(MircoSec)->
	receive
	after MircoSec->
	ok
	end.

mac2string(undefined)->"";	
mac2string(Mac)->
	L = [lists:flatten(io_lib:format("~.16B",[X]))||X<-Mac],
	string:join(L,".").
	
cacl_speed(V2,V1,S) when V2==undefined orelse V1 == undefined orelse S == 0->
	0;
cacl_speed(V2,V1,S)->
	float_round((V2-V1)/S).
	
float_round(N)->
	round(N*10000)/10000.
			
ipstr2tuple(Ipstr) when is_list(Ipstr)->			
	list_to_tuple([list_to_integer(X) || X <- string:tokens(Ipstr,".")]);
ipstr2tuple(_) ->{0,0,0,0}.

get_classifier(error)->				%%get_classifier阀值函数，定义阀值条件
  case THIS:get_property(error_classifier) of
	{ok,{error_classifier,Classifier}}->
		Classifier;
	_->
		[{status,'!=',"ok"}]
	end;
get_classifier(warning)->
	case THIS:get_property(warning_classifier) of
		{ok,{warning_classifier,Classifier}}->
			Classifier;
		_->
			[{status,'!=',"ok"}]
	end;
get_classifier(good)->
	case THIS:get_property(good_classifier) of
		{ok,{good_classifier,Classifier}}->
			Classifier;
		_->
			[{status,'==',"ok"}]
	end.
	
%% @spec get_template_property()-> Result
%% Result = [Record]
%% Record = property
%% @doc get monitor template of browsable_snmp_base
get_template_property()->
	BASE:get_template_property() ++ 
	[
	#property{name=status,title="Status",type=text,configurable=false,state=true},
	#property{name=in_bps,title="receive(bps)",type=numeric,configurable=false,state=true},
	#property{name=out_bps,title="transmit(bps)",type=numeric,configurable=false,state=true},
	#property{name=in_pps,title="receive(pps)",type=numeric,configurable=false,state=true},
	#property{name=out_pps,title="transmit(pps)",type=numeric,configurable=false,state=true},
	#property{name=in_percent_util,title="receive percent utilization",type=numeric,configurable=false,state=true},
	#property{name=out_percent_util,title="transmit percent utilization",type=numeric,configurable=false,state=true},
	#property{name=out_ucast_pps,title="transmit ucast pps",type=numeric,configurable=false,state=true},
	#property{name=in_ucast_pps,title="receive ucast(pps)",type=numeric,configurable=false,state=true},
	#property{name=out_mcast_pps,title="transmit mcast(pps)",type=numeric,configurable=false,state=true},
	#property{name=in_mcast_pps,title="receive mcast(pps)",type=numeric,configurable=false,state=true}
	].