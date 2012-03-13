package com.maverick.ssh;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Shell
{
  public static final int OS_WINDOWS = 1;
  public static final int OS_LINUX = 2;
  public static final int OS_SOLARIS = 3;
  public static final int OS_AIX = 4;
  public static final int OS_DARWIN = 5;
  public static final int OS_FREEBSD = 6;
  public static final int OS_OPENBSD = 7;
  public static final int OS_NETBSD = 8;
  public static final int OS_HPUX = 9;
  public static final int OS_UNKNOWN = 99;
  private int t = 99;
  private String n = "Unknown";
  BufferedInputStream j;
  OutputStream q;
  int b = 1;
  boolean m;
  private String r = "echo";
  private String f = "\r\n";
  private String d = "%errorlevel%";
  private int o = 2000;
  List<Runnable> i = new ArrayList();
  int g = 0;
  private static Log e = LogFactory.getLog(Shell.class);
  private static boolean l = Boolean.getBoolean("maverick.shell.verbose");
  private _b p;
  private ShellController h;
  private boolean c = false;
  public static final int EXIT_CODE_PROCESS_ACTIVE = -2147483648;
  public static final int EXIT_CODE_UNKNOWN = -2147483647;
  long s;
  long k;

  public Shell(SshClient paramSshClient)
    throws SshException, SshIOException, ChannelOpenException, IOException, ShellTimeoutException
  {
    this(paramSshClient, null, 30000L, "vt100", 1024, 80);
  }

  public Shell(SshClient paramSshClient, ShellStartupTrigger paramShellStartupTrigger)
    throws SshException, SshIOException, ChannelOpenException, IOException, ShellTimeoutException
  {
    this(paramSshClient, paramShellStartupTrigger, 30000L, "vt100", 1024, 80);
  }

  public Shell(SshClient paramSshClient, ShellStartupTrigger paramShellStartupTrigger, long paramLong)
    throws SshException, SshIOException, ChannelOpenException, IOException, ShellTimeoutException
  {
    this(paramSshClient, paramShellStartupTrigger, paramLong, "vt100", 1024, 80);
  }

  public Shell(SshClient paramSshClient, ShellStartupTrigger paramShellStartupTrigger, long paramLong, String paramString)
    throws SshException, SshIOException, ChannelOpenException, IOException, ShellTimeoutException
  {
    this(paramSshClient, paramShellStartupTrigger, paramLong, paramString, 1024, 80);
  }

  public Shell(SshClient paramSshClient, ShellStartupTrigger paramShellStartupTrigger, long paramLong, String paramString, int paramInt1, int paramInt2)
    throws SshException, SshIOException, ChannelOpenException, IOException, ShellTimeoutException
  {
    this.s = paramLong;
    this.k = System.currentTimeMillis();
    if (e.isDebugEnabled())
      e.debug("Creating session for interactive shell");
    SshSession localSshSession = paramSshClient.openSessionChannel(5000L);
    PseudoTerminalModes localPseudoTerminalModes = new PseudoTerminalModes(paramSshClient);
    if ((paramString != null) && (!localSshSession.requestPseudoTerminal(paramString, paramInt1, paramInt2, 0, 0, localPseudoTerminalModes)) && (e.isWarnEnabled()))
      e.warn("Failed to allocate pseudo terminal; Shell may not function as intended");
    if (!localSshSession.startShell())
      throw new SshException("Server failed to open session channel", 6);
    this.i.add(new Runnable(localSshSession)
    {
      public void run()
      {
        this.c.close();
      }
    });
    try
    {
      Thread.sleep(this.o);
    }
    catch (InterruptedException localInterruptedException)
    {
    }
    b(localSshSession.getInputStream(), localSshSession.getOutputStream(), true, paramShellStartupTrigger);
  }

  Shell(InputStream paramInputStream, OutputStream paramOutputStream, String paramString1, String paramString2, String paramString3, int paramInt, String paramString4)
    throws SshIOException, SshException, IOException, ShellTimeoutException
  {
    this.f = paramString1;
    this.r = paramString2;
    this.d = paramString3;
    this.t = paramInt;
    this.n = paramString4;
    this.c = true;
    b(paramInputStream, paramOutputStream, true, null);
  }

  public InputStream getStartupInputStream()
  {
    return this.p;
  }

  void b(InputStream paramInputStream, OutputStream paramOutputStream, boolean paramBoolean, ShellStartupTrigger paramShellStartupTrigger)
    throws SshIOException, SshException, IOException, ShellTimeoutException
  {
    this.j = new BufferedInputStream(paramInputStream);
    this.q = paramOutputStream;
    this.p = new _b("---BEGIN---", paramBoolean, paramShellStartupTrigger);
    if (e.isDebugEnabled())
      e.debug("Session creation complete");
  }

  public boolean inStartup()
  {
    return this.m;
  }

  public ShellReader getStartupReader()
  {
    return this.h;
  }

  public Shell su(String paramString1, String paramString2)
    throws SshIOException, SshException, IOException, ShellTimeoutException
  {
    return su(paramString1, paramString2, "assword", new ShellDefaultMatcher());
  }

  public Shell su(String paramString1, String paramString2, String paramString3)
    throws SshException, SshIOException, IOException, ShellTimeoutException
  {
    return su(paramString1, paramString2, paramString3, new ShellDefaultMatcher());
  }

  public Shell su(String paramString1, String paramString2, String paramString3, ShellMatcher paramShellMatcher)
    throws SshException, SshIOException, IOException, ShellTimeoutException
  {
    ShellProcess localShellProcess = executeCommand(paramString1, false);
    ShellProcessController localShellProcessController = new ShellProcessController(localShellProcess, paramShellMatcher);
    if (localShellProcessController.expectNextLine(paramString3))
    {
      if (e.isDebugEnabled())
        e.debug("su password expression matched");
      localShellProcessController.typeAndReturn(paramString2);
    }
    else if (e.isDebugEnabled())
    {
      e.debug("su password expression not matched");
    }
    return new Shell(localShellProcess.getInputStream(), localShellProcess.getOutputStream(), this.f, this.r, this.d, this.t, this.n);
  }

  public ShellProcess sudo(String paramString1, String paramString2)
    throws SshException, ShellTimeoutException, IOException
  {
    return sudo(paramString1, paramString2, "assword", new ShellDefaultMatcher());
  }

  public ShellProcess sudo(String paramString1, String paramString2, String paramString3)
    throws SshException, ShellTimeoutException, IOException
  {
    return sudo(paramString1, paramString2, paramString3, new ShellDefaultMatcher());
  }

  public ShellProcess sudo(String paramString1, String paramString2, String paramString3, ShellMatcher paramShellMatcher)
    throws SshException, ShellTimeoutException, IOException
  {
    ShellProcess localShellProcess = executeCommand(paramString1, false);
    ShellProcessController localShellProcessController = new ShellProcessController(localShellProcess, paramShellMatcher);
    if (localShellProcessController.expectNextLine(paramString3))
    {
      if (e.isDebugEnabled())
        e.debug("sudo password expression matched");
      localShellProcessController.typeAndReturn(paramString2);
    }
    else if (e.isDebugEnabled())
    {
      e.debug("sudo password expression not matched");
    }
    return localShellProcess;
  }

  public boolean isClosed()
  {
    return this.b == 3;
  }

  private void b()
  {
    if (this.t == 3)
      this.n = "Solaris";
    else if (this.t == 4)
      this.n = "AIX";
    else if (this.t == 1)
      this.n = "Windows";
    else if (this.t == 5)
      this.n = "Darwin";
    else if (this.t == 6)
      this.n = "FreeBSD";
    else if (this.t == 7)
      this.n = "OpenBSD";
    else if (this.t == 8)
      this.n = "NetBSD";
    else if (this.t == 2)
      this.n = "Linux";
    else if (this.t == 9)
      this.n = "HP-UX";
    else
      this.n = "Unknown";
  }

  public void exit()
    throws IOException, SshException
  {
    this.q.write(("exit" + this.f).getBytes());
    while ((this.c) && (this.j.read() > -1));
    close();
  }

  public void close()
    throws IOException, SshException
  {
    c();
  }

  public String getNewline()
  {
    if (this.t == 1)
      return "\r\n";
    return "\n";
  }

  public synchronized ShellProcess executeCommand(String paramString)
    throws SshException
  {
    return executeCommand(paramString, false);
  }

  public synchronized ShellProcess executeCommand(String paramString, boolean paramBoolean)
    throws SshException
  {
    try
    {
      String str1 = paramString;
      if (this.b == 2)
        throw new SshException("Command still active", 4);
      if (this.b == 3)
        throw new SshException("Shell is closed!", 4);
      d();
      this.b = 2;
      StringBuffer localStringBuffer = new StringBuffer();
      paramBoolean |= (((paramString.startsWith(".")) || (paramString.startsWith("source"))) && (this.t == 9));
      if (paramBoolean)
      {
        this.q.write(this.f.getBytes());
        this.q.write(this.f.getBytes());
        int i1;
        while (((i1 = this.j.read()) > -1) && (i1 != 10));
        while (((i1 = this.j.read()) > -1) && (i1 != 10))
          localStringBuffer.append((char)i1);
        if (e.isDebugEnabled())
          e.debug("Prompt is " + localStringBuffer.toString().trim());
      }
      if (e.isDebugEnabled())
        e.debug("Executing command: " + str1);
      String str3 = f();
      String str2;
      if (this.t == 1)
        str2 = "echo ---BEGIN--- && " + str1 + " && " + this.r + " " + str3 + "0" + " || " + this.r + " " + str3 + "1" + "" + this.f;
      else
        str2 = "echo \"---BEGIN---\"; " + str1 + "; " + this.r + " \"" + str3 + this.d + "\"" + this.f;
      this.q.write(str2.getBytes());
      this.g += 1;
      return new ShellProcess(this, new b(this, "---BEGIN---", str3, paramString, paramBoolean, localStringBuffer.toString().trim()));
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
    	throw new SshException("Failed to execute command: " + localIOException.getMessage(), 6);
    }
  }

  public int getNumCommandsExecuted()
  {
    return this.g;
  }

  private void d()
    throws IOException
  {
    if (e.isDebugEnabled())
      e.debug("Checking state of startup controller");
    if (!this.p.b())
    {
      if (e.isDebugEnabled())
        e.debug("Shell still in startup mode, draining startup output");
      while (this.p.read() > -1);
    }
    if (e.isDebugEnabled())
      e.debug("Shell is ready for command");
  }

  private synchronized String f()
  {
    return "---END---;PROCESS=" + System.currentTimeMillis() + ";" + "EXITCODE=";
  }

  public int getOsType()
  {
    return this.t;
  }

  public String getOsDescription()
  {
    return this.n;
  }

  void b(String paramString)
    throws IOException
  {
    b(paramString.getBytes());
  }

  void b(byte[] paramArrayOfByte)
    throws IOException
  {
    this.q.write(paramArrayOfByte);
  }

  void g()
    throws IOException
  {
    b(this.f.getBytes());
  }

  void c(String paramString)
    throws IOException
  {
    b((paramString + this.f).getBytes());
  }

  void c()
  {
    this.b = 3;
    Iterator localIterator = this.i.iterator();
    while (localIterator.hasNext())
    {
      Runnable localRunnable = (Runnable)localIterator.next();
      try
      {
        localRunnable.run();
      }
      catch (Throwable localThrowable)
      {
      }
    }
  }

  class _b extends InputStream
  {
    char[] e;
    int c;
    StringBuffer f = new StringBuffer();
    boolean d;

    _b(String paramBoolean, boolean paramShellStartupTrigger, ShellStartupTrigger arg4)
      throws SshException, IOException, ShellTimeoutException
    {
      this.d = paramShellStartupTrigger;
      this.e = paramBoolean.toCharArray();
      Shell.b(Shell.this, new ShellController(Shell.this, new ShellDefaultMatcher(), this));
      Object localObject1;
      Object localObject2;
      if (localObject1 != null)
      {
        localObject2 = new StringBuffer();
        do
        {
          int i = b(Shell.this.j);
          if ((i != 10) && (i != 13) && (i != -1))
            ((StringBuffer)localObject2).append((char)i);
          if (i == 10)
            ((StringBuffer)localObject2).setLength(0);
          if (i != -1)
            continue;
          throw new SshException("Shell output ended before trigger could start shell", 20);
        }
        while (!localObject1.canStartShell(((StringBuffer)localObject2).toString(), Shell.checkVersion(Shell.this)));
      }
      if (paramShellStartupTrigger != 0)
      {
        localObject2 = "echo \"" + paramBoolean + "\"; echo $?\r\n";
        if (Shell.h().isDebugEnabled())
          Shell.h().debug("Performing marker test: " + (String)localObject2);
        Shell.this.q.write(((String)localObject2).getBytes());
      }
      Shell.this.m = paramShellStartupTrigger;
    }

    boolean b()
    {
      return !Shell.this.m;
    }

    int b(InputStream paramInputStream)
      throws IOException
    {
      while (true)
        try
        {
          return paramInputStream.read();
        }
        catch (SshIOException localSshIOException)
        {
          if (localSshIOException.getRealException().getReason() != 21)
            continue;
          if (System.currentTimeMillis() - Shell.this.k > Shell.this.s)
          {
            throw new SshIOException(new SshException("", 20));
            throw localSshIOException;
          }
        }
    }

    String c(InputStream paramInputStream)
      throws IOException
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int i;
      do
      {
        i = b(paramInputStream);
        if (i <= -1)
          continue;
        localStringBuffer.append((char)i);
      }
      while ((i != -1) && (i != 10));
      return localStringBuffer.toString().trim();
    }

    public int read()
      throws IOException
    {
      if (Shell.this.m)
      {
        Shell.this.j.mark(this.e.length + 1);
        StringBuffer localStringBuffer = new StringBuffer();
        while (true)
          try
          {
            i = b(Shell.this.j);
            localStringBuffer.append((char)i);
            if (this.c >= this.e.length - 1)
              continue;
            if (this.e[(this.c++)] == i)
              continue;
          }
          catch (SshIOException localSshIOException)
          {
            if (localSshIOException.getRealException().getReason() != 21)
              continue;
            if (System.currentTimeMillis() - Shell.this.k > Shell.this.s)
            {
              throw new SshIOException(new SshException("", 20));
              throw localSshIOException;
            }
          }
        if (this.c == this.e.length - 1)
        {
          if (Shell.h().isDebugEnabled())
            Shell.h().debug("Potentially found test marker [" + this.f.toString() + localStringBuffer.toString() + "]");
          i = b(Shell.this.j);
          if (i == 13)
          {
            if (Shell.h().isDebugEnabled())
              Shell.h().debug("Looking good, found CR");
            i = b(Shell.this.j);
          }
          if (i == 10)
          {
            if (Shell.h().isDebugEnabled())
              Shell.h().debug("Found test marker");
            try
            {
              c();
            }
            catch (SshException localSshException)
            {
              throw new SshIOException(localSshException);
            }
            return -1;
          }
          if (Shell.h().isDebugEnabled())
            Shell.h().debug("Detected echo of test marker command since we did not find LF at end of marker ch=" + Integer.valueOf(i) + " currentLine=" + this.f.toString() + localStringBuffer.toString());
        }
        Shell.this.j.reset();
        int i = b(Shell.this.j);
        this.c = 0;
        this.f.append((char)i);
        if (i == 10)
        {
          if (Shell.h().isDebugEnabled())
            Shell.h().debug("Shell startup (read): " + this.f.toString());
          this.f = new StringBuffer();
        }
        if ((Shell.e()) && (Shell.h().isDebugEnabled()))
          Shell.h().debug("Shell startup (read): " + this.f.toString());
        Shell.this.j.mark(-1);
        return i;
      }
      return -1;
    }

    void c()
      throws IOException, SshException
    {
      Shell.this.m = false;
      if (!this.d)
        return;
      if (Shell.h().isDebugEnabled())
        Shell.h().debug("Detecting shell settings");
      String str1 = c(Shell.this.j);
      if (Shell.h().isDebugEnabled())
        Shell.h().debug("Shell startup (detect): " + str1);
      Object localObject;
      if (str1.equals("0"))
      {
        if (Shell.h().isDebugEnabled())
          Shell.h().debug("This looks like a *nix type machine, setting EOL to CR only and exit code variable to $?");
        Shell.b(Shell.this, "\r");
        Shell.c(Shell.this, "$?");
        localObject = Shell.this.executeCommand("uname");
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(((ShellProcess)localObject).getInputStream()));
        String str2;
        for (str1 = ""; (str2 = localBufferedReader.readLine()) != null; str1 = str1 + str2);
        switch (((ShellProcess)localObject).getExitCode())
        {
        case 0:
          if (Shell.h().isDebugEnabled())
            Shell.h().debug("Remote side reported it is " + str1.trim());
          str1 = str1.toLowerCase();
          if (str1.startsWith("Sun"))
            Shell.b(Shell.this, 3);
          else if (str1.startsWith("aix"))
            Shell.b(Shell.this, 4);
          else if (str1.startsWith("darwin"))
            Shell.b(Shell.this, 5);
          else if (str1.startsWith("freebsd"))
            Shell.b(Shell.this, 6);
          else if (str1.startsWith("openbsd"))
            Shell.b(Shell.this, 7);
          else if (str1.startsWith("netbsd"))
            Shell.b(Shell.this, 8);
          else if (str1.startsWith("linux"))
            Shell.b(Shell.this, 2);
          else if (str1.startsWith("hp-ux"))
            Shell.b(Shell.this, 9);
          else
            Shell.b(Shell.this, 99);
          break;
        case 127:
          Shell.h().debug("Remote side does not support uname");
          break;
        default:
          Shell.h().debug("uname returned error code " + ((ShellProcess)localObject).getExitCode());
        }
      }
      else
      {
        localObject = "echo ---BEGIN--- && echo %errorlevel%\r\n";
        Shell.this.q.write(((String)localObject).getBytes());
        while (((str1 = c(Shell.this.j)) != null) && (!str1.endsWith("---BEGIN---")))
        {
          if ((str1.trim().equals("")) || (!Shell.h().isDebugEnabled()))
            continue;
          Shell.h().debug("Shell startup: " + str1);
        }
        str1 = c(Shell.this.j);
        if (str1.equals("0"))
        {
          if (Shell.h().isDebugEnabled())
            Shell.h().debug("This looks like a Windows machine, setting EOL to CRLF and exit code variable to %errorlevel%");
          Shell.b(Shell.this, "\r\n");
          Shell.c(Shell.this, "%errorlevel%");
          Shell.b(Shell.this, 1);
        }
      }
      Shell.b(Shell.this);
      if (Shell.h().isDebugEnabled())
        Shell.h().debug("Shell initialized");
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.Shell
 * JD-Core Version:    0.6.0
 */