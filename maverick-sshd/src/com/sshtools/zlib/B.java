package com.sshtools.zlib;

final class B
{
  private static final int[] T = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535 };
  static final int[] M = { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
  int L;
  int D;
  int P;
  int F;
  int[] I;
  int[] E = new int[1];
  int[] H = new int[1];
  F R;
  int J;
  int S;
  int B;
  int[] A = new int[4320];
  byte[] G;
  int C;
  int N;
  int O;
  Object K;
  long Q;

  B(ZStream paramZStream, Object paramObject, int paramInt)
  {
    this.G = new byte[paramInt];
    this.C = paramInt;
    this.K = paramObject;
    this.L = 0;
    A(paramZStream, null);
  }

  void A(ZStream paramZStream, long[] paramArrayOfLong)
  {
    if (paramArrayOfLong != null)
      paramArrayOfLong[0] = this.Q;
    if ((this.L == 4) || (this.L == 5))
      this.I = null;
    if (this.L == 6)
      this.R.A(paramZStream);
    this.L = 0;
    this.S = 0;
    this.B = 0;
    this.N = (this.O = 0);
    if (this.K != null)
      paramZStream.adler = (this.Q = paramZStream.A.A(0L, null, 0, 0));
  }

  int B(ZStream paramZStream, int paramInt)
  {
    int m = paramZStream.next_in_index;
    int n = paramZStream.avail_in;
    int j = this.B;
    int k = this.S;
    int i1 = this.O;
    int i2 = i1 < this.N ? this.N - i1 - 1 : this.C - i1;
    while (true)
    {
      int i;
      int[] arrayOfInt1;
      switch (this.L)
      {
      case 0:
        while (k < 3)
        {
          if (n != 0)
          {
            paramInt = 0;
          }
          else
          {
            this.B = j;
            this.S = k;
            paramZStream.avail_in = n;
            paramZStream.total_in += m - paramZStream.next_in_index;
            paramZStream.next_in_index = m;
            this.O = i1;
            return A(paramZStream, paramInt);
          }
          n--;
          j |= (paramZStream.next_in[(m++)] & 0xFF) << k;
          k += 8;
        }
        i = j & 0x7;
        this.J = (i & 0x1);
        switch (i >>> 1)
        {
        case 0:
          j >>>= 3;
          k -= 3;
          i = k & 0x7;
          j >>>= i;
          k -= i;
          this.L = 1;
          break;
        case 1:
          arrayOfInt1 = new int[1];
          int[] arrayOfInt2 = new int[1];
          int[][] arrayOfInt4 = new int[1][];
          int[][] arrayOfInt6 = new int[1][];
          G.A(arrayOfInt1, arrayOfInt2, arrayOfInt4, arrayOfInt6, paramZStream);
          this.R = new F(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt4[0], arrayOfInt6[0], paramZStream);
          j >>>= 3;
          k -= 3;
          this.L = 6;
          break;
        case 2:
          j >>>= 3;
          k -= 3;
          this.L = 3;
          break;
        case 3:
          j >>>= 3;
          k -= 3;
          this.L = 9;
          paramZStream.msg = "invalid block type";
          paramInt = -3;
          this.B = j;
          this.S = k;
          paramZStream.avail_in = n;
          paramZStream.total_in += m - paramZStream.next_in_index;
          paramZStream.next_in_index = m;
          this.O = i1;
          return A(paramZStream, paramInt);
        }
        break;
      case 1:
        while (k < 32)
        {
          if (n != 0)
          {
            paramInt = 0;
          }
          else
          {
            this.B = j;
            this.S = k;
            paramZStream.avail_in = n;
            paramZStream.total_in += m - paramZStream.next_in_index;
            paramZStream.next_in_index = m;
            this.O = i1;
            return A(paramZStream, paramInt);
          }
          n--;
          j |= (paramZStream.next_in[(m++)] & 0xFF) << k;
          k += 8;
        }
        if (((j ^ 0xFFFFFFFF) >>> 16 & 0xFFFF) != (j & 0xFFFF))
        {
          this.L = 9;
          paramZStream.msg = "invalid stored block lengths";
          paramInt = -3;
          this.B = j;
          this.S = k;
          paramZStream.avail_in = n;
          paramZStream.total_in += m - paramZStream.next_in_index;
          paramZStream.next_in_index = m;
          this.O = i1;
          return A(paramZStream, paramInt);
        }
        this.D = (j & 0xFFFF);
        j = k = 0;
        this.L = (this.J != 0 ? 7 : this.D != 0 ? 2 : 0);
        break;
      case 2:
        if (n == 0)
        {
          this.B = j;
          this.S = k;
          paramZStream.avail_in = n;
          paramZStream.total_in += m - paramZStream.next_in_index;
          paramZStream.next_in_index = m;
          this.O = i1;
          return A(paramZStream, paramInt);
        }
        if (i2 == 0)
        {
          if ((i1 == this.C) && (this.N != 0))
          {
            i1 = 0;
            i2 = i1 < this.N ? this.N - i1 - 1 : this.C - i1;
          }
          if (i2 == 0)
          {
            this.O = i1;
            paramInt = A(paramZStream, paramInt);
            i1 = this.O;
            i2 = i1 < this.N ? this.N - i1 - 1 : this.C - i1;
            if ((i1 == this.C) && (this.N != 0))
            {
              i1 = 0;
              i2 = i1 < this.N ? this.N - i1 - 1 : this.C - i1;
            }
            if (i2 == 0)
            {
              this.B = j;
              this.S = k;
              paramZStream.avail_in = n;
              paramZStream.total_in += m - paramZStream.next_in_index;
              paramZStream.next_in_index = m;
              this.O = i1;
              return A(paramZStream, paramInt);
            }
          }
        }
        paramInt = 0;
        i = this.D;
        if (i > n)
          i = n;
        if (i > i2)
          i = i2;
        System.arraycopy(paramZStream.next_in, m, this.G, i1, i);
        m += i;
        n -= i;
        i1 += i;
        i2 -= i;
        if (this.D -= i != 0)
          continue;
        this.L = (this.J != 0 ? 7 : 0);
        break;
      case 3:
        while (k < 14)
        {
          if (n != 0)
          {
            paramInt = 0;
          }
          else
          {
            this.B = j;
            this.S = k;
            paramZStream.avail_in = n;
            paramZStream.total_in += m - paramZStream.next_in_index;
            paramZStream.next_in_index = m;
            this.O = i1;
            return A(paramZStream, paramInt);
          }
          n--;
          j |= (paramZStream.next_in[(m++)] & 0xFF) << k;
          k += 8;
        }
        this.P = (i = j & 0x3FFF);
        if (((i & 0x1F) > 29) || ((i >> 5 & 0x1F) > 29))
        {
          this.L = 9;
          paramZStream.msg = "too many length or distance symbols";
          paramInt = -3;
          this.B = j;
          this.S = k;
          paramZStream.avail_in = n;
          paramZStream.total_in += m - paramZStream.next_in_index;
          paramZStream.next_in_index = m;
          this.O = i1;
          return A(paramZStream, paramInt);
        }
        i = 258 + (i & 0x1F) + (i >> 5 & 0x1F);
        this.I = new int[i];
        j >>>= 14;
        k -= 14;
        this.F = 0;
        this.L = 4;
      case 4:
        while (this.F < 4 + (this.P >>> 10))
        {
          while (k < 3)
          {
            if (n != 0)
            {
              paramInt = 0;
            }
            else
            {
              this.B = j;
              this.S = k;
              paramZStream.avail_in = n;
              paramZStream.total_in += m - paramZStream.next_in_index;
              paramZStream.next_in_index = m;
              this.O = i1;
              return A(paramZStream, paramInt);
            }
            n--;
            j |= (paramZStream.next_in[(m++)] & 0xFF) << k;
            k += 8;
          }
          this.I[M[(this.F++)]] = (j & 0x7);
          j >>>= 3;
          k -= 3;
        }
        while (this.F < 19)
          this.I[M[(this.F++)]] = 0;
        this.E[0] = 7;
        i = G.A(this.I, this.E, this.H, this.A, paramZStream);
        if (i != 0)
        {
          paramInt = i;
          if (paramInt == -3)
          {
            this.I = null;
            this.L = 9;
          }
          this.B = j;
          this.S = k;
          paramZStream.avail_in = n;
          paramZStream.total_in += m - paramZStream.next_in_index;
          paramZStream.next_in_index = m;
          this.O = i1;
          return A(paramZStream, paramInt);
        }
        this.F = 0;
        this.L = 5;
      case 5:
        while (true)
        {
          i = this.P;
          if (this.F >= 258 + (i & 0x1F) + (i >> 5 & 0x1F))
            break;
          i = this.E[0];
          while (k < i)
          {
            if (n != 0)
            {
              paramInt = 0;
            }
            else
            {
              this.B = j;
              this.S = k;
              paramZStream.avail_in = n;
              paramZStream.total_in += m - paramZStream.next_in_index;
              paramZStream.next_in_index = m;
              this.O = i1;
              return A(paramZStream, paramInt);
            }
            n--;
            j |= (paramZStream.next_in[(m++)] & 0xFF) << k;
            k += 8;
          }
          if (this.H[0] == -1);
          i = this.A[((this.H[0] + (j & T[i])) * 3 + 1)];
          int i5 = this.A[((this.H[0] + (j & T[i])) * 3 + 2)];
          if (i5 < 16)
          {
            j >>>= i;
            k -= i;
            this.I[(this.F++)] = i5;
          }
          else
          {
            int i3 = i5 == 18 ? 7 : i5 - 14;
            int i4 = i5 == 18 ? 11 : 3;
            while (k < i + i3)
            {
              if (n != 0)
              {
                paramInt = 0;
              }
              else
              {
                this.B = j;
                this.S = k;
                paramZStream.avail_in = n;
                paramZStream.total_in += m - paramZStream.next_in_index;
                paramZStream.next_in_index = m;
                this.O = i1;
                return A(paramZStream, paramInt);
              }
              n--;
              j |= (paramZStream.next_in[(m++)] & 0xFF) << k;
              k += 8;
            }
            j >>>= i;
            k -= i;
            i4 += (j & T[i3]);
            j >>>= i3;
            k -= i3;
            i3 = this.F;
            i = this.P;
            if ((i3 + i4 > 258 + (i & 0x1F) + (i >> 5 & 0x1F)) || ((i5 == 16) && (i3 < 1)))
            {
              this.I = null;
              this.L = 9;
              paramZStream.msg = "invalid bit length repeat";
              paramInt = -3;
              this.B = j;
              this.S = k;
              paramZStream.avail_in = n;
              paramZStream.total_in += m - paramZStream.next_in_index;
              paramZStream.next_in_index = m;
              this.O = i1;
              return A(paramZStream, paramInt);
            }
            i5 = i5 == 16 ? this.I[(i3 - 1)] : 0;
            do
            {
              this.I[(i3++)] = i5;
              i4--;
            }
            while (i4 != 0);
            this.F = i3;
          }
        }
        this.H[0] = -1;
        arrayOfInt1 = new int[1];
        int[] arrayOfInt3 = new int[1];
        int[] arrayOfInt5 = new int[1];
        int[] arrayOfInt7 = new int[1];
        arrayOfInt1[0] = 9;
        arrayOfInt3[0] = 6;
        i = this.P;
        i = G.A(257 + (i & 0x1F), 1 + (i >> 5 & 0x1F), this.I, arrayOfInt1, arrayOfInt3, arrayOfInt5, arrayOfInt7, this.A, paramZStream);
        if (i != 0)
        {
          if (i == -3)
          {
            this.I = null;
            this.L = 9;
          }
          paramInt = i;
          this.B = j;
          this.S = k;
          paramZStream.avail_in = n;
          paramZStream.total_in += m - paramZStream.next_in_index;
          paramZStream.next_in_index = m;
          this.O = i1;
          return A(paramZStream, paramInt);
        }
        this.R = new F(arrayOfInt1[0], arrayOfInt3[0], this.A, arrayOfInt5[0], this.A, arrayOfInt7[0], paramZStream);
        this.I = null;
        this.L = 6;
      case 6:
        this.B = j;
        this.S = k;
        paramZStream.avail_in = n;
        paramZStream.total_in += m - paramZStream.next_in_index;
        paramZStream.next_in_index = m;
        this.O = i1;
        if ((paramInt = this.R.A(this, paramZStream, paramInt)) != 1)
          return A(paramZStream, paramInt);
        paramInt = 0;
        this.R.A(paramZStream);
        m = paramZStream.next_in_index;
        n = paramZStream.avail_in;
        j = this.B;
        k = this.S;
        i1 = this.O;
        i2 = i1 < this.N ? this.N - i1 - 1 : this.C - i1;
        if (this.J != 0)
          break label2592;
        this.L = 0;
      case 7:
      case 8:
      case 9:
      }
    }
    label2592: this.L = 7;
    this.O = i1;
    paramInt = A(paramZStream, paramInt);
    i1 = this.O;
    i2 = i1 < this.N ? this.N - i1 - 1 : this.C - i1;
    if (this.N != this.O)
    {
      this.B = j;
      this.S = k;
      paramZStream.avail_in = n;
      paramZStream.total_in += m - paramZStream.next_in_index;
      paramZStream.next_in_index = m;
      this.O = i1;
      return A(paramZStream, paramInt);
    }
    this.L = 8;
    paramInt = 1;
    this.B = j;
    this.S = k;
    paramZStream.avail_in = n;
    paramZStream.total_in += m - paramZStream.next_in_index;
    paramZStream.next_in_index = m;
    this.O = i1;
    return A(paramZStream, paramInt);
    paramInt = -3;
    this.B = j;
    this.S = k;
    paramZStream.avail_in = n;
    paramZStream.total_in += m - paramZStream.next_in_index;
    paramZStream.next_in_index = m;
    this.O = i1;
    return A(paramZStream, paramInt);
    paramInt = -2;
    this.B = j;
    this.S = k;
    paramZStream.avail_in = n;
    paramZStream.total_in += m - paramZStream.next_in_index;
    paramZStream.next_in_index = m;
    this.O = i1;
    return A(paramZStream, paramInt);
  }

  void A(ZStream paramZStream)
  {
    A(paramZStream, null);
    this.G = null;
    this.A = null;
  }

  void A(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    System.arraycopy(paramArrayOfByte, paramInt1, this.G, 0, paramInt2);
    this.N = (this.O = paramInt2);
  }

  int A(ZStream paramZStream, int paramInt)
  {
    int j = paramZStream.next_out_index;
    int k = this.N;
    int i = (k <= this.O ? this.O : this.C) - k;
    if (i > paramZStream.avail_out)
      i = paramZStream.avail_out;
    if ((i != 0) && (paramInt == -5))
      paramInt = 0;
    paramZStream.avail_out -= i;
    paramZStream.total_out += i;
    if (this.K != null)
      paramZStream.adler = (this.Q = paramZStream.A.A(this.Q, this.G, k, i));
    System.arraycopy(this.G, k, paramZStream.next_out, j, i);
    j += i;
    k += i;
    if (k == this.C)
    {
      k = 0;
      if (this.O == this.C)
        this.O = 0;
      i = this.O - k;
      if (i > paramZStream.avail_out)
        i = paramZStream.avail_out;
      if ((i != 0) && (paramInt == -5))
        paramInt = 0;
      paramZStream.avail_out -= i;
      paramZStream.total_out += i;
      if (this.K != null)
        paramZStream.adler = (this.Q = paramZStream.A.A(this.Q, this.G, k, i));
      System.arraycopy(this.G, k, paramZStream.next_out, j, i);
      j += i;
      k += i;
    }
    paramZStream.next_out_index = j;
    this.N = k;
    return paramInt;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.B
 * JD-Core Version:    0.6.0
 */