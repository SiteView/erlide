package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERTaggedObject extends ASN1TaggedObject
{
  public DERTaggedObject(int paramInt, DEREncodable paramDEREncodable)
  {
    super(paramInt, paramDEREncodable);
  }

  public DERTaggedObject(boolean paramBoolean, int paramInt, DEREncodable paramDEREncodable)
  {
    super(paramBoolean, paramInt, paramDEREncodable);
  }

  public DERTaggedObject(int paramInt)
  {
    super(false, paramInt, new DERSequence());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    if (!this.dc)
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
      localDEROutputStream.writeObject(this.ec);
      localDEROutputStream.close();
      byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
      if (this.bc)
      {
        paramDEROutputStream.b(0xA0 | this.cc, arrayOfByte);
      }
      else
      {
        if ((arrayOfByte[0] & 0x20) != 0)
          arrayOfByte[0] = (byte)(0xA0 | this.cc);
        else
          arrayOfByte[0] = (byte)(0x80 | this.cc);
        paramDEROutputStream.write(arrayOfByte);
      }
    }
    else
    {
      paramDEROutputStream.b(0xA0 | this.cc, new byte[0]);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERTaggedObject
 * JD-Core Version:    0.6.0
 */