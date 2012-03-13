package com.maverick.sshd.scp;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class A
{
  private static A A = null;

  private static A A()
  {
    return A;
  }

  private static void A(A paramA)
  {
    A = paramA;
  }

  public static A B()
  {
    if (A() == null)
      A(new A());
    return A();
  }

  public String[] A(String paramString1, String paramString2)
  {
    return A(paramString1, paramString2, true);
  }

  protected String[] A(String paramString1, String paramString2, boolean paramBoolean)
  {
    ArrayList localArrayList = null;
    StringTokenizer localStringTokenizer = null;
    if (paramString1 == null)
      return null;
    if ((paramString2 == null) || (paramString2.length() == 0))
    {
      String[] arrayOfString = { paramString1 };
      return arrayOfString;
    }
    if (paramString1.length() == 0)
      return new String[0];
    localArrayList = new ArrayList();
    localStringTokenizer = new StringTokenizer(paramString1, paramString2, paramBoolean);
    if (paramBoolean)
      A(localArrayList, localStringTokenizer, paramString2);
    else
      A(localArrayList, localStringTokenizer);
    return (String[])(String[])localArrayList.toArray(new String[0]);
  }

  protected void A(List paramList, StringTokenizer paramStringTokenizer)
  {
    while (paramStringTokenizer.hasMoreTokens())
      paramList.add(paramStringTokenizer.nextToken());
  }

  protected void A(List paramList, StringTokenizer paramStringTokenizer, String paramString)
  {
    int i = 0;
    while (paramStringTokenizer.hasMoreTokens())
    {
      String str = paramStringTokenizer.nextToken();
      if (paramString.indexOf(str) >= 0)
      {
        if (i != 0)
          paramList.add("");
        i = 1;
        continue;
      }
      paramList.add(str);
      i = 0;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.scp.A
 * JD-Core Version:    0.6.0
 */