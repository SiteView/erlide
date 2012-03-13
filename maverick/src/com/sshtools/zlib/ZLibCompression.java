package com.sshtools.zlib;

import com.maverick.ssh.compression.SshCompression;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZLibCompression
  implements SshCompression
{
  ByteArrayOutputStream b = new ByteArrayOutputStream(65535);
  ByteArrayOutputStream f = new ByteArrayOutputStream(65535);
  private ZStream d = new ZStream();
  private byte[] e = new byte[65535];
  private byte[] c = new byte[65535];

  public String getAlgorithm()
  {
    return "zlib";
  }

  public void init(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 1)
      this.d.deflateInit(paramInt2);
    else if (paramInt1 == 0)
      this.d.inflateInit();
  }

  public byte[] compress(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.b.reset();
    this.d.next_in = paramArrayOfByte;
    this.d.next_in_index = paramInt1;
    this.d.avail_in = (paramInt2 - paramInt1);
    do
    {
      this.d.next_out = this.c;
      this.d.next_out_index = 0;
      this.d.avail_out = 65535;
      int i = this.d.deflate(1);
      switch (i)
      {
      case 0:
        this.b.write(this.c, 0, 65535 - this.d.avail_out);
        break;
      default:
        throw new IOException("compress: deflate returnd " + i);
      }
    }
    while (this.d.avail_out == 0);
    return this.b.toByteArray();
  }

  public byte[] uncompress(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.f.reset();
    this.d.next_in = paramArrayOfByte;
    this.d.next_in_index = paramInt1;
    this.d.avail_in = paramInt2;
    while (true)
    {
      this.d.next_out = this.e;
      this.d.next_out_index = 0;
      this.d.avail_out = 65535;
      int i = this.d.inflate(1);
      switch (i)
      {
      case 0:
        this.f.write(this.e, 0, 65535 - this.d.avail_out);
        break;
      case -5:
        return this.f.toByteArray();
      default:
        throw new IOException("uncompress: inflate returnd " + i);
      }
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.zlib.ZLibCompression
 * JD-Core Version:    0.6.0
 */