package com.maverick.crypto.publickey;

import java.math.BigInteger;

public class DsaKey
{
  protected BigInteger p;
  protected BigInteger q;
  protected BigInteger g;

  public DsaKey()
  {
  }

  public DsaKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    this.p = paramBigInteger1;
    this.q = paramBigInteger2;
    this.g = paramBigInteger3;
  }

  public BigInteger getP()
  {
    return this.p;
  }

  public BigInteger getQ()
  {
    return this.q;
  }

  public BigInteger getG()
  {
    return this.g;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.DsaKey
 * JD-Core Version:    0.6.0
 */