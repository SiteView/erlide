%% ---
%% node poller
%%
%%---
-module(nnm_poller_loader).
-compile(export_all).

-behaviour(gen_server).

-export([init/1, handle_call/3, handle_cast/2, handle_info/2, 
	code_change/3, terminate/2]).

-include("../../core/include/monitor.hrl").
-include("../../core/include/common.hrl").

-define(NNM_SCHEDULER,'ecc_nnm_scheduler').


-record(state, {}).

start_link() ->
    start_link([]).
	
start_link(Opts) when is_list(Opts) ->
    gen_server:start_link({local, ?MODULE}, ?MODULE, [Opts], []).

init([Opts])->
	erlcmdb:start(),
	F = fun(X)->
			dbcs_base:set_app(X,true),
			case nnm_dbcs_node:get_list([id,?APP,poll_interval,next_poll,sys_name]) of
				{error,Err}->
					[];
				Nodes->
					schedule_node(Nodes)
			end
		end,
	lists:map(F,app:all()),
	{ok,#state{}}.
	
stop() ->
    gen_server:cast(?MODULE, stop).
	
handle_call(Req, _, State) ->
    {reply, {error, {unknown_request, Req}}, State}.
	
handle_cast(_, State) ->
    {noreply, State}.

handle_info(_, State) ->
    {noreply, State}.

code_change(_Vsn, State, _Extra) ->
    {ok, State}.

terminate(_Reason, _State) ->
    ok.	
	
	

unschedule_node(Id)when is_atom(Id)->
	case siteview:get_object(Id) of
		[M|_]->
			case M:isRunning() of
				true->
					{error,monitor_is_running};
				_->
					case M:unschedule(M) of
						{ok,_}->
							M:stopMonitor(M),
							M:delete(),
							{ok,unschedule_ok};
						Else->
							Else
					end
			end;
		_->
			{error,not_found}
	end.

schedule_node(Id) when is_atom(Id)->
	case nnm_dbcs_node:get_node(Id,[id,?APP,poll_interval,next_poll,sys_name]) of
		{error,Err}->
			{error,Err};
		Node->
			schedule_node([Node])
	end;
schedule_node([])->{ok,schedule};
schedule_node([N|T])->
	Id = proplists:get_value(id,N),
	Pi = proplists:get_value(poll_interval,N),
	% Np = case proplists:get_value(next_poll,N) of V when is_integer(V)->V;_-> 0 end,
	Name = case proplists:get_value(sys_name,N) of undefined->"";Na->Na end,
	App = proplists:get_value(?APP,N),
	io:format("----------------------------APP:~p--------------------~n",[App]),
	Pl = nnm_node_poller:new(),
	Data = [
		{id,Id},
		{?NAME,"NNM NODE:" ++ Name},
		{frequency,Pi},
		{?APP,App},
		{error_frequency,0},
		{class,nnm_node_poller}
	],
	Pl:init(Pl,Data),
	
	%
	case nnm_dbcs_interface:get_by_node(Id,[id,?APP,node_id,poll_interval,next_poll,interface_name]) of
		{error,_}->
			pass;
		Interfaces->
			schedule_interface(Interfaces)
	end,
	Pl:startMonitor(Pl),
%	 if
%		Id=/= undefined andalso Pi =/= undefined andalso Pi > 0 ->
%			Ns:schedulePeriodicAction(Pl,Pi *1000,true,Np);
%		true->
%			error
%	end,
	schedule_node(T);
schedule_node(_)->{error,parameter_error}.
	
	
schedule_interface(Id) when is_atom(Id)->
	case nnm_dbcs_interface:get_interface(Id,[id,?APP,node_id,poll_interval,next_poll,interface_name]) of
		{error,Error}->
			{error,Error};
		Interface->
			schedule_interface([Interface])
	end;
schedule_interface([])->{ok,schedule_interface};
schedule_interface([If|T])->
	Id = proplists:get_value(id,If),
	NodeId = proplists:get_value(node_id,If),
	Pi = proplists:get_value(poll_interval,If),
	Name = case proplists:get_value(interface_name,If) of undefined->"";N->N end,
	App = proplists:get_value(?APP,If),
	% Np = case proplists:get_value(next_poll,If) of V when is_integer(V)->V;_-> 0 end,
	Mp = nnm_interface_poller:new(),
	Data = [
		{id,Id},
		{node_id,NodeId},
		{?NAME, "NNM INTERFACE:" ++ Name},
		{?APP,App},
		{frequency,Pi},
		{error_frequency,0},
		{class,nnm_interface_poller}
	],
	Mp:init(Mp,Data),
	% Mp:startMonitor(Mp),
	schedule_interface(T);
schedule_interface(_)->{error,parameter_error}.


unschedule_interface(Id) when is_atom(Id)->
	case siteview:get_object(Id) of
		[M|_]->
			case M:isRunning() of
				true->
					{error,monitor_is_running};
				_->
					% M:stopMonitor(M),
					M:delete(),
					{ok,unschedule_ok}
			end;
		_->
			{error,not_found}
	end.
	