package com.maverick.ssh2;

import com.maverick.ssh.ChannelEventListener;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.ForwardingRequestListener;
import com.maverick.ssh.PasswordAuthentication;
import com.maverick.ssh.PublicKeyAuthentication;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.SshTransport;
import com.maverick.ssh.SshTunnel;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshHmac;
import com.maverick.ssh.components.SshKeyExchangeClient;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.compression.SshCompression;
import com.maverick.ssh.message.SshAbstractChannel;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Ssh2Client
  implements SshClient
{
  TransportProtocol s;
  SshTransport y;
  AuthenticationProtocol x;
  b o;
  String t;
  String z;
  String[] w;
  String v;
  Hashtable cb = new Hashtable();
  Hashtable bb = new Hashtable();
  _b ab = new _b();
  SshAuthentication p;
  SshConnector r;
  boolean n = false;
  boolean q;
  static Log u = LogFactory.getLog(Ssh2Client.class);

  public void connect(SshTransport paramSshTransport, SshContext paramSshContext, SshConnector paramSshConnector, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws SshException
  {
    if ((paramSshConnector == null) || (!paramSshConnector.isLicensed()))
      throw new SshException("You cannot create Ssh2Client instances directly", 4);
    this.y = paramSshTransport;
    this.t = paramString2;
    this.z = paramString3;
    this.v = paramString1;
    this.q = paramBoolean;
    this.r = paramSshConnector;
    if (paramString1 == null)
    {
      try
      {
        paramSshTransport.close();
      }
      catch (IOException localIOException1)
      {
        if (u.isDebugEnabled())
          u.debug("RECIEVED IOException IN Ssh2Client.connect:" + localIOException1.getMessage());
      }
      throw new SshException("You must supply a valid username!", 4);
    }
    if (!(paramSshContext instanceof Ssh2Context))
    {
      try
      {
        paramSshTransport.close();
      }
      catch (IOException localIOException2)
      {
        if (u.isDebugEnabled())
          u.debug("RECIEVED IOException IN Ssh2Client.connect:" + localIOException2.getMessage());
      }
      throw new SshException("Ssh2Context required!", 4);
    }
    if (u.isDebugEnabled())
      u.debug("Connecting " + paramString1 + "@" + paramSshTransport.getHost() + ":" + paramSshTransport.getPort());
    if (u.isDebugEnabled())
      u.debug("Remote identification is " + paramString3);
    this.s = new TransportProtocol();
    if (u.isDebugEnabled())
      u.debug("Starting transport protocol");
    this.s.startTransportProtocol(paramSshTransport, (Ssh2Context)paramSshContext, paramString2, paramString3, this);
    if (u.isDebugEnabled())
      u.debug("Starting authentication protocol");
    this.x = new AuthenticationProtocol(this.s);
    this.x.setBannerDisplay(((Ssh2Context)paramSshContext).getBannerDisplay());
    this.o = new b(this.s, paramSshContext, paramBoolean);
    this.o.b(this.ab);
    getAuthenticationMethods(paramString1);
    if (u.isDebugEnabled())
      u.debug("SSH connection established");
  }

  public String[] getAuthenticationMethods(String paramString)
    throws SshException
  {
    b(false);
    if (this.w == null)
    {
      if (u.isDebugEnabled())
        u.debug("Requesting authentication methods");
      String str = this.x.getAuthenticationMethods(paramString, "ssh-connection");
      if (u.isDebugEnabled())
        u.debug("Available authentications are " + str);
      Vector localVector = new Vector();
      while (str != null)
      {
        int i = str.indexOf(',');
        if (i > -1)
        {
          localVector.addElement(str.substring(0, i));
          str = str.substring(i + 1);
          continue;
        }
        localVector.addElement(str);
        str = null;
      }
      this.w = new String[localVector.size()];
      localVector.copyInto(this.w);
      if (isAuthenticated())
        this.o.start();
    }
    return this.w;
  }

  private SshAuthentication b(SshAuthentication paramSshAuthentication)
  {
    int i = 0;
    for (int j = 0; j < this.w.length; j++)
    {
      if (this.w[j].equals("password"))
        return paramSshAuthentication;
      if (!this.w[j].equals("keyboard-interactive"))
        continue;
      i = 1;
    }
    if (i != 0)
    {
      KBIAuthentication localKBIAuthentication = new KBIAuthentication();
      localKBIAuthentication.setUsername(((PasswordAuthentication)paramSshAuthentication).getUsername());
      localKBIAuthentication.setKBIRequestHandler(new _c((PasswordAuthentication)paramSshAuthentication));
      return localKBIAuthentication;
    }
    return paramSshAuthentication;
  }

  public int authenticate(SshAuthentication paramSshAuthentication)
    throws SshException
  {
    b(false);
    if (isAuthenticated())
      throw new SshException("User is already authenticated! Did you check isAuthenticated?", 4);
    if (paramSshAuthentication.getUsername() == null)
      paramSshAuthentication.setUsername(this.v);
    if (((paramSshAuthentication instanceof PasswordAuthentication)) || ((paramSshAuthentication instanceof Ssh2PasswordAuthentication)))
      paramSshAuthentication = b(paramSshAuthentication);
    if (u.isDebugEnabled())
      u.debug("Authenticating with " + paramSshAuthentication.getMethod());
    Object localObject;
    int i;
    if (((paramSshAuthentication instanceof PasswordAuthentication)) && (!(paramSshAuthentication instanceof Ssh2PasswordAuthentication)))
    {
      localObject = new Ssh2PasswordAuthentication();
      ((Ssh2PasswordAuthentication)localObject).setUsername(((PasswordAuthentication)paramSshAuthentication).getUsername());
      ((Ssh2PasswordAuthentication)localObject).setPassword(((PasswordAuthentication)paramSshAuthentication).getPassword());
      i = this.x.authenticate((AuthenticationClient)localObject, "ssh-connection");
      if (((Ssh2PasswordAuthentication)localObject).requiresPasswordChange())
      {
        disconnect();
        throw new SshException("Password change required!", 8);
      }
    }
    else if (((paramSshAuthentication instanceof PublicKeyAuthentication)) && (!(paramSshAuthentication instanceof Ssh2PublicKeyAuthentication)))
    {
      localObject = new Ssh2PublicKeyAuthentication();
      ((Ssh2PublicKeyAuthentication)localObject).setUsername(((PublicKeyAuthentication)paramSshAuthentication).getUsername());
      ((Ssh2PublicKeyAuthentication)localObject).setPublicKey(((PublicKeyAuthentication)paramSshAuthentication).getPublicKey());
      ((Ssh2PublicKeyAuthentication)localObject).setPrivateKey(((PublicKeyAuthentication)paramSshAuthentication).getPrivateKey());
      i = this.x.authenticate((AuthenticationClient)localObject, "ssh-connection");
    }
    else if ((paramSshAuthentication instanceof AuthenticationClient))
    {
      i = this.x.authenticate((AuthenticationClient)paramSshAuthentication, "ssh-connection");
    }
    else
    {
      throw new SshException("Invalid authentication client", 4);
    }
    if (i == 1)
    {
      this.p = paramSshAuthentication;
      this.o.start();
    }
    if (u.isDebugEnabled())
      switch (i)
      {
      case 1:
        u.debug("Authentication complete");
        break;
      case 2:
        u.debug("Authentication failed");
        break;
      case 3:
        u.debug("Authentication successful but further authentication required");
        break;
      case 4:
        u.debug("Authentication cancelled");
        break;
      case 5:
        u.debug("Server accepts the public key provided");
        break;
      default:
        u.debug("Unknown authentication result " + i);
      }
    return i;
  }

  public boolean isAuthenticated()
  {
    return this.x.isAuthenticated();
  }

  public void disconnect()
  {
    try
    {
      if (u.isDebugEnabled())
        u.debug("Disconnecting");
      this.o.signalClosingState();
      this.s.disconnect(11, "The user disconnected the application");
    }
    catch (Throwable localThrowable)
    {
    }
    if (u.isDebugEnabled())
      u.debug("Disconnected");
  }

  public void exit()
  {
    try
    {
      if (u.isDebugEnabled())
        u.debug("Disconnecting");
      this.o.signalClosingState();
      this.s.disconnect(11, "The user disconnected the application");
    }
    catch (Throwable localThrowable)
    {
    }
    if (u.isDebugEnabled())
      u.debug("Disconnected");
  }

  public boolean isConnected()
  {
    return this.s.isConnected();
  }

  public void forceKeyExchange()
    throws SshException
  {
    if (u.isDebugEnabled())
      u.debug("Forcing key exchange");
    this.s.b(false);
  }

  public SshSession openSessionChannel()
    throws SshException, ChannelOpenException
  {
    return openSessionChannel(65535, 32768, null, 0L);
  }

  public SshSession openSessionChannel(long paramLong)
    throws SshException, ChannelOpenException
  {
    return openSessionChannel(65535, 32768, null, paramLong);
  }

  public SshSession openSessionChannel(ChannelEventListener paramChannelEventListener)
    throws SshException, ChannelOpenException
  {
    return openSessionChannel(65535, 32768, paramChannelEventListener, 0L);
  }

  public SshSession openSessionChannel(ChannelEventListener paramChannelEventListener, long paramLong)
    throws SshException, ChannelOpenException
  {
    return openSessionChannel(65535, 32768, paramChannelEventListener, paramLong);
  }

  public SshSession openSessionChannel(int paramInt1, int paramInt2)
    throws SshException, ChannelOpenException
  {
    return openSessionChannel(paramInt1, paramInt2, null, 0L);
  }

  public Ssh2Session openSessionChannel(int paramInt1, int paramInt2, ChannelEventListener paramChannelEventListener, long paramLong)
    throws ChannelOpenException, SshException
  {
    b(true);
    if (u.isDebugEnabled())
      u.debug("Opening session channel windowspace=" + paramInt1 + " packetsize=" + paramInt2 + " timeout=" + paramLong);
    Ssh2Session localSsh2Session = new Ssh2Session(paramInt1, paramInt2, this, paramLong);
    if (paramChannelEventListener != null)
      localSsh2Session.addChannelEventListener(paramChannelEventListener);
    this.o.b(localSsh2Session, null);
    if (u.isDebugEnabled())
      u.debug("Channel has been opened channelid=" + localSsh2Session.getChannelId());
    if (this.o.e().getX11Display() != null)
    {
      String str1 = this.o.e().getX11Display();
      int i = str1.indexOf(':');
      int j = 0;
      if (i != -1)
        str1 = str1.substring(i + 1);
      i = str1.indexOf('.');
      if (i > -1)
        j = Integer.parseInt(str1.substring(i + 1));
      byte[] arrayOfByte = this.o.e().getX11AuthenticationCookie();
      StringBuffer localStringBuffer = new StringBuffer();
      for (int k = 0; k < 16; k++)
      {
        String str2 = Integer.toHexString(arrayOfByte[k] & 0xFF);
        if (str2.length() == 1)
          str2 = "0" + str2;
        localStringBuffer.append(str2);
      }
      if (localSsh2Session.b(false, "MIT-MAGIC-COOKIE-1", localStringBuffer.toString(), j))
        this.n = true;
    }
    return localSsh2Session;
  }

  public SshClient openRemoteClient(String paramString1, int paramInt, String paramString2, SshConnector paramSshConnector)
    throws SshException, ChannelOpenException
  {
    if (u.isDebugEnabled())
      u.debug("Opening a remote SSH client from " + this.y.getHost() + " to " + paramString2 + "@" + paramString1 + ":" + paramInt);
    SshTunnel localSshTunnel = openForwardingChannel(paramString1, paramInt, "127.0.0.1", 22, "127.0.0.1", 22, null, null);
    return paramSshConnector.connect(localSshTunnel, paramString2, this.q);
  }

  public SshClient openRemoteClient(String paramString1, int paramInt, String paramString2)
    throws SshException, ChannelOpenException
  {
    return openRemoteClient(paramString1, paramInt, paramString2, this.r);
  }

  public SshTunnel openForwardingChannel(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, int paramInt3, SshTransport paramSshTransport, ChannelEventListener paramChannelEventListener)
    throws SshException, ChannelOpenException
  {
    try
    {
      if (u.isDebugEnabled())
        u.debug("Opening forwarding channel from " + paramString2 + ":" + paramInt2 + " to " + paramString1 + ":" + paramInt1);
      c localc = new c("direct-tcpip", 32768, 32768, paramString1, paramInt1, paramString2, paramInt2, paramString3, paramInt3, paramSshTransport);
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString1);
      localByteArrayWriter.writeInt(paramInt1);
      localByteArrayWriter.writeString(paramString3);
      localByteArrayWriter.writeInt(paramInt3);
      localc.addChannelEventListener(paramChannelEventListener);
      openChannel(localc, localByteArrayWriter.toByteArray());
      return localc;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public boolean requestRemoteForwarding(String paramString1, int paramInt1, String paramString2, int paramInt2, ForwardingRequestListener paramForwardingRequestListener)
    throws SshException
  {
    return requestRemoteForwarding(paramString1, paramInt1, paramString2, paramInt2, paramForwardingRequestListener, getContext().getMessageTimeout());
  }

  public boolean requestRemoteForwarding(String paramString1, int paramInt1, String paramString2, int paramInt2, ForwardingRequestListener paramForwardingRequestListener, long paramLong)
    throws SshException
  {
    try
    {
      if (paramForwardingRequestListener == null)
        throw new SshException("You must specify a listener to receive connection requests", 4);
      if (u.isDebugEnabled())
        u.debug("Requesting remote forwarding from " + paramString1 + ":" + paramInt1 + " to " + paramString2 + ":" + paramInt2);
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString1);
      localByteArrayWriter.writeInt(paramInt1);
      GlobalRequest localGlobalRequest = new GlobalRequest("tcpip-forward", localByteArrayWriter.toByteArray());
      if (sendGlobalRequest(localGlobalRequest, true, paramLong))
      {
        this.cb.put(paramString1 + ":" + String.valueOf(paramInt1), paramForwardingRequestListener);
        this.bb.put(paramString1 + ":" + String.valueOf(paramInt1), paramString2 + ":" + String.valueOf(paramInt2));
        return true;
      }
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public boolean cancelRemoteForwarding(String paramString, int paramInt)
    throws SshException
  {
    return cancelRemoteForwarding(paramString, paramInt, getContext().getMessageTimeout());
  }

  public boolean cancelRemoteForwarding(String paramString, int paramInt, long paramLong)
    throws SshException
  {
    try
    {
      if (u.isDebugEnabled())
        u.debug("Cancelling remote forwarding from " + paramString + ":" + paramInt);
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(paramString);
      localByteArrayWriter.writeInt(paramInt);
      GlobalRequest localGlobalRequest = new GlobalRequest("cancel-tcpip-forward", localByteArrayWriter.toByteArray());
      if (sendGlobalRequest(localGlobalRequest, true, paramLong))
      {
        this.cb.remove(paramString + ":" + String.valueOf(paramInt));
        this.bb.remove(paramString + ":" + String.valueOf(paramInt));
        return true;
      }
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public void openChannel(Ssh2Channel paramSsh2Channel, byte[] paramArrayOfByte)
    throws SshException, ChannelOpenException
  {
    b(true);
    this.o.b(paramSsh2Channel, paramArrayOfByte);
  }

  public void openChannel(SshAbstractChannel paramSshAbstractChannel)
    throws SshException, ChannelOpenException
  {
    b(true);
    if ((paramSshAbstractChannel instanceof Ssh2Channel))
      this.o.b((Ssh2Channel)paramSshAbstractChannel, null);
    else
      throw new SshException("The channel is not an SSH2 channel!", 4);
  }

  public void addChannelFactory(ChannelFactory paramChannelFactory)
    throws SshException
  {
    this.o.b(paramChannelFactory);
  }

  public SshContext getContext()
  {
    return this.s.sb;
  }

  public void addRequestHandler(GlobalRequestHandler paramGlobalRequestHandler)
    throws SshException
  {
    if (u.isDebugEnabled())
    {
      String str = "";
      for (int i = 0; i < paramGlobalRequestHandler.supportedRequests().length; i++)
        str = str + paramGlobalRequestHandler.supportedRequests()[i] + " ";
      u.debug("Installing global request handler for " + str.trim());
    }
    this.o.b(paramGlobalRequestHandler);
  }

  public boolean sendGlobalRequest(GlobalRequest paramGlobalRequest, boolean paramBoolean, long paramLong)
    throws SshException
  {
    b(true);
    return this.o.b(paramGlobalRequest, paramBoolean, paramLong);
  }

  public String getRemoteIdentification()
  {
    return this.z;
  }

  void b(boolean paramBoolean)
    throws SshException
  {
    if ((this.x == null) || (this.s == null) || (this.o == null))
      throw new SshException("Not connected!", 4);
    if (!this.s.isConnected())
      throw new SshException("The connection has been terminated!", 2);
    if ((!this.x.isAuthenticated()) && (paramBoolean))
      throw new SshException("The connection is not authenticated!", 4);
  }

  public String getUsername()
  {
    return this.v;
  }

  public SshClient duplicate()
    throws SshException
  {
    if ((this.v == null) || (this.p == null))
      throw new SshException("Cannot duplicate! The existing connection does not have a set of credentials", 4);
    try
    {
      if (u.isDebugEnabled())
        u.debug("Duplicating SSH client");
      SshClient localSshClient = this.r.connect(this.y.duplicate(), this.v, this.q, this.s.sb);
      if (localSshClient.authenticate(this.p) != 1)
      {
        localSshClient.disconnect();
        throw new SshException("Duplication attempt failed to authenicate user!", 5);
      }
      return localSshClient;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 10);
  }

  public int getChannelCount()
  {
    return this.o.getChannelCount();
  }

  public int getVersion()
  {
    return 2;
  }

  public boolean isBuffered()
  {
    return this.q;
  }

  public String getKeyExchangeInUse()
  {
    return this.s.oc == null ? "none" : this.s.oc.getAlgorithm();
  }

  public SshKeyExchangeClient getKeyExchangeInstanceInUse()
  {
    return this.s.oc;
  }

  public String getHostKeyInUse()
  {
    return this.s.z == null ? "none" : this.s.z.getAlgorithm();
  }

  public String getCipherInUseCS()
  {
    return this.s.ec == null ? "none" : this.s.ec.getAlgorithm();
  }

  public String getCipherInUseSC()
  {
    return this.s.zb == null ? "none" : this.s.zb.getAlgorithm();
  }

  public String getMacInUseCS()
  {
    return this.s.gb == null ? "none" : this.s.gb.getAlgorithm();
  }

  public String getMacInUseSC()
  {
    return this.s.vb == null ? "none" : this.s.vb.getAlgorithm();
  }

  public String getCompressionInUseCS()
  {
    return this.s.mc == null ? "none" : this.s.mc.getAlgorithm();
  }

  public String getCompressionInUseSC()
  {
    return this.s.pb == null ? "none" : this.s.pb.getAlgorithm();
  }

  public String toString()
  {
    return "SSH2 " + this.y.getHost() + ":" + this.y.getPort() + " [kex=" + (this.s.oc == null ? "none" : this.s.oc.getAlgorithm()) + " hostkey=" + (this.s.z == null ? "none" : this.s.z.getAlgorithm()) + " client->server=" + (this.s.ec == null ? "none" : this.s.ec.getAlgorithm()) + "," + (this.s.gb == null ? "none" : this.s.gb.getAlgorithm()) + "," + (this.s.mc == null ? "none" : this.s.mc.getAlgorithm()) + " server->client=" + (this.s.zb == null ? "none" : this.s.zb.getAlgorithm()) + "," + (this.s.vb == null ? "none" : this.s.vb.getAlgorithm()) + "," + (this.s.pb == null ? "none" : this.s.pb.getAlgorithm()) + "]";
  }

  class _b
    implements ChannelFactory
  {
    String[] c = { "forwarded-tcpip", "x11" };

    _b()
    {
    }

    public String[] supportedChannelTypes()
    {
      return this.c;
    }

    public Ssh2Channel createChannel(String paramString, byte[] paramArrayOfByte)
      throws SshException, ChannelOpenException
    {
      String str1;
      int i;
      String str2;
      int j;
      String str3;
      ForwardingRequestListener localForwardingRequestListener1;
      c localc;
      if (paramString.equals("forwarded-tcpip"))
        try
        {
          ByteArrayReader localByteArrayReader1 = new ByteArrayReader(paramArrayOfByte);
          str1 = localByteArrayReader1.readString();
          i = (int)localByteArrayReader1.readInt();
          str2 = localByteArrayReader1.readString();
          j = (int)localByteArrayReader1.readInt();
          str3 = str1 + ":" + String.valueOf(i);
          if (Ssh2Client.this.cb.containsKey(str3))
          {
            localForwardingRequestListener1 = (ForwardingRequestListener)Ssh2Client.this.cb.get(str3);
            String str4 = (String)Ssh2Client.this.bb.get(str3);
            String str5 = str4.substring(0, str4.indexOf(':'));
            int n = Integer.parseInt(str4.substring(str4.indexOf(':') + 1));
            if (Ssh2Client.u.isDebugEnabled())
              Ssh2Client.u.debug("Creating remote forwarding channel from " + str1 + ":" + i + " to " + str5 + ":" + n);
            localc = new c("forwarded-tcpip", 32768, 32768, str5, n, str1, i, str2, j, localForwardingRequestListener1.createConnection(str5, n));
            localForwardingRequestListener1.initializeTunnel(localc);
            return localc;
          }
          throw new ChannelOpenException("Forwarding had not previously been requested", 1);
        }
        catch (IOException localIOException)
        {
          throw new ChannelOpenException(localIOException.getMessage(), 4);
        }
        catch (SshException localSshException)
        {
          throw new ChannelOpenException(localSshException.getMessage(), 2);
        }
      if (paramString.equals("x11"))
      {
        if (!Ssh2Client.this.n)
          throw new ChannelOpenException("X Forwarding had not previously been requested", 1);
        try
        {
          ByteArrayReader localByteArrayReader2 = new ByteArrayReader(paramArrayOfByte);
          str1 = localByteArrayReader2.readString();
          i = (int)localByteArrayReader2.readInt();
          str2 = Ssh2Client.this.o.e().getX11Display();
          j = str2.indexOf(":");
          ForwardingRequestListener localForwardingRequestListener2 = 0;
          int m = 0;
          int k;
          if (j != -1)
          {
            str3 = str2.substring(0, j);
            str2 = str2.substring(j + 1);
            j = str2.indexOf('.');
            if (j > -1)
            {
              localForwardingRequestListener2 = Integer.parseInt(str2.substring(0, j));
              m = Integer.parseInt(str2.substring(j + 1));
            }
            else
            {
              localForwardingRequestListener2 = Integer.parseInt(str2);
            }
            localForwardingRequestListener1 = localForwardingRequestListener2;
          }
          else
          {
            str3 = str2;
            k = 6000;
          }
          if (k <= 10)
            k += 6000;
          if (Ssh2Client.u.isDebugEnabled())
            Ssh2Client.u.debug("Creating X11 forwarding channel for display " + str3 + ":" + m);
          ForwardingRequestListener localForwardingRequestListener3 = Ssh2Client.this.o.e().getX11RequestListener();
          localc = new c("x11", 32768, 32768, str3, k, str3, m, str1, i, localForwardingRequestListener3.createConnection(str3, k));
          localForwardingRequestListener3.initializeTunnel(localc);
          return localc;
        }
        catch (Throwable localThrowable)
        {
          throw new ChannelOpenException(localThrowable.getMessage(), 2);
        }
      }
      throw new ChannelOpenException(paramString + " is not supported", 3);
    }
  }

  private static class _c
    implements KBIRequestHandler
  {
    private String b;

    public _c(PasswordAuthentication paramPasswordAuthentication)
    {
      this.b = paramPasswordAuthentication.getPassword();
    }

    public boolean showPrompts(String paramString1, String paramString2, KBIPrompt[] paramArrayOfKBIPrompt)
    {
      for (int i = 0; i < paramArrayOfKBIPrompt.length; i++)
        paramArrayOfKBIPrompt[i].setResponse(this.b);
      return true;
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.Ssh2Client
 * JD-Core Version:    0.6.0
 */