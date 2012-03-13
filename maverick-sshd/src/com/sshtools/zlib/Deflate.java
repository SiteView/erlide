package com.sshtools.zlib;

public final class Deflate
{
  private static _A[] E = new _A[10];
  private static final String[] S;
  ZStream d;
  int j;
  byte[] z;
  int ª;
  int K;
  int P;
  int µ;
  byte g;
  byte ¤;
  int X;
  int i;
  int b;
  int T;
  byte[] w;
  int W;
  short[] h;
  short[] U;
  int A;
  int F;
  int £;
  int r;
  int u;
  int G;
  int H;
  int N;
  int y;
  int l;
  int o;
  int ¥;
  int e;
  int D;
  int c;
  int I;
  int k;
  int x;
  int s;
  short[] O = new short[1146];
  short[] a = new short[122];
  short[] Y = new short[78];
  C f = new C();
  C _ = new C();
  C t = new C();
  short[] v = new short[16];
  int[] J = new int[573];
  int M;
  int R;
  byte[] L = new byte[573];
  int ¢;
  int q;
  int V;
  int C;
  int p;
  int m;
  int B;
  int Z;
  short Q;
  int n;

  void F()
  {
    this.W = (2 * this.i);
    this.U[(this.F - 1)] = 0;
    for (int i1 = 0; i1 < this.F - 1; i1++)
      this.U[i1] = 0;
    this.c = E[this.I].E;
    this.x = E[this.I].A;
    this.s = E[this.I].B;
    this.D = E[this.I].D;
    this.l = 0;
    this.G = 0;
    this.¥ = 0;
    this.H = (this.e = 2);
    this.y = 0;
    this.A = 0;
  }

  void B()
  {
    this.f.A = this.O;
    this.f.F = E.E;
    this._.A = this.a;
    this._.F = E.C;
    this.t.A = this.Y;
    this.t.F = E.D;
    this.Q = 0;
    this.n = 0;
    this.Z = 8;
    I();
  }

  void I()
  {
    for (int i1 = 0; i1 < 286; i1++)
      this.O[(i1 * 2)] = 0;
    for (i1 = 0; i1 < 30; i1++)
      this.a[(i1 * 2)] = 0;
    for (i1 = 0; i1 < 19; i1++)
      this.Y[(i1 * 2)] = 0;
    this.O[512] = 1;
    this.p = (this.m = 0);
    this.V = (this.B = 0);
  }

  void B(short[] paramArrayOfShort, int paramInt)
  {
    int i1 = this.J[paramInt];
    int i2 = paramInt << 1;
    while (i2 <= this.M)
    {
      if ((i2 < this.M) && (A(paramArrayOfShort, this.J[(i2 + 1)], this.J[i2], this.L)))
        i2++;
      if (A(paramArrayOfShort, i1, this.J[i2], this.L))
        break;
      this.J[paramInt] = this.J[i2];
      paramInt = i2;
      i2 <<= 1;
    }
    this.J[paramInt] = i1;
  }

  static boolean A(short[] paramArrayOfShort, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    return (paramArrayOfShort[(paramInt1 * 2)] < paramArrayOfShort[(paramInt2 * 2)]) || ((paramArrayOfShort[(paramInt1 * 2)] == paramArrayOfShort[(paramInt2 * 2)]) && (paramArrayOfByte[paramInt1] <= paramArrayOfByte[paramInt2]));
  }

