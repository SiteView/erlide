package com.sshtools.publickey;

import com.maverick.ssh.components.SshPublicKey;
import java.io.IOException;

public abstract interface SshPublicKeyFile
{
  public abstract SshPublicKey toPublicKey()
    throws IOException;

  public abstract String getComment();

  public abstract byte[] getFormattedKey()
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.SshPublicKeyFile
 * JD-Core Version:    0.6.0
 */