package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha512 extends AbstractHmac
{
  public HmacSha512()
  {
    super("HmacSha512", 64);
  }

  public String getAlgorithm()
  {
    return "hmac-sha512";
  }

  public void init(byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      this.mac = (JCEProvider.getProviderForAlgorithm(this.jceAlgorithm) == null ? Mac.getInstance(this.jceAlgorithm) : Mac.getInstance(this.jceAlgorithm, JCEProvider.getProviderForAlgorithm(this.jceAlgorithm)));
      byte[] arrayOfByte = new byte[System.getProperty("miscomputes.ssh2.hmac.keys", "false").equalsIgnoreCase("true") ? 16 : 32];
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, arrayOfByte.length);
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte, this.jceAlgorithm);
      this.mac.init(localSecretKeySpec);
    }
    catch (Throwable localThrowable)
    {
      throw new SshException(localThrowable);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.HmacSha512
 * JD-Core Version:    0.6.0
 */