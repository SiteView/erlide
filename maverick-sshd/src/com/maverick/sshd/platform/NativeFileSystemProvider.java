package com.maverick.sshd.platform;

import com.maverick.sshd.SessionChannel;
import com.maverick.sshd.SftpFile;
import com.maverick.sshd.SftpFileAttributes;
import com.maverick.sshd.SshContext;
import com.maverick.util.UnsignedInteger32;
import com.maverick.util.UnsignedInteger64;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract interface NativeFileSystemProvider
{
  public static final int OPEN_READ = 1;
  public static final int OPEN_WRITE = 2;
  public static final int OPEN_APPEND = 4;
  public static final int OPEN_CREATE = 8;
  public static final int OPEN_TRUNCATE = 16;
  public static final int OPEN_EXCLUSIVE = 32;
  public static final String AUTHORIZED_KEYS_STORE = "authorized_keys";
  public static final String SFTP = "sftp";
  public static final String SCP = "scp";

  public abstract void init(SessionChannel paramSessionChannel, SshContext paramSshContext)
    throws IOException;

  public abstract void init(byte[] paramArrayOfByte, SessionChannel paramSessionChannel, SshContext paramSshContext, String paramString)
    throws IOException;

  public abstract void closeFilesystem();

  public abstract boolean fileExists(String paramString)
    throws IOException;

  public abstract String getRealPath(String paramString)
    throws IOException, FileNotFoundException;

  public abstract boolean makeDirectory(String paramString)
    throws PermissionDeniedException, FileNotFoundException, IOException;

  public abstract SftpFileAttributes getFileAttributes(String paramString)
    throws IOException, FileNotFoundException, PermissionDeniedException;

  public abstract SftpFileAttributes getFileAttributes(byte[] paramArrayOfByte)
    throws IOException, InvalidHandleException, PermissionDeniedException;

  public abstract byte[] openDirectory(String paramString)
    throws PermissionDeniedException, FileNotFoundException, IOException;

  public abstract SftpFile[] readDirectory(byte[] paramArrayOfByte)
    throws InvalidHandleException, EOFException, IOException;

  public abstract byte[] openFile(String paramString, UnsignedInteger32 paramUnsignedInteger32, SftpFileAttributes paramSftpFileAttributes)
    throws PermissionDeniedException, FileNotFoundException, IOException;

  public abstract int readFile(byte[] paramArrayOfByte1, UnsignedInteger64 paramUnsignedInteger64, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
    throws InvalidHandleException, EOFException, IOException;

  public abstract void writeFile(byte[] paramArrayOfByte1, UnsignedInteger64 paramUnsignedInteger64, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
    throws InvalidHandleException, IOException;

  public abstract void closeFile(byte[] paramArrayOfByte)
    throws InvalidHandleException, IOException;

  public abstract void removeFile(String paramString)
    throws PermissionDeniedException, IOException, FileNotFoundException;

  public abstract void renameFile(String paramString1, String paramString2)
    throws PermissionDeniedException, FileNotFoundException, IOException;

  public abstract void removeDirectory(String paramString)
    throws PermissionDeniedException, FileNotFoundException, IOException;

  public abstract void setFileAttributes(String paramString, SftpFileAttributes paramSftpFileAttributes)
    throws PermissionDeniedException, IOException, FileNotFoundException;

  public abstract void setFileAttributes(byte[] paramArrayOfByte, SftpFileAttributes paramSftpFileAttributes)
    throws PermissionDeniedException, IOException, InvalidHandleException;

  public abstract SftpFile readSymbolicLink(String paramString)
    throws UnsupportedFileOperationException, FileNotFoundException, IOException, PermissionDeniedException;

  public abstract void createSymbolicLink(String paramString1, String paramString2)
    throws UnsupportedFileOperationException, FileNotFoundException, IOException, PermissionDeniedException;

  public abstract String getDefaultPath()
    throws FileNotFoundException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.platform.NativeFileSystemProvider
 * JD-Core Version:    0.6.0
 */