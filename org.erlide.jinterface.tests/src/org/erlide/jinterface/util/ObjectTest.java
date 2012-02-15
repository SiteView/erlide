package org.erlide.jinterface.util;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

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
       	ErlObject pingTest = ErlObjectStore.create("ping_monitor","ping_monitor1");
    	pingTest.set("X", 10);
    	   	
        final int  expected = 10;
        final int actual = Integer.parseInt(pingTest.get("X").toString());

        ErlObjectStore.delete("ping_monitor1"); 
        Assert.assertEquals(expected, actual);
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
}
