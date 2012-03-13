package com.maverick.crypto.digests;

public class GeneralHMac
  implements HMac
{
  private Digest f;
  private int c;
  private int e;
  private byte[] b = new byte[64];
  private byte[] d = new byte[64];

  public GeneralHMac(Digest paramDigest)
  {
    this.f = paramDigest;
    this.c = paramDigest.getDigestSize();
    this.e = this.c;
  }

  public GeneralHMac(Digest paramDigest, int paramInt)
  {
    this.f = paramDigest;
    this.c = paramDigest.getDigestSize();
    this.e = paramInt;
  }

  public String getAlgorithmName()
  {
    return this.f.getAlgorithmName() + "/HMAC";
  }

  public int getOutputSize()
  {
    return this.e;
  }

  public Digest getUnderlyingDigest()
  {
    return this.f;
  }

  public void init(byte[] paramArrayOfByte)
  {
    this.f.reset();
    if (paramArrayOfByte.length > 64)
    {
      this.f.update(paramArrayOfByte, 0, paramArrayOfByte.length);
      this.f.doFinal(this.b, 0);
      for (i = this.c; i < this.b.length; i++)
        this.b[i] = 0;
    }
    else
    {
      System.arraycopy(paramArrayOfByte, 0, this.b, 0, paramArrayOfByte.length);
      for (i = paramArrayOfByte.length; i < this.b.length; i++)
        this.b[i] = 0;
    }
    this.d = new byte[this.b.length];
    System.arraycopy(this.b, 0, this.d, 0, this.b.length);
    for (int i = 0; i < this.b.length; i++)
    {
      int tmp156_155 = i;
      byte[] tmp156_152 = this.b;
      tmp156_152[tmp156_155] = (byte)(tmp156_152[tmp156_155] ^ 0x36);
    }
    for (i = 0; i < this.d.length; i++)
    {
      int tmp185_184 = i;
      byte[] tmp185_181 = this.d;
      tmp185_181[tmp185_184] = (byte)(tmp185_181[tmp185_184] ^ 0x5C);
    }
    this.f.update(this.b, 0, this.b.length);
  }

  public int getMacSize()
  {
    return this.c;
  }

  public void update(byte paramByte)
  {
    this.f.update(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.f.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[this.c];
    this.f.doFinal(arrayOfByte, 0);
    this.f.update(this.d, 0, this.d.length);
    this.f.update(arrayOfByte, 0, arrayOfByte.length);
    int i = this.f.doFinal(paramArrayOfByte, paramInt);
    reset();
    return i;
  }

  public void reset()
  {
    this.f.reset();
    this.f.update(this.b, 0, this.b.length);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.digests.GeneralHMac
 * JD-Core Version:    0.6.0
 */