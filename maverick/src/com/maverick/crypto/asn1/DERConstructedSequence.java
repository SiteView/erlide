package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class DERConstructedSequence extends ASN1Sequence
{
  public void addObject(DEREncodable paramDEREncodable)
  {
    super.addObject(paramDEREncodable);
  }

  public int getSize()
  {
    return size();
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
    Enumeration localEnumeration = getObjects();
    while (localEnumeration.hasMoreElements())
    {
      localObject = localEnumeration.nextElement();
      localDEROutputStream.writeObject(localObject);
    }
    localDEROutputStream.close();
    Object localObject = localByteArrayOutputStream.toByteArray();
    paramDEROutputStream.b(48, localObject);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERConstructedSequence
 * JD-Core Version:    0.6.0
 */