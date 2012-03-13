package socks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class UserPasswordAuthentication
  implements Authentication
{
  public static final int METHOD_ID = 2;
  String d;
  String b;
  byte[] c;

  public UserPasswordAuthentication(String paramString1, String paramString2)
  {
    this.d = paramString1;
    this.b = paramString2;
    b();
  }

  public String getUser()
  {
    return this.d;
  }

  public String getPassword()
  {
    return this.b;
  }

  public Object[] doSocksAuthentication(int paramInt, Socket paramSocket)
    throws IOException
  {
    if (paramInt != 2)
      return null;
    InputStream localInputStream = paramSocket.getInputStream();
    OutputStream localOutputStream = paramSocket.getOutputStream();
    localOutputStream.write(this.c);
    int i = localInputStream.read();
    if (i < 0)
      return null;
    int j = localInputStream.read();
    if (j != 0)
      return null;
    return new Object[] { localInputStream, localOutputStream };
  }

  private void b()
  {
    byte[] arrayOfByte1 = this.d.getBytes();
    byte[] arrayOfByte2 = this.b.getBytes();
    this.c = new byte[3 + arrayOfByte1.length + arrayOfByte2.length];
    this.c[0] = 1;
    this.c[1] = (byte)arrayOfByte1.length;
    System.arraycopy(arrayOfByte1, 0, this.c, 2, arrayOfByte1.length);
    this.c[(2 + arrayOfByte1.length)] = (byte)arrayOfByte2.length;
    System.arraycopy(arrayOfByte2, 0, this.c, 3 + arrayOfByte1.length, arrayOfByte2.length);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.UserPasswordAuthentication
 * JD-Core Version:    0.6.0
 */