package com.maverick.ssh.components.standalone;

import com.maverick.crypto.digests.GeneralHMac;
import com.maverick.crypto.digests.HMac;
import com.maverick.crypto.digests.SHA1Digest;
import com.maverick.ssh.SshException;
import java.io.IOException;

public class SHA1HMac extends AbstractHmac
{
  public SHA1HMac()
  {
    super("hmac-sha1", new GeneralHMac(new SHA1Digest()));
  }

  public void init(byte[] paramArrayOfByte)
    throws SshException
  {
    byte[] arrayOfByte = new byte[System.getProperty("miscomputes.ssh2.hmac.keys", "false").equalsIgnoreCase("true") ? 16 : getMacLength()];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, arrayOfByte.length);
    try
    {
      this.B.init(arrayOfByte);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.SHA1HMac
 * JD-Core Version:    0.6.0
 */