package com.maverick.sshd.platform;

import com.maverick.sshd.Channel;
import com.maverick.sshd.ChannelEventAdapter;
import com.maverick.sshd.SessionChannel;
import com.maverick.util.DynamicBuffer;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public abstract class ExecutableCommand
{
  public static final int STILL_ACTIVE = -2147483648;
  DynamicBuffer B = new DynamicBuffer();
  protected SessionChannel session;
  protected OutputStream stdout = new _A();
  protected OutputStream stderr = new _B();
  protected InputStream stdin = new _C(this.B.getInputStream());
  private boolean A = false;
  private int C;

  public ExecutableCommand()
  {
    this(65535);
  }

  public ExecutableCommand(int paramInt)
  {
    this.C = paramInt;
  }

  public void init(SessionChannel paramSessionChannel)
  {
    this.session = paramSessionChannel;
    paramSessionChannel.haltIncomingData();
    paramSessionChannel.addEventListener(new ChannelEventAdapter()
    {
      boolean C = false;

      public void onChannelClose(Channel paramChannel)
      {
        try
        {
          if (ExecutableCommand.this.stdin.available() == 0)
            ExecutableCommand.this.stdin.close();
        }
        catch (IOException localIOException)
        {
        }
      }

      public void onChannelEOF(Channel paramChannel)
      {
        try
        {
          ExecutableCommand.this.stdin.close();
        }
        catch (IOException localIOException)
        {
        }
      }
    });
  }

  public abstract boolean createProcess(String paramString, Map paramMap);

  public void start()
  {
    this.A = true;
    onStart();
  }

  public abstract void onStart();

  public abstract void kill();

  public abstract int getExitCode();

  public OutputStream getOutputStream()
  {
    return this.stdout;
  }

  public OutputStream getStderrOutputStream()
  {
    return this.stderr;
  }

  public InputStream getInputStream()
  {
    return this.stdin;
  }

  public void processStdinData(byte[] paramArrayOfByte)
    throws IOException
  {
    if (this.B.getInputStream().available() + this.session.getLocalWindow() + paramArrayOfByte.length >= this.C)
      this.session.haltIncomingData();
    this.B.getOutputStream().write(paramArrayOfByte);
  }

  class _B extends OutputStream
  {
    _B()
    {
    }

    public void write(int paramInt)
      throws IOException
    {
      if (!ExecutableCommand.this.A)
        throw new IOException("Bad API usage: the process has not been started!");
      ExecutableCommand.this.session.sendStderrData(new byte[] { (byte)paramInt });
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (!ExecutableCommand.this.A)
        throw new IOException("Bad API usage: the process has not been started!");
      ExecutableCommand.this.session.sendStderrData(paramArrayOfByte, paramInt1, paramInt2);
    }
  }

  class _A extends OutputStream
  {
    _A()
    {
    }

    public void write(int paramInt)
      throws IOException
    {
      if (!ExecutableCommand.this.A)
        throw new IOException("Bad API usage: the process has not been started!");
      ExecutableCommand.this.session.sendChannelDataWithBuffering(new byte[] { (byte)paramInt });
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (!ExecutableCommand.this.A)
        throw new IOException("Bad API usage: the process has not been started!");
      ExecutableCommand.this.session.sendChannelDataWithBuffering(paramArrayOfByte, paramInt1, paramInt2);
    }
  }

  class _C extends FilterInputStream
  {
    _C(InputStream arg2)
    {
      super();
    }

    public int read()
      throws IOException
    {
      int i = this.in.read();
      A();
      return i;
    }

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
      A();
      return i;
    }

    private void A()
      throws IOException
    {
      if (this.in.available() < 16384)
        ExecutableCommand.this.session.resumeIncomingData();
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.platform.ExecutableCommand
 * JD-Core Version:    0.6.0
 */