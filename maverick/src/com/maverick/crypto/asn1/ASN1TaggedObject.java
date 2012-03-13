package com.maverick.crypto.asn1;

import java.io.IOException;

public abstract class ASN1TaggedObject extends DERObject
{
  int cc;
  boolean dc = false;
  boolean bc = true;
  DEREncodable ec = null;

  public static ASN1TaggedObject getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (paramBoolean)
      return (ASN1TaggedObject)paramASN1TaggedObject.getObject();
    throw new IllegalArgumentException("implicitly tagged tagged object");
  }

  public ASN1TaggedObject(int paramInt, DEREncodable paramDEREncodable)
  {
    this.bc = true;
    this.cc = paramInt;
    this.ec = paramDEREncodable;
  }

  public ASN1TaggedObject(boolean paramBoolean, int paramInt, DEREncodable paramDEREncodable)
  {
    this.bc = paramBoolean;
    this.cc = paramInt;
    this.ec = paramDEREncodable;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof ASN1TaggedObject)))
      return false;
    ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramObject;
    if ((this.cc != localASN1TaggedObject.cc) || (this.dc != localASN1TaggedObject.dc) || (this.bc != localASN1TaggedObject.bc))
      return false;
    if (this.ec == null)
    {
      if (localASN1TaggedObject.ec != null)
        return false;
    }
    else if (!this.ec.equals(localASN1TaggedObject.ec))
      return false;
    return true;
  }

  public int hashCode()
  {
    int i = this.cc;
    if (this.ec != null)
      i ^= this.ec.hashCode();
    return i;
  }

  public int getTagNo()
  {
    return this.cc;
  }

  public boolean isExplicit()
  {
    return this.bc;
  }

  public boolean isEmpty()
  {
    return this.dc;
  }

  public DERObject getObject()
  {
    if (this.ec != null)
      return this.ec.getDERObject();
    return null;
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1TaggedObject
 * JD-Core Version:    0.6.0
 */