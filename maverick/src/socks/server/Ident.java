package socks.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.StringTokenizer;

public class Ident
{
  public String errorMessage;
  public String hostType;
  public String userName;
  public boolean successful;
  public int errorCode;
  public static final int ERR_NO_CONNECT = 1;
  public static final int ERR_TIMEOUT = 2;
  public static final int ERR_PROTOCOL = 3;
  public static final int ERR_PROTOCOL_INCORRECT = 4;
  public static final int connectionTimeout = 10000;

  public Ident(Socket paramSocket)
  {
    Socket localSocket = null;
    this.successful = false;
    try
    {
      localSocket = new Socket(paramSocket.getInetAddress(), 113);
      localSocket.setSoTimeout(10000);
      byte[] arrayOfByte = ("" + paramSocket.getPort() + " , " + paramSocket.getLocalPort() + "\r\n").getBytes();
      localSocket.getOutputStream().write(arrayOfByte);
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
      b(localBufferedReader.readLine());
    }
    catch (InterruptedIOException localIOException2)
    {
      this.errorCode = 2;
      this.errorMessage = "Connection to identd timed out.";
    }
    catch (ConnectException localIOException3)
    {
      this.errorCode = 1;
      this.errorMessage = "Connection to identd server failed.";
    }
    catch (IOException localIOException5)
    {
      this.errorCode = 1;
      this.errorMessage = ("" + localIOException4);
    }
    finally
    {
      try
      {
        if (localSocket != null)
          localSocket.close();
      }
      catch (IOException localIOException6)
      {
      }
    }
  }

  private void b(String paramString)
  {
    if (paramString == null)
    {
      this.errorCode = 4;
      this.errorMessage = "Identd server closed connection.";
      return;
    }
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ":");
    if (localStringTokenizer.countTokens() < 3)
    {
      this.errorCode = 4;
      this.errorMessage = "Can't parse server response.";
      return;
    }
    localStringTokenizer.nextToken();
    String str = localStringTokenizer.nextToken().trim().toUpperCase();
    if ((str.equals("USERID")) && (localStringTokenizer.countTokens() >= 2))
    {
      this.successful = true;
      this.hostType = localStringTokenizer.nextToken().trim();
      this.userName = localStringTokenizer.nextToken("").substring(1);
    }
    else if (str.equals("ERROR"))
    {
      this.errorCode = 3;
      this.errorMessage = localStringTokenizer.nextToken();
    }
    else
    {
      this.errorCode = 4;
      System.out.println("Opa!");
      this.errorMessage = "Can't parse server response.";
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.server.Ident
 * JD-Core Version:    0.6.0
 */