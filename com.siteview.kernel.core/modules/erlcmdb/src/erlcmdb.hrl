-record(query_condition,{	id,	type,	maxResult,	firstResult,	conditions=[],	attributeAlias=[],	orderby	}).	-record(erlcmdb_ci,{	type,	parent,	attributes=[]	}).-record(ci_attribute,{	name,	type	}).	