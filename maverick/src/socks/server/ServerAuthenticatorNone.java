package socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.DatagramPacket;
import java.net.Socket;
import socks.ProxyMessage;
import socks.UDPEncapsulation;

public class ServerAuthenticatorNone
  implements ServerAuthenticator
{
  static final byte[] d = { 5, 0 };
  InputStream b;
  OutputStream c;

  public ServerAuthenticatorNone()
  {
    this.b = null;
    this.c = null;
  }

  public ServerAuthenticatorNone(InputStream paramInputStream, OutputStream paramOutputStream)
  {
    this.b = paramInputStream;
    this.c = paramOutputStream;
  }

  public ServerAuthenticator startSession(Socket paramSocket)
    throws IOException
  {
    PushbackInputStream localPushbackInputStream = new PushbackInputStream(paramSocket.getInputStream());
    OutputStream localOutputStream = paramSocket.getOutputStream();
    int i = localPushbackInputStream.read();
    if (i == 5)
    {
      if (!selectSocks5Authentication(localPushbackInputStream, localOutputStream, 0))
        return null;
    }
    else if (i == 4)
      localPushbackInputStream.unread(i);
    else
      return null;
    return new ServerAuthenticatorNone(localPushbackInputStream, localOutputStream);
  }

  public InputStream getInputStream()
  {
    return this.b;
  }

  public OutputStream getOutputStream()
  {
    return this.c;
  }

  public UDPEncapsulation getUdpEncapsulation()
  {
    return null;
  }

  public boolean checkRequest(ProxyMessage paramProxyMessage)
  {
    return true;
  }

  public boolean checkRequest(DatagramPacket paramDatagramPacket, boolean paramBoolean)
  {
    return true;
  }

  public void endSession()
  {
  }

  public static boolean selectSocks5Authentication(InputStream paramInputStream, OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    int i = paramInputStream.read();
    if (i <= 0)
      return false;
    byte[] arrayOfByte1 = new byte[i];
    byte[] arrayOfByte2 = new byte[2];
    int j = 0;
    arrayOfByte2[0] = 5;
    arrayOfByte2[1] = -1;
    int k = 0;
    while (k < i)
      k += paramInputStream.read(arrayOfByte1, k, i - k);
    for (int m = 0; m < i; m++)
    {
      if (arrayOfByte1[m] != paramInt)
        continue;
      j = 1;
      arrayOfByte2[1] = (byte)paramInt;
      break;
    }
    paramOutputStream.write(arrayOfByte2);
    return j;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.server.ServerAuthenticatorNone
 * JD-Core Version:    0.6.0
 */