package socks;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import socks.server.ServerAuthenticator;

class c
  implements Runnable
{
  DatagramSocket n;
  DatagramSocket m;
  Socket b;
  int i;
  InetAddress j;
  Thread e;
  Thread d;
  Thread c;
  ServerAuthenticator f;
  long g;
  static PrintStream h = null;
  static Proxy k = null;
  static int l = 65535;
  static int o = 180000;

  public c(InetAddress paramInetAddress, int paramInt, Thread paramThread, Socket paramSocket, ServerAuthenticator paramServerAuthenticator)
    throws IOException
  {
    this.c = paramThread;
    this.b = paramSocket;
    this.f = paramServerAuthenticator;
    this.n = new Socks5DatagramSocket(true, paramServerAuthenticator.getUdpEncapsulation(), paramInetAddress, paramInt);
    this.i = this.n.getLocalPort();
    this.j = this.n.getLocalAddress();
    if (this.j.getHostAddress().equals("0.0.0.0"))
      this.j = InetAddress.getLocalHost();
    if (k == null)
      this.m = new DatagramSocket();
    else
      this.m = new Socks5DatagramSocket(k, 0, null);
  }

  public static void c(int paramInt)
  {
    o = paramInt;
  }

  public static void b(int paramInt)
  {
    l = paramInt;
  }

  public void d()
    throws IOException
  {
    this.m.setSoTimeout(o);
    this.n.setSoTimeout(o);
    b("Starting UDP relay server on " + this.j + ":" + this.i);
    b("Remote socket " + this.m.getLocalAddress() + ":" + this.m.getLocalPort());
    this.e = new Thread(this, "pipe1");
    this.d = new Thread(this, "pipe2");
    this.g = System.currentTimeMillis();
    this.e.start();
    this.d.start();
  }

  public synchronized void b()
  {
    this.c = null;
    this.b = null;
    c();
  }

  public void run()
  {
    try
    {
      if (Thread.currentThread().getName().equals("pipe1"))
        b(this.m, this.n, false);
      else
        b(this.n, this.m, true);
    }
    catch (IOException localIOException)
    {
    }
    finally
    {
      c();
      b("UDP Pipe thread " + Thread.currentThread().getName() + " stopped.");
    }
  }

  private synchronized void c()
  {
    if (this.e == null)
      return;
    b("Aborting UDP Relay Server");
    this.m.close();
    this.n.close();
    if (this.b != null)
      try
      {
        this.b.close();
      }
      catch (IOException localIOException)
      {
      }
    if (this.c != null)
      this.c.interrupt();
    this.e.interrupt();
    this.d.interrupt();
    this.e = null;
  }

  private static void b(String paramString)
  {
    if (h != null)
    {
      h.println(paramString);
      h.flush();
    }
  }

  private void b(DatagramSocket paramDatagramSocket1, DatagramSocket paramDatagramSocket2, boolean paramBoolean)
    throws IOException
  {
    byte[] arrayOfByte = new byte[l];
    DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
    while (true)
    {
      try
      {
        paramDatagramSocket1.receive(localDatagramPacket);
        this.g = System.currentTimeMillis();
        if (!this.f.checkRequest(localDatagramPacket, paramBoolean))
          continue;
        paramDatagramSocket2.send(localDatagramPacket);
      }
      catch (UnknownHostException localUnknownHostException)
      {
        b("Dropping datagram for unknown host");
      }
      catch (InterruptedIOException localInterruptedIOException)
      {
        if (o == 0)
          return;
        long l1 = System.currentTimeMillis() - this.g;
        if (l1 >= o - 100)
          return;
      }
      localDatagramPacket.setLength(arrayOfByte.length);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.c
 * JD-Core Version:    0.6.0
 */