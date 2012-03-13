package com.maverick.ssh2;

import com.maverick.ssh.ChannelEventListener;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.message.Message;
import com.maverick.ssh.message.MessageObserver;
import com.maverick.ssh.message.SshAbstractChannel;
import com.maverick.ssh.message.SshChannelMessage;
import com.maverick.ssh.message.SshMessage;
import com.maverick.ssh.message.SshMessageStore;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Ssh2Channel extends SshAbstractChannel
{
  public static final String SESSION_CHANNEL = "session";
  b i;
  int b;
  String v;
  Vector m = new Vector();
  boolean n = false;
  boolean r = false;
  static Log f = LogFactory.getLog(Ssh2Channel.class);
  final MessageObserver e = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 93:
      case 96:
      case 97:
        return true;
      case 94:
      case 95:
      }
      return false;
    }
  };
  final MessageObserver u = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 94:
      case 96:
      case 97:
        return true;
      case 95:
      }
      return false;
    }
  };
  final MessageObserver c = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 95:
      case 96:
      case 97:
        return true;
      }
      return false;
    }
  };
  final MessageObserver s = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 97:
      case 99:
      case 100:
        return true;
      case 98:
      }
      return false;
    }
  };
  final MessageObserver l = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 97:
        return true;
      }
      return false;
    }
  };
  static final MessageObserver t = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 96:
      case 97:
        return true;
      }
      return false;
    }
  };
  _d g;
  _b q;
  _c k;
  _c p;
  boolean d = false;
  boolean j = false;
  long h;
  boolean o = false;

  public Ssh2Channel(String paramString, int paramInt1, int paramInt2, long paramLong)
  {
    this.v = paramString;
    this.k = new _c(paramInt1, paramInt2);
    this.g = new _d(this.u);
    this.q = new _b();
    this.h = paramLong;
  }

  protected MessageObserver getStickyMessageIds()
  {
    return t;
  }

  public void setAutoConsumeInput(boolean paramBoolean)
  {
    this.n = paramBoolean;
  }

  long c()
  {
    return this.k.c();
  }

  int b()
  {
    return this.k.b();
  }

  protected SshMessageStore getMessageStore()
    throws SshException
  {
    return super.getMessageStore();
  }

  public String getName()
  {
    return this.v;
  }

  public InputStream getInputStream()
  {
    return this.g;
  }

  public OutputStream getOutputStream()
  {
    return this.q;
  }

  public void addChannelEventListener(ChannelEventListener paramChannelEventListener)
  {
    synchronized (this.m)
    {
      if (paramChannelEventListener != null)
        this.m.addElement(paramChannelEventListener);
    }
  }

  public boolean isSendKeepAliveOnIdle()
  {
    return this.r;
  }

  public void setSendKeepAliveOnIdle(boolean paramBoolean)
  {
    this.r = paramBoolean;
  }

  public void idle()
  {
    if (this.r)
      try
      {
        sendRequest("keep-alive@sshtools.com", false, null, false);
      }
      catch (SshException localSshException)
      {
      }
  }

  void b(b paramb, int paramInt)
  {
    this.i = paramb;
    super.init(paramb, paramInt);
  }

  protected byte[] create()
  {
    return null;
  }

  protected void open(int paramInt1, long paramLong, int paramInt2)
    throws IOException
  {
    this.b = paramInt1;
    this.p = new _c(paramLong, paramInt2);
    this.state = 2;
    synchronized (this.m)
    {
      Enumeration localEnumeration = this.m.elements();
      while (localEnumeration.hasMoreElements())
        ((ChannelEventListener)localEnumeration.nextElement()).channelOpened(this);
    }
  }

  protected void open(int paramInt1, long paramLong, int paramInt2, byte[] paramArrayOfByte)
    throws IOException
  {
    open(paramInt1, paramLong, paramInt2);
  }

  protected boolean processChannelMessage(SshChannelMessage paramSshChannelMessage)
    throws SshException
  {
    try
    {
      switch (paramSshChannelMessage.getMessageId())
      {
      case 98:
        String str = paramSshChannelMessage.readString();
        boolean bool = paramSshChannelMessage.read() != 0;
        byte[] arrayOfByte = new byte[paramSshChannelMessage.available()];
        paramSshChannelMessage.read(arrayOfByte);
        if (f.isDebugEnabled())
          f.debug("Received SSH_MSG_CHANNEL_REQUEST id=" + this.channelid + " rid=" + this.b + " request=" + str + " wantreply=" + bool);
        channelRequest(str, bool, arrayOfByte);
        return true;
      case 93:
        paramSshChannelMessage.mark(4);
        int i1 = (int)paramSshChannelMessage.readInt();
        if (f.isDebugEnabled())
          f.debug("Received SSH_MSG_WINDOW_ADJUST id=" + this.channelid + " rid=" + this.b + " window=" + this.p.c() + " adjust=" + i1);
        paramSshChannelMessage.reset();
        return false;
      case 94:
        if (f.isDebugEnabled())
          f.debug("Received SSH_MSG_CHANNEL_DATA id=" + this.channelid + " rid=" + this.b + " len=" + (paramSshChannelMessage.available() - 4) + " window=" + this.k.c());
        if (this.n)
        {
          this.k.b(paramSshChannelMessage.available() - 4);
          if (this.k.c() <= this.k.d() / 2L)
            adjustWindow(this.k.d() - this.k.c());
        }
        Enumeration localEnumeration1 = this.m.elements();
        while (localEnumeration1.hasMoreElements())
          ((ChannelEventListener)localEnumeration1.nextElement()).dataReceived(this, paramSshChannelMessage.array(), paramSshChannelMessage.getPosition() + 4, paramSshChannelMessage.available() - 4);
        return this.n;
      case 95:
        if (f.isDebugEnabled())
          f.debug("Received SSH_MSG_CHANNEL_EXTENDED_DATA id=" + this.channelid + " rid=" + this.b + " len=" + (paramSshChannelMessage.available() - 4) + " window=" + this.k.c());
        int i2 = (int)ByteArrayReader.readInt(paramSshChannelMessage.array(), paramSshChannelMessage.getPosition());
        if (this.n)
        {
          this.k.b(paramSshChannelMessage.available() - 8);
          if (this.k.c() <= this.k.d() / 2L)
            adjustWindow(this.k.d() - this.k.c());
        }
        Enumeration localEnumeration2 = this.m.elements();
        while (localEnumeration2.hasMoreElements())
          ((ChannelEventListener)localEnumeration2.nextElement()).extendedDataReceived(this, paramSshChannelMessage.array(), paramSshChannelMessage.getPosition() + 8, paramSshChannelMessage.available() - 8, i2);
        return this.n;
      case 97:
        if (f.isDebugEnabled())
          f.debug("Received SSH_MSG_CHANNEL_CLOSE id=" + this.channelid + " rid=" + this.b);
        synchronized (this)
        {
          if (!this.d)
            synchronized (this.ms)
            {
              if (!this.ms.isClosed())
                this.ms.close();
            }
        }
        b(true);
        return false;
      case 96:
        if (f.isDebugEnabled())
          f.debug("Received SSH_MSG_CHANNEL_EOF id=" + this.channelid + " rid=" + this.b);
        ??? = this.m.elements();
        while (((Enumeration)???).hasMoreElements())
          ((ChannelEventListener)((Enumeration)???).nextElement()).channelEOF(this);
        channelEOF();
        return false;
      }
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  SshChannelMessage b(MessageObserver paramMessageObserver)
    throws SshException, EOFException
  {
    SshChannelMessage localSshChannelMessage = (SshChannelMessage)this.ms.nextMessage(paramMessageObserver, this.h);
    switch (localSshChannelMessage.getMessageId())
    {
    case 93:
      try
      {
        this.p.b(localSshChannelMessage.readInt());
        if (f.isDebugEnabled())
          f.debug("Applied window adjust window=" + this.p.c());
      }
      catch (IOException localIOException1)
      {
        throw new SshException(5, localIOException1);
      }
    case 94:
      try
      {
        int i1 = (int)localSshChannelMessage.readInt();
        processStandardData(i1, localSshChannelMessage);
      }
      catch (IOException localIOException2)
      {
        throw new SshException(5, localIOException2);
      }
    case 95:
      try
      {
        int i2 = (int)localSshChannelMessage.readInt();
        int i3 = (int)localSshChannelMessage.readInt();
        processExtendedData(i2, i3, localSshChannelMessage);
      }
      catch (IOException localIOException3)
      {
        throw new SshException(5, localIOException3);
      }
    case 97:
      b(true);
      throw new EOFException("The channel is closed");
    case 96:
      throw new EOFException("The channel is EOF");
    }
    return localSshChannelMessage;
  }

  protected void processStandardData(int paramInt, SshChannelMessage paramSshChannelMessage)
    throws SshException
  {
    this.g.b(paramInt, paramSshChannelMessage);
  }

  protected void processExtendedData(int paramInt1, int paramInt2, SshChannelMessage paramSshChannelMessage)
    throws SshException
  {
  }

  protected _d createExtendedDataStream()
  {
    return new _d(this.c);
  }

  void b(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException
  {
    try
    {
      if (this.state != 2)
        throw new SshException("The channel is closed", 6);
      if (paramInt2 > 0)
      {
        localObject = new ByteArrayWriter(paramInt2 + 9);
        ((ByteArrayWriter)localObject).write(94);
        ((ByteArrayWriter)localObject).writeInt(this.b);
        ((ByteArrayWriter)localObject).writeBinaryString(paramArrayOfByte, paramInt1, paramInt2);
        if (f.isDebugEnabled())
          f.debug("Sending SSH_MSG_CHANNEL_DATA id=" + this.channelid + " rid=" + this.b + " len=" + paramInt2 + " window=" + this.p.c());
        this.i.b(((ByteArrayWriter)localObject).toByteArray(), true);
      }
      Object localObject = this.m.elements();
      while (((Enumeration)localObject).hasMoreElements())
        ((ChannelEventListener)((Enumeration)localObject).nextElement()).dataSent(this, paramArrayOfByte, paramInt1, paramInt2);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public long getWindow()
  {
    return this.k.c;
  }

  public void adjustWindow(long paramLong)
    throws SshException
  {
    try
    {
      if ((this.d) || (isClosed()))
        return;
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter(9);
      localByteArrayWriter.write(93);
      localByteArrayWriter.writeInt(this.b);
      localByteArrayWriter.writeInt(paramLong);
      if (f.isDebugEnabled())
        f.debug("Sending SSH_MSG_WINDOW_ADJUST id=" + this.channelid + " rid=" + this.b + " window=" + this.k.c() + " adjust=" + paramLong);
      this.k.b(paramLong);
      this.i.b(localByteArrayWriter.toByteArray(), true);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public boolean sendRequest(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
    throws SshException
  {
    return sendRequest(paramString, paramBoolean, paramArrayOfByte, true);
  }

  public boolean sendRequest(String paramString, boolean paramBoolean1, byte[] paramArrayOfByte, boolean paramBoolean2)
    throws SshException
  {
    synchronized (this.i)
    {
      try
      {
        ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
        localByteArrayWriter.write(98);
        localByteArrayWriter.writeInt(this.b);
        localByteArrayWriter.writeString(paramString);
        localByteArrayWriter.writeBoolean(paramBoolean1);
        if (paramArrayOfByte != null)
          localByteArrayWriter.write(paramArrayOfByte);
        if (f.isDebugEnabled())
          f.debug("Sending SSH_MSG_CHANNEL_REQUEST id=" + this.channelid + " rid=" + this.b + " request=" + paramString + " wantreply=" + paramBoolean1);
        this.i.b(localByteArrayWriter.toByteArray(), true);
        int i1 = 0;
        if (paramBoolean1)
        {
          SshChannelMessage localSshChannelMessage = b(this.s);
          return localSshChannelMessage.getMessageId() == 99;
        }
        return i1;
      }
      catch (IOException localIOException)
      {
        throw new SshException(localIOException, 5);
      }
    }
  }

  public void close()
  {
    int i1 = 0;
    synchronized (this)
    {
      if (!this.d)
        i1 = this.d = 1;
    }
    if ((this.state == 2) && (i1 != 0))
    {
      synchronized (this.m)
      {
        Enumeration localEnumeration = this.m.elements();
        while (localEnumeration.hasMoreElements())
          ((ChannelEventListener)localEnumeration.nextElement()).channelClosing(this);
      }
      try
      {
        this.q.b(!this.q.c);
        ??? = new ByteArrayWriter(5);
        ((ByteArrayWriter)???).write(97);
        ((ByteArrayWriter)???).writeInt(this.b);
        try
        {
          if (f.isDebugEnabled())
            f.debug("Sending SSH_MSG_CHANNEL_CLOSE id=" + this.channelid + " rid=" + this.b);
          this.i.b(((ByteArrayWriter)???).toByteArray(), true);
        }
        catch (SshException localSshException)
        {
        }
        this.state = 3;
      }
      catch (EOFException localEOFException)
      {
      }
      catch (IOException localIOException)
      {
        this.i.nb.disconnect(10, "IOException during channel close: " + localIOException.getMessage());
      }
      finally
      {
        b(false);
      }
    }
  }

  private void b(boolean paramBoolean)
  {
    if (this.state != 3)
    {
      close();
      if (!paramBoolean)
        paramBoolean = this.ms.hasMessage(this.l) != null;
    }
    if (paramBoolean)
      synchronized (this)
      {
        if (!this.j)
        {
          this.i.b(this);
          synchronized (this.m)
          {
            Enumeration localEnumeration = this.m.elements();
            while (localEnumeration.hasMoreElements())
              ((ChannelEventListener)localEnumeration.nextElement()).channelClosed(this);
          }
          this.j = true;
        }
      }
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof Ssh2Channel))
      return ((Ssh2Channel)paramObject).getChannelId() == this.channelid;
    return false;
  }

  protected void channelRequest(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
    throws SshException
  {
    if (paramBoolean)
      try
      {
        ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
        localByteArrayWriter.write(100);
        localByteArrayWriter.writeInt(this.b);
        this.i.b(localByteArrayWriter.toByteArray(), true);
      }
      catch (IOException localIOException)
      {
        throw new SshException(localIOException, 5);
      }
  }

  protected void channelEOF()
  {
  }

  static class _c
  {
    long c;
    long d;
    int b;

    _c(long paramLong, int paramInt)
    {
      this.d = paramLong;
      this.c = paramLong;
      this.b = paramInt;
    }

    int b()
    {
      return this.b;
    }

    long d()
    {
      return this.d;
    }

    void b(long paramLong)
    {
      this.c += paramLong;
    }

    void b(int paramInt)
    {
      this.c -= paramInt;
    }

    long c()
    {
      return this.c;
    }
  }

  class _d extends InputStream
  {
    int d = 0;
    MessageObserver f;
    long c = 0L;
    SshChannelMessage e = null;

    _d(MessageObserver arg2)
    {
      Object localObject;
      this.f = localObject;
    }

    void b(int paramInt, SshChannelMessage paramSshChannelMessage)
    {
      this.d = paramInt;
      this.e = paramSshChannelMessage;
    }

    public synchronized int available()
      throws IOException
    {
      try
      {
        if ((this.d == 0) && (Ssh2Channel.this.getMessageStore().hasMessage(this.f) != null))
          Ssh2Channel.this.b(this.f);
        return this.d;
      }
      catch (EOFException localEOFException)
      {
        return -1;
      }
      catch (SshException localSshException)
      {
      }
      throw new SshIOException(localSshException);
    }

    public int read()
      throws IOException
    {
      byte[] arrayOfByte = new byte[1];
      int i = read(arrayOfByte, 0, 1);
      if (i > 0)
        return arrayOfByte[0] & 0xFF;
      return -1;
    }

    public long skip(long paramLong)
      throws IOException
    {
      int i = this.d < paramLong ? this.d : (int)paramLong;
      try
      {
        if ((i == 0) && (Ssh2Channel.this.isClosed()))
          throw new EOFException("The inputstream is closed");
        this.e.skip(i);
        this.d -= i;
        if (this.d + Ssh2Channel.this.k.c() < Ssh2Channel.this.k.d() / 2L)
          try
          {
            Ssh2Channel.this.adjustWindow(Ssh2Channel.this.k.d() - Ssh2Channel.this.k.c() - this.d);
          }
          catch (SshException localSshException)
          {
            throw new SshIOException(localSshException);
          }
      }
      finally
      {
        this.c += i;
      }
      return i;
    }

    public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      try
      {
        if (available() == -1)
          return -1;
        if ((this.d <= 0) && (!Ssh2Channel.this.isClosed()))
          Ssh2Channel.this.b(this.f);
        int i = this.d < paramInt2 ? this.d : paramInt2;
        if ((i == 0) && (Ssh2Channel.this.isClosed()))
          return -1;
        this.e.read(paramArrayOfByte, paramInt1, i);
        Ssh2Channel.this.k.b(i);
        this.d -= i;
        if ((System.getProperty("maverick.windowAdjustTest", "false").equals("true")) || ((this.d + Ssh2Channel.this.k.c() < Ssh2Channel.this.k.d() / 2L) && (!Ssh2Channel.this.isClosed()) && (!Ssh2Channel.this.d)))
          Ssh2Channel.this.adjustWindow(Ssh2Channel.this.k.d() - Ssh2Channel.this.k.c() - this.d);
        this.c += i;
        return i;
      }
      catch (SshException localSshException)
      {
        throw new SshIOException(localSshException);
      }
      catch (EOFException localEOFException)
      {
      }
      return -1;
    }
  }

  class _b extends OutputStream
  {
    boolean c = false;

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
      try
      {
        do
        {
          if (Ssh2Channel.this.p.c() <= 0L)
            Ssh2Channel.this.b(Ssh2Channel.this.e);
          synchronized (Ssh2Channel.this)
          {
            if (this.c)
              throw new EOFException("The channel is EOF");
            if ((Ssh2Channel.this.isClosed()) || (Ssh2Channel.this.d))
              throw new EOFException("The channel is closed");
            long l = Ssh2Channel.this.p.b() < paramInt2 ? Ssh2Channel.this.p.b() : paramInt2;
            if (l > 0L)
            {
              Ssh2Channel.this.b(paramArrayOfByte, paramInt1, (int)l);
              Ssh2Channel.this.p.b((int)l);
              paramInt2 = (int)(paramInt2 - l);
              paramInt1 = (int)(paramInt1 + l);
            }
          }
        }
        while (paramInt2 > 0);
      }
      catch (SshException localSshException)
      {
        throw new SshIOException(localSshException);
      }
    }

    public void close()
      throws IOException
    {
      b((!Ssh2Channel.this.isClosed()) && (!this.c) && (!Ssh2Channel.this.d));
    }

    public void b(boolean paramBoolean)
      throws IOException
    {
      if (paramBoolean)
      {
        ByteArrayWriter localByteArrayWriter = new ByteArrayWriter(5);
        localByteArrayWriter.write(96);
        localByteArrayWriter.writeInt(Ssh2Channel.this.b);
        try
        {
          if (Ssh2Channel.f.isDebugEnabled())
            Ssh2Channel.f.debug("Sending SSH_MSG_CHANNEL_EOF id=" + Ssh2Channel.this.getChannelId() + " rid=" + Ssh2Channel.this.b);
          Ssh2Channel.this.i.b(localByteArrayWriter.toByteArray(), true);
        }
        catch (SshException localSshException)
        {
          throw new SshIOException(localSshException);
        }
        finally
        {
          this.c = true;
        }
      }
      this.c = true;
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.Ssh2Channel
 * JD-Core Version:    0.6.0
 */