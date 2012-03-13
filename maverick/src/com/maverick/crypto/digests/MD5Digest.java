package com.maverick.crypto.digests;

public class MD5Digest extends GeneralDigest
{
  private int h;
  private int g;
  private int f;
  private int e;
  private int[] j = new int[16];
  private int i;

  public MD5Digest()
  {
    reset();
  }

  public MD5Digest(MD5Digest paramMD5Digest)
  {
    super(paramMD5Digest);
    this.h = paramMD5Digest.h;
    this.g = paramMD5Digest.g;
    this.f = paramMD5Digest.f;
    this.e = paramMD5Digest.e;
    System.arraycopy(paramMD5Digest.j, 0, this.j, 0, paramMD5Digest.j.length);
    this.i = paramMD5Digest.i;
  }

  public String getAlgorithmName()
  {
    return "MD5";
  }

  public int getDigestSize()
  {
    return 16;
  }

  protected void processWord(byte[] paramArrayOfByte, int paramInt)
  {
    this.j[(this.i++)] = (paramArrayOfByte[paramInt] & 0xFF | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 3)] & 0xFF) << 24);
    if (this.i == 16)
      processBlock();
  }

  protected void processLength(long paramLong)
  {
    if (this.i > 14)
      processBlock();
    this.j[14] = (int)(paramLong & 0xFFFFFFFF);
    this.j[15] = (int)(paramLong >>> 32);
  }

  private void b(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = (byte)paramInt1;
    paramArrayOfByte[(paramInt2 + 1)] = (byte)(paramInt1 >>> 8);
    paramArrayOfByte[(paramInt2 + 2)] = (byte)(paramInt1 >>> 16);
    paramArrayOfByte[(paramInt2 + 3)] = (byte)(paramInt1 >>> 24);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    b(this.h, paramArrayOfByte, paramInt);
    b(this.g, paramArrayOfByte, paramInt + 4);
    b(this.f, paramArrayOfByte, paramInt + 8);
    b(this.e, paramArrayOfByte, paramInt + 12);
    reset();
    return 16;
  }

  public void reset()
  {
    super.reset();
    this.h = 1732584193;
    this.g = -271733879;
    this.f = -1732584194;
    this.e = 271733878;
    this.i = 0;
    for (int k = 0; k != this.j.length; k++)
      this.j[k] = 0;
  }

  private int b(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }

  private int b(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
  }

  private int d(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt3 | paramInt2 & (paramInt3 ^ 0xFFFFFFFF);
  }

  private int c(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 ^ paramInt2 ^ paramInt3;
  }

  private int e(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt2 ^ (paramInt1 | paramInt3 ^ 0xFFFFFFFF);
  }

  protected void processBlock()
  {
    int k = this.h;
    int m = this.g;
    int n = this.f;
    int i1 = this.e;
    k = b(k + b(m, n, i1) + this.j[0] + -680876936, 7) + m;
    i1 = b(i1 + b(k, m, n) + this.j[1] + -389564586, 12) + k;
    n = b(n + b(i1, k, m) + this.j[2] + 606105819, 17) + i1;
    m = b(m + b(n, i1, k) + this.j[3] + -1044525330, 22) + n;
    k = b(k + b(m, n, i1) + this.j[4] + -176418897, 7) + m;
    i1 = b(i1 + b(k, m, n) + this.j[5] + 1200080426, 12) + k;
    n = b(n + b(i1, k, m) + this.j[6] + -1473231341, 17) + i1;
    m = b(m + b(n, i1, k) + this.j[7] + -45705983, 22) + n;
    k = b(k + b(m, n, i1) + this.j[8] + 1770035416, 7) + m;
    i1 = b(i1 + b(k, m, n) + this.j[9] + -1958414417, 12) + k;
    n = b(n + b(i1, k, m) + this.j[10] + -42063, 17) + i1;
    m = b(m + b(n, i1, k) + this.j[11] + -1990404162, 22) + n;
    k = b(k + b(m, n, i1) + this.j[12] + 1804603682, 7) + m;
    i1 = b(i1 + b(k, m, n) + this.j[13] + -40341101, 12) + k;
    n = b(n + b(i1, k, m) + this.j[14] + -1502002290, 17) + i1;
    m = b(m + b(n, i1, k) + this.j[15] + 1236535329, 22) + n;
    k = b(k + d(m, n, i1) + this.j[1] + -165796510, 5) + m;
    i1 = b(i1 + d(k, m, n) + this.j[6] + -1069501632, 9) + k;
    n = b(n + d(i1, k, m) + this.j[11] + 643717713, 14) + i1;
    m = b(m + d(n, i1, k) + this.j[0] + -373897302, 20) + n;
    k = b(k + d(m, n, i1) + this.j[5] + -701558691, 5) + m;
    i1 = b(i1 + d(k, m, n) + this.j[10] + 38016083, 9) + k;
    n = b(n + d(i1, k, m) + this.j[15] + -660478335, 14) + i1;
    m = b(m + d(n, i1, k) + this.j[4] + -405537848, 20) + n;
    k = b(k + d(m, n, i1) + this.j[9] + 568446438, 5) + m;
    i1 = b(i1 + d(k, m, n) + this.j[14] + -1019803690, 9) + k;
    n = b(n + d(i1, k, m) + this.j[3] + -187363961, 14) + i1;
    m = b(m + d(n, i1, k) + this.j[8] + 1163531501, 20) + n;
    k = b(k + d(m, n, i1) + this.j[13] + -1444681467, 5) + m;
    i1 = b(i1 + d(k, m, n) + this.j[2] + -51403784, 9) + k;
    n = b(n + d(i1, k, m) + this.j[7] + 1735328473, 14) + i1;
    m = b(m + d(n, i1, k) + this.j[12] + -1926607734, 20) + n;
    k = b(k + c(m, n, i1) + this.j[5] + -378558, 4) + m;
    i1 = b(i1 + c(k, m, n) + this.j[8] + -2022574463, 11) + k;
    n = b(n + c(i1, k, m) + this.j[11] + 1839030562, 16) + i1;
    m = b(m + c(n, i1, k) + this.j[14] + -35309556, 23) + n;
    k = b(k + c(m, n, i1) + this.j[1] + -1530992060, 4) + m;
    i1 = b(i1 + c(k, m, n) + this.j[4] + 1272893353, 11) + k;
    n = b(n + c(i1, k, m) + this.j[7] + -155497632, 16) + i1;
    m = b(m + c(n, i1, k) + this.j[10] + -1094730640, 23) + n;
    k = b(k + c(m, n, i1) + this.j[13] + 681279174, 4) + m;
    i1 = b(i1 + c(k, m, n) + this.j[0] + -358537222, 11) + k;
    n = b(n + c(i1, k, m) + this.j[3] + -722521979, 16) + i1;
    m = b(m + c(n, i1, k) + this.j[6] + 76029189, 23) + n;
    k = b(k + c(m, n, i1) + this.j[9] + -640364487, 4) + m;
    i1 = b(i1 + c(k, m, n) + this.j[12] + -421815835, 11) + k;
    n = b(n + c(i1, k, m) + this.j[15] + 530742520, 16) + i1;
    m = b(m + c(n, i1, k) + this.j[2] + -995338651, 23) + n;
    k = b(k + e(m, n, i1) + this.j[0] + -198630844, 6) + m;
    i1 = b(i1 + e(k, m, n) + this.j[7] + 1126891415, 10) + k;
    n = b(n + e(i1, k, m) + this.j[14] + -1416354905, 15) + i1;
    m = b(m + e(n, i1, k) + this.j[5] + -57434055, 21) + n;
    k = b(k + e(m, n, i1) + this.j[12] + 1700485571, 6) + m;
    i1 = b(i1 + e(k, m, n) + this.j[3] + -1894986606, 10) + k;
    n = b(n + e(i1, k, m) + this.j[10] + -1051523, 15) + i1;
    m = b(m + e(n, i1, k) + this.j[1] + -2054922799, 21) + n;
    k = b(k + e(m, n, i1) + this.j[8] + 1873313359, 6) + m;
    i1 = b(i1 + e(k, m, n) + this.j[15] + -30611744, 10) + k;
    n = b(n + e(i1, k, m) + this.j[6] + -1560198380, 15) + i1;
    m = b(m + e(n, i1, k) + this.j[13] + 1309151649, 21) + n;
    k = b(k + e(m, n, i1) + this.j[4] + -145523070, 6) + m;
    i1 = b(i1 + e(k, m, n) + this.j[11] + -1120210379, 10) + k;
    n = b(n + e(i1, k, m) + this.j[2] + 718787259, 15) + i1;
    m = b(m + e(n, i1, k) + this.j[9] + -343485551, 21) + n;
    this.h += k;
    this.g += m;
    this.f += n;
    this.e += i1;
    this.i = 0;
    for (int i2 = 0; i2 != this.j.length; i2++)
      this.j[i2] = 0;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.digests.MD5Digest
 * JD-Core Version:    0.6.0
 */