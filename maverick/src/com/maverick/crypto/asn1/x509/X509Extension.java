package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1OctetString;
import com.maverick.crypto.asn1.DERBoolean;

public class X509Extension
{
  boolean c;
  ASN1OctetString b;

  public X509Extension(DERBoolean paramDERBoolean, ASN1OctetString paramASN1OctetString)
  {
    this.c = paramDERBoolean.isTrue();
    this.b = paramASN1OctetString;
  }

  public X509Extension(boolean paramBoolean, ASN1OctetString paramASN1OctetString)
  {
    this.c = paramBoolean;
    this.b = paramASN1OctetString;
  }

  public boolean isCritical()
  {
    return this.c;
  }

  public ASN1OctetString getValue()
  {
    return this.b;
  }

  public int hashCode()
  {
    if (isCritical())
      return getValue().hashCode();
    return getValue().hashCode() ^ 0xFFFFFFFF;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof X509Extension)))
      return false;
    X509Extension localX509Extension = (X509Extension)paramObject;
    return (localX509Extension.getValue().equals(getValue())) && (localX509Extension.isCritical() == isCritical());
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.X509Extension
 * JD-Core Version:    0.6.0
 */