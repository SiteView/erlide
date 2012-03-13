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
  private DERObjectIdentifier Ä;
  private DEREncodable Æ;
  private boolean Å = false;

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
    this.Ä = paramDERObjectIdentifier;
  }

  public AlgorithmIdentifier(String paramString)
  {
    this.Ä = new DERObjectIdentifier(paramString);
  }

  public AlgorithmIdentifier(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.Å = true;
    this.Ä = paramDERObjectIdentifier;
    this.Æ = paramDEREncodable;
  }

  public AlgorithmIdentifier(ASN1Sequence paramASN1Sequence)
  {
    this.Ä = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() == 2)
    {
      this.Å = true;
      this.Æ = paramASN1Sequence.getObjectAt(1);
    }
    else
    {
      this.Æ = null;
    }
  }

  public DERObjectIdentifier getObjectId()
  {
    return this.Ä;
  }

  public DEREncodable getParameters()
  {
    return this.Æ;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.Ä);
    if (this.Å)
      localASN1EncodableVector.add(this.Æ);
    return new DERSequence(localASN1EncodableVector);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.AlgorithmIdentifier
 * JD-Core Version:    0.6.0
 */