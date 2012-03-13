package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERVisibleString extends DERObject
  implements DERString
{
  String fb;

  public static DERVisibleString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERVisibleString)))
      return (DERVisibleString)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERVisibleString(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERVisibleString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERVisibleString(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.fb = new String(arrayOfChar);
  }

  public DERVisibleString(String paramString)
  {
    this.fb = paramString;
  }

  public String getString()
  {
    return this.fb;
  }

  public byte[] getOctets()
  {
    char[] arrayOfChar = this.fb.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(26, getOctets());
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERVisibleString)))
      return false;
    return getString().equals(((DERVisibleString)paramObject).getString());
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERVisibleString
 * JD-Core Version:    0.6.0
 */