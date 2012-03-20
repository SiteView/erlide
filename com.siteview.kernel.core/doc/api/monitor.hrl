-ifndef(_MONITOR_H_).
-define(_MONITOR_H_,1).

-include("log.hrl").

-define(MAX_MONITORS_SKIPS,6).
-define(ID_SEPARATOR,"/").
-define(DEPENDS_PREFIX,"depends ").
-define(MIN_GROUP_INTERVAL,10).
-define(MIN_MONITOR_INTERVAL,15).

-define(RULES,rules).

-define(ID,id).
-define(CLASS,class).
-define(NAME,name).
-define(DESCRIPTION,description).
-define(FREQUENCY,frequency).
-define(ERROR_FREQUENCY,error_frequency).
-define(PARENT,parent).
-define(TITLE,title).

-define(LAST_UPDATE,last).
-define(FORCE_REFRESH,force_refresh).
-define(MONITOR_DELAY_BETWEEN_REFRESH,monitor_delay_between_refresh).
-define(INITIAL_MONITOR_DELAY,initial_monitor_delay).
-define(DISABLED_DESCRIPTION,disabled_description).
-define(DISABLED,disabled).
-define(RUN_OWNRULES,run_ownrules).
-define(NO_DATA,nodata).
-define(CATEGORY,category).
-define(LAST_CATEGORY,last_category).
-define(GETHOSTNAME,gethostname).

-define(MEASUREMENT,measurement).
-define(MEASUREMENT_PROPERTY,measurement_property). %%标识一个属性是measurement

-define(SAMPLE,sample).
-define(VERFIY_ERROR,verfiy_error).
-define(STATE_STRING,state_string).
-define(DEPENDS_ON,depends_on).
-define(DEPENDS_CONDITION,depends_condition).
-define(OPERATION_ERROR_CODE,operation_error_code).
-define(OPERATION_ERROR_MESSAGE,operation_error_message).
-define(ALERT_DISABLED,alert_disabled).
-define(ALERT_DISABLED_DESCRIPTION,alert_disabled_description).

-define(GOOD_CATEGORY,good).
-define(WARNING_CATEGORY,warning).
-define(ERROR_CATEGORY,error).

-define(TIMED_DISABLE,timed_disable).

-define(NTCounterSummaryMax,3).

-define(MAX_RECENT_MONITOR,20).

-define(APP,app_).

%%templates path
-define(TEMPLATES_PERFMON,"templates.perfmon").


%%browsable monitor 
-define(BROWSE_COUNTER,"browser_counter").
-define(BROWSE_SPLIT,"@").

% define page parameter name
-define(PAGE_PARAMS,'_PAGE_PARAMS').

-define(DEFAULT_UPAMOUNT,"5").

-record(machine,{id,
				 name="",
				 host="",
				 login="",
				 passwd="",
				 trace="0",
				 os="nt",
				 status="unknown",
				 method="",    			%%以上为NT/Unix共用的选项，中间部分为Unix选项
				 prompt="#",
				 loginprom="login",
				 passwdprom="password",
				 secondprom="",
				 secondresp="",
				 initshell="",
				 remoteencoding="",
				 sshcommand="",    		%%以下为SSH Advance选项
				 sshclient="interJavalib",
				 sshport=22,
				 disableconncaching="0",
				 connlimit=3,
				 version="",
				 keyfile="",
				 sshauthmethod="",
                 label="",      %% 标签
                 total=0        %% 设备统计
				}).

%% machine 标签
-record(machine_label,{
                                   id,
                                   name="undefined",  %label name
                                   type="nt",         %标签类型
                                   index,   %引用计数 
                                   syslabel="false" %% 是否为系统标签
                                   }).

-define(MachLabelGroup, "machine_label").     %% 远程服务器, unix标签组标示
-define(MachUnixLabel, "unix").     %% 远程服务器, unix标签组标示
-define(MachNTLabel, "nt").       %% 远程服务器, nt标签组标示
-define(DefMachNTLabel, "nt_undefined").       %% 默认的远程服务器, nt标签组标示
-define(DefMachUnixLabel, "unix_undefined").     %% 远程服务器, unix标签组标示
-define(DefMachNTLabelName, "undefined").       %% 默认的远程服务器, nt标签组标示
-define(DefMachUnixLabelName, "undefined").     %% 远程服务器, unix标签组标示

