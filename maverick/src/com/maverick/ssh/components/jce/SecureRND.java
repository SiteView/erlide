package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureRND
  implements SshSecureRandomGenerator
{
  SecureRandom b = JCEProvider.getSecureRandom();

  public SecureRND()
    throws NoSuchAlgorithmException
  {
  }

  public void nextBytes(byte[] paramArrayOfByte)
  {
    this.b.nextBytes(paramArrayOfByte);
  }

  public void nextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException
  {
    try
    {
      byte[] arrayOfByte = new byte[paramInt2];
      this.b.nextBytes(arrayOfByte);
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, paramInt2);
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      throw new SshException("ArrayIndexOutOfBoundsException: Index " + paramInt1 + " on actual array length " + paramArrayOfByte.length + " with len=" + paramInt2, 5);
    }
  }

  public int nextInt()
  {
    return this.b.nextInt();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.SecureRND
 * JD-Core Version:    0.6.0
 */