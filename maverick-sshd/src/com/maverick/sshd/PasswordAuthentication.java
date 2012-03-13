package com.maverick.sshd;

import com.maverick.nio.SocketConnection;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.sshd.platform.PasswordChangeException;
import com.maverick.util.ByteArrayReader;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PasswordAuthentication
  implements AuthenticationMechanism
{
  TransportProtocol K;
  AuthenticationProtocol I;
  AuthenticationProvider J;
  protected byte[] sessionid;

  public void init(TransportProtocol paramTransportProtocol, AuthenticationProtocol paramAuthenticationProtocol, byte[] paramArrayOfByte)
    throws IOException
  {
    this.K = paramTransportProtocol;
    this.I = paramAuthenticationProtocol;
    this.sessionid = paramArrayOfByte;
    this.J = paramTransportProtocol.getSshContext().getAuthenticationProvider();
  }

  public String getMethod()
  {
    return "password";
  }

  public boolean startRequest(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    int i = localByteArrayReader.read() == 0 ? 0 : 1;
    String str = localByteArrayReader.readString();
    try
    {
      boolean bool = false;
      if (i != 0)
        bool = this.J.logon(this.sessionid, paramString, str, localByteArrayReader.readString(), this.K.getSocketConnection().getRemoteAddress());
      else
        bool = this.J.logon(this.sessionid, paramString, str, this.K.getSocketConnection().getRemoteAddress());
      if (bool)
        this.I.completedAuthentication();
      else
        this.I.failedAuthentication(false, false);
    }
    catch (PasswordChangeException localPasswordChangeException)
    {
      this.K.postMessage(new SshMessage()
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          paramByteBuffer.put(60);
          String str = "Password change required.";
          paramByteBuffer.putInt(str.length());
          paramByteBuffer.put(str.getBytes());
          paramByteBuffer.putInt(0);
          return true;
        }

        public void messageSent()
        {
        }
      });
      this.I.discardAuthentication();
    }
    return true;
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    return false;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.PasswordAuthentication
 * JD-Core Version:    0.6.0
 */