package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERGeneralString extends DERObject
  implements DERString
{
  private String jb;

  public static DERGeneralString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERGeneralString)))
      return (DERGeneralString)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERGeneralString(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERGeneralString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERGeneralString(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.jb = new String(arrayOfChar);
  }

  public DERGeneralString(String paramString)
  {
    this.jb = paramString;
  }

  public String getString()
  {
    return this.jb;
  }

  public byte[] getOctets()
  {
    char[] arrayOfChar = this.jb.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(27, getOctets());
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DERGeneralString))
      return false;
    DERGeneralString localDERGeneralString = (DERGeneralString)paramObject;
    return getString().equals(localDERGeneralString.getString());
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERGeneralString
 * JD-Core Version:    0.6.0
 */