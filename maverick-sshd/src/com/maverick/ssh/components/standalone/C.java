package com.maverick.ssh.components.standalone;

import com.maverick.crypto.engines.CipherEngine;
import java.io.IOException;

final class C
  implements CipherEngine
{
  private static final byte[][] P = { { -87, 103, -77, -24, 4, -3, -93, 118, -102, -110, -128, 120, -28, -35, -47, 56, 13, -58, 53, -104, 24, -9, -20, 108, 67, 117, 55, 38, -6, 19, -108, 72, -14, -48, -117, 48, -124, 84, -33, 35, 25, 91, 61, 89, -13, -82, -94, -126, 99, 1, -125, 46, -39, 81, -101, 124, -90, -21, -91, -66, 22, 12, -29, 97, -64, -116, 58, -11, 115, 44, 37, 11, -69, 78, -119, 107, 83, 106, -76, -15, -31, -26, -67, 69, -30, -12, -74, 102, -52, -107, 3, 86, -44, 28, 30, -41, -5, -61, -114, -75, -23, -49, -65, -70, -22, 119, 57, -81, 51, -55, 98, 113, -127, 121, 9, -83, 36, -51, -7, -40, -27, -59, -71, 77, 68, 8, -122, -25, -95, 29, -86, -19, 6, 112, -78, -46, 65, 123, -96, 17, 49, -62, 39, -112, 32, -10, 96, -1, -106, 92, -79, -85, -98, -100, 82, 27, 95, -109, 10, -17, -111, -123, 73, -18, 45, 79, -113, 59, 71, -121, 109, 70, -42, 62, 105, 100, 42, -50, -53, 47, -4, -105, 5, 122, -84, 127, -43, 26, 75, 14, -89, 90, 40, 20, 63, 41, -120, 60, 76, 2, -72, -38, -80, 23, 85, 31, -118, 125, 87, -57, -115, 116, -73, -60, -97, 114, 126, 21, 34, 18, 88, 7, -103, 52, 110, 80, -34, 104, 101, -68, -37, -8, -56, -88, 43, 64, -36, -2, 50, -92, -54, 16, 33, -16, -45, 93, 15, 0, 111, -99, 54, 66, 74, 94, -63, -32 }, { 117, -13, -58, -12, -37, 123, -5, -56, 74, -45, -26, 107, 69, 125, -24, 75, -42, 50, -40, -3, 55, 113, -15, -31, 48, 15, -8, 27, -121, -6, 6, 63, 94, -70, -82, 91, -118, 0, -68, -99, 109, -63, -79, 14, -128, 93, -46, -43, -96, -124, 7, 20, -75, -112, 44, -93, -78, 115, 76, 84, -110, 116, 54, 81, 56, -80, -67, 90, -4, 96, 98, -106, 108, 66, -9, 16, 124, 40, 39, -116, 19, -107, -100, -57, 36, 70, 59, 112, -54, -29, -123, -53, 17, -48, -109, -72, -90, -125, 32, -1, -97, 119, -61, -52, 3, 111, 8, -65, 64, -25, 43, -30, 121, 12, -86, -126, 65, 58, -22, -71, -28, -102, -92, -105, 126, -38, 122, 23, 102, -108, -95, 29, 61, -16, -34, -77, 11, 114, -89, 28, -17, -47, 83, 62, -113, 51, 38, 95, -20, 118, 42, 73, -127, -120, -18, 33, -60, 26, -21, -39, -59, 57, -103, -51, -83, 49, -117, 1, 24, 35, -35, 31, 78, 45, -7, 72, 79, -14, 101, -114, 120, 92, 88, 25, -115, -27, -104, 87, 103, 127, 5, 100, -81, 99, -74, -2, -11, -73, 60, -91, -50, -23, 104, 68, -32, 77, 67, 105, 41, 46, -84, 21, 89, -88, 10, -98, 110, 71, -33, 52, 53, 106, -49, -36, 34, -55, -64, -101, -119, -44, -19, -85, 18, -94, 13, 82, -69, 2, 47, -87, -41, 97, 30, -76, 80, 4, -10, -62, 22, 37, -122, 86, 85, 9, -66, -111 } };
  private boolean O = false;
  private int[] T = new int[256];
  private int[] S = new int[256];
  private int[] R = new int[256];
  private int[] Q = new int[256];
  private int[] N;
  private int[] V;
  private int U = 0;
  private byte[] M = null;

  public C()
  {
    int[] arrayOfInt1 = new int[2];
    int[] arrayOfInt2 = new int[2];
    int[] arrayOfInt3 = new int[2];
    for (int j = 0; j < 256; j++)
    {
      int i = P[0][j] & 0xFF;
      arrayOfInt1[0] = i;
      arrayOfInt2[0] = (M(i) & 0xFF);
      arrayOfInt3[0] = (J(i) & 0xFF);
      i = P[1][j] & 0xFF;
      arrayOfInt1[1] = i;
      arrayOfInt2[1] = (M(i) & 0xFF);
      arrayOfInt3[1] = (J(i) & 0xFF);
      this.T[j] = (arrayOfInt1[1] | arrayOfInt2[1] << 8 | arrayOfInt3[1] << 16 | arrayOfInt3[1] << 24);
      this.S[j] = (arrayOfInt3[0] | arrayOfInt3[0] << 8 | arrayOfInt2[0] << 16 | arrayOfInt1[0] << 24);
      this.R[j] = (arrayOfInt2[1] | arrayOfInt3[1] << 8 | arrayOfInt1[1] << 16 | arrayOfInt3[1] << 24);
      this.Q[j] = (arrayOfInt2[0] | arrayOfInt1[0] << 8 | arrayOfInt3[0] << 16 | arrayOfInt2[0] << 24);
    }
  }

  public void init(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    this.O = paramBoolean;
    this.M = paramArrayOfByte;
    this.U = (this.M.length / 8);
    A(this.M);
  }

  public final int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws IOException
  {
    if (this.M == null)
      throw new IOException("Twofish not initialised");
    if (paramInt1 + 16 > paramArrayOfByte1.length)
      throw new IOException("input buffer too short");
    if (paramInt2 + 16 > paramArrayOfByte2.length)
      throw new IOException("output buffer too short");
    if (this.O)
      B(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    else
      A(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return 16;
  }

  public int getBlockSize()
  {
    return 16;
  }

  private void A(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt1 = new int[4];
    int[] arrayOfInt2 = new int[4];
    int[] arrayOfInt3 = new int[4];
    this.N = new int[40];
    if ((this.U < 1) || (this.U > 4))
      throw new IllegalArgumentException("Key size larger than 256 bits");
    int i = 0;
    int j = 0;
    while (i < this.U)
    {
      j = i * 8;
      arrayOfInt1[i] = C(paramArrayOfByte, j);
      arrayOfInt2[i] = C(paramArrayOfByte, j + 4);
      arrayOfInt3[(this.U - 1 - i)] = B(arrayOfInt1[i], arrayOfInt2[i]);
      i++;
    }
    for (int m = 0; m < 20; m++)
    {
      i = m * 33686018;
      j = A(i, arrayOfInt1);
      int k = A(i + 16843009, arrayOfInt2);
      k = k << 8 | k >>> 24;
      j += k;
      this.N[(m * 2)] = j;
      j += k;
      this.N[(m * 2 + 1)] = (j << 9 | j >>> 23);
    }
    m = arrayOfInt3[0];
    int n = arrayOfInt3[1];
    int i1 = arrayOfInt3[2];
    int i2 = arrayOfInt3[3];
    this.V = new int[1024];
    for (int i7 = 0; i7 < 256; i7++)
    {
      int i6;
      int i5;
      int i4;
      int i3 = i4 = i5 = i6 = i7;
      switch (this.U & 0x3)
      {
      case 1:
        this.V[(i7 * 2)] = this.T[(P[0][i3] & 0xFF ^ G(m))];
        this.V[(i7 * 2 + 1)] = this.S[(P[0][i4] & 0xFF ^ N(m))];
        this.V[(i7 * 2 + 512)] = this.R[(P[1][i5] & 0xFF ^ K(m))];
        this.V[(i7 * 2 + 513)] = this.Q[(P[1][i6] & 0xFF ^ D(m))];
        break;
      case 0:
        i3 = P[1][i3] & 0xFF ^ G(i2);
        i4 = P[0][i4] & 0xFF ^ N(i2);
        i5 = P[0][i5] & 0xFF ^ K(i2);
        i6 = P[1][i6] & 0xFF ^ D(i2);
      case 3:
        i3 = P[1][i3] & 0xFF ^ G(i1);
        i4 = P[1][i4] & 0xFF ^ N(i1);
        i5 = P[0][i5] & 0xFF ^ K(i1);
        i6 = P[0][i6] & 0xFF ^ D(i1);
      case 2:
        this.V[(i7 * 2)] = this.T[(P[0][(P[0][i3] & 0xFF ^ G(n))] & 0xFF ^ G(m))];
        this.V[(i7 * 2 + 1)] = this.S[(P[0][(P[1][i4] & 0xFF ^ N(n))] & 0xFF ^ N(m))];
        this.V[(i7 * 2 + 512)] = this.R[(P[1][(P[0][i5] & 0xFF ^ K(n))] & 0xFF ^ K(m))];
        this.V[(i7 * 2 + 513)] = this.Q[(P[1][(P[1][i6] & 0xFF ^ D(n))] & 0xFF ^ D(m))];
      }
    }
  }

  private void B(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = C(paramArrayOfByte1, paramInt1) ^ this.N[0];
    int j = C(paramArrayOfByte1, paramInt1 + 4) ^ this.N[1];
    int k = C(paramArrayOfByte1, paramInt1 + 8) ^ this.N[2];
    int m = C(paramArrayOfByte1, paramInt1 + 12) ^ this.N[3];
    int n = 8;
    for (int i3 = 0; i3 < 16; i3 += 2)
    {
      int i1 = I(i);
      int i2 = F(j);
      k ^= i1 + i2 + this.N[(n++)];
      k = k >>> 1 | k << 31;
      m = (m << 1 | m >>> 31) ^ i1 + 2 * i2 + this.N[(n++)];
      i1 = I(k);
      i2 = F(m);
      i ^= i1 + i2 + this.N[(n++)];
      i = i >>> 1 | i << 31;
      j = (j << 1 | j >>> 31) ^ i1 + 2 * i2 + this.N[(n++)];
    }
    A(k ^ this.N[4], paramArrayOfByte2, paramInt2);
    A(m ^ this.N[5], paramArrayOfByte2, paramInt2 + 4);
    A(i ^ this.N[6], paramArrayOfByte2, paramInt2 + 8);
    A(j ^ this.N[7], paramArrayOfByte2, paramInt2 + 12);
  }

  private void A(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    int i = C(paramArrayOfByte1, paramInt1) ^ this.N[4];
    int j = C(paramArrayOfByte1, paramInt1 + 4) ^ this.N[5];
    int k = C(paramArrayOfByte1, paramInt1 + 8) ^ this.N[6];
    int m = C(paramArrayOfByte1, paramInt1 + 12) ^ this.N[7];
    int n = 39;
    for (int i3 = 0; i3 < 16; i3 += 2)
    {
      int i1 = I(i);
      int i2 = F(j);
      m ^= i1 + 2 * i2 + this.N[(n--)];
      k = (k << 1 | k >>> 31) ^ i1 + i2 + this.N[(n--)];
      m = m >>> 1 | m << 31;
      i1 = I(k);
      i2 = F(m);
      j ^= i1 + 2 * i2 + this.N[(n--)];
      i = (i << 1 | i >>> 31) ^ i1 + i2 + this.N[(n--)];
      j = j >>> 1 | j << 31;
    }
    A(k ^ this.N[0], paramArrayOfByte2, paramInt2);
    A(m ^ this.N[1], paramArrayOfByte2, paramInt2 + 4);
    A(i ^ this.N[2], paramArrayOfByte2, paramInt2 + 8);
    A(j ^ this.N[3], paramArrayOfByte2, paramInt2 + 12);
  }

  private final int A(int paramInt, int[] paramArrayOfInt)
  {
    int i = G(paramInt);
    int j = N(paramInt);
    int k = K(paramInt);
    int m = D(paramInt);
    int n = paramArrayOfInt[0];
    int i1 = paramArrayOfInt[1];
    int i2 = paramArrayOfInt[2];
    int i3 = paramArrayOfInt[3];
    int i4 = 0;
    switch (this.U & 0x3)
    {
    case 1:
      i4 = this.T[(P[0][i] & 0xFF ^ G(n))] ^ this.S[(P[0][j] & 0xFF ^ N(n))] ^ this.R[(P[1][k] & 0xFF ^ K(n))] ^ this.Q[(P[1][m] & 0xFF ^ D(n))];
      break;
    case 0:
      i = P[1][i] & 0xFF ^ G(i3);
      j = P[0][j] & 0xFF ^ N(i3);
      k = P[0][k] & 0xFF ^ K(i3);
      m = P[1][m] & 0xFF ^ D(i3);
    case 3:
      i = P[1][i] & 0xFF ^ G(i2);
      j = P[1][j] & 0xFF ^ N(i2);
      k = P[0][k] & 0xFF ^ K(i2);
      m = P[0][m] & 0xFF ^ D(i2);
    case 2:
      i4 = this.T[(P[0][(P[0][i] & 0xFF ^ G(i1))] & 0xFF ^ G(n))] ^ this.S[(P[0][(P[1][j] & 0xFF ^ N(i1))] & 0xFF ^ N(n))] ^ this.R[(P[1][(P[0][k] & 0xFF ^ K(i1))] & 0xFF ^ K(n))] ^ this.Q[(P[1][(P[1][m] & 0xFF ^ D(i1))] & 0xFF ^ D(n))];
    }
    return i4;
  }

  private final int B(int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    for (int j = 0; j < 4; j++)
      i = H(i);
    i ^= paramInt1;
    for (j = 0; j < 4; j++)
      i = H(i);
    return i;
  }

  private final int H(int paramInt)
  {
    int i = paramInt >>> 24 & 0xFF;
    int j = (i << 1 ^ ((i & 0x80) != 0 ? 333 : 0)) & 0xFF;
    int k = i >>> 1 ^ ((i & 0x1) != 0 ? 166 : 0) ^ j;
    return paramInt << 8 ^ k << 24 ^ j << 16 ^ k << 8 ^ i;
  }

  private final int L(int paramInt)
  {
    return paramInt >> 1 ^ ((paramInt & 0x1) != 0 ? 180 : 0);
  }

  private final int E(int paramInt)
  {
    return paramInt >> 2 ^ ((paramInt & 0x2) != 0 ? 180 : 0) ^ ((paramInt & 0x1) != 0 ? 90 : 0);
  }

  private final int M(int paramInt)
  {
    return paramInt ^ E(paramInt);
  }

  private final int J(int paramInt)
  {
    return paramInt ^ L(paramInt) ^ E(paramInt);
  }

  private final int G(int paramInt)
  {
    return paramInt & 0xFF;
  }

  private final int N(int paramInt)
  {
    return paramInt >>> 8 & 0xFF;
  }

  private final int K(int paramInt)
  {
    return paramInt >>> 16 & 0xFF;
  }

  private final int D(int paramInt)
  {
    return paramInt >>> 24 & 0xFF;
  }

  private final int I(int paramInt)
  {
    return this.V[(0 + 2 * (paramInt & 0xFF))] ^ this.V[(1 + 2 * (paramInt >>> 8 & 0xFF))] ^ this.V[(512 + 2 * (paramInt >>> 16 & 0xFF))] ^ this.V[(513 + 2 * (paramInt >>> 24 & 0xFF))];
  }

  private final int F(int paramInt)
  {
    return this.V[(0 + 2 * (paramInt >>> 24 & 0xFF))] ^ this.V[(1 + 2 * (paramInt & 0xFF))] ^ this.V[(512 + 2 * (paramInt >>> 8 & 0xFF))] ^ this.V[(513 + 2 * (paramInt >>> 16 & 0xFF))];
  }

  private final int C(byte[] paramArrayOfByte, int paramInt)
  {
    return paramArrayOfByte[paramInt] & 0xFF | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 3)] & 0xFF) << 24;
  }

  private final void A(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = (byte)paramInt1;
    paramArrayOfByte[(paramInt2 + 1)] = (byte)(paramInt1 >> 8);
    paramArrayOfByte[(paramInt2 + 2)] = (byte)(paramInt1 >> 16);
    paramArrayOfByte[(paramInt2 + 3)] = (byte)(paramInt1 >> 24);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.C
 * JD-Core Version:    0.6.0
 */