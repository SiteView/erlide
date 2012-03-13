package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1Encodable;
import com.maverick.crypto.asn1.ASN1EncodableVector;
import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DERBoolean;
import com.maverick.crypto.asn1.DERInteger;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.DERSequence;
import java.math.BigInteger;

public class BasicConstraints extends ASN1Encodable
{
  DERBoolean nc = new DERBoolean(false);
  DERInteger oc = null;

  public static BasicConstraints getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public static BasicConstraints getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof BasicConstraints)))
      return (BasicConstraints)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new BasicConstraints((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory");
  }

  public BasicConstraints(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() == 0)
    {
      this.nc = null;
      this.oc = null;
    }
    else
    {
      this.nc = ((DERBoolean)paramASN1Sequence.getObjectAt(0));
      if (paramASN1Sequence.size() > 1)
        this.oc = ((DERInteger)paramASN1Sequence.getObjectAt(1));
    }
  }

  public BasicConstraints(boolean paramBoolean, int paramInt)
  {
    if (paramBoolean)
    {
      this.nc = new DERBoolean(paramBoolean);
      this.oc = new DERInteger(paramInt);
    }
    else
    {
      this.nc = null;
      this.oc = null;
    }
  }

  public BasicConstraints(boolean paramBoolean)
  {
    if (paramBoolean)
      this.nc = new DERBoolean(true);
    else
      this.nc = null;
    this.oc = null;
  }

  public BasicConstraints(int paramInt)
  {
    this.nc = new DERBoolean(true);
    this.oc = new DERInteger(paramInt);
  }

  public boolean isCA()
  {
    return (this.nc != null) && (this.nc.isTrue());
  }

  public BigInteger getPathLenConstraint()
  {
    if (this.oc != null)
      return this.oc.getValue();
    return null;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.nc != null)
    {
      localASN1EncodableVector.add(this.nc);
      if (this.oc != null)
        localASN1EncodableVector.add(this.oc);
    }
    return new DERSequence(localASN1EncodableVector);
  }

  public String toString()
  {
    if (this.oc == null)
    {
      if (this.nc == null)
        return "BasicConstraints: isCa(false)";
      return "BasicConstraints: isCa(" + isCA() + ")";
    }
    return "BasicConstraints: isCa(" + isCA() + "), pathLenConstraint = " + this.oc.getValue();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.BasicConstraints
 * JD-Core Version:    0.6.0
 */