package socks;

import com.maverick.ssh.SshChannel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketWrappedChannel extends Socket
{
  SshChannel b;

  SocketWrappedChannel(SshChannel paramSshChannel)
  {
    this.b = paramSshChannel;
  }

  public InputStream getInputStream()
    throws IOException
  {
    return this.b.getInputStream();
  }

  public OutputStream getOutputStream()
    throws IOException
  {
    return this.b.getOutputStream();
  }

  public void close()
    throws IOException
  {
    this.b.close();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.SocketWrappedChannel
 * JD-Core Version:    0.6.0
 */