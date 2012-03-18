-module(nnm_discovery_scan_type).
-export([scanSwitch/3,scanRoute/6,scanRouteSwitch/7]).

scanSwitch(InterfaceObject,AftObject,ArpObject) ->
	AftInfo =
		case AftObject == [] of
			true ->
				[];
			false ->
				AftObject:getDataFromSnmp()
		end,
%% 	io:format("AftInfo:~p~n",[AftInfo]),
	ArpInfo = 
		case ArpObject == [] of
			true ->
				[];
			false ->
				ArpObject:getDataFromSnmp()
		end,
%% 	io:format("ArpInfo:~p~n",[ArpInfo]),
	InterfaceInfo = 
		case InterfaceObject == [] of
			true ->
				[];
			false ->
				InterfaceObject:getDataFromSnmp()
		end,
%% 	io:format("InterfaceInfo:~p~n",[InterfaceInfo]),
	
	{InterfaceInfo,AftInfo,ArpInfo}.

scanRoute(InterfaceObject,RouteObject,ArpObject,OspfObject,BgpObject,DirectObject) ->
	InterfaceInfo = 
		case InterfaceObject == [] of
			true ->
				[];
			false ->
				InterfaceObject:getDataFromSnmp()
		end,
%% 	io:format("InterfaceInfo:~p~n",[InterfaceInfo]),
	RouteInfo = 
		case RouteObject == [] of
			true ->
				[];
			false ->
				RouteObject:getDataFromSnmp()
		end,
%% 	io:format("routeInfo:~p~n",[RouteInfo]),
	%%AftInfo = 
	%%	case AftObject == [] of
	%%		true ->
	%%			[];
	%%		false ->
	%%			AftObject:getDataFromSnmp()
	%%	end,
	%%io:format("AftInfo:~p~n",[AftInfo]),
	ArpInfo =
		case ArpObject == [] of
			true ->
				[];
			false ->
				ArpObject:getDataFromSnmp()
		end,
%% 	io:format("ArpInfo:~p~n",[ArpInfo]),
	OspfInfo =
		case OspfObject == [] of
			true ->
				[];
			false ->
				OspfObject:getDataFromSnmp()
		end,
	BgpInfo = 
		case BgpObject == [] of
			true ->
				[];
			false ->
				BgpObject:getDataFromSnmp()
		end,
	DirectInfo =
		case DirectObject == [] of
			true ->
				[];
			false ->
				DirectObject:getDataFromSnmp()
		end,
	
	{InterfaceInfo,RouteInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo}.

scanRouteSwitch(InterfaceObject,RouteObject,AftObject,ArpObject,OspfObject,BgpObject,DirectObject) ->
	InterfaceInfo = 
		case InterfaceObject == [] of
			true ->
				[];
			false ->
				InterfaceObject:getDataFromSnmp()
		end,
%% 	io:format("InterfaceInfo:~p~n",[InterfaceInfo]),
	RouteInfo = 
		case RouteObject == [] of
			true ->
				[];
			false ->
				RouteObject:getDataFromSnmp()
		end,
%% 	io:format("routeInfo:~p~n",[RouteInfo]),
	AftInfo = 
		case AftObject == [] of
			true ->
				[];
			false ->
				AftObject:getDataFromSnmp()
		end,
%% 	io:format("AftInfo:~p~n",[AftInfo]),
	ArpInfo =
		case ArpObject == [] of
			true ->
				[];
			false ->
				ArpObject:getDataFromSnmp()
		end,
%% 	io:format("ArpInfo:~p~n",[ArpInfo]),
	OspfInfo =
		case OspfObject == [] of
			true ->
				[];
			false ->
				OspfObject:getDataFromSnmp()
		end,
	BgpInfo = 
		case BgpObject == [] of
			true ->
				[];
			false ->
				BgpObject:getDataFromSnmp()
		end,
	DirectInfo =
		case DirectObject == [] of
			true ->
				[];
			false ->
				DirectObject:getDataFromSnmp()
		end,
	
	{InterfaceInfo,RouteInfo,AftInfo,ArpInfo,OspfInfo,BgpInfo,DirectInfo}.

	
	
	
	
	
	