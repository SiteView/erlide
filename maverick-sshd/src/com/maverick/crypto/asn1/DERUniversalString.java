package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERUniversalString extends DERObject
  implements DERString
{
  private static final char[] o = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  private byte[] n;

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
    this.n = paramArrayOfByte;
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
      localStringBuffer.append(o[((arrayOfByte[i] >>> 4) % 15)]);
      localStringBuffer.append(o[(arrayOfByte[i] & 0xF)]);
    }
    return localStringBuffer.toString();
  }

  public byte[] getOctets()
  {
    return this.n;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(28, getOctets());
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERUniversalString)))
      return false;
    return getString().equals(((DERUniversalString)paramObject).getString());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERUniversalString
 * JD-Core Version:    0.6.0
 */