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
  ASN1Sequence S;
  TBSCertificateStructure W;
  AlgorithmIdentifier U;
  DERBitString V;

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
    this.S = paramASN1Sequence;
    if (paramASN1Sequence.size() == 3)
    {
      this.W = TBSCertificateStructure.getInstance(paramASN1Sequence.getObjectAt(0));
      this.U = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
      this.V = ((DERBitString)paramASN1Sequence.getObjectAt(2));
    }
  }

  public TBSCertificateStructure getTBSCertificate()
  {
    return this.W;
  }

  public int getVersion()
  {
    return this.W.getVersion();
  }

  public DERInteger getSerialNumber()
  {
    return this.W.getSerialNumber();
  }

  public X509Name getIssuer()
  {
    return this.W.getIssuer();
  }

  public Time getStartDate()
  {
    return this.W.getStartDate();
  }

  public Time getEndDate()
  {
    return this.W.getEndDate();
  }

  public X509Name getSubject()
  {
    return this.W.getSubject();
  }

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
  {
    return this.W.getSubjectPublicKeyInfo();
  }

  public AlgorithmIdentifier getSignatureAlgorithm()
  {
    return this.U;
  }

  public DERBitString getSignature()
  {
    return this.V;
  }

  public DERObject getDERObject()
  {
    return this.S;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.X509CertificateStructure
 * JD-Core Version:    0.6.0
 */