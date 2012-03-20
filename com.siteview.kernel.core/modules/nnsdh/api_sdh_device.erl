-module(api_sdh_device).
-behaviour(gen_server).

-export([start/0,stop/0,test/0,demo/0,vpn/0]).
-export([init/1, handle_call/3, handle_cast/2,terminate/2,handle_info/2,code_change/3]).

-export([getEMSAlarms/4,queryAlarms/1]).

-record(emsAlarms, {eventType, eventInfo}).

-record(tca_eventInfo, {notificationId,objectName,nativeEMSName,objectType,emsTime,neTime,isClearable,perceivedSeverity,layerRate,granularity,pmParameterName,pmLocation,thresholdType,value,unit,additionalInfo}).

-record(alarm_eventInfo,{notificationId,objectName,nativeEMSName,nativeProbableCause,objectType,emsTime,neTime,isClearable,layerRate,probableCause,probableCauseQualifier,perceivedSeverity,serviceAffecting,affectedTPList,additionalText,additionalInfo,x733EventType,objectTypeQualifier}).

-record(objectName, {ems,managedelement,ptp,ctp}).

-record(additionalInfo, {alarmserialno,alarmreason,productname,equipmentname,affirmstate,detailinfo,slaveshelf}).


-record(alarmsInfo, {id, sdhserver, eventType, equipmentname, productname, emsTime, perceivedSeverity, alarms}).

-record(sdhinfo,{ip,port,user,password,lastrecordid}).

writeLog(Log) ->
	case file:open("sdhInfoTest.txt",[append]) of
		{ok,File} -> io:format(File,"~n~p~n~n",[Log]),
		file:close(File);
		_ -> ok
	end.

test() ->
	AlarmsFun = fun(Alarm) -> io:format("~n~n Alarm = ~p~n",[Alarm]) end,
	Alarms = getEMSAlarms("10.184.249.250",12001,"nncsyh","nncsyh123"),
	case Alarms of
		{error,_} -> Alarms;
		_ -> lists:map(AlarmsFun,Alarms)
	end.

vpn() ->
	AlarmsFun = fun(Alarm) -> io:format("~n~n Alarm = ~p~n",[Alarm]) end,
	Alarms = getEMSAlarms("2.4.145.23",9315,"administrator","ncia"),
	case Alarms of
		{error,_} -> Alarms;
		_ -> lists:map(AlarmsFun,Alarms)
	end.

