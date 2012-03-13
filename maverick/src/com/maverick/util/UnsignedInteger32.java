package com.maverick.util;

public class UnsignedInteger32
{
  public static final long MAX_VALUE = 4294967295L;
  public static final long MIN_VALUE = 0L;
  private Long b;

  public UnsignedInteger32(long paramLong)
  {
    if ((paramLong < 0L) || (paramLong > 4294967295L))
      throw new NumberFormatException();
    this.b = new Long(paramLong);
  }

  public UnsignedInteger32(String paramString)
    throws NumberFormatException
  {
    long l = Long.parseLong(paramString);
    if ((l < 0L) || (l > 4294967295L))
      throw new NumberFormatException();
    this.b = new Long(l);
  }

  public int intValue()
  {
    return (int)this.b.longValue();
  }

  public long longValue()
  {
    return this.b.longValue();
  }

  public String toString()
  {
    return this.b.toString();
  }

  public int hashCode()
  {
    return this.b.hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof UnsignedInteger32))
      return false;
    return ((UnsignedInteger32)paramObject).b.equals(this.b);
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

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.UnsignedInteger32
 * JD-Core Version:    0.6.0
 */