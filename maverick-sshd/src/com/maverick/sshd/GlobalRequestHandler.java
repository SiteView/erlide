package com.maverick.sshd;

import com.maverick.ssh2.GlobalRequest;

public abstract interface GlobalRequestHandler
{
  public abstract boolean processGlobalRequest(GlobalRequest paramGlobalRequest, ConnectionProtocol paramConnectionProtocol);

  public abstract String[] supportedRequests();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.GlobalRequestHandler
 * JD-Core Version:    0.6.0
 */