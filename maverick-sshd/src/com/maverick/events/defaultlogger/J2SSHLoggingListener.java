package com.maverick.events.defaultlogger;

import com.maverick.events.Event;
import com.maverick.events.EventListener;
import com.maverick.events.J2SSHEventMessages;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class J2SSHLoggingListener
  implements EventListener
{
  String H = "J2SSH:";
  Hashtable F = new Hashtable();
  Log G = LogFactory.getLog(J2SSHLoggingListener.class);
  boolean E = false;

  public void setProduct(String paramString)
  {
    this.H = paramString;
  }

  public String getProduct()
  {
    return this.H;
  }

  public J2SSHLoggingListener()
  {
  }

  public J2SSHLoggingListener(boolean paramBoolean)
  {
    setIgnoreLogEvents(paramBoolean);
  }

  public void setIgnoreLogEvents(boolean paramBoolean)
  {
    this.E = paramBoolean;
  }

  public void processEvent(Event paramEvent)
  {
    if (((paramEvent.getId() == 110) || (paramEvent.getId() == 111) || (paramEvent.getId() == 112)) && (!this.E))
    {
      Class localClass = paramEvent.getSource().getClass();
      if (!this.F.containsKey(localClass))
        this.F.put(localClass, LogFactory.getLog(localClass));
      switch (paramEvent.getId())
      {
      case 110:
        ((Log)this.F.get(localClass)).info(this.H + paramEvent.getAttribute("LOG_MESSAGE"));
        break;
      case 111:
        ((Log)this.F.get(localClass)).debug(this.H + paramEvent.getAttribute("LOG_MESSAGE"));
        break;
      case 112:
        ((Log)this.F.get(localClass)).info(this.H + paramEvent.getAttribute("LOG_MESSAGE"), (Throwable)paramEvent.getAttribute("THROWABLE"));
      }
    }
    else
    {
      this.G.info(this.H + J2SSHEventMessages.messageCodes.get(new Integer(paramEvent.getId())) + paramEvent.getAllAttributes());
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.events.defaultlogger.J2SSHLoggingListener
 * JD-Core Version:    0.6.0
 */