package com.maverick.nio;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public abstract interface SelectorRegistrationListener
{
  public abstract void registrationCompleted(SelectableChannel paramSelectableChannel, SelectionKey paramSelectionKey, SelectorThread paramSelectorThread);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.SelectorRegistrationListener
 * JD-Core Version:    0.6.0
 */