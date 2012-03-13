package com.maverick.ssh;

public abstract interface SshAuthentication
{
  public static final int COMPLETE = 1;
  public static final int FAILED = 2;
  public static final int FURTHER_AUTHENTICATION_REQUIRED = 3;
  public static final int CANCELLED = 4;
  public static final int PUBLIC_KEY_ACCEPTABLE = 5;

  public abstract void setUsername(String paramString);

  public abstract String getUsername();

  public abstract String getMethod();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.SshAuthentication
 * JD-Core Version:    0.6.0
 */