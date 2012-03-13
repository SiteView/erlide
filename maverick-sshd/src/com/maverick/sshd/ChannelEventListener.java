package com.maverick.sshd;

public abstract interface ChannelEventListener
{
  public abstract void onChannelOpen(Channel paramChannel);

  public abstract void onChannelClose(Channel paramChannel);

  public abstract void onChannelEOF(Channel paramChannel);

  public abstract void onChannelClosing(Channel paramChannel);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.ChannelEventListener
 * JD-Core Version:    0.6.0
 */