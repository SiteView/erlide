package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERIA5String extends DERObject
  implements DERString
{
  String p;

  public static DERIA5String getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERIA5String)))
      return (DERIA5String)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERIA5String(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERIA5String getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERIA5String(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.p = new String(arrayOfChar);
  }

  public DERIA5String(String paramString)
  {
    this.p = paramString;
  }

  public String getString()
  {
    return this.p;
  }

  public byte[] getOctets()
  {
    char[] arrayOfChar = this.p.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(22, getOctets());
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DERIA5String))
      return false;
    DERIA5String localDERIA5String = (DERIA5String)paramObject;
    return getString().equals(localDERIA5String.getString());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERIA5String
 * JD-Core Version:    0.6.0
 */