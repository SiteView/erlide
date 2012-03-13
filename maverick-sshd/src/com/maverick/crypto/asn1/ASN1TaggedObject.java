package com.maverick.crypto.asn1;

import java.io.IOException;

public abstract class ASN1TaggedObject extends DERObject
{
  int ª;
  boolean µ = false;
  boolean ¥ = true;
  DEREncodable º = null;

  public static ASN1TaggedObject getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (paramBoolean)
      return (ASN1TaggedObject)paramASN1TaggedObject.getObject();
    throw new IllegalArgumentException("implicitly tagged tagged object");
  }

  public ASN1TaggedObject(int paramInt, DEREncodable paramDEREncodable)
  {
    this.¥ = true;
    this.ª = paramInt;
    this.º = paramDEREncodable;
  }

  public ASN1TaggedObject(boolean paramBoolean, int paramInt, DEREncodable paramDEREncodable)
  {
    this.¥ = paramBoolean;
    this.ª = paramInt;
    this.º = paramDEREncodable;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof ASN1TaggedObject)))
      return false;
    ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramObject;
    if ((this.ª != localASN1TaggedObject.ª) || (this.µ != localASN1TaggedObject.µ) || (this.¥ != localASN1TaggedObject.¥))
      return false;
    if (this.º == null)
    {
      if (localASN1TaggedObject.º != null)
        return false;
    }
    else if (!this.º.equals(localASN1TaggedObject.º))
      return false;
    return true;
  }

  public int hashCode()
  {
    int i = this.ª;
    if (this.º != null)
      i ^= this.º.hashCode();
    return i;
  }

  public int getTagNo()
  {
    return this.ª;
  }

  public boolean isExplicit()
  {
    return this.¥;
  }

  public boolean isEmpty()
  {
    return this.µ;
  }

  public DERObject getObject()
  {
    if (this.º != null)
      return this.º.getDERObject();
    return null;
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1TaggedObject
 * JD-Core Version:    0.6.0
 */