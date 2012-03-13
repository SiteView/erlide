package com.maverick.ssh.components.standalone;

import com.maverick.crypto.digests.HMac;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshHmac;
import java.io.IOException;

public class AbstractHmac
  implements SshHmac
{
  HMac B;
  String A;

  public AbstractHmac(String paramString, HMac paramHMac)
  {
    this.A = paramString;
    this.B = paramHMac;
  }

  public String getAlgorithm()
  {
    return this.A;
  }

  public int getMacLength()
  {
    return this.B.getOutputSize();
  }

  public void generate(long paramLong, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    byte[] arrayOfByte1 = new byte[this.B.getMacSize()];
    byte[] arrayOfByte2 = new byte[4];
    arrayOfByte2[0] = (byte)(int)(paramLong >> 24);
    arrayOfByte2[1] = (byte)(int)(paramLong >> 16);
    arrayOfByte2[2] = (byte)(int)(paramLong >> 8);
    arrayOfByte2[3] = (byte)(int)(paramLong >> 0);
    this.B.update(arrayOfByte2, 0, arrayOfByte2.length);
    this.B.update(paramArrayOfByte1, paramInt1, paramInt2);
    this.B.doFinal(arrayOfByte1, 0);
    System.arraycopy(arrayOfByte1, 0, paramArrayOfByte2, paramInt3, this.B.getOutputSize());
  }

  public void init(byte[] paramArrayOfByte)
    throws SshException
  {
    byte[] arrayOfByte = new byte[this.B.getMacSize()];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, arrayOfByte.length);
    try
    {
      this.B.init(arrayOfByte);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public boolean verify(long paramLong, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (paramArrayOfByte1.length < paramInt1 + paramInt2)
      throw new RuntimeException("Not enough data for message and mac!");
    if (paramArrayOfByte2.length - paramInt3 < getMacLength())
      throw new RuntimeException(String.valueOf(getMacLength()) + " bytes of MAC data required!");
    byte[] arrayOfByte = new byte[this.B.getOutputSize()];
    generate(paramLong, paramArrayOfByte1, paramInt1, paramInt2, arrayOfByte, 0);
    for (int i = 0; i < arrayOfByte.length; i++)
      if (arrayOfByte[i] != paramArrayOfByte2[(paramInt3 + i)])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.AbstractHmac
 * JD-Core Version:    0.6.0
 */