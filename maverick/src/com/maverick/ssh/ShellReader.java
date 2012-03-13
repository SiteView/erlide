package com.maverick.ssh;

public abstract interface ShellReader
{
  public abstract String readLine()
    throws SshException, ShellTimeoutException;

  public abstract String readLine(long paramLong)
    throws SshException, ShellTimeoutException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellReader
 * JD-Core Version:    0.6.0
 */