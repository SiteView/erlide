package com.maverick.sshd;

import com.maverick.nio.ByteBufferPool;
import com.maverick.nio.Daemon;
import com.maverick.nio.DaemonContext;
import com.maverick.nio.EventLog;
import com.maverick.ssh.Packet;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class Subsystem
{
  protected SessionChannel session;
  protected SshContext context;
  String C;
  ByteBuffer A;
  int B = 0;
  static ByteBufferPool D = new ByteBufferPool(131072, false);

  public Subsystem(String paramString)
  {
    this.C = paramString;
  }

  protected void init(SessionChannel paramSessionChannel, SshContext paramSshContext)
    throws IOException
  {
    this.session = paramSessionChannel;
    this.context = paramSshContext;
  }

  protected void processMessage(byte[] paramArrayOfByte)
    throws IOException
  {
    if (this.A == null)
      this.A = D.get();
    this.A.put(paramArrayOfByte);
    this.A.flip();
    if ((this.B == 0) && (this.A.remaining() >= 4))
      this.B = this.A.getInt();
    if ((this.B < 0) || (this.B > this.context.getMaximumPacketLength()))
    {
      EventLog.LogErrorEvent(this, "Incoming subsystem message length " + this.B + " exceeds maximum supported packet length " + this.context.getMaximumPacketLength());
      this.session.close();
      return;
    }
    while ((this.B > 0) && (this.A.remaining() >= this.B))
    {
      byte[] arrayOfByte = new byte[this.B];
      this.A.get(arrayOfByte);
      onMessageReceived(arrayOfByte);
      if (this.A.remaining() >= 4)
        this.B = this.A.getInt();
      else
        this.B = 0;
    }
    if (!this.A.hasRemaining())
    {
      D.add(this.A);
      this.A = null;
    }
    else
    {
      this.A.compact();
    }
  }

  void A()
  {
    onSubsystemFree();
    if (this.A != null)
      this.context.getServer().getContext().getBufferPool().add(this.A);
    this.A = null;
    this.context = null;
    this.session = null;
  }

  protected abstract void onSubsystemFree();

  protected abstract void onMessageReceived(byte[] paramArrayOfByte)
    throws IOException;

  protected void sendMessage(Packet paramPacket)
  {
    paramPacket.finish();
    this.session.sendChannelDataWithBuffering(paramPacket.array(), 0, paramPacket.size());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.Subsystem
 * JD-Core Version:    0.6.0
 */