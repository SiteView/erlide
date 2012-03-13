package com.maverick.ssh.components;

import java.io.IOException;

public abstract class SshCipher
{
  String A;
  public static final int ENCRYPT_MODE = 0;
  public static final int DECRYPT_MODE = 1;

  public SshCipher(String paramString)
  {
    this.A = paramString;
  }

  public String getAlgorithm()
  {
    return this.A;
  }

  public abstract int getBlockSize();

  public abstract void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IOException;

  public void transform(byte[] paramArrayOfByte)
    throws IOException
  {
    transform(paramArrayOfByte, 0, paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public abstract void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.SshCipher
 * JD-Core Version:    0.6.0
 */