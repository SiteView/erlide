package com.maverick.crypto.digests;

public class GeneralHMac
  implements HMac
{
  private Digest E;
  private int B;
  private int D;
  private byte[] A = new byte[64];
  private byte[] C = new byte[64];

  public GeneralHMac(Digest paramDigest)
  {
    this.E = paramDigest;
    this.B = paramDigest.getDigestSize();
    this.D = this.B;
  }

  public GeneralHMac(Digest paramDigest, int paramInt)
  {
    this.E = paramDigest;
    this.B = paramDigest.getDigestSize();
    this.D = paramInt;
  }

  public String getAlgorithmName()
  {
    return this.E.getAlgorithmName() + "/HMAC";
  }

  public int getOutputSize()
  {
    return this.D;
  }

  public Digest getUnderlyingDigest()
  {
    return this.E;
  }

  public void init(byte[] paramArrayOfByte)
  {
    this.E.reset();
    if (paramArrayOfByte.length > 64)
    {
      this.E.update(paramArrayOfByte, 0, paramArrayOfByte.length);
      this.E.doFinal(this.A, 0);
      for (i = this.B; i < this.A.length; i++)
        this.A[i] = 0;
    }
    else
    {
      System.arraycopy(paramArrayOfByte, 0, this.A, 0, paramArrayOfByte.length);
      for (i = paramArrayOfByte.length; i < this.A.length; i++)
        this.A[i] = 0;
    }
    this.C = new byte[this.A.length];
    System.arraycopy(this.A, 0, this.C, 0, this.A.length);
    for (int i = 0; i < this.A.length; i++)
    {
      int tmp156_155 = i;
      byte[] tmp156_152 = this.A;
      tmp156_152[tmp156_155] = (byte)(tmp156_152[tmp156_155] ^ 0x36);
    }
    for (i = 0; i < this.C.length; i++)
    {
      int tmp185_184 = i;
      byte[] tmp185_181 = this.C;
      tmp185_181[tmp185_184] = (byte)(tmp185_181[tmp185_184] ^ 0x5C);
    }
    this.E.update(this.A, 0, this.A.length);
  }

  public int getMacSize()
  {
    return this.B;
  }

  public void update(byte paramByte)
  {
    this.E.update(paramByte);
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.E.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte = new byte[this.B];
    this.E.doFinal(arrayOfByte, 0);
    this.E.update(this.C, 0, this.C.length);
    this.E.update(arrayOfByte, 0, arrayOfByte.length);
    int i = this.E.doFinal(paramArrayOfByte, paramInt);
    reset();
    return i;
  }

  public void reset()
  {
    this.E.reset();
    this.E.update(this.A, 0, this.A.length);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.digests.GeneralHMac
 * JD-Core Version:    0.6.0
 */