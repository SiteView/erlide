package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERUniversalString extends DERObject
  implements DERString
{
  private static final char[] lb = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  private byte[] kb;

  public static DERUniversalString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERUniversalString)))
      return (DERUniversalString)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERUniversalString(((ASN1OctetString)paramObject).getOctets());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERUniversalString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERUniversalString(byte[] paramArrayOfByte)
  {
    this.kb = paramArrayOfByte;
  }

  public String getString()
  {
    StringBuffer localStringBuffer = new StringBuffer("#");
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ASN1OutputStream localASN1OutputStream = new ASN1OutputStream(localByteArrayOutputStream);
    try
    {
      localASN1OutputStream.writeObject(this);
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("internal error encoding BitString");
    }
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    for (int i = 0; i != arrayOfByte.length; i++)
    {
      localStringBuffer.append(lb[((arrayOfByte[i] >>> 4) % 15)]);
      localStringBuffer.append(lb[(arrayOfByte[i] & 0xF)]);
    }
    return localStringBuffer.toString();
  }

  public byte[] getOctets()
  {
    return this.kb;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(28, getOctets());
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERUniversalString)))
      return false;
    return getString().equals(((DERUniversalString)paramObject).getString());
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERUniversalString
 * JD-Core Version:    0.6.0
 */