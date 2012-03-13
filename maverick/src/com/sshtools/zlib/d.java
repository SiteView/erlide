package com.sshtools.zlib;

final class d
{
  static final int[] j = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0 };
  static final int[] f = { 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13 };
  static final int[] i = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 7 };
  static final byte[] e = { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
  static final byte[] d = { 0, 1, 2, 3, 4, 4, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0, 0, 16, 17, 18, 18, 19, 19, 20, 20, 20, 20, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29 };
  static final byte[] c = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28 };
  static final int[] h = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16, 20, 24, 28, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 0 };
  static final int[] l = { 0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 128, 192, 256, 384, 512, 768, 1024, 1536, 2048, 3072, 4096, 6144, 8192, 12288, 16384, 24576 };
  short[] b;
  int k;
  f g;

  static int b(int paramInt)
  {
    return paramInt < 256 ? d[paramInt] : d[(256 + (paramInt >>> 7))];
  }

  void c(Deflate paramDeflate)
  {
    short[] arrayOfShort1 = this.b;
    short[] arrayOfShort2 = this.g.h;
    int[] arrayOfInt = this.g.i;
    int m = this.g.c;
    int n = this.g.j;
    int i7 = 0;
    for (int i4 = 0; i4 <= 15; i4++)
      paramDeflate.xb[i4] = 0;
    arrayOfShort1[(paramDeflate.k[paramDeflate.s] * 2 + 1)] = 0;
    int i2;
    for (int i1 = paramDeflate.s + 1; i1 < 573; i1++)
    {
      i2 = paramDeflate.k[i1];
      i4 = arrayOfShort1[(arrayOfShort1[(i2 * 2 + 1)] * 2 + 1)] + 1;
      if (i4 > n)
      {
        i4 = n;
        i7++;
      }
      arrayOfShort1[(i2 * 2 + 1)] = (short)i4;
      if (i2 > this.k)
        continue;
      int tmp169_167 = i4;
      short[] tmp169_164 = paramDeflate.xb;
      tmp169_164[tmp169_167] = (short)(tmp169_164[tmp169_167] + 1);
      int i5 = 0;
      if (i2 >= m)
        i5 = arrayOfInt[(i2 - m)];
      int i6 = arrayOfShort1[(i2 * 2)];
      paramDeflate.rb += i6 * (i4 + i5);
      if (arrayOfShort2 == null)
        continue;
      paramDeflate.ob += i6 * (arrayOfShort2[(i2 * 2 + 1)] + i5);
    }
    if (i7 == 0)
      return;
    do
    {
      for (i4 = n - 1; paramDeflate.xb[i4] == 0; i4--);
      int tmp287_285 = i4;
      short[] tmp287_282 = paramDeflate.xb;
      tmp287_282[tmp287_285] = (short)(tmp287_282[tmp287_285] - 1);
      int tmp301_300 = (i4 + 1);
      short[] tmp301_294 = paramDeflate.xb;
      tmp301_294[tmp301_300] = (short)(tmp301_294[tmp301_300] + 2);
      int tmp313_311 = n;
      short[] tmp313_308 = paramDeflate.xb;
      tmp313_308[tmp313_311] = (short)(tmp313_308[tmp313_311] - 1);
      i7 -= 2;
    }
    while (i7 > 0);
    for (i4 = n; i4 != 0; i4--)
    {
      i2 = paramDeflate.xb[i4];
      while (i2 != 0)
      {
        i1--;
        int i3 = paramDeflate.k[i1];
        if (i3 > this.k)
          continue;
        if (arrayOfShort1[(i3 * 2 + 1)] != i4)
        {
          Deflate tmp388_387 = paramDeflate;
          tmp388_387.rb = (int)(tmp388_387.rb + (i4 - arrayOfShort1[(i3 * 2 + 1)]) * arrayOfShort1[(i3 * 2)]);
          arrayOfShort1[(i3 * 2 + 1)] = (short)i4;
        }
        i2--;
      }
    }
  }

  void b(Deflate paramDeflate)
  {
    short[] arrayOfShort1 = this.b;
    short[] arrayOfShort2 = this.g.h;
    int m = this.g.b;
    int i2 = -1;
    paramDeflate.n = 0;
    paramDeflate.s = 573;
    for (int n = 0; n < m; n++)
      if (arrayOfShort1[(n * 2)] != 0)
      {
        int tmp73_71 = n;
        i2 = tmp73_71;
        paramDeflate.k[(++paramDeflate.n)] = tmp73_71;
        paramDeflate.m[n] = 0;
      }
      else
      {
        arrayOfShort1[(n * 2 + 1)] = 0;
      }
    while (paramDeflate.n < 2)
    {
      i2++;
      i3 = paramDeflate.k[(++paramDeflate.n)] = i2 < 2 ? i2 : 0;
      arrayOfShort1[(i3 * 2)] = 1;
      paramDeflate.m[i3] = 0;
      paramDeflate.rb -= 1;
      if (arrayOfShort2 == null)
        continue;
      paramDeflate.ob -= arrayOfShort2[(i3 * 2 + 1)];
    }
    this.k = i2;
    for (n = paramDeflate.n / 2; n >= 1; n--)
      paramDeflate.c(arrayOfShort1, n);
    int i3 = m;
    do
    {
      n = paramDeflate.k[1];
      paramDeflate.k[1] = paramDeflate.k[(paramDeflate.n--)];
      paramDeflate.c(arrayOfShort1, 1);
      int i1 = paramDeflate.k[1];
      paramDeflate.k[(--paramDeflate.s)] = n;
      paramDeflate.k[(--paramDeflate.s)] = i1;
      arrayOfShort1[(i3 * 2)] = (short)(arrayOfShort1[(n * 2)] + arrayOfShort1[(i1 * 2)]);
      paramDeflate.m[i3] = (byte)(Math.max(paramDeflate.m[n], paramDeflate.m[i1]) + 1);
      short tmp375_374 = (short)i3;
      arrayOfShort1[(i1 * 2 + 1)] = tmp375_374;
      arrayOfShort1[(n * 2 + 1)] = tmp375_374;
      paramDeflate.k[1] = (i3++);
      paramDeflate.c(arrayOfShort1, 1);
    }
    while (paramDeflate.n >= 2);
    paramDeflate.k[(--paramDeflate.s)] = paramDeflate.k[1];
    c(paramDeflate);
    b(arrayOfShort1, i2, paramDeflate.xb);
  }

  static void b(short[] paramArrayOfShort1, int paramInt, short[] paramArrayOfShort2)
  {
    short[] arrayOfShort = new short[16];
    int m = 0;
    for (int n = 1; n <= 15; n++)
    {
      short tmp33_32 = (short)(m + paramArrayOfShort2[(n - 1)] << 1);
      m = tmp33_32;
      arrayOfShort[n] = tmp33_32;
    }
    for (int i1 = 0; i1 <= paramInt; i1++)
    {
      int i2 = paramArrayOfShort1[(i1 * 2 + 1)];
      if (i2 == 0)
        continue;
      int tmp78_76 = i2;
      short[] tmp78_75 = arrayOfShort;
      short tmp80_79 = tmp78_75[tmp78_76];
      tmp78_75[tmp78_76] = (short)(tmp80_79 + 1);
      paramArrayOfShort1[(i1 * 2)] = (short)b(tmp80_79, i2);
    }
  }

  static int b(int paramInt1, int paramInt2)
  {
    int m = 0;
    do
    {
      m |= paramInt1 & 0x1;
      paramInt1 >>>= 1;
      m <<= 1;
      paramInt2--;
    }
    while (paramInt2 > 0);
    return m >>> 1;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.zlib.d
 * JD-Core Version:    0.6.0
 */