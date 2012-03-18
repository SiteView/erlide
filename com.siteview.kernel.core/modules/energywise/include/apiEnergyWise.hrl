%*** ENERGYWISE CONSTANT DEFINITIONS ***
-ifndef(ENERGYWISE_H_).
-define(ENERGYWISE_H_, "").


-define(ENERGYWISE_DEFAULT_PORT, 43440).    %% IANA registered

-define(ENERGYWISE_ID_SIZE_V1, 32).
-define(ENERGYWISE_ID_SIZE, ENERGYWISE_ID_SIZE_V1).
-define(ENERGYWISE_ID_LEN, ENERGYWISE_ID_SIZE).
-define(ENERGYWISE_MAX_LEVEL, 11).

-define(ENERGYWISE_STR_LEN_V1, 128).
-define(ENERGYWISE_STR_LEN, ENERGYWISE_STR_LEN_V1).
-define(ENERGYWISE_UNIT_WATTS, 0).
-define(ENERGYWISE_UNIT_MWATTS, -3).

-define(ENERGYWISE_QUERY_TIMEOUT_DEFAULT, 6).
-define(ENERGYWISE_QUERY_TIMEOUT_MAX, 180).
-define(ENERGYWISE_QUERY_TIMEOUT_MIN, 1).
-define(ENERGYWISE_IMPORTANCE_DEFAULT, 1).
-define(ENERGYWISE_LEVEL_DEFAULT, 10).

-define(PN_CHR_PER_LINE, 0x10).
-define(ENERGYWISE_PROTOCOL_VERSION, 0x01).

-record(energywise_sender_id_t,{
                                id=[],
                                phy_idx=0
                        }).
       
%% enum energywise_usage_caliber_t_
-define(ENERGYWISE_USAGE_CALIBER_MAX, -2).
-define(ENERGYWISE_USAGE_CALIBER_PRESUMED, -1).
-define(ENERGYWISE_USAGE_CALIBER_UNKNOWN, 0).
-define(ENERGYWISE_USAGE_CALIBER_ACTUAL, 1).
-define(ENERGYWISE_USAGE_CALIBER_TRUSTED, 2).

-record(energywise_recurrence_t,{
                                cron=[],
                                level=0,
                                importance=0,
                                remove=true
                        }).

%% enum ew_attribute_type_t_
-define(EW_ATTRIBUTE_TYPE_ENERGYWISE_ID, 1).
-define(EW_ATTRIBUTE_TYPE_ROLE, 2).
-define(EW_ATTRIBUTE_TYPE_DOMAIN, 3).
-define(EW_ATTRIBUTE_TYPE_NAME, 4).
-define(EW_ATTRIBUTE_TYPE_KEYWORDS, 5).
-define(EW_ATTRIBUTE_TYPE_ERROR_STRING, 6).
-define(EW_ATTRIBUTE_TYPE_UNITS, 7).
-define(EW_ATTRIBUTE_TYPE_USAGE, 8).
-define(EW_ATTRIBUTE_TYPE_LEVEL, 9).
-define(EW_ATTRIBUTE_TYPE_IMPORTANCE, 10).
-define(EW_ATTRIBUTE_TYPE_ENTITY_TYPE, 11).
-define(EW_ATTRIBUTE_TYPE_REPLY_TO, 12).
-define(EW_ATTRIBUTE_TYPE_NEIGHBOR, 13).
-define(EW_ATTRIBUTE_TYPE_NEIGHBOR_COUNT, 14).
-define(EW_ATTRIBUTE_TYPE_NANNY_VECTOR, 15).
-define(EW_ATTRIBUTE_TYPE_DELTA_VECTOR, 16).
-define(EW_ATTRIBUTE_TYPE_USAGE_CALIBER, 17).
-define(EW_ATTRIBUTE_TYPE_USAGE_VECTOR, 18).
-define(EW_ATTRIBUTE_TYPE_QUERY_TIMEOUT, 19).
-define(EW_ATTRIBUTE_TYPE_RECURRENCE, 20).
-define(EW_ATTRIBUTE_TYPE_DEVICE_TYPE, 21).

%% enum ew_class_t
-define(EW_CLASS_QUERY, 1).
-define(EW_CLASS_AGGREGATE, 2).

%% enum ew_action_query_t
-define(EW_ACTION_QUERY_SET, 1).
-define(EW_ACTION_QUERY_COLLECT, 2).
-define(EW_ACTION_QUERY_SAVE, 2).

%% enum ew_action_aggregate_t
-define(EW_ACTION_AGGREGATE_SUM, 1).



-endif.