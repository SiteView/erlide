-record(mail_alert,{
		sendto,				%%收件人
		other="",			%%其他地址
		template="Default"	%%邮件模板
		}).

-record(sms_alert,{
		phone,
		type,				%%发送短信的方式
        url,                  %%web方式发送短信的url 
        username,     %%web 发送短信的用户名
        password,      %%web发送短信密码
        other="",
		template="Default",	%%模板
		cd					%%发送参数
		}).


-record(script_alert,{
		server,				%%服务器
		script,				%%脚本
		params,				%%参数
		template="Default"	%%模板
		}).

-record(snmptrap_alert,{
		msgprefix,			%%消息前缀
		trapto,				%%目标
		template="Default"	%%模板
		}).

-record(sound_alert,{
		file				%%文件
		}).
        
-record(syslog_alert,{
        host,
        port,
        facility,
        program,
        level,
        template="Default"
        }).

-record(database_alert,{
		url,
		sql,
		dbuser,
		dbpasswd,
		dbdriver,
		dbBackup
		}).

-record(disable_alert,{
		oper,				%%操作disable/enable
		target,				%%目标，list()
        distarget           %%显示目标
		}).

-record(eventlog_alert,{
		template="Default"	%%模板
		}).

-record(post_alert,{
		url,				%%post form url
        proxy,
        challengeResponse,
        username,
        password,
        proxyUserName,
        proxyPassword,
		template="Default"	%%模板
		}).


-record(alertlog,{id,type,name="",monitor,receiver="",title="",time,result="",content="",measurement}).