package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERGeneralizedTime;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.DERUTCTime;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Time
  implements DEREncodable
{
  DERObject t;

  public static Time getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public Time(DERObject paramDERObject)
  {
    if ((!(paramDERObject instanceof DERUTCTime)) && (!(paramDERObject instanceof DERGeneralizedTime)))
      throw new IllegalArgumentException("unknown object passed to Time");
    this.t = paramDERObject;
  }

  public Time(Date paramDate)
  {
    SimpleTimeZone localSimpleTimeZone = new SimpleTimeZone(0, "Z");
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    localSimpleDateFormat.setTimeZone(localSimpleTimeZone);
    String str = localSimpleDateFormat.format(paramDate) + "Z";
    int i = Integer.parseInt(str.substring(0, 4));
    if ((i < 1950) || (i > 2049))
      this.t = new DERGeneralizedTime(str);
    else
      this.t = new DERUTCTime(str.substring(2));
  }

  public static Time getInstance(Object paramObject)
  {
    if ((paramObject instanceof Time))
      return (Time)paramObject;
    if ((paramObject instanceof DERUTCTime))
      return new Time((DERUTCTime)paramObject);
    if ((paramObject instanceof DERGeneralizedTime))
      return new Time((DERGeneralizedTime)paramObject);
    throw new IllegalArgumentException("unknown object in factory");
  }

  public String getTime()
  {
    if ((this.t instanceof DERUTCTime))
      return ((DERUTCTime)this.t).getAdjustedTime();
    return ((DERGeneralizedTime)this.t).getTime();
  }

  public Date getDate()
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
    return localSimpleDateFormat.parse(getTime(), new ParsePosition(0));
  }

  public DERObject getDERObject()
  {
    return this.t;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.Time
 * JD-Core Version:    0.6.0
 */