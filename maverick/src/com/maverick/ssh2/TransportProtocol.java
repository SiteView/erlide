package com.maverick.ssh2;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.events.EventServiceImplementation;
import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SocketTimeoutSupport;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshTransport;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshHmac;
import com.maverick.ssh.components.SshKeyExchangeClient;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.ssh.compression.SshCompression;
import com.maverick.ssh.message.SshMessageReader;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransportProtocol
  implements SshMessageReader
{
  public static String CHARSET_ENCODING = "UTF8";
  DataInputStream jb;
  OutputStream dc;
  SshTransport mb;
  Ssh2Context sb;
  Ssh2Client hc;
  String bc;
  String tb;
  byte[] nc;
  byte[] bb;
  byte[] pc;
  public static final int NEGOTIATING_PROTOCOL = 1;
  public static final int PERFORMING_KEYEXCHANGE = 2;
  public static final int CONNECTED = 3;
  public static final int DISCONNECTED = 4;
  int rb;
  Throwable x;
  String cb;
  SshKeyExchangeClient oc;
  SshKeyExchangeClient hb;
  SshCipher ec;
  SshCipher zb;
  SshHmac gb;
  SshHmac vb;
  SshCompression mc;
  SshCompression pb;
  SshPublicKey z;
  boolean lb = false;
  boolean jc = false;
  int w = 8;
  int ib = 0;
  boolean ob = false;
  byte[] ab;
  ByteArrayWriter kb;
  int xb = 8;
  int lc = 0;
  long gc = 0L;
  long kc = 0L;
  int fb;
  int ac;
  int yb;
  int wb;
  long ic = 0L;
  long eb = 0L;
  Object ub = new Object();
  Vector cc = new Vector();
  Vector<Runnable> y = new Vector();
  Vector<TransportProtocolListener> fc = new Vector();
  long qb = System.currentTimeMillis();
  static Log db = LogFactory.getLog(TransportProtocol.class);
  static boolean nb = Boolean.getBoolean("maverick.verbose");
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

  public SshTransport getProvider()
  {
    return this.mb;
  }

  public void addListener(TransportProtocolListener paramTransportProtocolListener)
  {
    this.fc.addElement(paramTransportProtocolListener);
  }

  public Ssh2Client getClient()
  {
    return this.hc;
  }

  public boolean isConnected()
  {
    return (this.rb == 3) || (this.rb == 2);
  }

  public Throwable getLastError()
  {
    return this.x;
  }

  public Ssh2Context getContext()
  {
    return this.sb;
  }

  public boolean getIgnoreHostKeyifEmpty()
  {
    return this.ob;
  }

  public void setIgnoreHostKeyifEmpty(boolean paramBoolean)
  {
    this.ob = paramBoolean;
  }

  public void startTransportProtocol(SshTransport paramSshTransport, Ssh2Context paramSsh2Context, String paramString1, String paramString2, Ssh2Client paramSsh2Client)
    throws SshException
  {
    try
    {
      this.jb = new DataInputStream(paramSshTransport.getInputStream());
      this.dc = paramSshTransport.getOutputStream();
      this.mb = paramSshTransport;
      this.bc = paramString1;
      this.tb = paramString2;
      this.sb = paramSsh2Context;
      this.ab = new byte[this.sb.getMaximumPacketLength()];
      this.kb = new ByteArrayWriter(this.sb.getMaximumPacketLength());
      this.hc = paramSsh2Client;
      if ((paramSsh2Context.getSocketTimeout() > 0) && ((paramSshTransport instanceof SocketTimeoutSupport)))
        ((SocketTimeoutSupport)paramSshTransport).setSoTimeout(paramSsh2Context.getSocketTimeout());
      else if (paramSsh2Context.getSocketTimeout() > 0)
        db.error("Socket timeout is set on SshContext but SshTransport does not support socket timeouts");
      this.rb = 1;
      b(false);
      if ((nb) && (db.isDebugEnabled()))
        db.debug("Waiting for transport protocol to complete initialization");
      while ((processMessage(j())) && (this.rb != 3));
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 10);
    }
    if (db.isDebugEnabled())
      db.debug("Transport protocol initialized");
  }

  public String getRemoteIdentification()
  {
    return this.tb;
  }

  public byte[] getSessionIdentifier()
  {
    return this.pc;
  }

  public void disconnect(int paramInt, String paramString)
  {
    try
    {
      this.cb = paramString;
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(1);
      localByteArrayWriter.writeInt(paramInt);
      localByteArrayWriter.writeString(paramString);
      localByteArrayWriter.writeString("");
      if (db.isDebugEnabled())
        db.debug("Sending SSH_MSG_DISCONNECT");
      sendMessage(localByteArrayWriter.toByteArray(), true);
    }
    catch (Throwable localThrowable)
    {
    }
    finally
    {
      k();
    }
  }

  public void sendMessage(byte[] paramArrayOfByte, boolean paramBoolean)
    throws SshException
  {
    synchronized (this.cc)
    {
      if ((this.rb == 2) && (!d(paramArrayOfByte[0])))
      {
        this.cc.addElement(paramArrayOfByte);
        return;
      }
    }
    if ((nb) && (db.isDebugEnabled()))
      db.debug("Sending transport protocol message");
    synchronized (this.dc)
    {
      try
      {
        this.kb.reset();
        int i = 4;
        if ((this.mc != null) && (this.jc))
          paramArrayOfByte = this.mc.compress(paramArrayOfByte, 0, paramArrayOfByte.length);
        i += (this.w - (paramArrayOfByte.length + 5 + i) % this.w) % this.w;
        this.kb.writeInt(paramArrayOfByte.length + 1 + i);
        this.kb.write(i);
        this.kb.write(paramArrayOfByte, 0, paramArrayOfByte.length);
        ComponentManager.getInstance().getRND().nextBytes(this.kb.array(), this.kb.size(), i);
        this.kb.move(i);
        if (this.gb != null)
          this.gb.generate(this.gc, this.kb.array(), 0, this.kb.size(), this.kb.array(), this.kb.size());
        if (this.ec != null)
          this.ec.transform(this.kb.array(), 0, this.kb.array(), 0, this.kb.size());
        this.kb.move(this.ib);
        this.ic += this.kb.size();
        this.dc.write(this.kb.array(), 0, this.kb.size());
        this.dc.flush();
        if (paramBoolean)
          this.qb = System.currentTimeMillis();
        if ((nb) && (db.isDebugEnabled()))
          db.debug("Sent " + this.kb.size() + " bytes of transport data outgoingSequence=" + this.gc + " totalBytesSinceKEX=" + this.yb);
        this.gc += 1L;
        this.yb += paramArrayOfByte.length;
        this.wb += 1;
        if (this.gc >= 4294967296L)
          this.gc = 0L;
        if ((!this.sb.isKeyReExchangeDisabled()) && ((this.yb >= 1073741824) || (this.wb >= 2147483647)))
        {
          if (db.isDebugEnabled())
            db.debug("Requesting key re-exchange");
          b(false);
        }
      }
      catch (IOException localIOException)
      {
        k();
        throw new SshException("Unexpected termination: " + localIOException.getMessage(), 1);
      }
    }
  }

  public byte[] nextMessage()
    throws SshException
  {
    if ((nb) && (db.isDebugEnabled()))
      db.debug("transport next message");
    synchronized (this.ub)
    {
      byte[] arrayOfByte;
      do
        arrayOfByte = j();
      while (processMessage(arrayOfByte));
      return arrayOfByte;
    }
  }

  void b(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    throws SshException
  {
    int i = 0;
    int j = 0;
    if (paramBoolean)
      j = c(this.sb.getPartialMessageTimeout());
    try
    {
      try
      {
        int k = this.jb.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
        if (k == -1)
          throw new SshException("EOF received from remote side", 1);
        i += k;
      }
      catch (InterruptedIOException localInterruptedIOException)
      {
        if (db.isDebugEnabled())
          db.debug("Socket timed out during read!  isPartialMessage=" + paramBoolean + " bytesTransfered=" + localInterruptedIOException.bytesTransferred);
        if ((paramBoolean) && (localInterruptedIOException.bytesTransferred > 0))
        {
          i += localInterruptedIOException.bytesTransferred;
        }
        else
        {
          if (paramBoolean)
          {
            k();
            throw new SshException("Remote host failed to respond during message receive!", 19);
          }
          if ((getContext().getIdleConnectionTimeoutSeconds() > 0) && (System.currentTimeMillis() - this.qb > getContext().getIdleConnectionTimeoutSeconds() * 1000))
          {
            if (db.isDebugEnabled())
              db.debug("Connection is idle, disconnecting idleMax=" + getContext().getIdleConnectionTimeoutSeconds());
            disconnect(11, "Idle connection");
            throw new SshException("Connection has been dropped as it reached max idle time of " + getContext().getIdleConnectionTimeoutSeconds() + " seconds.", 12);
          }
          if (getContext().isSendIgnorePacketOnIdle())
            try
            {
              if (db.isDebugEnabled())
                db.debug("Sending SSH_MSG_IGNORE");
              ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
              localByteArrayWriter.write(2);
              int m = (int)(Math.random() * getContext().getKeepAliveMaxDataLength() + 1.0D);
              byte[] arrayOfByte = new byte[m];
              ComponentManager.getInstance().getRND().nextBytes(arrayOfByte);
              localByteArrayWriter.writeBinaryString(arrayOfByte);
              sendMessage(localByteArrayWriter.toByteArray(), false);
            }
            catch (IOException localIOException2)
            {
              b("Connection failed during SSH_MSG_IGNORE packet", 10);
            }
          Enumeration localEnumeration = this.fc.elements();
          while (localEnumeration.hasMoreElements())
          {
            TransportProtocolListener localTransportProtocolListener = (TransportProtocolListener)localEnumeration.nextElement();
            try
            {
              localTransportProtocolListener.onIdle(this.qb);
            }
            catch (Throwable localThrowable)
            {
            }
          }
          throw new SshException("Socket connection timed out.", 19);
        }
      }
    }
    catch (IOException localIOException1)
    {
      do
        throw new SshException("IO error received from remote" + localIOException1.getMessage(), 1, localIOException1);
      while (i < paramInt2);
    }
    finally
    {
      if (paramBoolean)
        c(j);
    }
  }

  private int c(int paramInt)
  {
    if ((this.mb instanceof SocketTimeoutSupport))
      try
      {
        SocketTimeoutSupport localSocketTimeoutSupport = (SocketTimeoutSupport)this.mb;
        int i = localSocketTimeoutSupport.getSoTimeout();
        localSocketTimeoutSupport.setSoTimeout(paramInt);
        return i;
      }
      catch (IOException localIOException)
      {
      }
    return 0;
  }

  byte[] j()
    throws SshException
  {
    if ((nb) && (db.isDebugEnabled()))
      db.debug("transport read message");
    synchronized (this.ub)
    {
      try
      {
        if ((nb) && (db.isDebugEnabled()))
          db.debug("Waiting for transport message");
        b(this.ab, 0, this.xb, this.sb.getPartialMessageTimeout(), false);
        if (this.zb != null)
          this.zb.transform(this.ab, 0, this.ab, 0, this.xb);
        int i = (int)ByteArrayReader.readInt(this.ab, 0);
        if (i <= 0)
          throw new SshException("Server sent invalid message length of " + i + "!", 3);
        int j = this.ab[4] & 0xFF;
        int k = i - (this.xb - 4);
        if ((nb) && (db.isDebugEnabled()))
          db.debug("Incoming transport message msglen=" + i + " padlen=" + j);
        if (k < 0)
        {
          k();
          throw new SshException("EOF whilst reading message data block", 1);
        }
        if (k > this.ab.length - this.xb)
        {
          if (k + this.xb + this.lc > this.sb.getMaximumPacketLength())
          {
            k();
            throw new SshException("Incoming packet length violates SSH protocol [" + k + this.xb + " bytes]", 1);
          }
          arrayOfByte = new byte[k + this.xb + this.lc];
          System.arraycopy(this.ab, 0, arrayOfByte, 0, this.xb);
          this.ab = arrayOfByte;
        }
        if (k > 0)
        {
          b(this.ab, this.xb, k, this.sb.getPartialMessageTimeout(), true);
          if (this.zb != null)
            this.zb.transform(this.ab, this.xb, this.ab, this.xb, k);
        }
        if (this.vb != null)
        {
          b(this.ab, this.xb + k, this.lc, this.sb.getPartialMessageTimeout(), true);
          if (!this.vb.verify(this.kc, this.ab, 0, this.xb + k, this.ab, this.xb + k))
          {
            disconnect(5, "Corrupt Mac on input");
            throw new SshException("Corrupt Mac on input", 3);
          }
        }
        if (++this.kc >= 4294967296L)
          this.kc = 0L;
        this.eb += this.xb + k + this.lc;
        byte[] arrayOfByte = new byte[i + 4 - j - 5];
        System.arraycopy(this.ab, 5, arrayOfByte, 0, arrayOfByte.length);
        if ((this.pb != null) && (this.lb))
          return this.pb.uncompress(arrayOfByte, 0, arrayOfByte.length);
        this.fb += arrayOfByte.length;
        this.ac += 1;
        if ((!this.sb.isKeyReExchangeDisabled()) && ((this.fb >= 1073741824) || (this.ac >= 2147483647)))
          b(false);
        if ((nb) && (db.isDebugEnabled()))
          db.debug("Completed incoming transport message");
        return arrayOfByte;
      }
      catch (InterruptedIOException localInterruptedIOException)
      {
        throw new SshException("Interrupted IO; possible socket timeout detected?", 19);
      }
      catch (IOException localIOException)
      {
        k();
        throw new SshException("Unexpected terminaton: " + (localIOException.getMessage() != null ? localIOException.getMessage() : localIOException.getClass().getName()) + " sequenceNo = " + this.kc + " bytesIn = " + this.eb + " bytesOut = " + this.ic, 1, localIOException);
      }
    }
  }

  public SshKeyExchangeClient getKeyExchange()
  {
    return this.oc;
  }

  public static boolean Arrayequals(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    if (paramArrayOfByte1 == paramArrayOfByte2)
      return true;
    if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null))
      return false;
    int i = paramArrayOfByte1.length;
    if (paramArrayOfByte2.length != i)
      return false;
    for (int j = 0; j < i; j++)
      if (paramArrayOfByte1[j] != paramArrayOfByte2[j])
        return false;
    return true;
  }

  void e(byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      synchronized (this.ub)
      {
        if (this.nc == null)
          b(false);
        this.rb = 2;
        this.bb = paramArrayOfByte;
        ByteArrayReader localByteArrayReader = new ByteArrayReader(this.bb, 0, this.bb.length);
        localByteArrayReader.skip(17L);
        String str1 = b("key exchange", localByteArrayReader.readString());
        String str2 = b("public key", localByteArrayReader.readString());
        String str3 = b("client->server cipher", localByteArrayReader.readString());
        String str4 = b("server->client cipher", localByteArrayReader.readString());
        String str5 = b("client->server mac", localByteArrayReader.readString());
        String str6 = b("server->client mac", localByteArrayReader.readString());
        String str7 = b("client->server comp", localByteArrayReader.readString());
        String str8 = b("server->client comp", localByteArrayReader.readString());
        String str9 = localByteArrayReader.readString();
        String str10 = localByteArrayReader.readString();
        boolean bool1 = localByteArrayReader.readBoolean();
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 3, true).addAttribute("REMOTE_KEY_EXCHANGES", str1).addAttribute("LOCAL_KEY_EXCHANGES", this.sb.supportedKeyExchanges().list(this.sb.getPreferredKeyExchange())).addAttribute("REMOTE_PUBLICKEYS", str2).addAttribute("LOCAL_PUBLICKEYS", this.sb.supportedPublicKeys().list(this.sb.getPreferredPublicKey())).addAttribute("REMOTE_CIPHERS_CS", str3).addAttribute("LOCAL_CIPHERS_CS", this.sb.supportedCiphersCS().list(this.sb.getPreferredCipherCS())).addAttribute("REMOTE_CIPHERS_SC", str4).addAttribute("LOCAL_CIPHERS_SC", this.sb.supportedCiphersSC().list(this.sb.getPreferredCipherSC())).addAttribute("REMOTE_CS_MACS", str5).addAttribute("LOCAL_CS_MACS", this.sb.supportedMacsCS().list(this.sb.getPreferredMacCS())).addAttribute("REMOTE_SC_MACS", str6).addAttribute("LOCAL_SC_MACS", this.sb.supportedMacsSC().list(this.sb.getPreferredMacSC())).addAttribute("REMOTE_CS_COMPRESSIONS", str7).addAttribute("LOCAL_CS_COMPRESSIONS", this.sb.supportedCompressionsCS().list(this.sb.getPreferredCompressionCS())).addAttribute("REMOTE_SC_COMPRESSIONS", str8).addAttribute("LOCAL_SC_COMPRESSIONS", this.sb.supportedCompressionsSC().list(this.sb.getPreferredCompressionSC())));
        if (db.isDebugEnabled())
          db.debug("Remote computer supports key exchanges: " + str1);
        if (db.isDebugEnabled())
          db.debug("Remote computer supports public keys: " + str2);
        if (db.isDebugEnabled())
          db.debug("Remote computer supports client->server ciphers: " + str3);
        String str11 = c(this.sb.supportedCiphersCS().list(this.sb.getPreferredCipherCS()), str3);
        if (db.isDebugEnabled())
          db.debug("Negotiated client->server cipher: " + str11);
        if (db.isDebugEnabled())
          db.debug("Remote computer supports client->server ciphers: " + str3);
        String str12 = c(this.sb.supportedCiphersSC().list(this.sb.getPreferredCipherSC()), str4);
        if (db.isDebugEnabled())
          db.debug("Negotiated server->client cipher: " + str12);
        SshCipher localSshCipher1 = (SshCipher)this.sb.supportedCiphersCS().getInstance(str11);
        SshCipher localSshCipher2 = (SshCipher)this.sb.supportedCiphersSC().getInstance(str12);
        String str13 = c(this.sb.supportedMacsCS().list(this.sb.getPreferredMacCS()), b("client->server hmac", str5));
        String str14 = c(this.sb.supportedMacsSC().list(this.sb.getPreferredMacSC()), b("server->client hmac", str6));
        SshHmac localSshHmac1 = (SshHmac)this.sb.supportedMacsCS().getInstance(str13);
        SshHmac localSshHmac2 = (SshHmac)this.sb.supportedMacsSC().getInstance(str14);
        String str15 = c(this.sb.supportedCompressionsCS().list(this.sb.getPreferredCompressionCS()), b("client->server compression", str7));
        String str16 = c(this.sb.supportedCompressionsSC().list(this.sb.getPreferredCompressionSC()), b("server->client compression", str8));
        SshCompression localSshCompression1 = null;
        if (!str15.equals("none"))
        {
          localSshCompression1 = (SshCompression)this.sb.supportedCompressionsCS().getInstance(str15);
          localSshCompression1.init(1, 6);
        }
        SshCompression localSshCompression2 = null;
        if (!str16.equals("none"))
        {
          localSshCompression2 = (SshCompression)this.sb.supportedCompressionsSC().getInstance(str16);
          localSshCompression2.init(0, 6);
        }
        boolean bool2 = false;
        String str17 = c(this.sb.supportedKeyExchanges().list(this.sb.getPreferredKeyExchange()), str1);
        if ((this.hb == null) || (!str17.equals(this.hb.getAlgorithm())))
          this.oc = ((SshKeyExchangeClient)this.sb.supportedKeyExchanges().getInstance(str17));
        if (db.isDebugEnabled())
          db.debug("Negotiated key exchange: " + this.oc.getAlgorithm());
        if (bool1)
        {
          if (!str17.equals(this.sb.getPreferredKeyExchange()))
            bool2 = true;
          str18 = c(this.sb.supportedPublicKeys().list(this.sb.getPreferredPublicKey()), str2);
          if ((!bool2) && (!str18.equals(this.sb.getPreferredPublicKey())))
            bool2 = true;
        }
        this.oc.init(this, bool2);
        this.oc.performClientExchange(this.bc, this.tb, this.nc, this.bb);
        String str18 = c(this.sb.supportedPublicKeys().list(this.sb.getPreferredPublicKey()), str2);
        this.z = ((SshPublicKey)this.sb.supportedPublicKeys().getInstance(str18));
        if ((!this.ob) || (!Arrayequals(this.oc.getHostKey(), "".getBytes())))
        {
          EventServiceImplementation.getInstance().fireEvent(new Event(this, 0, true).addAttribute("HOST_KEY", new String(this.oc.getHostKey())));
          this.z.init(this.oc.getHostKey(), 0, this.oc.getHostKey().length);
          if (this.sb.getHostKeyVerification() != null)
          {
            if (!this.sb.getHostKeyVerification().verifyHost(this.mb.getHost(), this.z))
            {
              EventServiceImplementation.getInstance().fireEvent(new Event(this, 1, false));
              disconnect(9, "Host key not accepted");
              throw new SshException("The host key was not accepted", 8);
            }
            if (!this.z.verifySignature(this.oc.getSignature(), this.oc.getExchangeHash()))
            {
              EventServiceImplementation.getInstance().fireEvent(new Event(this, 1, false));
              disconnect(9, "Invalid host key signature");
              throw new SshException("The host key signature is invalid", 3);
            }
            EventServiceImplementation.getInstance().fireEvent(new Event(this, 2, true));
          }
        }
        if (this.pc == null)
          this.pc = this.oc.getExchangeHash();
        sendMessage(new byte[] { 21 }, true);
        localSshCipher1.init(0, b('A'), b('C'));
        this.w = localSshCipher1.getBlockSize();
        localSshHmac1.init(b('E'));
        this.ib = localSshHmac1.getMacLength();
        this.ec = localSshCipher1;
        this.gb = localSshHmac1;
        this.mc = localSshCompression1;
        do
        {
          paramArrayOfByte = j();
          if (processMessage(paramArrayOfByte))
            continue;
          EventServiceImplementation.getInstance().fireEvent(new Event(this, 4, true));
          disconnect(2, "Invalid message received");
          throw new SshException("Invalid message received during key exchange", 3);
        }
        while (paramArrayOfByte[0] != 21);
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 5, true).addAttribute("USING_PUBLICKEY", str18).addAttribute("USING_KEY_EXCHANGE", str17).addAttribute("USING_CS_CIPHER", str11).addAttribute("USING_SC_CIPHERC", str12).addAttribute("USING_CS_MAC", str13).addAttribute("USING_SC_MAC", str14).addAttribute("USING_CS_COMPRESSION", str15).addAttribute("USING_SC_COMPRESSION", str16));
        localSshCipher2.init(1, b('B'), b('D'));
        this.xb = localSshCipher2.getBlockSize();
        localSshHmac2.init(b('F'));
        this.lc = localSshHmac2.getMacLength();
        this.zb = localSshCipher2;
        this.vb = localSshHmac2;
        this.pb = localSshCompression2;
        if ((localSshCompression2 != null) && (!localSshCompression2.getAlgorithm().equals("zlib@openssh.com")))
          this.lb = true;
        if ((localSshCompression1 != null) && (!localSshCompression1.getAlgorithm().equals("zlib@openssh.com")))
          this.jc = true;
        synchronized (this.cc)
        {
          this.rb = 3;
          Enumeration localEnumeration = this.cc.elements();
          while (localEnumeration.hasMoreElements())
            sendMessage((byte[])(byte[])localEnumeration.nextElement(), true);
          this.cc.removeAllElements();
        }
        this.nc = null;
        this.bb = null;
      }
    }
    catch (IOException localIOException)
    {
      EventServiceImplementation.getInstance().fireEvent(new Event(this, 4, true));
      throw new SshException(localIOException, 5);
    }
    catch (SshException localSshException)
    {
      EventServiceImplementation.getInstance().fireEvent(new Event(this, 4, true));
      throw localSshException;
    }
  }

  void i()
  {
    if ((this.pb != null) && (this.pb.getAlgorithm().equals("zlib@openssh.com")))
      this.lb = true;
    if ((this.mc != null) && (this.mc.getAlgorithm().equals("zlib@openssh.com")))
      this.jc = true;
  }

  public void startService(String paramString)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(5);
      localByteArrayWriter.writeString(paramString);
      if (db.isDebugEnabled())
        db.debug("Sending SSH_MSG_SERVICE_REQUEST");
      sendMessage(localByteArrayWriter.toByteArray(), true);
      byte[] arrayOfByte;
      do
        arrayOfByte = j();
      while ((processMessage(arrayOfByte)) || (arrayOfByte[0] != 6));
      if (db.isDebugEnabled())
        db.debug("Received SSH_MSG_SERVICE_ACCEPT");
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  void b(String paramString, int paramInt)
  {
    this.rb = 4;
    try
    {
      this.mb.close();
    }
    catch (IOException localIOException)
    {
    }
    Enumeration localEnumeration = this.fc.elements();
    while (localEnumeration.hasMoreElements())
    {
      TransportProtocolListener localTransportProtocolListener = (TransportProtocolListener)localEnumeration.nextElement();
      try
      {
        localTransportProtocolListener.onDisconnect(paramString, paramInt);
      }
      catch (Throwable localThrowable2)
      {
      }
    }
    for (int i = 0; i < this.y.size(); i++)
      try
      {
        ((Runnable)this.y.elementAt(i)).run();
      }
      catch (Throwable localThrowable1)
      {
      }
  }

  void k()
  {
    this.rb = 4;
    try
    {
      this.mb.close();
    }
    catch (IOException localIOException)
    {
    }
    for (int i = 0; i < this.y.size(); i++)
      try
      {
        ((Runnable)this.y.elementAt(i)).run();
      }
      catch (Throwable localThrowable)
      {
      }
  }

  void b(Runnable paramRunnable)
  {
    if (paramRunnable != null)
      this.y.addElement(paramRunnable);
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      if (paramArrayOfByte.length < 1)
      {
        disconnect(2, "Invalid message received");
        throw new SshException("Invalid transport protocol message", 5);
      }
      switch (paramArrayOfByte[0])
      {
      case 1:
        if (db.isDebugEnabled())
          db.debug("Received SSH_MSG_DISCONNECT");
        k();
        ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte, 5, paramArrayOfByte.length - 5);
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 21, true));
        throw new SshException(localByteArrayReader.readString(), 2);
      case 2:
        if (db.isDebugEnabled())
          db.debug("Received SSH_MSG_IGNORE");
        return true;
      case 4:
        this.qb = System.currentTimeMillis();
        if (db.isDebugEnabled())
          db.debug("Received SSH_MSG_DEBUG");
        return true;
      case 21:
        this.qb = System.currentTimeMillis();
        if (db.isDebugEnabled())
          db.debug("Received SSH_MSG_NEWKEYS");
        return true;
      case 20:
        this.qb = System.currentTimeMillis();
        if (db.isDebugEnabled())
          db.debug("Received SSH_MSG_KEX_INIT");
        if (this.bb != null)
        {
          disconnect(2, "Key exchange already in progress!");
          throw new SshException("Key exchange already in progress!", 3);
        }
        e(paramArrayOfByte);
        return true;
      }
      this.qb = System.currentTimeMillis();
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException.getMessage(), 5);
  }

  boolean d(int paramInt)
  {
    switch (paramInt)
    {
    case 1:
    case 2:
    case 4:
    case 20:
    case 21:
      return true;
    }
    if (this.oc != null)
      return this.oc.isKeyExchangeMessage(paramInt);
    return false;
  }

  String c(String paramString1, String paramString2)
    throws SshException
  {
    String str1 = paramString2;
    String str2 = paramString1;
    Vector localVector = new Vector();
    int i;
    while ((i = str1.indexOf(",")) > -1)
    {
      localVector.addElement(str1.substring(0, i).trim());
      str1 = str1.substring(i + 1).trim();
    }
    localVector.addElement(str1);
    while ((i = str2.indexOf(",")) > -1)
    {
      String str3 = str2.substring(0, i).trim();
      if (localVector.contains(str3))
        return str3;
      str2 = str2.substring(i + 1).trim();
    }
    if (localVector.contains(str2))
      return str2;
    EventServiceImplementation.getInstance().fireEvent(new Event(this, 32, true).addAttribute("LOCAL_COMPONENT_LIST", paramString1).addAttribute("REMOTE_COMPONENT_LIST", paramString2));
    throw new SshException("Failed to negotiate a transport component [" + paramString1 + "] [" + paramString2 + "]", 9);
  }

  void b(boolean paramBoolean)
    throws SshException
  {
    try
    {
      this.fb = 0;
      this.ac = 0;
      this.yb = 0;
      this.wb = 0;
      this.rb = 2;
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      byte[] arrayOfByte = new byte[16];
      ComponentManager.getInstance().getRND().nextBytes(arrayOfByte);
      localByteArrayWriter.write(20);
      localByteArrayWriter.write(arrayOfByte);
      localByteArrayWriter.writeString(this.sb.supportedKeyExchanges().list(this.sb.getPreferredKeyExchange()));
      localByteArrayWriter.writeString(this.sb.supportedPublicKeys().list(this.sb.getPreferredPublicKey()));
      localByteArrayWriter.writeString(this.sb.supportedCiphersCS().list(this.sb.getPreferredCipherCS()));
      localByteArrayWriter.writeString(this.sb.supportedCiphersSC().list(this.sb.getPreferredCipherSC()));
      localByteArrayWriter.writeString(this.sb.supportedMacsCS().list(this.sb.getPreferredMacCS()));
      localByteArrayWriter.writeString(this.sb.supportedMacsSC().list(this.sb.getPreferredMacSC()));
      localByteArrayWriter.writeString(this.sb.supportedCompressionsCS().list(this.sb.getPreferredCompressionCS()));
      localByteArrayWriter.writeString(this.sb.supportedCompressionsSC().list(this.sb.getPreferredCompressionSC()));
      localByteArrayWriter.writeString("");
      localByteArrayWriter.writeString("");
      localByteArrayWriter.writeBoolean(paramBoolean);
      localByteArrayWriter.writeInt(0);
      if (db.isDebugEnabled())
        db.debug("Sending SSH_MSG_KEX_INIT");
      sendMessage(this.nc = localByteArrayWriter.toByteArray(), true);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  byte[] b(char paramChar)
    throws IOException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      byte[] arrayOfByte = new byte[20];
      Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("SHA-1");
      localDigest.putBigInteger(this.oc.getSecret());
      localDigest.putBytes(this.oc.getExchangeHash());
      localDigest.putByte((byte)paramChar);
      localDigest.putBytes(this.pc);
      arrayOfByte = localDigest.doFinal();
      localByteArrayWriter.write(arrayOfByte);
      localDigest.reset();
      localDigest.putBigInteger(this.oc.getSecret());
      localDigest.putBytes(this.oc.getExchangeHash());
      localDigest.putBytes(arrayOfByte);
      arrayOfByte = localDigest.doFinal();
      localByteArrayWriter.write(arrayOfByte);
      return localByteArrayWriter.toByteArray();
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }

  private String b(String paramString1, String paramString2)
    throws IOException
  {
    if (paramString2.trim().equals(""))
      throw new IOException("Server sent invalid " + paramString1 + " value '" + paramString2 + "'");
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString2, ",");
    if (!localStringTokenizer.hasMoreElements())
      throw new IOException("Server sent invalid " + paramString1 + " value '" + paramString2 + "'");
    return paramString2;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.TransportProtocol
 * JD-Core Version:    0.6.0
 */