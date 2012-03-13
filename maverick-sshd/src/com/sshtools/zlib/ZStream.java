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
  Deflate C;
  D B;
  int D;
  public long adler;
  A A = new A();

  public int inflateInit()
  {
    return inflateInit(15);
  }

  public int inflateInit(int paramInt)
  {
    this.B = new D();
    return this.B.B(this, paramInt);
  }

  public int inflate(int paramInt)
  {
    if (this.B == null)
      return -2;
    return this.B.A(this, paramInt);
  }

  public int inflateEnd()
  {
    if (this.B == null)
      return -2;
    int i = this.B.A(this);
    this.B = null;
    return i;
  }

  public int inflateSync()
  {
    if (this.B == null)
      return -2;
    return this.B.C(this);
  }

  public int inflateSetDictionary(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.B == null)
      return -2;
    return this.B.A(this, paramArrayOfByte, paramInt);
  }

  public int deflateInit(int paramInt)
  {
    return deflateInit(paramInt, 15);
  }

  public int deflateInit(int paramInt1, int paramInt2)
  {
    this.C = new Deflate();
    return this.C.B(this, paramInt1, paramInt2);
  }

  public int deflate(int paramInt)
  {
    if (this.C == null)
      return -2;
    return this.C.A(this, paramInt);
  }

  public int deflateEnd()
  {
    if (this.C == null)
      return -2;
    int i = this.C.J();
    this.C = null;
    return i;
  }

  public int deflateParams(int paramInt1, int paramInt2)
  {
    if (this.C == null)
      return -2;
    return this.C.A(this, paramInt1, paramInt2);
  }

  public int deflateSetDictionary(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.C == null)
      return -2;
    return this.C.A(this, paramArrayOfByte, paramInt);
  }

  void A()
  {
    int i = this.C.P;
    if (i > this.avail_out)
      i = this.avail_out;
    if (i == 0)
      return;
    if ((this.C.z.length <= this.C.K) || (this.next_out.length <= this.next_out_index) || (this.C.z.length < this.C.K + i) || (this.next_out.length < this.next_out_index + i))
    {
      System.out.println(this.C.z.length + ", " + this.C.K + ", " + this.next_out.length + ", " + this.next_out_index + ", " + i);
      System.out.println("avail_out=" + this.avail_out);
    }
    System.arraycopy(this.C.z, this.C.K, this.next_out, this.next_out_index, i);
    this.next_out_index += i;
    this.C.K += i;
    this.total_out += i;
    this.avail_out -= i;
    this.C.P -= i;
    if (this.C.P == 0)
      this.C.K = 0;
  }

  int A(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = this.avail_in;
    if (i > paramInt2)
      i = paramInt2;
    if (i == 0)
      return 0;
    this.avail_in -= i;
    if (this.C.µ == 0)
      this.adler = this.A.A(this.adler, this.next_in, this.next_in_index, i);
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
    this.A = null;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.ZStream
 * JD-Core Version:    0.6.0
 */