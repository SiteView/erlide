package com.sshtools.net;

import com.maverick.ssh.SshTunnel;

public abstract interface ForwardingClientListener
{
  public static final int LOCAL_FORWARDING = 1;
  public static final int REMOTE_FORWARDING = 2;
  public static final int X11_FORWARDING = 3;

  public abstract void forwardingStarted(int paramInt1, String paramString1, String paramString2, int paramInt2);

  public abstract void forwardingStopped(int paramInt1, String paramString1, String paramString2, int paramInt2);

  public abstract void channelFailure(int paramInt1, String paramString1, String paramString2, int paramInt2, boolean paramBoolean, Throwable paramThrowable);

  public abstract void channelOpened(int paramInt, String paramString, SshTunnel paramSshTunnel);

  public abstract void channelClosed(int paramInt, String paramString, SshTunnel paramSshTunnel);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.ForwardingClientListener
 * JD-Core Version:    0.6.0
 */