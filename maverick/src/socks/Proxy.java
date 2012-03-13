package socks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public abstract class Proxy
{
  protected InetRange directHosts = new InetRange();
  protected InetAddress proxyIP = null;
  protected String proxyHost = null;
  protected int proxyPort;
  protected Socket proxySocket = null;
  protected InputStream in;
  protected OutputStream out;
  protected int version;
  protected Proxy chainProxy = null;
  protected static Proxy defaultProxy = null;
  public static final int SOCKS_SUCCESS = 0;
  public static final int SOCKS_FAILURE = 1;
  public static final int SOCKS_BADCONNECT = 2;
  public static final int SOCKS_BADNETWORK = 3;
  public static final int SOCKS_HOST_UNREACHABLE = 4;
  public static final int SOCKS_CONNECTION_REFUSED = 5;
  public static final int SOCKS_TTL_EXPIRE = 6;
  public static final int SOCKS_CMD_NOT_SUPPORTED = 7;
  public static final int SOCKS_ADDR_NOT_SUPPORTED = 8;
  public static final int SOCKS_NO_PROXY = 65536;
  public static final int SOCKS_PROXY_NO_CONNECT = 131072;
  public static final int SOCKS_PROXY_IO_ERROR = 196608;
  public static final int SOCKS_AUTH_NOT_SUPPORTED = 262144;
  public static final int SOCKS_AUTH_FAILURE = 327680;
  public static final int SOCKS_JUST_ERROR = 393216;
  public static final int SOCKS_DIRECT_FAILED = 458752;
  public static final int SOCKS_METHOD_NOTSUPPORTED = 524288;

  Proxy(Proxy paramProxy, String paramString, int paramInt)
    throws UnknownHostException
  {
    this.chainProxy = paramProxy;
    this.proxyHost = paramString;
    if (paramProxy == null)
      this.proxyIP = InetAddress.getByName(paramString);
    this.proxyPort = paramInt;
  }

  Proxy(Proxy paramProxy, InetAddress paramInetAddress, int paramInt)
  {
    this.chainProxy = paramProxy;
    this.proxyIP = paramInetAddress;
    this.proxyPort = paramInt;
  }

  public int getPort()
  {
    return this.proxyPort;
  }

  public InetAddress getInetAddress()
  {
    return this.proxyIP;
  }

  public void addDirect(InetAddress paramInetAddress)
  {
    this.directHosts.add(paramInetAddress);
  }

  public boolean addDirect(String paramString)
  {
    return this.directHosts.add(paramString);
  }

  public void addDirect(InetAddress paramInetAddress1, InetAddress paramInetAddress2)
  {
    this.directHosts.add(paramInetAddress1, paramInetAddress2);
  }

  public void setDirect(InetRange paramInetRange)
  {
    this.directHosts = paramInetRange;
  }

  public InetRange getDirect()
  {
    return this.directHosts;
  }

  public boolean isDirect(String paramString)
  {
    return this.directHosts.contains(paramString);
  }

  public boolean isDirect(InetAddress paramInetAddress)
  {
    return this.directHosts.contains(paramInetAddress);
  }

  public void setChainProxy(Proxy paramProxy)
  {
    this.chainProxy = paramProxy;
  }

  public Proxy getChainProxy()
  {
    return this.chainProxy;
  }

  public String toString()
  {
    return "" + this.proxyIP.getHostName() + ":" + this.proxyPort + "\tVersion " + this.version;
  }

  public static void setDefaultProxy(String paramString1, int paramInt, String paramString2)
    throws UnknownHostException
  {
    defaultProxy = new Socks4Proxy(paramString1, paramInt, paramString2);
  }

  public static void setDefaultProxy(InetAddress paramInetAddress, int paramInt, String paramString)
  {
    defaultProxy = new Socks4Proxy(paramInetAddress, paramInt, paramString);
  }

  public static void setDefaultProxy(String paramString, int paramInt)
    throws UnknownHostException
  {
    defaultProxy = new Socks5Proxy(paramString, paramInt);
  }

  public static void setDefaultProxy(InetAddress paramInetAddress, int paramInt)
  {
    defaultProxy = new Socks5Proxy(paramInetAddress, paramInt);
  }

  public static void setDefaultProxy(Proxy paramProxy)
  {
    defaultProxy = paramProxy;
  }

  public static Proxy getDefaultProxy()
  {
    return defaultProxy;
  }

  public static Proxy parseProxy(String paramString)
  {
    int i = 1080;
    String str2 = null;
    String str3 = null;
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ":");
    if (localStringTokenizer.countTokens() < 1)
      return null;
    String str1 = localStringTokenizer.nextToken();
    if (localStringTokenizer.hasMoreTokens())
      try
      {
        i = Integer.parseInt(localStringTokenizer.nextToken().trim());
      }
      catch (NumberFormatException localNumberFormatException)
      {
      }
    if (localStringTokenizer.hasMoreTokens())
      str2 = localStringTokenizer.nextToken();
    if (localStringTokenizer.hasMoreTokens())
      str3 = localStringTokenizer.nextToken();
    Object localObject;
    try
    {
      if (str2 == null)
      {
        localObject = new Socks5Proxy(str1, i);
      }
      else if (str3 == null)
      {
        localObject = new Socks4Proxy(str1, i, str2);
      }
      else
      {
        localObject = new Socks5Proxy(str1, i);
        UserPasswordAuthentication localUserPasswordAuthentication = new UserPasswordAuthentication(str2, str3);
        ((Socks5Proxy)localObject).setAuthenticationMethod(2, localUserPasswordAuthentication);
      }
    }
    catch (UnknownHostException localUnknownHostException)
    {
      return null;
    }
    return (Proxy)localObject;
  }

  protected void startSession()
    throws SocksException
  {
    System.out.println("Session: " + this.proxyHost + ":" + this.proxyPort);
    try
    {
      if (this.chainProxy == null)
        this.proxySocket = new Socket(this.proxyIP, this.proxyPort);
      else if (this.proxyIP != null)
        this.proxySocket = new SocksSocket(this.chainProxy, this.proxyIP, this.proxyPort);
      else
        this.proxySocket = new SocksSocket(this.chainProxy, this.proxyHost, this.proxyPort);
      this.in = this.proxySocket.getInputStream();
      this.out = this.proxySocket.getOutputStream();
    }
    catch (SocksException localSocksException)
    {
      throw localSocksException;
    }
    catch (IOException localIOException)
    {
      throw new SocksException(196608, "" + localIOException);
    }
  }

  protected abstract Proxy copy();

  protected abstract ProxyMessage formMessage(int paramInt1, InetAddress paramInetAddress, int paramInt2);

  protected abstract ProxyMessage formMessage(int paramInt1, String paramString, int paramInt2)
    throws UnknownHostException;

  protected abstract ProxyMessage formMessage(InputStream paramInputStream)
    throws SocksException, IOException;

  protected ProxyMessage connect(InetAddress paramInetAddress, int paramInt)
    throws SocksException
  {
    try
    {
      startSession();
      ProxyMessage localProxyMessage = formMessage(1, paramInetAddress, paramInt);
      return exchange(localProxyMessage);
    }
    catch (SocksException localSocksException)
    {
      endSession();
    }
    throw localSocksException;
  }

  protected ProxyMessage connect(String paramString, int paramInt)
    throws UnknownHostException, SocksException
  {
    try
    {
      startSession();
      ProxyMessage localProxyMessage = formMessage(1, paramString, paramInt);
      return exchange(localProxyMessage);
    }
    catch (SocksException localSocksException)
    {
      endSession();
    }
    throw localSocksException;
  }

  protected ProxyMessage bind(InetAddress paramInetAddress, int paramInt)
    throws SocksException
  {
    try
    {
      startSession();
      ProxyMessage localProxyMessage = formMessage(2, paramInetAddress, paramInt);
      return exchange(localProxyMessage);
    }
    catch (SocksException localSocksException)
    {
      endSession();
    }
    throw localSocksException;
  }

  protected ProxyMessage bind(String paramString, int paramInt)
    throws UnknownHostException, SocksException
  {
    try
    {
      startSession();
      ProxyMessage localProxyMessage = formMessage(2, paramString, paramInt);
      return exchange(localProxyMessage);
    }
    catch (SocksException localSocksException)
    {
      endSession();
    }
    throw localSocksException;
  }

  protected ProxyMessage accept()
    throws IOException, SocksException
  {
    ProxyMessage localProxyMessage;
    try
    {
      localProxyMessage = formMessage(this.in);
    }
    catch (InterruptedIOException localInterruptedIOException)
    {
      throw localInterruptedIOException;
    }
    catch (IOException localIOException)
    {
      endSession();
      throw new SocksException(196608, "While Trying accept:" + localIOException);
    }
    return localProxyMessage;
  }

  protected ProxyMessage udpAssociate(InetAddress paramInetAddress, int paramInt)
    throws SocksException
  {
    try
    {
      startSession();
      ProxyMessage localProxyMessage = formMessage(3, paramInetAddress, paramInt);
      if (localProxyMessage != null)
        return exchange(localProxyMessage);
    }
    catch (SocksException localSocksException)
    {
      endSession();
      throw localSocksException;
    }
    endSession();
    throw new SocksException(524288, "This version of proxy does not support UDP associate, use version 5");
  }

  protected ProxyMessage udpAssociate(String paramString, int paramInt)
    throws UnknownHostException, SocksException
  {
    try
    {
      startSession();
      ProxyMessage localProxyMessage = formMessage(3, paramString, paramInt);
      if (localProxyMessage != null)
        return exchange(localProxyMessage);
    }
    catch (SocksException localSocksException)
    {
      endSession();
      throw localSocksException;
    }
    endSession();
    throw new SocksException(524288, "This version of proxy does not support UDP associate, use version 5");
  }

  protected void endSession()
  {
    try
    {
      if (this.proxySocket != null)
        this.proxySocket.close();
      this.proxySocket = null;
    }
    catch (IOException localIOException)
    {
    }
  }

  protected void sendMsg(ProxyMessage paramProxyMessage)
    throws SocksException, IOException
  {
    paramProxyMessage.write(this.out);
  }

  protected ProxyMessage readMsg()
    throws SocksException, IOException
  {
    return formMessage(this.in);
  }

  protected ProxyMessage exchange(ProxyMessage paramProxyMessage)
    throws SocksException
  {
    ProxyMessage localProxyMessage;
    try
    {
      paramProxyMessage.write(this.out);
      localProxyMessage = formMessage(this.in);
    }
    catch (SocksException localSocksException)
    {
      throw localSocksException;
    }
    catch (IOException localIOException)
    {
      throw new SocksException(196608, "" + localIOException);
    }
    return localProxyMessage;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.Proxy
 * JD-Core Version:    0.6.0
 */