package com.maverick.ssh.components.standalone;

import com.maverick.ssh.components.SshCipher;
import java.io.IOException;

public class Ssh1Des3 extends SshCipher
{
  Ssh1Des b = new Ssh1Des();
  Ssh1Des a = new Ssh1Des();
  Ssh1Des _ = new Ssh1Des();
  int c;

  public Ssh1Des3()
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
  {
    byte[] arrayOfByte = new byte[8];
    this.c = paramInt;
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, 8);
    this.b.init(paramInt == 0 ? 0 : 1, paramArrayOfByte1, arrayOfByte);
    System.arraycopy(paramArrayOfByte2, 8, arrayOfByte, 0, 8);
    this.a.init(paramInt == 0 ? 1 : 0, paramArrayOfByte1, arrayOfByte);
    System.arraycopy(paramArrayOfByte2, 16, arrayOfByte, 0, 8);
    this._.init(paramInt == 0 ? 0 : 1, paramArrayOfByte1, arrayOfByte);
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    if (this.c == 0)
    {
      this.b.transform(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
      this.a.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
      this._.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
    }
    else
    {
      this._.transform(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
      this.a.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
      this.b.transform(paramArrayOfByte2, paramInt2, paramArrayOfByte2, paramInt2, paramInt3);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Ssh1Des3
 * JD-Core Version:    0.6.0
 */