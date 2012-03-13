package com.sshtools.zlib;

final class D
{
  int G;
  int A;
  long[] C = new long[1];
  long I;
  int F;
  int D;
  int H;
  B B;
  private static byte[] E = { 0, 0, -1, -1 };

  int B(ZStream paramZStream)
  {
    if ((paramZStream == null) || (paramZStream.B == null))
      return -2;
    paramZStream.total_in = (paramZStream.total_out = 0L);
    paramZStream.msg = null;
    paramZStream.B.G = (paramZStream.B.D != 0 ? 7 : 0);
    paramZStream.B.B.A(paramZStream, null);
    return 0;
  }

  int A(ZStream paramZStream)
  {
    if (this.B != null)
      this.B.A(paramZStream);
    this.B = null;
    return 0;
  }

  int B(ZStream paramZStream, int paramInt)
  {
    paramZStream.msg = null;
    this.B = null;
    this.D = 0;
    if (paramInt < 0)
    {
      paramInt = -paramInt;
      this.D = 1;
    }
    if ((paramInt < 8) || (paramInt > 15))
    {
      A(paramZStream);
      return -2;
    }
    this.H = paramInt;
    paramZStream.B.B = new B(paramZStream, paramZStream.B.D != 0 ? null : this, 1 << paramInt);
    B(paramZStream);
    return 0;
  }

  int A(ZStream paramZStream, int paramInt)
  {
    if ((paramZStream == null) || (paramZStream.B == null) || (paramZStream.next_in == null))
      return -2;
    paramInt = paramInt == 4 ? -5 : 0;
    int i = -5;
    while (true)
      switch (paramZStream.B.G)
      {
      case 0:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        if (((paramZStream.B.A = paramZStream.next_in[(paramZStream.next_in_index++)]) & 0xF) != 8)
        {
          paramZStream.B.G = 13;
          paramZStream.msg = "unknown compression method";
          paramZStream.B.F = 5;
          continue;
        }
        if ((paramZStream.B.A >> 4) + 8 > paramZStream.B.H)
        {
          paramZStream.B.G = 13;
          paramZStream.msg = "invalid window size";
          paramZStream.B.F = 5;
          continue;
        }
        paramZStream.B.G = 1;
      case 1:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        int j = paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF;
        if (((paramZStream.B.A << 8) + j) % 31 != 0)
        {
          paramZStream.B.G = 13;
          paramZStream.msg = "incorrect header check";
          paramZStream.B.F = 5;
          continue;
        }
        if ((j & 0x20) == 0)
        {
          paramZStream.B.G = 7;
          continue;
        }
        paramZStream.B.G = 2;
      case 2:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.B.I = ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 24 & 0xFF000000);
        paramZStream.B.G = 3;
      case 3:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.B.I += ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 16 & 0xFF0000);
        paramZStream.B.G = 4;
      case 4:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.B.I += ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 8 & 0xFF00);
        paramZStream.B.G = 5;
      case 5:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.B.I += (paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF);
        paramZStream.adler = paramZStream.B.I;
        paramZStream.B.G = 6;
        return 2;
      case 6:
        paramZStream.B.G = 13;
        paramZStream.msg = "need dictionary";
        paramZStream.B.F = 0;
        return -2;
      case 7:
        i = paramZStream.B.B.B(paramZStream, i);
        if (i == -3)
        {
          paramZStream.B.G = 13;
          paramZStream.B.F = 0;
          continue;
        }
        if (i == 0)
          i = paramInt;
        if (i != 1)
          return i;
        i = paramInt;
        paramZStream.B.B.A(paramZStream, paramZStream.B.C);
        if (paramZStream.B.D != 0)
        {
          paramZStream.B.G = 12;
          continue;
        }
        paramZStream.B.G = 8;
      case 8:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.B.I = ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 24 & 0xFF000000);
        paramZStream.B.G = 9;
      case 9:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.B.I += ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 16 & 0xFF0000);
        paramZStream.B.G = 10;
      case 10:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.B.I += ((paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF) << 8 & 0xFF00);
        paramZStream.B.G = 11;
      case 11:
        if (paramZStream.avail_in == 0)
          return i;
        i = paramInt;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        paramZStream.B.I += (paramZStream.next_in[(paramZStream.next_in_index++)] & 0xFF);
        if ((int)paramZStream.B.C[0] == (int)paramZStream.B.I)
          break label1176;
        paramZStream.B.G = 13;
        paramZStream.msg = "incorrect data check";
        paramZStream.B.F = 5;
      case 12:
      case 13:
      }
    label1176: paramZStream.B.G = 12;
    return 1;
    return -3;
    return -2;
  }

  int A(ZStream paramZStream, byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    int j = paramInt;
    if ((paramZStream == null) || (paramZStream.B == null) || (paramZStream.B.G != 6))
      return -2;
    if (paramZStream.A.A(1L, paramArrayOfByte, 0, paramInt) != paramZStream.adler)
      return -3;
    paramZStream.adler = paramZStream.A.A(0L, null, 0, 0);
    if (j >= 1 << paramZStream.B.H)
    {
      j = (1 << paramZStream.B.H) - 1;
      i = paramInt - j;
    }
    paramZStream.B.B.A(paramArrayOfByte, i, j);
    paramZStream.B.G = 7;
    return 0;
  }

  int C(ZStream paramZStream)
  {
    if ((paramZStream == null) || (paramZStream.B == null))
      return -2;
    if (paramZStream.B.G != 13)
    {
      paramZStream.B.G = 13;
      paramZStream.B.F = 0;
    }
    int i;
    if ((i = paramZStream.avail_in) == 0)
      return -5;
    int j = paramZStream.next_in_index;
    int k = paramZStream.B.F;
    while ((i != 0) && (k < 4))
    {
      if (paramZStream.next_in[j] == E[k])
        k++;
      else if (paramZStream.next_in[j] != 0)
        k = 0;
      else
        k = 4 - k;
      j++;
      i--;
    }
    paramZStream.total_in += j - paramZStream.next_in_index;
    paramZStream.next_in_index = j;
    paramZStream.avail_in = i;
    paramZStream.B.F = k;
    if (k != 4)
      return -3;
    long l1 = paramZStream.total_in;
    long l2 = paramZStream.total_out;
    B(paramZStream);
    paramZStream.total_in = l1;
    paramZStream.total_out = l2;
    paramZStream.B.G = 7;
    return 0;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.D
 * JD-Core Version:    0.6.0
 */