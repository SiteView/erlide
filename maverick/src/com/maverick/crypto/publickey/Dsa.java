package com.maverick.crypto.publickey;

import com.maverick.crypto.security.SecureRandom;
import java.math.BigInteger;

public final class Dsa
{
  public static byte[] sign(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, byte[] paramArrayOfByte)
  {
    BigInteger localBigInteger1 = new BigInteger(1, paramArrayOfByte);
    localBigInteger1 = localBigInteger1.mod(paramBigInteger3);
    BigInteger localBigInteger2 = paramBigInteger4.modPow(paramBigInteger1, paramBigInteger2).mod(paramBigInteger3);
    BigInteger localBigInteger3 = paramBigInteger1.modInverse(paramBigInteger3).multiply(localBigInteger1.add(paramBigInteger1.multiply(localBigInteger2))).mod(paramBigInteger3);
    int i = paramArrayOfByte.length;
    byte[] arrayOfByte1 = new byte[i * 2];
    byte[] arrayOfByte2 = b(localBigInteger2, i);
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, i);
    arrayOfByte2 = b(localBigInteger3, i);
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i, i);
    return arrayOfByte1;
  }

  public static boolean verify(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length / 2;
    byte[] arrayOfByte1 = new byte[i];
    byte[] arrayOfByte2 = new byte[i];
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, i);
    System.arraycopy(paramArrayOfByte1, i, arrayOfByte2, 0, i);
    BigInteger localBigInteger1 = new BigInteger(1, paramArrayOfByte2);
    BigInteger localBigInteger2 = new BigInteger(1, arrayOfByte1);
    BigInteger localBigInteger3 = new BigInteger(1, arrayOfByte2);
    localBigInteger1 = localBigInteger1.mod(paramBigInteger3);
    BigInteger localBigInteger4 = localBigInteger3.modInverse(paramBigInteger3);
    BigInteger localBigInteger5 = localBigInteger1.multiply(localBigInteger4).mod(paramBigInteger3);
    BigInteger localBigInteger6 = localBigInteger2.multiply(localBigInteger4).mod(paramBigInteger3);
    BigInteger localBigInteger7 = paramBigInteger4.modPow(localBigInteger5, paramBigInteger2).multiply(paramBigInteger1.modPow(localBigInteger6, paramBigInteger2)).mod(paramBigInteger2).mod(paramBigInteger3);
    return localBigInteger7.compareTo(localBigInteger2) == 0;
  }

  private static byte[] b(BigInteger paramBigInteger, int paramInt)
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

  public static BigInteger generatePublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    return paramBigInteger1.modPow(paramBigInteger3, paramBigInteger2);
  }

  public static DsaPrivateKey generateKey(int paramInt, SecureRandom paramSecureRandom)
  {
    BigInteger localBigInteger5 = BigInteger.valueOf(0L);
    d locald = new d();
    locald.b(paramInt, 80, paramSecureRandom);
    b localb = locald.b();
    BigInteger localBigInteger2 = localb.b();
    BigInteger localBigInteger1 = localb.c();
    BigInteger localBigInteger3 = localb.d();
    BigInteger localBigInteger4;
    do
      localBigInteger4 = new BigInteger(160, paramSecureRandom);
    while ((localBigInteger4.equals(localBigInteger5)) || (localBigInteger4.compareTo(localBigInteger2) >= 0));
    return new DsaPrivateKey(localBigInteger1, localBigInteger2, localBigInteger3, localBigInteger4);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.Dsa
 * JD-Core Version:    0.6.0
 */