package com.maverick.ssh.message;

import com.maverick.util.ByteArrayReader;

public class SshMessage extends ByteArrayReader
  implements Message
{
  int c;
  SshMessage d;
  SshMessage e;

  SshMessage()
  {
    super(new byte[0]);
  }

  public SshMessage(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
    this.c = read();
  }

  public int getMessageId()
  {
    return this.c;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.message.SshMessage
 * JD-Core Version:    0.6.0
 */