package com.maverick.crypto.asn1;

import java.io.IOException;
import java.math.BigInteger;

public class DEREnumerated extends DERObject
{
  byte[] ¢;

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
    this.¢ = BigInteger.valueOf(paramInt).toByteArray();
  }

  public DEREnumerated(BigInteger paramBigInteger)
  {
    this.¢ = paramBigInteger.toByteArray();
  }

  public DEREnumerated(byte[] paramArrayOfByte)
  {
    this.¢ = paramArrayOfByte;
  }

  public BigInteger getValue()
  {
    return new BigInteger(this.¢);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(10, this.¢);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DEREnumerated)))
      return false;
    DEREnumerated localDEREnumerated = (DEREnumerated)paramObject;
    if (this.¢.length != localDEREnumerated.¢.length)
      return false;
    for (int i = 0; i != this.¢.length; i++)
      if (this.¢[i] != localDEREnumerated.¢[i])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DEREnumerated
 * JD-Core Version:    0.6.0
 */