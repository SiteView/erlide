package com.maverick.crypto.asn1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DERUTCTime extends DERObject
{
  String hc;

  public static DERUTCTime getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERUTCTime)))
      return (DERUTCTime)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERUTCTime(((ASN1OctetString)paramObject).getOctets());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERUTCTime getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERUTCTime(String paramString)
  {
    this.hc = paramString;
  }

  public DERUTCTime(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    this.hc = localSimpleDateFormat.format(paramDate);
  }

  DERUTCTime(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.hc = new String(arrayOfChar);
  }

  public String getTime()
  {
    if (this.hc.length() == 11)
      return this.hc.substring(0, 10) + "00GMT+00:00";
    if (this.hc.length() == 13)
      return this.hc.substring(0, 12) + "GMT+00:00";
    if (this.hc.length() == 17)
      return this.hc.substring(0, 12) + "GMT" + this.hc.substring(12, 15) + ":" + this.hc.substring(15, 17);
    return this.hc;
  }

  public String getAdjustedTime()
  {
    String str = getTime();
    if (str.charAt(0) < '5')
      return "20" + str;
    return "19" + str;
  }

  private byte[] h()
  {
    char[] arrayOfChar = this.hc.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(23, h());
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERUTCTime)))
      return false;
    return this.hc.equals(((DERUTCTime)paramObject).hc);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERUTCTime
 * JD-Core Version:    0.6.0
 */