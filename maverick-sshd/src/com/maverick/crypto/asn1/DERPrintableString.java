package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERPrintableString extends DERObject
  implements DERString
{
  String r;

  public static DERPrintableString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERPrintableString)))
      return (DERPrintableString)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERPrintableString(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERPrintableString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERPrintableString(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.r = new String(arrayOfChar);
  }

  public DERPrintableString(String paramString)
  {
    this.r = paramString;
  }

  public String getString()
  {
    return this.r;
  }

  public byte[] getOctets()
  {
    char[] arrayOfChar = this.r.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(19, getOctets());
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DERPrintableString))
      return false;
    DERPrintableString localDERPrintableString = (DERPrintableString)paramObject;
    return getString().equals(localDERPrintableString.getString());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERPrintableString
 * JD-Core Version:    0.6.0
 */