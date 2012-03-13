package com.sshtools.sftp;

import com.maverick.sftp.SftpFile;
import com.maverick.sftp.SftpStatusException;
import com.maverick.ssh.SshException;
import java.io.File;

public abstract interface RegularExpressionMatching
{
  public abstract SftpFile[] matchFilesWithPattern(SftpFile[] paramArrayOfSftpFile, String paramString)
    throws SftpStatusException, SshException;

  public abstract String[] matchFileNamesWithPattern(File[] paramArrayOfFile, String paramString)
    throws SftpStatusException, SshException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.sftp.RegularExpressionMatching
 * JD-Core Version:    0.6.0
 */