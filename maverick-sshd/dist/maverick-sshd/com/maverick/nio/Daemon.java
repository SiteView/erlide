// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.maverick.nio;

import com.maverick.events.EventService;
import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

// Referenced classes of package com.maverick.nio:
//            EventLog, DaemonContext, C, LicenseException, 
//            SelectorThreadPool, ListeningInterface, A, ProtocolContext, 
//            ForwardingManager, SelectorThread, SocketHandler, ClientConnector, 
//            ClientAcceptor, EventServiceImplementation, Event, SelectorThreadImpl

public abstract class Daemon
{
    class _B extends ClientAcceptor
    {

        public boolean finishAccept(SelectionKey selectionkey, ProtocolContext protocolcontext)
        {
            try
            {
                EventServiceImplementation.getInstance().fireEvent((new Event(this, 100, true)).addAttribute("IP", ((ServerSocketChannel)selectionkey.channel()).socket().getInetAddress().getHostAddress()));
                SocketChannel socketchannel = ((ServerSocketChannel)selectionkey.channel()).accept();
                if(socketchannel != null)
                {
                    socketchannel.socket().setKeepAlive(protocolcontext.getSocketOptionKeepAlive());
                    socketchannel.socket().setTcpNoDelay(protocolcontext.getSocketOptionTcpNoDelay());
                    socketchannel.socket().setReceiveBufferSize(protocolcontext.getReceiveBufferSize());
                    socketchannel.socket().setSendBufferSize(protocolcontext.getSendBufferSize());
                    socketchannel.configureBlocking(false);
                    registerHandler(protocolcontext.createConnection(Daemon.this), socketchannel);
                }
            }
            catch(Throwable throwable)
            {
                EventLog.LogEvent(this, "SSH client acceptor failed to accept", throwable);
            }
            return !((ServerSocketChannel)selectionkey.channel()).isOpen();
        }

        public void stopAccepting()
        {
            try
            {
                C.close();
            }
            catch(Throwable throwable) { }
            if(B.getProxy() != null)
                B.getProxy().A();
        }

        ServerSocketChannel C;
        ListeningInterface B;

        _B(ListeningInterface listeninginterface, ServerSocketChannel serversocketchannel)
        {
            super(listeninginterface.getContext());
            B = listeninginterface;
            C = serversocketchannel;
        }
    }

    class _D
        implements SelectorThreadImpl
    {

        public boolean processSelectionKey(SelectionKey selectionkey)
        {
            ClientConnector clientconnector = (ClientConnector)selectionkey.attachment();
            return clientconnector.finishConnect(selectionkey);
        }

        public String getName()
        {
            return C.getProduct() + "-CONNECT";
        }

        _D()
        {
            super();
        }
    }

    class _C
        implements SelectorThreadImpl
    {

        public boolean processSelectionKey(SelectionKey selectionkey)
        {
            SocketHandler sockethandler = (SocketHandler)selectionkey.attachment();
            boolean flag = false;
            if(selectionkey.isReadable())
                flag = sockethandler.processReadEvent();
            if(flag)
                return flag;
            if(selectionkey.isWritable())
                flag = sockethandler.processWriteEvent();
            return flag;
        }

        public String getName()
        {
            return C.getProduct() + "-TRANSFER";
        }

        _C()
        {
            super();
        }
    }

    class _A
        implements SelectorThreadImpl
    {

        public boolean processSelectionKey(SelectionKey selectionkey)
        {
            ClientAcceptor clientacceptor = (ClientAcceptor)selectionkey.attachment();
            return clientacceptor.finishAccept(selectionkey);
        }

        public String getName()
        {
            return C.getProduct() + "-ACCEPT";
        }

        _A()
        {
            super();
        }
    }


    public Daemon()
    {
        H = Collections.synchronizedMap(new HashMap());
        M = false;
        F = new ArrayList();
        K = false;
    }

    public DaemonContext getContext()
    {
        return C;
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
        return K;
    }

