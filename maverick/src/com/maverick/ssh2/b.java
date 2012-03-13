package com.maverick.ssh2;

import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.message.Message;
import com.maverick.ssh.message.MessageObserver;
import com.maverick.ssh.message.SshAbstractChannel;
import com.maverick.ssh.message.SshChannelMessage;
import com.maverick.ssh.message.SshMessage;
import com.maverick.ssh.message.SshMessageRouter;
import com.maverick.ssh.message.SshMessageStore;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class b extends SshMessageRouter
  implements TransportProtocolListener
{
  Object mb = new Object();
  static final MessageObserver kb = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 91:
      case 92:
        return true;
      }
      return false;
    }
  };
  static final MessageObserver lb = new MessageObserver()
  {
    public boolean wantsNotification(Message paramMessage)
    {
      switch (paramMessage.getMessageId())
      {
      case 81:
      case 82:
        return true;
      }
      return false;
    }
  };
  TransportProtocol nb;
  Hashtable ib = new Hashtable();
  Hashtable jb = new Hashtable();
  static Log ab = LogFactory.getLog(b.class);

  public b(TransportProtocol paramTransportProtocol, SshContext paramSshContext, boolean paramBoolean)
  {
    super(paramTransportProtocol, paramSshContext.getChannelLimit(), paramBoolean);
    this.nb = paramTransportProtocol;
    this.nb.addListener(this);
    paramTransportProtocol.b(new Runnable()
    {
      public void run()
      {
        b.this.stop();
      }
    });
  }

  public void b(ChannelFactory paramChannelFactory)
    throws SshException
  {
    String[] arrayOfString = paramChannelFactory.supportedChannelTypes();
    for (int i = 0; i < arrayOfString.length; i++)
    {
      if (this.ib.containsKey(arrayOfString[i]))
        throw new SshException(arrayOfString[i] + " channel is already registered!", 4);
      this.ib.put(arrayOfString[i], paramChannelFactory);
    }
  }

  public void b(GlobalRequestHandler paramGlobalRequestHandler)
    throws SshException
  {
    String[] arrayOfString = paramGlobalRequestHandler.supportedRequests();
    for (int i = 0; i < arrayOfString.length; i++)
    {
      if (this.jb.containsKey(arrayOfString[i]))
        throw new SshException(arrayOfString[i] + " request is already registered!", 4);
      this.jb.put(arrayOfString[i], paramGlobalRequestHandler);
    }
  }

  public boolean b(GlobalRequest paramGlobalRequest, boolean paramBoolean, long paramLong)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(80);
      localByteArrayWriter.writeString(paramGlobalRequest.getName());
      localByteArrayWriter.writeBoolean(paramBoolean);
      if (paramGlobalRequest.getData() != null)
        localByteArrayWriter.write(paramGlobalRequest.getData());
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_MSG_GLOBAL_REQUEST request=" + paramGlobalRequest.getName() + " wantreply=" + paramBoolean);
      b(localByteArrayWriter.toByteArray(), true);
      if (paramBoolean)
      {
        SshMessage localSshMessage = getGlobalMessages().nextMessage(lb, paramLong);
        if (localSshMessage.getMessageId() == 81)
        {
          if (ab.isDebugEnabled())
            ab.debug("Received SSH_MSG_REQUEST_SUCCESS request=" + paramGlobalRequest.getName());
          if (localSshMessage.available() > 0)
          {
            byte[] arrayOfByte = new byte[localSshMessage.available()];
            localSshMessage.read(arrayOfByte);
            paramGlobalRequest.setData(arrayOfByte);
          }
          else
          {
            paramGlobalRequest.setData(null);
          }
          return true;
        }
        if (ab.isDebugEnabled())
          ab.debug("Received SSH_MSG_REQUEST_FAILURE request=" + paramGlobalRequest.getName());
        return false;
      }
      return true;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public void b(Ssh2Channel paramSsh2Channel)
  {
    freeChannel(paramSsh2Channel);
  }

  public SshContext e()
  {
    return this.nb.sb;
  }

  public void b(Ssh2Channel paramSsh2Channel, byte[] paramArrayOfByte)
    throws SshException, ChannelOpenException
  {
    b(paramSsh2Channel, paramArrayOfByte, e().getMessageTimeout());
  }

  public void b(Ssh2Channel paramSsh2Channel, byte[] paramArrayOfByte, long paramLong)
    throws SshException, ChannelOpenException
  {
    try
    {
      int i = allocateChannel(paramSsh2Channel);
      if (i == -1)
        throw new ChannelOpenException("Maximum number of channels exceeded", 4);
      paramSsh2Channel.b(this, i);
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(90);
      localByteArrayWriter.writeString(paramSsh2Channel.getName());
      localByteArrayWriter.writeInt(paramSsh2Channel.getChannelId());
      localByteArrayWriter.writeInt(paramSsh2Channel.c());
      localByteArrayWriter.writeInt(paramSsh2Channel.b());
      if (paramArrayOfByte != null)
        localByteArrayWriter.write(paramArrayOfByte);
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_MSG_CHANNEL_OPEN type=" + paramSsh2Channel.getName() + " id=" + paramSsh2Channel.getChannelId() + " window=" + paramSsh2Channel.c() + " packet=" + paramSsh2Channel.b());
      this.nb.sendMessage(localByteArrayWriter.toByteArray(), true);
      if (ab.isDebugEnabled())
        ab.debug("sent transport message, getting message stores next message");
      SshMessage localSshMessage = paramSsh2Channel.getMessageStore().nextMessage(kb, paramLong);
      if (localSshMessage.getMessageId() == 92)
      {
        if (ab.isDebugEnabled())
          ab.debug("Received SSH_MSG_CHANNEL_OPEN_FAILURE id=" + paramSsh2Channel.getChannelId());
        freeChannel(paramSsh2Channel);
        j = (int)localSshMessage.readInt();
        throw new ChannelOpenException(localSshMessage.readString(), j);
      }
      int j = (int)localSshMessage.readInt();
      long l = localSshMessage.readInt();
      int k = (int)localSshMessage.readInt();
      byte[] arrayOfByte = new byte[localSshMessage.available()];
      localSshMessage.read(arrayOfByte);
      if (ab.isDebugEnabled())
        ab.debug("Received SSH_MSG_CHANNEL_OPEN_CONFIRMATION id=" + paramSsh2Channel.getChannelId() + " rid=" + j + " window=" + l + " packet=" + k);
      paramSsh2Channel.open(j, l, k, arrayOfByte);
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  protected void b(byte[] paramArrayOfByte, boolean paramBoolean)
    throws SshException
  {
    this.nb.sendMessage(paramArrayOfByte, paramBoolean);
  }

  protected SshMessage createMessage(byte[] paramArrayOfByte)
    throws SshException
  {
    if ((paramArrayOfByte[0] >= 91) && (paramArrayOfByte[0] <= 100))
      return new SshChannelMessage(paramArrayOfByte);
    return new SshMessage(paramArrayOfByte);
  }

  protected boolean processGlobalMessage(SshMessage paramSshMessage)
    throws SshException
  {
    try
    {
      String str;
      int i;
      switch (paramSshMessage.getMessageId())
      {
      case 90:
        str = paramSshMessage.readString();
        i = (int)paramSshMessage.readInt();
        int j = (int)paramSshMessage.readInt();
        int k = (int)paramSshMessage.readInt();
        byte[] arrayOfByte2 = paramSshMessage.available() > 0 ? new byte[paramSshMessage.available()] : null;
        paramSshMessage.read(arrayOfByte2);
        if (ab.isDebugEnabled())
          ab.debug("Received SSH_MSG_CHANNEL_OPEN rid=" + i + " window=" + j + " packet=" + k);
        b(str, i, j, k, arrayOfByte2);
        return true;
      case 80:
        str = paramSshMessage.readString();
        i = paramSshMessage.read() != 0 ? 1 : 0;
        byte[] arrayOfByte1 = new byte[paramSshMessage.available()];
        paramSshMessage.read(arrayOfByte1);
        if (ab.isDebugEnabled())
          ab.debug("Received SSH_MSG_GLOBAL_REQUEST request=" + str + " wantreply=" + i);
        b(str, i, arrayOfByte1);
        return true;
      }
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  void b(String paramString, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      if (this.ib.containsKey(paramString))
      {
        try
        {
          Ssh2Channel localSsh2Channel = ((ChannelFactory)this.ib.get(paramString)).createChannel(paramString, paramArrayOfByte);
          if (ab.isDebugEnabled())
            ab.debug("There are " + getChannelCount() + " channels open");
          int i = allocateChannel(localSsh2Channel);
          if (i > -1)
          {
            try
            {
              localSsh2Channel.b(this, i);
              byte[] arrayOfByte = localSsh2Channel.create();
              localByteArrayWriter.write(91);
              localByteArrayWriter.writeInt(paramInt1);
              localByteArrayWriter.writeInt(i);
              localByteArrayWriter.writeInt(localSsh2Channel.c());
              localByteArrayWriter.writeInt(localSsh2Channel.b());
              if (arrayOfByte != null)
                localByteArrayWriter.write(arrayOfByte);
              if (ab.isDebugEnabled())
                ab.debug("Sending SSH_MSG_CHANNEL_OPEN_CONFIRMATION type=" + localSsh2Channel.getName() + " id=" + localSsh2Channel.getChannelId() + " rid=" + paramInt1 + " window=" + localSsh2Channel.c() + " packet=" + localSsh2Channel.b());
              this.nb.sendMessage(localByteArrayWriter.toByteArray(), true);
              localSsh2Channel.open(paramInt1, paramInt2, paramInt3);
              return;
            }
            catch (SshException localSshException)
            {
              localByteArrayWriter.write(92);
              localByteArrayWriter.writeInt(paramInt1);
              localByteArrayWriter.writeInt(2);
              localByteArrayWriter.writeString(localSshException.getMessage());
              localByteArrayWriter.writeString("");
            }
          }
          else
          {
            localByteArrayWriter.write(92);
            localByteArrayWriter.writeInt(paramInt1);
            localByteArrayWriter.writeInt(4);
            localByteArrayWriter.writeString("Maximum allowable open channel limit of " + String.valueOf(maximumChannels()) + " exceeded!");
            localByteArrayWriter.writeString("");
          }
        }
        catch (ChannelOpenException localChannelOpenException)
        {
          localByteArrayWriter.write(92);
          localByteArrayWriter.writeInt(paramInt1);
          localByteArrayWriter.writeInt(localChannelOpenException.getReason());
          localByteArrayWriter.writeString(localChannelOpenException.getMessage());
          localByteArrayWriter.writeString("");
        }
      }
      else
      {
        localByteArrayWriter.write(92);
        localByteArrayWriter.writeInt(paramInt1);
        localByteArrayWriter.writeInt(3);
        localByteArrayWriter.writeString(paramString + " is not a supported channel type!");
        localByteArrayWriter.writeString("");
      }
      if (ab.isDebugEnabled())
        ab.debug("Sending SSH_MSG_CHANNEL_OPEN_FAILURE rid=" + paramInt1);
      this.nb.sendMessage(localByteArrayWriter.toByteArray(), true);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException.getMessage(), 5);
    }
  }

  void b(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      boolean bool = false;
      GlobalRequest localGlobalRequest = new GlobalRequest(paramString, paramArrayOfByte);
      if (this.jb.containsKey(paramString))
        bool = ((GlobalRequestHandler)this.jb.get(paramString)).processGlobalRequest(localGlobalRequest);
      if (paramBoolean)
        if (bool)
        {
          ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
          localByteArrayWriter.write(81);
          if (localGlobalRequest.getData() != null)
            localByteArrayWriter.write(localGlobalRequest.getData());
          if (ab.isDebugEnabled())
            ab.debug("Sending SSH_MSG_REQUEST_SUCCESS request=" + paramString);
          this.nb.sendMessage(localByteArrayWriter.toByteArray(), true);
        }
        else
        {
          this.nb.sendMessage(new byte[] { 82 }, true);
        }
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  protected void onThreadExit()
  {
    if (this.nb.isConnected())
      this.nb.disconnect(10, "Exiting");
    stop();
  }

  public void onDisconnect(String paramString, int paramInt)
  {
  }

  public void onIdle(long paramLong)
  {
    SshAbstractChannel[] arrayOfSshAbstractChannel = getActiveChannels();
    for (int i = 0; i < arrayOfSshAbstractChannel.length; i++)
      arrayOfSshAbstractChannel[i].idle();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.b
 * JD-Core Version:    0.6.0
 */