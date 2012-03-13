package com.maverick.util;

import java.io.IOException;
import java.io.OutputStream;

class b extends OutputStream
{
  EOLProcessor b;

  public b(int paramInt1, int paramInt2, OutputStream paramOutputStream)
    throws IOException
  {
    this.b = new EOLProcessor(paramInt1, paramInt2, paramOutputStream);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.b.processBytes(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void write(int paramInt)
    throws IOException
  {
    this.b.processBytes(new byte[] { (byte)paramInt }, 0, 1);
  }

  public void close()
    throws IOException
  {
    this.b.close();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.b
 * JD-Core Version:    0.6.0
 */