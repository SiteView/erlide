package com.maverick.ssh2;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.events.EventServiceImplementation;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshTransport;
import com.maverick.ssh.components.SshKeyExchangeClient;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;

public class AuthenticationProtocol
{
  public static final int SSH_MSG_USERAUTH_REQUEST = 50;
  TransportProtocol d;
  BannerDisplay c;
  int b = 2;
  public static final String SERVICE_NAME = "ssh-userauth";

  public SshKeyExchangeClient getKeyExchange()
  {
    return this.d.getKeyExchange();
  }

  public AuthenticationProtocol(TransportProtocol paramTransportProtocol)
    throws SshException
  {
    this.d = paramTransportProtocol;
    paramTransportProtocol.startService("ssh-userauth");
  }

  public void setBannerDisplay(BannerDisplay paramBannerDisplay)
  {
    this.c = paramBannerDisplay;
  }

  public byte[] readMessage()
    throws SshException, AuthenticationResult
  {
    byte[] arrayOfByte;
    while (b(arrayOfByte = this.d.nextMessage()));
    return arrayOfByte;
  }

  public int authenticate(AuthenticationClient paramAuthenticationClient, String paramString)
    throws SshException
  {
    try
    {
      paramAuthenticationClient.authenticate(this, paramString);
      readMessage();
      this.d.disconnect(2, "Unexpected response received from Authentication Protocol");
      throw new SshException("Unexpected response received from Authentication Protocol", 3);
    }
    catch (AuthenticationResult localAuthenticationResult)
    {
      this.b = localAuthenticationResult.getResult();
      if (this.b == 1)
        this.d.i();
    }
    return this.b;
  }

  public String getAuthenticationMethods(String paramString1, String paramString2)
    throws SshException
  {
    sendRequest(paramString1, paramString2, "none", null);
    try
    {
      readMessage();
      this.d.disconnect(2, "Unexpected response received from Authentication Protocol");
      throw new SshException("Unexpected response received from Authentication Protocol", 3);
    }
    catch (AuthenticationResult localAuthenticationResult)
    {
      this.b = localAuthenticationResult.getResult();
      EventServiceImplementation.getInstance().fireEvent(new Event(this, 11, true).addAttribute("AUTHENTICATION_METHODS", localAuthenticationResult.getAuthenticationMethods()));
    }
    return localAuthenticationResult.getAuthenticationMethods();
  }

  public void sendRequest(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.write(50);
      localByteArrayWriter.writeString(paramString1);
      localByteArrayWriter.writeString(paramString2);
      localByteArrayWriter.writeString(paramString3);
      if (paramArrayOfByte != null)
        localByteArrayWriter.write(paramArrayOfByte);
      this.d.sendMessage(localByteArrayWriter.toByteArray(), true);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public boolean isAuthenticated()
  {
    return this.b == 1;
  }

  public byte[] getSessionIdentifier()
  {
    return this.d.getSessionIdentifier();
  }

  private boolean b(byte[] paramArrayOfByte)
    throws SshException, AuthenticationResult
  {
    try
    {
      ByteArrayReader localByteArrayReader;
      switch (paramArrayOfByte[0])
      {
      case 51:
        localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
        localByteArrayReader.skip(1L);
        String str = localByteArrayReader.readString();
        if (localByteArrayReader.read() == 0)
        {
          EventServiceImplementation.getInstance().fireEvent(new Event(this, 14, true));
          throw new AuthenticationResult(2, str);
        }
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 15, true));
        throw new AuthenticationResult(3, str);
      case 52:
        EventServiceImplementation.getInstance().fireEvent(new Event(this, 13, true));
        throw new AuthenticationResult(1);
      case 53:
        localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
        localByteArrayReader.skip(1L);
        if (this.c != null)
          this.c.displayBanner(localByteArrayReader.readString());
        return true;
      }
      return false;
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public void sendMessage(byte[] paramArrayOfByte)
    throws SshException
  {
    this.d.sendMessage(paramArrayOfByte, true);
  }

  public String getHost()
  {
    return this.d.mb.getHost();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.AuthenticationProtocol
 * JD-Core Version:    0.6.0
 */