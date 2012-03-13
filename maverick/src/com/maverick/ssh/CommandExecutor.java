package com.maverick.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommandExecutor
{
  SshSession e;
  String d;
  String b;
  String c;

  public CommandExecutor(SshSession paramSshSession, String paramString1, String paramString2, String paramString3, String paramString4)
    throws SshException, IOException
  {
    this.e = paramSshSession;
    this.d = paramString1;
    this.b = paramString3;
    this.c = paramString4;
    executeCommand(paramString2);
  }

  public String executeCommand(String paramString)
    throws SshException, IOException
  {
    try
    {
      this.e.getOutputStream().write(paramString.getBytes());
      this.e.getOutputStream().write(this.d.getBytes());
      StringBuffer localStringBuffer = new StringBuffer();
      do
      {
        int i = this.e.getInputStream().read();
        if (i == -1)
          break;
        localStringBuffer.append((char)i);
      }
      while (!localStringBuffer.toString().endsWith(this.b));
      return localStringBuffer.toString().substring(0, localStringBuffer.length() - this.b.length()).trim();
    }
    catch (SshIOException localSshIOException)
    {
    }
    throw localSshIOException.getRealException();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.CommandExecutor
 * JD-Core Version:    0.6.0
 */