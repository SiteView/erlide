-module(asset_util).
-compile(export_all).
-include("../../core/include/monitor.hrl").
-include("asset.hrl").

%display只替换attribute中alias,display的内容
get_display(Asset, Template) ->
    Rule = proplists:get_value(display, Template),
    if
        is_list(Rule) ->
            parse_rule(Rule, Asset, proplists:get_value(attrs, Template, []),[]);
        true ->
            ""
    end.

% get random id for ci and its attributes
get_id(TYPE)->
	case now() of
		{A,B,C}->
			TYPE++integer_to_list(A)++ ":" ++ integer_to_list(B) ++ ":" ++ integer_to_list(C)++integer_to_list(random:uniform(10000));
		_->
			TYPE++integer_to_list(random:uniform(99999))++integer_to_list(random:uniform(99999))++integer_to_list(random:uniform(999999))
	end.
    
get_time() ->
    {{Y,M,D},{H,Min,S}} = calendar:local_time(),
    lists:flatten(io_lib:format("~p-~p-~p ~p:~p:~p",[Y,M,D,H,Min,S])).
    
get_date({Y, M, D}) ->
    lists:flatten(io_lib:format("~p-~p-~p",[Y,M,D])).
    
get_time({H,M,S}) ->
    lists:flatten(io_lib:format("~p:~p:~p",[H,M,S])).
    
timestamp() ->
    {A,B,C} = os:timestamp(),
    A*1000000000+B*1000+(C div 1000).

parse_rule([], _, _, Result) ->lists:flatten(lists:reverse(Result));
parse_rule("${"++R, Asset, Attrs, Result) ->
    Index = string:str(R, "}"),
    Rv = if
        Index>1 ->
            string:sub_string(R, 1, Index-1);
        true ->
            ""
    end,
    Value = find_attribute_value(Attrs, Rv, Asset),
    if
        Index<length(R) ->
            parse_rule(string:sub_string(R, Index+1), Asset, Attrs, [Value|Result]);
        Index == 0 ->
            parse_rule(R, Asset, Attrs, ["${"|Result]);
        Index == length(R) ->
            parse_rule("", Asset, Attrs, [Value|Result]);
        true ->
            parse_rule(R, Asset, Attrs, [Value|Result])
    end;
parse_rule([F|R], Asset, Attrs, Result) ->
    parse_rule(R, Asset, Attrs, [F|Result]).

find_attribute_value([], Rv, _) ->
    Rv;
find_attribute_value([F|R], Rv, Asset) ->
    IsAttr = ((proplists:get_value(alias, F)==Rv) or (proplists:get_value(display, F)==Rv)),
    if
        IsAttr ->
            Aid = proplists:get_value(id, F),
            case proplists:get_value(Aid, proplists:get_value(attrs, Asset, [])) of
                undefined ->
                    find_attribute_value(R, Rv, Asset);
                Other ->
                    to_string(Other)
            end;
        true ->
            find_attribute_value(R, Rv, Asset)
    end.
    
    
to_string(V) when is_atom(V) ->
    atom_to_list(V);
to_string(V) when is_integer(V) ->
    integer_to_list(V);
to_string(V) when is_list(V) ->
    V;
to_string(V) ->
    lists:flatten(io_lib:format("~p",[V])).
    
to_numeric(V) when is_number(V) ->
    V;
to_numeric(V) when is_list(V) ->
    R1 = try list_to_integer(V) of
        NV -> NV
    catch
    _:_ -> V
    end,
    if
        is_integer(R1) ->
            R1;
        true ->
            try list_to_float(R1) of
                FR -> FR
            catch
            _:_ -> 0
            end
    end.
    
to_bool(V) when is_atom(V) ->
    if
        ((V==true) or (V==false)) ->
            V;
        true ->
            false
    end;
to_bool(V) when is_list(V) ->
    to_bool(list_to_atom(V));
to_bool(_) ->
    false.


gv(K,L) ->
    proplists:get_value(K,L).
    
gv(K,L,D) ->
    case proplists:get_value(K,L) of
        undefined ->
            D;
        O ->
            O
    end.
    
%% *****************************************************************************
%% **********通用处理, （组装成数据库的Where和Order）
%% *****************************************************************************
build_condition(Condition=#query_condition1{}) ->
    Index = Condition#query_condition1.index,
    Count = Condition#query_condition1.count,
    Where = 
        case Condition#query_condition1.where of
            ConWhere = #query_condition_where{} ->
                ParseWhere = parse_where_condition(ConWhere#query_condition_where.where),
                ParseWhere1 = string:strip(ParseWhere),
                LastStr = 
                    case ParseWhere1 of
                        [] ->
                            [];
                        _ ->
                            string:substr(ParseWhere1, string:len(ParseWhere1))
                    end,
                %%io:format("LastStr = ~p~n", [LastStr]),
                if
                    LastStr=:="&" ->
                        string:substr(ParseWhere1, 1, string:len(ParseWhere1)-2);
                    LastStr=:="|" ->
                        string:substr(ParseWhere1, 1, string:len(ParseWhere1)-2);
                    true ->
                        ParseWhere1
                end;
            OWhere ->
                OWhere
        end,
    %%io:format("Where = ~p~n", [Where]),
    Sort = Condition#query_condition1.sort,
    SortType = Condition#query_condition1.sortType,
    SortContent =
    case Sort of
        [] ->
            [];
        _V4 when erlang:is_list(Sort) ->
            "&order=my." ++ Sort++SortType;
        _ ->
            []
    end,
    IndexStr = 
        try erlang:integer_to_list(Index) of
            V when erlang:is_list(V) ->
                V;
            _ ->
                "0"
        catch
            _:_ ->
                "0"
        end,
    CountStr = 
        try erlang:integer_to_list(Index+Count) of
            V1 when erlang:is_list(V1) ->
                V1;
            _ ->
                "0"
        catch
            _:_ ->
                "0"
        end,
    Order = "from="++IndexStr++"&to="++CountStr++SortContent,%%++"&order=my."++Order,
    #query_beam_condition1{
        where=Where,
        order=Order
    }.

%% 解析查询条件Where
parse_where_condition([]) ->
    [];
parse_where_condition([ConditionWhere=#query_condition_where{}|T]) ->
    "(" ++ parse_where_condition(ConditionWhere#query_condition_where.where) ++ ")" ++ " | " ++
    parse_where_condition(T);
parse_where_condition([{Field,Operation,Value,Relation}|T]) ->
    case string:strip(Relation) of
        "" ->
            lists:append(lists:append([Field," ",Operation," ", Value]),
                            parse_where_condition(T));
        Re ->
            lists:append(lists:append([Field," ",Operation, " ", Value, " ", Re, " "]),
                            parse_where_condition(T))
    end;
parse_where_condition([_|T]) ->
    parse_where_condition(T).
