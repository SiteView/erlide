package com.maverick.ssh.message;

import com.maverick.ssh.SshException;

public abstract interface SshMessageReader
{
  public abstract byte[] nextMessage()
    throws SshException;

  public abstract boolean isConnected();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.message.SshMessageReader
 * JD-Core Version:    0.6.0
 */