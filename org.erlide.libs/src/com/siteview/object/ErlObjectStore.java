package com.siteview.object;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javolution.util.FastList;

import com.ericsson.otp.erlang.OtpConverter;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpGateway;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

import org.erlide.jinterface.ErlLogger;
import org.erlide.jinterface.rpc.IRpcCallSite;


public final class ErlObjectStore
{
	public final static String module = ErlObjectStore.class.getName();
	private static IRpcCallSite b=null;

    public ErlObjectStore()
    {
    }
    
    public static void setBackend(IRpcCallSite backend) {
        b = backend;
    }
  
    public static List<String> get_all_name() throws Exception
    {
		List<Object> list = new ArrayList<Object>();

		if (size() == 0)   return null;
	
	    return (List<String>) b.call2("object", "get_all_name", "");
//	    return (List<String>) b.call2("object", "get_all_name", list);
    }
    
    public static List<ErlObject> get_all() throws Exception
    {
		List<Object> list = new ArrayList<Object>();
		List<String> rtnList = new ArrayList<String>();
		List<ErlObject> erlObjList = FastList.newInstance();
		
		if (size() == 0)   return null;
	
		rtnList = (List<String>) b.call2("object", "get_all_name", "");
		
    	for(String ObjName :  rtnList) {
    		erlObjList.add(new ErlObject(ObjName));
    	}
    	return erlObjList;    	
    }
    
    public static void delete_all() throws Exception
    {
		if (size() > 0)   
		    b.call2("object", "delete_all", "");
    }
    
    public static void delete(String Name) throws Exception
    {
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(Name));
    	b.call2("object", "delete", "a",Name);
    }
    
    public static void delete(List<String> NameList) throws Exception
    {
    	b.call2("object", "delete", "l",NameList);
    }
    
    public static void delete(ErlObject Obj) throws Exception
    {
        delete(Obj.getName());        
    }
    
    public static void delete_list (List<ErlObject> ObjList) throws Exception {
        List<String> objNameList = new ArrayList<String>();
        for(ErlObject obj : ObjList ) {
            objNameList.add(obj.getName());
        }
        delete(objNameList);
    }
    
    public static Boolean isValidName (String Value) throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(Value));      
        return  (Boolean) b.call2("object", "isValidName", "a",Value);      
    }
    
    public static Long size() throws  Exception {
        List<Object> list = new ArrayList<Object>();
        return  (Long) b.call2("object", "get_size", "");        
    }
    
    public static ErlObject create(String Type, String Name) throws Exception{
		List<Object> list = new ArrayList<Object>();
		list.add(new OtpErlangAtom(Type)); 
		list.add(new OtpErlangAtom("start")); 
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(new OtpErlangAtom(Name));
		list.add(paramList);
		String rtnObj = (String) b.call2("erlang", "apply", "aal",Type,"start",paramList);
    	return new ErlObject(Type,rtnObj);
    }
    
    public static ErlObject get_by_name(String Name) throws Exception{
                
		return isValidName(Name) ? new ErlObject(Name) : null;		
    }
    
    public static void add_fact(String engine,List<Object> fact) throws Exception{
        List<Object> list = new ArrayList<Object>();
        list.add(new OtpErlangAtom(engine));
        list.add(OtpConverter.Object2OtpErlangObject(fact.toArray()));        
        b.call2("object", "add_fact", "sl",engine,fact);      
    }
    
    public static List<ErlObject> get_by_attr(String AttributeName, String AttributeValue) throws Exception{
		List<Object> list = new ArrayList<Object>();
		List<ErlObject> objList = new ArrayList<ErlObject>();
		
		list.add(AttributeName);
		list.add(AttributeValue);
		
		List<String> rtnList = (List<String>) b.call2("object", "jget_by_attr", "");		
		
		for(String objname : rtnList) {
			objList.add(new ErlObject(objname));			
		}
		
		return objList;
    }

    public static List<ErlObject> get_by_attr(String AttributeName, List<String> AttributeValueList) throws Exception{
		List<Object> list = new ArrayList<Object>();
		List<OtpErlangAtom> paramList = new ArrayList<OtpErlangAtom>();
		List<ErlObject> objList = new ArrayList<ErlObject>();
		
		for(String AttributeValue : AttributeValueList) {
			paramList.add(new OtpErlangAtom(AttributeValue));		
		}
		
		list.add(AttributeName);
		list.add(paramList);
		
		List<String> rtnList = (List<String>) b.call2("object", "jget_by_attr", "");		
		
		for(String objname : rtnList) {
			objList.add(new ErlObject(objname));			
		}
		
		return objList;
    }
    
    
    public static void main(String args[]) throws Exception
    {
        ErlObjectStore store = new ErlObjectStore();
        List<String> list = new ArrayList<String>();
        List<ErlObject> objlist = new ArrayList<ErlObject>();
//      store.create("point","point99");
        store.create("ping_monitor","point99");
        ErlObject pointTest = store.get_by_name("point99");

        final String actual = pointTest.getName();
        list  = store.get_all_name();
        objlist  = store.get_all();
        store.delete_all();

        objlist = store.get_all();
        System.out.println(list.toString());
        System.out.println(objlist.toString());
    }
    
    
    
}