package com.maverick.nio;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

class A extends Thread
{
  ServerSocket D = null;
  String C;
  int E;
  String A;
  int B;

  public A(String paramString1, int paramInt1, String paramString2, int paramInt2)
    throws IOException
  {
    this.C = paramString1;
    this.E = paramInt1;
    this.A = paramString2;
    this.B = paramInt2;
    this.D = new ServerSocket();
    this.D.bind(new InetSocketAddress(InetAddress.getByName(paramString1), paramInt1), 50);
    start();
  }

  public void A()
  {
    try
    {
      this.D.close();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void run()
  {
    try
    {
      Socket localSocket1 = null;
      while ((localSocket1 = this.D.accept()) != null)
        try
        {
          Socket localSocket2 = new Socket(this.A, this.B);
          B localB1 = new B(localSocket1, localSocket2);
          localB1.start();
          B localB2 = new B(localSocket2, localSocket1);
          localB2.start();
        }
        catch (UnknownHostException localUnknownHostException)
        {
          System.err.println("Error: Unknown Host " + this.A);
        }
        catch (IOException localIOException)
        {
        }
    }
    catch (Throwable localThrowable)
    {
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.A
 * JD-Core Version:    0.6.0
 */