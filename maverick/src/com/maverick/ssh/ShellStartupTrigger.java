package com.maverick.ssh;

import java.io.IOException;

public abstract interface ShellStartupTrigger
{
  public abstract boolean canStartShell(String paramString, ShellWriter paramShellWriter)
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellStartupTrigger
 * JD-Core Version:    0.6.0
 */