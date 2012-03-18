%%
%%资产主要数据结构定义
%%

-define(ALIAS,alias).
-define(DERIVED,derived).
-define(DISPLAY,display).
-define(CREATED,created).
-define(MODIFIED,modified).
-define(BLUEPRINT, blueprint).
-define(PATH, path).
-define(ICON,icon).
-define(PARAMS,params).
-define(TYPE,type).
-define(NE,ne).
-define(ORDER,order).
-define(RELATION,relation).
-define(TARGET,target).
-define(SOURCE,source).

-define(BasicAttribute, base_attribute).

-define(TEMPLATE_TABEL, 'asset_template').
-define(ASSET_TO_LABEL, 'asset_to_label').
-define(LOG_TABEL, 'asset_log').
-define(ICON_TABLE, 'asset_icon').

-define(CREATE_ASSET, "create asset").
-define(UPDATE_ASSET, "update asset").
-define(DELETE_ASSET, "delete asset").
-define(CREATE_TEMPLATE, "create template").
-define(UPDATE_TEMPLATE, "update template").
-define(DELETE_TEMPLATE, "delete template").
-define(CREATE_LABEL, "create label").
-define(UPDATE_LABEL, "update label").
-define(DELETE_LABEL, "delete label").

-define(TEMPLATE,"template@@").
-define(ASSET,"asset@@").
-define(REF,"ref@@").
-define(ATTR,"attr@@").
-define(LABELRELATION,"relation@@").
-define(IMG, "icon@@").
-define(TRANSACTION, "transaction@@").
-define(RFC, "rfc@@").

-define(NE_TEMPLATE, ["Options", "Reference"]).

-define(LABEL_ROOT,"root").
-define(USER_ROOT,"user_root").
-define(TEMPLATE_ROOT,"template_root").

-define(LABEL_ROOT_INDEX,"0").
-define(USER_ROOT_INDEX,"0:2").
-define(TEMPLATE_ROOT_INDEX,"0:1").

-define(ROOT, "Ci").
-define(CI_ROOT, "Ci").
-define(ATTRIBUTE_ROOT, "Attribute").

-define(REGISTERED_STATE, 1).
-define(PROCESSING_STATE, 2).
-define(COMMITED_STATE ,4).
-define(REJECTED_STATE, 8).

-record(rfc_result, {rejected=false, cause, txId, issuer, ciAdded=0, ciModified=0, ciDeleted=0, start, stop}).

