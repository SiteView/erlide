package com.maverick.ssh2;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.events.EventServiceImplementation;
import com.maverick.ssh.PseudoTerminalModes;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.message.SshChannelMessage;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.io.InputStream;

public class Ssh2Session extends Ssh2Channel
  implements SshSession
{
  Ssh2Channel._d gc;
  boolean fc = false;
  int hc = -2147483648;
  String ec = "";
  Ssh2Client dc;

  public Ssh2Session(int paramInt1, int paramInt2, Ssh2Client paramSsh2Client, long paramLong)
  {
    super("session", paramInt1, paramInt2, paramLong);
    this.dc = paramSsh2Client;
    this.gc = createExtendedDataStream();
  }

  public SshClient getClient()
  {
    return this.dc;
  }

  protected void processExtendedData(int paramInt1, int paramInt2, SshChannelMessage paramSshChannelMessage)
    throws SshException
  {
    super.processExtendedData(paramInt1, paramInt2, paramSshChannelMessage);
    if (paramInt1 == 1)
      this.gc.b(paramInt2, paramSshChannelMessage);
  }

  public InputStream getStderrInputStream()
  {
    return this.gc;
  }

  public boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws SshException
  {
    return requestPseudoTerminal(paramString, paramInt1, paramInt2, paramInt3, paramInt4, new byte[] { 0 });
  }

  public boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, PseudoTerminalModes paramPseudoTerminalModes)
    throws SshException
  {
    return requestPseudoTerminal(paramString, paramInt1, paramInt2, paramInt3, paramInt4, paramPseudoTerminalModes.toByteArray());
  }

  public boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString);
      localByteArrayWriter.writeInt(paramInt1);
      localByteArrayWriter.writeInt(paramInt2);
      localByteArrayWriter.writeInt(paramInt3);
      localByteArrayWriter.writeInt(paramInt4);
      localByteArrayWriter.writeBinaryString(paramArrayOfByte);
      return sendRequest("pty-req", true, localByteArrayWriter.toByteArray());
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public boolean startShell()
    throws SshException
  {
    boolean bool = sendRequest("shell", true, null);
    if (bool)
      EventServiceImplementation.getInstance().fireEvent(new Event(this, 23, true));
    else
      EventServiceImplementation.getInstance().fireEvent(new Event(this, 24, false));
    this.r = true;
    return bool;
  }

  public boolean executeCommand(String paramString)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString);
      boolean bool = sendRequest("exec", true, localByteArrayWriter.toByteArray());
      if (bool)
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 30, true).addAttribute("COMMAND", paramString));
      else
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 30, false).addAttribute("COMMAND", paramString));
      this.r = true;
      return bool;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public boolean executeCommand(String paramString1, String paramString2)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString1, paramString2);
      boolean bool = sendRequest("exec", true, localByteArrayWriter.toByteArray());
      if (bool)
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 30, true).addAttribute("COMMAND", paramString1));
      else
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 30, false).addAttribute("COMMAND", paramString1));
      this.r = true;
      return bool;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public boolean startSubsystem(String paramString)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString);
      boolean bool = sendRequest("subsystem", true, localByteArrayWriter.toByteArray());
      if (bool)
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 1001, true).addAttribute("COMMAND", paramString));
      else
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 1001, false).addAttribute("COMMAND", paramString));
      this.r = true;
      return bool;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  boolean b(boolean paramBoolean, String paramString1, String paramString2, int paramInt)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeBoolean(paramBoolean);
      localByteArrayWriter.writeString(paramString1);
      localByteArrayWriter.writeString(paramString2);
      localByteArrayWriter.writeInt(paramInt);
      return sendRequest("x11-req", true, localByteArrayWriter.toByteArray());
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public boolean setEnvironmentVariable(String paramString1, String paramString2)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString1);
      localByteArrayWriter.writeString(paramString2);
      return sendRequest("env", true, localByteArrayWriter.toByteArray());
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public void changeTerminalDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeInt(paramInt1);
      localByteArrayWriter.writeInt(paramInt2);
      localByteArrayWriter.writeInt(paramInt4);
      localByteArrayWriter.writeInt(paramInt3);
      sendRequest("window-change", false, localByteArrayWriter.toByteArray());
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public boolean isFlowControlEnabled()
  {
    return this.fc;
  }

  public void signal(String paramString)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString);
      sendRequest("signal", false, localByteArrayWriter.toByteArray());
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  protected void channelRequest(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      if ((paramString.equals("exit-status")) && (paramArrayOfByte != null))
        this.hc = (int)ByteArrayReader.readInt(paramArrayOfByte, 0);
      if ((paramString.equals("exit-signal")) && (paramArrayOfByte != null))
      {
        ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte, 0, paramArrayOfByte.length);
        this.ec = ("Signal=" + localByteArrayReader.readString() + " CoreDump=" + String.valueOf(localByteArrayReader.read() != 0) + " Message=" + localByteArrayReader.readString());
      }
      if (paramString.equals("xon-xoff"))
        this.fc = ((paramArrayOfByte != null) && (paramArrayOfByte[0] != 0));
      super.channelRequest(paramString, paramBoolean, paramArrayOfByte);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public int exitCode()
  {
    return this.hc;
  }

  public boolean hasExitSignal()
  {
    return !this.ec.equals("");
  }

  public String getExitSignalInfo()
  {
    return this.ec;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.Ssh2Session
 * JD-Core Version:    0.6.0
 */