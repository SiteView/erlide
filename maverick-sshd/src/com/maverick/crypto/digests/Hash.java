package com.maverick.crypto.digests;

import com.maverick.util.ByteArrayWriter;
import java.math.BigInteger;

public class Hash
{
  private Digest A;

  public Hash(String paramString)
  {
    this.A = DigestFactory.createDigest(paramString);
  }

  public Hash(Digest paramDigest)
  {
    this.A = paramDigest;
  }

  public void putBigInteger(BigInteger paramBigInteger)
  {
    byte[] arrayOfByte = paramBigInteger.toByteArray();
    putInt(arrayOfByte.length);
    putBytes(arrayOfByte);
  }

  public void putByte(byte paramByte)
  {
    this.A.update(paramByte);
  }

  public void putBytes(byte[] paramArrayOfByte)
  {
    this.A.update(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.A.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void putInt(int paramInt)
  {
    putBytes(ByteArrayWriter.encodeInt(paramInt));
  }

  public void putString(String paramString)
  {
    putInt(paramString.length());
    putBytes(paramString.getBytes());
  }

  public void reset()
  {
    this.A.reset();
  }

  public byte[] doFinal()
  {
    byte[] arrayOfByte = new byte[this.A.getDigestSize()];
    this.A.doFinal(arrayOfByte, 0);
    return arrayOfByte;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.digests.Hash
 * JD-Core Version:    0.6.0
 */