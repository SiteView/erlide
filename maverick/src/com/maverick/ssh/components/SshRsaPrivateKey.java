package com.maverick.ssh.components;

import java.io.IOException;
import java.math.BigInteger;

public abstract interface SshRsaPrivateKey extends SshPrivateKey
{
  public abstract BigInteger getModulus();

  public abstract BigInteger getPrivateExponent();

  public abstract byte[] sign(byte[] paramArrayOfByte)
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.SshRsaPrivateKey
 * JD-Core Version:    0.6.0
 */