package socks;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

class b extends ProxyMessage
{
  private byte[] d;
  private int c;
  static final String[] b = { "Request Granted", "Request Rejected or Failed", "Failed request, can't connect to Identd", "Failed request, bad user name" };

  public b(int paramInt)
  {
    super(paramInt, null, 0);
    this.user = null;
    this.c = 2;
    this.d = new byte[2];
    this.d[0] = 0;
    this.d[1] = (byte)this.command;
  }

  public b(int paramInt1, InetAddress paramInetAddress, int paramInt2)
  {
    this(0, paramInt1, paramInetAddress, paramInt2, null);
  }

  public b(int paramInt1, InetAddress paramInetAddress, int paramInt2, String paramString)
  {
    this(4, paramInt1, paramInetAddress, paramInt2, paramString);
  }

  public b(int paramInt1, int paramInt2, InetAddress paramInetAddress, int paramInt3, String paramString)
  {
    super(paramInt2, paramInetAddress, paramInt3);
    this.user = paramString;
    this.version = paramInt1;
    this.c = (paramString == null ? 8 : 9 + paramString.length());
    this.d = new byte[this.c];
    this.d[0] = (byte)paramInt1;
    this.d[1] = (byte)this.command;
    this.d[2] = (byte)(paramInt3 >> 8);
    this.d[3] = (byte)paramInt3;
    byte[] arrayOfByte1;
    if (paramInetAddress != null)
    {
      arrayOfByte1 = paramInetAddress.getAddress();
    }
    else
    {
      arrayOfByte1 = new byte[4];
      int tmp126_125 = (arrayOfByte1[2] = arrayOfByte1[3] = 0);
      arrayOfByte1[1] = tmp126_125;
      arrayOfByte1[0] = tmp126_125;
    }
    System.arraycopy(arrayOfByte1, 0, this.d, 4, 4);
    if (paramString != null)
    {
      byte[] arrayOfByte2 = paramString.getBytes();
      System.arraycopy(arrayOfByte2, 0, this.d, 8, arrayOfByte2.length);
      this.d[(this.d.length - 1)] = 0;
    }
  }

  public b(InputStream paramInputStream, boolean paramBoolean)
    throws IOException
  {
    this.d = null;
    read(paramInputStream, paramBoolean);
  }

  public void read(InputStream paramInputStream)
    throws IOException
  {
    read(paramInputStream, true);
  }

  public void read(InputStream paramInputStream, boolean paramBoolean)
    throws IOException
  {
    DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
    this.version = localDataInputStream.readUnsignedByte();
    this.command = localDataInputStream.readUnsignedByte();
    if ((paramBoolean) && (this.command != 90))
    {
      if ((this.command > 90) && (this.command < 93))
        localObject = b[(this.command - 90)];
      else
        localObject = "Unknown Reply Code";
      throw new SocksException(this.command, (String)localObject);
    }
    this.port = localDataInputStream.readUnsignedShort();
    Object localObject = new byte[4];
    localDataInputStream.readFully(localObject);
    this.ip = b(localObject);
    this.host = this.ip.getHostName();
    if (!paramBoolean)
    {
      int i = paramInputStream.read();
      byte[] arrayOfByte = new byte[256];
      int j = 0;
      for (j = 0; (j < arrayOfByte.length) && (i > 0); j++)
      {
        arrayOfByte[j] = (byte)i;
        i = paramInputStream.read();
      }
      this.user = new String(arrayOfByte, 0, j);
    }
  }

  public void write(OutputStream paramOutputStream)
    throws IOException
  {
    if (this.d == null)
    {
      b localb = new b(this.version, this.command, this.ip, this.port, this.user);
      this.d = localb.d;
      this.c = localb.c;
    }
    paramOutputStream.write(this.d);
  }

  static InetAddress b(byte[] paramArrayOfByte)
  {
    String str = c(paramArrayOfByte, 0);
    try
    {
      return InetAddress.getByName(str);
    }
    catch (UnknownHostException localUnknownHostException)
    {
    }
    return null;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.b
 * JD-Core Version:    0.6.0
 */