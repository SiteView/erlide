package com.maverick.sshd;

import com.maverick.nio.ByteBufferPool;
import com.maverick.nio.Daemon;
import com.maverick.nio.DaemonContext;
import com.maverick.nio.EventLog;
import com.maverick.nio.ProtocolEngine;
import com.maverick.nio.SelectorThread;
import com.maverick.nio.SocketHandler;
import com.maverick.nio.WriteOperationRequest;
import com.maverick.ssh.ChannelOpenException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public abstract class SocketForwardingChannel extends ForwardingChannel
  implements SocketHandler
{
  SocketChannel á;
  SelectorThread Ý;
  SelectionKey ß;
  boolean Ü = false;
  LinkedList Ú = new LinkedList();
  LinkedList Þ = new LinkedList();
  boolean Û = false;
  ByteBuffer à;

  public SocketForwardingChannel(String paramString)
  {
    super(paramString, 34000, 32768);
  }

  protected void onChannelOpen()
  {
  }

  public void initialize(ProtocolEngine paramProtocolEngine, Daemon paramDaemon)
  {
  }

  protected abstract byte[] createChannel()
    throws IOException;

  protected void onExtendedData(byte[] paramArrayOfByte, int paramInt)
  {
  }

  protected abstract void onRegistrationComplete();

  public void registrationCompleted(SelectableChannel paramSelectableChannel, SelectionKey paramSelectionKey, SelectorThread paramSelectorThread)
  {
    this.Ý = paramSelectorThread;
    this.ß = paramSelectionKey;
    onRegistrationComplete();
    synchronized (this.Þ)
    {
      while (this.Þ.size() > 0)
        onChannelData((byte[])(byte[])this.Þ.removeFirst());
    }
  }

  protected void onChannelData(byte[] paramArrayOfByte)
  {
    if (this.Ý == null)
    {
      synchronized (this.Þ)
      {
        this.Þ.addLast(paramArrayOfByte);
      }
    }
    else
    {
      ??? = ByteBuffer.wrap(paramArrayOfByte);
      if (this.Ú != null)
        synchronized (this.Ú)
        {
          if (this.Ú.isEmpty())
          {
            try
            {
              this.á.write((ByteBuffer)???);
            }
            catch (IOException localIOException)
            {
              close();
            }
            if (getLocalWindow() < 4096)
            {
              EventLog.LogEvent(this, "Sending more windows space");
              sendWindowAdjust(32768 - getLocalWindow());
            }
            if (((ByteBuffer)???).hasRemaining())
            {
              this.Ý.addSelectorOperation(new Runnable()
              {
                public void run()
                {
                  if (SocketForwardingChannel.this.ß.isValid())
                    SocketForwardingChannel.this.ß.interestOps(4);
                }
              });
              this.Ú.addLast(???);
              this.Ý.wakeup();
            }
          }
          else
          {
            this.Ú.addLast(???);
          }
        }
    }
  }

  protected void onChannelRequest(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    sendRequestResponse(false);
  }

  protected synchronized void onChannelFree()
  {
  }

  protected void onChannelClosing()
  {
  }

  protected boolean canClose()
  {
    synchronized (this.Ú)
    {
      return (this.Ú.isEmpty()) && (super.canClose());
    }
  }

  protected void onRemoteClose()
  {
    evaluateClosure();
    super.onRemoteClose();
  }

  protected void evaluateClosure()
  {
    this.Û = true;
    if (canClose())
      close();
  }

  protected void shutdownSocket()
  {
    if (this.Ý != null)
    {
      this.Ý.addSelectorOperation(new Runnable()
      {
        public void run()
        {
          synchronized (SocketForwardingChannel.this.Ú)
          {
            if (SocketForwardingChannel.this.Ú.size() > 0)
              EventLog.LogEvent(this, "Forwarding channel still has queued data");
            if ((SocketForwardingChannel.this.á != null) && (SocketForwardingChannel.this.á.isOpen()))
            {
              try
              {
                SocketForwardingChannel.this.á.close();
              }
              catch (IOException localIOException)
              {
              }
              finally
              {
              }
              if ((SocketForwardingChannel.this.ß != null) && (SocketForwardingChannel.this.ß.isValid()))
                SocketForwardingChannel.this.ß.cancel();
              SocketForwardingChannel.this.á = null;
              SocketForwardingChannel.this.ß = null;
            }
          }
        }
      });
      this.Ý.wakeup();
    }
    else
    {
      try
      {
        this.á.close();
      }
      catch (Throwable localThrowable)
      {
      }
    }
  }

  protected synchronized void onChannelClosed()
  {
    shutdownSocket();
  }

  protected synchronized void onLocalEOF()
  {
    if (this.O)
      close();
  }

  protected synchronized void onRemoteEOF()
  {
    if (this.L)
      close();
  }

  protected abstract void onChannelOpenConfirmation();

  protected void evaluateWindowSpace(int paramInt)
  {
  }

  protected abstract byte[] openChannel(byte[] paramArrayOfByte)
    throws WriteOperationRequest, ChannelOpenException;

  public synchronized boolean processReadEvent()
  {
    if ((this.á == null) || (!this.á.isOpen()) || (!isOpen()))
      return true;
    ByteBuffer localByteBuffer = getContext().getServer().getContext().getBufferPool().get();
    try
    {
      int i = this.á.read(localByteBuffer);
      if (i == -1)
      {
        sendEOF();
        this.Ý.addSelectorOperation(new Runnable()
        {
          public void run()
          {
            if (SocketForwardingChannel.this.ß.isValid())
              SocketForwardingChannel.this.ß.interestOps(4);
          }
        });
        evaluateClosure();
      }
      else if (i > 0)
      {
        localByteBuffer.flip();
        byte[] arrayOfByte = new byte[i];
        localByteBuffer.get(arrayOfByte);
        sendChannelDataWithBuffering(arrayOfByte);
      }
    }
    catch (Throwable localThrowable)
    {
      evaluateClosure();
    }
    finally
    {
      getContext().getServer().getContext().getBufferPool().add(localByteBuffer);
    }
    synchronized (this.Ú)
    {
      return (!isOpen()) && (this.Ú.size() == 0);
    }
  }

  public synchronized boolean processWriteEvent()
  {
    synchronized (this.Ú)
    {
      if ((this.á == null) || (!this.á.isOpen()))
        return true;
      int i = 0;
      try
      {
        if (this.Ü)
        {
          i = this.á.write(this.à);
          if (!this.à.hasRemaining())
          {
            this.à = null;
            this.Ü = false;
          }
        }
        else if (this.Ú.size() > 0)
        {
          this.à = ((ByteBuffer)this.Ú.removeFirst());
          i = this.á.write(this.à);
          if (this.à.hasRemaining())
            this.Ü = true;
          else
            this.à = null;
        }
        if (this.Ú.isEmpty())
        {
          EventLog.LogEvent(this, "Queue is empty");
          if (getLocalWindow() < 4096)
          {
            EventLog.LogEvent(this, "Sending more windows space");
            sendWindowAdjust(32768 - getLocalWindow());
          }
          this.Ý.addSelectorOperation(new Runnable()
          {
            public void run()
            {
              if ((SocketForwardingChannel.this.ß != null) && (SocketForwardingChannel.this.ß.isValid()))
                SocketForwardingChannel.this.ß.interestOps(1);
            }
          });
        }
      }
      catch (Throwable localThrowable)
      {
        evaluateClosure();
      }
      finally
      {
        if ((this.Û) && (canClose()))
          close();
      }
      return (!isOpen()) && (this.Ú.size() == 0);
    }
  }

  public int getInterestedOps()
  {
    return 5;
  }

  public void setThread(SelectorThread paramSelectorThread)
  {
    this.Ý = paramSelectorThread;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.SocketForwardingChannel
 * JD-Core Version:    0.6.0
 */