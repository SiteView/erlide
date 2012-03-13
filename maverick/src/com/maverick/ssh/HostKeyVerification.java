package com.maverick.ssh;

import com.maverick.ssh.components.SshPublicKey;

public abstract interface HostKeyVerification
{
  public abstract boolean verifyHost(String paramString, SshPublicKey paramSshPublicKey)
    throws SshException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.HostKeyVerification
 * JD-Core Version:    0.6.0
 */