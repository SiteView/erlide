package com.maverick.ssh.components.standalone;

import com.maverick.crypto.digests.Hash;
import com.maverick.ssh.components.Digest;

public class SHA1Digest extends Hash
  implements Digest
{
  public SHA1Digest()
  {
    super("SHA-1");
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.SHA1Digest
 * JD-Core Version:    0.6.0
 */