  void A(short[] paramArrayOfShort, int paramInt)
  {
    int i2 = -1;
    int i4 = paramArrayOfShort[1];
    int i5 = 0;
    int i6 = 7;
    int i7 = 4;
    if (i4 == 0)
    {
      i6 = 138;
      i7 = 3;
    }
    paramArrayOfShort[((paramInt + 1) * 2 + 1)] = -1;
    for (int i1 = 0; i1 <= paramInt; i1++)
    {
      int i3 = i4;
      i4 = paramArrayOfShort[((i1 + 1) * 2 + 1)];
      i5++;
      if ((i5 < i6) && (i3 == i4))
        continue;
      if (i5 < i7)
      {
        int tmp98_97 = (i3 * 2);
        short[] tmp98_91 = this.Y;
        tmp98_91[tmp98_97] = (short)(tmp98_91[tmp98_97] + i5);
      }
      else if (i3 != 0)
      {
        if (i3 != i2)
        {
          int tmp128_127 = (i3 * 2);
          short[] tmp128_121 = this.Y;
          tmp128_121[tmp128_127] = (short)(tmp128_121[tmp128_127] + 1);
        }
        short[] tmp140_135 = this.Y;
        tmp140_135[32] = (short)(tmp140_135[32] + 1);
      }
      else if (i5 <= 10)
      {
        short[] tmp162_157 = this.Y;
        tmp162_157[34] = (short)(tmp162_157[34] + 1);
      }
      else
      {
        short[] tmp177_172 = this.Y;
        tmp177_172[36] = (short)(tmp177_172[36] + 1);
      }
      i5 = 0;
      i2 = i3;
      if (i4 == 0)
      {
        i6 = 138;
        i7 = 3;
      }
      else if (i3 == i4)
      {
        i6 = 6;
        i7 = 3;
      }
      else
      {
        i6 = 7;
        i7 = 4;
      }
    }
  }

  int G()
  {
    A(this.O, this.f.J);
    A(this.a, this._.J);
    this.t.A(this);
    for (int i1 = 18; (i1 >= 3) && (this.Y[(C.D[i1] * 2 + 1)] == 0); i1--);
    this.p += 3 * (i1 + 1) + 5 + 5 + 4;
    return i1;
  }

  void A(int paramInt1, int paramInt2, int paramInt3)
  {
    A(paramInt1 - 257, 5);
    A(paramInt2 - 1, 5);
    A(paramInt3 - 4, 4);
    for (int i1 = 0; i1 < paramInt3; i1++)
      A(this.Y[(C.D[i1] * 2 + 1)], 3);
    C(this.O, paramInt1 - 1);
    C(this.a, paramInt2 - 1);
  }

  void C(short[] paramArrayOfShort, int paramInt)
  {
    int i2 = -1;
    int i4 = paramArrayOfShort[1];
    int i5 = 0;
    int i6 = 7;
    int i7 = 4;
    if (i4 == 0)
    {
      i6 = 138;
      i7 = 3;
    }
    for (int i1 = 0; i1 <= paramInt; i1++)
    {
      int i3 = i4;
      i4 = paramArrayOfShort[((i1 + 1) * 2 + 1)];
      i5++;
      if ((i5 < i6) && (i3 == i4))
        continue;
      if (i5 < i7)
      {
        do
        {
          A(i3, this.Y);
          i5--;
        }
        while (i5 != 0);
      }
      else if (i3 != 0)
      {
        if (i3 != i2)
        {
          A(i3, this.Y);
          i5--;
        }
        A(16, this.Y);
        A(i5 - 3, 2);
      }
      else if (i5 <= 10)
      {
        A(17, this.Y);
        A(i5 - 3, 3);
      }
      else
      {
        A(18, this.Y);
        A(i5 - 11, 7);
      }
      i5 = 0;
      i2 = i3;
      if (i4 == 0)
      {
        i6 = 138;
        i7 = 3;
      }
      else if (i3 == i4)
      {
        i6 = 6;
        i7 = 3;
      }
      else
      {
        i6 = 7;
        i7 = 4;
      }
    }
  }

  final void A(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    System.arraycopy(paramArrayOfByte, paramInt1, this.z, this.P, paramInt2);
    this.P += paramInt2;
  }

  final void A(byte paramByte)
  {
    this.z[(this.P++)] = paramByte;
  }

  final void B(int paramInt)
  {
    A((byte)paramInt);
    A((byte)(paramInt >>> 8));
  }

