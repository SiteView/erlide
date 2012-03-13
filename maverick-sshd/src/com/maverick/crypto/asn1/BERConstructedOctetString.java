package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class BERConstructedOctetString extends DEROctetString
{
  private Vector Á;

  private static byte[] A(Vector paramVector)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    for (int i = 0; i != paramVector.size(); i++)
    {
      DEROctetString localDEROctetString = (DEROctetString)paramVector.elementAt(i);
      try
      {
        localByteArrayOutputStream.write(localDEROctetString.getOctets());
      }
      catch (IOException localIOException)
      {
        throw new RuntimeException("exception converting octets " + localIOException.toString());
      }
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public BERConstructedOctetString(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }

  public BERConstructedOctetString(Vector paramVector)
  {
    super(A(paramVector));
    this.Á = paramVector;
  }

  public BERConstructedOctetString(DERObject paramDERObject)
  {
    super(paramDERObject);
  }

  public BERConstructedOctetString(DEREncodable paramDEREncodable)
  {
    super(paramDEREncodable.getDERObject());
  }

  public byte[] getOctets()
  {
    return this.À;
  }

  public Enumeration getObjects()
  {
    if (this.Á == null)
      return F().elements();
    return this.Á.elements();
  }

  private Vector F()
  {
    int i = 0;
    int j = 0;
    Vector localVector = new Vector();
    while (j + 1 < this.À.length)
    {
      if ((this.À[j] == 0) && (this.À[(j + 1)] == 0))
      {
        arrayOfByte = new byte[j - i + 1];
        System.arraycopy(this.À, i, arrayOfByte, 0, arrayOfByte.length);
        localVector.addElement(new DEROctetString(arrayOfByte));
        i = j + 1;
      }
      j++;
    }
    byte[] arrayOfByte = new byte[this.À.length - i];
    System.arraycopy(this.À, i, arrayOfByte, 0, arrayOfByte.length);
    localVector.addElement(new DEROctetString(arrayOfByte));
    return localVector;
  }

  public void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.write(36);
      paramDEROutputStream.write(128);
      int i;
      if (this.Á != null)
      {
        for (i = 0; i != this.Á.size(); i++)
          paramDEROutputStream.writeObject(this.Á.elementAt(i));
      }
      else
      {
        i = 0;
        for (int j = 0; j + 1 < this.À.length; j++)
        {
          if ((this.À[j] != 0) || (this.À[(j + 1)] != 0))
            continue;
          arrayOfByte = new byte[j - i + 1];
          System.arraycopy(this.À, i, arrayOfByte, 0, arrayOfByte.length);
          paramDEROutputStream.writeObject(new DEROctetString(arrayOfByte));
          i = j + 1;
        }
        byte[] arrayOfByte = new byte[this.À.length - i];
        System.arraycopy(this.À, i, arrayOfByte, 0, arrayOfByte.length);
        paramDEROutputStream.writeObject(new DEROctetString(arrayOfByte));
      }
      paramDEROutputStream.write(0);
      paramDEROutputStream.write(0);
    }
    else
    {
      super.encode(paramDEROutputStream);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.BERConstructedOctetString
 * JD-Core Version:    0.6.0
 */