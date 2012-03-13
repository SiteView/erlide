package com.maverick.events;

public abstract interface EventService
{
  public abstract void addListener(String paramString, EventListener paramEventListener);

  public abstract void removeListener(String paramString);

  public abstract void fireEvent(Event paramEvent);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.events.EventService
 * JD-Core Version:    0.6.0
 */