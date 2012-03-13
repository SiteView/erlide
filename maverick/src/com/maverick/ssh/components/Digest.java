package com.maverick.ssh.components;

import java.math.BigInteger;

public abstract interface Digest
{
  public abstract void putBigInteger(BigInteger paramBigInteger);

  public abstract void putByte(byte paramByte);

  public abstract void putBytes(byte[] paramArrayOfByte);

  public abstract void putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  public abstract void putInt(int paramInt);

  public abstract void putString(String paramString);

  public abstract void reset();

  public abstract byte[] doFinal();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.Digest
 * JD-Core Version:    0.6.0
 */