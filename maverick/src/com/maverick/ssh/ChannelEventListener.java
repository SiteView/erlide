package com.maverick.ssh;

public abstract interface ChannelEventListener
{
  public abstract void channelOpened(SshChannel paramSshChannel);

  public abstract void channelClosing(SshChannel paramSshChannel);

  public abstract void channelClosed(SshChannel paramSshChannel);

  public abstract void channelEOF(SshChannel paramSshChannel);

  public abstract void dataReceived(SshChannel paramSshChannel, byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  public abstract void dataSent(SshChannel paramSshChannel, byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  public abstract void extendedDataReceived(SshChannel paramSshChannel, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ChannelEventListener
 * JD-Core Version:    0.6.0
 */