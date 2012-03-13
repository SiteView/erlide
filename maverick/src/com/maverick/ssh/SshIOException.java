package com.maverick.ssh;

import java.io.IOException;

public class SshIOException extends IOException
{
  SshException b;

  public SshIOException(SshException paramSshException)
  {
    this.b = paramSshException;
  }

  public SshException getRealException()
  {
    return this.b;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshIOException
 * JD-Core Version:    0.6.0
 */