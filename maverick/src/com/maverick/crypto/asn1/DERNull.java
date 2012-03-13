package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERNull extends ASN1Null
{
  byte[] sb = new byte[0];

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(5, this.sb);
  }

  public boolean equals(Object paramObject)
  {
    return (paramObject != null) && ((paramObject instanceof DERNull));
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERNull
 * JD-Core Version:    0.6.0
 */