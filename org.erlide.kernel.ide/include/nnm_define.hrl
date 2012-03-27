-define(NNM_DEVICE_TYPE_ROUTE_SWITCH, "0").
-define(NNM_DEVICE_TYPE_SWITCH, "1").
-define(NNM_DEVICE_TYPE_ROUTE, "2").
-define(NNM_DEVICE_TYPE_FIREWALL, "3").
-define(NNM_DEVICE_TYPE_SERVER, "4").
-define(NNM_DEVICE_TYPE_HOST, "5").
-define(NNM_DEVICE_TYPE_OTHER, "6").

-define(NNM_DISCOVERY_OIDLISTPATH,"templates.nnm/oidlist.dat").
-define(NNM_DISCOVERY_SYSOBJECTPATH,"templates.nnm/sysobjectidlist.dat").
-define(NNM_DISCOVERY_SPECIALOIDLISTPATH,"templates.nnm/specialoidlist.dat").
-define(NNM_DISCOVERY_TELNETLISTPATH,"templates.nnm/telnetlist.dat").
-define(NNM_DISCOVERY_TELNETPARAMPATH,"templates.nnm/telnetparam.dat").
-define(NNM_DISCOVERY_IPLISTSPATH,"templates.nnm/iplist.dat").
-define(NNM_DISCOVERY_ANALYSEDLLPATH,"/nnm_discovery").

-define(NNM_DISCOVERY_PROFILE_TABLE,"nnm_discovery_profile").
-define(NNM_DISCOVERY_DEVICE_TABLE,"nnm_discovery_node").
-define(NNM_DISCOVERY_IPADDRD_TABLE,"nnm_discovery_ipAddrd").
-define(NNM_DISCOVERY_INTERFACE_TABLE,"nnm_discovery_interface").
-define(NNM_DISCOVERY_DIRECT_TABLE,"nnm_discovery_direct").
-define(NNM_DISCOVERY_EDGE_TABLE,"nnm_discovery_edge").
-define(NNM_DISCOVERY_ROUTE_TABLE,"nnm_discovery_route").
-define(NNM_DISCOVERY_OSPF_TABLE,"nnm_discovery_ospf").
-define(NNM_DISCOVERY_BGP_TABLE,"nnm_discovery_bgp").
-define(NNM_DISCOVERY_VRRP_TABLE,"nnm_discovery_vrrp").
-define(NNM_DISCOVERY_ARP_TABLE,"nnm_discovery_arp").
-define(NNM_DISCOVERY_AFT_TABLE,"nnm_discovery_aft").
-define(NNM_DISCOVERY_DEVICELOGIN_TABLE,"nnm_discovery_device_login").
-define(NNM_DISCOVERY_SCANPARAM_TABLE,"nnm_discovery_scan_param").
-define(NNM_SNMP_NODE_TABLE,"nnm_node").
-define(NNM_SNMP_INTERFACE_TABLE,"nnm_interface").
-define(NNM_DISCOVERY_SCAN_CONFIG_TABLE,"nnm_discovery_scan_config").
-define(NNM_DISCOVERY_SCAN_VERSION_TABLE,"nnm_discovery_scan_version").
-define(NNM_DISCOVERY_TOPOCHART_TABLE,"nnm_discovery_topo_chart").
-define(NNM_DISCOVERY_TOPOCHILDCHART_TABLE,"nnm_discovery_topo_child_chart").
-define(NNM_DISCOVERY_LINE_LEVEL,"nnm_discovery_line_level").
-define(NNM_MONITOR_INTERVAL_TABLE,"nnm_monitor_interval").
-define(NNM_DISCOVERY_PCLIST_TABLE,"nnm_discovery_pcList").
-define(NNM_BASEIPMAC_TABLE,"nnm_baseIpMac").
-define(NNM_CHANGEIPMAC_TABLE,"nnm_changeIpMac").