  final void E(int paramInt)
  {
    A((byte)(paramInt >> 8));
    A((byte)paramInt);
  }

  final void A(int paramInt, short[] paramArrayOfShort)
  {
    A(paramArrayOfShort[(paramInt * 2)] & 0xFFFF, paramArrayOfShort[(paramInt * 2 + 1)] & 0xFFFF);
  }

  void A(int paramInt1, int paramInt2)
  {
    int i1 = paramInt2;
    if (this.n > 16 - i1)
    {
      int i2 = paramInt1;
      this.Q = (short)(this.Q | i2 << this.n & 0xFFFF);
      B(this.Q);
      this.Q = (short)(i2 >>> 16 - this.n);
      this.n += i1 - 16;
    }
    else
    {
      this.Q = (short)(this.Q | paramInt1 << this.n & 0xFFFF);
      this.n += i1;
    }
  }

  void C()
  {
    A(2, 3);
    A(256, E.J);
    A();
    if (1 + this.Z + 10 - this.n < 9)
    {
      A(2, 3);
      A(256, E.J);
      A();
    }
    this.Z = 7;
  }

  boolean B(int paramInt1, int paramInt2)
  {
    this.z[(this.C + this.V * 2)] = (byte)(paramInt1 >>> 8);
    this.z[(this.C + this.V * 2 + 1)] = (byte)paramInt1;
    this.z[(this.¢ + this.V)] = (byte)paramInt2;
    this.V += 1;
    if (paramInt1 == 0)
    {
      int tmp78_77 = (paramInt2 * 2);
      short[] tmp78_72 = this.O;
      tmp78_72[tmp78_77] = (short)(tmp78_72[tmp78_77] + 1);
    }
    else
    {
      this.B += 1;
      paramInt1--;
      int tmp117_116 = ((C.B[paramInt2] + 256 + 1) * 2);
      short[] tmp117_101 = this.O;
      tmp117_101[tmp117_116] = (short)(tmp117_101[tmp117_116] + 1);
      int tmp133_132 = (C.A(paramInt1) * 2);
      short[] tmp133_124 = this.a;
      tmp133_124[tmp133_132] = (short)(tmp133_124[tmp133_132] + 1);
    }
    if (((this.V & 0x1FFF) == 0) && (this.I > 2))
    {
      int i1 = this.V * 8;
      int i2 = this.l - this.G;
      for (int i3 = 0; i3 < 30; i3++)
        i1 = (int)(i1 + this.a[(i3 * 2)] * (5L + C.E[i3]));
      i1 >>>= 3;
      if ((this.B < this.V / 2) && (i1 < i2 / 2))
        return true;
    }
    return this.V == this.q - 1;
  }

