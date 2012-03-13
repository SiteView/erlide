package com.maverick.ssh.components;

import java.io.IOException;

public abstract interface SshPrivateKey
{
  public abstract byte[] sign(byte[] paramArrayOfByte)
    throws IOException;

  public abstract String getAlgorithm();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.SshPrivateKey
 * JD-Core Version:    0.6.0
 */