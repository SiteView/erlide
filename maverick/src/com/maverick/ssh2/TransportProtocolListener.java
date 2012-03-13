package com.maverick.ssh2;

public abstract interface TransportProtocolListener
{
  public abstract void onDisconnect(String paramString, int paramInt);

  public abstract void onIdle(long paramLong);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.TransportProtocolListener
 * JD-Core Version:    0.6.0
 */