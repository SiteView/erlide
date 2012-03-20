
-record(property,{
			name,
			title,
			type=text, %%属性的类型(scalar,frequency,server,text,bool,numeric,schedule,check_group,password,hidden)
			description="",
			order=99,
			editable=true,
			configurable=true,
			advance=false,
			state=false,
			optional=false,
			default="",
			allowother=false, %%是否允许scalar属性带有other的输入框
			listSize=1,			%%scalar属性使用
			multiple=false,		%%scalar属性使用，是否多选
			upIsBad=true,     %%用于baseline, 是否值越大越坏, 比如:占用率越大就越坏, 但剩余空间越大就越好
			baselinable=false,     %%用于baseline功能，有些数字类型比如 http返回值 200 或 400，就不可以进行基线化
			properties=[]
			}).

-record(property_category,{
			name,		%%属性名字
			title,		%%属性标题
			properties	%%属性(list)
			}
		).
