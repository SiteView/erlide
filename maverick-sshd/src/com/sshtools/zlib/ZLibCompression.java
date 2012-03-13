package com.sshtools.zlib;

import com.maverick.ssh.compression.SshCompression;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZLibCompression
  implements SshCompression
{
  ByteArrayOutputStream A = new ByteArrayOutputStream(65535);
  ByteArrayOutputStream E = new ByteArrayOutputStream(65535);
  private ZStream C = new ZStream();
  private byte[] D = new byte[65535];
  private byte[] B = new byte[65535];

  public String getAlgorithm()
  {
    return "zlib";
  }

  public void init(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 1)
      this.C.deflateInit(paramInt2);
    else if (paramInt1 == 0)
      this.C.inflateInit();
  }

  public byte[] compress(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.A.reset();
    this.C.next_in = paramArrayOfByte;
    this.C.next_in_index = paramInt1;
    this.C.avail_in = (paramInt2 - paramInt1);
    do
    {
      this.C.next_out = this.B;
      this.C.next_out_index = 0;
      this.C.avail_out = 65535;
      int i = this.C.deflate(1);
      switch (i)
      {
      case 0:
        this.A.write(this.B, 0, 65535 - this.C.avail_out);
        break;
      default:
        throw new IOException("compress: deflate returnd " + i);
      }
    }
    while (this.C.avail_out == 0);
    return this.A.toByteArray();
  }

  public byte[] uncompress(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.E.reset();
    this.C.next_in = paramArrayOfByte;
    this.C.next_in_index = paramInt1;
    this.C.avail_in = paramInt2;
    while (true)
    {
      this.C.next_out = this.D;
      this.C.next_out_index = 0;
      this.C.avail_out = 65535;
      int i = this.C.inflate(1);
      switch (i)
      {
      case 0:
        this.E.write(this.D, 0, 65535 - this.C.avail_out);
        break;
      case -5:
        return this.E.toByteArray();
      default:
        throw new IOException("uncompress: inflate returnd " + i);
      }
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.zlib.ZLibCompression
 * JD-Core Version:    0.6.0
 */