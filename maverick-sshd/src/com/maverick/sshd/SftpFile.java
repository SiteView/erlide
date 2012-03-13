package com.maverick.sshd;

public class SftpFile
{
  String B;
  SftpFileAttributes C;
  String A;

  public SftpFile(String paramString, SftpFileAttributes paramSftpFileAttributes)
  {
    this.A = paramString;
    int i = paramString.lastIndexOf('/');
    if (i > -1)
      this.B = paramString.substring(i + 1);
    else
      this.B = paramString;
    this.C = paramSftpFileAttributes;
  }

  public int hashCode()
  {
    return this.A.hashCode();
  }

  public SftpFile(String paramString)
  {
    this(paramString, new SftpFileAttributes());
  }

  public String getFilename()
  {
    return this.B;
  }

  public SftpFileAttributes getAttributes()
  {
    return this.C;
  }

  public String getAbsolutePath()
  {
    return this.A;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.SftpFile
 * JD-Core Version:    0.6.0
 */