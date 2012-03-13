package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class BERInputStream extends DERInputStream
{
  private DERObject h = new DERObject()
  {
    void encode(DEROutputStream paramDEROutputStream)
      throws IOException
    {
      throw new IOException("Eeek!");
    }
  };

  public BERInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }

  private byte[] D()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i;
    for (int j = read(); ((i = read()) >= 0) && ((j != 0) || (i != 0)); j = i)
      localByteArrayOutputStream.write(j);
    return localByteArrayOutputStream.toByteArray();
  }

  private BERConstructedOctetString C()
    throws IOException
  {
    Vector localVector = new Vector();
    while (true)
    {
      DERObject localDERObject = readObject();
      if (localDERObject == this.h)
        break;
      localVector.addElement(localDERObject);
    }
    return new BERConstructedOctetString(localVector);
  }

  public DERObject readObject()
    throws IOException
  {
    int i = read();
    if (i == -1)
      throw new EOFException();
    int j = readLength();
    if (j < 0)
    {
      Object localObject2;
      Object localObject3;
      switch (i)
      {
      case 5:
        return null;
      case 48:
        localObject1 = new BERConstructedSequence();
        while (true)
        {
          localObject2 = readObject();
          if (localObject2 == this.h)
            break;
          ((BERConstructedSequence)localObject1).addObject((DEREncodable)localObject2);
        }
        return localObject1;
      case 36:
        return C();
      case 49:
        localObject2 = new ASN1EncodableVector();
        while (true)
        {
          localObject3 = readObject();
          if (localObject3 == this.h)
            break;
          ((ASN1EncodableVector)localObject2).add((DEREncodable)localObject3);
        }
        return new BERSet((DEREncodableVector)localObject2);
      }
      if ((i & 0x80) != 0)
      {
        if ((i & 0x1F) == 31)
          throw new IOException("unsupported high tag encountered");
        if ((i & 0x20) == 0)
        {
          localObject3 = D();
          return new BERTaggedObject(false, i & 0x1F, new DEROctetString(localObject3));
        }
        localObject3 = readObject();
        if (localObject3 == this.h)
          return new DERTaggedObject(i & 0x1F);
        DERObject localDERObject = readObject();
        if (localDERObject == this.h)
          return new BERTaggedObject(i & 0x1F, (DEREncodable)localObject3);
        localObject1 = new BERConstructedSequence();
        ((BERConstructedSequence)localObject1).addObject((DEREncodable)localObject3);
        do
        {
          ((BERConstructedSequence)localObject1).addObject(localDERObject);
          localDERObject = readObject();
        }
        while (localDERObject != this.h);
        return new BERTaggedObject(false, i & 0x1F, (DEREncodable)localObject1);
      }
      throw new IOException("unknown BER object encountered");
    }
    if ((i == 0) && (j == 0))
      return this.h;
    Object localObject1 = new byte[j];
    readFully(localObject1);
    return (DERObject)(DERObject)(DERObject)buildObject(i, localObject1);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.BERInputStream
 * JD-Core Version:    0.6.0
 */