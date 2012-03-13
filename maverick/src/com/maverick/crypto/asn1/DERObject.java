package com.maverick.crypto.asn1;

import java.io.IOException;

public abstract class DERObject
  implements DERTags, DEREncodable
{
  public DERObject getDERObject()
  {
    return this;
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERObject
 * JD-Core Version:    0.6.0
 */