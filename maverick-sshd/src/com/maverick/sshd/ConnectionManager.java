package com.maverick.sshd;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager
{
  private static ConnectionManager A;
  private Map C = Collections.synchronizedMap(new HashMap());
  private Map B = Collections.synchronizedMap(new HashMap());

  public static ConnectionManager getInstance()
  {
    return A == null ? (ConnectionManager.A = new ConnectionManager()) : A;
  }

  synchronized void A(ConnectionProtocol paramConnectionProtocol)
  {
    this.C.put(new String(paramConnectionProtocol.getSessionIdentifier()), paramConnectionProtocol);
  }

  synchronized void B(ConnectionProtocol paramConnectionProtocol)
  {
    this.C.remove(new String(paramConnectionProtocol.getSessionIdentifier()));
  }

  public synchronized ConnectionProtocol getConnection(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (this.C.containsKey(new String(paramArrayOfByte))))
      return (ConnectionProtocol)this.C.get(new String(paramArrayOfByte));
    return null;
  }

  public synchronized ConnectionProtocol[] getConnections()
  {
    return (ConnectionProtocol[])(ConnectionProtocol[])this.C.values().toArray(new ConnectionProtocol[0]);
  }

  synchronized void B(TransportProtocol paramTransportProtocol)
  {
    this.B.put(new String(paramTransportProtocol.getSessionIdentifier()), paramTransportProtocol);
  }

  synchronized void A(TransportProtocol paramTransportProtocol)
  {
    this.B.remove(new String(paramTransportProtocol.getSessionIdentifier()));
  }

  public synchronized TransportProtocol getTransport(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (this.B.containsKey(new String(paramArrayOfByte))))
      return (TransportProtocol)this.B.get(new String(paramArrayOfByte));
    return null;
  }

  public synchronized TransportProtocol[] getTransports()
  {
    return (TransportProtocol[])(TransportProtocol[])this.B.values().toArray(new TransportProtocol[0]);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.ConnectionManager
 * JD-Core Version:    0.6.0
 */