-record(tr069_device,{
                                    ip,                     %设备IP
                                    %deviceid,       %设备id                                    
			                        manufacturer, %厂商
                                    oui,                   %organizationally unique identifier
                                    productclass,   %设备类型
                                    serialnumber,  %序列号
                                    profile = "",             %profile字符串                                     
                                    deviceport = "7547",     %设备端口
                                    authtype = "Basic",        %认证方式
                                    user = "",               %认证用户名
                                    password = "",     %认证密码
                                    keyfile = "",          %ssh认证证书                           
                                    acsip = "",     %所属acs的IP
                                    acsname = "",      %所属acs名
                                    timestamp,  %时间戳
                                    keepalive = "1",      %是否启用心跳
                                    keepalivetime = "15", %心跳时间间隔                                     
                                    state = "alive",             %设备状态
                                    label= [],      %设备标签
                                    description= "",
                                    total=0                                    
                                    }).
                                    
 
-record(tr069_label,{
                                   name,  %label name
                                   index   %引用计数 
                                   }).
                                   
%% 查询条件通用数据结构(由接口使用)
-record(query_condition1,{
                                where=[],
                                index=0,
                                count=0,
                                sort=[],
                                sortType="@D"
                        }).
                        
%% 查询条件通用数据结构(中间格式，与dbecc接口)
-record(query_beam_condition1,{
                                where=[],
                                order=[]
                        }).

%% 查询条件通用数据结构(中间格式，Where条件)
-record(query_condition_where,{
                                where=[]        %%([{field,operation,value,relation}|T])
                        }).
                                   
-record(tr069_label_id,{
                                   number %
                                   }).                                   

-record(tr069_paramcaches,{
                                deviceid=[],       %%所属设备id
                                paramname=[],      %%参数名
                                value=[],          %%参数值
                                is_paracache_table="true",     %%是否为参数缓存的字段
                                updatetime=0        %%缓存参数修改的时间
                        }).

