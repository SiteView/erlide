package com.maverick.ssh2;

import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.SshException;

public abstract interface ChannelFactory
{
  public abstract String[] supportedChannelTypes();

  public abstract Ssh2Channel createChannel(String paramString, byte[] paramArrayOfByte)
    throws SshException, ChannelOpenException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.ChannelFactory
 * JD-Core Version:    0.6.0
 */