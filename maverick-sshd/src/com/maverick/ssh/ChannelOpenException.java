package com.maverick.ssh;

public class ChannelOpenException extends Exception
{
  public static final int ADMINISTRATIVIVELY_PROHIBITED = 1;
  public static final int CONNECT_FAILED = 2;
  public static final int UNKNOWN_CHANNEL_TYPE = 3;
  public static final int RESOURCE_SHORTAGE = 4;
  int A;

  public ChannelOpenException(String paramString, int paramInt)
  {
    super(paramString);
    this.A = paramInt;
  }

  public int getReason()
  {
    return this.A;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.ChannelOpenException
 * JD-Core Version:    0.6.0
 */