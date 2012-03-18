-module(queryTests).

-compile(export_all).

-include("es_util.hrl").
-include("energyWise.hrl").

%% test api
%% Test PDU w/query for usage
%%
%% Equivalent CLI: energywise query imp 100 name * collect usage
%%
testGetUsageQuery(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    io:format("caining~n"),
    io:format("Id: ~p~n", [Id]),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    io:format("Digest: ~p~n", [Digest]),
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    Query = engywise_api:createCollectQuery(Domain, 100),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_NAME),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_ROLE),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_USAGE),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_UNITS),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_LEVEL),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_DEVICE_TYPE),
    
    engywise_api:addQualifier(Query, ?EW_ATTRIBUTE_TYPE_QUERY_TIMEOUT, 6),
    
    if
        Name /= [] ->
            engywise_api:addNameQualifier(Query, Name);
        true ->
            ok
    end,
    if
        Keywords /= [] ->
            engywise_api:addKeyQualifier(Query, Keywords);
        true ->
            ok
    end,
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    
	io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    io:format("~nEnd queryResults...~n"),
    
    %%Row = engywise_api:getNextRow(Resultsets),
    %%io:format("Row: ~p~n", [Row]),
    
    circle_row_GetUsageQuery(Resultsets, 1),
    io:format("kkkk~n"),
    %% release
    engywise_api:releaseResult(Resultsets),
    io:format("yun~n"),
    engywise_api:releaseQuery(Query),
    io:format("yun1~n"),
    engywise_api:closeSession(Session),
    io:format("yun2~n"),
    ok.

%% test api
%% Test PDU w/query for delta vector
%%
testGetDeltaQuery(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    Query = engywise_api:createCollectQuery(Domain, 100),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_DELTA_VECTOR),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_NAME),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_ROLE),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_UNITS),
    
    if
        Name /= [] ->
            engywise_api:addNameQualifier(Query, Name);
        true ->
            ok
    end,
    if
        Keywords /= [] ->
            engywise_api:addKeyQualifier(Query, Keywords);
        true ->
            ok
    end,
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    io:format("~nEnd queryResults...~n"),
    
    io:format("Delta usage (Watts) @ Levels ~n"),
    io:format("Host            Name            0      1      2      3      4      5      6      7      8      9      10~n"),
    io:format("----            ----            ------------------------------------------------------------------------~n"),
    
    circle_row_GetDeltaQuery(Resultsets, 1),
    %% release
    engywise_api:releaseResult(Resultsets),
    engywise_api:releaseQuery(Query),
    engywise_api:closeSession(Session),
    ok.

%% test api
%% Test PDU w/sum query for usage
%%
testSumQuery(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    io:format("~ndest: ~p~n", [Dest]),
    io:format("~nport: ~p~n", [?ENERGYWISE_DEFAULT_PORT]),
    io:format("~nsecret: ~p~n", [Digest]),
    io:format("~nsecret_len: ~p~n", [string:len(Digest)]),
    io:format("~nCreate Session Ok~n"),
    
    Query = engywise_api:createSumQuery(Domain, 100),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_USAGE),
    
    io:format("~nBegin exeQuery...~n"),
    
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    io:format("~nnotSendCount: ~p~n", [NotSendLength]),
    io:format("~nEnd exeQuery...~n"),
    
	io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    io:format("~nEnd queryResults...~n"),

	io:format("~nBegin getNextRow...~n"),
    
    Row = engywise_api:getNextRow(Resultsets),
    %%io:format("Row: ~p~n", [Row]),
    ADomain = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_DOMAIN, 0),
    %%io:format("ADomain: ~p~n", [ADomain]),
    AUsage = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_USAGE, 0),
    %%io:format("AUsage: ~p~n", [AUsage]),
    AUnit = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_UNITS, 0),
    %%io:format("AUnit: ~p~n", [AUnit]),
    ASender = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_REPLY_TO, 0),
    {_,_,Addr,_} = ASender,
    %%io:format("ASender: ~p~n", [ASender]),
    
    io:format("*****************~n"),
    %%io:format("NotSendLength: ~p~n", [NotSendLength]),
    %%io:format("Resultsets: ~p~n", [Resultsets]),
    %%io:format("Row: ~p~n", [Row]),
    %%io:format("Result: ~n"),
    io:format("row ~p Results: Domain = ~p, Usage = ~p, Units = ~p, Addr = ~p~n", [1, ADomain, AUsage, AUnit, Addr]),
    io:format("*****************~n"),
    %% release
    engywise_api:releaseResult(Resultsets),
    engywise_api:releaseQuery(Query),
    engywise_api:closeSession(Session),
    ok.

