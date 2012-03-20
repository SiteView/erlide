-module(asset_default_template).
-compile(export_all).
-include("asset.hrl").
-behaviour(gen_server).

%% gen_server callbacks
 -export([init/1, handle_call/3, handle_cast/2, handle_info/2,
         code_change/3, terminate/2]).

-record(state, {templates=[]}).
-record(template, {id, update=false, template}).

-define(TEMPLATE_DIR,"templates.asset/default_templates.conf").

-define(SERVER,'elecc_asset_default_template').

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
	Templates = case file:consult(?TEMPLATE_DIR) of
		{ok,Data}->
			Data;
		_->
			[]
	end,
    {ok, #state{templates=Templates}}.
            
stop() ->
    gen_server:cast(?SERVER, stop).
    
import_template(Alias) ->
    import_template(Alias, fix).
    
import_template(Alias, Method) ->
    gen_server:call(?SERVER,{import_template, Alias, Method}).
    
import_templates(AliasList, Method) ->
    gen_server:call(?SERVER,{import_templates, AliasList, Method}).
    
reload() ->
    reload("default_templates.conf").
    
reload(File) ->
    gen_server:call(?SERVER,{reload, "templates.asset/"++File}).
    
get_default_templates() ->
    gen_server:call(?SERVER,default_templates).
    
get_imported_templates() ->
    gen_server:call(?SERVER,imported_templates).
    
get_info(Alias) ->
    gen_server:call(?SERVER,{template_info,Alias}).
	
handle_call({import_template, Alias, Method}, _, State)->
	Reply = case proplists:get_value(Alias, State#state.templates) of
		undefined ->
			{error, not_found};
        R ->
            import_each(R, Method)
    end,
    {reply, Reply, State};
    
handle_call({import_templates, AliasList, Method}, _, State)->
    Fun = fun(X, All) ->
        case proplists:get_value(X, State#state.templates) of
            undefined ->
                [];
            T ->
                T
        end ++ All
    end,
    Templates = lists:foldl(Fun, [], AliasList),
    Reply = import_each(Templates, Method),
    {reply, Reply, State};

handle_call({reload, File}, _, State)->
    NS = case file:consult(File) of
		{ok,Data}->
            Reply = {ok, finish},
			State#state{templates=Data};
        Error ->
            Reply = Error,
            State
    end,
    {reply, Reply, NS};

handle_call(default_templates, _, State)->
	{reply,[K||{K,_}<- State#state.templates],State};
    
handle_call(imported_templates, _, State)->
    Root = [N||{N, _}<- State#state.templates],
    IT = case asset_template:get_template_by_aliasList(Root, 0, 1000, "", "") of
        {error, _} ->
            [];
        ImportT ->
            ImportT
    end,
	{reply,IT,State};
    
handle_call({template_info,Alias}, _, State) ->
    R = case proplists:get_value(Alias, State#state.templates) of
        undefined ->
            [];
        Temps ->
            lists:map(fun(X)->proplists:get_value(alias, X) end, Temps)
    end,
    {reply,R, State};

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

verify(_,_)->
	{ok,""}.
    
get_template_by_alias(undefined, _) ->{error, not_found};
get_template_by_alias(Alias, Templates) ->
    Fun = fun(X) ->
        case proplists:get_value(alias, X) of
            Alias ->
                true;
            _ ->
                false
        end
    end,
    case lists:filter(Fun, Templates) of
        List when length(List)>0 ->
            hd(List);
        _ ->
            {error, not_found}
    end.
    
create_template(Template) ->
    Attributes = proplists:get_value(attrs, Template, []),
    Fun = fun(X, All) ->
        [case proplists:get_value(id, Attributes) of
            undefined ->
                [{id, asset_util:get_id(?ATTR)}|X];
            _ ->
                X
        end | All]
    end,
    NewAttributes = lists:foldl(Fun, [], Attributes),
    dbcs_asset_template:create_template(lists:keyreplace(attrs, 1, Template, {attrs, NewAttributes})).
    
delete_template(Id) ->
    dbcs_asset_template:remove_template(Id).
    
update_template(Template) ->
    dbcs_asset_template:update_template(Template).
    
add_child(Temp, Child) ->
    case proplists:get_value(child, Temp) of
        undefined ->
            Temp++[{child, [Child]}];
        C ->
            case lists:member(Child, C) of
                false ->
                    lists:keyreplace(child, 1, Temp, {child, C++[Child]});
                _ ->
                    Temp
            end
    end.
    
update_parent(Template, Map) ->
    Temp = Template#template.template,
    Derived = proplists:get_value(derived, Temp),
    Alias = proplists:get_value(alias, Temp),
    ParentTemp = case get_template_by_alias(Derived, [X#template.template||X<- Map]) of
        {error, _} ->
            [];
        T ->
            add_child(T, Alias)
    end,
    if
        ParentTemp /= [] ->
            PTID = proplists:get_value(id, ParentTemp),
            Fun = fun(X) ->case X#template.id of PTID ->X#template{template=ParentTemp};_ ->X end end,
            lists:map(Fun, Map);
        true ->
            Map
    end.
    
import_each(Templates, Method) ->
    import_each(Templates, Method, []).
    
import_each([], Method, Map) ->
    case Method of
        fix ->
            lists:map(fun(X)-> case X#template.update of false ->create_template(X#template.template); _ ->nothing end end, Map);
        reset ->
            lists:map(fun(X)-> case X#template.update of true ->delete_template(X#template.id); _ ->nothing end end, Map),
            lists:map(fun(X)-> create_template(X#template.template) end, Map);
        delete ->
            lists:map(fun(X)-> case X#template.update of true ->delete_template(X#template.id); _ ->nothing end end, Map);
        _ ->
            io:format("Unkown Method~n")
    end,
    {ok, finish};
import_each([F|R], Method, Map) ->
    Template = case asset_template:get_template(proplists:get_value(id, F)) of
        {error, _} ->
            NF = case proplists:get_value(id, F) of
                undefined ->
                    F++[{id, asset_util:get_id(?TEMPLATE)}];
                _ ->
                    F
            end,
            #template{id=proplists:get_value(id, NF), update=false, template=NF};
        OT ->
            #template{id=proplists:get_value(id, OT), update=true, template=OT}
    end,
    NewMap = update_parent(Template, [Template|Map]),
    import_each(R, Method, NewMap).
    
            
            
            
