package com.maverick.ssh2;

import com.maverick.ssh.PasswordAuthentication;
import com.maverick.ssh.SshException;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;

public class Ssh2PasswordAuthentication extends PasswordAuthentication
  implements AuthenticationClient
{
  String v;
  boolean u = false;

  public void setNewPassword(String paramString)
  {
    this.v = paramString;
  }

  public boolean requiresPasswordChange()
  {
    return this.u;
  }

  public void authenticate(AuthenticationProtocol paramAuthenticationProtocol, String paramString)
    throws SshException, AuthenticationResult
  {
    try
    {
      if ((getUsername() == null) || (getPassword() == null))
        throw new SshException("Username or password not set!", 4);
      if ((this.u) && (this.v == null))
        throw new SshException("You must set a new password!", 4);
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeBoolean(this.u);
      localByteArrayWriter.writeString(getPassword());
      if (this.u)
        localByteArrayWriter.writeString(this.v);
      paramAuthenticationProtocol.sendRequest(getUsername(), paramString, "password", localByteArrayWriter.toByteArray());
      byte[] arrayOfByte = paramAuthenticationProtocol.readMessage();
      if (arrayOfByte[0] != 60)
      {
        paramAuthenticationProtocol.d.disconnect(2, "Unexpected message received");
        throw new SshException("Unexpected response from Authentication Protocol", 3);
      }
      this.u = true;
      throw new AuthenticationResult(2);
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.Ssh2PasswordAuthentication
 * JD-Core Version:    0.6.0
 */