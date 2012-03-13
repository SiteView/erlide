package com.maverick.ssh1;

import com.maverick.ssh.ChannelEventListener;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.message.Message;
import com.maverick.ssh.message.MessageObserver;
import com.maverick.ssh.message.SshAbstractChannel;
import com.maverick.ssh.message.SshChannelMessage;
import com.maverick.ssh.message.SshMessage;
import com.maverick.ssh.message.SshMessageStore;
import com.maverick.util.ByteArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class e extends SshAbstractChannel
{
  b ud;
  int zd;
  _c wd;
  _b rd;
  boolean td = false;
  boolean sd = false;
  boolean pd = false;
  Vector vd = new Vector();
  long xd;
  static Log qd = LogFactory.getLog(e.class);
  MessageObserver yd = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 24:
      case 25:
        return true;
      }
      return false;
    }
  };

  void b(b paramb, int paramInt, long paramLong)
  {
    this.ud = paramb;
    this.xd = paramLong;
    super.init(paramb, paramInt);
  }

  void b(int paramInt)
  {
    this.zd = paramInt;
    this.wd = new _c(23);
    this.rd = new _b();
    synchronized (this.vd)
    {
      for (int i = 0; i < this.vd.size(); i++)
        ((ChannelEventListener)this.vd.elementAt(i)).channelOpened(this);
    }
  }

  public void setAutoConsumeInput(boolean paramBoolean)
  {
    this.pd = paramBoolean;
  }

  protected SshMessageStore getMessageStore()
    throws SshException
  {
    return super.getMessageStore();
  }

  protected boolean processChannelMessage(SshChannelMessage paramSshChannelMessage)
    throws SshException
  {
    switch (paramSshChannelMessage.getMessageId())
    {
    case 23:
      if (this.vd != null)
        for (int i = 0; i < this.vd.size(); i++)
          ((ChannelEventListener)this.vd.elementAt(i)).dataReceived(this, paramSshChannelMessage.array(), paramSshChannelMessage.getPosition() + 4, paramSshChannelMessage.available() - 4);
      return this.pd;
    case 24:
      if (!this.sd)
        try
        {
          ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
          localByteArrayWriter.write(25);
          localByteArrayWriter.writeInt(this.zd);
          if (qd.isDebugEnabled())
            qd.debug("Sending SSH_MSG_CHANNEL_CLOSE_CONFIRMATION");
          this.ud.b(localByteArrayWriter.toByteArray());
          this.sd = (this.td = 1);
          synchronized (this.vd)
          {
            for (int k = 0; k < this.vd.size(); k++)
              ((ChannelEventListener)this.vd.elementAt(k)).channelClosed(this);
          }
        }
        catch (IOException localIOException)
        {
          throw new SshException(5, localIOException);
        }
      return false;
    case 25:
      this.td = true;
      synchronized (this.vd)
      {
        for (int j = 0; j < this.vd.size(); j++)
          ((ChannelEventListener)this.vd.elementAt(j)).channelClosed(this);
      }
      return false;
    }
    return false;
  }

  public void addChannelEventListener(ChannelEventListener paramChannelEventListener)
  {
    synchronized (this.vd)
    {
      if (paramChannelEventListener != null)
        this.vd.addElement(paramChannelEventListener);
    }
  }

  public InputStream getInputStream()
  {
    return this.wd;
  }

  public OutputStream getOutputStream()
  {
    return this.rd;
  }

  public void close()
  {
    try
    {
      this.rd.close();
    }
    catch (IOException localIOException)
    {
      this.ud.close();
    }
  }

  protected MessageObserver getStickyMessageIds()
  {
    return this.yd;
  }

  class _b extends OutputStream
  {
    _b()
    {
    }

    public void write(int paramInt)
      throws IOException
    {
      write(new byte[] { (byte)paramInt }, 0, 1);
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (e.this.ud.isClosed())
        throw new SshIOException(new SshException("The session is closed", 6));
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter(paramInt2 + 9);
      localByteArrayWriter.write(23);
      localByteArrayWriter.writeInt(e.this.zd);
      localByteArrayWriter.writeBinaryString(paramArrayOfByte, paramInt1, paramInt2);
      if (e.qd.isDebugEnabled())
        e.qd.debug("Sending SSH_MSG_CHANNEL_DATA");
      try
      {
        e.this.ud.b(localByteArrayWriter.toByteArray());
      }
      catch (SshException localSshException)
      {
        throw new SshIOException(localSshException);
      }
      if (e.this.vd != null)
        for (int i = 0; i < e.this.vd.size(); i++)
          ((ChannelEventListener)e.this.vd.elementAt(i)).dataSent(e.this, paramArrayOfByte, paramInt1, paramInt2);
    }

    public void close()
      throws IOException
    {
      if (!e.this.td)
      {
        ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
        localByteArrayWriter.write(24);
        localByteArrayWriter.writeInt(e.this.zd);
        if (e.qd.isDebugEnabled())
          e.qd.debug("Sending SSH_MSG_CHANNEL_CLOSE");
        try
        {
          e.this.ud.b(localByteArrayWriter.toByteArray());
        }
        catch (SshException localSshException)
        {
          throw new EOFException();
        }
        e.this.sd = true;
      }
    }
  }

  class _c extends InputStream
  {
    int c;
    SshMessage e;
    MessageObserver d;

    _c(int arg2)
    {
      int i;
      this.c = i;
      this.d = new MessageObserver(e.this)
      {
        public boolean wantsNotification(Message paramMessage)
        {
          switch (paramMessage.getMessageId())
          {
          case 24:
          case 25:
            return true;
          }
          return e._c.this.c == paramMessage.getMessageId();
        }
      };
    }

    public int available()
      throws IOException
    {
      try
      {
        if ((this.e == null) || (this.e.available() == 0))
          if (e.this.getMessageStore().hasMessage(this.d) != null)
            b();
          else
            return 0;
        return e.this.td ? -1 : this.e.available() > 0 ? this.e.available() : 0;
      }
      catch (SshException localSshException)
      {
      }
      throw new SshIOException(localSshException);
    }

    public int read()
      throws IOException
    {
      try
      {
        if ((e.this.td) && (available() <= 0))
          return -1;
        b();
        return this.e.read();
      }
      catch (EOFException localEOFException)
      {
      }
      return -1;
    }

    void b()
      throws IOException
    {
      try
      {
        if ((this.e == null) || (this.e.available() == 0))
        {
          SshMessage localSshMessage = e.this.getMessageStore().nextMessage(this.d, e.this.xd);
          switch (localSshMessage.getMessageId())
          {
          case 23:
            if (e.qd.isDebugEnabled())
              e.qd.debug("Received SSH_MSG_CHANNEL_DATA");
            localSshMessage.skip(4L);
            this.e = localSshMessage;
            break;
          case 24:
            if (e.qd.isDebugEnabled())
              e.qd.debug("Received SSH_MSG_CHANNEL_CLOSE");
            throw new EOFException("The channel has been closed");
          case 25:
            if (e.qd.isDebugEnabled())
              e.qd.debug("Received SSH_MSG_CHANNEL_CLOSE_CONFIRMATION");
            throw new EOFException("The channel has been closed");
          }
        }
      }
      catch (SshException localSshException)
      {
        throw new SshIOException(localSshException);
      }
    }

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      try
      {
        if ((e.this.td) && (available() <= 0))
          return -1;
        b();
        return this.e.read(paramArrayOfByte, paramInt1, paramInt2);
      }
      catch (EOFException localEOFException)
      {
      }
      return -1;
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh1.e
 * JD-Core Version:    0.6.0
 */