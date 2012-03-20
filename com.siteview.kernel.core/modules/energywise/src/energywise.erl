%%  Copyright (c) 2001 Cai Ning
%%
%%  See the file "license.terms" for information on usage and redistribution
%%  of this file, and for a DISCLAIMER OF ALL WARRANTIES.
%% 
%%     $Id$
%%
%%%----------------------------------------------------------------------
%%% File    : energywise.erl
%%% Author  : Cai Ning <ning.cai@dragonflow.com>
%%% Purpose : To give access to the Cisco EnergyWise
%%%           by Sam Lantinga, see www.siteview.com.
%%% Created : 23 Jun 2010 by Cai Ning <ning.cai@dragonflow.com>
%%%----------------------------------------------------------------------

-module(energywise).

-include("energyWise.hrl").
%%-include("engywise_util.hrl").

-export([init/1, quit/0, getError/0]).
-export([cast/2, call/2]).
%%-export([send_bin/1, send_bin/3]).

%% SDL functions
%%% Functions  

-define(ENGYWISE_Init, ?ENERGYWISE_HRL + 1).
-define(ENGYWISE_Quit, ?ENGYWISE_Init + 1).
-define(ENGYWISE_GetError, ?ENGYWISE_Quit +1).

%% Func:  init
%% Args:  BitMask
%% Returns:  WrapperRef
%% C-API func: int ENGYWISE_Init(Uint32 flags);
%% Desc:  Initializes the EnergyWise (including the erlang specific parts)
init(Flags) ->
    Path = case code:priv_dir(energywise) of
	       P when is_list(P) -> 
		   P;
	       {error, _} ->  %% in case you use erl -pa ../ebin priv_dir don't work :-(		   
		   case code:is_loaded(?MODULE) of
		       {file, ENGYWISEPath} -> 
			   strip(ENGYWISEPath, "ebin/" ++ atom_to_list(?MODULE) ++ ".beam") ++ "priv/";
		       _ ->  %% For debugging 
			   atom_to_list(c:pwd()) ++ "../priv/"
		   end
	   end,
    case os:type() of
	{win32, _} ->
	    OsPath = filename:nativename(Path) ++ ";" ++ os:getenv("PATH"),
	    os:putenv("PATH", OsPath);
	_ ->
	    ignore
    end,
    io:format("Path: ~p~n", [Path]),
    case catch erl_ddll:load_driver(Path, "engywise_driver") of
	ok -> 
	    ok;
	{error, R} -> 
	    io:format("Driver failed: ~s ~n", [erl_ddll:format_error(R)]);
	Other ->
	    io:format("Driver crashed: ~p ~n", [Other])
    end,
    Port = open_port({spawn, "engywise_driver"}, [binary]),
    register(engywise_port, Port), 
    Port.

%% Func:  quit
%% Args:  none
%% Returns:  Quits the wrapper
%% C-API func: void ENGYWISE_Quit(void);
quit() ->    
%%    cast(?SDL_Quit, []),
    erlang:port_close(engywise_port),
    erl_ddll:unload_driver("engywise_driver").

%% Func:  getError
%% Args:  none
%% Returns:  DescString
%% C-API func: char * ENGYWISE_GetError(void);
getError() ->
    Bin = call(?ENGYWISE_GetError, []),
    binary_to_list(Bin).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

cast(Op, Arg) ->
    erlang:port_control(engywise_port, Op, Arg),
    ok.

call(Op, Arg) ->
    erlang:port_control(engywise_port, Op, Arg).

%%send_bin(Bin) when is_binary(Bin) ->
%%    erlang:port_command(engywise_port, Bin).

%%send_bin(#sdlmem{bin=Bin}, _, _) -> send_bin(Bin);
%%send_bin(Bin, _, _) when is_binary(Bin) -> send_bin(Bin);
%%send_bin(Term, Mod, Line) -> erlang:error({Mod,Line,unsupported_type,Term}).

%%%%%%%%%%%%%%%%% NON SDL FUNCTIONS %%%%%%%%%%%%%%%%

strip(Src, Src) ->
    [];
strip([H|R], Src) ->
    [H| strip(R, Src)].
