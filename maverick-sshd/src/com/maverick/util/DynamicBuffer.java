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
  protected InputStream in = new _B();
  protected OutputStream out = new _A();
  private boolean A = false;
  private int B = 5000;

  public InputStream getInputStream()
  {
    return this.in;
  }

  public OutputStream getOutputStream()
  {
    return this.out;
  }

  private synchronized void A(int paramInt)
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
    return this.A ? -1 : this.writepos - this.readpos > 0 ? this.writepos - this.readpos : 0;
  }

  private synchronized void A()
    throws InterruptedException
  {
    if (!this.A)
      while ((this.readpos >= this.writepos) && (!this.A))
        wait(this.B);
  }

  public synchronized void close()
  {
    if (!this.A)
    {
      this.A = true;
      notifyAll();
    }
  }

  protected synchronized void write(int paramInt)
    throws IOException
  {
    if (this.A)
      throw new IOException("The buffer is closed");
    A(1);
    this.buf[this.writepos] = (byte)paramInt;
    this.writepos += 1;
    notifyAll();
  }

  protected synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.A)
      throw new IOException("The buffer is closed");
    A(paramInt2);
    System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.writepos, paramInt2);
    this.writepos += paramInt2;
    notifyAll();
  }

  public void setBlockInterrupt(int paramInt)
  {
    this.B = paramInt;
  }

  protected synchronized int read()
    throws IOException
  {
    try
    {
      A();
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new InterruptedIOException("The blocking operation was interrupted");
    }
    if ((this.A) && (available() <= 0))
      return -1;
    return this.buf[(this.readpos++)];
  }

  protected synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      A();
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new InterruptedIOException("The blocking operation was interrupted");
    }
    if ((this.A) && (available() <= 0))
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

  class _A extends OutputStream
  {
    _A()
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

  class _B extends InputStream
  {
    _B()
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

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.DynamicBuffer
 * JD-Core Version:    0.6.0
 */