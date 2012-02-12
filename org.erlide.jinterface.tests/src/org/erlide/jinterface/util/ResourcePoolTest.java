package org.erlide.jinterface.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.siteview.object.ErlObject;
import com.siteview.object.ErlObjectStore;
import com.siteview.object.ResourcePool;

public class ResourcePoolTest {
    @Test
    public void requestTest() throws Throwable {
    	ErlObjectStore.delete("pingTest");
        ErlObject pingTest = ErlObjectStore.create("ping_monitor","pingTest");
        Long Counter0 = ResourcePool.get_counter(pingTest);
        Long Max= pingTest.get_max();
        Long N = 3L;
        for (long i = 0;i<N;i++)
        	ResourcePool.request(pingTest,"frequency");
        Long Counter1 = ResourcePool.get_counter("pingTest");
        
        final Long expected = Counter1 - Counter0;
        final Long actual = (Counter0 + N) % Max;
    	
        Assert.assertEquals(expected, actual);
        ResourcePool.set_counter(pingTest, Counter0);
        ErlObjectStore.delete("pingTest");
    }
}