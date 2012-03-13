package com.maverick.nio;

import com.maverick.events.EventService;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaemonContext
{
  String M = "SSHD";
  Daemon D;
  int I = 1;
  int E = 1;
  int L = 2;
  int J = 1000;
  int N = 1;
  int G = 1;
  boolean B = true;
  int O = 65535;
  Map A = Collections.synchronizedMap(new HashMap());
  int H = 60022;
  String F = "127.0.0.1";
  SelectorProvider C = SelectorProvider.provider();
  ByteBufferPool K = null;

  public void setProduct(String paramString)
  {
    if (paramString.indexOf('-') > -1)
      throw new IllegalArgumentException("Use of - in product name is invalid");
    this.M = paramString;
  }

  public String getProduct()
  {
    return this.M;
  }

  DaemonContext(Daemon paramDaemon)
  {
    this.D = paramDaemon;
  }

  public synchronized ByteBufferPool getBufferPool()
  {
    if (this.K == null)
      this.K = new ByteBufferPool(this.O, this.B);
    return this.K;
  }

  public void setSelectorProvider(SelectorProvider paramSelectorProvider)
  {
    this.C = paramSelectorProvider;
  }

  public SelectorProvider getSelectorProvider()
  {
    return this.C;
  }

  public Daemon getServer()
  {
    return this.D;
  }

  public boolean isUsingDirectBuffers()
  {
    return this.B;
  }

  public void setUsingDirectBuffers(boolean paramBoolean)
  {
    this.B = paramBoolean;
  }

  public void setBufferPoolArraySize(int paramInt)
  {
    if (paramInt < 35000)
      throw new IllegalArgumentException("The buffer pool must have an array size of at least 35000 bytes (the maximum packet size supported)");
    this.O = paramInt;
  }

  public void addListeningInterface(String paramString, int paramInt, ProtocolContext paramProtocolContext)
    throws IOException
  {
    addListeningInterface(InetAddress.getByName(paramString), paramInt, paramProtocolContext);
  }

  public void addListeningInterface(InetAddress paramInetAddress, int paramInt, ProtocolContext paramProtocolContext)
    throws IOException
  {
    InetSocketAddress localInetSocketAddress = new InetSocketAddress(paramInetAddress, paramInt);
    ListeningInterface localListeningInterface = new ListeningInterface(localInetSocketAddress, paramProtocolContext);
    this.A.put(localInetSocketAddress.toString(), localListeningInterface);
    if ((this.D.isStarted()) && (!this.D.isStarting()))
      this.D.startListeningInterface(localListeningInterface);
  }

  public void removeListeningInterface(InetAddress paramInetAddress, int paramInt)
  {
    InetSocketAddress localInetSocketAddress = new InetSocketAddress(paramInetAddress, paramInt);
    ListeningInterface localListeningInterface = (ListeningInterface)this.A.remove(localInetSocketAddress.toString());
    if (localListeningInterface != null)
    {
      if (localListeningInterface.getProxy() != null)
      {
        localListeningInterface.getProxy().A();
        this.D.F.remove(localListeningInterface.getProxy());
      }
      this.D.removeAcceptor(localListeningInterface);
    }
  }

  public void removeListeningInterface(String paramString, int paramInt)
    throws UnknownHostException
  {
    removeListeningInterface(InetAddress.getByName(paramString), paramInt);
  }

  public void removeListeningInterface(String paramString)
    throws IOException
  {
    this.A.remove(paramString);
  }

  public ListeningInterface[] getListeningInterfaces()
  {
    return (ListeningInterface[])(ListeningInterface[])this.A.values().toArray(new ListeningInterface[this.A.size()]);
  }

  public int getPermanentAcceptThreads()
  {
    return this.I;
  }

  public void setPermanentAcceptThreads(int paramInt)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("There must be at least one permanent ACCEPT thread");
    this.I = paramInt;
  }

  public int getPermanentConnectThreads()
  {
    return this.E;
  }

  public void setPermanentConnectThreads(int paramInt)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("There must be at least one permanent CONNECT thread");
    this.E = paramInt;
  }

  public int getPermanentTransferThreads()
  {
    return this.L;
  }

  public void setPermanentTransferThreads(int paramInt)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("There must be at least one permanent TRANSFER thread");
    this.L = paramInt;
  }

  public int getMaximumChannelsPerThread()
  {
    return this.J;
  }

  public void setMaximumChannelsPerThread(int paramInt)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("You must have at least 1 selector per thread");
    this.J = paramInt;
  }

  public int getIdleServiceRunPeriod()
  {
    return this.N;
  }

  public void setIdleServiceRunPeriod(int paramInt)
  {
    this.N = paramInt;
  }

  public int getInactiveServiceRunsPerIdleEvent()
  {
    return this.G;
  }

  public void setInactiveServiceRunsPerIdleEvent(int paramInt)
  {
    this.G = paramInt;
  }

  public static void addEventListener(com.maverick.events.EventListener paramEventListener)
  {
    EventServiceImplementation.getInstance().addListener("", paramEventListener);
  }

  public static void addEventListener(String paramString, com.maverick.events.EventListener paramEventListener)
  {
    EventServiceImplementation.getInstance().addListener(paramString, paramEventListener);
  }

  public static void addEventListener(EventListener paramEventListener)
  {
    EventServiceImplementation.getInstance().addListener("", paramEventListener);
  }

  public static void removeEventListener(String paramString)
  {
    EventServiceImplementation.getInstance().removeListener(paramString);
  }

  public int getIpv6WorkaroundPort()
  {
    return this.H;
  }

  public void setIpv6WorkaroundPort(int paramInt)
  {
    this.H = paramInt;
  }

  public String getIpv6WorkaroundBindAddress()
  {
    return this.F;
  }

  public void setIpv6WorkaroundBindAddress(String paramString)
  {
    this.F = paramString;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.DaemonContext
 * JD-Core Version:    0.6.0
 */