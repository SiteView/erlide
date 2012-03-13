package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1OctetString extends DERObject
{
  byte[] À;

  public static ASN1OctetString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public static ASN1OctetString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ASN1OctetString)))
      return (ASN1OctetString)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    if ((paramObject instanceof ASN1Sequence))
    {
      Vector localVector = new Vector();
      Enumeration localEnumeration = ((ASN1Sequence)paramObject).getObjects();
      while (localEnumeration.hasMoreElements())
        localVector.addElement(localEnumeration.nextElement());
      return new BERConstructedOctetString(localVector);
    }
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public ASN1OctetString(byte[] paramArrayOfByte)
  {
    this.À = paramArrayOfByte;
  }

  public ASN1OctetString(DEREncodable paramDEREncodable)
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
      localDEROutputStream.writeObject(paramDEREncodable);
      localDEROutputStream.close();
      this.À = localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("Error processing object : " + localIOException.toString());
    }
  }

  public byte[] getOctets()
  {
    return this.À;
  }

  public int hashCode()
  {
    byte[] arrayOfByte = getOctets();
    int i = 0;
    for (int j = 0; j != arrayOfByte.length; j++)
      i ^= (arrayOfByte[j] & 0xFF) << j % 4;
    return i;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DEROctetString)))
      return false;
    DEROctetString localDEROctetString = (DEROctetString)paramObject;
    byte[] arrayOfByte1 = localDEROctetString.getOctets();
    byte[] arrayOfByte2 = getOctets();
    if (arrayOfByte1.length != arrayOfByte2.length)
      return false;
    for (int i = 0; i != arrayOfByte1.length; i++)
      if (arrayOfByte1[i] != arrayOfByte2[i])
        return false;
    return true;
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1OctetString
 * JD-Core Version:    0.6.0
 */