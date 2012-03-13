package com.sshtools.publickey;

import com.maverick.ssh.components.SshKeyPair;
import java.io.IOException;

public abstract interface SshPrivateKeyFile
{
  public abstract boolean isPassphraseProtected();

  public abstract SshKeyPair toKeyPair(String paramString)
    throws IOException, InvalidPassphraseException;

  public abstract boolean supportsPassphraseChange();

  public abstract String getType();

  public abstract void changePassphrase(String paramString1, String paramString2)
    throws IOException, InvalidPassphraseException;

  public abstract byte[] getFormattedKey()
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.SshPrivateKeyFile
 * JD-Core Version:    0.6.0
 */