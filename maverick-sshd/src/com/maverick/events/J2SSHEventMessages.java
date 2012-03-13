package com.maverick.events;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;

public final class J2SSHEventMessages
{
  public static Hashtable messageCodes = new Hashtable();
  public static Hashtable messageAttributes = new Hashtable();

  static
  {
    Class localClass = J2SSHEventCodes.class;
    Field[] arrayOfField = localClass.getFields();
    for (int i = 0; i < arrayOfField.length; i++)
    {
      int j = arrayOfField[i].getModifiers();
      if ((!Modifier.isFinal(j)) || (!Modifier.isStatic(j)))
        continue;
      try
      {
        String str = arrayOfField[i].getName();
        if (str.startsWith("EVENT_"))
          messageCodes.put(arrayOfField[i].get(null), str.substring(6));
        else
          messageAttributes.put(arrayOfField[i].get(null), str.substring(10));
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        localIllegalArgumentException.printStackTrace();
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        localIllegalAccessException.printStackTrace();
      }
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.events.J2SSHEventMessages
 * JD-Core Version:    0.6.0
 */