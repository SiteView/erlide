package com.maverick.ssh.components.standalone;

public class Twofish256Cbc extends A
{
  public static final String TWOFISH256_CBC = "twofish256-cbc";
  public static final String TWOFISH_CBC = "twofish-cbc";

  public Twofish256Cbc()
  {
    super(256, new C(), "twofish256-cbc");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Twofish256Cbc
 * JD-Core Version:    0.6.0
 */