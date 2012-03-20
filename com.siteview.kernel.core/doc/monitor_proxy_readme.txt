配置：
1.将服务端文件复制到新的主机。
2.配置Server.conf文件。
例子如下：
{serverID,'0'}.
{release_date,"03/01/2010"}.
{master_node,'debug@HOST'}.
{dbNode,'sv3ren@HOST'}.
{proxy_sync,[
	{dir,"templates.solutions",10},
	{dir,"templates.sets",10},
	{dir,"templates.mail",10},
	{dir,"templates.sms",10},
	{dir,"templates.applications",10}
	]}.

master_node--指定主节点名。
dbNode --指定数据库节点名。
proxy_sync--指定同步的文件和目录，{dir,"templates.solutions",10} 指定了同步目录“templates.solutions”，同步的时间间隔是10分钟。

3.监测器代理节点不能跟主节点在同一台机器上。
4.通过sveccmonproxy.bat脚本启动监测器代理。