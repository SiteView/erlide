package com.sshtools.net;

import com.maverick.ssh.SshTransport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocksProxyTransport extends Socket
  implements SshTransport
{
  public static final int SOCKS4 = 4;
  public static final int SOCKS5 = 5;
  private static final String[] oc = { "Success", "General SOCKS server failure", "Connection not allowed by ruleset", "Network unreachable", "Host unreachable", "Connection refused", "TTL expired", "Command not supported", "Address type not supported" };
  private static final String[] ic = { "Request rejected or failed", "SOCKS server cannot connect to identd on the client", "The client program and identd report different user-ids" };
  private String qc;
  private int sc;
  private String kc;
  private int nc;
  private String mc;
  private int jc;
  private String lc;
  private String rc;
  private boolean pc;

  private SocksProxyTransport(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3)
    throws IOException, UnknownHostException
  {
    super(paramString2, paramInt2);
    this.qc = paramString2;
    this.sc = paramInt2;
    this.kc = paramString1;
    this.nc = paramInt1;
    this.jc = paramInt3;
  }

  public static SocksProxyTransport connectViaSocks4Proxy(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3)
    throws IOException, UnknownHostException
  {
    SocksProxyTransport localSocksProxyTransport = new SocksProxyTransport(paramString1, paramInt1, paramString2, paramInt2, 4);
    localSocksProxyTransport.lc = paramString3;
    try
    {
      InputStream localInputStream = localSocksProxyTransport.getInputStream();
      OutputStream localOutputStream = localSocksProxyTransport.getOutputStream();
      InetAddress localInetAddress = InetAddress.getByName(paramString1);
      localOutputStream.write(4);
      localOutputStream.write(1);
      localOutputStream.write(paramInt1 >>> 8 & 0xFF);
      localOutputStream.write(paramInt1 & 0xFF);
      localOutputStream.write(localInetAddress.getAddress());
      localOutputStream.write(paramString3.getBytes());
      localOutputStream.write(0);
      localOutputStream.flush();
      int i = localInputStream.read();
      if (i == -1)
        throw new IOException("SOCKS4 server " + paramString2 + ":" + paramInt2 + " disconnected");
      if (i != 0)
        throw new IOException("Invalid response from SOCKS4 server (" + i + ") " + paramString2 + ":" + paramInt2);
      int j = localInputStream.read();
      if (j != 90)
      {
        if ((j > 90) && (j < 93))
          throw new IOException("SOCKS4 server unable to connect, reason: " + ic[(j - 91)]);
        throw new IOException("SOCKS4 server unable to connect, reason: " + j);
      }
      byte[] arrayOfByte = new byte[6];
      if (localInputStream.read(arrayOfByte, 0, 6) != 6)
        throw new IOException("SOCKS4 error reading destination address/port");
      localSocksProxyTransport.mc = (arrayOfByte[2] + "." + arrayOfByte[3] + "." + arrayOfByte[4] + "." + arrayOfByte[5] + ":" + (arrayOfByte[0] << 8 | arrayOfByte[1]));
    }
    catch (SocketException localSocketException)
    {
      throw new SocketException("Error communicating with SOCKS4 server " + paramString2 + ":" + paramInt2 + ", " + localSocketException.getMessage());
    }
    return localSocksProxyTransport;
  }

  public static SocksProxyTransport connectViaSocks5Proxy(String paramString1, int paramInt1, String paramString2, int paramInt2, boolean paramBoolean, String paramString3, String paramString4)
    throws IOException, UnknownHostException
  {
    SocksProxyTransport localSocksProxyTransport = new SocksProxyTransport(paramString1, paramInt1, paramString2, paramInt2, 5);
    localSocksProxyTransport.lc = paramString3;
    localSocksProxyTransport.rc = paramString4;
    localSocksProxyTransport.pc = paramBoolean;
    try
    {
      InputStream localInputStream = localSocksProxyTransport.getInputStream();
      OutputStream localOutputStream = localSocksProxyTransport.getOutputStream();
      byte[] arrayOfByte1 = { 5, 2, 0, 2 };
      localOutputStream.write(arrayOfByte1);
      localOutputStream.flush();
      int i = localInputStream.read();
      if (i == -1)
        throw new IOException("SOCKS5 server " + paramString2 + ":" + paramInt2 + " disconnected");
      if (i != 5)
        throw new IOException("Invalid response from SOCKS5 server (" + i + ") " + paramString2 + ":" + paramInt2);
      int j = localInputStream.read();
      switch (j)
      {
      case 0:
        break;
      case 2:
        b(localInputStream, localOutputStream, paramString3, paramString4, paramString2, paramInt2);
        break;
      default:
        throw new IOException("SOCKS5 server does not support our authentication methods");
      }
      if (paramBoolean)
      {
        InetAddress localInetAddress;
        try
        {
          localInetAddress = InetAddress.getByName(paramString1);
        }
        catch (UnknownHostException localUnknownHostException)
        {
          throw new IOException("Can't do local lookup on: " + paramString1 + ", try socks5 without local lookup");
        }
        arrayOfByte1 = new byte[] { 5, 1, 0, 1 };
        localOutputStream.write(arrayOfByte1);
        localOutputStream.write(localInetAddress.getAddress());
      }
      else
      {
        arrayOfByte1 = new byte[] { 5, 1, 0, 3 };
        localOutputStream.write(arrayOfByte1);
        localOutputStream.write(paramString1.length());
        localOutputStream.write(paramString1.getBytes());
      }
      localOutputStream.write(paramInt1 >>> 8 & 0xFF);
      localOutputStream.write(paramInt1 & 0xFF);
      localOutputStream.flush();
      i = localInputStream.read();
      if (i != 5)
        throw new IOException("Invalid response from SOCKS5 server (" + i + ") " + paramString2 + ":" + paramInt2);
      int k = localInputStream.read();
      if (k != 0)
      {
        if ((k > 0) && (k < 9))
          throw new IOException("SOCKS5 server unable to connect, reason: " + oc[k]);
        throw new IOException("SOCKS5 server unable to connect, reason: " + k);
      }
      localInputStream.read();
      int m = localInputStream.read();
      byte[] arrayOfByte2 = new byte['ÿ'];
      switch (m)
      {
      case 1:
        if (localInputStream.read(arrayOfByte2, 0, 4) != 4)
          throw new IOException("SOCKS5 error reading address");
        localSocksProxyTransport.mc = (arrayOfByte2[0] + "." + arrayOfByte2[1] + "." + arrayOfByte2[2] + "." + arrayOfByte2[3]);
        break;
      case 3:
        int n = localInputStream.read();
        if (localInputStream.read(arrayOfByte2, 0, n) != n)
          throw new IOException("SOCKS5 error reading address");
        localSocksProxyTransport.mc = new String(arrayOfByte2);
        break;
      default:
        throw new IOException("SOCKS5 gave unsupported address type: " + m);
      }
      if (localInputStream.read(arrayOfByte2, 0, 2) != 2)
        throw new IOException("SOCKS5 error reading port");
      SocksProxyTransport tmp801_799 = localSocksProxyTransport;
      tmp801_799.mc = (tmp801_799.mc + ":" + (arrayOfByte2[0] << 8 | arrayOfByte2[1]));
    }
    catch (SocketException localSocketException)
    {
      throw new SocketException("Error communicating with SOCKS5 server " + paramString2 + ":" + paramInt2 + ", " + localSocketException.getMessage());
    }
    return localSocksProxyTransport;
  }

  private static void b(InputStream paramInputStream, OutputStream paramOutputStream, String paramString1, String paramString2, String paramString3, int paramInt)
    throws IOException
  {
    paramOutputStream.write(1);
    paramOutputStream.write(paramString1.length());
    paramOutputStream.write(paramString1.getBytes());
    paramOutputStream.write(paramString2.length());
    paramOutputStream.write(paramString2.getBytes());
    int i = paramInputStream.read();
    if ((i != 1) && (i != 5))
      throw new IOException("Invalid response from SOCKS5 server (" + i + ") " + paramString3 + ":" + paramInt);
    if (paramInputStream.read() != 0)
      throw new IOException("Invalid username/password for SOCKS5 server");
  }

  public String toString()
  {
    return "SocksProxySocket[addr=" + getInetAddress() + ",port=" + getPort() + ",localport=" + getLocalPort() + "]";
  }

  public static SocksProxyTransport connectViaSocks5Proxy(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, String paramString4)
    throws IOException, UnknownHostException
  {
    return connectViaSocks5Proxy(paramString1, paramInt1, paramString2, paramInt2, false, paramString3, paramString4);
  }

  public String getHost()
  {
    return this.kc;
  }

  public SshTransport duplicate()
    throws IOException
  {
    switch (this.jc)
    {
    case 4:
      return connectViaSocks4Proxy(this.kc, this.nc, this.qc, this.sc, this.lc);
    }
    return connectViaSocks5Proxy(this.kc, this.nc, this.qc, this.sc, this.pc, this.lc, this.rc);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.SocksProxyTransport
 * JD-Core Version:    0.6.0
 */