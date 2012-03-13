package com.maverick.crypto.publickey;

import com.maverick.crypto.digests.SHA1Digest;
import java.math.BigInteger;

public class RsaPublicKey extends RsaKey
  implements PublicKey
{
  protected BigInteger publicExponent;
  protected static final byte[] ASN_SHA1 = { 48, 33, 48, 9, 6, 5, 43, 14, 3, 2, 26, 5, 0, 4, 20 };

  public RsaPublicKey()
  {
  }

  public RsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(paramBigInteger1);
    this.publicExponent = paramBigInteger2;
  }

  public BigInteger getPublicExponent()
  {
    return this.publicExponent;
  }

  protected void setPublicExponent(BigInteger paramBigInteger)
  {
    this.publicExponent = paramBigInteger;
  }

  public boolean verifySignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    BigInteger localBigInteger = new BigInteger(1, paramArrayOfByte1);
    localBigInteger = Rsa.doPublic(localBigInteger, getModulus(), this.publicExponent);
    localBigInteger = Rsa.removePKCS1(localBigInteger, 1);
    paramArrayOfByte1 = localBigInteger.toByteArray();
    SHA1Digest localSHA1Digest = new SHA1Digest();
    localSHA1Digest.update(paramArrayOfByte2, 0, paramArrayOfByte2.length);
    byte[] arrayOfByte1 = new byte[localSHA1Digest.getDigestSize()];
    localSHA1Digest.doFinal(arrayOfByte1, 0);
    if (arrayOfByte1.length != paramArrayOfByte1.length - ASN_SHA1.length)
      return false;
    byte[] arrayOfByte2 = ASN_SHA1;
    int i = 0;
    for (int j = 0; i < paramArrayOfByte1.length; j++)
    {
      if (i == ASN_SHA1.length)
      {
        arrayOfByte2 = arrayOfByte1;
        j = 0;
      }
      if (paramArrayOfByte1[i] != arrayOfByte2[j])
        return false;
      i++;
    }
    return true;
  }

  public int hashCode()
  {
    int i = getModulus().hashCode();
    i ^= this.publicExponent.hashCode();
    return i;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.publickey.RsaPublicKey
 * JD-Core Version:    0.6.0
 */