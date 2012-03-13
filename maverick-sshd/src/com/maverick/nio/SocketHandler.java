package com.maverick.nio;

public abstract interface SocketHandler extends SelectorRegistrationListener
{
  public abstract void initialize(ProtocolEngine paramProtocolEngine, Daemon paramDaemon);

  public abstract boolean processReadEvent();

  public abstract boolean processWriteEvent();

  public abstract int getInterestedOps();

  public abstract void setThread(SelectorThread paramSelectorThread);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.SocketHandler
 * JD-Core Version:    0.6.0
 */