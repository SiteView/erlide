package com.sshtools.sftp;

import com.maverick.sftp.SftpFile;
import com.maverick.sftp.SftpFileAttributes;
import com.maverick.sftp.SftpStatusException;
import com.maverick.ssh.SshException;
import com.maverick.util.UnsignedInteger64;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class DirectoryOperation
{
  Vector e = new Vector();
  Vector d = new Vector();
  Vector b = new Vector();
  Vector g = new Vector();
  Vector f = new Vector();
  Hashtable c = new Hashtable();

  void b(File paramFile)
  {
    this.d.addElement(paramFile);
  }

  void b(File paramFile, SftpStatusException paramSftpStatusException)
  {
    this.c.put(paramFile, paramSftpStatusException);
  }

  void d(File paramFile)
  {
    this.b.addElement(paramFile);
  }

  void e(File paramFile)
  {
    this.g.addElement(paramFile);
  }

  void c(File paramFile)
  {
    this.e.addElement(paramFile);
  }

  void b(SftpFile paramSftpFile)
  {
    this.d.addElement(paramSftpFile);
  }

  void e(SftpFile paramSftpFile)
  {
    this.b.addElement(paramSftpFile);
  }

  void c(SftpFile paramSftpFile)
  {
    this.g.addElement(paramSftpFile);
  }

  void d(SftpFile paramSftpFile)
  {
    this.e.addElement(paramSftpFile);
  }

  public Vector getNewFiles()
  {
    return this.d;
  }

  public Vector getUpdatedFiles()
  {
    return this.b;
  }

  public Vector getUnchangedFiles()
  {
    return this.e;
  }

  public Vector getDeletedFiles()
  {
    return this.g;
  }

  public Hashtable getFailedTransfers()
  {
    return this.c;
  }

  public boolean containsFile(File paramFile)
  {
    return (this.e.contains(paramFile)) || (this.d.contains(paramFile)) || (this.b.contains(paramFile)) || (this.g.contains(paramFile)) || (this.f.contains(paramFile)) || (this.c.containsKey(paramFile));
  }

  public boolean containsFile(SftpFile paramSftpFile)
  {
    return (this.e.contains(paramSftpFile)) || (this.d.contains(paramSftpFile)) || (this.b.contains(paramSftpFile)) || (this.g.contains(paramSftpFile)) || (this.f.contains(paramSftpFile.getAbsolutePath())) || (this.c.containsKey(paramSftpFile));
  }

  public void addDirectoryOperation(DirectoryOperation paramDirectoryOperation, File paramFile)
  {
    b(paramDirectoryOperation.getUpdatedFiles(), this.b);
    b(paramDirectoryOperation.getNewFiles(), this.d);
    b(paramDirectoryOperation.getUnchangedFiles(), this.e);
    b(paramDirectoryOperation.getDeletedFiles(), this.g);
    Enumeration localEnumeration = paramDirectoryOperation.c.keys();
    while (localEnumeration.hasMoreElements())
    {
      Object localObject = localEnumeration.nextElement();
      this.c.put(localObject, paramDirectoryOperation.c.get(localObject));
    }
    this.f.addElement(paramFile);
  }

  void b(Vector paramVector1, Vector paramVector2)
  {
    Enumeration localEnumeration = paramVector1.elements();
    while (localEnumeration.hasMoreElements())
      paramVector2.addElement(localEnumeration.nextElement());
  }

  public int getFileCount()
  {
    return this.d.size() + this.b.size();
  }

  public void addDirectoryOperation(DirectoryOperation paramDirectoryOperation, String paramString)
  {
    b(paramDirectoryOperation.getUpdatedFiles(), this.b);
    b(paramDirectoryOperation.getNewFiles(), this.d);
    b(paramDirectoryOperation.getUnchangedFiles(), this.e);
    b(paramDirectoryOperation.getDeletedFiles(), this.g);
    Enumeration localEnumeration = paramDirectoryOperation.c.keys();
    while (localEnumeration.hasMoreElements())
    {
      Object localObject = localEnumeration.nextElement();
      this.c.put(localObject, paramDirectoryOperation.c.get(localObject));
    }
    this.f.addElement(paramString);
  }

  public long getTransferSize()
    throws SftpStatusException, SshException
  {
    long l = 0L;
    Enumeration localEnumeration = this.d.elements();
    Object localObject;
    File localFile;
    SftpFile localSftpFile;
    while (localEnumeration.hasMoreElements())
    {
      localObject = localEnumeration.nextElement();
      if ((localObject instanceof File))
      {
        localFile = (File)localObject;
        if (!localFile.isFile())
          continue;
        l += localFile.length();
        continue;
      }
      if (!(localObject instanceof SftpFile))
        continue;
      localSftpFile = (SftpFile)localObject;
      if (!localSftpFile.isFile())
        continue;
      l += localSftpFile.getAttributes().getSize().longValue();
    }
    localEnumeration = this.b.elements();
    while (localEnumeration.hasMoreElements())
    {
      localObject = localEnumeration.nextElement();
      if ((localObject instanceof File))
      {
        localFile = (File)localObject;
        if (!localFile.isFile())
          continue;
        l += localFile.length();
        continue;
      }
      if (!(localObject instanceof SftpFile))
        continue;
      localSftpFile = (SftpFile)localObject;
      if (!localSftpFile.isFile())
        continue;
      l += localSftpFile.getAttributes().getSize().longValue();
    }
    return l;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.sftp.DirectoryOperation
 * JD-Core Version:    0.6.0
 */