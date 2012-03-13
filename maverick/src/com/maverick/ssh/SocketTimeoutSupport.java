package com.maverick.ssh;

import java.io.IOException;

public abstract interface SocketTimeoutSupport
{
  public abstract void setSoTimeout(int paramInt)
    throws IOException;

  public abstract int getSoTimeout()
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SocketTimeoutSupport
 * JD-Core Version:    0.6.0
 */