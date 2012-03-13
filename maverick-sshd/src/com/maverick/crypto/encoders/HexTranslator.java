package com.maverick.crypto.encoders;

public class HexTranslator
{
  private static final byte[] A = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };

  public int getEncodedBlockSize()
  {
    return 2;
  }

  public int encode(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    int i = 0;
    for (int j = 0; i < paramInt2; j += 2)
    {
      paramArrayOfByte2[(paramInt3 + j)] = A[(paramArrayOfByte1[paramInt1] >> 4 & 0xF)];
      paramArrayOfByte2[(paramInt3 + j + 1)] = A[(paramArrayOfByte1[paramInt1] & 0xF)];
      paramInt1++;
      i++;
    }
    return paramInt2 * 2;
  }

  public int getDecodedBlockSize()
  {
    return 1;
  }

  public int decode(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    int i = paramInt2 / 2;
    for (int m = 0; m < i; m++)
    {
      int j = paramArrayOfByte1[(paramInt1 + m * 2)];
      int k = paramArrayOfByte1[(paramInt1 + m * 2 + 1)];
      if (j < 97)
        paramArrayOfByte2[paramInt3] = (byte)(j - 48 << 4);
      else
        paramArrayOfByte2[paramInt3] = (byte)(j - 97 + 10 << 4);
      if (k < 97)
      {
        int tmp87_85 = paramInt3;
        byte[] tmp87_83 = paramArrayOfByte2;
        tmp87_83[tmp87_85] = (byte)(tmp87_83[tmp87_85] + (byte)(k - 48));
      }
      else
      {
        int tmp105_103 = paramInt3;
        byte[] tmp105_101 = paramArrayOfByte2;
        tmp105_101[tmp105_103] = (byte)(tmp105_101[tmp105_103] + (byte)(k - 97 + 10));
      }
      paramInt3++;
    }
    return i;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.encoders.HexTranslator
 * JD-Core Version:    0.6.0
 */