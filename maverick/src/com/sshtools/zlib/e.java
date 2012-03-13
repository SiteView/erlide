package com.sshtools.zlib;

final class e
{
  int h;
  int b;
  long[] d = new long[1];
  long j;
  int g;
  int e;
  int i;
  c c;
  private static byte[] f = { 0, 0, -1, -1 };

  int c(ZStream paramZStream)
  {
    if ((paramZStream == null) || (paramZStream.c == null))
      return -2;
    paramZStream.total_in = (paramZStream.total_out = 0L);
    paramZStream.msg = null;
    paramZStream.c.h = (paramZStream.c.e != 0 ? 7 : 0);
    paramZStream.c.c.b(paramZStream, null);
    return 0;
  }

  int b(ZStream paramZStream)
  {
    if (this.c != null)
      this.c.b(paramZStream);
    this.c = null;
    return 0;
  }

  int c(ZStream paramZStream, int paramInt)
  {
    paramZStream.msg = null;
    this.c = null;
    this.e = 0;
    if (paramInt < 0)
    {
      paramInt = -paramInt;
      this.e = 1;
    }
    if ((paramInt < 8) || (paramInt > 15))
    {
      b(paramZStream);
      return -2;
    }
    this.i = paramInt;
    paramZStream.c.c = new c(paramZStream, paramZStream.c.e != 0 ? null : this, 1 << paramInt);
    c(paramZStream);
    return 0;
  }

  int b(ZStream paramZStream, int paramInt)
  {
    if ((paramZStream == null) || (paramZStream.c == null) || (paramZStream.next_in == null))
      return -2;
    paramInt = paramInt == 4 ? -5 : 0;
    int k = -5;
    while (true)
      switch (paramZStream.c.h)
      {
      case 0:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        if (((paramZStream.c.b = paramZStream.next_in[(paramZStream.next_in_index++)]) & 0xF) != 8)
        {
          paramZStream.c.h = 13;
          paramZStream.msg = "unknown compression method";
          paramZStream.c.g = 5;
          continue;
        }
        if ((paramZStream.c.b >> 4) + 8 > paramZStream.c.i)
        {
          paramZStream.c.h = 13;
          paramZStream.msg = "invalid window size";
          paramZStream.c.g = 5;
          continue;
        }
        paramZStream.c.h = 1;
      case 1:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        int m = paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF;
        if (((paramZStream.c.b << 8) + m) % 31 != 0)
        {
          paramZStream.c.h = 13;
          paramZStream.msg = "incorrect header check";
          paramZStream.c.g = 5;
          continue;
        }
        if ((m & 0x20) == 0)
        {
          paramZStream.c.h = 7;
          continue;
        }
        paramZStream.c.h = 2;
      case 2:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.c.j = ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 24 & 0xFF000000);
        paramZStream.c.h = 3;
      case 3:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.c.j += ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 16 & 0xFF0000);
        paramZStream.c.h = 4;
      case 4:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.c.j += ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 8 & 0xFF00);
        paramZStream.c.h = 5;
      case 5:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.c.j += (paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF);
        paramZStream.adler = paramZStream.c.j;
        paramZStream.c.h = 6;
        return 2;
      case 6:
        paramZStream.c.h = 13;
        paramZStream.msg = "need dictionary";
        paramZStream.c.g = 0;
        return -2;
      case 7:
        k = paramZStream.c.c.c(paramZStream, k);
        if (k == -3)
        {
          paramZStream.c.h = 13;
          paramZStream.c.g = 0;
          continue;
        }
        if (k == 0)
          k = paramInt;
        if (k != 1)
          return k;
        k = paramInt;
        paramZStream.c.c.b(paramZStream, paramZStream.c.d);
        if (paramZStream.c.e != 0)
        {
          paramZStream.c.h = 12;
          continue;
        }
        paramZStream.c.h = 8;
      case 8:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.c.j = ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 24 & 0xFF000000);
        paramZStream.c.h = 9;
      case 9:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.c.j += ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 16 & 0xFF0000);
        paramZStream.c.h = 10;
      case 10:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.c.j += ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 8 & 0xFF00);
        paramZStream.c.h = 11;
      case 11:
        if (paramZStream.avail_in == 0)
          return k;
        k = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.c.j += (paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF);
        if ((int)paramZStream.c.d[0] == (int)paramZStream.c.j)
          break label1176;
        paramZStream.c.h = 13;
        paramZStream.msg = "incorrect data check";
        paramZStream.c.g = 5;
      case 12:
      case 13:
      }
    label1176: paramZStream.c.h = 12;
    return 1;
    return -3;
    return -2;
  }

  int b(ZStream paramZStream, byte[] paramArrayOfByte, int paramInt)
  {
    int k = 0;
    int m = paramInt;
    if ((paramZStream == null) || (paramZStream.c == null) || (paramZStream.c.h != 6))
      return -2;
    if (paramZStream.b.b(1L, paramArrayOfByte, 0, paramInt) != paramZStream.adler)
      return -3;
    paramZStream.adler = paramZStream.b.b(0L, null, 0, 0);
    if (m >= 1 << paramZStream.c.i)
    {
      m = (1 << paramZStream.c.i) - 1;
      k = paramInt - m;
    }
    paramZStream.c.c.b(paramArrayOfByte, k, m);
    paramZStream.c.h = 7;
    return 0;
  }

  int d(ZStream paramZStream)
  {
    if ((paramZStream == null) || (paramZStream.c == null))
      return -2;
    if (paramZStream.c.h != 13)
    {
      paramZStream.c.h = 13;
      paramZStream.c.g = 0;
    }
    int k;
    if ((k = paramZStream.avail_in) == 0)
      return -5;
    int m = paramZStream.next_in_index;
    int n = paramZStream.c.g;
    while ((k != 0) && (n < 4))
    {
      if (paramZStream.next_in[m] == f[n])
        n++;
      else if (paramZStream.next_in[m] != 0)
        n = 0;
      else
        n = 4 - n;
      m++;
      k--;
    }
    paramZStream.total_in += m - paramZStream.next_in_index;
    paramZStream.next_in_index = m;
    paramZStream.avail_in = k;
    paramZStream.c.g = n;
    if (n != 4)
      return -3;
    long l1 = paramZStream.total_in;
    long l2 = paramZStream.total_out;
    c(paramZStream);
    paramZStream.total_in = l1;
    paramZStream.total_out = l2;
    paramZStream.c.h = 7;
    return 0;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.zlib.e
 * JD-Core Version:    0.6.0
 */