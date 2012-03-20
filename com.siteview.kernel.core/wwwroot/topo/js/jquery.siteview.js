var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
function siteview() {
    
}

siteview.uuid = function uuid(len, radix) {
    var chars = CHARS, uuid = [];
    radix = radix || chars.length;

    if (len) {
        // Compact form
        for (var i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
    } else {
        // rfc4122, version 4 form
        var r;

        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';

        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (var i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | Math.random() * 16;
                uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
            }
        }
    }

    return uuid.join('');
};

siteview.QueryString = function(sName) {
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
};

siteview.FindNodeFromArray = function(arr, id) {
    for (var i = 0; i < arr.length; i++) {
        var node = arr[i];
        if (id == node.Id())
            return node;
    }
    return null;
}

siteview.FindVertexFromArray = function(arr, id) {
    for (var i = 0; i < arr.length; i++) {
        var v1 = arr[i];
        if (id == v1.id)
            return v1;
    }
    return null;
}

siteview.FindLinkFromArray = function(arr, id) {
    for (var i = 0; i < arr.length; i++) {
        var v1 = arr[i];
        if (id == v1.Id())
            return v1;
    }

    return null;
}

siteview.Link = function(from, fromport, to, toport) {
    var _value = {
        id: "",
        fromid: "",
        fromport: "",
        fromportdescr: "",
        toid: "",
        toport: "",
        toportdescr: "",
        innerHtml: "",
        usage: ""
    };

    if (from) _value.fromid = from;
    if (fromport) _value.fromport = fromport;
    if (to) _value.toid = to;
    if (toport) _value.toport = toport;

    this.Id = function(val) {
        if (val)
            _value.id = val;
        return _value.id;
    }
    
    this.From = function(val) {
        if (val)
            _value.fromid = val;

        return _value.fromid;
    }

    this.FromPort = function(val) {
        if (val)
            _value.fromport = val;

        return _value.fromport;
    }

    this.FromPortDescr = function(val) {
        if (val)
            _value.fromportdescr = val;

        return _value.fromportdescr;
    }

    this.To = function(val) {
        if (val)
            _value.toid = val;

        return _value.toid;
    }

    this.ToPort = function(val) {
        if (val)
            _value.toport = val;

        return _value.toport;
    }

    this.ToPortDescr = function(val) {
        if (val)
            _value.toportdescr = val;

        return _value.toportdescr;
    }

    this.Usage = function(val) {
        if (val)
            _value.usage = val;

        return _value.usage;
    }
}

siteview.Node = function(id, ip, x, y, type) {
    var _value = {
        id: "",
        ip: "",
        x: 50,
        y: 50,
        type: ""
    };

    if (id) _value.id = id;
    if (ip) _value.ip = ip;
    if (x) _value.x = x;
    if (y) _value.y = y;
    if (type) _value.type = type;

    this.Id = function(val) {
        if (val)
            _value.id = val;

        return _value.id;
    }
    this.Ip = function(val) {
        if (val)
            _value.ip = val;

        return _value.ip;
    }
    this.X = function(val) {
        if (val)
            _value.x = parseInt(val);

        return _value.x;
    }
    this.Y = function(val) {
        if (val)
            _value.y = parseInt(val);

        return _value.y;
    }
    this.Type = function(val) {
        if (val)
            _value.type = val;

        return _value.type;
    }
}