package com.maverick.crypto.asn1;

import java.io.IOException;

public class DEROctetString extends ASN1OctetString
{
  public DEROctetString(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }

  public DEROctetString(DEREncodable paramDEREncodable)
  {
    super(paramDEREncodable);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(4, this.fc);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DEROctetString
 * JD-Core Version:    0.6.0
 */