package com.maverick.events;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class EventServiceImplementation
  implements EventService
{
  private static final EventService B = new EventServiceImplementation();
  private final Hashtable C = new Hashtable();
  private Vector A = new Vector();

  public static EventService getInstance()
  {
    return B;
  }

  public synchronized void addListener(String paramString, EventListener paramEventListener)
  {
    if (paramString.trim().equals(""))
      this.A.addElement(paramEventListener);
    else
      this.C.put(paramString.trim(), paramEventListener);
  }

  public synchronized void removeListener(String paramString)
  {
    this.C.remove(paramString);
  }

  public synchronized void fireEvent(Event paramEvent)
  {
    if (paramEvent == null)
      return;
    Object localObject1 = this.A.elements();
    Object localObject3;
    while (((Enumeration)localObject1).hasMoreElements())
    {
      localObject2 = ((Enumeration)localObject1).nextElement();
      localObject3 = (EventListener)localObject2;
      try
      {
        ((EventListener)localObject3).processEvent(paramEvent);
      }
      catch (Throwable localThrowable1)
      {
      }
    }
    localObject1 = Thread.currentThread().getName();
    Object localObject2 = this.C.keys();
    while (((Enumeration)localObject2).hasMoreElements())
    {
      localObject3 = (String)((Enumeration)localObject2).nextElement();
      try
      {
        String str = "";
        if (((String)localObject1).indexOf('-') > -1)
        {
          str = ((String)localObject1).substring(0, ((String)localObject1).indexOf('-'));
          if (((String)localObject3).startsWith(str))
          {
            EventListener localEventListener = (EventListener)this.C.get(localObject3);
            localEventListener.processEvent(paramEvent);
          }
        }
      }
      catch (Throwable localThrowable2)
      {
      }
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.events.EventServiceImplementation
 * JD-Core Version:    0.6.0
 */