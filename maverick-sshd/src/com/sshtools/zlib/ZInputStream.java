package com.sshtools.zlib;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZInputStream extends FilterInputStream
{
  protected ZStream z = new ZStream();
  protected int bufsize = 512;
  protected int flush = 0;
  protected byte[] buf = new byte[this.bufsize];
  protected byte[] buf1 = new byte[1];
  protected boolean compress;
  private InputStream A = null;
  private boolean B = false;

  public ZInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
    this.A = paramInputStream;
    this.z.inflateInit();
    this.compress = false;
    this.z.next_in = this.buf;
    this.z.next_in_index = 0;
    this.z.avail_in = 0;
  }

  public ZInputStream(InputStream paramInputStream, int paramInt)
  {
    super(paramInputStream);
    this.A = paramInputStream;
    this.z.deflateInit(paramInt);
    this.compress = true;
    this.z.next_in = this.buf;
    this.z.next_in_index = 0;
    this.z.avail_in = 0;
  }

  public int read()
    throws IOException
  {
    if (read(this.buf1, 0, 1) == -1)
      return -1;
    return this.buf1[0] & 0xFF;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 == 0)
      return 0;
    this.z.next_out = paramArrayOfByte;
    this.z.next_out_index = paramInt1;
    this.z.avail_out = paramInt2;
    int i;
    do
    {
      if ((this.z.avail_in == 0) && (!this.B))
      {
        this.z.next_in_index = 0;
        this.z.avail_in = this.A.read(this.buf, 0, this.bufsize);
        if (this.z.avail_in == -1)
        {
          this.z.avail_in = 0;
          this.B = true;
        }
      }
      if (this.compress)
        i = this.z.deflate(this.flush);
      else
        i = this.z.inflate(this.flush);
      if ((this.B) && (i == -5))
        return -1;
      if ((i != 0) && (i != 1))
        throw new ZStreamException((this.compress ? "de" : "in") + "flating: " + this.z.msg);
      if (((this.B) || (i == 1)) && (this.z.avail_out == paramInt2))
        return -1;
    }
    while ((this.z.avail_out == paramInt2) && (i == 0));
    return paramInt2 - this.z.avail_out;
  }

  public long skip(long paramLong)
    throws IOException
  {
    int i = 512;
    if (paramLong < i)
      i = (int)paramLong;
    byte[] arrayOfByte = new byte[i];
    return read(arrayOfByte);
  }

  public int getFlushMode()
  {
    return this.flush;
  }

  public void setFlushMode(int paramInt)
  {
    this.flush = paramInt;
  }

  public long getTotalIn()
  {
    return this.z.total_in;
  }

  public long getTotalOut()
  {
    return this.z.total_out;
  }

  public void close()
    throws IOException
  {
    this.A.close();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.ZInputStream
 * JD-Core Version:    0.6.0
 */