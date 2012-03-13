package com.maverick.ssh.components.standalone;

import com.maverick.crypto.digests.Hash;
import com.maverick.ssh.components.Digest;

public class MD5Digest extends Hash
  implements Digest
{
  public MD5Digest()
  {
    super("MD5");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.MD5Digest
 * JD-Core Version:    0.6.0
 */