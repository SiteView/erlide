package com.maverick.crypto.engines;

import java.io.IOException;

public class DESedeEngine extends DESEngine
{
  protected static final int BLOCK_SIZE = 8;
  private int[] fb = null;
  private int[] db = null;
  private int[] cb = null;
  private boolean eb;

  public void init(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte1 = paramArrayOfByte;
    byte[] arrayOfByte2 = new byte[8];
    byte[] arrayOfByte3 = new byte[8];
    byte[] arrayOfByte4 = new byte[8];
    this.eb = paramBoolean;
    if (arrayOfByte1.length == 24)
    {
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
      System.arraycopy(arrayOfByte1, 8, arrayOfByte3, 0, arrayOfByte3.length);
      System.arraycopy(arrayOfByte1, 16, arrayOfByte4, 0, arrayOfByte4.length);
      this.fb = generateWorkingKey(paramBoolean, arrayOfByte2);
      this.db = generateWorkingKey(!paramBoolean, arrayOfByte3);
      this.cb = generateWorkingKey(paramBoolean, arrayOfByte4);
    }
    else
    {
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
      System.arraycopy(arrayOfByte1, 8, arrayOfByte3, 0, arrayOfByte3.length);
      this.fb = generateWorkingKey(paramBoolean, arrayOfByte2);
      this.db = generateWorkingKey(!paramBoolean, arrayOfByte3);
      this.cb = this.fb;
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
    if (this.fb == null)
      throw new IllegalStateException("DESede engine not initialised");
    if (paramInt1 + 8 > paramArrayOfByte1.length)
      throw new IOException("input buffer too short");
    if (paramInt2 + 8 > paramArrayOfByte2.length)
      throw new IOException("output buffer too short");
    if (this.eb)
    {
      desFunc(this.fb, paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
      desFunc(this.db, paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2);
      desFunc(this.cb, paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2);
    }
    else
    {
      desFunc(this.cb, paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
      desFunc(this.db, paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2);
      desFunc(this.fb, paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2);
    }
    return 8;
  }

  public void reset()
  {
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.engines.DESedeEngine
 * JD-Core Version:    0.6.0
 */