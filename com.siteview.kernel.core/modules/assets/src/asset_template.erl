-module(asset_template).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").

%ci_template, ["id","app_","alias","derived","child","display","attrs","value","created","modified","description","icon","total"]
%attribute,["id","alias","display","type","scalarvalue","ref","refType","default","icon","description","ne","order","editable"]
%type, ["numeric", "password", "text", "scalar", "textArea", "ci_reference", "bool"]
%模板增加继承关系， 根模板级：继承于ci，其他子模板均继承于根级模板
%模板的alias不允许被修改,derived与child中都是用alias作为template标示


create_template(Template) ->
    Alias = proplists:get_value(alias, Template),
    if
        ((Alias==undefined) or (Alias=="")) ->
            {error, alias_blank};
        true ->
            case get_template_by_alias(Alias) of
                {error, _} ->
                    Derived = proplists:get_value(derived, Template),
                    %update parent childs
                    case get_template_by_alias(Derived) of
                        {error, _} ->
                            ok;
                        Parent ->
                            io:format("~p's parent:~p~n", [Alias, proplists:get_value(alias, Parent)]),
                            ParentChild = proplists:get_value(child, Parent, []),
                            update_template(lists:keyreplace(child, 1, Parent, {child, lists:usort([Alias|ParentChild])}))
                    end,
                    Id = case proplists:get_value(id, Template) of
                        undefined ->
                            asset_util:get_id(?TEMPLATE);
                        Oid ->
                            Oid
                    end,
                    Time = asset_util:get_time(),
                    Attrs = add_id(proplists:get_value(attrs, Template, [])),
                    NewTemp = [{id, Id},{alias, Alias},{derived, Derived},{child, proplists:get_value(child, Template, [])},{description,proplists:get_value(description,Template,"")},
                        {icon, proplists:get_value(icon,Template, "")},{display, proplists:get_value(display, Template, "")},{attrs,Attrs},
                        {value, proplists:get_value(value,Template,[])},{created,Time},{modified,Time}],
                    dbcs_asset_template:create_template(NewTemp);
                _ ->
                    {error, template_alias_existed}
            end
    end.

update_template(Template) ->
    Id = proplists:get_value(id, Template),
    case get_template(Id) of
        {error, _} ->
            {error, template_not_found};
        OldTemp ->
            Time = asset_util:get_time(),
            Attrs = add_id(proplists:get_value(attrs, Template,[])),
            NewTemp = [{id, Id},{alias, proplists:get_value(alias, OldTemp)},
                {derived, proplists:get_value(derived, OldTemp)},{child, proplists:get_value(child, Template,[])},
                {display, proplists:get_value(display, Template, "")},{attrs,Attrs},{icon, proplists:get_value(icon,Template, "")},
                {value,proplists:get_value(value,Template,[])},{created,proplists:get_value(created, Template)},{modified,Time},{description,proplists:get_value(description,Template,"")}],
            dbcs_asset_template:update_template(NewTemp)
    end.
    
remove_template(Id) ->
    case get_template(Id) of
        {error, _} ->
            {error, template_not_found};
        Temp ->
            Alias = proplists:get_value(alias, Temp),
            Derived = proplists:get_value(derived, Temp),
            %update parent child
            case get_template_by_alias(Derived) of
                {error, _} ->
                    ok;
                Parent ->
                    ParentChild = proplists:get_value(child, Parent, []),
                    dbcs_asset_template:update_template(lists:keyreplace(child, 1, Parent, {child, lists:filter(fun(X)->X/=Alias end, ParentChild)}))
            end,
            dbcs_asset_template:remove_template(proplists:get_value(id, Temp))
    end.
    
%error
remove_all_templates() ->
    dbcs_asset_template:remove_all().

get_template(Id) when is_list(Id) ->
    dbcs_asset_template:get_template(Id);
get_template(_) ->{error, params_error}.

get_templates(Index, Count, Sort, SortType) ->
    WhereCondition=
        #query_condition_where{
                where=[]
            },
    Query_Condition=
        #query_condition1{
            where=WhereCondition,
            index=Index,
            count=Count,
            sort=Sort,
            sortType=SortType
        },
    dbcs_asset_template:get_templateWhere(Query_Condition).
    
get_template_by_alias(Alias) when is_list(Alias) ->
    case dbcs_asset_template:get_template_match("my.alias="++Alias) of
        [] ->
            {error, template_not_found};
        Temp ->
            hd(Temp)
    end;
get_template_by_alias(_) ->{error, params_error}.

get_root_templates() ->
    get_root_templates(0, 1000, "", "").
    
get_root_templates(Index, Count, Sort, SortType)->
    Where =#query_condition_where{
    where=[{"my.derived","=","'"++?ROOT++"'","&"}]
    },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= Index,
                     count=Count,
                     sort=Sort,
                     sortType=SortType
    },
    dbcs_asset_template:get_templateWhere(Query_Condition).

get_all_templates() ->
    dbcs_asset_template:get_all().
    
%用于在创建、修改资产时使用的模板，此模板包含了它继承的所有属性
get_asset_template_by_alias(Alias) ->
    case get_template_by_alias(Alias) of
        {error, _} ->
            {error, template_not_found};
        Temp ->
            get_asset_template(proplists:get_value(id, Temp))
    end.

