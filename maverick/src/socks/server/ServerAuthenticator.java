package socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.Socket;
import socks.ProxyMessage;
import socks.UDPEncapsulation;

public abstract interface ServerAuthenticator
{
  public abstract ServerAuthenticator startSession(Socket paramSocket)
    throws IOException;

  public abstract InputStream getInputStream();

  public abstract OutputStream getOutputStream();

  public abstract UDPEncapsulation getUdpEncapsulation();

  public abstract boolean checkRequest(ProxyMessage paramProxyMessage);

  public abstract boolean checkRequest(DatagramPacket paramDatagramPacket, boolean paramBoolean);

  public abstract void endSession();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.server.ServerAuthenticator
 * JD-Core Version:    0.6.0
 */