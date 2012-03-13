package com.maverick.ssh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShellProcessController extends ShellController
{
  ShellProcess c;
  static Log b = LogFactory.getLog(ShellProcessController.class);

  public ShellProcessController(ShellProcess paramShellProcess)
  {
    this(paramShellProcess, new ShellDefaultMatcher());
  }

  public ShellProcessController(ShellProcess paramShellProcess, ShellMatcher paramShellMatcher)
  {
    super(paramShellProcess.getShell(), paramShellMatcher, paramShellProcess.getInputStream());
    this.c = paramShellProcess;
  }

  public boolean isActive()
  {
    return this.c.isActive();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellProcessController
 * JD-Core Version:    0.6.0
 */