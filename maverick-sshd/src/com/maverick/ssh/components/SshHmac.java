package com.maverick.ssh.components;

import com.maverick.ssh.SshException;

public abstract interface SshHmac
{
  public abstract int getMacLength();

  public abstract void generate(long paramLong, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3);

  public abstract void init(byte[] paramArrayOfByte)
    throws SshException;

  public abstract boolean verify(long paramLong, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3);

  public abstract String getAlgorithm();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.SshHmac
 * JD-Core Version:    0.6.0
 */