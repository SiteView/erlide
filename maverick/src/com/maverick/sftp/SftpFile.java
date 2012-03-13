package com.maverick.sftp;

import com.maverick.ssh.SshException;
import com.maverick.util.UnsignedInteger32;

public class SftpFile
{
  String c;
  byte[] g;
  SftpFileAttributes d;
  SftpSubsystemChannel e;
  String b;
  String f;

  public SftpFile(String paramString, SftpFileAttributes paramSftpFileAttributes)
  {
    this.b = paramString;
    this.d = paramSftpFileAttributes;
    if (this.b.equals("/"))
    {
      this.c = "/";
    }
    else
    {
      this.b = this.b.trim();
      if (this.b.endsWith("/"))
        this.b = this.b.substring(0, this.b.length() - 1);
      int i = this.b.lastIndexOf('/');
      if (i > -1)
        this.c = this.b.substring(i + 1);
      else
        this.c = this.b;
    }
  }

  public SftpFile getParent()
    throws SshException, SftpStatusException
  {
    if (this.b.lastIndexOf('/') == -1)
    {
      str1 = this.e.getDefaultDirectory();
      return this.e.getFile(str1);
    }
    String str1 = this.e.getAbsolutePath(this.b);
    if (str1.equals("/"))
      return null;
    if ((this.c.equals(".")) || (this.c.equals("..")))
      return this.e.getFile(str1).getParent();
    int i = str1.lastIndexOf('/');
    String str2 = str1.substring(0, i);
    if (str2.equals(""))
      str2 = "/";
    return this.e.getFile(str2);
  }

  public String toString()
  {
    return this.b;
  }

  public int hashCode()
  {
    return this.b.hashCode();
  }

  public String getLongname()
  {
    return this.f;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof SftpFile))
    {
      boolean bool = ((SftpFile)paramObject).getAbsolutePath().equals(this.b);
      if ((this.g == null) && (((SftpFile)paramObject).g == null))
        return bool;
      if ((this.g != null) && (((SftpFile)paramObject).g != null))
        for (int i = 0; i < this.g.length; i++)
          if (((SftpFile)paramObject).g[i] != this.g[i])
            return false;
      return bool;
    }
    return false;
  }

  public void delete()
    throws SftpStatusException, SshException
  {
    if (this.e == null)
      throw new SshException("Instance not connected to SFTP subsystem", 4);
    if (isDirectory())
      this.e.removeDirectory(getAbsolutePath());
    else
      this.e.removeFile(getAbsolutePath());
  }

  public boolean canWrite()
    throws SftpStatusException, SshException
  {
    return (getAttributes().getPermissions().longValue() & 0x80) == 128L;
  }

  public boolean canRead()
    throws SftpStatusException, SshException
  {
    return (getAttributes().getPermissions().longValue() & 0x100) == 256L;
  }

  public boolean isOpen()
  {
    if (this.e == null)
      return false;
    return this.e.c(this.g);
  }

  void b(byte[] paramArrayOfByte)
  {
    this.g = paramArrayOfByte;
  }

  public byte[] getHandle()
  {
    return this.g;
  }

  void b(SftpSubsystemChannel paramSftpSubsystemChannel)
  {
    this.e = paramSftpSubsystemChannel;
  }

  public SftpSubsystemChannel getSFTPChannel()
  {
    return this.e;
  }

  public String getFilename()
  {
    return this.c;
  }

  public SftpFileAttributes getAttributes()
    throws SftpStatusException, SshException
  {
    if (this.d == null)
      this.d = this.e.getAttributes(getAbsolutePath());
    return this.d;
  }

  public String getAbsolutePath()
  {
    return this.b;
  }

  public void close()
    throws SftpStatusException, SshException
  {
    this.e.closeFile(this);
  }

  public boolean isDirectory()
    throws SftpStatusException, SshException
  {
    return getAttributes().isDirectory();
  }

  public boolean isFile()
    throws SftpStatusException, SshException
  {
    return getAttributes().isFile();
  }

  public boolean isLink()
    throws SftpStatusException, SshException
  {
    return getAttributes().isLink();
  }

  public boolean isFifo()
    throws SftpStatusException, SshException
  {
    return (getAttributes().getPermissions().longValue() & 0x1000) == 4096L;
  }

  public boolean isBlock()
    throws SftpStatusException, SshException
  {
    return (getAttributes().getPermissions().longValue() & 0x6000) == 24576L;
  }

  public boolean isCharacter()
    throws SftpStatusException, SshException
  {
    return (getAttributes().getPermissions().longValue() & 0x2000) == 8192L;
  }

  public boolean isSocket()
    throws SftpStatusException, SshException
  {
    return (getAttributes().getPermissions().longValue() & 0xC000) == 49152L;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.sftp.SftpFile
 * JD-Core Version:    0.6.0
 */