%% 每个设备所对应的所有参数集合
-record(tr069_paramcachesdev,{
                                id=[],      %% id
                                params=[]   %% 参数集合[{paramname,#tr069_paramcaches{}}|T]
                        }).

-record(tr069_download,{
            deviceid, %设备id
            commandkey,%标识本次升级的字符串
            startTime,   %升级开始时间 
            completeTime,%升级完成时间
            state %升级状态，分别为complete,upgrading,failure
          }).
          
%% 站点
-record(tr069_upgradesite,{
                                devaccessaddr=[],       %%设备访问地址
                                clientaccessaddr=[],      %%用户访问地址
                                username=[],          %%用户名
                                password=[],     %%密码
                                protocol=[]     %%协议
                        }).

%% 升级设置
-record(tr069_upgradeset,{
                                manufacturer=[],       %%厂商
                                productclass=[],      %%设备类型
                                version=[],          %%版本
                                filename=[],     %%文件名
                                is_rightrow="true", %%是否立即
                                begintime=[],          %%开始时间
                                devices=[]             %%待升级的设备
                        }).

%% 升级文件
-record(tr069_upgradefile,{
                                manufacturer=[],       %%厂商
                                productclass=[],      %%设备类型
                                version=[],          %%版本
                                filename=[],     %%文件名
                                filetype=[],     %%文件类型
                                filesize="0",       %%文件大小
                                is_upgradefile = "true" %%是否为升级文件
                        }).

%% 升级状态
-record(tr069_upgradestatus,{
                                commandkey,        %%commandkey 
                                devid = [],         %%设备id
                                upgradestatus=[],   %%升级状态
                                version=[],             %%现有版本
                                datetime=[],               %%时间
                                begindatetime=[],          %%升级开始时间
                                enddatetime=[],             %%升级结束时间
                                ip=[],  %%ip
                                mac=[],     %% 序列号
                                group=[],   %% 归属组
                                manufacturer, %厂商
                                oui,                   %organizationally unique identifier
                                productclass,   %设备类型
                                anothorname,    %% 别名
                                total = 0           %% 数量
                        }).
                        
%% 设备同时升级数量
-record(tr069_upgradeamount,{
                                id=[],
                                amount=?DEFAULT_UPAMOUNT  %% 同时升级5台
                        }).

-record(realtimealarm,{id,manufacturer,oui,productclass,serialnumber,xevent,description="",statu="untreated",notes="",timestamp,number=1,timeofoccurrence,total=0}).

-record(schedule,{id,name,type,days=[]}). %%type="range","absolute"

-record(weekday,{enabled=true,day,time_start,time_end}).

%%browsable 监测器的counter
-record(counters,{id,name,default=false}).

-record(monitorlog,{id,name,time,category,desc,measurement}).

-record(perf_counter,{object,counterName,instance,objectID,counterID}).

-record(perf_result,{counterType,measurement,baseMeasurement,lastMeasurement,lastBaseMeasurement,measurementFreq,measurementTicks,measurementTime,lastMeasurementTime,lastMeasurementTicks,percent,perSec,precision,value}).

%%settings
-record(additional_email_settings,
		{name,						% 名字
		email,						% email 地址
		disable="false",			% 是否禁止
		template,					% 模版
		schedule					% shedule [{1,"enabled",{"20:00","24:00"}}]
		}).

-record(additional_snmp_settings,
		{
		name,
		disable,
		snmp_host,
		snmp_objectid,
		snmp_objectid_other,
		snmp_community,
		snmp_generic,
		snmp_specific,
		snmp_trap_version
		}).

% monitor set template
-record(monitor_set,{title,desc,variable,monitors}).

% dynamic update
-record(dynamic_update_sets,{id,
							mset,				% Select the Monitor Set Template used to create new monitors.
							subgroup_name,		% Monitor group name to be assigned to each set of monitors created using the above template.
							group_name,			% name for the group to be created to contain all subgroups created using the template set.
							parent_group,		% an existing SiteScope group to which the above container group will be added as a subgroup.
							update_frequency,	% Frequency (in seconds) that SiteScope will query the MIB or database for new nodes and create monitor sets for new nodes.
							exclude_ip,			% Enter IP addresses to be excluded from the Update Set, for example, the default gateway IP. 
							title,				% Optional title for this Dynamic Update Set. The default title is the server address
							snmp_search,		% SNMP MIB Search (it is record 'snmp_mib_search')
							database_search		% Database Search (it is record 'database_search')
							}).
							
-record(snmp_mib_search,{server_address,snmp_oid,snmp_oid_other,pool_included,snmp_community}).
-record(database_search,{url,driver,sql,username,password,connect_timeout,query_timeout}).

%%URL Montor
-record(urlresults,{status=-1,totalDuration=0,totalBytes=0,lastModified=0,currentDate=0,body="",head=[],errorMessage="",contentBuffer="",redirectBuffer="",sessionBuffer=""}).

%%
-define(URLok, 200).
-define(kURLok, 200).
-define(kXMLElementNotFoundError, -986).
-define(kXMLFormatError, -987).
-define(kXMLElementMatchError, -988).
-define(kURLContentErrorFound, -989).
-define(kURLContentElementMissing, -990).
-define(kURLNoStatusError, -991).
-define(kMonitorSpecificError, -992).
-define(kURLNoRouteToHostError, -993).
-define(kDLLCrashedError, -994).
-define(kURLContentChangedError, -995).
-define(kURLTimeoutError, -996).
-define(kURLBadHostNameError, -997).
-define(kURLNoConnectionError, -998).
-define(kURLContentMatchError, -999).
-define(kURLUnknownError, -1000).
-define(kDNSIPAddressMismatch, -1001).
-define(kURLRemoteMonitoringError, -1002).
-define(kURLSecurityMismatch, 12157).
-define(kURLCertificateExpired, 12037).
-define(kURLCertificateNameError, 12038).
-define(kURLMissingClientCertificate, 12044).
-define(kURLInvalidCA, 12045).

-define(statusMapping,[
                {200,"ok"},
                {201,"created"},
                {202,"accepted"},
                {203,"non-authoratative"},
                {204,"no content"},
                {205,"reset content"},
                {206,"partial content"},
                {301,"document moved"},
                {302,"document moved"},
                {303,"document moved"},
                {307,"document moved"},
                {400,"bad request"},
                {401,"unauthorized"},
                {403,"forbidden"},
                {404,"not found"},
                {407,"proxy authentication required"},
                {500,"server error"},
                {501,"not implemented"},
                {502,"proxy received invalid response"},
                {503,"server busy"},
                {504,"proxy timed out"},
                {-992,"error"},
                {-990,"content element missing"},
                {-995,"content changed"},
                {-996,"timed out reading"},
                {-987,"XML format error"},
                {-986,"XML element not found"},
                {-988,"XML element value mismatch"},
                {-999,"content match error"},
                {-989,"content error found"},
                {-998,"unable to connect to server"},
                {-997,"unknown host name"},
                {-994,"internal error in WinInet library"},
                {-993,"unable to reach server"},
                {-991,"no status in reply from server"},
                {-1001,"ip address does not match"},
                {12157,"insufficient encryption, probably needs 128 bit Internet Explorer"},
                {12037,"secure certificate expired"},
                {12038,"secure certificate name does not match host name"},
                {12044,"requires client certificate authentification"},
                {12045,"certificate authority not registered in Internet Explorer"},
                {-1002,"unable to connect to remote monitoring server"}
                ]).
				
-record(operation_log,{id,create_time,record_state,user_id,operate_time,operate_type,operate_objname,operate_objinfo,dstr}).

-endif.
