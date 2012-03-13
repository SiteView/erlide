package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class DERSet extends ASN1Set
{
  public DERSet()
  {
  }

  public DERSet(DEREncodable paramDEREncodable)
  {
    addObject(paramDEREncodable);
  }

  public DERSet(DEREncodableVector paramDEREncodableVector)
  {
    for (int i = 0; i != paramDEREncodableVector.size(); i++)
      addObject(paramDEREncodableVector.get(i));
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
    paramDEROutputStream.b(49, localObject);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERSet
 * JD-Core Version:    0.6.0
 */