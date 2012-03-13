package com.maverick.sftp;

public abstract interface FileTransferProgress
{
  public abstract void started(long paramLong, String paramString);

  public abstract boolean isCancelled();

  public abstract void progressed(long paramLong);

  public abstract void completed();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.sftp.FileTransferProgress
 * JD-Core Version:    0.6.0
 */