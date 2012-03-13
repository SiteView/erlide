package com.maverick.ssh;

import java.io.IOException;

public abstract interface ShellWriter
{
  public abstract void interrupt()
    throws IOException;

  public abstract void type(String paramString)
    throws IOException;

  public abstract void carriageReturn()
    throws IOException;

  public abstract void typeAndReturn(String paramString)
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellWriter
 * JD-Core Version:    0.6.0
 */