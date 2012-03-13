package com.maverick.crypto.math;

import I;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Stack;

public class BigInteger
{
  private static final int[][] F = { { 3, 5, 7, 11, 13, 17, 19, 23 }, { 29, 31, 37, 41, 43 }, { 47, 53, 59, 61, 67 }, { 71, 73, 79, 83 }, { 89, 97, 101, 103 }, { 107, 109, 113, 127 }, { 131, 137, 139, 149 }, { 151, 157, 163, 167 }, { 173, 179, 181, 191 }, { 193, 197, 199, 211 }, { 223, 227, 229 }, { 233, 239, 241 }, { 251, 257, 263 }, { 269, 271, 277 }, { 281, 283, 293 }, { 307, 311, 313 }, { 317, 331, 337 }, { 347, 349, 353 }, { 359, 367, 373 }, { 379, 383, 389 }, { 397, 401, 409 }, { 419, 421, 431 }, { 433, 439, 443 }, { 449, 457, 461 }, { 463, 467, 479 }, { 487, 491, 499 }, { 503, 509, 521 }, { 523, 541, 547 }, { 557, 563, 569 }, { 571, 577, 587 }, { 593, 599, 601 }, { 607, 613, 617 }, { 619, 631, 641 }, { 643, 647, 653 }, { 659, 661, 673 }, { 677, 683, 691 }, { 701, 709, 719 }, { 727, 733, 739 }, { 743, 751, 757 }, { 761, 769, 773 }, { 787, 797, 809 }, { 811, 821, 823 }, { 827, 829, 839 }, { 853, 857, 859 }, { 863, 877, 881 }, { 883, 887, 907 }, { 911, 919, 929 }, { 937, 941, 947 }, { 953, 967, 971 }, { 977, 983, 991 }, { 997, 1009, 1013 }, { 1019, 1021, 1031 } };
  private static int[] A;
  private static final int[] L = new int[0];
  public static final BigInteger ZERO = new BigInteger(0, L);
  public static final BigInteger ONE = valueOf(1L);
  private static final BigInteger I = valueOf(2L);
  private static final BigInteger K = valueOf(3L);
  private int D;
  private int[] H;
  private int J = -1;
  private int B = -1;
  private long E = -1L;
  private static final byte[] C;
  private static final byte[] G;

  private BigInteger()
  {
  }

  private BigInteger(int paramInt, int[] paramArrayOfInt)
  {
    if (paramArrayOfInt.length > 0)
    {
      this.D = paramInt;
      for (int i = 0; (i < paramArrayOfInt.length) && (paramArrayOfInt[i] == 0); i++);
      if (i == 0)
      {
        this.H = paramArrayOfInt;
      }
      else
      {
        int[] arrayOfInt = new int[paramArrayOfInt.length - i];
        System.arraycopy(paramArrayOfInt, i, arrayOfInt, 0, arrayOfInt.length);
        this.H = arrayOfInt;
        if (arrayOfInt.length == 0)
          this.D = 0;
      }
    }
    else
    {
      this.H = paramArrayOfInt;
      this.D = 0;
    }
  }

  public BigInteger(String paramString)
    throws NumberFormatException
  {
    this(paramString, 10);
  }

  public BigInteger(String paramString, int paramInt)
    throws NumberFormatException
  {
    if (paramString.length() == 0)
      throw new NumberFormatException("Zero length BigInteger");
    if ((paramInt < 2) || (paramInt > 36))
      throw new NumberFormatException("Radix out of range");
    int i = 0;
    this.D = 1;
    if (paramString.charAt(0) == '-')
    {
      if (paramString.length() == 1)
        throw new NumberFormatException("Zero length BigInteger");
      this.D = -1;
    }
    for (i = 1; (i < paramString.length()) && (Character.digit(paramString.charAt(i), paramInt) == 0); i++);
    if (i >= paramString.length())
    {
      this.D = 0;
      this.H = new int[0];
      return;
    }
    BigInteger localBigInteger1 = ZERO;
    BigInteger localBigInteger2 = valueOf(paramInt);
    while (i < paramString.length())
    {
      localBigInteger1 = localBigInteger1.multiply(localBigInteger2).add(valueOf(Character.digit(paramString.charAt(i), paramInt)));
      i++;
    }
    this.H = localBigInteger1.H;
  }

  public BigInteger(byte[] paramArrayOfByte)
    throws NumberFormatException
  {
    if (paramArrayOfByte.length == 0)
      throw new NumberFormatException("Zero length BigInteger");
    this.D = 1;
    if (paramArrayOfByte[0] < 0)
      this.D = -1;
    this.H = A(paramArrayOfByte, this.D);
    if (this.H.length == 0)
      this.D = 0;
  }

