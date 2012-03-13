package com.maverick.sshd.vfs;

import com.maverick.sshd.SftpFileAttributes;
import java.io.FileNotFoundException;

public abstract interface VFSPermissionHandler
{
  public abstract boolean canRead(String paramString1, String paramString2, String paramString3);

  public abstract boolean canWrite(String paramString1, String paramString2, String paramString3);

  public abstract boolean canExecute(String paramString1, String paramString2, String paramString3);

  public abstract SftpFileAttributes getPermissions(String paramString1, String paramString2)
    throws FileNotFoundException;

  public abstract String getVFSHomeDirectory(String paramString)
    throws FileNotFoundException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.vfs.VFSPermissionHandler
 * JD-Core Version:    0.6.0
 */