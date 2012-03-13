package com.maverick.ssh.components.standalone;

import com.maverick.crypto.engines.CipherEngine;
import com.maverick.ssh.components.SshCipher;
import java.io.IOException;

class D extends SshCipher
{
  private CipherEngine R;
  private byte[] P = null;
  private byte[] Q = null;
  private byte[] N = null;
  private byte[] O = null;

  public D(int paramInt, CipherEngine paramCipherEngine, String paramString)
  {
    super(paramString);
    this.Q = new byte[paramInt / 8];
    this.R = paramCipherEngine;
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    if (paramInt3 % getBlockSize() != 0)
      throw new IOException("Input data length MUST be a multiple of the cipher block size!");
    int i = 0;
    while (i < paramInt3)
    {
      A(paramArrayOfByte1, paramInt1 + i, paramArrayOfByte2, i + paramInt2);
      i += getBlockSize();
    }
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IOException
  {
    System.arraycopy(paramArrayOfByte2, 0, this.Q, 0, this.Q.length);
    this.R.init(true, this.Q);
    this.P = new byte[getBlockSize()];
    System.arraycopy(paramArrayOfByte1, 0, this.P, 0, this.P.length);
    this.N = new byte[getBlockSize()];
    this.O = new byte[getBlockSize()];
    System.arraycopy(paramArrayOfByte1, 0, this.N, 0, this.N.length);
  }

  public int getBlockSize()
  {
    return this.R.getBlockSize();
  }

  public int A(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws IOException
  {
    this.R.processBlock(this.N, 0, this.O, 0);
    for (int i = 0; i < this.O.length; i++)
      paramArrayOfByte2[(paramInt2 + i)] = (byte)(this.O[i] ^ paramArrayOfByte1[(paramInt1 + i)]);
    i = 1;
    for (int j = this.N.length - 1; j >= 0; j--)
    {
      int k = (this.N[j] & 0xFF) + i;
      if (k > 255)
        i = 1;
      else
        i = 0;
      this.N[j] = (byte)k;
    }
    return this.N.length;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.D
 * JD-Core Version:    0.6.0
 */