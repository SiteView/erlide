package com.maverick.sshd;

import com.maverick.nio.EventLog;
import com.maverick.nio.ForwardingManager;
import com.maverick.nio.SocketConnection;
import com.maverick.nio.WriteOperationRequest;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh2.GlobalRequest;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConnectionProtocol
  implements A
{
  TransportProtocol N;
  Channel[] J;
  static RemoteForwardingManager L;
  String M;
  Map K = Collections.synchronizedMap(new HashMap());

  public void init(TransportProtocol paramTransportProtocol)
    throws IOException
  {
    this.N = paramTransportProtocol;
    this.J = new Channel[paramTransportProtocol.getSshContext().getChannelLimit()];
    this.M = AuthenticationProvider.getUsername(getSessionIdentifier());
    ConnectionManager.getInstance().A(this);
  }

  public void addGlobalRequestHandler(GlobalRequestHandler paramGlobalRequestHandler)
  {
    if (paramGlobalRequestHandler != null)
    {
      String[] arrayOfString = paramGlobalRequestHandler.supportedRequests();
      for (int i = 0; i < arrayOfString.length; i++)
        this.K.put(arrayOfString[i], paramGlobalRequestHandler);
    }
  }

  public SocketAddress getRemoteAddress()
  {
    return this.N.getSocketConnection().getRemoteAddress();
  }

  public InetAddress getLocalAddress()
  {
    return this.N.getSocketConnection().getLocalAddress();
  }

  public int getLocalPort()
  {
    return this.N.getSocketConnection().getLocalPort();
  }

  public String getUsername()
  {
    return this.M;
  }

  public void stop()
  {
    ConnectionManager.getInstance().B(this);
    L.unregisterConnection(this);
    if (this.J != null)
      synchronized (this.J)
      {
        for (int i = 0; i < this.J.length; i++)
        {
          if (this.J[i] == null)
            continue;
          try
          {
            this.J[i].close();
          }
          catch (Throwable localThrowable)
          {
          }
          B(this.J[i]);
        }
      }
  }

  public byte[] getSessionIdentifier()
  {
    return this.N.getSessionIdentifier();
  }

  int A(Channel paramChannel)
  {
    synchronized (this.J)
    {
      for (int i = 0; i < this.J.length; i++)
      {
        if (this.J[i] != null)
          continue;
        this.J[i] = paramChannel;
        return i;
      }
    }
    return -1;
  }

  void B(Channel paramChannel)
  {
    synchronized (this.J)
    {
      if (paramChannel != null)
        this.J[paramChannel.getLocalId()] = null;
    }
  }

  public boolean openChannel(Channel paramChannel)
  {
    if (!this.N.isConnected())
      return false;
    paramChannel.A(this);
    if (getContext().getAccessManager() != null)
    {
      boolean bool = getContext().getAccessManager().canOpenChannel(getSessionIdentifier(), getUsername(), paramChannel);
      if (!bool)
        return false;
    }
    synchronized (paramChannel)
    {
      try
      {
        int i = A(paramChannel);
        if (i == -1)
          return false;
        this.N.postMessage(new _A(paramChannel, paramChannel.C(i)));
        return true;
      }
      catch (IOException localIOException)
      {
        return false;
      }
    }
  }

  boolean A()
  {
    return this.N.isConnected();
  }

  void A(SshMessage paramSshMessage)
  {
    this.N.postMessage(paramSshMessage);
  }

  public void disconnect()
  {
    A(11, "User Disconnected");
  }

  void A(int paramInt, String paramString)
  {
    this.N.disconnect(paramInt, paramString);
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    switch (paramArrayOfByte[0])
    {
    case 90:
      D(paramArrayOfByte);
      return true;
    case 91:
      I(paramArrayOfByte);
      return true;
    case 92:
      C(paramArrayOfByte);
      return true;
    case 98:
      G(paramArrayOfByte);
      return true;
    case 94:
      E(paramArrayOfByte);
      return true;
    case 95:
      E(paramArrayOfByte);
      return true;
    case 93:
      J(paramArrayOfByte);
      return true;
    case 96:
      F(paramArrayOfByte);
      return true;
    case 97:
      B(paramArrayOfByte);
      return true;
    case 80:
      K(paramArrayOfByte);
      return true;
    case 81:
    case 82:
    case 83:
    case 84:
    case 85:
    case 86:
    case 87:
    case 88:
    case 89:
    }
    return false;
  }

  void K(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    String str = localByteArrayReader.readString();
    int i = localByteArrayReader.read() != 0 ? 1 : 0;
    boolean bool = true;
    byte[] arrayOfByte1 = null;
    Object localObject;
    int j;
    if (str.equals("tcpip-forward"))
    {
      localObject = localByteArrayReader.readString();
      j = (int)localByteArrayReader.readInt();
      if (getContext().getAccessManager() != null)
        bool = getContext().getAccessManager().canListen(this.N.getSessionIdentifier(), this.M, (String)localObject, j);
      if (bool)
        bool = L.startRemoteForwarding((String)localObject, j, this);
      arrayOfByte1 = ByteArrayWriter.encodeInt(j);
    }
    else if (str.equals("cancel-tcpip-forward"))
    {
      localObject = localByteArrayReader.readString();
      j = (int)localByteArrayReader.readInt();
      bool = L.stopRemoteForwarding((String)localObject, j, getContext().getRemoteForwardingCancelKillsTunnels(), this);
      arrayOfByte1 = ByteArrayWriter.encodeInt(j);
    }
    else
    {
      localObject = getContext().getGlobalRequestHandler(str);
      if (localObject == null)
        localObject = (GlobalRequestHandler)this.K.get(str);
      if (localObject != null)
      {
        byte[] arrayOfByte2 = new byte[localByteArrayReader.available()];
        localByteArrayReader.read(arrayOfByte2);
        GlobalRequest localGlobalRequest = new GlobalRequest(str, arrayOfByte2);
        bool = ((GlobalRequestHandler)localObject).processGlobalRequest(localGlobalRequest, this);
      }
    }
    if (i != 0)
      if (bool)
        H(arrayOfByte1);
      else
        B();
  }

  void E(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    int i = localByteArrayReader.read();
    int j = (int)localByteArrayReader.readInt();
    Channel localChannel = A(j);
    if (localChannel == null)
      EventLog.LogEvent(this, "Channel data received with invalid channel id " + j);
    else
      try
      {
        if (i == 94)
          localChannel.A(localByteArrayReader.readBinaryString());
        else
          localChannel.A((int)localByteArrayReader.readInt(), localByteArrayReader.readBinaryString());
      }
      catch (IOException localIOException)
      {
        EventLog.LogEvent(this, "Error processing channel data", localIOException);
        this.N.disconnect(2, localIOException.getMessage());
      }
  }

  void J(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    int i = (int)localByteArrayReader.readInt();
    int j = (int)localByteArrayReader.readInt();
    Channel localChannel = A(i);
    if (localChannel == null)
      EventLog.LogEvent(this, "SSH_MSG_CHANNEL_WINDOW_ADJUST received with invalid channel id " + i);
    else
      localChannel.B(j);
  }

  void F(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    int i = (int)localByteArrayReader.readInt();
    Channel localChannel = A(i);
    if (localChannel == null)
      this.N.disconnect(2, "SSH_MSG_CHANNEL_EOF received with invalid channel id " + i);
    else
      localChannel.B();
  }

  Channel A(int paramInt)
  {
    synchronized (this.J)
    {
      return this.J[paramInt];
    }
  }

  void B(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    int i = (int)localByteArrayReader.readInt();
    Channel localChannel = A(i);
    if (localChannel == null)
      this.N.disconnect(2, "SSH_MSG_CHANNEL_CLOSE received with invalid channel id " + i);
    else
      localChannel.A();
  }

  void I(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    int i = (int)localByteArrayReader.readInt();
    Channel localChannel = A(i);
    if (localChannel == null)
    {
      this.N.disconnect(2, "Invalid channel id " + i + " in SSH_MSG_CHANNEL_OPEN_CONFIRMATION");
      return;
    }
    int j = (int)localByteArrayReader.readInt();
    int k = (int)localByteArrayReader.readInt();
    int m = (int)localByteArrayReader.readInt();
    byte[] arrayOfByte = null;
    if (localByteArrayReader.available() > 0)
    {
      arrayOfByte = new byte[localByteArrayReader.available()];
      localByteArrayReader.read(arrayOfByte);
    }
    synchronized (localChannel)
    {
      localChannel.A(j, k, m);
    }
  }

  void C(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    int i = (int)localByteArrayReader.readInt();
    Channel localChannel = A(i);
    if (localChannel == null)
    {
      this.N.disconnect(2, "Invalid channel id " + i + " in SSH_MSG_CHANNEL_OPEN_FAILURE");
      return;
    }
    synchronized (localChannel)
    {
      localChannel.F();
      B(localChannel);
    }
  }

  void D(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    String str = localByteArrayReader.readString();
    int i = (int)localByteArrayReader.readInt();
    int j = (int)localByteArrayReader.readInt();
    int k = (int)localByteArrayReader.readInt();
    byte[] arrayOfByte = null;
    if (localByteArrayReader.available() > 0)
    {
      arrayOfByte = new byte[localByteArrayReader.available()];
      localByteArrayReader.read(arrayOfByte);
    }
    try
    {
      Channel localChannel = (Channel)this.N.getSshContext().supportedChannels().getInstance(str);
      localChannel.A(this);
      if (getContext().getAccessManager() != null)
      {
        boolean bool = getContext().getAccessManager().canOpenChannel(getSessionIdentifier(), getUsername(), localChannel);
        if (!bool)
        {
          this.N.postMessage(new _C(i, 1, "You do not have permission to open the " + localChannel.getChannelType() + " channel"));
          return;
        }
      }
      int m = A(localChannel);
      if (m > -1)
        try
        {
          sendChannelOpenConfirmation(localChannel, localChannel.A(m, i, k, j, arrayOfByte));
          localChannel.onChannelOpen();
          return;
        }
        catch (ChannelOpenException localChannelOpenException)
        {
          this.N.postMessage(new _C(i, localChannelOpenException.getReason(), localChannelOpenException.getMessage()));
        }
        catch (WriteOperationRequest localWriteOperationRequest)
        {
        }
      else
        this.N.postMessage(new _C(i, 4, "Maximum number of open channels exceeded"));
    }
    catch (SshException localSshException)
    {
      this.N.postMessage(new _C(i, 3, "Unknown channel type" + str));
    }
  }

  public void sendGlobalRequest(GlobalRequest paramGlobalRequest)
  {
    this.N.postMessage(new _D(paramGlobalRequest));
  }

  public int getQueueSize()
  {
    return this.N.getQueueSizes();
  }

  public void sendChannelOpenConfirmation(Channel paramChannel, byte[] paramArrayOfByte)
  {
    this.N.postMessage(new _E(paramChannel, paramArrayOfByte));
    paramChannel.C();
  }

  public void sendChannelOpenFailure(Channel paramChannel, int paramInt, String paramString)
  {
    this.N.postMessage(new _C(paramChannel.getRemoteId(), paramInt, paramString));
    B(paramChannel);
  }

  void H(byte[] paramArrayOfByte)
  {
    this.N.postMessage(new _F(paramArrayOfByte));
  }

  void B()
  {
    this.N.postMessage(new _B());
  }

  void G(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    int i = (int)localByteArrayReader.readInt();
    String str = localByteArrayReader.readString();
    boolean bool = localByteArrayReader.read() != 0;
    byte[] arrayOfByte = null;
    if (localByteArrayReader.available() > 0)
    {
      arrayOfByte = new byte[localByteArrayReader.available()];
      localByteArrayReader.read(arrayOfByte);
    }
    Channel localChannel = A(i);
    if (localChannel != null)
      localChannel.onChannelRequest(str, bool, arrayOfByte);
    else
      this.N.disconnect(2, "Request for non-existent channel id " + String.valueOf(i));
  }

  public SshContext getContext()
  {
    return this.N.getSshContext();
  }

  public TransportProtocol getTransport()
  {
    return this.N;
  }

  public void start()
  {
    try
    {
      L = (RemoteForwardingManager)ForwardingManager.getInstance(this.N.getSshContext().getRemoteForwardingManagerImpl());
      L.registerConnection(this, this.N.getSshContext().getRemoteForwardingFactoryImpl());
    }
    catch (IOException localIOException)
    {
      EventLog.LogEvent(this, "Could not start Connection protocol.. disconnecting", localIOException);
      this.N.disconnect(7, localIOException.getMessage());
    }
  }

  class _C
    implements SshMessage
  {
    int X;
    int W;
    String Y;

    _C(int paramInt1, int paramString, String arg4)
    {
      this.X = paramInt1;
      this.W = paramString;
      Object localObject;
      this.Y = localObject;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(92);
      paramByteBuffer.putInt(this.X);
      paramByteBuffer.putInt(this.W);
      paramByteBuffer.putInt(this.Y.length());
      paramByteBuffer.put(this.Y.getBytes());
      paramByteBuffer.putInt(0);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _E
    implements SshMessage
  {
    Channel _;
    byte[] a;

    _E(Channel paramArrayOfByte, byte[] arg3)
    {
      this._ = paramArrayOfByte;
      Object localObject;
      this.a = localObject;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(91);
      paramByteBuffer.putInt(this._.A);
      paramByteBuffer.putInt(this._.getLocalId());
      paramByteBuffer.putInt(this._.localwindow);
      paramByteBuffer.putInt(this._.getLocalPacket());
      if (this.a != null)
        paramByteBuffer.put(this.a);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _A
    implements SshMessage
  {
    Channel V;
    byte[] U;

    _A(Channel paramArrayOfByte, byte[] arg3)
    {
      this.V = paramArrayOfByte;
      Object localObject;
      this.U = localObject;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(90);
      paramByteBuffer.putInt(this.V.getChannelType().length());
      paramByteBuffer.put(this.V.getChannelType().getBytes());
      paramByteBuffer.putInt(this.V.getLocalId());
      paramByteBuffer.putInt(this.V.localwindow);
      paramByteBuffer.putInt(this.V.getLocalPacket());
      if (this.U != null)
        paramByteBuffer.put(this.U);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _F
    implements SshMessage
  {
    byte[] b;

    _F(byte[] arg2)
    {
      Object localObject;
      this.b = localObject;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(81);
      if (this.b != null)
        paramByteBuffer.put(this.b);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _B
    implements SshMessage
  {
    _B()
    {
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(82);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _D
    implements SshMessage
  {
    GlobalRequest Z;

    _D(GlobalRequest arg2)
    {
      Object localObject;
      this.Z = localObject;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(80);
      paramByteBuffer.putInt(this.Z.getName().length());
      paramByteBuffer.put(this.Z.getName().getBytes());
      paramByteBuffer.put(0);
      if (this.Z.getData() != null)
        paramByteBuffer.put(this.Z.getData());
      return true;
    }

    public void messageSent()
    {
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.ConnectionProtocol
 * JD-Core Version:    0.6.0
 */