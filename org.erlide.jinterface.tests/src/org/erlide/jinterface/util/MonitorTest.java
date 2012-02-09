package org.erlide.jinterface.util;

import junit.framework.Assert;

import org.junit.Test;

import com.siteview.object.ErlObject;
import com.siteview.object.ErlObjectStore;

public class MonitorTest {
    @Test
    public void wakeupTest() throws Throwable {
    	ErlObjectStore.delete_all();
    	
    	ErlObject ping1 = ErlObjectStore.create("ping_monitor","ping1");
    	
    	Assert.assertEquals("start", ping1.getState());
    	ping1.add_fact("wakeup");
    	Assert.assertEquals("waiting", ping1.getState());
    }
}