get_asset_template(Id) ->
    case get_template(Id) of
        {error, _} ->
            {error, template_not_found};
        Temp ->
            {Attrs, Icon, Display} = get_template_attribute(Id, 1),
            T1 = lists:keyreplace(attrs, 1, Temp, {attrs, Attrs}),
            T2 = lists:keyreplace(icon, 1, T1, {icon, Icon}),
            lists:keyreplace(display, 1, T2, {display, Display})
    end.
    
get_parent_template(Id) ->
    case get_template(Id) of
        {error, _} ->
            {error, template_not_found};
        Temp ->
            Derived = proplists:get_value(derived, Temp),
            get_template_by_alias(Derived)
    end.
    
get_child_template(Id) ->
    get_child_template(Id, 0, 10000, "", "").

get_child_template(Id, Index, Count, Sort, SortType) ->
    case get_template(Id) of
        {error,_} ->
            {error, template_not_found};
        Temp ->
            Childs = proplists:get_value(child, Temp,[]),
            Where =#query_condition_where{
            where=[{"my.alias","in",textutils:listToStr(Childs),"&"}]
            },
            Query_Condition=#query_condition1{
                            where=Where,
                             index= Index,
                             count=Count,
                             sort=Sort,
                             sortType=SortType
            },
            dbcs_asset_template:get_templateWhere(Query_Condition)
    end.
    
get_template_by_aliasList(AliasList, Index, Count, Sort, SortType) ->
    Where =#query_condition_where{
    where=[{"my.alias","in",textutils:listToStr(AliasList),"&"}]
    },
    Query_Condition=#query_condition1{
                    where=Where,
                     index= Index,
                     count=Count,
                     sort=Sort,
                     sortType=SortType
    },
    dbcs_asset_template:get_templateWhere(Query_Condition).
    
get_all_children(Id) ->
    case get_child_template(Id) of
        {error, _} ->
            [];
        Childs ->
            parse_child(Childs, [])
    end.
    
get_template_attribute(Id) ->
    get_template_attribute(Id, 1).
    
get_template_attribute(Id, Index) ->
    get_template_attribute(Id, Index, [], [], "", "").

%由下到上,所以不应该覆盖原来的值
get_template_attribute(Id, Index, Result, Default, Icon, Display) ->
    case get_template(Id) of
        {error, _} ->
            Result;
        Temp ->
            Derived = proplists:get_value(derived, Temp),
            Attrs = [[{hierarchy, {proplists:get_value(alias, Temp), Index}}|X] || X<- proplists:get_value(attrs, Temp, [])],
            Value = proplists:get_value(value, Temp, []),
            NewDefault = parse_value(Value, Default),
            NewIcon = if
                Icon/="" ->
                    Icon;
                true ->
                    proplists:get_value(icon, Temp, "")
            end,
            NewDisplay = if
                Display/="" ->
                    Display;
                true ->
                    proplists:get_value(display, Temp, "")
            end,
            if
                (Derived==undefined) -> %根节点
                    {set_default_value(Result++Attrs, NewDefault, []), NewIcon, NewDisplay};
                true ->
                    case get_template_by_alias(Derived) of
                        {error, _} ->
                            {set_default_value(Result++Attrs, NewDefault, []), NewIcon, NewDisplay};
                        Parent ->
                            Pid = proplists:get_value(id, Parent),
                            get_template_attribute(Pid, Index+1, Result++Attrs, NewDefault, NewIcon, NewDisplay)
                    end
            end
    end.
    
is_offspring(Alias, Parent) ->
    case get_template_by_alias(Alias) of
        {error, _} ->
            false;
        Temp ->
            case proplists:get_value(derived, Temp) of
                Parent ->
                    true;
                Other ->
                    is_offspring(Other, Parent)
            end
    end.
    
get_attribute_id_by_alias([], _) -> {error, not_found};
get_attribute_id_by_alias([F|R], Alias) ->
    case proplists:get_value(alias, F) of
        Alias ->
            proplists:get_value(id, F);
        _ ->
            get_attribute_id_by_alias(R, Alias)
    end.
            
    
parse_child([], Result) ->Result;
parse_child([F|R], Result) ->
    C = case get_child_template(proplists:get_value(id, F)) of
        {error, _} ->
            [];
        Childs ->
            Childs
    end,
    parse_child(C++R, Result++[F]).
    
%
parse_value([], Default) ->Default;
parse_value([{K,V}|R],Default) ->
    NR = case proplists:get_value(K, Default) of
        undefined ->
            [{K,V}|Default];
        _ ->
            Default
    end,
    parse_value(R, NR);
parse_value([_|R], Default) ->
    parse_value(R, Default).

add_id(Attrs) ->
    add_id(Attrs, []).

add_id([], Result) ->Result;
add_id([F|R], Result) ->
    NewF = case proplists:get_value(id, F) of
        undefined ->
            [{id, asset_util:get_id(?ATTR)}|F];
        _ ->
            F
    end,
    add_id(R, [NewF|Result]).
    
set_default_value([], _, Result) ->Result;
set_default_value([F|R], Value, Result) ->
    Alias = proplists:get_value(alias, F),
    NR = case proplists:get_value(Alias, Value) of
        undefined ->
            [F|Result];
        Val ->
            NF = lists:keyreplace(default, 1, F, {default, Val}),
            [NF|Result]
    end,
    set_default_value(R, Value, NR).