package com.maverick.crypto.asn1;

import java.io.IOException;

public class BERNull extends DERNull
{
  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.write(5);
      paramDEROutputStream.write(0);
      paramDEROutputStream.write(0);
    }
    else
    {
      super.encode(paramDEROutputStream);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.BERNull
 * JD-Core Version:    0.6.0
 */