package com.maverick.ssh.components;

import com.maverick.events.EventLog;
import com.maverick.ssh.SshException;
import java.math.BigInteger;

public abstract class ComponentManager
{
  private static boolean K = false;
  private static boolean R = false;
  private static ComponentManager U;
  ComponentFactory W;
  ComponentFactory P;
  ComponentFactory L;
  ComponentFactory S;
  ComponentFactory Q;
  ComponentFactory N;
  ComponentFactory O;
  ComponentFactory T;
  ComponentFactory M;
  static Object V = new Object();

  public static boolean isEnableNoneCipher()
  {
    return R;
  }

  public static void setEnableNoneCipher(boolean paramBoolean)
  {
    R = paramBoolean;
  }

  public static void setPerContextAlgorithmPreferences(boolean paramBoolean)
  {
    K = paramBoolean;
  }

  public static boolean getPerContextAlgorithmPreferences()
  {
    return K;
  }

  public static ComponentManager getInstance()
  {
    synchronized (V)
    {
      if (U != null)
        return U;
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
        U = (ComponentManager)localClass.newInstance();
        U.init();
        return U;
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
          U = (ComponentManager)localClass.newInstance();
          U.init();
          return U;
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
    EventLog.LogEvent(this, "Initializing SSH1 server->client ciphers");
    this.W = new ComponentFactory(SshCipher.class);
    initializeSsh1CipherFactory(this.W);
    EventLog.LogEvent(this, "Initializing SSH1 client-server ciphers");
    this.L = new ComponentFactory(SshCipher.class);
    initializeSsh1CipherFactory(this.L);
    EventLog.LogEvent(this, "Initializing SSH2 server->client ciphers");
    this.P = new ComponentFactory(SshCipher.class);
    initializeSsh2CipherFactory(this.P);
    if (R)
    {
      this.P.add("none", NoneCipher.class);
      EventLog.LogEvent(this, "   none will be a supported cipher");
    }
    EventLog.LogEvent(this, "Initializing SSH2 client->server ciphers");
    this.S = new ComponentFactory(SshCipher.class);
    initializeSsh2CipherFactory(this.S);
    if (R)
    {
      this.S.add("none", NoneCipher.class);
      EventLog.LogEvent(this, "   none will be a supported cipher");
    }
    EventLog.LogEvent(this, "Initializing SSH2 server->client HMACs");
    this.N = new ComponentFactory(SshHmac.class);
    initializeHmacFactory(this.N);
    EventLog.LogEvent(this, "Initializing SSH2 client->server HMACs");
    this.Q = new ComponentFactory(SshHmac.class);
    initializeHmacFactory(this.Q);
    EventLog.LogEvent(this, "Initializing SSH2 key exchanges");
    this.O = new ComponentFactory(SshKeyExchange.class);
    initializeKeyExchangeFactory(this.O);
    this.T = new ComponentFactory(SshPublicKey.class);
    initializePublicKeyFactory(this.T);
    EventLog.LogEvent(this, "Initializing digests");
    this.M = new ComponentFactory(SshPublicKey.class);
    initializeDigestFactory(this.M);
    EventLog.LogEvent(this, "Initializing Secure Random Number Generator");
    getRND().nextInt();
  }

  protected abstract void initializeSsh1CipherFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeSsh2CipherFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeHmacFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializePublicKeyFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeKeyExchangeFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeDigestFactory(ComponentFactory paramComponentFactory);

  public static void setInstance(ComponentManager paramComponentManager)
  {
    U = paramComponentManager;
  }

  public ComponentFactory supportedSsh1CiphersSC()
  {
    if (K)
      return (ComponentFactory)this.W.clone();
    return this.W;
  }

  public ComponentFactory supportedSsh1CiphersCS()
  {
    if (K)
      return (ComponentFactory)this.L.clone();
    return this.L;
  }

  public ComponentFactory supportedSsh2CiphersSC()
  {
    if (K)
      return (ComponentFactory)this.P.clone();
    return this.P;
  }

  public ComponentFactory supportedSsh2CiphersCS()
  {
    if (K)
      return (ComponentFactory)this.S.clone();
    return this.S;
  }

  public ComponentFactory supportedHMacsSC()
  {
    if (K)
      return (ComponentFactory)this.N.clone();
    return this.N;
  }

  public ComponentFactory supportedHMacsCS()
  {
    if (K)
      return (ComponentFactory)this.Q.clone();
    return this.Q;
  }

  public ComponentFactory supportedKeyExchanges()
  {
    if (K)
      return (ComponentFactory)this.O.clone();
    return this.O;
  }

  public ComponentFactory supportedPublicKeys()
  {
    if (K)
      return (ComponentFactory)this.T.clone();
    return this.T;
  }

  public ComponentFactory supportedDigests()
  {
    if (K)
      return (ComponentFactory)this.M.clone();
    return this.M;
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

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.ComponentManager
 * JD-Core Version:    0.6.0
 */