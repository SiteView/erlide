package com.maverick.scp;

import com.maverick.sftp.FileTransferProgress;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshSession;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ScpClientIO
{
  protected SshClient ssh;
  boolean l = true;

  public ScpClientIO(SshClient paramSshClient)
  {
    this.ssh = paramSshClient;
  }

  public void put(InputStream paramInputStream, long paramLong, String paramString1, String paramString2)
    throws SshException, ChannelOpenException
  {
    put(paramInputStream, paramLong, paramString1, paramString2, null);
  }

  public void put(InputStream paramInputStream, long paramLong, String paramString1, String paramString2, FileTransferProgress paramFileTransferProgress)
    throws SshException, ChannelOpenException
  {
    ScpEngineIO localScpEngineIO = new ScpEngineIO("scp -t " + paramString2, this.ssh.openSessionChannel());
    try
    {
      localScpEngineIO.waitForResponse();
      if (paramFileTransferProgress != null)
        paramFileTransferProgress.started(paramLong, paramString2);
      localScpEngineIO.writeStreamToRemote(paramInputStream, paramLong, paramString1, paramFileTransferProgress);
      if (paramFileTransferProgress != null)
        paramFileTransferProgress.completed();
      localScpEngineIO.close();
    }
    catch (IOException localIOException)
    {
      localScpEngineIO.close();
      throw new SshException(localIOException, 6);
    }
  }

  public InputStream get(String paramString)
    throws SshException, ChannelOpenException
  {
    return get(paramString, null);
  }

  public InputStream get(String paramString, FileTransferProgress paramFileTransferProgress)
    throws SshException, ChannelOpenException
  {
    ScpEngineIO localScpEngineIO = new ScpEngineIO("scp -f " + paramString, this.ssh.openSessionChannel());
    try
    {
      return localScpEngineIO.readStreamFromRemote(paramString, paramFileTransferProgress);
    }
    catch (IOException localIOException)
    {
      localScpEngineIO.close();
    }
    throw new SshException(localIOException, 6);
  }

  static class _b extends InputStream
  {
    long f;
    InputStream b;
    long e;
    ScpClientIO.ScpEngineIO d;
    FileTransferProgress c;
    String g;

    _b(long paramLong, InputStream paramInputStream, ScpClientIO.ScpEngineIO paramScpEngineIO, FileTransferProgress paramFileTransferProgress, String paramString)
    {
      this.f = paramLong;
      this.b = paramInputStream;
      this.d = paramScpEngineIO;
      this.c = paramFileTransferProgress;
      this.g = paramString;
    }

    public int read()
      throws IOException
    {
      if (this.e == this.f)
        return -1;
      if (this.e >= this.f)
        throw new EOFException("End of file.");
      int i = this.b.read();
      if (i == -1)
        throw new EOFException("Unexpected EOF.");
      this.e += 1L;
      if (this.e == this.f)
      {
        this.d.waitForResponse();
        this.d.writeOk();
        if (this.c != null)
          this.c.completed();
      }
      if (this.c != null)
      {
        if (this.c.isCancelled())
          throw new SshIOException(new SshException("SCP transfer was cancelled by user", 18));
        this.c.progressed(this.e);
      }
      return i;
    }

    public int available()
      throws IOException
    {
      if (this.e == this.f)
        return -1;
      return (int)(this.f - this.e);
    }

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (this.e >= this.f)
        return -1;
      int i = this.b.read(paramArrayOfByte, paramInt1, (int)(this.f - this.e > paramInt2 ? paramInt2 : this.f - this.e));
      if (i == -1)
        throw new EOFException("Unexpected EOF.");
      this.e += i;
      if (this.e >= this.f)
      {
        this.d.waitForResponse();
        this.d.writeOk();
        if (this.c != null)
          this.c.completed();
      }
      if (this.c != null)
      {
        if (this.c.isCancelled())
          throw new SshIOException(new SshException("SCP transfer was cancelled by user", 18));
        this.c.progressed(this.e);
      }
      return i;
    }

    public void close()
      throws IOException
    {
      try
      {
        this.d.close();
      }
      catch (SshException localSshException)
      {
        throw new SshIOException(localSshException);
      }
    }
  }

  public class ScpEngineIO
  {
    protected byte[] buffer = new byte[16384];
    protected String cmd;
    protected SshSession session;
    protected OutputStream out;
    protected InputStream in;

    protected ScpEngineIO(String paramSshSession, SshSession arg3)
      throws SshException
    {
      try
      {
        Object localObject;
        this.session = localObject;
        this.cmd = paramSshSession;
        this.in = localObject.getInputStream();
        this.out = localObject.getOutputStream();
        if (!localObject.executeCommand(paramSshSession))
        {
          localObject.close();
          throw new SshException("Failed to execute the command " + paramSshSession, 6);
        }
      }
      catch (SshIOException localSshIOException)
      {
        throw localSshIOException.getRealException();
      }
    }

    public void close()
      throws SshException
    {
      try
      {
        this.session.getOutputStream().close();
      }
      catch (IOException localIOException)
      {
        throw new SshException(localIOException);
      }
      try
      {
        Thread.sleep(500L);
      }
      catch (Throwable localThrowable)
      {
      }
      this.session.close();
    }

    protected void writeStreamToRemote(InputStream paramInputStream, long paramLong, String paramString, FileTransferProgress paramFileTransferProgress)
      throws IOException
    {
      String str = "C0644 " + paramLong + " " + paramString + "\n";
      this.out.write(str.getBytes());
      waitForResponse();
      writeCompleteFile(paramInputStream, paramLong, paramFileTransferProgress);
      writeOk();
      waitForResponse();
    }

    protected InputStream readStreamFromRemote(String paramString, FileTransferProgress paramFileTransferProgress)
      throws IOException
    {
      String[] arrayOfString = new String[3];
      writeOk();
      String str;
      while (true)
      {
        try
        {
          str = readString();
        }
        catch (EOFException localEOFException)
        {
          return null;
        }
        catch (SshIOException localSshIOException)
        {
          return null;
        }
        int i = str.charAt(0);
        switch (i)
        {
        case 69:
          writeOk();
          return null;
        case 84:
        case 68:
        case 67:
        }
      }
      throw new IOException("Directories cannot be copied to a stream");
      parseCommand(str, arrayOfString);
      writeOk();
      long l = Long.parseLong(arrayOfString[1]);
      if (paramFileTransferProgress != null)
        paramFileTransferProgress.started(l, paramString);
      return new ScpClientIO._b(l, this.in, this, paramFileTransferProgress, paramString);
      writeError("Unexpected cmd: " + str);
      throw new IOException("SCP unexpected cmd: " + str);
    }

    protected void parseCommand(String paramString, String[] paramArrayOfString)
      throws IOException
    {
      int i = paramString.indexOf(' ');
      int j = paramString.indexOf(' ', i + 1);
      if ((i == -1) || (j == -1))
      {
        writeError("Syntax error in cmd");
        throw new IOException("Syntax error in cmd");
      }
      paramArrayOfString[0] = paramString.substring(1, i);
      paramArrayOfString[1] = paramString.substring(i + 1, j);
      paramArrayOfString[2] = paramString.substring(j + 1);
    }

    protected String readString()
      throws IOException
    {
      int j = 0;
      int i;
      while (((i = this.in.read()) != 10) && (i >= 0))
        this.buffer[(j++)] = (byte)i;
      if (i == -1)
        throw new EOFException("Unexpected EOF");
      if (this.buffer[0] == 10)
        throw new IOException("Unexpected <NL>");
      if ((this.buffer[0] == 2) || (this.buffer[0] == 1))
      {
        String str = new String(this.buffer, 1, j - 1);
        if (this.buffer[0] == 2)
          throw new IOException(str);
        throw new IOException("SCP returned an unexpected error: " + str);
      }
      return new String(this.buffer, 0, j);
    }

    public void waitForResponse()
      throws IOException
    {
      int i = this.in.read();
      if (ScpClientIO.this.l)
      {
        while ((i > 0) && (i != 2))
          i = this.in.read();
        ScpClientIO.this.l = false;
      }
      if (i == 0)
        return;
      if (i == -1)
        throw new EOFException("SCP returned unexpected EOF");
      String str = readString();
      if (i == 2)
        throw new IOException(str);
      throw new IOException("SCP returned an unexpected error: " + str);
    }

    protected void writeOk()
      throws IOException
    {
      this.out.write(0);
    }

    protected void writeError(String paramString)
      throws IOException
    {
      this.out.write(1);
      this.out.write(paramString.getBytes());
    }

    protected void writeCompleteFile(InputStream paramInputStream, long paramLong, FileTransferProgress paramFileTransferProgress)
      throws IOException
    {
      long l = 0L;
      try
      {
        while (l < paramLong)
        {
          int i = paramInputStream.read(this.buffer, 0, (int)this.buffer.length);
          if (i == -1)
            throw new EOFException("SCP received an unexpected EOF");
          l += i;
          this.out.write(this.buffer, 0, i);
          if (paramFileTransferProgress == null)
            continue;
          if (paramFileTransferProgress.isCancelled())
            throw new SshIOException(new SshException("SCP transfer was cancelled by user", 18));
          paramFileTransferProgress.progressed(l);
        }
      }
      finally
      {
        paramInputStream.close();
      }
    }

    protected void readCompleteFile(OutputStream paramOutputStream, long paramLong, FileTransferProgress paramFileTransferProgress)
      throws IOException
    {
      long l = 0L;
      try
      {
        while (l < paramLong)
        {
          int i = this.in.read(this.buffer, 0, (int)this.buffer.length);
          if (i == -1)
            throw new EOFException("SCP received an unexpected EOF");
          l += i;
          paramOutputStream.write(this.buffer, 0, i);
          if (paramFileTransferProgress == null)
            continue;
          if (paramFileTransferProgress.isCancelled())
            throw new SshIOException(new SshException("SCP transfer was cancelled by user", 18));
          paramFileTransferProgress.progressed(l);
        }
      }
      finally
      {
        paramOutputStream.close();
      }
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.scp.ScpClientIO
 * JD-Core Version:    0.6.0
 */