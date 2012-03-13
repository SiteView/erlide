package com.sshtools.zlib;

final class F
{
  private static final int[] B = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535 };
  int I = 0;
  int J;
  int[] O;
  int G = 0;
  int M;
  int N;
  int E;
  int L;
  byte H;
  byte D;
  int[] K;
  int C;
  int[] F;
  int A;

  F(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, ZStream paramZStream)
  {
    this.H = (byte)paramInt1;
    this.D = (byte)paramInt2;
    this.K = paramArrayOfInt1;
    this.C = paramInt3;
    this.F = paramArrayOfInt2;
    this.A = paramInt4;
  }

  F(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, ZStream paramZStream)
  {
    this.H = (byte)paramInt1;
    this.D = (byte)paramInt2;
    this.K = paramArrayOfInt1;
    this.C = 0;
    this.F = paramArrayOfInt2;
    this.A = 0;
  }

  int A(B paramB, ZStream paramZStream, int paramInt)
  {
    int m = 0;
    int n = 0;
    int i1 = 0;
    i1 = paramZStream.next_in_index;
    int i2 = paramZStream.avail_in;
    m = paramB.B;
    n = paramB.S;
    int i3 = paramB.O;
    int i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
    while (true)
    {
      int i;
      int j;
      int k;
      switch (this.I)
      {
      case 0:
        if ((i4 >= 258) && (i2 >= 10))
        {
          paramB.B = m;
          paramB.S = n;
          paramZStream.avail_in = i2;
          paramZStream.total_in += i1 - paramZStream.next_in_index;
          paramZStream.next_in_index = i1;
          paramB.O = i3;
          paramInt = A(this.H, this.D, this.K, this.C, this.F, this.A, paramB, paramZStream);
          i1 = paramZStream.next_in_index;
          i2 = paramZStream.avail_in;
          m = paramB.B;
          n = paramB.S;
          i3 = paramB.O;
          i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
          if (paramInt != 0)
          {
            this.I = (paramInt == 1 ? 7 : 9);
            continue;
          }
        }
        this.M = this.H;
        this.O = this.K;
        this.G = this.C;
        this.I = 1;
      case 1:
        i = this.M;
        while (n < i)
        {
          if (i2 != 0)
          {
            paramInt = 0;
          }
          else
          {
            paramB.B = m;
            paramB.S = n;
            paramZStream.avail_in = i2;
            paramZStream.total_in += i1 - paramZStream.next_in_index;
            paramZStream.next_in_index = i1;
            paramB.O = i3;
            return paramB.A(paramZStream, paramInt);
          }
          i2--;
          m |= (paramZStream.next_in[(i1++)] & 0xFF) << n;
          n += 8;
        }
        j = (this.G + (m & B[i])) * 3;
        m >>>= this.O[(j + 1)];
        n -= this.O[(j + 1)];
        k = this.O[j];
        if (k == 0)
        {
          this.N = this.O[(j + 2)];
          this.I = 6;
          continue;
        }
        if ((k & 0x10) != 0)
        {
          this.E = (k & 0xF);
          this.J = this.O[(j + 2)];
          this.I = 2;
          continue;
        }
        if ((k & 0x40) == 0)
        {
          this.M = k;
          this.G = (j / 3 + this.O[(j + 2)]);
          continue;
        }
        if ((k & 0x20) != 0)
        {
          this.I = 7;
          continue;
        }
        this.I = 9;
        paramZStream.msg = "invalid literal/length code";
        paramInt = -3;
        paramB.B = m;
        paramB.S = n;
        paramZStream.avail_in = i2;
        paramZStream.total_in += i1 - paramZStream.next_in_index;
        paramZStream.next_in_index = i1;
        paramB.O = i3;
        return paramB.A(paramZStream, paramInt);
      case 2:
        i = this.E;
        while (n < i)
        {
          if (i2 != 0)
          {
            paramInt = 0;
          }
          else
          {
            paramB.B = m;
            paramB.S = n;
            paramZStream.avail_in = i2;
            paramZStream.total_in += i1 - paramZStream.next_in_index;
            paramZStream.next_in_index = i1;
            paramB.O = i3;
            return paramB.A(paramZStream, paramInt);
          }
          i2--;
          m |= (paramZStream.next_in[(i1++)] & 0xFF) << n;
          n += 8;
        }
        this.J += (m & B[i]);
        m >>= i;
        n -= i;
        this.M = this.D;
        this.O = this.F;
        this.G = this.A;
        this.I = 3;
      case 3:
        i = this.M;
        while (n < i)
        {
          if (i2 != 0)
          {
            paramInt = 0;
          }
          else
          {
            paramB.B = m;
            paramB.S = n;
            paramZStream.avail_in = i2;
            paramZStream.total_in += i1 - paramZStream.next_in_index;
            paramZStream.next_in_index = i1;
            paramB.O = i3;
            return paramB.A(paramZStream, paramInt);
          }
          i2--;
          m |= (paramZStream.next_in[(i1++)] & 0xFF) << n;
          n += 8;
        }
        j = (this.G + (m & B[i])) * 3;
        m >>= this.O[(j + 1)];
        n -= this.O[(j + 1)];
        k = this.O[j];
        if ((k & 0x10) != 0)
        {
          this.E = (k & 0xF);
          this.L = this.O[(j + 2)];
          this.I = 4;
          continue;
        }
        if ((k & 0x40) == 0)
        {
          this.M = k;
          this.G = (j / 3 + this.O[(j + 2)]);
          continue;
        }
        this.I = 9;
        paramZStream.msg = "invalid distance code";
        paramInt = -3;
        paramB.B = m;
        paramB.S = n;
        paramZStream.avail_in = i2;
        paramZStream.total_in += i1 - paramZStream.next_in_index;
        paramZStream.next_in_index = i1;
        paramB.O = i3;
        return paramB.A(paramZStream, paramInt);
      case 4:
        i = this.E;
        while (n < i)
        {
          if (i2 != 0)
          {
            paramInt = 0;
          }
          else
          {
            paramB.B = m;
            paramB.S = n;
            paramZStream.avail_in = i2;
            paramZStream.total_in += i1 - paramZStream.next_in_index;
            paramZStream.next_in_index = i1;
            paramB.O = i3;
            return paramB.A(paramZStream, paramInt);
          }
          i2--;
          m |= (paramZStream.next_in[(i1++)] & 0xFF) << n;
          n += 8;
        }
        this.L += (m & B[i]);
        m >>= i;
        n -= i;
        this.I = 5;
      case 5:
        int i5 = i3 - this.L;
        while (i5 < 0)
          i5 += paramB.C;
        while (this.J != 0)
        {
          if (i4 == 0)
          {
            if ((i3 == paramB.C) && (paramB.N != 0))
            {
              i3 = 0;
              i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
            }
            if (i4 == 0)
            {
              paramB.O = i3;
              paramInt = paramB.A(paramZStream, paramInt);
              i3 = paramB.O;
              i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
              if ((i3 == paramB.C) && (paramB.N != 0))
              {
                i3 = 0;
                i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
              }
              if (i4 == 0)
              {
                paramB.B = m;
                paramB.S = n;
                paramZStream.avail_in = i2;
                paramZStream.total_in += i1 - paramZStream.next_in_index;
                paramZStream.next_in_index = i1;
                paramB.O = i3;
                return paramB.A(paramZStream, paramInt);
              }
            }
          }
          paramB.G[(i3++)] = paramB.G[(i5++)];
          i4--;
          if (i5 == paramB.C)
            i5 = 0;
          this.J -= 1;
        }
        this.I = 0;
        break;
      case 6:
        if (i4 == 0)
        {
          if ((i3 == paramB.C) && (paramB.N != 0))
          {
            i3 = 0;
            i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
          }
          if (i4 == 0)
          {
            paramB.O = i3;
            paramInt = paramB.A(paramZStream, paramInt);
            i3 = paramB.O;
            i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
            if ((i3 == paramB.C) && (paramB.N != 0))
            {
              i3 = 0;
              i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
            }
            if (i4 == 0)
            {
              paramB.B = m;
              paramB.S = n;
              paramZStream.avail_in = i2;
              paramZStream.total_in += i1 - paramZStream.next_in_index;
              paramZStream.next_in_index = i1;
              paramB.O = i3;
              return paramB.A(paramZStream, paramInt);
            }
          }
        }
        paramInt = 0;
        paramB.G[(i3++)] = (byte)this.N;
        i4--;
        this.I = 0;
      case 7:
      case 8:
      case 9:
      }
    }
    if (n > 7)
    {
      n -= 8;
      i2++;
      i1--;
    }
    paramB.O = i3;
    paramInt = paramB.A(paramZStream, paramInt);
    i3 = paramB.O;
    i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
    if (paramB.N != paramB.O)
    {
      paramB.B = m;
      paramB.S = n;
      paramZStream.avail_in = i2;
      paramZStream.total_in += i1 - paramZStream.next_in_index;
      paramZStream.next_in_index = i1;
      paramB.O = i3;
      return paramB.A(paramZStream, paramInt);
    }
    this.I = 8;
    paramInt = 1;
    paramB.B = m;
    paramB.S = n;
    paramZStream.avail_in = i2;
    paramZStream.total_in += i1 - paramZStream.next_in_index;
    paramZStream.next_in_index = i1;
    paramB.O = i3;
    return paramB.A(paramZStream, paramInt);
    paramInt = -3;
    paramB.B = m;
    paramB.S = n;
    paramZStream.avail_in = i2;
    paramZStream.total_in += i1 - paramZStream.next_in_index;
    paramZStream.next_in_index = i1;
    paramB.O = i3;
    return paramB.A(paramZStream, paramInt);
    paramInt = -2;
    paramB.B = m;
    paramB.S = n;
    paramZStream.avail_in = i2;
    paramZStream.total_in += i1 - paramZStream.next_in_index;
    paramZStream.next_in_index = i1;
    paramB.O = i3;
    return paramB.A(paramZStream, paramInt);
  }

  void A(ZStream paramZStream)
  {
  }

  int A(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, B paramB, ZStream paramZStream)
  {
    int i1 = paramZStream.next_in_index;
    int i2 = paramZStream.avail_in;
    int m = paramB.B;
    int n = paramB.S;
    int i3 = paramB.O;
    int i4 = i3 < paramB.N ? paramB.N - i3 - 1 : paramB.C - i3;
    int i5 = B[paramInt1];
    int i6 = B[paramInt2];
    label1365: 
    do
    {
      while (n < 20)
      {
        i2--;
        m |= (paramZStream.next_in[(i1++)] & 0xFF) << n;
        n += 8;
      }
      int i = m & i5;
      int[] arrayOfInt = paramArrayOfInt1;
      int j = paramInt3;
      int k;
      if ((k = arrayOfInt[((j + i) * 3)]) == 0)
      {
        m >>= arrayOfInt[((j + i) * 3 + 1)];
        n -= arrayOfInt[((j + i) * 3 + 1)];
        paramB.G[(i3++)] = (byte)arrayOfInt[((j + i) * 3 + 2)];
        i4--;
      }
      else
      {
        while (true)
        {
          m >>= arrayOfInt[((j + i) * 3 + 1)];
          n -= arrayOfInt[((j + i) * 3 + 1)];
          if ((k & 0x10) != 0)
          {
            k &= 15;
            i7 = arrayOfInt[((j + i) * 3 + 2)] + (m & B[k]);
            m >>= k;
            n -= k;
            while (n < 15)
            {
              i2--;
              m |= (paramZStream.next_in[(i1++)] & 0xFF) << n;
              n += 8;
            }
            i = m & i6;
            arrayOfInt = paramArrayOfInt2;
            j = paramInt4;
            for (k = arrayOfInt[((j + i) * 3)]; ; k = arrayOfInt[((j + i) * 3)])
            {
              m >>= arrayOfInt[((j + i) * 3 + 1)];
              n -= arrayOfInt[((j + i) * 3 + 1)];
              if ((k & 0x10) != 0)
              {
                k &= 15;
                while (n < k)
                {
                  i2--;
                  m |= (paramZStream.next_in[(i1++)] & 0xFF) << n;
                  n += 8;
                }
                int i8 = arrayOfInt[((j + i) * 3 + 2)] + (m & B[k]);
                m >>= k;
                n -= k;
                i4 -= i7;
                int i9;
                if (i3 >= i8)
                {
                  i9 = i3 - i8;
                  if ((i3 - i9 > 0) && (2 > i3 - i9))
                  {
                    paramB.G[(i3++)] = paramB.G[(i9++)];
                    i7--;
                    paramB.G[(i3++)] = paramB.G[(i9++)];
                    i7--;
                  }
                  else
                  {
                    System.arraycopy(paramB.G, i9, paramB.G, i3, 2);
                    i3 += 2;
                    i9 += 2;
                    i7 -= 2;
                  }
                }
                else
                {
                  i9 = i3 - i8;
                  do
                    i9 += paramB.C;
                  while (i9 < 0);
                  k = paramB.C - i9;
                  if (i7 > k)
                  {
                    i7 -= k;
                    if ((i3 - i9 > 0) && (k > i3 - i9))
                    {
                      do
                      {
                        paramB.G[(i3++)] = paramB.G[(i9++)];
                        k--;
                      }
                      while (k != 0);
                    }
                    else
                    {
                      System.arraycopy(paramB.G, i9, paramB.G, i3, k);
                      i3 += k;
                      i9 += k;
                      k = 0;
                    }
                    i9 = 0;
                  }
                }
                if ((i3 - i9 > 0) && (i7 > i3 - i9))
                {
                  do
                  {
                    paramB.G[(i3++)] = paramB.G[(i9++)];
                    i7--;
                  }
                  while (i7 != 0);
                  break label1365;
                }
                System.arraycopy(paramB.G, i9, paramB.G, i3, i7);
                i3 += i7;
                i9 += i7;
                i7 = 0;
                break label1365;
              }
              if ((k & 0x40) != 0)
                break;
              i += arrayOfInt[((j + i) * 3 + 2)];
              i += (m & B[k]);
            }
            paramZStream.msg = "invalid distance code";
            i7 = paramZStream.avail_in - i2;
            i7 = n >> 3 < i7 ? n >> 3 : i7;
            i2 += i7;
            i1 -= i7;
            n -= (i7 << 3);
            paramB.B = m;
            paramB.S = n;
            paramZStream.avail_in = i2;
            paramZStream.total_in += i1 - paramZStream.next_in_index;
            paramZStream.next_in_index = i1;
            paramB.O = i3;
            return -3;
          }
          if ((k & 0x40) != 0)
            break;
          i += arrayOfInt[((j + i) * 3 + 2)];
          i += (m & B[k]);
          if ((k = arrayOfInt[((j + i) * 3)]) != 0)
            continue;
          m >>= arrayOfInt[((j + i) * 3 + 1)];
          n -= arrayOfInt[((j + i) * 3 + 1)];
          paramB.G[(i3++)] = (byte)arrayOfInt[((j + i) * 3 + 2)];
          i4--;
          break label1365;
        }
        if ((k & 0x20) != 0)
        {
          i7 = paramZStream.avail_in - i2;
          i7 = n >> 3 < i7 ? n >> 3 : i7;
          i2 += i7;
          i1 -= i7;
          n -= (i7 << 3);
          paramB.B = m;
          paramB.S = n;
          paramZStream.avail_in = i2;
          paramZStream.total_in += i1 - paramZStream.next_in_index;
          paramZStream.next_in_index = i1;
          paramB.O = i3;
          return 1;
        }
        paramZStream.msg = "invalid literal/length code";
        i7 = paramZStream.avail_in - i2;
        i7 = n >> 3 < i7 ? n >> 3 : i7;
        i2 += i7;
        i1 -= i7;
        n -= (i7 << 3);
        paramB.B = m;
        paramB.S = n;
        paramZStream.avail_in = i2;
        paramZStream.total_in += i1 - paramZStream.next_in_index;
        paramZStream.next_in_index = i1;
        paramB.O = i3;
        return -3;
      }
    }
    while ((i4 >= 258) && (i2 >= 10));
    int i7 = paramZStream.avail_in - i2;
    i7 = n >> 3 < i7 ? n >> 3 : i7;
    i2 += i7;
    i1 -= i7;
    n -= (i7 << 3);
    paramB.B = m;
    paramB.S = n;
    paramZStream.avail_in = i2;
    paramZStream.total_in += i1 - paramZStream.next_in_index;
    paramZStream.next_in_index = i1;
    paramB.O = i3;
    return 0;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.F
 * JD-Core Version:    0.6.0
 */