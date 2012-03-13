package com.maverick.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class B extends Thread
{
  Socket A;
  Socket B;

  B(Socket paramSocket1, Socket paramSocket2)
  {
    this.A = paramSocket1;
    this.B = paramSocket2;
  }

  public void run()
  {
    byte[] arrayOfByte = new byte[32768];
    try
    {
      OutputStream localOutputStream = this.B.getOutputStream();
      InputStream localInputStream = this.A.getInputStream();
      int i;
      while ((i = localInputStream.read(arrayOfByte, 0, arrayOfByte.length)) > -1)
        localOutputStream.write(arrayOfByte, 0, i);
      this.A.close();
      this.B.close();
    }
    catch (IOException localIOException)
    {
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.B
 * JD-Core Version:    0.6.0
 */