package com.maverick.ssh.components;

import java.io.IOException;
import java.math.BigInteger;

public abstract interface SshDsaPrivateKey extends SshPrivateKey
{
  public abstract BigInteger getX();

  public abstract byte[] sign(byte[] paramArrayOfByte)
    throws IOException;

  public abstract SshDsaPublicKey getPublicKey();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.SshDsaPrivateKey
 * JD-Core Version:    0.6.0
 */