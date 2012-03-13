package com.sshtools.net;

import com.maverick.ssh.SocketTimeoutSupport;
import com.maverick.ssh.SshTransport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketWrapper
  implements SshTransport, SocketTimeoutSupport
{
  protected Socket socket;

  public SocketWrapper(Socket paramSocket)
  {
    this.socket = paramSocket;
  }

  public InputStream getInputStream()
    throws IOException
  {
    return this.socket.getInputStream();
  }

  public OutputStream getOutputStream()
    throws IOException
  {
    return this.socket.getOutputStream();
  }

  public String getHost()
  {
    return this.socket.getInetAddress() == null ? "proxied" : this.socket.getInetAddress().getHostAddress();
  }

  public int getPort()
  {
    return this.socket.getPort();
  }

  public void close()
    throws IOException
  {
    this.socket.close();
  }

  public SshTransport duplicate()
    throws IOException
  {
    return new SocketWrapper(new Socket(getHost(), this.socket.getPort()));
  }

  public void setSoTimeout(int paramInt)
    throws IOException
  {
    this.socket.setSoTimeout(paramInt);
  }

  public int getSoTimeout()
    throws IOException
  {
    return this.socket.getSoTimeout();
  }

  public Socket getSocket()
  {
    return this.socket;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.SocketWrapper
 * JD-Core Version:    0.6.0
 */