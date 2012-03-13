package com.maverick.sshd.components.jce;

import com.maverick.events.EventLog;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshDsaPrivateKey;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshHmac;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import com.maverick.ssh.components.SshRsaPrivateKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.ssh.components.jce.AES128Cbc;
import com.maverick.ssh.components.jce.AES128Ctr;
import com.maverick.ssh.components.jce.AES192Cbc;
import com.maverick.ssh.components.jce.AES192Ctr;
import com.maverick.ssh.components.jce.AES256Cbc;
import com.maverick.ssh.components.jce.AES256Ctr;
import com.maverick.ssh.components.jce.AbstractDigest;
import com.maverick.ssh.components.jce.AbstractHmac;
import com.maverick.ssh.components.jce.AbstractJCECipher;
import com.maverick.ssh.components.jce.AbstractKeyExchange;
import com.maverick.ssh.components.jce.ArcFour;
import com.maverick.ssh.components.jce.ArcFour128;
import com.maverick.ssh.components.jce.ArcFour256;
import com.maverick.ssh.components.jce.BlowfishCbc;
import com.maverick.ssh.components.jce.HmacMD5;
import com.maverick.ssh.components.jce.HmacMD596;
import com.maverick.ssh.components.jce.HmacSha1;
import com.maverick.ssh.components.jce.HmacSha196;
import com.maverick.ssh.components.jce.HmacSha256;
import com.maverick.ssh.components.jce.JCEAlgorithms;
import com.maverick.ssh.components.jce.JCEProvider;
import com.maverick.ssh.components.jce.MD5Digest;
import com.maverick.ssh.components.jce.SHA1Digest;
import com.maverick.ssh.components.jce.SecureRND;
import com.maverick.ssh.components.jce.Ssh1RsaPublicKey;
import com.maverick.ssh.components.jce.Ssh2DsaPrivateKey;
import com.maverick.ssh.components.jce.Ssh2DsaPublicKey;
import com.maverick.ssh.components.jce.Ssh2RsaPrivateCrtKey;
import com.maverick.ssh.components.jce.Ssh2RsaPrivateKey;
import com.maverick.ssh.components.jce.Ssh2RsaPublicKey;
import com.maverick.ssh.components.jce.SshX509DsaPublicKey;
import com.maverick.ssh.components.jce.SshX509RsaPublicKey;
import com.maverick.ssh.components.jce.SshX509RsaSha1PublicKey;
import com.maverick.ssh.components.jce.TripleDesCbc;
import com.maverick.ssh.components.jce.TripleDesCtr;
import com.maverick.sshd.SshContext;
import com.maverick.sshd.components.ServerComponentManager;
import com.maverick.sshd.components.SshKeyExchangeServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JCEServerComponentManager extends ServerComponentManager
  implements JCEAlgorithms
{
  SecureRND b;

  public static void initializeDefaultProvider(Provider paramProvider)
  {
    JCEProvider.initializeDefaultProvider(paramProvider);
  }

  public static void initializeProviderForAlgorithm(String paramString, Provider paramProvider)
  {
    JCEProvider.initializeProviderForAlgorithm(paramString, paramProvider);
  }

  public static String getSecureRandomAlgorithm()
  {
    return JCEProvider.getSecureRandomAlgorithm();
  }

  public static void setSecureRandomAlgorithm(String paramString)
  {
    JCEProvider.setSecureRandomAlgorithm(paramString);
  }

  public static Provider getProviderForAlgorithm(String paramString)
  {
    return JCEProvider.getProviderForAlgorithm(paramString);
  }

  public static SecureRandom getSecureRandom()
    throws NoSuchAlgorithmException
  {
    return JCEProvider.getSecureRandom();
  }

  public SshDsaPrivateKey createDsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5)
    throws SshException
  {
    return new Ssh2DsaPrivateKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, paramBigInteger5);
  }

  public SshDsaPublicKey createDsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
    throws SshException
  {
    try
    {
      return new Ssh2DsaPublicKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new SshException(localThrowable);
  }

  public SshDsaPublicKey createDsaPublicKey()
  {
    return new Ssh2DsaPublicKey();
  }

  public SshRsaPrivateCrtKey createRsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6)
    throws SshException
  {
    try
    {
      BigInteger localBigInteger1 = paramBigInteger4.subtract(BigInteger.ONE);
      localBigInteger1 = paramBigInteger3.mod(localBigInteger1);
      BigInteger localBigInteger2 = paramBigInteger5.subtract(BigInteger.ONE);
      localBigInteger2 = paramBigInteger3.mod(localBigInteger2);
      return new Ssh2RsaPrivateCrtKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, paramBigInteger5, localBigInteger1, localBigInteger2, paramBigInteger6);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new SshException(localThrowable);
  }

  public SshRsaPrivateCrtKey createRsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
    throws SshException
  {
    try
    {
      return new Ssh2RsaPrivateCrtKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, paramBigInteger5, paramBigInteger6, paramBigInteger7, paramBigInteger8);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new SshException(localThrowable);
  }

  public SshRsaPrivateKey createRsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws SshException
  {
    try
    {
      return new Ssh2RsaPrivateKey(paramBigInteger1, paramBigInteger2);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new SshException(localThrowable);
  }

  public SshRsaPublicKey createRsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, int paramInt)
    throws SshException
  {
    try
    {
      switch (paramInt)
      {
      case 1:
        return new Ssh1RsaPublicKey(paramBigInteger1, paramBigInteger2);
      case 2:
        return new Ssh2RsaPublicKey(paramBigInteger1, paramBigInteger2);
      }
      throw new SshException("Illegal version number " + paramInt, 5);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new SshException(localThrowable);
  }

  public SshRsaPublicKey createSsh2RsaPublicKey()
    throws SshException
  {
    return new Ssh2RsaPublicKey();
  }

  public SshKeyPair generateDsaKeyPair(int paramInt)
    throws SshException
  {
    try
    {
      KeyPairGenerator localKeyPairGenerator = JCEProvider.getProviderForAlgorithm("DSA") == null ? KeyPairGenerator.getInstance("DSA") : KeyPairGenerator.getInstance("DSA", JCEProvider.getProviderForAlgorithm("DSA"));
      localKeyPairGenerator.initialize(paramInt);
      KeyPair localKeyPair = localKeyPairGenerator.genKeyPair();
      PrivateKey localPrivateKey = localKeyPair.getPrivate();
      PublicKey localPublicKey = localKeyPair.getPublic();
      SshKeyPair localSshKeyPair = new SshKeyPair();
      localSshKeyPair.setPrivateKey(new Ssh2DsaPrivateKey((DSAPrivateKey)localPrivateKey, (DSAPublicKey)localPublicKey));
      localSshKeyPair.setPublicKey(new Ssh2DsaPublicKey((DSAPublicKey)localPublicKey));
      return localSshKeyPair;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
    }
    throw new SshException(localNoSuchAlgorithmException);
  }

  public SshKeyPair generateRsaKeyPair(int paramInt)
    throws SshException
  {
    try
    {
      KeyPairGenerator localKeyPairGenerator = JCEProvider.getProviderForAlgorithm("RSA") == null ? KeyPairGenerator.getInstance("RSA") : KeyPairGenerator.getInstance("RSA", JCEProvider.getProviderForAlgorithm("RSA"));
      localKeyPairGenerator.initialize(paramInt);
      KeyPair localKeyPair = localKeyPairGenerator.genKeyPair();
      PrivateKey localPrivateKey = localKeyPair.getPrivate();
      PublicKey localPublicKey = localKeyPair.getPublic();
      SshKeyPair localSshKeyPair = new SshKeyPair();
      if (!(localPrivateKey instanceof RSAPrivateCrtKey))
        throw new SshException("RSA key generation requires RSAPrivateCrtKey as private key type.", 16);
      localSshKeyPair.setPrivateKey(new Ssh2RsaPrivateCrtKey((RSAPrivateCrtKey)localPrivateKey));
      localSshKeyPair.setPublicKey(new Ssh2RsaPublicKey((RSAPublicKey)localPublicKey));
      return localSshKeyPair;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
    }
    throw new SshException(localNoSuchAlgorithmException);
  }

  public SshSecureRandomGenerator getRND()
    throws SshException
  {
    try
    {
      return this.b == null ? new SecureRND() : this.b;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
    }
    throw new SshException(localNoSuchAlgorithmException);
  }

  protected void initializeDigestFactory(ComponentFactory paramComponentFactory)
  {
    if (F("MD5", MD5Digest.class))
      paramComponentFactory.add("MD5", MD5Digest.class);
    if (F("SHA-1", SHA1Digest.class))
      paramComponentFactory.add("SHA-1", SHA1Digest.class);
    if (F("SHA1", SHA1Digest.class))
      paramComponentFactory.add("SHA1", SHA1Digest.class);
  }

  protected void initializeHmacFactory(ComponentFactory paramComponentFactory)
  {
    if (E("hmac-md5", HmacMD5.class))
      paramComponentFactory.add("hmac-md5", HmacMD5.class);
    if (E("hmac-sha1", HmacSha1.class))
      paramComponentFactory.add("hmac-sha1", HmacSha1.class);
    if (E("hmac-md5-96", HmacMD596.class))
      paramComponentFactory.add("hmac-md5-96", HmacMD596.class);
    if (E("hmac-sha1-96", HmacSha196.class))
      paramComponentFactory.add("hmac-sha1-96", HmacSha196.class);
    if (E("hmac-sha256", HmacSha256.class))
    {
      paramComponentFactory.add("hmac-sha256", HmacSha256.class);
      paramComponentFactory.add("hmac-sha256@ssh.com", HmacSha256.class);
    }
  }

  protected void initializeKeyExchangeFactory(ComponentFactory paramComponentFactory)
  {
    if (H("diffie-hellman-group14-sha1", DiffieHellmanGroup14Sha1JCE.class))
      paramComponentFactory.add("diffie-hellman-group14-sha1", DiffieHellmanGroup14Sha1JCE.class);
    if (H("diffie-hellman-group1-sha1", DiffieHellmanGroup1Sha1JCE.class))
      paramComponentFactory.add("diffie-hellman-group1-sha1", DiffieHellmanGroup1Sha1JCE.class);
    if (H("diffie-hellman-group-exchange-sha1", DiffieHellmanGroupExchangeSha1JCE.class))
      paramComponentFactory.add("diffie-hellman-group-exchange-sha1", DiffieHellmanGroupExchangeSha1JCE.class);
  }

  protected void initializePublicKeyFactory(ComponentFactory paramComponentFactory)
  {
    paramComponentFactory.add("ssh-dss", Ssh2DsaPublicKey.class);
    paramComponentFactory.add("ssh-rsa", Ssh2RsaPublicKey.class);
    paramComponentFactory.add("x509v3-sign-rsa", SshX509RsaPublicKey.class);
    paramComponentFactory.add("x509v3-sign-dss", SshX509DsaPublicKey.class);
    paramComponentFactory.add("x509v3-sign-rsa-sha1", SshX509RsaSha1PublicKey.class);
  }

  protected void initializeSsh2CipherFactory(ComponentFactory paramComponentFactory)
  {
    if (G("3des-ctr", TripleDesCtr.class))
      paramComponentFactory.add("3des-ctr", TripleDesCtr.class);
    if (G("aes128-ctr", AES128Ctr.class))
      paramComponentFactory.add("aes128-ctr", AES128Ctr.class);
    if (G("aes192-ctr", AES192Ctr.class))
      paramComponentFactory.add("aes192-ctr", AES192Ctr.class);
    if (G("aes256-ctr", AES256Ctr.class))
      paramComponentFactory.add("aes256-ctr", AES256Ctr.class);
    if (G("3des-cbc", TripleDesCbc.class))
      paramComponentFactory.add("3des-cbc", TripleDesCbc.class);
    if (G("blowfish-cbc", BlowfishCbc.class))
      paramComponentFactory.add("blowfish-cbc", BlowfishCbc.class);
    if (G("aes128-cbc", AES128Cbc.class))
      paramComponentFactory.add("aes128-cbc", AES128Cbc.class);
    if (G("aes192-cbc", AES192Cbc.class))
      paramComponentFactory.add("aes192-cbc", AES192Cbc.class);
    if (G("aes256-cbc", AES256Cbc.class))
      paramComponentFactory.add("aes256-cbc", AES256Cbc.class);
    if (G("arcfour", ArcFour.class))
      paramComponentFactory.add("arcfour", ArcFour.class);
    if (G("arcfour128", ArcFour128.class))
      paramComponentFactory.add("arcfour128", ArcFour128.class);
    if (G("arcfour256", ArcFour256.class))
      paramComponentFactory.add("arcfour256", ArcFour256.class);
  }

  private boolean G(String paramString, Class paramClass)
  {
    try
    {
      SshCipher localSshCipher = (SshCipher)paramClass.newInstance();
      byte[] arrayOfByte = new byte[1024];
      localSshCipher.init(0, arrayOfByte, arrayOfByte);
      if ((localSshCipher instanceof AbstractJCECipher))
        EventLog.LogEvent(this, "   " + paramString + " will be supported using JCE Provider " + ((AbstractJCECipher)localSshCipher).getProvider());
      return true;
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "   " + paramString + " will not be supported: " + localThrowable.getMessage());
    }
    return false;
  }

  private boolean H(String paramString, Class paramClass)
  {
    SshKeyExchangeServer localSshKeyExchangeServer = null;
    try
    {
      localSshKeyExchangeServer = (SshKeyExchangeServer)paramClass.newInstance();
      String str1 = "SSH-2.0-SOFTWARE_VERSION_COMMENTS";
      String str2 = "SSH-2.0-ExampleSSHD_1.2.3_Comments";
      byte[] arrayOfByte1 = { 20, 9, 23, -34, -78, 80, 43, 43, -33, -62, 73, 10, 4, 125, -72, -88, -20, 0, 0, 0, 27, 100, 105, 102, 102, 105, 101, 45, 104, 101, 108, 108, 109, 97, 110, 45, 103, 114, 111, 117, 112, 49, 52, 45, 115, 104, 97, 49, 0, 0, 0, 15, 115, 115, 104, 45, 100, 115, 115, 44, 115, 115, 104, 45, 114, 115, 97, 0, 0, 0, 32, 97, 101, 115, 49, 50, 56, 45, 99, 98, 99, 44, 51, 100, 101, 115, 45, 99, 98, 99, 44, 98, 108, 111, 119, 102, 105, 115, 104, 45, 99, 98, 99, 0, 0, 0, 32, 97, 101, 115, 49, 50, 56, 45, 99, 98, 99, 44, 51, 100, 101, 115, 45, 99, 98, 99, 44, 98, 108, 111, 119, 102, 105, 115, 104, 45, 99, 98, 99, 0, 0, 0, 18, 104, 109, 97, 99, 45, 115, 104, 97, 49, 44, 104, 109, 97, 99, 45, 109, 100, 53, 0, 0, 0, 18, 104, 109, 97, 99, 45, 115, 104, 97, 49, 44, 104, 109, 97, 99, 45, 109, 100, 53, 0, 0, 0, 9, 110, 111, 110, 101, 44, 122, 108, 105, 98, 0, 0, 0, 9, 110, 111, 110, 101, 44, 122, 108, 105, 98, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      byte[] arrayOfByte2 = { 20, 23, 119, -40, -10, 11, -1, -102, 84, -3, 119, 47, -92, 81, 17, -51, -53, 0, 0, 0, 54, 100, 105, 102, 102, 105, 101, 45, 104, 101, 108, 108, 109, 97, 110, 45, 103, 114, 111, 117, 112, 49, 45, 115, 104, 97, 49, 44, 100, 105, 102, 102, 105, 101, 45, 104, 101, 108, 108, 109, 97, 110, 45, 103, 114, 111, 117, 112, 49, 52, 45, 115, 104, 97, 49, 0, 0, 0, 15, 115, 115, 104, 45, 100, 115, 115, 44, 115, 115, 104, 45, 114, 115, 97, 0, 0, 0, 111, 97, 101, 115, 49, 50, 56, 45, 99, 98, 99, 44, 51, 100, 101, 115, 45, 99, 98, 99, 44, 98, 108, 111, 119, 102, 105, 115, 104, 45, 99, 98, 99, 44, 97, 101, 115, 49, 57, 50, 45, 99, 98, 99, 44, 97, 101, 115, 50, 53, 54, 45, 99, 98, 99, 44, 116, 119, 111, 102, 105, 115, 104, 49, 50, 56, 45, 99, 98, 99, 44, 116, 119, 111, 102, 105, 115, 104, 49, 57, 50, 45, 99, 98, 99, 44, 116, 119, 111, 102, 105, 115, 104, 50, 53, 54, 45, 99, 98, 99, 44, 99, 97, 115, 116, 49, 50, 56, 45, 99, 98, 99, 0, 0, 0, 111, 97, 101, 115, 49, 50, 56, 45, 99, 98, 99, 44, 51, 100, 101, 115, 45, 99, 98, 99, 44, 98, 108, 111, 119, 102, 105, 115, 104, 45, 99, 98, 99, 44, 97, 101, 115, 49, 57, 50, 45, 99, 98, 99, 44, 97, 101, 115, 50, 53, 54, 45, 99, 98, 99, 44, 116, 119, 111, 102, 105, 115, 104, 49, 50, 56, 45, 99, 98, 99, 44, 116, 119, 111, 102, 105, 115, 104, 49, 57, 50, 45, 99, 98, 99, 44, 116, 119, 111, 102, 105, 115, 104, 50, 53, 54, 45, 99, 98, 99, 44, 99, 97, 115, 116, 49, 50, 56, 45, 99, 98, 99, 0, 0, 0, 43, 104, 109, 97, 99, 45, 115, 104, 97, 49, 44, 104, 109, 97, 99, 45, 109, 100, 53, 44, 104, 109, 97, 99, 45, 109, 100, 53, 45, 57, 54, 44, 104, 109, 97, 99, 45, 115, 104, 97, 49, 45, 57, 54, 0, 0, 0, 43, 104, 109, 97, 99, 45, 115, 104, 97, 49, 44, 104, 109, 97, 99, 45, 109, 100, 53, 44, 104, 109, 97, 99, 45, 109, 100, 53, 45, 57, 54, 44, 104, 109, 97, 99, 45, 115, 104, 97, 49, 45, 57, 54, 0, 0, 0, 9, 110, 111, 110, 101, 44, 122, 108, 105, 98, 0, 0, 0, 9, 110, 111, 110, 101, 44, 122, 108, 105, 98, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      localSshKeyExchangeServer.init(null, str1, str2, arrayOfByte1, arrayOfByte2, null, null, false, false);
    }
    catch (IOException localIOException)
    {
      EventLog.LogEvent(this, "   " + paramString + " will not be supported: " + localIOException.getMessage());
      return false;
    }
    catch (Throwable localThrowable)
    {
    }
    if ((localSshKeyExchangeServer instanceof AbstractKeyExchange))
      EventLog.LogEvent(this, "   " + paramString + " will be supported using JCE Provider " + ((AbstractKeyExchange)localSshKeyExchangeServer).getProvider());
    return true;
  }

  private boolean F(String paramString, Class paramClass)
  {
    try
    {
      Digest localDigest = (Digest)paramClass.newInstance();
      if ((localDigest instanceof AbstractDigest))
        EventLog.LogEvent(this, "   " + paramString + " will be supported using JCE Provider " + ((AbstractDigest)localDigest).getProvider());
      return true;
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "   " + paramString + " will not be supported: " + localThrowable.getMessage());
    }
    return false;
  }

  private boolean E(String paramString, Class paramClass)
  {
    try
    {
      SshHmac localSshHmac = (SshHmac)paramClass.newInstance();
      byte[] arrayOfByte = new byte[1024];
      localSshHmac.init(arrayOfByte);
      if ((localSshHmac instanceof AbstractHmac))
        EventLog.LogEvent(this, "   " + paramString + " will be supported using JCE Provider " + ((AbstractHmac)localSshHmac).getProvider());
      return true;
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "   " + paramString + " will not be supported: " + localThrowable.getMessage());
    }
    return false;
  }

  public void loadKeystore(InputStream paramInputStream, String paramString1, String paramString2, String paramString3, SshContext paramSshContext)
    throws IOException
  {
    loadKeystore(paramInputStream, paramString1, paramString2, paramString3, "PKCS12", paramSshContext);
  }

  public void loadKeystore(InputStream paramInputStream, String paramString1, String paramString2, String paramString3, String paramString4, SshContext paramSshContext)
    throws IOException
  {
    try
    {
      KeyStore localKeyStore = KeyStore.getInstance(paramString4);
      localKeyStore.load(paramInputStream, paramString2.toCharArray());
      Key localKey = localKeyStore.getKey(paramString1, paramString3.toCharArray());
      X509Certificate localX509Certificate = (X509Certificate)localKeyStore.getCertificate(paramString1);
      String str = localKey.getAlgorithm();
      SshKeyPair localSshKeyPair1 = new SshKeyPair();
      if (str.equals("RSA"))
      {
        localSshKeyPair1.setPublicKey(new SshX509RsaSha1PublicKey(localX509Certificate));
        localSshKeyPair1.setPrivateKey(new Ssh2RsaPrivateKey((RSAPrivateKey)localKey));
        SshKeyPair localSshKeyPair2 = new SshKeyPair();
        localSshKeyPair2.setPublicKey(new SshX509RsaPublicKey(localX509Certificate));
        localSshKeyPair2.setPrivateKey(new Ssh2RsaPrivateKey((RSAPrivateKey)localKey));
        paramSshContext.addHostKey(localSshKeyPair2);
        paramSshContext.addHostKey(localSshKeyPair1);
        return;
      }
      localSshKeyPair1.setPublicKey(new SshX509DsaPublicKey(localX509Certificate));
      localSshKeyPair1.setPrivateKey(new Ssh2DsaPrivateKey((DSAPrivateKey)localKey, (DSAPublicKey)localX509Certificate.getPublicKey()));
      paramSshContext.addHostKey(localSshKeyPair1);
    }
    catch (Throwable localThrowable)
    {
      throw new IOException("Could not load keystore from stream: " + localThrowable.getMessage());
    }
  }

  public void loadKeystore(File paramFile, String paramString1, String paramString2, String paramString3, SshContext paramSshContext)
    throws IOException
  {
    loadKeystore(paramFile, paramString1, paramString2, paramString3, "PKCS12", paramSshContext);
  }

  public void loadKeystore(File paramFile, String paramString1, String paramString2, String paramString3, String paramString4, SshContext paramSshContext)
    throws IOException
  {
    loadKeystore(new FileInputStream(paramFile), paramString1, paramString2, paramString3, paramString4, paramSshContext);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.components.jce.JCEServerComponentManager
 * JD-Core Version:    0.6.0
 */