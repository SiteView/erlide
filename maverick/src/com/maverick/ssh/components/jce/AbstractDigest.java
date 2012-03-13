package com.maverick.ssh.components.jce;

import com.maverick.ssh.components.Digest;
import com.maverick.util.ByteArrayWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

public class AbstractDigest
  implements Digest
{
  MessageDigest b;

  public AbstractDigest(String paramString)
    throws NoSuchAlgorithmException
  {
    this.b = (JCEProvider.getProviderForAlgorithm(paramString) == null ? MessageDigest.getInstance(paramString) : MessageDigest.getInstance(paramString, JCEProvider.getProviderForAlgorithm(paramString)));
  }

  public byte[] doFinal()
  {
    return this.b.digest();
  }

  public void putBigInteger(BigInteger paramBigInteger)
  {
    byte[] arrayOfByte = paramBigInteger.toByteArray();
    putInt(arrayOfByte.length);
    putBytes(arrayOfByte);
  }

  public void putByte(byte paramByte)
  {
    this.b.update(paramByte);
  }

  public void putBytes(byte[] paramArrayOfByte)
  {
    this.b.update(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.b.update(paramArrayOfByte, paramInt1, paramInt2);
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
    this.b.reset();
  }

  public String getProvider()
  {
    return this.b.getProvider().getName();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AbstractDigest
 * JD-Core Version:    0.6.0
 */