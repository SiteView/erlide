-module(nnm_discovery_analyse).
-behaviour(gen_server).
-export([start_link/0,analyse/2]).

-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
terminate/2, code_change/3]).

-include("nnm_define.hrl").
-define(ANALYSE,"Analyse").

start_link() ->
    gen_server:start_link({local, ?MODULE}, ?MODULE, [], []).

init([]) ->
	{_, Path} = file:get_cwd(),  
    case erl_ddll:load_driver(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH, ?ANALYSE) of
	ok -> ok;
	{error, already_loaded} -> ok;
	{error,ErrorDesc} -> io:format("===>~p~n", [erl_ddll:format_error(ErrorDesc)]),exit({error, could_not_load_driver});
	_ ->  exit({error, could_not_load_driver})
    end,   
    {ok, []}.

handle_call(_, _, State) -> {noreply, State}.

handle_cast(stop, State) ->  {stop, normal, State};
handle_cast(_, State) -> {noreply, State}.

handle_info({'EXIT', Port, Reason}, Port) ->	
    {stop, {port_died, Reason}, Port};
handle_info({'EXIT', _Pid, _Reason}, Port) ->	
    {noreply, Port};
handle_info(_, State) ->
    {noreply, State}.

code_change(_OldVsn, State, _Extra) ->
    {ok, State}.

terminate(_Reason, _) ->    ok.

analyse(Msg,Path) ->
%%	io:format("~n~nMSg:~p ~nPath:~p~n~n",[Msg,Path]),
	case Msg of
		[] -> [];
		_ -> save_info(Msg,Path)
	end,
%% 	{DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo} = Msg,
%% 	Data = nnm_scan_format_data:dataFormat(DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo),
%% 	nnm_discovery_util:writeFile(Data,"ddd.txt",write),
	Port = open_port({spawn, ?ANALYSE}, [binary]),
	Bin = term_to_binary(Path),
	port_command(Port,Bin),
	Result = receive_port_data(Port),
	case Result of
	    {error,timeout} -> {error,timeout};
	    _ ->	    
		port_close(Port),
		
		Result
%% 		binary_to_term(Result)
	end.	

receive_port_data(Port) ->
    receive
        {Port, {data, Data}} ->  Data
	after 600000 ->  {error,timeout}
    end.


%% file operator 
save_info(Msg,Path) ->
	{DeviceInfo,InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo} = Msg,
	{ok,Dir} = file:get_cwd(),
	DirPath = Dir ++ "\\" ++ Path,
	save_scanInfo_device(DeviceInfo,DirPath),
	save_scanInfo_interface(InterfaceInfo,DirPath),
	save_scanInfo_route(RouteInfo,DirPath),
	save_scanInfo_arp(ArpInfo,DirPath),
	save_scanInfo_aft(AftInfo,DirPath),
	save_scanInfo_ospf(OspfInfo,DirPath),
	save_scanInfo_bpg(BgpInfo,DirPath),
	save_scanInfo_direct(DirectInfo,DirPath).

save_scanInfo_device(DeviceInfo,Path) ->
	{ok,File} = file:open(Path ++ "DeviceInfos.txt", [write]),
	save_scanInfo_device_w(DeviceInfo,File),
	file:close(File).	

save_scanInfo_device_w([],_) -> [];
save_scanInfo_device_w([H|T],File) ->
%% 	io:format("device:~p~n",[H]),
	Ip = proplists:get_value(ip,H,""),
	SnmpFlag = proplists:get_value(snmpFlag,H,""),
	SnmpParam = proplists:get_value(snmpParam,H,[]),
	CommunityGet = proplists:get_value(getCommunity,SnmpParam,""),
	CommunitySet = proplists:get_value(setCommunity,SnmpParam,""),
	SysobjectId = proplists:get_value(sysobjectId,H,""),
	DevType = proplists:get_value(devType,H,""),
	DevFactory = proplists:get_value(devFactory,H,""),
	DevModel = proplists:get_value(devModel,H,""),
	DevTypeName = proplists:get_value(devTypeName,H,""),
	BaseMac = proplists:get_value(baseMac,H,""),
	SysName = proplists:get_value(sysName,H,""),
	SysSvcs = proplists:get_value(sysSvcs,H,""),
	IpAddr = proplists:get_value(ipAddr,H,""),
	Macs = proplists:get_value(macs,H,[]),
	
	CIpAddr = [proplists:get_value(ipAddress,X,"") ++ "/" ++ proplists:get_value(ipMask,X,"") ++ "/" ++ proplists:get_value(ipIfIndex,X,"") || X <- IpAddr],
	CCIpAddr = string:join(CIpAddr, "[:]"),
	CMacs = string:join(Macs, "[:]"),
	
	StringList = [Ip,SnmpFlag,CommunityGet,CommunitySet,SysobjectId,DevType,DevFactory,DevModel,DevTypeName,BaseMac,SysName,SysSvcs,CCIpAddr,CMacs],
	
	io:format(File, "~s~n", [string:join(StringList, "[::]")]),
	save_scanInfo_device_w(T,File).

