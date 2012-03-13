package com.maverick.ssh.components;

import com.maverick.ssh.SshException;

public abstract interface SshSecureRandomGenerator
{
  public abstract void nextBytes(byte[] paramArrayOfByte);

  public abstract void nextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException;

  public abstract int nextInt();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.SshSecureRandomGenerator
 * JD-Core Version:    0.6.0
 */