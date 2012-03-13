package com.maverick.ssh.components;

import java.math.BigInteger;

public abstract interface SshDsaPublicKey extends SshPublicKey
{
  public abstract BigInteger getP();

  public abstract BigInteger getQ();

  public abstract BigInteger getG();

  public abstract BigInteger getY();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.SshDsaPublicKey
 * JD-Core Version:    0.6.0
 */