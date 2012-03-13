package com.maverick.sshd;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.nio.EventLog;
import com.maverick.nio.IdleStateListener;
import com.maverick.nio.IdleStateManager;
import com.maverick.nio.ProtocolEngine;
import com.maverick.nio.SocketConnection;
import com.maverick.nio.SocketWriteCallback;
import com.maverick.nio.WriteOperationRequest;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshHmac;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.compression.SshCompression;
import com.maverick.sshd.components.ServerComponentManager;
import com.maverick.sshd.components.SshKeyExchangeServer;
import com.maverick.sshd.events.EventServiceImplementation;
import com.maverick.sshd.events.SSHDEvent;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

public class TransportProtocol
  implements ProtocolEngine, IdleStateListener
{
  public static String CHARSET_ENCODING = "UTF-8";
  SecureRandom T;
  byte[] Ç;
  String o = "SSH-2.0-";
  StringBuffer i = new StringBuffer();
  boolean w = false;
  boolean º = false;
  byte[] j;
  byte[] Æ;
  byte[] µ;
  LinkedList À = new LinkedList();
  LinkedList Â = new LinkedList();
  LinkedList e = new LinkedList();
  A h;
  Vector z = new Vector();
  boolean _ = true;
  int m = 0;
  byte[] Y;
  int f = 0;
  int n;
  int Á;
  int c;
  int q;
  long v = System.currentTimeMillis();
  public static final int NEGOTIATING_PROTOCOL = 1;
  public static final int PERFORMING_KEYEXCHANGE = 2;
  public static final int CONNECTED = 3;
  public static final int DISCONNECTED = 4;
  int g;
  SshKeyExchangeServer Å;
  SshCipher Ã;
  SshCipher U;
  SshHmac R;
  SshHmac S;
  SshCompression r;
  SshCompression k;
  String W;
  String s;
  String u;
  String X;
  String b;
  String x;
  long ¤ = 0L;
  long É = 0L;
  long ª = 0L;
  long d = 0L;
  Object Ä = new Object();
  boolean a = false;
  boolean Z = false;
  public static final int HOST_NOT_ALLOWED = 1;
  public static final int PROTOCOL_ERROR = 2;
  public static final int KEY_EXCHANGE_FAILED = 3;
  public static final int RESERVED = 4;
  public static final int MAC_ERROR = 5;
  public static final int COMPRESSION_ERROR = 6;
  public static final int SERVICE_NOT_AVAILABLE = 7;
  public static final int PROTOCOL_VERSION_NOT_SUPPORTED = 8;
  public static final int HOST_KEY_NOT_VERIFIABLE = 9;
  public static final int CONNECTION_LOST = 10;
  public static final int BY_APPLICATION = 11;
  public static final int TOO_MANY_CONNECTIONS = 12;
  public static final int AUTH_CANCELLED_BY_USER = 13;
  public static final int NO_MORE_AUTH_METHODS_AVAILABLE = 14;
  public static final int ILLEGAL_USER_NAME = 15;
  _B È;
  long Ê = 0L;
  SshContext £;
  SocketConnection y;
  public static int numberOfConnections = 0;
  public static Object lock = new Object();
  boolean t = false;
  int l = 8;
  int V = 0;
  int ¥ = 0;
  int p = 0;
  int ¢ = 0;

  public TransportProtocol(SshContext paramSshContext)
  {
    this.£ = paramSshContext;
    this.È = new _B();
  }

  public SocketConnection getSocketConnection()
  {
    return this.y;
  }

  public void addEventListener(TransportProtocolListener paramTransportProtocolListener)
  {
    if (paramTransportProtocolListener != null)
      this.z.add(paramTransportProtocolListener);
  }

  public SocketAddress getRemoteAddress()
  {
    return this.y.getRemoteAddress();
  }

  public int getRemotePort()
  {
    return this.y.getPort();
  }

  public SshContext getContext()
  {
    return this.£;
  }

  public void onSocketConnect(SocketConnection paramSocketConnection)
  {
    this.y = paramSocketConnection;
    this.y.getIdleStates().register(this);
    if (this.£.getAccessManager() != null)
    {
      boolean bool = this.£.getAccessManager().canConnect(paramSocketConnection.getRemoteAddress());
      if (!bool)
      {
        EventLog.LogEvent(this, "Access denied based up remote client IP");
        paramSocketConnection.closeConnection();
        return;
      }
    }
    if ((this.£.getMaximumConnections() > -1) && (numberOfConnections >= this.£.getMaximumConnections()))
    {
      this.t = true;
      if (!this.£.getAllowDeniedKEX())
      {
        EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 101, false).addAttribute("NUMBER_OF_CONNECTIONS", "" + numberOfConnections));
        EventLog.LogEvent(this, "Denying connection.. too many users currently online");
        paramSocketConnection.closeConnection();
        return;
      }
    }
    synchronized (lock)
    {
      if (!this.t)
      {
        numberOfConnections += 1;
        EventLog.LogEvent(this, "There " + (numberOfConnections > 1 ? "are" : "is") + " now " + numberOfConnections + " active connections");
      }
    }
    EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 99, true).addAttribute("IP", ((InetSocketAddress)getRemoteAddress()).getAddress().getHostAddress()));
    this.T = new SecureRandom();
    this.o = (this.o + this.£.getSoftwareVersionComments() + "\r\n");
    this.Ç = new byte[this.£.getMaximumPacketLength()];
    D(1);
    if (!this.º)
      postMessage(new SshMessage()
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          paramByteBuffer.put(TransportProtocol.this.o.getBytes());
          return true;
        }

        public void messageSent()
        {
          TransportProtocol.this.º = true;
          if (TransportProtocol.this.w)
            TransportProtocol.this.G();
        }
      });
  }

  public boolean onSocketRead(ByteBuffer paramByteBuffer)
  {
    boolean bool = false;
    try
    {
      switch (this.g)
      {
      case 1:
        A(paramByteBuffer);
        break;
      case 2:
      case 3:
        bool = B(paramByteBuffer);
      }
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "Connection closed on socket read: " + localThrowable.getMessage());
      this.y.closeConnection();
    }
    return bool;
  }

  public boolean isConnected()
  {
    return (this.g == 1) || (this.g == 2) || (this.g == 3);
  }

  void A(ByteBuffer paramByteBuffer)
    throws IOException
  {
    if (this.w)
    {
      B(paramByteBuffer);
      return;
    }
    char c1 = '\000';
    while (paramByteBuffer.remaining() > 0)
    {
      c1 = (char)paramByteBuffer.get();
      if (c1 == '\n')
        break;
      this.i.append(c1);
    }
    if (c1 == '\n')
    {
      String str = this.i.toString();
      if ((!str.startsWith("SSH-2.0-")) && (!str.startsWith("SSH-1.99-")))
      {
        EventLog.LogEvent(this, "Remote client reported an invalid protocol version!");
        this.y.closeConnection();
        return;
      }
      this.w = true;
      if (this.º)
      {
        G();
        B(paramByteBuffer);
      }
    }
  }

  boolean B(ByteBuffer paramByteBuffer)
  {
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    try
    {
      while ((isConnected()) && (((this._) && (paramByteBuffer.remaining() > this.l)) || ((this.m > 0) && (paramByteBuffer.hasRemaining()) && (i1 == 0))))
      {
        synchronized (this.Ä)
        {
          if (this._)
          {
            paramByteBuffer.get(this.Ç, this.f, this.l);
            if (this.U != null)
              this.U.transform(this.Ç, this.f, this.Ç, this.f, this.l);
            this.¥ = (int)ByteArrayReader.readInt(this.Ç, this.f);
            if (this.¥ <= 0)
              throw new IOException("Client sent invalid message length of " + this.¥ + "!");
            if ((this.¥ + 4 < 0) || (this.¥ + 4 > this.£.getMaximumPacketLength()))
            {
              disconnect(2, "Incoming packet length " + this.¥ + (this.¥ + 4 < 0 ? " is too small" : new StringBuffer().append(" exceeds maximum supported length of ").append(this.£.getMaximumPacketLength()).toString()));
              return true;
            }
            this.p = (this.Ç[4] & 0xFF);
            this.¢ = (this.¥ - (this.l - 4));
            this.m = (this.¢ + this.V);
            this.Y = new byte[this.¥ - this.p - 1];
            this._ = false;
            this.f += this.l;
          }
          if ((!this._) && (paramByteBuffer.remaining() > 0))
          {
            int i4 = this.m > paramByteBuffer.remaining() ? paramByteBuffer.remaining() : this.m;
            paramByteBuffer.get(this.Ç, this.f, i4);
            this.m -= i4;
            this.f += i4;
            if (this.m == 0)
            {
              i3 = this.¥ + 4;
              if (this.U != null)
                this.U.transform(this.Ç, this.l, this.Ç, this.l, this.¢);
              if ((this.S != null) && (!this.S.verify(this.É, this.Ç, 0, i3, this.Ç, i3)))
                throw new IOException("Corrupt Mac on input");
              System.arraycopy(this.Ç, 5, this.Y, 0, this.¥ - this.p - 1);
              if (this.k != null)
                this.Y = this.k.uncompress(this.Y, 0, this.Y.length);
              i2 = 1;
            }
          }
        }
        if (i2 == 0)
          continue;
        try
        {
          A(this.Y, this.É);
        }
        catch (WriteOperationRequest localWriteOperationRequest)
        {
          i1 = 1;
        }
        finally
        {
          if (++this.É >= 4294967296L)
            this.É = 0L;
          this.d += i3;
          this.c += i3;
          this.q += 1;
          if ((this.c >= getContext().getKeyExchangeTransferLimit()) || (this.q >= getContext().getKeyExchangePacketLimit()))
            G();
          this._ = true;
          this.m = 0;
          this.f = 0;
          i2 = 0;
        }
      }
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "Connection Error", localThrowable);
      if (isConnected())
        disconnect(2, "Failed to read binary packet data!");
      i1 = 1;
    }
    return i1;
  }

  public boolean wantsToWrite()
  {
    synchronized (this.Ä)
    {
      if (this.g == 2)
        return this.Â.size() > 0;
      return (this.À.size() > 0) || (this.Â.size() > 0);
    }
  }

  public int getQueueSizes()
  {
    synchronized (this.Ä)
    {
      return this.À.size() + this.Â.size();
    }
  }

  public boolean idle()
  {
    if (this.g == 4)
      return true;
    long l1 = (System.currentTimeMillis() - this.v) / 1000L;
    if (((this.g == 3) || (this.g == 2)) && (getContext().getKeepAliveInterval() > 0) && (l1 > getContext().getKeepAliveInterval()))
    {
      long l2 = getContext().getKeepAliveInterval() + 1;
      if (this.Ê > 0L)
        l2 = (System.currentTimeMillis() - this.Ê) / 1000L;
      if (l2 > getContext().getKeepAliveInterval())
      {
        postMessage(this.È);
        this.Ê = System.currentTimeMillis();
      }
    }
    if ((getContext().getIdleConnectionTimeoutSeconds() > 0) && (l1 > getContext().getIdleConnectionTimeoutSeconds()))
    {
      disconnect(11, "Idle connection");
      return true;
    }
    return false;
  }

  private void H()
  {
    this.v = System.currentTimeMillis();
    if ((getContext().getIdleConnectionTimeoutSeconds() > 0) && (this.y != null))
      this.y.getIdleStates().reset(this);
  }

  public void sendMessage(SshMessage paramSshMessage)
  {
    postMessage(paramSshMessage);
  }

  public SocketWriteCallback onSocketWrite(ByteBuffer paramByteBuffer)
  {
    synchronized (this.Ä)
    {
      try
      {
        SshMessage localSshMessage;
        if ((this.Â.size() > 0) || (this.À.size() > 0))
        {
          if (this.g == 2)
          {
            if (this.Â.size() > 0)
            {
              localSshMessage = (SshMessage)this.Â.getFirst();
              if (localSshMessage.writeMessageIntoBuffer(paramByteBuffer))
                this.Â.removeFirst();
            }
            else
            {
              return null;
            }
          }
          else
            synchronized (this.À)
            {
              localSshMessage = (SshMessage)this.À.getFirst();
              if (localSshMessage.writeMessageIntoBuffer(paramByteBuffer))
                this.À.removeFirst();
            }
          if (this.g != 1)
          {
            paramByteBuffer.flip();
            ??? = new byte[paramByteBuffer.remaining()];
            paramByteBuffer.get(???);
            paramByteBuffer.clear();
            int i1 = 4;
            int i2 = 8;
            if (this.Ã != null)
              i2 = this.Ã.getBlockSize();
            if (this.r != null)
              ??? = this.r.compress(???, 0, ???.length);
            i1 += (i2 - (???.length + 5 + i1) % i2) % i2;
            paramByteBuffer.putInt(???.length + 1 + i1);
            paramByteBuffer.put((byte)i1);
            paramByteBuffer.put(???, 0, ???.length);
            this.ª += ???.length + i1 + 5;
            byte[] arrayOfByte1 = new byte[i1];
            this.T.nextBytes(arrayOfByte1);
            paramByteBuffer.put(arrayOfByte1);
            paramByteBuffer.flip();
            byte[] arrayOfByte2 = new byte[paramByteBuffer.remaining()];
            paramByteBuffer.get(arrayOfByte2);
            byte[] arrayOfByte3 = null;
            if (this.R != null)
            {
              arrayOfByte3 = new byte[this.R.getMacLength()];
              this.R.generate(this.¤, arrayOfByte2, 0, arrayOfByte2.length, arrayOfByte3, 0);
            }
            if (this.Ã != null)
              this.Ã.transform(arrayOfByte2);
            paramByteBuffer.clear();
            paramByteBuffer.put(arrayOfByte2);
            if (arrayOfByte3 != null)
            {
              paramByteBuffer.put(arrayOfByte3);
              this.ª += arrayOfByte3.length;
            }
            this.n += paramByteBuffer.position();
            this.Á += 1;
            this.¤ += 1L;
            if (this.¤ >= 4294967296L)
              this.¤ = 0L;
          }
        }
        else
        {
          localSshMessage = null;
        }
        if ((this.n >= getContext().getKeyExchangeTransferLimit()) || (this.Á >= getContext().getKeyExchangePacketLimit()))
          G();
        return new SocketWriteCallback(localSshMessage)
        {
          public void completedWrite()
          {
            if (this.val$msg != null)
              this.val$msg.messageSent();
          }
        };
      }
      catch (Throwable localThrowable)
      {
        EventLog.LogEvent(this, "Connection closed on socket write: " + localThrowable.getMessage());
        this.y.closeConnection();
        return null;
      }
    }
  }

  public int getState()
  {
    return this.g;
  }

  public InetAddress getLocalAddress()
  {
    return this.y.getLocalAddress();
  }

  public int getLocalPort()
  {
    return this.y.getLocalPort();
  }

  public String getRemoteIdentification()
  {
    return this.i.toString();
  }

  public byte[] getSessionIdentifier()
  {
    return this.µ;
  }

  public void disconnect(int paramInt, String paramString)
  {
    if (paramString == null)
      paramString = "Failure";
    postMessage(new _A(paramInt, paramString));
  }

  public void onSocketClose()
  {
    D(4);
    EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 20, true).addAttribute("SESSION_ID", getSessionIdentifier()));
    if (this.µ != null)
      ConnectionManager.getInstance().A(this);
    if (this.y != null)
      this.y.getIdleStates().remove(this);
    if (this.h != null)
      this.h.stop();
    if (this.µ != null)
    {
      localObject1 = getSshContext().getAuthenticationProvider();
      if (localObject1 != null)
        ((AuthenticationProvider)localObject1).logoff(this.µ, this.y.getRemoteAddress());
    }
    Object localObject1 = this.z.iterator();
    while (((Iterator)localObject1).hasNext())
      ((TransportProtocolListener)((Iterator)localObject1).next()).onDisconnect(this);
    synchronized (lock)
    {
      if (!this.t)
      {
        numberOfConnections -= 1;
        EventLog.LogEvent(this, "There " + (numberOfConnections == 1 ? "are" : "is") + " now " + numberOfConnections + " active " + (numberOfConnections == 1 ? "connection" : "connections"));
      }
    }
  }

  public SecureRandom getRND()
  {
    return this.T;
  }

  void D(int paramInt)
  {
    this.g = paramInt;
  }

  void B(byte[] paramArrayOfByte)
    throws IOException, WriteOperationRequest
  {
    try
    {
      this.g = 2;
      G();
      this.Æ = paramArrayOfByte;
      ByteArrayReader localByteArrayReader = new ByteArrayReader(this.Æ, 0, this.Æ.length);
      localByteArrayReader.skip(17L);
      String str1 = A("key exchange", localByteArrayReader.readString());
      String str2 = A("public key", localByteArrayReader.readString());
      String str3 = A("client->server cipher", localByteArrayReader.readString());
      String str4 = A("server->client cipher", localByteArrayReader.readString());
      String str5 = localByteArrayReader.readString();
      String str6 = localByteArrayReader.readString();
      String str7 = localByteArrayReader.readString();
      String str8 = localByteArrayReader.readString();
      String str9 = localByteArrayReader.readString();
      str9 = localByteArrayReader.readString();
      boolean bool1 = localByteArrayReader.read() != 0;
      String str10 = this.£.supportedKeyExchanges().list(this.£.getPreferredKeyExchange());
      String str11 = B(str1, str10);
      this.Å = ((SshKeyExchangeServer)this.£.supportedKeyExchanges().getInstance(str11));
      String str12 = B(str2, this.£.getSupportedPublicKeys());
      SshKeyPair localSshKeyPair = this.£.getHostKey(str12);
      boolean bool2 = (str1.startsWith(this.£.getPreferredKeyExchange())) && (str2.startsWith(this.£.getPreferredPublicKey()));
      EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 3, true).addAttribute("REMOTE_KEY_EXCHANGES", str1).addAttribute("LOCAL_KEY_EXCHANGES", this.£.supportedKeyExchanges().list(this.£.getPreferredKeyExchange())).addAttribute("REMOTE_PUBLICKEYS", str2).addAttribute("LOCAL_PUBLICKEYS", this.£.getSupportedPublicKeys()).addAttribute("REMOTE_CIPHERS_CS", str3).addAttribute("LOCAL_CIPHERS_CS", this.£.supportedCiphersCS().list(this.£.getPreferredCipherCS())).addAttribute("REMOTE_CIPHERS_SC", str4).addAttribute("LOCAL_CIPHERS_SC", this.£.supportedCiphersSC().list(this.£.getPreferredCipherSC())).addAttribute("REMOTE_CS_MACS", str5).addAttribute("LOCAL_CS_MACS", this.£.supportedMacsCS().list(this.£.getPreferredMacCS())).addAttribute("REMOTE_SC_MACS", str6).addAttribute("LOCAL_SC_MACS", this.£.supportedMacsSC().list(this.£.getPreferredMacSC())).addAttribute("REMOTE_CS_COMPRESSIONS", str7).addAttribute("LOCAL_CS_COMPRESSIONS", this.£.supportedCompressionsCS().list(this.£.getPreferredCompressionCS())).addAttribute("REMOTE_SC_COMPRESSIONS", str8).addAttribute("LOCAL_SC_COMPRESSIONS", this.£.supportedCompressionsSC().list(this.£.getPreferredCompressionSC())));
      this.Å.init(this, this.i.toString().trim(), this.o.trim(), this.Æ, this.j, localSshKeyPair.getPrivateKey(), localSshKeyPair.getPublicKey(), bool1, bool2);
      this.W = B(A("client->server cipher list", str3), this.£.supportedCiphersCS().list(this.£.getPreferredCipherCS()));
      this.s = B(A("server->client cipher list", str4), this.£.supportedCiphersSC().list(this.£.getPreferredCipherSC()));
      this.u = B(A("client->server hmac list", str5), this.£.supportedMacsCS().list(this.£.getPreferredMacCS()));
      this.X = B(A("server->client hmac list", str6), this.£.supportedMacsSC().list(this.£.getPreferredMacSC()));
      this.b = B(A("client->server compression list", str7), this.£.supportedCompressionsCS().list(this.£.getPreferredCompressionCS()));
      this.x = B(A("server->client compression list", str8), this.£.supportedCompressionsSC().list(this.£.getPreferredCompressionSC()));
      if (this.t)
      {
        EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 4, true));
        disconnect(12, this.£.getTooManyConnectionsText());
        throw new WriteOperationRequest();
      }
      EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 5, true).addAttribute("USING_PUBLICKEY", str12).addAttribute("USING_KEY_EXCHANGE", str11).addAttribute("USING_CS_CIPHER", this.W).addAttribute("USING_SC_CIPHERC", this.s).addAttribute("USING_CS_MAC", this.u).addAttribute("USING_SC_MAC", this.X).addAttribute("USING_CS_COMPRESSION", this.b).addAttribute("USING_SC_COMPRESSION", this.x));
    }
    catch (SshException localSshException)
    {
      EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 4, true));
      throw new IOException("Unexpected protocol termination: " + localSshException.getMessage());
    }
  }

  void C(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    String str = localByteArrayReader.readString();
    if (str.equals("ssh-userauth"))
    {
      this.h = new AuthenticationProtocol();
      this.h.init(this);
      postMessage(new SshMessage(str)
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          paramByteBuffer.put(6);
          paramByteBuffer.putInt(this.val$servicename.length());
          paramByteBuffer.put(this.val$servicename.getBytes());
          return true;
        }

        public void messageSent()
        {
          TransportProtocol.this.h.start();
        }
      });
    }
    else
    {
      disconnect(7, str + " is not a valid service.");
    }
  }

  void A(A paramA)
  {
    try
    {
      this.h.stop();
      this.h = paramA;
      paramA.init(this);
      paramA.start();
    }
    catch (IOException localIOException)
    {
      EventLog.LogEvent(this, "Failed to start the service", localIOException);
      disconnect(7, "Failed to start the service");
    }
  }

  void A(byte[] paramArrayOfByte, long paramLong)
    throws SshException, IOException, WriteOperationRequest
  {
    H();
    if (paramArrayOfByte.length < 1)
    {
      EventLog.LogEvent(this, "Invalid transport protocol message");
      throw new IOException("Invalid transport protocol message");
    }
    switch (paramArrayOfByte[0])
    {
    case 1:
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
      localByteArrayReader.skip(5L);
      this.y.closeConnection();
      break;
    case 2:
      EventLog.LogEvent(this, "Processing SSH_MSG_IGNORE");
      break;
    case 4:
      EventLog.LogEvent(this, "Processing SSH_MSG_DEBUG");
      break;
    case 21:
      EventLog.LogEvent(this, "Processing SSH_MSG_NEWKEYS");
      synchronized (this.Å)
      {
        this.Å.setReceivedNewKeys(true);
        if (!I())
          throw new WriteOperationRequest();
      }
      break;
    case 20:
      B(paramArrayOfByte);
      break;
    case 5:
      EventLog.LogEvent(this, "Processing SSH_MSG_SERVICE_REQUEST");
      C(paramArrayOfByte);
      break;
    case 3:
      ??? = new ByteArrayReader(paramArrayOfByte);
      ((ByteArrayReader)???).skip(1L);
      EventLog.LogEvent(this, "Processing SSH_MSG_UNIMPLEMENTED for sequence " + ((ByteArrayReader)???).readInt());
      break;
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    case 16:
    case 17:
    case 18:
    case 19:
    default:
      if (((this.g == 2) && (this.Å.processMessage(paramArrayOfByte))) || ((this.h != null) && (this.h.processMessage(paramArrayOfByte))))
        break;
      EventLog.LogEvent(this, "Unimplemented Message " + String.valueOf(paramArrayOfByte[0]) + " received");
      postMessage(new _C(paramLong));
    }
  }

  public void sendNewKeys()
  {
    postMessage(new SshMessage()
    {
      public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
      {
        paramByteBuffer.put(21);
        return true;
      }

      public void messageSent()
      {
        synchronized (TransportProtocol.this.Å)
        {
          TransportProtocol.this.Å.setSentNewKeys(true);
          TransportProtocol.this.I();
        }
      }
    }
    , true);
  }

  boolean I()
  {
    if ((this.Å.hasSentNewKeys()) && (this.Å.hasReceivedNewKeys()))
      synchronized (this.Ä)
      {
        try
        {
          if (this.µ == null)
          {
            this.µ = this.Å.getExchangeHash();
            ConnectionManager.getInstance().B(this);
          }
          this.Ã = ((SshCipher)this.£.supportedCiphersSC().getInstance(this.s));
          this.U = ((SshCipher)this.£.supportedCiphersCS().getInstance(this.W));
          this.R = ((SshHmac)this.£.supportedMacsSC().getInstance(this.X));
          this.S = ((SshHmac)this.£.supportedMacsCS().getInstance(this.u));
          this.r = null;
          if (!this.x.equals("none"))
          {
            this.r = ((SshCompression)this.£.supportedCompressionsSC().getInstance(this.x));
            this.r.init(1, getSshContext().getCompressionLevel());
          }
          this.k = null;
          if (!this.b.equals("none"))
          {
            this.k = ((SshCompression)this.£.supportedCompressionsCS().getInstance(this.b));
            this.k.init(0, getSshContext().getCompressionLevel());
          }
          this.Ã.init(0, A('B'), A('D'));
          this.R.init(A('F'));
          this.U.init(1, A('A'), A('C'));
          this.S.init(A('E'));
          this.l = this.U.getBlockSize();
          this.V = this.S.getMacLength();
          D(3);
          this.j = null;
          this.Æ = null;
          return true;
        }
        catch (Throwable localThrowable)
        {
          EventLog.LogEvent(this, "Failed to create transport component", localThrowable);
          disconnect(2, "Failed to create a transport component! " + localThrowable.getMessage());
        }
      }
    return false;
  }

  public SshContext getSshContext()
  {
    return this.£;
  }

  String B(String paramString1, String paramString2)
    throws IOException
  {
    Vector localVector = new Vector();
    int i1;
    while ((i1 = paramString2.indexOf(",")) > -1)
    {
      localVector.addElement(paramString2.substring(0, i1).trim());
      paramString2 = paramString2.substring(i1 + 1).trim();
    }
    localVector.addElement(paramString2);
    while ((i1 = paramString1.indexOf(",")) > -1)
    {
      String str = paramString1.substring(0, i1).trim();
      if (localVector.contains(str))
        return str;
      paramString1 = paramString1.substring(i1 + 1).trim();
    }
    if (localVector.contains(paramString1))
      return paramString1;
    EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 32, true).addAttribute("LOCAL_COMPONENT_LIST", paramString2).addAttribute("REMOTE_COMPONENT_LIST", paramString1));
    throw new IOException("Failed to negotiate a transport component");
  }

  void G()
  {
    try
    {
      synchronized (this.Ä)
      {
        this.c = 0;
        this.q = 0;
        this.n = 0;
        this.Á = 0;
        D(2);
        if (this.j == null)
        {
          ByteArrayWriter localByteArrayWriter = new ByteArrayWriter(500);
          byte[] arrayOfByte = new byte[16];
          this.T.nextBytes(arrayOfByte);
          localByteArrayWriter.write(20);
          localByteArrayWriter.write(arrayOfByte);
          String str = this.£.supportedKeyExchanges().list(this.£.getPreferredKeyExchange());
          localByteArrayWriter.writeString(str);
          str = this.£.getSupportedPublicKeys();
          localByteArrayWriter.writeString(str);
          str = this.£.supportedCiphersCS().list(this.£.getPreferredCipherCS());
          localByteArrayWriter.writeString(str);
          str = this.£.supportedCiphersSC().list(this.£.getPreferredCipherSC());
          localByteArrayWriter.writeString(str);
          str = this.£.supportedMacsCS().list(this.£.getPreferredMacCS());
          localByteArrayWriter.writeString(str);
          str = this.£.supportedMacsSC().list(this.£.getPreferredMacSC());
          localByteArrayWriter.writeString(str);
          str = this.£.supportedCompressionsCS().list(this.£.getPreferredCompressionCS());
          localByteArrayWriter.writeString(str);
          str = this.£.supportedCompressionsSC().list(this.£.getPreferredCompressionSC());
          localByteArrayWriter.writeString(str);
          localByteArrayWriter.writeInt(0);
          localByteArrayWriter.writeInt(0);
          localByteArrayWriter.write(0);
          localByteArrayWriter.writeInt(0);
          this.j = localByteArrayWriter.toByteArray();
          this.Â.clear();
          postMessage(new SshMessage()
          {
            public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
            {
              paramByteBuffer.put(TransportProtocol.this.j);
              return true;
            }

            public void messageSent()
            {
            }
          }
          , true);
        }
      }
    }
    catch (IOException localIOException)
    {
      disconnect(2, "Failed to create SSH_MSG_KEX_INIT");
    }
  }

  private String A(String paramString1, String paramString2)
    throws IOException
  {
    if (paramString2.trim().equals(""))
      throw new IOException("Client sent invalid " + paramString1 + " value '" + paramString2 + "'");
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString2, ",");
    if (!localStringTokenizer.hasMoreElements())
      throw new IOException("Client sent invalid " + paramString1 + " value '" + paramString2 + "'");
    return paramString2;
  }

  public void postMessage(SshMessage paramSshMessage)
  {
    if ((!paramSshMessage.equals(this.È)) && (!(paramSshMessage instanceof _A)))
      H();
    postMessage(paramSshMessage, false);
  }

  public void postMessage(SshMessage paramSshMessage, boolean paramBoolean)
  {
    LinkedList localLinkedList = paramBoolean ? this.Â : this.À;
    synchronized (this.Ä)
    {
      localLinkedList.addLast(paramSshMessage);
      this.y.setWriteState(true);
    }
  }

  byte[] A(char paramChar)
    throws SshException, IOException
  {
    ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
    byte[] arrayOfByte = new byte[20];
    Digest localDigest = (Digest)ServerComponentManager.getInstance().supportedDigests().getInstance("SHA1");
    localDigest.putBigInteger(this.Å.getSecret());
    localDigest.putBytes(this.Å.getExchangeHash());
    localDigest.putByte((byte)paramChar);
    localDigest.putBytes(this.µ);
    arrayOfByte = localDigest.doFinal();
    localByteArrayWriter.write(arrayOfByte);
    localDigest.reset();
    localDigest.putBigInteger(this.Å.getSecret());
    localDigest.putBytes(this.Å.getExchangeHash());
    localDigest.putBytes(arrayOfByte);
    arrayOfByte = localDigest.doFinal();
    localByteArrayWriter.write(arrayOfByte);
    return localByteArrayWriter.toByteArray();
  }

  class _A
    implements SshMessage
  {
    int P;
    String O;

    _A(int paramString, String arg3)
    {
      this.P = paramString;
      Object localObject;
      this.O = localObject;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(1);
      paramByteBuffer.putInt(this.P);
      paramByteBuffer.putInt(this.O.length());
      paramByteBuffer.put(this.O.getBytes());
      paramByteBuffer.putInt(0);
      return true;
    }

    public void messageSent()
    {
      TransportProtocol.this.y.closeConnection();
    }
  }

  class _C
    implements SshMessage
  {
    long S;

    _C(long arg2)
    {
      Object localObject;
      this.S = localObject;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(3);
      paramByteBuffer.putInt((int)this.S);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _B
    implements SshMessage
  {
    SecureRandom R = new SecureRandom();
    byte[] Q = new byte[TransportProtocol.this.getContext().getKeepAliveDataMaxLength()];

    _B()
    {
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(2);
      int i = (int)(Math.random() * (this.Q.length + 1));
      this.R.nextBytes(this.Q);
      paramByteBuffer.putInt(i);
      paramByteBuffer.put(this.Q, 0, i);
      return true;
    }

    public void messageSent()
    {
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.TransportProtocol
 * JD-Core Version:    0.6.0
 */