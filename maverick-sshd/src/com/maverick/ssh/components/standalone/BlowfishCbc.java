package com.maverick.ssh.components.standalone;

import com.maverick.crypto.engines.BlowfishEngine;

public class BlowfishCbc extends A
{
  public BlowfishCbc()
  {
    super(128, new BlowfishEngine(), "blowfish-cbc");
  }

  public String getAlgorithm()
  {
    return "blowfish-cbc";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.BlowfishCbc
 * JD-Core Version:    0.6.0
 */