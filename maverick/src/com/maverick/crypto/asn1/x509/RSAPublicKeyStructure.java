package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1EncodableVector;
import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERInteger;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.DERSequence;
import java.math.BigInteger;
import java.util.Enumeration;

public class RSAPublicKeyStructure
  implements DEREncodable
{
  private BigInteger b;
  private BigInteger c;

  public static RSAPublicKeyStructure getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public static RSAPublicKeyStructure getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof RSAPublicKeyStructure)))
      return (RSAPublicKeyStructure)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RSAPublicKeyStructure((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid RSAPublicKeyStructure: " + paramObject.getClass().getName());
  }

  public RSAPublicKeyStructure(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    this.b = paramBigInteger1;
    this.c = paramBigInteger2;
  }

  public RSAPublicKeyStructure(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.b = ((DERInteger)localEnumeration.nextElement()).getPositiveValue();
    this.c = ((DERInteger)localEnumeration.nextElement()).getPositiveValue();
  }

  public BigInteger getModulus()
  {
    return this.b;
  }

  public BigInteger getPublicExponent()
  {
    return this.c;
  }

  public DERObject getDERObject()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(new DERInteger(getModulus()));
    localASN1EncodableVector.add(new DERInteger(getPublicExponent()));
    return new DERSequence(localASN1EncodableVector);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.RSAPublicKeyStructure
 * JD-Core Version:    0.6.0
 */