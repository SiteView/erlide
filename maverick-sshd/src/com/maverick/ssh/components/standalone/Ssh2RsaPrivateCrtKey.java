package com.maverick.ssh.components.standalone;

import com.maverick.crypto.publickey.Rsa;
import com.maverick.crypto.publickey.RsaPrivateCrtKey;
import com.maverick.ssh.components.SshRsaPrivateCrtKey;
import java.math.BigInteger;

public class Ssh2RsaPrivateCrtKey extends RsaPrivateCrtKey
  implements SshRsaPrivateCrtKey
{
  public Ssh2RsaPrivateCrtKey(RsaPrivateCrtKey paramRsaPrivateCrtKey)
  {
    super(paramRsaPrivateCrtKey.getModulus(), paramRsaPrivateCrtKey.getPublicExponent(), paramRsaPrivateCrtKey.getPrivateExponent(), paramRsaPrivateCrtKey.getPrimeP(), paramRsaPrivateCrtKey.getPrimeQ(), paramRsaPrivateCrtKey.getPrimeExponentP(), paramRsaPrivateCrtKey.getPrimeExponentQ(), paramRsaPrivateCrtKey.getCrtCoefficient());
  }

  public Ssh2RsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6)
  {
    this(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, paramBigInteger5, Rsa.getPrimeExponent(paramBigInteger3, paramBigInteger4), Rsa.getPrimeExponent(paramBigInteger3, paramBigInteger5), paramBigInteger6);
  }

  public Ssh2RsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
  {
    super(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, paramBigInteger5, paramBigInteger6, paramBigInteger7, paramBigInteger8);
  }

  public String getAlgorithm()
  {
    return "ssh-rsa";
  }

  public BigInteger doPrivate(BigInteger paramBigInteger)
  {
    paramBigInteger = Rsa.doPrivate(paramBigInteger, getModulus(), getPrivateExponent());
    return Rsa.removePKCS1(paramBigInteger, 2);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Ssh2RsaPrivateCrtKey
 * JD-Core Version:    0.6.0
 */