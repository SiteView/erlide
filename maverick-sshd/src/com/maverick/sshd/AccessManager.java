package com.maverick.sshd;

import java.net.SocketAddress;

public abstract interface AccessManager
{
  public abstract boolean canConnect(String paramString);

  public abstract boolean canConnect(SocketAddress paramSocketAddress);

  public abstract boolean canOpenChannel(byte[] paramArrayOfByte, String paramString, Channel paramChannel);

  public abstract boolean canStartShell(byte[] paramArrayOfByte, String paramString);

  public abstract boolean canExecuteCommand(byte[] paramArrayOfByte, String paramString1, String paramString2);

  public abstract boolean canStartSubsystem(byte[] paramArrayOfByte, String paramString1, String paramString2);

  public abstract boolean canForward(byte[] paramArrayOfByte, String paramString, ForwardingChannel paramForwardingChannel, boolean paramBoolean);

  public abstract boolean canListen(byte[] paramArrayOfByte, String paramString1, String paramString2, int paramInt);

  public abstract String[] getRequiredAuthentications(byte[] paramArrayOfByte, String paramString);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.AccessManager
 * JD-Core Version:    0.6.0
 */