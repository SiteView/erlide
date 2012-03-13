package com.maverick.sshd;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.nio.EventLog;
import com.maverick.nio.IdleStateListener;
import com.maverick.nio.IdleStateManager;
import com.maverick.nio.SocketConnection;
import com.maverick.nio.WriteOperationRequest;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.sshd.events.EventServiceImplementation;
import com.maverick.sshd.events.SSHDEvent;
import com.maverick.sshd.platform.ExecutableCommand;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class SessionChannel extends Channel
  implements IdleStateListener
{
  protected int MAX_WINDOW_SPACE = Integer.parseInt(System.getProperty("ssh.maxWindowSpace", "131072"));
  protected int MIN_WINDOW_SPACE = Integer.parseInt(System.getProperty("ssh.minWindowSpace", String.valueOf(this.MAX_WINDOW_SPACE / 2)));
  Subsystem Í;
  ExecutableCommand Ï;
  Map Ë = Collections.synchronizedMap(new HashMap());
  boolean Ì = false;
  boolean Î = false;
  long Ð = System.currentTimeMillis();

  public SessionChannel()
  {
    super("session", 32768, System.getProperty("filezilla.bug.workaround", "false").equalsIgnoreCase("true") ? 35000 : 0);
  }

  protected final byte[] createChannel()
    throws IOException
  {
    registerExtendedData(1);
    return null;
  }

  protected abstract boolean allocatePseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte);

  protected abstract void changeWindowDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  protected abstract void processSignal(String paramString);

  protected abstract boolean setEnvironmentVariable(String paramString1, String paramString2);

  protected abstract boolean startShell();

  protected abstract boolean executeCommand(String paramString);

  protected abstract void processStdinData(byte[] paramArrayOfByte);

  protected abstract void onSessionOpen();

  protected void onChannelOpen()
  {
    if (getContext().getSessionTimeout() > 0)
      this.connection.N.getSocketConnection().getIdleStates().register(this);
  }

  public boolean idle()
  {
    if (getContext().getSessionTimeout() > 0)
    {
      long l = (System.currentTimeMillis() - this.Ð) / 1000L;
      if (getContext().getSessionTimeout() < l)
      {
        this.Ì = true;
        close();
        return true;
      }
    }
    else
    {
      return true;
    }
    return false;
  }

  protected void onChannelRequest(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    boolean bool1 = false;
    J();
    try
    {
      ByteArrayReader localByteArrayReader1;
      String str1;
      int m;
      int n;
      if (paramString.equals("pty-req"))
      {
        localByteArrayReader1 = new ByteArrayReader(paramArrayOfByte);
        str1 = localByteArrayReader1.readString();
        int j = (int)localByteArrayReader1.readInt();
        m = (int)localByteArrayReader1.readInt();
        n = (int)localByteArrayReader1.readInt();
        int i1 = (int)localByteArrayReader1.readInt();
        byte[] arrayOfByte = localByteArrayReader1.readBinaryString();
        bool1 = allocatePseudoTerminal(str1, j, m, n, i1, arrayOfByte);
      }
      else if (!paramString.equals("x11-req"))
      {
        if (paramString.equals("env"))
        {
          localByteArrayReader1 = new ByteArrayReader(paramArrayOfByte);
          str1 = localByteArrayReader1.readString();
          String str4 = localByteArrayReader1.readString();
          this.Ë.put(str1, str4);
          bool1 = setEnvironmentVariable(str1, str4);
        }
        else if (paramString.equals("shell"))
        {
          boolean bool2 = true;
          if (this.connection.getContext().getAccessManager() != null)
            bool2 = this.connection.getContext().getAccessManager().canStartShell(this.connection.getSessionIdentifier(), this.connection.getUsername());
          if (bool2)
          {
            if (this.connection.getContext().getShellCommand() != null)
              try
              {
                this.Ï = ((ExecutableCommand)this.connection.getContext().getShellCommand().newInstance());
                this.Ï.init(this);
                bool1 = this.Ï.createProcess("", this.Ë);
              }
              catch (Throwable localThrowable)
              {
                bool1 = false;
                EventLog.LogEvent(this, "Failed to create shell process", localThrowable);
              }
            else
              bool1 = startShell();
          }
          else
            bool1 = bool2;
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 23, bool1).addAttribute("SESSION_ID", getSessionIdentifier()));
        }
        else
        {
          ByteArrayReader localByteArrayReader2;
          String str2;
          if (paramString.equals("exec"))
          {
            localByteArrayReader2 = new ByteArrayReader(paramArrayOfByte);
            str2 = localByteArrayReader2.readString();
            if (this.connection.getContext().getAccessManager() != null)
            {
              bool1 = this.connection.getContext().getAccessManager().canExecuteCommand(this.connection.getSessionIdentifier(), this.connection.getUsername(), str2);
              if (bool1)
              {
                bool1 = A(str2);
                if (!bool1)
                  bool1 = executeCommand(str2);
              }
            }
            else
            {
              bool1 = A(str2);
              if (!bool1)
                bool1 = executeCommand(str2);
            }
            EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 203, bool1).addAttribute("SESSION_ID", getSessionIdentifier()).addAttribute("COMMAND", str2));
          }
          else if (paramString.equals("subsystem"))
          {
            localByteArrayReader2 = new ByteArrayReader(paramArrayOfByte);
            str2 = localByteArrayReader2.readString();
            if (this.connection.getContext().supportedSubsystems().contains(str2))
              try
              {
                if (this.connection.getContext().getAccessManager() != null)
                  bool1 = this.connection.getContext().getAccessManager().canStartSubsystem(this.connection.getSessionIdentifier(), this.connection.getUsername(), str2);
                else
                  bool1 = true;
                if (bool1)
                {
                  this.Í = ((Subsystem)this.connection.getContext().supportedSubsystems().getInstance(str2));
                  this.Í.init(this, this.connection.getContext());
                }
                sendWindowAdjust(this.MAX_WINDOW_SPACE - this.localwindow);
              }
              catch (SshException localSshException)
              {
                EventLog.LogEvent(this, "Failed to create instance of supported subsystem " + str2, localSshException);
                bool1 = false;
              }
          }
          else if (paramString.equals("window-change"))
          {
            localByteArrayReader2 = new ByteArrayReader(paramArrayOfByte);
            int i = (int)localByteArrayReader2.readInt();
            int k = (int)localByteArrayReader2.readInt();
            m = (int)localByteArrayReader2.readInt();
            n = (int)localByteArrayReader2.readInt();
            changeWindowDimensions(i, k, m, n);
          }
          else if (paramString.equals("signal"))
          {
            localByteArrayReader2 = new ByteArrayReader(paramArrayOfByte);
            String str3 = localByteArrayReader2.readString();
            processSignal(str3);
          }
        }
      }
    }
    catch (IOException localIOException)
    {
      EventLog.LogEvent(this, "An unexpected exception occurred", localIOException);
    }
    if ((bool1) && ((paramString.equals("exec")) || (paramString.equals("shell"))))
    {
      sendWindowAdjust(this.MAX_WINDOW_SPACE - this.localwindow);
      if ((!paramString.equals("subsystem")) && (this.Ï == null))
      {
        J();
        onSessionOpen();
      }
      if (paramBoolean)
        sendRequestResponse(bool1);
      if (this.Ï != null)
        this.Ï.start();
    }
    else if (paramBoolean)
    {
      sendRequestResponse(bool1);
    }
    if ((!bool1) && ((paramString.equals("exec")) || (paramString.equals("shell")) || (paramString.equals("subsystem"))))
      close();
  }

  boolean A(String paramString)
  {
    int i = paramString.indexOf(' ');
    String str;
    if (i > -1)
      str = paramString.substring(0, i);
    else
      str = paramString;
    if (this.connection.getContext().containsCommand(str))
      try
      {
        this.Ï = ((ExecutableCommand)this.connection.getContext().getCommand(str).newInstance());
        this.Ï.init(this);
        return this.Ï.createProcess(paramString, this.Ë);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        EventLog.LogEvent(this, "Failed to create an ExecutableCommand", localIllegalAccessException);
      }
      catch (InstantiationException localInstantiationException)
      {
        EventLog.LogEvent(this, "Failed to instantiate an ExecutableCommand", localInstantiationException);
      }
    return false;
  }

  protected void onChannelOpenConfirmation()
  {
  }

  protected void onRemoteEOF()
  {
    close();
  }

  protected void onChannelFree()
  {
    if (this.Í != null)
      this.Í.A();
    this.Í = null;
    this.Ï = null;
  }

  protected void onChannelClosing()
  {
    if ((getContext().getSessionTimeout() > 0) && (!this.Ì))
      this.connection.N.getSocketConnection().getIdleStates().remove(this);
    if (this.Ï != null)
    {
      if (this.Ï.getExitCode() == -2147483648)
        this.Ï.kill();
      if (this.Ï.getExitCode() != -2147483648)
        sendExitStatus(this.Ï.getExitCode());
    }
    if (this.Í != null)
      this.Í.A();
    this.Í = null;
  }

  private void J()
  {
    this.Ð = System.currentTimeMillis();
    if (getContext().getSessionTimeout() > 0)
      this.connection.N.getSocketConnection().getIdleStates().reset(this);
  }

  protected void onChannelData(byte[] paramArrayOfByte)
  {
    J();
    if (this.Í != null)
      try
      {
        this.Í.processMessage(paramArrayOfByte);
      }
      catch (IOException localIOException1)
      {
        EventLog.LogEvent(this, "The channel failed to process a subsystem message", localIOException1);
        close();
      }
    else if (this.Ï != null)
      try
      {
        this.Ï.processStdinData(paramArrayOfByte);
      }
      catch (IOException localIOException2)
      {
        EventLog.LogEvent(this, "The command failed to process channel data", localIOException2);
        close();
      }
    else
      processStdinData(paramArrayOfByte);
  }

  protected void onExtendedData(byte[] paramArrayOfByte, int paramInt)
  {
    J();
  }

  public void sendStdoutData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    J();
    sendChannelData(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void sendChannelDataWithBuffering(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    J();
    super.sendChannelDataWithBuffering(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void sendStdoutData(byte[] paramArrayOfByte)
  {
    J();
    sendChannelData(paramArrayOfByte);
  }

  public void sendStderrData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    J();
    sendExtendedData(paramArrayOfByte, paramInt1, paramInt2, 1);
  }

  public void sendStderrData(byte[] paramArrayOfByte)
  {
    sendStderrData(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  protected void sendExitStatus(int paramInt)
  {
    if (isOpen())
      sendChannelRequest("exit-status", false, ByteArrayWriter.encodeInt(paramInt));
  }

  protected final void evaluateWindowSpace(int paramInt)
  {
    synchronized (this.localWindowLock)
    {
      if ((this.localwindow < this.MIN_WINDOW_SPACE) && (isOpen()) && (!this.Î))
        sendWindowAdjust(this.MAX_WINDOW_SPACE - this.localwindow);
    }
  }

  public void haltIncomingData()
  {
    this.Î = true;
  }

  public void resumeIncomingData()
  {
    synchronized (this.localWindowLock)
    {
      this.Î = false;
      evaluateWindowSpace(this.localwindow);
    }
  }

  protected final byte[] openChannel(byte[] paramArrayOfByte)
    throws WriteOperationRequest, ChannelOpenException
  {
    try
    {
      registerExtendedData(1);
    }
    catch (IOException localIOException)
    {
      throw new ChannelOpenException("Could not allocate extended data channel!", 2);
    }
    return null;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.SessionChannel
 * JD-Core Version:    0.6.0
 */