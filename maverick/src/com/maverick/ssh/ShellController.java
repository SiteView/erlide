package com.maverick.ssh;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShellController
  implements ShellReader, ShellWriter
{
  protected Shell shell;
  protected ShellMatcher matcher = null;
  protected InputStream in;
  static Log b = LogFactory.getLog(ShellController.class);

  ShellController(Shell paramShell, ShellMatcher paramShellMatcher, InputStream paramInputStream)
  {
    this.shell = paramShell;
    this.matcher = paramShellMatcher;
    this.in = paramInputStream;
  }

  public void setMatcher(ShellMatcher paramShellMatcher)
  {
    this.matcher = paramShellMatcher;
  }

  public void interrupt()
    throws IOException
  {
    this.shell.b(new String(new char[] { '\003' }));
  }

  public synchronized void type(String paramString)
    throws IOException
  {
    this.shell.b(paramString);
  }

  public synchronized void carriageReturn()
    throws IOException
  {
    this.shell.g();
  }

  public synchronized void typeAndReturn(String paramString)
    throws IOException
  {
    this.shell.c(paramString);
  }

  public synchronized boolean expect(String paramString)
    throws ShellTimeoutException, SshException
  {
    return expect(paramString, false, 0L, 0L);
  }

  public synchronized boolean expect(String paramString, boolean paramBoolean)
    throws ShellTimeoutException, SshException
  {
    return expect(paramString, paramBoolean, 0L, 0L);
  }

  public synchronized boolean expect(String paramString, long paramLong)
    throws ShellTimeoutException, SshException
  {
    return expect(paramString, false, paramLong, 0L);
  }

  public synchronized boolean expect(String paramString, boolean paramBoolean, long paramLong)
    throws ShellTimeoutException, SshException
  {
    return expect(paramString, paramBoolean, paramLong, 0L);
  }

  public synchronized boolean expectNextLine(String paramString)
    throws ShellTimeoutException, SshException
  {
    return expect(paramString, false, 0L, 1L);
  }

  public synchronized boolean expectNextLine(String paramString, boolean paramBoolean)
    throws ShellTimeoutException, SshException
  {
    return expect(paramString, paramBoolean, 0L, 1L);
  }

  public synchronized boolean expectNextLine(String paramString, boolean paramBoolean, long paramLong)
    throws ShellTimeoutException, SshException
  {
    return expect(paramString, paramBoolean, paramLong, 1L);
  }

  public synchronized boolean expect(String paramString, boolean paramBoolean, long paramLong1, long paramLong2)
    throws ShellTimeoutException, SshException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    long l1 = System.currentTimeMillis();
    long l2 = 0L;
    while ((System.currentTimeMillis() - l1 < paramLong1) || (paramLong1 == 0L))
    {
      if ((paramLong2 > 0L) && (l2 >= paramLong2))
        return false;
      try
      {
        int i = this.in.read();
        if (i == -1)
          return false;
        if ((i != 10) && (i != 13))
          localStringBuffer.append((char)i);
        if (this.matcher.matches(localStringBuffer.toString(), paramString))
        {
          if (b.isDebugEnabled())
            b.debug("Matched: [" + paramString + "] " + localStringBuffer.toString());
          while ((paramBoolean) && (i != 10) && (i != -1) && (i != 10) && (i != -1));
          if (b.isDebugEnabled())
            b.debug("Shell output: " + localStringBuffer.toString());
          return true;
        }
        if (i == 10)
        {
          l2 += 1L;
          if (b.isDebugEnabled())
            b.debug("Shell output: " + localStringBuffer.toString());
          localStringBuffer.delete(0, localStringBuffer.length());
        }
      }
      catch (SshIOException localSshIOException)
      {
        if (localSshIOException.getRealException().getReason() != 21)
          throw localSshIOException.getRealException();
      }
      catch (IOException localIOException)
      {
        throw new SshException(localIOException);
      }
    }
    throw new ShellTimeoutException();
  }

  public boolean isActive()
  {
    return this.shell.inStartup();
  }

  public synchronized String readLine()
    throws SshException, ShellTimeoutException
  {
    return readLine(0L);
  }

  public synchronized String readLine(long paramLong)
    throws SshException, ShellTimeoutException
  {
    if (!isActive())
      return null;
    StringBuffer localStringBuffer = new StringBuffer();
    long l = System.currentTimeMillis();
    do
      try
      {
        int i = this.in.read();
        if ((i == -1) || (i == 10))
        {
          if ((localStringBuffer.length() == 0) && (i == -1))
            return null;
          return localStringBuffer.toString();
        }
        if ((i != 10) && (i != 13))
          localStringBuffer.append((char)i);
      }
      catch (SshIOException localSshIOException)
      {
        if (localSshIOException.getRealException().getReason() != 21)
          throw localSshIOException.getRealException();
      }
      catch (IOException localIOException)
      {
        throw new SshException(localIOException);
      }
    while ((System.currentTimeMillis() - l < paramLong) || (paramLong == 0L));
    throw new ShellTimeoutException();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ShellController
 * JD-Core Version:    0.6.0
 */