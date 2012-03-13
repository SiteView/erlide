package com.maverick.sshd;

import java.net.SocketAddress;

public class AccessManagerAdapter
  implements AccessManager
{
  public boolean canConnect(SocketAddress paramSocketAddress)
  {
    return true;
  }

  public boolean canConnect(String paramString)
  {
    return true;
  }

  public boolean canExecuteCommand(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    return true;
  }

  public boolean canForward(byte[] paramArrayOfByte, String paramString, ForwardingChannel paramForwardingChannel, boolean paramBoolean)
  {
    return true;
  }

  public boolean canListen(byte[] paramArrayOfByte, String paramString1, String paramString2, int paramInt)
  {
    return true;
  }

  public boolean canOpenChannel(byte[] paramArrayOfByte, String paramString, Channel paramChannel)
  {
    return true;
  }

  public boolean canStartShell(byte[] paramArrayOfByte, String paramString)
  {
    return true;
  }

  public boolean canStartSubsystem(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    return true;
  }

  public String[] getRequiredAuthentications(byte[] paramArrayOfByte, String paramString)
  {
    return null;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.AccessManagerAdapter
 * JD-Core Version:    0.6.0
 */