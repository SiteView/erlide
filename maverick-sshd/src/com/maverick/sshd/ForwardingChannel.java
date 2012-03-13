package com.maverick.sshd;

public abstract class ForwardingChannel extends Channel
{
  protected String hostToConnect;
  protected int portToConnect;
  protected String originatingHost;
  protected int originatingPort;

  public ForwardingChannel(String paramString, int paramInt1, int paramInt2)
  {
    super(paramString, paramInt1, paramInt2);
  }

  public String getHost()
  {
    return this.hostToConnect;
  }

  public int getPort()
  {
    return this.portToConnect;
  }

  public String getOriginatingHost()
  {
    return this.originatingHost;
  }

  public int getOriginatingPort()
  {
    return this.originatingPort;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.ForwardingChannel
 * JD-Core Version:    0.6.0
 */