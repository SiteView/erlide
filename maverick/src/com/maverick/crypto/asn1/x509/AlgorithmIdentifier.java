package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1Encodable;
import com.maverick.crypto.asn1.ASN1EncodableVector;
import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.DERObjectIdentifier;
import com.maverick.crypto.asn1.DERSequence;

public class AlgorithmIdentifier extends ASN1Encodable
{
  private DERObjectIdentifier kc;
  private DEREncodable mc;
  private boolean lc = false;

  public static AlgorithmIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public static AlgorithmIdentifier getInstance(Object paramObject)
  {
    if ((paramObject instanceof AlgorithmIdentifier))
      return (AlgorithmIdentifier)paramObject;
    if ((paramObject instanceof DERObjectIdentifier))
      return new AlgorithmIdentifier((DERObjectIdentifier)paramObject);
    if ((paramObject instanceof String))
      return new AlgorithmIdentifier((String)paramObject);
    if ((paramObject instanceof ASN1Sequence))
      return new AlgorithmIdentifier((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory");
  }

  public AlgorithmIdentifier(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.kc = paramDERObjectIdentifier;
  }

  public AlgorithmIdentifier(String paramString)
  {
    this.kc = new DERObjectIdentifier(paramString);
  }

  public AlgorithmIdentifier(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.lc = true;
    this.kc = paramDERObjectIdentifier;
    this.mc = paramDEREncodable;
  }

  public AlgorithmIdentifier(ASN1Sequence paramASN1Sequence)
  {
    this.kc = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() == 2)
    {
      this.lc = true;
      this.mc = paramASN1Sequence.getObjectAt(1);
    }
    else
    {
      this.mc = null;
    }
  }

  public DERObjectIdentifier getObjectId()
  {
    return this.kc;
  }

  public DEREncodable getParameters()
  {
    return this.mc;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.kc);
    if (this.lc)
      localASN1EncodableVector.add(this.mc);
    return new DERSequence(localASN1EncodableVector);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.AlgorithmIdentifier
 * JD-Core Version:    0.6.0
 */