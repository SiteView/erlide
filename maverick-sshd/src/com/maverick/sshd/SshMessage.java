package com.maverick.sshd;

import java.nio.ByteBuffer;

public abstract interface SshMessage
{
  public abstract boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer);

  public abstract void messageSent();
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.SshMessage
 * JD-Core Version:    0.6.0
 */