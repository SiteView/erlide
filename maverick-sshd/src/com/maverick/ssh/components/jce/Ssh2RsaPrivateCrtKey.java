package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import javax.crypto.Cipher;

public class Ssh2RsaPrivateCrtKey
  implements SshRsaPrivateCrtKey
{
  protected RSAPrivateCrtKey prv;

  public Ssh2RsaPrivateCrtKey(RSAPrivateCrtKey paramRSAPrivateCrtKey)
  {
    this.prv = paramRSAPrivateCrtKey;
  }

  public Ssh2RsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
    throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    KeyFactory localKeyFactory = JCEProvider.getProviderForAlgorithm("RSA") == null ? KeyFactory.getInstance("RSA") : KeyFactory.getInstance("RSA", JCEProvider.getProviderForAlgorithm("RSA"));
    RSAPrivateCrtKeySpec localRSAPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, paramBigInteger5, paramBigInteger6, paramBigInteger7, paramBigInteger8);
    this.prv = ((RSAPrivateCrtKey)localKeyFactory.generatePrivate(localRSAPrivateCrtKeySpec));
  }

  public BigInteger doPrivate(BigInteger paramBigInteger)
    throws SshException
  {
    try
    {
      Cipher localCipher = JCEProvider.getProviderForAlgorithm("RSA") == null ? Cipher.getInstance("RSA") : Cipher.getInstance("RSA", JCEProvider.getProviderForAlgorithm("RSA"));
      localCipher.init(2, this.prv, JCEProvider.getSecureRandom());
      return new BigInteger(localCipher.doFinal(paramBigInteger.toByteArray()));
    }
    catch (Throwable localThrowable)
    {
    }
    throw new SshException(localThrowable);
  }

  public BigInteger getCrtCoefficient()
  {
    return this.prv.getCrtCoefficient();
  }

  public BigInteger getPrimeExponentP()
  {
    return this.prv.getPrimeExponentP();
  }

  public BigInteger getPrimeExponentQ()
  {
    return this.prv.getPrimeExponentQ();
  }

  public BigInteger getPrimeP()
  {
    return this.prv.getPrimeP();
  }

  public BigInteger getPrimeQ()
  {
    return this.prv.getPrimeQ();
  }

  public BigInteger getPublicExponent()
  {
    return this.prv.getPublicExponent();
  }

  public BigInteger getModulus()
  {
    return this.prv.getModulus();
  }

  public BigInteger getPrivateExponent()
  {
    return this.prv.getPrivateExponent();
  }

  public byte[] sign(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      Signature localSignature = JCEProvider.getProviderForAlgorithm("SHA1WithRSA") == null ? Signature.getInstance("SHA1WithRSA") : Signature.getInstance("SHA1WithRSA", JCEProvider.getProviderForAlgorithm("SHA1WithRSA"));
      localSignature.initSign(this.prv);
      localSignature.update(paramArrayOfByte);
      return localSignature.sign();
    }
    catch (Exception localException)
    {
    }
    throw new IOException("Failed to sign data! " + localException.getMessage());
  }

  public String getAlgorithm()
  {
    return "ssh-rsa";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh2RsaPrivateCrtKey
 * JD-Core Version:    0.6.0
 */