package com.maverick.ssh.components.jce;

import java.io.IOException;

public class AES128Ctr extends AbstractJCECipher
{
  public AES128Ctr()
    throws IOException
  {
    super("AES/CTR/NoPadding", "AES", 16, "aes128-ctr");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AES128Ctr
 * JD-Core Version:    0.6.0
 */