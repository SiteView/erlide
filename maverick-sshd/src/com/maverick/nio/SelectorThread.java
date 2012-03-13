package com.maverick.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class SelectorThread extends Thread
{
  Selector B;
  boolean C;
  LinkedList G;
  LinkedList D;
  int J;
  SelectorThreadImpl F;
  SelectorThreadPool E;
  boolean I;
  int A;
  IdleStateManager H;

  public SelectorThread(SelectorThreadPool paramSelectorThreadPool, SelectorThreadImpl paramSelectorThreadImpl, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4, SelectorProvider paramSelectorProvider)
    throws IOException
  {
    this.E = paramSelectorThreadPool;
    this.F = paramSelectorThreadImpl;
    this.I = paramBoolean;
    this.A = paramInt2;
    this.J = paramInt1;
    this.H = new IdleStateManager(paramInt3, paramInt4);
    this.G = new LinkedList();
    this.D = new LinkedList();
    this.B = paramSelectorProvider.openSelector();
    setName(paramSelectorThreadImpl.getName() + "-" + paramInt2);
  }

  IdleStateManager C()
  {
    return this.H;
  }

  public synchronized boolean register(SelectableChannel paramSelectableChannel, int paramInt, Object paramObject, boolean paramBoolean)
    throws ClosedChannelException
  {
    if (getThreadLoad() >= this.J)
      return false;
    synchronized (this.G)
    {
      this.G.addLast(new _A(paramSelectableChannel, paramInt, paramObject));
    }
    if (paramBoolean)
      this.B.wakeup();
    return true;
  }

  private boolean A()
  {
    int i = !this.G.isEmpty() ? 1 : 0;
    synchronized (this.G)
    {
      while (!this.G.isEmpty())
        try
        {
          _A local_A = (_A)this.G.removeFirst();
          if (local_A.C().isOpen())
          {
            SelectionKey localSelectionKey = local_A.C().register(this.B, local_A.A(), local_A.B());
            if ((local_A.B() instanceof SelectorRegistrationListener))
              ((SocketHandler)local_A.B()).registrationCompleted(local_A.C(), localSelectionKey, this);
          }
        }
        catch (ClosedChannelException localClosedChannelException)
        {
        }
    }
    return i;
  }

  private void B()
  {
    Iterator localIterator = this.B.keys().iterator();
    while (localIterator.hasNext())
    {
      SelectionKey localSelectionKey = (SelectionKey)localIterator.next();
      Object localObject1 = localSelectionKey.attachment();
      if ((localObject1 instanceof SocketConnection))
      {
        try
        {
          ((SocketConnection)localObject1).socketChannel.close();
        }
        catch (IOException localIOException1)
        {
        }
        ((SocketConnection)localObject1).protocolEngine.onSocketClose();
      }
      SelectableChannel localSelectableChannel = localSelectionKey.channel();
      try
      {
        synchronized (localSelectableChannel)
        {
          if (localSelectableChannel.isOpen())
            localSelectableChannel.close();
        }
      }
      catch (IOException localIOException2)
      {
      }
    }
  }

  public void addSelectorOperation(Runnable paramRunnable)
  {
    synchronized (this.D)
    {
      this.D.addLast(paramRunnable);
      wakeup();
    }
  }

  private boolean D()
  {
    int i = !this.D.isEmpty() ? 1 : 0;
    synchronized (this.D)
    {
      while (!this.D.isEmpty())
      {
        Runnable localRunnable = (Runnable)this.D.removeFirst();
        localRunnable.run();
      }
    }
    return i;
  }

  public void wakeup()
  {
    this.B.wakeup();
  }

  public synchronized int getThreadLoad()
  {
    return this.B.keys().size() + this.G.size();
  }

  public boolean isPermanent()
  {
    return this.I;
  }

  public void shutdown()
  {
    this.C = false;
    if (!Thread.currentThread().equals(this))
      this.B.wakeup();
  }

  public int getSelectorId()
  {
    return this.A;
  }

  public int getMaximumLoad()
  {
    return this.J;
  }

  public void run()
  {
    this.C = true;
    int i = 0;
    while (this.C)
      try
      {
        D();
        try
        {
          i = this.B.select(1000L);
        }
        catch (Exception localException)
        {
        }
        if (this.B.isOpen())
          continue;
        break;
        synchronized (this.H)
        {
          if (this.H.isReady())
            this.H.service();
        }
        D();
        A();
        if (i == 0)
        {
          if ((this.B.keys().size() == 0) && (this.G.size() == 0) && (!this.I))
            shutdown();
          continue;
        }
        ??? = this.B.selectedKeys().iterator();
        while (((Iterator)???).hasNext())
        {
          SelectionKey localSelectionKey = (SelectionKey)((Iterator)???).next();
          if (!localSelectionKey.isValid())
          {
            ((Iterator)???).remove();
            continue;
          }
          ((Iterator)???).remove();
          if (this.F.processSelectionKey(localSelectionKey))
            localSelectionKey.cancel();
        }
      }
      catch (Throwable localThrowable)
      {
        EventLog.LogEvent(this, "Selector thread encountered an error", localThrowable);
      }
    this.E.A(this);
    B();
  }

  class _A
  {
    SelectableChannel A;
    int B;
    Object C;

    _A(SelectableChannel paramInt, int paramObject, Object arg4)
    {
      this.A = paramInt;
      this.B = paramObject;
      Object localObject;
      this.C = localObject;
    }

    public SelectableChannel C()
    {
      return this.A;
    }

    public int A()
    {
      return this.B;
    }

    public Object B()
    {
      return this.C;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.SelectorThread
 * JD-Core Version:    0.6.0
 */