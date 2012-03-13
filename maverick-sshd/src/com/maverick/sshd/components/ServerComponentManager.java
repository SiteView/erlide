package com.maverick.sshd.components;

import com.maverick.events.EventLog;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.NoneCipher;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshDsaPrivateKey;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshHmac;
import com.maverick.ssh.components.SshKeyExchange;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import com.maverick.ssh.components.SshRsaPrivateKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.sshd.SshContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public abstract class ServerComponentManager
{
  private static boolean E = false;
  private static boolean C = false;
  private static ServerComponentManager J;
  ComponentFactory D;
  ComponentFactory I;
  ComponentFactory B;
  ComponentFactory G;
  ComponentFactory F;
  ComponentFactory A;
  ComponentFactory H;

  public static boolean isEnableNoneCipher()
  {
    return E;
  }

  public static void setEnableNoneCipher(boolean paramBoolean)
  {
    E = paramBoolean;
  }

  public static void setPerContextAlgorithmPreferences(boolean paramBoolean)
  {
    C = paramBoolean;
  }

  public static boolean getPerContextAlgorithmPreferences()
  {
    return C;
  }

  public static ServerComponentManager getInstance()
  {
    synchronized (ServerComponentManager.class)
    {
      if (J != null)
        return J;
      String str = null;
      try
      {
        str = System.getProperty("com.maverick.sshd.components.ServerComponentManager.tryStandaloneCryptographyBeforeJCE");
        if (str == null)
          str = System.getProperty("com.maverick.ssh.components.ComponentManager.tryStandaloneCryptographyBeforeJCE", "false");
      }
      catch (SecurityException localSecurityException)
      {
      }
      try
      {
        if ((str != null) && (str.equalsIgnoreCase("false")))
          localClass = Class.forName("com.maverick.sshd.components.jce.JCEServerComponentManager");
        else
          localClass = Class.forName("com.maverick.sshd.components.standalone.StandaloneServerComponentManager");
        J = (ServerComponentManager)localClass.newInstance();
        J.init();
        return J;
      }
      catch (Throwable localThrowable1)
      {
        try
        {
          Class localClass;
          if ((str != null) && (str.equals("false")))
            localClass = Class.forName("com.maverick.sshd.components.standalone.StandaloneServerComponentManager");
          else
            localClass = Class.forName("com.maverick.sshd.components.jce.JCEServerComponentManager");
          J = (ServerComponentManager)localClass.newInstance();
          J.init();
          return J;
        }
        catch (Throwable localThrowable2)
        {
          localThrowable2.printStackTrace();
          throw new RuntimeException("Unable to create cryptographic provider: " + localThrowable2.getMessage());
        }
      }
    }
  }

  public static void setInstance(ServerComponentManager paramServerComponentManager)
  {
    J = paramServerComponentManager;
  }

  protected void init()
    throws SshException
  {
    EventLog.LogEvent(this, "Initializing SSH1 server->client ciphers");
    EventLog.LogEvent(this, "Initializing SSH2 server->client ciphers");
    this.D = new ComponentFactory(SshCipher.class);
    initializeSsh2CipherFactory(this.D);
    if (E)
    {
      this.D.add("none", NoneCipher.class);
      EventLog.LogEvent(this, "   none will be a supported cipher");
    }
    EventLog.LogEvent(this, "Initializing SSH2 client->server ciphers");
    this.B = new ComponentFactory(SshCipher.class);
    initializeSsh2CipherFactory(this.B);
    if (E)
    {
      this.B.add("none", NoneCipher.class);
      EventLog.LogEvent(this, "   none will be a supported cipher");
    }
    EventLog.LogEvent(this, "Initializing SSH2 server->client HMACs");
    this.I = new ComponentFactory(SshHmac.class);
    initializeHmacFactory(this.I);
    EventLog.LogEvent(this, "Initializing SSH2 client->server HMACs");
    this.G = new ComponentFactory(SshHmac.class);
    initializeHmacFactory(this.G);
    EventLog.LogEvent(this, "Initializing SSH2 key exchanges");
    this.F = new ComponentFactory(SshKeyExchange.class);
    initializeKeyExchangeFactory(this.F);
    EventLog.LogEvent(this, "Initializing public keys");
    this.A = new ComponentFactory(SshPublicKey.class);
    initializePublicKeyFactory(this.A);
    EventLog.LogEvent(this, "Initializing digests");
    this.H = new ComponentFactory(SshPublicKey.class);
    initializeDigestFactory(this.H);
    EventLog.LogEvent(this, "Initializing Secure Random Number Generator");
    getRND().nextInt();
  }

  protected abstract void initializeSsh2CipherFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeHmacFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializePublicKeyFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeKeyExchangeFactory(ComponentFactory paramComponentFactory);

  protected abstract void initializeDigestFactory(ComponentFactory paramComponentFactory);

  public ComponentFactory supportedSsh2CiphersCS()
  {
    if (C)
      return (ComponentFactory)this.B.clone();
    return this.B;
  }

  public ComponentFactory supportedSsh2CiphersSC()
  {
    if (C)
      return (ComponentFactory)this.D.clone();
    return this.D;
  }

  public ComponentFactory supportedHMacsCS()
  {
    if (C)
      return (ComponentFactory)this.G.clone();
    return this.G;
  }

  public ComponentFactory supportedHMacsSC()
  {
    if (C)
      return (ComponentFactory)this.I.clone();
    return this.I;
  }

  public ComponentFactory supportedKeyExchanges()
  {
    if (C)
      return (ComponentFactory)this.F.clone();
    return this.F;
  }

  public ComponentFactory supportedPublicKeys()
  {
    if (C)
      return (ComponentFactory)this.A.clone();
    return this.A;
  }

  public ComponentFactory supportedDigests()
  {
    if (C)
      return (ComponentFactory)this.H.clone();
    return this.H;
  }

  public abstract SshKeyPair generateRsaKeyPair(int paramInt)
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

  public abstract void loadKeystore(File paramFile, String paramString1, String paramString2, String paramString3, SshContext paramSshContext)
    throws IOException;

  public abstract void loadKeystore(InputStream paramInputStream, String paramString1, String paramString2, String paramString3, SshContext paramSshContext)
    throws IOException;

  public abstract void loadKeystore(File paramFile, String paramString1, String paramString2, String paramString3, String paramString4, SshContext paramSshContext)
    throws IOException;

  public abstract void loadKeystore(InputStream paramInputStream, String paramString1, String paramString2, String paramString3, String paramString4, SshContext paramSshContext)
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.components.ServerComponentManager
 * JD-Core Version:    0.6.0
 */