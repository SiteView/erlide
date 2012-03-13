package com.maverick.ssh1;

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
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.message.SshMessage;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.math.BigInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Ssh1Client
  implements SshClient
{
  String hb;
  boolean jb = false;
  boolean eb;
  b kb;
  f lb;
  String ib;
  SshAuthentication db;
  SshConnector fb;
  static Log gb = LogFactory.getLog(Ssh1Client.class);

  public void connect(SshTransport paramSshTransport, SshContext paramSshContext, SshConnector paramSshConnector, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws SshException
  {
    if ((paramSshConnector == null) || (!paramSshConnector.isLicensed()))
      throw new SshException("You cannot create Ssh1Client instances directly", 4);
    this.lb = new f(paramSshTransport, paramSshContext);
    this.hb = paramString1;
    this.eb = paramBoolean;
    this.fb = paramSshConnector;
    this.ib = paramString3;
    if (paramString1 == null)
      throw new SshException("You must supply a valid username!", 4);
    this.lb.b();
    this.lb.g();
    this.jb = this.lb.b(paramString1);
  }

  public String getRemoteIdentification()
  {
    return this.ib;
  }

  public boolean isAuthenticated()
  {
    return this.jb;
  }

  public int authenticate(SshAuthentication paramSshAuthentication)
    throws SshException
  {
    try
    {
      if (this.jb)
        throw new SshException("The connection has already been authenticated!", 4);
      if (paramSshAuthentication.getUsername() == null)
        paramSshAuthentication.setUsername(this.hb);
      Object localObject;
      if ((paramSshAuthentication instanceof PasswordAuthentication))
      {
        localObject = new ByteArrayWriter();
        ((ByteArrayWriter)localObject).write(9);
        ((ByteArrayWriter)localObject).writeString(((PasswordAuthentication)paramSshAuthentication).getPassword());
        this.lb.d(((ByteArrayWriter)localObject).toByteArray());
        this.jb = this.lb.f();
        if (this.jb)
        {
          this.db = paramSshAuthentication;
          return 1;
        }
        return 2;
      }
      SshRsaPublicKey localSshRsaPublicKey;
      ByteArrayWriter localByteArrayWriter;
      if ((paramSshAuthentication instanceof Ssh1RhostsRsaAuthentication))
      {
        localObject = (Ssh1RhostsRsaAuthentication)paramSshAuthentication;
        if (((((Ssh1RhostsRsaAuthentication)localObject).getPublicKey() instanceof SshRsaPublicKey)) && ((((Ssh1RhostsRsaAuthentication)localObject).getPrivateKey() instanceof SshRsaPrivateCrtKey)))
        {
          localSshRsaPublicKey = (SshRsaPublicKey)((Ssh1RhostsRsaAuthentication)localObject).getPublicKey();
          localByteArrayWriter = new ByteArrayWriter();
          localByteArrayWriter.write(35);
          localByteArrayWriter.writeString(((Ssh1RhostsRsaAuthentication)localObject).getClientUsername());
          localByteArrayWriter.writeInt(localSshRsaPublicKey.getBitLength());
          localByteArrayWriter.writeMPINT(localSshRsaPublicKey.getPublicExponent());
          localByteArrayWriter.writeMPINT(localSshRsaPublicKey.getModulus());
          if (gb.isDebugEnabled())
            gb.debug("Sending SSH_CMSG_AUTH_RHOSTS_RSA");
          this.lb.d(localByteArrayWriter.toByteArray());
          this.jb = b(true, (SshRsaPrivateCrtKey)((Ssh1RhostsRsaAuthentication)localObject).getPrivateKey());
          if (this.jb)
          {
            this.db = paramSshAuthentication;
            return 1;
          }
          return 2;
        }
        throw new SshException("Only SSH1 RSA keys are suitable for SSH1 hostbased authentication", 4);
      }
      if ((paramSshAuthentication instanceof PublicKeyAuthentication))
      {
        localObject = (PublicKeyAuthentication)paramSshAuthentication;
        if ((((PublicKeyAuthentication)localObject).getPublicKey() instanceof SshRsaPublicKey))
        {
          localSshRsaPublicKey = (SshRsaPublicKey)((PublicKeyAuthentication)localObject).getPublicKey();
          localByteArrayWriter = new ByteArrayWriter();
          localByteArrayWriter.write(6);
          localByteArrayWriter.writeMPINT(localSshRsaPublicKey.getModulus());
          if (gb.isDebugEnabled())
            gb.debug("Sending SSH_CMSG_AUTH_RSA");
          this.lb.d(localByteArrayWriter.toByteArray());
          this.jb = b(((PublicKeyAuthentication)localObject).isAuthenticating(), (SshRsaPrivateCrtKey)((PublicKeyAuthentication)localObject).getPrivateKey());
          if (((PublicKeyAuthentication)localObject).isAuthenticating())
          {
            if (this.jb)
            {
              this.db = paramSshAuthentication;
              return 1;
            }
            return 2;
          }
          return 5;
        }
        throw new SshException("Only SSH1 RSA private keys are acceptable for SSH1 RSA Authentication", 4);
      }
      if ((paramSshAuthentication instanceof Ssh1ChallengeResponseAuthentication))
      {
        if (((Ssh1ChallengeResponseAuthentication)paramSshAuthentication).getPrompt() == null)
          throw new SshException("SSH1 challenge-response requires prompt!", 4);
        localObject = new ByteArrayWriter();
        ((ByteArrayWriter)localObject).write(39);
        if (gb.isDebugEnabled())
          gb.debug("Sending SSH_CMSG_AUTH_TIS");
        this.lb.d(((ByteArrayWriter)localObject).toByteArray());
        this.jb = b((Ssh1ChallengeResponseAuthentication)paramSshAuthentication);
        if (this.jb)
        {
          this.db = paramSshAuthentication;
          return 1;
        }
        return 2;
      }
      throw new SshException("Unsupported SSH1 authentication type!", 4);
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Ssh1Client.authenticate caught an IOException: " + localIOException.getMessage(), 5);
  }

  private boolean b(Ssh1ChallengeResponseAuthentication paramSsh1ChallengeResponseAuthentication)
    throws SshException
  {
    try
    {
      SshMessage localSshMessage = new SshMessage(this.lb.nextMessage());
      if (localSshMessage.getMessageId() == 40)
      {
        if (gb.isDebugEnabled())
          gb.debug("Received SSH_SMSG_AUTH_TIS_CHALLENGE");
        String str1 = localSshMessage.readString();
        String str2 = paramSsh1ChallengeResponseAuthentication.getPrompt().getResponse(str1);
        if (str2 != null)
        {
          ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
          localByteArrayWriter.write(41);
          localByteArrayWriter.writeString(str2);
          if (gb.isDebugEnabled())
            gb.debug("Sending SSH_CMSG_AUTH_TIS_RESPONSE");
          this.lb.d(localByteArrayWriter.toByteArray());
          return this.lb.f();
        }
        return false;
      }
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Ssh1Client.performChallengeResponse() caught an IOException: " + localIOException.getMessage(), 5);
  }

  private boolean b(boolean paramBoolean, SshRsaPrivateCrtKey paramSshRsaPrivateCrtKey)
    throws SshException
  {
    try
    {
      SshMessage localSshMessage = new SshMessage(this.lb.nextMessage());
      if (localSshMessage.getMessageId() == 7)
      {
        if (gb.isDebugEnabled())
          gb.debug("Received SSH_SMSG_AUTH_RSA_CHALLENGE");
        byte[] arrayOfByte = new byte[16];
        if ((paramBoolean) && (paramSshRsaPrivateCrtKey != null))
        {
          localObject = localSshMessage.readMPINT();
          localObject = paramSshRsaPrivateCrtKey.doPrivate((BigInteger)localObject);
          arrayOfByte = ((BigInteger)localObject).toByteArray();
          Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("MD5");
          if (arrayOfByte[0] == 0)
            localDigest.putBytes(arrayOfByte, 1, 32);
          else
            localDigest.putBytes(arrayOfByte, 0, 32);
          localDigest.putBytes(this.lb.q);
          arrayOfByte = localDigest.doFinal();
        }
        Object localObject = new ByteArrayWriter();
        ((ByteArrayWriter)localObject).write(8);
        ((ByteArrayWriter)localObject).write(arrayOfByte);
        if (gb.isDebugEnabled())
          gb.debug("Sending SSH_CMSG_AUTH_RSA_RESPONSE");
        this.lb.d(((ByteArrayWriter)localObject).toByteArray());
        return this.lb.f();
      }
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Ssh1Client.performRSAChallenge() caught an IOException: " + localIOException.getMessage(), 5);
  }

  public SshSession openSessionChannel()
    throws SshException, ChannelOpenException
  {
    return openSessionChannel(null);
  }

  public SshSession openSessionChannel(long paramLong)
    throws SshException, ChannelOpenException
  {
    return openSessionChannel(null, paramLong);
  }

  public SshSession openSessionChannel(ChannelEventListener paramChannelEventListener)
    throws SshException, ChannelOpenException
  {
    return openSessionChannel(paramChannelEventListener, 0L);
  }

  public SshSession openSessionChannel(ChannelEventListener paramChannelEventListener, long paramLong)
    throws SshException, ChannelOpenException
  {
    if (!this.jb)
      throw new SshException("The connection must be authenticated first!", 4);
    if (this.kb == null)
    {
      this.kb = new b(this.lb, this, paramChannelEventListener, this.eb, paramLong);
      if ((this.lb.b.getX11Display() != null) && (!this.kb.ob))
        this.kb.b(this.lb.b.getX11Display(), this.lb.b.getX11RequestListener());
      return this.kb;
    }
    return duplicate().openSessionChannel(paramChannelEventListener);
  }

  public SshTunnel openForwardingChannel(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, int paramInt3, SshTransport paramSshTransport, ChannelEventListener paramChannelEventListener)
    throws SshException, ChannelOpenException
  {
    if ((this.kb == null) || (!this.kb.bc))
      throw new SshException("SSH1 forwarding channels can only be opened after the user's shell has been started!", 4);
    return this.kb.b(paramString1, paramInt1, paramString2, paramInt2, paramString3, paramInt3, paramSshTransport, paramChannelEventListener);
  }

  public SshClient openRemoteClient(String paramString1, int paramInt, String paramString2, SshConnector paramSshConnector)
    throws SshException, ChannelOpenException
  {
    SshTunnel localSshTunnel = openForwardingChannel(paramString1, paramInt, "127.0.0.1", 22, "127.0.0.1", 22, null, null);
    return paramSshConnector.connect(localSshTunnel, paramString2);
  }

  public SshClient openRemoteClient(String paramString1, int paramInt, String paramString2)
    throws SshException, ChannelOpenException
  {
    return openRemoteClient(paramString1, paramInt, paramString2, this.fb);
  }

  public boolean requestXForwarding(String paramString, ForwardingRequestListener paramForwardingRequestListener)
    throws SshException
  {
    if ((this.kb != null) && (this.kb.bc))
      throw new SshException("SSH1 X forwarding requests must be made after opening the session but before starting the shell!", 4);
    if (this.kb == null)
      throw new SshException("SSH1 X forwarding requests must be made after opening the session but before starting the shell!", 4);
    this.lb.b.setX11Display(paramString);
    this.lb.b.setX11RequestListener(paramForwardingRequestListener);
    this.kb.b(paramString, paramForwardingRequestListener);
    return this.kb.ob;
  }

  public boolean requestRemoteForwarding(String paramString1, int paramInt1, String paramString2, int paramInt2, ForwardingRequestListener paramForwardingRequestListener)
    throws SshException
  {
    if ((this.kb != null) && (this.kb.bc))
      throw new SshException("SSH1 forwarding requests must be made after opening the session but before starting the shell!", 4);
    if (this.kb == null)
      throw new SshException("SSH1 forwarding requests must be made after opening the session but before starting the shell!", 4);
    return this.kb.b(paramInt1, paramString2, paramInt2, paramForwardingRequestListener);
  }

  public boolean cancelRemoteForwarding(String paramString, int paramInt)
    throws SshException
  {
    return false;
  }

  public void disconnect()
  {
    try
    {
      if (this.kb != null)
        this.kb.signalClosingState();
      this.lb.c("The user disconnected the application");
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void exit()
  {
    try
    {
      if (this.kb != null)
        this.kb.signalClosingState();
      this.lb.c("The user disconnected the application");
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public boolean isConnected()
  {
    return this.lb.e() == 2;
  }

  public String getUsername()
  {
    return this.hb;
  }

  public SshClient duplicate()
    throws SshException
  {
    if ((this.hb == null) || (this.db == null))
      throw new SshException("Cannot duplicate! The existing connection does not have a set of credentials", 4);
    try
    {
      SshClient localSshClient = this.fb.connect(this.lb.r.duplicate(), this.hb, this.eb, this.lb.b);
      if ((!localSshClient.isAuthenticated()) && (localSshClient.authenticate(this.db) != 1))
        throw new SshException("Duplication attempt failed to authenicate user!", 5);
      return localSshClient;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Failed to duplicate SshClient", 10);
  }

  public SshContext getContext()
  {
    return this.lb.b;
  }

  public int getChannelCount()
  {
    if (this.kb == null)
      return 0;
    return this.kb.getChannelCount();
  }

  public int getVersion()
  {
    return 1;
  }

  public boolean isBuffered()
  {
    return this.eb;
  }

  public String toString()
  {
    return "SSH1 " + this.lb.r.getHost() + ":" + this.lb.r.getPort() + "[" + "cipher=" + (this.lb.t == null ? "none" : this.lb.t.getAlgorithm()) + "]";
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh1.Ssh1Client
 * JD-Core Version:    0.6.0
 */