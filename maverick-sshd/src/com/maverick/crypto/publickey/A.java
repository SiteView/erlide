package com.maverick.crypto.publickey;

import java.math.BigInteger;

class A
{
  private BigInteger B;
  private BigInteger C;
  private BigInteger D;
  private B A;

  public A(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, B paramB)
  {
    this.B = paramBigInteger3;
    this.D = paramBigInteger1;
    this.C = paramBigInteger2;
    this.A = paramB;
  }

  public BigInteger B()
  {
    return this.D;
  }

  public BigInteger A()
  {
    return this.C;
  }

  public BigInteger C()
  {
    return this.B;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof A))
      return false;
    A localA = (A)paramObject;
    return (localA.B().equals(this.D)) && (localA.A().equals(this.C)) && (localA.C().equals(this.B));
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.publickey.A
 * JD-Core Version:    0.6.0
 */