save_scanInfo_interface(InterfaceInfo,Path) -> 
	{ok,File} = file:open(Path ++ "InfProps.txt", [write]),
	save_scanInfo_interface_w(InterfaceInfo,File),
	file:close(File).

save_scanInfo_interface_w([],_) -> [];
save_scanInfo_interface_w([H|T],File) ->
	{Ip,Info} = H,
%% 	io:format("info:~p~n",[Info]),
	IfAmount = proplists:get_value(ifAmount,Info,""),
	Inrec = proplists:get_value(inrec,Info,[]),
	
	CInrec = [proplists:get_value(ifIndex,X,"") ++ "[:]" 
			 ++ proplists:get_value(ifType,X,"") ++ "[:]"
			 ++ proplists:get_value(ifMac,X,"") ++ "[:]"
			 ++ proplists:get_value(ifPort,X,"") ++ "[:]"
			 ++ proplists:get_value(ifDescr,X,"") ++ "[:]"
			 ++ proplists:get_value(ifSpeed,X,"") ++ "[:]"
			 ++ proplists:get_value(ifAlias,X,"") || X <- Inrec],
	CCInrec = string:join(CInrec, "[::]"),
	
	StringList = [Ip,IfAmount,CCInrec],
	io:format(File, "~s~n", [string:join(StringList, "[:::]")]),
	save_scanInfo_interface_w(T,File).

save_scanInfo_route(RouteInfo,Path) -> 
	{ok,File} = file:open(Path ++ "Route_ORG.txt", [write]),
	save_scanInfo_route_w(RouteInfo,File),
	file:close(File).

save_scanInfo_route_w([],_) -> [];
save_scanInfo_route_w([H|T],File) ->
	{Ip,Info} = H,
	RouteInfo = [proplists:get_value(routeIfIndex,X,"") ++ ":"
				++ proplists:get_value(routeDest,X,"") ++ "/"
				++ proplists:get_value(routeNextHop,X,"") ++ "/"
				++ proplists:get_value(routeMask,X,"") || X <- Info],
	CRouteInfo = string:join(RouteInfo, ";"),
	io:format(File, "~s~n", [Ip ++ "::" ++ CRouteInfo]),
	save_scanInfo_route_w(T,File).

save_scanInfo_arp(ArpInfo,Path) -> 
	FArpInfo = format_arp(ArpInfo),
	{ok,File} = file:open(Path ++ "Arp_ORG.txt", [write]),
	save_scanInfo_arp_w(FArpInfo,File),
	file:close(File).

save_scanInfo_arp_w([],_) -> [];
save_scanInfo_arp_w([H|T],File) ->
	{Ip,Info} = H,
	ArpInfo = [IfIndex ++ ":" ++ string:join(IpMac, ",") || {IfIndex,IpMac} <- Info],
	CArpInfo = string:join(ArpInfo, ";"),
	io:format(File, "~s~n", [Ip ++ "::" ++ CArpInfo]),
	save_scanInfo_arp_w(T,File).

format_arp([]) -> [];
format_arp([H|ArpInfo]) ->
	{SourceIp,IfIpMac} = H,
	FIfIpMac = format_arp(IfIpMac,SourceIp,[]),
	[{SourceIp,FIfIpMac}|format_arp(ArpInfo)].

format_arp([],_,Result) -> Result;
format_arp([H|IfIpMac],SourceIp,Result) ->
 	%%io:format("H:~p~n",[H]),
	IfIndex = proplists:get_value(ifIndex,H),
	Ip = proplists:get_value(ip,H,""),
	Mac = proplists:get_value(mac,H,""),
	Str = 
		if
			length(Ip) < 4 ->
				"0";
			true ->
				lists:nth(4,string:tokens(Ip,"."))
		end,
		
	case Ip =:= SourceIp orelse Str =:= "255" orelse Str =:= "0" of
		true ->
			format_arp(IfIpMac,SourceIp,Result);
		false ->
			case proplists:get_value(IfIndex,Result) of
				undefined ->
					format_arp(IfIpMac,SourceIp,[{IfIndex,[Ip ++ "-" ++ Mac]}|Result]);
				R ->
					V = [Ip ++ "-" ++ Mac|R],
					format_arp(IfIpMac,SourceIp,lists:keyreplace(IfIndex, 1, Result, {IfIndex,V}))
			end
	end.

save_scanInfo_aft(AftInfo,Path) -> 
	FAftInfo = format_aft(AftInfo),
	{ok,File} = file:open(Path ++ "Aft_ORG.txt", [write]),
	save_scanInfo_aft_w(FAftInfo,File),
	file:close(File).

format_aft([]) -> [];
format_aft([H|AftInfo]) ->
	{SourceIp,IpMac} = H,
	IfIp = format_aft(IpMac,[]),
	[{SourceIp,IfIp}|format_aft(AftInfo)].

format_aft([],Result) -> Result;
format_aft([H|IpMac],Result) ->
	Mac = proplists:get_value(mac,H),
	Port = proplists:get_value(port,H),
	
	case proplists:get_value(Port,Result) of
		undefined ->
			format_aft(IpMac,[{Port,[Mac]}|Result]);
		R ->
			V = [Mac|R],
			format_aft(IpMac,lists:keyreplace(Port, 1, Result, {Port,V}))
	end.

