package com.sshtools.zlib;

public final class Deflate
{
  private static _b[] f = new _b[10];
  private static final String[] t;
  ZStream fb;
  int lb;
  byte[] bc;
  int gc;
  int l;
  int q;
  int hc;
  byte ib;
  byte ec;
  int y;
  int kb;
  int db;
  int u;
  byte[] yb;
  int x;
  short[] jb;
  short[] v;
  int b;
  int g;
  int dc;
  int tb;
  int wb;
  int h;
  int i;
  int o;
  int ac;
  int nb;
  int qb;
  int fc;
  int gb;
  int e;
  int eb;
  int j;
  int mb;
  int zb;
  int ub;
  short[] p = new short[1146];
  short[] cb = new short[122];
  short[] z = new short[78];
  d hb = new d();
  d bb = new d();
  d vb = new d();
  short[] xb = new short[16];
  int[] k = new int[573];
  int n;
  int s;
  byte[] m = new byte[573];
  int cc;
  int sb;
  int w;
  int d;
  int rb;
  int ob;
  int c;
  int ab;
  short r;
  int pb;

  void g()
  {
    this.x = (2 * this.kb);
    this.v[(this.g - 1)] = 0;
    for (int i1 = 0; i1 < this.g - 1; i1++)
      this.v[i1] = 0;
    this.eb = f[this.j].f;
    this.zb = f[this.j].b;
    this.ub = f[this.j].c;
    this.e = f[this.j].e;
    this.nb = 0;
    this.h = 0;
    this.fc = 0;
    this.i = (this.gb = 2);
    this.ac = 0;
    this.b = 0;
  }

  void c()
  {
    this.hb.b = this.p;
    this.hb.g = f.f;
    this.bb.b = this.cb;
    this.bb.g = f.log;
    this.vb.b = this.z;
    this.vb.g = f.e;
    this.r = 0;
    this.pb = 0;
    this.ab = 8;
    j();
  }

  void j()
  {
    for (int i1 = 0; i1 < 286; i1++)
      this.p[(i1 * 2)] = 0;
    for (i1 = 0; i1 < 30; i1++)
      this.cb[(i1 * 2)] = 0;
    for (i1 = 0; i1 < 19; i1++)
      this.z[(i1 * 2)] = 0;
    this.p[512] = 1;
    this.rb = (this.ob = 0);
    this.w = (this.c = 0);
  }

  void c(short[] paramArrayOfShort, int paramInt)
  {
    int i1 = this.k[paramInt];
    int i2 = paramInt << 1;
    while (i2 <= this.n)
    {
      if ((i2 < this.n) && (b(paramArrayOfShort, this.k[(i2 + 1)], this.k[i2], this.m)))
        i2++;
      if (b(paramArrayOfShort, i1, this.k[i2], this.m))
        break;
      this.k[paramInt] = this.k[i2];
      paramInt = i2;
      i2 <<= 1;
    }
    this.k[paramInt] = i1;
  }

  static boolean b(short[] paramArrayOfShort, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    return (paramArrayOfShort[(paramInt1 * 2)] < paramArrayOfShort[(paramInt2 * 2)]) || ((paramArrayOfShort[(paramInt1 * 2)] == paramArrayOfShort[(paramInt2 * 2)]) && (paramArrayOfByte[paramInt1] <= paramArrayOfByte[paramInt2]));
  }

