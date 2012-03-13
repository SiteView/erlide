package com.maverick.ssh.components.standalone;

import com.maverick.crypto.digests.GeneralHMac;
import com.maverick.crypto.digests.MD5Digest;

public class MD5HMac extends AbstractHmac
{
  public MD5HMac()
  {
    super("hmac-md5", new GeneralHMac(new MD5Digest()));
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.MD5HMac
 * JD-Core Version:    0.6.0
 */