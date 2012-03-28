exports = {
    sys: require({module: "beamjs_mod_sys"}),
    object: require({module: "object"})
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

analysis_ping_Data = function (name, attrnames, data)
{
	var strInfo = "......js.................analysis_ping_Data  debug Info...... input  is:name:";
	exports.sys.print(strInfo + name + "....attrnames:" + attrnames + "....data:" + data +  "\n");

	//exports.ping_monitor.analysis_Data(x);
	
	var values = exports.object.getTimedValue(name, attrnames);

//	var beamjs = require({module: "beamjs_mod_beamjs"});
//	var test = 	beamjs.vm_run("1+1");
	/* beamjs.VM.current.run(expr, function (result) {
							   if (result != null) {
								   exports.sys.print("beamjs.VM.current.run expr:" + expr + result + "\n");
							   }
						   });*/
	//values = exports.sys.run_jseval("3+7");
	return values;
};

run_Calendar = function (x)
{
	icalParser.parseIcal(x);

	//icalParser.ical is now set
	icalParser.ical.version; 
	icalParser.ical.prodid;

	////Arrays
	//All the vevent elements
	icalParser.ical.events;
	//All the vtodo elements
	icalParser.ical.todos;
	//All the journal elements
	icalParser.ical.journals;
	//All the freebusy elements
	icalParser.ical.freebusy;
	
	return x;
};

run_Classifier = function (name, attrnames, data)
{
	return name;
};