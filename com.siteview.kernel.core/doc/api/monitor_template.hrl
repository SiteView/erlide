
-record(property,{
			name,
			title,
			type=text, %%���Ե�����(scalar,frequency,server,text,bool,numeric,schedule,check_group,password,hidden)
			description="",
			order=99,
			editable=true,
			configurable=true,
			advance=false,
			state=false,
			optional=false,
			default="",
			allowother=false, %%�Ƿ�����scalar���Դ���other�������
			listSize=1,			%%scalar����ʹ��
			multiple=false,		%%scalar����ʹ�ã��Ƿ��ѡ
			upIsBad=true,     %%����baseline, �Ƿ�ֵԽ��Խ��, ����:ռ����Խ���Խ��, ��ʣ��ռ�Խ���Խ��
			baselinable=false,     %%����baseline���ܣ���Щ�������ͱ��� http����ֵ 200 �� 400���Ͳ����Խ��л��߻�
			properties=[]
			}).

-record(property_category,{
			name,		%%��������
			title,		%%���Ա���
			properties	%%����(list)
			}
		).
