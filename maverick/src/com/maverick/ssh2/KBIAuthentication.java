package com.maverick.ssh2;

import com.maverick.ssh.SshException;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;

public class KBIAuthentication
  implements AuthenticationClient
{
  String r;
  KBIRequestHandler q;

  public String getUsername()
  {
    return this.r;
  }

  public void setUsername(String paramString)
  {
    this.r = paramString;
  }

  public String getMethod()
  {
    return "keyboard-interactive";
  }

  public void setKBIRequestHandler(KBIRequestHandler paramKBIRequestHandler)
  {
    this.q = paramKBIRequestHandler;
  }

  public void authenticate(AuthenticationProtocol paramAuthenticationProtocol, String paramString)
    throws SshException, AuthenticationResult
  {
    try
    {
      if (this.q == null)
        throw new SshException("A request handler must be set!", 4);
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString("");
      localByteArrayWriter.writeString("");
      paramAuthenticationProtocol.sendRequest(this.r, paramString, "keyboard-interactive", localByteArrayWriter.toByteArray());
      while (true)
      {
        byte[] arrayOfByte = paramAuthenticationProtocol.readMessage();
        ByteArrayReader localByteArrayReader = new ByteArrayReader(arrayOfByte);
        if (localByteArrayReader.read() != 60)
        {
          paramAuthenticationProtocol.d.disconnect(2, "Unexpected authentication message received!");
          throw new SshException("Unexpected authentication message received!", 3);
        }
        String str1 = localByteArrayReader.readString();
        String str2 = localByteArrayReader.readString();
        String str3 = localByteArrayReader.readString();
        int i = (int)localByteArrayReader.readInt();
        KBIPrompt[] arrayOfKBIPrompt = new KBIPrompt[i];
        for (int j = 0; j < i; j++)
        {
          String str4 = localByteArrayReader.readString();
          boolean bool = localByteArrayReader.read() == 1;
          arrayOfKBIPrompt[j] = new KBIPrompt(str4, bool);
        }
        if (!this.q.showPrompts(str1, str2, arrayOfKBIPrompt))
          throw new AuthenticationResult(4);
        localByteArrayWriter.reset();
        localByteArrayWriter.write(61);
        localByteArrayWriter.writeInt(arrayOfKBIPrompt.length);
        for (j = 0; j < arrayOfKBIPrompt.length; j++)
          localByteArrayWriter.writeString(arrayOfKBIPrompt[j].getResponse());
        paramAuthenticationProtocol.d.sendMessage(localByteArrayWriter.toByteArray(), true);
      }
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public String getMethodName()
  {
    return "keyboard-interactive";
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.KBIAuthentication
 * JD-Core Version:    0.6.0
 */