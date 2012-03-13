package com.maverick.ssh;

import java.io.IOException;

public class SshIOException extends IOException
{
  SshException A;

  public SshIOException(SshException paramSshException)
  {
    this.A = paramSshException;
  }

  public SshException getRealException()
  {
    return this.A;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.SshIOException
 * JD-Core Version:    0.6.0
 */