-module(nnm_discovery_profile).
-export([
		 addProfile/1,
		 findProfile/1,
		 deleteAllProfile/0,
		 deleteProfile/1,
		 updateProfile/1,
		 updateProfile/2
		 ]).

-include("nnm_define.hrl").
-include("nnm_discovery_topo.hrl").

addProfile(Profile) ->
	ProfileData = [
				   {name,Profile#nnm_profile.name},
				   {description,Profile#nnm_profile.description},
				   {runTimeInSecond,Profile#nnm_profile.runTimeInSecond},
				   {lastRun,Profile#nnm_profile.lastRun},
				   {status,Profile#nnm_profile.status},
				   {sipPort,Profile#nnm_profile.sipPort},
				   {hopCount,Profile#nnm_profile.hopCount},
				   {searchTimeout,Profile#nnm_profile.searchTimeout},
				   {snmpTimeout,Profile#nnm_profile.snmpTimeout},
				   {snmpRetries,Profile#nnm_profile.snmpRetries},
				   {repeatInterval,Profile#nnm_profile.repeatInterval},
				   {active,Profile#nnm_profile.active},
				   {progress,Profile#nnm_profile.progress},
				   {duplicateNodes,Profile#nnm_profile.duplicateNodes},
				   {importUpInterface,Profile#nnm_profile.importUpInterface},
				   {importDownInterface,Profile#nnm_profile.importDownInterface},
				   {importShutdownInterface,Profile#nnm_profile.importShutdownInterface},
				   {selectionMethod,Profile#nnm_profile.selectionMethod},
				   {jobTimeout,Profile#nnm_profile.jobTimeout},
				   {threads,Profile#nnm_profile.threads},
				   {routeswitchCount,Profile#nnm_profile.routeswitchCount},
				   {switchCount,Profile#nnm_profile.switchCount},
				   {routeCount,Profile#nnm_profile.routeCount},
				   {firewallCount,Profile#nnm_profile.firewallCount},
				   {serverCount,Profile#nnm_profile.serverCount},
				   {hostCount,Profile#nnm_profile.hostCount},
				   {otherCount,Profile#nnm_profile.otherCount}
				   ],
	RValue = erlcmdb:create_ci(?NNM_DISCOVERY_PROFILE_TABLE,ProfileData),
	case RValue of
		{ok,[H|_T]} ->
			{id,ProfileID} = H,
			ProfileID;
		_ ->
			error
	end.

findProfile(Where) ->
	Data = erlcmdb:find_ci(?NNM_DISCOVERY_PROFILE_TABLE,Where),
	%%io:format("outData:~p~n", [Data]),
	ReturnValue = findResultAnalyse(Data),
	ReturnValue.

findResultAnalyse([]) -> [];
findResultAnalyse([H|DataList]) ->
	Profileid = proplists:get_value(id,H),
	Name = proplists:get_value(name,H),
	Description = proplists:get_value(description,H),
	RunTimeInSecond = proplists:get_value(runTimeInSecond,H),
	LastRun = proplists:get_value(lastRun,H),
	Status = proplists:get_value(status,H),
	SipPort = proplists:get_value(sipPort,H),
	HopCount = proplists:get_value(hopCount,H),
	SearchTimeout = proplists:get_value(searchTimeout,H),
	SnmpTimeout = proplists:get_value(snmpTimeout,H),
	SnmpRetries = proplists:get_value(snmpRetries,H),
	RepeatInterval = proplists:get_value(repeatInterval,H),
	Active = proplists:get_value(active,H),
	Progress = proplists:get_value(progress,H),
	DuplicateNodes = proplists:get_value(duplicateNodes,H),
	ImportUpInterface = proplists:get_value(importUpInterface,H),
	ImportDownInterface = proplists:get_value(importDownInterface,H),
	ImportShutdownInterface = proplists:get_value(importShutdownInterface,H),
	SelectionMethod = proplists:get_value(selectionMethod,H),
	JobTimeout = proplists:get_value(jobTimeout,H),
	Threads = proplists:get_value(threads, H),
	RouteswitchCount = proplists:get_value(routeswitchCount,H),
	SwitchCount = proplists:get_value(switchCount,H),
	RouteCount = proplists:get_value(routeCount,H),
	FirewallCount = proplists:get_value(firewallCount,H),
	ServerCount = proplists:get_value(serverCount,H),
	HostCount = proplists:get_value(hostCount,H),
	OtherCount = proplists:get_value(otherCount,H),
	
	[#nnm_profile{
				  profileid = Profileid,
				  name = Name,
				  description = Description,
				  runTimeInSecond = RunTimeInSecond,
				  lastRun = LastRun,
				  status = Status,
				  sipPort = SipPort,
				  hopCount = HopCount,
				  searchTimeout = SearchTimeout,
				  snmpTimeout = SnmpTimeout,
				  snmpRetries = SnmpRetries,
				  repeatInterval = RepeatInterval,
				  active = Active,
				  progress = Progress,
				  duplicateNodes = DuplicateNodes,
				  importUpInterface = ImportUpInterface,
				  importDownInterface = ImportDownInterface,
				  importShutdownInterface = ImportShutdownInterface,
				  selectionMethod = SelectionMethod,
				  jobTimeout = JobTimeout,
				  threads = Threads,
				  routeswitchCount = RouteswitchCount,
				  switchCount = SwitchCount,
				  routeCount = RouteCount,
				  firewallCount = FirewallCount,
				  serverCount = ServerCount,
				  hostCount = HostCount,
				  otherCount = OtherCount
				  }|findResultAnalyse(DataList)].


deleteProfile([]) -> [];
deleteProfile([ID|IDList]) ->
	erlcmdb:delete_ci(?NNM_DISCOVERY_PROFILE_TABLE,ID),
	deleteProfile(IDList).

deleteAllProfile() ->
	Data = erlcmdb:find_ci(?NNM_DISCOVERY_PROFILE_TABLE,[]),
	IDList = getIDList(Data),
	deleteProfile(IDList).

getIDList([]) -> [];
getIDList([H|DataList]) ->
	Profileid = proplists:get_value(id,H),
	[Profileid|getIDList(DataList)].

updateProfile(Profile) ->
	ProfileData = [
				   {name,Profile#nnm_profile.name},
				   {description,Profile#nnm_profile.description},
				   {runTimeInSecond,Profile#nnm_profile.runTimeInSecond},
				   {lastRun,Profile#nnm_profile.lastRun},
				   {status,Profile#nnm_profile.status},
				   {sipPort,Profile#nnm_profile.sipPort},
				   {hopCount,Profile#nnm_profile.hopCount},
				   {searchTimeout,Profile#nnm_profile.searchTimeout},
				   {snmpTimeout,Profile#nnm_profile.snmpTimeout},
				   {snmpRetries,Profile#nnm_profile.snmpRetries},
				   {repeatInterval,Profile#nnm_profile.repeatInterval},
				   {active,Profile#nnm_profile.active},
				   {progress,Profile#nnm_profile.progress},
				   {duplicateNodes,Profile#nnm_profile.duplicateNodes},
				   {importUpInterface,Profile#nnm_profile.importUpInterface},
				   {importDownInterface,Profile#nnm_profile.importDownInterface},
				   {importShutdownInterface,Profile#nnm_profile.importShutdownInterface},
				   {selectionMethod,Profile#nnm_profile.selectionMethod},
				   {jobTimeout,Profile#nnm_profile.jobTimeout},
				   {threads,Profile#nnm_profile.threads},
				   {routeswitchCount,Profile#nnm_profile.routeswitchCount},
				   {switchCount,Profile#nnm_profile.switchCount},
				   {routeCount,Profile#nnm_profile.routeCount},
				   {firewallCount,Profile#nnm_profile.firewallCount},
				   {serverCount,Profile#nnm_profile.serverCount},
				   {hostCount,Profile#nnm_profile.hostCount},
				   {otherCount,Profile#nnm_profile.otherCount}
				   ],
	
	erlcmdb:update_ci(?NNM_DISCOVERY_PROFILE_TABLE,Profile#nnm_profile.profileid,ProfileData).

updateProfile(Id,ProfileData) ->
	erlcmdb:update_ci(?NNM_DISCOVERY_PROFILE_TABLE,Id,ProfileData).


