%% test api
%% Test PDU w/set query for level
%%
testSetQuery(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    Level = 6,
    
    Query = engywise_api:createSetQuery(Domain, 100),
    io:format("Query: ~p~n", [Query]),
    engywise_api:addSetAttribute(Query, ?EW_ATTRIBUTE_TYPE_NAME, "caining1"),
    engywise_api:addSetAttribute(Query, ?EW_ATTRIBUTE_TYPE_ROLE, "caining1"),
    engywise_api:addSetAttribute(Query, ?EW_ATTRIBUTE_TYPE_KEYWORDS, "caining1"),
    
    engywise_api:addSetAttribute(Query, ?EW_ATTRIBUTE_TYPE_IMPORTANCE, Level),
    engywise_api:addSetAttribute(Query, ?EW_ATTRIBUTE_TYPE_LEVEL, Level),
    
    io:format("iiiii~n"),
    
    if
        Name /= [] ->
            engywise_api:addNameQualifier(Query, Name);
        true ->
            ok
    end,
    if
        Keywords /= [] ->
            engywise_api:addKeyQualifier(Query, Keywords);
        true ->
            ok
    end,
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    io:format("NotSendLength: ~p~n", [NotSendLength]),
    
    io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    io:format("~nEnd queryResults...~n"),
    
    io:format("Resultsets: ~p~n", [Resultsets]),
    
    Count = engywise_api:getAckCount(Resultsets),
    
    io:format("Count: ~p~n", [Count]),
    
    LIds = engywise_api:getAcks(Resultsets, Count),
    io:format("LIds: ~p~n", [LIds]),
    
    %% release
    engywise_api:releaseResult(Resultsets),
    engywise_api:releaseQuery(Query),
    engywise_api:closeSession(Session),
    ok.

%% test api
testGetRecurrence(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    Query = engywise_api:createCollectQuery(Domain, 100),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_RECURRENCE),
    
    if
        Name /= [] ->
            engywise_api:addNameQualifier(Query, Name);
        true ->
            ok
    end,
    if
        Keywords /= [] ->
            engywise_api:addKeyQualifier(Query, Keywords);
        true ->
            ok
    end,
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    
	io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    io:format("~nEnd queryResults...~n"),
    
    Count = engywise_api:getAckCount(Resultsets),
    
    io:format("Count: ~p~n", [Count]),
    
    LIds = engywise_api:getAcks(Resultsets, Count),
    
    io:format("LIds: ~p~n", [LIds]),
    
    NakCount = engywise_api:getNakCount(Resultsets),
    
    io:format("NakCount: ~p~n", [NakCount]),
    
    NakLIds = engywise_api:getNaks(Resultsets, NakCount),
    
    io:format("NakLIds: ~p~n", [NakLIds]),
    
    %%Row = engywise_api:getNextRow(Resultsets),
    %%io:format("Row: ~p~n", [Row]),
    
    %% release
    engywise_api:releaseResult(Resultsets),
    engywise_api:releaseQuery(Query),
    engywise_api:closeSession(Session),
    ok.

%% test api
testSetRecurrence(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    
    R ={
        "0 5 * * *",
        5,
        100,
        0
    },
    
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    Query = engywise_api:createSetQuery(Domain, 100),
    io:format("Query: ~p~n", [Query]),
    engywise_api:addSetAttribute(Query, ?EW_ATTRIBUTE_TYPE_RECURRENCE, R),
    
    io:format("caining~n"),
    
    if
        Name /= [] ->
            engywise_api:addNameQualifier(Query, Name);
        true ->
            ok
    end,
    if
        Keywords /= [] ->
            engywise_api:addKeyQualifier(Query, Keywords);
        true ->
            ok
    end,
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    io:format("NotSendLength: ~p~n", [NotSendLength]),
    
    io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    io:format("~nEnd queryResults...~n"),
    
    io:format("Resultsets: ~p~n", [Resultsets]),
    
    Count = engywise_api:getAckCount(Resultsets),
    
    io:format("Count: ~p~n", [Count]),
    
    LIds = engywise_api:getAcks(Resultsets, Count),
    io:format("LIds: ~p~n", [LIds]),
    
    NakCount = engywise_api:getNakCount(Resultsets),
    
    io:format("NakCount: ~p~n", [NakCount]),
    
    NakLIds = engywise_api:getNaks(Resultsets, NakCount),
    
    io:format("NakLIds: ~p~n", [NakLIds]),
    
    %% release
    engywise_api:releaseResult(Resultsets),
    engywise_api:releaseQuery(Query),
    engywise_api:closeSession(Session),
    ok.

%% test api
testDeleteRecurrence(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    
    R ={
        "0 5 * * *",
        5,
        100,
        1               %%/* set TRUE to delete this recurrence */
    },
    
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    Query = engywise_api:createSetQuery(Domain, 100),
    io:format("Query: ~p~n", [Query]),
    engywise_api:addSetAttribute(Query, ?EW_ATTRIBUTE_TYPE_RECURRENCE, R),
    
    io:format("caining~n"),
    
    if
        Name /= [] ->
            engywise_api:addNameQualifier(Query, Name);
        true ->
            ok
    end,
    if
        Keywords /= [] ->
            engywise_api:addKeyQualifier(Query, Keywords);
        true ->
            ok
    end,
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    io:format("NotSendLength: ~p~n", [NotSendLength]),
    
    io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    io:format("~nEnd queryResults...~n"),
    
    io:format("Resultsets: ~p~n", [Resultsets]),
    
    Count = engywise_api:getAckCount(Resultsets),
    
    io:format("Count: ~p~n", [Count]),
    
    LIds = engywise_api:getAcks(Resultsets, Count),
    io:format("LIds: ~p~n", [LIds]),
    
    NakCount = engywise_api:getNakCount(Resultsets),
    
    io:format("NakCount: ~p~n", [NakCount]),
    
    NakLIds = engywise_api:getNaks(Resultsets, NakCount),
    
    io:format("NakLIds: ~p~n", [NakLIds]),
    
    %% release
    engywise_api:releaseResult(Resultsets),
    engywise_api:releaseQuery(Query),
    engywise_api:closeSession(Session),
    ok.


%% test api
%% This function will collect the ID and other attributes from an
%% interface using the name qualifier "foo".  It will then issue a 
%% subsequent query using the ID it just collected as the only 
%% qualifier.  Proper functionality will show the exact same results
%% from both queries.  The first filtered by name "foo", the second filtered
%% by foo's ID.
%%
%% NOTE:  You need to give one of the switch interfaces or endpoints the name
%% "foo" for this exact sample to work, or alternatively change the name
%% qualifier value in this example.
%%
testGetUsageQueryById(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    Query = engywise_api:createCollectQuery(Domain, 100),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_NAME),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_ROLE),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_USAGE),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_UNITS),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_LEVEL),
    engywise_api:addGetAttribute(Query, ?EW_ATTRIBUTE_TYPE_DEVICE_TYPE),
    if
        Name /= [] ->
            engywise_api:addNameQualifier(Query, Name);
        true ->
            ok
    end,
    if
        Keywords /= [] ->
            engywise_api:addKeyQualifier(Query, Keywords);
        true ->
            ok
    end,
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    LocalId = circle_row_GetUsageQuery_Id(Resultsets, 1, Id),
    
    io:format("~nEnd queryResults...~n"),
    
    io:format("LocalId: ~p~n", [LocalId]),
    
    io:format("~nReceived ID from first query.~n"),
    io:format("Using as filter for query 2.~n"),
    io:format("-----~n"),
    
    %% release
    engywise_api:releaseResult(Resultsets),
    engywise_api:releaseQuery(Query),
    
    Query1 = engywise_api:createCollectQuery(Domain, 100),
    engywise_api:addGetAttribute(Query1, ?EW_ATTRIBUTE_TYPE_NAME),
    engywise_api:addGetAttribute(Query1, ?EW_ATTRIBUTE_TYPE_ROLE),
    engywise_api:addGetAttribute(Query1, ?EW_ATTRIBUTE_TYPE_USAGE),
    engywise_api:addGetAttribute(Query1, ?EW_ATTRIBUTE_TYPE_UNITS),
    engywise_api:addGetAttribute(Query1, ?EW_ATTRIBUTE_TYPE_LEVEL),
    engywise_api:addGetAttribute(Query1, ?EW_ATTRIBUTE_TYPE_DEVICE_TYPE),
    
    %%
    engywise_api:addIdQualifier(Query1, LocalId),
    
    
    NotSendCount = engywise_api:execQuery(Session, Query1),
    
    
    io:format("~nBegin queryResults1...~n"),
    
    Resultsets1 = engywise_api:queryResults(Session, Query1),
    
    io:format("~nEnd queryResults1...~n"),
    
    circle_row_GetUsageQuery(Resultsets1, 1),
    
    %% release
    engywise_api:releaseResult(Resultsets1),
    engywise_api:releaseQuery(Query1),
    engywise_api:closeSession(Session),
    
    ok.

