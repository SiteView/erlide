package com.maverick.crypto.digests;

public abstract class GeneralDigest
  implements Digest
{
  private byte[] A;
  private int B;
  private long C;

  protected GeneralDigest()
  {
    this.A = new byte[4];
    this.B = 0;
  }

  protected GeneralDigest(GeneralDigest paramGeneralDigest)
  {
    this.A = new byte[paramGeneralDigest.A.length];
    System.arraycopy(paramGeneralDigest.A, 0, this.A, 0, paramGeneralDigest.A.length);
    this.B = paramGeneralDigest.B;
    this.C = paramGeneralDigest.C;
  }

  public void update(byte paramByte)
  {
    this.A[(this.B++)] = paramByte;
    if (this.B == this.A.length)
    {
      processWord(this.A, 0);
      this.B = 0;
    }
    this.C += 1L;
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    while ((this.B != 0) && (paramInt2 > 0))
    {
      update(paramArrayOfByte[paramInt1]);
      paramInt1++;
      paramInt2--;
    }
    while (paramInt2 > this.A.length)
    {
      processWord(paramArrayOfByte, paramInt1);
      paramInt1 += this.A.length;
      paramInt2 -= this.A.length;
      this.C += this.A.length;
    }
    while (paramInt2 > 0)
    {
      update(paramArrayOfByte[paramInt1]);
      paramInt1++;
      paramInt2--;
    }
  }

  public void finish()
  {
    long l = this.C << 3;
    update(-128);
    while (this.B != 0)
      update(0);
    processLength(l);
    processBlock();
  }

  public void reset()
  {
    this.C = 0L;
    this.B = 0;
    for (int i = 0; i < this.A.length; i++)
      this.A[i] = 0;
  }

  protected abstract void processWord(byte[] paramArrayOfByte, int paramInt);

  protected abstract void processLength(long paramLong);

  protected abstract void processBlock();

  public abstract int getDigestSize();

  public abstract int doFinal(byte[] paramArrayOfByte, int paramInt);

  public abstract String getAlgorithmName();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.digests.GeneralDigest
 * JD-Core Version:    0.6.0
 */