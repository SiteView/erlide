package com.maverick.ssh.components.jce;

import java.security.NoSuchAlgorithmException;

public class SHA256Digest extends AbstractDigest
{
  public SHA256Digest()
    throws NoSuchAlgorithmException
  {
    super("SHA-256");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.SHA256Digest
 * JD-Core Version:    0.6.0
 */