%% test api
testSaveQuery(Domain, Secret, Dest, Name, Keywords) ->
    svEnergyWise:start_link(),
    Id = engywise_util:createUuid(),
    Secret_len = string:len(Secret),
    Digest = engywise_util:composeKey(Secret, Secret_len, Id, "", ?SHA_DIGEST_LENGTH),
    Session = engywise_api:createSession(Dest, ?ENERGYWISE_DEFAULT_PORT, Id, Digest, ?SHA_DIGEST_LENGTH),
    
    Query = engywise_api:createQuery(?EW_CLASS_QUERY, ?EW_ACTION_QUERY_SAVE, Domain, 100),
    
    io:format("Query: ~p~n", [Query]),
    
    if
        Name /= [] ->
            engywise_api:addNameQualifier(Query, Name);
        true ->
            ok
    end,
    if
        Keywords /= [] ->
            engywise_api:addKeyQualifier(Query, Keywords);
        true ->
            ok
    end,
    NotSendLength = engywise_api:execQuery(Session, Query),
    
    io:format("~nBegin queryResults...~n"),
    
    Resultsets = engywise_api:queryResults(Session, Query),
    
    io:format("~nEnd queryResults...~n"),
    
    Count = engywise_api:getAckCount(Resultsets),
    
    io:format("Count: ~p~n", [Count]),
    
    LIds = engywise_api:getAcks(Resultsets, Count),
    io:format("LIds: ~p~n", [LIds]),
    
    NakCount = engywise_api:getNakCount(Resultsets),
    
    io:format("NakCount: ~p~n", [NakCount]),
    
    NakLIds = engywise_api:getNaks(Resultsets, NakCount),
    
    io:format("NakLIds: ~p~n", [NakLIds]),
    
    %% release
    engywise_api:releaseResult(Resultsets),
    engywise_api:releaseQuery(Query),
    engywise_api:closeSession(Session),
    ok.

    

