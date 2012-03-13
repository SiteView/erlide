package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERBoolean extends DERObject
{
  byte Ã;
  public static final DERBoolean FALSE = new DERBoolean(false);
  public static final DERBoolean TRUE = new DERBoolean(true);

  public static DERBoolean getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERBoolean)))
      return (DERBoolean)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERBoolean(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERBoolean getInstance(boolean paramBoolean)
  {
    return paramBoolean ? TRUE : FALSE;
  }

  public static DERBoolean getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERBoolean(byte[] paramArrayOfByte)
  {
    this.Ã = paramArrayOfByte[0];
  }

  public DERBoolean(boolean paramBoolean)
  {
    this.Ã = (paramBoolean ? -1 : 0);
  }

  public boolean isTrue()
  {
    return this.Ã != 0;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    arrayOfByte[0] = this.Ã;
    paramDEROutputStream.A(1, arrayOfByte);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERBoolean)))
      return false;
    return this.Ã == ((DERBoolean)paramObject).Ã;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERBoolean
 * JD-Core Version:    0.6.0
 */