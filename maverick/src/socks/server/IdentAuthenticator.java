package socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import socks.InetRange;
import socks.ProxyMessage;

public class IdentAuthenticator extends ServerAuthenticatorNone
{
  Vector f;
  Vector g;
  String e;

  public IdentAuthenticator()
  {
    this.f = new Vector();
    this.g = new Vector();
  }

  IdentAuthenticator(InputStream paramInputStream, OutputStream paramOutputStream, String paramString)
  {
    super(paramInputStream, paramOutputStream);
    this.e = paramString;
  }

  public synchronized void add(InetRange paramInetRange, Hashtable paramHashtable)
  {
    this.f.addElement(paramInetRange);
    this.g.addElement(paramHashtable);
  }

  public ServerAuthenticator startSession(Socket paramSocket)
    throws IOException
  {
    int i = b(paramSocket.getInetAddress());
    String str = null;
    if (i < 0)
      return null;
    ServerAuthenticatorNone localServerAuthenticatorNone = (ServerAuthenticatorNone)super.startSession(paramSocket);
    if (localServerAuthenticatorNone == null)
      return null;
    Hashtable localHashtable = (Hashtable)this.g.elementAt(i);
    if (localHashtable != null)
    {
      Ident localIdent = new Ident(paramSocket);
      if (!localIdent.successful)
        return null;
      if (!localHashtable.containsKey(localIdent.userName))
        return null;
      str = localIdent.userName;
    }
    return new IdentAuthenticator(localServerAuthenticatorNone.b, localServerAuthenticatorNone.c, str);
  }

  public boolean checkRequest(ProxyMessage paramProxyMessage, Socket paramSocket)
  {
    if ((paramProxyMessage.version == 5) || (this.e == null))
      return true;
    if (paramProxyMessage.version != 4)
      return false;
    return this.e.equals(paramProxyMessage.user);
  }

  public String toString()
  {
    String str = "";
    for (int i = 0; i < this.f.size(); i++)
      str = str + "Range:" + this.f.elementAt(i) + "\nUsers:" + b(i) + "\n";
    return str;
  }

  private int b(InetAddress paramInetAddress)
  {
    int i = 0;
    Enumeration localEnumeration = this.f.elements();
    while (localEnumeration.hasMoreElements())
    {
      InetRange localInetRange = (InetRange)localEnumeration.nextElement();
      if (localInetRange.contains(paramInetAddress))
        return i;
      i++;
    }
    return -1;
  }

  private String b(int paramInt)
  {
    if (this.g.elementAt(paramInt) == null)
      return "Everybody is permitted.";
    Enumeration localEnumeration = ((Hashtable)this.g.elementAt(paramInt)).keys();
    if (!localEnumeration.hasMoreElements())
      return "";
    for (String str = localEnumeration.nextElement().toString(); localEnumeration.hasMoreElements(); str = str + "; " + localEnumeration.nextElement());
    return str;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.server.IdentAuthenticator
 * JD-Core Version:    0.6.0
 */