package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureRND
  implements SshSecureRandomGenerator
{
  SecureRandom A = JCEProvider.getSecureRandom();

  public SecureRND()
    throws NoSuchAlgorithmException
  {
  }

  public void nextBytes(byte[] paramArrayOfByte)
  {
    this.A.nextBytes(paramArrayOfByte);
  }

  public void nextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException
  {
    try
    {
      byte[] arrayOfByte = new byte[paramInt2];
      this.A.nextBytes(arrayOfByte);
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, paramInt2);
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      throw new SshException("ArrayIndexOutOfBoundsException: Index " + paramInt1 + " on actual array length " + paramArrayOfByte.length + " with len=" + paramInt2, 5);
    }
  }

  public int nextInt()
  {
    return this.A.nextInt();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.SecureRND
 * JD-Core Version:    0.6.0
 */