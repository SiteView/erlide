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
  DERBoolean Ç = new DERBoolean(false);
  DERInteger È = null;

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
      this.Ç = null;
      this.È = null;
    }
    else
    {
      this.Ç = ((DERBoolean)paramASN1Sequence.getObjectAt(0));
      if (paramASN1Sequence.size() > 1)
        this.È = ((DERInteger)paramASN1Sequence.getObjectAt(1));
    }
  }

  public BasicConstraints(boolean paramBoolean, int paramInt)
  {
    if (paramBoolean)
    {
      this.Ç = new DERBoolean(paramBoolean);
      this.È = new DERInteger(paramInt);
    }
    else
    {
      this.Ç = null;
      this.È = null;
    }
  }

  public BasicConstraints(boolean paramBoolean)
  {
    if (paramBoolean)
      this.Ç = new DERBoolean(true);
    else
      this.Ç = null;
    this.È = null;
  }

  public BasicConstraints(int paramInt)
  {
    this.Ç = new DERBoolean(true);
    this.È = new DERInteger(paramInt);
  }

  public boolean isCA()
  {
    return (this.Ç != null) && (this.Ç.isTrue());
  }

  public BigInteger getPathLenConstraint()
  {
    if (this.È != null)
      return this.È.getValue();
    return null;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.Ç != null)
    {
      localASN1EncodableVector.add(this.Ç);
      if (this.È != null)
        localASN1EncodableVector.add(this.È);
    }
    return new DERSequence(localASN1EncodableVector);
  }

  public String toString()
  {
    if (this.È == null)
    {
      if (this.Ç == null)
        return "BasicConstraints: isCa(false)";
      return "BasicConstraints: isCa(" + isCA() + ")";
    }
    return "BasicConstraints: isCa(" + isCA() + "), pathLenConstraint = " + this.È.getValue();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.BasicConstraints
 * JD-Core Version:    0.6.0
 */