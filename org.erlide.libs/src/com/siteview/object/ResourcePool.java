package com.siteview.object;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javolution.util.FastList;

import com.ericsson.otp.erlang.OtpConverter;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpGateway;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;


public final class ResourcePool
{
	public final static String module = ResourcePool.class.getName();

    public ResourcePool()
    {
    }
  
    public static void request(String name,String type) throws Exception
    {
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(name));
        list.add(new OtpErlangAtom(type));

    	OtpGateway.getOtpInterface().call("resource_pool", "request", list);
    }
    
    public static void request(ErlObject obj,String Type) throws Exception
    {
        request(obj.getName(),Type);
    }
    
    public static Long get_counter(String name) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(name));

        return (Long) OtpGateway.getOtpInterface().call("resource_pool", "get_counter", list);
    }
    
    public static Long get_counter(ErlObject obj) throws Exception
    {
        return get_counter(obj.getName());
    }
    
    public static void  set_counter(String name, Long Counter) throws Exception
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(name));
        list.add(Counter);

        OtpGateway.getOtpInterface().call("resource_pool", "set_counter", list);
    }
    
    public static void set_counter(ErlObject obj, Long Counter) throws Exception
    {
        set_counter(obj.getName(),Counter);
    }
    
    public static int size() throws Exception
    {
        return get_pools().size();        
    }
    
    public static List<Object> get_pools() throws Exception
    {
        List<Object> list = new ArrayList<Object>();
//        list.add("resource_pool");
        List<Object> rtnList = (List<Object>)OtpGateway.getOtpInterface().call("resource_pool", "get_pools", list);
        return rtnList;        
    }
}