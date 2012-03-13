package com.maverick.crypto.digests;

public class SHA1Digest extends GeneralDigest
{
  private int o;
  private int n;
  private int m;
  private int l;
  private int k;
  private int[] q = new int[80];
  private int p;

  public SHA1Digest()
  {
    reset();
  }

  public SHA1Digest(SHA1Digest paramSHA1Digest)
  {
    super(paramSHA1Digest);
    this.o = paramSHA1Digest.o;
    this.n = paramSHA1Digest.n;
    this.m = paramSHA1Digest.m;
    this.l = paramSHA1Digest.l;
    this.k = paramSHA1Digest.k;
    System.arraycopy(paramSHA1Digest.q, 0, this.q, 0, paramSHA1Digest.q.length);
    this.p = paramSHA1Digest.p;
  }

  public String getAlgorithmName()
  {
    return "SHA-1";
  }

  public int getDigestSize()
  {
    return 20;
  }

  protected void processWord(byte[] paramArrayOfByte, int paramInt)
  {
    this.q[(this.p++)] = ((paramArrayOfByte[paramInt] & 0xFF) << 24 | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 3)] & 0xFF);
    if (this.p == 16)
      processBlock();
  }

  private void c(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = (byte)(paramInt1 >>> 24);
    paramArrayOfByte[(paramInt2 + 1)] = (byte)(paramInt1 >>> 16);
    paramArrayOfByte[(paramInt2 + 2)] = (byte)(paramInt1 >>> 8);
    paramArrayOfByte[(paramInt2 + 3)] = (byte)paramInt1;
  }

  protected void processLength(long paramLong)
  {
    if (this.p > 14)
      processBlock();
    this.q[14] = (int)(paramLong >>> 32);
    this.q[15] = (int)(paramLong & 0xFFFFFFFF);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    c(this.o, paramArrayOfByte, paramInt);
    c(this.n, paramArrayOfByte, paramInt + 4);
    c(this.m, paramArrayOfByte, paramInt + 8);
    c(this.l, paramArrayOfByte, paramInt + 12);
    c(this.k, paramArrayOfByte, paramInt + 16);
    reset();
    return 20;
  }

  public void reset()
  {
    super.reset();
    this.o = 1732584193;
    this.n = -271733879;
    this.m = -1732584194;
    this.l = 271733878;
    this.k = -1009589776;
    this.p = 0;
    for (int i = 0; i != this.q.length; i++)
      this.q[i] = 0;
  }

  private int g(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
  }

  private int h(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 ^ paramInt2 ^ paramInt3;
  }

  private int f(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt1 & paramInt3 | paramInt2 & paramInt3;
  }

  private int c(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }

  protected void processBlock()
  {
    for (int i = 16; i <= 79; i++)
      this.q[i] = c(this.q[(i - 3)] ^ this.q[(i - 8)] ^ this.q[(i - 14)] ^ this.q[(i - 16)], 1);
    i = this.o;
    int j = this.n;
    int i1 = this.m;
    int i2 = this.l;
    int i3 = this.k;
    int i5;
    for (int i4 = 0; i4 <= 19; i4++)
    {
      i5 = c(i, 5) + g(j, i1, i2) + i3 + this.q[i4] + 1518500249;
      i3 = i2;
      i2 = i1;
      i1 = c(j, 30);
      j = i;
      i = i5;
    }
    for (i4 = 20; i4 <= 39; i4++)
    {
      i5 = c(i, 5) + h(j, i1, i2) + i3 + this.q[i4] + 1859775393;
      i3 = i2;
      i2 = i1;
      i1 = c(j, 30);
      j = i;
      i = i5;
    }
    for (i4 = 40; i4 <= 59; i4++)
    {
      i5 = c(i, 5) + f(j, i1, i2) + i3 + this.q[i4] + -1894007588;
      i3 = i2;
      i2 = i1;
      i1 = c(j, 30);
      j = i;
      i = i5;
    }
    for (i4 = 60; i4 <= 79; i4++)
    {
      i5 = c(i, 5) + h(j, i1, i2) + i3 + this.q[i4] + -899497514;
      i3 = i2;
      i2 = i1;
      i1 = c(j, 30);
      j = i;
      i = i5;
    }
    this.o += i;
    this.n += j;
    this.m += i1;
    this.l += i2;
    this.k += i3;
    this.p = 0;
    for (i4 = 0; i4 != this.q.length; i4++)
      this.q[i4] = 0;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.digests.SHA1Digest
 * JD-Core Version:    0.6.0
 */