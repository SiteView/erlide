package com.maverick.ssh.components;

public class SshKeyPair
{
  SshPrivateKey B;
  SshPublicKey A;

  public SshPrivateKey getPrivateKey()
  {
    return this.B;
  }

  public SshPublicKey getPublicKey()
  {
    return this.A;
  }

  public static SshKeyPair getKeyPair(SshPrivateKey paramSshPrivateKey, SshPublicKey paramSshPublicKey)
  {
    SshKeyPair localSshKeyPair = new SshKeyPair();
    localSshKeyPair.A = paramSshPublicKey;
    localSshKeyPair.B = paramSshPrivateKey;
    return localSshKeyPair;
  }

  public void setPrivateKey(SshPrivateKey paramSshPrivateKey)
  {
    this.B = paramSshPrivateKey;
  }

  public void setPublicKey(SshPublicKey paramSshPublicKey)
  {
    this.A = paramSshPublicKey;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.SshKeyPair
 * JD-Core Version:    0.6.0
 */