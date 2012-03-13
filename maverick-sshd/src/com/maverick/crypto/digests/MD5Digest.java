package com.maverick.crypto.digests;

public class MD5Digest extends GeneralDigest
{
  private int G;
  private int F;
  private int E;
  private int D;
  private int[] I = new int[16];
  private int H;

  public MD5Digest()
  {
    reset();
  }

  public MD5Digest(MD5Digest paramMD5Digest)
  {
    super(paramMD5Digest);
    this.G = paramMD5Digest.G;
    this.F = paramMD5Digest.F;
    this.E = paramMD5Digest.E;
    this.D = paramMD5Digest.D;
    System.arraycopy(paramMD5Digest.I, 0, this.I, 0, paramMD5Digest.I.length);
    this.H = paramMD5Digest.H;
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
    this.I[(this.H++)] = (paramArrayOfByte[paramInt] & 0xFF | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 3)] & 0xFF) << 24);
    if (this.H == 16)
      processBlock();
  }

  protected void processLength(long paramLong)
  {
    if (this.H > 14)
      processBlock();
    this.I[14] = (int)(paramLong & 0xFFFFFFFF);
    this.I[15] = (int)(paramLong >>> 32);
  }

  private void A(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = (byte)paramInt1;
    paramArrayOfByte[(paramInt2 + 1)] = (byte)(paramInt1 >>> 8);
    paramArrayOfByte[(paramInt2 + 2)] = (byte)(paramInt1 >>> 16);
    paramArrayOfByte[(paramInt2 + 3)] = (byte)(paramInt1 >>> 24);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    A(this.G, paramArrayOfByte, paramInt);
    A(this.F, paramArrayOfByte, paramInt + 4);
    A(this.E, paramArrayOfByte, paramInt + 8);
    A(this.D, paramArrayOfByte, paramInt + 12);
    reset();
    return 16;
  }

  public void reset()
  {
    super.reset();
    this.G = 1732584193;
    this.F = -271733879;
    this.E = -1732584194;
    this.D = 271733878;
    this.H = 0;
    for (int i = 0; i != this.I.length; i++)
      this.I[i] = 0;
  }

  private int A(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }

  private int A(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
  }

  private int C(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt3 | paramInt2 & (paramInt3 ^ 0xFFFFFFFF);
  }

  private int B(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 ^ paramInt2 ^ paramInt3;
  }

  private int D(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt2 ^ (paramInt1 | paramInt3 ^ 0xFFFFFFFF);
  }

  protected void processBlock()
  {
    int i = this.G;
    int j = this.F;
    int k = this.E;
    int m = this.D;
    i = A(i + A(j, k, m) + this.I[0] + -680876936, 7) + j;
    m = A(m + A(i, j, k) + this.I[1] + -389564586, 12) + i;
    k = A(k + A(m, i, j) + this.I[2] + 606105819, 17) + m;
    j = A(j + A(k, m, i) + this.I[3] + -1044525330, 22) + k;
    i = A(i + A(j, k, m) + this.I[4] + -176418897, 7) + j;
    m = A(m + A(i, j, k) + this.I[5] + 1200080426, 12) + i;
    k = A(k + A(m, i, j) + this.I[6] + -1473231341, 17) + m;
    j = A(j + A(k, m, i) + this.I[7] + -45705983, 22) + k;
    i = A(i + A(j, k, m) + this.I[8] + 1770035416, 7) + j;
    m = A(m + A(i, j, k) + this.I[9] + -1958414417, 12) + i;
    k = A(k + A(m, i, j) + this.I[10] + -42063, 17) + m;
    j = A(j + A(k, m, i) + this.I[11] + -1990404162, 22) + k;
    i = A(i + A(j, k, m) + this.I[12] + 1804603682, 7) + j;
    m = A(m + A(i, j, k) + this.I[13] + -40341101, 12) + i;
    k = A(k + A(m, i, j) + this.I[14] + -1502002290, 17) + m;
    j = A(j + A(k, m, i) + this.I[15] + 1236535329, 22) + k;
    i = A(i + C(j, k, m) + this.I[1] + -165796510, 5) + j;
    m = A(m + C(i, j, k) + this.I[6] + -1069501632, 9) + i;
    k = A(k + C(m, i, j) + this.I[11] + 643717713, 14) + m;
    j = A(j + C(k, m, i) + this.I[0] + -373897302, 20) + k;
    i = A(i + C(j, k, m) + this.I[5] + -701558691, 5) + j;
    m = A(m + C(i, j, k) + this.I[10] + 38016083, 9) + i;
    k = A(k + C(m, i, j) + this.I[15] + -660478335, 14) + m;
    j = A(j + C(k, m, i) + this.I[4] + -405537848, 20) + k;
    i = A(i + C(j, k, m) + this.I[9] + 568446438, 5) + j;
    m = A(m + C(i, j, k) + this.I[14] + -1019803690, 9) + i;
    k = A(k + C(m, i, j) + this.I[3] + -187363961, 14) + m;
    j = A(j + C(k, m, i) + this.I[8] + 1163531501, 20) + k;
    i = A(i + C(j, k, m) + this.I[13] + -1444681467, 5) + j;
    m = A(m + C(i, j, k) + this.I[2] + -51403784, 9) + i;
    k = A(k + C(m, i, j) + this.I[7] + 1735328473, 14) + m;
    j = A(j + C(k, m, i) + this.I[12] + -1926607734, 20) + k;
    i = A(i + B(j, k, m) + this.I[5] + -378558, 4) + j;
    m = A(m + B(i, j, k) + this.I[8] + -2022574463, 11) + i;
    k = A(k + B(m, i, j) + this.I[11] + 1839030562, 16) + m;
    j = A(j + B(k, m, i) + this.I[14] + -35309556, 23) + k;
    i = A(i + B(j, k, m) + this.I[1] + -1530992060, 4) + j;
    m = A(m + B(i, j, k) + this.I[4] + 1272893353, 11) + i;
    k = A(k + B(m, i, j) + this.I[7] + -155497632, 16) + m;
    j = A(j + B(k, m, i) + this.I[10] + -1094730640, 23) + k;
    i = A(i + B(j, k, m) + this.I[13] + 681279174, 4) + j;
    m = A(m + B(i, j, k) + this.I[0] + -358537222, 11) + i;
    k = A(k + B(m, i, j) + this.I[3] + -722521979, 16) + m;
    j = A(j + B(k, m, i) + this.I[6] + 76029189, 23) + k;
    i = A(i + B(j, k, m) + this.I[9] + -640364487, 4) + j;
    m = A(m + B(i, j, k) + this.I[12] + -421815835, 11) + i;
    k = A(k + B(m, i, j) + this.I[15] + 530742520, 16) + m;
    j = A(j + B(k, m, i) + this.I[2] + -995338651, 23) + k;
    i = A(i + D(j, k, m) + this.I[0] + -198630844, 6) + j;
    m = A(m + D(i, j, k) + this.I[7] + 1126891415, 10) + i;
    k = A(k + D(m, i, j) + this.I[14] + -1416354905, 15) + m;
    j = A(j + D(k, m, i) + this.I[5] + -57434055, 21) + k;
    i = A(i + D(j, k, m) + this.I[12] + 1700485571, 6) + j;
    m = A(m + D(i, j, k) + this.I[3] + -1894986606, 10) + i;
    k = A(k + D(m, i, j) + this.I[10] + -1051523, 15) + m;
    j = A(j + D(k, m, i) + this.I[1] + -2054922799, 21) + k;
    i = A(i + D(j, k, m) + this.I[8] + 1873313359, 6) + j;
    m = A(m + D(i, j, k) + this.I[15] + -30611744, 10) + i;
    k = A(k + D(m, i, j) + this.I[6] + -1560198380, 15) + m;
    j = A(j + D(k, m, i) + this.I[13] + 1309151649, 21) + k;
    i = A(i + D(j, k, m) + this.I[4] + -145523070, 6) + j;
    m = A(m + D(i, j, k) + this.I[11] + -1120210379, 10) + i;
    k = A(k + D(m, i, j) + this.I[2] + 718787259, 15) + m;
    j = A(j + D(k, m, i) + this.I[9] + -343485551, 21) + k;
    this.G += i;
    this.F += j;
    this.E += k;
    this.D += m;
    this.H = 0;
    for (int n = 0; n != this.I.length; n++)
      this.I[n] = 0;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.digests.MD5Digest
 * JD-Core Version:    0.6.0
 */