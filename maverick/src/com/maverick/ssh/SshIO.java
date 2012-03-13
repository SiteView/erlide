package com.maverick.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract interface SshIO
{
  public abstract InputStream getInputStream()
    throws IOException;

  public abstract OutputStream getOutputStream()
    throws IOException;

  public abstract void close()
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshIO
 * JD-Core Version:    0.6.0
 */