package com.maverick.sshd;

import com.maverick.events.EventService;
import com.maverick.nio.EventLog;
import com.maverick.nio.ForwardingManager;
import com.maverick.sshd.events.EventServiceImplementation;
import com.maverick.sshd.events.SSHDEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RemoteForwardingManager extends ForwardingManager
{
  public synchronized void registerConnection(ConnectionProtocol paramConnectionProtocol, Class paramClass)
  {
    this.forwardingFactories.put(paramConnectionProtocol, paramClass);
  }

  public synchronized void unregisterConnection(ConnectionProtocol paramConnectionProtocol)
  {
    if (this.listeningPorts != null)
    {
      this.forwardingFactories.remove(paramConnectionProtocol);
      Iterator localIterator = this.listeningPorts.keySet().iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        RemoteForwardingFactory localRemoteForwardingFactory = (RemoteForwardingFactory)this.listeningPorts.get(localObject);
        if (localRemoteForwardingFactory.belongsTo(paramConnectionProtocol))
        {
          localRemoteForwardingFactory.stopListening(true);
          localIterator.remove();
        }
      }
    }
  }

  public synchronized boolean isListening(int paramInt)
  {
    return this.listeningPorts.containsKey(new Integer(paramInt));
  }

  public synchronized RemoteForwardingFactory getRemoteForwardingFactory(int paramInt)
    throws IOException
  {
    if (!isListening(paramInt))
      throw new IOException("Remote forwarding not listening on port " + paramInt);
    return (RemoteForwardingFactory)this.listeningPorts.get(new Integer(paramInt));
  }

  public synchronized boolean startRemoteForwarding(String paramString, int paramInt, ConnectionProtocol paramConnectionProtocol)
  {
    Integer localInteger = new Integer(paramInt);
    if (this.listeningPorts.containsKey(localInteger))
      return false;
    String str = paramString + ":" + String.valueOf(paramInt);
    try
    {
      Class localClass = (Class)this.forwardingFactories.get(paramConnectionProtocol);
      RemoteForwardingFactory localRemoteForwardingFactory = (RemoteForwardingFactory)localClass.newInstance();
      localRemoteForwardingFactory.bindInterface(paramString, paramInt, paramConnectionProtocol);
      this.listeningPorts.put(localInteger, localRemoteForwardingFactory);
      EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 17, true).addAttribute("FORWARDING_TUNNEL_ENTRANCE", paramString + ":" + paramInt));
      return true;
    }
    catch (IOException localIOException)
    {
    }
    catch (Throwable localThrowable)
    {
      EventLog.LogEvent(this, "Could not instantiate remote forwarding channel factory", localThrowable);
    }
    return false;
  }

  public synchronized boolean stopRemoteForwarding(String paramString, int paramInt, boolean paramBoolean, ConnectionProtocol paramConnectionProtocol)
  {
    String str = paramString + ":" + String.valueOf(paramInt);
    Integer localInteger = new Integer(paramInt);
    if (this.listeningPorts.containsKey(localInteger))
    {
      RemoteForwardingFactory localRemoteForwardingFactory = (RemoteForwardingFactory)this.listeningPorts.get(localInteger);
      if (localRemoteForwardingFactory.belongsTo(paramConnectionProtocol))
      {
        localRemoteForwardingFactory.stopListening(paramBoolean);
        this.listeningPorts.remove(localInteger);
        EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 19, true).addAttribute("FORWARDING_TUNNEL_ENTRANCE", paramString + ":" + paramInt));
      }
      return true;
    }
    return false;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.RemoteForwardingManager
 * JD-Core Version:    0.6.0
 */