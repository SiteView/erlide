package com.maverick.ssh;

import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;

public class SshKeyFingerprint
{
  public static final String MD5_FINGERPRINT = "MD5";
  public static final String SHA1_FINGERPRINT = "SHA-1";
  public static final String SHA256_FINGERPRINT = "SHA-256";
  static char[] b = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  public static String getFingerprint(byte[] paramArrayOfByte)
    throws SshException
  {
    return getFingerprint(paramArrayOfByte, "MD5");
  }

  public static String getFingerprint(byte[] paramArrayOfByte, String paramString)
    throws SshException
  {
    Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance(paramString);
    localDigest.putBytes(paramArrayOfByte);
    byte[] arrayOfByte = localDigest.doFinal();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int j = 0; j < arrayOfByte.length; j++)
    {
      int i = arrayOfByte[j] & 0xFF;
      if (j > 0)
        localStringBuffer.append(':');
      localStringBuffer.append(b[(i >>> 4 & 0xF)]);
      localStringBuffer.append(b[(i & 0xF)]);
    }
    return localStringBuffer.toString();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshKeyFingerprint
 * JD-Core Version:    0.6.0
 */