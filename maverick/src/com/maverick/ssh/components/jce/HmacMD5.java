package com.maverick.ssh.components.jce;

public class HmacMD5 extends AbstractHmac
{
  public HmacMD5()
  {
    super("HmacMD5", 16);
  }

  public String getAlgorithm()
  {
    return "hmac-md5";
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.HmacMD5
 * JD-Core Version:    0.6.0
 */