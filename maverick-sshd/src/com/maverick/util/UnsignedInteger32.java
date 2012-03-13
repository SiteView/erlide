package com.maverick.util;

public class UnsignedInteger32
{
  public static final long MAX_VALUE = 4294967295L;
  public static final long MIN_VALUE = 0L;
  private Long A;

  public UnsignedInteger32(long paramLong)
  {
    if ((paramLong < 0L) || (paramLong > 4294967295L))
      throw new NumberFormatException();
    this.A = new Long(paramLong);
  }

  public UnsignedInteger32(String paramString)
    throws NumberFormatException
  {
    long l = Long.parseLong(paramString);
    if ((l < 0L) || (l > 4294967295L))
      throw new NumberFormatException();
    this.A = new Long(l);
  }

  public int intValue()
  {
    return (int)this.A.longValue();
  }

  public long longValue()
  {
    return this.A.longValue();
  }

  public String toString()
  {
    return this.A.toString();
  }

  public int hashCode()
  {
    return this.A.hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof UnsignedInteger32))
      return false;
    return ((UnsignedInteger32)paramObject).A.equals(this.A);
  }

  public static UnsignedInteger32 add(UnsignedInteger32 paramUnsignedInteger321, UnsignedInteger32 paramUnsignedInteger322)
  {
    return new UnsignedInteger32(paramUnsignedInteger321.longValue() + paramUnsignedInteger322.longValue());
  }

  public static UnsignedInteger32 add(UnsignedInteger32 paramUnsignedInteger32, int paramInt)
  {
    return new UnsignedInteger32(paramUnsignedInteger32.longValue() + paramInt);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.UnsignedInteger32
 * JD-Core Version:    0.6.0
 */