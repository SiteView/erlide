%% 
%% @doc api function of monitor template
%% @version{1.0}
%% @copyright 2009 dragonflow.com
%% @author Shi xianfang <xianfang.shi@dragonflow.com>

-module(api_monitor_template).
-extends(api_siteview).
-compile(export_all).
-include("siteview.hrl").
-include("monitor_template.hrl").

-export([get_templates/0,get_templates_beta/0,get_templates_release/0,get_template/1, get_template/2, writeobjtypexml/1, writeentityxml/1,write_template_ofbizs/0,write_atomtemplate_ofbizs/0, write_templates/0,writemoitorerls/0]).

-export([delete_template/1,get_template_name/1,get_scalar_property/3,get_classifier/2,get_monitor_counters/2]).

-export([get_template_state/2,get_template_state/3,get_servers/1,get_template_type/1]).

%% @spec get_templates()->[Temp]
%% where
%%	Temp = {Key,Name,Options}
%%	Key = atom()
%%	Name = string()
%%	Options =list()
%% @doc get list of monitor template
get_templates()->
	 case file:consult("conf/monitor_template.conf") of
		{ok,R}->
			F = fun(X,Y)->element(2,X)<element(2,Y) end,
			lists:sort(F,R);
		_->
			[]
	end.

%% @spec get_templates_beta()->[Temp]
%% where
%%	Temp = {Key,Name,Options}
%%	Key = atom()
%%	Name = string()
%%	Options =list()
%% @doc get list of beta monitor 
get_templates_beta()->
	 case file:consult("conf/monitor_template.conf") of
		{ok,R}->
			F = fun(X)->
				lists:member(beta,element(3,X))
			end,
			lists:filter(F,R);
		_->
			[]
	end.

write_templates()->
	L = get_templates(),
	F = fun(X) -> 
		{Name, Des, Other} = X,
		Prop = [{m_templatename ,Name},{m_title ,Des},{m_other, Other}],
%% 			io:format("write_template_ofbiz props:~p~n", Prop),
			ofbiz:callservice("createMonitorTemplates",Prop)
	end,	
	lists:foreach(F, L).

%% @spec get_templates_release()->[Temp]
%% where
%%	Temp = {Key,Name,Options}
%%	Key = atom()
%%	Name = string()
%%	Options =list()
%% @doc get list of release monitor 
get_templates_release()->
	 case file:consult("conf/monitor_template.conf") of
		{ok,R}->
			F = fun(X)->
				not lists:member(beta,element(3,X) )
			end,
			lists:filter(F,R);
		_->
			[]
	end.

