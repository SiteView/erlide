%%  Copyright (c) 2001 Dan Gudmundsson
%%
%%  See the file "license.terms" for information on usage and redistribution
%%  of this file, and for a DISCLAIMER OF ALL WARRANTIES.
%% 
%%     $Id$
%%
%%% File    : sdl_audio.erl
%%% Author  : Dan Gudmundsson <dgud@compaq.du.uab.ericsson.se>
%%% Purpose : Implements a SDL_AUDIO interface
%%% Created : 9 Aug 2000 by Dan Gudmundsson <dgud@compaq.du.uab.ericsson.se>

-module(engywise_util).

-compile(export_all).

-include("es_util.hrl").
-include("energyWise.hrl").
%%-include("engywise_util.hrl").

%% Functions 

-define(ENGYWISE_Utl_ComposeKey, ?UTIL_HRL +1).
-define(ENGYWISE_Utl_CreateUuid, ?ENGYWISE_Utl_ComposeKey +1).
-define(ENGYWISE_Utl_CreateUuidFromMac, ?ENGYWISE_Utl_CreateUuid +1).
-define(ENGYWISE_Utl_getVersion, ?ENGYWISE_Utl_CreateUuidFromMac +1).
-define(ENGYWISE_Utl_CreateUuidFromDetails, ?ENGYWISE_Utl_getVersion +1).


-import(energywise, [cast/2,call/2]).

%%-include("engywise_util.hrl").

%%-record(audiop,					%Pointer to loaded audio data.
%%	{ptr,
%%	 size}).


%% Func:  createUuid
%% Args:  
%% Returns:  energywise_sender_id_t
%% C-API func: int i_energywise_utl_createUuid(energywise_sender_id_t *id);
createUuid() ->
    Res = call(?ENGYWISE_Utl_CreateUuid, erlang:term_to_binary([])),
    {Id, Phy_idx} = 
        case erlang:binary_to_term(Res) of
            {_Id, _Phy_idx} ->
                {_Id, _Phy_idx};
            _ ->
                {"", 0}
        end,
    #energywise_sender_id_t{
        id = Id,
        phy_idx = Phy_idx
    }.

%% Func:  composeKey(Secret, Secret_len, Id, Key, Key_len)
%% Args:  
%% Returns:  energywise_sender_id_t
%% C-API func: int i_energywise_utl_composeKey(const unsigned char* secret, int secret_len, const energywise_sender_id_t *id, unsigned char* key, int key_len)
composeKey(Secret, Secret_len, Id, Key, Key_len) ->
    EId = Id#energywise_sender_id_t.id,
    EPhy_idx = Id#energywise_sender_id_t.phy_idx,
    Args = {Secret, Secret_len, {EId, EPhy_idx}, Key, Key_len},
    ArgsBin = erlang:term_to_binary(Args),
    Res = call(?ENGYWISE_Utl_ComposeKey, ArgsBin),
    erlang:binary_to_term(Res).
    
    
getVersion() ->
    Res = call(?ENGYWISE_Utl_getVersion, erlang:term_to_binary([])),
    erlang:binary_to_term(Res).

    
createUuidFromMac(Id, MAC) ->
    Res = call(?ENGYWISE_Utl_CreateUuid, erlang:term_to_binary({Id, MAC})),
    {Id, Phy_idx} = 
        case erlang:binary_to_term(Res) of
            {_Id, _Phy_idx} ->
                {_Id, _Phy_idx};
            _ ->
                {"", 0}
        end,
    #energywise_sender_id_t{
        id = Id,
        phy_idx = Phy_idx
    }.
    

createUuidFromDetails(Id, Product_id, Version, Serial_no, Mac) ->
    Res = call(?ENGYWISE_Utl_CreateUuid, erlang:term_to_binary(Id, Product_id, Version, Serial_no, Mac)),
    {Id, Phy_idx} = 
        case erlang:binary_to_term(Res) of
            {_Id, _Phy_idx} ->
                {_Id, _Phy_idx};
            _ ->
                {"", 0}
        end,
    #energywise_sender_id_t{
        id = Id,
        phy_idx = Phy_idx
    }.

