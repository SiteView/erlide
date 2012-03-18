-module(asset_default_instance).
-compile(export_all).
-include("asset.hrl").
-behaviour(gen_server).

%% gen_server callbacks
 -export([init/1, handle_call/3, handle_cast/2, handle_info/2,
         code_change/3, terminate/2]).

-record(state, {instances=[]}).
-record(instance, {id, update=false, instance}).

-define(INSTANCE_DIR,"templates.asset/default_instances.conf").
-define(SERVER,'elecc_asset_default_instance').

%% @spec start_link() ->(ok | {error,Reason})
%% where
%%	Reason = atom()
%% @doc start preferences services
%%
start_link() ->
    start_link([]).

start_link(Opts) when is_list(Opts) ->
    gen_server:start_link({local, ?SERVER}, ?MODULE, Opts, []).

init(_)->
	Instances = case file:consult(?INSTANCE_DIR) of
		{ok,Data}->
			Data;
		_->
			[]
	end,
    {ok, #state{instances=Instances}}.
            
stop() ->
    gen_server:cast(?SERVER, stop).
    
import_instance(Alias) ->
    import_instance(Alias, fix).
    
import_instance(Alias, Method) ->
    gen_server:call(?SERVER,{import_instance, Alias, Method}).
    
import_instances(AliasList, Method) ->
    gen_server:call(?SERVER,{import_instances, AliasList, Method}).
    
reload() ->
    reload("default_instances.conf").
    
reload(File) ->
    gen_server:call(?SERVER,{reload, "templates.asset/"++File}).
	
handle_call({import_instance, Alias, Method}, _, State)->
	Reply = case proplists:get_value(Alias, State#state.instances) of
		undefined ->
			{error, not_found};
        R ->
            import_each(R, Method)
    end,
    {reply, Reply, State};
    
handle_call({import_instances, AliasList, Method}, _, State)->
    Fun = fun(X, All) ->
        case proplists:get_value(X, State#state.instances) of
            undefined ->
                [];
            T ->
                T
        end ++ All
    end,
    Instances = lists:foldl(Fun, [], AliasList),
    Reply = import_each(Instances, Method),
    {reply, Reply, State};

handle_call({reload, File}, _, State)->
    NS = case file:consult(File) of
		{ok,Data}->
            Reply = {ok, finish},
			State#state{instances=Data};
        Error ->
            Reply = Error,
            State
    end,
    {reply, Reply, NS};

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

create_instance(Instance) ->
    dbcs_asset:create_asset(Instance).
    
delete_instance(Id) ->
    dbcs_asset:remove_asset(Id).

get_template_by_alias(Alias, Templates) ->
    T = [X||X<- Templates, proplists:get_value(alias, X)==Alias], 
    if
        length(T)/=0 ->
            hd(T);
        true ->
            {error, not_found}
    end.

build_instance(Asset, Template) ->
    Attributes = proplists:get_value(attrs, Asset, []),
    AttributeTemplates = proplists:get_value(attrs, Template, []),
    Fun = fun(X, All) ->
        case proplists:get_value(proplists:get_value(alias, X), Attributes) of
            undefined ->
                [];
            V ->
                [{proplists:get_value(id, X), V}]
        end ++ All
    end,
    NewAttributes = lists:foldl(Fun, [], AttributeTemplates),
    case proplists:get_value(id, Asset) of
        undefined ->
            Iid = asset_util:get_id(?ASSET),
            #instance{id=Iid, instance=lists:keyreplace(attrs, 1, [{id, Iid}|Asset], {attrs, NewAttributes}), update=false};
        Id ->
            case asset:get_asset(Id) of
                {error, _} ->
                    #instance{id=Id, instance=lists:keyreplace(attrs, 1, Asset, {attrs, NewAttributes}), update=false};
                _ ->
                    #instance{id=Id, instance=lists:keyreplace(attrs, 1, Asset, {attrs, NewAttributes}), update=true}
            end
    end.

import_each(Instances, Method) ->
    import_each(Instances, Method, [], []).
    
import_each([], Method, Map, _) ->
    case Method of
        fix ->
            lists:map(fun(X)-> case X#instance.update of false ->create_instance(X#instance.instance); _ ->nothing end end, Map);
        reset ->
            lists:map(fun(X)-> case X#instance.update of true ->delete_instance(X#instance.id); _ ->nothing end end, Map),
            lists:map(fun(X)-> create_instance(X#instance.instance) end, Map);
        delete ->
            lists:map(fun(X)-> case X#instance.update of true ->delete_instance(X#instance.id); _ ->nothing end end, Map);
        _ ->
            io:format("Unkown Method~n")
    end,
    {ok, finish};
import_each([F|R], Method, Map, Templates) ->
    Derived = proplists:get_value(derived, F),
    if
        Derived == undefined ->
            import_each(R, Method, Map, Templates);
        true ->
            case get_template_by_alias(Derived, Templates) of
                {error, _} ->
                    case asset_template:get_asset_template_by_alias(Derived) of
                        {error, _} ->
                            import_each(R, Method, Map, Templates);
                        Template ->
                            NewInstance = build_instance(F, Template),
                            import_each(R, Method, [NewInstance|Map], [Template|Templates])
                    end;
                Template ->
                    NewInstance = build_instance(F, Template),
                    import_each(R, Method, [NewInstance|Map], Templates)
            end
    end.
    
    