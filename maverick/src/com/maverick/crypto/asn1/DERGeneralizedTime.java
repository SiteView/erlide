package com.maverick.crypto.asn1;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DERGeneralizedTime extends DERObject
{
  String wb;

  public static DERGeneralizedTime getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERGeneralizedTime)))
      return (DERGeneralizedTime)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERGeneralizedTime(((ASN1OctetString)paramObject).getOctets());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERGeneralizedTime getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERGeneralizedTime(String paramString)
  {
    this.wb = paramString;
  }

  public DERGeneralizedTime(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    this.wb = localSimpleDateFormat.format(paramDate);
  }

  DERGeneralizedTime(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.wb = new String(arrayOfChar);
  }

  public String getTime()
  {
    if (this.wb.charAt(this.wb.length() - 1) == 'Z')
      return this.wb.substring(0, this.wb.length() - 1) + "GMT+00:00";
    int i = this.wb.length() - 5;
    int j = this.wb.charAt(i);
    if ((j == 45) || (j == 43))
      return this.wb.substring(0, i) + "GMT" + this.wb.substring(i, i + 3) + ":" + this.wb.substring(i + 3);
    i = this.wb.length() - 3;
    j = this.wb.charAt(i);
    if ((j == 45) || (j == 43))
      return this.wb.substring(0, i) + "GMT" + this.wb.substring(i) + ":00";
    return this.wb;
  }

  public Date getDate()
    throws ParseException
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    return localSimpleDateFormat.parse(this.wb);
  }

  private byte[] f()
  {
    char[] arrayOfChar = this.wb.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(24, f());
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERGeneralizedTime)))
      return false;
    return this.wb.equals(((DERGeneralizedTime)paramObject).wb);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERGeneralizedTime
 * JD-Core Version:    0.6.0
 */