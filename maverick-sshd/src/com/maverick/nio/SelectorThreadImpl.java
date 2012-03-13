package com.maverick.nio;

import java.nio.channels.SelectionKey;

public abstract interface SelectorThreadImpl
{
  public abstract boolean processSelectionKey(SelectionKey paramSelectionKey);

  public abstract String getName();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.SelectorThreadImpl
 * JD-Core Version:    0.6.0
 */