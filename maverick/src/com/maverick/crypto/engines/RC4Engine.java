package com.maverick.crypto.engines;

public class RC4Engine
{
  private byte[] d = null;
  private int c = 0;
  private int e = 0;
  private byte[] b = null;

  public void init(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    this.b = paramArrayOfByte;
    b(this.b);
  }

  public String getAlgorithmName()
  {
    return "RC4";
  }

  public byte returnByte(byte paramByte)
  {
    this.c = (this.c + 1 & 0xFF);
    this.e = (this.d[this.c] + this.e & 0xFF);
    int i = this.d[this.c];
    this.d[this.c] = this.d[this.e];
    this.d[this.e] = i;
    return (byte)(paramByte ^ this.d[(this.d[this.c] + this.d[this.e] & 0xFF)]);
  }

  public void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (paramInt1 + paramInt2 > paramArrayOfByte1.length)
      throw new RuntimeException("input buffer too short");
    if (paramInt3 + paramInt2 > paramArrayOfByte2.length)
      throw new RuntimeException("output buffer too short");
    for (int i = 0; i < paramInt2; i++)
    {
      this.c = (this.c + 1 & 0xFF);
      this.e = (this.d[this.c] + this.e & 0xFF);
      int j = this.d[this.c];
      this.d[this.c] = this.d[this.e];
      this.d[this.e] = j;
      paramArrayOfByte2[(i + paramInt3)] = (byte)(paramArrayOfByte1[(i + paramInt1)] ^ this.d[(this.d[this.c] + this.d[this.e] & 0xFF)]);
    }
  }

  public void reset()
  {
    b(this.b);
  }

  private void b(byte[] paramArrayOfByte)
  {
    this.b = paramArrayOfByte;
    this.c = 0;
    this.e = 0;
    if (this.d == null)
      this.d = new byte[256];
    for (int i = 0; i < 256; i++)
      this.d[i] = (byte)i;
    i = 0;
    int j = 0;
    for (int k = 0; k < 256; k++)
    {
      j = (paramArrayOfByte[i] & 0xFF) + this.d[k] + j & 0xFF;
      int m = this.d[k];
      this.d[k] = this.d[j];
      this.d[j] = m;
      i = (i + 1) % paramArrayOfByte.length;
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.engines.RC4Engine
 * JD-Core Version:    0.6.0
 */