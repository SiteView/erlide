package com.maverick.ssh;

public abstract interface SshTunnel extends SshChannel, SshTransport
{
  public abstract int getPort();

  public abstract String getListeningAddress();

  public abstract int getListeningPort();

  public abstract String getOriginatingHost();

  public abstract int getOriginatingPort();

  public abstract boolean isLocal();

  public abstract boolean isX11();

  public abstract SshTransport getTransport();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshTunnel
 * JD-Core Version:    0.6.0
 */