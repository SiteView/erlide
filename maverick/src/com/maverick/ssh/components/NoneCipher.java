package com.maverick.ssh.components;

import java.io.IOException;

public class NoneCipher extends SshCipher
{
  public NoneCipher()
  {
    super("none");
  }

  public int getBlockSize()
  {
    return 8;
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IOException
  {
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.NoneCipher
 * JD-Core Version:    0.6.0
 */