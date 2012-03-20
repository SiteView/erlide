%数据匹配类型
-define(EQUALS, 0).
-define(GREATER_THAN, 1)
-define(LESS_THAN, 2).
-define(GREATER_THAN_OR_EQUAL, 3).
-define(LESS_THAN_OR_EQUAL, 4).
-define(LIKE, 5).
-define(CONTAINS, 6).
-define(IS_NULL, 7).

%attribute类型


%数据查询结构
-record(page_info, {start=0, max=0}).

-record(ci_filter, {item_offspring_selector=[]}).

-record(graph_query, {item_offspring_selector=[], item_alias_selector=[], item_relation_selector=[]}).

%attribute_value_selector有两种用法
%1、直接使用attribute_value_constraint，但只能加一个约束
%2、使用多个item_and_group_contraint，item_or_group_contraint嵌套
% page_info：page_info的记录

% limit_to_child：当为true时只有直接继承于template_alias的ci会被返回
% match_template：true时只有满足条件的template会被返回
-record(item_offspring_selector, {id, template_alias, page_info, attribute_value_selector, limit_to_child=false, match_template=false}).

%mandatory：true时只有包含这种关系的Items会被返回
-record(item_relation_selector, {id, template_alias, page_info, attribute_value_selector=[], target, source, mandatory=false}).

%alias：ci的别名
%aliases：别名集
-record(item_alias_selector, {id, template_alias, page_info, attribute_value_selector=[], alias, aliases=[]}).


%%item_and_group_contraint,item_or_group_contraint中可包含本身或另外的约束，为嵌套结构
%在此组中的attribute约束均为与关系 &&
-record(item_and_group_contraint, {attribute_value_constraints=[]}).

%在此组的attribute约束均为或关系  ||
-record(item_or_group_contraint, {attribute_value_constraints=[]}).

-record(attribute_value_constraint, {alias, operation, value, value_type}).





