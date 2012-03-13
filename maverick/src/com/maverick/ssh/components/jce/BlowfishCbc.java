package com.maverick.ssh.components.jce;

import java.io.IOException;

public class BlowfishCbc extends AbstractJCECipher
{
  public BlowfishCbc()
    throws IOException
  {
    super("Blowfish/CBC/NoPadding", "Blowfish", 16, "blowfish-cbc");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.BlowfishCbc
 * JD-Core Version:    0.6.0
 */