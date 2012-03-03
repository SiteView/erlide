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


public class ErlObject
{
	public final static String module = ErlObject.class.getName();
	public String Name = null;
	public String Type = null;

    public ErlObject()
    {
    }
    
    public ErlObject(String ObjName) throws Exception
    {
    	this.Name = ObjName;
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        this.Type = OtpGateway.getOtpInterface().call("object", "getClass", list).toString();
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
    	OtpGateway.getOtpInterface().call("object", "set", list);
    }
    
    public void setTimedValue(String  AttributeName,Object AttributeValue) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        list.add(new OtpErlangAtom(AttributeName));
        list.add(OtpConverter.Object2OtpErlangObject(AttributeValue));
        OtpGateway.getOtpInterface().call("object", "setTimedValue", list);
    }

    public Object get(String AttributeName) throws Exception
    {
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(Name));
		list.add(new OtpErlangAtom(AttributeName));
    	return OtpGateway.getOtpInterface().call("object", "get", list);
    }
    
    public Map getValueWithTime(String AttributeName) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        list.add(new OtpErlangAtom(AttributeName));
        Object[] timedValue =(Object[]) OtpGateway.getOtpInterface().call("object", "getValueWithTime", list);
        Map <String,Object> map = new HashMap();
        map.put("value", timedValue[0]);
        map.put("timestamp", (Timestamp)timedValue[1]);
        return map;        
    }
    
    public Object getLatestValue(String AttributeName) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        list.add(new OtpErlangAtom(AttributeName));
        return OtpGateway.getOtpInterface().call("object", "getTimedValue", list);
    }
    
    public List<Object> getValueHistory(String AttributeName) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        List<Object> rtnList = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        list.add(new OtpErlangAtom(AttributeName));
        List<Object> historyValue = (List)  OtpGateway.getOtpInterface().call("object", "getValueHistory", list);
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
    	return OtpGateway.getOtpInterface().call("object", "call", list);
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
        return (OtpErlangPid) OtpGateway.getOtpInterface().call("object", "executorof", list);

    }
    
    public String getState () throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        return (String) OtpGateway.getOtpInterface().call("object", "stateof", list);

    }
    
    public Long getMem () throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
        return (Long) OtpGateway.getOtpInterface().call("object", "total_mem", list);

    }
    
    public Map<String,Object> get_defined_attrs() throws Exception
    {
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(Name));
    	return (Map<String,Object>) OtpGateway.getOtpInterface().call("object", "get_defined_attrs", list);
    }
    
    public Map<String,Object> get_system_attrs() throws Exception
    {
		List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Name));
    	return (Map<String,Object>) OtpGateway.getOtpInterface().call("object", "get_system_attrs", list);
    }

    public Long get_max() throws Exception
    {
        List<Object> list = new ArrayList<Object>();
//        list.add(new OtpErlangAtom(Type));
        return (Long) OtpGateway.getOtpInterface().call(Type, "get_max", list);
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