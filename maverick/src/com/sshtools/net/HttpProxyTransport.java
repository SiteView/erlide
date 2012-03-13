package com.sshtools.net;

import com.maverick.ssh.SshTransport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;

public class HttpProxyTransport extends Socket
  implements SshTransport
{
  private String id;
  private int ld;
  private String ed;
  private int hd;
  private HttpResponse cd;
  private String gd;
  private String jd;
  private String kd;
  private HttpRequest fd = new HttpRequest();
  private Hashtable dd;

  private HttpProxyTransport(String paramString1, int paramInt1, String paramString2, int paramInt2)
    throws IOException, UnknownHostException
  {
    super(paramString2, paramInt2);
    this.id = paramString2;
    this.ld = paramInt2;
    this.ed = paramString1;
    this.hd = paramInt1;
  }

  public static HttpProxyTransport connectViaProxy(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, String paramString4, String paramString5)
    throws IOException, UnknownHostException
  {
    return connectViaProxy(paramString1, paramInt1, paramString2, paramInt2, paramString3, paramString4, paramString5, null);
  }

  public static HttpProxyTransport connectViaProxy(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, String paramString4, String paramString5, Hashtable paramHashtable)
    throws IOException, UnknownHostException
  {
    HttpProxyTransport localHttpProxyTransport = new HttpProxyTransport(paramString1, paramInt1, paramString2, paramInt2);
    localHttpProxyTransport.gd = paramString3;
    localHttpProxyTransport.jd = paramString4;
    localHttpProxyTransport.kd = paramString5;
    localHttpProxyTransport.dd = paramHashtable;
    int i;
    try
    {
      InputStream localInputStream = localHttpProxyTransport.getInputStream();
      OutputStream localOutputStream = localHttpProxyTransport.getOutputStream();
      localHttpProxyTransport.fd.setHeaderBegin("CONNECT " + paramString1 + ":" + paramInt1 + " HTTP/1.0");
      localHttpProxyTransport.fd.setHeaderField("User-Agent", paramString5);
      localHttpProxyTransport.fd.setHeaderField("Pragma", "No-Cache");
      localHttpProxyTransport.fd.setHeaderField("Host", paramString1);
      localHttpProxyTransport.fd.setHeaderField("Proxy-Connection", "Keep-Alive");
      Object localObject;
      String str;
      if (paramHashtable != null)
      {
        localObject = paramHashtable.keys();
        while (((Enumeration)localObject).hasMoreElements())
        {
          str = (String)((Enumeration)localObject).nextElement();
          localHttpProxyTransport.fd.setHeaderField(str, (String)paramHashtable.get(str));
        }
      }
      localOutputStream.write(localHttpProxyTransport.fd.toString().getBytes());
      localOutputStream.flush();
      localHttpProxyTransport.cd = new HttpResponse(localInputStream);
      if (localHttpProxyTransport.cd.getStatus() == 407)
      {
        localObject = localHttpProxyTransport.cd.getAuthenticationRealm();
        str = localHttpProxyTransport.cd.getAuthenticationMethod();
        if (localObject == null)
          localObject = "";
        if (str.equalsIgnoreCase("basic"))
        {
          localHttpProxyTransport.close();
          localHttpProxyTransport = new HttpProxyTransport(paramString1, paramInt1, paramString2, paramInt2);
          localInputStream = localHttpProxyTransport.getInputStream();
          localOutputStream = localHttpProxyTransport.getOutputStream();
          localHttpProxyTransport.fd.setBasicAuthentication(paramString3, paramString4);
          localOutputStream.write(localHttpProxyTransport.fd.toString().getBytes());
          localOutputStream.flush();
          localHttpProxyTransport.cd = new HttpResponse(localInputStream);
        }
        else
        {
          if (str.equalsIgnoreCase("digest"))
            throw new IOException("Digest authentication is not supported");
          throw new IOException("'" + str + "' is not supported");
        }
      }
      i = localHttpProxyTransport.cd.getStatus();
    }
    catch (SocketException localSocketException)
    {
      throw new SocketException("Error communicating with proxy server " + paramString2 + ":" + paramInt2 + " (" + localSocketException.getMessage() + ")");
    }
    if ((i < 200) || (i > 299))
      throw new IOException("Proxy tunnel setup failed: " + localHttpProxyTransport.cd.getStartLine());
    return (HttpProxyTransport)localHttpProxyTransport;
  }

  public String toString()
  {
    return "HTTPProxySocket [Proxy IP=" + getInetAddress() + ",Proxy Port=" + getPort() + ",localport=" + getLocalPort() + "Remote Host=" + this.ed + "Remote Port=" + String.valueOf(this.hd) + "]";
  }

  public String getHost()
  {
    return this.ed;
  }

  public SshTransport duplicate()
    throws IOException
  {
    return connectViaProxy(this.ed, this.hd, this.id, this.ld, this.gd, this.jd, this.kd, this.dd);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.net.HttpProxyTransport
 * JD-Core Version:    0.6.0
 */