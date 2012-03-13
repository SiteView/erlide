package com.maverick.ssh.components.jce;

import java.security.NoSuchAlgorithmException;

public class SHA1Digest extends AbstractDigest
{
  public SHA1Digest()
    throws NoSuchAlgorithmException
  {
    super("SHA-1");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.SHA1Digest
 * JD-Core Version:    0.6.0
 */