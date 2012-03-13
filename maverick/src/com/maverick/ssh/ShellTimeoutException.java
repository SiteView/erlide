package com.maverick.ssh;

public class ShellTimeoutException extends Exception
{
  ShellTimeoutException()
  {
    super("The shell operation timed out");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellTimeoutException
 * JD-Core Version:    0.6.0
 */