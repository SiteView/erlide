package com.maverick.nio;

import com.maverick.events.EventService;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Daemon
{
  DaemonContext C;
  SelectorThreadPool E;
  SelectorThreadPool A;
  SelectorThreadPool J;
  Map H = Collections.synchronizedMap(new HashMap());
  boolean M = false;
  Thread D;
  boolean L;
  List F = new ArrayList();
  boolean K = false;
  static C G = new C();
  private static String I = "1.4.34";
  private static long B = 0L;

  public DaemonContext getContext()
  {
    return this.C;
  }

  public static String getVersion()
  {
    return I;
  }

  public static Date getReleaseDate()
  {
    return new Date(B);
  }

  public boolean isStarting()
  {
    return this.K;
  }

  public boolean startup()
    throws IOException
  {
    this.K = true;
    try
    {
      this.D = new Thread()
      {
        public void run()
        {
          Daemon.this.shutdown();
        }
      };
      EventLog.LogEvent(this, "Java version is " + System.getProperty("java.version"));
      EventLog.LogEvent(this, "OS is " + System.getProperty("os.name"));
      this.C = new DaemonContext(this);
      configure(this.C);
      int i = G.A();
//      switch (i & 0x1F)
//      {
//      case 1:
//        throw new LicenseException("Your license has expired! visit http://www.sshtools.com to obtain an update version of the software.");
//      case 4:
//        break;
//      case 2:
//        throw new LicenseException("Your license is invalid!");
//      case 8:
//        throw new LicenseException("This version of SSHD Maverick requires a license. visit http://www.sshtools.com to register");
//      case 16:
//        throw new LicenseException("Your subscription has expired! visit http://www.sshtools.com to purchase a subscription");
//      default:
//        throw new LicenseException("An unexpected license status was received.");
//      }
      if (Runtime.getRuntime() != null)
        Runtime.getRuntime().addShutdownHook(this.D);
      this.A = new SelectorThreadPool(new _D(), this.C.getPermanentConnectThreads(), this.C.getMaximumChannelsPerThread(), this.C.getIdleServiceRunPeriod(), this.C.getInactiveServiceRunsPerIdleEvent(), this.C.getSelectorProvider());
      this.J = new SelectorThreadPool(new _C(), this.C.getPermanentTransferThreads(), this.C.getMaximumChannelsPerThread(), this.C.getIdleServiceRunPeriod(), this.C.getInactiveServiceRunsPerIdleEvent(), this.C.getSelectorProvider());
      this.E = new SelectorThreadPool(new _A(), this.C.getPermanentAcceptThreads(), this.C.getMaximumChannelsPerThread(), this.C.getIdleServiceRunPeriod(), this.C.getInactiveServiceRunsPerIdleEvent(), this.C.getSelectorProvider());
      ListeningInterface[] arrayOfListeningInterface = this.C.getListeningInterfaces();
      int k = 0;
      for (int m = 0; m < arrayOfListeningInterface.length; m++)
      {
        if (!startListeningInterface(arrayOfListeningInterface[m]))
          continue;
        k++;
      }
      if (k == 0)
      {
        EventLog.LogEvent(this, "No listening interfaces were bound!");
        shutdown();
//        m = 0;
        return false;
      }
      this.L = true;
//      m = 1;
      return true;
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "The server failed to start", localThrowable);
      shutdown();
      if ((localThrowable instanceof LicenseException))
        throw ((IOException)localThrowable);
      int j = 0;
      return false;
    }
    finally
    {
      this.K = false;
    }
//    throw localObject;
  }

  protected boolean startListeningInterface(ListeningInterface paramListeningInterface)
    throws IOException
  {
    if (((paramListeningInterface.isIPV6Interface()) && (System.getProperty("os.name").toUpperCase().startsWith("WINDOWS")) && (Boolean.getBoolean("maverick.windowsIpv6Workaround"))) || ((paramListeningInterface.isIPV6Interface()) && (Boolean.getBoolean("maverick.windowsIpv6WorkaroundForce"))))
    {
      EventLog.LogEvent(this, "Detected IPv6 listener on Windows, need to workaround JDK bug 4640544 on interface " + paramListeningInterface.getAddressToBind().toString());
      InetSocketAddress localObject1;
      if (!this.M)
      {
        localObject1 = new InetSocketAddress(this.C.getIpv6WorkaroundBindAddress(), this.C.getIpv6WorkaroundPort());
        ListeningInterface localObject2 = new ListeningInterface((InetSocketAddress)localObject1, paramListeningInterface.getContext());
        if (!startListeningInterface(localObject2))
        {
          EventLog.LogEvent(this, "Could not start IPv6 workaround listener " + (localObject2).getAddressToBind().toString());
          return false;
        }
        this.M = true;
      }
      try
      {
        A A1  = new A(paramListeningInterface.getAddressToBind().getHostName(), paramListeningInterface.getAddressToBind().getPort(), this.C.getIpv6WorkaroundBindAddress(), this.C.getIpv6WorkaroundPort());
        this.F.add(A1);
        paramListeningInterface.setProxy(A1);
      }
      catch (IOException localIOException1)
      {
        EventLog.LogEvent(this, "Failed to bind to " + paramListeningInterface.getAddressToBind().toString(), localIOException1);
        return false;
      }
      return true;
    }
    EventLog.LogEvent(this, "Binding server to " + paramListeningInterface.getAddressToBind().toString());
    ServerSocketChannel localServerSocketChannel = this.C.getSelectorProvider().openServerSocketChannel();
    localServerSocketChannel.configureBlocking(false);
    localServerSocketChannel.socket().setReceiveBufferSize(paramListeningInterface.getContext().getReceiveBufferSize());
    localServerSocketChannel.socket().setReuseAddress(paramListeningInterface.getContext().getSocketOptionReuseAddress());
    Object localObject2 = localServerSocketChannel.socket();
    try
    {
      ((ServerSocket)localObject2).bind(paramListeningInterface.getAddressToBind(), 50);
      _B local_B = new _B(paramListeningInterface, localServerSocketChannel);
      registerAcceptor(local_B, localServerSocketChannel);
      this.H.put(paramListeningInterface.getAddressToBind().toString(), local_B);
      return true;
    }
    catch (IOException localIOException2)
    {
      EventLog.LogEvent(this, "Failed to bind to " + paramListeningInterface.getAddressToBind().toString(), localIOException2);
      this.C.removeListeningInterface(paramListeningInterface.getAddressToBind().toString());
    }
    return false;
  }

  public void removeAcceptor(ListeningInterface paramListeningInterface)
  {
    EventLog.LogEvent(this, "Removing interface " + paramListeningInterface.getAddressToBind().toString());
    _B local_B = (_B)this.H.remove(paramListeningInterface.getAddressToBind().toString());
    if (local_B != null)
      local_B.stopAccepting();
  }

  public boolean isStarted()
  {
    return this.L;
  }

  public void shutdown()
  {
    ForwardingManager.A();
    if ((this.M) && (this.F.size() > 0))
    {
      Iterator localIterator = this.F.iterator();
      while (localIterator.hasNext())
        ((A)localIterator.next()).A();
      this.F.clear();
      this.M = false;
    }
    try
    {
      if (this.E != null)
        this.E.shutdown();
      if (this.A != null)
        this.A.shutdown();
      if (this.J != null)
        this.J.shutdown();
      try
      {
        if (Runtime.getRuntime() != null)
          Runtime.getRuntime().removeShutdownHook(this.D);
      }
      catch (IllegalStateException localIllegalStateException)
      {
      }
    }
    finally
    {
      this.L = false;
    }
  }

  public void registerConnector(ClientConnector paramClientConnector, SocketChannel paramSocketChannel)
    throws IOException
  {
    SelectorThread localSelectorThread = this.A.selectNextThread();
    localSelectorThread.register(paramSocketChannel, 8, paramClientConnector, true);
  }

  public void registerAcceptor(ClientAcceptor paramClientAcceptor, ServerSocketChannel paramServerSocketChannel)
    throws IOException
  {
    SelectorThread localSelectorThread = this.E.selectNextThread();
    localSelectorThread.register(paramServerSocketChannel, 16, paramClientAcceptor, true);
  }

  public void registerHandler(SocketHandler paramSocketHandler, SelectableChannel paramSelectableChannel)
    throws IOException
  {
    SelectorThread localSelectorThread = this.J.selectNextThread();
    registerHandler(paramSocketHandler, paramSelectableChannel, localSelectorThread);
  }

  public void registerHandler(SocketHandler paramSocketHandler, SelectableChannel paramSelectableChannel, SelectorThread paramSelectorThread)
    throws IOException
  {
    paramSocketHandler.setThread(paramSelectorThread);
    if (paramSelectorThread == null)
      throw new IOException("Unable to allocate thread");
    paramSelectorThread.register(paramSelectableChannel, paramSocketHandler.getInterestedOps(), paramSocketHandler, true);
  }

  protected abstract void configure(DaemonContext paramDaemonContext)
    throws IOException;

  static
  {
    B = 1316539815622L;
  }

  class _B extends ClientAcceptor
  {
    ServerSocketChannel C;
    ListeningInterface B;

//    _B(ListeningInterface paramServerSocketChannel, ServerSocketChannel arg3)
//    {
//      super();
//      this.B = paramServerSocketChannel;
//      ServerSocketChannel localObject;
//      this.C = localObject;
//    }
    
    _B(ListeningInterface listeninginterface, ServerSocketChannel serversocketchannel)
    {
        super(listeninginterface.getContext());
        B = listeninginterface;
        C = serversocketchannel;
    }

    public boolean finishAccept(SelectionKey paramSelectionKey, ProtocolContext paramProtocolContext)
    {
      try
      {
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 100, true).addAttribute("IP", ((ServerSocketChannel)paramSelectionKey.channel()).socket().getInetAddress().getHostAddress()));
        SocketChannel localSocketChannel = ((ServerSocketChannel)paramSelectionKey.channel()).accept();
        if (localSocketChannel != null)
        {
          localSocketChannel.socket().setKeepAlive(paramProtocolContext.getSocketOptionKeepAlive());
          localSocketChannel.socket().setTcpNoDelay(paramProtocolContext.getSocketOptionTcpNoDelay());
          localSocketChannel.socket().setReceiveBufferSize(paramProtocolContext.getReceiveBufferSize());
          localSocketChannel.socket().setSendBufferSize(paramProtocolContext.getSendBufferSize());
          localSocketChannel.configureBlocking(false);
          Daemon.this.registerHandler(paramProtocolContext.createConnection(Daemon.this), localSocketChannel);
        }
      }
      catch (Throwable localThrowable)
      {
        EventLog.LogEvent(this, "SSH client acceptor failed to accept", localThrowable);
      }
      return !((ServerSocketChannel)paramSelectionKey.channel()).isOpen();
    }

    public void stopAccepting()
    {
      try
      {
        this.C.close();
      }
      catch (Throwable localThrowable)
      {
      }
      if (this.B.getProxy() != null)
        this.B.getProxy().A();
    }
  }

  class _D
    implements SelectorThreadImpl
  {
    _D()
    {
    }

    public boolean processSelectionKey(SelectionKey paramSelectionKey)
    {
      ClientConnector localClientConnector = (ClientConnector)paramSelectionKey.attachment();
      return localClientConnector.finishConnect(paramSelectionKey);
    }

    public String getName()
    {
      return Daemon.this.C.getProduct() + "-CONNECT";
    }
  }

  class _C
    implements SelectorThreadImpl
  {
    _C()
    {
    }

    public boolean processSelectionKey(SelectionKey paramSelectionKey)
    {
      SocketHandler localSocketHandler = (SocketHandler)paramSelectionKey.attachment();
      boolean bool = false;
      if (paramSelectionKey.isReadable())
        bool = localSocketHandler.processReadEvent();
      if (bool)
        return bool;
      if (paramSelectionKey.isWritable())
        bool = localSocketHandler.processWriteEvent();
      return bool;
    }

    public String getName()
    {
      return Daemon.this.C.getProduct() + "-TRANSFER";
    }
  }

  class _A
    implements SelectorThreadImpl
  {
    _A()
    {
    }

    public boolean processSelectionKey(SelectionKey paramSelectionKey)
    {
      ClientAcceptor localClientAcceptor = (ClientAcceptor)paramSelectionKey.attachment();
      return localClientAcceptor.finishAccept(paramSelectionKey);
    }

    public String getName()
    {
      return Daemon.this.C.getProduct() + "-ACCEPT";
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.Daemon
 * JD-Core Version:    0.6.0
 */