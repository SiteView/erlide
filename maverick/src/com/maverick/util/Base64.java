package com.maverick.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;

public class Base64
{
  public static final boolean ENCODE = true;
  public static final boolean DECODE = false;
  private static final byte[] b = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
  private static final byte[] c = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };

  public static byte[] decode(String paramString)
  {
    byte[] arrayOfByte = paramString.getBytes();
    return decode(arrayOfByte, 0, arrayOfByte.length);
  }

  public static byte[] decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt2 * 3 / 4;
    byte[] arrayOfByte1 = new byte[i];
    int j = 0;
    byte[] arrayOfByte2 = new byte[4];
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    for (m = paramInt1; m < paramInt2; m++)
    {
      n = (byte)(paramArrayOfByte[m] & 0x7F);
      i1 = c[n];
      if (i1 >= -5)
      {
        if (i1 < -1)
          continue;
        arrayOfByte2[(k++)] = n;
        if (k <= 3)
          continue;
        j += c(arrayOfByte2, 0, arrayOfByte1, j);
        k = 0;
        if (n != 61)
          continue;
        break;
      }
      System.err.println("Bad Base64 input character at " + m + ": " + paramArrayOfByte[m] + "(decimal)");
      return null;
    }
    byte[] arrayOfByte3 = new byte[j];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, j);
    return arrayOfByte3;
  }

  public static Object decodeToObject(String paramString)
  {
    byte[] arrayOfByte = decode(paramString);
    ByteArrayInputStream localByteArrayInputStream = null;
    ObjectInputStream localObjectInputStream = null;
    try
    {
      localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
      Object localObject1 = localObjectInputStream.readObject();
      return localObject1;
    }
    catch (IOException localIOException)
    {
      localObject2 = null;
      return localObject2;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      Object localObject2 = null;
      return localObject2;
    }
    finally
    {
      try
      {
        localByteArrayInputStream.close();
      }
      catch (Exception localException7)
      {
      }
      try
      {
        localObjectInputStream.close();
      }
      catch (Exception localException8)
      {
      }
    }
    throw localObject3;
  }

  public static String decodeToString(String paramString)
  {
    return new String(decode(paramString));
  }

  public static String encodeBytes(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return encodeBytes(paramArrayOfByte, 0, paramArrayOfByte.length, paramBoolean);
  }

  public static String encodeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = paramInt2 * 4 / 3;
    byte[] arrayOfByte = new byte[i + (paramInt2 % 3 > 0 ? 4 : 0) + i / 64];
    int j = 0;
    int k = 0;
    int m = paramInt2 - 2;
    int n = 0;
    while (j < m)
    {
      c(paramArrayOfByte, j + paramInt1, 3, arrayOfByte, k);
      n += 4;
      if ((!paramBoolean) && (n == 64))
      {
        arrayOfByte[(k + 4)] = 10;
        k++;
        n = 0;
      }
      j += 3;
      k += 4;
    }
    if (j < paramInt2)
    {
      c(paramArrayOfByte, j + paramInt1, paramInt2 - j, arrayOfByte, k);
      k += 4;
    }
    return new String(arrayOfByte, 0, k);
  }

  public static String encodeObject(Serializable paramSerializable)
  {
    ByteArrayOutputStream localByteArrayOutputStream = null;
    OutputStream localOutputStream = null;
    ObjectOutputStream localObjectOutputStream = null;
    try
    {
      localByteArrayOutputStream = new ByteArrayOutputStream();
      localOutputStream = new OutputStream(localByteArrayOutputStream, true);
      localObjectOutputStream = new ObjectOutputStream(localOutputStream);
      localObjectOutputStream.writeObject(paramSerializable);
    }
    catch (IOException localIOException)
    {
      Object localObject1 = null;
      return localObject1;
    }
    finally
    {
      try
      {
        localObjectOutputStream.close();
      }
      catch (Exception localException7)
      {
      }
      try
      {
        localOutputStream.close();
      }
      catch (Exception localException8)
      {
      }
      try
      {
        localByteArrayOutputStream.close();
      }
      catch (Exception localException9)
      {
      }
    }
    return new String(localByteArrayOutputStream.toByteArray());
  }

  public static String encodeString(String paramString, boolean paramBoolean)
  {
    return encodeBytes(paramString.getBytes(), paramBoolean);
  }

  private static byte[] b(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte1 = new byte[3];
    int i = c(paramArrayOfByte, 0, arrayOfByte1, 0);
    byte[] arrayOfByte2 = new byte[i];
    for (int j = 0; j < i; j++)
      arrayOfByte2[j] = arrayOfByte1[j];
    return arrayOfByte2;
  }

  private static int c(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (paramArrayOfByte1[(paramInt1 + 2)] == 61)
    {
      i = c[paramArrayOfByte1[paramInt1]] << 24 >>> 6 | c[paramArrayOfByte1[(paramInt1 + 1)]] << 24 >>> 12;
      paramArrayOfByte2[paramInt2] = (byte)(i >>> 16);
      return 1;
    }
    if (paramArrayOfByte1[(paramInt1 + 3)] == 61)
    {
      i = c[paramArrayOfByte1[paramInt1]] << 24 >>> 6 | c[paramArrayOfByte1[(paramInt1 + 1)]] << 24 >>> 12 | c[paramArrayOfByte1[(paramInt1 + 2)]] << 24 >>> 18;
      paramArrayOfByte2[paramInt2] = (byte)(i >>> 16);
      paramArrayOfByte2[(paramInt2 + 1)] = (byte)(i >>> 8);
      return 2;
    }
    int i = c[paramArrayOfByte1[paramInt1]] << 24 >>> 6 | c[paramArrayOfByte1[(paramInt1 + 1)]] << 24 >>> 12 | c[paramArrayOfByte1[(paramInt1 + 2)]] << 24 >>> 18 | c[paramArrayOfByte1[(paramInt1 + 3)]] << 24 >>> 24;
    paramArrayOfByte2[paramInt2] = (byte)(i >> 16);
    paramArrayOfByte2[(paramInt2 + 1)] = (byte)(i >> 8);
    paramArrayOfByte2[(paramInt2 + 2)] = (byte)i;
    return 3;
  }

  private static byte[] c(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    c(paramArrayOfByte, 0, paramInt, arrayOfByte, 0);
    return arrayOfByte;
  }

  private static byte[] c(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    int i = (paramInt2 > 0 ? paramArrayOfByte1[paramInt1] << 24 >>> 8 : 0) | (paramInt2 > 1 ? paramArrayOfByte1[(paramInt1 + 1)] << 24 >>> 16 : 0) | (paramInt2 > 2 ? paramArrayOfByte1[(paramInt1 + 2)] << 24 >>> 24 : 0);
    switch (paramInt2)
    {
    case 3:
      paramArrayOfByte2[paramInt3] = b[(i >>> 18)];
      paramArrayOfByte2[(paramInt3 + 1)] = b[(i >>> 12 & 0x3F)];
      paramArrayOfByte2[(paramInt3 + 2)] = b[(i >>> 6 & 0x3F)];
      paramArrayOfByte2[(paramInt3 + 3)] = b[(i & 0x3F)];
      return paramArrayOfByte2;
    case 2:
      paramArrayOfByte2[paramInt3] = b[(i >>> 18)];
      paramArrayOfByte2[(paramInt3 + 1)] = b[(i >>> 12 & 0x3F)];
      paramArrayOfByte2[(paramInt3 + 2)] = b[(i >>> 6 & 0x3F)];
      paramArrayOfByte2[(paramInt3 + 3)] = 61;
      return paramArrayOfByte2;
    case 1:
      paramArrayOfByte2[paramInt3] = b[(i >>> 18)];
      paramArrayOfByte2[(paramInt3 + 1)] = b[(i >>> 12 & 0x3F)];
      paramArrayOfByte2[(paramInt3 + 2)] = 61;
      paramArrayOfByte2[(paramInt3 + 3)] = 61;
      return paramArrayOfByte2;
    }
    return paramArrayOfByte2;
  }

  public static class OutputStream extends FilterOutputStream
  {
    private byte[] d;
    private boolean f;
    private int e;
    private int c;
    private int b;

    public OutputStream(OutputStream paramOutputStream)
    {
      this(paramOutputStream, true);
    }

    public OutputStream(OutputStream paramOutputStream, boolean paramBoolean)
    {
      super();
      this.f = paramBoolean;
      this.e = (paramBoolean ? 3 : 4);
      this.d = new byte[this.e];
      this.b = 0;
      this.c = 0;
    }

    public void close()
      throws IOException
    {
      flush();
      super.close();
      this.out.close();
      this.d = null;
      this.out = null;
    }

    public void flush()
      throws IOException
    {
      if (this.b > 0)
        if (this.f)
          this.out.write(Base64.b(this.d, this.b));
        else
          throw new IOException("Base64 input not properly padded.");
      super.flush();
      this.out.flush();
    }

    public void write(int paramInt)
      throws IOException
    {
      this.d[(this.b++)] = (byte)paramInt;
      if (this.b >= this.e)
      {
        if (this.f)
        {
          this.out.write(Base64.b(this.d, this.e));
          this.c += 4;
          if (this.c >= 64)
          {
            this.out.write(10);
            this.c = 0;
          }
        }
        else
        {
          this.out.write(Base64.checkVersion(this.d));
        }
        this.b = 0;
      }
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      for (int i = 0; i < paramInt2; i++)
        write(paramArrayOfByte[(paramInt1 + i)]);
    }
  }

  public static class InputStream extends FilterInputStream
  {
    private byte[] c;
    private boolean e;
    private int d;
    private int f;
    private int b;

    public InputStream(InputStream paramInputStream)
    {
      this(paramInputStream, false);
    }

    public InputStream(InputStream paramInputStream, boolean paramBoolean)
    {
      super();
      this.e = paramBoolean;
      this.d = (paramBoolean ? 4 : 3);
      this.c = new byte[this.d];
      this.b = -1;
    }

    public int read()
      throws IOException
    {
      if (this.b < 0)
      {
        byte[] arrayOfByte;
        int j;
        if (this.e)
        {
          arrayOfByte = new byte[3];
          this.f = 0;
          for (j = 0; j < 3; j++)
            try
            {
              int k = this.in.read();
              if (k >= 0)
              {
                arrayOfByte[j] = (byte)k;
                this.f += 1;
              }
            }
            catch (IOException localIOException)
            {
              if (j != 0)
                continue;
              throw localIOException;
            }
          if (this.f > 0)
          {
            Base64.b(arrayOfByte, 0, this.f, this.c, 0);
            this.b = 0;
          }
        }
        else
        {
          arrayOfByte = new byte[4];
          j = 0;
          for (j = 0; j < 4; j++)
          {
            int m = 0;
            do
              m = this.in.read();
            while ((m >= 0) && (Base64.b()[(m & 0x7F)] < -5));
            if (m < 0)
              break;
            arrayOfByte[j] = (byte)m;
          }
          if (j == 4)
          {
            this.f = Base64.b(arrayOfByte, 0, this.c, 0);
            this.b = 0;
          }
        }
      }
      if (this.b >= 0)
      {
        if ((!this.e) && (this.b >= this.f))
          return -1;
        int i = this.c[(this.b++)];
        if (this.b >= this.d)
          this.b = -1;
        return i;
      }
      return -1;
    }

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      for (int i = 0; i < paramInt2; i++)
      {
        int j = read();
        if (j < 0)
          return -1;
        paramArrayOfByte[(paramInt1 + i)] = (byte)j;
      }
      return i;
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.Base64
 * JD-Core Version:    0.6.0
 */