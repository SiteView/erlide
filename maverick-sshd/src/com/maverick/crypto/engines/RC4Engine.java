package com.maverick.crypto.engines;

public class RC4Engine
{
  private byte[] C = null;
  private int B = 0;
  private int D = 0;
  private byte[] A = null;

  public void init(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    this.A = paramArrayOfByte;
    A(this.A);
  }

  public String getAlgorithmName()
  {
    return "RC4";
  }

  public byte returnByte(byte paramByte)
  {
    this.B = (this.B + 1 & 0xFF);
    this.D = (this.C[this.B] + this.D & 0xFF);
    int i = this.C[this.B];
    this.C[this.B] = this.C[this.D];
    this.C[this.D] = i;
    return (byte)(paramByte ^ this.C[(this.C[this.B] + this.C[this.D] & 0xFF)]);
  }

  public void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (paramInt1 + paramInt2 > paramArrayOfByte1.length)
      throw new RuntimeException("input buffer too short");
    if (paramInt3 + paramInt2 > paramArrayOfByte2.length)
      throw new RuntimeException("output buffer too short");
    for (int i = 0; i < paramInt2; i++)
    {
      this.B = (this.B + 1 & 0xFF);
      this.D = (this.C[this.B] + this.D & 0xFF);
      int j = this.C[this.B];
      this.C[this.B] = this.C[this.D];
      this.C[this.D] = j;
      paramArrayOfByte2[(i + paramInt3)] = (byte)(paramArrayOfByte1[(i + paramInt1)] ^ this.C[(this.C[this.B] + this.C[this.D] & 0xFF)]);
    }
  }

  public void reset()
  {
    A(this.A);
  }

  private void A(byte[] paramArrayOfByte)
  {
    this.A = paramArrayOfByte;
    this.B = 0;
    this.D = 0;
    if (this.C == null)
      this.C = new byte[256];
    for (int i = 0; i < 256; i++)
      this.C[i] = (byte)i;
    i = 0;
    int j = 0;
    for (int k = 0; k < 256; k++)
    {
      j = (paramArrayOfByte[i] & 0xFF) + this.C[k] + j & 0xFF;
      int m = this.C[k];
      this.C[k] = this.C[j];
      this.C[j] = m;
      i = (i + 1) % paramArrayOfByte.length;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.engines.RC4Engine
 * JD-Core Version:    0.6.0
 */