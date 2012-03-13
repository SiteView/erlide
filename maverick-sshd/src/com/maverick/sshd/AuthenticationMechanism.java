package com.maverick.sshd;

import java.io.IOException;

public abstract interface AuthenticationMechanism
{
  public abstract void init(TransportProtocol paramTransportProtocol, AuthenticationProtocol paramAuthenticationProtocol, byte[] paramArrayOfByte)
    throws IOException;

  public abstract boolean startRequest(String paramString, byte[] paramArrayOfByte)
    throws IOException;

  public abstract boolean processMessage(byte[] paramArrayOfByte)
    throws IOException;

  public abstract String getMethod();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.AuthenticationMechanism
 * JD-Core Version:    0.6.0
 */