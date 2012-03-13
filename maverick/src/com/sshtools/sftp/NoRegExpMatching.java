package com.sshtools.sftp;

import com.maverick.sftp.SftpFile;
import com.maverick.sftp.SftpStatusException;
import com.maverick.ssh.SshException;
import java.io.File;

public class NoRegExpMatching
  implements RegularExpressionMatching
{
  public String[] matchFileNamesWithPattern(File[] paramArrayOfFile, String paramString)
    throws SshException, SftpStatusException
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = paramArrayOfFile[0].getName();
    return arrayOfString;
  }

  public SftpFile[] matchFilesWithPattern(SftpFile[] paramArrayOfSftpFile, String paramString)
    throws SftpStatusException, SshException
  {
    return paramArrayOfSftpFile;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.sftp.NoRegExpMatching
 * JD-Core Version:    0.6.0
 */