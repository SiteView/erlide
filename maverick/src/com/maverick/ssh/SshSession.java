package com.maverick.ssh;

import java.io.InputStream;
import java.io.OutputStream;

public abstract interface SshSession extends SshChannel
{
  public static final int EXITCODE_NOT_RECEIVED = -2147483648;

  public abstract boolean startShell()
    throws SshException;

  public abstract SshClient getClient();

  public abstract boolean executeCommand(String paramString)
    throws SshException;

  public abstract boolean executeCommand(String paramString1, String paramString2)
    throws SshException;

  public abstract boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
    throws SshException;

  public abstract boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, PseudoTerminalModes paramPseudoTerminalModes)
    throws SshException;

  public abstract boolean requestPseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws SshException;

  public abstract InputStream getInputStream()
    throws SshIOException;

  public abstract OutputStream getOutputStream()
    throws SshIOException;

  public abstract InputStream getStderrInputStream()
    throws SshIOException;

  public abstract void close();

  public abstract int exitCode();

  public abstract void changeTerminalDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws SshException;

  public abstract boolean isClosed();
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.SshSession
 * JD-Core Version:    0.6.0
 */