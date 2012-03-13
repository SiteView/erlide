package com.maverick.crypto.security;

import com.maverick.crypto.digests.SHA1Digest;
import java.util.Random;

public class SecureRandom extends Random
{
  private static SecureRandom e = new SecureRandom();
  private long c = 1L;
  private SHA1Digest g = new SHA1Digest();
  private byte[] f = new byte[this.g.getDigestSize()];
  private byte[] b = new byte[4];
  private byte[] d = new byte[8];

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
    synchronized (e)
    {
      e.setSeed(System.currentTimeMillis());
      return e;
    }
  }

  public static byte[] getSeed(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    synchronized (e)
    {
      e.setSeed(System.currentTimeMillis());
      e.nextBytes(arrayOfByte);
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
    this.g.update(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public synchronized void nextBytes(byte[] paramArrayOfByte)
  {
    nextBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public synchronized void nextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 0;
    this.g.doFinal(this.f, 0);
    for (int j = 0; j != paramInt2; j++)
    {
      if (i == this.f.length)
      {
        byte[] arrayOfByte2 = b(this.c++);
        this.g.update(arrayOfByte2, 0, arrayOfByte2.length);
        this.g.update(this.f, 0, this.f.length);
        this.g.doFinal(this.f, 0);
        i = 0;
      }
      paramArrayOfByte[(j + paramInt1)] = this.f[(i++)];
    }
    byte[] arrayOfByte1 = b(this.c++);
    this.g.update(arrayOfByte1, 0, arrayOfByte1.length);
    this.g.update(this.f, 0, this.f.length);
  }

  public synchronized void setSeed(long paramLong)
  {
    if (paramLong != 0L)
      setSeed(b(paramLong));
  }

  public synchronized int nextInt()
  {
    nextBytes(this.b);
    int i = 0;
    for (int j = 0; j < 4; j++)
      i = (i << 8) + (this.b[j] & 0xFF);
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

  private synchronized byte[] b(long paramLong)
  {
    for (int i = 0; i != 8; i++)
    {
      this.d[i] = (byte)(int)paramLong;
      paramLong >>>= 8;
    }
    return this.d;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.security.SecureRandom
 * JD-Core Version:    0.6.0
 */