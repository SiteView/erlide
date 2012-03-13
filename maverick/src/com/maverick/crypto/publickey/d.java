package com.maverick.crypto.publickey;

import com.maverick.crypto.digests.SHA1Digest;
import com.maverick.crypto.security.SecureRandom;
import java.math.BigInteger;

class d
{
  private int d;
  private int f;
  private SecureRandom e;
  private static BigInteger c = BigInteger.valueOf(1L);
  private static BigInteger b = BigInteger.valueOf(2L);

  public void b(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
  {
    this.d = paramInt1;
    this.f = paramInt2;
    this.e = paramSecureRandom;
  }

  private void b(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    int i = (paramArrayOfByte2[(paramArrayOfByte2.length - 1)] & 0xFF) + paramInt;
    paramArrayOfByte1[(paramArrayOfByte2.length - 1)] = (byte)i;
    i >>>= 8;
    for (int j = paramArrayOfByte2.length - 2; j >= 0; j--)
    {
      i += (paramArrayOfByte2[j] & 0xFF);
      paramArrayOfByte1[j] = (byte)i;
      i >>>= 8;
    }
  }

  public b b()
  {
    byte[] arrayOfByte1 = new byte[20];
    byte[] arrayOfByte2 = new byte[20];
    byte[] arrayOfByte3 = new byte[20];
    byte[] arrayOfByte4 = new byte[20];
    SHA1Digest localSHA1Digest = new SHA1Digest();
    int i = (this.d - 1) / 160;
    byte[] arrayOfByte5 = new byte[this.d / 8];
    BigInteger localBigInteger1 = null;
    BigInteger localBigInteger2 = null;
    BigInteger localBigInteger3 = null;
    int j = 0;
    int k = 0;
    BigInteger localBigInteger5;
    while (k == 0)
    {
      do
      {
        this.e.nextBytes(arrayOfByte1);
        localSHA1Digest.update(arrayOfByte1, 0, arrayOfByte1.length);
        localSHA1Digest.doFinal(arrayOfByte2, 0);
        System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
        b(arrayOfByte3, arrayOfByte1, 1);
        localSHA1Digest.update(arrayOfByte3, 0, arrayOfByte3.length);
        localSHA1Digest.doFinal(arrayOfByte3, 0);
        for (m = 0; m != arrayOfByte4.length; m++)
          arrayOfByte4[m] = (byte)(arrayOfByte2[m] ^ arrayOfByte3[m]);
        int tmp166_165 = 0;
        byte[] tmp166_163 = arrayOfByte4;
        tmp166_163[tmp166_165] = (byte)(tmp166_163[tmp166_165] | 0xFFFFFF80);
        byte[] tmp177_173 = arrayOfByte4;
        tmp177_173[19] = (byte)(tmp177_173[19] | 0x1);
        localBigInteger1 = new BigInteger(1, arrayOfByte4);
      }
      while (!localBigInteger1.isProbablePrime(this.f));
      j = 0;
      int m = 2;
      while (j < 4096)
      {
        for (int n = 0; n < i; n++)
        {
          b(arrayOfByte2, arrayOfByte1, m + n);
          localSHA1Digest.update(arrayOfByte2, 0, arrayOfByte2.length);
          localSHA1Digest.doFinal(arrayOfByte2, 0);
          System.arraycopy(arrayOfByte2, 0, arrayOfByte5, arrayOfByte5.length - (n + 1) * arrayOfByte2.length, arrayOfByte2.length);
        }
        b(arrayOfByte2, arrayOfByte1, m + i);
        localSHA1Digest.update(arrayOfByte2, 0, arrayOfByte2.length);
        localSHA1Digest.doFinal(arrayOfByte2, 0);
        System.arraycopy(arrayOfByte2, arrayOfByte2.length - (arrayOfByte5.length - i * arrayOfByte2.length), arrayOfByte5, 0, arrayOfByte5.length - i * arrayOfByte2.length);
        int tmp344_343 = 0;
        byte[] tmp344_341 = arrayOfByte5;
        tmp344_341[tmp344_343] = (byte)(tmp344_341[tmp344_343] | 0xFFFFFF80);
        localBigInteger5 = new BigInteger(1, arrayOfByte5);
        BigInteger localBigInteger6 = localBigInteger5.mod(localBigInteger1.multiply(b));
        localBigInteger2 = localBigInteger5.subtract(localBigInteger6.subtract(c));
        if ((localBigInteger2.testBit(this.d - 1)) && (localBigInteger2.isProbablePrime(this.f)))
        {
          k = 1;
          break;
        }
        j++;
        m += i + 1;
      }
    }
    BigInteger localBigInteger4 = localBigInteger2.subtract(c).divide(localBigInteger1);
    do
    {
      do
        localBigInteger5 = new BigInteger(this.d, this.e);
      while ((localBigInteger5.compareTo(c) <= 0) || (localBigInteger5.compareTo(localBigInteger2.subtract(c)) >= 0));
      localBigInteger3 = localBigInteger5.modPow(localBigInteger4, localBigInteger2);
    }
    while (localBigInteger3.compareTo(c) <= 0);
    return new b(localBigInteger2, localBigInteger1, localBigInteger3, new c(arrayOfByte1, j));
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.publickey.d
 * JD-Core Version:    0.6.0
 */