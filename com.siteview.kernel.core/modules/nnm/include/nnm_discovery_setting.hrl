%%snmp param setting
-record(nnm_snmpPara,{
		ip={},
		port=161,
		getCommunity="public",
		setCommunity="private",
		timeout=2000,
		retry=2,
		snmpver="v1",
		authType="",
		user="",
		passwd="",
		privPasswd="",
		contextID="",
		contextName=""
		}).

-record(nnm_deviceLoginType,{
							 loginid,
							 ip="",
							 snmpversion="",
							 community="",
							 port="",
							 authtype="",
							 user="",
							 password="",
							 privpassword="",
							 contextid="",
							 contextname="",
							 logintype="",
							 timeout=""
							 }).

-record(nnm_scan_param,{
						scanparamID,
						scanSeed="",
						includeIp="",
						excludeIp="",
						threads="",
						depth="",
						scanType=""
						}).


