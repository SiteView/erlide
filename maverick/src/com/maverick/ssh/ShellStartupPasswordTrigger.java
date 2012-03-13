package com.maverick.ssh;

import java.io.IOException;

public class ShellStartupPasswordTrigger
  implements ShellStartupTrigger
{
  String c;
  String b;
  ShellMatcher d;

  public ShellStartupPasswordTrigger(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, new ShellDefaultMatcher());
  }

  public ShellStartupPasswordTrigger(String paramString1, String paramString2, ShellMatcher paramShellMatcher)
  {
    this.c = paramString1;
    this.b = paramString2;
    this.d = paramShellMatcher;
  }

  public boolean canStartShell(String paramString, ShellWriter paramShellWriter)
    throws IOException
  {
    if (this.d.matches(paramString, this.c))
    {
      paramShellWriter.typeAndReturn(this.b);
      return true;
    }
    return false;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellStartupPasswordTrigger
 * JD-Core Version:    0.6.0
 */