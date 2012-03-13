package com.maverick.nio;

import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectorThreadPool
{
  SelectorThreadImpl B;
  ArrayList D = new ArrayList();
  int C;
  int H;
  int E;
  int F;
  int A;
  SelectorProvider G;

  public SelectorThreadPool(SelectorThreadImpl paramSelectorThreadImpl, int paramInt1, int paramInt2, int paramInt3, int paramInt4, SelectorProvider paramSelectorProvider)
    throws IOException
  {
    this.B = paramSelectorThreadImpl;
    this.C = paramInt1;
    this.H = paramInt2;
    this.F = paramInt3;
    this.A = paramInt4;
    this.G = paramSelectorProvider;
    for (int i = 0; i < paramInt1; i++)
      A();
    this.E = 0;
  }

  public synchronized void shutdown()
  {
    Iterator localIterator = this.D.iterator();
    while (localIterator.hasNext())
    {
      SelectorThread localSelectorThread = (SelectorThread)localIterator.next();
      localSelectorThread.shutdown();
    }
  }

  synchronized void A(SelectorThread paramSelectorThread)
  {
    this.D.remove(paramSelectorThread);
  }

  private synchronized SelectorThread A()
    throws IOException
  {
    SelectorThread localSelectorThread = new SelectorThread(this, this.B, this.D.size() < this.C, this.H, this.D.size() + 1, this.F, this.A, this.G);
    this.D.add(localSelectorThread);
    localSelectorThread.start();
    return localSelectorThread;
  }

  public synchronized SelectorThread selectNextThread()
    throws IOException
  {
    int i = -1;
    int j = 0;
    for (int m = 0; m < this.D.size(); m++)
    {
      SelectorThread localSelectorThread = (SelectorThread)this.D.get(m);
      int k = localSelectorThread.getMaximumLoad() - localSelectorThread.getThreadLoad();
      if (k == localSelectorThread.getMaximumLoad())
        return localSelectorThread;
      if ((k <= 0) || (k <= j))
        continue;
      j = k;
      i = m;
    }
    if (i > -1)
      return (SelectorThread)this.D.get(i);
    return A();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.SelectorThreadPool
 * JD-Core Version:    0.6.0
 */