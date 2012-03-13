package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DERBitString;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERInteger;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.pkcs.PKCSObjectIdentifiers;

public class X509CertificateStructure
  implements DEREncodable, X509ObjectIdentifiers, PKCSObjectIdentifiers
{
  ASN1Sequence p;
  TBSCertificateStructure s;
  AlgorithmIdentifier q;
  DERBitString r;

  public static X509CertificateStructure getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public static X509CertificateStructure getInstance(Object paramObject)
  {
    if ((paramObject instanceof X509CertificateStructure))
      return (X509CertificateStructure)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new X509CertificateStructure((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory");
  }

  public X509CertificateStructure(ASN1Sequence paramASN1Sequence)
  {
    this.p = paramASN1Sequence;
    if (paramASN1Sequence.size() == 3)
    {
      this.s = TBSCertificateStructure.getInstance(paramASN1Sequence.getObjectAt(0));
      this.q = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
      this.r = ((DERBitString)paramASN1Sequence.getObjectAt(2));
    }
  }

  public TBSCertificateStructure getTBSCertificate()
  {
    return this.s;
  }

  public int getVersion()
  {
    return this.s.getVersion();
  }

  public DERInteger getSerialNumber()
  {
    return this.s.getSerialNumber();
  }

  public X509Name getIssuer()
  {
    return this.s.getIssuer();
  }

  public Time getStartDate()
  {
    return this.s.getStartDate();
  }

  public Time getEndDate()
  {
    return this.s.getEndDate();
  }

  public X509Name getSubject()
  {
    return this.s.getSubject();
  }

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
  {
    return this.s.getSubjectPublicKeyInfo();
  }

  public AlgorithmIdentifier getSignatureAlgorithm()
  {
    return this.q;
  }

  public DERBitString getSignature()
  {
    return this.r;
  }

  public DERObject getDERObject()
  {
    return this.p;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.X509CertificateStructure
 * JD-Core Version:    0.6.0
 */