package com.maverick.ssh.components;

public class SshKeyPair
{
  SshPrivateKey c;
  SshPublicKey b;

  public SshPrivateKey getPrivateKey()
  {
    return this.c;
  }

  public SshPublicKey getPublicKey()
  {
    return this.b;
  }

  public static SshKeyPair getKeyPair(SshPrivateKey paramSshPrivateKey, SshPublicKey paramSshPublicKey)
  {
    SshKeyPair localSshKeyPair = new SshKeyPair();
    localSshKeyPair.b = paramSshPublicKey;
    localSshKeyPair.c = paramSshPrivateKey;
    return localSshKeyPair;
  }

  public void setPrivateKey(SshPrivateKey paramSshPrivateKey)
  {
    this.c = paramSshPrivateKey;
  }

  public void setPublicKey(SshPublicKey paramSshPublicKey)
  {
    this.b = paramSshPublicKey;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.SshKeyPair
 * JD-Core Version:    0.6.0
 */