package socks;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

class e extends ProxyMessage
{
  public int g;
  byte[] f;
  static boolean e = true;

  public e(int paramInt)
  {
    super(paramInt, null, 0);
    this.f = new byte[3];
    this.f[0] = 5;
    this.f[1] = (byte)paramInt;
    this.f[2] = 0;
  }

  public e(int paramInt1, InetAddress paramInetAddress, int paramInt2)
  {
    super(paramInt1, paramInetAddress, paramInt2);
    this.host = (paramInetAddress == null ? "0.0.0.0" : paramInetAddress.getHostName());
    this.version = 5;
    byte[] arrayOfByte;
    if (paramInetAddress == null)
    {
      arrayOfByte = new byte[4];
      int tmp55_54 = (arrayOfByte[2] = arrayOfByte[3] = 0);
      arrayOfByte[1] = tmp55_54;
      arrayOfByte[0] = tmp55_54;
    }
    else
    {
      arrayOfByte = paramInetAddress.getAddress();
    }
    this.g = (arrayOfByte.length == 4 ? 1 : 4);
    this.f = new byte[6 + arrayOfByte.length];
    this.f[0] = 5;
    this.f[1] = (byte)this.command;
    this.f[2] = 0;
    this.f[3] = (byte)this.g;
    System.arraycopy(arrayOfByte, 0, this.f, 4, arrayOfByte.length);
    this.f[(this.f.length - 2)] = (byte)(paramInt2 >> 8);
    this.f[(this.f.length - 1)] = (byte)paramInt2;
  }

  public e(int paramInt1, String paramString, int paramInt2)
  {
    super(paramInt1, null, paramInt2);
    this.host = paramString;
    this.version = 5;
    this.g = 3;
    byte[] arrayOfByte = paramString.getBytes();
    this.f = new byte[7 + arrayOfByte.length];
    this.f[0] = 5;
    this.f[1] = (byte)this.command;
    this.f[2] = 0;
    this.f[3] = 3;
    this.f[4] = (byte)arrayOfByte.length;
    System.arraycopy(arrayOfByte, 0, this.f, 5, arrayOfByte.length);
    this.f[(this.f.length - 2)] = (byte)(paramInt2 >> 8);
    this.f[(this.f.length - 1)] = (byte)paramInt2;
  }

  public e(InputStream paramInputStream)
    throws SocksException, IOException
  {
    this(paramInputStream, true);
  }

  public e(InputStream paramInputStream, boolean paramBoolean)
    throws SocksException, IOException
  {
    read(paramInputStream, paramBoolean);
  }

  public void read(InputStream paramInputStream)
    throws SocksException, IOException
  {
    read(paramInputStream, true);
  }

  public void read(InputStream paramInputStream, boolean paramBoolean)
    throws SocksException, IOException
  {
    this.f = null;
    this.ip = null;
    DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
    this.version = localDataInputStream.readUnsignedByte();
    this.command = localDataInputStream.readUnsignedByte();
    if ((paramBoolean) && (this.command != 0))
      throw new SocksException(this.command);
    int i = localDataInputStream.readUnsignedByte();
    this.g = localDataInputStream.readUnsignedByte();
    byte[] arrayOfByte;
    switch (this.g)
    {
    case 1:
      arrayOfByte = new byte[4];
      localDataInputStream.readFully(arrayOfByte);
      this.host = c(arrayOfByte, 0);
      break;
    case 4:
      arrayOfByte = new byte[16];
      localDataInputStream.readFully(arrayOfByte);
      this.host = b(arrayOfByte, 0);
      break;
    case 3:
      arrayOfByte = new byte[localDataInputStream.readUnsignedByte()];
      localDataInputStream.readFully(arrayOfByte);
      this.host = new String(arrayOfByte);
      break;
    case 2:
    default:
      throw new SocksException(393216);
    }
    this.port = localDataInputStream.readUnsignedShort();
    if ((this.g != 3) && (e))
      try
      {
        this.ip = InetAddress.getByName(this.host);
      }
      catch (UnknownHostException localUnknownHostException)
      {
      }
  }

  public void write(OutputStream paramOutputStream)
    throws SocksException, IOException
  {
    if (this.f == null)
    {
      e locale;
      if (this.g == 3)
      {
        locale = new e(this.command, this.host, this.port);
      }
      else
      {
        if (this.ip == null)
          try
          {
            this.ip = InetAddress.getByName(this.host);
          }
          catch (UnknownHostException localUnknownHostException)
          {
            throw new SocksException(393216);
          }
        locale = new e(this.command, this.ip, this.port);
      }
      this.f = locale.f;
    }
    paramOutputStream.write(this.f);
  }

  public InetAddress getInetAddress()
    throws UnknownHostException
  {
    if (this.ip != null)
      return this.ip;
    return this.ip = InetAddress.getByName(this.host);
  }

  public String toString()
  {
    String str = "Socks5Message:\nVN   " + this.version + "\n" + "CMD  " + this.command + "\n" + "ATYP " + this.g + "\n" + "ADDR " + this.host + "\n" + "PORT " + this.port + "\n";
    return str;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.e
 * JD-Core Version:    0.6.0
 */