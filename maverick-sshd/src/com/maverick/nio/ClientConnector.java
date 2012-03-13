package com.maverick.nio;

import java.nio.channels.SelectionKey;

public abstract interface ClientConnector extends SelectorRegistrationListener
{
  public abstract boolean finishConnect(SelectionKey paramSelectionKey);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.ClientConnector
 * JD-Core Version:    0.6.0
 */