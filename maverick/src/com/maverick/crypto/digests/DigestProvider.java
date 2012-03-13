package com.maverick.crypto.digests;

public abstract interface DigestProvider
{
  public abstract Digest createDigest(String paramString);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.digests.DigestProvider
 * JD-Core Version:    0.6.0
 */