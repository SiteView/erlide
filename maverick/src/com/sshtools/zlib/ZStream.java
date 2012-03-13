package com.sshtools.zlib;

import java.io.PrintStream;

public final class ZStream
{
  public byte[] next_in;
  public int next_in_index;
  public int avail_in;
  public long total_in;
  public byte[] next_out;
  public int next_out_index;
  public int avail_out;
  public long total_out;
  public String msg;
  Deflate d;
  e c;
  int e;
  public long adler;
  b b = new b();

  public int inflateInit()
  {
    return inflateInit(15);
  }

  public int inflateInit(int paramInt)
  {
    this.c = new e();
    return this.c.c(this, paramInt);
  }

  public int inflate(int paramInt)
  {
    if (this.c == null)
      return -2;
    return this.c.b(this, paramInt);
  }

  public int inflateEnd()
  {
    if (this.c == null)
      return -2;
    int i = this.c.b(this);
    this.c = null;
    return i;
  }

  public int inflateSync()
  {
    if (this.c == null)
      return -2;
    return this.c.d(this);
  }

  public int inflateSetDictionary(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.c == null)
      return -2;
    return this.c.b(this, paramArrayOfByte, paramInt);
  }

  public int deflateInit(int paramInt)
  {
    return deflateInit(paramInt, 15);
  }

  public int deflateInit(int paramInt1, int paramInt2)
  {
    this.d = new Deflate();
    return this.d.c(this, paramInt1, paramInt2);
  }

  public int deflate(int paramInt)
  {
    if (this.d == null)
      return -2;
    return this.d.b(this, paramInt);
  }

  public int deflateEnd()
  {
    if (this.d == null)
      return -2;
    int i = this.d.k();
    this.d = null;
    return i;
  }

  public int deflateParams(int paramInt1, int paramInt2)
  {
    if (this.d == null)
      return -2;
    return this.d.b(this, paramInt1, paramInt2);
  }

  public int deflateSetDictionary(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.d == null)
      return -2;
    return this.d.b(this, paramArrayOfByte, paramInt);
  }

  void b()
  {
    int i = this.d.q;
    if (i > this.avail_out)
      i = this.avail_out;
    if (i == 0)
      return;
    if ((this.d.bc.length <= this.d.l) || (this.next_out.length <= this.next_out_index) || (this.d.bc.length < this.d.l + i) || (this.next_out.length < this.next_out_index + i))
    {
      System.out.println(this.d.bc.length + ", " + this.d.l + ", " + this.next_out.length + ", " + this.next_out_index + ", " + i);
      System.out.println("avail_out=" + this.avail_out);
    }
    System.arraycopy(this.d.bc, this.d.l, this.next_out, this.next_out_index, i);
    this.next_out_index += i;
    this.d.l += i;
    this.total_out += i;
    this.avail_out -= i;
    this.d.q -= i;
    if (this.d.q == 0)
      this.d.l = 0;
  }

  int b(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = this.avail_in;
    if (i > paramInt2)
      i = paramInt2;
    if (i == 0)
      return 0;
    this.avail_in -= i;
    if (this.d.hc == 0)
      this.adler = this.b.b(this.adler, this.next_in, this.next_in_index, i);
    System.arraycopy(this.next_in, this.next_in_index, paramArrayOfByte, paramInt1, i);
    this.next_in_index += i;
    this.total_in += i;
    return i;
  }

  public void free()
  {
    this.next_in = null;
    this.next_out = null;
    this.msg = null;
    this.b = null;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.zlib.ZStream
 * JD-Core Version:    0.6.0
 */