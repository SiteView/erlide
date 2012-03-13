package com.maverick.ssh;

import com.maverick.ssh.components.SshPrivateKey;
import com.maverick.ssh.components.SshPublicKey;

public class PublicKeyAuthentication
  implements SshAuthentication
{
  String k;
  SshPrivateKey i;
  SshPublicKey h;
  boolean j = true;

  public void setUsername(String paramString)
  {
    this.k = paramString;
  }

  public String getUsername()
  {
    return this.k;
  }

  public void setPrivateKey(SshPrivateKey paramSshPrivateKey)
  {
    this.i = paramSshPrivateKey;
  }

  public String getMethod()
  {
    return "publickey";
  }

  public SshPrivateKey getPrivateKey()
  {
    return this.i;
  }

  public void setPublicKey(SshPublicKey paramSshPublicKey)
  {
    this.h = paramSshPublicKey;
  }

  public SshPublicKey getPublicKey()
  {
    return this.h;
  }

  public void setAuthenticating(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }

  public boolean isAuthenticating()
  {
    return this.j;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.PublicKeyAuthentication
 * JD-Core Version:    0.6.0
 */