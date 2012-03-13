package com.maverick.util;

import java.io.IOException;
import java.io.InputStream;

class B extends InputStream
{
  EOLProcessor D;
  InputStream A;
  DynamicBuffer B = new DynamicBuffer();
  byte[] C = new byte[32768];

  public B(int paramInt1, int paramInt2, InputStream paramInputStream)
    throws IOException
  {
    this.A = paramInputStream;
    this.D = new EOLProcessor(paramInt1, paramInt2, this.B.getOutputStream());
  }

  public int read()
    throws IOException
  {
    A(1);
    return this.B.getInputStream().read();
  }

  public int available()
    throws IOException
  {
    return this.A.available();
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    A(paramInt2);
    return this.B.getInputStream().read(paramArrayOfByte, paramInt1, paramInt2);
  }

  private void A(int paramInt)
    throws IOException
  {
    while (this.B.available() < paramInt)
    {
      int i = this.A.read(this.C);
      if (i == -1)
      {
        this.B.close();
        return;
      }
      this.D.processBytes(this.C, 0, i);
    }
  }

  public void close()
    throws IOException
  {
    this.A.close();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.B
 * JD-Core Version:    0.6.0
 */