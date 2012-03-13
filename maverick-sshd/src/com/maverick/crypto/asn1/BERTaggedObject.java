package com.maverick.crypto.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERTaggedObject extends DERTaggedObject
{
  public BERTaggedObject(int paramInt, DEREncodable paramDEREncodable)
  {
    super(paramInt, paramDEREncodable);
  }

  public BERTaggedObject(boolean paramBoolean, int paramInt, DEREncodable paramDEREncodable)
  {
    super(paramBoolean, paramInt, paramDEREncodable);
  }

  public BERTaggedObject(int paramInt)
  {
    super(false, paramInt, new BERConstructedSequence());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (((paramDEROutputStream instanceof ASN1OutputStream)) || ((paramDEROutputStream instanceof BEROutputStream)))
    {
      paramDEROutputStream.write(0xA0 | this.ª);
      paramDEROutputStream.write(128);
      if (!this.µ)
        if (!this.¥)
        {
          Enumeration localEnumeration;
          if ((this.º instanceof ASN1OctetString))
          {
            if ((this.º instanceof BERConstructedOctetString))
            {
              localEnumeration = ((BERConstructedOctetString)this.º).getObjects();
            }
            else
            {
              ASN1OctetString localASN1OctetString = (ASN1OctetString)this.º;
              BERConstructedOctetString localBERConstructedOctetString = new BERConstructedOctetString(localASN1OctetString.getOctets());
              localEnumeration = localBERConstructedOctetString.getObjects();
            }
            while (localEnumeration.hasMoreElements())
              paramDEROutputStream.writeObject(localEnumeration.nextElement());
          }
          else if ((this.º instanceof ASN1Sequence))
          {
            localEnumeration = ((ASN1Sequence)this.º).getObjects();
            while (localEnumeration.hasMoreElements())
              paramDEROutputStream.writeObject(localEnumeration.nextElement());
          }
          else if ((this.º instanceof ASN1Set))
          {
            localEnumeration = ((ASN1Set)this.º).getObjects();
            while (localEnumeration.hasMoreElements())
              paramDEROutputStream.writeObject(localEnumeration.nextElement());
          }
          else
          {
            throw new RuntimeException("not implemented: " + this.º.getClass().getName());
          }
        }
        else
        {
          paramDEROutputStream.writeObject(this.º);
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
 * Qualified Name:     com.maverick.crypto.asn1.BERTaggedObject
 * JD-Core Version:    0.6.0
 */