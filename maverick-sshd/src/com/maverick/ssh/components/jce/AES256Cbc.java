package com.maverick.ssh.components.jce;

import java.io.IOException;

public class AES256Cbc extends AbstractJCECipher
{
  public AES256Cbc()
    throws IOException
  {
    super("AES/CBC/NoPadding", "AES", 32, "aes256-cbc");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AES256Cbc
 * JD-Core Version:    0.6.0
 */