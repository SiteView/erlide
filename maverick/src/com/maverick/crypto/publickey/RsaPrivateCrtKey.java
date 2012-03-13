package com.maverick.crypto.publickey;

import B;
import com.maverick.crypto.digests.SHA1Digest;
import java.math.BigInteger;

public class RsaPrivateCrtKey extends RsaPrivateKey
{
  protected BigInteger publicExponent;
  protected BigInteger primeP;
  protected BigInteger primeQ;
  protected BigInteger primeExponentP;
  protected BigInteger primeExponentQ;
  protected BigInteger crtCoefficient;

  public RsaPrivateCrtKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
  {
    super(paramBigInteger1, paramBigInteger3);
    this.publicExponent = paramBigInteger2;
    this.primeP = paramBigInteger4;
    this.primeQ = paramBigInteger5;
    this.primeExponentP = paramBigInteger6;
    this.primeExponentQ = paramBigInteger7;
    this.crtCoefficient = paramBigInteger8;
  }

  public BigInteger getPublicExponent()
  {
    return this.publicExponent;
  }

  public BigInteger getPrimeP()
  {
    return this.primeP;
  }

  public BigInteger getPrimeQ()
  {
    return this.primeQ;
  }

  public BigInteger getPrimeExponentP()
  {
    return this.primeExponentP;
  }

  public BigInteger getPrimeExponentQ()
  {
    return this.primeExponentQ;
  }

  public BigInteger getCrtCoefficient()
  {
    return this.crtCoefficient;
  }

  public byte[] sign(byte[] paramArrayOfByte)
  {
    SHA1Digest localSHA1Digest = new SHA1Digest();
    localSHA1Digest.update(paramArrayOfByte, 0, paramArrayOfByte.length);
    Object localObject = new byte[localSHA1Digest.getDigestSize()];
    localSHA1Digest.doFinal(localObject, 0);
    byte[] arrayOfByte1 = new byte[localObject.length + ASN_SHA1.length];
    System.arraycopy(ASN_SHA1, 0, arrayOfByte1, 0, ASN_SHA1.length);
    System.arraycopy(localObject, 0, arrayOfByte1, ASN_SHA1.length, localObject.length);
    localObject = arrayOfByte1;
    BigInteger localBigInteger1 = new BigInteger(1, localObject);
    int i = (getModulus().bitLength() + 7) / 8;
    localBigInteger1 = Rsa.padPKCS1(localBigInteger1, 1, i);
    BigInteger localBigInteger2 = null;
    BigInteger localBigInteger3 = getPrimeP();
    BigInteger localBigInteger4 = getPrimeQ();
    BigInteger localBigInteger5 = getPrimeExponentP();
    BigInteger localBigInteger6 = getPrimeExponentQ();
    BigInteger localBigInteger7 = getCrtCoefficient();
    localBigInteger2 = Rsa.doPrivateCrt(localBigInteger1, localBigInteger3, localBigInteger4, localBigInteger5, localBigInteger6, localBigInteger7);
    byte[] arrayOfByte2 = unsignedBigIntToBytes(localBigInteger2, i);
    return (B)arrayOfByte2;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.RsaPrivateCrtKey
 * JD-Core Version:    0.6.0
 */