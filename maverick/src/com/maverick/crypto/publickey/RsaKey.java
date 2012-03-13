package com.maverick.crypto.publickey;

import java.math.BigInteger;

public abstract class RsaKey
{
  protected BigInteger modulus;

  public RsaKey()
  {
  }

  public RsaKey(BigInteger paramBigInteger)
  {
    this.modulus = paramBigInteger;
  }

  public BigInteger getModulus()
  {
    return this.modulus;
  }

  protected void setModulus(BigInteger paramBigInteger)
  {
    this.modulus = paramBigInteger;
  }

  public int getBitLength()
  {
    return this.modulus.bitLength();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.RsaKey
 * JD-Core Version:    0.6.0
 */