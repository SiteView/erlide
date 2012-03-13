package com.maverick.ssh1;

import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.components.SshPrivateKey;
import com.maverick.ssh.components.SshPublicKey;

public class Ssh1RhostsRsaAuthentication
  implements SshAuthentication
{
  String g;
  String f;
  SshPrivateKey d;
  SshPublicKey e;

  public void setUsername(String paramString)
  {
    this.g = paramString;
  }

  public String getUsername()
  {
    return this.g;
  }

  public String getMethod()
  {
    return "rhosts";
  }

  public void setPublicKey(SshPublicKey paramSshPublicKey)
  {
    this.e = paramSshPublicKey;
  }

  public void setPrivateKey(SshPrivateKey paramSshPrivateKey)
  {
    this.d = paramSshPrivateKey;
  }

  public void setClientUsername(String paramString)
  {
    this.f = paramString;
  }

  public String getClientUsername()
  {
    return this.f == null ? this.g : this.f;
  }

  public SshPrivateKey getPrivateKey()
  {
    return this.d;
  }

  public SshPublicKey getPublicKey()
  {
    return this.e;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh1.Ssh1RhostsRsaAuthentication
 * JD-Core Version:    0.6.0
 */