package com.maverick.crypto.encoders;

public class Hex
{
  private static HexTranslator A = new HexTranslator();

  public static byte[] encode(byte[] paramArrayOfByte)
  {
    return encode(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public static byte[] encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2 * 2];
    A.encode(paramArrayOfByte, paramInt1, paramInt2, arrayOfByte, 0);
    return arrayOfByte;
  }

  public static byte[] decode(String paramString)
  {
    byte[] arrayOfByte = new byte[paramString.length() / 2];
    String str = paramString.toLowerCase();
    for (int i = 0; i < str.length(); i += 2)
    {
      int j = str.charAt(i);
      int k = str.charAt(i + 1);
      int m = i / 2;
      if (j < 97)
        arrayOfByte[m] = (byte)(j - 48 << 4);
      else
        arrayOfByte[m] = (byte)(j - 97 + 10 << 4);
      if (k < 97)
      {
        int tmp92_90 = m;
        byte[] tmp92_89 = arrayOfByte;
        tmp92_89[tmp92_90] = (byte)(tmp92_89[tmp92_90] + (byte)(k - 48));
      }
      else
      {
        int tmp109_107 = m;
        byte[] tmp109_106 = arrayOfByte;
        tmp109_106[tmp109_107] = (byte)(tmp109_106[tmp109_107] + (byte)(k - 97 + 10));
      }
    }
    return arrayOfByte;
  }

  public static byte[] decode(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
    A.decode(paramArrayOfByte, 0, paramArrayOfByte.length, arrayOfByte, 0);
    return arrayOfByte;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.encoders.Hex
 * JD-Core Version:    0.6.0
 */