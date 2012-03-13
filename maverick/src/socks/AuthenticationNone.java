package socks;

import java.io.IOException;
import java.net.Socket;

public class AuthenticationNone
  implements Authentication
{
  public Object[] doSocksAuthentication(int paramInt, Socket paramSocket)
    throws IOException
  {
    if (paramInt != 0)
      return null;
    return new Object[] { paramSocket.getInputStream(), paramSocket.getOutputStream() };
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.AuthenticationNone
 * JD-Core Version:    0.6.0
 */