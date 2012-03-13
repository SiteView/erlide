package com.maverick.ssh.components.jce;

import com.maverick.ssh.components.SshCipher;
import java.io.IOException;

public class Ssh1Des3 extends SshCipher
{
  Ssh1Des U = new Ssh1Des();
  Ssh1Des T = new Ssh1Des();
  Ssh1Des S = new Ssh1Des();
  int V;

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
    this.V = paramInt;
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, 8);
    this.U.init(paramInt == 0 ? 0 : 1, paramArrayOfByte1, arrayOfByte);
    System.arraycopy(paramArrayOfByte2, 8, arrayOfByte, 0, 8);
    this.T.init(paramInt == 0 ? 1 : 0, paramArrayOfByte1, arrayOfByte);
    System.arraycopy(paramArrayOfByte2, 16, arrayOfByte, 0, 8);
    this.S.init(paramInt == 0 ? 0 : 1, paramArrayOfByte1, arrayOfByte);
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    if (this.V == 0)
    {
      this.U.transform(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
      this.T.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
      this.S.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
    }
    else
    {
      this.S.transform(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
      this.T.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
      this.U.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh1Des3
 * JD-Core Version:    0.6.0
 */