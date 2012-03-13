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
  byte[] C;
  String H = System.getProperty("line.separator");
  boolean B = false;
  boolean F = false;
  boolean G = false;
  boolean E = false;
  boolean A = false;
  OutputStream D;

  public EOLProcessor(int paramInt1, int paramInt2, OutputStream paramOutputStream)
    throws IOException
  {
    this.D = new BufferedOutputStream(paramOutputStream);
    switch (paramInt1)
    {
    case 1:
      this.G = true;
      break;
    case 3:
      this.B = true;
      break;
    case 2:
      this.F = true;
      break;
    case 4:
      this.B = true;
      this.F = true;
      this.G = true;
      break;
    case 0:
      byte[] arrayOfByte = this.H.getBytes();
      if ((arrayOfByte.length == 2) && (arrayOfByte[0] == 13) && (arrayOfByte[1] == 10))
        this.G = true;
      else if ((arrayOfByte.length == 1) && (arrayOfByte[0] == 13))
        this.B = true;
      else if ((arrayOfByte.length == 1) && (arrayOfByte[0] == 10))
        this.F = true;
      else
        throw new IOException("Unsupported system EOL mode");
    default:
      throw new IllegalArgumentException("Unknown text style: " + paramInt2);
    }
    switch (paramInt2)
    {
    case 0:
      this.C = this.H.getBytes();
      break;
    case 1:
      this.C = new byte[] { 13, 10 };
      break;
    case 3:
      this.C = new byte[] { 13 };
      break;
    case 2:
      this.C = new byte[] { 10 };
      break;
    case 4:
      throw new IllegalArgumentException("TEXT_ALL cannot be used for an output style");
    default:
      throw new IllegalArgumentException("Unknown text style: " + paramInt2);
    }
  }

  public boolean hasBinary()
  {
    return this.E;
  }

  public void close()
    throws IOException
  {
    if ((this.A) && (!this.B))
      this.D.write(13);
    this.D.close();
  }

  public void processBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2), 32768);
    int i;
    while ((i = localBufferedInputStream.read()) != -1)
    {
      if (i == 13)
      {
        if (this.G)
        {
          localBufferedInputStream.mark(1);
          int j = localBufferedInputStream.read();
          if (j == -1)
          {
            this.A = true;
            break;
          }
          if (j == 10)
          {
            this.D.write(this.C);
          }
          else
          {
            localBufferedInputStream.reset();
            if (this.B)
              this.D.write(this.C);
            else
              this.D.write(i);
          }
          continue;
        }
        if (this.B)
        {
          this.D.write(this.C);
          continue;
        }
        this.D.write(i);
        continue;
      }
      if (i == 10)
      {
        if (this.A)
        {
          this.D.write(this.C);
          this.A = false;
          continue;
        }
        if (this.F)
        {
          this.D.write(this.C);
          continue;
        }
        this.D.write(i);
        continue;
      }
      if (this.A)
        if (this.B)
          this.D.write(this.C);
        else
          this.D.write(i);
      if ((i != 116) && (i != 12) && ((i & 0xFF) < 32))
        this.E = true;
      this.D.write(i);
    }
    this.D.flush();
  }

  public static OutputStream createOutputStream(int paramInt1, int paramInt2, OutputStream paramOutputStream)
    throws IOException
  {
    return new A(paramInt1, paramInt2, paramOutputStream);
  }

  public static InputStream createInputStream(int paramInt1, int paramInt2, InputStream paramInputStream)
    throws IOException
  {
    return new B(paramInt1, paramInt2, paramInputStream);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.EOLProcessor
 * JD-Core Version:    0.6.0
 */