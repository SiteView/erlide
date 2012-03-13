package com.maverick.ssh;

import com.maverick.util.ByteArrayWriter;
import java.io.IOException;

public class Packet extends ByteArrayWriter
{
  int A = -1;

  public Packet()
    throws IOException
  {
    this(35000);
  }

  public Packet(int paramInt)
    throws IOException
  {
    super(paramInt + 4);
    writeInt(0);
  }

  public int setPosition(int paramInt)
  {
    int i = this.count;
    this.count = paramInt;
    return i;
  }

  public int position()
  {
    return this.count;
  }

  public void finish()
  {
    this.buf[0] = (byte)(this.count - 4 >> 24);
    this.buf[1] = (byte)(this.count - 4 >> 16);
    this.buf[2] = (byte)(this.count - 4 >> 8);
    this.buf[3] = (byte)(this.count - 4);
  }

  public void reset()
  {
    super.reset();
    try
    {
      writeInt(0);
    }
    catch (IOException localIOException)
    {
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.Packet
 * JD-Core Version:    0.6.0
 */