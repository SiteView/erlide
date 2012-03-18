
%%device 
-record(nnm_device,{
		nodeId,
		devIp="",
		baseMac="",			
		snmpFlag="0",
		snmpParam=[{snmpVersion,"v1"},{snmpPort,161},{getCommunity,"public"},{setCommunity,"private"},{timeout,500},{authType,""},{user,""},
				   {password,""},{privPassword,""},{contextId,""},{contextName,""}],
		telnetFlag="0",
		telnetParam=[{passwd,""},{privPasswd,""}],
		devType="",
		devFactory="",
		devModel="",
		devTypeName="",
		sysDescr="",		
		sysobjectId="",			
		sysSvcs="",			
		sysName="",			
		ipAddr=[],
		macs=[],
		infs=[]
		}).

-record(nnm_ipAddrd,{
					 ipAdEntAddr="",
					 ipAdEntIfIndex="",
					 ipAdEntNetMask=""
					 }).

%%interface info  {devIP,(ifAmount,[(ifindex,ifType,ifDescr,ifMac,ifPort,ifSpeed)])}
-record(nnm_ifprop,{
				    interfaceid,
				    profileid,
				    sourceIP="",
				    ifAmount=0,
				    inrec=[]
				   }).

-record(nnm_inrec,{
		ifIndex="",
		ifType="",
		ifDesc="",
		ifMac="",
		ifPort="",
		ifSpeed="",
		ifAlias=""
		}).

%%topu data  {sourceIP,directItem}
-record(nnm_directData,{
					  directDataid,
						profileID,
						sourceIP="",
						directItem=[]
						}).

-record(nnm_directitem,{
		localPortInx="",			%%The side port of the index
		localPortDsc="",			%%The side port of the description
		peerID="",      			%%Other side of the device ID
		peerIP="",      			%%Other side of the device IP
		peerPortInx="", 			%%Other side port of the index
		peerPortDsc="" 				%%Other side port of the description
		}).

%%edge info 
-record(nnm_edge,{
		edgeid,
		profileid,
		ip_left="",				%%Left side ip
		pt_left="",				%%Left side port
		inf_left="",			%%Left side inf
		dsc_left="",			%%Left side description
		ip_right="",			%%Right side ip
		pt_right="",			%%Right side port
		inf_right="",			%%Right side inf
		dsc_right=""			%%Right side description
		}).

%%Route table {sourceIP,{infInx,[nextIP]}}
-record(nnm_routeTable,{
		routeid,
		profileid,
		sourceIP="",
		infInxNextIP=[] 
		}).

-record(nnm_infInxnextIP,{
		infInx,
		nextIP=[]
		}).

-record(nnm_routeItem,{
		destNet="",			%%Destination network
		nextHop="",			%%Next hop IP
		destMask=""				%%Destination network mask		   
		}).

%%OSPF info  {sourceIP,{infInx,[destIP]}}
-record(nnm_ospfnbr,{
		ospfid,
		profileid,
		sourceIP="",
		infInxDest=[]			 
		}).

-record(nnm_infInxDest,{
		infInx,
		destIP=[]				
		}).
		
-record(nnm_destIP,{
		destIP=""
		}).

%%BGP info
-record(nnm_bgpTable,{
		bgpid,
		profileid,
		sourceIP="",
		bgpItem=[]
		}).

-record(nnm_bgpitem,{
		local_ip="",
		local_port="",
		peer_ip="",
		peer_port=""		 
		}).

%%VRRP info {vrid,string}
-record(nnm_vrrpitem,{
		vrids=[],
		assoips=[]		  
		}).

-record(nnm_vrid,{
		vrId="",
		masterIp="",
		primaryIp="",
		virtualMac=""	  
		}).

%%RouterStandbyInfo {string,RouterStandbyItem}  %%vrrp,hsrp
-record(nnm_RouterStandbyInfo,{
							   profileid,
							   deviceIP,
							   routerStandbyItem=[]
							   }).

-record(nnm_routerStandbyItem,{
		virtualIps=[],
		virtualMacs=[]
		}).

%%arp/aft table  {sourceIP,{infInx,[(MAC,destIP)]}}
-record(nnm_arplist,{
		arpid,
		profileid,
		sourceIP="",
		infmacip=[]		 
		}).
-record(nnm_infmacip,{
		infinx,
		macip=[]		 
		}).
-record(nnm_macip,{
		mac="",
		destip=""	   
		}).

%%profile info
-record(nnm_profile,{
		profileid,
		name="",
		description="",
		runTimeInSecond="",
		lastRun="",
		status="",
		sipPort="",
		hopCount="",
		searchTimeout="",
		snmpTimeout="",
		snmpRetries="",
		repeatInterval="",
		active="",
		progress="",
		duplicateNodes="",
		importUpInterface="",
		importDownInterface="",
		importShutdownInterface="",
		selectionMethod="",
		jobTimeout="",
		threads="",
		routeswitchCount="",
		switchCount="",
		routeCount="",
		firewallCount="",
		serverCount="",
		hostCount="",
		otherCount=""
		}).




