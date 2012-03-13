package com.maverick.ssh2;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshPublicKey;

public abstract interface SignatureGenerator
{
  public abstract byte[] sign(SshPublicKey paramSshPublicKey, byte[] paramArrayOfByte)
    throws SshException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.SignatureGenerator
 * JD-Core Version:    0.6.0
 */