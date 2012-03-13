package com.maverick.sshd;

import com.maverick.nio.ClientConnector;
import com.maverick.nio.Daemon;
import com.maverick.nio.SocketConnection;
import com.maverick.nio.WriteOperationRequest;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.util.ByteArrayReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class LocalForwardingChannel extends SocketForwardingChannel
  implements ClientConnector
{
  boolean â = false;

  public LocalForwardingChannel()
  {
    super("direct-tcpip");
  }

  protected byte[] createChannel()
  {
    return null;
  }

  protected byte[] openChannel(byte[] paramArrayOfByte)
    throws WriteOperationRequest, ChannelOpenException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
      this.hostToConnect = localByteArrayReader.readString();
      this.portToConnect = (int)localByteArrayReader.readInt();
      this.originatingHost = localByteArrayReader.readString();
      this.originatingPort = (int)localByteArrayReader.readInt();
      boolean bool = true;
      if (this.connection.getContext().getAccessManager() != null)
        bool = this.connection.getContext().getAccessManager().canForward(this.connection.getSessionIdentifier(), this.connection.getUsername(), this, true);
      if (!bool)
        throw new ChannelOpenException("User does not have permission", 1);
      this.á = SocketChannel.open();
      this.á.configureBlocking(false);
      this.á.socket().setTcpNoDelay(true);
      if (this.á.connect(new InetSocketAddress(this.hostToConnect, this.portToConnect)))
        return null;
      this.connection.getContext().getServer().registerConnector(this, this.á);
      throw new WriteOperationRequest();
    }
    catch (IOException localIOException)
    {
    }
    throw new ChannelOpenException("Failed to read channel request data" + localIOException.getMessage(), 2);
  }

  protected synchronized void onRegistrationComplete()
  {
  }

  public synchronized boolean finishConnect(SelectionKey paramSelectionKey)
  {
    if (this.á == null)
      return true;
    try
    {
      while (!this.á.finishConnect());
      this.connection.sendChannelOpenConfirmation(this, null);
    }
    catch (IOException localIOException)
    {
      this.connection.sendChannelOpenFailure(this, 2, "Connection failed.");
    }
    return true;
  }

  protected synchronized void onChannelOpenConfirmation()
  {
    try
    {
      this.connection.getContext().getServer().registerHandler(this, this.á, this.connection.getTransport().getSocketConnection().getThread());
    }
    catch (IOException localIOException)
    {
      close();
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.LocalForwardingChannel
 * JD-Core Version:    0.6.0
 */