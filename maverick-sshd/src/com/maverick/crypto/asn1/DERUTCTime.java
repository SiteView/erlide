package com.maverick.crypto.asn1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DERUTCTime extends DERObject
{
  String Â;

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
    this.Â = paramString;
  }

  public DERUTCTime(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    this.Â = localSimpleDateFormat.format(paramDate);
  }

  DERUTCTime(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.Â = new String(arrayOfChar);
  }

  public String getTime()
  {
    if (this.Â.length() == 11)
      return this.Â.substring(0, 10) + "00GMT+00:00";
    if (this.Â.length() == 13)
      return this.Â.substring(0, 12) + "GMT+00:00";
    if (this.Â.length() == 17)
      return this.Â.substring(0, 12) + "GMT" + this.Â.substring(12, 15) + ":" + this.Â.substring(15, 17);
    return this.Â;
  }

  public String getAdjustedTime()
  {
    String str = getTime();
    if (str.charAt(0) < '5')
      return "20" + str;
    return "19" + str;
  }

  private byte[] G()
  {
    char[] arrayOfChar = this.Â.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(23, G());
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERUTCTime)))
      return false;
    return this.Â.equals(((DERUTCTime)paramObject).Â);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERUTCTime
 * JD-Core Version:    0.6.0
 */