-module(test_asset).
-compile(export_all).
-include("../../core/include/monitor.hrl").
-include("asset.hrl").

import_data()->
    asset_sup:start(),
    R1 =dbcs_asset:remove_asset_all(),
    %%io:format("~n dbcs_asset:remove_asset_all successful.  ~p~n",[R1]),
    R2 = asset_config_manager:remove_all(?TEMPLATE_TABEL),
    %%io:format("~n asset_config_manager:remove_all(?TEMPLATE_TABEL) successful.  ~p ~n",[R2]),
    R3 = asset_machine:import_all(),
    %%io:format("~n asset_machine:import_all successful. ~p ~n",[R3]),
    R4 = asset_config:get_all_templates(),
    %% io:format("~n asset_config:get_all_templates sucessful.  ~p ~n",[R4]),
    R5 = asset:get_all_assets(),
   %% io:format("~n asset_machine:import_all successfule. ~p ~n",[R5]),
    R6 = asset_label_manage:create_sysLabel(),
    io:format("~n asset_label_manage:create_sysLabel  successful. ~p ~n",[R6]),
    R7 = asset_label_manage:get_syslabel(),
    io:format("~n asset_label_manage:get_syslabel  successful. ~p ~n",[R7]).
