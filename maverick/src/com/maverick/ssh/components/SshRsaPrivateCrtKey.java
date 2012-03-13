package com.maverick.ssh.components;

import com.maverick.ssh.SshException;
import java.math.BigInteger;

public abstract interface SshRsaPrivateCrtKey extends SshRsaPrivateKey
{
  public abstract BigInteger getPublicExponent();

  public abstract BigInteger getPrimeP();

  public abstract BigInteger getPrimeQ();

  public abstract BigInteger getPrimeExponentP();

  public abstract BigInteger getPrimeExponentQ();

  public abstract BigInteger getCrtCoefficient();

  public abstract BigInteger doPrivate(BigInteger paramBigInteger)
    throws SshException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.SshRsaPrivateCrtKey
 * JD-Core Version:    0.6.0
 */