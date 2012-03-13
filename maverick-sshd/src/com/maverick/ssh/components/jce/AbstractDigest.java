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
  MessageDigest B;

  public AbstractDigest(String paramString)
    throws NoSuchAlgorithmException
  {
    this.B = (JCEProvider.getProviderForAlgorithm(paramString) == null ? MessageDigest.getInstance(paramString) : MessageDigest.getInstance(paramString, JCEProvider.getProviderForAlgorithm(paramString)));
  }

  public byte[] doFinal()
  {
    return this.B.digest();
  }

  public void putBigInteger(BigInteger paramBigInteger)
  {
    byte[] arrayOfByte = paramBigInteger.toByteArray();
    putInt(arrayOfByte.length);
    putBytes(arrayOfByte);
  }

  public void putByte(byte paramByte)
  {
    this.B.update(paramByte);
  }

  public void putBytes(byte[] paramArrayOfByte)
  {
    this.B.update(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.B.update(paramArrayOfByte, paramInt1, paramInt2);
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
    this.B.reset();
  }

  public String getProvider()
  {
    return this.B.getProvider().getName();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AbstractDigest
 * JD-Core Version:    0.6.0
 */