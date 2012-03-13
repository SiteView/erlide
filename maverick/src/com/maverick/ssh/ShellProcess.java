package com.maverick.ssh;

import java.io.InputStream;
import java.io.OutputStream;

public class ShellProcess
{
  Shell c;
  b b;

  ShellProcess(Shell paramShell, b paramb)
  {
    this.c = paramShell;
    this.b = paramb;
  }

  public InputStream getInputStream()
  {
    return this.b;
  }

  public OutputStream getOutputStream()
    throws SshIOException
  {
    return this.c.q;
  }

  public int getExitCode()
  {
    return this.b.c();
  }

  public boolean hasSucceeded()
  {
    return this.b.f();
  }

  public boolean isActive()
  {
    return this.b.b();
  }

  public String getCommandOutput()
  {
    return this.b.g();
  }

  public Shell getShell()
  {
    return this.c;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellProcess
 * JD-Core Version:    0.6.0
 */