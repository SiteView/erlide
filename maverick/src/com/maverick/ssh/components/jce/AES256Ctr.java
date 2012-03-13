package com.maverick.ssh.components.jce;

import java.io.IOException;

public class AES256Ctr extends AbstractJCECipher
{
  public AES256Ctr()
    throws IOException
  {
    super("AES/CTR/NoPadding", "AES", 32, "aes256-ctr");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AES256Ctr
 * JD-Core Version:    0.6.0
 */