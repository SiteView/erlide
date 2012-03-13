package com.sshtools.zlib;

final class c
{
  private static final int[] u = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535 };
  static final int[] n = { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
  int m;
  int e;
  int q;
  int g;
  int[] j;
  int[] f = new int[1];
  int[] i = new int[1];
  g s;
  int k;
  int t;
  int c;
  int[] b = new int[4320];
  byte[] h;
  int d;
  int o;
  int p;
  Object l;
  long r;

  c(ZStream paramZStream, Object paramObject, int paramInt)
  {
    this.h = new byte[paramInt];
    this.d = paramInt;
    this.l = paramObject;
    this.m = 0;
    b(paramZStream, null);
  }

  void b(ZStream paramZStream, long[] paramArrayOfLong)
  {
    if (paramArrayOfLong != null)
      paramArrayOfLong[0] = this.r;
    if ((this.m == 4) || (this.m == 5))
      this.j = null;
    if (this.m == 6)
      this.s.b(paramZStream);
    this.m = 0;
    this.t = 0;
    this.c = 0;
    this.o = (this.p = 0);
    if (this.l != null)
      paramZStream.adler = (this.r = paramZStream.b.b(0L, null, 0, 0));
  }

  int c(ZStream paramZStream, int paramInt)
  {
    int i4 = paramZStream.next_in_index;
    int i5 = paramZStream.avail_in;
    int i2 = this.c;
    int i3 = this.t;
    int i6 = this.p;
    int i7 = i6 < this.o ? this.o - i6 - 1 : this.d - i6;
    while (true)
    {
      int i1;
      int[] arrayOfInt1;
      switch (this.m)
      {
      case 0:
        while (i3 < 3)
        {
          if (i5 != 0)
          {
            paramInt = 0;
          }
          else
          {
            this.c = i2;
            this.t = i3;
            paramZStream.avail_in = i5;
            paramZStream.total_in += i4 - paramZStream.next_in_index;
            paramZStream.next_in_index = i4;
            this.p = i6;
            return b(paramZStream, paramInt);
          }
          i5--;
          i2 |= (paramZStream.next_in[(i4++)] & 0xFF) << i3;
          i3 += 8;
        }
        i1 = i2 & 0x7;
        this.k = (i1 & 0x1);
        switch (i1 >>> 1)
        {
        case 0:
          i2 >>>= 3;
          i3 -= 3;
          i1 = i3 & 0x7;
          i2 >>>= i1;
          i3 -= i1;
          this.m = 1;
          break;
        case 1:
          arrayOfInt1 = new int[1];
          int[] arrayOfInt2 = new int[1];
          int[][] arrayOfInt4 = new int[1][];
          int[][] arrayOfInt6 = new int[1][];
          h.b(arrayOfInt1, arrayOfInt2, arrayOfInt4, arrayOfInt6, paramZStream);
          this.s = new g(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt4[0], arrayOfInt6[0], paramZStream);
          i2 >>>= 3;
          i3 -= 3;
          this.m = 6;
          break;
        case 2:
          i2 >>>= 3;
          i3 -= 3;
          this.m = 3;
          break;
        case 3:
          i2 >>>= 3;
          i3 -= 3;
          this.m = 9;
          paramZStream.msg = "invalid block type";
          paramInt = -3;
          this.c = i2;
          this.t = i3;
          paramZStream.avail_in = i5;
          paramZStream.total_in += i4 - paramZStream.next_in_index;
          paramZStream.next_in_index = i4;
          this.p = i6;
          return b(paramZStream, paramInt);
        }
        break;
      case 1:
        while (i3 < 32)
        {
          if (i5 != 0)
          {
            paramInt = 0;
          }
          else
          {
            this.c = i2;
            this.t = i3;
            paramZStream.avail_in = i5;
            paramZStream.total_in += i4 - paramZStream.next_in_index;
            paramZStream.next_in_index = i4;
            this.p = i6;
            return b(paramZStream, paramInt);
          }
          i5--;
          i2 |= (paramZStream.next_in[(i4++)] & 0xFF) << i3;
          i3 += 8;
        }
        if (((i2 ^ 0xFFFFFFFF) >>> 16 & 0xFFFF) != (i2 & 0xFFFF))
        {
          this.m = 9;
          paramZStream.msg = "invalid stored block lengths";
          paramInt = -3;
          this.c = i2;
          this.t = i3;
          paramZStream.avail_in = i5;
          paramZStream.total_in += i4 - paramZStream.next_in_index;
          paramZStream.next_in_index = i4;
          this.p = i6;
          return b(paramZStream, paramInt);
        }
        this.e = (i2 & 0xFFFF);
        i2 = i3 = 0;
        this.m = (this.k != 0 ? 7 : this.e != 0 ? 2 : 0);
        break;
      case 2:
        if (i5 == 0)
        {
          this.c = i2;
          this.t = i3;
          paramZStream.avail_in = i5;
          paramZStream.total_in += i4 - paramZStream.next_in_index;
          paramZStream.next_in_index = i4;
          this.p = i6;
          return b(paramZStream, paramInt);
        }
        if (i7 == 0)
        {
          if ((i6 == this.d) && (this.o != 0))
          {
            i6 = 0;
            i7 = i6 < this.o ? this.o - i6 - 1 : this.d - i6;
          }
          if (i7 == 0)
          {
            this.p = i6;
            paramInt = b(paramZStream, paramInt);
            i6 = this.p;
            i7 = i6 < this.o ? this.o - i6 - 1 : this.d - i6;
            if ((i6 == this.d) && (this.o != 0))
            {
              i6 = 0;
              i7 = i6 < this.o ? this.o - i6 - 1 : this.d - i6;
            }
            if (i7 == 0)
            {
              this.c = i2;
              this.t = i3;
              paramZStream.avail_in = i5;
              paramZStream.total_in += i4 - paramZStream.next_in_index;
              paramZStream.next_in_index = i4;
              this.p = i6;
              return b(paramZStream, paramInt);
            }
          }
        }
        paramInt = 0;
        i1 = this.e;
        if (i1 > i5)
          i1 = i5;
        if (i1 > i7)
          i1 = i7;
        System.arraycopy(paramZStream.next_in, i4, this.h, i6, i1);
        i4 += i1;
        i5 -= i1;
        i6 += i1;
        i7 -= i1;
        if (this.e -= i1 != 0)
          continue;
        this.m = (this.k != 0 ? 7 : 0);
        break;
      case 3:
        while (i3 < 14)
        {
          if (i5 != 0)
          {
            paramInt = 0;
          }
          else
          {
            this.c = i2;
            this.t = i3;
            paramZStream.avail_in = i5;
            paramZStream.total_in += i4 - paramZStream.next_in_index;
            paramZStream.next_in_index = i4;
            this.p = i6;
            return b(paramZStream, paramInt);
          }
          i5--;
          i2 |= (paramZStream.next_in[(i4++)] & 0xFF) << i3;
          i3 += 8;
        }
        this.q = (i1 = i2 & 0x3FFF);
        if (((i1 & 0x1F) > 29) || ((i1 >> 5 & 0x1F) > 29))
        {
          this.m = 9;
          paramZStream.msg = "too many length or distance symbols";
          paramInt = -3;
          this.c = i2;
          this.t = i3;
          paramZStream.avail_in = i5;
          paramZStream.total_in += i4 - paramZStream.next_in_index;
          paramZStream.next_in_index = i4;
          this.p = i6;
          return b(paramZStream, paramInt);
        }
        i1 = 258 + (i1 & 0x1F) + (i1 >> 5 & 0x1F);
        this.j = new int[i1];
        i2 >>>= 14;
        i3 -= 14;
        this.g = 0;
        this.m = 4;
      case 4:
        while (this.g < 4 + (this.q >>> 10))
        {
          while (i3 < 3)
          {
            if (i5 != 0)
            {
              paramInt = 0;
            }
            else
            {
              this.c = i2;
              this.t = i3;
              paramZStream.avail_in = i5;
              paramZStream.total_in += i4 - paramZStream.next_in_index;
              paramZStream.next_in_index = i4;
              this.p = i6;
              return b(paramZStream, paramInt);
            }
            i5--;
            i2 |= (paramZStream.next_in[(i4++)] & 0xFF) << i3;
            i3 += 8;
          }
          this.j[n[(this.g++)]] = (i2 & 0x7);
          i2 >>>= 3;
          i3 -= 3;
        }
        while (this.g < 19)
          this.j[n[(this.g++)]] = 0;
        this.f[0] = 7;
        i1 = h.b(this.j, this.f, this.i, this.b, paramZStream);
        if (i1 != 0)
        {
          paramInt = i1;
          if (paramInt == -3)
          {
            this.j = null;
            this.m = 9;
          }
          this.c = i2;
          this.t = i3;
          paramZStream.avail_in = i5;
          paramZStream.total_in += i4 - paramZStream.next_in_index;
          paramZStream.next_in_index = i4;
          this.p = i6;
          return b(paramZStream, paramInt);
        }
        this.g = 0;
        this.m = 5;
      case 5:
        while (true)
        {
          i1 = this.q;
          if (this.g >= 258 + (i1 & 0x1F) + (i1 >> 5 & 0x1F))
            break;
          i1 = this.f[0];
          while (i3 < i1)
          {
            if (i5 != 0)
            {
              paramInt = 0;
            }
            else
            {
              this.c = i2;
              this.t = i3;
              paramZStream.avail_in = i5;
              paramZStream.total_in += i4 - paramZStream.next_in_index;
              paramZStream.next_in_index = i4;
              this.p = i6;
              return b(paramZStream, paramInt);
            }
            i5--;
            i2 |= (paramZStream.next_in[(i4++)] & 0xFF) << i3;
            i3 += 8;
          }
          if (this.i[0] == -1);
          i1 = this.b[((this.i[0] + (i2 & u[i1])) * 3 + 1)];
          int i10 = this.b[((this.i[0] + (i2 & u[i1])) * 3 + 2)];
          if (i10 < 16)
          {
            i2 >>>= i1;
            i3 -= i1;
            this.j[(this.g++)] = i10;
          }
          else
          {
            int i8 = i10 == 18 ? 7 : i10 - 14;
            int i9 = i10 == 18 ? 11 : 3;
            while (i3 < i1 + i8)
            {
              if (i5 != 0)
              {
                paramInt = 0;
              }
              else
              {
                this.c = i2;
                this.t = i3;
                paramZStream.avail_in = i5;
                paramZStream.total_in += i4 - paramZStream.next_in_index;
                paramZStream.next_in_index = i4;
                this.p = i6;
                return b(paramZStream, paramInt);
              }
              i5--;
              i2 |= (paramZStream.next_in[(i4++)] & 0xFF) << i3;
              i3 += 8;
            }
            i2 >>>= i1;
            i3 -= i1;
            i9 += (i2 & u[i8]);
            i2 >>>= i8;
            i3 -= i8;
            i8 = this.g;
            i1 = this.q;
            if ((i8 + i9 > 258 + (i1 & 0x1F) + (i1 >> 5 & 0x1F)) || ((i10 == 16) && (i8 < 1)))
            {
              this.j = null;
              this.m = 9;
              paramZStream.msg = "invalid bit length repeat";
              paramInt = -3;
              this.c = i2;
              this.t = i3;
              paramZStream.avail_in = i5;
              paramZStream.total_in += i4 - paramZStream.next_in_index;
              paramZStream.next_in_index = i4;
              this.p = i6;
              return b(paramZStream, paramInt);
            }
            i10 = i10 == 16 ? this.j[(i8 - 1)] : 0;
            do
            {
              this.j[(i8++)] = i10;
              i9--;
            }
            while (i9 != 0);
            this.g = i8;
          }
        }
        this.i[0] = -1;
        arrayOfInt1 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt5 = new int[1];
        int[] arrayOfInt7 = new int[1];
        arrayOfInt1[0] = 9;
        arrayOfInt3[0] = 6;
        i1 = this.q;
        i1 = h.b(257 + (i1 & 0x1F), 1 + (i1 >> 5 & 0x1F), this.j, arrayOfInt1, arrayOfInt3, arrayOfInt5, arrayOfInt7, this.b, paramZStream);
        if (i1 != 0)
        {
          if (i1 == -3)
          {
            this.j = null;
            this.m = 9;
          }
          paramInt = i1;
          this.c = i2;
          this.t = i3;
          paramZStream.avail_in = i5;
          paramZStream.total_in += i4 - paramZStream.next_in_index;
          paramZStream.next_in_index = i4;
          this.p = i6;
          return b(paramZStream, paramInt);
        }
        this.s = new g(arrayOfInt1[0], arrayOfInt3[0], this.b, arrayOfInt5[0], this.b, arrayOfInt7[0], paramZStream);
        this.j = null;
        this.m = 6;
      case 6:
        this.c = i2;
        this.t = i3;
        paramZStream.avail_in = i5;
        paramZStream.total_in += i4 - paramZStream.next_in_index;
        paramZStream.next_in_index = i4;
        this.p = i6;
        if ((paramInt = this.s.b(this, paramZStream, paramInt)) != 1)
          return b(paramZStream, paramInt);
        paramInt = 0;
        this.s.b(paramZStream);
        i4 = paramZStream.next_in_index;
        i5 = paramZStream.avail_in;
        i2 = this.c;
        i3 = this.t;
        i6 = this.p;
        i7 = i6 < this.o ? this.o - i6 - 1 : this.d - i6;
        if (this.k != 0)
          break label2592;
        this.m = 0;
      case 7:
      case 8:
      case 9:
      }
    }
    label2592: this.m = 7;
    this.p = i6;
    paramInt = b(paramZStream, paramInt);
    i6 = this.p;
    i7 = i6 < this.o ? this.o - i6 - 1 : this.d - i6;
    if (this.o != this.p)
    {
      this.c = i2;
      this.t = i3;
      paramZStream.avail_in = i5;
      paramZStream.total_in += i4 - paramZStream.next_in_index;
      paramZStream.next_in_index = i4;
      this.p = i6;
      return b(paramZStream, paramInt);
    }
    this.m = 8;
    paramInt = 1;
    this.c = i2;
    this.t = i3;
    paramZStream.avail_in = i5;
    paramZStream.total_in += i4 - paramZStream.next_in_index;
    paramZStream.next_in_index = i4;
    this.p = i6;
    return b(paramZStream, paramInt);
    paramInt = -3;
    this.c = i2;
    this.t = i3;
    paramZStream.avail_in = i5;
    paramZStream.total_in += i4 - paramZStream.next_in_index;
    paramZStream.next_in_index = i4;
    this.p = i6;
    return b(paramZStream, paramInt);
    paramInt = -2;
    this.c = i2;
    this.t = i3;
    paramZStream.avail_in = i5;
    paramZStream.total_in += i4 - paramZStream.next_in_index;
    paramZStream.next_in_index = i4;
    this.p = i6;
    return b(paramZStream, paramInt);
  }

  void b(ZStream paramZStream)
  {
    b(paramZStream, null);
    this.h = null;
    this.b = null;
  }

  void b(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    System.arraycopy(paramArrayOfByte, paramInt1, this.h, 0, paramInt2);
    this.o = (this.p = paramInt2);
  }

  int b(ZStream paramZStream, int paramInt)
  {
    int i2 = paramZStream.next_out_index;
    int i3 = this.o;
    int i1 = (i3 <= this.p ? this.p : this.d) - i3;
    if (i1 > paramZStream.avail_out)
      i1 = paramZStream.avail_out;
    if ((i1 != 0) && (paramInt == -5))
      paramInt = 0;
    paramZStream.avail_out -= i1;
    paramZStream.total_out += i1;
    if (this.l != null)
      paramZStream.adler = (this.r = paramZStream.b.b(this.r, this.h, i3, i1));
    System.arraycopy(this.h, i3, paramZStream.next_out, i2, i1);
    i2 += i1;
    i3 += i1;
    if (i3 == this.d)
    {
      i3 = 0;
      if (this.p == this.d)
        this.p = 0;
      i1 = this.p - i3;
      if (i1 > paramZStream.avail_out)
        i1 = paramZStream.avail_out;
      if ((i1 != 0) && (paramInt == -5))
        paramInt = 0;
      paramZStream.avail_out -= i1;
      paramZStream.total_out += i1;
      if (this.l != null)
        paramZStream.adler = (this.r = paramZStream.b.b(this.r, this.h, i3, i1));
      System.arraycopy(this.h, i3, paramZStream.next_out, i2, i1);
      i2 += i1;
      i3 += i1;
    }
    paramZStream.next_out_index = i2;
    this.o = i3;
    return paramInt;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.zlib.c
 * JD-Core Version:    0.6.0
 */