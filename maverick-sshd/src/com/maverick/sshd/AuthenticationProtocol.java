package com.maverick.sshd;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.nio.EventLog;
import com.maverick.nio.SocketConnection;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.sshd.events.EventServiceImplementation;
import com.maverick.sshd.events.SSHDEvent;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.util.ByteArrayReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationProtocol
  implements A
{
  public static final int SSH_MSG_USERAUTH_REQUEST = 50;
  public static final int SSH_MSG_USERAUTH_FAILURE = 51;
  public static final int SSH_MSG_USERAUTH_SUCCESS = 52;
  public static final int SSH_MSG_USERAUTH_BANNER = 53;
  TransportProtocol B;
  boolean H = false;
  int F = 0;
  AuthenticationMechanism D;
  String C;
  String E;
  String G;
  ArrayList I = new ArrayList();
  Map A = Collections.synchronizedMap(new HashMap());

  public void init(TransportProtocol paramTransportProtocol)
  {
    this.B = paramTransportProtocol;
  }

  public void stop()
  {
    this.B = null;
    this.D = null;
  }

  public void start()
  {
    if (this.B.getSshContext().getBannerMessage().length() > 0)
      this.B.postMessage(new SshMessage()
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          paramByteBuffer.put(53);
          paramByteBuffer.putInt(AuthenticationProtocol.this.B.getSshContext().getBannerMessage().length());
          paramByteBuffer.put(AuthenticationProtocol.this.B.getSshContext().getBannerMessage().getBytes());
          paramByteBuffer.putInt(0);
          return true;
        }

        public void messageSent()
        {
        }
      });
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    if (this.H)
      return this.D.processMessage(paramArrayOfByte);
    switch (paramArrayOfByte[0])
    {
    case 50:
      A(paramArrayOfByte);
      return true;
    }
    return false;
  }

  public Object getParameter(String paramString)
  {
    return this.A.get(paramString);
  }

  public void setParameter(String paramString, Object paramObject)
  {
    this.A.put(paramString, paramObject);
  }

  void A(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    localByteArrayReader.skip(1L);
    this.E = localByteArrayReader.readString();
    this.G = localByteArrayReader.readString();
    this.B.getSshContext().getAuthenticationProvider().registerSession(this.B.getSessionIdentifier(), this.E, this.B.getSocketConnection().getRemoteAddress());
    boolean bool = true;
    if (this.B.getSshContext().getAccessManager() != null)
      bool = this.B.getSshContext().getAccessManager().canConnect(this.E);
    this.C = localByteArrayReader.readString();
    EventLog.LogEvent(this, "Client is attempting " + this.C + " authentication");
    byte[] arrayOfByte = null;
    if (localByteArrayReader.available() > 0)
    {
      arrayOfByte = new byte[localByteArrayReader.available()];
      localByteArrayReader.read(arrayOfByte);
    }
    if ((bool) && (this.B.getSshContext().supportedAuthenticationMechanisms().contains(this.C)) && (this.G.equals("ssh-connection")))
      try
      {
        this.D = ((AuthenticationMechanism)this.B.getSshContext().supportedAuthenticationMechanisms().getInstance(this.C));
        this.D.init(this.B, this, this.B.getSessionIdentifier());
        this.H = true;
        this.D.startRequest(this.E, arrayOfByte);
        return;
      }
      catch (SshException localSshException)
      {
      }
    failedAuthentication();
  }

  public void completedAuthentication()
    throws IOException
  {
    if (((this.D instanceof KeyboardInteractiveAuthentication)) && ((((KeyboardInteractiveAuthentication)this.D).L instanceof PasswordKeyboardInteractiveProvider)))
      this.I.add("password");
    this.I.add(this.D.getMethod());
    EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 13, true).addAttribute("SESSION_ID", this.B.getSessionIdentifier()).addAttribute("USERNAME", this.E).addAttribute("AUTHENTICATION_METHOD", this.C));
    String[] arrayOfString = null;
    if (this.B.getSshContext().getAccessManager() != null)
      arrayOfString = this.B.getSshContext().getAccessManager().getRequiredAuthentications(this.B.getSessionIdentifier(), this.E);
    if (arrayOfString == null)
      arrayOfString = this.B.getSshContext().getRequiredAuthentications();
    boolean bool = true;
    for (int i = 0; i < arrayOfString.length; i++)
      bool &= this.I.contains(arrayOfString[i]);
    if (bool)
    {
      if (!this.B.getSshContext().getAuthenticationProvider().isSessionLoggedOn(this.B.getSessionIdentifier()))
        this.B.getSshContext().getAuthenticationProvider().logon(this.B.getSessionIdentifier(), this.E, this.B.getSocketConnection().getRemoteAddress());
      this.B.postMessage(new SshMessage()
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          paramByteBuffer.put(52);
          return true;
        }

        public void messageSent()
        {
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 205, true).addAttribute("SESSION_ID", AuthenticationProtocol.this.B.getSessionIdentifier()).addAttribute("USERNAME", AuthenticationProtocol.this.E).addAttribute("AUTHENTICATION_METHODS", AuthenticationProtocol.this.I));
          AuthenticationProtocol.this.B.A(new ConnectionProtocol());
        }
      });
      this.H = false;
    }
    else
    {
      failedAuthentication(true, true);
    }
  }

  public void discardAuthentication()
  {
    this.H = false;
  }

  public void failedAuthentication()
    throws IOException
  {
    failedAuthentication(false, false);
  }

  public void failedAuthentication(boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    if ((!this.C.equals("none")) && (!paramBoolean1))
    {
      if (!paramBoolean2)
        this.F += 1;
      if (this.F >= this.B.getSshContext().getMaxAuthentications())
      {
        this.B.disconnect(11, "Too many bad authentication attempts!");
        return;
      }
    }
    this.B.postMessage(new SshMessage(paramBoolean1)
    {
      public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
      {
        paramByteBuffer.put(51);
        String str = AuthenticationProtocol.this.B.getSshContext().supportedAuthenticationMechanisms().list("");
        paramByteBuffer.putInt(str.length());
        paramByteBuffer.put(str.getBytes());
        paramByteBuffer.put((byte)(this.val$partial ? 1 : 0));
        return true;
      }

      public void messageSent()
      {
        if (!this.val$partial)
          EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 14, true).addAttribute("SESSION_ID", AuthenticationProtocol.this.B.getSessionIdentifier()).addAttribute("USERNAME", AuthenticationProtocol.this.E).addAttribute("AUTHENTICATION_METHOD", AuthenticationProtocol.this.C));
      }
    });
    this.H = false;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.AuthenticationProtocol
 * JD-Core Version:    0.6.0
 */