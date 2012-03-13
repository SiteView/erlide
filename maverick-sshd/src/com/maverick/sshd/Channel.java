package com.maverick.sshd;

import com.maverick.events.EventService;
import com.maverick.nio.EventLog;
import com.maverick.nio.IdleStateListener;
import com.maverick.nio.IdleStateManager;
import com.maverick.nio.SocketConnection;
import com.maverick.nio.WriteOperationRequest;
import com.maverick.ssh.ChannelOpenException;
import com.maverick.sshd.events.EventServiceImplementation;
import com.maverick.sshd.events.SSHDEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public abstract class Channel
{
  protected ConnectionProtocol connection;
  String N;
  int C;
  int A;
  int M = 32000;
  protected int localwindow = 0;
  protected Object localWindowLock = new Object();
  boolean L = false;
  boolean O = false;
  boolean D = false;
  boolean I = false;
  boolean Q = false;
  boolean F = false;
  int E = 0;
  Vector G = new Vector();
  Vector B = new Vector();
  Vector K = new Vector();
  boolean P = false;
  _I J;
  static int H = 0;

  public Channel(String paramString, int paramInt1, int paramInt2)
  {
    this.N = paramString;
    this.M = paramInt1;
    this.localwindow = paramInt2;
  }

  public boolean isClosed()
  {
    return this.E == 2;
  }

  public boolean isEOF()
  {
    return this.O;
  }

  void A(ConnectionProtocol paramConnectionProtocol)
  {
    this.connection = paramConnectionProtocol;
  }

  public int getQueueSize()
  {
    return this.J.B.size();
  }

  public void resetIdleState(IdleStateListener paramIdleStateListener)
  {
    this.connection.N.getSocketConnection().getIdleStates().register(paramIdleStateListener);
  }

  public void clearIdleState(IdleStateListener paramIdleStateListener)
  {
    this.connection.N.getSocketConnection().getIdleStates().remove(paramIdleStateListener);
  }

  public void addEventListener(ChannelEventListener paramChannelEventListener)
  {
    if (paramChannelEventListener != null)
      this.G.add(paramChannelEventListener);
  }

  public void addInputListener(OutputStream paramOutputStream)
  {
    if (paramOutputStream != null)
      this.B.add(paramOutputStream);
  }

  public void addOutputListener(OutputStream paramOutputStream)
  {
    if (paramOutputStream != null)
      this.K.add(paramOutputStream);
  }

  public String getChannelType()
  {
    return this.N;
  }

  public int getRemoteWindow()
  {
    return this.J.B();
  }

  public int getLocalWindow()
  {
    return this.localwindow;
  }

  public int getLocalPacket()
  {
    return this.M;
  }

  public int getRemotePacket()
  {
    return this.J.C;
  }

  public int getLocalId()
  {
    return this.C;
  }

  public int getRemoteId()
  {
    return this.A;
  }

  byte[] A(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
    throws WriteOperationRequest, ChannelOpenException
  {
    this.C = paramInt1;
    this.A = paramInt2;
    this.J = new _I(paramInt4, paramInt3);
    return openChannel(paramArrayOfByte);
  }

  void C()
  {
    this.E = 1;
    onChannelOpenConfirmation();
    Iterator localIterator = this.G.iterator();
    while ((localIterator != null) && (localIterator.hasNext()))
      ((ChannelEventListener)localIterator.next()).onChannelOpen(this);
  }

  void A(int paramInt1, int paramInt2, int paramInt3)
  {
    this.A = paramInt1;
    this.J = new _I(paramInt3, paramInt2);
    C();
  }

  protected int queueSize()
  {
    return this.J.B.size();
  }

  protected void registerExtendedData(int paramInt)
    throws IOException
  {
  }

  public byte[] getSessionIdentifier()
  {
    return this.connection.getSessionIdentifier();
  }

  void B(int paramInt)
  {
    this.J.A(paramInt);
    onWindowAdjust(paramInt);
  }

  protected void onWindowAdjust(int paramInt)
  {
  }

  protected boolean hasQueuedData()
  {
    return this.J.A();
  }

  public ConnectionProtocol getConnection()
  {
    return this.connection;
  }

  void A(int paramInt)
    throws IOException
  {
    synchronized (this.localWindowLock)
    {
      if (this.localwindow < paramInt)
        throw new IOException("Data length of " + String.valueOf(paramInt) + " bytes exceeded available window space of " + String.valueOf(this.localwindow) + " bytes.");
      this.localwindow -= paramInt;
    }
  }

  void A(byte[] paramArrayOfByte)
    throws IOException
  {
    A(paramArrayOfByte.length);
    onChannelData(paramArrayOfByte);
    Iterator localIterator = this.B.iterator();
    while ((localIterator != null) && (localIterator.hasNext()))
      ((OutputStream)localIterator.next()).write(paramArrayOfByte);
    synchronized (this.localWindowLock)
    {
      evaluateWindowSpace(this.localwindow);
    }
  }

  public void sendChannelData(byte[] paramArrayOfByte)
  {
    sendChannelData(paramArrayOfByte, null);
  }

  public void sendChannelData(byte[] paramArrayOfByte, Runnable paramRunnable)
  {
    sendChannelData(paramArrayOfByte, 0, paramArrayOfByte.length, paramRunnable);
  }

  public void sendChannelData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    sendChannelData(paramArrayOfByte, paramInt1, paramInt2, null);
  }

  public void sendChannelData(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Runnable paramRunnable)
  {
    try
    {
      Iterator localIterator = this.K.iterator();
      while ((localIterator != null) && (localIterator.hasNext()))
        ((OutputStream)localIterator.next()).write(paramArrayOfByte, paramInt1, paramInt2);
    }
    catch (IOException localIOException)
    {
    }
    this.J.A(paramArrayOfByte, paramInt1, paramInt2, paramRunnable);
  }

  public void sendChannelDataWithBuffering(byte[] paramArrayOfByte)
  {
    sendChannelDataWithBuffering(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void sendChannelDataWithBuffering(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (this.connection.N.getSocketConnection().isSelectorThread())
      sendChannelData(paramArrayOfByte, paramInt1, paramInt2);
    else
      this.J.A(paramArrayOfByte, paramInt1, paramInt2);
  }

  public SshContext getContext()
  {
    return this.connection.getContext();
  }

  protected void sendExtendedData(byte[] paramArrayOfByte, int paramInt)
  {
    sendExtendedData(paramArrayOfByte, 0, paramArrayOfByte.length, paramInt);
  }

  protected void sendExtendedData(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    this.J.A(paramArrayOfByte, paramInt1, paramInt2, paramInt3, null);
  }

  protected abstract void onChannelData(byte[] paramArrayOfByte);

  void A(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    A(paramArrayOfByte.length);
    onExtendedData(paramArrayOfByte, paramInt);
    synchronized (this.localWindowLock)
    {
      evaluateWindowSpace(this.localwindow);
    }
  }

  protected abstract void onExtendedData(byte[] paramArrayOfByte, int paramInt);

  synchronized void B()
  {
    this.O = true;
    Iterator localIterator = this.G.iterator();
    while ((localIterator != null) && (localIterator.hasNext()))
      ((ChannelEventListener)localIterator.next()).onChannelEOF(this);
    onRemoteEOF();
  }

  void A()
  {
    this.Q = true;
    onRemoteClose();
  }

  protected void sendChannelRequest(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    this.connection.A(new _H(paramString, paramBoolean, paramArrayOfByte));
  }

  void F()
  {
    onChannelOpenFailure();
  }

  protected void onChannelOpenFailure()
  {
  }

  protected void onRemoteClose()
  {
    this.P = true;
    if (canClose())
      close();
  }

  public boolean isClosing()
  {
    return this.D;
  }

  public synchronized void close()
  {
    boolean bool = this.D;
    if ((!this.D) && (!hasQueuedData()))
    {
      this.D = true;
      Iterator localIterator = this.G.iterator();
      while ((localIterator != null) && (localIterator.hasNext()))
        ((ChannelEventListener)localIterator.next()).onChannelClosing(this);
      onChannelClosing();
      this.E = 2;
      if (this.connection.A())
      {
        this.connection.A(new _E(this.Q));
        this.F = this.Q;
      }
      else
      {
        D();
        this.F = true;
      }
    }
    else if ((!this.D) && (hasQueuedData()))
    {
      this.I = true;
    }
    if (((this.Q) || ((!this.Q) && (!this.connection.A()))) && (bool) && (!this.F))
      D();
  }

  private synchronized void D()
  {
    Iterator localIterator = this.G.iterator();
    while ((localIterator != null) && (localIterator.hasNext()))
      ((ChannelEventListener)localIterator.next()).onChannelClose(this);
    onChannelClosed();
    this.connection.B(this);
    E();
  }

  protected abstract void onChannelFree();

  private void E()
  {
    if (this.G != null)
      this.G.clear();
    onChannelFree();
  }

  byte[] C(int paramInt)
    throws IOException
  {
    this.C = paramInt;
    return createChannel();
  }

  protected abstract byte[] createChannel()
    throws IOException;

  protected abstract byte[] openChannel(byte[] paramArrayOfByte)
    throws WriteOperationRequest, ChannelOpenException;

  protected abstract void onChannelOpenConfirmation();

  protected abstract void onChannelClosed();

  protected abstract void onChannelOpen();

  protected abstract void onChannelClosing();

  protected abstract void onChannelRequest(String paramString, boolean paramBoolean, byte[] paramArrayOfByte);

  protected abstract void evaluateWindowSpace(int paramInt);

  protected abstract void onRemoteEOF();

  protected void sendEOF()
  {
    if ((!this.D) && (!this.L))
    {
      this.L = true;
      if (!this.J.A())
      {
        this.connection.A(new _B());
        onLocalEOF();
      }
    }
  }

  protected boolean canClose()
  {
    return !this.J.A();
  }

  protected abstract void onLocalEOF();

  protected boolean isOpen()
  {
    return this.E == 1;
  }

  protected void sendRequestResponse(boolean paramBoolean)
  {
    if (paramBoolean)
      this.connection.A(new _A());
    else
      this.connection.A(new _F());
  }

  protected void sendWindowAdjust(int paramInt)
  {
    synchronized (this.localWindowLock)
    {
      this.connection.A(new _G(this, paramInt));
      this.localwindow += paramInt;
    }
  }

  protected class QueuedData
  {
    int C;
    byte[] B;
    int E;
    int A;
    Runnable D;

    protected QueuedData()
    {
    }
  }

  class _I
  {
    LinkedList B = new LinkedList();
    int A;
    int C;

    _I(int paramInt1, int arg3)
    {
      this.A = paramInt1;
      int i;
      this.C = i;
    }

    public synchronized void A(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Runnable paramRunnable)
    {
      A(paramArrayOfByte, paramInt1, paramInt2, 0, paramRunnable);
    }

    public synchronized void A(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, Runnable paramRunnable)
    {
      Channel.QueuedData localQueuedData = new Channel.QueuedData(Channel.this);
      localQueuedData.C = paramInt3;
      localQueuedData.B = paramArrayOfByte;
      localQueuedData.E = paramInt1;
      localQueuedData.A = paramInt2;
      localQueuedData.D = paramRunnable;
      A(localQueuedData);
    }

    public void A(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      Channel.QueuedData localQueuedData = new Channel.QueuedData(Channel.this);
      localQueuedData.C = 0;
      localQueuedData.B = paramArrayOfByte;
      localQueuedData.E = paramInt1;
      localQueuedData.A = paramInt2;
      synchronized (localQueuedData)
      {
        if (!A(localQueuedData))
          try
          {
            localQueuedData.wait();
          }
          catch (InterruptedException localInterruptedException)
          {
            EventLog.LogEvent(this, "Thread was interupted before it had notification of its data being sent for channel id " + Channel.this.C);
          }
      }
    }

    public synchronized boolean A()
    {
      return this.B.size() > 0;
    }

    public synchronized int B()
    {
      return this.A;
    }

    public synchronized void A(int paramInt)
    {
      this.A += paramInt;
      A(null);
    }

    public synchronized boolean A(Channel.QueuedData paramQueuedData)
    {
      if (paramQueuedData != null)
        this.B.addLast(paramQueuedData);
      while ((this.A > 0) && (!this.B.isEmpty()))
      {
        Channel.QueuedData localQueuedData = (Channel.QueuedData)this.B.getFirst();
        int i = Math.min(this.A, this.C);
        if (localQueuedData.A <= i)
        {
          if (localQueuedData.C > 0)
            Channel.this.connection.A(new Channel._C(Channel.this, localQueuedData.B, localQueuedData.E, localQueuedData.A, localQueuedData.C));
          else
            Channel.this.connection.A(new Channel._D(Channel.this, localQueuedData.B, localQueuedData.E, localQueuedData.A));
          this.B.removeFirst();
          if (localQueuedData.D != null)
            localQueuedData.D.run();
          this.A -= localQueuedData.A;
          synchronized (localQueuedData)
          {
            localQueuedData.notifyAll();
          }
          localQueuedData.B = null;
          localQueuedData = null;
          continue;
        }
        if (localQueuedData.C > 0)
          Channel.this.connection.A(new Channel._C(Channel.this, localQueuedData.B, localQueuedData.E, i, localQueuedData.C));
        else
          Channel.this.connection.A(new Channel._D(Channel.this, localQueuedData.B, localQueuedData.E, i));
        localQueuedData.E += i;
        localQueuedData.A -= i;
        this.A -= i;
      }
      if ((Channel.this.P) && (Channel.this.canClose()))
      {
        Channel.this.close();
      }
      else if ((Channel.this.I) && (Channel.this.canClose()))
      {
        Channel.this.close();
      }
      else if ((Channel.this.L) && (this.B.isEmpty()))
      {
        Channel.this.connection.A(new Channel._B(Channel.this));
        Channel.this.onLocalEOF();
      }
      return this.B.isEmpty();
    }
  }

  class _B
    implements SshMessage
  {
    _B()
    {
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(96);
      paramByteBuffer.putInt(Channel.this.A);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _E
    implements SshMessage
  {
    boolean I;

    _E(boolean arg2)
    {
      boolean bool;
      this.I = bool;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(97);
      paramByteBuffer.putInt(Channel.this.A);
      return true;
    }

    public void messageSent()
    {
      if (this.I)
        Channel.this.D();
    }
  }

  class _D
    implements SshMessage
  {
    byte[] G;
    int H;
    int E;
    int F = Channel.H++;

    _D(byte[] paramInt1, int paramInt2, int arg4)
    {
      this.G = paramInt1;
      this.H = paramInt2;
      int i;
      this.E = i;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(94);
      paramByteBuffer.putInt(Channel.this.A);
      paramByteBuffer.mark();
      paramByteBuffer.putInt(0);
      paramByteBuffer.put(this.G, this.H, this.E);
      int i = paramByteBuffer.position();
      paramByteBuffer.reset();
      paramByteBuffer.putInt(this.E);
      paramByteBuffer.position(i);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _C
    implements SshMessage
  {
    int B;
    byte[] C;
    int D;
    int A;

    _C(byte[] paramInt1, int paramInt2, int paramInt3, int arg5)
    {
      this.C = paramInt1;
      this.D = paramInt2;
      this.A = paramInt3;
      int i;
      this.B = i;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(95);
      paramByteBuffer.putInt(Channel.this.A);
      paramByteBuffer.putInt(this.B);
      paramByteBuffer.mark();
      paramByteBuffer.putInt(0);
      paramByteBuffer.put(this.C, this.D, this.A);
      int i = paramByteBuffer.position();
      paramByteBuffer.reset();
      paramByteBuffer.putInt(this.A);
      paramByteBuffer.position(i);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _F
    implements SshMessage
  {
    _F()
    {
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(100);
      paramByteBuffer.putInt(Channel.this.A);
      return true;
    }

    public void messageSent()
    {
      EventServiceImplementation.getInstance().fireEvent(new SSHDEvent(this, 24, true));
    }
  }

  class _A
    implements SshMessage
  {
    _A()
    {
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(99);
      paramByteBuffer.putInt(Channel.this.A);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _G
    implements SshMessage
  {
    int K;
    Channel J;

    _G(Channel paramInt, int arg3)
    {
      this.J = paramInt;
      int i;
      this.K = i;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(93);
      paramByteBuffer.putInt(Channel.this.A);
      paramByteBuffer.putInt(this.K);
      return true;
    }

    public void messageSent()
    {
    }
  }

  class _H
    implements SshMessage
  {
    String L;
    boolean N;
    byte[] M;

    _H(String paramBoolean, boolean paramArrayOfByte, byte[] arg4)
    {
      this.L = paramBoolean;
      this.N = paramArrayOfByte;
      Object localObject;
      this.M = localObject;
    }

    public boolean writeMessageIntoBuffer(ByteBuffer paramByteBuffer)
    {
      try
      {
        paramByteBuffer.put(98);
        paramByteBuffer.putInt(Channel.this.A);
        paramByteBuffer.putInt(this.L.length());
        paramByteBuffer.put(this.L.getBytes(TransportProtocol.CHARSET_ENCODING));
        paramByteBuffer.put((byte)(this.N ? 1 : 0));
        if (this.M != null)
          paramByteBuffer.put(this.M);
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Channel.this.connection.A(2, "Could not encode string using " + TransportProtocol.CHARSET_ENCODING + " charset");
      }
      return true;
    }

    public void messageSent()
    {
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.Channel
 * JD-Core Version:    0.6.0
 */