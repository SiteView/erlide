package com.maverick.ssh.components.jce;

import com.maverick.ssh.components.SshRsaPrivateKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;

public class Ssh2RsaPrivateKey
  implements SshRsaPrivateKey
{
  protected RSAPrivateKey prv;

  public Ssh2RsaPrivateKey(RSAPrivateKey paramRSAPrivateKey)
  {
    this.prv = paramRSAPrivateKey;
  }

  public Ssh2RsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    KeyFactory localKeyFactory = JCEProvider.getProviderForAlgorithm("RSA") == null ? KeyFactory.getInstance("RSA") : KeyFactory.getInstance("RSA", JCEProvider.getProviderForAlgorithm("RSA"));
    RSAPrivateKeySpec localRSAPrivateKeySpec = new RSAPrivateKeySpec(paramBigInteger1, paramBigInteger2);
    this.prv = ((RSAPrivateKey)localKeyFactory.generatePrivate(localRSAPrivateKeySpec));
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

  public BigInteger getModulus()
  {
    return this.prv.getModulus();
  }

  public BigInteger getPrivateExponent()
  {
    return this.prv.getPrivateExponent();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh2RsaPrivateKey
 * JD-Core Version:    0.6.0
 */