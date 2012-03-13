package com.maverick.sshd;

import java.io.IOException;

public abstract interface RemoteForwardingFactory
{
  public abstract void bindInterface(String paramString, int paramInt, ConnectionProtocol paramConnectionProtocol)
    throws IOException;

  public abstract boolean belongsTo(ConnectionProtocol paramConnectionProtocol);

  public abstract void stopListening(boolean paramBoolean);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.RemoteForwardingFactory
 * JD-Core Version:    0.6.0
 */