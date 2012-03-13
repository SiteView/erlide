package com.maverick.sshd;

import com.maverick.nio.Daemon;
import com.maverick.nio.EventLog;
import com.maverick.nio.SocketConnection;
import com.maverick.nio.WriteOperationRequest;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class RemoteForwardingChannel extends SocketForwardingChannel
{
  public RemoteForwardingChannel(String paramString, int paramInt, SocketChannel paramSocketChannel)
  {
    super("forwarded-tcpip");
    this.á = paramSocketChannel;
    this.hostToConnect = paramString;
    this.portToConnect = paramInt;
  }

  protected byte[] createChannel()
    throws IOException
  {
    boolean bool = true;
    if (getContext().getAccessManager() != null)
      bool = getContext().getAccessManager().canForward(getSessionIdentifier(), this.connection.getUsername(), this, false);
    if (!bool)
    {
      try
      {
        this.á.close();
      }
      catch (Throwable localThrowable)
      {
        EventLog.LogEvent(this, "Failed to close socket channel");
      }
      throw new IOException("Cannot create channel because access has been denied by the Access Manager");
    }
    ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
    localByteArrayWriter.writeString(this.hostToConnect);
    localByteArrayWriter.writeInt(this.portToConnect);
    localByteArrayWriter.writeString(this.originatingHost = ((InetSocketAddress)this.á.socket().getRemoteSocketAddress()).getAddress().getHostAddress());
    localByteArrayWriter.writeInt(this.originatingPort = ((InetSocketAddress)this.á.socket().getRemoteSocketAddress()).getPort());
    return localByteArrayWriter.toByteArray();
  }

  protected void onRemoteEOF()
  {
    EventLog.LogEvent(this, "Remote EOF");
    this.Û = true;
    close();
  }

  protected void onRegistrationComplete()
  {
  }

  protected void onChannelOpenConfirmation()
  {
    try
    {
      getContext().getServer().registerHandler(this, this.á, getConnection().getTransport().getSocketConnection().getThread());
    }
    catch (IOException localIOException)
    {
      EventLog.LogEvent(this, "Failed to register channel with a selector", localIOException);
    }
  }

  protected byte[] openChannel(byte[] paramArrayOfByte)
    throws WriteOperationRequest, ChannelOpenException
  {
    EventLog.LogEvent(this, "Open channel");
    return null;
  }

  protected void onChannelOpenFailure()
  {
    try
    {
      this.á.close();
    }
    catch (IOException localIOException)
    {
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.RemoteForwardingChannel
 * JD-Core Version:    0.6.0
 */