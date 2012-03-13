package com.maverick.ssh.components;

import com.maverick.ssh.SshException;

public abstract interface SshPublicKey
{
  public abstract void init(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException;

  public abstract String getAlgorithm();

  public abstract int getBitLength();

  public abstract byte[] getEncoded()
    throws SshException;

  public abstract String getFingerprint()
    throws SshException;

  public abstract boolean verifySignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SshException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.SshPublicKey
 * JD-Core Version:    0.6.0
 */