package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1EncodableVector;
import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DERBitString;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERInputStream;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.DERSequence;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;

public class SubjectPublicKeyInfo
  implements DEREncodable
{
  private AlgorithmIdentifier y;
  private DERBitString z;

  public static SubjectPublicKeyInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public static SubjectPublicKeyInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof SubjectPublicKeyInfo))
      return (SubjectPublicKeyInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SubjectPublicKeyInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory");
  }

  public SubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, DEREncodable paramDEREncodable)
  {
    this.z = new DERBitString(paramDEREncodable);
    this.y = paramAlgorithmIdentifier;
  }

  public SubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.z = new DERBitString(paramArrayOfByte);
    this.y = paramAlgorithmIdentifier;
  }

  public SubjectPublicKeyInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.y = AlgorithmIdentifier.getInstance(localEnumeration.nextElement());
    this.z = ((DERBitString)localEnumeration.nextElement());
  }

  public AlgorithmIdentifier getAlgorithmId()
  {
    return this.y;
  }

  public DERObject getPublicKey()
    throws IOException
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.z.getBytes());
    DERInputStream localDERInputStream = new DERInputStream(localByteArrayInputStream);
    return localDERInputStream.readObject();
  }

  public DERBitString getPublicKeyData()
  {
    return this.z;
  }

  public DERObject getDERObject()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.y);
    localASN1EncodableVector.add(this.z);
    return new DERSequence(localASN1EncodableVector);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.SubjectPublicKeyInfo
 * JD-Core Version:    0.6.0
 */