demo() ->
	Ra = alarms_Format(sdh_dy_data:analogue_data(),[]),
	save_alarms(lists:reverse(Ra),#sdhinfo{ip="10.184.249.250",port="12001",user="nncsyh",password="nncsyh123"}).

%~ ȡϢ
%~ Ip	CorbaӿƷַ
%~ Port	CorbaӿƷ˿
%~ User	Corbaʺ
%~ Pass	Corba
%~ Count	һλȡ¼
getEMSAlarms(Ip,Port,User,Pass) -> gen_server:call(?MODULE,{getEMSAlarms,Ip,Port,User,Pass},infinity).

queryAlarms(Query) -> gen_server:call(?MODULE,{queryAlarms,Query},infinity).

start() ->
    gen_server:start_link({local, ?MODULE}, ?MODULE, [], []).

stop() ->
	gen_server:cast(?MODULE, stop).

init(_Args) ->
	mnesia:delete_schema([node()]),
	mnesia:create_schema([node()]),
	mnesia:start(),
	orber:install([node()],[{nameservice_storage_type, disc_copies}]),
	oe_globaldefs:oe_register(),
	oe_common:oe_register(),
	oe_transmissionParameters:oe_register(),
	oe_terminationPoint:oe_register(),
	oe_subnetworkConnection:oe_register(),
	oe_managedElement:oe_register(),
	oe_topologicalLink:oe_register(),
	oe_protection:oe_register(),
	oe_multiLayerSubnetwork:oe_register(),
	oe_CosNotification:oe_register(),
	oe_notifications:oe_register(),
	oe_emsMgr:oe_register(),
	spawn_link(fun()->polling_data() end),
    {ok, _Args}.

handle_call({getEMSAlarms,Ip,Port,User,Pass},_From, State) ->
	Alarms = get_sdh_from_db(#sdhinfo{ip=Ip,port=if is_integer(Port) -> integer_to_list(Port); true -> Port end,user=User,password=Pass}),
    {reply, Alarms, State};

handle_call({queryAlarms,Query},_From,State) ->
	Alarms = query_alarms(Query),
	{reply, Alarms, State};

handle_call(stop, _From, Tab) -> mnesia:stop(), {stop, normal, stopped, Tab}. 

handle_cast(stop, State) -> {stop, normal, State};
handle_cast(_Msg, State) -> {noreply, State}.

handle_info(_Info, State) -> {noreply, State}.
terminate(_Reason, _State) -> ok.  
code_change(_OldVsn, State, _Extra) -> {ok, State}.




%% 创建 Corba 连接 Url
to_URL(IP,Port) ->
	Cp = if is_list(Port) -> Port;
			is_integer(Port) -> integer_to_list(Port);
			true -> "12001"
		end,
	lists:flatten(["corbaname:iiop:1.2@",IP,":",Cp,"/NameService"]).



%% 内部函数：获取EMS Session 工厂
get_EmsSessionFactory(Root) ->
	case 'CosNaming_NamingContext':list(Root, 10) of
		{ok,[{'CosNaming_Binding',NameComponent,_}],_} ->
			NextRoot = 'CosNaming_NamingContext':resolve(Root,NameComponent),
			case NextRoot of
				{'IOP_IOR',"IDL:mtnm.tmforum.org/emsSessionFactory/EmsSessionFactory_I:1.0",_} -> 
					NextRoot;
				_ -> 
					get_EmsSessionFactory(NextRoot)
			end;
		_ -> Root
	end.


%% 内部函数：获取指定数量的报警信息
%~ ,{interceptors, {native,[orber_iiop_tracer_stealth]}},{orber_debug_level, 10}
getAlarms(SdhInfo) ->
	Url = to_URL(SdhInfo#sdhinfo.ip,SdhInfo#sdhinfo.port),
	corba:orb_init([{orbInitRef,Url}]),
	orber:start(),
	case catch corba:string_to_object(Url) of
		{'EXCEPTION', {'CosNaming_NamingContextExt_InvalidAddress',_}} -> 
			orber:stop(),
			{error,"Invalid host address or port"};
		{'EXCEPTION', E} -> 
			orber:stop(),
			{error,E};
		CorbaObj ->
			SessionFactory = get_EmsSessionFactory(CorbaObj),
			NmsSession = nmsSession_NmsSession_I:oe_create(),
			case catch emsSessionFactory_EmsSessionFactory_I:getEmsSession(SessionFactory,SdhInfo#sdhinfo.user,SdhInfo#sdhinfo.password,NmsSession) of
				{'EXCEPTION', {_E1,_E2,'EXCPT_INTERNAL_ERROR',E}} -> 
					orber:stop(),
					{error,E};
				{'EXCEPTION', _} ->
					orber:stop(),
					{error,"GetEmsSession error"};
				{ok,EmsSession} -> case catch emsSession_EmsSession_I:getManager(EmsSession,"EMS") of
						{'EXCEPTION',{_,_,_,E}} ->
							emsSession_EmsSession_I:endSession(EmsSession),
							orber:stop(),
							{error,E};
						{ok,EMS} ->
							io:format("~n~n Get getAllEMSAndMEActiveAlarms ~n~n"),

							%{Time,EMSMgrAlarms} = timer:tc(emsMgr_EMSMgr_I,getAllEMSAndMEActiveAlarms,[EMS,[],['PS_INDETERMINATE'],5000]),
							{Time,EMSMgrAlarms} = timer:tc(emsMgr_EMSMgr_I,getAllEMSAndMEActiveAlarms,[EMS,[],['PS_INDETERMINATE'],1]),

							io:format("~n~n Get Alarms Return time = ~ps~n~n",[(Time/1000/1000)]),
							%io:format("write in file...~n"),
							%writeLog(EMSMgrAlarms),
							
							case EMSMgrAlarms of
								{'EXCEPTION',{_,_,_,E}} -> 
									emsSession_EmsSession_I:endSession(EmsSession),
									io:format("EXCEPTION E...~p~n",[E]),
									orber:stop(),
									{error,E};
								{ok,Alarms,NEventIt} ->
									Atom = 	fun	(X) when is_list(X) -> list_to_atom(X);
												(X) when is_integer(X) -> integer_to_list(X);
												(X) when is_atom(X) -> X;
												(_) -> undefined
									end,
									Alarm_Record = alarms_Format(Alarms,[]),

									GetAll = fun(Sdh,Node,Alarm,Event,Flag,Self,AlertNum,ACC) ->
										Sn = get_Alarm_no(Alarm),
										ID = {Atom(Sdh#sdhinfo.ip),Atom(Sn)},
										case rpc:call(Node,mnesia,dirty_read,[sdh, ID]) of
											[] ->
												case Flag of
													true ->
														io:format("next_n before~n"),
													       % io:format("next_n before Event=~p~n",[Event]),
														{Cu,TAlarm} = notifications_EventIterator_I:next_n(Event,AlertNum),
														io:format("next_n after~n"),
														%io:format("TAlarm=~p~n",[TAlarm]),		
														TRecord = alarms_Format(TAlarm,[]),
														io:format("alarms_Format after~n"),
														TRecord;
													        %Self(Sdh,Node,TRecord,Event,false,Self,AlertNum,Alarm ++ ACC);
													_ -> Alarm ++ ACC
												end;
											_  -> ACC
										end
									end,
									
									Replay = case get_db_node(db) of
										{ok,Node} ->
											{Flag,AlertNum} = case notifications_EventIterator_I:getLength(NEventIt) of 
														Long when Long 
														> 0 ->io:format("getLength Long=~p~n",[Long]), {true,Long} ;
														_ -> {false,0}
													end,
											ReAlarms = GetAll(SdhInfo,Node,Alarm_Record,NEventIt,Flag,GetAll,AlertNum,[]),
											save_alarms(ReAlarms,SdhInfo),
											io:format("save_alarms ...~n"),
											{ok,length(ReAlarms)};
										Err -> Err
									end,
									emsSession_EmsSession_I:endSession(EmsSession),
									orber:stop(),
									Replay;
								Any ->
									emsSession_EmsSession_I:endSession(EmsSession),
									orber:stop(),							
									io:format("EXCEPTION Any...~p~n",[Any]),
									{error,Any}
							end;
						Any ->
							emsSession_EmsSession_I:endSession(EmsSession),
							orber:stop(),
							{error,Any}
					end;
				Any -> 
					orber:stop(),
					{error,Any}
			end
	end.

%% 内部函数：解析报警信息
alarms_Format([],State) -> lists:reverse(State);
alarms_Format([Item|Alarms],State) ->
	case Item of
		{'CosNotification_StructuredEvent',EventType,EventInfo,_} ->
			case EventType of
				{'CosNotification_EventHeader',{'CosNotification_FixedEventHeader',{'CosNotification_EventType',_,Type},_},_} ->
					case Type of
						"NT_TCA" ->
							Alarm = #emsAlarms {eventType = Type,eventInfo = tca_alarms_Info(EventInfo,#tca_eventInfo{})},
							alarms_Format(Alarms,[Alarm|State]);
						"NT_ALARM" ->
							Alarm = #emsAlarms {eventType = Type,eventInfo = alarm_alarms_Info(EventInfo,#alarm_eventInfo{})},
							alarms_Format(Alarms,[Alarm|State]);
						_ -> 
							io:format("~n~n Other Type = ~p~n Other Info = ~p~n",[EventType,EventInfo]),
							alarms_Format(Alarms,State)
					end;
				_ -> alarms_Format(Alarms,State)
			end;
		_ -> io:format("~n~n Other Alarm = ~p~n",[Item]),
			alarms_Format(Alarms,State)
	end.

tca_alarms_Info([],State) -> State;
tca_alarms_Info([Item|Info],State) ->
	NewState = case Item of
		{'CosNotification_Property',"notificationId",{any,_,Value}} -> State#tca_eventInfo {notificationId = Value};
		{'CosNotification_Property',"objectName",{any,_,Value}}		-> State#tca_eventInfo {objectName = alarms_Object(Value,#objectName{})};
		{'CosNotification_Property',"nativeEMSName",{any,_,Value}}	-> State#tca_eventInfo {nativeEMSName = Value};
		{'CosNotification_Property',"objectType",{any,_,Value}}		-> State#tca_eventInfo {objectType = Value};
		{'CosNotification_Property',"emsTime",{any,_,Value}}		-> State#tca_eventInfo {emsTime = Value};
		{'CosNotification_Property',"neTime",{any,_,Value}}			-> State#tca_eventInfo {neTime = Value};
		{'CosNotification_Property',"isClearable",{any,_,Value}}	-> State#tca_eventInfo {isClearable = Value};
		{'CosNotification_Property',"perceivedSeverity",{any,_,Value}}->State#tca_eventInfo{perceivedSeverity=Value};
		{'CosNotification_Property',"layerRate",{any,_,Value}}		-> State#tca_eventInfo {layerRate = Value};
		{'CosNotification_Property',"granularity",{any,_,Value}}	-> State#tca_eventInfo {granularity = Value};
		{'CosNotification_Property',"pmParameterName",{any,_,Value}}-> State#tca_eventInfo {pmParameterName = Value};
		{'CosNotification_Property',"pmLocation",{any,_,Value}}		-> State#tca_eventInfo {pmLocation = Value};
		{'CosNotification_Property',"thresholdType",{any,_,Value}}	-> State#tca_eventInfo {thresholdType = Value};
		{'CosNotification_Property',"value",{any,_,Value}}			-> State#tca_eventInfo {value = Value};
		{'CosNotification_Property',"unit",{any,_,Value}}			-> State#tca_eventInfo {unit = Value};
		{'CosNotification_Property',"additionalInfo",{any,_,Value}}	-> State#tca_eventInfo {additionalInfo = alarms_AdditionalInfo(Value,#additionalInfo{})};
		_ -> State
	end,
	tca_alarms_Info(Info,NewState).

alarm_alarms_Info([],State) -> State;
alarm_alarms_Info([Item|Info],State) ->
	NewState = case Item of
		{'CosNotification_Property',"notificationId",{any,_,Value}} -> State#alarm_eventInfo {notificationId = Value};
		{'CosNotification_Property',"objectName",{any,_,Value}} 	-> State#alarm_eventInfo {objectName = alarms_Object(Value,#objectName{})};
		{'CosNotification_Property',"nativeEMSName",{any,_,Value}} 	-> State#alarm_eventInfo {nativeEMSName = Value};
		{'CosNotification_Property',"nativeProbableCause",{any,_,Value}} -> State#alarm_eventInfo {nativeProbableCause = Value};
		{'CosNotification_Property',"objectType",{any,_,Value}} 	-> State#alarm_eventInfo {objectType = Value};
		{'CosNotification_Property',"emsTime",{any,_,Value}} 		-> State#alarm_eventInfo {emsTime = Value};
		{'CosNotification_Property',"neTime",{any,_,Value}} 		-> State#alarm_eventInfo {neTime = Value};
		{'CosNotification_Property',"isClearable",{any,_,Value}} 	-> State#alarm_eventInfo {isClearable = Value};
		{'CosNotification_Property',"layerRate",{any,_,Value}} 		-> State#alarm_eventInfo {layerRate = Value};
		{'CosNotification_Property',"probableCause",{any,_,Value}} 	-> State#alarm_eventInfo {probableCause = Value};
		{'CosNotification_Property',"probableCauseQualifier",{any,_,Value}} -> State#alarm_eventInfo {probableCauseQualifier = Value};
		{'CosNotification_Property',"perceivedSeverity",{any,_,Value}} -> State#alarm_eventInfo {perceivedSeverity = Value};
		{'CosNotification_Property',"serviceAffecting",{any,_,Value}} -> State#alarm_eventInfo {serviceAffecting = Value};
		{'CosNotification_Property',"affectedTPList",{any,_,Value}} -> State#alarm_eventInfo {affectedTPList = Value};
		{'CosNotification_Property',"additionalText",{any,_,Value}} -> State#alarm_eventInfo {additionalText = Value};
		{'CosNotification_Property',"additionalInfo",{any,_,Value}} -> State#alarm_eventInfo {additionalInfo = alarms_AdditionalInfo(Value,#additionalInfo{})};
		{'CosNotification_Property',"X.733::EventType",{any,_,Value}} -> State#alarm_eventInfo {x733EventType = Value};
		{'CosNotification_Property',"objectTypeQualifier",{any,_,Value}} -> State#alarm_eventInfo {objectTypeQualifier = Value};
		_ -> State
	end,
	alarm_alarms_Info(Info,NewState).

alarms_Object([],State) -> State;
alarms_Object([Item|Info],State) ->
	NewState = case Item of
		{globaldefs_NameAndStringValue_T,"EMS",Value} 				-> State#objectName {ems = Value};
		{globaldefs_NameAndStringValue_T,"ManagedElement",Value}	-> State#objectName {managedelement = Value};
		{globaldefs_NameAndStringValue_T,"PTP",Value} 				-> State#objectName {ptp = Value};
		{globaldefs_NameAndStringValue_T,"CTP",Value} 				-> State#objectName {ctp = Value};
		_ -> State
	end,
	alarms_Object(Info,NewState).

alarms_AdditionalInfo([],State) -> State;
alarms_AdditionalInfo([Item|Info],State) ->
	NewState = case Item of
		{globaldefs_NameAndStringValue_T,"AlarmSerialNo",Value} -> State#additionalInfo {alarmserialno = Value};
		{globaldefs_NameAndStringValue_T,"AlarmReason",Value} 	-> State#additionalInfo {alarmreason = Value};
		{globaldefs_NameAndStringValue_T,"ProductName",Value}	-> State#additionalInfo {productname = Value};
		{globaldefs_NameAndStringValue_T,"EquipmentName",Value}	-> State#additionalInfo {equipmentname = Value};
		{globaldefs_NameAndStringValue_T,"AffirmState",Value} 	-> State#additionalInfo {affirmstate = Value};
		{globaldefs_NameAndStringValue_T,"DetailInfo",Value} 	-> State#additionalInfo {detailinfo = Value};
		_ -> State
	end,
	alarms_AdditionalInfo(Info,NewState).


%~ writelog(Log) ->
    %~ case file:open("alarms.log", [append]) of
        %~ {ok, File} ->
            %~ io:format(File, "~p~n~n", [Log]),
            %~ file:close(File);
         %~ _ -> ok
    %~ end.	



%~ 数据库操作

%% 从数据库中获取最新数据
get_sdh_from_db(SdhInfo) ->
	Atom = 	fun	(X) when is_list(X) -> list_to_atom(X);
				(X) when is_integer(X) -> integer_to_list(X);
				(X) when is_atom(X) -> X;
				(_) -> undefined
	end,
	case get_db_node(db) of
		{ok,Node} ->
			ID = {Atom(SdhInfo#sdhinfo.ip)},
			case rpc:call(Node,mnesia,dirty_read,[sdh, ID]) of
				{badrpc,_} -> {error,"db node down"};
				[] -> 
					rpc:call(Node,mnesia,dirty_write,[sdh, #alarmsInfo {id={Atom(SdhInfo#sdhinfo.ip)},eventType=sdhinfo,alarms=SdhInfo}]),
					{ok,[]};
				[Info] ->
					Sdh = Info#alarmsInfo.alarms,
					RID = Sdh#sdhinfo.lastrecordid,
					io:format("~n~n === Sdh_monitor get data === ~p~n~n",[RID]),
					case RID of
						undefined -> {ok,[]};
						[]		  -> {ok,[]};
						{}		  -> {ok,[]};
						_ ->
							case rpc:call(Node,mnesia,dirty_read,[sdh, RID]) of
								[] -> {ok,[]};
								[Ai] -> {ok,Ai#alarmsInfo.alarms}
							end
					end
			end;
		Err -> Err
	end.

%% 轮循数据
polling_data() ->
	case query_alarms([{eventType,'==',sdhinfo}]) of
		{ok,SdhInfo} when SdhInfo =/= [] -> 
			PollingFun = fun([],_) -> [];
							([Item|Items],Fun) ->
								case Item#sdhinfo.ip =/= [] of
									true ->
										io:format("~n~n polling data : Server = ~s, Port = ~s~n~n",[Item#sdhinfo.ip,Item#sdhinfo.port]),
										case getAlarms(Item) of
											{error,Err} -> io:format("~n~n ===polling data info=== ~n~p (Server = ~s:~s)~n~n",[Err,Item#sdhinfo.ip,Item#sdhinfo.port]);
											{ok,Log} -> io:format("~n~n ===polling data info=== ~n Get Sdh alarms count : ~p (Server = ~s:~s)~n~n",[Log,Item#sdhinfo.ip,Item#sdhinfo.port]);
											_ -> ok
										end,
										Fun(Items,Fun);
									_ ->
										Fun(Items,Fun)
								end
						end,
			PollingFun(SdhInfo,PollingFun);
		_ -> io:format("~nNo Sdh Server!~n")
	end,
	timer:sleep(timer:minutes(5)), %old 1
	polling_data().

%% 保存报警记录到数据库
save_alarms(Alarms,SdhInfo) when is_record(Alarms,emsAlarms) -> save_alarms([Alarms],SdhInfo);
save_alarms([],_) -> ok;
save_alarms([Alarm|Alarms],SdhInfo) ->
	case get_db_node(db) of
		{ok,Node} ->
			case save_sdh_data(Node,SdhInfo,Alarm) of
				true -> save_alarms(Alarms,SdhInfo);
				_	 -> ok
			end;
		Err -> Err
	end.

%% 查询数据
%% 
%% 可查询字段: sdhserver, eventType, equipmentname, productname, emsTime, perceivedSeverity
%%     sdhserver 		 服务器地址
%%     eventType 		 报警类型 <NT_ALARM,NT_TCA>
%%     equipmentname 	 产生告警的设备名称
%%     productname 		 产生告警的设备类型
%%     emsTime			 EMS上报事件的时间值
%%     perceivedSeverity 告警级别（包括 PS_CRITICAL、PS_MAJOR、PS_MINOR、PS_WARNING四个），对于清除告警，其告警级别为 PS_CLEARED 对于NT_TCA类型告警级别为 PS_INDETERMINATE
%%
%% Query 参数格式: [{sdhserver,'==',value},{eventType,'>',value},...]
%%
%% 可能操作符: ==,/=,>,>=,<,=<
query_alarms(Query) ->
	case get_db_node(db) of
		{ok,Node} ->
			case create_sdh_db(Node) of
				true ->
					[Match,Guard] = format_query(Query,#alarmsInfo{alarms='$0', _='_'},[]),
					case rpc:call(Node,mnesia,dirty_select,[sdh,[{Match,Guard,['$0']}]]) of
						{badrpc,_}	-> {error,"db node down"};
						Alarms		-> {ok,Alarms}
					end;
				_	-> {error,"db node down"}
			end;
		Err -> Err
	end.
	
%% 格式化查询条件
format_query([],Match,Guard) -> [Match,Guard];
format_query(Query,Match,Guard) when is_tuple(Query) ->  format_query([Query],Match,Guard);
format_query([Q|QS],Match,Guard) ->
	[M,G] = case Q of
		{sdhserver,Op,Va} 			-> [Match#alarmsInfo {sdhserver='$1'},[{Op,'$1',Va}|Guard]];
		{eventType,Op,Va} 			-> [Match#alarmsInfo {eventType='$2'},[{Op,'$2',Va}|Guard]];
		{equipmentname,Op,Va} 		-> [Match#alarmsInfo {equipmentname='$3'},[{Op,'$3',Va}|Guard]];
		{productname,Op,Va} 		-> [Match#alarmsInfo {productname='$4'},[{Op,'$4',Va}|Guard]];
		{emsTime,Op,Va} 			-> [Match#alarmsInfo {emsTime='$5'},[{Op,'$5',Va}|Guard]];
		{perceivedSeverity,Op,Va} 	-> [Match#alarmsInfo {perceivedSeverity='$6'},[{Op,'$6',Va}|Guard]];
		_	-> [Match,Guard]
	end,
	format_query(QS,M,G);
format_query(_,Match,Guard) -> [Match,Guard].

%% 获取报警记录的编号
get_Alarm_no(Record) when is_list(Record) -> [AI] = Record, get_Alarm_no(AI);
get_Alarm_no(Record) ->
	case Record#emsAlarms.eventType of
		"NT_ALARM" 	-> ((Record#emsAlarms.eventInfo)#alarm_eventInfo.additionalInfo)#additionalInfo.alarmserialno;
		"NT_TCA"	->	
						Et = (Record#emsAlarms.eventInfo)#tca_eventInfo.emsTime,
						Ets = string:tokens(Et,"."),
						element(1,list_to_tuple(Ets));
		_ 			-> calendar:datetime_to_gregorian_seconds(calendar:local_time())
	end.

%% 获取数据库节点名称
get_db_node() ->
	{ok,Hostname} = inet:gethostname(),
	Node = list_to_atom("db@"++Hostname),
	case net_adm:ping(Node) of
		pang -> {error,"Db node down"};
		pong -> {ok,Node}
	end.

get_db_node(Key) ->
	Conf = "../../conf/nodes.conf",
	case file:consult(Conf) of
		{ok,Data} ->
			Fun = fun([],_) -> get_db_node();
					([Item|Items],Self) ->
						case Item of
							{Key,Node} -> {ok,Node};
							_ -> Self(Items,Self)
						end
				end,
			Fun(Data,Fun);
		_ -> get_db_node()
	end.


%% 在数据库节点上创建数据表
create_sdh_db(Node) ->
	case rpc:call(Node,cache,get,[fragments, sdh]) of
		undefined	->
			case rpc:call(Node,mnesia,create_table,[sdh,[{disc_copies, [Node]}, {index, [equipmentname,emsTime]}, {record_name, alarmsInfo}, {attributes, record_info(fields, alarmsInfo)}]]) of
				{atomic, ok} ->	
					 rpc:call(Node,cache,set,[fragments, sdh, sdh]),
					 true;
				_ -> false
			end;
		{badrpc,_}	-> false;
		sdh	 -> true;
		_	 -> false
	end.

%% 保存报警到数据库
save_sdh_data(Node,Sdh,Record) ->
	Atom = 	fun	(X) when is_list(X) -> list_to_atom(X);
				(X) when is_integer(X) -> list_to_atom(integer_to_list(X));
				(X) when is_atom(X) -> X;
				(_) -> undefined
	end,
	case create_sdh_db(Node) of
		true ->
			ID = {Atom(Sdh#sdhinfo.ip),Atom(get_Alarm_no(Record))},
			case rpc:call(Node,mnesia,dirty_read,[sdh, ID]) of
				[]	->	
						Document = alarm_to_db(Sdh#sdhinfo.ip,ID,Record#emsAlarms.eventType,Record),
						case rpc:call(Node,mnesia,dirty_write,[sdh, Document]) of
							ok	->	rpc:call(Node,mnesia,dirty_write,[sdh, #alarmsInfo {id={Atom(Sdh#sdhinfo.ip)},eventType=sdhinfo,alarms=Sdh#sdhinfo{lastrecordid=ID}}]),
									true;
							_	->	false
						end;
				_	-> 	true
			end;
		_	 -> false
	end.

%% 格式化报警记录为数据记录
alarm_to_db(Sdh,ID,Type,Alarm) ->
	case Type of
		"NT_ALARM" 	->
			#alarmsInfo {
				id 				= ID,
				sdhserver		= Sdh,
				eventType		= Type, 
				equipmentname	= ((Alarm#emsAlarms.eventInfo)#alarm_eventInfo.additionalInfo)#additionalInfo.equipmentname,
				productname		= ((Alarm#emsAlarms.eventInfo)#alarm_eventInfo.additionalInfo)#additionalInfo.productname,
				emsTime			= (Alarm#emsAlarms.eventInfo)#alarm_eventInfo.emsTime,
				perceivedSeverity = (Alarm#emsAlarms.eventInfo)#alarm_eventInfo.perceivedSeverity, 
				alarms			= Alarm
			};
		"NT_TCA"	->
			#alarmsInfo {
				id 				= ID,
				sdhserver		= Sdh,
				eventType		= Type, 
				equipmentname	= ((Alarm#emsAlarms.eventInfo)#tca_eventInfo.additionalInfo)#additionalInfo.equipmentname,
				productname		= ((Alarm#emsAlarms.eventInfo)#tca_eventInfo.additionalInfo)#additionalInfo.productname,
				emsTime			= (Alarm#emsAlarms.eventInfo)#tca_eventInfo.emsTime,
				perceivedSeverity = (Alarm#emsAlarms.eventInfo)#tca_eventInfo.perceivedSeverity, 
				alarms			= Alarm
			};
		_ 			-> 
			#alarmsInfo {
				id 				= ID,
				sdhserver		= Sdh,
				eventType		= Type, 
				alarms			= Alarm
			}
	end.
