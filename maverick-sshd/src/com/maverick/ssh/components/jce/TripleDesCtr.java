package com.maverick.ssh.components.jce;

import java.io.IOException;

public class TripleDesCtr extends AbstractJCECipher
{
  public TripleDesCtr()
    throws IOException
  {
    super("DESede/CTR/NoPadding", "DESede", 24, "3des-ctr");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.TripleDesCtr
 * JD-Core Version:    0.6.0
 */