circle_row_GetRecurrence(Resultsets, I) ->
    Row = engywise_api:getNextRow(Resultsets),
    io:format("Row: ~p~n", [Row]),
    if
        Row /= 0 ->
            Id = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_ENERGYWISE_ID, 0),
            {Cron, Level, Importance, Remove} = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_RECURRENCE, 0),
            io:format("~p ~p ~p ~p ~p~n", Cron, Level, Importance, Id#energywise_sender_id_t.id, Id#energywise_sender_id_t.phy_idx),
            circle_row_GetUsageQuery(Resultsets, I+1),
            ok;
        true ->
            []
    end.

    
circle_row_GetUsageQuery(Resultsets, I) ->
    Row = engywise_api:getNextRow(Resultsets),
    io:format("Row: ~p~n", [Row]),
    if
        Row /= 0 ->
            AName = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_NAME, 0),
            ARole = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_ROLE, 0),
            AUsage = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_USAGE, 0),
            ALevel = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_LEVEL, 0),
            AUnit = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_UNITS, 0),
            ADomain = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_DOMAIN, 0),
            ASender = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_REPLY_TO, 0),
            {_,_,Addr,_} = ASender,
            ADeviceType = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_DEVICE_TYPE, 0),
            Id = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_ENERGYWISE_ID, 0),
            io:format("Domain = ~p, ", [ADomain]),
            io:format("Name = ~p, ", [AName]),
            io:format("Role = ~p, ", [ARole]),
            io:format("Usage = ~p {~p}, ", [AUsage, AUnit]),
            io:format("Level = ~p, ", [ALevel]),
            io:format("Addr = ~p, ", [Addr]),
            io:format("Type = ~p~n", [ADeviceType]),
            circle_row_GetUsageQuery(Resultsets, I+1),
            ok;
        true ->
            []
    end.
    