save_scanInfo_aft_w([],_) -> [];
save_scanInfo_aft_w([H|T],File) ->
	{Ip,Info} = H,
	AftInfo = [Port ++ ":" ++ string:join(Mac, ",") || {Port,Mac} <- Info],
	CAftInfo = string:join(AftInfo, ";"),
	io:format(File, "~s~n", [Ip ++ "::" ++ CAftInfo]),
	save_scanInfo_aft_w(T,File).

save_scanInfo_ospf(OspfInfo,Path) -> 
	FOspfInfo = format_ospf(OspfInfo),
	{ok,File} = file:open(Path ++ "OspfNbr_ORG.txt", [write]),
	save_scanInfo_ospf_w(FOspfInfo,File),
	file:close(File).

format_ospf([]) -> [];
format_ospf([H|OspfInf]) ->
%% 	io:format("H:~p~n",[H]),
	{SourceIp,Ospf} = H,
	IfIp = format_ospf(Ospf,[]),
	[{SourceIp,IfIp}|format_ospf(OspfInf)].

format_ospf([],Result) -> Result;
format_ospf([H|IfIp],Result) ->
	Index = proplists:get_value(index,H),
	Ip = proplists:get_value(ip,H),
	
	case proplists:get_value(Index,Result) of
		undefined ->
			format_ospf(IfIp,[{Index,[Ip]}|Result]);
		R ->
			V = [Ip|R],
			format_ospf(IfIp,lists:keyreplace(Index, 1, Result, {Index,V}))
	end.

save_scanInfo_ospf_w([],_) -> [];
save_scanInfo_ospf_w([H|T],File) ->
%% 	io:format("format H:~p~n",[H]),
	{SourceIp,Info} = H,
	OspfInfo = [Index ++ ":" ++ string:join(Ip, ",") || {Index,Ip} <- Info],
	COspfInfo = string:join(OspfInfo, ";"),
	io:format(File, "~s~n", [SourceIp ++ "::" ++ COspfInfo]),
	save_scanInfo_ospf_w(T,File).

save_scanInfo_bpg(BgpInfo,Path) -> 
	FBgpInfo = format_bgp(BgpInfo),
	{ok,File} = file:open(Path ++ "Bgp_ORG.txt", [write]),
	save_scanInfo_bpg_w(FBgpInfo,File),
	file:close(File).

format_bgp([]) -> [];
format_bgp([H|BgpInfo]) ->
%% 	io:format("H:~p~n",[H]),
	{_,Bgp} = H,
	FBgpInfo = [[proplists:get_value(localAddr,X),
				proplists:get_value(localPort,X),
				proplists:get_value(remoteAddr,X),
				proplists:get_value(remotePort,X)] || X <- Bgp],
	lists:append(FBgpInfo, format_bgp(BgpInfo)).

save_scanInfo_bpg_w([],_) -> [];
save_scanInfo_bpg_w([H|T],File) ->
	BgpInfo = string:join(H, "::"),
	io:format(File, "~s~n", [BgpInfo]),
	save_scanInfo_bpg_w(T,File).

save_scanInfo_direct(DirectInfo,Path) -> 
	{ok,File} = file:open(Path ++ "Direct_Data.txt", [write]),
	save_scanInfo_direct_w(DirectInfo,File),
	file:close(File).

save_scanInfo_direct_w([],_) -> [];
save_scanInfo_direct_w([H|T],File) ->
	[].

%% start(SharedLib) ->
%% 	{ok,Path} = file:get_cwd(),
%% 	case erl_ddll:load_driver(Path ++ ?NNM_DISCOVERY_ANALYSEDLLPATH, SharedLib) of
%% 		ok -> ok;
%% 		{error, already_loaded} -> ok;
%% 		_ -> exit({error, could_not_load_driver})
%% 	end,
%% 	spawn(fun() -> init(SharedLib) end).
%% 	
%% init(SharedLib) ->
%% 	register(nnm_discovery_scan, self()),
%% 	Port = open_port({spawn, SharedLib}, []),
%% 	loop(Port).
%% 	
%% stop() ->
%% 	nnm_discovery_scan ! stop.
%% 
%% call_port(Pid,Msg) ->
%% 	Pid ! {call, self(), Msg},
%% 	receive
%% 		{nnm_discovery_scan, Result} ->
%% 			Result
%% 	end.
%% 
%% loop(Port) ->
%% 	receive
%% 		{call, Caller, Msg} ->
%% 			Port ! {self(), {command, term_to_binary(Msg)}},
%% 			receive
%% 				{Port, {data, Data}} ->
%% 					Caller ! {nnm_discovery_scan, Data}
%% 			end,
%% 			loop(Port);
%% 		stop ->
%% 			Port ! {self(), close},
%% 				receive
%% 					{Port, closed} ->
%% 						exit(normal)
%% 				end;
%% 		{'EXIT', Port, Reason} ->
%% 			io:format("~p ~n" , [Reason]),
%% 			exit(port_terminated)
%% 	end.