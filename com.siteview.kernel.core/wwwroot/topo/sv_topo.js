// JScript source code
SET_DHTML(CURSOR_MOVE, "page__myCanvas");

var cnv = document.getElementById("page__myCanvas");
var jg = new jsGraphics(cnv);

var maxx = 0, maxy = 0;
var file = QueryString("file");

if (!file || file == "") file = "topodata.xml";
LoadTopoData(file);



//document.onmousemove = mouseMove;
//document.onmouseup   = mouseUp;

////存放我们想要拖动的DOM对象   
var dragObject = null;
var mouseOffset = null;
//function mouseMove(ev){   
//   ev = ev || window.event;
//   //这里用到了上面第一点写的函数（mouseCoords
//    var mousePos = mouseCoords(ev);
//  
//    if(dragObject){
//        //dragObject.style.position = 'absolute';
//        dragObject.style.position = 'relative';     
//        dragObject.style.top      = mousePos.y;   
//        dragObject.style.left     = mousePos.x;   
//  
//        return false;   
//    }   
//}

//function mouseUp(){   
//    dragObject = null;   
//}   
//  
function makeDraggable(item) {
    dragObject = item;
    //    if(!item) return;   
    //    item.onmousedown = function(ev){   
    //        dragObject  = this;   
    //        mouseOffset = getMouseOffset(this, ev);   
    //        return false;   
    //    }
}

//function mouseCoords(ev){   
//   if (ev.pageX || ev.pageY) {   
//       return {   
//           x: ev.pageX,   
//           y: ev.pageY   
//       };   
//   }   
//   return {   
//       x: ev.clientX + document.body.scrollLeft - document.body.clientLeft - 30   
//        y: ev.clientY + document.body.scrollTop - document.body.clientTop - 40  
//    };   
//} 
//} 

//function  getMouseOffset(target, ev)  {   
//  ev = ev || window.event;   
//  var docPos = getPosition(target);   
//  var mousePos = mouseCoords(ev);   
//  return {x:mousePos.x - docPos.x, y:mousePos.y - docPos.y} ;   
//}   
//  
//function getPosition(e) {   
//   var left = 0;   
//   var top = 0;   
//   while (e.offsetParent) {   
//     left += e.offsetLeft;   
//     top += e.offsetTop;   
//     e = e.offsetParent;   
//  }   
//  left += e.offsetLeft;   
//  top += e.offsetTop;   
//  return {x:left, y:top} ;
//}

function LoadTopoData(file) {
    jg.clear();
    var mygetrequest = new ajaxRequest();
    if (mygetrequest.overrideMimeType) {
        mygetrequest.overrideMimeType('text/xml');
    }
    mygetrequest.onreadystatechange = function() {
        if (mygetrequest.readyState == 4) {
            if (mygetrequest.status == 200) {

                var xmldata = mygetrequest.responseXML;
                DrawTopoChart(xmldata);

            }
            else {
                alert("An error has occured making the request")
            }
        }
    }

    mygetrequest.open("GET", file, true);
    mygetrequest.send(null);  //send GET request
}


function DrawTopoChart(xmldata) {
    var root = xmldata.getElementsByTagName('TopGraph');
    var temp = root.item(0).getElementsByTagName('Lines');
    if (temp.length > 0) {
        lines = temp.item(0).getElementsByTagName('Line');
        jg.setColor("#00F000");

        for (var i = 0; i < lines.length; i++) {
            DrawTopoLine(lines.item(i));
        }
    }
    var nodes = root.item(0).getElementsByTagName('Nodes');
    temp = root.item(0).getElementsByTagName('Nodes');
    if (temp.length > 0) {
        nodes = temp.item(0).getElementsByTagName('Node');
        jg.setColor("#000000");
        for (var i = 0; i < nodes.length; i++) {
            DrawTopoNode(nodes.item(i));
        }
    }
    dd.elements.page__myCanvas.resizeTo(maxx+100, maxy+200);
    //cnv.style.width = maxx+200;
    //cnv.style.height = maxy+200;
    jg.paint();

}