    public boolean startup()
        throws IOException
    {
        K = true;
        boolean flag1;
        D = new Thread() {

            public void run()
            {
                shutdown();
            }

            
            {
                super();
            }
        }
;
        EventLog.LogEvent(this, "Java version is " + System.getProperty("java.version"));
        EventLog.LogEvent(this, "OS is " + System.getProperty("os.name"));
        C = new DaemonContext(this);
        configure(C);
        int i = G.A();
        switch(i & 0x1f)
        {
        case 1: // '\001'
            throw new LicenseException("Your license has expired! visit http://www.sshtools.com to obtain an update version of the software.");

        case 2: // '\002'
            throw new LicenseException("Your license is invalid!");

        case 8: // '\b'
            throw new LicenseException("This version of SSHD Maverick requires a license. visit http://www.sshtools.com to register");

        case 16: // '\020'
            throw new LicenseException("Your subscription has expired! visit http://www.sshtools.com to purchase a subscription");

        default:
            throw new LicenseException("An unexpected license status was received.");

        case 4: // '\004'
            break;
        }
        if(Runtime.getRuntime() != null)
            Runtime.getRuntime().addShutdownHook(D);
        A = new SelectorThreadPool(new _D(), C.getPermanentConnectThreads(), C.getMaximumChannelsPerThread(), C.getIdleServiceRunPeriod(), C.getInactiveServiceRunsPerIdleEvent(), C.getSelectorProvider());
        J = new SelectorThreadPool(new _C(), C.getPermanentTransferThreads(), C.getMaximumChannelsPerThread(), C.getIdleServiceRunPeriod(), C.getInactiveServiceRunsPerIdleEvent(), C.getSelectorProvider());
        E = new SelectorThreadPool(new _A(), C.getPermanentAcceptThreads(), C.getMaximumChannelsPerThread(), C.getIdleServiceRunPeriod(), C.getInactiveServiceRunsPerIdleEvent(), C.getSelectorProvider());
        ListeningInterface alisteninginterface[] = C.getListeningInterfaces();
        int j = 0;
        for(int k = 0; k < alisteninginterface.length; k++)
            if(startListeningInterface(alisteninginterface[k]))
                j++;

        if(j != 0)
            break MISSING_BLOCK_LABEL_448;
        EventLog.LogEvent(this, "No listening interfaces were bound!");
        shutdown();
        flag1 = false;
        K = false;
        return flag1;
        L = true;
        flag1 = true;
        K = false;
        return flag1;
        Throwable throwable;
        throwable;
        boolean flag;
        EventLog.LogEvent(this, "The server failed to start", throwable);
        shutdown();
        if(throwable instanceof LicenseException)
            throw (IOException)throwable;
        flag = false;
        K = false;
        return flag;
        Exception exception;
        exception;
        K = false;
        throw exception;
    }

    protected boolean startListeningInterface(ListeningInterface listeninginterface)
        throws IOException
    {
        if(listeninginterface.isIPV6Interface() && System.getProperty("os.name").toUpperCase().startsWith("WINDOWS") && Boolean.getBoolean("maverick.windowsIpv6Workaround") || listeninginterface.isIPV6Interface() && Boolean.getBoolean("maverick.windowsIpv6WorkaroundForce"))
        {
            EventLog.LogEvent(this, "Detected IPv6 listener on Windows, need to workaround JDK bug 4640544 on interface " + listeninginterface.getAddressToBind().toString());
            if(!M)
            {
                InetSocketAddress inetsocketaddress = new InetSocketAddress(C.getIpv6WorkaroundBindAddress(), C.getIpv6WorkaroundPort());
                ListeningInterface listeninginterface1 = new ListeningInterface(inetsocketaddress, listeninginterface.getContext());
                if(!startListeningInterface(listeninginterface1))
                {
                    EventLog.LogEvent(this, "Could not start IPv6 workaround listener " + listeninginterface1.getAddressToBind().toString());
                    return false;
                }
                M = true;
            }
            try
            {
                A a = new A(listeninginterface.getAddressToBind().getHostName(), listeninginterface.getAddressToBind().getPort(), C.getIpv6WorkaroundBindAddress(), C.getIpv6WorkaroundPort());
                F.add(a);
                listeninginterface.setProxy(a);
            }
            catch(IOException ioexception)
            {
                EventLog.LogEvent(this, "Failed to bind to " + listeninginterface.getAddressToBind().toString(), ioexception);
                return false;
            }
            return true;
        }
        EventLog.LogEvent(this, "Binding server to " + listeninginterface.getAddressToBind().toString());
        ServerSocketChannel serversocketchannel = C.getSelectorProvider().openServerSocketChannel();
        serversocketchannel.configureBlocking(false);
        serversocketchannel.socket().setReceiveBufferSize(listeninginterface.getContext().getReceiveBufferSize());
        serversocketchannel.socket().setReuseAddress(listeninginterface.getContext().getSocketOptionReuseAddress());
        ServerSocket serversocket = serversocketchannel.socket();
        try
        {
            serversocket.bind(listeninginterface.getAddressToBind(), 50);
            _B _lb = new _B(listeninginterface, serversocketchannel);
            registerAcceptor(_lb, serversocketchannel);
            H.put(listeninginterface.getAddressToBind().toString(), _lb);
            return true;
        }
        catch(IOException ioexception1)
        {
            EventLog.LogEvent(this, "Failed to bind to " + listeninginterface.getAddressToBind().toString(), ioexception1);
        }
        C.removeListeningInterface(listeninginterface.getAddressToBind().toString());
        return false;
    }

