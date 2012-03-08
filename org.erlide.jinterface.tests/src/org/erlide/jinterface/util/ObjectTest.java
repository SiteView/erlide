package org.erlide.jinterface.util;

import java.sql.Timestamp;
import java.util.Map;


import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.otp.erlang.OtpErlangPid;
import com.siteview.object.ErlObject;
import com.siteview.object.ErlObjectStore;

public class ObjectTest {
	
    @Test
    public void createObjTest() throws Throwable {
    	ErlObject pingTest = ErlObjectStore.create("ping_monitor","pingTest");
    	
        final String expected = "pingTest";
        final String actual = pingTest.getName();
        ErlObjectStore.delete("pingTest");
        Assert.assertEquals(expected, actual);
    }

//    @Test
    public void deleteAllTest() throws Throwable {
    	ErlObjectStore.delete_all();
        final Long expected = 0L;
        final Long actual = ErlObjectStore.size();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void deleteObjTest() throws Throwable {
       	ErlObject pingTest = ErlObjectStore.create("ping_monitor","pingDeleteTest");
       	ErlObjectStore.delete(pingTest);    	
    	
        Assert.assertFalse(ErlObjectStore.isValidName("pingDeleteTest"));
    }
    
    @Test
    public void isValideNameTest() throws Throwable {
   	
        Assert.assertFalse(ErlObjectStore.isValidName("noname"));
    }
    
    @Test
    public void attrTest() throws Throwable {
       	ErlObject pingTest = ErlObjectStore.create("ping_monitor","ping1");
    	pingTest.set("X", 10);
    	   	
        final int  expected = 10;
        final int actual = Integer.parseInt(pingTest.get("X").toString());

        
        ErlObjectStore.delete("ping1"); 
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void timedValueTest() throws Throwable {
    	int expected, actual;
        ErlObjectStore.delete("ping1"); 
       	ErlObject pingTest = ErlObjectStore.create("ping_monitor","ping1");
    	pingTest.setTimedValue("X", 1);
    	actual = Integer.parseInt(pingTest.getLatestValue("X").toString());
        Assert.assertEquals(1, actual);
    	pingTest.setTimedValue("X", 2);
    	actual = Integer.parseInt(pingTest.getLatestValue("X").toString());
    	Assert.assertEquals(2, actual);
    	pingTest.setTimedValue("X", 3);
    	actual = Integer.parseInt(pingTest.getLatestValue("X").toString());
    	Assert.assertEquals(3, actual);
    	
    	actual = Integer.parseInt(pingTest.getValueWithTime("X").get("value").toString());
    	Assert.assertEquals(3, actual);

//    	Timestamp ts =  (Timestamp) pingTest.getValueWithTime("X").get("timestamp");
    	Assert.assertTrue(pingTest.getValueWithTime("X").get("timestamp") instanceof Timestamp);
    	
    	actual =  pingTest.getValueHistory("X").size();
    	Assert.assertEquals(3, actual);

        ErlObjectStore.delete("ping1"); 
    }
    
    @Test
    public void get_defined_attrs() throws Throwable {
    	ErlObjectStore.delete("pingTest"); 
       	ErlObject pingTest = ErlObjectStore.create("ping_monitor","pingTest");
    	pingTest.set("X", 10);
    	pingTest.set("Y", 100);
    	
    	Map<String,Object> attrs = pingTest.get_defined_attrs();
    	   	
        final int  expected = 100;
        final int actual = Integer.parseInt(attrs.get("Y").toString());

        ErlObjectStore.delete("pingTest"); 
        Assert.assertEquals(10, Integer.parseInt(attrs.get("X").toString()));
        Assert.assertEquals(100, Integer.parseInt(attrs.get("Y").toString()));
    }
    
    @Test
    public void get_system_attrs() throws Throwable {
    	ErlObjectStore.delete("pingTest"); 
    	String className = "ping_monitor";
    	String objName = "pingTest";
       	ErlObject pingTest = ErlObjectStore.create(className,objName);
    	
    	Map<String,Object> attrs = pingTest.get_system_attrs();
    	   	
        ErlObjectStore.delete(objName); 
        
        Assert.assertEquals(className, attrs.get("class").toString());
        Assert.assertEquals(objName, attrs.get("name").toString());
    }

    @Test
    public void ping_monitor_test() throws Throwable {
    	ErlObjectStore.delete("pingTest"); 
    	String className = "ping_monitor";
    	String objName = "0.1.12";
       	ErlObject pingTest = ErlObjectStore.create(className,objName);
       	
//       	Map<String,Object> params = FastMap.newInstance();
//       	params.put("frequency", 10);
       	pingTest.set("error_frequency",0);
       	pingTest.set("frequency",600);       	
       	pingTest.set("svobjId","10181");
       	pingTest.set("_proxy", "default");
       	pingTest.set("id","0.1.12");
       	pingTest.set("m_class","ping_monitor");
       	pingTest.set("m_name","asd");
       	pingTest.set("disabled",false);
       	pingTest.set("verfiy_error",false);
       	pingTest.set("depends_condition","good");
       	pingTest.set("depends_on","none");
       	pingTest.set("schedule","all");
       	pingTest.set("activate_baseline",false);        
       	pingTest.set("parent","0.1");
       	pingTest.set("svobjId","10181");
       	pingTest.set("hostname","www.sina.com.cn");
       	pingTest.set("timeout",3000);
       	pingTest.set("size",32);
    	Map<String,Object> attrs = pingTest.get_system_attrs();
    	   	
        ErlObjectStore.delete(objName); 
        
        Assert.assertEquals(className, attrs.get("class").toString());
        Assert.assertEquals(objName, attrs.get("name").toString());
    }

    
}
