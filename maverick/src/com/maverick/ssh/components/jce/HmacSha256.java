package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha256 extends AbstractHmac
{
  public HmacSha256()
  {
    super("HmacSha256", 32);
  }

  public String getAlgorithm()
  {
    return "hmac-sha256@ssh.com";
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

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.HmacSha256
 * JD-Core Version:    0.6.0
 */