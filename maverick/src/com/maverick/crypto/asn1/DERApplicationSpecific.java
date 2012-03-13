package com.maverick.crypto.asn1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERApplicationSpecific extends DERObject
{
  private int tb;
  private byte[] ub;

  public DERApplicationSpecific(int paramInt, byte[] paramArrayOfByte)
  {
    this.tb = paramInt;
    this.ub = paramArrayOfByte;
  }

  public DERApplicationSpecific(int paramInt, DEREncodable paramDEREncodable)
    throws IOException
  {
    this.tb = (paramInt | 0x20);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
    localDEROutputStream.writeObject(paramDEREncodable);
    this.ub = localByteArrayOutputStream.toByteArray();
  }

  public boolean isConstructed()
  {
    return (this.tb & 0x20) != 0;
  }

  public byte[] getContents()
  {
    return this.ub;
  }

  public int getApplicationTag()
  {
    return this.tb & 0x1F;
  }

  public DERObject getObject()
    throws IOException
  {
    return new ASN1InputStream(new ByteArrayInputStream(getContents())).readObject();
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(0x40 | this.tb, this.ub);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERApplicationSpecific
 * JD-Core Version:    0.6.0
 */