    public void removeAcceptor(ListeningInterface listeninginterface)
    {
        EventLog.LogEvent(this, "Removing interface " + listeninginterface.getAddressToBind().toString());
        _B _lb = (_B)H.remove(listeninginterface.getAddressToBind().toString());
        if(_lb != null)
            _lb.stopAccepting();
    }

    public boolean isStarted()
    {
        return L;
    }

    public void shutdown()
    {
        ForwardingManager.A();
        if(M && F.size() > 0)
        {
            for(Iterator iterator = F.iterator(); iterator.hasNext(); ((A)iterator.next()).A());
            F.clear();
            M = false;
        }
        if(E != null)
            E.shutdown();
        if(A != null)
            A.shutdown();
        if(J != null)
            J.shutdown();
        try
        {
            if(Runtime.getRuntime() != null)
                Runtime.getRuntime().removeShutdownHook(D);
        }
        catch(IllegalStateException illegalstateexception) { }
        L = false;
        break MISSING_BLOCK_LABEL_149;
        Exception exception;
        exception;
        L = false;
        throw exception;
    }

    public void registerConnector(ClientConnector clientconnector, SocketChannel socketchannel)
        throws IOException
    {
        SelectorThread selectorthread = A.selectNextThread();
        selectorthread.register(socketchannel, 8, clientconnector, true);
    }

    public void registerAcceptor(ClientAcceptor clientacceptor, ServerSocketChannel serversocketchannel)
        throws IOException
    {
        SelectorThread selectorthread = E.selectNextThread();
        selectorthread.register(serversocketchannel, 16, clientacceptor, true);
    }

    public void registerHandler(SocketHandler sockethandler, SelectableChannel selectablechannel)
        throws IOException
    {
        SelectorThread selectorthread = J.selectNextThread();
        registerHandler(sockethandler, selectablechannel, selectorthread);
    }

    public void registerHandler(SocketHandler sockethandler, SelectableChannel selectablechannel, SelectorThread selectorthread)
        throws IOException
    {
        sockethandler.setThread(selectorthread);
        if(selectorthread == null)
        {
            throw new IOException("Unable to allocate thread");
        } else
        {
            selectorthread.register(selectablechannel, sockethandler.getInterestedOps(), sockethandler, true);
            return;
        }
    }

    protected abstract void configure(DaemonContext daemoncontext)
        throws IOException;

    DaemonContext C;
    SelectorThreadPool E;
    SelectorThreadPool A;
    SelectorThreadPool J;
    Map H;
    boolean M;
    Thread D;
    boolean L;
    List F;
    boolean K;
    static C G = new C();
    private static String I = "1.4.34";
    private static long B = 0L;

    static 
    {
        B = 0x13287e356c6L;
    }
}
