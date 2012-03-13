package com.maverick.crypto.digests;

public class DigestFactory
{
  static DigestProvider b = null;

  public static void setProvider(DigestProvider paramDigestProvider)
  {
    b = paramDigestProvider;
  }

  public static Digest createDigest(String paramString)
  {
    if (b != null)
      return b.createDigest(paramString);
    if (paramString.equals("MD5"))
      return new MD5Digest();
    return new SHA1Digest();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.digests.DigestFactory
 * JD-Core Version:    0.6.0
 */