package com.maverick.ssh;

import com.maverick.ssh.message.SshMessageRouter;

public abstract interface SshChannel extends SshIO
{
  public abstract int getChannelId();

  public abstract boolean isClosed();

  public abstract void addChannelEventListener(ChannelEventListener paramChannelEventListener);

  public abstract void setAutoConsumeInput(boolean paramBoolean);

  public abstract SshMessageRouter getMessageRouter();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshChannel
 * JD-Core Version:    0.6.0
 */