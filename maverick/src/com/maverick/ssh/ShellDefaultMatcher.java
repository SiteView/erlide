package com.maverick.ssh;

public class ShellDefaultMatcher
  implements ShellMatcher
{
  public boolean matches(String paramString1, String paramString2)
  {
    return paramString1.indexOf(paramString2) > -1;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellDefaultMatcher
 * JD-Core Version:    0.6.0
 */