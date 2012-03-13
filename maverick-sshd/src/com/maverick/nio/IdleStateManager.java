package com.maverick.nio;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IdleStateManager
{
  Map C = Collections.synchronizedMap(new HashMap());
  int B;
  int A;
  long D = 0L;
  boolean E = false;

  public IdleStateManager(int paramInt1, int paramInt2)
  {
    this.B = paramInt1;
    this.A = paramInt2;
  }

  public synchronized void reset(IdleStateListener paramIdleStateListener)
  {
    if (this.C.containsKey(paramIdleStateListener))
      this.C.put(paramIdleStateListener, new Long(System.currentTimeMillis()));
  }

  public synchronized void register(IdleStateListener paramIdleStateListener)
  {
    this.C.put(paramIdleStateListener, new Long(System.currentTimeMillis()));
  }

  public synchronized void remove(IdleStateListener paramIdleStateListener)
  {
    if (!this.E)
      this.C.remove(paramIdleStateListener);
  }

  public synchronized boolean isReady()
  {
    return (System.currentTimeMillis() - this.D) / 1000L >= this.B;
  }

  public synchronized void service()
  {
    this.D = System.currentTimeMillis();
    this.E = true;
    Iterator localIterator = this.C.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      long l1 = ((Long)localEntry.getValue()).longValue();
      long l2 = System.currentTimeMillis();
      long l3 = (l2 - l1) / 1000L;
      if ((l3 >= this.B * this.A) && (((IdleStateListener)localEntry.getKey()).idle()))
        localIterator.remove();
    }
    this.E = false;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.IdleStateManager
 * JD-Core Version:    0.6.0
 */