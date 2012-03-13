package com.maverick.crypto.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSet extends DERSet
{
  public BERSet()
  {
  }

  public BERSet(DEREncodable paramDEREncodable)
  {
    super(paramDEREncodable);
  }

  public BERSet(DEREncodableVector paramDEREncodableVector)
  {
    super(paramDEREncodableVector);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.write(49);
      paramDEROutputStream.write(128);
      Enumeration localEnumeration = getObjects();
      while (localEnumeration.hasMoreElements())
        paramDEROutputStream.writeObject(localEnumeration.nextElement());
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
 * Qualified Name:     com.maverick.crypto.asn1.BERSet
 * JD-Core Version:    0.6.0
 */