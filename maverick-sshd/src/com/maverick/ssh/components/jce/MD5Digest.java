package com.maverick.ssh.components.jce;

import java.security.NoSuchAlgorithmException;

public class MD5Digest extends AbstractDigest
{
  public MD5Digest()
    throws NoSuchAlgorithmException
  {
    super("MD5");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.MD5Digest
 * JD-Core Version:    0.6.0
 */