  private int[] A(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramInt >= 0)
    {
      for (j = 0; (j < paramArrayOfByte.length) && (paramArrayOfByte[j] == 0); j++);
      if (j >= paramArrayOfByte.length)
        return new int[0];
      k = (paramArrayOfByte.length - j + 3) / 4;
      m = (paramArrayOfByte.length - j) % 4;
      if (m == 0)
        m = 4;
      localObject = new int[k];
      n = 0;
      i1 = 0;
      for (i = j; i < paramArrayOfByte.length; i++)
      {
        n <<= 8;
        n |= paramArrayOfByte[i] & 0xFF;
        m--;
        if (m > 0)
          continue;
        localObject[i1] = n;
        i1++;
        m = 4;
        n = 0;
      }
      return localObject;
    }
    for (int j = 0; (j < paramArrayOfByte.length - 1) && (paramArrayOfByte[j] == 255); j++);
    int k = paramArrayOfByte.length;
    int m = 0;
    if (paramArrayOfByte[j] == 128)
    {
      for (i = j + 1; (i < paramArrayOfByte.length) && (paramArrayOfByte[i] == 0); i++);
      if (i == paramArrayOfByte.length)
      {
        k++;
        m = 1;
      }
    }
    int n = (k - j + 3) / 4;
    int i1 = (k - j) % 4;
    if (i1 == 0)
      i1 = 4;
    Object localObject = new int[n];
    int i2 = 0;
    int i3 = 0;
    if (m != 0)
    {
      i1--;
      if (i1 <= 0)
      {
        i3++;
        i1 = 4;
      }
    }
    for (int i = j; i < paramArrayOfByte.length; i++)
    {
      i2 <<= 8;
      i2 |= (paramArrayOfByte[i] ^ 0xFFFFFFFF) & 0xFF;
      i1--;
      if (i1 > 0)
        continue;
      localObject[i3] = i2;
      i3++;
      i1 = 4;
      i2 = 0;
    }
    localObject = A(localObject);
    if (localObject[0] == 0)
    {
      int[] arrayOfInt = new int[localObject.length - 1];
      System.arraycopy(localObject, 1, arrayOfInt, 0, arrayOfInt.length);
      localObject = arrayOfInt;
    }
    return (I)localObject;
  }

  public BigInteger(int paramInt, byte[] paramArrayOfByte)
    throws NumberFormatException
  {
    if ((paramInt < -1) || (paramInt > 1))
      throw new NumberFormatException("Invalid sign value");
    if (paramInt == 0)
    {
      this.D = 0;
      this.H = new int[0];
      return;
    }
    this.H = A(paramArrayOfByte, 1);
    this.D = paramInt;
  }

  public BigInteger(int paramInt, Random paramRandom)
    throws IllegalArgumentException
  {
    if (paramInt < 0)
      throw new IllegalArgumentException("numBits must be non-negative");
    this.J = -1;
    this.B = -1;
    if (paramInt == 0)
    {
      this.H = L;
      return;
    }
    int i = (paramInt + 7) / 8;
    byte[] arrayOfByte = new byte[i];
    A(paramRandom, arrayOfByte);
    int tmp80_79 = 0;
    byte[] tmp80_77 = arrayOfByte;
    tmp80_77[tmp80_79] = (byte)(tmp80_77[tmp80_79] & C[(8 * i - paramInt)]);
    this.H = A(arrayOfByte, 1);
    this.D = (this.H.length < 1 ? 0 : 1);
  }

  private void A(Random paramRandom, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    int j = 0;
    int k = 0;
    if ((paramRandom instanceof SecureRandom))
      ((SecureRandom)paramRandom).nextBytes(paramArrayOfByte);
    else
      while (true)
        for (int m = 0; m < 4; m++)
        {
          if (j == i)
            return;
          k = m == 0 ? paramRandom.nextInt() : k >> 8;
          paramArrayOfByte[(j++)] = (byte)k;
        }
  }

  public BigInteger(int paramInt1, int paramInt2, Random paramRandom)
    throws ArithmeticException
  {
    if (paramInt1 < 2)
      throw new ArithmeticException("bitLength < 2");
    this.D = 1;
    this.B = paramInt1;
    if (paramInt1 == 2)
    {
      this.H = (paramRandom.nextInt() < 0 ? I.H : K.H);
      return;
    }
    int i = (paramInt1 + 7) / 8;
    int j = 8 * i - paramInt1;
    int k = C[j];
    byte[] arrayOfByte = new byte[i];
    while (true)
    {
      A(paramRandom, arrayOfByte);
      int tmp120_119 = 0;
      byte[] tmp120_117 = arrayOfByte;
      tmp120_117[tmp120_119] = (byte)(tmp120_117[tmp120_119] & k);
      int tmp130_129 = 0;
      byte[] tmp130_127 = arrayOfByte;
      tmp130_127[tmp130_129] = (byte)(tmp130_127[tmp130_129] | (byte)(1 << 7 - j));
      int tmp149_148 = (i - 1);
      byte[] tmp149_143 = arrayOfByte;
      tmp149_143[tmp149_148] = (byte)(tmp149_143[tmp149_148] | 0x1);
      this.H = A(arrayOfByte, 1);
      this.J = -1;
      this.E = -1L;
      if ((paramInt2 < 1) || (isProbablePrime(paramInt2)))
        break;
      if (paramInt1 <= 32)
        continue;
      for (int m = 0; m < 10000; m++)
      {
        int n = 33 + (paramRandom.nextInt() >>> 1) % (paramInt1 - 2);
        this.H[(this.H.length - (n >>> 5))] ^= 1 << (n & 0x1F);
        this.H[(this.H.length - 1)] ^= paramRandom.nextInt() << 1;
        this.E = -1L;
        if (isProbablePrime(paramInt2))
          return;
      }
    }
  }

  public BigInteger abs()
  {
    return this.D >= 0 ? this : negate();
  }

  private int[] C(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = paramArrayOfInt1.length - 1;
    int j = paramArrayOfInt2.length - 1;
    long l = 0L;
    while (j >= 0)
    {
      l += (paramArrayOfInt1[i] & 0xFFFFFFFF) + (paramArrayOfInt2[(j--)] & 0xFFFFFFFF);
      paramArrayOfInt1[(i--)] = (int)l;
      l >>>= 32;
    }
    while ((i >= 0) && (l != 0L))
    {
      l += (paramArrayOfInt1[i] & 0xFFFFFFFF);
      paramArrayOfInt1[(i--)] = (int)l;
      l >>>= 32;
    }
    return paramArrayOfInt1;
  }

  private int[] A(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length - 1;
    long l = 0L;
    l = (paramArrayOfInt[i] & 0xFFFFFFFF) + 1L;
    paramArrayOfInt[(i--)] = (int)l;
    l >>>= 32;
    while ((i >= 0) && (l != 0L))
    {
      l += (paramArrayOfInt[i] & 0xFFFFFFFF);
      paramArrayOfInt[(i--)] = (int)l;
      l >>>= 32;
    }
    return paramArrayOfInt;
  }

  public BigInteger add(BigInteger paramBigInteger)
    throws ArithmeticException
  {
    if ((paramBigInteger.D == 0) || (paramBigInteger.H.length == 0))
      return this;
    if ((this.D == 0) || (this.H.length == 0))
      return paramBigInteger;
    if (paramBigInteger.D < 0)
    {
      if (this.D > 0)
        return subtract(paramBigInteger.negate());
    }
    else if (this.D < 0)
      return paramBigInteger.subtract(negate());
    return C(paramBigInteger.H);
  }

  private BigInteger C(int[] paramArrayOfInt)
  {
    int[] arrayOfInt1;
    int[] arrayOfInt2;
    if (this.H.length < paramArrayOfInt.length)
    {
      arrayOfInt1 = paramArrayOfInt;
      arrayOfInt2 = this.H;
    }
    else
    {
      arrayOfInt1 = this.H;
      arrayOfInt2 = paramArrayOfInt;
    }
    int i = 2147483647;
    if (arrayOfInt1.length == arrayOfInt2.length)
      i -= arrayOfInt2[0];
    int j = (arrayOfInt1[0] ^ 0x80000000) >= i ? 1 : 0;
    int k = j != 0 ? 1 : 0;
    int[] arrayOfInt3 = new int[arrayOfInt1.length + k];
    System.arraycopy(arrayOfInt1, 0, arrayOfInt3, k, arrayOfInt1.length);
    arrayOfInt3 = C(arrayOfInt3, arrayOfInt2);
    return new BigInteger(this.D, arrayOfInt3);
  }

  public BigInteger and(BigInteger paramBigInteger)
  {
    if ((this.D == 0) || (paramBigInteger.D == 0))
      return ZERO;
    int[] arrayOfInt1 = this.D > 0 ? this.H : add(ONE).H;
    int[] arrayOfInt2 = paramBigInteger.D > 0 ? paramBigInteger.H : paramBigInteger.add(ONE).H;
    int i = (this.D < 0) && (paramBigInteger.D < 0) ? 1 : 0;
    int j = Math.max(arrayOfInt1.length, arrayOfInt2.length);
    int[] arrayOfInt3 = new int[j];
    int k = arrayOfInt3.length - arrayOfInt1.length;
    int m = arrayOfInt3.length - arrayOfInt2.length;
    for (int n = 0; n < arrayOfInt3.length; n++)
    {
      int i1 = n >= k ? arrayOfInt1[(n - k)] : 0;
      int i2 = n >= m ? arrayOfInt2[(n - m)] : 0;
      if (this.D < 0)
        i1 ^= -1;
      if (paramBigInteger.D < 0)
        i2 ^= -1;
      arrayOfInt3[n] = (i1 & i2);
      if (i == 0)
        continue;
      arrayOfInt3[n] ^= -1;
    }
    BigInteger localBigInteger = new BigInteger(1, arrayOfInt3);
    if (i != 0)
      localBigInteger = localBigInteger.not();
    return localBigInteger;
  }

  public BigInteger andNot(BigInteger paramBigInteger)
  {
    return and(paramBigInteger.not());
  }

  public int bitCount()
  {
    if (this.J == -1)
      if (this.D < 0)
      {
        this.J = not().bitCount();
      }
      else
      {
        int i = 0;
        for (int j = 0; j < this.H.length; j++)
        {
          i += G[(this.H[j] & 0xFF)];
          i += G[(this.H[j] >> 8 & 0xFF)];
          i += G[(this.H[j] >> 16 & 0xFF)];
          i += G[(this.H[j] >> 24 & 0xFF)];
        }
        this.J = i;
      }
    return this.J;
  }

  private int A(int paramInt, int[] paramArrayOfInt)
  {
    if (paramArrayOfInt.length == 0)
      return 0;
    while ((paramInt != paramArrayOfInt.length) && (paramArrayOfInt[paramInt] == 0))
      paramInt++;
    if (paramInt == paramArrayOfInt.length)
      return 0;
    int i = 32 * (paramArrayOfInt.length - paramInt - 1);
    i += A(paramArrayOfInt[paramInt]);
    if (this.D < 0)
    {
      int j = G[(paramArrayOfInt[paramInt] & 0xFF)] + G[(paramArrayOfInt[paramInt] >> 8 & 0xFF)] + G[(paramArrayOfInt[paramInt] >> 16 & 0xFF)] + G[(paramArrayOfInt[paramInt] >> 24 & 0xFF)] == 1 ? 1 : 0;
      for (int k = paramInt + 1; (k < paramArrayOfInt.length) && (j != 0); k++)
        j = paramArrayOfInt[k] == 0 ? 1 : 0;
      i -= (j != 0 ? 1 : 0);
    }
    return i;
  }

  public int bitLength()
  {
    if (this.B == -1)
      if (this.D == 0)
        this.B = 0;
      else
        this.B = A(0, this.H);
    return this.B;
  }

  static int A(int paramInt)
  {
    return paramInt < 1073741824 ? 30 : paramInt < 536870912 ? 29 : paramInt < 268435456 ? 28 : paramInt < 134217728 ? 27 : paramInt < 67108864 ? 26 : paramInt < 33554432 ? 25 : paramInt < 16777216 ? 24 : paramInt < 8388608 ? 23 : paramInt < 4194304 ? 22 : paramInt < 2097152 ? 21 : paramInt < 1048576 ? 20 : paramInt < 524288 ? 19 : paramInt < 262144 ? 18 : paramInt < 131072 ? 17 : paramInt < 65536 ? 16 : paramInt < 32768 ? 15 : paramInt < 16384 ? 14 : paramInt < 8192 ? 13 : paramInt < 4096 ? 12 : paramInt < 2048 ? 11 : paramInt < 1024 ? 10 : paramInt < 512 ? 9 : paramInt < 256 ? 8 : paramInt < 128 ? 7 : paramInt < 64 ? 6 : paramInt < 32 ? 5 : paramInt < 16 ? 4 : paramInt < 8 ? 3 : paramInt < 4 ? 2 : paramInt < 2 ? 1 : paramInt < 1 ? 0 : paramInt < 0 ? 32 : 31;
  }

  private boolean A()
  {
    return (this.D > 0) && (this.J == 1);
  }

  public int compareTo(Object paramObject)
  {
    return compareTo((BigInteger)paramObject);
  }

  private int C(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2)
  {
    while ((paramInt1 != paramArrayOfInt1.length) && (paramArrayOfInt1[paramInt1] == 0))
      paramInt1++;
    while ((paramInt2 != paramArrayOfInt2.length) && (paramArrayOfInt2[paramInt2] == 0))
      paramInt2++;
    return B(paramInt1, paramArrayOfInt1, paramInt2, paramArrayOfInt2);
  }

  private int B(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2)
  {
    int i = paramArrayOfInt1.length - paramArrayOfInt2.length - (paramInt1 - paramInt2);
    if (i != 0)
      return i < 0 ? -1 : 1;
    while (paramInt1 < paramArrayOfInt1.length)
    {
      int j = paramArrayOfInt1[(paramInt1++)];
      int k = paramArrayOfInt2[(paramInt2++)];
      if (j != k)
        return (j ^ 0x80000000) < (k ^ 0x80000000) ? -1 : 1;
    }
    return 0;
  }

  public int compareTo(BigInteger paramBigInteger)
  {
    if (this.D < paramBigInteger.D)
      return -1;
    if (this.D > paramBigInteger.D)
      return 1;
    if (this.D == 0)
      return 0;
    return this.D * C(0, this.H, 0, paramBigInteger.H);
  }

  private int[] D(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = C(0, paramArrayOfInt1, 0, paramArrayOfInt2);
    Object localObject;
    if (i > 0)
    {
      int j = A(0, paramArrayOfInt1) - A(0, paramArrayOfInt2);
      int[] arrayOfInt1;
      if (j > 1)
      {
        arrayOfInt1 = A(paramArrayOfInt2, j - 1);
        localObject = A(ONE.H, j - 1);
        if (j % 32 == 0)
        {
          arrayOfInt2 = new int[j / 32 + 1];
          System.arraycopy(localObject, 0, arrayOfInt2, 1, arrayOfInt2.length - 1);
          arrayOfInt2[0] = 0;
          localObject = arrayOfInt2;
        }
      }
      else
      {
        arrayOfInt1 = new int[paramArrayOfInt1.length];
        localObject = new int[1];
        System.arraycopy(paramArrayOfInt2, 0, arrayOfInt1, arrayOfInt1.length - paramArrayOfInt2.length, paramArrayOfInt2.length);
        localObject[0] = 1;
      }
      int[] arrayOfInt2 = new int[localObject.length];
      A(0, paramArrayOfInt1, 0, arrayOfInt1);
      System.arraycopy(localObject, 0, arrayOfInt2, 0, localObject.length);
      int k = 0;
      int m = 0;
      int n = 0;
      while (true)
      {
        for (int i1 = C(k, paramArrayOfInt1, m, arrayOfInt1); i1 >= 0; i1 = C(k, paramArrayOfInt1, m, arrayOfInt1))
        {
          A(k, paramArrayOfInt1, m, arrayOfInt1);
          C(localObject, arrayOfInt2);
        }
        i = C(k, paramArrayOfInt1, 0, paramArrayOfInt2);
        if (i > 0)
        {
          if (paramArrayOfInt1[k] == 0)
            k++;
          j = A(m, arrayOfInt1) - A(k, paramArrayOfInt1);
          if (j == 0)
          {
            arrayOfInt1 = B(m, arrayOfInt1);
            arrayOfInt2 = B(n, arrayOfInt2);
          }
          else
          {
            arrayOfInt1 = A(m, arrayOfInt1, j);
            arrayOfInt2 = A(n, arrayOfInt2, j);
          }
          if (arrayOfInt1[m] == 0)
            m++;
          if (arrayOfInt2[n] == 0)
            n++;
        }
        else
        {
          if (i != 0)
            break;
          C(localObject, ONE.H);
          for (int i2 = k; i2 != paramArrayOfInt1.length; i2++)
            paramArrayOfInt1[i2] = 0;
          break;
        }
      }
    }
    else if (i == 0)
    {
      localObject = new int[1];
      localObject[0] = 1;
    }
    else
    {
      localObject = new int[1];
      localObject[0] = 0;
    }
    return (I)localObject;
  }

  public BigInteger divide(BigInteger paramBigInteger)
    throws ArithmeticException
  {
    if (paramBigInteger.D == 0)
      throw new ArithmeticException("Divide by zero");
    if (this.D == 0)
      return ZERO;
    if (paramBigInteger.compareTo(ONE) == 0)
      return this;
    int[] arrayOfInt = new int[this.H.length];
    System.arraycopy(this.H, 0, arrayOfInt, 0, arrayOfInt.length);
    return new BigInteger(this.D * paramBigInteger.D, D(arrayOfInt, paramBigInteger.H));
  }

  public BigInteger[] divideAndRemainder(BigInteger paramBigInteger)
    throws ArithmeticException
  {
    if (paramBigInteger.D == 0)
      throw new ArithmeticException("Divide by zero");
    BigInteger[] arrayOfBigInteger = new BigInteger[2];
    if (this.D == 0)
    {
      BigInteger tmp36_33 = ZERO;
      arrayOfBigInteger[1] = tmp36_33;
      arrayOfBigInteger[0] = tmp36_33;
      return arrayOfBigInteger;
    }
    if (paramBigInteger.compareTo(ONE) == 0)
    {
      arrayOfBigInteger[0] = this;
      arrayOfBigInteger[1] = ZERO;
      return arrayOfBigInteger;
    }
    int[] arrayOfInt1 = new int[this.H.length];
    System.arraycopy(this.H, 0, arrayOfInt1, 0, arrayOfInt1.length);
    int[] arrayOfInt2 = D(arrayOfInt1, paramBigInteger.H);
    arrayOfBigInteger[0] = new BigInteger(this.D * paramBigInteger.D, arrayOfInt2);
    arrayOfBigInteger[1] = new BigInteger(this.D, arrayOfInt1);
    return arrayOfBigInteger;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if (!(paramObject instanceof BigInteger))
      return false;
    BigInteger localBigInteger = (BigInteger)paramObject;
    if ((localBigInteger.D != this.D) || (localBigInteger.H.length != this.H.length))
      return false;
    for (int i = 0; i < this.H.length; i++)
      if (localBigInteger.H[i] != this.H[i])
        return false;
    return true;
  }

  public BigInteger gcd(BigInteger paramBigInteger)
  {
    if (paramBigInteger.D == 0)
      return abs();
    if (this.D == 0)
      return paramBigInteger.abs();
    Object localObject1 = this;
    BigInteger localBigInteger;
    for (Object localObject2 = paramBigInteger; ((BigInteger)localObject2).D != 0; localObject2 = localBigInteger)
    {
      localBigInteger = ((BigInteger)localObject1).mod((BigInteger)localObject2);
      localObject1 = localObject2;
    }
    return (BigInteger)(BigInteger)localObject1;
  }

  public int hashCode()
  {
    int i = this.H.length;
    if (this.H.length > 0)
    {
      i ^= this.H[0];
      if (this.H.length > 1)
        i ^= this.H[(this.H.length - 1)];
    }
    return this.D < 0 ? i ^ 0xFFFFFFFF : i;
  }

  public int intValue()
  {
    if (this.H.length == 0)
      return 0;
    if (this.D < 0)
      return -this.H[(this.H.length - 1)];
    return this.H[(this.H.length - 1)];
  }

  public byte byteValue()
  {
    return (byte)intValue();
  }

  public boolean isProbablePrime(int paramInt)
  {
    if (paramInt <= 0)
      return true;
    if (this.D == 0)
      return false;
    BigInteger localBigInteger1 = abs();
    if (!localBigInteger1.testBit(0))
      return localBigInteger1.equals(I);
    if (localBigInteger1.equals(ONE))
      return false;
    int i = Math.min(localBigInteger1.bitLength() - 1, F.length);
    for (int j = 0; j < i; j++)
    {
      k = localBigInteger1.C(A[j]);
      localObject = F[j];
      for (int m = 0; m < localObject.length; m++)
      {
        int n = localObject[m];
        int i1 = k % n;
        if (i1 == 0)
          return (localBigInteger1.bitLength() < 16) && (localBigInteger1.intValue() == n);
      }
    }
    BigInteger localBigInteger2 = localBigInteger1.subtract(ONE);
    int k = localBigInteger2.getLowestSetBit();
    Object localObject = localBigInteger2.shiftRight(k);
    Random localRandom = new Random();
    do
    {
      BigInteger localBigInteger3;
      do
        localBigInteger3 = new BigInteger(localBigInteger1.bitLength(), localRandom);
      while ((localBigInteger3.compareTo(ONE) <= 0) || (localBigInteger3.compareTo(localBigInteger2) >= 0));
      BigInteger localBigInteger4 = localBigInteger3.modPow((BigInteger)localObject, localBigInteger1);
      if (!localBigInteger4.equals(ONE))
      {
        int i2 = 0;
        while (!localBigInteger4.equals(localBigInteger2))
        {
          i2++;
          if (i2 == k)
            return false;
          localBigInteger4 = localBigInteger4.modPow(I, localBigInteger1);
          if (localBigInteger4.equals(ONE))
            return false;
        }
      }
      paramInt -= 2;
    }
    while (paramInt > 0);
    return true;
  }

  public long longValue()
  {
    long l = 0L;
    if (this.H.length == 0)
      return 0L;
    if (this.H.length > 1)
      l = this.H[(this.H.length - 2)] << 32 | this.H[(this.H.length - 1)] & 0xFFFFFFFF;
    else
      l = this.H[(this.H.length - 1)] & 0xFFFFFFFF;
    if (this.D < 0)
      return -l;
    return l;
  }

  public BigInteger max(BigInteger paramBigInteger)
  {
    return compareTo(paramBigInteger) > 0 ? this : paramBigInteger;
  }

  public BigInteger min(BigInteger paramBigInteger)
  {
    return compareTo(paramBigInteger) < 0 ? this : paramBigInteger;
  }

  public BigInteger mod(BigInteger paramBigInteger)
    throws ArithmeticException
  {
    if (paramBigInteger.D <= 0)
      throw new ArithmeticException("BigInteger: modulus is not positive");
    BigInteger localBigInteger = remainder(paramBigInteger);
    return localBigInteger.D >= 0 ? localBigInteger : localBigInteger.add(paramBigInteger);
  }

  public BigInteger modInverse(BigInteger paramBigInteger)
    throws ArithmeticException
  {
    if (paramBigInteger.D != 1)
      throw new ArithmeticException("Modulus must be positive");
    BigInteger localBigInteger1 = new BigInteger();
    BigInteger localBigInteger2 = A(this, paramBigInteger, localBigInteger1, null);
    if (!localBigInteger2.equals(ONE))
      throw new ArithmeticException("Numbers not relatively prime.");
    if (localBigInteger1.compareTo(ZERO) < 0)
      localBigInteger1 = localBigInteger1.add(paramBigInteger);
    return localBigInteger1;
  }

  private static BigInteger A(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    Object localObject1 = ONE;
    Object localObject2 = paramBigInteger1;
    Object localObject3 = ZERO;
    Object localObject4;
    for (BigInteger localBigInteger1 = paramBigInteger2; localBigInteger1.D > 0; localBigInteger1 = localObject4[1])
    {
      localObject4 = ((BigInteger)localObject2).divideAndRemainder(localBigInteger1);
      BigInteger localBigInteger2 = ((BigInteger)localObject1).subtract(((BigInteger)localObject3).multiply(localObject4[0]));
      localObject1 = localObject3;
      localObject3 = localBigInteger2;
      localObject2 = localBigInteger1;
    }
    if (paramBigInteger3 != null)
    {
      paramBigInteger3.D = ((BigInteger)localObject1).D;
      paramBigInteger3.H = ((BigInteger)localObject1).H;
    }
    if (paramBigInteger4 != null)
    {
      localObject4 = ((BigInteger)localObject2).subtract(((BigInteger)localObject1).multiply(paramBigInteger1)).divide(paramBigInteger2);
      paramBigInteger4.D = ((BigInteger)localObject4).D;
      paramBigInteger4.H = ((BigInteger)localObject4).H;
    }
    return (BigInteger)(BigInteger)(BigInteger)(BigInteger)localObject2;
  }

  private void B(int[] paramArrayOfInt)
  {
    for (int i = 0; i != paramArrayOfInt.length; i++)
      paramArrayOfInt[i] = 0;
  }

  public BigInteger modPow(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws ArithmeticException
  {
    if (paramBigInteger2.D < 1)
      throw new ArithmeticException("Modulus must be positive");
    if (paramBigInteger2.equals(ONE))
      return ZERO;
    if (paramBigInteger1.D == 0)
      return ONE;
    if (this.D == 0)
      return ZERO;
    Object localObject = null;
    int[] arrayOfInt1 = null;
    int i = (paramBigInteger2.H[(paramBigInteger2.H.length - 1)] & 0x1) == 1 ? 1 : 0;
    long l = 0L;
    BigInteger localBigInteger1;
    if (i != 0)
    {
      l = paramBigInteger2.B();
      localBigInteger1 = shiftLeft(32 * paramBigInteger2.H.length).mod(paramBigInteger2);
      localObject = localBigInteger1.H;
      i = localObject.length <= paramBigInteger2.H.length ? 1 : 0;
      if (i != 0)
      {
        arrayOfInt1 = new int[paramBigInteger2.H.length + 1];
        if (localObject.length < paramBigInteger2.H.length)
        {
          int[] arrayOfInt3 = new int[paramBigInteger2.H.length];
          System.arraycopy(localObject, 0, arrayOfInt3, arrayOfInt3.length - localObject.length, localObject.length);
          localObject = arrayOfInt3;
        }
      }
    }
    if (i == 0)
    {
      if (this.H.length <= paramBigInteger2.H.length)
      {
        localObject = new int[paramBigInteger2.H.length];
        System.arraycopy(this.H, 0, localObject, localObject.length - this.H.length, this.H.length);
      }
      else
      {
        localBigInteger1 = remainder(paramBigInteger2);
        localObject = new int[paramBigInteger2.H.length];
        System.arraycopy(localBigInteger1.H, 0, localObject, localObject.length - localBigInteger1.H.length, localBigInteger1.H.length);
      }
      arrayOfInt1 = new int[paramBigInteger2.H.length * 2];
    }
    int[] arrayOfInt2 = new int[paramBigInteger2.H.length];
    for (int j = 0; j < paramBigInteger1.H.length; j++)
    {
      int k = paramBigInteger1.H[j];
      int m = 0;
      if (j == 0)
      {
        while (k > 0)
        {
          k <<= 1;
          m++;
        }
        System.arraycopy(localObject, 0, arrayOfInt2, 0, localObject.length);
        k <<= 1;
        m++;
      }
      while (k != 0)
      {
        if (i != 0)
        {
          A(arrayOfInt1, arrayOfInt2, arrayOfInt2, paramBigInteger2.H, l);
        }
        else
        {
          A(arrayOfInt1, arrayOfInt2);
          B(arrayOfInt1, paramBigInteger2.H);
          System.arraycopy(arrayOfInt1, arrayOfInt1.length - arrayOfInt2.length, arrayOfInt2, 0, arrayOfInt2.length);
          B(arrayOfInt1);
        }
        m++;
        if (k < 0)
          if (i != 0)
          {
            A(arrayOfInt1, arrayOfInt2, localObject, paramBigInteger2.H, l);
          }
          else
          {
            A(arrayOfInt1, arrayOfInt2, localObject);
            B(arrayOfInt1, paramBigInteger2.H);
            System.arraycopy(arrayOfInt1, arrayOfInt1.length - arrayOfInt2.length, arrayOfInt2, 0, arrayOfInt2.length);
            B(arrayOfInt1);
          }
        k <<= 1;
      }
      while (m < 32)
      {
        if (i != 0)
        {
          A(arrayOfInt1, arrayOfInt2, arrayOfInt2, paramBigInteger2.H, l);
        }
        else
        {
          A(arrayOfInt1, arrayOfInt2);
          B(arrayOfInt1, paramBigInteger2.H);
          System.arraycopy(arrayOfInt1, arrayOfInt1.length - arrayOfInt2.length, arrayOfInt2, 0, arrayOfInt2.length);
          B(arrayOfInt1);
        }
        m++;
      }
    }
    if (i != 0)
    {
      B(localObject);
      localObject[(localObject.length - 1)] = 1;
      A(arrayOfInt1, arrayOfInt2, localObject, paramBigInteger2.H, l);
    }
    BigInteger localBigInteger2 = new BigInteger(1, arrayOfInt2);
    return paramBigInteger1.D > 0 ? localBigInteger2 : (BigInteger)localBigInteger2.modInverse(paramBigInteger2);
  }

  private int[] A(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = paramArrayOfInt1.length - 1;
    for (int j = paramArrayOfInt2.length - 1; j != 0; j--)
    {
      long l4 = paramArrayOfInt2[j] & 0xFFFFFFFF;
      l1 = l4 * l4;
      l2 = l1 >>> 32;
      l1 &= 4294967295L;
      l1 += (paramArrayOfInt1[i] & 0xFFFFFFFF);
      paramArrayOfInt1[i] = (int)l1;
      long l3 = l2 + (l1 >> 32);
      for (int k = j - 1; k >= 0; k--)
      {
        i--;
        l1 = (paramArrayOfInt2[k] & 0xFFFFFFFF) * l4;
        l2 = l1 >>> 31;
        l1 = (l1 & 0x7FFFFFFF) << 1;
        l1 += (paramArrayOfInt1[i] & 0xFFFFFFFF) + l3;
        paramArrayOfInt1[i] = (int)l1;
        l3 = l2 + (l1 >>> 32);
      }
      i--;
      l3 += (paramArrayOfInt1[i] & 0xFFFFFFFF);
      paramArrayOfInt1[i] = (int)l3;
      i--;
      if (i >= 0)
        paramArrayOfInt1[i] = (int)(l3 >> 32);
      i += j;
    }
    long l1 = paramArrayOfInt2[0] & 0xFFFFFFFF;
    l1 *= l1;
    long l2 = l1 >>> 32;
    l1 &= 4294967295L;
    l1 += (paramArrayOfInt1[i] & 0xFFFFFFFF);
    paramArrayOfInt1[i] = (int)l1;
    i--;
    if (i >= 0)
      paramArrayOfInt1[i] = (int)(l2 + (l1 >> 32) + paramArrayOfInt1[i]);
    return paramArrayOfInt1;
  }

  private int[] A(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
  {
    int i = paramArrayOfInt3.length;
    if (i < 1)
      return paramArrayOfInt1;
    int j = paramArrayOfInt1.length - paramArrayOfInt2.length;
    while (true)
    {
      i--;
      long l1 = paramArrayOfInt3[i] & 0xFFFFFFFF;
      long l2 = 0L;
      for (int k = paramArrayOfInt2.length - 1; k >= 0; k--)
      {
        l2 += l1 * (paramArrayOfInt2[k] & 0xFFFFFFFF) + (paramArrayOfInt1[(j + k)] & 0xFFFFFFFF);
        paramArrayOfInt1[(j + k)] = (int)l2;
        l2 >>>= 32;
      }
      j--;
      if (i < 1)
      {
        if (j < 0)
          break;
        paramArrayOfInt1[j] = (int)l2;
        break;
      }
      paramArrayOfInt1[j] = (int)l2;
    }
    return paramArrayOfInt1;
  }

  private long A(long paramLong1, long paramLong2, long[] paramArrayOfLong)
  {
    long l2 = 1L;
    long l3 = paramLong1;
    long l4 = 0L;
    long l5 = paramLong2;
    while (l5 > 0L)
    {
      long l6 = l3 / l5;
      long l7 = l2 - l4 * l6;
      l2 = l4;
      l4 = l7;
      l7 = l3 - l5 * l6;
      l3 = l5;
      l5 = l7;
    }
    paramArrayOfLong[0] = l2;
    long l1 = (l3 - l2 * paramLong1) / paramLong2;
    paramArrayOfLong[1] = l1;
    return l3;
  }

  private long A(long paramLong1, long paramLong2)
    throws ArithmeticException
  {
    if (paramLong2 < 0L)
      throw new ArithmeticException("Modulus must be positive");
    long[] arrayOfLong = new long[2];
    long l = A(paramLong1, paramLong2, arrayOfLong);
    if (l != 1L)
      throw new ArithmeticException("Numbers not relatively prime.");
    if (arrayOfLong[0] < 0L)
      arrayOfLong[0] += paramLong2;
    return arrayOfLong[0];
  }

  private long B()
  {
    if (this.E != -1L)
      return this.E;
    if ((this.H[(this.H.length - 1)] & 0x1) == 0)
      return -1L;
    long l = (this.H[(this.H.length - 1)] ^ 0xFFFFFFFF | 0x1) & 0xFFFFFFFF;
    this.E = A(l, 4294967296L);
    return this.E;
  }

  private void A(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, long paramLong)
  {
    int i = paramArrayOfInt4.length;
    int j = i - 1;
    long l1 = paramArrayOfInt3[(i - 1)] & 0xFFFFFFFF;
    for (int k = 0; k <= i; k++)
      paramArrayOfInt1[k] = 0;
    for (k = i; k > 0; k--)
    {
      long l2 = paramArrayOfInt2[(k - 1)] & 0xFFFFFFFF;
      long l3 = ((paramArrayOfInt1[i] & 0xFFFFFFFF) + (l2 * l1 & 0xFFFFFFFF) & 0xFFFFFFFF) * paramLong & 0xFFFFFFFF;
      long l4 = l2 * l1;
      long l5 = l3 * (paramArrayOfInt4[(i - 1)] & 0xFFFFFFFF);
      long l6 = (paramArrayOfInt1[i] & 0xFFFFFFFF) + (l4 & 0xFFFFFFFF) + (l5 & 0xFFFFFFFF);
      long l7 = (l4 >>> 32) + (l5 >>> 32) + (l6 >>> 32);
      for (int m = j; m > 0; m--)
      {
        l4 = l2 * (paramArrayOfInt3[(m - 1)] & 0xFFFFFFFF);
        l5 = l3 * (paramArrayOfInt4[(m - 1)] & 0xFFFFFFFF);
        l6 = (paramArrayOfInt1[m] & 0xFFFFFFFF) + (l4 & 0xFFFFFFFF) + (l5 & 0xFFFFFFFF) + (l7 & 0xFFFFFFFF);
        l7 = (l7 >>> 32) + (l4 >>> 32) + (l5 >>> 32) + (l6 >>> 32);
        paramArrayOfInt1[(m + 1)] = (int)l6;
      }
      l7 += (paramArrayOfInt1[0] & 0xFFFFFFFF);
      paramArrayOfInt1[1] = (int)l7;
      paramArrayOfInt1[0] = (int)(l7 >>> 32);
    }
    if (C(0, paramArrayOfInt1, 0, paramArrayOfInt4) >= 0)
      A(0, paramArrayOfInt1, 0, paramArrayOfInt4);
    System.arraycopy(paramArrayOfInt1, 1, paramArrayOfInt2, 0, i);
  }

  public BigInteger multiply(BigInteger paramBigInteger)
  {
    if ((this.D == 0) || (paramBigInteger.D == 0))
      return ZERO;
    int i = bitLength() + paramBigInteger.bitLength();
    int j = (i + 31) / 32;
    int[] arrayOfInt = new int[j];
    if (paramBigInteger == this)
      A(arrayOfInt, this.H);
    else
      A(arrayOfInt, this.H, paramBigInteger.H);
    return new BigInteger(this.D * paramBigInteger.D, arrayOfInt);
  }

  public BigInteger negate()
  {
    if (this.D == 0)
      return this;
    return new BigInteger(-this.D, this.H);
  }

  public BigInteger not()
  {
    return add(ONE).negate();
  }

  public BigInteger pow(int paramInt)
    throws ArithmeticException
  {
    if (paramInt < 0)
      throw new ArithmeticException("Negative exponent");
    if (this.D == 0)
      return paramInt == 0 ? ONE : this;
    BigInteger localBigInteger1 = ONE;
    BigInteger localBigInteger2 = this;
    while (paramInt != 0)
    {
      if ((paramInt & 0x1) == 1)
        localBigInteger1 = localBigInteger1.multiply(localBigInteger2);
      paramInt >>= 1;
      if (paramInt == 0)
        continue;
      localBigInteger2 = localBigInteger2.multiply(localBigInteger2);
    }
    return localBigInteger1;
  }

  public static BigInteger probablePrime(int paramInt, Random paramRandom)
  {
    return new BigInteger(paramInt, 100, paramRandom);
  }

  private int C(int paramInt)
  {
    long l = 0L;
    for (int i = 0; i < this.H.length; i++)
      l = (l << 32 | this.H[i] & 0xFFFFFFFF) % paramInt;
    return (int)l;
  }

  private int[] B(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    for (int i = 0; (i < paramArrayOfInt1.length) && (paramArrayOfInt1[i] == 0); i++);
    for (int j = 0; (j < paramArrayOfInt2.length) && (paramArrayOfInt2[j] == 0); j++);
    int k = B(i, paramArrayOfInt1, j, paramArrayOfInt2);
    int m;
    if (k > 0)
    {
      m = A(j, paramArrayOfInt2);
      int n = A(i, paramArrayOfInt1);
      int i1 = n - m;
      int i2 = 0;
      int i3 = m;
      int[] arrayOfInt;
      if (i1 > 0)
      {
        arrayOfInt = A(paramArrayOfInt2, i1);
        i3 += i1;
      }
      else
      {
        int i4 = paramArrayOfInt2.length - j;
        arrayOfInt = new int[i4];
        System.arraycopy(paramArrayOfInt2, j, arrayOfInt, 0, i4);
      }
      while (true)
      {
        if ((i3 < n) || (B(i, paramArrayOfInt1, i2, arrayOfInt) >= 0))
        {
          A(i, paramArrayOfInt1, i2, arrayOfInt);
          while (paramArrayOfInt1[i] == 0)
          {
            i++;
            if (i == paramArrayOfInt1.length)
              return paramArrayOfInt1;
          }
          k = B(i, paramArrayOfInt1, j, paramArrayOfInt2);
          if (k <= 0)
            break;
          n = 32 * (paramArrayOfInt1.length - i - 1) + A(paramArrayOfInt1[i]);
        }
        i1 = i3 - n;
        if (i1 < 2)
        {
          arrayOfInt = B(i2, arrayOfInt);
          i3--;
        }
        else
        {
          arrayOfInt = A(i2, arrayOfInt, i1);
          i3 -= i1;
        }
        while (arrayOfInt[i2] == 0)
          i2++;
      }
    }
    if (k == 0)
      for (m = i; m < paramArrayOfInt1.length; m++)
        paramArrayOfInt1[m] = 0;
    return paramArrayOfInt1;
  }

  public BigInteger remainder(BigInteger paramBigInteger)
    throws ArithmeticException
  {
    if (paramBigInteger.D == 0)
      throw new ArithmeticException("BigInteger: Divide by zero");
    if (this.D == 0)
      return ZERO;
    if (paramBigInteger.H.length == 1)
    {
      int i = paramBigInteger.H[0];
      if (i > 0)
      {
        if (i == 1)
          return ZERO;
        int j = C(i);
        return j == 0 ? ZERO : new BigInteger(this.D, new int[] { j });
      }
    }
    if (C(0, this.H, 0, paramBigInteger.H) < 0)
      return this;
    int[] arrayOfInt;
    if (paramBigInteger.A())
    {
      arrayOfInt = B(paramBigInteger.abs().bitLength() - 1);
    }
    else
    {
      arrayOfInt = new int[this.H.length];
      System.arraycopy(this.H, 0, arrayOfInt, 0, arrayOfInt.length);
      arrayOfInt = B(arrayOfInt, paramBigInteger.H);
    }
    return new BigInteger(this.D, arrayOfInt);
  }

  private int[] B(int paramInt)
  {
    if (paramInt < 1)
      return L;
    int i = (paramInt + 31) / 32;
    i = Math.min(i, this.H.length);
    int[] arrayOfInt = new int[i];
    System.arraycopy(this.H, this.H.length - i, arrayOfInt, 0, i);
    int j = paramInt % 32;
    if (j != 0)
      arrayOfInt[0] &= (-1 << j ^ 0xFFFFFFFF);
    return arrayOfInt;
  }

  private int[] A(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramInt >>> 5;
    int j = paramInt & 0x1F;
    int k = paramArrayOfInt.length;
    int[] arrayOfInt = null;
    if (j == 0)
    {
      arrayOfInt = new int[k + i];
      System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, k);
    }
    else
    {
      int m = 0;
      int n = 32 - j;
      int i1 = paramArrayOfInt[0] >>> n;
      if (i1 != 0)
      {
        arrayOfInt = new int[k + i + 1];
        arrayOfInt[(m++)] = i1;
      }
      else
      {
        arrayOfInt = new int[k + i];
      }
      int i2 = paramArrayOfInt[0];
      for (int i3 = 0; i3 < k - 1; i3++)
      {
        int i4 = paramArrayOfInt[(i3 + 1)];
        arrayOfInt[(m++)] = (i2 << j | i4 >>> n);
        i2 = i4;
      }
      arrayOfInt[m] = (paramArrayOfInt[(k - 1)] << j);
    }
    return arrayOfInt;
  }

  public BigInteger shiftLeft(int paramInt)
  {
    if ((this.D == 0) || (this.H.length == 0))
      return ZERO;
    if (paramInt == 0)
      return this;
    if (paramInt < 0)
      return shiftRight(-paramInt);
    BigInteger localBigInteger = new BigInteger(this.D, A(this.H, paramInt));
    if (this.J != -1)
      localBigInteger.J = (this.D > 0 ? this.J : this.J + paramInt);
    if (this.B != -1)
      this.B += paramInt;
    return localBigInteger;
  }

  private int[] A(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    int i = (paramInt2 >>> 5) + paramInt1;
    int j = paramInt2 & 0x1F;
    int k = paramArrayOfInt.length - 1;
    int m;
    int n;
    if (i != paramInt1)
    {
      m = i - paramInt1;
      for (n = k; n >= i; n--)
        paramArrayOfInt[n] = paramArrayOfInt[(n - m)];
      for (n = i - 1; n >= paramInt1; n--)
        paramArrayOfInt[n] = 0;
    }
    if (j != 0)
    {
      m = 32 - j;
      n = paramArrayOfInt[k];
      for (int i1 = k; i1 >= i + 1; i1--)
      {
        int i2 = paramArrayOfInt[(i1 - 1)];
        paramArrayOfInt[i1] = (n >>> j | i2 << m);
        n = i2;
      }
      paramArrayOfInt[i] >>>= j;
    }
    return paramArrayOfInt;
  }

  private int[] B(int paramInt, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length - 1;
    int j = paramArrayOfInt[i];
    for (int k = i; k > paramInt; k--)
    {
      int m = paramArrayOfInt[(k - 1)];
      paramArrayOfInt[k] = (j >>> 1 | m << 31);
      j = m;
    }
    paramArrayOfInt[paramInt] >>>= 1;
    return paramArrayOfInt;
  }

  public BigInteger shiftRight(int paramInt)
  {
    if (paramInt == 0)
      return this;
    if (paramInt < 0)
      return shiftLeft(-paramInt);
    if (paramInt >= bitLength())
      return this.D < 0 ? valueOf(-1L) : ZERO;
    int[] arrayOfInt = new int[this.H.length];
    System.arraycopy(this.H, 0, arrayOfInt, 0, arrayOfInt.length);
    return new BigInteger(this.D, A(0, arrayOfInt, paramInt));
  }

  public int signum()
  {
    return this.D;
  }

  private int[] A(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2)
  {
    // Byte code:
    //   0: aload_2
    //   1: arraylength
    //   2: istore 5
    //   4: aload 4
    //   6: arraylength
    //   7: istore 6
    //   9: iconst_0
    //   10: istore 9
    //   12: aload_2
    //   13: iinc 5 255
    //   16: iload 5
    //   18: iaload
    //   19: i2l
    //   20: ldc2_w 145
    //   23: land
    //   24: aload 4
    //   26: iinc 6 255
    //   29: iload 6
    //   31: iaload
    //   32: i2l
    //   33: ldc2_w 145
    //   36: land
    //   37: lsub
    //   38: iload 9
    //   40: i2l
    //   41: ladd
    //   42: lstore 7
    //   44: aload_2
    //   45: iload 5
    //   47: lload 7
    //   49: l2i
    //   50: iastore
    //   51: lload 7
    //   53: bipush 63
    //   55: lshr
    //   56: l2i
    //   57: istore 9
    //   59: iload 6
    //   61: iload_3
    //   62: if_icmpgt -50 -> 12
    //   65: iload 9
    //   67: ifeq +22 -> 89
    //   70: aload_2
    //   71: iinc 5 255
    //   74: iload 5
    //   76: dup2
    //   77: iaload
    //   78: iconst_1
    //   79: isub
    //   80: dup_x2
    //   81: iastore
    //   82: iconst_m1
    //   83: if_icmpne +6 -> 89
    //   86: goto -16 -> 70
    //   89: aload_2
    //   90: areturn
  }

  public BigInteger subtract(BigInteger paramBigInteger)
  {
    if ((paramBigInteger.D == 0) || (paramBigInteger.H.length == 0))
      return this;
    if ((this.D == 0) || (this.H.length == 0))
      return paramBigInteger.negate();
    if (this.D != paramBigInteger.D)
      return add(paramBigInteger.negate());
    int i = C(0, this.H, 0, paramBigInteger.H);
    if (i == 0)
      return ZERO;
    BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    if (i < 0)
    {
      localBigInteger1 = paramBigInteger;
      localBigInteger2 = this;
    }
    else
    {
      localBigInteger1 = this;
      localBigInteger2 = paramBigInteger;
    }
    int[] arrayOfInt = new int[localBigInteger1.H.length];
    System.arraycopy(localBigInteger1.H, 0, arrayOfInt, 0, arrayOfInt.length);
    return new BigInteger(this.D * i, A(0, arrayOfInt, 0, localBigInteger2.H));
  }

  public byte[] toByteArray()
  {
    if (this.D == 0)
      return new byte[1];
    int i = bitLength();
    byte[] arrayOfByte = new byte[i / 8 + 1];
    int j = this.H.length;
    int k = arrayOfByte.length;
    int m;
    if (this.D > 0)
    {
      while (j > 1)
      {
        j--;
        m = this.H[j];
        k--;
        arrayOfByte[k] = (byte)m;
        k--;
        arrayOfByte[k] = (byte)(m >>> 8);
        k--;
        arrayOfByte[k] = (byte)(m >>> 16);
        k--;
        arrayOfByte[k] = (byte)(m >>> 24);
      }
      m = this.H[0];
      while ((m & 0xFFFFFF00) != 0)
      {
        k--;
        arrayOfByte[k] = (byte)m;
        m >>>= 8;
      }
      k--;
      arrayOfByte[k] = (byte)m;
    }
    else
    {
      m = 1;
      while (j > 1)
      {
        j--;
        n = this.H[j] ^ 0xFFFFFFFF;
        if (m != 0)
        {
          n++;
          m = n == 0 ? 1 : 0;
        }
        k--;
        arrayOfByte[k] = (byte)n;
        k--;
        arrayOfByte[k] = (byte)(n >>> 8);
        k--;
        arrayOfByte[k] = (byte)(n >>> 16);
        k--;
        arrayOfByte[k] = (byte)(n >>> 24);
      }
      int n = this.H[0];
      if (m != 0)
        n--;
      while ((n & 0xFFFFFF00) != 0)
      {
        k--;
        arrayOfByte[k] = (byte)(n ^ 0xFFFFFFFF);
        n >>>= 8;
      }
      k--;
      arrayOfByte[k] = (byte)(n ^ 0xFFFFFFFF);
      if (k > 0)
      {
        k--;
        arrayOfByte[k] = -1;
      }
    }
    return arrayOfByte;
  }

  public BigInteger xor(BigInteger paramBigInteger)
  {
    if (this.D == 0)
      return paramBigInteger;
    if (paramBigInteger.D == 0)
      return this;
    int[] arrayOfInt1 = this.D > 0 ? this.H : add(ONE).H;
    int[] arrayOfInt2 = paramBigInteger.D > 0 ? paramBigInteger.H : paramBigInteger.add(ONE).H;
    int i = ((this.D < 0) && (paramBigInteger.D >= 0)) || ((this.D >= 0) && (paramBigInteger.D < 0)) ? 1 : 0;
    int j = Math.max(arrayOfInt1.length, arrayOfInt2.length);
    int[] arrayOfInt3 = new int[j];
    int k = arrayOfInt3.length - arrayOfInt1.length;
    int m = arrayOfInt3.length - arrayOfInt2.length;
    for (int n = 0; n < arrayOfInt3.length; n++)
    {
      int i1 = n >= k ? arrayOfInt1[(n - k)] : 0;
      int i2 = n >= m ? arrayOfInt2[(n - m)] : 0;
      if (this.D < 0)
        i1 ^= -1;
      if (paramBigInteger.D < 0)
        i2 ^= -1;
      arrayOfInt3[n] = (i1 ^ i2);
      if (i == 0)
        continue;
      arrayOfInt3[n] ^= -1;
    }
    BigInteger localBigInteger = new BigInteger(1, arrayOfInt3);
    if (i != 0)
      localBigInteger = localBigInteger.not();
    return localBigInteger;
  }

  public BigInteger or(BigInteger paramBigInteger)
  {
    if (this.D == 0)
      return paramBigInteger;
    if (paramBigInteger.D == 0)
      return this;
    int[] arrayOfInt1 = this.D > 0 ? this.H : add(ONE).H;
    int[] arrayOfInt2 = paramBigInteger.D > 0 ? paramBigInteger.H : paramBigInteger.add(ONE).H;
    int i = (this.D < 0) || (paramBigInteger.D < 0) ? 1 : 0;
    int j = Math.max(arrayOfInt1.length, arrayOfInt2.length);
    int[] arrayOfInt3 = new int[j];
    int k = arrayOfInt3.length - arrayOfInt1.length;
    int m = arrayOfInt3.length - arrayOfInt2.length;
    for (int n = 0; n < arrayOfInt3.length; n++)
    {
      int i1 = n >= k ? arrayOfInt1[(n - k)] : 0;
      int i2 = n >= m ? arrayOfInt2[(n - m)] : 0;
      if (this.D < 0)
        i1 ^= -1;
      if (paramBigInteger.D < 0)
        i2 ^= -1;
      arrayOfInt3[n] = (i1 | i2);
      if (i == 0)
        continue;
      arrayOfInt3[n] ^= -1;
    }
    BigInteger localBigInteger = new BigInteger(1, arrayOfInt3);
    if (i != 0)
      localBigInteger = localBigInteger.not();
    return localBigInteger;
  }

  public BigInteger setBit(int paramInt)
    throws ArithmeticException
  {
    if (paramInt < 0)
      throw new ArithmeticException("Bit address less than zero");
    if (testBit(paramInt))
      return this;
    if ((this.D > 0) && (paramInt < bitLength() - 1))
      return D(paramInt);
    return or(ONE.shiftLeft(paramInt));
  }

  public BigInteger clearBit(int paramInt)
    throws ArithmeticException
  {
    if (paramInt < 0)
      throw new ArithmeticException("Bit address less than zero");
    if (!testBit(paramInt))
      return this;
    if ((this.D > 0) && (paramInt < bitLength() - 1))
      return D(paramInt);
    return andNot(ONE.shiftLeft(paramInt));
  }

  public BigInteger flipBit(int paramInt)
    throws ArithmeticException
  {
    if (paramInt < 0)
      throw new ArithmeticException("Bit address less than zero");
    if ((this.D > 0) && (paramInt < bitLength() - 1))
      return D(paramInt);
    return xor(ONE.shiftLeft(paramInt));
  }

  private BigInteger D(int paramInt)
  {
    int[] arrayOfInt = new int[this.H.length];
    System.arraycopy(this.H, 0, arrayOfInt, 0, arrayOfInt.length);
    arrayOfInt[(arrayOfInt.length - 1 - (paramInt >>> 5))] ^= 1 << (paramInt & 0x1F);
    return new BigInteger(this.D, arrayOfInt);
  }

  public String toString()
  {
    return toString(10);
  }

  public String toString(int paramInt)
  {
    if (this.H == null)
      return "null";
    if (this.D == 0)
      return "0";
    StringBuffer localStringBuffer = new StringBuffer();
    int i;
    if (paramInt == 16)
    {
      for (i = 0; i < this.H.length; i++)
      {
        String str = "0000000" + Integer.toHexString(this.H[i]);
        str = str.substring(str.length() - 8);
        localStringBuffer.append(str);
      }
    }
    else if (paramInt == 2)
    {
      localStringBuffer.append('1');
      for (i = bitLength() - 2; i >= 0; i--)
        localStringBuffer.append(testBit(i) ? '1' : '0');
    }
    else
    {
      localObject = new Stack();
      BigInteger localBigInteger1 = new BigInteger(Integer.toString(paramInt, paramInt), paramInt);
      for (BigInteger localBigInteger2 = abs(); !localBigInteger2.equals(ZERO); localBigInteger2 = localBigInteger2.divide(localBigInteger1))
      {
        BigInteger localBigInteger3 = localBigInteger2.mod(localBigInteger1);
        if (localBigInteger3.equals(ZERO))
          ((Stack)localObject).push("0");
        else
          ((Stack)localObject).push(Integer.toString(localBigInteger3.H[0], paramInt));
      }
      while (!((Stack)localObject).empty())
        localStringBuffer.append((String)((Stack)localObject).pop());
    }
    for (Object localObject = localStringBuffer.toString(); (((String)localObject).length() > 1) && (((String)localObject).charAt(0) == '0'); localObject = ((String)localObject).substring(1));
    if (((String)localObject).length() == 0)
      localObject = "0";
    else if (this.D == -1)
      localObject = "-" + (String)localObject;
    return (String)localObject;
  }

  public static BigInteger valueOf(long paramLong)
  {
    if (paramLong == 0L)
      return ZERO;
    if (paramLong < 0L)
    {
      if (paramLong == -9223372036854775808L)
        return valueOf(paramLong ^ 0xFFFFFFFF).not();
      return valueOf(-paramLong).negate();
    }
    byte[] arrayOfByte = new byte[8];
    for (int i = 0; i < 8; i++)
    {
      arrayOfByte[(7 - i)] = (byte)(int)paramLong;
      paramLong >>= 8;
    }
    return new BigInteger(arrayOfByte);
  }

  public int getLowestSetBit()
  {
    if (this.D == 0)
      return -1;
    int i = this.H.length;
    while (true)
    {
      i--;
      if (i <= 0)
        break;
      if (this.H[i] == 0)
        continue;
    }
    int j = this.H[i];
    for (int k = (j & 0xFF) == 0 ? 23 : (j & 0xFFFF) == 0 ? 15 : (j & 0xFF0000) == 0 ? 7 : 31; (k > 0) && (j << k != -2147483648); k--);
    return (this.H.length - i) * 32 - (k + 1);
  }

  public boolean testBit(int paramInt)
    throws ArithmeticException
  {
    if (paramInt < 0)
      throw new ArithmeticException("Bit position must not be negative");
    if (this.D < 0)
      return !not().testBit(paramInt);
    int i = paramInt / 32;
    if (i >= this.H.length)
      return false;
    int j = this.H[(this.H.length - 1 - i)];
    return (j >> paramInt % 32 & 0x1) > 0;
  }

  static
  {
    ZERO.J = 0;
    ZERO.B = 0;
    ONE.J = 1;
    ONE.B = 1;
    I.J = 1;
    I.B = 2;
    A = new int[F.length];
    for (int i = 0; i < F.length; i++)
    {
      int[] arrayOfInt = F[i];
      int j = 1;
      for (int k = 0; k < arrayOfInt.length; k++)
        j *= arrayOfInt[k];
      A[i] = j;
    }
    C = new byte[] { -1, 127, 63, 31, 15, 7, 3, 1 };
    G = new byte[] { 0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8 };
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.math.BigInteger
 * JD-Core Version:    0.6.0
 */