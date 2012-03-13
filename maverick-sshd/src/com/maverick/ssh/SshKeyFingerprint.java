package com.maverick.ssh;

import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;

public class SshKeyFingerprint
{
  static char[] A = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  public static String getFingerprint(byte[] paramArrayOfByte)
    throws SshException
  {
    Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("MD5");
    localDigest.putBytes(paramArrayOfByte);
    byte[] arrayOfByte = localDigest.doFinal();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int j = 0; j < arrayOfByte.length; j++)
    {
      int i = arrayOfByte[j] & 0xFF;
      if (j > 0)
        localStringBuffer.append(':');
      localStringBuffer.append(A[(i >>> 4 & 0xF)]);
      localStringBuffer.append(A[(i & 0xF)]);
    }
    return localStringBuffer.toString();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.SshKeyFingerprint
 * JD-Core Version:    0.6.0
 */