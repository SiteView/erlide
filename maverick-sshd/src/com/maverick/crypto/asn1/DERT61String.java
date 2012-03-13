package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERT61String extends DERObject
  implements DERString
{
  String l;

  public static DERT61String getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERT61String)))
      return (DERT61String)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERT61String(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERT61String getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERT61String(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.l = new String(arrayOfChar);
  }

  public DERT61String(String paramString)
  {
    this.l = paramString;
  }

  public String getString()
  {
    return this.l;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(20, getOctets());
  }

  public byte[] getOctets()
  {
    char[] arrayOfChar = this.l.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERT61String)))
      return false;
    return getString().equals(((DERT61String)paramObject).getString());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERT61String
 * JD-Core Version:    0.6.0
 */