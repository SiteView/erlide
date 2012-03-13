package socks;

import java.net.InetAddress;
import java.net.UnknownHostException;

class d
  implements Runnable
{
  Object[] b;
  String d;
  String c;

  d(Object[] paramArrayOfObject)
  {
    this.b = paramArrayOfObject;
    this.d = (this.c = null);
  }

  d(Object[] paramArrayOfObject, String paramString1, String paramString2)
  {
    this.b = paramArrayOfObject;
    this.d = paramString1;
    this.c = paramString2;
  }

  public final void b(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      Thread localThread = new Thread(this);
      localThread.start();
    }
    else
    {
      run();
    }
  }

  public void run()
  {
    try
    {
      InetAddress localInetAddress;
      Object localObject;
      if (this.d == null)
      {
        localInetAddress = InetAddress.getByName((String)this.b[0]);
        this.b[1] = localInetAddress;
        localObject = new Long(InetRange.b(localInetAddress));
        Object tmp50_49 = localObject;
        this.b[3] = tmp50_49;
        this.b[2] = tmp50_49;
      }
      else
      {
        localInetAddress = InetAddress.getByName(this.d);
        localObject = InetAddress.getByName(this.c);
        this.b[2] = new Long(InetRange.b(localInetAddress));
        this.b[3] = new Long(InetRange.b((InetAddress)localObject));
      }
    }
    catch (UnknownHostException localUnknownHostException)
    {
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.d
 * JD-Core Version:    0.6.0
 */