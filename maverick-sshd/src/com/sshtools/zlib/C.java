package com.sshtools.zlib;

final class C
{
  static final int[] I = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0 };
  static final int[] E = { 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13 };
  static final int[] H = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 7 };
  static final byte[] D = { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
  static final byte[] C = { 0, 1, 2, 3, 4, 4, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0, 0, 16, 17, 18, 18, 19, 19, 20, 20, 20, 20, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29 };
  static final byte[] B = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28 };
  static final int[] G = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16, 20, 24, 28, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 0 };
  static final int[] K = { 0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 128, 192, 256, 384, 512, 768, 1024, 1536, 2048, 3072, 4096, 6144, 8192, 12288, 16384, 24576 };
  short[] A;
  int J;
  E F;

  static int A(int paramInt)
  {
    return paramInt < 256 ? C[paramInt] : C[(256 + (paramInt >>> 7))];
  }

  void B(Deflate paramDeflate)
  {
    short[] arrayOfShort1 = this.A;
    short[] arrayOfShort2 = this.F.G;
    int[] arrayOfInt = this.F.H;
    int i = this.F.B;
    int j = this.F.I;
    int i4 = 0;
    for (int i1 = 0; i1 <= 15; i1++)
      paramDeflate.v[i1] = 0;
    arrayOfShort1[(paramDeflate.J[paramDeflate.R] * 2 + 1)] = 0;
    int m;
    for (int k = paramDeflate.R + 1; k < 573; k++)
    {
      m = paramDeflate.J[k];
      i1 = arrayOfShort1[(arrayOfShort1[(m * 2 + 1)] * 2 + 1)] + 1;
      if (i1 > j)
      {
        i1 = j;
        i4++;
      }
      arrayOfShort1[(m * 2 + 1)] = (short)i1;
      if (m > this.J)
        continue;
      int tmp169_167 = i1;
      short[] tmp169_164 = paramDeflate.v;
      tmp169_164[tmp169_167] = (short)(tmp169_164[tmp169_167] + 1);
      int i2 = 0;
      if (m >= i)
        i2 = arrayOfInt[(m - i)];
      int i3 = arrayOfShort1[(m * 2)];
      paramDeflate.p += i3 * (i1 + i2);
      if (arrayOfShort2 == null)
        continue;
      paramDeflate.m += i3 * (arrayOfShort2[(m * 2 + 1)] + i2);
    }
    if (i4 == 0)
      return;
    do
    {
      for (i1 = j - 1; paramDeflate.v[i1] == 0; i1--);
      int tmp287_285 = i1;
      short[] tmp287_282 = paramDeflate.v;
      tmp287_282[tmp287_285] = (short)(tmp287_282[tmp287_285] - 1);
      int tmp301_300 = (i1 + 1);
      short[] tmp301_294 = paramDeflate.v;
      tmp301_294[tmp301_300] = (short)(tmp301_294[tmp301_300] + 2);
      int tmp313_311 = j;
      short[] tmp313_308 = paramDeflate.v;
      tmp313_308[tmp313_311] = (short)(tmp313_308[tmp313_311] - 1);
      i4 -= 2;
    }
    while (i4 > 0);
    for (i1 = j; i1 != 0; i1--)
    {
      m = paramDeflate.v[i1];
      while (m != 0)
      {
        k--;
        int n = paramDeflate.J[k];
        if (n > this.J)
          continue;
        if (arrayOfShort1[(n * 2 + 1)] != i1)
        {
          Deflate tmp388_387 = paramDeflate;
          tmp388_387.p = (int)(tmp388_387.p + (i1 - arrayOfShort1[(n * 2 + 1)]) * arrayOfShort1[(n * 2)]);
          arrayOfShort1[(n * 2 + 1)] = (short)i1;
        }
        m--;
      }
    }
  }

  void A(Deflate paramDeflate)
  {
    short[] arrayOfShort1 = this.A;
    short[] arrayOfShort2 = this.F.G;
    int i = this.F.A;
    int m = -1;
    paramDeflate.M = 0;
    paramDeflate.R = 573;
    for (int j = 0; j < i; j++)
      if (arrayOfShort1[(j * 2)] != 0)
      {
        int tmp73_71 = j;
        m = tmp73_71;
        paramDeflate.J[(++paramDeflate.M)] = tmp73_71;
        paramDeflate.L[j] = 0;
      }
      else
      {
        arrayOfShort1[(j * 2 + 1)] = 0;
      }
    while (paramDeflate.M < 2)
    {
      m++;
      n = paramDeflate.J[(++paramDeflate.M)] = m < 2 ? m : 0;
      arrayOfShort1[(n * 2)] = 1;
      paramDeflate.L[n] = 0;
      paramDeflate.p -= 1;
      if (arrayOfShort2 == null)
        continue;
      paramDeflate.m -= arrayOfShort2[(n * 2 + 1)];
    }
    this.J = m;
    for (j = paramDeflate.M / 2; j >= 1; j--)
      paramDeflate.B(arrayOfShort1, j);
    int n = i;
    do
    {
      j = paramDeflate.J[1];
      paramDeflate.J[1] = paramDeflate.J[(paramDeflate.M--)];
      paramDeflate.B(arrayOfShort1, 1);
      int k = paramDeflate.J[1];
      paramDeflate.J[(--paramDeflate.R)] = j;
      paramDeflate.J[(--paramDeflate.R)] = k;
      arrayOfShort1[(n * 2)] = (short)(arrayOfShort1[(j * 2)] + arrayOfShort1[(k * 2)]);
      paramDeflate.L[n] = (byte)(Math.max(paramDeflate.L[j], paramDeflate.L[k]) + 1);
      short tmp375_374 = (short)n;
      arrayOfShort1[(k * 2 + 1)] = tmp375_374;
      arrayOfShort1[(j * 2 + 1)] = tmp375_374;
      paramDeflate.J[1] = (n++);
      paramDeflate.B(arrayOfShort1, 1);
    }
    while (paramDeflate.M >= 2);
    paramDeflate.J[(--paramDeflate.R)] = paramDeflate.J[1];
    B(paramDeflate);
    A(arrayOfShort1, m, paramDeflate.v);
  }

  static void A(short[] paramArrayOfShort1, int paramInt, short[] paramArrayOfShort2)
  {
    short[] arrayOfShort = new short[16];
    int i = 0;
    for (int j = 1; j <= 15; j++)
    {
      short tmp33_32 = (short)(i + paramArrayOfShort2[(j - 1)] << 1);
      i = tmp33_32;
      arrayOfShort[j] = tmp33_32;
    }
    for (int k = 0; k <= paramInt; k++)
    {
      int m = paramArrayOfShort1[(k * 2 + 1)];
      if (m == 0)
        continue;
      int tmp78_76 = m;
      short[] tmp78_75 = arrayOfShort;
      short tmp80_79 = tmp78_75[tmp78_76];
      tmp78_75[tmp78_76] = (short)(tmp80_79 + 1);
      paramArrayOfShort1[(k * 2)] = (short)A(tmp80_79, m);
    }
  }

  static int A(int paramInt1, int paramInt2)
  {
    int i = 0;
    do
    {
      i |= paramInt1 & 0x1;
      paramInt1 >>>= 1;
      i <<= 1;
      paramInt2--;
    }
    while (paramInt2 > 0);
    return i >>> 1;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.C
 * JD-Core Version:    0.6.0
 */