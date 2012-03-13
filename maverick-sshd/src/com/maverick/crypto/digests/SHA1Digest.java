package com.maverick.crypto.digests;

public class SHA1Digest extends GeneralDigest
{
  private int N;
  private int M;
  private int L;
  private int K;
  private int J;
  private int[] P = new int[80];
  private int O;

  public SHA1Digest()
  {
    reset();
  }

  public SHA1Digest(SHA1Digest paramSHA1Digest)
  {
    super(paramSHA1Digest);
    this.N = paramSHA1Digest.N;
    this.M = paramSHA1Digest.M;
    this.L = paramSHA1Digest.L;
    this.K = paramSHA1Digest.K;
    this.J = paramSHA1Digest.J;
    System.arraycopy(paramSHA1Digest.P, 0, this.P, 0, paramSHA1Digest.P.length);
    this.O = paramSHA1Digest.O;
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
    this.P[(this.O++)] = ((paramArrayOfByte[paramInt] & 0xFF) << 24 | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 3)] & 0xFF);
    if (this.O == 16)
      processBlock();
  }

  private void B(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = (byte)(paramInt1 >>> 24);
    paramArrayOfByte[(paramInt2 + 1)] = (byte)(paramInt1 >>> 16);
    paramArrayOfByte[(paramInt2 + 2)] = (byte)(paramInt1 >>> 8);
    paramArrayOfByte[(paramInt2 + 3)] = (byte)paramInt1;
  }

  protected void processLength(long paramLong)
  {
    if (this.O > 14)
      processBlock();
    this.P[14] = (int)(paramLong >>> 32);
    this.P[15] = (int)(paramLong & 0xFFFFFFFF);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    B(this.N, paramArrayOfByte, paramInt);
    B(this.M, paramArrayOfByte, paramInt + 4);
    B(this.L, paramArrayOfByte, paramInt + 8);
    B(this.K, paramArrayOfByte, paramInt + 12);
    B(this.J, paramArrayOfByte, paramInt + 16);
    reset();
    return 20;
  }

  public void reset()
  {
    super.reset();
    this.N = 1732584193;
    this.M = -271733879;
    this.L = -1732584194;
    this.K = 271733878;
    this.J = -1009589776;
    this.O = 0;
    for (int i = 0; i != this.P.length; i++)
      this.P[i] = 0;
  }

  private int F(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
  }

  private int G(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 ^ paramInt2 ^ paramInt3;
  }

  private int E(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt1 & paramInt3 | paramInt2 & paramInt3;
  }

  private int B(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }

  protected void processBlock()
  {
    for (int i = 16; i <= 79; i++)
      this.P[i] = B(this.P[(i - 3)] ^ this.P[(i - 8)] ^ this.P[(i - 14)] ^ this.P[(i - 16)], 1);
    i = this.N;
    int j = this.M;
    int k = this.L;
    int m = this.K;
    int n = this.J;
    int i2;
    for (int i1 = 0; i1 <= 19; i1++)
    {
      i2 = B(i, 5) + F(j, k, m) + n + this.P[i1] + 1518500249;
      n = m;
      m = k;
      k = B(j, 30);
      j = i;
      i = i2;
    }
    for (i1 = 20; i1 <= 39; i1++)
    {
      i2 = B(i, 5) + G(j, k, m) + n + this.P[i1] + 1859775393;
      n = m;
      m = k;
      k = B(j, 30);
      j = i;
      i = i2;
    }
    for (i1 = 40; i1 <= 59; i1++)
    {
      i2 = B(i, 5) + E(j, k, m) + n + this.P[i1] + -1894007588;
      n = m;
      m = k;
      k = B(j, 30);
      j = i;
      i = i2;
    }
    for (i1 = 60; i1 <= 79; i1++)
    {
      i2 = B(i, 5) + G(j, k, m) + n + this.P[i1] + -899497514;
      n = m;
      m = k;
      k = B(j, 30);
      j = i;
      i = i2;
    }
    this.N += i;
    this.M += j;
    this.L += k;
    this.K += m;
    this.J += n;
    this.O = 0;
    for (i1 = 0; i1 != this.P.length; i1++)
      this.P[i1] = 0;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.digests.SHA1Digest
 * JD-Core Version:    0.6.0
 */