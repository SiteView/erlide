package com.maverick.ssh2;

import com.maverick.ssh.ForwardingRequestListener;
import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SshContext;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Ssh2Context
  implements SshContext
{
  ComponentFactory hb;
  ComponentFactory eb;
  ComponentFactory cb;
  ComponentFactory y;
  ComponentFactory n;
  ComponentFactory nb;
  ComponentFactory jb;
  ComponentFactory fb;
  public static final String CIPHER_TRIPLEDES_CBC = "3des-cbc";
  public static final String CIPHER_BLOWFISH_CBC = "blowfish-cbc";
  public static final String CIPHER_AES128_CBC = "aes128-cbc";
  public static final String HMAC_SHA1 = "hmac-sha1";
  public static final String HMAC_SHA1_96 = "hmac-sha1-96";
  public static final String HMAC_MD5 = "hmac-md5";
  public static final String HMAC_MD5_96 = "hmac-md5-96";
  public static final String COMPRESSION_NONE = "none";
  public static final String COMPRESSION_ZLIB = "zlib";
  public static final String KEX_DIFFIE_HELLMAN_GROUP1_SHA1 = "diffie-hellman-group1-sha1";
  public static final String KEX_DIFFIE_HELLMAN_GROUP14_SHA1 = "diffie-hellman-group14-sha1";
  public static final String KEX_DIFFIE_HELLMAN_GROUP_EXCHANGE_SHA1 = "diffie-hellman-group-exchange-sha1";
  public static final String PUBLIC_KEY_SSHDSS = "ssh-dss";
  public static final String PUBLIC_KEY_SSHRSA = "ssh-rsa";
  String sb = "3des-cbc";
  String qb = "3des-cbc";
  String m = "hmac-sha1";
  String k = "hmac-sha1";
  String o = "none";
  String l = "none";
  String bb = "diffie-hellman-group1-sha1";
  String ab = "ssh-dss";
  String t = "/usr/libexec/sftp-server";
  int v = 100;
  BannerDisplay u;
  HostKeyVerification w;
  String ob = null;
  byte[] db = null;
  byte[] rb = null;
  ForwardingRequestListener pb = null;
  String mb = "";
  int s = 131072;
  boolean ib = false;
  int x = 30000;
  int gb = 128;
  int z = 0;
  boolean lb = true;
  int kb = 10000;
  int q = 60000;
  static Log r = LogFactory.getLog(Ssh2Context.class);
  MaverickCallbackHandler p = null;

  public Ssh2Context()
    throws SshException
  {
    try
    {
      this.cb = ComponentManager.getInstance().supportedSsh2CiphersCS();
      this.y = ComponentManager.getInstance().supportedSsh2CiphersSC();
      this.n = ComponentManager.getInstance().supportedKeyExchanges();
      this.nb = ComponentManager.getInstance().supportedHMacsCS();
      this.jb = ComponentManager.getInstance().supportedHMacsSC();
      this.fb = ComponentManager.getInstance().supportedPublicKeys();
      if (this.cb.contains("aes128-cbc"))
        this.sb = "aes128-cbc";
      if (this.y.contains("aes128-cbc"))
        this.qb = "aes128-cbc";
      if (r.isDebugEnabled())
        r.debug("Creating compression factory");
      this.eb = new ComponentFactory(Class.forName("com.maverick.ssh.compression.SshCompression"));
      if (r.isDebugEnabled())
        r.debug("Adding None Compression");
      this.eb.add("none", Class.forName("java.lang.Object"));
      try
      {
        if (r.isDebugEnabled())
          r.debug("Adding ZLib Compression");
        this.eb.add("zlib", Class.forName("com.sshtools.zlib.ZLibCompression"));
        this.eb.add("zlib@openssh.com", Class.forName("com.sshtools.zlib.OpenSSHZLibCompression"));
      }
      catch (Throwable localThrowable1)
      {
      }
      this.hb = new ComponentFactory(Class.forName("com.maverick.ssh.compression.SshCompression"));
      if (r.isDebugEnabled())
        r.debug("Adding None Compression");
      this.hb.add("none", Class.forName("java.lang.Object"));
      try
      {
        if (r.isDebugEnabled())
          r.debug("Adding ZLib Compression");
        this.hb.add("zlib", Class.forName("com.sshtools.zlib.ZLibCompression"));
        this.hb.add("zlib@openssh.com", Class.forName("com.sshtools.zlib.OpenSSHZLibCompression"));
      }
      catch (Throwable localThrowable2)
      {
      }
    }
    catch (Throwable localThrowable3)
    {
      throw new SshException(localThrowable3.getMessage() != null ? localThrowable3.getMessage() : localThrowable3.getClass().getName(), 5);
    }
    if (r.isDebugEnabled())
      r.debug("Completed Ssh2Context creation");
  }

  public int getMaximumPacketLength()
  {
    return this.s;
  }

  public void setGssCallback(MaverickCallbackHandler paramMaverickCallbackHandler)
  {
    this.p = paramMaverickCallbackHandler;
  }

  public MaverickCallbackHandler getGssCallback()
  {
    return this.p;
  }

  public void setMaximumPacketLength(int paramInt)
  {
    if (paramInt < 35000)
      throw new IllegalArgumentException("The minimum packet length supported must be 35,000 bytes or greater!");
    this.s = paramInt;
  }

  public void setChannelLimit(int paramInt)
  {
    this.v = paramInt;
  }

  public int getChannelLimit()
  {
    return this.v;
  }

  public void setX11Display(String paramString)
  {
    this.ob = paramString;
  }

  public String getX11Display()
  {
    return this.ob;
  }

  public byte[] getX11AuthenticationCookie()
    throws SshException
  {
    if (this.db == null)
    {
      this.db = new byte[16];
      ComponentManager.getInstance().getRND().nextBytes(this.db);
    }
    return this.db;
  }

  public void setX11RealCookie(byte[] paramArrayOfByte)
  {
    this.rb = paramArrayOfByte;
  }

  public byte[] getX11RealCookie()
    throws SshException
  {
    if (this.rb == null)
      this.rb = getX11AuthenticationCookie();
    return this.rb;
  }

  public void setX11RequestListener(ForwardingRequestListener paramForwardingRequestListener)
  {
    this.pb = paramForwardingRequestListener;
  }

  public ForwardingRequestListener getX11RequestListener()
  {
    return this.pb;
  }

  public BannerDisplay getBannerDisplay()
  {
    return this.u;
  }

  public void setBannerDisplay(BannerDisplay paramBannerDisplay)
  {
    this.u = paramBannerDisplay;
  }

  public ComponentFactory supportedCiphersSC()
  {
    return this.y;
  }

  public ComponentFactory supportedCiphersCS()
  {
    return this.cb;
  }

  public String getPreferredCipherCS()
  {
    return this.sb;
  }

  public void setPreferredCipherCS(String paramString)
    throws SshException
  {
    if (paramString == null)
      return;
    if (this.cb.contains(paramString))
    {
      this.sb = paramString;
      setCipherPreferredPositionCS(paramString, 0);
    }
    else
    {
      throw new SshException(paramString + " is not supported", 7);
    }
  }

  public String getPreferredCipherSC()
  {
    return this.qb;
  }

  public String getCiphersCS()
  {
    return this.cb.list(this.sb);
  }

  public String getCiphersSC()
  {
    return this.y.list(this.qb);
  }

  public String getMacsCS()
  {
    return this.nb.list(this.m);
  }

  public String getMacsSC()
  {
    return this.jb.list(this.k);
  }

  public String getPublicKeys()
  {
    return this.fb.list(this.ab);
  }

  public String getKeyExchanges()
  {
    return this.n.list(this.bb);
  }

  public void setPreferredCipherSC(int[] paramArrayOfInt)
    throws SshException
  {
    this.qb = this.y.createNewOrdering(paramArrayOfInt);
  }

  public void setPreferredCipherCS(int[] paramArrayOfInt)
    throws SshException
  {
    this.sb = this.cb.createNewOrdering(paramArrayOfInt);
  }

  public void setCipherPreferredPositionCS(String paramString, int paramInt)
    throws SshException
  {
    this.sb = this.cb.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setCipherPreferredPositionSC(String paramString, int paramInt)
    throws SshException
  {
    this.qb = this.y.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setMacPreferredPositionSC(String paramString, int paramInt)
    throws SshException
  {
    this.k = this.jb.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setMacPreferredPositionCS(String paramString, int paramInt)
    throws SshException
  {
    this.m = this.nb.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setPreferredMacSC(int[] paramArrayOfInt)
    throws SshException
  {
    this.qb = this.jb.createNewOrdering(paramArrayOfInt);
  }

  public void setPreferredMacCS(int[] paramArrayOfInt)
    throws SshException
  {
    this.qb = this.nb.createNewOrdering(paramArrayOfInt);
  }

  public void setPreferredCipherSC(String paramString)
    throws SshException
  {
    if (paramString == null)
      return;
    if (this.y.contains(paramString))
    {
      this.qb = paramString;
      setCipherPreferredPositionSC(paramString, 0);
    }
    else
    {
      throw new SshException(paramString + " is not supported", 7);
    }
  }

  public ComponentFactory supportedMacsSC()
  {
    return this.jb;
  }

  public ComponentFactory supportedMacsCS()
  {
    return this.nb;
  }

  public String getPreferredMacCS()
  {
    return this.m;
  }

  public void setPreferredMacCS(String paramString)
    throws SshException
  {
    if (paramString == null)
      return;
    if (this.nb.contains(paramString))
    {
      this.m = paramString;
      setMacPreferredPositionCS(paramString, 0);
    }
    else
    {
      throw new SshException(paramString + " is not supported", 7);
    }
  }

  public String getPreferredMacSC()
  {
    return this.k;
  }

  public void setPreferredMacSC(String paramString)
    throws SshException
  {
    if (paramString == null)
      return;
    if (this.jb.contains(paramString))
    {
      this.k = paramString;
      setMacPreferredPositionSC(paramString, 0);
    }
    else
    {
      throw new SshException(paramString + " is not supported", 7);
    }
  }

  public ComponentFactory supportedCompressionsSC()
  {
    return this.eb;
  }

  public ComponentFactory supportedCompressionsCS()
  {
    return this.hb;
  }

  public String getPreferredCompressionCS()
  {
    return this.o;
  }

  public void setPreferredCompressionCS(String paramString)
    throws SshException
  {
    if (paramString == null)
      return;
    if (this.hb.contains(paramString))
      this.o = paramString;
    else
      throw new SshException(paramString + " is not supported", 7);
  }

  public String getPreferredCompressionSC()
  {
    return this.l;
  }

  public void setPreferredCompressionSC(String paramString)
    throws SshException
  {
    if (paramString == null)
      return;
    if (this.eb.contains(paramString))
      this.l = paramString;
    else
      throw new SshException(paramString + " is not supported", 7);
  }

  public void enableCompression()
    throws SshException
  {
    supportedCompressionsCS().changePositionofAlgorithm("zlib", 0);
    supportedCompressionsCS().changePositionofAlgorithm("zlib@openssh.com", 1);
    this.o = supportedCompressionsCS().changePositionofAlgorithm("none", 2);
    supportedCompressionsSC().changePositionofAlgorithm("zlib", 0);
    supportedCompressionsSC().changePositionofAlgorithm("zlib@openssh.com", 1);
    this.l = supportedCompressionsSC().changePositionofAlgorithm("none", 2);
  }

  public void disableCompression()
    throws SshException
  {
    supportedCompressionsCS().changePositionofAlgorithm("none", 0);
    supportedCompressionsCS().changePositionofAlgorithm("zlib", 1);
    this.o = supportedCompressionsCS().changePositionofAlgorithm("zlib@openssh.com", 2);
    supportedCompressionsSC().changePositionofAlgorithm("none", 0);
    supportedCompressionsSC().changePositionofAlgorithm("zlib", 1);
    this.l = supportedCompressionsSC().changePositionofAlgorithm("zlib@openssh.com", 2);
  }

  public ComponentFactory supportedKeyExchanges()
  {
    return this.n;
  }

  public String getPreferredKeyExchange()
  {
    return this.bb;
  }

  public void setPreferredKeyExchange(String paramString)
    throws SshException
  {
    if (paramString == null)
      return;
    if (this.n.contains(paramString))
    {
      this.bb = paramString;
      setKeyExchangePreferredPosition(paramString, 0);
    }
    else
    {
      throw new SshException(paramString + " is not supported", 7);
    }
  }

  public ComponentFactory supportedPublicKeys()
  {
    return this.fb;
  }

  public String getPreferredPublicKey()
  {
    return this.ab;
  }

  public void setPreferredPublicKey(String paramString)
    throws SshException
  {
    if (paramString == null)
      return;
    if (this.fb.contains(paramString))
    {
      this.ab = paramString;
      setPublicKeyPreferredPosition(paramString, 0);
    }
    else
    {
      throw new SshException(paramString + " is not supported", 7);
    }
  }

  public void setHostKeyVerification(HostKeyVerification paramHostKeyVerification)
  {
    this.w = paramHostKeyVerification;
  }

  public HostKeyVerification getHostKeyVerification()
  {
    return this.w;
  }

  public void setSFTPProvider(String paramString)
  {
    this.t = paramString;
  }

  public String getSFTPProvider()
  {
    return this.t;
  }

  public void setPartialMessageTimeout(int paramInt)
  {
    this.x = paramInt;
  }

  public int getPartialMessageTimeout()
  {
    return this.x;
  }

  public boolean isKeyReExchangeDisabled()
  {
    return this.ib;
  }

  public void setKeyReExchangeDisabled(boolean paramBoolean)
  {
    this.ib = paramBoolean;
  }

  public void setPublicKeyPreferredPosition(String paramString, int paramInt)
    throws SshException
  {
    this.ab = this.fb.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setKeyExchangePreferredPosition(String paramString, int paramInt)
    throws SshException
  {
    this.bb = this.n.changePositionofAlgorithm(paramString, paramInt);
  }

  public int getIdleConnectionTimeoutSeconds()
  {
    return this.z;
  }

  public void setIdleConnectionTimeoutSeconds(int paramInt)
  {
    this.z = paramInt;
  }

  public boolean isSendIgnorePacketOnIdle()
  {
    return this.lb;
  }

  public void setSendIgnorePacketOnIdle(boolean paramBoolean)
  {
    this.lb = paramBoolean;
  }

  public int getKeepAliveMaxDataLength()
  {
    return this.gb;
  }

  public void setKeepAliveMaxDataLength(int paramInt)
  {
    if (paramInt < 8)
      throw new IllegalArgumentException("There must be at least 8 bytes of random data");
    this.gb = paramInt;
  }

  public int getSocketTimeout()
  {
    return this.kb;
  }

  public void setSocketTimeout(int paramInt)
  {
    this.kb = paramInt;
  }

  public void setMessageTimeout(int paramInt)
  {
    this.q = paramInt;
  }

  public int getMessageTimeout()
  {
    return this.q;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.Ssh2Context
 * JD-Core Version:    0.6.0
 */