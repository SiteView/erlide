package com.maverick.ssh;

public abstract interface ForwardingRequestListener
{
  public abstract SshTransport createConnection(String paramString, int paramInt)
    throws SshException;

  public abstract void initializeTunnel(SshTunnel paramSshTunnel);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.ForwardingRequestListener
 * JD-Core Version:    0.6.0
 */