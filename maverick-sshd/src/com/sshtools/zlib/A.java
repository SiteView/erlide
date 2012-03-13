package com.sshtools.zlib;

final class A
{
  long A(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte == null)
      return 1L;
    long l1 = paramLong & 0xFFFF;
    long l2 = paramLong >> 16 & 0xFFFF;
    while (paramInt2 > 0)
    {
      int i = paramInt2 < 5552 ? paramInt2 : 5552;
      paramInt2 -= i;
      while (i >= 16)
      {
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
        l2 += l1;
        i -= 16;
      }
      if (i != 0)
        do
        {
          l1 += (paramArrayOfByte[(paramInt1++)] & 0xFF);
          l2 += l1;
          i--;
        }
        while (i != 0);
      l1 %= 65521L;
      l2 %= 65521L;
    }
    return l2 << 16 | l1;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.A
 * JD-Core Version:    0.6.0
 */