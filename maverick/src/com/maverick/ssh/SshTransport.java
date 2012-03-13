package com.maverick.ssh;

import java.io.IOException;

public abstract interface SshTransport extends SshIO
{
  public abstract String getHost();

  public abstract int getPort();

  public abstract SshTransport duplicate()
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshTransport
 * JD-Core Version:    0.6.0
 */