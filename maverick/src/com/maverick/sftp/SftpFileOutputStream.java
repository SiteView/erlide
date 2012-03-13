package com.maverick.sftp;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.util.UnsignedInteger32;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

public class SftpFileOutputStream extends OutputStream
{
  SftpFile e;
  SftpSubsystemChannel d;
  long c;
  Vector b = new Vector();

  public SftpFileOutputStream(SftpFile paramSftpFile)
    throws SftpStatusException, SshException
  {
    if (paramSftpFile.getHandle() == null)
      throw new SftpStatusException(100, "The file does not have a valid handle!");
    if (paramSftpFile.getSFTPChannel() == null)
      throw new SshException("The file is not attached to an SFTP subsystem!", 4);
    this.e = paramSftpFile;
    this.d = paramSftpFile.getSFTPChannel();
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      while (paramInt2 > 0)
      {
        int i = Math.min(32768, paramInt2);
        this.b.addElement(this.d.postWriteRequest(this.e.getHandle(), this.c, paramArrayOfByte, paramInt1, i));
        b(100);
        paramInt1 += i;
        paramInt2 -= i;
        this.c += i;
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

  public void write(int paramInt)
    throws IOException
  {
    try
    {
      byte[] arrayOfByte = { (byte)paramInt };
      this.b.addElement(this.d.postWriteRequest(this.e.getHandle(), this.c, arrayOfByte, 0, 1));
      b(100);
      this.c += 1L;
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

  private boolean b(int paramInt)
    throws SftpStatusException, SshException
  {
    if (this.b.size() > paramInt)
    {
      UnsignedInteger32 localUnsignedInteger32 = (UnsignedInteger32)this.b.elementAt(0);
      this.d.getOKRequestStatus(localUnsignedInteger32);
      this.b.removeElementAt(0);
    }
    return this.b.size() > 0;
  }

  public void close()
    throws IOException
  {
    try
    {
      while (b(0));
      this.e.close();
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
 * Qualified Name:     com.maverick.sftp.SftpFileOutputStream
 * JD-Core Version:    0.6.0
 */