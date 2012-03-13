package com.maverick.crypto.digests;

import java.io.IOException;

public abstract interface HMac
{
  public abstract int doFinal(byte[] paramArrayOfByte, int paramInt);

  public abstract int getMacSize();

  public abstract int getOutputSize();

  public abstract void init(byte[] paramArrayOfByte)
    throws IOException;

  public abstract void reset();

  public abstract void update(byte paramByte);

  public abstract void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.digests.HMac
 * JD-Core Version:    0.6.0
 */