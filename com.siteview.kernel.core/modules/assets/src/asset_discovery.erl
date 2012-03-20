-module(asset_discovery).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").

get_rate(User)->
    nmap_manage:get_rate(User).
    
get_result(User) ->
    nmap_manage:get_result(User).
    
start(IPString, User)->
    nmap_manage:start(IPString, User).
    
stop()->
    nmap_manage:stop().
    
remove_scan_result() ->
    case asset:get_offspring_by_template("Discovery", 0, 1000, "", "") of
        {error, Reason} ->
            {error, Reason};
        Assets ->
            [asset:remove_asset(proplists:get_value(id, X))||X<- Assets],
            {ok, finish}
    end.
    
build_attribute_map([], _, Result) ->Result;
build_attribute_map([F|R], Attrs, Result) ->
    AttrId = proplists:get_value(id, F),
    case proplists:get_value(proplists:get_value(alias, F), Attrs) of
        undefined ->
            build_attribute_map(R, Attrs, Result);
        Value ->
            build_attribute_map(R, Attrs, [{AttrId, Value}|Result])
    end.
    
import_scan_result(IPString, User) ->
    remove_scan_result(),
    Result = get_result(User),
    Network = case update_network(IPString) of
        {error, _} ->
            "";
        {ok, Temp} ->
            proplists:get_value(id, Temp)
    end,
    %删除所有相关ports
    asset:remove_asset_by_template("NMAP_TCP_Port"),
    asset:remove_asset_by_template("NMAP_UDP_Port"),
    update_each(Result, Network).
    
update_network(IPString) ->
    case asset_template:get_template_by_alias("NMAP_Network") of
        {error, _} ->
            {error, template_not_found};
        Temp ->
            Attributes = proplists:get_value(attrs, Temp, []),
            asset:create_asset([{derived, "NMAP_Network"}, {attrs, build_attribute_map(Attributes, [{"networkAddress", IPString},{"networkMask", ""}], [])}])
    end.
    
update_device(IP, Network) ->
    case asset_template:get_template_by_alias("NMAP_Host") of
        {error, _} ->
            {error, template_not_found};
        Temp ->
            Attributes = proplists:get_value(attrs, Temp, []),
            asset:create_asset([{derived, "NMAP_Host"}, {attrs, build_attribute_map(Attributes, [{"ipv4Address", IP},{"network", build_relation(asset_util:get_id(?ASSET), Network, "NMAP_Network", "BelongsTo")}], [])}])
    end.
    
update_port([], _) ->{ok, finish};
update_port([F|R], Device) ->
    Id = asset_util:get_id(?ASSET),
    Port = proplists:get_value(port, F, []),
    {Protocol, Derived} = case proplists:get_value(protocol, F) of
        "tcp" ->
            {"tcp", "NMAP_TCP_Port"};
        _ ->
            {"udp", "NMAP_UDP_Port"}
    end,
    case asset_template:get_asset_template_by_alias(Derived) of
        {error, _} ->
            nothing;
        Temp ->
            Attributes = proplists:get_value(attrs, Temp, []),
            asset:create_asset([{derived, Derived}, {attrs, build_attribute_map(Attributes, [{"host", build_relation(Id, Device, Derived, "BelongsTo")}, {"port", proplists:get_value(portid, Port)}, {"protocol", Protocol}, {"serviceName", proplists:get_value(service, Port, "")}], [])}])
    end,
    update_port(R, Device).

update_each([], _) ->{ok, finish};
update_each([F|R], Network) ->
    Ip = proplists:get_value(ip, F, ""),
    Ports = proplists:get_value(service, F),
    Device = case update_device(Ip, Network) of
        {error, _} ->
            "";
        {ok, Dev} ->
            proplists:get_value(id, Dev)
    end,
    %删除此host对应的所有ref
    Refs = case asset_ref:get_ref_by_source(Device) of
        {error, _} ->
            [];
        TR ->
            TR
    end,
    [asset_ref:remove_ref(proplists:get_value(id, X))||X<- Refs],
    update_port(Ports, Device),
    update_each(R, Network).

build_relation("", _, _, _) ->"";
build_relation(undefined, _, _, _) ->"";
build_relation(_, "", _, _) ->"";
build_relation(_, undefined, _, _) ->"";
build_relation(Self, Target, Ref, RefType) ->
    case asset_ref:create_ref([{source, Self}, {target, [Target]}, {ref, Ref}, {refType, RefType}]) of
        {ok, Ref} ->
            proplists:get_value(id, Ref);
        _ ->
            ""
    end.

