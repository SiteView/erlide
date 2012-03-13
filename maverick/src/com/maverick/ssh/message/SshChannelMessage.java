package com.maverick.ssh.message;

import com.maverick.ssh.SshException;
import java.io.IOException;

public class SshChannelMessage extends SshMessage
{
  int f;

  public SshChannelMessage(byte[] paramArrayOfByte)
    throws SshException
  {
    super(paramArrayOfByte);
    try
    {
      this.f = (int)readInt();
    }
    catch (IOException localIOException)
    {
      throw new SshException(5, localIOException);
    }
  }

  int b()
  {
    return this.f;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.message.SshChannelMessage
 * JD-Core Version:    0.6.0
 */