package com.maverick.util;

import java.math.BigInteger;

public class UnsignedInteger64
{
  public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");
  public static final BigInteger MIN_VALUE = new BigInteger("0");
  private BigInteger b;

  public UnsignedInteger64(String paramString)
    throws NumberFormatException
  {
    this.b = new BigInteger(paramString);
    if ((this.b.compareTo(MIN_VALUE) < 0) || (this.b.compareTo(MAX_VALUE) > 0))
      throw new NumberFormatException();
  }

  public UnsignedInteger64(byte[] paramArrayOfByte)
    throws NumberFormatException
  {
    this.b = new BigInteger(paramArrayOfByte);
    if ((this.b.compareTo(MIN_VALUE) < 0) || (this.b.compareTo(MAX_VALUE) > 0))
      throw new NumberFormatException();
  }

  public UnsignedInteger64(long paramLong)
  {
    this.b = BigInteger.valueOf(paramLong);
  }

  public UnsignedInteger64(BigInteger paramBigInteger)
  {
    this.b = new BigInteger(paramBigInteger.toString());
    if ((this.b.compareTo(MIN_VALUE) < 0) || (this.b.compareTo(MAX_VALUE) > 0))
      throw new NumberFormatException();
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == null)
      return false;
    try
    {
      UnsignedInteger64 localUnsignedInteger64 = (UnsignedInteger64)paramObject;
      return localUnsignedInteger64.b.equals(this.b);
    }
    catch (ClassCastException localClassCastException)
    {
    }
    return false;
  }

  public BigInteger bigIntValue()
  {
    return this.b;
  }

  public long longValue()
  {
    return this.b.longValue();
  }

  public String toString()
  {
    return this.b.toString(10);
  }

  public int hashCode()
  {
    return this.b.hashCode();
  }

  public static UnsignedInteger64 add(UnsignedInteger64 paramUnsignedInteger641, UnsignedInteger64 paramUnsignedInteger642)
  {
    return new UnsignedInteger64(paramUnsignedInteger641.b.add(paramUnsignedInteger642.b));
  }

  public static UnsignedInteger64 add(UnsignedInteger64 paramUnsignedInteger64, int paramInt)
  {
    return new UnsignedInteger64(paramUnsignedInteger64.b.add(BigInteger.valueOf(paramInt)));
  }

  public byte[] toByteArray()
  {
    byte[] arrayOfByte1 = new byte[8];
    byte[] arrayOfByte2 = bigIntValue().toByteArray();
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, arrayOfByte1.length - arrayOfByte2.length, arrayOfByte2.length);
    return arrayOfByte1;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.UnsignedInteger64
 * JD-Core Version:    0.6.0
 */