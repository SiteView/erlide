package com.maverick.ssh;

public class LicenseManager
{
  public static final int EXPIRED = 1;
  public static final int INVALID = 2;
  public static final int OK = 4;
  public static final int NOT_LICENSED = 8;
  public static final int EXPIRED_SUBSCRIPTION = 16;

  public static void addLicense(String paramString)
  {
    SshConnector.b(paramString);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.LicenseManager
 * JD-Core Version:    0.6.0
 */