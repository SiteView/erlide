package com.maverick.sshd;

import com.maverick.ssh2.KBIPrompt;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.sshd.platform.KeyboardInteractiveProvider;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class KeyboardInteractiveAuthentication
  implements AuthenticationMechanism
{
  public static final int SSH_MSG_USERAUTH_INFO_REQUEST = 60;
  public static final int SSH_MSG_USERAUTH_INFO_RESPONSE = 61;
  TransportProtocol O;
  AuthenticationProtocol M;
  AuthenticationProvider N;
  protected byte[] sessionid;
  KeyboardInteractiveProvider L;

  public AuthenticationProvider getProvider()
  {
    return this.N;
  }

  public String getMethod()
  {
    return "keyboard-interactive";
  }

  public void init(TransportProtocol paramTransportProtocol, AuthenticationProtocol paramAuthenticationProtocol, byte[] paramArrayOfByte)
    throws IOException
  {
    this.O = paramTransportProtocol;
    this.M = paramAuthenticationProtocol;
    this.sessionid = paramArrayOfByte;
    this.N = paramTransportProtocol.getSshContext().getAuthenticationProvider();
    try
    {
      this.L = ((KeyboardInteractiveProvider)paramTransportProtocol.getSshContext().getKeyboardInteractiveProvider().newInstance());
    }
    catch (Throwable localThrowable)
    {
      throw new IOException(localThrowable.getMessage());
    }
  }

  public TransportProtocol getTransport()
  {
    return this.O;
  }

  public byte[] getSessionid()
  {
    return this.sessionid;
  }

  public boolean processMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    int i = localByteArrayReader.read();
    if (i != 61)
      return false;
    ArrayList localArrayList = new ArrayList();
    int j = (int)localByteArrayReader.readInt();
    for (int k = 0; k < j; k++)
      localArrayList.add(localByteArrayReader.readString());
    KBIPrompt[] arrayOfKBIPrompt = this.L.setResponse((String[])(String[])localArrayList.toArray(new String[0]));
    if (arrayOfKBIPrompt != null)
      A(arrayOfKBIPrompt, this.L.getName(), this.L.getInstruction());
    else if (this.L.hasAuthenticated())
      this.M.completedAuthentication();
    else
      this.M.failedAuthentication();
    return true;
  }

  public boolean startRequest(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
    String str1 = localByteArrayReader.readString();
    String str2 = localByteArrayReader.readString();
    KBIPrompt[] arrayOfKBIPrompt = this.L.init(paramString, this);
    A(arrayOfKBIPrompt, this.L.getName(), this.L.getInstruction());
    return false;
  }

  void A(KBIPrompt[] paramArrayOfKBIPrompt, String paramString1, String paramString2)
    throws IOException
  {
    ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
    localByteArrayWriter.write(60);
    localByteArrayWriter.writeString(paramString1);
    localByteArrayWriter.writeString(paramString2);
    localByteArrayWriter.writeString("");
    localByteArrayWriter.writeInt(paramArrayOfKBIPrompt.length);
    for (int i = 0; i < paramArrayOfKBIPrompt.length; i++)
    {
      localByteArrayWriter.writeString(paramArrayOfKBIPrompt[i].getPrompt());
      localByteArrayWriter.writeBoolean(paramArrayOfKBIPrompt[i].echo());
    }
    this.O.postMessage(new _A(localByteArrayWriter.toByteArray()));
  }

  class _A
    implements SshMessage
  {
    byte[] T;

    _A(byte[] arg2)
    {
      Object localObject;
      this.T = localObject;
    }

    public void messageSent()
    {
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(this.T);
      return true;
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.KeyboardInteractiveAuthentication
 * JD-Core Version:    0.6.0
 */