%%  Copyright (c) 2001 Dan Gudmundsson
%%
%%  See the file "license.terms" for information on usage and redistribution
%%  of this file, and for a DISCLAIMER OF ALL WARRANTIES.
%% 
%%     $Id$
%%
%%%----------------------------------------------------------------------
%%% File    : sdl_util.erl
%%% Author  : Dan Gudmundsson <dgud@erix.ericsson.se>
%%% Purpose : 
%%% Created : 13 Sep 2000 by Dan Gudmundsson <dgud@erix.ericsson.se>
%%%----------------------------------------------------------------------

-module(es_util).

%%-export([term2bin/3,matrix2bin/2,bin2list/3,
%%	 tuplelist2bin/3, tuplelist2bin2/2, tuplelist2bin3/2, tuplelist2bin4/2,
%%	 alloc/2,getBin/1,
%%	 read/2,write/2,write/3,readBin/2,
%%	 debug/1,wb/1,whex/1]).

%% Obsolete - use alloc instead.
%%-export([malloc/2,free/1]).

-include("es_util.hrl").
-include("energyWise.hrl").

-import(energywise, [call/2,cast/2]).
-import(lists, [reverse/1]).

-compile(export_all).


-define(MYENGYWISE_malloc, (?ES_UTIL_HRL+1)).
-define(MYENGYWISE_write,  (?ES_UTIL_HRL+2)).
-define(MYENGYWISE_getPtrValue,  (?ES_UTIL_HRL+3)).

%% test
getPtrValue(Ptr) ->
    io:format("Ptr: ~p~n", [Ptr]),
    %%io:format("_PTR: ~p~n", [?_PTR]),
    Res = call(?MYENGYWISE_getPtrValue, <<Ptr:?_PTR>>),
    io:format("Res: ~p~n", [Res]),
    ok.


%% 将从c函数中返回的id数组转换为energywise_sender_id_t结构的列表
procAcksToList([]) ->
    [];
procAcksToList([{Id, Phy_idx}|T]) ->
    [
    #energywise_sender_id_t{
        id = Id,
        phy_idx = Phy_idx
    }
    ] ++
    procAcksToList(T);
procAcksToList([H|T]) ->
    procAcksToList(T).
