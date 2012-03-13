package com.maverick.ssh.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadSynchronizer
{
  boolean d;
  Thread b = null;
  static Log c = LogFactory.getLog(ThreadSynchronizer.class);
  static boolean e = Boolean.getBoolean("maverick.verbose");

  public ThreadSynchronizer(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }

  public synchronized boolean requestBlock(MessageStore paramMessageStore, MessageObserver paramMessageObserver, MessageHolder paramMessageHolder)
    throws InterruptedException
  {
    if ((e) && (c.isDebugEnabled()))
      c.debug("requesting block");
    int i = (!this.d) || (isBlockOwner(Thread.currentThread())) ? 1 : 0;
    paramMessageHolder.msg = paramMessageStore.hasMessage(paramMessageObserver);
    if (paramMessageHolder.msg != null)
      return false;
    if (i != 0)
    {
      this.d = true;
      this.b = Thread.currentThread();
    }
    else
    {
      if ((e) && (c.isDebugEnabled()))
        c.debug("can't block so wait isBlocking:" + this.d + " blockowner name:id{" + this.b.getName() + "} currentthread name:id{" + Thread.currentThread().getName() + "}");
      wait(1000L);
    }
    return i;
  }

  public synchronized boolean isBlockOwner(Thread paramThread)
  {
    return (this.b != null) && (this.b.equals(paramThread));
  }

  public synchronized void releaseWaiting()
  {
    notifyAll();
  }

  public synchronized void releaseBlock()
  {
    this.d = false;
    this.b = null;
    notifyAll();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.message.ThreadSynchronizer
 * JD-Core Version:    0.6.0
 */