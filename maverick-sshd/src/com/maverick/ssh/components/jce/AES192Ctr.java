package com.maverick.ssh.components.jce;

import java.io.IOException;

public class AES192Ctr extends AbstractJCECipher
{
  public AES192Ctr()
    throws IOException
  {
    super("AES/CTR/NoPadding", "AES", 24, "aes192-ctr");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AES192Ctr
 * JD-Core Version:    0.6.0
 */