package com.maverick.crypto.digests;

public class DigestFactory
{
  static DigestProvider A = null;

  public static void setProvider(DigestProvider paramDigestProvider)
  {
    A = paramDigestProvider;
  }

  public static Digest createDigest(String paramString)
  {
    if (A != null)
      return A.createDigest(paramString);
    if (paramString.equals("MD5"))
      return new MD5Digest();
    return new SHA1Digest();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.digests.DigestFactory
 * JD-Core Version:    0.6.0
 */