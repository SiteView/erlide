package com.maverick.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

public class SocketConnection
  implements SocketHandler
{
  protected SocketChannel socketChannel;
  protected ProtocolEngine protocolEngine;
  protected DaemonContext daemonContext;
  protected SelectorThread selectorThread;
  protected SelectionKey key;
  protected Daemon daemon;
  protected ByteBuffer socketDataIn;
  protected ByteBuffer socketDataOut;
  protected boolean closed;
  boolean Ù = false;
  int Ô = 5;
  int Ó = 0;
  int Õ = 0;
  Object Ö = new Object();
  LinkedList Ò = new LinkedList();
  boolean Ø = false;

  public void initialize(ProtocolEngine paramProtocolEngine, Daemon paramDaemon)
  {
    this.protocolEngine = paramProtocolEngine;
    this.daemon = paramDaemon;
    this.daemonContext = paramDaemon.getContext();
  }

  public void registrationCompleted(SelectableChannel paramSelectableChannel, SelectionKey paramSelectionKey, SelectorThread paramSelectorThread)
  {
    this.socketChannel = ((SocketChannel)paramSelectableChannel);
    this.selectorThread = paramSelectorThread;
    this.key = paramSelectionKey;
    this.protocolEngine.onSocketConnect(this);
  }

  public void closeConnection()
  {
    if (!this.closed)
    {
      if ((this.socketChannel != null) && (this.socketChannel.isOpen()))
        try
        {
          this.socketChannel.close();
        }
        catch (IOException localIOException)
        {
        }
      this.protocolEngine.onSocketClose();
      this.closed = true;
    }
  }

  public ProtocolEngine getProtocolEngine()
  {
    return this.protocolEngine;
  }

  public DaemonContext getDaemonContext()
  {
    return this.daemonContext;
  }

  public InetAddress getLocalAddress()
  {
    return this.socketChannel.socket().getLocalAddress();
  }

  public int getLocalPort()
  {
    return this.socketChannel.socket().getLocalPort();
  }

  public int getPort()
  {
    return this.socketChannel.socket().getPort();
  }

  public SocketAddress getRemoteAddress()
  {
    return this.socketChannel.socket().getRemoteSocketAddress();
  }

  public SocketChannel getSocketChannel()
  {
    return this.socketChannel;
  }

  public IdleStateManager getIdleStates()
  {
    return this.selectorThread.C();
  }

  public boolean isSelectorThread()
  {
    return Thread.currentThread().equals(this.selectorThread);
  }

  public void setWriteState(boolean paramBoolean)
  {
    synchronized (this.Ö)
    {
      this.Ø |= paramBoolean;
      if (!this.Ù)
        this.selectorThread.addSelectorOperation(new Runnable()
        {
          public void run()
          {
            synchronized (SocketConnection.this.Ö)
            {
              if ((SocketConnection.this.key != null) && (SocketConnection.this.key.isValid()))
                SocketConnection.this.key.interestOps(SocketConnection.this.Ø ? 5 : 1);
              SocketConnection.this.Ù = false;
              SocketConnection.this.Ø = false;
            }
          }
        });
      this.Ù = true;
      this.selectorThread.wakeup();
    }
  }

  protected boolean isConnected()
  {
    return (this.socketChannel != null) && (this.socketChannel.isOpen()) && (this.protocolEngine.isConnected());
  }

  public SelectorThread getThread()
  {
    return this.selectorThread;
  }

  public int getInterestedOps()
  {
    return 5;
  }

  public boolean processReadEvent()
  {
    try
    {
      if (!isConnected())
      {
        i = 1;
        return i;
      }
      if (this.socketDataIn == null)
        this.socketDataIn = this.daemonContext.getBufferPool().get();
      int i = this.socketChannel.read(this.socketDataIn);
      this.socketDataIn.flip();
      if (i == -1)
      {
        closeConnection();
        j = 1;
        return j;
      }
      if (this.socketDataIn.hasRemaining())
        this.protocolEngine.onSocketRead(this.socketDataIn);
      setWriteState(((this.socketDataOut != null) && (this.socketDataOut.hasRemaining())) || (this.protocolEngine.wantsToWrite()));
      j = !isConnected() ? 1 : 0;
      return j;
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "Connection closed on socket read: " + localThrowable.getMessage());
      closeConnection();
      int j = 1;
      return j;
    }
    finally
    {
      if (this.socketDataIn != null)
        if (!this.socketDataIn.hasRemaining())
        {
          this.daemonContext.getBufferPool().add(this.socketDataIn);
          this.socketDataIn = null;
        }
        else
        {
          this.socketDataIn.compact();
        }
    }
    throw localObject;
  }

  public boolean processWriteEvent()
  {
    if ((this.socketChannel == null) || (!this.socketChannel.isOpen()))
      return true;
    if (this.socketDataOut == null)
      this.socketDataOut = this.daemonContext.getBufferPool().get();
    try
    {
      if ((this.socketDataOut.remaining() == this.socketDataOut.capacity()) && (this.protocolEngine.isConnected()))
      {
        SocketWriteCallback localSocketWriteCallback = this.protocolEngine.onSocketWrite(this.socketDataOut);
        if (localSocketWriteCallback != null)
          this.Ò.addLast(localSocketWriteCallback);
      }
      this.socketDataOut.flip();
      if (!this.socketChannel.isOpen())
      {
        i = 1;
        jsr 159;
      }
      if (this.socketDataOut.hasRemaining())
        i = this.socketChannel.write(this.socketDataOut);
      if (this.socketDataIn != null)
      {
        this.socketDataIn.flip();
        if (this.socketDataIn.hasRemaining())
          this.protocolEngine.onSocketRead(this.socketDataIn);
      }
      setWriteState((this.socketDataOut.hasRemaining()) || (this.protocolEngine.wantsToWrite()));
      i = !isConnected() ? 1 : 0;
    }
    catch (Throwable localThrowable)
    {
      int i;
      EventLog.LogEvent(this, "Connection closed on socket write: " + localThrowable.getMessage());
      closeConnection();
      int j = 1;
      return j;
    }
    finally
    {
      if (this.socketDataOut != null)
        if (!this.socketDataOut.hasRemaining())
        {
          this.daemonContext.getBufferPool().add(this.socketDataOut);
          this.socketDataOut = null;
          Iterator localIterator = this.Ò.iterator();
          while (localIterator.hasNext())
            ((SocketWriteCallback)localIterator.next()).completedWrite();
          this.Ò.clear();
        }
        else
        {
          this.socketDataOut.compact();
        }
      if (this.socketDataIn != null)
        if (!this.socketDataIn.hasRemaining())
        {
          this.daemonContext.getBufferPool().add(this.socketDataIn);
          this.socketDataIn = null;
        }
        else
        {
          this.socketDataIn.compact();
        }
    }
  }

  public void setThread(SelectorThread paramSelectorThread)
  {
    this.selectorThread = paramSelectorThread;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.SocketConnection
 * JD-Core Version:    0.6.0
 */