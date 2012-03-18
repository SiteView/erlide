-module(conditions_analyse).
-compile(export_all).
-include("asset.hrl").
-include("erl_cmdb.hrl").
-include("../../core/include/monitor.hrl").

test1()->
    SS="
    ((((Email Account !like asd)|((Escalation = false)&(test > 1111)))|((CC !like ggg)&(Due Date > 1/12/11)))&(((Company < Ala Bala)&(Escalation != false))&((# >= asd)|(Company != EURO))))",
    SS1 = trim_t_n(SS),
    io:format("~n SS1:~p ",[SS1]),
    analyse(SS1).

%%主解析函数
analyse(Condition)->
    ConditionStr = trim(Condition),
    case length(ConditionStr) > 0 of
        true->
            FirstStr = string:substr(ConditionStr,1,1),
            case FirstStr   =:= "(" of
                true->
                    EndIndex = compare_bracket_opposite(ConditionStr,1),
                    io:format("~n EndIndex:~p ",[EndIndex]),
                    FirstSubStr = string:substr(ConditionStr,1,EndIndex),
                    io:format("~n FirstSubStr:~p ",[FirstSubStr]),
                    LastOperator = back_operator(ConditionStr,EndIndex),
                    io:format("~n LastOperator:~p ",[LastOperator]),
                    case LastOperator of
                        {"&",LastConnectOperatorIndex}->
                            LastBracket = back_operator(ConditionStr,LastConnectOperatorIndex),
                            io:format("~n LastBracket_and:~p ",[LastBracket]),
                            case LastBracket of
                                {"(",LastBracketIndex}->
                                    EndBracketIndex = compare_bracket_opposite(ConditionStr,LastBracketIndex),
                                    LastSubStr = string:substr(ConditionStr,LastBracketIndex,EndBracketIndex-LastBracketIndex+1),
                                    io:format("~n LastSubStr:~p ",[LastSubStr]),
                                    FistRecord = analyse(FirstSubStr),
                                    LastRecord = analyse(LastSubStr),
                                    #item_and_group_contraint{attribute_value_constraints=[FistRecord,LastRecord]};
                                _->{error,"error operator"}
                            end;
                        {"|",LastConnectOperatorIndex}->
                             LastBracket = back_operator(ConditionStr,LastConnectOperatorIndex),
                             io:format("~n LastBracket_or:~p ",[LastBracket]),
                            case LastBracket of
                                {"(",LastBracketIndex}->
                                    EndBracketIndex = compare_bracket_opposite(ConditionStr,LastBracketIndex),
                                    LastSubStr = string:substr(ConditionStr,LastBracketIndex,EndBracketIndex-LastBracketIndex+1),
                                     io:format("~n LastSubStr:~p ",[LastSubStr]),
                                    FistRecord = analyse(FirstSubStr),
                                    LastRecord = analyse(LastSubStr),
                                    #item_or_group_contraint{attribute_value_constraints=[FistRecord,LastRecord]};
                                _->{error,"error operator"}
                            end;
                        {error,"no operator"}-> %%原子过滤器
                            SubStr = string:substr(FirstSubStr,2,length(FirstSubStr)-2),
                            io:format("~n SubStr :~p ",[SubStr]),
                            case string:str(SubStr,"&") > 0 orelse string:str(SubStr,"|") > 0 of 
                            true->
                                %%里面还含有连接符号.所以,去掉括号继续递归解析
                                analyse(SubStr);
                            _->
                                %%里面不含有连接符，说明已经是个过滤的原子操作了，所以取出来放入record中。
                                assemly_contraint_record(SubStr)
                            end;
                        _->{error,"error operator"}
                     end;   
                _->
                    {error,"branket is error"}
            end;
        _->[]
    end.

%%用来找出连接符前面的反括号
get_forword_branket(Constr,Branket_Index)->
    Forword_operator = forword_operator(Constr,Branket_Index),
    case Forword_operator of
        {")",Index}->Index;
        _->{error,"null branket"}
    end.
    
%% 将分解出来的一个过滤器原子拼装到 attribute_value_constraint 操作符包含:"!like","like","!=","=",">",">=","<","<="

assemly_contraint_record(ContraintStr)->
    case contains(ContraintStr,"!like") of
    true->
        Value_Nlike = get_value(ContraintStr,"!like"),
        #attribute_value_constraint{alias=get_alias(ContraintStr,"!like"),operation="!like",value=Value_Nlike,value_type=get_type(Value_Nlike)};
    _->
        case contains(ContraintStr,"like") of
        true->
            Value_like = get_value(ContraintStr,"like"),
             #attribute_value_constraint{alias=get_alias(ContraintStr,"like"),operation="like",value= Value_like,value_type=get_type(Value_like)};
        _->
            case contains(ContraintStr,"!=") of
            true->
                Value_NEquel = get_value(ContraintStr,"!="),
                #attribute_value_constraint{alias=get_alias(ContraintStr,"!="),operation="!=",value= Value_NEquel,value_type=get_type(Value_NEquel)};
            _->
                case contains(ContraintStr,">=") of
                true->
                    Value_GreaterEquel = get_value(ContraintStr,">="),
                    #attribute_value_constraint{alias=get_alias(ContraintStr,">="),operation=">=",value= Value_GreaterEquel,value_type=get_type(Value_GreaterEquel)};
                    _->
                        case contains(ContraintStr,"<=") of
                        true->
                            Value_LessEquel = get_value(ContraintStr,"<="),
                            #attribute_value_constraint{alias=get_alias(ContraintStr,"<="),operation="<=",value= Value_LessEquel,value_type=get_type(Value_LessEquel)};
                        _->
                        case contains(ContraintStr,">") of
                        true->
                            Value_Greater = get_value(ContraintStr,">"),
                            #attribute_value_constraint{alias=get_alias(ContraintStr,">"),operation=">",value= Value_Greater,value_type=get_type(Value_Greater)};
                        _->
                            case contains(ContraintStr,"<") of
                            true->
                                Value_Less = get_value(ContraintStr,"<"),
                                #attribute_value_constraint{alias=get_alias(ContraintStr,"<"),operation="<",value= Value_Less,value_type=get_type(Value_Less)};
                            _->
                                 case contains(ContraintStr,"=") of
                                true->
                                    Value_Equel = get_value(ContraintStr,"="),
                                    #attribute_value_constraint{alias=get_alias(ContraintStr,"="),operation="=",value= Value_Equel,value_type=get_type(Value_Equel)};
                                _->
                                    {error,"know operator"}
                                end
                            end
                        end
                    end
                end
            end
        end
    end.

%% 获得当前index前面的逻辑控制符:"&","|","(",")"
forword_operator(ConStr,Index)->
    case Index>0 of
    true->
        case string:substr(ConStr,Index-1,1) of 
        []->forword_operator(ConStr,Index-1);
        "("->{"(",Index-1};
        "&"->{"&",Index-1};
        "|"->{"|",Index-1};
        ")"->{")",Index-1};
        _->{error,"know operate"}
        end;
    _->
        {error,"no operator"}
    end.

%% 找到与反括号匹配的那个正括号的位置
compare_bracket(ConStr,Index)->
   compare_bracket_t(ConStr,Index,1).
compare_bracket_t(ConStr,Index,0)->Index;
compare_bracket_t(ConStr,Index,N)->
     case Index>0 of
    true->
        case string:substr(ConStr,Index-1,1) of 
            "("->compare_bracket_t(ConStr,Index-1,N-1);
            ")"->compare_bracket_t(ConStr,Index-1,N+1);
            _->compare_bracket_t(ConStr,Index-1,N)
        end;
    _->
        {error,"no operator"}
    end.

%% 找到与反括号匹配的那个反括号的位置
compare_bracket_opposite(ConStr,Index)->
   compare_bracket_opposite_t(ConStr,Index,1).
compare_bracket_opposite_t(ConStr,Index,0)->Index;
compare_bracket_opposite_t(ConStr,Index,N)->
     case Index>0 andalso Index<length(ConStr)+1 of
    true->
        case string:substr(ConStr,Index+1,1) of 
            "("->compare_bracket_opposite_t(ConStr,Index+1,N +1);
            ")"->compare_bracket_opposite_t(ConStr,Index+1,N-1);
            _->compare_bracket_opposite_t(ConStr,Index +1,N)
        end;
    _->
        {error,"no operator"}
    end.

%% 获得当前index前面的逻辑控制符:"&","|","(",")"
back_operator(ConStr,Index)->
    case Index>0 andalso Index<length(ConStr)+1 of
    true->
        case string:substr(ConStr,Index+1,1) of 
            ")"->{")",Index+1};
            "&"->{"&",Index+1};
            "|"->{"|",Index+1};
            "("->{"(",Index+1};
            _->back_operator(ConStr,Index+1)
        end;
    _->
        {error,"no operator"}
    end. 

contains(Str,ContainStr)->
   case string:str(Str,ContainStr)>0 of
    true->
        true;
    _->
        false
    end.

get_alias(String,Operator)->
    Index = string:str(String,Operator),
    trim(string:sub_string(String,1,Index-1)). 

get_value(String,Operator)->
    Index = string:str(String,Operator),
    trim(string:substr(String,Index+string:len(Operator)
    )). 

get_type(Value)->
    try 
        list_to_integer(Value),
        "integer"
    catch
        _:_->
        try
            list_to_float(Value),
            "float"
        catch
            _:_->
            "string"
        end
    end.

trim(String)->
    string:strip(String,both,$ ). 

trim_t_n(String)->
    Index = string:str(String,"\r\n"),
    case Index>0 of
        true->
            String2 = string:substr(String,1,Index-1)++string:substr(String,Index+4),
            trim_t_n(String2);
        _->
        String
    end.
    
%%用来移除括号及括号里面的类容
%%remove_bracket(Constr,Branket_Index)->
%%    Forword_operator = forword_operator(Constr,Branket_Index),
%%    case Forword_operator of
%%    {"(",Operator_Index}->
%%        remove_bracket(trim(string:substr(OtherStr,1,SplitIndex-1)) ++ trim(string:substr(OtherStr,IndexE1+1)),Operator_Index);
%%    {Operator,Index}->
%%        {trim(string:substr(OtherStr,1,SplitIndex-1)) ++ trim(string:substr(OtherStr,IndexE1+1)),Operator,Index};
%%    _->
%%        {error,"error operator"}
%%    end.
         
%%将不含括号的操作封转到record中
%%    get_contraint_record(WhereCondition)->
%%    case length(WhereCondition)>0 of
%%    true->
%%        case string:str("&")>0 of
%%        true->
%% 用& 将他分开，让后拼装到 item_and_group_contraint
%%        Conditions = string:tokens(WhereCondition,"&"),
%%        #item_and_group_contraint{attribute_value_constraints=[assemly_contraint_record(lists:nth(1,Conditions)),assemly_contraint_record(lists:nth(2,Conditions))]};
%%        _->
%%            case string:str("|")>0 of
%%            true->
            %% 用|将他分开,然后拼装到 item_or_group_contraint
%%            Conditions = string:tokens(WhereCondition,"|"),
%%            #item_or_group_contraint{attribute_value_constraints=[assemly_contraint_record(lists:nth(1,Conditions)),assemly_contraint_record(lists:nth(2,Conditions))]};
%%            _->
            %%直接拼装到 attribute_value_constraint
%%                assemly_contraint_record(WhereCondition)
%%            end;
%%    _->
%%        {}
%%    end.
