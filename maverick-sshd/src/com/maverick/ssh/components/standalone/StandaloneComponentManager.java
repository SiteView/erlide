package com.maverick.ssh.components.standalone;

import com.maverick.crypto.publickey.Dsa;
import com.maverick.crypto.publickey.DsaPrivateKey;
import com.maverick.crypto.publickey.Rsa;
import com.maverick.crypto.publickey.RsaPrivateCrtKey;
import com.maverick.crypto.security.SecureRandom;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshDsaPrivateKey;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import com.maverick.ssh.components.SshRsaPrivateKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import java.math.BigInteger;

public class StandaloneComponentManager extends ComponentManager
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
    try
    {
      paramComponentFactory.add("diffie-hellman-group14-sha1", Class.forName("com.maverick.ssh.components.standalone.DiffieHellmanGroup14Sha1"));
      paramComponentFactory.add("diffie-hellman-group1-sha1", Class.forName("com.maverick.ssh.components.standalone.DiffieHellmanGroup1Sha1"));
      paramComponentFactory.add("diffie-hellman-group-exchange-sha1", Class.forName("com.maverick.ssh.components.standalone.DiffieHellmanGroupExchangeSha1"));
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
    }
  }

  protected void initializeSsh1CipherFactory(ComponentFactory paramComponentFactory)
  {
    paramComponentFactory.add("2", Ssh1Des.class);
    paramComponentFactory.add("3", Ssh1Des3.class);
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

  public SshKeyPair generateRsaKeyPair(int paramInt1, int paramInt2)
  {
    SshKeyPair localSshKeyPair = new SshKeyPair();
    RsaPrivateCrtKey localRsaPrivateCrtKey = Rsa.generateKey(paramInt1, SecureRandom.getInstance());
    localSshKeyPair.setPrivateKey(new Ssh2RsaPrivateCrtKey(localRsaPrivateCrtKey));
    switch (paramInt2)
    {
    case 1:
      localSshKeyPair.setPublicKey(new Ssh1RsaPublicKey(localRsaPrivateCrtKey.getModulus(), localRsaPrivateCrtKey.getPublicExponent()));
      break;
    case 2:
      localSshKeyPair.setPublicKey(new Ssh2RsaPublicKey(localRsaPrivateCrtKey.getModulus(), localRsaPrivateCrtKey.getPublicExponent()));
      break;
    default:
      throw new IllegalArgumentException("Version parameter must be either 1 or 2");
    }
    return localSshKeyPair;
  }

  protected void initializeDigestFactory(ComponentFactory paramComponentFactory)
  {
    paramComponentFactory.add("MD5", MD5Digest.class);
    paramComponentFactory.add("SHA-1", SHA1Digest.class);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.StandaloneComponentManager
 * JD-Core Version:    0.6.0
 */