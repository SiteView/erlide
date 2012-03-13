package com.maverick.ssh.components;

import com.maverick.ssh.SshException;
import java.math.BigInteger;

public abstract interface SshRsaPublicKey extends SshPublicKey
{
  public abstract BigInteger getModulus();

  public abstract BigInteger getPublicExponent();

  public abstract int getVersion();

  public abstract BigInteger doPublic(BigInteger paramBigInteger)
    throws SshException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.SshRsaPublicKey
 * JD-Core Version:    0.6.0
 */