package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ASN1Encodable
  implements DEREncodable
{
  public byte[] getEncoded()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ASN1OutputStream localASN1OutputStream = new ASN1OutputStream(localByteArrayOutputStream);
    localASN1OutputStream.writeObject(this);
    return localByteArrayOutputStream.toByteArray();
  }

  public int hashCode()
  {
    return getDERObject().hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DEREncodable)))
      return false;
    DEREncodable localDEREncodable = (DEREncodable)paramObject;
    return getDERObject().equals(localDEREncodable.getDERObject());
  }

  public DERObject getDERObject()
  {
    return toASN1Object();
  }

  public abstract DERObject toASN1Object();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1Encodable
 * JD-Core Version:    0.6.0
 */