%% @spec get_template(Key)->([#property{}] | {error,Reason})
%% where
%%	Key = atom()
%%	Reason = atom()
%% @doc get detail information of monitor template by key
%%	<br>return list of #property{}</br>
%%	<br>#property{} is defined in <a href="monitor_template.hrl">monitor_template.hrl</a></br>
get_template(Key) when is_atom(Key)->
	M = Key:new(),
	try
	Ret = M:get_template_property(),
	Ret
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end;
get_template(_)->{error,parameter_error}.

write_atomtemplate_ofbizs()->
	write_atomtemplate_ofbiz(memory_monitor).

%% @spec get_template(Key)->([#property{}] | {error,Reason})
%% where
%%	Key = atom()
%%	Reason = atom()
%% @doc get detail information of monitor template by key
%%	<br>return list of #property{}</br>
%%	<br>#property{} is defined in <a href="monitor_template.hrl">monitor_template.hrl</a></br>
write_atomtemplate_ofbiz(Key) when is_atom(Key)->
	M = Key:new(),	
	try
	Ret = M:get_template_property(),	
	Sps = [P || P<-Ret,lists:member(P#property.name, [name, frequency, disabled, verfiy_error, error_frequency, depends_condition, depends_on, schedule, activate_baseline, proxy, error_classifier,
		 warning_classifier, good_classifier])==true],
%% 	io:format("write_template_ofbiz props: ~p~n", [Sps]),
	F = fun(X) -> 
			Prop = [{m_templatename ,atom_to_list(atomic_monitor)},{m_name ,X#property.name},
			{m_title ,X#property.title}, 
			{m_type ,X#property.type}, 
			{m_description ,X#property.description}, 
			{m_order ,X#property.order}, 
			{m_editable ,X#property.editable}, 
			{m_configurable ,X#property.configurable},
			{m_advance ,X#property.advance},
			{m_state ,X#property.state}, 
			{m_optional ,X#property.optional}, 
			{m_default ,X#property.default}, 
			{m_allowother ,X#property.allowother},
			{m_listSize ,X#property.listSize},
			{m_multiple ,X#property.multiple},
			{m_upIsBad ,X#property.upIsBad},
			{m_baselinable ,X#property.baselinable},
			{m_primarystate ,X#property.primarystate},
			{m_properties ,X#property.properties}],
%% 			io:format("write_template_ofbiz props:~p~n", Prop),
			ofbiz:callservice("createMoitorTemplate",Prop)
	end,	
	lists:foreach(F, Sps)
	
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end;
write_atomtemplate_ofbiz(_)->{error,parameter_error}.

write_template_ofbizs()->
	L = get_templates(),	
	Templates = [Name||{Name, _, _ } <- L],
	F = fun(X) -> 
%%     	io:format(S, "<SvObjType description=\"~p\" hasTable=\"N\" parentTypeId=\"Monitor\" svobjTypeId=\"~p\"/>~n", [X, X])
		write_template_ofbiz(X)
	end,	
	lists:foreach(F, Templates).

%% @spec get_template(Key)->([#property{}] | {error,Reason})
%% where
%%	Key = atom()
%%	Reason = atom()
%% @doc get detail information of monitor template by key
%%	<br>return list of #property{}</br>
%%	<br>#property{} is defined in <a href="monitor_template.hrl">monitor_template.hrl</a></br>
write_template_ofbiz(Key) when is_atom(Key)->
	M = Key:new(),
	try
	Ret = M:get_template_property(),
	Sps = [P || P<-Ret,P#property.configurable=:=true, lists:member(P#property.name, 
		[name, frequency, disabled, verfiy_error, error_frequency, depends_condition, depends_on, schedule, activate_baseline, proxy, error_classifier,
		 warning_classifier, good_classifier])=:=false],
	F = fun(X) -> 
			Prop = [{m_templatename ,atom_to_list(Key)},{m_name ,X#property.name},
			{m_title ,X#property.title}, 
			{m_type ,X#property.type}, 
			{m_description ,X#property.description}, 
			{m_order ,X#property.order}, 
			{m_editable ,X#property.editable}, 
			{m_configurable ,X#property.configurable},
			{m_advance ,X#property.advance},
			{m_state ,X#property.state}, 
			{m_optional ,X#property.optional}, 
			{m_default ,X#property.default}, 
			{m_allowother ,X#property.allowother},
			{m_listSize ,X#property.listSize},
			{m_multiple ,X#property.multiple},
			{m_upIsBad ,X#property.upIsBad},
			{m_baselinable ,X#property.baselinable},
			{m_primarystate ,X#property.primarystate},
			{m_properties ,X#property.properties}],
%% 			io:format("write_template_ofbiz props:~p~n", Prop),
			ofbiz:callservice("createMoitorTemplate",Prop)
	end,	
	lists:foreach(F, Sps)
	
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end;
write_template_ofbiz(_)->{error,parameter_error}.

writeobjtypexml(File)->
	L = get_templates(),	
	Templates = [Name||{Name, _, _ } <- L],
	{ok, S} = file:open(File, [write, append]),
	F = fun(X) -> 
    	io:format(S, "<SvObjType description=\"~p\" hasTable=\"N\" parentTypeId=\"Monitor\" svobjTypeId=\"~p\"/>~n", [X, X])
	end,	
	lists:foreach(F, Templates),
	file:close(S).

writeentityxml(File)->
	L = get_templates(),	
	Templates = [Name||{Name, _, _ } <- L],
	lists:foreach(fun(X) -> write_template_xml(File,X) end, Templates).

%% @spec get_template(Key)->([#property{}] | {error,Reason})
%% where
%%	Key = atom()
%%	Reason = atom()
%% @doc get detail information of monitor template by key
%%	<br>return list of #property{}</br>
%%	<br>#property{} is defined in <a href="monitor_template.hrl">monitor_template.hrl</a></br>
write_template_xml(File, Key) when is_atom(Key)->
	M = Key:new(),
	try
	Ret = M:get_template_property(),
	Sps = [{P#property.name, P#property.type} || P<-Ret,P#property.configurable=:=true, lists:member(P#property.name, 
		[name, frequency, disabled, verfiy_error, error_frequency, depends_condition, depends_on, schedule, activate_baseline, proxy, error_classifier,
		 warning_classifier, good_classifier])=:=false],
%% 	writexml("c:/monitorentity.xml", Key, Sps)
	writexml(File, Key, Sps)
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end;
write_template_xml(_, _)->{error,parameter_error}.

writemoitorerls()->
	L = get_templates(),	
	Templates = [Name||{Name, _, _ } <- L],
	Extends = lists:delete(atomic_monitor,lists:usort([get_extends_info(Name)||{Name, _, _ } <- L])),
%% 	io:format("Extends_info:~p~n", [Extends]), 
	F = fun(X) -> 
		writemonitorerl(X)
	end,
	lists:foreach(F, Templates),
	
	F1 = fun(X) -> 
		writeextenderl(X)
	end,
	lists:foreach(F1, Extends).
	
get_extends_info(ModuleName) -> 
    ModuleInfo = ModuleName:module_info(),	
    {_,{_,Attributes}} = lists:keysearch(attributes, 1, ModuleInfo),
    {_,{_,[Extends]}} = lists:keysearch(extends, 1, Attributes),
%% 	io:format("get_extends_info:~p~n", [Extends]), 
    Extends.	

writeextenderl(Key)->	
	M = Key:new(),
	try
	Ret = M:get_template_property(),
	%io:format("writemonitorerl1", []),
	Lp = [{P#property.name, P#property.type, P#property.default} || P<-Ret,P#property.configurable=:=true, lists:member(P#property.name, 
		[name, frequency, disabled, verfiy_error, error_frequency, depends_condition, depends_on, schedule, activate_baseline, proxy, error_classifier,
		 warning_classifier, good_classifier])=:=false],
	%io:format("writemonitorerl2", []),
	La = [{P#property.name, P#property.type, P#property.default} || P<-Ret,P#property.configurable=:=false, lists:member(P#property.name, 
		[name, frequency, disabled, verfiy_error, error_frequency, depends_condition, depends_on, schedule, activate_baseline, proxy, error_classifier,
		 warning_classifier, good_classifier])=:=false],	
	%io:format("writemonitorerl3", []),

	ExtendsClass = get_extends_info(Key), 
	writextendcontent("C:\\monitor\\" ++ atom_to_list(Key) ++ ".erl", Key, ExtendsClass, Lp, La)
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end.

writemonitorerl(Key)->	
	M = Key:new(),
	try
	Ret = M:get_template_property(),
	%io:format("writemonitorerl1", []),
	Lp = [{P#property.name, P#property.type, P#property.default} || P<-Ret,P#property.configurable=:=true, lists:member(P#property.name, 
		[name, frequency, disabled, verfiy_error, error_frequency, depends_condition, depends_on, schedule, activate_baseline, proxy, error_classifier,
		 warning_classifier, good_classifier])=:=false],
	%io:format("writemonitorerl2", []),
	La = [{P#property.name, P#property.type, P#property.default} || P<-Ret,P#property.configurable=:=false, lists:member(P#property.name, 
		[name, frequency, disabled, verfiy_error, error_frequency, depends_condition, depends_on, schedule, activate_baseline, proxy, error_classifier,
		 warning_classifier, good_classifier])=:=false],	
	%io:format("writemonitorerl3", []),

	ExtendsClass = get_extends_info(Key), 
	writemoitorcontent("C:\\monitor\\" ++ atom_to_list(Key) ++ ".erl", Key, ExtendsClass, Lp, La)
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end.

writemoitorcontent(File, Name, ExtendsClass, Lp, La)->
	%io:format("writemoitorcontent~p ~p ~p ~p ~n ", [File, Name, Lp, La]),
	{ok, S} = file:open(File, [write, append]),
	io:format(S, "-module (~p).~n" ,[Name]),
	io:format(S, "-compile(export_all).~n" ,[]),
	io:format(S, "-include(\"../../include/object.hrl\").~n" ,[]),
	io:format(S, "-include(\"../../include/monitor.hrl\").~n" ,[]),
	io:format(S, "~n" ,[]),
	
	io:format(S, "extends () -> ~p.~n" ,[ExtendsClass]),
	io:format(S, "~n" ,[]),
	
	io:format(S, "?SUPERCLAUSE(action).~n" ,[]),
	io:format(S, "?SUPERCLAUSE(event).~n" ,[]),
	io:format(S, "?SUPERCLAUSE(pattern).~n" ,[]),
	io:format(S, "~n" ,[]),
	
	io:format(S, "~p(Self, Name)->~n" ,[Name]),
	
	%%
	F = fun(X) ->
		{PropName, PropType, PropDefault} = X,
		io:format(S, "	?SETVALUE(~p,~p),~n" ,[PropName, PropDefault])		
%		io:format(S, "	?SETVALUE(timeout,1000),~n" ,[]),
%		io:format(S, "	?SETVALUE(size,4),~n" ,[]),	
	end,	
	lists:foreach(F, Lp),

	io:format(S, "~n" ,[]),
	io:format(S, "	?SETVALUE(name,Name),~n" ,[]),
	io:format(S, "	object:super(Self, [Name]).~n" ,[]),
	
	io:format(S, "~n" ,[]),
	io:format(S, "~p_(Self)->eresye:stop(?VALUE(name)).~n" ,[Name]),

	io:format(S, "~n" ,[]),
	io:format(S, "init_action(Self,EventType,Pattern,State) ->~n" ,[]),
%	io:format(S, "	io:format ( \"[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n\",	[?VALUE(name),?MODULE,State,EventType,Pattern]),~n" ,[]),
	io:format(S, "	object:do(Self,waiting).~n" ,[]),

	io:format(S, "~n" ,[]),
	io:format(S, "get_max() -> 10.  %max number of this type of monitor can be run in parallel~n" ,[]),
	io:format(S, "get_resource_type() -> ?MODULE.  %the type of resource consumpted from the system, e.g. mem, cpu, network, diskio etc.~n" ,[]),

	io:format(S, "~n" ,[]),
	io:format(S, "do_pong(Self,EventType,Pattern,State) ->~n" ,[]),
%	io:format(S, "	io:format ( \"[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n\",	[?VALUE(name),?MODULE,State,EventType,Pattern]),~n" ,[]),
	io:format(S, "	object:do(Self,start).~n" ,[]),

	io:format(S, "~n" ,[]),
	io:format(S, "update_action(Self,EventType,Pattern,State) ->~n" ,[]),
%	io:format(S, "%% 	io:format ( \"[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n\",	[?VALUE(name),?MODULE,State,EventType,Pattern]),~n" ,[]),

	io:format(S, "	{Session,_} = Pattern,  %%resource_allocated  ~n" ,[]),
	io:format(S, "	eresye:wait(?LOGNAME, {?VALUE(name),Session,'_',allocate_resource}), ~n" ,[]),
	io:format(S, "	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),  ~n" ,[]),	
	 	
	io:format(S, "   	Start = erlang:now(),~n" ,[]),
	io:format(S, "	 object:do(Self,running),~n" ,[]),

	%%
	io:format(S, "~n" ,[]),
%	io:format(S, "	io:format(\"[~w:~w] Running: Hostname:~w, Timeout:~w, Size:~w\n\", [?MODULE,?LINE,?VALUE(name),?VALUE(timeout),?VALUE(size)]),~n" ,[]),
	
%	io:format(S, "%% 	Cmd = \"ping -n 4 -l \" ++ object:get(Size,'value') ++ \" -w \" ++ object:get(Timeout,'value') ++ \"  \" ++ object:get(Hostname,'value'),~n" ,[]),
	
	io:format(S, "~n" ,[]),
	io:format(S, "%% 	simulated random data~n" ,[]),

	%%
	F1 = fun(X) ->
		{PropName, PropType, PropDefault} = X,
		io:format(S, "	?SETVALUE(~p,~p),~n" ,[PropName, PropDefault])		
		%io:format(S, "	?SETVALUE(round_trip_time,100 * random:uniform(10)),~n" ,[]),
		%io:format(S, "	?SETVALUE(packetsgood,random:uniform(4)),~n" ,[]),
	end,	
	lists:foreach(F1, La),		
	
	io:format(S, "~n" ,[]),
	io:format(S, "	Diff = timer:now_diff(erlang:now(), Start)/1000000,~n" ,[]),
	io:format(S, "	?SETVALUE(?MEASUREMENTTIME,Diff),~n" ,[]),
	io:format(S, "	?SETVALUE(?LASTUPDATE,erlang:now()),~n" ,[]),	
	
	%%
	io:format(S, "~n" ,[]),
%	io:format(S, " 	io:format(\"[~w:~w] finish in ~w ms, return: RoundTripTime:~w, PacketsGood:~w\n\", [?MODULE,?LINE,Diff,?VALUE(round_trip_time),?VALUE(packetsgood)]),~n" ,[]),
	io:format(S, " 	io:format(\"---------------------module: ++p update_action :  ++p ++n\", [?MODULE, ?VALUE(name)]),~n" ,[]),

	%% 		
	io:format(S, "~n" ,[]),
	io:format(S, "	resource_pool:release(?VALUE(name)), %%trigging the release_resource_pattern in resource_pool module~n" ,[]),
%	io:format(S, "	object:super(Self, post_updating,[]),	~n" ,[]),
	io:format(S, "	eresye:assert(?VALUE(name), {Session,logging}),	~n" ,[]),
	io:format(S, "	object:do(Self,logging).~n" ,[]),

	io:format(S, "~n" ,[]),
	io:format(S, "start(Name) ->~n" ,[]),
	io:format(S, "	case object:get_by_name(Name) of~n" ,[]),
	io:format(S, "		[] -> ~n" ,[]),
	io:format(S, "				X = object:new(?MODULE,[Name]),~n" ,[]),
	io:format(S, "				object:start(X),~n" ,[]),
	io:format(S, "				eresye:assert(Name,{wakeup}),~n" ,[]),
	io:format(S, "				X;~n" ,[]),
	io:format(S, "		_ -> atom_to_list(Name) ++ \" already existed, choose a new name\"~n" ,[]),
	io:format(S, "	end.~n" ,[]),

	file:close(S).

    
writextendcontent(File, Name, ExtendsClass, Lp, La)->
	%io:format("writemoitorcontent~p ~p ~p ~p ~n ", [File, Name, Lp, La]),
	{ok, S} = file:open(File, [write, append]),
	io:format(S, "-module (~p).~n" ,[Name]),
	io:format(S, "-compile(export_all).~n" ,[]),
	io:format(S, "-include(\"../../include/object.hrl\").~n" ,[]),
	io:format(S, "-include(\"../../include/monitor.hrl\").~n" ,[]),
	io:format(S, "~n" ,[]),
	
	io:format(S, "extends () -> ~p.~n" ,[ExtendsClass]),
	io:format(S, "~n" ,[]),
	
	io:format(S, "?SUPERCLAUSE(action).~n" ,[]),
	io:format(S, "?SUPERCLAUSE(event).~n" ,[]),
	io:format(S, "?SUPERCLAUSE(pattern).~n" ,[]),
	io:format(S, "~n" ,[]),
	
	io:format(S, "~p(Self, Name)->~n" ,[Name]),
	
	%%
	F = fun(X) ->
		{PropName, PropType, PropDefault} = X,
		io:format(S, "	?SETVALUE(~p,~p),~n" ,[PropName, PropDefault])		
%		io:format(S, "	?SETVALUE(timeout,1000),~n" ,[]),
%		io:format(S, "	?SETVALUE(size,4),~n" ,[]),	
	end,	
	lists:foreach(F, Lp),

	io:format(S, "~n" ,[]),
	io:format(S, "	?SETVALUE(name,Name),~n" ,[]),
	io:format(S, "	object:super(Self, [Name]).~n" ,[]),
	
	io:format(S, "~n" ,[]),
	io:format(S, "~p_(Self)->eresye:stop(?VALUE(name)).~n" ,[Name]),
	
%% 	io:format(S, "~n" ,[]),
%% 	io:format(S, "init_action(Self,EventType,Pattern,State) ->~n" ,[]),
%% %	io:format(S, "	io:format ( \"[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n\",	[?VALUE(name),?MODULE,State,EventType,Pattern]),~n" ,[]),
%% 	io:format(S, "	object:do(Self,waiting).~n" ,[]),
%% 
%% 	io:format(S, "~n" ,[]),
%% 	io:format(S, "get_max() -> 10.  %max number of this type of monitor can be run in parallel~n" ,[]),
%% 	io:format(S, "get_resource_type() -> ?MODULE.  %the type of resource consumpted from the system, e.g. mem, cpu, network, diskio etc.~n" ,[]),
%% 
%% 	io:format(S, "~n" ,[]),
%% 	io:format(S, "do_pong(Self,EventType,Pattern,State) ->~n" ,[]),
%% %	io:format(S, "	io:format ( \"[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n\",	[?VALUE(name),?MODULE,State,EventType,Pattern]),~n" ,[]),
%% 	io:format(S, "	object:do(Self,start).~n" ,[]),
%% 
%% 	io:format(S, "~n" ,[]),
%% 	io:format(S, "update_action(Self,EventType,Pattern,State) ->~n" ,[]),
%% %	io:format(S, "%% 	io:format ( \"[~w]:Type=~w,Action=update,State=~w,Event=~w,Pattern=~w\n\",	[?VALUE(name),?MODULE,State,EventType,Pattern]),~n" ,[]),
%% 
%% 	io:format(S, "	{Session,_} = Pattern,  %%resource_allocated  ~n" ,[]),
%% 	io:format(S, "	eresye:wait(?LOGNAME, {?VALUE(name),Session,'_',allocate_resource}), ~n" ,[]),
%% 	io:format(S, "	eresye:assert(?LOGNAME, {?VALUE(name),Session,erlang:now(),update}),  ~n" ,[]),	
%% 	 	
%% 	io:format(S, "   	Start = erlang:now(),~n" ,[]),
%% 	io:format(S, "	 object:do(Self,running),~n" ,[]),
%% 
%% 	%%
%% 	io:format(S, "~n" ,[]),
%% %	io:format(S, "	io:format(\"[~w:~w] Running: Hostname:~w, Timeout:~w, Size:~w\n\", [?MODULE,?LINE,?VALUE(name),?VALUE(timeout),?VALUE(size)]),~n" ,[]),
%% 	
%% %	io:format(S, "%% 	Cmd = \"ping -n 4 -l \" ++ object:get(Size,'value') ++ \" -w \" ++ object:get(Timeout,'value') ++ \"  \" ++ object:get(Hostname,'value'),~n" ,[]),
%% 	
%% 	io:format(S, "~n" ,[]),
%% 	io:format(S, "%% 	simulated random data~n" ,[]),
%% 
%% 	%%
%% 	F1 = fun(X) ->
%% 		{PropName, PropType, PropDefault} = X,
%% 		io:format(S, "	?SETVALUE(~p,~p),~n" ,[PropName, PropDefault])		
%% 		%io:format(S, "	?SETVALUE(round_trip_time,100 * random:uniform(10)),~n" ,[]),
%% 		%io:format(S, "	?SETVALUE(packetsgood,random:uniform(4)),~n" ,[]),
%% 	end,	
%% 	lists:foreach(F1, La),		
%% 	
%% 	io:format(S, "~n" ,[]),
%% 	io:format(S, "	Diff = timer:now_diff(erlang:now(), Start)/1000000,~n" ,[]),
%% 	io:format(S, "	?SETVALUE(?MEASUREMENTTIME,Diff),~n" ,[]),
%% 	io:format(S, "	?SETVALUE(?LASTUPDATE,erlang:now()),~n" ,[]),	
%% 	
%% 	%%
%% 	io:format(S, "~n" ,[]),
%% %	io:format(S, " 	io:format(\"[~w:~w] finish in ~w ms, return: RoundTripTime:~w, PacketsGood:~w\n\", [?MODULE,?LINE,Diff,?VALUE(round_trip_time),?VALUE(packetsgood)]),~n" ,[]),
%% 	io:format(S, " 	io:format(\"---------------------module: ++p update_action :  ++p ++n\", [?MODULE, ?VALUE(name)]),~n" ,[]),
%% 
%% 	%% 		
%% 	io:format(S, "~n" ,[]),
%% 	io:format(S, "	resource_pool:release(?VALUE(name)), %%trigging the release_resource_pattern in resource_pool module~n" ,[]),
%% %	io:format(S, "	object:super(Self, post_updating,[]),	~n" ,[]),
%% 	io:format(S, "	eresye:assert(?VALUE(name), {Session,logging}),	~n" ,[]),
%% 	io:format(S, "	object:do(Self,logging).~n" ,[]),

	io:format(S, "~n" ,[]),
	io:format(S, "start(Name) ->~n" ,[]),
	io:format(S, "	case object:get_by_name(Name) of~n" ,[]),
	io:format(S, "		[] -> ~n" ,[]),
	io:format(S, "				X = object:new(?MODULE,[Name]),~n" ,[]),
	io:format(S, "				object:start(X),~n" ,[]),
	io:format(S, "				eresye:assert(Name,{wakeup}),~n" ,[]),
	io:format(S, "				X;~n" ,[]),
	io:format(S, "		_ -> atom_to_list(Name) ++ \" already existed, choose a new name\"~n" ,[]),
	io:format(S, "	end.~n" ,[]),

	file:close(S).

writexml(File, Name, L)->
	{ok, S} = file:open(File, [write, append]),
	io:format(S, "<entity entity-name=\"~p\"~n", [Name]),
	io:format(S, "	package-name=\"org.ofbiz.svobj.svobj\"~n" ,[]),
	io:format(S, "	title=\"~p Monitor Entity\">~n", [Name]),
	io:format(S, "<field name=\"svobjId\" type=\"id-long\"></field>~n" ,[]),
	F = fun(X) -> 
		case X of
			{PropName, text} ->
				io:format(S, "<field name=\"~p\" type=\"name\"></field> ~n", [PropName]);
			{PropName, bool} ->
				io:format(S, "<field name=\"~p\" type=\"id-long\"></field> ~n", [PropName]);
			{PropName, numeric}->
				io:format(S, "<field name=\"~p\" type=\"numeric\"></field> ~n", [PropName]);
			{PropName, _}->
				io:format(S, "<field name=\"~p\" type=\"object\"></field> ~n", [PropName])
		end
	end,
	lists:foreach(F, L),
    io:format(S, "<prim-key field=\"svobjId\"/>~n" ,[]),	
%% 	io:format(S, "<relation type=\"one\" fk-name=\"~p\" rel-entity-name=\"Monitor\">~n", [string:to_upper(list_to_bitstring(Name))]),
 	io:format(S, "<relation type=\"one\" fk-name=\"~p\" rel-entity-name=\"Monitor\">~n", [Name]),
    io:format(S, "	<key-map field-name=\"svobjId\"/>~n" ,[]),
    io:format(S, "</relation>~n" ,[]),
    io:format(S, "</entity>~n" ,[]),
	file:close(S).

%% @spec get_template(Key,Params)->([#property{}] | {error,Reason})
%% where
%%	Key = atom()
%%	Params = [{Prop,Value}]
%%	Prop = atom()
%%	Value = term()
%%	Reason = atom()
%% @doc get detail information of monitor by key
%%	
%%	<br>return list of #property{}</br>
%%	<br>#property{} is defined in <a href="monitor_template.hrl">monitor_template.hrl</a></br>
get_template(Key,Params) when is_atom(Key) andalso is_list(Params) ->
	M = Key:new(),
	try
	M:set_property(?PAGE_PARAMS,Params),
	Ret = M:get_template_property(),
	Ret
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end;
get_template(_,_)->{error,parameter_error}.
	
%% @spec delete_template(Key)-> ({ok,Result} | {error,Reason})
%% where
%%	Key = atom()
%%	Result = atom()
%%	Reason = atom()
%% @doc delete a template from template list
delete_template(Key) when is_atom(Key)->
	case file:consult("conf/monitor_template.conf") of
		{ok,R}->
			case lists:keymember(Key,1,R) of
				true->
					lists:keydelete(Key,1,R);
				false->
					{error,not_found_template}
			end;
		_->
			{error,template_file_error}
	end;
delete_template(_)->{error,parameter_error}.

%% @spec get_template_name(Key)-> ( string() |{error,Err})
%% where
%%	Key = atom()
%% @doc get a template name by key,if not found template,will return ""
%%
get_template_name(Key) when is_atom(Key)->
	MTs = get_templates(),
	case lists:keysearch(Key,1,MTs) of
		{value,M}->
			element(2,M);
		_->
			"NOT FOUND"
	end;
get_template_name(_)->{error,parameter_error}.

%% @spec get_template_type(Key)-> (Type |{error,Reason})
%% where
%%	Key = atom()
%%    Type = string()
%% @doc get a template type by key,if not found template,will return {error,"NOT FOUND"}
%%
get_template_type(Key) when is_atom(Key)->
	MTs = get_templates(),
	case lists:keysearch(Key,1,MTs) of
		{value,M}->
			Element = element(3,M),
            case lists:keysearch(type,1,Element) of
                {value,{type,Type}}->
                                Type;
				_->
					{error,"NOT FOUND"}
            end;
		_->
			{error,"NOT FOUND"}
	end;
get_template_type(_)->{error,"parameter_error"}.

%% @spec get_scalar_property(Key,Prop,Parms)-> [{Id,Name}]
%% where
%%	Key = atom()
%%	Prop = atom()
%%	Params = [{Key1,Value}]
%%	Key1 = atom()
%%	Value = term()
%%	Id = string()
%%	Name = string()
%% @doc get options of scalar property,when property's type is scalar this function will be called
%% the function will return a list which display in a dropdown box
get_scalar_property(Key,Prop,Parms) when is_atom(Key) andalso is_atom(Prop) andalso is_list(Parms)->
	M = Key:new(),
	try
		Ret = M:getScalarValues(Prop,Parms),
		%%io:format("get_scalar_property:~p,~p,~p~n",[Key,Prop,Ret]),
		M:delete(),
		Ret
	catch
		_:_->M:delete(),[]
	end;
get_scalar_property(_,_,_)->{error,parameter_error}.

%% @spec get_classifier(Key,Category)-> [Classifier]
%% where
%%	Key = atom()
%%	Category = (error | warning | good)
%%	Classifier = {property,Oper,Value}
%%	Oper = ('>' | '<' | '>=' | '<=' | '==' | '!=' | 'contains' | '!contains')
%% @doc get default threshold condition of monitor,Key is the monitor type
get_classifier(Key,Category) when is_atom(Key) and is_atom(Category)->
	M = Key:new(),
	try
	Ret = M:get_classifier(Category),
	Ret
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end;
get_classifier(_,_)->{error,parameter_error}.

%% @spec get_monitor_counters(Key,Params)->({error,parameter_error} | [Counter])
%% where
%%	Key = atom()
%%  Counter = {string(),string()}
%% @doc get monitor's available counters
get_monitor_counters(Key,Params) when is_atom(Key) andalso is_list(Params)->
	M = Key:new(),
	try
		M:set_property(?PAGE_PARAMS,Params),
		Ret = M:getAvailableCounters(M),
		Ret
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end;
get_monitor_counters(_,_)->{error,parameter_error}.

%% @spec get_template_state(Key,Params)->({error,parameter_error}|[#property{}])
%% where
%%	Key = atom()
%%	Params = [{key,term()}]
%% @doc get monitor's threshold property,,Params is the input from UI
get_template_state(Key,Params) when is_atom(Key)->
	M = Key:new(),
	try
		Ret = M:getStateProperties(M,Params),
		Ret
	catch
		_:Err->
			{error,Err}
	after
		M:delete()
	end;
get_template_state(_,_)->{error,parameter_error}.

%% @spec get_template_state(Id,Key,Params)->({error,parameter_error}|[#property{}])
%% where
%%	Key = atom()
%%	Id = atom()
%%	Params = [{key,term()}]
%% @doc get monitor's threshold property,,Params is the input from UI
%% Id is monitor'id
get_template_state(Id,Key,Params) when is_atom(Key) andalso is_atom(Id) ->
	case api_siteview:find_object(Id) of
		[] ->
			get_template_state(Key,Params);
		[M|_]->
			M:getStateProperties(M,Params)
	end;
get_template_state(_,_,_)->{error,parameter_error}.

%% @spec get_servers(Params)->[{Name,Addr}]
%% where
%%	Params = [{key,term()}]
%%	Name = string()
%%	Addr = string()
%% @doc get available servers of a monitor,Params is the input from UI
get_servers(Params) when is_list(Params)->
	% io:format("get_server:~p~n",[Params]),
	case proplists:get_value("_platform",Params) of
		undefined->
			case proplists:get_value("class",Params) of
				undefined->
					[];
				"log_monitor"->
					Mas = dbcs_machine:get_Unixmachine(),
					case platform:getOs() of
						1->
							[];
						_->
							[{"this server",""}]
					end ++
					[{X#machine.name,X#machine.host}||X<-Mas];
				Class->
					C = list_to_atom(Class),
					Obj = C:new(),
					case Obj:remoteCommandLineAllowed() of
						false->
							Obj:delete(),
							Ms = dbcs_machine:get_NTmachine(),
							case platform:getOs() of
								1->
									[{"this server",""}];
								_->
									[]
							end ++
							[{X#machine.name,X#machine.host}||X<-Ms];
						_->
							Obj:delete(),
							[{"this server",""}] ++
							[{X#machine.name,X#machine.host}|| X<-dbcs_machine:get_all()]
					end
			end;
		"windows"->
			Ms = dbcs_machine:get_NTmachine(),
			case platform:getOs() of
				1->
					[{"this server",""}];
				_->
					[]
			end ++
			[{X#machine.name,X#machine.host}||X<-Ms];
		"unix"->
			Mas = dbcs_machine:get_Unixmachine(),
			case platform:getOs() of
				1->
					[];
				_->
					[{"this server",""}]
			end ++
			[{X#machine.name,X#machine.host}||X<-Mas];
		_->
			[{"this server",""}] ++
			[{X#machine.name,X#machine.host}|| X<-dbcs_machine:get_all()]
	end;
get_servers(Params)->
	{error,parameter_error}.

%% @spec run_monitor_from_template(TempName,Values,Timeout)->({ok,Result} | {error,Reason})
%% where
%%	TempName = string()
%%	Values = [{Key,Value}]
%%	Key = atom()
%%	Value = term()
%%	Timeout = integer()
%%	Result = atom()
%%	Reason = atom()
%% @doc run monitor from template with input values
run_monitor_from_template(TempName,Values,Timout)->
	ok.

get_monitor_types()->
	Temps = get_templates(),
	F = fun(Key)->
		case lists:keysearch(Key,1,Temps) of
		{value,M}->
			element(2,M);
		_->
			"NOT FOUND"
		end
	end,
	
	case dbcs_monitor:get_all() of
		{error,_}->
			[];
		Monitors->
			lists:foldl(fun(X,R)->
				Class = proplists:get_value(class,X),
				case proplists:get_value(Class,R) of
					undefined->
						R ++ [{Class,F(Class)}];
					_->
						R
				end
			end,[],Monitors)
	end.
