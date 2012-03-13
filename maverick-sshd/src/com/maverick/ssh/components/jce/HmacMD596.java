package com.maverick.ssh.components.jce;

public class HmacMD596 extends AbstractHmac
{
  public HmacMD596()
  {
    super("HmacMD5", 16, 12);
  }

  public String getAlgorithm()
  {
    return "hmac-md5";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.HmacMD596
 * JD-Core Version:    0.6.0
 */