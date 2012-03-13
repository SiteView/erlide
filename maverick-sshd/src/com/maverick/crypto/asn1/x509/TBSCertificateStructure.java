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
  ASN1Sequence R;
  DERInteger N;
  DERInteger I;
  AlgorithmIdentifier H;
  X509Name M;
  Time G;
  Time K;
  X509Name Q;
  SubjectPublicKeyInfo D;
  DERBitString F;
  DERBitString J;
  X509Extensions P;

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
    int i = 0;
    this.R = paramASN1Sequence;
    if ((paramASN1Sequence.getObjectAt(0) instanceof DERTaggedObject))
    {
      this.N = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    }
    else
    {
      i = -1;
      this.N = new DERInteger(0);
    }
    this.I = DERInteger.getInstance(paramASN1Sequence.getObjectAt(i + 1));
    this.H = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(i + 2));
    this.M = X509Name.getInstance(paramASN1Sequence.getObjectAt(i + 3));
    ASN1Sequence localASN1Sequence = (ASN1Sequence)paramASN1Sequence.getObjectAt(i + 4);
    this.G = Time.getInstance(localASN1Sequence.getObjectAt(0));
    this.K = Time.getInstance(localASN1Sequence.getObjectAt(1));
    this.Q = X509Name.getInstance(paramASN1Sequence.getObjectAt(i + 5));
    this.D = SubjectPublicKeyInfo.getInstance(paramASN1Sequence.getObjectAt(i + 6));
    for (int j = paramASN1Sequence.size() - (i + 6) - 1; j > 0; j--)
    {
      DERTaggedObject localDERTaggedObject = (DERTaggedObject)paramASN1Sequence.getObjectAt(i + 6 + j);
      switch (localDERTaggedObject.getTagNo())
      {
      case 1:
        this.F = DERBitString.getInstance(localDERTaggedObject);
        break;
      case 2:
        this.J = DERBitString.getInstance(localDERTaggedObject);
        break;
      case 3:
        this.P = X509Extensions.getInstance(localDERTaggedObject);
      }
    }
  }

  public int getVersion()
  {
    return this.N.getValue().intValue() + 1;
  }

  public DERInteger getVersionNumber()
  {
    return this.N;
  }

  public DERInteger getSerialNumber()
  {
    return this.I;
  }

  public AlgorithmIdentifier getSignature()
  {
    return this.H;
  }

  public X509Name getIssuer()
  {
    return this.M;
  }

  public Time getStartDate()
  {
    return this.G;
  }

  public Time getEndDate()
  {
    return this.K;
  }

  public X509Name getSubject()
  {
    return this.Q;
  }

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
  {
    return this.D;
  }

  public DERBitString getIssuerUniqueId()
  {
    return this.F;
  }

  public DERBitString getSubjectUniqueId()
  {
    return this.J;
  }

  public X509Extensions getExtensions()
  {
    return this.P;
  }

  public DERObject getDERObject()
  {
    return this.R;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.TBSCertificateStructure
 * JD-Core Version:    0.6.0
 */