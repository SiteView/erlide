// TODO: how to test sequence

package org.erlide.jinterface.util;

import junit.framework.Assert;

import org.junit.Test;

import com.siteview.object.ErlObject;
import com.siteview.object.ErlObjectStore;

public class MonitorTest {
    @Test
    public void wakeupTest() throws Throwable {
    	ErlObject pingTest = ErlObjectStore.create("ping_monitor","pingTest");
    	Assert.assertEquals("start", pingTest.getState());
    	pingTest.add_fact("wakeup");
    	Assert.assertEquals("waiting", pingTest.getState());
    	ErlObjectStore.delete("pingTest");
    }
}
