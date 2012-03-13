package com.maverick.util;

import java.math.BigInteger;

public class UnsignedInteger64
{
  public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");
  public static final BigInteger MIN_VALUE = new BigInteger("0");
  private BigInteger A;

  public UnsignedInteger64(String paramString)
    throws NumberFormatException
  {
    this.A = new BigInteger(paramString);
    if ((this.A.compareTo(MIN_VALUE) < 0) || (this.A.compareTo(MAX_VALUE) > 0))
      throw new NumberFormatException();
  }

  public UnsignedInteger64(byte[] paramArrayOfByte)
    throws NumberFormatException
  {
    this.A = new BigInteger(paramArrayOfByte);
    if ((this.A.compareTo(MIN_VALUE) < 0) || (this.A.compareTo(MAX_VALUE) > 0))
      throw new NumberFormatException();
  }

  public UnsignedInteger64(long paramLong)
  {
    this.A = BigInteger.valueOf(paramLong);
  }

  public UnsignedInteger64(BigInteger paramBigInteger)
  {
    this.A = new BigInteger(paramBigInteger.toString());
    if ((this.A.compareTo(MIN_VALUE) < 0) || (this.A.compareTo(MAX_VALUE) > 0))
      throw new NumberFormatException();
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == null)
      return false;
    try
    {
      UnsignedInteger64 localUnsignedInteger64 = (UnsignedInteger64)paramObject;
      return localUnsignedInteger64.A.equals(this.A);
    }
    catch (ClassCastException localClassCastException)
    {
    }
    return false;
  }

  public BigInteger bigIntValue()
  {
    return this.A;
  }

  public long longValue()
  {
    return this.A.longValue();
  }

  public String toString()
  {
    return this.A.toString(10);
  }

  public int hashCode()
  {
    return this.A.hashCode();
  }

  public static UnsignedInteger64 add(UnsignedInteger64 paramUnsignedInteger641, UnsignedInteger64 paramUnsignedInteger642)
  {
    return new UnsignedInteger64(paramUnsignedInteger641.A.add(paramUnsignedInteger642.A));
  }

  public static UnsignedInteger64 add(UnsignedInteger64 paramUnsignedInteger64, int paramInt)
  {
    return new UnsignedInteger64(paramUnsignedInteger64.A.add(BigInteger.valueOf(paramInt)));
  }

  public byte[] toByteArray()
  {
    byte[] arrayOfByte1 = new byte[8];
    byte[] arrayOfByte2 = bigIntValue().toByteArray();
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, arrayOfByte1.length - arrayOfByte2.length, arrayOfByte2.length);
    return arrayOfByte1;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.UnsignedInteger64
 * JD-Core Version:    0.6.0
 */