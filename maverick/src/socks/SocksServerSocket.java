package socks;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocksServerSocket extends ServerSocket
{
  protected Proxy proxy;
  protected String localHost;
  protected InetAddress localIP;
  protected int localPort;
  boolean c = false;
  InetAddress b;

  public SocksServerSocket(String paramString, int paramInt)
    throws SocksException, UnknownHostException, IOException
  {
    this(Proxy.defaultProxy, paramString, paramInt);
  }

  public SocksServerSocket(Proxy paramProxy, String paramString, int paramInt)
    throws SocksException, UnknownHostException, IOException
  {
    super(0);
    if (paramProxy == null)
      throw new SocksException(65536);
    this.proxy = paramProxy.copy();
    if (this.proxy.isDirect(paramString))
    {
      this.b = InetAddress.getByName(paramString);
      this.proxy = null;
      b();
    }
    else
    {
      b(this.proxy.bind(paramString, paramInt));
    }
  }

  public SocksServerSocket(InetAddress paramInetAddress, int paramInt)
    throws SocksException, IOException
  {
    this(Proxy.defaultProxy, paramInetAddress, paramInt);
  }

  public SocksServerSocket(Proxy paramProxy, InetAddress paramInetAddress, int paramInt)
    throws SocksException, IOException
  {
    super(0);
    if (paramProxy == null)
      throw new SocksException(65536);
    this.proxy = paramProxy.copy();
    if (this.proxy.isDirect(paramInetAddress))
    {
      this.b = paramInetAddress;
      b();
    }
    else
    {
      b(this.proxy.bind(paramInetAddress, paramInt));
    }
  }

  public Socket accept()
    throws IOException
  {
    Object localObject;
    if (!this.c)
    {
      if (this.proxy == null)
        return null;
      ProxyMessage localProxyMessage = this.proxy.accept();
      localObject = localProxyMessage.ip == null ? new SocksSocket(localProxyMessage.host, localProxyMessage.port, this.proxy) : new SocksSocket(localProxyMessage.ip, localProxyMessage.port, this.proxy);
      this.proxy.proxySocket.setSoTimeout(0);
    }
    else
    {
      while (true)
      {
        localObject = super.accept();
        if (((Socket)localObject).getInetAddress().equals(this.b))
          break;
        ((Socket)localObject).close();
      }
    }
    this.proxy = null;
    return (Socket)localObject;
  }

  public void close()
    throws IOException
  {
    super.close();
    if (this.proxy != null)
      this.proxy.endSession();
    this.proxy = null;
  }

  public String getHost()
  {
    return this.localHost;
  }

  public InetAddress getInetAddress()
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

  public int getLocalPort()
  {
    return this.localPort;
  }

  public void setSoTimeout(int paramInt)
    throws SocketException
  {
    super.setSoTimeout(paramInt);
    if (!this.c)
      this.proxy.proxySocket.setSoTimeout(paramInt);
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
  {
    this.c = true;
    this.localPort = super.getLocalPort();
    this.localIP = super.getInetAddress();
    this.localHost = this.localIP.getHostName();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.SocksServerSocket
 * JD-Core Version:    0.6.0
 */