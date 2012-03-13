package com.maverick.ssh;

public abstract class ChannelAdapter
  implements ChannelEventListener
{
  public void channelOpened(SshChannel paramSshChannel)
  {
  }

  public void channelClosing(SshChannel paramSshChannel)
  {
  }

  public void channelClosed(SshChannel paramSshChannel)
  {
  }

  public void channelEOF(SshChannel paramSshChannel)
  {
  }

  public void dataReceived(SshChannel paramSshChannel, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
  }

  public void dataSent(SshChannel paramSshChannel, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
  }

  public void extendedDataReceived(SshChannel paramSshChannel, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ChannelAdapter
 * JD-Core Version:    0.6.0
 */