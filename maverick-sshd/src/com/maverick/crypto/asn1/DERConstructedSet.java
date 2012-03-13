package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class DERConstructedSet extends ASN1Set
{
  public DERConstructedSet()
  {
  }

  public DERConstructedSet(DEREncodable paramDEREncodable)
  {
    addObject(paramDEREncodable);
  }

  public DERConstructedSet(DEREncodableVector paramDEREncodableVector)
  {
    for (int i = 0; i != paramDEREncodableVector.size(); i++)
      addObject(paramDEREncodableVector.get(i));
  }

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
    paramDEROutputStream.A(49, localObject);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERConstructedSet
 * JD-Core Version:    0.6.0
 */