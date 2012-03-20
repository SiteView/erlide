%% ---
%% nnm poller
%%
%%---
-module(nnm_poller,[BASE]).
-compile(export_all).
-extends(atomic_monitor).

-include("../../core/include/monitor.hrl").
-include_lib("snmp/include/snmp_types.hrl").

new()->
	Base = atomic_monitor:new(),
	{?MODULE,Base}.
	
init(This,Data)->
	%% BASE:init(This,Data),
	This:remove_properties(),
	This:add_properties(Data),
	case proplists:get_value(id,Data) of
		undefined->
			pass;
		Id->
			dbcs_base:set_app(proplists:get_value(?APP,Data)),
			siteview:set_object(Id,nnm_poller,element(1,This),This)
	end.
	
getCostInLicensePoints()->0.

get_vbs_value(Oid,Vbs)->
	F = fun(X)->
		X#varbind.oid == Oid
		end,
	case lists:filter(F,Vbs) of
		[Vb|_]->
			vbs_value(Vb);
		_->
			undefined
	end.
	
vbs_value(Vb)->
	case Vb#varbind.variabletype of
		'OCTET STRING'->
			case is_list(Vb#varbind.value) of
				true->
					string:strip(Vb#varbind.value,right,0);
				_->
					Vb#varbind.value
			end;
		_->
			Vb#varbind.value
	end.

save_result(This,Category)->
	{ok,{id,Id}}= This:get_property(id),
	{ok,{?STATE_STRING,State}} =  This:get_attribute(?STATE_STRING),
	F = fun(X)->
			case This:get_attribute(X) of
				{ok,Val}->
					Val;
				_->
					{X,not_found}
			end
		end,
	R = lists:map(F,This:getLogProperties(This)),
	Log = #monitorlog{id=Id,name=This:get_name(),time=erlang:localtime(),category=Category,desc=State,measurement=R},
	monitor_logger:log(Log).