package com.maverick.events;

public class EventObject
{
  protected transient Object source;

  public EventObject(Object paramObject)
  {
    if (paramObject == null)
      throw new IllegalArgumentException("null source");
    this.source = paramObject;
  }

  public Object getSource()
  {
    return this.source;
  }

  public String toString()
  {
    return getClass().getName() + "[source=" + this.source + "]";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.events.EventObject
 * JD-Core Version:    0.6.0
 */