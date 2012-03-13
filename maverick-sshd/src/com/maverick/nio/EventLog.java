package com.maverick.nio;

import com.maverick.events.EventService;

public class EventLog
{
  public static void LogEvent(Object paramObject, String paramString)
  {
    EventServiceImplementation.getInstance().fireEvent(new Event(paramObject, 110, true).addAttribute("LOG_MESSAGE", paramString));
  }

  public static void LogErrorEvent(Object paramObject, String paramString)
  {
    EventServiceImplementation.getInstance().fireEvent(new Event(paramObject, 113, true).addAttribute("LOG_MESSAGE", paramString));
  }

  public static void LogDebugEvent(Object paramObject, String paramString)
  {
    EventServiceImplementation.getInstance().fireEvent(new Event(paramObject, 111, true).addAttribute("LOG_MESSAGE", paramString));
  }

  public static void LogEvent(Object paramObject, String paramString, Throwable paramThrowable)
  {
    EventServiceImplementation.getInstance().fireEvent(new Event(paramObject, 112, true).addAttribute("LOG_MESSAGE", paramString).addAttribute("THROWABLE", paramThrowable));
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.EventLog
 * JD-Core Version:    0.6.0
 */