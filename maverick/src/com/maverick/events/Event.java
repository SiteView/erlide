package com.maverick.events;

import java.util.Enumeration;
import java.util.Hashtable;

public class Event extends EventObject
{
  private final int e;
  private final boolean d;
  private final Hashtable c = new Hashtable();
  private Throwable b;

  public Event(Object paramObject, int paramInt, boolean paramBoolean)
  {
    super(paramObject);
    this.e = paramInt;
    this.d = paramBoolean;
  }

  public Event(Object paramObject, int paramInt, Throwable paramThrowable)
  {
    super(paramObject);
    this.e = paramInt;
    this.d = false;
    this.b = paramThrowable;
  }

  public Throwable getError()
  {
    return this.b;
  }

  public int getId()
  {
    return this.e;
  }

  public boolean getState()
  {
    return this.d;
  }

  public Object getAttribute(String paramString)
  {
    return this.c.get(paramString);
  }

  public String getAllAttributes()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Enumeration localEnumeration = this.c.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str1 = (String)localEnumeration.nextElement();
      String str2 = this.c.get(str1).toString();
      localStringBuffer.append("|\r\n");
      localStringBuffer.append(str1);
      localStringBuffer.append(" = ");
      localStringBuffer.append(str2);
    }
    return localStringBuffer.toString();
  }

  public Event addAttribute(String paramString, Object paramObject)
  {
    this.c.put(paramString, paramObject == null ? "null" : paramObject);
    return this;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.events.Event
 * JD-Core Version:    0.6.0
 */