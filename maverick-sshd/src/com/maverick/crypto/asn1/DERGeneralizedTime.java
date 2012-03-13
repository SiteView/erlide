package com.maverick.crypto.asn1;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DERGeneralizedTime extends DERObject
{
  String z;

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
    this.z = paramString;
  }

  public DERGeneralizedTime(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    this.z = localSimpleDateFormat.format(paramDate);
  }

  DERGeneralizedTime(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfChar[i] = (char)(paramArrayOfByte[i] & 0xFF);
    this.z = new String(arrayOfChar);
  }

  public String getTime()
  {
    if (this.z.charAt(this.z.length() - 1) == 'Z')
      return this.z.substring(0, this.z.length() - 1) + "GMT+00:00";
    int i = this.z.length() - 5;
    int j = this.z.charAt(i);
    if ((j == 45) || (j == 43))
      return this.z.substring(0, i) + "GMT" + this.z.substring(i, i + 3) + ":" + this.z.substring(i + 3);
    i = this.z.length() - 3;
    j = this.z.charAt(i);
    if ((j == 45) || (j == 43))
      return this.z.substring(0, i) + "GMT" + this.z.substring(i) + ":00";
    return this.z;
  }

  public Date getDate()
    throws ParseException
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    return localSimpleDateFormat.parse(this.z);
  }

  private byte[] E()
  {
    char[] arrayOfChar = this.z.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; i != arrayOfChar.length; i++)
      arrayOfByte[i] = (byte)arrayOfChar[i];
    return arrayOfByte;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(24, E());
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERGeneralizedTime)))
      return false;
    return this.z.equals(((DERGeneralizedTime)paramObject).z);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERGeneralizedTime
 * JD-Core Version:    0.6.0
 */