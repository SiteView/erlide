package com.maverick.sshd.components.standalone;

import com.maverick.crypto.publickey.Dsa;
import com.maverick.crypto.publickey.DsaPrivateKey;
import com.maverick.crypto.publickey.Rsa;
import com.maverick.crypto.publickey.RsaPrivateCrtKey;
import com.maverick.crypto.security.SecureRandom;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.SshDsaPrivateKey;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import com.maverick.ssh.components.SshRsaPrivateKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.ssh.components.standalone.AES128Cbc;
import com.maverick.ssh.components.standalone.AES128Ctr;
import com.maverick.ssh.components.standalone.AES192Cbc;
import com.maverick.ssh.components.standalone.AES192Ctr;
import com.maverick.ssh.components.standalone.AES256Cbc;
import com.maverick.ssh.components.standalone.AES256Ctr;
import com.maverick.ssh.components.standalone.BlowfishCbc;
import com.maverick.ssh.components.standalone.CAST128Cbc;
import com.maverick.ssh.components.standalone.MD5Digest;
import com.maverick.ssh.components.standalone.MD5HMac;
import com.maverick.ssh.components.standalone.MD5HMac96;
import com.maverick.ssh.components.standalone.SHA1Digest;
import com.maverick.ssh.components.standalone.SHA1HMac;
import com.maverick.ssh.components.standalone.SHA1HMac96;
import com.maverick.ssh.components.standalone.SecureRND;
import com.maverick.ssh.components.standalone.Ssh1RsaPublicKey;
import com.maverick.ssh.components.standalone.Ssh2DsaPrivateKey;
import com.maverick.ssh.components.standalone.Ssh2DsaPublicKey;
import com.maverick.ssh.components.standalone.Ssh2RsaPrivateCrtKey;
import com.maverick.ssh.components.standalone.Ssh2RsaPrivateKey;
import com.maverick.ssh.components.standalone.Ssh2RsaPublicKey;
import com.maverick.ssh.components.standalone.TripleDesCbc;
import com.maverick.ssh.components.standalone.Twofish128Cbc;
import com.maverick.ssh.components.standalone.Twofish192Cbc;
import com.maverick.ssh.components.standalone.Twofish256Cbc;
import com.maverick.sshd.SshContext;
import com.maverick.sshd.components.ServerComponentManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class StandaloneServerComponentManager extends ServerComponentManager
{
  public SshSecureRandomGenerator getRND()
  {
    return SecureRND.getInstance();
  }

  public SshDsaPublicKey createDsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    return new Ssh2DsaPublicKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4);
  }

  public SshDsaPublicKey createDsaPublicKey()
  {
    return new Ssh2DsaPublicKey();
  }

  public SshRsaPublicKey createRsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, int paramInt)
  {
    switch (paramInt)
    {
    case 1:
      return new Ssh1RsaPublicKey(paramBigInteger1, paramBigInteger2);
    case 2:
      return new Ssh2RsaPublicKey(paramBigInteger1, paramBigInteger2);
    }
    throw new RuntimeException("Unsupported SSH version for RSA public keys " + paramInt);
  }

  public SshRsaPublicKey createSsh2RsaPublicKey()
    throws SshException
  {
    return new Ssh2RsaPublicKey();
  }

  public void initializeSsh2CipherFactory(ComponentFactory paramComponentFactory)
  {
    paramComponentFactory.add("3des-cbc", TripleDesCbc.class);
    paramComponentFactory.add("blowfish-cbc", BlowfishCbc.class);
    paramComponentFactory.add("aes128-cbc", AES128Cbc.class);
    paramComponentFactory.add("aes128-ctr", AES128Ctr.class);
    paramComponentFactory.add("aes192-cbc", AES192Cbc.class);
    paramComponentFactory.add("aes192-ctr", AES192Ctr.class);
    paramComponentFactory.add("aes256-cbc", AES256Cbc.class);
    paramComponentFactory.add("aes256-ctr", AES256Ctr.class);
    paramComponentFactory.add("twofish128-cbc", Twofish128Cbc.class);
    paramComponentFactory.add("twofish192-cbc", Twofish192Cbc.class);
    paramComponentFactory.add("twofish256-cbc", Twofish256Cbc.class);
    paramComponentFactory.add("cast128-cbc", CAST128Cbc.class);
  }

  public void initializeHmacFactory(ComponentFactory paramComponentFactory)
  {
    paramComponentFactory.add("hmac-md5", MD5HMac.class);
    paramComponentFactory.add("hmac-md5-96", MD5HMac96.class);
    paramComponentFactory.add("hmac-sha1", SHA1HMac.class);
    paramComponentFactory.add("hmac-sha1-96", SHA1HMac96.class);
  }

  public void initializePublicKeyFactory(ComponentFactory paramComponentFactory)
  {
    paramComponentFactory.add("ssh-dss", Ssh2DsaPublicKey.class);
    paramComponentFactory.add("ssh-rsa", Ssh2RsaPublicKey.class);
  }

  protected void initializeKeyExchangeFactory(ComponentFactory paramComponentFactory)
  {
    paramComponentFactory.add("diffie-hellman-group-exchange-sha1", DiffieHellmanGroupExchangeSha1Server.class);
    paramComponentFactory.add("diffie-hellman-group14-sha1", DiffieHellmanGroup14Sha1Server.class);
    paramComponentFactory.add("diffie-hellman-group1-sha1", DiffieHellmanGroup1Sha1Server.class);
  }

  public SshDsaPrivateKey createDsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5)
  {
    return new Ssh2DsaPrivateKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4);
  }

  public SshRsaPrivateCrtKey createRsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6)
  {
    return new Ssh2RsaPrivateCrtKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, paramBigInteger5, paramBigInteger6);
  }

  public SshRsaPrivateCrtKey createRsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
  {
    return new Ssh2RsaPrivateCrtKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, paramBigInteger5, paramBigInteger6, paramBigInteger7, paramBigInteger8);
  }

  public SshRsaPrivateKey createRsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    return new Ssh2RsaPrivateKey(paramBigInteger1, paramBigInteger2);
  }

  public SshKeyPair generateDsaKeyPair(int paramInt)
  {
    DsaPrivateKey localDsaPrivateKey = Dsa.generateKey(paramInt, SecureRandom.getInstance());
    SshKeyPair localSshKeyPair = new SshKeyPair();
    localSshKeyPair.setPublicKey(new Ssh2DsaPublicKey(localDsaPrivateKey.getP(), localDsaPrivateKey.getQ(), localDsaPrivateKey.getG(), Dsa.generatePublicKey(localDsaPrivateKey.getG(), localDsaPrivateKey.getP(), localDsaPrivateKey.getX())));
    localSshKeyPair.setPrivateKey(new Ssh2DsaPrivateKey(localDsaPrivateKey));
    return localSshKeyPair;
  }

  public SshKeyPair generateRsaKeyPair(int paramInt)
  {
    SshKeyPair localSshKeyPair = new SshKeyPair();
    RsaPrivateCrtKey localRsaPrivateCrtKey = Rsa.generateKey(paramInt, SecureRandom.getInstance());
    localSshKeyPair.setPrivateKey(new Ssh2RsaPrivateCrtKey(localRsaPrivateCrtKey));
    localSshKeyPair.setPublicKey(new Ssh2RsaPublicKey(localRsaPrivateCrtKey.getModulus(), localRsaPrivateCrtKey.getPublicExponent()));
    return localSshKeyPair;
  }

  protected void initializeDigestFactory(ComponentFactory paramComponentFactory)
  {
    paramComponentFactory.add("MD5", MD5Digest.class);
    paramComponentFactory.add("SHA1", SHA1Digest.class);
    paramComponentFactory.add("SHA-1", SHA1Digest.class);
  }

  public void loadKeystore(File paramFile, String paramString1, String paramString2, String paramString3, SshContext paramSshContext)
    throws IOException
  {
    throw new IOException("Standalone provider does not support X509 host key types");
  }

  public void loadKeystore(InputStream paramInputStream, String paramString1, String paramString2, String paramString3, SshContext paramSshContext)
    throws IOException
  {
    throw new IOException("Standalone provider does not support X509 host key types");
  }

  public void loadKeystore(File paramFile, String paramString1, String paramString2, String paramString3, String paramString4, SshContext paramSshContext)
    throws IOException
  {
    throw new IOException("Standalone provider does not support X509 host key types");
  }

  public void loadKeystore(InputStream paramInputStream, String paramString1, String paramString2, String paramString3, String paramString4, SshContext paramSshContext)
    throws IOException
  {
    throw new IOException("Standalone provider does not support X509 host key types");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.components.standalone.StandaloneServerComponentManager
 * JD-Core Version:    0.6.0
 */