package com.maverick.sshd;

import java.io.IOException;
import java.nio.ByteBuffer;

public class NoneAuthentication
  implements AuthenticationMechanism
{
  TransportProtocol C;
  AuthenticationProtocol A;
  byte[] B;

  public final String getMethod()
  {
    return "none";
  }

  public final void init(TransportProtocol paramTransportProtocol, AuthenticationProtocol paramAuthenticationProtocol, byte[] paramArrayOfByte)
    throws IOException
  {
    this.C = paramTransportProtocol;
    this.A = paramAuthenticationProtocol;
    this.B = paramArrayOfByte;
  }

  public final boolean processMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    return false;
  }

  public String getBannerForUser(String paramString)
  {
    return null;
  }

  public final boolean startRequest(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    String str = getBannerForUser(paramString);
    if (str != null)
      this.C.postMessage(new SshMessage(str)
      {
        public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
        {
          paramByteBuffer.put(53);
          paramByteBuffer.putInt(this.val$banner.getBytes().length);
          paramByteBuffer.put(this.val$banner.getBytes());
          paramByteBuffer.putInt(0);
          return true;
        }

        public void messageSent()
        {
        }
      });
    this.A.failedAuthentication();
    return true;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.NoneAuthentication
 * JD-Core Version:    0.6.0
 */