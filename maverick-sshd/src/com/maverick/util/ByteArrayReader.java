package com.maverick.util;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class ByteArrayReader extends ByteArrayInputStream
{
  private static String A = "UTF8";
  public static boolean encode;

  public ByteArrayReader(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    super(paramArrayOfByte, paramInt1, paramInt2);
  }

  public ByteArrayReader(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public byte[] array()
  {
    return this.buf;
  }

  public static void setCharsetEncoding(String paramString)
  {
    try
    {
      String str = "123456890";
      str.getBytes(paramString);
      A = paramString;
      encode = true;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      A = "";
      encode = false;
    }
  }

  public static String getCharsetEncoding()
  {
    return A;
  }

  public void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 0)
      throw new IndexOutOfBoundsException();
    int i = 0;
    while (i < paramInt2)
    {
      int j = read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
      if (j < 0)
        throw new EOFException("Could not read number of bytes requested: " + paramInt2 + ", got " + i + " into buffer size " + paramArrayOfByte.length + " at offset " + paramInt1);
      i += j;
    }
  }

  public boolean readBoolean()
    throws IOException
  {
    return read() == 1;
  }

  public void readFully(byte[] paramArrayOfByte)
    throws IOException
  {
    readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public BigInteger readBigInteger()
    throws IOException
  {
    int i = (int)readInt();
    byte[] arrayOfByte = new byte[i];
    readFully(arrayOfByte);
    return new BigInteger(arrayOfByte);
  }

  public UnsignedInteger64 readUINT64()
    throws IOException
  {
    byte[] arrayOfByte = new byte[8];
    readFully(arrayOfByte);
    return new UnsignedInteger64(arrayOfByte);
  }

  public UnsignedInteger32 readUINT32()
    throws IOException
  {
    return new UnsignedInteger32(readInt());
  }

  public static long readInt(byte[] paramArrayOfByte, int paramInt)
  {
    long l = (paramArrayOfByte[paramInt] & 0xFF) << 24 & 0xFFFFFFFF | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt + 3)] & 0xFF) << 0;
    return l;
  }

  public static short readShort(byte[] paramArrayOfByte, int paramInt)
  {
    int i = (short)((paramArrayOfByte[paramInt] & 0xFF) << 8 | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 0);
    return i;
  }

  public byte[] readBinaryString()
    throws IOException
  {
    int i = (int)readInt();
    byte[] arrayOfByte = new byte[i];
    readFully(arrayOfByte);
    return arrayOfByte;
  }

  public long readInt()
    throws IOException
  {
    int i = read();
    int j = read();
    int k = read();
    int m = read();
    if ((i | j | k | m) < 0)
      throw new EOFException();
    return (i << 24) + (j << 16) + (k << 8) + (m << 0) & 0xFFFFFFFF;
  }

  public String readString()
    throws IOException
  {
    return readString(A);
  }

  public String readString(String paramString)
    throws IOException
  {
    long l = readInt();
    if (l > available())
      throw new IOException("Cannot read string of length " + l + " bytes when only " + available() + " bytes are available");
    byte[] arrayOfByte = new byte[(int)l];
    readFully(arrayOfByte);
    if (encode)
      return new String(arrayOfByte, paramString);
    return new String(arrayOfByte);
  }

  public short readShort()
    throws IOException
  {
    int i = read();
    int j = read();
    if ((i | j) < 0)
      throw new EOFException();
    return (short)((i << 8) + (j << 0));
  }

  public BigInteger readMPINT32()
    throws IOException
  {
    int i = (int)readInt();
    byte[] arrayOfByte = new byte[(i + 7) / 8 + 1];
    arrayOfByte[0] = 0;
    readFully(arrayOfByte, 1, arrayOfByte.length - 1);
    return new BigInteger(arrayOfByte);
  }

  public BigInteger readMPINT()
    throws IOException
  {
    int i = readShort();
    byte[] arrayOfByte = new byte[(i + 7) / 8 + 1];
    arrayOfByte[0] = 0;
    readFully(arrayOfByte, 1, arrayOfByte.length - 1);
    return new BigInteger(arrayOfByte);
  }

  public int getPosition()
  {
    return this.pos;
  }

  static
  {
    setCharsetEncoding(A);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.ByteArrayReader
 * JD-Core Version:    0.6.0
 */