-module(asset_log).
-compile(export_all).
-include("asset.hrl").
-behaviour(gen_server).

%% gen_server callbacks
 -export([init/1, handle_call/3, handle_cast/2, handle_info/2,
         code_change/3, terminate/2]).
         
-export([start_log/0, stop_log/0, log/3, get_log/0, get_log/1]).
         
-define(SERVER,'elecc_asset_log').
-record(state, {open}).

start_link() ->
    gen_server:start_link({local, ?SERVER}, ?MODULE, [], []).

init([])->
	{ok,#state{open=false}}.

stop() ->
    gen_server:cast(?SERVER, stop).

start_log() ->
    gen_server:cast(?SERVER, {start_log}).
    
stop_log() ->
    gen_server:cast(?SERVER, {stop_log}).

log(Oper, Target, Result) ->
    App = dbcs_base:get_app(),
    log([{app_, atom_to_list(App)}, {time, asset_util:get_time()}, {oper, Oper}, {target, Target}, {result, Result}]).
    
log(Log) ->
    gen_server:cast(?SERVER, {log,Log}).
    
get_log() ->
    get_log(asset_util:get_date(date())).
    
get_log(Date) ->
    gen_server:call(?SERVER, {get_log, Date}).
    
get_all_logs() ->
    gen_server:call(?SERVER, {get_all_logs}).
    
get_log_state() ->
    gen_server:call(?SERVER, {get_log_state}).
    
remove_log() ->
    remove_log(asset_util:get_date(date())).
    
remove_log(Date) ->
    gen_server:call(?SERVER, {remove_log, Date}).
    
handle_cast({log, Log}, State) ->
    Open = State#state.open,
    Day = asset_util:get_date(date()),
    if
        Open ->
            L = case asset_config_manager:get(?LOG_TABEL, Day) of
                {ok, Logs} when Logs/=[] ->
                    case proplists:get_value(Day, Logs) of
                        undefined ->
                            [Log];
                        Other ->
                            Other++[Log]
                    end;  
                _ ->
                    [Log]
            end,
            asset_config_manager:set(?LOG_TABEL, Day, L);
        true ->
            nothing
    end,
    {noreply,State};
handle_cast({start_log}, State) ->
    {noreply,State#state{open=true}};
handle_cast({stop_log}, State) ->
    {noreply,State#state{open=false}};
handle_cast(stop, S) ->
    {stop, normal, S};
handle_cast(_, State) ->
    {noreply, State}.

handle_call({get_log, Date}, _, State) ->
    Day = if
        is_tuple(Date) ->
            asset_util:get_date(Date);
        is_list(Date) ->
            Date;
        true ->
            asset_util:get_date(date())
    end,
    Relay = asset_config_manager:get(?LOG_TABEL, Day),
    {reply, Relay, State};
handle_call({get_all_logs}, _, State) ->
    Relay = asset_config_manager:all(?LOG_TABEL),
    {reply, Relay, State};
handle_call({get_log_state}, _, State) ->
    R = case State#state.open of
        true ->
            logging;
        _ ->
            waitting
    end,
    {reply, {ok, R}, State};
handle_call({remove_log, Date}, _, State) ->
    Day = if
        is_tuple(Date) ->
            asset_util:get_date(Date);
        is_list(Date) ->
            Date;
        true ->
            asset_util:get_date(date())
    end,
    Relay = asset_config_manager:remove(?LOG_TABEL, Day),
    {reply, Relay, State};
handle_call(Req, _, State) ->
    {reply, {error, {unknown_request, Req}}, State}.

handle_info(_, State) ->
    {noreply, State}.

code_change(_Vsn, State, _Extra) ->
    {ok, State}.

terminate(_Reason, _State) ->
    ok.
    
    
    
    
    
    