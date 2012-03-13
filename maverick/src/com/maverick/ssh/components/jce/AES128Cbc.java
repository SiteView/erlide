package com.maverick.ssh.components.jce;

import java.io.IOException;

public class AES128Cbc extends AbstractJCECipher
{
  public AES128Cbc()
    throws IOException
  {
    super("AES/CBC/NoPadding", "AES", 16, "aes128-cbc");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AES128Cbc
 * JD-Core Version:    0.6.0
 */