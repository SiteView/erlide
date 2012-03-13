package com.maverick.ssh.components;

import com.maverick.ssh.SshException;
import com.maverick.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class ComponentFactory
  implements Cloneable
{
  protected Hashtable supported = new Hashtable();
  protected Vector order = new Vector();
  Class A;

  public synchronized String changePositionofAlgorithm(String paramString, int paramInt)
    throws SshException
  {
    if (paramInt < 0)
      throw new SshException("index out of bounds", 4);
    if (paramInt >= this.order.size())
      paramInt = this.order.size();
    int i = this.order.indexOf(paramString);
    if (i < paramInt)
    {
      this.order.insertElementAt(paramString, paramInt);
      this.order.removeElementAt(i);
    }
    else
    {
      this.order.removeElementAt(i);
      this.order.insertElementAt(paramString, paramInt);
    }
    return (String)this.order.elementAt(0);
  }

  public synchronized String createNewOrdering(int[] paramArrayOfInt)
    throws SshException
  {
    if (paramArrayOfInt.length > this.order.size())
      throw new SshException("too many indicies", 4);
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      if ((paramArrayOfInt[i] < 0) || (paramArrayOfInt[i] >= this.order.size()))
        throw new SshException("index out of bounds", 4);
      this.order.insertElementAt(this.order.elementAt(paramArrayOfInt[i]), this.order.size());
    }
    Arrays.sort(paramArrayOfInt);
    for (i = paramArrayOfInt.length - 1; i >= 0; i--)
      this.order.removeElementAt(paramArrayOfInt[i]);
    for (i = 0; i < paramArrayOfInt.length; i++)
    {
      Object localObject = this.order.elementAt(this.order.size() - 1);
      this.order.removeElementAt(this.order.size() - 1);
      this.order.insertElementAt(localObject, 0);
    }
    return (String)this.order.elementAt(0);
  }

  public ComponentFactory(Class paramClass)
  {
    this.A = paramClass;
  }

  public boolean contains(String paramString)
  {
    return this.supported.containsKey(paramString);
  }

  public synchronized String list(String paramString)
  {
    return A(paramString);
  }

  public synchronized void add(String paramString, Class paramClass)
  {
    this.supported.put(paramString, paramClass);
    if (!this.order.contains(paramString))
      this.order.addElement(paramString);
  }

  public Object getInstance(String paramString)
    throws SshException
  {
    if (this.supported.containsKey(paramString))
      try
      {
        return createInstance(paramString, (Class)this.supported.get(paramString));
      }
      catch (Throwable localThrowable)
      {
        throw new SshException(localThrowable.getMessage(), 5);
      }
    throw new SshException(paramString + " is not supported", 7);
  }

  protected Object createInstance(String paramString, Class paramClass)
    throws Throwable
  {
    return paramClass.newInstance();
  }

  private synchronized String A(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = this.order.indexOf(paramString);
    if (i != -1)
      localStringBuffer.append(paramString);
    for (int j = 0; j < this.order.size(); j++)
    {
      if (i == j)
        continue;
      localStringBuffer.append("," + (String)this.order.elementAt(j));
    }
    if ((i == -1) && (localStringBuffer.length() > 0))
      return localStringBuffer.toString().substring(1);
    return localStringBuffer.toString();
  }

  public synchronized void remove(String paramString)
  {
    this.supported.remove(paramString);
    this.order.removeElement(paramString);
  }

  public synchronized void clear()
  {
    this.supported.clear();
    this.order.removeAllElements();
  }

  public Object clone()
  {
    ComponentFactory localComponentFactory = new ComponentFactory(this.A);
    localComponentFactory.order = ((Vector)this.order.clone());
    localComponentFactory.supported = ((Hashtable)this.supported.clone());
    return localComponentFactory;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.ComponentFactory
 * JD-Core Version:    0.6.0
 */