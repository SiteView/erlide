package com.maverick.nio;

import java.io.IOException;

public abstract class ProtocolContext
{
  protected boolean keepAlive = false;
  protected boolean tcpNoDelay = false;
  protected boolean reuseAddress = true;
  protected int receiveBufferSize = 8192;
  protected int sendBufferSize = 8192;
  private Class A = SocketConnection.class;

  public SocketHandler createConnection(Daemon paramDaemon)
    throws IOException
  {
    SocketHandler localSocketHandler = createConnectionImpl();
    localSocketHandler.initialize(createEngine(), paramDaemon);
    return localSocketHandler;
  }

  protected abstract ProtocolEngine createEngine()
    throws IOException;

  public boolean getSocketOptionKeepAlive()
  {
    return this.keepAlive;
  }

  public boolean getSocketOptionReuseAddress()
  {
    return this.reuseAddress;
  }

  public void setSocketOptionReuseAddress(boolean paramBoolean)
  {
    this.reuseAddress = paramBoolean;
  }

  public void setSocketHandlerImpl(Class paramClass)
    throws IOException
  {
    if (!SocketHandler.class.isAssignableFrom(paramClass))
      throw new IOException("socketConnectionImpl must be a subclass of com.maverick.nio.SocketHandler!");
    this.A = paramClass;
  }

  protected SocketHandler createConnectionImpl()
    throws IOException
  {
    try
    {
      return (SocketHandler)this.A.newInstance();
    }
    catch (Throwable localThrowable)
    {
    }
    throw new IOException("Failed to create SocketConnection implementation class: " + localThrowable.getMessage());
  }

  public void setSocketOptionKeepAlive(boolean paramBoolean)
  {
    this.keepAlive = paramBoolean;
  }

  public boolean getSocketOptionTcpNoDelay()
  {
    return this.tcpNoDelay;
  }

  public void setSocketOptionTcpNoDelay(boolean paramBoolean)
  {
    this.tcpNoDelay = paramBoolean;
  }

  public void setReceiveBufferSize(int paramInt)
  {
    this.receiveBufferSize = paramInt;
  }

  public void setSendBufferSize(int paramInt)
  {
    this.sendBufferSize = paramInt;
  }

  public int getReceiveBufferSize()
  {
    return this.receiveBufferSize;
  }

  public int getSendBufferSize()
  {
    return this.sendBufferSize;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.ProtocolContext
 * JD-Core Version:    0.6.0
 */