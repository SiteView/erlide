package com.maverick.ssh.components.standalone;

import com.maverick.crypto.engines.CipherEngine;
import com.maverick.ssh.components.SshCipher;
import java.io.IOException;

class A extends SshCipher
{
  private CipherEngine D;
  private int H;
  private byte[] B = null;
  private byte[] G = null;
  private byte[] E = null;
  private byte[] C = null;
  private int F;

  public A(int paramInt, CipherEngine paramCipherEngine, String paramString)
  {
    super(paramString);
    this.C = new byte[paramInt / 8];
    this.D = paramCipherEngine;
    this.F = paramCipherEngine.getBlockSize();
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.H = paramInt;
    System.arraycopy(paramArrayOfByte2, 0, this.C, 0, this.C.length);
    this.D.init(paramInt == 0, this.C);
    this.B = new byte[this.F];
    System.arraycopy(paramArrayOfByte1, 0, this.B, 0, this.B.length);
    this.G = new byte[this.F];
    System.arraycopy(this.B, 0, this.G, 0, this.G.length);
    this.E = new byte[this.F];
  }

  public int getBlockSize()
  {
    return this.F;
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    if (this.G == null)
      throw new IOException("Cipher not initialized!");
    if (paramInt3 % this.F != 0)
      throw new IOException("Input data length MUST be a multiple of the cipher block size!");
    int i = 0;
    while (i < paramInt3)
    {
      switch (this.H)
      {
      case 0:
        for (int j = 0; j < this.F; j++)
          this.E[j] = (byte)(paramArrayOfByte1[(paramInt1 + i + j)] ^ this.G[j]);
        this.D.processBlock(this.E, 0, this.G, 0);
        System.arraycopy(this.G, 0, paramArrayOfByte2, paramInt2 + i, this.F);
        break;
      case 1:
        byte[] arrayOfByte = new byte[this.F];
        System.arraycopy(paramArrayOfByte1, paramInt1 + i, arrayOfByte, 0, this.F);
        this.D.processBlock(paramArrayOfByte1, paramInt2 + i, this.E, 0);
        for (int k = 0; k < this.F; k++)
          paramArrayOfByte2[(paramInt2 + k + i)] = (byte)(this.E[k] ^ this.G[k]);
        System.arraycopy(arrayOfByte, 0, this.G, 0, this.F);
        arrayOfByte = null;
        break;
      default:
        throw new IOException("Invalid cipher mode!");
      }
      i += this.F;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.A
 * JD-Core Version:    0.6.0
 */