package com.sshtools.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

public abstract class HttpHeader
{
  protected static final String white_SPACE = " \t\r";
  Hashtable b = new Hashtable();
  protected String begin;

  protected String readLine(InputStream paramInputStream)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while (true)
    {
      int i = paramInputStream.read();
      if (i == -1)
        throw new IOException("Failed to read expected HTTP header line");
      if (i == 10)
        continue;
      if (i == 13)
        break;
      localStringBuffer.append((char)i);
    }
    return new String(localStringBuffer);
  }

  public String getStartLine()
  {
    return this.begin;
  }

  public Hashtable getHeaderFields()
  {
    return this.b;
  }

  public Enumeration getHeaderFieldNames()
  {
    return this.b.keys();
  }

  public String getHeaderField(String paramString)
  {
    Enumeration localEnumeration = this.b.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      if (str.equalsIgnoreCase(paramString))
        return (String)this.b.get(str);
    }
    return null;
  }

  public void setHeaderField(String paramString1, String paramString2)
  {
    this.b.put(paramString1, paramString2);
  }

  public String toString()
  {
    String str1 = this.begin + "\r\n";
    Enumeration localEnumeration = getHeaderFieldNames();
    while (localEnumeration.hasMoreElements())
    {
      String str2 = (String)localEnumeration.nextElement();
      str1 = str1 + str2 + ": " + getHeaderField(str2) + "\r\n";
    }
    str1 = str1 + "\r\n";
    return str1;
  }

  protected void processHeaderFields(InputStream paramInputStream)
    throws IOException
  {
    this.b = new Hashtable();
    StringBuffer localStringBuffer = new StringBuffer();
    String str1 = null;
    while (true)
    {
      i = paramInputStream.read();
      if (i == -1)
        throw new IOException("EOF returned from server but HTTP response is not complete!");
      if (i == 10)
        continue;
      if (i != 13)
      {
        localStringBuffer.append((char)i);
        continue;
      }
      if (localStringBuffer.length() == 0)
        break;
      String str2 = localStringBuffer.toString();
      str1 = b(str2, str1);
      localStringBuffer.setLength(0);
    }
    int i = paramInputStream.read();
  }

  private String b(String paramString1, String paramString2)
    throws IOException
  {
    int i = paramString1.charAt(0);
    String str1;
    String str2;
    if ((i == 32) || (i == 9))
    {
      str1 = paramString2;
      str2 = getHeaderField(paramString2) + " " + paramString1.trim();
    }
    else
    {
      int j = paramString1.indexOf(':');
      if (j == -1)
        throw new IOException("HTTP Header encoutered a corrupt field: '" + paramString1 + "'");
      str1 = paramString1.substring(0, j).toLowerCase();
      str2 = paramString1.substring(j + 1).trim();
    }
    setHeaderField(str1, str2);
    return str1;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.HttpHeader
 * JD-Core Version:    0.6.0
 */