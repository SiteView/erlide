package com.maverick.ssh;

public abstract interface SshClient extends Client
{
  public abstract void connect(SshTransport paramSshTransport, SshContext paramSshContext, SshConnector paramSshConnector, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws SshException;

  public abstract int authenticate(SshAuthentication paramSshAuthentication)
    throws SshException;

  public abstract SshSession openSessionChannel()
    throws SshException, ChannelOpenException;

  public abstract SshSession openSessionChannel(long paramLong)
    throws SshException, ChannelOpenException;

  public abstract SshSession openSessionChannel(ChannelEventListener paramChannelEventListener)
    throws SshException, ChannelOpenException;

  public abstract SshSession openSessionChannel(ChannelEventListener paramChannelEventListener, long paramLong)
    throws SshException, ChannelOpenException;

  public abstract SshTunnel openForwardingChannel(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, int paramInt3, SshTransport paramSshTransport, ChannelEventListener paramChannelEventListener)
    throws SshException, ChannelOpenException;

  public abstract SshClient openRemoteClient(String paramString1, int paramInt, String paramString2, SshConnector paramSshConnector)
    throws SshException, ChannelOpenException;

  public abstract SshClient openRemoteClient(String paramString1, int paramInt, String paramString2)
    throws SshException, ChannelOpenException;

  public abstract boolean requestRemoteForwarding(String paramString1, int paramInt1, String paramString2, int paramInt2, ForwardingRequestListener paramForwardingRequestListener)
    throws SshException;

  public abstract boolean cancelRemoteForwarding(String paramString, int paramInt)
    throws SshException;

  public abstract void disconnect();

  public abstract boolean isAuthenticated();

  public abstract boolean isConnected();

  public abstract String getRemoteIdentification();

  public abstract String getUsername();

  public abstract SshClient duplicate()
    throws SshException;

  public abstract SshContext getContext();

  public abstract int getChannelCount();

  public abstract int getVersion();

  public abstract boolean isBuffered();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshClient
 * JD-Core Version:    0.6.0
 */