exports = {
    sys: require({module: "beamjs_mod_sys"}),
    ping_monitor: require({module: "ping_monitor"})
};

//var sys = require({module: "beamjs_mod_sys"});
var strClassifier = "";
f = function (x) { 
	return x * 10 
};

f2 = function (x) { 
	return x + 10 
	};

erlang_run = function (x)
{
	var strInfo = "erlang_run debug .............................. input x is......:";
	exports.ping_monitor.print(strInfo + x + "\n");
	return f(x) * f2(x) 
};

analysis_ping_Data= function (name, attrnames, data)
{
	var strInfo = "......js.................analysis_ping_Data  debug Info...... input  is:name:";
	exports.sys.print(strInfo + name + "....attrnames:" + attrnames + "....data:" + data +  "\n");

	//exports.ping_monitor.analysis_Data(x);
	
	var values = exports.ping_monitor.js_getTimedValue(name, attrnames);

	/*var beamjs = require({module: "beamjs_mod_beamjs"}),
	 beamjs.VM.current.run(expr, function (result) {
							   if (result != null) {
								   exports.sys.print("beamjs.VM.current.run expr:" + expr + result + "\n");
							   }
						   });*/

	return values;
};