circle_row_GetUsageQuery_Id(Resultsets, I, LocalId) ->
    Row = engywise_api:getNextRow(Resultsets),
    if
        Row /= 0 ->
            AName = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_NAME, 0),
            ARole = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_ROLE, 0),
            AUsage = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_USAGE, 0),
            ALevel = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_LEVEL, 0),
            AUnit = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_UNITS, 0),
            ADomain = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_DOMAIN, 0),
            ASender = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_REPLY_TO, 0),
            {_,_,Addr,_} = ASender,
            ADeviceType = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_DEVICE_TYPE, 0),
            Id = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_ENERGYWISE_ID, 0),
            io:format("row ~p Results: ", [I]),
            io:format("Domain = ~p, ", [ADomain]),
            io:format("Name = ~p, ", [AName]),
            io:format("Role = ~p, ", [ARole]),
            io:format("Usage = ~p {~p}, ", [AUsage, AUnit]),
            io:format("Level = ~p, ", [ALevel]),
            io:format("Addr = ~p, ", [Addr]),
            io:format("Type = ~p~n", [ADeviceType]),
            
            circle_row_GetUsageQuery_Id(Resultsets, I+1, Id);
        true ->
            LocalId
    end.
    
    
circle_row_GetDeltaQuery(Resultsets, I) ->
    Row = engywise_api:getNextRow(Resultsets),
    if
        Row /= 0 ->
            AName = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_NAME, 0),
            AUnit = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_UNITS, 0),
            ASender = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_REPLY_TO, 0),
            {_,_,Addr,_} = ASender,
            AVec = engywise_api:getAttributeFromRowByType(Row, ?EW_ATTRIBUTE_TYPE_DELTA_VECTOR, 0),
            io:format("~15p ~15p ~p ", [Addr, AName, AUnit]),
            Units = math:pow(10.0, AUnit),
            circle_row_GetDeltaQuery_t(AVec, Units),
            io:format("~n"),
            circle_row_GetDeltaQuery(Resultsets, I+1),
            ok;
        true ->
            []
    end.
circle_row_GetDeltaQuery_t([], Units) ->
    [];
circle_row_GetDeltaQuery_t([Delta|T], Units) when erlang:is_integer(Delta) ->
    io:format("~6p ", [Delta * Units]),
    circle_row_GetDeltaQuery_t(T, Units);
circle_row_GetDeltaQuery_t([H|T], Units) ->
    circle_row_GetDeltaQuery_t(T, Units).