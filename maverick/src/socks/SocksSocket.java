package socks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocksSocket extends Socket
{
  protected Proxy proxy;
  protected String localHost;
  protected String remoteHost;
  protected InetAddress localIP;
  protected InetAddress remoteIP;
  protected int localPort;
  protected int remotePort;
  private Socket b = null;

  public SocksSocket(String paramString, int paramInt)
    throws SocksException, UnknownHostException
  {
    this(Proxy.defaultProxy, paramString, paramInt);
  }

  public SocksSocket(Proxy paramProxy, String paramString, int paramInt)
    throws SocksException, UnknownHostException
  {
    if (paramProxy == null)
      throw new SocksException(65536);
    this.proxy = paramProxy.copy();
    this.remoteHost = paramString;
    this.remotePort = paramInt;
    if (this.proxy.isDirect(paramString))
    {
      this.remoteIP = InetAddress.getByName(paramString);
      b();
    }
    else
    {
      b(this.proxy.connect(paramString, paramInt));
    }
  }

  public SocksSocket(InetAddress paramInetAddress, int paramInt)
    throws SocksException
  {
    this(Proxy.defaultProxy, paramInetAddress, paramInt);
  }

  public SocksSocket(Proxy paramProxy, InetAddress paramInetAddress, int paramInt)
    throws SocksException
  {
    if (paramProxy == null)
      throw new SocksException(65536);
    this.proxy = paramProxy.copy();
    this.remoteIP = paramInetAddress;
    this.remotePort = paramInt;
    this.remoteHost = paramInetAddress.getHostName();
    if (this.proxy.isDirect(this.remoteIP))
      b();
    else
      b(this.proxy.connect(paramInetAddress, paramInt));
  }

  protected SocksSocket(String paramString, int paramInt, Proxy paramProxy)
  {
    this.remotePort = paramInt;
    this.proxy = paramProxy;
    this.localIP = paramProxy.proxySocket.getLocalAddress();
    this.localPort = paramProxy.proxySocket.getLocalPort();
    this.remoteHost = paramString;
  }

  protected SocksSocket(InetAddress paramInetAddress, int paramInt, Proxy paramProxy)
  {
    this.remoteIP = paramInetAddress;
    this.remotePort = paramInt;
    this.proxy = paramProxy;
    this.localIP = paramProxy.proxySocket.getLocalAddress();
    this.localPort = paramProxy.proxySocket.getLocalPort();
    this.remoteHost = this.remoteIP.getHostName();
  }

  public void close()
    throws IOException
  {
    if (this.proxy != null)
      this.proxy.endSession();
    this.proxy = null;
  }

  public InputStream getInputStream()
  {
    return this.proxy.in;
  }

  public OutputStream getOutputStream()
  {
    return this.proxy.out;
  }

  public int getPort()
  {
    return this.remotePort;
  }

  public String getHost()
  {
    return this.remoteHost;
  }

  public InetAddress getInetAddress()
  {
    if (this.remoteIP == null)
      try
      {
        this.remoteIP = InetAddress.getByName(this.remoteHost);
      }
      catch (UnknownHostException localUnknownHostException)
      {
        return null;
      }
    return this.remoteIP;
  }

  public int getLocalPort()
  {
    return this.localPort;
  }

  public InetAddress getLocalAddress()
  {
    if (this.localIP == null)
      try
      {
        this.localIP = InetAddress.getByName(this.localHost);
      }
      catch (UnknownHostException localUnknownHostException)
      {
        return null;
      }
    return this.localIP;
  }

  public String getLocalHost()
  {
    return this.localHost;
  }

  public void setSoLinger(boolean paramBoolean, int paramInt)
    throws SocketException
  {
    this.proxy.proxySocket.setSoLinger(paramBoolean, paramInt);
  }

  public int getSoLinger(int paramInt)
    throws SocketException
  {
    return this.proxy.proxySocket.getSoLinger();
  }

  public void setSoTimeout(int paramInt)
    throws SocketException
  {
    this.proxy.proxySocket.setSoTimeout(paramInt);
  }

  public int getSoTimeout(int paramInt)
    throws SocketException
  {
    return this.proxy.proxySocket.getSoTimeout();
  }

  public void setTcpNoDelay(boolean paramBoolean)
    throws SocketException
  {
    this.proxy.proxySocket.setTcpNoDelay(paramBoolean);
  }

  public boolean getTcpNoDelay()
    throws SocketException
  {
    return this.proxy.proxySocket.getTcpNoDelay();
  }

  public String toString()
  {
    if (this.b != null)
      return "Direct connection:" + this.b;
    return "Proxy:" + this.proxy + ";" + "addr:" + this.remoteHost + ",port:" + this.remotePort + ",localport:" + this.localPort;
  }

  private void b(ProxyMessage paramProxyMessage)
    throws SocksException
  {
    this.localPort = paramProxyMessage.port;
    if (paramProxyMessage.host.equals("0.0.0.0"))
    {
      this.localIP = this.proxy.proxyIP;
      this.localHost = this.localIP.getHostName();
    }
    else
    {
      this.localHost = paramProxyMessage.host;
      this.localIP = paramProxyMessage.ip;
    }
  }

  private void b()
    throws SocksException
  {
    try
    {
      this.b = new Socket(this.remoteIP, this.remotePort);
      this.proxy.out = this.b.getOutputStream();
      this.proxy.in = this.b.getInputStream();
      this.proxy.proxySocket = this.b;
      this.localIP = this.b.getLocalAddress();
      this.localPort = this.b.getLocalPort();
    }
    catch (IOException localIOException)
    {
      throw new SocksException(458752, "Direct connect failed:" + localIOException);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.SocksSocket
 * JD-Core Version:    0.6.0
 */