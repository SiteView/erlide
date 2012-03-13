package com.maverick.ssh.components.standalone;

import com.maverick.crypto.engines.DESedeEngine;

public class TripleDesCbc extends A
{
  public static final String TRIPLEDES_CBC = "3des-cbc";

  public TripleDesCbc()
  {
    super(192, new DESedeEngine(), "3des-cbc");
  }

  public String getAlgorithm()
  {
    return "3des-cbc";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.TripleDesCbc
 * JD-Core Version:    0.6.0
 */