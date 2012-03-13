package com.maverick.crypto.publickey;

class c
{
  private byte[] c;
  private int b;

  public c(byte[] paramArrayOfByte, int paramInt)
  {
    this.c = paramArrayOfByte;
    this.b = paramInt;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof c)))
      return false;
    c localc = (c)paramObject;
    if (localc.b != this.b)
      return false;
    if (localc.c.length != this.c.length)
      return false;
    for (int i = 0; i != localc.c.length; i++)
      if (localc.c[i] != this.c[i])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.c
 * JD-Core Version:    0.6.0
 */