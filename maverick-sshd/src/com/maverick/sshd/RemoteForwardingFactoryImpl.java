package com.maverick.sshd;

import com.maverick.nio.ClientAcceptor;
import com.maverick.nio.Daemon;
import com.maverick.nio.ProtocolContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class RemoteForwardingFactoryImpl extends ClientAcceptor
  implements RemoteForwardingFactory
{
  String F;
  int I;
  ServerSocketChannel G;
  ConnectionProtocol E;
  InetSocketAddress H;
  B D = new B();

  public RemoteForwardingFactoryImpl()
  {
    super(null);
  }

  public boolean belongsTo(ConnectionProtocol paramConnectionProtocol)
  {
    return (this.E != null) && (this.E.equals(paramConnectionProtocol));
  }

  public void bindInterface(String paramString, int paramInt, ConnectionProtocol paramConnectionProtocol)
    throws IOException
  {
    this.F = paramString;
    this.I = paramInt;
    this.E = paramConnectionProtocol;
    this.H = new InetSocketAddress(paramString, paramInt);
    this.G = ServerSocketChannel.open();
    this.G.configureBlocking(false);
    this.G.socket().setReuseAddress(true);
    this.G.socket().setReceiveBufferSize(paramConnectionProtocol.getContext().getReceiveBufferSize());
    ServerSocket localServerSocket = this.G.socket();
    localServerSocket.bind(this.H, paramConnectionProtocol.getContext().getMaximumSocketsBacklogPerRemotelyForwardedConnection());
    paramConnectionProtocol.getContext().getServer().registerAcceptor(this, this.G);
  }

  public boolean finishAccept(SelectionKey paramSelectionKey, ProtocolContext paramProtocolContext)
  {
    try
    {
      SocketChannel localSocketChannel = this.G.accept();
      if (localSocketChannel != null)
      {
        localSocketChannel.configureBlocking(false);
        localSocketChannel.socket().setReceiveBufferSize(this.E.getContext().getReceiveBufferSize());
        localSocketChannel.socket().setSendBufferSize(this.E.getContext().getSendBufferSize());
        localSocketChannel.socket().setKeepAlive(this.E.getContext().getSocketOptionKeepAlive());
        localSocketChannel.socket().setTcpNoDelay(this.E.getContext().getSocketOptionTcpNoDelay());
        RemoteForwardingChannel localRemoteForwardingChannel = new RemoteForwardingChannel(this.F, this.I, localSocketChannel);
        localRemoteForwardingChannel.addEventListener(this.D);
        if (!this.E.openChannel(localRemoteForwardingChannel))
          try
          {
            localSocketChannel.close();
          }
          catch (IOException localIOException2)
          {
          }
      }
    }
    catch (IOException localIOException1)
    {
    }
    return !this.G.isOpen();
  }

  public void stopListening(boolean paramBoolean)
  {
    try
    {
      this.G.close();
    }
    catch (IOException localIOException)
    {
    }
    if (paramBoolean)
      this.D.A();
  }

  public void stopAccepting()
  {
    try
    {
      this.G.close();
    }
    catch (Throwable localThrowable)
    {
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.RemoteForwardingFactoryImpl
 * JD-Core Version:    0.6.0
 */