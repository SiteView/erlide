package com.maverick.ssh.message;

public abstract interface MessageObserver
{
  public abstract boolean wantsNotification(Message paramMessage);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.message.MessageObserver
 * JD-Core Version:    0.6.0
 */