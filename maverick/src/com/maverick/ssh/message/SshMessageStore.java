package com.maverick.ssh.message;

import com.maverick.ssh.SshException;
import java.io.EOFException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SshMessageStore
  implements MessageStore
{
  public static final int NO_MESSAGES = -1;
  SshAbstractChannel g;
  SshMessageRouter b;
  boolean f = false;
  SshMessage d = new SshMessage();
  int i = 0;
  MessageObserver h;
  static Log c = LogFactory.getLog(SshMessageStore.class);
  static boolean e = Boolean.getBoolean("maverick.verbose");

  public SshMessageStore(SshMessageRouter paramSshMessageRouter, SshAbstractChannel paramSshAbstractChannel, MessageObserver paramMessageObserver)
  {
    this.b = paramSshMessageRouter;
    this.g = paramSshAbstractChannel;
    this.h = paramMessageObserver;
    this.d.d = (this.d.e = this.d);
  }

  public SshMessage nextMessage(MessageObserver paramMessageObserver, long paramLong)
    throws SshException, EOFException
  {
    try
    {
      SshMessage localSshMessage = this.b.nextMessage(this.g, paramMessageObserver, paramLong);
      if ((e) && (c.isDebugEnabled()))
        c.debug("got managers next message");
      if (localSshMessage != null)
        synchronized (this.d)
        {
          if (this.h.wantsNotification(localSshMessage))
            return localSshMessage;
          b(localSshMessage);
          return localSshMessage;
        }
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new SshException("The thread was interrupted", 5);
    }
    throw new EOFException("The required message could not be found in the message store");
  }

  public boolean isClosed()
  {
    synchronized (this.d)
    {
      return this.f;
    }
  }

  private void b(SshMessage paramSshMessage)
  {
    if (paramSshMessage == this.d)
      throw new IndexOutOfBoundsException();
    paramSshMessage.e.d = paramSshMessage.d;
    paramSshMessage.d.e = paramSshMessage.e;
    this.i -= 1;
  }

  public Message hasMessage(MessageObserver paramMessageObserver)
  {
    if ((e) && (c.isDebugEnabled()))
      c.debug("waiting for header lock");
    synchronized (this.d)
    {
      SshMessage localSshMessage = this.d.d;
      if (localSshMessage == null)
      {
        if ((e) && (c.isDebugEnabled()))
          c.debug("header.next is null");
        return null;
      }
      while (localSshMessage != this.d)
      {
        if (paramMessageObserver.wantsNotification(localSshMessage))
        {
          if ((e) && (c.isDebugEnabled()))
            c.debug("found message");
          return localSshMessage;
        }
        localSshMessage = localSshMessage.d;
      }
      if ((e) && (c.isDebugEnabled()))
        c.debug("no messages");
      return null;
    }
  }

  public void close()
  {
    synchronized (this.d)
    {
      this.f = true;
    }
  }

  void c(SshMessage paramSshMessage)
  {
    synchronized (this.d)
    {
      paramSshMessage.d = this.d;
      paramSshMessage.e = this.d.e;
      paramSshMessage.e.d = paramSshMessage;
      paramSshMessage.d.e = paramSshMessage;
      this.i += 1;
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.message.SshMessageStore
 * JD-Core Version:    0.6.0
 */