-define(NNM_DISCOVERY_DEVICE_LOGIN_SNMP,"1").
-define(NNM_DISCOVERY_DEVICE_LOGIN_TELNET,"2").
-define(NNM_DISCOVERY_DEVICE_DEFAULT_LOGIN_SNMP,"11").
-define(NNM_DISCOVERY_DEVICE_DEFAULT_LOGIN_TELNET,"12").

-define(IPTABLE,ipTable).
-define(ROUTETABLE,routeTable).
-define(INTERFACETABLE,interfaceTable).
-define(ARPTABLE,arpTable).
-define(AFTTABLE,aftTable).
-define(CPUTABLE,cpuTable).
-define(MEMORYTABLE,memoryTable).
-define(FLOWTABLE,flowTable).
-define(OSPFTABLE,ospfTable).
-define(BGPTABLE,bgpTable).
-define(DIRECTTABLE,directTable).
-define(DEVICE,device).

-define(SYSOBJECTID,[1,3,6,1,2,1,1,2]).
-define(SYSUPTIME,[1,3,6,1,2,1,1,3]).
-define(SYSNAME,[1,3,6,1,2,1,1,5]).
-define(SYSSERVICES,[1,3,6,1,2,1,1,7]).
-define(SYSDESCR,[1,3,6,1,2,1,1,1]).
-define(SYSTEM,[1,3,6,1,2,1,1]).
-define(BASEMAC,[1,3,6,1,2,1,17,1,1]).
-define(IPADDRESS,[1,3,6,1,2,1,4,20,1,1]).
-define(IPIFINDEX,[1,3,6,1,2,1,4,20,1,2]).
-define(IPMASK,[1,3,6,1,2,1,4,20,1,3]).
-define(ARPIFINDEX,[1,3,6,1,2,1,4,22,1,1]).
-define(ARPMAC,[1,3,6,1,2,1,4,22,1,2]).
-define(ARPIP,[1,3,6,1,2,1,4,22,1,3]).
-define(ARPTYPE,[1,3,6,1,2,1,4,22,1,4]).
-define(QTPFDBADDRESS,[1,3,6,1,2,1,17,7,1,2,2,1,1]).
-define(QTPFDBPORT,[1,3,6,1,2,1,17,7,1,2,2,1,2]).
-define(QTPFDBSTATUS,[1,3,6,1,2,1,17,7,1,2,2,1,3]).
-define(DTPFDBADDRESS,[1,3,6,1,2,1,17,4,3,1,1]).
-define(DTPFDBPORT,[1,3,6,1,2,1,17,4,3,1,2]).
-define(DTPFDBSTATUS,[1,3,6,1,2,1,17,4,3,1,3]).
-define(ROUTEDEST,[1,3,6,1,2,1,4,21,1,1]).
-define(ROUTEIFINDEX,[1,3,6,1,2,1,4,21,1,2]).
-define(ROUTENEXTHOP,[1,3,6,1,2,1,4,21,1,7]).
-define(ROUTETYPE,[1,3,6,1,2,1,4,21,1,8]).
-define(ROUTEPROTO,[1,3,6,1,2,1,4,21,1,9]).
-define(ROUTEMASK,[1,3,6,1,2,1,4,21,1,11]).
-define(OSPFNBRADDRESSLESSINDEX,[1,3,6,1,2,1,14,10,1,2]).
-define(OSPFNBRIPADDR,[1,3,6,1,2,1,14,10,1,1]).
-define(IFNUMBER,[1,3,6,1,2,1,2,1]).
-define(IFINDEX,[1,3,6,1,2,1,2,2,1,1]).
-define(IFDESCR,[1,3,6,1,2,1,2,2,1,2]).
-define(IFTYPE,[1,3,6,1,2,1,2,2,1,3]).
-define(IFMTU,[1,3,6,1,2,1,2,2,1,4]).
-define(IFSPEED,[1,3,6,1,2,1,2,2,1,5]).
-define(IFMAC,[1,3,6,1,2,1,2,2,1,6]).
-define(IFADMINSTATUS,[1,3,6,1,2,1,2,2,1,7]).
-define(IFOPERSTATUS,[1,3,6,1,2,1,2,2,1,8]).
-define(IFLASTCHANGE,[1,3,6,1,2,1,2,2,1,9]).
-define(IFINOCTETS,[1,3,6,1,2,1,2,2,1,10]).
-define(IFINUCASTPKTS,[1,3,6,1,2,1,2,2,1,11]).
-define(IFINNUCASTPKTS,[1,3,6,1,2,1,2,2,1,12]).
-define(IFINDISCARDS,[1,3,6,1,2,1,2,2,1,13]).
-define(IFINERRORS,[1,3,6,1,2,1,2,2,1,14]).
-define(IFINUNKNOWNPROTOS,[1,3,6,1,2,1,2,2,1,15]).
-define(IFOUTOCTETS,[1,3,6,1,2,1,2,2,1,16]).
-define(IFOUTUCASTPKTS,[1,3,6,1,2,1,2,2,1,17]).
-define(IFOUTNUCASTPKTS,[1,3,6,1,2,1,2,2,1,18]).
-define(IFOUTDISCARDS,[1,3,6,1,2,1,2,2,1,19]).
-define(IFOUTERRORS,[1,3,6,1,2,1,2,2,1,20]).
-define(IFOUTQLEN,[1,3,6,1,2,1,2,2,1,21]).
-define(IFSPECIFIC,[1,3,6,1,2,1,2,2,1,22]).
-define(IFALIAS,[1,3,6,1,2,1,31,1,1,1,18]).
-define(IFPORT,[1,3,6,1,2,1,17,1,4,1,2]).
-define(IFHCINOCTETS,[1,3,6,1,2,1,31,1,1,1,6]).
-define(IFHCOUTOCTETS,[1,3,6,1,2,1,31,1,1,1,10]).
-define(BGPPEERLOCALADDR,[1,3,6,1,2,1,15,3,1,5]).
-define(BGPPEERLOCALPORT,[1,3,6,1,2,1,15,3,1,6]).
-define(BGPPEERREMOTEADDR,[1,3,6,1,2,1,15,3,1,7]).
-define(BGPPEERREMOTEPORT,[1,3,6,1,2,1,15,3,1,8]).
-define(CDPCACHEADDRESS,[1,3,6,1,4,1,9,9,23,1,2,1,1,4]).
-define(CDPCACHEVERSION,[1,3,6,1,4,1,9,9,23,1,2,1,1,5]).
-define(CDPCACHEDEVICEID,[1,3,6,1,4,1,9,9,23,1,2,1,1,6]).
-define(CDPCACHEDEVICEPORT,[1,3,6,1,4,1,9,9,23,1,2,1,1,7]).
-define(CDPCACHEPLATFORM,[1,3,6,1,4,1,9,9,23,1,2,1,1,8]).
-define(CDPCACHEVTPMGMTDOMAIN,[1,3,6,1,4,1,9,9,23,1,2,1,1,10]).
-define(CDPCACHENATIVEVLAN,[1,3,6,1,4,1,9,9,23,1,2,1,1,11]).
-define(CDPCACHEDUPLEX,[1,3,6,1,4,1,9,9,23,1,2,1,1,12]).
-define(CDPCACHEMTU,[1,3,6,1,4,1,9,9,23,1,2,1,1,16]).
-define(CPUDUTYUSED,"").
-define(CPUDUTYUNUSED,"").
-define(CPUDUTYONEMINUSED,"").
-define(CPUDUTYFIVEMINUSED,"").
-define(MEMORYSIZE,"").
-define(MEMORYFREE,"").
-define(MEMORYFREERATE,"").
-define(MEMORYUSED,"").
-define(MEMORYUSEDRATE,"").
-define(ENTLOGICALCOMMUNITY,[1,3,6,1,2,1,47,1,2,1,1,4]).


























