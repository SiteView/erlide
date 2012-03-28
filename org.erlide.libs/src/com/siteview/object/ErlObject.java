package com.siteview.object;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import com.ericsson.otp.erlang.OtpConverter;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpGateway;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpMsg;
import com.ericsson.otp.erlang.OtpNode;

import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.IRpcCallSite;


public class ErlObject
{
	public final static String module = ErlObject.class.getName();
	public String Name = null;
	public String Type = null;
    private static IRpcCallSite b=null;

    public ErlObject()
    {
    }
    
    public static void setBackend(IRpcCallSite backend) {
        b = backend;
    }
    
    public ErlObject(String ObjName) throws Exception
    {
    	this.Name = ObjName;
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        this.Type = b.call2("object", "getClass", "a",ObjName).toString();
    }
    
    public ErlObject(String ObjType, String ObjName)
    {
    	this.Name = ObjName;
    	this.Type = ObjType;
    }
    
    public void set(String  AttributeName,Object AttributeValue) throws Exception
    {
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(Name));
		list.add(new OtpErlangAtom(AttributeName));
		list.add(OtpConverter.Object2OtpErlangObject(AttributeValue));
        b.call2("object", "set", "aax",Name,AttributeName,AttributeValue);
    }
    
    public void setTimedValue(String  AttributeName,Object AttributeValue) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        list.add(new OtpErlangAtom(AttributeName));
        list.add(OtpConverter.Object2OtpErlangObject(AttributeValue));
        b.call2("object", "setTimedValue", "aax",Name,AttributeName,AttributeValue);
    }

    public Object get(String AttributeName) throws Exception
    {
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(Name));
		list.add(new OtpErlangAtom(AttributeName));
    	return b.call2("object", "get","aa",Name,AttributeName);
    }

//  @doc get the latest value with timestamp map: {value=Value,timestamp=Time}    
    public Map getValueWithTime(String AttributeName) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        list.add(new OtpErlangAtom(AttributeName));
        Object[] timedValue =(Object[]) b.call2("object", "getValueWithTime", "aa",Name,AttributeName);
        Map <String,Object> map = new HashMap();
        map.put("value", timedValue[0]);
        map.put("timestamp", (Timestamp)timedValue[1]);
        return map;        
    }
    
//  @doc get the latest value without timestamp
    public Object getLatestValue(String AttributeName) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        list.add(new OtpErlangAtom(AttributeName));
        return b.call2("object", "getTimedValue", "aa",Name,AttributeName);
    }
    
//    @doc get a list of value [{value,timestamp}, ...]
    public List<Object> getValueHistory(String AttributeName) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        List<Object> rtnList = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        list.add(new OtpErlangAtom(AttributeName));
        List<Object> historyValue = (List)  b.call2("object", "getValueHistory", "aa",Name,AttributeName);
        for(Object obj : historyValue) {
            Map <String,Object> map = new HashMap();
            Object[] timedValue = (Object[]) obj;
            map.put("value", timedValue[0]);
            map.put("timestamp", timedValue[1]);
            rtnList.add(map);            
        }
        return rtnList;        
    }
    
    public Object call(String Method, List<Object> params) throws Exception {
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(Name));   	
		list.add(new OtpErlangAtom(Method));   	
		list.add(OtpConverter.Object2OtpErlangObject(params));
    	return b.call2("object", "call", "aal",Name,Method,params);
    }

    public String getName () {
        return this.Name;
    }

    public String getType () {
        return this.Type;
    }

    public OtpErlangPid getExecutor () throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        return (OtpErlangPid) b.call2("object", "executorof", "a",Name);

    }
    
    public String getState () throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        return (String) b.call2("object", "stateof", "a",Name);

    }
    
    public Long getMem () throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        return (Long) b.call2("object", "total_mem", "a",Name);

    }
    
    public Map<String,Object> get_defined_attrs() throws Exception
    {
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(Name));
    	return (Map<String,Object>) b.call2("object", "get_defined_attrs", "a",Name);
    }
    
    public Map<String,Object> get_system_attrs() throws Exception
    {
		List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
    	return (Map<String,Object>) b.call2("object", "get_system_attrs", "a",Name);
    }

    public Long get_max() throws Exception
    {
        List<Object> list = new ArrayList<Object>();
//        list.add(new OtpErlangAtom(Type));
        return (Long) b.call2(Type, "get_max", "");
    }

    public void add_fact(List<Object> fact) throws Exception{
        ErlObjectStore.add_fact(Name, fact);
    }
    
    public void add_fact(String fact) throws Exception{
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(fact));
        ErlObjectStore.add_fact(Name, list);
    }
}