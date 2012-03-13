package com.maverick.crypto.publickey;

import B;
import com.maverick.crypto.digests.SHA1Digest;
import java.math.BigInteger;

public class RsaPrivateKey extends RsaKey
{
  protected BigInteger privateExponent;
  protected static final byte[] ASN_SHA1 = { 48, 33, 48, 9, 6, 5, 43, 14, 3, 2, 26, 5, 0, 4, 20 };

  public RsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(paramBigInteger1);
    this.privateExponent = paramBigInteger2;
  }

  public BigInteger getPrivateExponent()
  {
    return this.privateExponent;
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
    BigInteger localBigInteger3 = getPrivateExponent();
    BigInteger localBigInteger4 = getModulus();
    localBigInteger2 = Rsa.doPrivate(localBigInteger1, localBigInteger4, localBigInteger3);
    byte[] arrayOfByte2 = unsignedBigIntToBytes(localBigInteger2, i);
    return (B)arrayOfByte2;
  }

  protected static byte[] unsignedBigIntToBytes(BigInteger paramBigInteger, int paramInt)
  {
    byte[] arrayOfByte1 = paramBigInteger.toByteArray();
    byte[] arrayOfByte2 = null;
    if (arrayOfByte1.length > paramInt)
    {
      arrayOfByte2 = new byte[paramInt];
      System.arraycopy(arrayOfByte1, arrayOfByte1.length - paramInt, arrayOfByte2, 0, paramInt);
    }
    else if (arrayOfByte1.length < paramInt)
    {
      arrayOfByte2 = new byte[paramInt];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, paramInt - arrayOfByte1.length, arrayOfByte1.length);
    }
    else
    {
      arrayOfByte2 = arrayOfByte1;
    }
    return arrayOfByte2;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof RsaPrivateKey))
    {
      RsaPrivateKey localRsaPrivateKey = (RsaPrivateKey)paramObject;
      return (localRsaPrivateKey.getBitLength() == getBitLength()) && (localRsaPrivateKey.getModulus().compareTo(getModulus()) == 0) && (localRsaPrivateKey.getPrivateExponent().compareTo(getPrivateExponent()) == 0);
    }
    return false;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.RsaPrivateKey
 * JD-Core Version:    0.6.0
 */