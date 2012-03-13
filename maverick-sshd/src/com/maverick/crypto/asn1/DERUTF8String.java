package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERUTF8String extends DERObject
  implements DERString
{
  String k;

  public static DERUTF8String getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERUTF8String)))
      return (DERUTF8String)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERUTF8String(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERUTF8String getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  DERUTF8String(byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    while (i < paramArrayOfByte.length)
    {
      j++;
      if ((paramArrayOfByte[i] & 0xE0) == 224)
      {
        i += 3;
        continue;
      }
      if ((paramArrayOfByte[i] & 0xC0) == 192)
      {
        i += 2;
        continue;
      }
      i++;
    }
    char[] arrayOfChar = new char[j];
    i = 0;
    j = 0;
    while (i < paramArrayOfByte.length)
    {
      int m;
      if ((paramArrayOfByte[i] & 0xE0) == 224)
      {
        m = (char)((paramArrayOfByte[i] & 0x1F) << 12 | (paramArrayOfByte[(i + 1)] & 0x3F) << 6 | paramArrayOfByte[(i + 2)] & 0x3F);
        i += 3;
      }
      else if ((paramArrayOfByte[i] & 0xC0) == 192)
      {
        m = (char)((paramArrayOfByte[i] & 0x3F) << 6 | paramArrayOfByte[(i + 1)] & 0x3F);
        i += 2;
      }
      else
      {
        m = (char)(paramArrayOfByte[i] & 0xFF);
        i++;
      }
      arrayOfChar[(j++)] = m;
    }
    this.k = new String(arrayOfChar);
  }

  public DERUTF8String(String paramString)
  {
    this.k = paramString;
  }

  public String getString()
  {
    return this.k;
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DERUTF8String))
      return false;
    DERUTF8String localDERUTF8String = (DERUTF8String)paramObject;
    return getString().equals(localDERUTF8String.getString());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    char[] arrayOfChar = this.k.toCharArray();
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i = 0; i != arrayOfChar.length; i++)
    {
      int j = arrayOfChar[i];
      if (j < 128)
      {
        localByteArrayOutputStream.write(j);
      }
      else if (j < 2048)
      {
        localByteArrayOutputStream.write(0xC0 | j >> 6);
        localByteArrayOutputStream.write(0x80 | j & 0x3F);
      }
      else
      {
        localByteArrayOutputStream.write(0xE0 | j >> 12);
        localByteArrayOutputStream.write(0x80 | j >> 6 & 0x3F);
        localByteArrayOutputStream.write(0x80 | j & 0x3F);
      }
    }
    paramDEROutputStream.A(12, localByteArrayOutputStream.toByteArray());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERUTF8String
 * JD-Core Version:    0.6.0
 */