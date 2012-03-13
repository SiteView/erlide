package com.maverick.ssh.message;

import com.maverick.ssh.SshChannel;
import com.maverick.ssh.SshException;

public abstract class SshAbstractChannel
  implements SshChannel
{
  public static final int CHANNEL_UNINITIALIZED = 1;
  public static final int CHANNEL_OPEN = 2;
  public static final int CHANNEL_CLOSED = 3;
  protected int channelid = -1;
  protected int state = 1;
  protected SshMessageRouter manager;
  protected SshMessageStore ms;

  protected SshMessageStore getMessageStore()
    throws SshException
  {
    if (this.ms == null)
      throw new SshException("Channel is not initialized!", 5);
    return this.ms;
  }

  public int getChannelId()
  {
    return this.channelid;
  }

  public SshMessageRouter getMessageRouter()
  {
    return this.manager;
  }

  protected void init(SshMessageRouter paramSshMessageRouter, int paramInt)
  {
    this.channelid = paramInt;
    this.manager = paramSshMessageRouter;
    this.ms = new SshMessageStore(paramSshMessageRouter, this, getStickyMessageIds());
  }

  protected abstract MessageObserver getStickyMessageIds();

  public boolean isClosed()
  {
    return this.state == 3;
  }

  public void idle()
  {
  }

  protected abstract boolean processChannelMessage(SshChannelMessage paramSshChannelMessage)
    throws SshException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.message.SshAbstractChannel
 * JD-Core Version:    0.6.0
 */