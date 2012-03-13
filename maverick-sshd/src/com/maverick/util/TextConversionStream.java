package com.maverick.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TextConversionStream extends FilterOutputStream
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
  String G = System.getProperty("line.separator");
  boolean B;
  boolean E;
  boolean F;
  boolean D = false;
  boolean A = false;

  public TextConversionStream(int paramInt1, int paramInt2, OutputStream paramOutputStream)
  {
    super(paramOutputStream);
    switch (paramInt1)
    {
    case 1:
      this.B = false;
      this.E = false;
      this.F = true;
      break;
    case 3:
      this.B = true;
      this.E = false;
      this.F = false;
      break;
    case 2:
      this.B = false;
      this.E = true;
      this.F = false;
      break;
    case 4:
      this.B = true;
      this.E = true;
      this.F = true;
      break;
    default:
      throw new IllegalArgumentException("Unknown text style: " + paramInt2);
    }
    switch (paramInt2)
    {
    case 0:
      this.C = this.G.getBytes();
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
    return this.D;
  }

  public void write(int paramInt)
    throws IOException
  {
    write(new byte[] { (byte)paramInt });
  }

  public void close()
    throws IOException
  {
    if ((this.A) && (!this.B))
      this.out.write(13);
    super.close();
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2), 32768);
    int i;
    while ((i = localBufferedInputStream.read()) != -1)
    {
      if (i == 13)
      {
        if (this.F)
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
            this.out.write(this.C);
          }
          else
          {
            localBufferedInputStream.reset();
            if (this.B)
              this.out.write(this.C);
            else
              this.out.write(i);
          }
          continue;
        }
        if (this.B)
        {
          this.out.write(this.C);
          continue;
        }
        this.out.write(i);
        continue;
      }
      if (i == 10)
      {
        if (this.A)
        {
          this.out.write(this.C);
          this.A = false;
          continue;
        }
        if (this.E)
        {
          this.out.write(this.C);
          continue;
        }
        this.out.write(i);
        continue;
      }
      if (this.A)
        if (this.B)
          this.out.write(this.C);
        else
          this.out.write(i);
      if ((i != 116) && (i != 12) && ((i & 0xFF) < 32))
        this.D = true;
      this.out.write(i);
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      TextConversionStream localTextConversionStream = new TextConversionStream(1, 3, new FileOutputStream("C:\\TEXT.txt"));
      localTextConversionStream.write("1234567890\r".getBytes());
      localTextConversionStream.write("\n01234567890\r\n".getBytes());
      localTextConversionStream.write("\r\n12323445546657".getBytes());
      localTextConversionStream.write("21344356545656\r".getBytes());
      localTextConversionStream.close();
    }
    catch (Exception localException)
    {
      System.out.println("RECIEVED IOException IN Ssh1Protocol.close:" + localException.getMessage());
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.TextConversionStream
 * JD-Core Version:    0.6.0
 */