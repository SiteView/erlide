package socks;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Socks4Proxy extends Proxy
  implements Cloneable
{
  String b;

  public Socks4Proxy(Proxy paramProxy, String paramString1, int paramInt, String paramString2)
    throws UnknownHostException
  {
    super(paramProxy, paramString1, paramInt);
    this.b = new String(paramString2);
    this.version = 4;
  }

  public Socks4Proxy(String paramString1, int paramInt, String paramString2)
    throws UnknownHostException
  {
    this(null, paramString1, paramInt, paramString2);
  }

  public Socks4Proxy(Proxy paramProxy, InetAddress paramInetAddress, int paramInt, String paramString)
  {
    super(paramProxy, paramInetAddress, paramInt);
    this.b = new String(paramString);
    this.version = 4;
  }

  public Socks4Proxy(InetAddress paramInetAddress, int paramInt, String paramString)
  {
    this(null, paramInetAddress, paramInt, paramString);
  }

  public Object clone()
  {
    Socks4Proxy localSocks4Proxy = new Socks4Proxy(this.proxyIP, this.proxyPort, this.b);
    localSocks4Proxy.directHosts = ((InetRange)this.directHosts.clone());
    localSocks4Proxy.chainProxy = this.chainProxy;
    return localSocks4Proxy;
  }

  protected Proxy copy()
  {
    Socks4Proxy localSocks4Proxy = new Socks4Proxy(this.proxyIP, this.proxyPort, this.b);
    localSocks4Proxy.directHosts = this.directHosts;
    localSocks4Proxy.chainProxy = this.chainProxy;
    return localSocks4Proxy;
  }

  protected ProxyMessage formMessage(int paramInt1, InetAddress paramInetAddress, int paramInt2)
  {
    switch (paramInt1)
    {
    case 1:
      paramInt1 = 1;
      break;
    case 2:
      paramInt1 = 2;
      break;
    default:
      return null;
    }
    return new b(paramInt1, paramInetAddress, paramInt2, this.b);
  }

  protected ProxyMessage formMessage(int paramInt1, String paramString, int paramInt2)
    throws UnknownHostException
  {
    return formMessage(paramInt1, InetAddress.getByName(paramString), paramInt2);
  }

  protected ProxyMessage formMessage(InputStream paramInputStream)
    throws SocksException, IOException
  {
    return new b(paramInputStream, true);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.Socks4Proxy
 * JD-Core Version:    0.6.0
 */