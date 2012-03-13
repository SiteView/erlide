package com.maverick.ssh.components.jce;

import java.io.IOException;

public class TripleDesCbc extends AbstractJCECipher
{
  public TripleDesCbc()
    throws IOException
  {
    super("DESede/CBC/NoPadding", "DESede", 24, "3des-cbc");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.TripleDesCbc
 * JD-Core Version:    0.6.0
 */