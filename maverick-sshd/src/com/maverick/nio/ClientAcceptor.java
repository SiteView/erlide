package com.maverick.nio;

import java.nio.channels.SelectionKey;

public abstract class ClientAcceptor
{
  ProtocolContext A;

  public ClientAcceptor(ProtocolContext paramProtocolContext)
  {
    this.A = paramProtocolContext;
  }

  public boolean finishAccept(SelectionKey paramSelectionKey)
  {
    return finishAccept(paramSelectionKey, this.A);
  }

  public abstract boolean finishAccept(SelectionKey paramSelectionKey, ProtocolContext paramProtocolContext);

  public abstract void stopAccepting();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.ClientAcceptor
 * JD-Core Version:    0.6.0
 */