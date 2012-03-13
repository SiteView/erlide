package socks;

import java.io.IOException;
import java.net.Socket;

public abstract interface Authentication
{
  public abstract Object[] doSocksAuthentication(int paramInt, Socket paramSocket)
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.Authentication
 * JD-Core Version:    0.6.0
 */