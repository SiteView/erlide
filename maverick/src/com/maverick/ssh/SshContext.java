package com.maverick.ssh;

public abstract interface SshContext
{
  public abstract void setChannelLimit(int paramInt);

  public abstract int getChannelLimit();

  public abstract void setHostKeyVerification(HostKeyVerification paramHostKeyVerification);

  public abstract HostKeyVerification getHostKeyVerification();

  public abstract void setSFTPProvider(String paramString);

  public abstract String getSFTPProvider();

  public abstract void setX11Display(String paramString);

  public abstract String getX11Display();

  public abstract byte[] getX11AuthenticationCookie()
    throws SshException;

  public abstract void setX11RealCookie(byte[] paramArrayOfByte);

  public abstract byte[] getX11RealCookie()
    throws SshException;

  public abstract void setX11RequestListener(ForwardingRequestListener paramForwardingRequestListener);

  public abstract ForwardingRequestListener getX11RequestListener();

  public abstract void setMessageTimeout(int paramInt);

  public abstract int getMessageTimeout();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshContext
 * JD-Core Version:    0.6.0
 */