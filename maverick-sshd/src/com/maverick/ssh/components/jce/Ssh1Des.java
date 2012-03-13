package com.maverick.ssh.components.jce;

import java.io.IOException;

public class Ssh1Des extends AbstractJCECipher
{
  public Ssh1Des()
    throws IOException
  {
    super("DES/CBC/NoPadding", "DES", 8, "ssh1DES");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh1Des
 * JD-Core Version:    0.6.0
 */