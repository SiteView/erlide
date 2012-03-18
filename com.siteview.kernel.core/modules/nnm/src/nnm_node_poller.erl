%% ---
%% node poller
%%
%%---
-module(nnm_node_poller,[BASE]).
-compile(export_all).
-extends(nnm_poller).

-include("../../core/include/monitor.hrl").
-include("../../core/include/monitor_template.hrl").
-include("nnm_discovery_setting.hrl").
-include("nnm_discovery_topo.hrl").

-include_lib("snmp/include/snmp_types.hrl").


new()->
	Base = nnm_poller:new(),
	Base:set_attribute(lastMeasurementTime,0),
	{?MODULE,Base}.
	
% get_action_id()->
%	Monitor.
	
update()-> 
	
	{ok,{_,Id}} = THIS:get_property(id),
	{ok,{_,App}} = THIS:get_property(app_),
	io:format("*App:~p,node plloer execute:~p~n",[App,Id]),
	dbcs_base:set_app(App,true),
	Flds = [ip_address,community,agent_port,snmp_version,snmpv3_username,snmpv3_context,poll_interval,
			snmpv3_priv_method,snmpv3_priv_key,snmpv3_priv_key_ispwd,min_response_time,max_response_time,
			snmpv3_auth_method,snmpv3_auth_key,snmpv3_auth_key_ispwd,avg_response_time],
			
	case nnm_dbcs_node:get_node(Id,Flds) of
		{error,Err}->
			{error,Err};
		Node->
			Param = #nnm_snmpPara{
				ip = ipstr2tuple(proplists:get_value(ip_address,Node)),
				port = case proplists:get_value(agent_port,Node) of undefined->161;P1->P1 end,
				getCommunity = proplists:get_value(community,Node),
				timeout = 5000,
				retry = 2,
				snmpver = proplists:get_value(snmp_version,Node),
				authType = proplists:get_value(snmpv3_auth_method,Node),
				user = proplists:get_value(snmpv3_username,Node),
				passwd = proplists:get_value(snmpv3_auth_key,Node),
				privPasswd = proplists:get_value(snmpv3_priv_key,Node),
				contextID = proplists:get_value(snmpv3_context_id,Node),
				contextName = proplists:get_value(snmpv3_context,Node)
				},
				
			Pm = ping_monitor:new(),
			Pm:set_property(id,Id),
			%Pm:set_property(?NAME,"NNM NODE POLLER"),
			Pm:set_property(hostname,proplists:get_value(ip_address,Node)),
			Pm:set_property(timeout,3000),
			Pm:set_property(size,32),
			Pm:monitorUpdate(Pm),
			RespTime = case Pm:get_attribute(roundTripTime) of {ok,{_,RR1}} when is_integer(RR1)->RR1;_-> undefined end, 
			Pm:delete(),
			
			% io:format("**node:~p~n",[RespTime]),
			
			THIS:set_attribute(response_time,RespTime),
			
			MinResp = 
				case proplists:get_value(min_response_time,Node) of
					undefined->
						RespTime;
					MR1->
						if
							MR1>RespTime->
								RespTime;
							true ->
								MR1
						end
				end,
			MaxResp = 
				case proplists:get_value(max_response_time,Node) of
					undefined->
						RespTime;
					MR2->
						if
							MR2<RespTime->
								RespTime;
							true ->
								MR2
						end
				end,
				
			AvgResp = 
				case proplists:get_value(avg_response_time,Node) of
					undefined->
						RespTime;
					AR3 when RespTime=/=undefined andalso AR3=/=undefined->
						float_round((AR3+RespTime)/2);
					AR2->
						AR2
				end,
				
			THIS:set_attribute(avg_response_time,AvgResp),
			THIS:set_attribute(min_response_time,MinResp),
			THIS:set_attribute(max_response_time,MaxResp),


			Session = snmp_session:new(Param#nnm_snmpPara.ip,Param#nnm_snmpPara.port,Param#nnm_snmpPara.snmpver,
						   Param#nnm_snmpPara.getCommunity,Param#nnm_snmpPara.authType,Param#nnm_snmpPara.user,
						   Param#nnm_snmpPara.passwd,Param#nnm_snmpPara.privPasswd,Param#nnm_snmpPara.contextID,
						   Param#nnm_snmpPara.contextName,Param#nnm_snmpPara.timeout),
							   
			% poll interface first
			THIS:poll_interfaces(Id),
			
			case nnm_snmpDG:getMibObject(Param,[1,3,6,1,2,1,1,2]) of
				{ok,{_,Soid}}->
					Dev = nnm_discovery_api:getDeviceBascInfo(Soid),
					Cpu = case get_cpu_load(Dev#nnm_device.devFactory,Param) of
							{ok,{_,CC1}}->
								CC1;
							_->
								0
						end,
					Memory = case get_memory(Dev#nnm_device.devFactory,Param) of
							{ok,{MemoryUsed,MomoryFree}}->
								THIS:set_attribute(total_memory,MemoryUsed+MomoryFree),
								THIS:set_attribute(memory_used,MemoryUsed),
								THIS:set_attribute(percent_memory_used,float_round(MemoryUsed/(MemoryUsed+MomoryFree))),
								[{total_memory,MemoryUsed+MomoryFree},
								{memory_used,MemoryUsed},
								{percent_memory_used,float_round(MemoryUsed/(MemoryUsed+MomoryFree))}
								];
							_->
								THIS:set_attribute(total_memory,0),
								THIS:set_attribute(memory_used,0),
								THIS:set_attribute(percent_memory_used,0),
								[
								{memory_used,0},
								{total_memory,0},
								{percent_memory_used,0}
								]
							end,
					nnm_dbcs_node:update_node([{id,Id},
									{vendor,Dev#nnm_device.devFactory},
									{machine_type,Dev#nnm_device.devTypeName},
									{object_subtype,Dev#nnm_device.devModel},
									{cpu_load,Cpu}
									]++Memory
									),
					THIS:set_attribute(cpu_load,Cpu)
					;
				_->
					error
			end,
			

			
			dbcs_base:set_app(App,true),
			
			Status = 
					case nnm_dbcs_interface:get_by_node(Id,[admin_status,oper_status]) of
						{error,Erree}->
							io:format("nnm_dbcs_interface:get_by_node:~p~n",[Erree]),
							1;
						Ifs->
							F = fun(X)->
								case proplists:get_value(admin_status,X) of
									1->
										false;
									_->
										true
								end orelse
								case proplists:get_value(oper_status,X) of
									1->
										false;
									_->
										true
								end
							end,
							case lists:any(F,Ifs) of
								true->	
									0;
								_->
									1
							end
					end,
					
			StatusDescr = 	case Status of
								1->
									"Node Status is Up";
								0-> 
									"Node Status is Up,one or more interface is Down." ;
								_->
									"Node status is Down"
							end,
							
			THIS:set_attribute(?STATE_STRING,StatusDescr),
			
			THIS:set_attribute(status,Status),
			
			Oids = [
					[1,3,6,1,2,1,1,3,0],
					[1,3,6,1,2,1,1,4,0],
					[1,3,6,1,2,1,1,1,0],
					[1,3,6,1,2,1,1,6,0],
					[1,3,6,1,2,1,1,5,0],
					[1,3,6,1,2,1,1,2,0],
					[1,3,6,1,2,1,4,3,0],
					[1,3,6,1,2,1,4,8,0],
					[1,3,6,1,2,1,4,10,0],
					[1,3,6,1,2,1,4,11,0],
					[1,3,6,1,2,1,17,1,1]
					],
			PollInterval = proplists:get_value(poll_interval,Node),
			
			case Session:gm(Oids) of
				{ok,{noError,_,VBs},_}->
					SysUpTime = THIS:get_vbs_value([1,3,6,1,2,1,1,3,0],VBs),
					SysContact = THIS:get_vbs_value([1,3,6,1,2,1,1,4,0],VBs),
					SysDescr = THIS:get_vbs_value([1,3,6,1,2,1,1,1,0],VBs),
					SysLocation = THIS:get_vbs_value([1,3,6,1,2,1,1,6,0],VBs),
					SysName = THIS:get_vbs_value([1,3,6,1,2,1,1,5,0],VBs),
					SysObjectID = THIS:get_vbs_value([1,3,6,1,2,1,1,2,0],VBs),
					InRecv = THIS:get_vbs_value([1,3,6,1,2,1,4,3,0],VBs),
					InDiscards = THIS:get_vbs_value([1,3,6,1,2,1,4,8,0],VBs),
					OutReqs = THIS:get_vbs_value([1,3,6,1,2,1,4,10,0],VBs),
					OutDiscards = THIS:get_vbs_value([1,3,6,1,2,1,4,11,0],VBs),
					PhysicalAddress = mac2string(THIS:get_vbs_value([1,3,6,1,2,1,17,1,1],VBs)),
					Loss = 
					if
						InDiscards == undefined orelse OutDiscards == undefined ->
							0;
						InRecv == undefined orelse OutReqs == undefined->
							0;
						true->
							float_round((InDiscards + OutDiscards) / (InRecv + OutReqs))
					end,
		
					THIS:set_attribute(percent_loss,Loss),
					
					case nnm_dbcs_node:update_node([{id,Id},
											{status,Status},
											{status_description,StatusDescr},
											{systemup_time,SysUpTime},
											{contact,SysContact},
											{description,SysDescr},
											{location,SysLocation},
											{sys_name,SysName},
											{percent_loss,Loss},
											{physical_address,PhysicalAddress},
											{next_poll,sv_datetime:now()+ PollInterval*1000},
											{sys_objectid,SysObjectID}
											] ++
											case RespTime of
												undefined->
													[];
												_->
													[
													{response_time,RespTime},
													{avg_response_time,AvgResp},
													{min_response_time,MinResp},
													{max_response_time,MaxResp}
													]
											end
											) of
						{ok,_}->
							{ok,Id};
						{error,Err}->
							{error,Err};
						Else->
							{error,Else}
					end;
				ElseError->
					io:format("snmp gm:~p~n",[ElseError]),
					THIS:set_attribute(percent_loss,0),
					THIS:set_attribute(response_time,0),
					THIS:set_attribute(avg_response_time,AvgResp),
					THIS:set_attribute(min_response_time,MinResp),
					THIS:set_attribute(max_response_time,MaxResp),
					case nnm_dbcs_node:update_node([{id,Id},
											{status,Status},
											{status_description,StatusDescr},
											{next_poll,sv_datetime:now()+ PollInterval*1000}
											] ++
											case RespTime of
												undefined->
													[];
												_->
													[
													{response_time,RespTime},
													{avg_response_time,AvgResp},
													{min_response_time,MinResp},
													{max_response_time,MaxResp}
													]
											end
											) of
						{ok,_}->
							{ok,Id};
						{error,Err}->
							{error,Err};
						Else->
							{error,Else}
					end
			end
	end.
	
poll_interfaces(NodeId)->
	case nnm_dbcs_interface:get_by_node(NodeId,[id,?APP,node_id,interface_name]) of
		{error,_}->
			pass;
		Interfaces->
			% io:format("find interfaces:~p~n",[Interfaces]),
			poll_interfaces(NodeId,Interfaces)
	end.
	
poll_interfaces(_,[])->ok;	
poll_interfaces(NodeId,[If|T])->
	Id = proplists:get_value(id,If),
	case siteview:get_object(Id) of
		[M|_]->
			try
			M:set_attribute(running,true),
			M:monitorUpdate(M),
			{ok,{?CATEGORY,C2}} = M:get_attribute(?CATEGORY),
			M:save_result(M,C2),
			M:runActionRules(M,C2),
			M:set_attribute(running,false)
			catch
			error:_->error
			end;
		_->
			io:format("not find interface monitor:~p,App:~p~n",[Id,dbcs_base:get_app()]),
			error
	end,
	poll_interfaces(NodeId,T).
	
mac2string(undefined)->"";	
mac2string(Mac) when is_list(Mac)->
	L = [lists:flatten(io_lib:format("~.16B",[X]))||X<-Mac],
	string:join(L,".");
mac2string(_)->"".
			
float_round(N)->
	round(N*10000)/10000.			
			
ipstr2tuple(Ipstr) when is_list(Ipstr)->			
	list_to_tuple([list_to_integer(X) || X <- string:tokens(Ipstr,".")]);
ipstr2tuple(_) ->{0,0,0,0}.

get_cpu_load(Vendor,Param)->
	case Vendor of
		"Cisco"->
			nnm_snmpDG:getMibObject(Param,[1,3,6,1,4,1,9,2,1,57]);
		"Huawei"->
			nnm_snmpDG:getMibNextObject(Param,[1,3,6,1,4,1,2011,6,1,1,1,3]);
		"Huawei-3Com"->
			nnm_snmpDG:getMibNextObject(Param,[1,3,6,1,4,1,2011,6,1,1,1,3]);
		_->
			undefined
	end.
	
	
	
get_memory(Vendor,Param)->
	case Vendor of
		"Cisco"->
			Used = case nnm_snmpDG:getMibObject(Param,[1,3,6,1,4,1,9,9,48,1,1,1,5,1]) of
					{ok,{_,V1}}->
						V1;
					_->
						0
				end,
			Free = case nnm_snmpDG:getMibObject(Param,[1,3,6,1,4,1,9,9,48,1,1,1,6,1]) of
					{ok,{_,V2}}->
						V2;
					_->
						0
				end,
			{ok,{Used,Free}};
		"Huawei"->
			Total = case nnm_snmpDG:getMibNextObject(Param,[1,3,6,1,4,1,2011,6,1,2,1,1,2]) of
					{ok,{_,V1}} when is_integer(V1)->
						V1;
					_->
						0
				end,
			Free = case nnm_snmpDG:getMibNextObject(Param,[1,3,6,1,4,1,2011,6,1,2,1,1,3]) of
					{ok,{_,V2}} when is_integer(V2)->
						V2;
					_->
						0
				end,
			{ok,{Total-Free,Free}};
		"Huawei-3Com"->
			Total = case nnm_snmpDG:getMibNextObject(Param,[1,3,6,1,4,1,2011,6,1,2,1,1,2]) of
					{ok,{_,V1}} when is_integer(V1)->
						V1;
					_->
						0
				end,
			Free = case nnm_snmpDG:getMibNextObject(Param,[1,3,6,1,4,1,2011,6,1,2,1,1,3]) of
					{ok,{_,V2}} when is_integer(V2)->
						V2;
					_->
						0
				end,
			{ok,{Total-Free,Free}};
		_->
			undefined
	end.
	
get_classifier(error)->				%%get_classifier阀值函数，定义阀值条件
  case THIS:get_property(error_classifier) of
	{ok,{error_classifier,Classifier}}->
		Classifier;
	_->
		[{status,'==',2}]
	end;
get_classifier(warning)->
	case THIS:get_property(warning_classifier) of
		{ok,{warning_classifier,Classifier}}->
			Classifier;
		_->
			[{status,'==',0}]
	end;
get_classifier(good)->
	case THIS:get_property(good_classifier) of
		{ok,{good_classifier,Classifier}}->
			Classifier;
		_->
			[{status,'==',1}]
	end.
	
%% @spec get_template_property()-> Result
%% Result = [Record]
%% Record = property
%% @doc get monitor template of browsable_snmp_base
get_template_property()->
	BASE:get_template_property() ++ 
	[
	#property{name=status,title="Status",type=numeric,configurable=false,state=true},
	#property{name=total_memory,title="Total Memory",type=numeric,configurable=false,state=true},
	#property{name=memory_used,title="Memory Used",type=numeric,configurable=false,state=true},
	#property{name=cpu_load,title="CPU Load",type=numeric,configurable=false,state=true},
	#property{name=percent_memory_used,title="Percent Memory Used",type=numeric,configurable=false,state=true},
	#property{name=percent_loss,title="Percent Loss",type=numeric,configurable=false,state=true},
	#property{name=response_time,title="Response Time",type=numeric,configurable=false,state=true},
	#property{name=min_response_time,title="Min Response Time",type=numeric,configurable=false,state=true},
	#property{name=max_response_time,title="Max Response Time",type=numeric,configurable=false,state=true}
	].