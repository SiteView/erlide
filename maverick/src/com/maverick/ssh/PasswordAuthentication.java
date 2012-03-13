package com.maverick.ssh;

public class PasswordAuthentication
  implements SshAuthentication
{
  String s;
  String t;

  public void setPassword(String paramString)
  {
    this.s = paramString;
  }

  public String getPassword()
  {
    return this.s;
  }

  public String getMethod()
  {
    return "password";
  }

  public void setUsername(String paramString)
  {
    this.t = paramString;
  }

  public String getUsername()
  {
    return this.t;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.PasswordAuthentication
 * JD-Core Version:    0.6.0
 */