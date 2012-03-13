package socks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;

public class Socks5Proxy extends Proxy
  implements Cloneable
{
  private Hashtable e = new Hashtable();
  private int f;
  boolean c = false;
  UDPEncapsulation d = null;

  public Socks5Proxy(Proxy paramProxy, String paramString, int paramInt)
    throws UnknownHostException
  {
    super(paramProxy, paramString, paramInt);
    this.version = 5;
    setAuthenticationMethod(0, new AuthenticationNone());
  }

  public Socks5Proxy(String paramString, int paramInt)
    throws UnknownHostException
  {
    this(null, paramString, paramInt);
  }

  public Socks5Proxy(Proxy paramProxy, InetAddress paramInetAddress, int paramInt)
  {
    super(paramProxy, paramInetAddress, paramInt);
    this.version = 5;
    setAuthenticationMethod(0, new AuthenticationNone());
  }

  public Socks5Proxy(InetAddress paramInetAddress, int paramInt)
  {
    this(null, paramInetAddress, paramInt);
  }

  public boolean resolveAddrLocally(boolean paramBoolean)
  {
    boolean bool = this.c;
    this.c = paramBoolean;
    return bool;
  }

  public boolean resolveAddrLocally()
  {
    return this.c;
  }

  public boolean setAuthenticationMethod(int paramInt, Authentication paramAuthentication)
  {
    if ((paramInt < 0) || (paramInt > 255))
      return false;
    if (paramAuthentication == null)
      return this.e.remove(new Integer(paramInt)) != null;
    this.e.put(new Integer(paramInt), paramAuthentication);
    return true;
  }

  public Authentication getAuthenticationMethod(int paramInt)
  {
    Object localObject = this.e.get(new Integer(paramInt));
    if (localObject == null)
      return null;
    return (Authentication)localObject;
  }

  public Object clone()
  {
    Socks5Proxy localSocks5Proxy = new Socks5Proxy(this.proxyIP, this.proxyPort);
    localSocks5Proxy.e = ((Hashtable)this.e.clone());
    localSocks5Proxy.directHosts = ((InetRange)this.directHosts.clone());
    localSocks5Proxy.c = this.c;
    localSocks5Proxy.chainProxy = this.chainProxy;
    return localSocks5Proxy;
  }

  protected Proxy copy()
  {
    Socks5Proxy localSocks5Proxy = new Socks5Proxy(this.proxyIP, this.proxyPort);
    localSocks5Proxy.e = this.e;
    localSocks5Proxy.directHosts = this.directHosts;
    localSocks5Proxy.chainProxy = this.chainProxy;
    localSocks5Proxy.c = this.c;
    return localSocks5Proxy;
  }

  protected void startSession()
    throws SocksException
  {
    super.startSession();
    Socket localSocket = this.proxySocket;
    try
    {
      int i = (byte)this.e.size();
      byte[] arrayOfByte = new byte[2 + i];
      arrayOfByte[0] = (byte)this.version;
      arrayOfByte[1] = i;
      int j = 2;
      Enumeration localEnumeration = this.e.keys();
      while (localEnumeration.hasMoreElements())
        arrayOfByte[(j++)] = (byte)((Integer)localEnumeration.nextElement()).intValue();
      this.out.write(arrayOfByte);
      this.out.flush();
      int k = this.in.read();
      this.f = this.in.read();
      if ((k < 0) || (this.f < 0))
      {
        endSession();
        throw new SocksException(196608, "Connection to proxy lost.");
      }
      if ((k >= this.version) || (this.f == 255))
      {
        localSocket.close();
        throw new SocksException(262144);
      }
      Authentication localAuthentication = getAuthenticationMethod(this.f);
      if (localAuthentication == null)
        throw new SocksException(393216, "Speciefied Authentication not found!");
      Object[] arrayOfObject = localAuthentication.doSocksAuthentication(this.f, localSocket);
      if (arrayOfObject == null)
        throw new SocksException(327680);
      this.in = ((InputStream)arrayOfObject[0]);
      this.out = ((OutputStream)arrayOfObject[1]);
      if (arrayOfObject.length > 2)
        this.d = ((UDPEncapsulation)arrayOfObject[2]);
    }
    catch (SocksException localSocksException)
    {
      throw localSocksException;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw new SocksException(131072);
    }
    catch (SocketException localSocketException)
    {
      throw new SocksException(131072);
    }
    catch (IOException localIOException)
    {
      throw new SocksException(196608, "" + localIOException);
    }
  }

  protected ProxyMessage formMessage(int paramInt1, InetAddress paramInetAddress, int paramInt2)
  {
    return new e(paramInt1, paramInetAddress, paramInt2);
  }

  protected ProxyMessage formMessage(int paramInt1, String paramString, int paramInt2)
    throws UnknownHostException
  {
    if (this.c)
      return formMessage(paramInt1, InetAddress.getByName(paramString), paramInt2);
    return new e(paramInt1, paramString, paramInt2);
  }

  protected ProxyMessage formMessage(InputStream paramInputStream)
    throws SocksException, IOException
  {
    return new e(paramInputStream);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.Socks5Proxy
 * JD-Core Version:    0.6.0
 */