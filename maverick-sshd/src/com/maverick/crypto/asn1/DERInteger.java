package com.maverick.crypto.asn1;

import java.io.IOException;
import java.math.BigInteger;

public class DERInteger extends DERObject
{
  byte[] y;

  public static DERInteger getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERInteger)))
      return (DERInteger)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERInteger(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERInteger getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERInteger(int paramInt)
  {
    this.y = BigInteger.valueOf(paramInt).toByteArray();
  }

  public DERInteger(BigInteger paramBigInteger)
  {
    this.y = paramBigInteger.toByteArray();
  }

  public DERInteger(byte[] paramArrayOfByte)
  {
    this.y = paramArrayOfByte;
  }

  public BigInteger getValue()
  {
    return new BigInteger(this.y);
  }

  public BigInteger getPositiveValue()
  {
    return new BigInteger(1, this.y);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(2, this.y);
  }

  public int hashCode()
  {
    int i = 0;
    for (int j = 0; j != this.y.length; j++)
      i ^= (this.y[j] & 0xFF) << j % 4;
    return i;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERInteger)))
      return false;
    DERInteger localDERInteger = (DERInteger)paramObject;
    if (this.y.length != localDERInteger.y.length)
      return false;
    for (int i = 0; i != this.y.length; i++)
      if (this.y[i] != localDERInteger.y[i])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERInteger
 * JD-Core Version:    0.6.0
 */