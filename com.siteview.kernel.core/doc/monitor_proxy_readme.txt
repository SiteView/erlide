���ã�
1.��������ļ����Ƶ��µ�������
2.����Server.conf�ļ���
�������£�
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

master_node--ָ�����ڵ�����
dbNode --ָ�����ݿ�ڵ�����
proxy_sync--ָ��ͬ�����ļ���Ŀ¼��{dir,"templates.solutions",10} ָ����ͬ��Ŀ¼��templates.solutions����ͬ����ʱ������10���ӡ�

3.���������ڵ㲻�ܸ����ڵ���ͬһ̨�����ϡ�
4.ͨ��sveccmonproxy.bat�ű��������������