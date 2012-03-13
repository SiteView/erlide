package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1EncodableVector;
import com.maverick.crypto.asn1.ASN1OctetString;
import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DERBoolean;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.DERObjectIdentifier;
import com.maverick.crypto.asn1.DERSequence;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class X509Extensions
  implements DEREncodable
{
  public static final DERObjectIdentifier SubjectKeyIdentifier = new DERObjectIdentifier("2.5.29.14");
  public static final DERObjectIdentifier KeyUsage = new DERObjectIdentifier("2.5.29.15");
  public static final DERObjectIdentifier PrivateKeyUsagePeriod = new DERObjectIdentifier("2.5.29.16");
  public static final DERObjectIdentifier SubjectAlternativeName = new DERObjectIdentifier("2.5.29.17");
  public static final DERObjectIdentifier IssuerAlternativeName = new DERObjectIdentifier("2.5.29.18");
  public static final DERObjectIdentifier BasicConstraints = new DERObjectIdentifier("2.5.29.19");
  public static final DERObjectIdentifier CRLNumber = new DERObjectIdentifier("2.5.29.20");
  public static final DERObjectIdentifier ReasonCode = new DERObjectIdentifier("2.5.29.21");
  public static final DERObjectIdentifier InstructionCode = new DERObjectIdentifier("2.5.29.23");
  public static final DERObjectIdentifier InvalidityDate = new DERObjectIdentifier("2.5.29.24");
  public static final DERObjectIdentifier DeltaCRLIndicator = new DERObjectIdentifier("2.5.29.27");
  public static final DERObjectIdentifier IssuingDistributionPoint = new DERObjectIdentifier("2.5.29.28");
  public static final DERObjectIdentifier CertificateIssuer = new DERObjectIdentifier("2.5.29.29");
  public static final DERObjectIdentifier NameConstraints = new DERObjectIdentifier("2.5.29.30");
  public static final DERObjectIdentifier CRLDistributionPoints = new DERObjectIdentifier("2.5.29.31");
  public static final DERObjectIdentifier CertificatePolicies = new DERObjectIdentifier("2.5.29.32");
  public static final DERObjectIdentifier PolicyMappings = new DERObjectIdentifier("2.5.29.33");
  public static final DERObjectIdentifier AuthorityKeyIdentifier = new DERObjectIdentifier("2.5.29.35");
  public static final DERObjectIdentifier PolicyConstraints = new DERObjectIdentifier("2.5.29.36");
  public static final DERObjectIdentifier ExtendedKeyUsage = new DERObjectIdentifier("2.5.29.37");
  public static final DERObjectIdentifier InhibitAnyPolicy = new DERObjectIdentifier("2.5.29.54");
  public static final DERObjectIdentifier AuthorityInfoAccess = new DERObjectIdentifier("1.3.6.1.5.5.7.1.1");
  private Hashtable ab = new Hashtable();
  private Vector bb = new Vector();

  public static X509Extensions getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public static X509Extensions getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof X509Extensions)))
      return (X509Extensions)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new X509Extensions((ASN1Sequence)paramObject);
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public X509Extensions(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (localEnumeration.hasMoreElements())
    {
      ASN1Sequence localASN1Sequence = (ASN1Sequence)localEnumeration.nextElement();
      if (localASN1Sequence.size() == 3)
        this.ab.put(localASN1Sequence.getObjectAt(0), new X509Extension((DERBoolean)localASN1Sequence.getObjectAt(1), (ASN1OctetString)localASN1Sequence.getObjectAt(2)));
      else
        this.ab.put(localASN1Sequence.getObjectAt(0), new X509Extension(false, (ASN1OctetString)localASN1Sequence.getObjectAt(1)));
      this.bb.addElement(localASN1Sequence.getObjectAt(0));
    }
  }

  public X509Extensions(Hashtable paramHashtable)
  {
    this(null, paramHashtable);
  }

  public X509Extensions(Vector paramVector, Hashtable paramHashtable)
  {
    if (paramVector == null)
      localEnumeration = paramHashtable.keys();
    else
      localEnumeration = paramVector.elements();
    while (localEnumeration.hasMoreElements())
      this.bb.addElement(localEnumeration.nextElement());
    Enumeration localEnumeration = this.bb.elements();
    while (localEnumeration.hasMoreElements())
    {
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
      X509Extension localX509Extension = (X509Extension)paramHashtable.get(localDERObjectIdentifier);
      this.ab.put(localDERObjectIdentifier, localX509Extension);
    }
  }

  public Enumeration oids()
  {
    return this.bb.elements();
  }

  public X509Extension getExtension(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return (X509Extension)this.ab.get(paramDERObjectIdentifier);
  }

  public DERObject getDERObject()
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    Enumeration localEnumeration = this.bb.elements();
    while (localEnumeration.hasMoreElements())
    {
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
      X509Extension localX509Extension = (X509Extension)this.ab.get(localDERObjectIdentifier);
      ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
      localASN1EncodableVector2.add(localDERObjectIdentifier);
      if (localX509Extension.isCritical())
        localASN1EncodableVector2.add(new DERBoolean(true));
      localASN1EncodableVector2.add(localX509Extension.getValue());
      localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
    }
    return new DERSequence(localASN1EncodableVector1);
  }

  public int hashCode()
  {
    Enumeration localEnumeration = this.ab.keys();
    int i = 0;
    while (localEnumeration.hasMoreElements())
    {
      Object localObject = localEnumeration.nextElement();
      i ^= localObject.hashCode();
      i ^= this.ab.get(localObject).hashCode();
    }
    return i;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof X509Extensions)))
      return false;
    X509Extensions localX509Extensions = (X509Extensions)paramObject;
    Enumeration localEnumeration1 = this.ab.keys();
    Enumeration localEnumeration2 = localX509Extensions.ab.keys();
    while ((localEnumeration1.hasMoreElements()) && (localEnumeration2.hasMoreElements()))
    {
      Object localObject1 = localEnumeration1.nextElement();
      Object localObject2 = localEnumeration2.nextElement();
      if (!localObject1.equals(localObject2))
        return false;
    }
    return (!localEnumeration1.hasMoreElements()) && (!localEnumeration2.hasMoreElements());
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.X509Extensions
 * JD-Core Version:    0.6.0
 */