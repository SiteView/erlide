package socks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class ProxyMessage
{
  public InetAddress ip = null;
  public int version;
  public int port;
  public int command;
  public String host = null;
  public String user = null;

  ProxyMessage(int paramInt1, InetAddress paramInetAddress, int paramInt2)
  {
    this.command = paramInt1;
    this.ip = paramInetAddress;
    this.port = paramInt2;
  }

  ProxyMessage()
  {
  }

  public abstract void read(InputStream paramInputStream)
    throws SocksException, IOException;

  public abstract void read(InputStream paramInputStream, boolean paramBoolean)
    throws SocksException, IOException;

  public abstract void write(OutputStream paramOutputStream)
    throws SocksException, IOException;

  public InetAddress getInetAddress()
    throws UnknownHostException
  {
    return this.ip;
  }

  public String toString()
  {
    return "Proxy Message:\nVersion:" + this.version + "\n" + "Command:" + this.command + "\n" + "IP:     " + this.ip + "\n" + "Port:   " + this.port + "\n" + "User:   " + this.user + "\n";
  }

  static final String c(byte[] paramArrayOfByte, int paramInt)
  {
    String str = "" + (paramArrayOfByte[paramInt] & 0xFF);
    for (int i = paramInt + 1; i < paramInt + 4; i++)
      str = str + "." + (paramArrayOfByte[i] & 0xFF);
    return str;
  }

  static final String b(byte[] paramArrayOfByte, int paramInt)
  {
    return null;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.ProxyMessage
 * JD-Core Version:    0.6.0
 */