package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.DERBitString;

public class KeyUsage extends DERBitString
{
  public static final int digitalSignature = 128;
  public static final int nonRepudiation = 64;
  public static final int keyEncipherment = 32;
  public static final int dataEncipherment = 16;
  public static final int keyAgreement = 8;
  public static final int keyCertSign = 4;
  public static final int cRLSign = 2;
  public static final int encipherOnly = 1;
  public static final int decipherOnly = 32768;

  public KeyUsage(int paramInt)
  {
    super(getBytes(paramInt), getPadBits(paramInt));
  }

  public KeyUsage(DERBitString paramDERBitString)
  {
    super(paramDERBitString.getBytes(), paramDERBitString.getPadBits());
  }

  public String toString()
  {
    if (this.data.length == 1)
      return "KeyUsage: 0x" + Integer.toHexString(this.data[0] & 0xFF);
    return "KeyUsage: 0x" + Integer.toHexString((this.data[1] & 0xFF) << 8 | this.data[0] & 0xFF);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.KeyUsage
 * JD-Core Version:    0.6.0
 */