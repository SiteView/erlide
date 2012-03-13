package com.maverick.events;

public abstract interface EventService
{
  public abstract void addListener(String paramString, EventListener paramEventListener);

  public abstract void removeListener(String paramString);

  public abstract void fireEvent(Event paramEvent);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.events.EventService
 * JD-Core Version:    0.6.0
 */