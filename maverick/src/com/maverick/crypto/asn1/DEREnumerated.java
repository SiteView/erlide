package com.maverick.crypto.asn1;

import java.io.IOException;
import java.math.BigInteger;

public class DEREnumerated extends DERObject
{
  byte[] xb;

  public static DEREnumerated getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DEREnumerated)))
      return (DEREnumerated)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DEREnumerated(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DEREnumerated getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DEREnumerated(int paramInt)
  {
    this.xb = BigInteger.valueOf(paramInt).toByteArray();
  }

  public DEREnumerated(BigInteger paramBigInteger)
  {
    this.xb = paramBigInteger.toByteArray();
  }

  public DEREnumerated(byte[] paramArrayOfByte)
  {
    this.xb = paramArrayOfByte;
  }

  public BigInteger getValue()
  {
    return new BigInteger(this.xb);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(10, this.xb);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DEREnumerated)))
      return false;
    DEREnumerated localDEREnumerated = (DEREnumerated)paramObject;
    if (this.xb.length != localDEREnumerated.xb.length)
      return false;
    for (int i = 0; i != this.xb.length; i++)
      if (this.xb[i] != localDEREnumerated.xb[i])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DEREnumerated
 * JD-Core Version:    0.6.0
 */