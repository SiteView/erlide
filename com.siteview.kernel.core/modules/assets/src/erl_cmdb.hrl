%����ƥ������
-define(EQUALS, 0).
-define(GREATER_THAN, 1)
-define(LESS_THAN, 2).
-define(GREATER_THAN_OR_EQUAL, 3).
-define(LESS_THAN_OR_EQUAL, 4).
-define(LIKE, 5).
-define(CONTAINS, 6).
-define(IS_NULL, 7).

%attribute����


%���ݲ�ѯ�ṹ
-record(page_info, {start=0, max=0}).

-record(ci_filter, {item_offspring_selector=[]}).

-record(graph_query, {item_offspring_selector=[], item_alias_selector=[], item_relation_selector=[]}).

%attribute_value_selector�������÷�
%1��ֱ��ʹ��attribute_value_constraint����ֻ�ܼ�һ��Լ��
%2��ʹ�ö��item_and_group_contraint��item_or_group_contraintǶ��
% page_info��page_info�ļ�¼

% limit_to_child����Ϊtrueʱֻ��ֱ�Ӽ̳���template_alias��ci�ᱻ����
% match_template��trueʱֻ������������template�ᱻ����
-record(item_offspring_selector, {id, template_alias, page_info, attribute_value_selector, limit_to_child=false, match_template=false}).

%mandatory��trueʱֻ�а������ֹ�ϵ��Items�ᱻ����
-record(item_relation_selector, {id, template_alias, page_info, attribute_value_selector=[], target, source, mandatory=false}).

%alias��ci�ı���
%aliases��������
-record(item_alias_selector, {id, template_alias, page_info, attribute_value_selector=[], alias, aliases=[]}).


%%item_and_group_contraint,item_or_group_contraint�пɰ�������������Լ����ΪǶ�׽ṹ
%�ڴ����е�attributeԼ����Ϊ���ϵ &&
-record(item_and_group_contraint, {attribute_value_constraints=[]}).

%�ڴ����attributeԼ����Ϊ���ϵ  ||
-record(item_or_group_contraint, {attribute_value_constraints=[]}).

-record(attribute_value_constraint, {alias, operation, value, value_type}).





