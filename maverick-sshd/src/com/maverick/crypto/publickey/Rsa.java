package com.maverick.crypto.publickey;

import com.maverick.crypto.security.SecureRandom;
import java.math.BigInteger;

public final class Rsa
{
  private static BigInteger A = BigInteger.valueOf(1L);

  public static BigInteger doPrivateCrt(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5)
  {
    return doPrivateCrt(paramBigInteger1, paramBigInteger3, paramBigInteger4, getPrimeExponent(paramBigInteger2, paramBigInteger3), getPrimeExponent(paramBigInteger2, paramBigInteger4), paramBigInteger5);
  }

  public static BigInteger doPrivateCrt(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6)
  {
    if (!paramBigInteger6.equals(paramBigInteger3.modInverse(paramBigInteger2)))
    {
      localBigInteger1 = paramBigInteger2;
      paramBigInteger2 = paramBigInteger3;
      paramBigInteger3 = localBigInteger1;
      localBigInteger1 = paramBigInteger4;
      paramBigInteger4 = paramBigInteger5;
      paramBigInteger5 = localBigInteger1;
    }
    BigInteger localBigInteger1 = paramBigInteger1.modPow(paramBigInteger4, paramBigInteger2);
    BigInteger localBigInteger2 = paramBigInteger1.modPow(paramBigInteger5, paramBigInteger3);
    BigInteger localBigInteger3 = paramBigInteger6.multiply(localBigInteger1.subtract(localBigInteger2)).mod(paramBigInteger2);
    return localBigInteger2.add(localBigInteger3.multiply(paramBigInteger3));
  }

  public static BigInteger getPrimeExponent(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    BigInteger localBigInteger = paramBigInteger2.subtract(A);
    return paramBigInteger1.mod(localBigInteger);
  }

  public static BigInteger padPKCS1(BigInteger paramBigInteger, int paramInt1, int paramInt2)
    throws IllegalStateException
  {
    int i = (paramBigInteger.bitLength() + 7) / 8;
    if (i > paramInt2 - 11)
      throw new IllegalStateException("PKCS1 failed to pad input! input=" + String.valueOf(i) + " padding=" + String.valueOf(paramInt2));
    byte[] arrayOfByte1 = new byte[paramInt2 - i - 3 + 1];
    arrayOfByte1[0] = 0;
    for (int j = 1; j < paramInt2 - i - 3 + 1; j++)
      if (paramInt1 == 1)
      {
        arrayOfByte1[j] = -1;
      }
      else
      {
        byte[] arrayOfByte2 = new byte[1];
        do
          SecureRandom.getInstance().nextBytes(arrayOfByte2);
        while (arrayOfByte2[0] == 0);
        arrayOfByte1[j] = arrayOfByte2[0];
      }
    BigInteger localBigInteger2 = new BigInteger(1, arrayOfByte1);
    localBigInteger2 = localBigInteger2.shiftLeft((i + 1) * 8);
    BigInteger localBigInteger1 = BigInteger.valueOf(paramInt1);
    localBigInteger1 = localBigInteger1.shiftLeft((paramInt2 - 2) * 8);
    localBigInteger1 = localBigInteger1.or(localBigInteger2);
    localBigInteger1 = localBigInteger1.or(paramBigInteger);
    return localBigInteger1;
  }

  public static BigInteger removePKCS1(BigInteger paramBigInteger, int paramInt)
    throws IllegalStateException
  {
    byte[] arrayOfByte1 = paramBigInteger.toByteArray();
    if (arrayOfByte1[0] != paramInt)
      throw new IllegalStateException("PKCS1 padding type " + paramInt + " is not valid");
    for (int i = 1; (i < arrayOfByte1.length) && (arrayOfByte1[i] != 0); i++)
    {
      if ((paramInt != 1) || (arrayOfByte1[i] == -1))
        continue;
      throw new IllegalStateException("Corrupt data found in expected PKSC1 padding");
    }
    if (i == arrayOfByte1.length)
      throw new IllegalStateException("Corrupt data found in expected PKSC1 padding");
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length - i];
    System.arraycopy(arrayOfByte1, i, arrayOfByte2, 0, arrayOfByte2.length);
    return new BigInteger(1, arrayOfByte2);
  }

  public static RsaPrivateCrtKey generateKey(int paramInt, SecureRandom paramSecureRandom)
  {
    return generateKey(paramInt, BigInteger.valueOf(65537L), paramSecureRandom);
  }

  public static RsaPrivateCrtKey generateKey(int paramInt, BigInteger paramBigInteger, SecureRandom paramSecureRandom)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    BigInteger localBigInteger1 = null;
    BigInteger localBigInteger2 = null;
    BigInteger localBigInteger3 = null;
    BigInteger localBigInteger4 = null;
    int i = 0;
    BigInteger localBigInteger5 = BigInteger.valueOf(1L);
    int j = (paramInt + 1) / 2;
    int k = paramInt - j;
    while (i == 0)
    {
      localObject1 = new BigInteger(j, 80, paramSecureRandom);
      localObject2 = new BigInteger(k, 80, paramSecureRandom);
      if (((BigInteger)localObject1).compareTo((BigInteger)localObject2) == 0)
        continue;
      if (((BigInteger)localObject1).compareTo((BigInteger)localObject2) < 0)
      {
        localObject3 = localObject2;
        localObject2 = localObject1;
        localObject1 = localObject3;
      }
      if ((!((BigInteger)localObject1).isProbablePrime(25)) || (!((BigInteger)localObject2).isProbablePrime(25)))
        continue;
      localObject3 = ((BigInteger)localObject1).gcd((BigInteger)localObject2);
      if (((BigInteger)localObject3).compareTo(localBigInteger5) != 0)
        continue;
      localBigInteger4 = ((BigInteger)localObject1).multiply((BigInteger)localObject2);
      if (localBigInteger4.bitLength() != paramInt)
        continue;
      localBigInteger1 = ((BigInteger)localObject1).subtract(localBigInteger5).multiply(((BigInteger)localObject2).subtract(localBigInteger5));
      localBigInteger2 = paramBigInteger.modInverse(localBigInteger1);
      localBigInteger3 = ((BigInteger)localObject2).modInverse((BigInteger)localObject1);
      i = 1;
    }
    return (RsaPrivateCrtKey)(RsaPrivateCrtKey)(RsaPrivateCrtKey)new RsaPrivateCrtKey(localBigInteger4, paramBigInteger, localBigInteger2, (BigInteger)localObject1, (BigInteger)localObject2, getPrimeExponent(localBigInteger2, (BigInteger)localObject1), getPrimeExponent(localBigInteger2, (BigInteger)localObject2), localBigInteger3);
  }

  public static BigInteger doPublic(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    return paramBigInteger1.modPow(paramBigInteger3, paramBigInteger2);
  }

  public static BigInteger doPrivate(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    return doPublic(paramBigInteger1, paramBigInteger2, paramBigInteger3);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.publickey.Rsa
 * JD-Core Version:    0.6.0
 */