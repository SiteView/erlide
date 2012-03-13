package com.maverick.ssh.components.standalone;

import com.maverick.crypto.engines.DESEngine;
import com.maverick.ssh.components.SshCipher;
import java.io.IOException;

public class Ssh1Des extends SshCipher
{
  byte[] J;
  byte[] I;
  byte[] M;
  int L;
  DESEngine K = new DESEngine();

  public Ssh1Des()
  {
    super("DES");
  }

  public int getBlockSize()
  {
    return 8;
  }

  public String getAlgorithm()
  {
    return "des";
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.J = new byte[8];
    this.I = new byte[8];
    this.M = new byte[8];
    this.L = paramInt;
    this.K.init(paramInt == 0, paramArrayOfByte2);
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    if (this.L == 0)
      encrypt(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
    else
      decrypt(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2, paramInt3);
  }

  public synchronized void encrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    int i = paramInt1 + paramInt3;
    int k = paramInt1;
    for (int m = paramInt2; k < i; m += 8)
    {
      for (int j = 0; j < 8; j++)
      {
        int tmp36_34 = j;
        byte[] tmp36_31 = this.J;
        tmp36_31[tmp36_34] = (byte)(tmp36_31[tmp36_34] ^ paramArrayOfByte1[(k + j)]);
      }
      this.K.processBlock(this.J, 0, this.J, 0);
      for (j = 0; j < 8; j++)
        paramArrayOfByte2[(m + j)] = this.J[j];
      k += 8;
    }
  }

  public synchronized void decrypt(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    int i = paramInt1 + paramInt3;
    int k = paramInt1;
    for (int m = paramInt2; k < i; m += 8)
    {
      for (int j = 0; j < 8; j++)
        this.I[j] = paramArrayOfByte1[(k + j)];
      this.K.processBlock(this.I, 0, this.M, 0);
      for (j = 0; j < 8; j++)
      {
        paramArrayOfByte2[(m + j)] = (byte)((this.J[j] ^ this.M[j]) & 0xFF);
        this.J[j] = this.I[j];
      }
      k += 8;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Ssh1Des
 * JD-Core Version:    0.6.0
 */