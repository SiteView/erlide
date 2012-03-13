package com.maverick.ssh.components;

import com.maverick.ssh.SshException;
import java.math.BigInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ComponentManager
{
  private static boolean e = false;
  private static boolean g = false;
  static Log d = LogFactory.getLog(ComponentManager.class);
  private static ComponentManager m;
  ComponentFactory o;
  ComponentFactory f;
  ComponentFactory n;
  ComponentFactory c;
  ComponentFactory i;
  ComponentFactory k;
  ComponentFactory h;
  ComponentFactory b;
  ComponentFactory j;
  static Object l = new Object();

  public static boolean isEnableNoneCipher()
  {
    return g;
  }

  public static void setEnableNoneCipher(boolean paramBoolean)
  {
    g = paramBoolean;
  }

  public static void setPerContextAlgorithmPreferences(boolean paramBoolean)
  {
    e = paramBoolean;
  }

  public static boolean getPerContextAlgorithmPreferences()
  {
    return e;
  }

  public static ComponentManager getInstance()
  {
    synchronized (l)
    {
      if (m != null)
        return m;
      String str = null;
      try
      {
        str = System.getProperty("com.maverick.ssh.components.ComponentManager.tryStandaloneCryptographyBeforeJCE", "false");
      }
      catch (SecurityException localSecurityException)
      {
      }
      try
      {
        if ((str != null) && (str.equals("false")))
          localClass = Class.forName("com.maverick.ssh.components.jce.JCEComponentManager");
        else
          localClass = Class.forName("com.maverick.ssh.components.standalone.StandaloneComponentManager");
        m = (ComponentManager)localClass.newInstance();
        m.init();
        return m;
      }
      catch (Throwable localThrowable1)
      {
        try
        {
          Class localClass;
          if ((str != null) && (str.equals("false")))
            localClass = Class.forName("com.maverick.ssh.components.standalone.StandaloneComponentManager");
          else
            localClass = Class.forName("com.maverick.ssh.components.jce.JCEComponentManager");
          m = (ComponentManager)localClass.newInstance();
          m.init();
          return m;
        }
        catch (Throwable localThrowable2)
        {
          throw new RuntimeException("Unable to locate a cryptographic provider");
        }
      }
    }
  }

  protected void init()
    throws SshException
  {
    if (d.isInfoEnabled())
      d.info("Initializing SSH1 server->client ciphers");
    this.o = new ComponentFactory(SshCipher.class);
    initializeSsh1CipherFactory(this.o);
    if (d.isInfoEnabled())
      d.info("Initializing SSH1 client-server ciphers");
    this.n = new ComponentFactory(SshCipher.class);
    initializeSsh1CipherFactory(this.n);
    if (d.isInfoEnabled())
      d.info("Initializing SSH2 server->client ciphers");
    this.f = new ComponentFactory(SshCipher.class);
    initializeSsh2CipherFactory(this.f);
    if (g)
    {
      this.f.add("none", NoneCipher.class);
      if (d.isInfoEnabled())
        d.info("   none will be a supported cipher");
    }
    if (d.isInfoEnabled())
      d.info("Initializing SSH2 client->server ciphers");
    this.c = new ComponentFactory(SshCipher.class);
    initializeSsh2CipherFactory(this.c);
    if (g)
    {
      this.c.add("none", NoneCipher.class);
      if (d.isInfoEnabled())
        d.info("   none will be a supported cipher");
    }
    if (d.isInfoEnabled())
      d.info("Initializing SSH2 server->client HMACs");
    this.k = new ComponentFactory(SshHmac.class);
    initializeHmacFactory(this.k);
    if (d.isInfoEnabled())
      d.info("Initializing SSH2 client->server HMACs");
    this.i = new ComponentFactory(SshHmac.class);
    initializeHmacFactory(this.i);
    if (d.isInfoEnabled())
      d.info("Initializing SSH2 key exchanges");
    this.h = new ComponentFactory(SshKeyExchange.class);
    initializeKeyExchangeFactory(this.h);
    this.b = new ComponentFactory(SshPublicKey.class);
    initializePublicKeyFactory(this.b);
    if (d.isInfoEnabled())
      d.info("Initializing digests");
    this.j = new ComponentFactory(SshPublicKey.class);
    initializeDigestFactory(this.j);
    if (d.isInfoEnabled())
      d.info("Initializing Secure Random Number Generator");
    getRND().nextInt();
    if (d.isInfoEnabled())
      d.info("Secure Random Number Generator Initialized");
  }

  protected abstract void initializeSsh1CipherFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeSsh2CipherFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeHmacFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializePublicKeyFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeKeyExchangeFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeDigestFactory(ComponentFactory paramComponentFactory);

  public static void setInstance(ComponentManager paramComponentManager)
  {
    m = paramComponentManager;
  }

  public ComponentFactory supportedSsh1CiphersSC()
  {
    if (e)
      return (ComponentFactory)this.o.clone();
    return this.o;
  }

  public ComponentFactory supportedSsh1CiphersCS()
  {
    if (e)
      return (ComponentFactory)this.n.clone();
    return this.n;
  }

  public ComponentFactory supportedSsh2CiphersSC()
  {
    if (e)
      return (ComponentFactory)this.f.clone();
    return this.f;
  }

  public ComponentFactory supportedSsh2CiphersCS()
  {
    if (e)
      return (ComponentFactory)this.c.clone();
    return this.c;
  }

  public ComponentFactory supportedHMacsSC()
  {
    if (e)
      return (ComponentFactory)this.k.clone();
    return this.k;
  }

  public ComponentFactory supportedHMacsCS()
  {
    if (e)
      return (ComponentFactory)this.i.clone();
    return this.i;
  }

  public ComponentFactory supportedKeyExchanges()
  {
    if (e)
      return (ComponentFactory)this.h.clone();
    return this.h;
  }

  public ComponentFactory supportedPublicKeys()
  {
    if (e)
      return (ComponentFactory)this.b.clone();
    return this.b;
  }

  public ComponentFactory supportedDigests()
  {
    if (e)
      return (ComponentFactory)this.j.clone();
    return this.j;
  }

  public abstract SshKeyPair generateRsaKeyPair(int paramInt1, int paramInt2)
    throws SshException;

  public abstract SshRsaPublicKey createRsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, int paramInt)
    throws SshException;

  public abstract SshRsaPublicKey createSsh2RsaPublicKey()
    throws SshException;

  public abstract SshRsaPrivateKey createRsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws SshException;

  public abstract SshRsaPrivateCrtKey createRsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6)
    throws SshException;

  public abstract SshRsaPrivateCrtKey createRsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
    throws SshException;

  public abstract SshKeyPair generateDsaKeyPair(int paramInt)
    throws SshException;

  public abstract SshDsaPublicKey createDsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
    throws SshException;

  public abstract SshDsaPublicKey createDsaPublicKey();

  public abstract SshDsaPrivateKey createDsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5)
    throws SshException;

  public abstract SshSecureRandomGenerator getRND()
    throws SshException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.ComponentManager
 * JD-Core Version:    0.6.0
 */