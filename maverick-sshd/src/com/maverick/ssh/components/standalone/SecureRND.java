package com.maverick.ssh.components.standalone;

import com.maverick.crypto.security.SecureRandom;
import com.maverick.ssh.components.SshSecureRandomGenerator;

public class SecureRND
  implements SshSecureRandomGenerator
{
  static SecureRND B;

  public static SecureRND getInstance()
  {
    return B == null ? (SecureRND.B = new SecureRND()) : B;
  }

  public void nextBytes(byte[] paramArrayOfByte)
  {
    SecureRandom.getInstance().nextBytes(paramArrayOfByte);
  }

  public void nextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    SecureRandom.getInstance().nextBytes(paramArrayOfByte, paramInt1, paramInt2);
  }

  public int nextInt()
  {
    return SecureRandom.getInstance().nextInt();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.SecureRND
 * JD-Core Version:    0.6.0
 */