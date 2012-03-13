package com.maverick.ssh2;

import com.maverick.ssh.SshException;

public abstract interface GlobalRequestHandler
{
  public abstract String[] supportedRequests();

  public abstract boolean processGlobalRequest(GlobalRequest paramGlobalRequest)
    throws SshException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.GlobalRequestHandler
 * JD-Core Version:    0.6.0
 */