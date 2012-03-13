package com.maverick.ssh.components.standalone;

import com.maverick.crypto.publickey.Dsa;
import com.maverick.crypto.publickey.DsaPrivateKey;
import com.maverick.ssh.components.SshDsaPrivateKey;
import com.maverick.ssh.components.SshDsaPublicKey;
import java.math.BigInteger;

public class Ssh2DsaPrivateKey extends DsaPrivateKey
  implements SshDsaPrivateKey
{
  public Ssh2DsaPrivateKey(DsaPrivateKey paramDsaPrivateKey)
  {
    super(paramDsaPrivateKey.getP(), paramDsaPrivateKey.getQ(), paramDsaPrivateKey.getG(), paramDsaPrivateKey.getX());
  }

  public Ssh2DsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    super(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4);
  }

  public SshDsaPublicKey getPublicKey()
  {
    return new Ssh2DsaPublicKey(this.p, this.q, this.g, Dsa.generatePublicKey(this.g, this.p, this.x));
  }

  public String getAlgorithm()
  {
    return "ssh-dss";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Ssh2DsaPrivateKey
 * JD-Core Version:    0.6.0
 */