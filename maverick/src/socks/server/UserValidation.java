package socks.server;

import java.net.Socket;

public abstract interface UserValidation
{
  public abstract boolean isUserValid(String paramString1, String paramString2, Socket paramSocket);
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.server.UserValidation
 * JD-Core Version:    0.6.0
 */