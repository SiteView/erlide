package com.maverick.ssh.compression;

import java.io.IOException;

public abstract interface SshCompression
{
  public static final int INFLATER = 0;
  public static final int DEFLATER = 1;

  public abstract void init(int paramInt1, int paramInt2);

  public abstract byte[] compress(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract byte[] uncompress(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract String getAlgorithm();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.compression.SshCompression
 * JD-Core Version:    0.6.0
 */