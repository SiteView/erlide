package com.maverick.sshd;

import java.io.IOException;

abstract interface A
{
  public abstract void init(TransportProtocol paramTransportProtocol)
    throws IOException;

  public abstract boolean processMessage(byte[] paramArrayOfByte)
    throws IOException;

  public abstract void start();

  public abstract void stop();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.A
 * JD-Core Version:    0.6.0
 */