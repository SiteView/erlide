-module(engywise_api).

-compile(export_all).

-include("es_util.hrl").
-include("energyWise.hrl").

%% Functions 

-define(ENGYWISE_CreateSession, ?API_HRL +1).
-define(ENGYWISE_CloseSession, ?ENGYWISE_CreateSession +1).
-define(ENGYWISE_CreateQuery, ?ENGYWISE_CloseSession +1).
-define(ENGYWISE_CreateSetQuery, ?ENGYWISE_CreateQuery +1).
-define(ENGYWISE_CreateCollectQuery, ?ENGYWISE_CreateSetQuery +1).
-define(ENGYWISE_CreateSumQuery, ?ENGYWISE_CreateCollectQuery +1).
-define(ENGYWISE_AddGetAttribute, ?ENGYWISE_CreateSumQuery +1).
-define(ENGYWISE_AddSetAttribute, ?ENGYWISE_AddGetAttribute +1).
-define(ENGYWISE_AddQualifier, ?ENGYWISE_AddSetAttribute +1).
-define(ENGYWISE_AddNameQualifier, ?ENGYWISE_AddQualifier +1).
-define(ENGYWISE_AddKeyQualifier, ?ENGYWISE_AddNameQualifier +1).
-define(ENGYWISE_AddRoleQualifier, ?ENGYWISE_AddKeyQualifier +1).
-define(ENGYWISE_AddIdQualifier, ?ENGYWISE_AddRoleQualifier +1).
-define(ENGYWISE_ExecQuery, ?ENGYWISE_AddIdQualifier +1).
-define(ENGYWISE_QueryResults, ?ENGYWISE_ExecQuery +1).
-define(ENGYWISE_GetNextRow, ?ENGYWISE_QueryResults +1).
-define(ENGYWISE_GetAttributeFromRowByType, ?ENGYWISE_GetNextRow +1).
-define(ENGYWISE_GetAckCount, ?ENGYWISE_GetAttributeFromRowByType +1).
-define(ENGYWISE_GetNakCount, ?ENGYWISE_GetAckCount +1).
-define(ENGYWISE_GetAcks, ?ENGYWISE_GetNakCount +1).
-define(ENGYWISE_GetNaks, ?ENGYWISE_GetAcks +1).
-define(ENGYWISE_GetRowCount, ?ENGYWISE_GetNaks +1).
-define(ENGYWISE_GetErrorCode, ?ENGYWISE_GetRowCount +1).
-define(ENGYWISE_ReleaseResult, ?ENGYWISE_GetErrorCode +1).
-define(ENGYWISE_ReleaseQuery, ?ENGYWISE_ReleaseResult +1).



-import(energywise, [cast/2,call/2]).

createSession(Targetaddr, Targetport, Id, Key, Key_len) ->
    EId = Id#energywise_sender_id_t.id,
    EPhy_idx = Id#energywise_sender_id_t.phy_idx,
    Args = {Targetaddr, Targetport, {EId, EPhy_idx}, Key, Key_len},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_CreateSession, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    

closeSession(Session) ->
    Args = {Session},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_CloseSession, ArgsBin),
    ok.
    

createQuery(Ew_class, Action, Domain, Importance) ->
    Args = {Ew_class, Action, Domain, Importance},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_CreateQuery, ArgsBin),
    erlang:binary_to_term(Res).
    
    
createSetQuery(Domain, Importance) ->
    Args = {Domain, Importance},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_CreateSetQuery, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
createCollectQuery(Domain, Importance) ->
    Args = {Domain, Importance},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_CreateCollectQuery, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
createSumQuery(Domain, Importance) ->
    Args = {Domain, Importance},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_CreateSumQuery, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
addGetAttribute(Query, Type) ->
    Args = {Query, Type},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_AddGetAttribute, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
addSetAttribute(Query, Type, ValueAddr) ->
    Args = {Query, Type, ValueAddr},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_AddSetAttribute, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
addQualifier(Query, Type, ValueAddr) ->
    Args = {Query, Type, ValueAddr},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_AddQualifier, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
addNameQualifier(Query, Name) ->
    Args = {Query, Name},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_AddNameQualifier, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
addKeyQualifier(Query, Keys) ->
    Args = {Query, Keys},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_AddKeyQualifier, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
addRoleQualifier(Query, Role) ->
    Args = {Query, Role},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_AddRoleQualifier, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
addIdQualifier(Query, Id) ->
    EId = Id#energywise_sender_id_t.id,
    EPhy_idx = Id#energywise_sender_id_t.phy_idx,
    Args = {Query, {EId, EPhy_idx}},
    %%io:format("Args: ~p~n", [Args]),
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_AddIdQualifier, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
execQuery(Session, Query) ->
    Args = {Session, Query},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_ExecQuery, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
queryResults(Session, Query) ->
    Args = {Session, Query},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_QueryResults, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
getNextRow(Resultset) ->
    Args = {Resultset},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_GetNextRow, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
getAttributeFromRowByType(Result_row, Attribute, Length) ->
    Args = {Result_row, Attribute, Length},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_GetAttributeFromRowByType, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    R = erlang:binary_to_term(Res),
    if
        Attribute == ?EW_ATTRIBUTE_TYPE_ENERGYWISE_ID ->
            {Id, Phy_idx} = 
                case R of
                    {_Id, _Phy_idx} ->
                        {_Id, _Phy_idx};
                    _ ->
                        {"", 0}
                end,
            #energywise_sender_id_t{
                id = Id,
                phy_idx = Phy_idx
            };
        true ->
            R
    end.
    
    
getAckCount(Resultset) ->
    Args = {Resultset},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_GetAckCount, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
getNakCount(Resultset) ->
    Args = {Resultset},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_GetNakCount, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
getAcks(Resultset, Count) ->
    Args = {Resultset, Count},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_GetAcks, ArgsBin),
    Acks = erlang:binary_to_term(Res),
    es_util:procAcksToList(Acks).
    
    
getNaks(Resultset, Count) ->
    Args = {Resultset, Count},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_GetNaks, ArgsBin),
    Acks = erlang:binary_to_term(Res),
    es_util:procAcksToList(Acks).
    
    
getRowCount(Resultset) ->
    Args = {Resultset},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_GetRowCount, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
getErrorCode(Resultset) ->
    Args = {Resultset},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_GetErrorCode, ArgsBin),
    %%io:format("Res :~p~n", [Res]),
    erlang:binary_to_term(Res).
    
    
releaseResult(Resultset) ->
    Args = {Resultset},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_ReleaseResult, ArgsBin),
    ok.
    
    
releaseQuery(Query) ->
    Args = {Query},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_ReleaseQuery, ArgsBin),
    ok.