function DrawTopoLine(line) {
    var x1 = 50+16 + parseInt(line.getElementsByTagName('From').item(0).getAttribute("x"), 10);
    var y1 = 16 + parseInt(line.getElementsByTagName('From').item(0).getAttribute("y"), 10);
    var x2 = 50+16 + parseInt(line.getElementsByTagName('To').item(0).getAttribute("x"), 10);
    var y2 = 16 + parseInt(line.getElementsByTagName('To').item(0).getAttribute("y"), 10);

    if (x1 > maxx) maxx = x1;
    if (x2 > maxx) maxx = x2;
    if (y1 > maxy) maxy = y1;
    if (y2 > maxy) maxy = y2;
    jg.drawLine(x1, y1, x2, y2); // co-ordinates related to "myCanvas"

}

function DrawTopoNode(node) {
    var x1 = 50+parseInt(node.getAttribute("x"));
    var y1 = parseInt(node.getAttribute("y"));
    var type = node.getAttribute("type");
    var ip = node.getAttribute("ip");

    if (x1 > maxx) maxx = x1;
    if (y1 > maxy) maxy = y1;

    var img;

    switch (type) { //0-三层交换机，1-二层交换机，2-路由器，3-防火墙，4-服务器，5-PC终端，6-其他
        case "0":
            img = "/topo/images/bmp_SwitchRouter_Blue.bmp"; break;
        case "1":
            img = "/topo/images/bmp_Switch_Blue.bmp"; break;
        case "2":
            img = "/topo/images/bmp_Router_Blue.bmp"; break;
        case "3":
            img = "/topo/images/bmp_Firewall_Blue.bmp"; break;
        case "4":
            img = "/topo/images/bmp_Server_Blue.bmp"; break;
        case "5":
            img = "/topo/images/bmp_PC_Blue.bmp"; break;
        case "6":
            img = "/topo/images/bmp_Other_Blue.bmp"; break;

    }

    jg.drawImage(img, x1, y1, 32, 32, '', '设备信息： ' + ip);
    jg.drawString(ip, x1 - 30, y1 + 30);
}

function ajaxRequest() {
    var activexmodes = ["Msxml2.XMLHTTP", "Microsoft.XMLHTTP"] //activeX versions to check for in IE
    if (window.ActiveXObject) { //Test for support for ActiveXObject in IE first (as XMLHttpRequest in IE7 is broken)
        for (var i = 0; i < activexmodes.length; i++) {
            try {
                return new ActiveXObject(activexmodes[i])
            }
            catch (e) {
                //suppress error
            }
        }
    }
    else if (window.XMLHttpRequest) // if Mozilla, Safari etc
    {
        return new XMLHttpRequest();
    }
    else
        return false
}

function QueryString(sName) {
    var sSource = String(window.document.location);
    var sReturn = "";
    var sQUS = "?";
    var sAMP = "&";
    var sEQ = "=";
    var iPos;

    iPos = sSource.indexOf(sQUS);

    var strQuery = sSource.substr(iPos, sSource.length - iPos);
    var strLCQuery = strQuery.toLowerCase();
    var strLCName = sName.toLowerCase();

    iPos = strLCQuery.indexOf(sQUS + strLCName + sEQ);
    if (iPos == -1) {
        iPos = strLCQuery.indexOf(sAMP + strLCName + sEQ);
        if (iPos == -1)
            return "";
    }

    sReturn = strQuery.substr(iPos + sName.length + 2, strQuery.length - (iPos + sName.length + 2));
    var iPosAMP = sReturn.indexOf(sAMP);

    if (iPosAMP == -1)
        return sReturn;
    else {
        sReturn = sReturn.substr(0, iPosAMP);
    }

    return sReturn;
}   