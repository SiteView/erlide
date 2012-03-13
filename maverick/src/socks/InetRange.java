package socks;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

public class InetRange
  implements Cloneable
{
  Hashtable e = new Hashtable();
  Vector b = new Vector();
  Vector d = new Vector();
  boolean c = true;

  public synchronized boolean add(String paramString)
  {
    if (paramString == null)
      return false;
    paramString = paramString.trim();
    if (paramString.length() == 0)
      return false;
    Object localObject;
    Object[] arrayOfObject;
    if (paramString.charAt(paramString.length() - 1) == '.')
    {
      localObject = b(paramString);
      long l2;
      long l1 = l2 = 0L;
      if (localObject == null)
        return false;
      for (int i = 0; i < 4; i++)
        if (localObject[i] >= 0)
        {
          l1 += (localObject[i] << 8 * (3 - i));
        }
        else
        {
          l2 = l1;
          while (i < 4)
            l2 += (255L << 8 * (3 - i++));
        }
      arrayOfObject = new Object[] { paramString, null, new Long(l1), new Long(l2) };
      this.b.addElement(arrayOfObject);
    }
    else if (paramString.charAt(0) == '.')
    {
      this.d.addElement(paramString);
      this.b.addElement(new Object[] { paramString, null, null, null });
    }
    else
    {
      localObject = new StringTokenizer(paramString, " \t\r\n\f:");
      if (((StringTokenizer)localObject).countTokens() > 1)
      {
        arrayOfObject = new Object[] { paramString, null, null, null };
        b(arrayOfObject, ((StringTokenizer)localObject).nextToken(), ((StringTokenizer)localObject).nextToken());
        this.b.addElement(arrayOfObject);
      }
      else
      {
        arrayOfObject = new Object[] { paramString, null, null, null };
        this.b.addElement(arrayOfObject);
        this.e.put(paramString, arrayOfObject);
        b(arrayOfObject);
      }
    }
    return true;
  }

  public synchronized void add(InetAddress paramInetAddress)
  {
    long l2;
    long l1 = l2 = b(paramInetAddress);
    this.b.addElement(new Object[] { paramInetAddress.getHostName(), paramInetAddress, new Long(l1), new Long(l2) });
  }

  public synchronized void add(InetAddress paramInetAddress1, InetAddress paramInetAddress2)
  {
    this.b.addElement(new Object[] { paramInetAddress1.getHostAddress() + ":" + paramInetAddress2.getHostAddress(), null, new Long(b(paramInetAddress1)), new Long(b(paramInetAddress2)) });
  }

  public synchronized boolean contains(String paramString)
  {
    return contains(paramString, true);
  }

  public synchronized boolean contains(String paramString, boolean paramBoolean)
  {
    if (this.b.size() == 0)
      return false;
    paramString = paramString.trim();
    if (paramString.length() == 0)
      return false;
    if (e(paramString))
      return true;
    if (d(paramString))
      return true;
    long l = c(paramString);
    if (l >= 0L)
      return b(l);
    if (!paramBoolean)
      return false;
    try
    {
      InetAddress localInetAddress = InetAddress.getByName(paramString);
      return contains(localInetAddress);
    }
    catch (UnknownHostException localUnknownHostException)
    {
    }
    return false;
  }

  public synchronized boolean contains(InetAddress paramInetAddress)
  {
    if (d(paramInetAddress.getHostName()))
      return true;
    if (e(paramInetAddress.getHostName()))
      return true;
    return b(b(paramInetAddress));
  }

  public synchronized String[] getAll()
  {
    int i = this.b.size();
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++)
    {
      Object[] arrayOfObject = (Object[])(Object[])this.b.elementAt(j);
      arrayOfString[j] = ((String)arrayOfObject[0]);
    }
    return arrayOfString;
  }

  public synchronized boolean remove(String paramString)
  {
    Enumeration localEnumeration = this.b.elements();
    while (localEnumeration.hasMoreElements())
    {
      Object[] arrayOfObject = (Object[])(Object[])localEnumeration.nextElement();
      if (paramString.equals(arrayOfObject[0]))
      {
        this.b.removeElement(arrayOfObject);
        this.d.removeElement(paramString);
        this.e.remove(paramString);
        return true;
      }
    }
    return false;
  }

  public String toString()
  {
    String[] arrayOfString = getAll();
    if (arrayOfString.length == 0)
      return "";
    String str = arrayOfString[0];
    for (int i = 1; i < arrayOfString.length; i++)
      str = str + "; " + arrayOfString[i];
    return str;
  }

  public Object clone()
  {
    InetRange localInetRange = new InetRange();
    localInetRange.b = ((Vector)this.b.clone());
    localInetRange.d = ((Vector)this.d.clone());
    localInetRange.e = ((Hashtable)this.e.clone());
    return localInetRange;
  }

  private synchronized boolean b(long paramLong)
  {
    Enumeration localEnumeration = this.b.elements();
    while (localEnumeration.hasMoreElements())
    {
      Object[] arrayOfObject = (Object[])(Object[])localEnumeration.nextElement();
      Long localLong1 = arrayOfObject[2] == null ? null : (Long)arrayOfObject[2];
      Long localLong2 = arrayOfObject[3] == null ? null : (Long)arrayOfObject[3];
      if ((localLong1 != null) && (localLong1.longValue() <= paramLong) && (localLong2.longValue() >= paramLong))
        return true;
    }
    return false;
  }

  private boolean e(String paramString)
  {
    return this.e.containsKey(paramString);
  }

  private boolean d(String paramString)
  {
    Enumeration localEnumeration = this.d.elements();
    while (localEnumeration.hasMoreElements())
      if (paramString.endsWith((String)localEnumeration.nextElement()))
        return true;
    return false;
  }

  private void b(Object[] paramArrayOfObject)
  {
    long l = c((String)paramArrayOfObject[0]);
    if (l >= 0L)
    {
       tmp29_26 = new Long(l);
      paramArrayOfObject[3] = tmp29_26;
      paramArrayOfObject[2] = tmp29_26;
    }
    else
    {
      d locald = new d(paramArrayOfObject);
      locald.b(this.c);
    }
  }

  private void b(Object[] paramArrayOfObject, String paramString1, String paramString2)
  {
    long l1;
    long l2;
    if (((l1 = c(paramString1)) >= 0L) && ((l2 = c(paramString2)) >= 0L))
    {
      paramArrayOfObject[2] = new Long(l1);
      paramArrayOfObject[3] = new Long(l2);
    }
    else
    {
      d locald = new d(paramArrayOfObject, paramString1, paramString2);
      locald.b(this.c);
    }
  }

  static long b(InetAddress paramInetAddress)
  {
    long l = 0L;
    byte[] arrayOfByte = paramInetAddress.getAddress();
    if (arrayOfByte.length == 4)
      for (int i = 0; i < 4; i++)
        l += ((arrayOfByte[i] & 0xFF) << 8 * (3 - i));
    else
      return 0L;
    return l;
  }

  long c(String paramString)
  {
    long l = 0L;
    if (!Character.isDigit(paramString.charAt(0)))
      return -1L;
    int[] arrayOfInt = b(paramString);
    if (arrayOfInt == null)
      return -1L;
    for (int i = 0; i < arrayOfInt.length; i++)
      l += ((arrayOfInt[i] >= 0 ? arrayOfInt[i] : 0) << 8 * (3 - i));
    return l;
  }

  static int[] b(String paramString)
  {
    int[] arrayOfInt = { -1, -1, -1, -1 };
    int i = 0;
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ".");
    if (localStringTokenizer.countTokens() > 4)
      return null;
    while (localStringTokenizer.hasMoreTokens())
      try
      {
        arrayOfInt[(i++)] = (Integer.parseInt(localStringTokenizer.nextToken()) & 0xFF);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return null;
      }
    return arrayOfInt;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.InetRange
 * JD-Core Version:    0.6.0
 */