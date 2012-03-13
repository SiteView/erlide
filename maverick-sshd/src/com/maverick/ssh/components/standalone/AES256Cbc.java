package com.maverick.ssh.components.standalone;

public class AES256Cbc extends A
{
  public static final String AES256_CBC = "aes256-cbc";

  public AES256Cbc()
  {
    super(256, new B(), "aes256-cbc");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.AES256Cbc
 * JD-Core Version:    0.6.0
 */