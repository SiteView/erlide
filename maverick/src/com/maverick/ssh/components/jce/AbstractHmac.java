package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshHmac;
import java.security.Provider;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public abstract class AbstractHmac
  implements SshHmac
{
  protected Mac mac;
  protected int macSize;
  protected int macLength;
  protected String jceAlgorithm;

  public AbstractHmac(String paramString, int paramInt)
  {
    this(paramString, paramInt, paramInt);
  }

  public AbstractHmac(String paramString, int paramInt1, int paramInt2)
  {
    this.jceAlgorithm = paramString;
    this.macSize = paramInt1;
    this.macLength = paramInt2;
  }

  public void generate(long paramLong, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    byte[] arrayOfByte1 = new byte[4];
    arrayOfByte1[0] = (byte)(int)(paramLong >> 24);
    arrayOfByte1[1] = (byte)(int)(paramLong >> 16);
    arrayOfByte1[2] = (byte)(int)(paramLong >> 8);
    arrayOfByte1[3] = (byte)(int)(paramLong >> 0);
    this.mac.update(arrayOfByte1);
    this.mac.update(paramArrayOfByte1, paramInt1, paramInt2);
    byte[] arrayOfByte2 = this.mac.doFinal();
    System.arraycopy(arrayOfByte2, 0, paramArrayOfByte2, paramInt3, this.macLength);
  }

  public void update(byte[] paramArrayOfByte)
  {
    this.mac.update(paramArrayOfByte);
  }

  public byte[] doFinal()
  {
    return this.mac.doFinal();
  }

  public abstract String getAlgorithm();

  public String getProvider()
  {
    return this.mac.getProvider().getName();
  }

  public int getMacLength()
  {
    return this.macLength;
  }

  public void init(byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      this.mac = (JCEProvider.getProviderForAlgorithm(this.jceAlgorithm) == null ? Mac.getInstance(this.jceAlgorithm) : Mac.getInstance(this.jceAlgorithm, JCEProvider.getProviderForAlgorithm(this.jceAlgorithm)));
      byte[] arrayOfByte = new byte[this.macSize];
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, arrayOfByte.length);
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte, this.jceAlgorithm);
      this.mac.init(localSecretKeySpec);
    }
    catch (Throwable localThrowable)
    {
      throw new SshException(localThrowable);
    }
  }

  public boolean verify(long paramLong, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    int i = getMacLength();
    byte[] arrayOfByte = new byte[i];
    generate(paramLong, paramArrayOfByte1, paramInt1, paramInt2, arrayOfByte, 0);
    for (int j = 0; j < arrayOfByte.length; j++)
      if (paramArrayOfByte2[(j + paramInt3)] != arrayOfByte[j])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AbstractHmac
 * JD-Core Version:    0.6.0
 */