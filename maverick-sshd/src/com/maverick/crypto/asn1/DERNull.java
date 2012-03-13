package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERNull extends ASN1Null
{
  byte[] v = new byte[0];

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(5, this.v);
  }

  public boolean equals(Object paramObject)
  {
    return (paramObject != null) && ((paramObject instanceof DERNull));
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERNull
 * JD-Core Version:    0.6.0
 */