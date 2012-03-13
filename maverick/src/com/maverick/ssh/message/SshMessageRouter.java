package com.maverick.ssh.message;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SshMessageRouter
{
  private SshAbstractChannel[] cb;
  SshMessageReader eb;
  SshMessageStore w;
  ThreadSynchronizer gb;
  private int fb = 0;
  boolean z;
  _b x;
  boolean hb = false;
  Vector<SshAbstractChannel> bb = new Vector();
  Vector<Runnable> y = new Vector();
  static Log ab = LogFactory.getLog(SshMessageRouter.class);
  static boolean db = Boolean.getBoolean("maverick.verbose");

  public SshMessageRouter(SshMessageReader paramSshMessageReader, int paramInt, boolean paramBoolean)
  {
    this.eb = paramSshMessageReader;
    this.z = paramBoolean;
    this.cb = new SshAbstractChannel[paramInt];
    this.w = new SshMessageStore(this, null, new MessageObserver()
    {
      public boolean wantsNotification(Message paramMessage)
      {
        return false;
      }
    });
    this.gb = new ThreadSynchronizer(paramBoolean);
    if (paramBoolean)
    {
      this.x = new _b();
      this.gb.b = this.x;
    }
  }

  public void start()
  {
    if ((db) && (ab.isDebugEnabled()))
      ab.debug("starting message pump");
    if ((this.x != null) && (!this.x.b()))
    {
      String str1 = "";
      String str2 = Thread.currentThread().getName();
      if (str2.indexOf('-') > -1)
        str1 = str2.substring(0, 1 + str2.indexOf('-'));
      this.x.setName(str1 + "MessagePump_" + this.x.getName());
      this.x.start();
      if ((db) && (ab.isDebugEnabled()))
        ab.debug("message pump started thread name:" + this.x.getName());
    }
  }

  public void addShutdownHook(Runnable paramRunnable)
  {
    if (paramRunnable != null)
      this.y.addElement(paramRunnable);
  }

  public boolean isBuffered()
  {
    return this.z;
  }

  public void stop()
  {
    signalClosingState();
    if (this.x != null)
      this.x.c();
    for (int i = 0; i < this.y.size(); i++)
      try
      {
        ((Runnable)this.y.elementAt(i)).run();
      }
      catch (Throwable localThrowable)
      {
      }
  }

  public void signalClosingState()
  {
    if ((this.z) && (this.x != null))
      synchronized (this.x)
      {
        this.hb = true;
      }
  }

  protected SshMessageStore getGlobalMessages()
  {
    return this.w;
  }

  protected int allocateChannel(SshAbstractChannel paramSshAbstractChannel)
  {
    synchronized (this.cb)
    {
      for (int i = 0; i < this.cb.length; i++)
      {
        if (this.cb[i] != null)
          continue;
        this.cb[i] = paramSshAbstractChannel;
        this.bb.addElement(paramSshAbstractChannel);
        this.fb += 1;
        return i;
      }
      return -1;
    }
  }

  protected void freeChannel(SshAbstractChannel paramSshAbstractChannel)
  {
    synchronized (this.cb)
    {
      if ((this.cb[paramSshAbstractChannel.getChannelId()] != null) && (paramSshAbstractChannel.equals(this.cb[paramSshAbstractChannel.getChannelId()])))
      {
        this.cb[paramSshAbstractChannel.getChannelId()] = null;
        this.bb.removeElement(paramSshAbstractChannel);
        this.fb -= 1;
      }
    }
  }

  protected SshAbstractChannel[] getActiveChannels()
  {
    return (SshAbstractChannel[])(SshAbstractChannel[])this.bb.toArray(new SshAbstractChannel[0]);
  }

  protected int maximumChannels()
  {
    return this.cb.length;
  }

  public int getChannelCount()
  {
    return this.fb;
  }

  protected SshMessage nextMessage(SshAbstractChannel paramSshAbstractChannel, MessageObserver paramMessageObserver, long paramLong)
    throws SshException, InterruptedException
  {
    long l = System.currentTimeMillis();
    SshMessageStore localSshMessageStore = paramSshAbstractChannel == null ? this.w : paramSshAbstractChannel.getMessageStore();
    if ((db) && (ab.isDebugEnabled()))
      ab.debug("using " + (paramSshAbstractChannel == null ? "global store" : "channel store"));
    MessageHolder localMessageHolder = new MessageHolder();
    while ((localMessageHolder.msg == null) && ((paramLong == 0L) || (System.currentTimeMillis() - l < paramLong)))
    {
      if ((this.z) && (this.x != null))
      {
        if ((db) && (ab.isDebugEnabled()))
          ab.debug("waiting for messagePump lock");
        synchronized (this.x)
        {
          if ((!this.hb) && (this.x.d != null))
          {
            Throwable localThrowable = this.x.d;
            this.x.d = null;
            if ((localThrowable instanceof SshException))
            {
              if ((db) && (ab.isDebugEnabled()))
                ab.debug("messagePump has SshException this will be caught by customer code");
              throw ((SshException)localThrowable);
            }
            if ((localThrowable instanceof SshIOException))
            {
              if ((db) && (ab.isDebugEnabled()))
                ab.debug("messagePump has SshIOException this will be caught by customer code");
              throw ((SshIOException)localThrowable).getRealException();
            }
            if ((db) && (ab.isDebugEnabled()))
              ab.debug("messagePump has some other exception this will be caught by customer code");
            throw new SshException(localThrowable);
          }
        }
      }
      if (!this.gb.requestBlock(localSshMessageStore, paramMessageObserver, localMessageHolder))
        continue;
      try
      {
        if ((db) && (ab.isDebugEnabled()))
          ab.debug("block for message");
        try
        {
          d();
        }
        catch (SshException localSshException)
        {
          if (localSshException.getReason() == 19)
          {
            if ((db) && (ab.isDebugEnabled()))
              ab.debug("Ignoring socket timeout as message timeout will alert any callers");
          }
          else
            throw localSshException;
        }
      }
      finally
      {
        this.gb.releaseBlock();
      }
    }
    if (localMessageHolder.msg == null)
    {
      if (ab.isDebugEnabled())
        ab.debug("Mesage timeout reached timeout=" + paramLong);
      throw new SshException("The message was not received before the specified timeout period timeout=" + paramLong, 21);
    }
    return (SshMessage)localMessageHolder.msg;
  }

  public boolean isBlockingThread(Thread paramThread)
  {
    return this.gb.isBlockOwner(paramThread);
  }

  private void d()
    throws SshException
  {
    SshMessage localSshMessage = createMessage(this.eb.nextMessage());
    if ((db) && (ab.isDebugEnabled()))
      ab.debug("read next message");
    SshAbstractChannel localSshAbstractChannel = null;
    if ((localSshMessage instanceof SshChannelMessage))
      localSshAbstractChannel = this.cb[((SshChannelMessage)localSshMessage).b()];
    boolean bool = localSshAbstractChannel == null ? processGlobalMessage(localSshMessage) : localSshAbstractChannel.processChannelMessage((SshChannelMessage)localSshMessage);
    if (!bool)
    {
      SshMessageStore localSshMessageStore = localSshAbstractChannel == null ? this.w : localSshAbstractChannel.getMessageStore();
      localSshMessageStore.c(localSshMessage);
    }
  }

  protected abstract void onThreadExit();

  protected abstract SshMessage createMessage(byte[] paramArrayOfByte)
    throws SshException;

  protected abstract boolean processGlobalMessage(SshMessage paramSshMessage)
    throws SshException;

  class _b extends Thread
  {
    Throwable d;
    boolean c = false;

    _b()
    {
    }

    public void run()
    {
      try
      {
        this.c = true;
        while (this.c)
          try
          {
            SshMessageRouter.b(SshMessageRouter.this);
            SshMessageRouter.this.gb.releaseWaiting();
          }
          catch (Throwable localThrowable)
          {
            synchronized (this)
            {
              if (!SshMessageRouter.this.hb)
              {
                SshMessageRouter.ab.error("Message pump caught exception: " + localThrowable.getMessage());
                this.d = localThrowable;
              }
              c();
            }
          }
        SshMessageRouter.this.gb.releaseBlock();
      }
      finally
      {
        SshMessageRouter.this.onThreadExit();
      }
    }

    public void c()
    {
      this.c = false;
      if (!Thread.currentThread().equals(this))
        interrupt();
    }

    public boolean b()
    {
      return this.c;
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.message.SshMessageRouter
 * JD-Core Version:    0.6.0
 */