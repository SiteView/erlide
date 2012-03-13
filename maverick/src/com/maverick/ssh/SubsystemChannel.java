package com.maverick.ssh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;

public class SubsystemChannel
{
  DataInputStream c;
  DataOutputStream d;
  protected SshChannel channel;
  Vector f = new Vector();
  _c b = new _c();
  _b e = new _b();

  public SubsystemChannel(SshChannel paramSshChannel)
    throws SshException
  {
    this.channel = paramSshChannel;
    try
    {
      this.c = new DataInputStream(paramSshChannel.getInputStream());
      this.d = new DataOutputStream(paramSshChannel.getOutputStream());
    }
    catch (SshIOException localSshIOException)
    {
      throw localSshIOException.getRealException();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException.getMessage(), 6);
    }
  }

  public boolean isClosed()
  {
    return this.channel.isClosed();
  }

  public void close()
    throws IOException
  {
    this.f.removeAllElements();
    this.channel.close();
  }

  public byte[] nextMessage()
    throws SshException
  {
    return this.b.b(this.c);
  }

  protected void sendMessage(Packet paramPacket)
    throws SshException
  {
    this.e.b(paramPacket);
  }

  protected void sendMessage(byte[] paramArrayOfByte)
    throws SshException
  {
    try
    {
      Packet localPacket = createPacket();
      localPacket.write(paramArrayOfByte);
      sendMessage(localPacket);
    }
    catch (IOException localIOException)
    {
      throw new SshException(1, localIOException);
    }
  }

  protected Packet createPacket()
    throws IOException
  {
    synchronized (this.f)
    {
      if (this.f.size() == 0)
        return new Packet();
      Packet localPacket = (Packet)this.f.elementAt(0);
      this.f.removeElementAt(0);
      return localPacket;
    }
  }

  class _c
  {
    _c()
    {
    }

    synchronized byte[] b(DataInputStream paramDataInputStream)
      throws SshException
    {
      int i = -1;
      try
      {
        i = paramDataInputStream.readInt();
        if (i < 0)
          throw new SshException("Negative message length in SFTP protocol.", 3);
        byte[] arrayOfByte = new byte[i];
        paramDataInputStream.readFully(arrayOfByte);
        return arrayOfByte;
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
        throw new SshException("Invalid message length in SFTP protocol [" + i + "]", 3);
      }
      catch (EOFException localEOFException)
      {
        try
        {
          SubsystemChannel.this.close();
        }
        catch (SshIOException localSshIOException1)
        {
          throw localSshIOException1.getRealException();
        }
        catch (IOException localIOException2)
        {
          throw new SshException(localIOException2.getMessage(), 6);
        }
        throw new SshException("The channel unexpectedly terminated", 6);
      }
      catch (IOException localIOException1)
      {
        if ((localIOException1 instanceof SshIOException))
          throw ((SshIOException)localIOException1).getRealException();
        try
        {
          SubsystemChannel.this.close();
        }
        catch (SshIOException localSshIOException2)
        {
          throw localSshIOException2.getRealException();
        }
        catch (IOException localIOException3)
        {
          throw new SshException(localIOException3.getMessage(), 6);
        }
      }
      throw new SshException(6, localIOException1);
    }
  }

  class _b
  {
    _b()
    {
    }

    synchronized void b(Packet paramPacket)
      throws SshException
    {
      try
      {
        paramPacket.finish();
        SubsystemChannel.this.d.write(paramPacket.array(), 0, paramPacket.size());
      }
      catch (SshIOException localSshIOException1)
      {
        throw localSshIOException1.getRealException();
      }
      catch (EOFException localEOFException)
      {
        try
        {
          SubsystemChannel.this.close();
        }
        catch (SshIOException localSshIOException2)
        {
          throw localSshIOException2.getRealException();
        }
        catch (IOException localIOException2)
        {
          throw new SshException(localIOException2.getMessage(), 6);
        }
        throw new SshException("The channel unexpectedly terminated", 6);
      }
      catch (IOException localIOException1)
      {
        try
        {
          SubsystemChannel.this.close();
        }
        catch (SshIOException localSshIOException3)
        {
          throw localSshIOException3.getRealException();
        }
        catch (IOException localIOException3)
        {
          throw new SshException(localIOException3.getMessage(), 6);
        }
        throw new SshException("Unknown channel IO failure: " + localIOException1.getMessage(), 6);
      }
      finally
      {
        paramPacket.reset();
        synchronized (SubsystemChannel.this.f)
        {
          SubsystemChannel.this.f.addElement(paramPacket);
        }
      }
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SubsystemChannel
 * JD-Core Version:    0.6.0
 */