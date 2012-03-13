package com.maverick.nio;

import java.net.Inet6Address;
import java.net.InetSocketAddress;

public class ListeningInterface
{
  private InetSocketAddress C;
  private ProtocolContext B;
  private A A;

  public ListeningInterface(InetSocketAddress paramInetSocketAddress, ProtocolContext paramProtocolContext)
  {
    this.C = paramInetSocketAddress;
    this.B = paramProtocolContext;
  }

  public InetSocketAddress getAddressToBind()
  {
    return this.C;
  }

  public ProtocolContext getContext()
  {
    return this.B;
  }

  public boolean isIPV6Interface()
  {
    return this.C.getAddress() instanceof Inet6Address;
  }

  public void setProxy(A paramA)
  {
    this.A = paramA;
  }

  public A getProxy()
  {
    return this.A;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.ListeningInterface
 * JD-Core Version:    0.6.0
 */