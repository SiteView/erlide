package com.maverick.sftp;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class SftpFileInputStream extends InputStream
{
  SftpFile g;
  SftpSubsystemChannel f;
  long c;
  Vector b = new Vector();
  SftpMessage h;
  int e;
  long d;

  public SftpFileInputStream(SftpFile paramSftpFile)
    throws SftpStatusException, SshException
  {
    this(paramSftpFile, 0L);
  }

  public SftpFileInputStream(SftpFile paramSftpFile, long paramLong)
    throws SftpStatusException, SshException
  {
    if (paramSftpFile.getHandle() == null)
      throw new SftpStatusException(100, "The file does not have a valid handle!");
    if (paramSftpFile.getSFTPChannel() == null)
      throw new SshException("The file is not attached to an SFTP subsystem!", 4);
    this.g = paramSftpFile;
    this.c = paramLong;
    this.f = paramSftpFile.getSFTPChannel();
    this.d = (paramSftpFile.getAttributes().getSize().longValue() - paramLong);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      int i = 0;
      int j = paramInt2;
      while ((i < j) && (i != -1))
      {
        b();
        if ((this.h == null) || (this.e == 0))
        {
          UnsignedInteger32 localUnsignedInteger32 = (UnsignedInteger32)this.b.elementAt(0);
          this.h = this.f.c(localUnsignedInteger32);
          this.b.removeElementAt(0);
          if (this.h.getType() == 103)
          {
            this.e = (int)this.h.readInt();
          }
          else
          {
            if (this.h.getType() == 101)
            {
              int m = (int)this.h.readInt();
              if (m == 1)
              {
                if (i != 0)
                  break;
                i = -1;
                break;
              }
              if (this.f.getVersion() >= 3)
              {
                String str = this.h.readString().trim();
                throw new IOException(str);
              }
              throw new IOException("");
            }
            close();
            throw new IOException("The server responded with an unexpected SFTP protocol message! type=" + this.h.getType());
          }
        }
        if (this.h == null)
          throw new IOException("Failed to obtain file data or status from the SFTP server!");
        int k = Math.min(this.e, paramInt2);
        System.arraycopy(this.h.array(), this.h.getPosition(), paramArrayOfByte, paramInt1, k);
        this.e -= k;
        this.h.skip(k);
        i += k;
        paramInt2 -= k;
        paramInt1 += k;
      }
      return i;
    }
    catch (SshException localSshException)
    {
      throw new SshIOException(localSshException);
    }
    catch (SftpStatusException localSftpStatusException)
    {
    }
    throw new IOException(localSftpStatusException.getMessage());
  }

  private void b()
    throws SftpStatusException, SshException
  {
    while (this.b.size() < 100)
    {
      this.b.addElement(this.f.postReadRequest(this.g.getHandle(), this.c, 32768));
      this.c += 32768L;
    }
  }

  public int read()
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    if (read(arrayOfByte) == 1)
      return arrayOfByte[0] & 0xFF;
    return -1;
  }

  public void close()
    throws IOException
  {
    try
    {
      this.g.close();
      while (this.b.size() > 0)
      {
        UnsignedInteger32 localUnsignedInteger32 = (UnsignedInteger32)this.b.elementAt(0);
        this.b.removeElementAt(0);
        this.f.c(localUnsignedInteger32);
      }
    }
    catch (SshException localSshException)
    {
      throw new SshIOException(localSshException);
    }
    catch (SftpStatusException localSftpStatusException)
    {
      throw new IOException(localSftpStatusException.getMessage());
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.sftp.SftpFileInputStream
 * JD-Core Version:    0.6.0
 */