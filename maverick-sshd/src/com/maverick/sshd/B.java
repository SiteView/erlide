package com.maverick.sshd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class B extends ChannelEventAdapter
{
  List B = Collections.synchronizedList(new ArrayList());
  boolean A = false;

  public void A()
  {
    synchronized (this.B)
    {
      this.A = true;
      Iterator localIterator = this.B.iterator();
      while (localIterator.hasNext())
      {
        Channel localChannel = (Channel)localIterator.next();
        try
        {
          localChannel.close();
        }
        catch (Throwable localThrowable)
        {
        }
        localIterator.remove();
      }
    }
  }

  public void onChannelOpen(Channel paramChannel)
  {
    synchronized (this.B)
    {
      if (!this.A)
        this.B.add(paramChannel);
    }
  }

  public void onChannelClose(Channel paramChannel)
  {
    synchronized (this.B)
    {
      if (!this.A)
        this.B.remove(paramChannel);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.B
 * JD-Core Version:    0.6.0
 */