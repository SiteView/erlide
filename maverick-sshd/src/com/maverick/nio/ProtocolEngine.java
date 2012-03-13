package com.maverick.nio;

import java.nio.ByteBuffer;

public abstract interface ProtocolEngine
{
  public static final int BY_APPLICATION = 11;

  public abstract void onSocketConnect(SocketConnection paramSocketConnection);

  public abstract void onSocketClose();

  public abstract boolean onSocketRead(ByteBuffer paramByteBuffer);

  public abstract SocketWriteCallback onSocketWrite(ByteBuffer paramByteBuffer);

  public abstract boolean wantsToWrite();

  public abstract boolean isConnected();

  public abstract SocketConnection getSocketConnection();

  public abstract void disconnect(int paramInt, String paramString);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.ProtocolEngine
 * JD-Core Version:    0.6.0
 */