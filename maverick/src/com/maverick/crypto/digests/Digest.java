package com.maverick.crypto.digests;

public abstract interface Digest
{
  public abstract int doFinal(byte[] paramArrayOfByte, int paramInt);

  public abstract void finish();

  public abstract String getAlgorithmName();

  public abstract int getDigestSize();

  public abstract void reset();

  public abstract void update(byte paramByte);

  public abstract void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.digests.Digest
 * JD-Core Version:    0.6.0
 */