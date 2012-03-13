package com.maverick.util;

import java.io.IOException;
import java.io.OutputStream;

class A extends OutputStream
{
  EOLProcessor A;

  public A(int paramInt1, int paramInt2, OutputStream paramOutputStream)
    throws IOException
  {
    this.A = new EOLProcessor(paramInt1, paramInt2, paramOutputStream);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.A.processBytes(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void write(int paramInt)
    throws IOException
  {
    this.A.processBytes(new byte[] { (byte)paramInt }, 0, 1);
  }

  public void close()
    throws IOException
  {
    this.A.close();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.A
 * JD-Core Version:    0.6.0
 */