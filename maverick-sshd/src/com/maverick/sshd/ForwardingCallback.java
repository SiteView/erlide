package com.maverick.sshd;

public abstract interface ForwardingCallback
{
  public abstract void localForwardingStarted(String paramString, int paramInt, byte[] paramArrayOfByte);

  public abstract void localForwardingCancelled(String paramString, int paramInt, byte[] paramArrayOfByte);

  public abstract void remoteForwardingStarted(String paramString, int paramInt, byte[] paramArrayOfByte);

  public abstract void remoteForwardingCancelled(String paramString, int paramInt, byte[] paramArrayOfByte);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.ForwardingCallback
 * JD-Core Version:    0.6.0
 */