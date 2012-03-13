package socks;

import com.maverick.ssh.ChannelOpenException;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.SshTunnel;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import socks.server.ServerAuthenticator;

public class ProxyServer
  implements Runnable
{
  ServerAuthenticator k;
  ProxyMessage d = null;
  SshClient b;
  Socket s = null;
  Socket i = null;
  ServerSocket p = null;
  c e = null;
  InputStream h;
  InputStream j;
  OutputStream r;
  OutputStream l;
  int n;
  Thread u;
  Thread t;
  long c;
  static int o = 180000;
  static int m = 180000;
  static Log f = LogFactory.getLog(ProxyServer.class);
  static Proxy q;
  static final String[] g = { "CONNECT", "BIND", "UDP_ASSOCIATE" };

  public ProxyServer(ServerAuthenticator paramServerAuthenticator, SshClient paramSshClient)
  {
    this.b = paramSshClient;
    this.k = paramServerAuthenticator;
  }

  ProxyServer(ServerAuthenticator paramServerAuthenticator, Socket paramSocket, SshClient paramSshClient)
  {
    this.k = paramServerAuthenticator;
    this.s = paramSocket;
    this.b = paramSshClient;
    this.n = 0;
  }

  public static void setLog(OutputStream paramOutputStream)
  {
  }

  public static void setProxy(Proxy paramProxy)
  {
    q = paramProxy;
    c.k = q;
  }

  public static Proxy getProxy()
  {
    return q;
  }

  public static void setIddleTimeout(int paramInt)
  {
    o = paramInt;
  }

  public static void setAcceptTimeout(int paramInt)
  {
    m = paramInt;
  }

  public static void setUDPTimeout(int paramInt)
  {
    c.checkVersion(paramInt);
  }

  public static void setDatagramSize(int paramInt)
  {
    c.b(paramInt);
  }

  public void start(int paramInt)
  {
    start(paramInt, 5, null);
  }

  public void start(int paramInt1, int paramInt2, InetAddress paramInetAddress)
  {
    try
    {
      this.p = new ServerSocket(paramInt1, paramInt2, paramInetAddress);
      b("Starting SOCKS Proxy on:" + this.p.getInetAddress().getHostAddress() + ":" + this.p.getLocalPort());
      while (true)
      {
        Socket localSocket = this.p.accept();
        b("Accepted from:" + localSocket.getInetAddress().getHostName() + ":" + localSocket.getPort());
        ProxyServer localProxyServer = new ProxyServer(this.k, localSocket, this.b);
        new Thread(localProxyServer).start();
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    finally
    {
    }
  }

  public void stop()
  {
    try
    {
      if (this.p != null)
        this.p.close();
    }
    catch (IOException localIOException)
    {
    }
  }

  public void run()
  {
    switch (this.n)
    {
    case 0:
      try
      {
        b();
      }
      catch (IOException localIOException1)
      {
        b(localIOException1);
      }
      finally
      {
        d();
        if (this.k != null)
          this.k.endSession();
        b("Main thread(client->remote)stopped.");
      }
      break;
    case 1:
      try
      {
        c();
        this.n = 2;
        this.u.interrupt();
        b(this.j, this.r);
      }
      catch (IOException localIOException2)
      {
        b(localIOException2);
      }
      finally
      {
        d();
        b("Accept thread(remote->client) stopped");
      }
      break;
    case 2:
      try
      {
        b(this.j, this.r);
      }
      catch (IOException localIOException3)
      {
      }
      finally
      {
        d();
        b("Support thread(remote->client) stopped");
      }
      break;
    case 3:
      break;
    default:
      b("Unexpected MODE " + this.n);
    }
  }

  private void b()
    throws IOException
  {
    this.s.setSoTimeout(o);
    try
    {
      this.k = this.k.startSession(this.s);
    }
    catch (IOException localIOException)
    {
      b("Auth throwed exception:" + localIOException);
      this.k = null;
      return;
    }
    if (this.k == null)
    {
      b("Authentication failed");
      return;
    }
    this.h = this.k.getInputStream();
    this.r = this.k.getOutputStream();
    this.d = b(this.h);
    d(this.d);
  }

  private void d(ProxyMessage paramProxyMessage)
    throws IOException
  {
    if (!this.k.checkRequest(paramProxyMessage))
      throw new SocksException(1);
    if (paramProxyMessage.ip == null)
      if ((paramProxyMessage instanceof e))
        paramProxyMessage.ip = InetAddress.getByName(paramProxyMessage.host);
      else
        throw new SocksException(1);
    e(paramProxyMessage);
    switch (paramProxyMessage.command)
    {
    case 1:
      c(paramProxyMessage);
      break;
    case 2:
      f(paramProxyMessage);
      break;
    case 3:
      b(paramProxyMessage);
      break;
    default:
      throw new SocksException(7);
    }
  }

  private void b(IOException paramIOException)
  {
    if (this.d == null)
      return;
    if (this.n == 3)
      return;
    if (this.n == 2)
      return;
    int i1 = 1;
    if ((paramIOException instanceof SocksException))
      i1 = ((SocksException)paramIOException).c;
    else if ((paramIOException instanceof NoRouteToHostException))
      i1 = 4;
    else if ((paramIOException instanceof ConnectException))
      i1 = 5;
    else if ((paramIOException instanceof InterruptedIOException))
      i1 = 6;
    if ((i1 > 8) || (i1 < 0))
      i1 = 1;
    b(i1);
  }

  private void c(ProxyMessage paramProxyMessage)
    throws IOException
  {
    Object localObject2 = null;
    Object localObject1;
    if (q == null)
    {
      SshTunnel localSshTunnel = null;
      try
      {
        if (this.b == null)
          b("Agent is NULL");
        if ((!this.b.isConnected()) || (!this.b.isAuthenticated()))
          b("Connection is dead");
        localSshTunnel = this.b.openForwardingChannel(paramProxyMessage.host, paramProxyMessage.port, "127.0.0.1", paramProxyMessage.port, "", paramProxyMessage.port, null, null);
        b("Opening SOCKS channel to " + paramProxyMessage.host + paramProxyMessage.port);
        localObject1 = new SocketWrappedChannel(localSshTunnel);
      }
      catch (ChannelOpenException localChannelOpenException)
      {
        b("Channel open exception: " + localChannelOpenException.getMessage());
        throw new IOException(localChannelOpenException.getMessage());
      }
      catch (SshException localSshException)
      {
        b("SSH error: " + localSshException.getMessage());
        throw new SshIOException(localSshException);
      }
    }
    else
    {
      localObject1 = new SocksSocket(q, paramProxyMessage.ip, paramProxyMessage.port);
    }
    b("Connected to " + ((Socket)localObject1).getInetAddress() + ":" + ((Socket)localObject1).getPort());
    if ((paramProxyMessage instanceof e))
      localObject2 = new e(0, ((Socket)localObject1).getLocalAddress(), ((Socket)localObject1).getLocalPort());
    else
      localObject2 = new b(90, ((Socket)localObject1).getLocalAddress(), ((Socket)localObject1).getLocalPort());
    ((ProxyMessage)localObject2).write(this.r);
    b((Socket)localObject1);
  }

  private void f(ProxyMessage paramProxyMessage)
    throws IOException
  {
    Object localObject1 = null;
    if (q == null)
      this.p = new ServerSocket(0);
    else
      this.p = new SocksServerSocket(q, paramProxyMessage.ip, paramProxyMessage.port);
    this.p.setSoTimeout(m);
    b("Trying accept on " + this.p.getInetAddress() + ":" + this.p.getLocalPort());
    if (paramProxyMessage.version == 5)
      localObject1 = new e(0, this.p.getInetAddress(), this.p.getLocalPort());
    else
      localObject1 = new b(90, this.p.getInetAddress(), this.p.getLocalPort());
    ((ProxyMessage)localObject1).write(this.r);
    this.n = 1;
    this.u = Thread.currentThread();
    this.t = new Thread(this);
    this.t.start();
    this.s.setSoTimeout(0);
    int i1 = 0;
    try
    {
      while ((i1 = this.h.read()) >= 0)
      {
        if (this.n == 1)
          continue;
        if (this.n != 2)
          return;
        this.l.write(i1);
      }
    }
    catch (EOFException localEOFException)
    {
      return;
    }
    catch (InterruptedIOException localInterruptedIOException)
    {
      if (this.n != 2)
        return;
    }
    finally
    {
    }
    if (i1 < 0)
      return;
    b(this.h, this.l);
  }

  private void b(ProxyMessage paramProxyMessage)
    throws IOException
  {
    if (paramProxyMessage.ip.getHostAddress().equals("0.0.0.0"))
      paramProxyMessage.ip = this.s.getInetAddress();
    b("Creating UDP relay server for " + paramProxyMessage.ip + ":" + paramProxyMessage.port);
    this.e = new c(paramProxyMessage.ip, paramProxyMessage.port, Thread.currentThread(), this.s, this.k);
    e locale = new e(0, this.e.j, this.e.i);
    locale.write(this.r);
    this.e.d();
    this.s.setSoTimeout(0);
    try
    {
      while (this.h.read() >= 0);
    }
    catch (EOFException localEOFException)
    {
    }
  }

  private void c()
    throws IOException
  {
    long l1 = System.currentTimeMillis();
    Socket localSocket;
    while (true)
    {
      localSocket = this.p.accept();
      if (localSocket.getInetAddress().equals(this.d.ip))
      {
        this.p.close();
        break;
      }
      if ((this.p instanceof SocksServerSocket))
      {
        localSocket.close();
        this.p.close();
        throw new SocksException(1);
      }
      if (m != 0)
      {
        int i1 = m - (int)(System.currentTimeMillis() - l1);
        if (i1 <= 0)
          throw new InterruptedIOException("In doAccept()");
        this.p.setSoTimeout(i1);
      }
      localSocket.close();
    }
    this.i = localSocket;
    this.j = localSocket.getInputStream();
    this.l = localSocket.getOutputStream();
    this.i.setSoTimeout(o);
    b("Accepted from " + localSocket.getInetAddress() + ":" + localSocket.getPort());
    Object localObject;
    if (this.d.version == 5)
      localObject = new e(0, localSocket.getInetAddress(), localSocket.getPort());
    else
      localObject = new b(90, localSocket.getInetAddress(), localSocket.getPort());
    ((ProxyMessage)localObject).write(this.r);
  }

  private ProxyMessage b(InputStream paramInputStream)
    throws IOException
  {
    PushbackInputStream localPushbackInputStream;
    if ((paramInputStream instanceof PushbackInputStream))
      localPushbackInputStream = (PushbackInputStream)paramInputStream;
    else
      localPushbackInputStream = new PushbackInputStream(paramInputStream);
    int i1 = localPushbackInputStream.read();
    localPushbackInputStream.unread(i1);
    Object localObject;
    if (i1 == 5)
      localObject = new e(localPushbackInputStream, false);
    else if (i1 == 4)
      localObject = new b(localPushbackInputStream, false);
    else
      throw new SocksException(1);
    return (ProxyMessage)localObject;
  }

  private void b(Socket paramSocket)
  {
    this.n = 2;
    this.i = paramSocket;
    try
    {
      this.j = paramSocket.getInputStream();
      this.l = paramSocket.getOutputStream();
      this.u = Thread.currentThread();
      this.t = new Thread(this);
      this.t.start();
      b(this.h, this.l);
    }
    catch (IOException localIOException)
    {
    }
  }

  private void b(int paramInt)
  {
    Object localObject;
    if ((this.d instanceof b))
      localObject = new b(91);
    else
      localObject = new e(paramInt);
    try
    {
      ((ProxyMessage)localObject).write(this.r);
    }
    catch (IOException localIOException)
    {
    }
  }

  private synchronized void d()
  {
    if (this.n == 3)
      return;
    this.n = 3;
    try
    {
      b("Aborting operation");
      if (this.i != null)
        this.i.close();
      if (this.s != null)
        this.s.close();
      if (this.e != null)
        this.e.b();
      if (this.p != null)
        this.p.close();
      if (this.u != null)
        this.u.interrupt();
      if (this.t != null)
        this.t.interrupt();
    }
    catch (IOException localIOException)
    {
    }
  }

  static final void b(String paramString)
  {
    f.info(paramString);
  }

  static final void e(ProxyMessage paramProxyMessage)
  {
    b("Request version:" + paramProxyMessage.version + "\tCommand: " + c(paramProxyMessage.command));
    b("IP:" + paramProxyMessage.ip + "\tPort:" + paramProxyMessage.port + (paramProxyMessage.version == 4 ? "\tUser:" + paramProxyMessage.user : ""));
  }

  private void b(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    this.c = System.currentTimeMillis();
    byte[] arrayOfByte = new byte[8192];
    int i1 = 0;
    while (i1 >= 0)
      try
      {
        if (i1 != 0)
        {
          paramOutputStream.write(arrayOfByte, 0, i1);
          paramOutputStream.flush();
        }
        i1 = paramInputStream.read(arrayOfByte);
        this.c = System.currentTimeMillis();
      }
      catch (InterruptedIOException localInterruptedIOException)
      {
        if (o == 0)
          return;
        long l1 = System.currentTimeMillis() - this.c;
        if (l1 >= o - 1000)
          return;
        i1 = 0;
      }
  }

  static final String c(int paramInt)
  {
    if ((paramInt > 0) && (paramInt < 4))
      return g[(paramInt - 1)];
    return "Unknown Command " + paramInt;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.ProxyServer
 * JD-Core Version:    0.6.0
 */