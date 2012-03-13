package com.maverick.crypto.publickey;

import com.maverick.crypto.digests.SHA1Digest;
import com.maverick.crypto.security.SecureRandom;
import java.math.BigInteger;

class C
{
  private int C;
  private int E;
  private SecureRandom D;
  private static BigInteger B = BigInteger.valueOf(1L);
  private static BigInteger A = BigInteger.valueOf(2L);

  public void A(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
  {
    this.C = paramInt1;
    this.E = paramInt2;
    this.D = paramSecureRandom;
  }

  private void A(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
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

  public A A()
  {
    byte[] arrayOfByte1 = new byte[20];
    byte[] arrayOfByte2 = new byte[20];
    byte[] arrayOfByte3 = new byte[20];
    byte[] arrayOfByte4 = new byte[20];
    SHA1Digest localSHA1Digest = new SHA1Digest();
    int i = (this.C - 1) / 160;
    byte[] arrayOfByte5 = new byte[this.C / 8];
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
        this.D.nextBytes(arrayOfByte1);
        localSHA1Digest.update(arrayOfByte1, 0, arrayOfByte1.length);
        localSHA1Digest.doFinal(arrayOfByte2, 0);
        System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
        A(arrayOfByte3, arrayOfByte1, 1);
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
      while (!localBigInteger1.isProbablePrime(this.E));
      j = 0;
      int m = 2;
      while (j < 4096)
      {
        for (int n = 0; n < i; n++)
        {
          A(arrayOfByte2, arrayOfByte1, m + n);
          localSHA1Digest.update(arrayOfByte2, 0, arrayOfByte2.length);
          localSHA1Digest.doFinal(arrayOfByte2, 0);
          System.arraycopy(arrayOfByte2, 0, arrayOfByte5, arrayOfByte5.length - (n + 1) * arrayOfByte2.length, arrayOfByte2.length);
        }
        A(arrayOfByte2, arrayOfByte1, m + i);
        localSHA1Digest.update(arrayOfByte2, 0, arrayOfByte2.length);
        localSHA1Digest.doFinal(arrayOfByte2, 0);
        System.arraycopy(arrayOfByte2, arrayOfByte2.length - (arrayOfByte5.length - i * arrayOfByte2.length), arrayOfByte5, 0, arrayOfByte5.length - i * arrayOfByte2.length);
        int tmp344_343 = 0;
        byte[] tmp344_341 = arrayOfByte5;
        tmp344_341[tmp344_343] = (byte)(tmp344_341[tmp344_343] | 0xFFFFFF80);
        localBigInteger5 = new BigInteger(1, arrayOfByte5);
        BigInteger localBigInteger6 = localBigInteger5.mod(localBigInteger1.multiply(A));
        localBigInteger2 = localBigInteger5.subtract(localBigInteger6.subtract(B));
        if ((localBigInteger2.testBit(this.C - 1)) && (localBigInteger2.isProbablePrime(this.E)))
        {
          k = 1;
          break;
        }
        j++;
        m += i + 1;
      }
    }
    BigInteger localBigInteger4 = localBigInteger2.subtract(B).divide(localBigInteger1);
    do
    {
      do
        localBigInteger5 = new BigInteger(this.C, this.D);
      while ((localBigInteger5.compareTo(B) <= 0) || (localBigInteger5.compareTo(localBigInteger2.subtract(B)) >= 0));
      localBigInteger3 = localBigInteger5.modPow(localBigInteger4, localBigInteger2);
    }
    while (localBigInteger3.compareTo(B) <= 0);
    return new A(localBigInteger2, localBigInteger1, localBigInteger3, new B(arrayOfByte1, j));
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.publickey.C
 * JD-Core Version:    0.6.0
 */