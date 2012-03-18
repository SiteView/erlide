-module(import_nmap_result).
-compile(export_all).
-include("asset.hrl").
-include("../../core/include/monitor.hrl").
%"id", "app_", "derived", "display", "created", "modified", "description", "icon", "attrs","total"

device("switch") ->   "Switch";
device("general purpose") ->  "Server";
device("router") ->   "Router";
device(_) ->   "System".

port("tcp") ->"NMAP_TCP_Port";
port("udp") ->"NMAP_UDP_Port";
port(_) ->"NMAP_Port".

os(Type) ->
    case Type of
        "Linux" ++ R ->
            {"Linux", "linux", string:strip(R)};
        "Windows" ++ R ->
            {win(Type), "windows", string:strip(R)};
        "Solaris" ++ R ->
            {"Solaris", "solaris", string:strip(R)};
        _ ->
            {"OS", "", ""}
    end.

win("Windows 2003") ->"Windows2003";
win(_) ->"Windows".

import_all(User, IpAddress) ->
    case create_network(IpAddress) of
        {ok, Network} ->
            Result = nmap_manage:get_result(User),
            parse_result(Result, proplists:get_value(id, Network));
        _ ->
            {error, create_network_error}
    end.

parse_result([], _) ->{ok, finish};
parse_result([F|R], Network) ->
    IP = proplists:get_value(ip, F),
    OS = proplists:get_value(os, F),
    Ports = proplists:get_value(service, F),
    Type = proplists:get_value(type, OS),
    case create_device(IP, Type, Network) of
        {ok, Device} ->
            create_port(Ports, proplists:get_value(id, Device)),
            create_os(OS, proplists:get_value(id, Device));
        _ ->
            create_port(Ports, ""),
            create_os(OS, "")
    end,
    parse_result(R, Network).
    
create_network(Ip) ->
    Attrs = [{"networkAddress", Ip}],
    create_asset("NMAP_Network", Attrs).
    
create_device(IP, Type, Network) ->
    Device = [{"ipaddress", IP}, {"network", Network}],
    create_asset(device(Type), Device).
    
create_port([], _) ->{ok, finish};
create_port([F|R], Device) ->
    Protocol = proplists:get_value(protocol, F),
    Type = port(Protocol),
    create_asset(Type, [{"host", Device}, {"port", proplists:get_value(portid, F, "unkown")}, 
        {"protocol", Protocol}, {"serviceName", proplists:get_value(service, F, "")}]),
    create_port(R, Device).
    
create_os(OS, Device) ->
    {Temp, Family, Version} = os(proplists:get_value(osfamily, OS)),
    create_asset(Temp, [{"installedon", Device}, {"family", Family}, {"version", Version}]).

create_asset(TempAlias, Attrs) ->
    case asset_template:get_template_by_alias(TempAlias) of
        {error, _} ->
            {error, template_not_found};
        Temp ->
            Tid = proplists:get_value(id, Temp),
            case asset_template:get_asset_template(Tid) of
                {error, _} ->
                    {error, template_not_found};
                T ->
                    Attributes = proplists:get_value(attrs, T),
                    NewAttrs = build_attribute(Attributes, Attrs, []),
                    Asset = [{id, asset_util:get_id(?ASSET)}, {derived, TempAlias}, {attrs, NewAttrs}],
                    asset:create_asset(Asset)
            end
    end.

build_attribute([], _, Result) ->Result;
build_attribute([F|R], Attrs, Result) ->
    Alias = proplists:get_value(alias, F),
    NR = case proplists:get_value(Alias, Attrs) of
        undefined ->
            Result;
        Value ->
            [{proplists:get_value(id, F), Value}|Result]
    end,
    build_attribute(R, Attrs, NR).


