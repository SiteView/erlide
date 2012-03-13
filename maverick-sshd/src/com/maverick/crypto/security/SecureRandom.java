package com.maverick.crypto.security;

import com.maverick.crypto.digests.SHA1Digest;
import java.util.Random;

public class SecureRandom extends Random
{
  private static SecureRandom D = new SecureRandom();
  private long B = 1L;
  private SHA1Digest F = new SHA1Digest();
  private byte[] E = new byte[this.F.getDigestSize()];
  private byte[] A = new byte[4];
  private byte[] C = new byte[8];

  public SecureRandom()
  {
    super(0L);
    setSeed(System.currentTimeMillis());
  }

  public SecureRandom(byte[] paramArrayOfByte)
  {
    setSeed(paramArrayOfByte);
  }

  public static SecureRandom getInstance(String paramString)
  {
    return new SecureRandom();
  }

  public static SecureRandom getInstance()
  {
    synchronized (D)
    {
      D.setSeed(System.currentTimeMillis());
      return D;
    }
  }

  public static byte[] getSeed(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    synchronized (D)
    {
      D.setSeed(System.currentTimeMillis());
      D.nextBytes(arrayOfByte);
    }
    return arrayOfByte;
  }

  public byte[] generateSeed(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    nextBytes(arrayOfByte);
    return arrayOfByte;
  }

  public synchronized void setSeed(byte[] paramArrayOfByte)
  {
    this.F.update(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public synchronized void nextBytes(byte[] paramArrayOfByte)
  {
    nextBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public synchronized void nextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 0;
    this.F.doFinal(this.E, 0);
    for (int j = 0; j != paramInt2; j++)
    {
      if (i == this.E.length)
      {
        byte[] arrayOfByte2 = A(this.B++);
        this.F.update(arrayOfByte2, 0, arrayOfByte2.length);
        this.F.update(this.E, 0, this.E.length);
        this.F.doFinal(this.E, 0);
        i = 0;
      }
      paramArrayOfByte[(j + paramInt1)] = this.E[(i++)];
    }
    byte[] arrayOfByte1 = A(this.B++);
    this.F.update(arrayOfByte1, 0, arrayOfByte1.length);
    this.F.update(this.E, 0, this.E.length);
  }

  public synchronized void setSeed(long paramLong)
  {
    if (paramLong != 0L)
      setSeed(A(paramLong));
  }

  public synchronized int nextInt()
  {
    nextBytes(this.A);
    int i = 0;
    for (int j = 0; j < 4; j++)
      i = (i << 8) + (this.A[j] & 0xFF);
    return i;
  }

  protected final synchronized int next(int paramInt)
  {
    int i = (paramInt + 7) / 8;
    byte[] arrayOfByte = new byte[i];
    nextBytes(arrayOfByte);
    int j = 0;
    for (int k = 0; k < i; k++)
      j = (j << 8) + (arrayOfByte[k] & 0xFF);
    return j & (1 << paramInt) - 1;
  }

  private synchronized byte[] A(long paramLong)
  {
    for (int i = 0; i != 8; i++)
    {
      this.C[i] = (byte)(int)paramLong;
      paramLong >>>= 8;
    }
    return this.C;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.security.SecureRandom
 * JD-Core Version:    0.6.0
 */