package com.maverick.crypto.publickey;

class B
{
  private byte[] B;
  private int A;

  public B(byte[] paramArrayOfByte, int paramInt)
  {
    this.B = paramArrayOfByte;
    this.A = paramInt;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof B)))
      return false;
    B localB = (B)paramObject;
    if (localB.A != this.A)
      return false;
    if (localB.B.length != this.B.length)
      return false;
    for (int i = 0; i != localB.B.length; i++)
      if (localB.B[i] != this.B[i])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.publickey.B
 * JD-Core Version:    0.6.0
 */