package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERBMPString extends DERObject
  implements DERString
{
  String nb;

  public static DERBMPString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERBMPString)))
      return (DERBMPString)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERBMPString(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERBMPString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERBMPString(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length / 2];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[(2 * i)] << 8 | paramArrayOfByte[(2 * i + 1)] & 0xFF);
    this.nb = new String(arrayOfChar);
  }

  public DERBMPString(String paramString)
  {
    this.nb = paramString;
  }

  public String getString()
  {
    return this.nb;
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DERBMPString))
      return false;
    DERBMPString localDERBMPString = (DERBMPString)paramObject;
    return getString().equals(localDERBMPString.getString());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    char[] arrayOfChar = this.nb.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length * 2];
    for (int i = 0; i != arrayOfChar.length; i++)
    {
      arrayOfByte[(2 * i)] = (byte)(arrayOfChar[i] >> '\b');
      arrayOfByte[(2 * i + 1)] = (byte)arrayOfChar[i];
    }
    paramDEROutputStream.b(30, arrayOfByte);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERBMPString
 * JD-Core Version:    0.6.0
 */