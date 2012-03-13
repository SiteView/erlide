package com.maverick.nio;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ForwardingManager
{
  private static Map A = Collections.synchronizedMap(new HashMap());
  protected Map forwardingFactories = Collections.synchronizedMap(new HashMap());
  protected Map listeningPorts = Collections.synchronizedMap(new HashMap());

  static void A()
  {
    A.clear();
  }

  public static ForwardingManager getInstance(Class paramClass)
    throws IOException
  {
    try
    {
      if (!A.containsKey(paramClass))
        A.put(paramClass, paramClass.newInstance());
      return (ForwardingManager)A.get(paramClass);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new IOException("ForwardingManager implementation failed to instatiate: " + localThrowable.getMessage());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.ForwardingManager
 * JD-Core Version:    0.6.0
 */