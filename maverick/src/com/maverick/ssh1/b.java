package com.maverick.ssh1;

import com.maverick.ssh.ChannelEventListener;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.ForwardingRequestListener;
import com.maverick.ssh.PseudoTerminalModes;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.SshTransport;
import com.maverick.ssh.SshTunnel;
import com.maverick.ssh.message.Message;
import com.maverick.ssh.message.MessageObserver;
import com.maverick.ssh.message.SshAbstractChannel;
import com.maverick.ssh.message.SshChannelMessage;
import com.maverick.ssh.message.SshMessage;
import com.maverick.ssh.message.SshMessageRouter;
import com.maverick.ssh.message.SshMessageStore;
import com.maverick.util.ByteArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class b extends SshMessageRouter
  implements SshSession
{
  static final MessageObserver qb = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 21:
      case 22:
        return true;
      }
      return false;
    }
  };
  f vb;
  Ssh1Client rb;
  InputStream yb = new _d(17);
  InputStream tb = new _d(18);
  OutputStream ub = new _c();
  boolean bc = false;
  int sb = -2147483648;
  boolean wb = false;
  boolean ob = false;
  Vector xb = new Vector();
  boolean pb = false;
  long zb;
  Hashtable cc = new Hashtable();
  _b ac = new _b();
  static Log ab = LogFactory.getLog(b.class);

  b(f paramf, Ssh1Client paramSsh1Client, ChannelEventListener paramChannelEventListener, boolean paramBoolean, long paramLong)
  {
    super(paramf, paramf.b.getChannelLimit(), paramBoolean);
    this.vb = paramf;
    this.rb = paramSsh1Client;
    this.zb = paramLong;
    if (paramChannelEventListener != null)
    {
      addChannelEventListener(paramChannelEventListener);
      synchronized (this.xb)
      {
        for (int i = 0; i < this.xb.size(); i++)
          ((ChannelEventListener)this.xb.elementAt(i)).channelOpened(this);
      }
    }
  }

  public void setAutoConsumeInput(boolean paramBoolean)
  {
    this.pb = paramBoolean;
  }

  protected int allocateChannel(SshAbstractChannel paramSshAbstractChannel)
  {
    return super.allocateChannel(paramSshAbstractChannel);
  }

  public SshClient getClient()
  {
    return this.rb;
  }

  public SshMessageRouter getMessageRouter()
  {
    return this;
  }

  protected SshMessage createMessage(byte[] paramArrayOfByte)
    throws SshException
  {
    if ((paramArrayOfByte[0] >= 21) && (paramArrayOfByte[0] <= 25))
      return new SshChannelMessage(paramArrayOfByte);
    return new SshMessage(paramArrayOfByte);
  }

  public int getChannelId()
  {
    return -1;
  }

  public void addChannelEventListener(ChannelEventListener paramChannelEventListener)
  {
    synchronized (this.xb)
    {
      if (paramChannelEventListener != null)
        this.xb.addElement(paramChannelEventListener);
    }
  }

  protected SshMessageStore getGlobalMessages()
  {
    return super.getGlobalMessages();
  }

  public boolean startShell()
    throws SshException
  {
    if (this.bc)
      throw new SshException("The session is already in interactive mode!", 4);
    this.vb.d(new byte[] { 12 });
    this.bc = true;
    start();
    return true;
  }

  public boolean executeCommand(String paramString)
    throws SshException
  {
    if (this.bc)
      throw new SshException("The session is already in interactive mode!", 4);
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(13);
      localByteArrayWriter.writeString(paramString);
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_CMSG_EXEC_CMD");
      this.vb.d(localByteArrayWriter.toByteArray());
      this.bc = true;
      start();
      return true;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Ssh1Session.executeCommand caught an IOException: " + localIOException.getMessage(), 5);
  }

  public boolean executeCommand(String paramString1, String paramString2)
    throws SshException
  {
    if (this.bc)
      throw new SshException("The session is already in interactive mode!", 4);
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(13);
      localByteArrayWriter.writeString(paramString1, paramString2);
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_CMSG_EXEC_CMD");
      this.vb.d(localByteArrayWriter.toByteArray());
      this.bc = true;
      start();
      return true;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Ssh1Session.executeCommand caught an IOException: " + localIOException.getMessage(), 5);
  }

  public boolean isClosed()
  {
    return this.vb.d == 3;
  }

  public boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws SshException
  {
    return requestPseudoTerminal(paramString, paramInt1, paramInt2, paramInt3, paramInt4, new byte[] { 0 });
  }

  public boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, PseudoTerminalModes paramPseudoTerminalModes)
    throws SshException
  {
    return requestPseudoTerminal(paramString, paramInt1, paramInt2, paramInt3, paramInt4, paramPseudoTerminalModes.toByteArray());
  }

  public boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
    throws SshException
  {
    if (this.bc)
      throw new SshException("The session is already in interactive mode!", 4);
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(10);
      localByteArrayWriter.writeString(paramString);
      localByteArrayWriter.writeInt(paramInt2);
      localByteArrayWriter.writeInt(paramInt1);
      localByteArrayWriter.writeInt(paramInt3);
      localByteArrayWriter.writeInt(paramInt4);
      localByteArrayWriter.write(paramArrayOfByte);
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_CMSG_REQUEST_PTY");
      this.vb.d(localByteArrayWriter.toByteArray());
      return this.vb.f();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Ssh1Client.requestPseudoTerminal() caught an IOException: " + localIOException.getMessage(), 5);
  }

  public void changeTerminalDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws SshException
  {
    if (!this.bc)
      throw new SshException("Dimensions can only be changed whilst in interactive mode", 4);
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(11);
      localByteArrayWriter.writeInt(paramInt2);
      localByteArrayWriter.writeInt(paramInt1);
      localByteArrayWriter.writeInt(paramInt4);
      localByteArrayWriter.writeInt(paramInt3);
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_CMSG_WINDOW_SIZE");
      this.vb.d(localByteArrayWriter.toByteArray());
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  void b(byte[] paramArrayOfByte)
    throws SshException
  {
    this.vb.d(paramArrayOfByte);
  }

  protected boolean processGlobalMessage(SshMessage paramSshMessage)
    throws SshException
  {
    int i;
    Object localObject5;
    switch (paramSshMessage.getMessageId())
    {
    case 17:
      if (this.xb != null)
        for (i = 0; i < this.xb.size(); i++)
          ((ChannelEventListener)this.xb.elementAt(i)).dataReceived(this, paramSshMessage.array(), paramSshMessage.getPosition() + 4, paramSshMessage.available() - 4);
      return this.pb;
    case 18:
      if (this.xb != null)
        for (i = 0; i < this.xb.size(); i++)
          ((ChannelEventListener)this.xb.elementAt(i)).extendedDataReceived(this, paramSshMessage.array(), paramSshMessage.getPosition() + 4, paramSshMessage.available() - 4, 1);
      return this.pb;
    case 20:
      int k;
      synchronized (this.xb)
      {
        for (k = 0; k < this.xb.size(); k++)
          ((ChannelEventListener)this.xb.elementAt(k)).channelClosing(this);
      }
      try
      {
        this.sb = (int)paramSshMessage.readInt();
      }
      catch (IOException i)
      {
        throw new SshException(5, (Throwable)???);
      }
      this.wb = true;
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_CMSG_EXIT_CONFIRMATION");
      try
      {
        this.vb.d(new byte[] { 33 });
        getGlobalMessages().close();
        this.vb.c();
        synchronized (this.xb)
        {
          for (k = 0; k < this.xb.size(); k++)
            ((ChannelEventListener)this.xb.elementAt(k)).channelClosed(this);
        }
        stop();
      }
      catch (Throwable i)
      {
        getGlobalMessages().close();
        this.vb.c();
        synchronized (this.xb)
        {
          for (k = 0; k < this.xb.size(); k++)
            ((ChannelEventListener)this.xb.elementAt(k)).channelClosed(this);
        }
        stop();
      }
      finally
      {
        getGlobalMessages().close();
        this.vb.c();
        synchronized (this.xb)
        {
          for (int i3 = 0; i3 < this.xb.size(); i3++)
            ((ChannelEventListener)this.xb.elementAt(i3)).channelClosed(this);
        }
        stop();
      }
      return true;
    case 29:
      int j = 0;
      ByteArrayWriter localByteArrayWriter2 = new ByteArrayWriter();
      try
      {
        j = (int)paramSshMessage.readInt();
        String str1 = paramSshMessage.readString();
        int n = (int)paramSshMessage.readInt();
        String str4 = "";
        if ((this.vb.l & 0x2) != 0)
          str4 = paramSshMessage.readString();
        localObject5 = this.ac.b(str1, n, str4);
        localByteArrayWriter2.write(21);
        localByteArrayWriter2.writeInt(j);
        localByteArrayWriter2.writeInt(((e)localObject5).getChannelId());
        if (ab.isDebugEnabled())
          ab.debug("Sending SSH_MSG_CHANNEL_OPEN_CONFIRMATION");
        b(localByteArrayWriter2.toByteArray());
        ((e)localObject5).b(j);
      }
      catch (Exception localException1)
      {
        try
        {
          localByteArrayWriter2.write(22);
          localByteArrayWriter2.writeInt(j);
          if (ab.isDebugEnabled())
            ab.debug("Sending SSH_MSG_CHANNEL_OPEN_FAILURE");
          b(localByteArrayWriter2.toByteArray());
        }
        catch (Exception localException3)
        {
        }
      }
      return true;
    case 27:
      ByteArrayWriter localByteArrayWriter1 = new ByteArrayWriter();
      int m = 0;
      try
      {
        m = (int)paramSshMessage.readInt();
        String str2 = "";
        if ((this.vb.l & 0x2) != 0)
          str2 = paramSshMessage.readString();
        String str3 = this.rb.getContext().getX11Display();
        int i1 = str3.indexOf(':');
        localObject5 = "localhost";
        int i2;
        if (i1 != -1)
        {
          localObject5 = str3.substring(0, i1);
          i2 = Integer.parseInt(str3.substring(i1 + 1));
        }
        else
        {
          i2 = Integer.parseInt(str3);
        }
        e locale = this.ac.b(str3, (String)localObject5, i2 <= 10 ? 6000 + i2 : i2, str2);
        localByteArrayWriter1.write(21);
        localByteArrayWriter1.writeInt(m);
        localByteArrayWriter1.writeInt(locale.getChannelId());
        if (ab.isDebugEnabled())
          ab.debug("Sending SSH_MSG_CHANNEL_OPEN_CONFIRMATION");
        b(localByteArrayWriter1.toByteArray());
        locale.b(m);
      }
      catch (Exception localException2)
      {
        try
        {
          localByteArrayWriter1.write(22);
          localByteArrayWriter1.writeInt(m);
          if (ab.isDebugEnabled())
            ab.debug("Sending SSH_MSG_CHANNEL_OPEN_FAILURE");
          b(localByteArrayWriter1.toByteArray());
        }
        catch (Exception localException4)
        {
        }
      }
      return true;
    case 19:
    case 21:
    case 22:
    case 23:
    case 24:
    case 25:
    case 26:
    case 28:
    }
    return false;
  }

  public int exitCode()
  {
    return this.sb;
  }

  public InputStream getInputStream()
  {
    return this.yb;
  }

  public InputStream getStderrInputStream()
  {
    return this.tb;
  }

  public OutputStream getOutputStream()
  {
    return this.ub;
  }

  public void close()
  {
    int i;
    synchronized (this.xb)
    {
      for (i = 0; i < this.xb.size(); i++)
        ((ChannelEventListener)this.xb.elementAt(i)).channelClosing(this);
    }
    try
    {
      this.ub.close();
    }
    catch (IOException )
    {
    }
    getGlobalMessages().close();
    signalClosingState();
    this.vb.c("The user disconnected the application");
    synchronized (this.xb)
    {
      for (i = 0; i < this.xb.size(); i++)
        ((ChannelEventListener)this.xb.elementAt(i)).channelClosed(this);
    }
  }

  void b(int paramInt, byte[] paramArrayOfByte, e parame)
    throws SshException, ChannelOpenException
  {
    b(paramInt, paramArrayOfByte, parame, this.rb.getContext().getMessageTimeout());
  }

  void b(int paramInt, byte[] paramArrayOfByte, e parame, long paramLong)
    throws SshException, ChannelOpenException
  {
    try
    {
      if (!this.bc)
        throw new SshException("The session must be in interactive mode! Start the user's shell before attempting this operation", 4);
      int i = allocateChannel(parame);
      if (i == -1)
        throw new ChannelOpenException("Maximum number of channels exceeded", 4);
      parame.b(this, i, paramLong);
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(paramInt);
      localByteArrayWriter.writeInt(i);
      localByteArrayWriter.write(paramArrayOfByte);
      this.vb.d(localByteArrayWriter.toByteArray());
      SshMessage localSshMessage = parame.getMessageStore().nextMessage(qb, paramLong);
      if (localSshMessage.getMessageId() == 21)
      {
        if (ab.isDebugEnabled())
          ab.debug("Received SSH_MSG_CHANNEL_OPEN_CONFIRMATION");
        parame.b((int)localSshMessage.readInt());
      }
      else
      {
        throw new SshException("The remote computer failed to open a channel", 6);
      }
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  boolean b(String paramString, ForwardingRequestListener paramForwardingRequestListener)
    throws SshException
  {
    if (!this.rb.getContext().getX11Display().equals(paramString))
      this.rb.getContext().setX11Display(paramString);
    int i = paramString.indexOf(':');
    String str1 = "localhost";
    int j;
    if (i != -1)
    {
      str1 = paramString.substring(0, i);
      j = Integer.parseInt(paramString.substring(i + 1));
    }
    else
    {
      j = Integer.parseInt(paramString);
    }
    byte[] arrayOfByte = this.rb.getContext().getX11AuthenticationCookie();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int k = 0; k < 16; k++)
    {
      String str2 = Integer.toHexString(arrayOfByte[k] & 0xFF);
      if (str2.length() == 1)
        str2 = "0" + str2;
      localStringBuffer.append(str2);
    }
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(34);
      localByteArrayWriter.writeString("MIT-MAGIC-COOKIE-1");
      localByteArrayWriter.writeString(localStringBuffer.toString());
      localByteArrayWriter.writeInt(j);
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_CMSG_X11_REQUEST_FORWARDING");
      this.vb.d(localByteArrayWriter.toByteArray());
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
    if ((this.ob = this.vb.f()))
    {
      this.cc.put(paramString, paramForwardingRequestListener);
      return true;
    }
    return false;
  }

  boolean b(int paramInt1, String paramString, int paramInt2, ForwardingRequestListener paramForwardingRequestListener)
    throws SshException
  {
    String str = paramString + ":" + String.valueOf(paramInt2);
    if (this.cc.containsKey(str))
      throw new SshException(str + " has already been requested!", 4);
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(28);
      localByteArrayWriter.writeInt(paramInt1);
      localByteArrayWriter.writeString(paramString);
      localByteArrayWriter.writeInt(paramInt2);
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_CMSG_PORT_FORWARD_REQUEST");
      this.vb.d(localByteArrayWriter.toByteArray());
      if (this.vb.f())
      {
        this.cc.put(str, paramForwardingRequestListener);
        return true;
      }
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public SshTunnel b(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, int paramInt3, SshTransport paramSshTransport, ChannelEventListener paramChannelEventListener)
    throws SshException, ChannelOpenException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString1);
      localByteArrayWriter.writeInt(paramInt1);
      if ((this.vb.l & 0x2) != 0)
        localByteArrayWriter.writeString(paramString3 + ":" + String.valueOf(paramInt3));
      d locald = new d(this.vb.b, paramString1, paramInt1, paramString2, paramInt2, paramString3, paramInt3, 1, paramSshTransport);
      locald.addChannelEventListener(paramChannelEventListener);
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_MSG_PORT_OPEN");
      b(29, localByteArrayWriter.toByteArray(), locald);
      return locald;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 6);
  }

  protected void onThreadExit()
  {
  }

  class _b
  {
    _b()
    {
    }

    public e b(String paramString1, String paramString2, int paramInt, String paramString3)
      throws SshException
    {
      if (b.this.cc.containsKey(paramString1))
      {
        ForwardingRequestListener localForwardingRequestListener = (ForwardingRequestListener)b.this.cc.get(paramString1);
        int i = paramString1.indexOf(":");
        String str;
        int j;
        if (i > -1)
        {
          str = paramString1.substring(0, i);
          j = Integer.parseInt(paramString1.substring(i + 1));
        }
        else
        {
          str = "";
          j = Integer.parseInt(paramString1.substring(i + 1));
        }
        d locald = new d(b.this.vb.b, paramString2, paramInt, str, j, paramString3, -1, 3, localForwardingRequestListener.createConnection(paramString2, paramInt));
        int k = b.this.allocateChannel(locald);
        locald.b(b.this, k, 0L);
        localForwardingRequestListener.initializeTunnel(locald);
        return locald;
      }
      throw new SshException("Forwarding had not previously been requested", 6);
    }

    public e b(String paramString1, int paramInt, String paramString2)
      throws SshException
    {
      String str = paramString1 + ":" + String.valueOf(paramInt);
      if (b.this.cc.containsKey(str))
      {
        ForwardingRequestListener localForwardingRequestListener = (ForwardingRequestListener)b.this.cc.get(str);
        d locald = new d(b.this.vb.b, paramString1, paramInt, "127.0.0.1", paramInt, paramString2, -1, 2, localForwardingRequestListener.createConnection(paramString1, paramInt));
        int i = b.this.allocateChannel(locald);
        locald.b(b.this, i, 0L);
        localForwardingRequestListener.initializeTunnel(locald);
        return locald;
      }
      throw new SshException("Forwarding had not previously been requested", 6);
    }
  }

  class _c extends OutputStream
  {
    _c()
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
      if (b.this.vb.e() == 3)
        throw new SshIOException(new SshException("The session is closed!", 6));
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter(paramInt2 + 5);
      localByteArrayWriter.write(16);
      localByteArrayWriter.writeBinaryString(paramArrayOfByte, paramInt1, paramInt2);
      try
      {
        b.this.vb.d(localByteArrayWriter.toByteArray());
      }
      catch (SshException localSshException)
      {
        throw new EOFException();
      }
      if (b.this.xb != null)
        for (int i = 0; i < b.this.xb.size(); i++)
          ((ChannelEventListener)b.this.xb.elementAt(i)).dataSent(b.this, paramArrayOfByte, paramInt1, paramInt2);
    }

    public void close()
      throws IOException
    {
      try
      {
        b.this.vb.d(new byte[] { 19 });
      }
      catch (SshException localSshException)
      {
      }
    }
  }

  class _d extends InputStream
  {
    int c;
    SshMessage e;
    MessageObserver d;

    _d(int arg2)
    {
      int i;
      this.c = i;
      this.d = new MessageObserver(b.this)
      {
        public boolean wantsNotification(Message paramMessage)
        {
          return paramMessage.getMessageId() == b._d.this.c;
        }
      };
    }

    public int available()
      throws IOException
    {
      try
      {
        if (((this.e == null) || (this.e.available() == 0)) && (b.this.getGlobalMessages().hasMessage(this.d) != null))
          b();
        return this.e == null ? 0 : this.e.available();
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

    void b()
      throws SshException, EOFException
    {
      this.e = b.this.getGlobalMessages().nextMessage(this.d, b.this.zb);
      this.e.skip(4L);
    }

    public int read()
      throws IOException
    {
      try
      {
        if ((this.e == null) || (this.e.available() == 0))
          b();
        return this.e.read();
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

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      try
      {
        if ((this.e == null) || (this.e.available() == 0))
          b();
        return this.e.read(paramArrayOfByte, paramInt1, paramInt2);
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
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh1.b
 * JD-Core Version:    0.6.0
 */