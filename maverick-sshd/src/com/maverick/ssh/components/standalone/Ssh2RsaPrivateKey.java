package com.maverick.ssh.components.standalone;

import com.maverick.crypto.publickey.RsaPrivateKey;
import com.maverick.ssh.components.SshRsaPrivateKey;
import java.math.BigInteger;

public class Ssh2RsaPrivateKey extends RsaPrivateKey
  implements SshRsaPrivateKey
{
  public Ssh2RsaPrivateKey(RsaPrivateKey paramRsaPrivateKey)
  {
    super(paramRsaPrivateKey.getModulus(), paramRsaPrivateKey.getPrivateExponent());
  }

  public Ssh2RsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(paramBigInteger1, paramBigInteger2);
  }

  public String getAlgorithm()
  {
    return "ssh-rsa";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Ssh2RsaPrivateKey
 * JD-Core Version:    0.6.0
 */