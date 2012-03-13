package com.maverick.sshd.events;

import com.maverick.events.Event;
import com.maverick.events.EventListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SSHDLoggingListener
  implements EventListener
{
  String D = "";
  Map B = Collections.synchronizedMap(new HashMap());
  Log C = LogFactory.getLog(SSHDLoggingListener.class);
  boolean A = false;

  public void setProduct(String paramString)
  {
    this.D = paramString;
  }

  public String getProduct()
  {
    return this.D;
  }

  public SSHDLoggingListener()
  {
  }

  public SSHDLoggingListener(boolean paramBoolean)
  {
    this.A = paramBoolean;
  }

  public void setIgnoreLogEvents(boolean paramBoolean)
  {
    this.A = paramBoolean;
  }

  public void processEvent(Event paramEvent)
  {
    if ((paramEvent.getId() == 110) || (paramEvent.getId() == 111) || (paramEvent.getId() == 112))
    {
      if (!this.A)
      {
        Class localClass = paramEvent.getSource().getClass();
        if (!this.B.containsKey(localClass))
          this.B.put(localClass, LogFactory.getLog(localClass));
        switch (paramEvent.getId())
        {
        case 110:
          ((Log)this.B.get(localClass)).info(this.D + paramEvent.getAttribute("LOG_MESSAGE"));
          break;
        case 111:
          ((Log)this.B.get(localClass)).debug(this.D + paramEvent.getAttribute("LOG_MESSAGE"));
          break;
        case 112:
          ((Log)this.B.get(localClass)).info(this.D + paramEvent.getAttribute("LOG_MESSAGE"), (Throwable)paramEvent.getAttribute("THROWABLE"));
        }
      }
    }
    else
      this.C.info(this.D + SSHDEventMessages.messageCodes.get(new Integer(paramEvent.getId())) + paramEvent.getAllAttributes() + "\r\n" + (paramEvent.getState() ? "SUCCESS" : "FAILURE"));
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.events.SSHDLoggingListener
 * JD-Core Version:    0.6.0
 */