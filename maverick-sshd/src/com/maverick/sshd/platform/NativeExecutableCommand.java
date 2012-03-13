package com.maverick.sshd.platform;

import com.maverick.util.IOStreamConnector;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

public class NativeExecutableCommand extends ExecutableCommand
{
  Process U;
  String[] R;
  String[] T;
  _A Q;
  int S = -2147483648;

  public NativeExecutableCommand()
  {
    super(65535);
  }

  public void onStart()
  {
    this.Q.start();
  }

  public int getExitCode()
  {
    return this.S;
  }

  public boolean createProcess(String paramString, Map paramMap)
  {
    this.R = G(paramString);
    Vector localVector = new Vector();
    if (paramMap != null)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localVector.add(localEntry.getKey().toString() + "=" + localEntry.getValue().toString());
      }
    }
    this.T = new String[localVector.size()];
    localVector.copyInto(this.T);
    try
    {
      this.U = Runtime.getRuntime().exec(this.R, this.T);
      this.Q = new _A();
      return true;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  private String[] G(String paramString)
  {
    Vector localVector = new Vector();
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int j = 33;
    for (int k = 0; k < paramString.length(); k++)
    {
      char c = paramString.charAt(k);
      if ((c == j) && (i != 0))
      {
        i = 0;
      }
      else if ((c == '"') || (c == '\''))
      {
        if (i == 0)
        {
          j = c;
          i = 1;
        }
        else
        {
          localStringBuffer.append(c);
        }
      }
      else if ((c == ' ') && (i == 0))
      {
        localVector.add(localStringBuffer.toString());
        localStringBuffer.delete(0, localStringBuffer.length());
      }
      else
      {
        localStringBuffer.append(c);
      }
    }
    if (localStringBuffer.length() > 0)
      localVector.add(localStringBuffer.toString());
    String[] arrayOfString = new String[localVector.size()];
    localVector.copyInto(arrayOfString);
    return arrayOfString;
  }

  public void processStdinData(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      if (this.U != null)
      {
        this.U.getOutputStream().write(paramArrayOfByte);
        this.U.getOutputStream().flush();
      }
    }
    catch (IOException localIOException)
    {
    }
  }

  public void kill()
  {
    this.U.destroy();
  }

  class _A extends Thread
  {
    IOStreamConnector A;
    IOStreamConnector B;

    _A()
    {
    }

    public void run()
    {
      try
      {
        this.A = new IOStreamConnector(NativeExecutableCommand.this.U.getInputStream(), NativeExecutableCommand.this.getOutputStream());
        this.B = new IOStreamConnector(NativeExecutableCommand.this.U.getErrorStream(), NativeExecutableCommand.this.getStderrOutputStream());
        NativeExecutableCommand.this.S = NativeExecutableCommand.this.U.waitFor();
      }
      catch (Throwable localThrowable)
      {
      }
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.platform.NativeExecutableCommand
 * JD-Core Version:    0.6.0
 */