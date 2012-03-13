package com.sshtools.net;

import com.maverick.ssh.SocketTimeoutSupport;
import com.maverick.ssh.SshTransport;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

public class SocketTransport extends Socket
  implements SshTransport, SocketTimeoutSupport
{
  String tc;

  public SocketTransport(String paramString, int paramInt)
    throws IOException
  {
    super(paramString, paramInt);
    this.tc = paramString;
    try
    {
      Method localMethod1 = Socket.class.getMethod("setSendBufferSize", new Class[] { Integer.TYPE });
      localMethod1.invoke(this, new Object[] { new Integer(65535) });
    }
    catch (Throwable localThrowable1)
    {
    }
    try
    {
      Method localMethod2 = Socket.class.getMethod("setReceiveBufferSize", new Class[] { Integer.TYPE });
      localMethod2.invoke(this, new Object[] { new Integer(65535) });
    }
    catch (Throwable localThrowable2)
    {
    }
  }

  public String getHost()
  {
    return this.tc;
  }

  public SshTransport duplicate()
    throws IOException
  {
    return new SocketTransport(getHost(), getPort());
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.SocketTransport
 * JD-Core Version:    0.6.0
 */