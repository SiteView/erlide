package com.sshtools.zlib;

final class g
{
  private static final int[] c = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535 };
  int j = 0;
  int k;
  int[] p;
  int h = 0;
  int n;
  int o;
  int f;
  int m;
  byte i;
  byte e;
  int[] l;
  int d;
  int[] g;
  int b;

  g(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, ZStream paramZStream)
  {
    this.i = (byte)paramInt1;
    this.e = (byte)paramInt2;
    this.l = paramArrayOfInt1;
    this.d = paramInt3;
    this.g = paramArrayOfInt2;
    this.b = paramInt4;
  }

  g(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, ZStream paramZStream)
  {
    this.i = (byte)paramInt1;
    this.e = (byte)paramInt2;
    this.l = paramArrayOfInt1;
    this.d = 0;
    this.g = paramArrayOfInt2;
    this.b = 0;
  }

  int b(c paramc, ZStream paramZStream, int paramInt)
  {
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    i6 = paramZStream.next_in_index;
    int i7 = paramZStream.avail_in;
    i4 = paramc.c;
    i5 = paramc.t;
    int i8 = paramc.p;
    int i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
    while (true)
    {
      int i1;
      int i2;
      int i3;
      switch (this.j)
      {
      case 0:
        if ((i9 >= 258) && (i7 >= 10))
        {
          paramc.c = i4;
          paramc.t = i5;
          paramZStream.avail_in = i7;
          paramZStream.total_in += i6 - paramZStream.next_in_index;
          paramZStream.next_in_index = i6;
          paramc.p = i8;
          paramInt = b(this.i, this.e, this.l, this.d, this.g, this.b, paramc, paramZStream);
          i6 = paramZStream.next_in_index;
          i7 = paramZStream.avail_in;
          i4 = paramc.c;
          i5 = paramc.t;
          i8 = paramc.p;
          i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
          if (paramInt != 0)
          {
            this.j = (paramInt == 1 ? 7 : 9);
            continue;
          }
        }
        this.n = this.i;
        this.p = this.l;
        this.h = this.d;
        this.j = 1;
      case 1:
        i1 = this.n;
        while (i5 < i1)
        {
          if (i7 != 0)
          {
            paramInt = 0;
          }
          else
          {
            paramc.c = i4;
            paramc.t = i5;
            paramZStream.avail_in = i7;
            paramZStream.total_in += i6 - paramZStream.next_in_index;
            paramZStream.next_in_index = i6;
            paramc.p = i8;
            return paramc.b(paramZStream, paramInt);
          }
          i7--;
          i4 |= (paramZStream.next_in[(i6++)] & 0xFF) << i5;
          i5 += 8;
        }
        i2 = (this.h + (i4 & c[i1])) * 3;
        i4 >>>= this.p[(i2 + 1)];
        i5 -= this.p[(i2 + 1)];
        i3 = this.p[i2];
        if (i3 == 0)
        {
          this.o = this.p[(i2 + 2)];
          this.j = 6;
          continue;
        }
        if ((i3 & 0x10) != 0)
        {
          this.f = (i3 & 0xF);
          this.k = this.p[(i2 + 2)];
          this.j = 2;
          continue;
        }
        if ((i3 & 0x40) == 0)
        {
          this.n = i3;
          this.h = (i2 / 3 + this.p[(i2 + 2)]);
          continue;
        }
        if ((i3 & 0x20) != 0)
        {
          this.j = 7;
          continue;
        }
        this.j = 9;
        paramZStream.msg = "invalid literal/length code";
        paramInt = -3;
        paramc.c = i4;
        paramc.t = i5;
        paramZStream.avail_in = i7;
        paramZStream.total_in += i6 - paramZStream.next_in_index;
        paramZStream.next_in_index = i6;
        paramc.p = i8;
        return paramc.b(paramZStream, paramInt);
      case 2:
        i1 = this.f;
        while (i5 < i1)
        {
          if (i7 != 0)
          {
            paramInt = 0;
          }
          else
          {
            paramc.c = i4;
            paramc.t = i5;
            paramZStream.avail_in = i7;
            paramZStream.total_in += i6 - paramZStream.next_in_index;
            paramZStream.next_in_index = i6;
            paramc.p = i8;
            return paramc.b(paramZStream, paramInt);
          }
          i7--;
          i4 |= (paramZStream.next_in[(i6++)] & 0xFF) << i5;
          i5 += 8;
        }
        this.k += (i4 & c[i1]);
        i4 >>= i1;
        i5 -= i1;
        this.n = this.e;
        this.p = this.g;
        this.h = this.b;
        this.j = 3;
      case 3:
        i1 = this.n;
        while (i5 < i1)
        {
          if (i7 != 0)
          {
            paramInt = 0;
          }
          else
          {
            paramc.c = i4;
            paramc.t = i5;
            paramZStream.avail_in = i7;
            paramZStream.total_in += i6 - paramZStream.next_in_index;
            paramZStream.next_in_index = i6;
            paramc.p = i8;
            return paramc.b(paramZStream, paramInt);
          }
          i7--;
          i4 |= (paramZStream.next_in[(i6++)] & 0xFF) << i5;
          i5 += 8;
        }
        i2 = (this.h + (i4 & c[i1])) * 3;
        i4 >>= this.p[(i2 + 1)];
        i5 -= this.p[(i2 + 1)];
        i3 = this.p[i2];
        if ((i3 & 0x10) != 0)
        {
          this.f = (i3 & 0xF);
          this.m = this.p[(i2 + 2)];
          this.j = 4;
          continue;
        }
        if ((i3 & 0x40) == 0)
        {
          this.n = i3;
          this.h = (i2 / 3 + this.p[(i2 + 2)]);
          continue;
        }
        this.j = 9;
        paramZStream.msg = "invalid distance code";
        paramInt = -3;
        paramc.c = i4;
        paramc.t = i5;
        paramZStream.avail_in = i7;
        paramZStream.total_in += i6 - paramZStream.next_in_index;
        paramZStream.next_in_index = i6;
        paramc.p = i8;
        return paramc.b(paramZStream, paramInt);
      case 4:
        i1 = this.f;
        while (i5 < i1)
        {
          if (i7 != 0)
          {
            paramInt = 0;
          }
          else
          {
            paramc.c = i4;
            paramc.t = i5;
            paramZStream.avail_in = i7;
            paramZStream.total_in += i6 - paramZStream.next_in_index;
            paramZStream.next_in_index = i6;
            paramc.p = i8;
            return paramc.b(paramZStream, paramInt);
          }
          i7--;
          i4 |= (paramZStream.next_in[(i6++)] & 0xFF) << i5;
          i5 += 8;
        }
        this.m += (i4 & c[i1]);
        i4 >>= i1;
        i5 -= i1;
        this.j = 5;
      case 5:
        int i10 = i8 - this.m;
        while (i10 < 0)
          i10 += paramc.d;
        while (this.k != 0)
        {
          if (i9 == 0)
          {
            if ((i8 == paramc.d) && (paramc.o != 0))
            {
              i8 = 0;
              i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
            }
            if (i9 == 0)
            {
              paramc.p = i8;
              paramInt = paramc.b(paramZStream, paramInt);
              i8 = paramc.p;
              i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
              if ((i8 == paramc.d) && (paramc.o != 0))
              {
                i8 = 0;
                i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
              }
              if (i9 == 0)
              {
                paramc.c = i4;
                paramc.t = i5;
                paramZStream.avail_in = i7;
                paramZStream.total_in += i6 - paramZStream.next_in_index;
                paramZStream.next_in_index = i6;
                paramc.p = i8;
                return paramc.b(paramZStream, paramInt);
              }
            }
          }
          paramc.h[(i8++)] = paramc.h[(i10++)];
          i9--;
          if (i10 == paramc.d)
            i10 = 0;
          this.k -= 1;
        }
        this.j = 0;
        break;
      case 6:
        if (i9 == 0)
        {
          if ((i8 == paramc.d) && (paramc.o != 0))
          {
            i8 = 0;
            i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
          }
          if (i9 == 0)
          {
            paramc.p = i8;
            paramInt = paramc.b(paramZStream, paramInt);
            i8 = paramc.p;
            i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
            if ((i8 == paramc.d) && (paramc.o != 0))
            {
              i8 = 0;
              i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
            }
            if (i9 == 0)
            {
              paramc.c = i4;
              paramc.t = i5;
              paramZStream.avail_in = i7;
              paramZStream.total_in += i6 - paramZStream.next_in_index;
              paramZStream.next_in_index = i6;
              paramc.p = i8;
              return paramc.b(paramZStream, paramInt);
            }
          }
        }
        paramInt = 0;
        paramc.h[(i8++)] = (byte)this.o;
        i9--;
        this.j = 0;
      case 7:
      case 8:
      case 9:
      }
    }
    if (i5 > 7)
    {
      i5 -= 8;
      i7++;
      i6--;
    }
    paramc.p = i8;
    paramInt = paramc.b(paramZStream, paramInt);
    i8 = paramc.p;
    i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
    if (paramc.o != paramc.p)
    {
      paramc.c = i4;
      paramc.t = i5;
      paramZStream.avail_in = i7;
      paramZStream.total_in += i6 - paramZStream.next_in_index;
      paramZStream.next_in_index = i6;
      paramc.p = i8;
      return paramc.b(paramZStream, paramInt);
    }
    this.j = 8;
    paramInt = 1;
    paramc.c = i4;
    paramc.t = i5;
    paramZStream.avail_in = i7;
    paramZStream.total_in += i6 - paramZStream.next_in_index;
    paramZStream.next_in_index = i6;
    paramc.p = i8;
    return paramc.b(paramZStream, paramInt);
    paramInt = -3;
    paramc.c = i4;
    paramc.t = i5;
    paramZStream.avail_in = i7;
    paramZStream.total_in += i6 - paramZStream.next_in_index;
    paramZStream.next_in_index = i6;
    paramc.p = i8;
    return paramc.b(paramZStream, paramInt);
    paramInt = -2;
    paramc.c = i4;
    paramc.t = i5;
    paramZStream.avail_in = i7;
    paramZStream.total_in += i6 - paramZStream.next_in_index;
    paramZStream.next_in_index = i6;
    paramc.p = i8;
    return paramc.b(paramZStream, paramInt);
  }

  void b(ZStream paramZStream)
  {
  }

  int b(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, c paramc, ZStream paramZStream)
  {
    int i6 = paramZStream.next_in_index;
    int i7 = paramZStream.avail_in;
    int i4 = paramc.c;
    int i5 = paramc.t;
    int i8 = paramc.p;
    int i9 = i8 < paramc.o ? paramc.o - i8 - 1 : paramc.d - i8;
    int i10 = c[paramInt1];
    int i11 = c[paramInt2];
    label1365: 
    do
    {
      while (i5 < 20)
      {
        i7--;
        i4 |= (paramZStream.next_in[(i6++)] & 0xFF) << i5;
        i5 += 8;
      }
      int i1 = i4 & i10;
      int[] arrayOfInt = paramArrayOfInt1;
      int i2 = paramInt3;
      int i3;
      if ((i3 = arrayOfInt[((i2 + i1) * 3)]) == 0)
      {
        i4 >>= arrayOfInt[((i2 + i1) * 3 + 1)];
        i5 -= arrayOfInt[((i2 + i1) * 3 + 1)];
        paramc.h[(i8++)] = (byte)arrayOfInt[((i2 + i1) * 3 + 2)];
        i9--;
      }
      else
      {
        while (true)
        {
          i4 >>= arrayOfInt[((i2 + i1) * 3 + 1)];
          i5 -= arrayOfInt[((i2 + i1) * 3 + 1)];
          if ((i3 & 0x10) != 0)
          {
            i3 &= 15;
            i12 = arrayOfInt[((i2 + i1) * 3 + 2)] + (i4 & c[i3]);
            i4 >>= i3;
            i5 -= i3;
            while (i5 < 15)
            {
              i7--;
              i4 |= (paramZStream.next_in[(i6++)] & 0xFF) << i5;
              i5 += 8;
            }
            i1 = i4 & i11;
            arrayOfInt = paramArrayOfInt2;
            i2 = paramInt4;
            for (i3 = arrayOfInt[((i2 + i1) * 3)]; ; i3 = arrayOfInt[((i2 + i1) * 3)])
            {
              i4 >>= arrayOfInt[((i2 + i1) * 3 + 1)];
              i5 -= arrayOfInt[((i2 + i1) * 3 + 1)];
              if ((i3 & 0x10) != 0)
              {
                i3 &= 15;
                while (i5 < i3)
                {
                  i7--;
                  i4 |= (paramZStream.next_in[(i6++)] & 0xFF) << i5;
                  i5 += 8;
                }
                int i13 = arrayOfInt[((i2 + i1) * 3 + 2)] + (i4 & c[i3]);
                i4 >>= i3;
                i5 -= i3;
                i9 -= i12;
                int i14;
                if (i8 >= i13)
                {
                  i14 = i8 - i13;
                  if ((i8 - i14 > 0) && (2 > i8 - i14))
                  {
                    paramc.h[(i8++)] = paramc.h[(i14++)];
                    i12--;
                    paramc.h[(i8++)] = paramc.h[(i14++)];
                    i12--;
                  }
                  else
                  {
                    System.arraycopy(paramc.h, i14, paramc.h, i8, 2);
                    i8 += 2;
                    i14 += 2;
                    i12 -= 2;
                  }
                }
                else
                {
                  i14 = i8 - i13;
                  do
                    i14 += paramc.d;
                  while (i14 < 0);
                  i3 = paramc.d - i14;
                  if (i12 > i3)
                  {
                    i12 -= i3;
                    if ((i8 - i14 > 0) && (i3 > i8 - i14))
                    {
                      do
                      {
                        paramc.h[(i8++)] = paramc.h[(i14++)];
                        i3--;
                      }
                      while (i3 != 0);
                    }
                    else
                    {
                      System.arraycopy(paramc.h, i14, paramc.h, i8, i3);
                      i8 += i3;
                      i14 += i3;
                      i3 = 0;
                    }
                    i14 = 0;
                  }
                }
                if ((i8 - i14 > 0) && (i12 > i8 - i14))
                {
                  do
                  {
                    paramc.h[(i8++)] = paramc.h[(i14++)];
                    i12--;
                  }
                  while (i12 != 0);
                  break label1365;
                }
                System.arraycopy(paramc.h, i14, paramc.h, i8, i12);
                i8 += i12;
                i14 += i12;
                i12 = 0;
                break label1365;
              }
              if ((i3 & 0x40) != 0)
                break;
              i1 += arrayOfInt[((i2 + i1) * 3 + 2)];
              i1 += (i4 & c[i3]);
            }
            paramZStream.msg = "invalid distance code";
            i12 = paramZStream.avail_in - i7;
            i12 = i5 >> 3 < i12 ? i5 >> 3 : i12;
            i7 += i12;
            i6 -= i12;
            i5 -= (i12 << 3);
            paramc.c = i4;
            paramc.t = i5;
            paramZStream.avail_in = i7;
            paramZStream.total_in += i6 - paramZStream.next_in_index;
            paramZStream.next_in_index = i6;
            paramc.p = i8;
            return -3;
          }
          if ((i3 & 0x40) != 0)
            break;
          i1 += arrayOfInt[((i2 + i1) * 3 + 2)];
          i1 += (i4 & c[i3]);
          if ((i3 = arrayOfInt[((i2 + i1) * 3)]) != 0)
            continue;
          i4 >>= arrayOfInt[((i2 + i1) * 3 + 1)];
          i5 -= arrayOfInt[((i2 + i1) * 3 + 1)];
          paramc.h[(i8++)] = (byte)arrayOfInt[((i2 + i1) * 3 + 2)];
          i9--;
          break label1365;
        }
        if ((i3 & 0x20) != 0)
        {
          i12 = paramZStream.avail_in - i7;
          i12 = i5 >> 3 < i12 ? i5 >> 3 : i12;
          i7 += i12;
          i6 -= i12;
          i5 -= (i12 << 3);
          paramc.c = i4;
          paramc.t = i5;
          paramZStream.avail_in = i7;
          paramZStream.total_in += i6 - paramZStream.next_in_index;
          paramZStream.next_in_index = i6;
          paramc.p = i8;
          return 1;
        }
        paramZStream.msg = "invalid literal/length code";
        i12 = paramZStream.avail_in - i7;
        i12 = i5 >> 3 < i12 ? i5 >> 3 : i12;
        i7 += i12;
        i6 -= i12;
        i5 -= (i12 << 3);
        paramc.c = i4;
        paramc.t = i5;
        paramZStream.avail_in = i7;
        paramZStream.total_in += i6 - paramZStream.next_in_index;
        paramZStream.next_in_index = i6;
        paramc.p = i8;
        return -3;
      }
    }
    while ((i9 >= 258) && (i7 >= 10));
    int i12 = paramZStream.avail_in - i7;
    i12 = i5 >> 3 < i12 ? i5 >> 3 : i12;
    i7 += i12;
    i6 -= i12;
    i5 -= (i12 << 3);
    paramc.c = i4;
    paramc.t = i5;
    paramZStream.avail_in = i7;
    paramZStream.total_in += i6 - paramZStream.next_in_index;
    paramZStream.next_in_index = i6;
    paramc.p = i8;
    return 0;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.zlib.g
 * JD-Core Version:    0.6.0
 */