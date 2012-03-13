package com.sshtools.net;

import com.maverick.ssh.SshTransport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HttpProxyTransportWrapper extends SocketWrapper
{
  private String vc;
  private int zc;
  private String uc;
  private int yc;
  private HttpResponse bd;
  private String ad;
  private String wc;
  private String xc;

  private HttpProxyTransportWrapper(String paramString1, int paramInt1, String paramString2, int paramInt2)
    throws IOException, UnknownHostException
  {
    super(new Socket(paramString2, paramInt2));
    this.vc = paramString2;
    this.zc = paramInt2;
    this.uc = paramString1;
    this.yc = paramInt1;
  }

  public static HttpProxyTransportWrapper connectViaProxy(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, String paramString4, String paramString5)
    throws IOException, UnknownHostException
  {
    HttpProxyTransportWrapper localHttpProxyTransportWrapper = new HttpProxyTransportWrapper(paramString1, paramInt1, paramString2, paramInt2);
    localHttpProxyTransportWrapper.ad = paramString3;
    localHttpProxyTransportWrapper.wc = paramString4;
    localHttpProxyTransportWrapper.xc = paramString5;
    int i;
    try
    {
      InputStream localInputStream = localHttpProxyTransportWrapper.getInputStream();
      OutputStream localOutputStream = localHttpProxyTransportWrapper.getOutputStream();
      HttpRequest localHttpRequest = new HttpRequest();
      localHttpRequest.setHeaderBegin("CONNECT " + paramString1 + ":" + paramInt1 + " HTTP/1.0");
      localHttpRequest.setHeaderField("User-Agent", paramString5);
      localHttpRequest.setHeaderField("Pragma", "No-Cache");
      localHttpRequest.setHeaderField("Host", paramString1);
      localHttpRequest.setHeaderField("Proxy-Connection", "Keep-Alive");
      localOutputStream.write(localHttpRequest.toString().getBytes());
      localOutputStream.flush();
      localHttpProxyTransportWrapper.bd = new HttpResponse(localInputStream);
      if (localHttpProxyTransportWrapper.bd.getStatus() == 407)
      {
        String str1 = localHttpProxyTransportWrapper.bd.getAuthenticationRealm();
        String str2 = localHttpProxyTransportWrapper.bd.getAuthenticationMethod();
        if (str1 == null)
          str1 = "";
        if (str2.equalsIgnoreCase("basic"))
        {
          localHttpProxyTransportWrapper.close();
          localHttpProxyTransportWrapper = new HttpProxyTransportWrapper(paramString1, paramInt1, paramString2, paramInt2);
          localInputStream = localHttpProxyTransportWrapper.getInputStream();
          localOutputStream = localHttpProxyTransportWrapper.getOutputStream();
          localHttpRequest.setBasicAuthentication(paramString3, paramString4);
          localOutputStream.write(localHttpRequest.toString().getBytes());
          localOutputStream.flush();
          localHttpProxyTransportWrapper.bd = new HttpResponse(localInputStream);
        }
        else
        {
          if (str2.equalsIgnoreCase("digest"))
            throw new IOException("Digest authentication is not supported");
          throw new IOException("'" + str2 + "' is not supported");
        }
      }
      i = localHttpProxyTransportWrapper.bd.getStatus();
    }
    catch (SocketException localSocketException)
    {
      throw new SocketException("Error communicating with proxy server " + paramString2 + ":" + paramInt2 + " (" + localSocketException.getMessage() + ")");
    }
    if ((i < 200) || (i > 299))
      throw new IOException("Proxy tunnel setup failed: " + localHttpProxyTransportWrapper.bd.getStartLine());
    return localHttpProxyTransportWrapper;
  }

  public String toString()
  {
    return "HTTPProxySocket [Proxy IP=" + this.socket.getInetAddress() + ",Proxy Port=" + getPort() + ",localport=" + this.socket.getLocalPort() + "Remote Host=" + this.uc + "Remote Port=" + String.valueOf(this.yc) + "]";
  }

  public String getHost()
  {
    return this.uc;
  }

  public SshTransport duplicate()
    throws IOException
  {
    return connectViaProxy(this.uc, this.yc, this.vc, this.zc, this.ad, this.wc, this.xc);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.HttpProxyTransportWrapper
 * JD-Core Version:    0.6.0
 */