package com.maverick.crypto.engines;

import java.io.IOException;

public class DESedeEngine extends DESEngine
{
  protected static final int BLOCK_SIZE = 8;
  private int[] n = null;
  private int[] l = null;
  private int[] k = null;
  private boolean m;

  public void init(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte1 = paramArrayOfByte;
    byte[] arrayOfByte2 = new byte[8];
    byte[] arrayOfByte3 = new byte[8];
    byte[] arrayOfByte4 = new byte[8];
    this.m = paramBoolean;
    if (arrayOfByte1.length == 24)
    {
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
      System.arraycopy(arrayOfByte1, 8, arrayOfByte3, 0, arrayOfByte3.length);
      System.arraycopy(arrayOfByte1, 16, arrayOfByte4, 0, arrayOfByte4.length);
      this.n = generateWorkingKey(paramBoolean, arrayOfByte2);
      this.l = generateWorkingKey(!paramBoolean, arrayOfByte3);
      this.k = generateWorkingKey(paramBoolean, arrayOfByte4);
    }
    else
    {
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
      System.arraycopy(arrayOfByte1, 8, arrayOfByte3, 0, arrayOfByte3.length);
      this.n = generateWorkingKey(paramBoolean, arrayOfByte2);
      this.l = generateWorkingKey(!paramBoolean, arrayOfByte3);
      this.k = this.n;
    }
  }

  public String getAlgorithmName()
  {
    return "DESede";
  }

  public int getBlockSize()
  {
    return 8;
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws IOException
  {
    if (this.n == null)
      throw new IllegalStateException("DESede engine not initialised");
    if (paramInt1 + 8 > paramArrayOfByte1.length)
      throw new IOException("input buffer too short");
    if (paramInt2 + 8 > paramArrayOfByte2.length)
      throw new IOException("output buffer too short");
    if (this.m)
    {
      desFunc(this.n, paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
      desFunc(this.l, paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2);
      desFunc(this.k, paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2);
    }
    else
    {
      desFunc(this.k, paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
      desFunc(this.l, paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2);
      desFunc(this.n, paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2);
    }
    return 8;
  }

  public void reset()
  {
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.engines.DESedeEngine
 * JD-Core Version:    0.6.0
 */