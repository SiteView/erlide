package com.maverick.crypto.publickey;

import java.math.BigInteger;

class b
{
  private BigInteger c;
  private BigInteger d;
  private BigInteger e;
  private c b;

  public b(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, c paramc)
  {
    this.c = paramBigInteger3;
    this.e = paramBigInteger1;
    this.d = paramBigInteger2;
    this.b = paramc;
  }

  public BigInteger c()
  {
    return this.e;
  }

  public BigInteger b()
  {
    return this.d;
  }

  public BigInteger d()
  {
    return this.c;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof b))
      return false;
    b localb = (b)paramObject;
    return (localb.c().equals(this.e)) && (localb.b().equals(this.d)) && (localb.d().equals(this.c));
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.b
 * JD-Core Version:    0.6.0
 */