package com.maverick.crypto.publickey;

import com.maverick.crypto.digests.SHA1Digest;
import java.math.BigInteger;

public class DsaPublicKey extends DsaKey
  implements PublicKey
{
  protected BigInteger y;

  public DsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    super(paramBigInteger1, paramBigInteger2, paramBigInteger3);
    this.y = paramBigInteger4;
  }

  public DsaPublicKey()
  {
  }

  public BigInteger getY()
  {
    return this.y;
  }

  public int getBitLength()
  {
    return this.p.bitLength();
  }

  public boolean verifySignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    SHA1Digest localSHA1Digest = new SHA1Digest();
    localSHA1Digest.update(paramArrayOfByte2, 0, paramArrayOfByte2.length);
    byte[] arrayOfByte = new byte[localSHA1Digest.getDigestSize()];
    localSHA1Digest.doFinal(arrayOfByte, 0);
    return Dsa.verify(this.y, this.p, this.q, this.g, paramArrayOfByte1, arrayOfByte);
  }

  protected boolean verifySignature(byte[] paramArrayOfByte, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    SHA1Digest localSHA1Digest = new SHA1Digest();
    localSHA1Digest.update(paramArrayOfByte, 0, paramArrayOfByte.length);
    byte[] arrayOfByte = new byte[localSHA1Digest.getDigestSize()];
    localSHA1Digest.doFinal(arrayOfByte, 0);
    BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte);
    localBigInteger1 = localBigInteger1.mod(this.q);
    if ((BigInteger.valueOf(0L).compareTo(paramBigInteger1) >= 0) || (this.q.compareTo(paramBigInteger1) <= 0))
      return false;
    if ((BigInteger.valueOf(0L).compareTo(paramBigInteger2) >= 0) || (this.q.compareTo(paramBigInteger2) <= 0))
      return false;
    BigInteger localBigInteger2 = paramBigInteger2.modInverse(this.q);
    BigInteger localBigInteger3 = localBigInteger1.multiply(localBigInteger2).mod(this.q);
    BigInteger localBigInteger4 = paramBigInteger1.multiply(localBigInteger2).mod(this.q);
    BigInteger localBigInteger5 = this.g.modPow(localBigInteger3, this.p).multiply(this.y.modPow(localBigInteger4, this.p)).mod(this.p).mod(this.q);
    return localBigInteger5.compareTo(paramBigInteger1) == 0;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.publickey.DsaPublicKey
 * JD-Core Version:    0.6.0
 */