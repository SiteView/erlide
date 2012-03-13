package com.maverick.ssh;

import java.io.IOException;

public abstract interface Client
{
  public abstract void exit()
    throws SshException, ShellTimeoutException, IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.Client
 * JD-Core Version:    0.6.0
 */