package com.maverick.util;

import java.io.IOException;
import java.io.InputStream;

class c extends InputStream
{
  EOLProcessor e;
  InputStream b;
  DynamicBuffer c = new DynamicBuffer();
  byte[] d = new byte[32768];

  public c(int paramInt1, int paramInt2, InputStream paramInputStream)
    throws IOException
  {
    this.b = paramInputStream;
    this.e = new EOLProcessor(paramInt1, paramInt2, this.c.getOutputStream());
  }

  public int read()
    throws IOException
  {
    b(1);
    return this.c.getInputStream().read();
  }

  public int available()
    throws IOException
  {
    return this.b.available();
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    b(paramInt2);
    return this.c.getInputStream().read(paramArrayOfByte, paramInt1, paramInt2);
  }

  private void b(int paramInt)
    throws IOException
  {
    while (this.c.available() < paramInt)
    {
      int i = this.b.read(this.d);
      if (i == -1)
      {
        this.c.close();
        return;
      }
      this.e.processBytes(this.d, 0, i);
    }
  }

  public void close()
    throws IOException
  {
    this.b.close();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.c
 * JD-Core Version:    0.6.0
 */