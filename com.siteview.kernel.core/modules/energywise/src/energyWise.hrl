%%  Copyright (c) 2001 Dan Gudmundsson
%%
%%  See the file "license.terms" for information on usage and redistribution
%%  of this file, and for a DISCLAIMER OF ALL WARRANTIES.
%%%----------------------------------------------------------------------
%%% File    : esdl.hrl
%%% Author  : Dan Gudmundsson <dgud@erix.ericsson.se>
%%% Purpose : 
%%% Created :  6 Oct 2000 by Dan Gudmundsson <dgud@erix.ericsson.se>
%%%----------------------------------------------------------------------

-include("apiEnergyWiseUtil.hrl").

-ifndef(ENERGYWISE_HRL).  %% These must exactly match those in c_src/esdl.h
-define(ENERGYWISE_HRL,         20).
-define(API_HRL,                30).
-define(LOG_HRL,                100).
-define(UTIL_HRL,                120).
-define(SDK_HRL,                140).
-define(ES_UTIL_HRL,           250).
-define(MAX_FUNCTIONS_HRL,    1279).  %/* Current Max.. Increase if needed */
-endif.