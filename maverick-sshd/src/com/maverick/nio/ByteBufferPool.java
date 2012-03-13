package com.maverick.nio;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ByteBufferPool
{
  private ArrayList C = new ArrayList();
  private int B = 4096;
  private int D = 0;
  private boolean E = true;
  private long A = 0L;

  public ByteBufferPool()
  {
  }

  public ByteBufferPool(int paramInt, boolean paramBoolean)
  {
    this.B = paramInt;
    this.E = paramBoolean;
  }

  public int getCapacity()
  {
    return this.B;
  }

  public int getAllocatedBuffers()
  {
    return this.D;
  }

  public int getFreeBuffers()
  {
    return this.C.size();
  }

  public long getTotalMemoryInUse()
  {
    return this.A - this.C.size() * this.B;
  }

  public synchronized long getTotalMemoryAllocated()
  {
    return this.A;
  }

  public synchronized ByteBuffer get()
  {
    if (this.C.isEmpty())
    {
      this.D += 1;
      localByteBuffer = this.E ? ByteBuffer.allocateDirect(this.B) : ByteBuffer.allocate(this.B);
      this.A += this.B;
      return localByteBuffer;
    }
    ByteBuffer localByteBuffer = (ByteBuffer)this.C.remove(this.C.size() - 1);
    localByteBuffer.clear();
    return localByteBuffer;
  }

  public synchronized void add(ByteBuffer paramByteBuffer)
  {
    if (paramByteBuffer == null)
      return;
    if (paramByteBuffer.capacity() == this.B)
    {
      paramByteBuffer.clear();
      this.C.add(paramByteBuffer);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.nio.ByteBufferPool
 * JD-Core Version:    0.6.0
 */