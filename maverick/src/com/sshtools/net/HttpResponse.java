package com.sshtools.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class HttpResponse extends HttpHeader
{
  private String d;
  private int c;
  private String e;

  public HttpResponse(InputStream paramInputStream)
    throws IOException
  {
    for (this.begin = readLine(paramInputStream); this.begin.trim().length() == 0; this.begin = readLine(paramInputStream));
    b();
    processHeaderFields(paramInputStream);
  }

  public String getVersion()
  {
    return this.d;
  }

  public int getStatus()
  {
    return this.c;
  }

  public String getReason()
  {
    return this.e;
  }

  private void b()
    throws IOException
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(this.begin, " \t\r", false);
    try
    {
      this.d = localStringTokenizer.nextToken();
      this.c = Integer.parseInt(localStringTokenizer.nextToken());
      this.e = localStringTokenizer.nextToken();
    }
    catch (NoSuchElementException localNoSuchElementException)
    {
      throw new IOException("Failed to read HTTP repsonse header");
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new IOException("Failed to read HTTP resposne header");
    }
  }

  public String getAuthenticationMethod()
  {
    String str1 = getHeaderField("Proxy-Authenticate");
    String str2 = null;
    if (str1 != null)
    {
      int i = str1.indexOf(' ');
      str2 = str1.substring(0, i);
    }
    return str2;
  }

  public String getAuthenticationRealm()
  {
    String str1 = getHeaderField("Proxy-Authenticate");
    String str2 = "";
    if (str1 != null)
    {
      int j = str1.indexOf('=');
      while (j >= 0)
      {
        int i = str1.lastIndexOf(' ', j);
        if (i <= -1)
          continue;
        String str3 = str1.substring(i + 1, j);
        if (str3.equalsIgnoreCase("realm"))
        {
          i = j + 2;
          j = str1.indexOf('"', i);
          str2 = str1.substring(i, j);
          break;
        }
        j = str1.indexOf('=', j + 1);
      }
    }
    return str2;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.HttpResponse
 * JD-Core Version:    0.6.0
 */