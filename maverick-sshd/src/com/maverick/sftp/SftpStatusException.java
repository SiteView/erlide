package com.maverick.sftp;

public class SftpStatusException extends Exception
{
  public static final int SSH_FX_OK = 0;
  public static final int SSH_FX_EOF = 1;
  public static final int SSH_FX_NO_SUCH_FILE = 2;
  public static final int SSH_FX_PERMISSION_DENIED = 3;
  public static final int SSH_FX_FAILURE = 4;
  public static final int SSH_FX_BAD_MESSAGE = 5;
  public static final int SSH_FX_NO_CONNECTION = 6;
  public static final int SSH_FX_CONNECTION_LOST = 7;
  public static final int SSH_FX_OP_UNSUPPORTED = 8;
  public static final int SSH_FX_INVALID_HANDLE = 9;
  public static final int SSH_FX_NO_SUCH_PATH = 10;
  public static final int SSH_FX_FILE_ALREADY_EXISTS = 11;
  public static final int SSH_FX_WRITE_PROTECT = 12;
  public static final int SSH_FX_NO_MEDIA = 13;
  public static final int INVALID_HANDLE = 100;
  public static final int INVALID_RESUME_STATE = 101;
  public static final int INVALID_TEXT_MODE = 102;
  int A;

  public SftpStatusException(int paramInt, String paramString)
  {
    super(getStatusText(paramInt) + ": " + paramString);
    this.A = paramInt;
  }

  public SftpStatusException(int paramInt)
  {
    this(paramInt, getStatusText(paramInt));
  }

  public int getStatus()
  {
    return this.A;
  }

  public static String getStatusText(int paramInt)
  {
    switch (paramInt)
    {
    case 0:
      return "OK";
    case 1:
      return "EOF";
    case 2:
      return "No such file.";
    case 3:
      return "Permission denied.";
    case 4:
      return "Server responded with an unknown failure.";
    case 5:
      return "Server responded to a bad message.";
    case 6:
      return "No connection available.";
    case 7:
      return "Connection lost.";
    case 8:
      return "The operation is unsupported.";
    case 9:
    case 100:
      return "Invalid file handle.";
    case 10:
      return "No such path.";
    case 11:
      return "File already exists.";
    case 12:
      return "Write protect error.";
    case 13:
      return "No media at location";
    case 101:
      return "Invalid resume state";
    }
    return "Unknown status type " + String.valueOf(paramInt);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sftp.SftpStatusException
 * JD-Core Version:    0.6.0
 */