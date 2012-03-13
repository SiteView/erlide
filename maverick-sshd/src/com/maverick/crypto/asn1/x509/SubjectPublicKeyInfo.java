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
  private AlgorithmIdentifier b;
  private DERBitString c;

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
    this.c = new DERBitString(paramDEREncodable);
    this.b = paramAlgorithmIdentifier;
  }

  public SubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.c = new DERBitString(paramArrayOfByte);
    this.b = paramAlgorithmIdentifier;
  }

  public SubjectPublicKeyInfo(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.b = AlgorithmIdentifier.getInstance(localEnumeration.nextElement());
    this.c = ((DERBitString)localEnumeration.nextElement());
  }

  public AlgorithmIdentifier getAlgorithmId()
  {
    return this.b;
  }

  public DERObject getPublicKey()
    throws IOException
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.c.getBytes());
    DERInputStream localDERInputStream = new DERInputStream(localByteArrayInputStream);
    return localDERInputStream.readObject();
  }

  public DERBitString getPublicKeyData()
  {
    return this.c;
  }

  public DERObject getDERObject()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.b);
    localASN1EncodableVector.add(this.c);
    return new DERSequence(localASN1EncodableVector);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.SubjectPublicKeyInfo
 * JD-Core Version:    0.6.0
 */