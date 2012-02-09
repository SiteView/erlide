package org.erlide.jinterface.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.siteview.object.ErlObjectStore;

public class ResourcePoolTest {
    @Test
    public void singleTest() throws Throwable {
    	List<Object> list = new ArrayList<Object>();
    	list.add(new OtpErlangAtom("ping1"));
    	list.add(new OtpErlangAtom("single_test"));
    	ErlObjectStore.add_fact("pool",list );
        final String expected = "ok";
        final String actual = (String)ErlObjectStore.get_by_name("pool").get("test");        
        Assert.assertEquals(expected, actual);
        ErlObjectStore.get_by_name("pool").set("test","error");
    }
    
    @Test
    public void doubleTest() throws Throwable {
    	List<Object> list = new ArrayList<Object>();
    	list.add(new OtpErlangAtom("ping1"));
    	list.add(new OtpErlangAtom("double_test"));
    	ErlObjectStore.add_fact("pool",list );
        final String expected = "ok";
        final String actual = (String)ErlObjectStore.get_by_name("pool").get("test");        
        Assert.assertEquals(expected, actual);
        ErlObjectStore.get_by_name("pool").set("test","error");
    }
}
