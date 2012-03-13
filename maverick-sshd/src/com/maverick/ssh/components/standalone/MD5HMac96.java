package com.maverick.ssh.components.standalone;

import com.maverick.crypto.digests.GeneralHMac;
import com.maverick.crypto.digests.MD5Digest;

public class MD5HMac96 extends AbstractHmac
{
  public MD5HMac96()
  {
    super("hmac-md5-96", new GeneralHMac(new MD5Digest(), 12));
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.MD5HMac96
 * JD-Core Version:    0.6.0
 */