package socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class UserPasswordAuthenticator extends ServerAuthenticatorNone
{
  UserValidation h;

  public UserPasswordAuthenticator(UserValidation paramUserValidation)
  {
    this.h = paramUserValidation;
  }

  public ServerAuthenticator startSession(Socket paramSocket)
    throws IOException
  {
    InputStream localInputStream = paramSocket.getInputStream();
    OutputStream localOutputStream = paramSocket.getOutputStream();
    if (localInputStream.read() != 5)
      return null;
    if (!selectSocks5Authentication(localInputStream, localOutputStream, 2))
      return null;
    if (!b(paramSocket, localInputStream, localOutputStream))
      return null;
    return new ServerAuthenticatorNone(localInputStream, localOutputStream);
  }

  private boolean b(Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    if (i != 1)
      return false;
    int j = paramInputStream.read();
    if (j < 0)
      return false;
    byte[] arrayOfByte1 = new byte[j];
    paramInputStream.read(arrayOfByte1);
    int k = paramInputStream.read();
    if (k < 0)
      return false;
    byte[] arrayOfByte2 = new byte[k];
    paramInputStream.read(arrayOfByte2);
    if (this.h.isUserValid(new String(arrayOfByte1), new String(arrayOfByte2), paramSocket))
    {
      paramOutputStream.write(new byte[] { 1, 0 });
    }
    else
    {
      paramOutputStream.write(new byte[] { 1, 1 });
      return false;
    }
    return true;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.server.UserPasswordAuthenticator
 * JD-Core Version:    0.6.0
 */