package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERNumericString extends DERObject
  implements DERString
{
  String j;

  public static DERNumericString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERNumericString)))
      return (DERNumericString)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERNumericString(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERNumericString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERNumericString(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.j = new String(arrayOfChar);
  }

  public DERNumericString(String paramString)
  {
    this.j = paramString;
  }

  public String getString()
  {
    return this.j;
  }

  public byte[] getOctets()
  {
    char[] arrayOfChar = this.j.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(18, getOctets());
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DERNumericString))
      return false;
    DERNumericString localDERNumericString = (DERNumericString)paramObject;
    return getString().equals(localDERNumericString.getString());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERNumericString
 * JD-Core Version:    0.6.0
 */