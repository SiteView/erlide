package com.maverick.crypto.asn1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERApplicationSpecific extends DERObject
{
  private int w;
  private byte[] x;

  public DERApplicationSpecific(int paramInt, byte[] paramArrayOfByte)
  {
    this.w = paramInt;
    this.x = paramArrayOfByte;
  }

  public DERApplicationSpecific(int paramInt, DEREncodable paramDEREncodable)
    throws IOException
  {
    this.w = (paramInt | 0x20);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
    localDEROutputStream.writeObject(paramDEREncodable);
    this.x = localByteArrayOutputStream.toByteArray();
  }

  public boolean isConstructed()
  {
    return (this.w & 0x20) != 0;
  }

  public byte[] getContents()
  {
    return this.x;
  }

  public int getApplicationTag()
  {
    return this.w & 0x1F;
  }

  public DERObject getObject()
    throws IOException
  {
    return new ASN1InputStream(new ByteArrayInputStream(getContents())).readObject();
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(0x40 | this.w, this.x);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERApplicationSpecific
 * JD-Core Version:    0.6.0
 */