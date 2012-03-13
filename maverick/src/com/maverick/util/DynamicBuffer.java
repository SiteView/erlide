package com.maverick.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

public class DynamicBuffer
{
  protected static final int DEFAULT_BUFFER_SIZE = 32768;
  protected byte[] buf = new byte[32768];
  protected int writepos = 0;
  protected int readpos = 0;
  protected InputStream in = new _c();
  protected OutputStream out = new _b();
  private boolean b = false;
  private int c = 5000;

  public InputStream getInputStream()
  {
    return this.in;
  }

  public OutputStream getOutputStream()
  {
    return this.out;
  }

  private synchronized void b(int paramInt)
  {
    if (paramInt > this.buf.length - this.writepos)
    {
      System.arraycopy(this.buf, this.readpos, this.buf, 0, this.writepos - this.readpos);
      this.writepos -= this.readpos;
      this.readpos = 0;
    }
    if (paramInt > this.buf.length - this.writepos)
    {
      byte[] arrayOfByte = new byte[this.buf.length + 32768];
      System.arraycopy(this.buf, 0, arrayOfByte, 0, this.writepos - this.readpos);
      this.buf = arrayOfByte;
    }
  }

  protected synchronized int available()
  {
    return this.b ? -1 : this.writepos - this.readpos > 0 ? this.writepos - this.readpos : 0;
  }

  private synchronized void b()
    throws InterruptedException
  {
    if (!this.b)
      while ((this.readpos >= this.writepos) && (!this.b))
        wait(this.c);
  }

  public synchronized void close()
  {
    if (!this.b)
    {
      this.b = true;
      notifyAll();
    }
  }

  protected synchronized void write(int paramInt)
    throws IOException
  {
    if (this.b)
      throw new IOException("The buffer is closed");
    b(1);
    this.buf[this.writepos] = (byte)paramInt;
    this.writepos += 1;
    notifyAll();
  }

  protected synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.b)
      throw new IOException("The buffer is closed");
    b(paramInt2);
    System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.writepos, paramInt2);
    this.writepos += paramInt2;
    notifyAll();
  }

  public void setBlockInterrupt(int paramInt)
  {
    this.c = paramInt;
  }

  protected synchronized int read()
    throws IOException
  {
    try
    {
      b();
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new InterruptedIOException("The blocking operation was interrupted");
    }
    if ((this.b) && (available() <= 0))
      return -1;
    return this.buf[(this.readpos++)];
  }

  protected synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      b();
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new InterruptedIOException("The blocking operation was interrupted");
    }
    if ((this.b) && (available() <= 0))
      return -1;
    int i = paramInt2 > this.writepos - this.readpos ? this.writepos - this.readpos : paramInt2;
    System.arraycopy(this.buf, this.readpos, paramArrayOfByte, paramInt1, i);
    this.readpos += i;
    return i;
  }

  protected synchronized void flush()
    throws IOException
  {
    notifyAll();
  }

  class _b extends OutputStream
  {
    _b()
    {
    }

    public void write(int paramInt)
      throws IOException
    {
      DynamicBuffer.this.write(paramInt);
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      DynamicBuffer.this.write(paramArrayOfByte, paramInt1, paramInt2);
    }

    public void flush()
      throws IOException
    {
      DynamicBuffer.this.flush();
    }

    public void close()
    {
      DynamicBuffer.this.close();
    }
  }

  class _c extends InputStream
  {
    _c()
    {
    }

    public int read()
      throws IOException
    {
      return DynamicBuffer.this.read();
    }

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      return DynamicBuffer.this.read(paramArrayOfByte, paramInt1, paramInt2);
    }

    public int available()
    {
      return DynamicBuffer.this.available();
    }

    public void close()
    {
      DynamicBuffer.this.close();
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.DynamicBuffer
 * JD-Core Version:    0.6.0
 */