package socks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Socks5DatagramSocket extends DatagramSocket
{
  InetAddress e;
  int c;
  Socks5Proxy b;
  private boolean d = false;
  UDPEncapsulation f;

  public Socks5DatagramSocket()
    throws SocksException, IOException
  {
    this(Proxy.defaultProxy, 0, null);
  }

  public Socks5DatagramSocket(int paramInt)
    throws SocksException, IOException
  {
    this(Proxy.defaultProxy, paramInt, null);
  }

  public Socks5DatagramSocket(int paramInt, InetAddress paramInetAddress)
    throws SocksException, IOException
  {
    this(Proxy.defaultProxy, paramInt, paramInetAddress);
  }

  public Socks5DatagramSocket(Proxy paramProxy, int paramInt, InetAddress paramInetAddress)
    throws SocksException, IOException
  {
    super(paramInt, paramInetAddress);
    if (paramProxy == null)
      throw new SocksException(65536);
    if (!(paramProxy instanceof Socks5Proxy))
      throw new SocksException(-1, "Datagram Socket needs Proxy version 5");
    if (paramProxy.chainProxy != null)
      throw new SocksException(393216, "Datagram Sockets do not support proxy chaining.");
    this.b = ((Socks5Proxy)paramProxy.copy());
    ProxyMessage localProxyMessage = this.b.udpAssociate(super.getLocalAddress(), super.getLocalPort());
    this.e = localProxyMessage.ip;
    if (this.e.getHostAddress().equals("0.0.0.0"))
      this.e = this.b.proxyIP;
    this.c = localProxyMessage.port;
    this.f = this.b.d;
  }

  Socks5DatagramSocket(boolean paramBoolean, UDPEncapsulation paramUDPEncapsulation, InetAddress paramInetAddress, int paramInt)
    throws IOException
  {
    this.d = paramBoolean;
    this.e = paramInetAddress;
    this.c = paramInt;
    this.f = paramUDPEncapsulation;
    this.b = null;
  }

  public void send(DatagramPacket paramDatagramPacket)
    throws IOException
  {
    if ((!this.d) && (this.b.isDirect(paramDatagramPacket.getAddress())))
    {
      super.send(paramDatagramPacket);
      return;
    }
    byte[] arrayOfByte1 = b(paramDatagramPacket.getAddress(), paramDatagramPacket.getPort());
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + paramDatagramPacket.getLength()];
    byte[] arrayOfByte3 = paramDatagramPacket.getData();
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    System.arraycopy(arrayOfByte3, 0, arrayOfByte2, arrayOfByte1.length, paramDatagramPacket.getLength());
    if (this.f != null)
      arrayOfByte2 = this.f.udpEncapsulate(arrayOfByte2, true);
    super.send(new DatagramPacket(arrayOfByte2, arrayOfByte2.length, this.e, this.c));
  }

  public void send(DatagramPacket paramDatagramPacket, String paramString)
    throws IOException
  {
    if (this.b.isDirect(paramString))
    {
      paramDatagramPacket.setAddress(InetAddress.getByName(paramString));
      super.send(paramDatagramPacket);
      return;
    }
    if (this.b.c)
      paramDatagramPacket.setAddress(InetAddress.getByName(paramString));
    byte[] arrayOfByte1 = b(paramString, paramDatagramPacket.getPort());
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + paramDatagramPacket.getLength()];
    byte[] arrayOfByte3 = paramDatagramPacket.getData();
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    System.arraycopy(arrayOfByte3, 0, arrayOfByte2, arrayOfByte1.length, paramDatagramPacket.getLength());
    if (this.f != null)
      arrayOfByte2 = this.f.udpEncapsulate(arrayOfByte2, true);
    super.send(new DatagramPacket(arrayOfByte2, arrayOfByte2.length, this.e, this.c));
  }

  public void receive(DatagramPacket paramDatagramPacket)
    throws IOException
  {
    super.receive(paramDatagramPacket);
    if (this.d)
    {
      int i = paramDatagramPacket.getLength();
      j = getSoTimeout();
      long l = System.currentTimeMillis();
      while ((!this.e.equals(paramDatagramPacket.getAddress())) || (this.c != paramDatagramPacket.getPort()))
      {
        paramDatagramPacket.setLength(i);
        if (j != 0)
        {
          k = j - (int)(System.currentTimeMillis() - l);
          if (k <= 0)
            throw new InterruptedIOException("In Socks5DatagramSocket->receive()");
          setSoTimeout(k);
        }
        super.receive(paramDatagramPacket);
      }
      if (j != 0)
        setSoTimeout(j);
    }
    else if ((!this.e.equals(paramDatagramPacket.getAddress())) || (this.c != paramDatagramPacket.getPort()))
    {
      return;
    }
    byte[] arrayOfByte = paramDatagramPacket.getData();
    if (this.f != null)
      arrayOfByte = this.f.udpEncapsulate(arrayOfByte, false);
    int j = 0;
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte, j, paramDatagramPacket.getLength());
    e locale = new e(localByteArrayInputStream);
    paramDatagramPacket.setPort(locale.port);
    paramDatagramPacket.setAddress(locale.getInetAddress());
    int k = localByteArrayInputStream.available();
    System.arraycopy(arrayOfByte, j + paramDatagramPacket.getLength() - k, arrayOfByte, j, k);
    paramDatagramPacket.setLength(k);
  }

  public int getLocalPort()
  {
    if (this.d)
      return super.getLocalPort();
    return this.c;
  }

  public InetAddress getLocalAddress()
  {
    if (this.d)
      return super.getLocalAddress();
    return this.e;
  }

  public void close()
  {
    if (!this.d)
      this.b.endSession();
    super.close();
  }

  public boolean isProxyAlive(int paramInt)
  {
    if (this.d)
      return false;
    if (this.b != null)
      try
      {
        this.b.proxySocket.setSoTimeout(paramInt);
        int i = this.b.in.read();
        return i >= 0;
      }
      catch (InterruptedIOException localInterruptedIOException)
      {
        return true;
      }
      catch (IOException localIOException)
      {
        return false;
      }
    return false;
  }

  private byte[] b(InetAddress paramInetAddress, int paramInt)
  {
    e locale = new e(0, paramInetAddress, paramInt);
    locale.f[0] = 0;
    return locale.f;
  }

  private byte[] b(String paramString, int paramInt)
  {
    e locale = new e(0, paramString, paramInt);
    locale.f[0] = 0;
    return locale.f;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.Socks5DatagramSocket
 * JD-Core Version:    0.6.0
 */