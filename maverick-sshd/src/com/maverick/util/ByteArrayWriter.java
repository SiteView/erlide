package com.maverick.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class ByteArrayWriter extends ByteArrayOutputStream
{
  public ByteArrayWriter()
  {
  }

  public ByteArrayWriter(int paramInt)
  {
    super(paramInt);
  }

  public byte[] array()
  {
    return this.buf;
  }

  public void move(int paramInt)
  {
    this.count += paramInt;
  }

  public void writeBigInteger(BigInteger paramBigInteger)
    throws IOException
  {
    byte[] arrayOfByte = paramBigInteger.toByteArray();
    writeInt(arrayOfByte.length);
    write(arrayOfByte);
  }

  public void writeBoolean(boolean paramBoolean)
  {
    write(paramBoolean ? 1 : 0);
  }

  public void writeBinaryString(byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte == null)
      writeInt(0);
    else
      writeBinaryString(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void writeBinaryString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramArrayOfByte == null)
    {
      writeInt(0);
    }
    else
    {
      writeInt(paramInt2);
      write(paramArrayOfByte, paramInt1, paramInt2);
    }
  }

  public void writeMPINT(BigInteger paramBigInteger)
  {
    int i = (short)((paramBigInteger.bitLength() + 7) / 8);
    byte[] arrayOfByte = paramBigInteger.toByteArray();
    writeShort((short)paramBigInteger.bitLength());
    if (arrayOfByte[0] == 0)
      write(arrayOfByte, 1, i);
    else
      write(arrayOfByte, 0, i);
  }

  public void writeShort(short paramShort)
  {
    write(paramShort >>> 8 & 0xFF);
    write(paramShort >>> 0 & 0xFF);
  }

  public void writeInt(long paramLong)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)(int)(paramLong >> 24);
    arrayOfByte[1] = (byte)(int)(paramLong >> 16);
    arrayOfByte[2] = (byte)(int)(paramLong >> 8);
    arrayOfByte[3] = (byte)(int)paramLong;
    write(arrayOfByte);
  }

  public void writeInt(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)(paramInt >> 24);
    arrayOfByte[1] = (byte)(paramInt >> 16);
    arrayOfByte[2] = (byte)(paramInt >> 8);
    arrayOfByte[3] = (byte)paramInt;
    write(arrayOfByte);
  }

  public static byte[] encodeInt(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)(paramInt >> 24);
    arrayOfByte[1] = (byte)(paramInt >> 16);
    arrayOfByte[2] = (byte)(paramInt >> 8);
    arrayOfByte[3] = (byte)paramInt;
    return arrayOfByte;
  }

  public static void encodeInt(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[(paramInt1++)] = (byte)(paramInt2 >> 24);
    paramArrayOfByte[(paramInt1++)] = (byte)(paramInt2 >> 16);
    paramArrayOfByte[(paramInt1++)] = (byte)(paramInt2 >> 8);
    paramArrayOfByte[paramInt1] = (byte)paramInt2;
  }

  public void writeUINT32(UnsignedInteger32 paramUnsignedInteger32)
    throws IOException
  {
    writeInt(paramUnsignedInteger32.longValue());
  }

  public void writeUINT64(UnsignedInteger64 paramUnsignedInteger64)
    throws IOException
  {
    byte[] arrayOfByte1 = new byte[8];
    byte[] arrayOfByte2 = paramUnsignedInteger64.bigIntValue().toByteArray();
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, arrayOfByte1.length - arrayOfByte2.length, arrayOfByte2.length);
    write(arrayOfByte1);
  }

  public void writeUINT64(long paramLong)
    throws IOException
  {
    writeUINT64(new UnsignedInteger64(paramLong));
  }

  public void writeString(String paramString)
    throws IOException
  {
    writeString(paramString, ByteArrayReader.getCharsetEncoding());
  }

  public void writeString(String paramString1, String paramString2)
    throws IOException
  {
    if (paramString1 == null)
    {
      writeInt(0);
    }
    else
    {
      byte[] arrayOfByte;
      if (ByteArrayReader.encode)
        arrayOfByte = paramString1.getBytes(paramString2);
      else
        arrayOfByte = paramString1.getBytes();
      writeInt(arrayOfByte.length);
      write(arrayOfByte);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.ByteArrayWriter
 * JD-Core Version:    0.6.0
 */