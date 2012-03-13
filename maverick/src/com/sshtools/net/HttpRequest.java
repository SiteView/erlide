package com.sshtools.net;

import com.maverick.util.Base64;

public class HttpRequest extends HttpHeader
{
  public void setHeaderBegin(String paramString)
  {
    this.begin = paramString;
  }

  public void setBasicAuthentication(String paramString1, String paramString2)
  {
    String str = paramString1 + ":" + paramString2;
    setHeaderField("Proxy-Authorization", "Basic " + Base64.encodeBytes(str.getBytes(), true));
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.HttpRequest
 * JD-Core Version:    0.6.0
 */