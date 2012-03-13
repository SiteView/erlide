package com.maverick.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EOLProcessor
{
  public static final int TEXT_SYSTEM = 0;
  public static final int TEXT_WINDOWS = 1;
  public static final int TEXT_DOS = 1;
  public static final int TEXT_CRLF = 1;
  public static final int TEXT_UNIX = 2;
  public static final int TEXT_LF = 2;
  public static final int TEXT_MAC = 3;
  public static final int TEXT_CR = 3;
  public static final int TEXT_ALL = 4;
  byte[] d;
  String i = System.getProperty("line.separator");
  boolean c = false;
  boolean g = false;
  boolean h = false;
  boolean f = false;
  boolean b = false;
  OutputStream e;

  public EOLProcessor(int paramInt1, int paramInt2, OutputStream paramOutputStream)
    throws IOException
  {
    this.e = new BufferedOutputStream(paramOutputStream);
    switch (paramInt1)
    {
    case 1:
      this.h = true;
      break;
    case 3:
      this.c = true;
      break;
    case 2:
      this.g = true;
      break;
    case 4:
      this.c = true;
      this.g = true;
      this.h = true;
      break;
    case 0:
      byte[] arrayOfByte = this.i.getBytes();
      if ((arrayOfByte.length == 2) && (arrayOfByte[0] == 13) && (arrayOfByte[1] == 10))
        this.h = true;
      else if ((arrayOfByte.length == 1) && (arrayOfByte[0] == 13))
        this.c = true;
      else if ((arrayOfByte.length == 1) && (arrayOfByte[0] == 10))
        this.g = true;
      else
        throw new IOException("Unsupported system EOL mode");
    default:
      throw new IllegalArgumentException("Unknown text style: " + paramInt2);
    }
    switch (paramInt2)
    {
    case 0:
      this.d = this.i.getBytes();
      break;
    case 1:
      this.d = new byte[] { 13, 10 };
      break;
    case 3:
      this.d = new byte[] { 13 };
      break;
    case 2:
      this.d = new byte[] { 10 };
      break;
    case 4:
      throw new IllegalArgumentException("TEXT_ALL cannot be used for an output style");
    default:
      throw new IllegalArgumentException("Unknown text style: " + paramInt2);
    }
  }

  public boolean hasBinary()
  {
    return this.f;
  }

  public void close()
    throws IOException
  {
    if ((this.b) && (!this.c))
      this.e.write(13);
    this.e.close();
  }

  public void processBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2), 32768);
    int j;
    while ((j = localBufferedInputStream.read()) != -1)
    {
      if (j == 13)
      {
        if (this.h)
        {
          localBufferedInputStream.mark(1);
          int k = localBufferedInputStream.read();
          if (k == -1)
          {
            this.b = true;
            break;
          }
          if (k == 10)
          {
            this.e.write(this.d);
          }
          else
          {
            localBufferedInputStream.reset();
            if (this.c)
              this.e.write(this.d);
            else
              this.e.write(j);
          }
          continue;
        }
        if (this.c)
        {
          this.e.write(this.d);
          continue;
        }
        this.e.write(j);
        continue;
      }
      if (j == 10)
      {
        if (this.b)
        {
          this.e.write(this.d);
          this.b = false;
          continue;
        }
        if (this.g)
        {
          this.e.write(this.d);
          continue;
        }
        this.e.write(j);
        continue;
      }
      if (this.b)
        if (this.c)
          this.e.write(this.d);
        else
          this.e.write(j);
      if ((j != 116) && (j != 12) && ((j & 0xFF) < 32))
        this.f = true;
      this.e.write(j);
    }
    this.e.flush();
  }

  public static OutputStream createOutputStream(int paramInt1, int paramInt2, OutputStream paramOutputStream)
    throws IOException
  {
    return new b(paramInt1, paramInt2, paramOutputStream);
  }

  public static InputStream createInputStream(int paramInt1, int paramInt2, InputStream paramInputStream)
    throws IOException
  {
    return new c(paramInt1, paramInt2, paramInputStream);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.EOLProcessor
 * JD-Core Version:    0.6.0
 */