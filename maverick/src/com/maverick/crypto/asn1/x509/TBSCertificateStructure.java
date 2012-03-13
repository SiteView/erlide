package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DERBitString;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERInteger;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.DERTaggedObject;
import com.maverick.crypto.asn1.pkcs.PKCSObjectIdentifiers;
import java.math.BigInteger;

public class TBSCertificateStructure
  implements DEREncodable, X509ObjectIdentifiers, PKCSObjectIdentifiers
{
  ASN1Sequence o;
  DERInteger l;
  DERInteger h;
  AlgorithmIdentifier g;
  X509Name k;
  Time f;
  Time j;
  X509Name n;
  SubjectPublicKeyInfo d;
  DERBitString e;
  DERBitString i;
  X509Extensions m;

  public static TBSCertificateStructure getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public static TBSCertificateStructure getInstance(Object paramObject)
  {
    if ((paramObject instanceof TBSCertificateStructure))
      return (TBSCertificateStructure)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new TBSCertificateStructure((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory");
  }

  public TBSCertificateStructure(ASN1Sequence paramASN1Sequence)
  {
    int i1 = 0;
    this.o = paramASN1Sequence;
    if ((paramASN1Sequence.getObjectAt(0) instanceof DERTaggedObject))
    {
      this.l = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    }
    else
    {
      i1 = -1;
      this.l = new DERInteger(0);
    }
    this.h = DERInteger.getInstance(paramASN1Sequence.getObjectAt(i1 + 1));
    this.g = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(i1 + 2));
    this.k = X509Name.getInstance(paramASN1Sequence.getObjectAt(i1 + 3));
    ASN1Sequence localASN1Sequence = (ASN1Sequence)paramASN1Sequence.getObjectAt(i1 + 4);
    this.f = Time.getInstance(localASN1Sequence.getObjectAt(0));
    this.j = Time.getInstance(localASN1Sequence.getObjectAt(1));
    this.n = X509Name.getInstance(paramASN1Sequence.getObjectAt(i1 + 5));
    this.d = SubjectPublicKeyInfo.getInstance(paramASN1Sequence.getObjectAt(i1 + 6));
    for (int i2 = paramASN1Sequence.size() - (i1 + 6) - 1; i2 > 0; i2--)
    {
      DERTaggedObject localDERTaggedObject = (DERTaggedObject)paramASN1Sequence.getObjectAt(i1 + 6 + i2);
      switch (localDERTaggedObject.getTagNo())
      {
      case 1:
        this.e = DERBitString.getInstance(localDERTaggedObject);
        break;
      case 2:
        this.i = DERBitString.getInstance(localDERTaggedObject);
        break;
      case 3:
        this.m = X509Extensions.getInstance(localDERTaggedObject);
      }
    }
  }

  public int getVersion()
  {
    return this.l.getValue().intValue() + 1;
  }

  public DERInteger getVersionNumber()
  {
    return this.l;
  }

  public DERInteger getSerialNumber()
  {
    return this.h;
  }

  public AlgorithmIdentifier getSignature()
  {
    return this.g;
  }

  public X509Name getIssuer()
  {
    return this.k;
  }

  public Time getStartDate()
  {
    return this.f;
  }

  public Time getEndDate()
  {
    return this.j;
  }

  public X509Name getSubject()
  {
    return this.n;
  }

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
  {
    return this.d;
  }

  public DERBitString getIssuerUniqueId()
  {
    return this.e;
  }

  public DERBitString getSubjectUniqueId()
  {
    return this.i;
  }

  public X509Extensions getExtensions()
  {
    return this.m;
  }

  public DERObject getDERObject()
  {
    return this.o;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.TBSCertificateStructure
 * JD-Core Version:    0.6.0
 */