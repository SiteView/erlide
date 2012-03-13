package com.maverick.events;

import java.util.Enumeration;
import java.util.Hashtable;

public class Event extends EventObject
{
  private final int C;
  private final boolean B;
  private final Hashtable A = new Hashtable();

  public Event(Object paramObject, int paramInt, boolean paramBoolean)
  {
    super(paramObject);
    this.C = paramInt;
    this.B = paramBoolean;
  }

  public int getId()
  {
    return this.C;
  }

  public boolean getState()
  {
    return this.B;
  }

  public Object getAttribute(String paramString)
  {
    return this.A.get(paramString);
  }

  public String getAllAttributes()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Enumeration localEnumeration = this.A.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str1 = (String)localEnumeration.nextElement();
      String str2 = this.A.get(str1).toString();
      localStringBuffer.append("|\r\n");
      localStringBuffer.append(str1);
      localStringBuffer.append(" = ");
      localStringBuffer.append(str2);
    }
    return localStringBuffer.toString();
  }

  public Event addAttribute(String paramString, Object paramObject)
  {
    this.A.put(paramString, paramObject == null ? "null" : paramObject);
    return this;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.events.Event
 * JD-Core Version:    0.6.0
 */