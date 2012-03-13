package com.maverick.crypto.digests;

public abstract class GeneralDigest
  implements Digest
{
  private byte[] b;
  private int c;
  private long d;

  protected GeneralDigest()
  {
    this.b = new byte[4];
    this.c = 0;
  }

  protected GeneralDigest(GeneralDigest paramGeneralDigest)
  {
    this.b = new byte[paramGeneralDigest.b.length];
    System.arraycopy(paramGeneralDigest.b, 0, this.b, 0, paramGeneralDigest.b.length);
    this.c = paramGeneralDigest.c;
    this.d = paramGeneralDigest.d;
  }

  public void update(byte paramByte)
  {
    this.b[(this.c++)] = paramByte;
    if (this.c == this.b.length)
    {
      processWord(this.b, 0);
      this.c = 0;
    }
    this.d += 1L;
  }

  public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    while ((this.c != 0) && (paramInt2 > 0))
    {
      update(paramArrayOfByte[paramInt1]);
      paramInt1++;
      paramInt2--;
    }
    while (paramInt2 > this.b.length)
    {
      processWord(paramArrayOfByte, paramInt1);
      paramInt1 += this.b.length;
      paramInt2 -= this.b.length;
      this.d += this.b.length;
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
    long l = this.d << 3;
    update(-128);
    while (this.c != 0)
      update(0);
    processLength(l);
    processBlock();
  }

  public void reset()
  {
    this.d = 0L;
    this.c = 0;
    for (int i = 0; i < this.b.length; i++)
      this.b[i] = 0;
  }

  protected abstract void processWord(byte[] paramArrayOfByte, int paramInt);

  protected abstract void processLength(long paramLong);

  protected abstract void processBlock();

  public abstract int getDigestSize();

  public abstract int doFinal(byte[] paramArrayOfByte, int paramInt);

  public abstract String getAlgorithmName();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.digests.GeneralDigest
 * JD-Core Version:    0.6.0
 */