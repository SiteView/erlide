%
%the module for initialize each application building with erl CMDB
%
-module(cmdb_initialize).
-include("asset.hrl").
-behaviour(gen_server).

%% gen_server callbacks
 -export([init/1, handle_call/3, handle_cast/2, handle_info/2,
         code_change/3, terminate/2]).
         
-export([start_link/0, stop/0, is_initialized/1, initialize/2, initialize_log/1]).

-record(state, {apps=[]}).

-define(APP_DIR,"templates.asset/cmdb_applications.conf").
-define(INITIALIZE_LOG, "templates.asset/initialize.log").

-define(FIX, fix).
-define(RESET, reset).
-define(DELETE, delete).

-define(SERVER,'elecc_cmdb_initialize').

start_link() ->
    start_link([]).

start_link(Opts) when is_list(Opts) ->
    gen_server:start_link({local, ?SERVER}, ?MODULE, Opts, []).

init(_)->
    create_root_template(),
    {ok, #state{apps=[]}}.
            
stop() ->
    gen_server:cast(?SERVER, stop).

create_root_template() ->
    Ci = [{alias, "Ci"}, {icon, "root"}, {display, "Ci"}, {attrs, []}],
    asset_template:create_template(Ci).

is_initialized(App) when is_list(App) ->
    gen_server:call(?SERVER,{is_initialized, App});
is_initialized(_) ->
    {error, error_params}.
    
initialize(App, Method) when is_list(App), is_atom(Method) ->
    gen_server:call(?SERVER,{initialize, App, Method});
initialize(_, _) ->
    {error, error_params}.
    
initialize_log(App) when is_list(App) ->
    gen_server:call(?SERVER, {init_log, App}).
    
handle_call({is_initialized, App}, _, State) ->
    Bool = case cmdb_initialize_log:q(App) of
        {error, _} ->
            false;
        _ ->
            true
    end,
    {reply, Bool, State};

handle_call({initialize, App, Option}, _, State)->
    Reply = case file:consult(?APP_DIR) of
        {ok, Apps} ->
            case proplists:get_value(App, Apps) of
                undefined ->
                    {error, app_config_file_not_found};
                Conf ->
                    case file:consult(Conf) of
                        {ok, Data} ->
                            process_data(Data, Option),
                            cmdb_initialize_log:log(App, Option);
                        _ ->
                            {error, read_config_file_error}
                    end
            end;
        _ ->
            {error, read_config_file_error}
    end,
    {reply, Reply, State};
    
handle_call({init_log, App}, _, State) ->
    Reply = cmdb_initialize_log:q(App),
    {reply, Reply, State};

handle_call(_Req, _, State) ->
    {reply, {error,unknown_request}, State}.

handle_cast(stop, S) ->
    {stop, normal, S};
handle_cast(_, State) ->
    {noreply, State}.

handle_info(_, State) ->
    {noreply, State}.

code_change(_Vsn, State, _Extra) ->
    {ok, State}.

terminate(_Reason, _State) ->
    ok.
    
process_data(Data, Method) ->
    Templates = proplists:get_value(template, Data, []),
    Instances = proplists:get_value(instance, Data, []),
    asset_default_template:import_templates(Templates, Method),
    asset_default_instance:import_instances(Instances, Method),
    {ok, finish}.
    