  void b(short[] paramArrayOfShort, int paramInt)
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
        short[] tmp98_91 = this.z;
        tmp98_91[tmp98_97] = (short)(tmp98_91[tmp98_97] + i5);
      }
      else if (i3 != 0)
      {
        if (i3 != i2)
        {
          int tmp128_127 = (i3 * 2);
          short[] tmp128_121 = this.z;
          tmp128_121[tmp128_127] = (short)(tmp128_121[tmp128_127] + 1);
        }
        short[] tmp140_135 = this.z;
        tmp140_135[32] = (short)(tmp140_135[32] + 1);
      }
      else if (i5 <= 10)
      {
        short[] tmp162_157 = this.z;
        tmp162_157[34] = (short)(tmp162_157[34] + 1);
      }
      else
      {
        short[] tmp177_172 = this.z;
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

  int h()
  {
    b(this.p, this.hb.k);
    b(this.cb, this.bb.k);
    this.vb.b(this);
    for (int i1 = 18; (i1 >= 3) && (this.z[(d.e[i1] * 2 + 1)] == 0); i1--);
    this.rb += 3 * (i1 + 1) + 5 + 5 + 4;
    return i1;
  }

  void b(int paramInt1, int paramInt2, int paramInt3)
  {
    b(paramInt1 - 257, 5);
    b(paramInt2 - 1, 5);
    b(paramInt3 - 4, 4);
    for (int i1 = 0; i1 < paramInt3; i1++)
      b(this.z[(d.e[i1] * 2 + 1)], 3);
    d(this.p, paramInt1 - 1);
    d(this.cb, paramInt2 - 1);
  }

  void d(short[] paramArrayOfShort, int paramInt)
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
          b(i3, this.z);
          i5--;
        }
        while (i5 != 0);
      }
      else if (i3 != 0)
      {
        if (i3 != i2)
        {
          b(i3, this.z);
          i5--;
        }
        b(16, this.z);
        b(i5 - 3, 2);
      }
      else if (i5 <= 10)
      {
        b(17, this.z);
        b(i5 - 3, 3);
      }
      else
      {
        b(18, this.z);
        b(i5 - 11, 7);
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

  final void b(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    System.arraycopy(paramArrayOfByte, paramInt1, this.bc, this.q, paramInt2);
    this.q += paramInt2;
  }

  final void b(byte paramByte)
  {
    this.bc[(this.q++)] = paramByte;
  }

  final void c(int paramInt)
  {
    b((byte)paramInt);
    b((byte)(paramInt >>> 8));
  }

  final void f(int paramInt)
  {
    b((byte)(paramInt >> 8));
    b((byte)paramInt);
  }

  final void b(int paramInt, short[] paramArrayOfShort)
  {
    b(paramArrayOfShort[(paramInt * 2)] & 0xFFFF, paramArrayOfShort[(paramInt * 2 + 1)] & 0xFFFF);
  }

  void b(int paramInt1, int paramInt2)
  {
    int i1 = paramInt2;
    if (this.pb > 16 - i1)
    {
      int i2 = paramInt1;
      this.r = (short)(this.r | i2 << this.pb & 0xFFFF);
      c(this.r);
      this.r = (short)(i2 >>> 16 - this.pb);
      this.pb += i1 - 16;
    }
    else
    {
      this.r = (short)(this.r | paramInt1 << this.pb & 0xFFFF);
      this.pb += i1;
    }
  }

  void d()
  {
    b(2, 3);
    b(256, f.k);
    b();
    if (1 + this.ab + 10 - this.pb < 9)
    {
      b(2, 3);
      b(256, f.k);
      b();
    }
    this.ab = 7;
  }

  boolean c(int paramInt1, int paramInt2)
  {
    this.bc[(this.d + this.w * 2)] = (byte)(paramInt1 >>> 8);
    this.bc[(this.d + this.w * 2 + 1)] = (byte)paramInt1;
    this.bc[(this.cc + this.w)] = (byte)paramInt2;
    this.w += 1;
    if (paramInt1 == 0)
    {
      int tmp78_77 = (paramInt2 * 2);
      short[] tmp78_72 = this.p;
      tmp78_72[tmp78_77] = (short)(tmp78_72[tmp78_77] + 1);
    }
    else
    {
      this.c += 1;
      paramInt1--;
      int tmp117_116 = ((d.SshVersion[paramInt2] + 256 + 1) * 2);
      short[] tmp117_101 = this.p;
      tmp117_101[tmp117_116] = (short)(tmp117_101[tmp117_116] + 1);
      int tmp133_132 = (d.b(paramInt1) * 2);
      short[] tmp133_124 = this.cb;
      tmp133_124[tmp133_132] = (short)(tmp133_124[tmp133_132] + 1);
    }
    if (((this.w & 0x1FFF) == 0) && (this.j > 2))
    {
      int i1 = this.w * 8;
      int i2 = this.nb - this.h;
      for (int i3 = 0; i3 < 30; i3++)
        i1 = (int)(i1 + this.cb[(i3 * 2)] * (5L + d.f[i3]));
      i1 >>>= 3;
      if ((this.c < this.w / 2) && (i1 < i2 / 2))
        return true;
    }
    return this.w == this.sb - 1;
  }

  void b(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
  {
    int i3 = 0;
    if (this.w != 0)
      do
      {
        int i1 = this.bc[(this.d + i3 * 2)] << 8 & 0xFF00 | this.bc[(this.d + i3 * 2 + 1)] & 0xFF;
        int i2 = this.bc[(this.cc + i3)] & 0xFF;
        i3++;
        if (i1 == 0)
        {
          b(i2, paramArrayOfShort1);
        }
        else
        {
          int i4 = d.SshVersion[i2];
          b(i4 + 256 + 1, paramArrayOfShort1);
          int i5 = d.j[i4];
          if (i5 != 0)
          {
            i2 -= d.h[i4];
            b(i2, i5);
          }
          i1--;
          i4 = d.b(i1);
          b(i4, paramArrayOfShort2);
          i5 = d.f[i4];
          if (i5 == 0)
            continue;
          i1 -= d.l[i4];
          b(i1, i5);
        }
      }
      while (i3 < this.w);
    b(256, paramArrayOfShort1);
    this.ab = paramArrayOfShort1[513];
  }

  void e()
  {
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    while (i1 < 7)
    {
      i3 += this.p[(i1 * 2)];
      i1++;
    }
    while (i1 < 128)
    {
      i2 += this.p[(i1 * 2)];
      i1++;
    }
    while (i1 < 256)
    {
      i3 += this.p[(i1 * 2)];
      i1++;
    }
    this.ib = (byte)(i3 > i2 >>> 2 ? 0 : 1);
  }

  void b()
  {
    if (this.pb == 16)
    {
      c(this.r);
      this.r = 0;
      this.pb = 0;
    }
    else if (this.pb >= 8)
    {
      b((byte)this.r);
      this.r = (short)(this.r >>> 8);
      this.pb -= 8;
    }
  }

  void f()
  {
    if (this.pb > 8)
      c(this.r);
    else if (this.pb > 0)
      b((byte)this.r);
    this.r = 0;
    this.pb = 0;
  }

  void b(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i1 = 0;
    f();
    this.ab = 8;
    if (paramBoolean)
    {
      c((short)paramInt2);
      c((short)(paramInt2 ^ 0xFFFFFFFF));
    }
    b(this.yb, paramInt1, paramInt2);
  }

  void b(boolean paramBoolean)
  {
    d(this.h >= 0 ? this.h : -1, this.nb - this.h, paramBoolean);
    this.h = this.nb;
    this.fb.b();
  }

  int e(int paramInt)
  {
    int i1 = 65535;
    if (i1 > this.gc - 5)
      i1 = this.gc - 5;
    while (true)
      if (this.fc <= 1)
      {
        i();
        if ((this.fc == 0) && (paramInt == 0))
          return 0;
        if (this.fc == 0)
          break;
      }
      else
      {
        this.nb += this.fc;
        this.fc = 0;
        int i2 = this.h + i1;
        if ((this.nb == 0) || (this.nb >= i2))
        {
          this.fc = (this.nb - i2);
          this.nb = i2;
          b(false);
          if (this.fb.avail_out == 0)
            return 0;
        }
        if (this.nb - this.h < this.kb - 262)
          continue;
        b(false);
        if (this.fb.avail_out == 0)
          return 0;
      }
    b(paramInt == 4);
    if (this.fb.avail_out == 0)
      return paramInt == 4 ? 2 : 0;
    return paramInt == 4 ? 3 : 1;
  }

  void c(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    b(0 + (paramBoolean ? 1 : 0), 3);
    b(paramInt1, paramInt2, true);
  }

  void d(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i3 = 0;
    int i1;
    int i2;
    if (this.j > 0)
    {
      if (this.ib == 2)
        e();
      this.hb.b(this);
      this.bb.b(this);
      i3 = h();
      i1 = this.rb + 3 + 7 >>> 3;
      i2 = this.ob + 3 + 7 >>> 3;
      if (i2 <= i1)
        i1 = i2;
    }
    else
    {
      i1 = i2 = paramInt2 + 5;
    }
    if ((paramInt2 + 4 <= i1) && (paramInt1 != -1))
    {
      c(paramInt1, paramInt2, paramBoolean);
    }
    else if (i2 == i1)
    {
      b(2 + (paramBoolean ? 1 : 0), 3);
      b(f.k, f.g);
    }
    else
    {
      b(4 + (paramBoolean ? 1 : 0), 3);
      b(this.hb.k + 1, this.bb.k + 1, i3 + 1);
      b(this.p, this.cb);
    }
    j();
    if (paramBoolean)
      f();
  }

  void i()
  {
    do
    {
      int i4 = this.x - this.fc - this.nb;
      if ((i4 == 0) && (this.nb == 0) && (this.fc == 0))
      {
        i4 = this.kb;
      }
      else if (i4 == -1)
      {
        i4--;
      }
      else if (this.nb >= this.kb + this.kb - 262)
      {
        System.arraycopy(this.yb, this.kb, this.yb, 0, this.kb);
        this.qb -= this.kb;
        this.nb -= this.kb;
        this.h -= this.kb;
        i1 = this.g;
        int i3 = i1;
        int i2;
        do
        {
          i3--;
          i2 = this.v[i3] & 0xFFFF;
          this.v[i3] = (i2 >= this.kb ? (short)(i2 - this.kb) : 0);
          i1--;
        }
        while (i1 != 0);
        i1 = this.kb;
        i3 = i1;
        do
        {
          i3--;
          i2 = this.jb[i3] & 0xFFFF;
          this.jb[i3] = (i2 >= this.kb ? (short)(i2 - this.kb) : 0);
          i1--;
        }
        while (i1 != 0);
        i4 += this.kb;
      }
      if (this.fb.avail_in == 0)
        return;
      int i1 = this.fb.b(this.yb, this.nb + this.fc, i4);
      this.fc += i1;
      if (this.fc < 3)
        continue;
      this.b = (this.yb[this.nb] & 0xFF);
      this.b = ((this.b << this.wb ^ this.yb[(this.nb + 1)] & 0xFF) & this.tb);
    }
    while ((this.fc < 262) && (this.fb.avail_in != 0));
  }

  int g(int paramInt)
  {
    int i1 = 0;
    while (true)
      if (this.fc < 262)
      {
        i();
        if ((this.fc < 262) && (paramInt == 0))
          return 0;
        if (this.fc == 0)
          break;
      }
      else
      {
        if (this.fc >= 3)
        {
          this.b = ((this.b << this.wb ^ this.yb[(this.nb + 2)] & 0xFF) & this.tb);
          i1 = this.v[this.b] & 0xFFFF;
          this.jb[(this.nb & this.u)] = this.v[this.b];
          this.v[this.b] = (short)this.nb;
        }
        if ((i1 != 0L) && ((this.nb - i1 & 0xFFFF) <= this.kb - 262) && (this.mb != 2))
          this.i = b(i1);
        boolean bool;
        if (this.i >= 3)
        {
          bool = c(this.nb - this.qb, this.i - 3);
          this.fc -= this.i;
          if ((this.i <= this.eb) && (this.fc >= 3))
          {
            this.i -= 1;
            do
            {
              this.nb += 1;
              this.b = ((this.b << this.wb ^ this.yb[(this.nb + 2)] & 0xFF) & this.tb);
              i1 = this.v[this.b] & 0xFFFF;
              this.jb[(this.nb & this.u)] = this.v[this.b];
              this.v[this.b] = (short)this.nb;
            }
            while (--this.i != 0);
            this.nb += 1;
          }
          else
          {
            this.nb += this.i;
            this.i = 0;
            this.b = (this.yb[this.nb] & 0xFF);
            this.b = ((this.b << this.wb ^ this.yb[(this.nb + 1)] & 0xFF) & this.tb);
          }
        }
        else
        {
          bool = c(0, this.yb[this.nb] & 0xFF);
          this.fc -= 1;
          this.nb += 1;
        }
        if (!bool)
          continue;
        b(false);
        if (this.fb.avail_out == 0)
          return 0;
      }
    b(paramInt == 4);
    if (this.fb.avail_out == 0)
    {
      if (paramInt == 4)
        return 2;
      return 0;
    }
    return paramInt == 4 ? 3 : 1;
  }

  int d(int paramInt)
  {
    int i1 = 0;
    boolean bool;
    while (true)
    {
      if (this.fc < 262)
      {
        i();
        if ((this.fc < 262) && (paramInt == 0))
          return 0;
        if (this.fc == 0)
          break;
      }
      if (this.fc >= 3)
      {
        this.b = ((this.b << this.wb ^ this.yb[(this.nb + 2)] & 0xFF) & this.tb);
        i1 = this.v[this.b] & 0xFFFF;
        this.jb[(this.nb & this.u)] = this.v[this.b];
        this.v[this.b] = (short)this.nb;
      }
      this.gb = this.i;
      this.o = this.qb;
      this.i = 2;
      if ((i1 != 0) && (this.gb < this.eb) && ((this.nb - i1 & 0xFFFF) <= this.kb - 262))
      {
        if (this.mb != 2)
          this.i = b(i1);
        if ((this.i <= 5) && ((this.mb == 1) || ((this.i == 3) && (this.nb - this.qb > 4096))))
          this.i = 2;
      }
      if ((this.gb >= 3) && (this.i <= this.gb))
      {
        int i2 = this.nb + this.fc - 3;
        bool = c(this.nb - 1 - this.o, this.gb - 3);
        this.fc -= this.gb - 1;
        this.gb -= 2;
        do
        {
          if (++this.nb > i2)
            continue;
          this.b = ((this.b << this.wb ^ this.yb[(this.nb + 2)] & 0xFF) & this.tb);
          i1 = this.v[this.b] & 0xFFFF;
          this.jb[(this.nb & this.u)] = this.v[this.b];
          this.v[this.b] = (short)this.nb;
        }
        while (--this.gb != 0);
        this.ac = 0;
        this.i = 2;
        this.nb += 1;
        if (bool)
        {
          b(false);
          if (this.fb.avail_out == 0)
            return 0;
        }
        continue;
      }
      if (this.ac != 0)
      {
        bool = c(0, this.yb[(this.nb - 1)] & 0xFF);
        if (bool)
          b(false);
        this.nb += 1;
        this.fc -= 1;
        if (this.fb.avail_out == 0)
          return 0;
      }
      this.ac = 1;
      this.nb += 1;
      this.fc -= 1;
    }
    if (this.ac != 0)
    {
      bool = c(0, this.yb[(this.nb - 1)] & 0xFF);
      this.ac = 0;
    }
    b(paramInt == 4);
    if (this.fb.avail_out == 0)
    {
      if (paramInt == 4)
        return 2;
      return 0;
    }
    return paramInt == 4 ? 3 : 1;
  }

  int b(int paramInt)
  {
    int i1 = this.e;
    int i2 = this.nb;
    int i5 = this.gb;
    int i6 = this.nb > this.kb - 262 ? this.nb - (this.kb - 262) : 0;
    int i7 = this.ub;
    int i8 = this.u;
    int i9 = this.nb + 258;
    int i10 = this.yb[(i2 + i5 - 1)];
    int i11 = this.yb[(i2 + i5)];
    if (this.gb >= this.zb)
      i1 >>= 2;
    if (i7 > this.fc)
      i7 = this.fc;
    do
    {
      int i3 = paramInt;
      if ((this.yb[(i3 + i5)] == i11) && (this.yb[(i3 + i5 - 1)] == i10) && (this.yb[i3] == this.yb[i2]))
      {
        i3++;
        if (this.yb[i3] == this.yb[(i2 + 1)])
        {
          i2 += 2;
          i3++;
          do
          {
            i2++;
            i3++;
            if (this.yb[i2] != this.yb[i3])
              break;
            i2++;
            i3++;
            if (this.yb[i2] != this.yb[i3])
              break;
            i2++;
            i3++;
            if (this.yb[i2] != this.yb[i3])
              break;
            i2++;
            i3++;
            if (this.yb[i2] != this.yb[i3])
              break;
            i2++;
            i3++;
            if (this.yb[i2] != this.yb[i3])
              break;
            i2++;
            i3++;
            if (this.yb[i2] != this.yb[i3])
              break;
            i2++;
            i3++;
            if (this.yb[i2] != this.yb[i3])
              break;
            i2++;
            i3++;
          }
          while ((this.yb[i2] == this.yb[i3]) && (i2 < i9));
          int i4 = 258 - (i9 - i2);
          i2 = i9 - 258;
          if (i4 > i5)
          {
            this.qb = paramInt;
            i5 = i4;
            if (i4 >= i7)
              break;
            i10 = this.yb[(i2 + i5 - 1)];
            i11 = this.yb[(i2 + i5)];
          }
        }
      }
      if ((paramInt = this.jb[(paramInt & i8)] & 0xFFFF) <= i6)
        break;
      i1--;
    }
    while (i1 != 0);
    if (i5 <= this.fc)
      return i5;
    return this.fc;
  }

  int c(ZStream paramZStream, int paramInt1, int paramInt2)
  {
    return b(paramZStream, paramInt1, 8, paramInt2, 8, 0);
  }

  int b(ZStream paramZStream, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
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
    paramZStream.d = this;
    this.hc = i1;
    this.db = paramInt3;
    this.kb = (1 << this.db);
    this.u = (this.kb - 1);
    this.dc = (paramInt4 + 7);
    this.g = (1 << this.dc);
    this.tb = (this.g - 1);
    this.wb = ((this.dc + 3 - 1) / 3);
    this.yb = new byte[this.kb * 2];
    this.jb = new short[this.kb];
    this.v = new short[this.g];
    this.sb = (1 << paramInt4 + 6);
    this.bc = new byte[this.sb * 4];
    this.gc = (this.sb * 4);
    this.d = (this.sb / 2);
    this.cc = (3 * this.sb);
    this.j = paramInt1;
    this.mb = paramInt5;
    this.ec = (byte)paramInt2;
    return b(paramZStream);
  }

  int b(ZStream paramZStream)
  {
    paramZStream.total_in = (paramZStream.total_out = 0L);
    paramZStream.msg = null;
    paramZStream.e = 2;
    this.q = 0;
    this.l = 0;
    if (this.hc < 0)
      this.hc = 0;
    this.lb = (this.hc != 0 ? 113 : 42);
    paramZStream.adler = paramZStream.b.b(0L, null, 0, 0);
    this.y = 0;
    c();
    g();
    return 0;
  }

  int k()
  {
    if ((this.lb != 42) && (this.lb != 113) && (this.lb != 666))
      return -2;
    this.bc = null;
    this.v = null;
    this.jb = null;
    this.yb = null;
    return this.lb == 113 ? -3 : 0;
  }

  int b(ZStream paramZStream, int paramInt1, int paramInt2)
  {
    int i1 = 0;
    if (paramInt1 == -1)
      paramInt1 = 6;
    if ((paramInt1 < 0) || (paramInt1 > 9) || (paramInt2 < 0) || (paramInt2 > 2))
      return -2;
    if ((f[this.j].d != f[paramInt1].d) && (paramZStream.total_in != 0L))
      i1 = paramZStream.deflate(1);
    if (this.j != paramInt1)
    {
      this.j = paramInt1;
      this.eb = f[this.j].f;
      this.zb = f[this.j].b;
      this.ub = f[this.j].c;
      this.e = f[this.j].e;
    }
    this.mb = paramInt2;
    return i1;
  }

  int b(ZStream paramZStream, byte[] paramArrayOfByte, int paramInt)
  {
    int i1 = paramInt;
    int i2 = 0;
    if ((paramArrayOfByte == null) || (this.lb != 42))
      return -2;
    paramZStream.adler = paramZStream.b.b(paramZStream.adler, paramArrayOfByte, 0, paramInt);
    if (i1 < 3)
      return 0;
    if (i1 > this.kb - 262)
    {
      i1 = this.kb - 262;
      i2 = paramInt - i1;
    }
    System.arraycopy(paramArrayOfByte, i2, this.yb, 0, i1);
    this.nb = i1;
    this.h = i1;
    this.b = (this.yb[0] & 0xFF);
    this.b = ((this.b << this.wb ^ this.yb[1] & 0xFF) & this.tb);
    for (int i3 = 0; i3 <= i1 - 3; i3++)
    {
      this.b = ((this.b << this.wb ^ this.yb[(i3 + 2)] & 0xFF) & this.tb);
      this.jb[(i3 & this.u)] = this.v[this.b];
      this.v[this.b] = (short)i3;
    }
    return 0;
  }

  int b(ZStream paramZStream, int paramInt)
  {
    if ((paramInt > 4) || (paramInt < 0))
      return -2;
    if ((paramZStream.next_out == null) || ((paramZStream.next_in == null) && (paramZStream.avail_in != 0)) || ((this.lb == 666) && (paramInt != 4)))
    {
      paramZStream.msg = t[4];
      return -2;
    }
    if (paramZStream.avail_out == 0)
    {
      paramZStream.msg = t[7];
      return -5;
    }
    this.fb = paramZStream;
    int i1 = this.y;
    this.y = paramInt;
    int i2;
    int i3;
    if (this.lb == 42)
    {
      i2 = 8 + (this.db - 8 << 4) << 8;
      i3 = (this.j - 1 & 0xFF) >> 1;
      if (i3 > 3)
        i3 = 3;
      i2 |= i3 << 6;
      if (this.nb != 0)
        i2 |= 32;
      i2 += 31 - i2 % 31;
      this.lb = 113;
      f(i2);
      if (this.nb != 0)
      {
        f((int)(paramZStream.adler >>> 16));
        f((int)(paramZStream.adler & 0xFFFF));
      }
      paramZStream.adler = paramZStream.b.b(0L, null, 0, 0);
    }
    if (this.q != 0)
    {
      paramZStream.b();
      if (paramZStream.avail_out == 0)
      {
        this.y = -1;
        return 0;
      }
    }
    else if ((paramZStream.avail_in == 0) && (paramInt <= i1) && (paramInt != 4))
    {
      paramZStream.msg = t[7];
      return -5;
    }
    if ((this.lb == 666) && (paramZStream.avail_in != 0))
    {
      paramZStream.msg = t[7];
      return -5;
    }
    if ((paramZStream.avail_in != 0) || (this.fc != 0) || ((paramInt != 0) && (this.lb != 666)))
    {
      i2 = -1;
      switch (f[this.j].d)
      {
      case 0:
        i2 = e(paramInt);
        break;
      case 1:
        i2 = g(paramInt);
        break;
      case 2:
        i2 = d(paramInt);
        break;
      }
      if ((i2 == 2) || (i2 == 3))
        this.lb = 666;
      if ((i2 == 0) || (i2 == 2))
      {
        if (paramZStream.avail_out == 0)
          this.y = -1;
        return 0;
      }
      if (i2 == 1)
      {
        if (paramInt == 1)
        {
          d();
        }
        else
        {
          c(0, 0, false);
          if (paramInt == 3)
            for (i3 = 0; i3 < this.g; i3++)
              this.v[i3] = 0;
        }
        paramZStream.b();
        if (paramZStream.avail_out == 0)
        {
          this.y = -1;
          return 0;
        }
      }
    }
    if (paramInt != 4)
      return 0;
    if (this.hc != 0)
      return 1;
    f((int)(paramZStream.adler >>> 16));
    f((int)(paramZStream.adler & 0xFFFF));
    paramZStream.b();
    this.hc = -1;
    return this.q != 0 ? 0 : 1;
  }

  static
  {
    f[0] = new _b(0, 0, 0, 0, 0);
    f[1] = new _b(4, 4, 8, 4, 1);
    f[2] = new _b(4, 5, 16, 8, 1);
    f[3] = new _b(4, 6, 32, 32, 1);
    f[4] = new _b(4, 4, 16, 16, 2);
    f[5] = new _b(8, 16, 32, 32, 2);
    f[6] = new _b(8, 16, 128, 128, 2);
    f[7] = new _b(8, 32, 128, 256, 2);
    f[8] = new _b(32, 128, 258, 1024, 2);
    f[9] = new _b(32, 258, 258, 4096, 2);
    t = new String[] { "need dictionary", "stream end", "", "file error", "stream error", "data error", "insufficient memory", "buffer error", "incompatible version", "" };
  }

  static class _b
  {
    int b;
    int f;
    int c;
    int e;
    int d;

    _b(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      this.b = paramInt1;
      this.f = paramInt2;
      this.c = paramInt3;
      this.e = paramInt4;
      this.d = paramInt5;
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.zlib.Deflate
 * JD-Core Version:    0.6.0
 */