package com.maverick.ssh.components.standalone;

public class CAST128Cbc extends A
{
  public static final String CAST128_CBC = "cast128-cbc";

  public CAST128Cbc()
  {
    super(128, new CAST5Engine(), "cast128-cbc");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.CAST128Cbc
 * JD-Core Version:    0.6.0
 */