  void A(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
  {
    int i3 = 0;
    if (this.V != 0)
      do
      {
        int i1 = this.z[(this.C + i3 * 2)] << 8 & 0xFF00 | this.z[(this.C + i3 * 2 + 1)] & 0xFF;
        int i2 = this.z[(this.¢ + i3)] & 0xFF;
        i3++;
        if (i1 == 0)
        {
          A(i2, paramArrayOfShort1);
        }
        else
        {
          int i4 = C.B[i2];
          A(i4 + 256 + 1, paramArrayOfShort1);
          int i5 = C.I[i4];
          if (i5 != 0)
          {
            i2 -= C.G[i4];
            A(i2, i5);
          }
          i1--;
          i4 = C.A(i1);
          A(i4, paramArrayOfShort2);
          i5 = C.E[i4];
          if (i5 == 0)
            continue;
          i1 -= C.K[i4];
          A(i1, i5);
        }
      }
      while (i3 < this.V);
    A(256, paramArrayOfShort1);
    this.Z = paramArrayOfShort1[513];
  }

  void D()
  {
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    while (i1 < 7)
    {
      i3 += this.O[(i1 * 2)];
      i1++;
    }
    while (i1 < 128)
    {
      i2 += this.O[(i1 * 2)];
      i1++;
    }
    while (i1 < 256)
    {
      i3 += this.O[(i1 * 2)];
      i1++;
    }
    this.g = (byte)(i3 > i2 >>> 2 ? 0 : 1);
  }

  void A()
  {
    if (this.n == 16)
    {
      B(this.Q);
      this.Q = 0;
      this.n = 0;
    }
    else if (this.n >= 8)
    {
      A((byte)this.Q);
      this.Q = (short)(this.Q >>> 8);
      this.n -= 8;
    }
  }

  void E()
  {
    if (this.n > 8)
      B(this.Q);
    else if (this.n > 0)
      A((byte)this.Q);
    this.Q = 0;
    this.n = 0;
  }

  void A(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i1 = 0;
    E();
    this.Z = 8;
    if (paramBoolean)
    {
      B((short)paramInt2);
      B((short)(paramInt2 ^ 0xFFFFFFFF));
    }
    A(this.w, paramInt1, paramInt2);
  }

  void A(boolean paramBoolean)
  {
    C(this.G >= 0 ? this.G : -1, this.l - this.G, paramBoolean);
    this.G = this.l;
    this.d.A();
  }

  int D(int paramInt)
  {
    int i1 = 65535;
    if (i1 > this.ª - 5)
      i1 = this.ª - 5;
    while (true)
      if (this.¥ <= 1)
      {
        H();
        if ((this.¥ == 0) && (paramInt == 0))
          return 0;
        if (this.¥ == 0)
          break;
      }
      else
      {
        this.l += this.¥;
        this.¥ = 0;
        int i2 = this.G + i1;
        if ((this.l == 0) || (this.l >= i2))
        {
          this.¥ = (this.l - i2);
          this.l = i2;
          A(false);
          if (this.d.avail_out == 0)
            return 0;
        }
        if (this.l - this.G < this.i - 262)
          continue;
        A(false);
        if (this.d.avail_out == 0)
          return 0;
      }
    A(paramInt == 4);
    if (this.d.avail_out == 0)
      return paramInt == 4 ? 2 : 0;
    return paramInt == 4 ? 3 : 1;
  }

  void B(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    A(0 + (paramBoolean ? 1 : 0), 3);
    A(paramInt1, paramInt2, true);
  }

  void C(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i3 = 0;
    int i1;
    int i2;
    if (this.I > 0)
    {
      if (this.g == 2)
        D();
      this.f.A(this);
      this._.A(this);
      i3 = G();
      i1 = this.p + 3 + 7 >>> 3;
      i2 = this.m + 3 + 7 >>> 3;
      if (i2 <= i1)
        i1 = i2;
    }
    else
    {
      i1 = i2 = paramInt2 + 5;
    }
    if ((paramInt2 + 4 <= i1) && (paramInt1 != -1))
    {
      B(paramInt1, paramInt2, paramBoolean);
    }
    else if (i2 == i1)
    {
      A(2 + (paramBoolean ? 1 : 0), 3);
      A(E.J, E.F);
    }
    else
    {
      A(4 + (paramBoolean ? 1 : 0), 3);
      A(this.f.J + 1, this._.J + 1, i3 + 1);
      A(this.O, this.a);
    }
    I();
    if (paramBoolean)
      E();
  }

  void H()
  {
    do
    {
      int i4 = this.W - this.¥ - this.l;
      if ((i4 == 0) && (this.l == 0) && (this.¥ == 0))
      {
        i4 = this.i;
      }
      else if (i4 == -1)
      {
        i4--;
      }
      else if (this.l >= this.i + this.i - 262)
      {
        System.arraycopy(this.w, this.i, this.w, 0, this.i);
        this.o -= this.i;
        this.l -= this.i;
        this.G -= this.i;
        i1 = this.F;
        int i3 = i1;
        int i2;
        do
        {
          i3--;
          i2 = this.U[i3] & 0xFFFF;
          this.U[i3] = (i2 >= this.i ? (short)(i2 - this.i) : 0);
          i1--;
        }
        while (i1 != 0);
        i1 = this.i;
        i3 = i1;
        do
        {
          i3--;
          i2 = this.h[i3] & 0xFFFF;
          this.h[i3] = (i2 >= this.i ? (short)(i2 - this.i) : 0);
          i1--;
        }
        while (i1 != 0);
        i4 += this.i;
      }
      if (this.d.avail_in == 0)
        return;
      int i1 = this.d.A(this.w, this.l + this.¥, i4);
      this.¥ += i1;
      if (this.¥ < 3)
        continue;
      this.A = (this.w[this.l] & 0xFF);
      this.A = ((this.A << this.u ^ this.w[(this.l + 1)] & 0xFF) & this.r);
    }
    while ((this.¥ < 262) && (this.d.avail_in != 0));
  }

  int F(int paramInt)
  {
    int i1 = 0;
    while (true)
      if (this.¥ < 262)
      {
        H();
        if ((this.¥ < 262) && (paramInt == 0))
          return 0;
        if (this.¥ == 0)
          break;
      }
      else
      {
        if (this.¥ >= 3)
        {
          this.A = ((this.A << this.u ^ this.w[(this.l + 2)] & 0xFF) & this.r);
          i1 = this.U[this.A] & 0xFFFF;
          this.h[(this.l & this.T)] = this.U[this.A];
          this.U[this.A] = (short)this.l;
        }
        if ((i1 != 0L) && ((this.l - i1 & 0xFFFF) <= this.i - 262) && (this.k != 2))
          this.H = A(i1);
        boolean bool;
        if (this.H >= 3)
        {
          bool = B(this.l - this.o, this.H - 3);
          this.¥ -= this.H;
          if ((this.H <= this.c) && (this.¥ >= 3))
          {
            this.H -= 1;
            do
            {
              this.l += 1;
              this.A = ((this.A << this.u ^ this.w[(this.l + 2)] & 0xFF) & this.r);
              i1 = this.U[this.A] & 0xFFFF;
              this.h[(this.l & this.T)] = this.U[this.A];
              this.U[this.A] = (short)this.l;
            }
            while (--this.H != 0);
            this.l += 1;
          }
          else
          {
            this.l += this.H;
            this.H = 0;
            this.A = (this.w[this.l] & 0xFF);
            this.A = ((this.A << this.u ^ this.w[(this.l + 1)] & 0xFF) & this.r);
          }
        }
        else
        {
          bool = B(0, this.w[this.l] & 0xFF);
          this.¥ -= 1;
          this.l += 1;
        }
        if (!bool)
          continue;
        A(false);
        if (this.d.avail_out == 0)
          return 0;
      }
    A(paramInt == 4);
    if (this.d.avail_out == 0)
    {
      if (paramInt == 4)
        return 2;
      return 0;
    }
    return paramInt == 4 ? 3 : 1;
  }

  int C(int paramInt)
  {
    int i1 = 0;
    boolean bool;
    while (true)
    {
      if (this.¥ < 262)
      {
        H();
        if ((this.¥ < 262) && (paramInt == 0))
          return 0;
        if (this.¥ == 0)
          break;
      }
      if (this.¥ >= 3)
      {
        this.A = ((this.A << this.u ^ this.w[(this.l + 2)] & 0xFF) & this.r);
        i1 = this.U[this.A] & 0xFFFF;
        this.h[(this.l & this.T)] = this.U[this.A];
        this.U[this.A] = (short)this.l;
      }
      this.e = this.H;
      this.N = this.o;
      this.H = 2;
      if ((i1 != 0) && (this.e < this.c) && ((this.l - i1 & 0xFFFF) <= this.i - 262))
      {
        if (this.k != 2)
          this.H = A(i1);
        if ((this.H <= 5) && ((this.k == 1) || ((this.H == 3) && (this.l - this.o > 4096))))
          this.H = 2;
      }
      if ((this.e >= 3) && (this.H <= this.e))
      {
        int i2 = this.l + this.¥ - 3;
        bool = B(this.l - 1 - this.N, this.e - 3);
        this.¥ -= this.e - 1;
        this.e -= 2;
        do
        {
          if (++this.l > i2)
            continue;
          this.A = ((this.A << this.u ^ this.w[(this.l + 2)] & 0xFF) & this.r);
          i1 = this.U[this.A] & 0xFFFF;
          this.h[(this.l & this.T)] = this.U[this.A];
          this.U[this.A] = (short)this.l;
        }
        while (--this.e != 0);
        this.y = 0;
        this.H = 2;
        this.l += 1;
        if (bool)
        {
          A(false);
          if (this.d.avail_out == 0)
            return 0;
        }
        continue;
      }
      if (this.y != 0)
      {
        bool = B(0, this.w[(this.l - 1)] & 0xFF);
        if (bool)
          A(false);
        this.l += 1;
        this.¥ -= 1;
        if (this.d.avail_out == 0)
          return 0;
      }
      this.y = 1;
      this.l += 1;
      this.¥ -= 1;
    }
    if (this.y != 0)
    {
      bool = B(0, this.w[(this.l - 1)] & 0xFF);
      this.y = 0;
    }
    A(paramInt == 4);
    if (this.d.avail_out == 0)
    {
      if (paramInt == 4)
        return 2;
      return 0;
    }
    return paramInt == 4 ? 3 : 1;
  }

  int A(int paramInt)
  {
    int i1 = this.D;
    int i2 = this.l;
    int i5 = this.e;
    int i6 = this.l > this.i - 262 ? this.l - (this.i - 262) : 0;
    int i7 = this.s;
    int i8 = this.T;
    int i9 = this.l + 258;
    int i10 = this.w[(i2 + i5 - 1)];
    int i11 = this.w[(i2 + i5)];
    if (this.e >= this.x)
      i1 >>= 2;
    if (i7 > this.¥)
      i7 = this.¥;
    do
    {
      int i3 = paramInt;
      if ((this.w[(i3 + i5)] == i11) && (this.w[(i3 + i5 - 1)] == i10) && (this.w[i3] == this.w[i2]))
      {
        i3++;
        if (this.w[i3] == this.w[(i2 + 1)])
        {
          i2 += 2;
          i3++;
          do
          {
            i2++;
            i3++;
            if (this.w[i2] != this.w[i3])
              break;
            i2++;
            i3++;
            if (this.w[i2] != this.w[i3])
              break;
            i2++;
            i3++;
            if (this.w[i2] != this.w[i3])
              break;
            i2++;
            i3++;
            if (this.w[i2] != this.w[i3])
              break;
            i2++;
            i3++;
            if (this.w[i2] != this.w[i3])
              break;
            i2++;
            i3++;
            if (this.w[i2] != this.w[i3])
              break;
            i2++;
            i3++;
            if (this.w[i2] != this.w[i3])
              break;
            i2++;
            i3++;
          }
          while ((this.w[i2] == this.w[i3]) && (i2 < i9));
          int i4 = 258 - (i9 - i2);
          i2 = i9 - 258;
          if (i4 > i5)
          {
            this.o = paramInt;
            i5 = i4;
            if (i4 >= i7)
              break;
            i10 = this.w[(i2 + i5 - 1)];
            i11 = this.w[(i2 + i5)];
          }
        }
      }
      if ((paramInt = this.h[(paramInt & i8)] & 0xFFFF) <= i6)
        break;
      i1--;
    }
    while (i1 != 0);
    if (i5 <= this.¥)
      return i5;
    return this.¥;
  }

  int B(ZStream paramZStream, int paramInt1, int paramInt2)
  {
    return A(paramZStream, paramInt1, 8, paramInt2, 8, 0);
  }

  int A(ZStream paramZStream, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i1 = 0;
    paramZStream.msg = null;
    if (paramInt1 == -1)
      paramInt1 = 6;
    if (paramInt3 < 0)
    {
      i1 = 1;
      paramInt3 = -paramInt3;
    }
    if ((paramInt4 < 1) || (paramInt4 > 9) || (paramInt2 != 8) || (paramInt3 < 9) || (paramInt3 > 15) || (paramInt1 < 0) || (paramInt1 > 9) || (paramInt5 < 0) || (paramInt5 > 2))
      return -2;
    paramZStream.C = this;
    this.µ = i1;
    this.b = paramInt3;
    this.i = (1 << this.b);
    this.T = (this.i - 1);
    this.£ = (paramInt4 + 7);
    this.F = (1 << this.£);
    this.r = (this.F - 1);
    this.u = ((this.£ + 3 - 1) / 3);
    this.w = new byte[this.i * 2];
    this.h = new short[this.i];
    this.U = new short[this.F];
    this.q = (1 << paramInt4 + 6);
    this.z = new byte[this.q * 4];
    this.ª = (this.q * 4);
    this.C = (this.q / 2);
    this.¢ = (3 * this.q);
    this.I = paramInt1;
    this.k = paramInt5;
    this.¤ = (byte)paramInt2;
    return A(paramZStream);
  }

  int A(ZStream paramZStream)
  {
    paramZStream.total_in = (paramZStream.total_out = 0L);
    paramZStream.msg = null;
    paramZStream.D = 2;
    this.P = 0;
    this.K = 0;
    if (this.µ < 0)
      this.µ = 0;
    this.j = (this.µ != 0 ? 113 : 42);
    paramZStream.adler = paramZStream.A.A(0L, null, 0, 0);
    this.X = 0;
    B();
    F();
    return 0;
  }

  int J()
  {
    if ((this.j != 42) && (this.j != 113) && (this.j != 666))
      return -2;
    this.z = null;
    this.U = null;
    this.h = null;
    this.w = null;
    return this.j == 113 ? -3 : 0;
  }

  int A(ZStream paramZStream, int paramInt1, int paramInt2)
  {
    int i1 = 0;
    if (paramInt1 == -1)
      paramInt1 = 6;
    if ((paramInt1 < 0) || (paramInt1 > 9) || (paramInt2 < 0) || (paramInt2 > 2))
      return -2;
    if ((E[this.I].C != E[paramInt1].C) && (paramZStream.total_in != 0L))
      i1 = paramZStream.deflate(1);
    if (this.I != paramInt1)
    {
      this.I = paramInt1;
      this.c = E[this.I].E;
      this.x = E[this.I].A;
      this.s = E[this.I].B;
      this.D = E[this.I].D;
    }
    this.k = paramInt2;
    return i1;
  }

  int A(ZStream paramZStream, byte[] paramArrayOfByte, int paramInt)
  {
    int i1 = paramInt;
    int i2 = 0;
    if ((paramArrayOfByte == null) || (this.j != 42))
      return -2;
    paramZStream.adler = paramZStream.A.A(paramZStream.adler, paramArrayOfByte, 0, paramInt);
    if (i1 < 3)
      return 0;
    if (i1 > this.i - 262)
    {
      i1 = this.i - 262;
      i2 = paramInt - i1;
    }
    System.arraycopy(paramArrayOfByte, i2, this.w, 0, i1);
    this.l = i1;
    this.G = i1;
    this.A = (this.w[0] & 0xFF);
    this.A = ((this.A << this.u ^ this.w[1] & 0xFF) & this.r);
    for (int i3 = 0; i3 <= i1 - 3; i3++)
    {
      this.A = ((this.A << this.u ^ this.w[(i3 + 2)] & 0xFF) & this.r);
      this.h[(i3 & this.T)] = this.U[this.A];
      this.U[this.A] = (short)i3;
    }
    return 0;
  }

  int A(ZStream paramZStream, int paramInt)
  {
    if ((paramInt > 4) || (paramInt < 0))
      return -2;
    if ((paramZStream.next_out == null) || ((paramZStream.next_in == null) && (paramZStream.avail_in != 0)) || ((this.j == 666) && (paramInt != 4)))
    {
      paramZStream.msg = S[4];
      return -2;
    }
    if (paramZStream.avail_out == 0)
    {
      paramZStream.msg = S[7];
      return -5;
    }
    this.d = paramZStream;
    int i1 = this.X;
    this.X = paramInt;
    int i2;
    int i3;
    if (this.j == 42)
    {
      i2 = 8 + (this.b - 8 << 4) << 8;
      i3 = (this.I - 1 & 0xFF) >> 1;
      if (i3 > 3)
        i3 = 3;
      i2 |= i3 << 6;
      if (this.l != 0)
        i2 |= 32;
      i2 += 31 - i2 % 31;
      this.j = 113;
      E(i2);
      if (this.l != 0)
      {
        E((int)(paramZStream.adler >>> 16));
        E((int)(paramZStream.adler & 0xFFFF));
      }
      paramZStream.adler = paramZStream.A.A(0L, null, 0, 0);
    }
    if (this.P != 0)
    {
      paramZStream.A();
      if (paramZStream.avail_out == 0)
      {
        this.X = -1;
        return 0;
      }
    }
    else if ((paramZStream.avail_in == 0) && (paramInt <= i1) && (paramInt != 4))
    {
      paramZStream.msg = S[7];
      return -5;
    }
    if ((this.j == 666) && (paramZStream.avail_in != 0))
    {
      paramZStream.msg = S[7];
      return -5;
    }
    if ((paramZStream.avail_in != 0) || (this.¥ != 0) || ((paramInt != 0) && (this.j != 666)))
    {
      i2 = -1;
      switch (E[this.I].C)
      {
      case 0:
        i2 = D(paramInt);
        break;
      case 1:
        i2 = F(paramInt);
        break;
      case 2:
        i2 = C(paramInt);
        break;
      }
      if ((i2 == 2) || (i2 == 3))
        this.j = 666;
      if ((i2 == 0) || (i2 == 2))
      {
        if (paramZStream.avail_out == 0)
          this.X = -1;
        return 0;
      }
      if (i2 == 1)
      {
        if (paramInt == 1)
        {
          C();
        }
        else
        {
          B(0, 0, false);
          if (paramInt == 3)
            for (i3 = 0; i3 < this.F; i3++)
              this.U[i3] = 0;
        }
        paramZStream.A();
        if (paramZStream.avail_out == 0)
        {
          this.X = -1;
          return 0;
        }
      }
    }
    if (paramInt != 4)
      return 0;
    if (this.µ != 0)
      return 1;
    E((int)(paramZStream.adler >>> 16));
    E((int)(paramZStream.adler & 0xFFFF));
    paramZStream.A();
    this.µ = -1;
    return this.P != 0 ? 0 : 1;
  }

  static
  {
    E[0] = new _A(0, 0, 0, 0, 0);
    E[1] = new _A(4, 4, 8, 4, 1);
    E[2] = new _A(4, 5, 16, 8, 1);
    E[3] = new _A(4, 6, 32, 32, 1);
    E[4] = new _A(4, 4, 16, 16, 2);
    E[5] = new _A(8, 16, 32, 32, 2);
    E[6] = new _A(8, 16, 128, 128, 2);
    E[7] = new _A(8, 32, 128, 256, 2);
    E[8] = new _A(32, 128, 258, 1024, 2);
    E[9] = new _A(32, 258, 258, 4096, 2);
    S = new String[] { "need dictionary", "stream end", "", "file error", "stream error", "data error", "insufficient memory", "buffer error", "incompatible version", "" };
  }

  static class _A
  {
    int A;
    int E;
    int B;
    int D;
    int C;

    _A(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      this.A = paramInt1;
      this.E = paramInt2;
      this.B = paramInt3;
      this.D = paramInt4;
      this.C = paramInt5;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.Deflate
 * JD-Core Version:    0.6.0
 */