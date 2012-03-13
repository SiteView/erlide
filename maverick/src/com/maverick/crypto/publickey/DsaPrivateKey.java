package com.maverick.crypto.publickey;

import com.maverick.crypto.digests.SHA1Digest;
import java.math.BigInteger;

public class DsaPrivateKey extends DsaKey
{
  protected BigInteger x;

  public DsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    super(paramBigInteger1, paramBigInteger2, paramBigInteger3);
    this.x = paramBigInteger4;
  }

  public BigInteger getX()
  {
    return this.x;
  }

  public byte[] sign(byte[] paramArrayOfByte)
  {
    SHA1Digest localSHA1Digest = new SHA1Digest();
    localSHA1Digest.update(paramArrayOfByte, 0, paramArrayOfByte.length);
    byte[] arrayOfByte = new byte[localSHA1Digest.getDigestSize()];
    localSHA1Digest.doFinal(arrayOfByte, 0);
    return Dsa.sign(this.x, this.p, this.q, this.g, arrayOfByte);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof DsaPrivateKey))
    {
      DsaPrivateKey localDsaPrivateKey = (DsaPrivateKey)paramObject;
      return (this.x.equals(localDsaPrivateKey.x)) && (this.p.equals(localDsaPrivateKey.p)) && (this.q.equals(localDsaPrivateKey.q)) && (this.g.equals(localDsaPrivateKey.g));
    }
    return false;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.DsaPrivateKey
 * JD-Core Version:    0.6.0
 */