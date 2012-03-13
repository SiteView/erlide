package com.maverick.sftp;

import com.maverick.ssh.message.Message;
import com.maverick.util.ByteArrayReader;
import java.io.IOException;

public class SftpMessage extends ByteArrayReader
  implements Message
{
  int g = read();
  int h = (int)readInt();

  SftpMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    super(paramArrayOfByte);
  }

  public int getType()
  {
    return this.g;
  }

  public int getMessageId()
  {
    return this.h;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.sftp.SftpMessage
 * JD-Core Version:    0.6.0
 */