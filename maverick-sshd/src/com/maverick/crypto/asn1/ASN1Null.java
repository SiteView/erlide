package com.maverick.crypto.asn1;

import java.io.IOException;

public abstract class ASN1Null extends DERObject
{
  public int hashCode()
  {
    return 0;
  }

  public boolean equals(Object paramObject)
  {
    return (paramObject != null) && ((paramObject instanceof ASN1Null));
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1Null
 * JD-Core Version:    0.6.0
 */