package com.sshtools.zlib;

import java.io.IOException;
import java.io.OutputStream;

public class ZOutputStream extends OutputStream
{
  protected ZStream z = new ZStream();
  protected int bufsize = 512;
  protected int flush = 0;
  protected byte[] buf = new byte[this.bufsize];
  protected byte[] buf1 = new byte[1];
  protected boolean compress;
  private OutputStream b;

  public ZOutputStream(OutputStream paramOutputStream)
  {
    this.b = paramOutputStream;
    this.z.inflateInit();
    this.compress = false;
  }

  public ZOutputStream(OutputStream paramOutputStream, int paramInt)
  {
    this.b = paramOutputStream;
    this.z.deflateInit(paramInt);
    this.compress = true;
  }

  public void write(int paramInt)
    throws IOException
  {
    this.buf1[0] = (byte)paramInt;
    write(this.buf1, 0, 1);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 == 0)
      return;
    this.z.next_in = paramArrayOfByte;
    this.z.next_in_index = paramInt1;
    this.z.avail_in = paramInt2;
    do
    {
      this.z.next_out = this.buf;
      this.z.next_out_index = 0;
      this.z.avail_out = this.bufsize;
      int i;
      if (this.compress)
        i = this.z.deflate(this.flush);
      else
        i = this.z.inflate(this.flush);
      if (i != 0)
        throw new ZStreamException((this.compress ? "de" : "in") + "flating: " + this.z.msg);
      this.b.write(this.buf, 0, this.bufsize - this.z.avail_out);
    }
    while ((this.z.avail_in > 0) || (this.z.avail_out == 0));
  }

  public int getFlushMode()
  {
    return this.flush;
  }

  public void setFlushMode(int paramInt)
  {
    this.flush = paramInt;
  }

  public void finish()
    throws IOException
  {
    do
    {
      this.z.next_out = this.buf;
      this.z.next_out_index = 0;
      this.z.avail_out = this.bufsize;
      int i;
      if (this.compress)
        i = this.z.deflate(4);
      else
        i = this.z.inflate(4);
      if ((i != 1) && (i != 0))
        throw new ZStreamException((this.compress ? "de" : "in") + "flating: " + this.z.msg);
      if (this.bufsize - this.z.avail_out <= 0)
        continue;
      this.b.write(this.buf, 0, this.bufsize - this.z.avail_out);
    }
    while ((this.z.avail_in > 0) || (this.z.avail_out == 0));
    flush();
  }

  public void end()
  {
    if (this.z == null)
      return;
    if (this.compress)
      this.z.deflateEnd();
    else
      this.z.inflateEnd();
    this.z.free();
    this.z = null;
  }

  public void close()
    throws IOException
  {
    try
    {
      try
      {
        finish();
      }
      catch (IOException localIOException)
      {
      }
    }
    finally
    {
      end();
      this.b.close();
      this.b = null;
    }
  }

  public long getTotalIn()
  {
    return this.z.total_in;
  }

  public long getTotalOut()
  {
    return this.z.total_out;
  }

  public void flush()
    throws IOException
  {
    this.b.flush();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.zlib.ZOutputStream
 * JD-Core Version:    0.6.0
 */