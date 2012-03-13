package com.maverick.ssh.components.jce;

import com.maverick.ssh.components.SshCipher;
import java.io.IOException;

public class Ssh1Des3 extends SshCipher
{
  Ssh1Des e = new Ssh1Des();
  Ssh1Des d = new Ssh1Des();
  Ssh1Des c = new Ssh1Des();
  int f;

  public Ssh1Des3()
    throws IOException
  {
    super("3DES");
  }

  public int getBlockSize()
  {
    return 8;
  }

  public String getAlgorithm()
  {
    return "3des";
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IOException
  {
    byte[] arrayOfByte = new byte[8];
    this.f = paramInt;
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, 8);
    this.e.init(paramInt == 0 ? 0 : 1, paramArrayOfByte1, arrayOfByte);
    System.arraycopy(paramArrayOfByte2, 8, arrayOfByte, 0, 8);
    this.d.init(paramInt == 0 ? 1 : 0, paramArrayOfByte1, arrayOfByte);
    System.arraycopy(paramArrayOfByte2, 16, arrayOfByte, 0, 8);
    this.c.init(paramInt == 0 ? 0 : 1, paramArrayOfByte1, arrayOfByte);
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    if (this.f == 0)
    {
      this.e.transform(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
      this.d.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
      this.c.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
    }
    else
    {
      this.c.transform(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
      this.d.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
      this.e.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh1Des3
 * JD-Core Version:    0.6.0
 */