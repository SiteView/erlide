package com.maverick.crypto.encoders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64
{
  private static final byte[] b = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
  private static final byte[] c = new byte[''];

  public static byte[] encode(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length % 3;
    byte[] arrayOfByte;
    if (i == 0)
      arrayOfByte = new byte[4 * paramArrayOfByte.length / 3];
    else
      arrayOfByte = new byte[4 * (paramArrayOfByte.length / 3 + 1)];
    int j = paramArrayOfByte.length - i;
    int i1 = 0;
    for (int i2 = 0; i1 < j; i2 += 4)
    {
      int k = paramArrayOfByte[i1] & 0xFF;
      int m = paramArrayOfByte[(i1 + 1)] & 0xFF;
      int n = paramArrayOfByte[(i1 + 2)] & 0xFF;
      arrayOfByte[i2] = b[(k >>> 2 & 0x3F)];
      arrayOfByte[(i2 + 1)] = b[((k << 4 | m >>> 4) & 0x3F)];
      arrayOfByte[(i2 + 2)] = b[((m << 2 | n >>> 6) & 0x3F)];
      arrayOfByte[(i2 + 3)] = b[(n & 0x3F)];
      i1 += 3;
    }
    int i4;
    switch (i)
    {
    case 0:
      break;
    case 1:
      i4 = paramArrayOfByte[(paramArrayOfByte.length - 1)] & 0xFF;
      i1 = i4 >>> 2 & 0x3F;
      i2 = i4 << 4 & 0x3F;
      arrayOfByte[(arrayOfByte.length - 4)] = b[i1];
      arrayOfByte[(arrayOfByte.length - 3)] = b[i2];
      arrayOfByte[(arrayOfByte.length - 2)] = 61;
      arrayOfByte[(arrayOfByte.length - 1)] = 61;
      break;
    case 2:
      i4 = paramArrayOfByte[(paramArrayOfByte.length - 2)] & 0xFF;
      int i5 = paramArrayOfByte[(paramArrayOfByte.length - 1)] & 0xFF;
      i1 = i4 >>> 2 & 0x3F;
      i2 = (i4 << 4 | i5 >>> 4) & 0x3F;
      int i3 = i5 << 2 & 0x3F;
      arrayOfByte[(arrayOfByte.length - 4)] = b[i1];
      arrayOfByte[(arrayOfByte.length - 3)] = b[i2];
      arrayOfByte[(arrayOfByte.length - 2)] = b[i3];
      arrayOfByte[(arrayOfByte.length - 1)] = 61;
    }
    return arrayOfByte;
  }

  public static byte[] decode(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte;
    if (paramArrayOfByte[(paramArrayOfByte.length - 2)] == 61)
      arrayOfByte = new byte[(paramArrayOfByte.length / 4 - 1) * 3 + 1];
    else if (paramArrayOfByte[(paramArrayOfByte.length - 1)] == 61)
      arrayOfByte = new byte[(paramArrayOfByte.length / 4 - 1) * 3 + 2];
    else
      arrayOfByte = new byte[paramArrayOfByte.length / 4 * 3];
    int n = 0;
    int i;
    int j;
    int k;
    int m;
    for (int i1 = 0; n < paramArrayOfByte.length - 4; i1 += 3)
    {
      i = c[paramArrayOfByte[n]];
      j = c[paramArrayOfByte[(n + 1)]];
      k = c[paramArrayOfByte[(n + 2)]];
      m = c[paramArrayOfByte[(n + 3)]];
      arrayOfByte[i1] = (byte)(i << 2 | j >> 4);
      arrayOfByte[(i1 + 1)] = (byte)(j << 4 | k >> 2);
      arrayOfByte[(i1 + 2)] = (byte)(k << 6 | m);
      n += 4;
    }
    if (paramArrayOfByte[(paramArrayOfByte.length - 2)] == 61)
    {
      i = c[paramArrayOfByte[(paramArrayOfByte.length - 4)]];
      j = c[paramArrayOfByte[(paramArrayOfByte.length - 3)]];
      arrayOfByte[(arrayOfByte.length - 1)] = (byte)(i << 2 | j >> 4);
    }
    else if (paramArrayOfByte[(paramArrayOfByte.length - 1)] == 61)
    {
      i = c[paramArrayOfByte[(paramArrayOfByte.length - 4)]];
      j = c[paramArrayOfByte[(paramArrayOfByte.length - 3)]];
      k = c[paramArrayOfByte[(paramArrayOfByte.length - 2)]];
      arrayOfByte[(arrayOfByte.length - 2)] = (byte)(i << 2 | j >> 4);
      arrayOfByte[(arrayOfByte.length - 1)] = (byte)(j << 4 | k >> 2);
    }
    else
    {
      i = c[paramArrayOfByte[(paramArrayOfByte.length - 4)]];
      j = c[paramArrayOfByte[(paramArrayOfByte.length - 3)]];
      k = c[paramArrayOfByte[(paramArrayOfByte.length - 2)]];
      m = c[paramArrayOfByte[(paramArrayOfByte.length - 1)]];
      arrayOfByte[(arrayOfByte.length - 3)] = (byte)(i << 2 | j >> 4);
      arrayOfByte[(arrayOfByte.length - 2)] = (byte)(j << 4 | k >> 2);
      arrayOfByte[(arrayOfByte.length - 1)] = (byte)(k << 6 | m);
    }
    return arrayOfByte;
  }

  private static boolean b(char paramChar)
  {
    return (paramChar == '\n') || (paramChar == '\r') || (paramChar == '\t') || (paramChar == ' ');
  }

  public static byte[] decode(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      decode(paramString, localByteArrayOutputStream);
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("exception decoding base64 string: " + localIOException);
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public static int decode(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    int n = 0;
    for (int i1 = paramString.length(); (i1 > 0) && (b(paramString.charAt(i1 - 1))); i1--);
    int i;
    int j;
    int k;
    int m;
    for (int i2 = 0; i2 < i1 - 4; i2 += 4)
    {
      if (b(paramString.charAt(i2)))
        continue;
      i = c[paramString.charAt(i2)];
      j = c[paramString.charAt(i2 + 1)];
      k = c[paramString.charAt(i2 + 2)];
      m = c[paramString.charAt(i2 + 3)];
      paramOutputStream.write(i << 2 | j >> 4);
      paramOutputStream.write(j << 4 | k >> 2);
      paramOutputStream.write(k << 6 | m);
      n += 3;
    }
    if (paramString.charAt(i1 - 2) == '=')
    {
      i = c[paramString.charAt(i1 - 4)];
      j = c[paramString.charAt(i1 - 3)];
      paramOutputStream.write(i << 2 | j >> 4);
      n++;
    }
    else if (paramString.charAt(i1 - 1) == '=')
    {
      i = c[paramString.charAt(i1 - 4)];
      j = c[paramString.charAt(i1 - 3)];
      k = c[paramString.charAt(i1 - 2)];
      paramOutputStream.write(i << 2 | j >> 4);
      paramOutputStream.write(j << 4 | k >> 2);
      n += 2;
    }
    else
    {
      i = c[paramString.charAt(i1 - 4)];
      j = c[paramString.charAt(i1 - 3)];
      k = c[paramString.charAt(i1 - 2)];
      m = c[paramString.charAt(i1 - 1)];
      paramOutputStream.write(i << 2 | j >> 4);
      paramOutputStream.write(j << 4 | k >> 2);
      paramOutputStream.write(k << 6 | m);
      n += 3;
    }
    return n;
  }

  static
  {
    for (int i = 65; i <= 90; i++)
      c[i] = (byte)(i - 65);
    for (i = 97; i <= 122; i++)
      c[i] = (byte)(i - 97 + 26);
    for (i = 48; i <= 57; i++)
      c[i] = (byte)(i - 48 + 52);
    c[43] = 62;
    c[47] = 63;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.encoders.Base64
 * JD-Core Version:    0.6.0
 */