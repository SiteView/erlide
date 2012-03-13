package com.maverick.crypto.engines;

import java.io.IOException;

public abstract interface CipherEngine
{
  public abstract void init(boolean paramBoolean, byte[] paramArrayOfByte);

  public abstract int getBlockSize();

  public abstract int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.engines.CipherEngine
 * JD-Core Version:    0.6.0
 */