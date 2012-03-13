package com.maverick.sshd.platform;

import com.maverick.ssh.components.SshPublicKey;
import java.io.IOException;
import java.net.SocketAddress;

public abstract interface NativeAuthenticationProvider
{
  public abstract int getAuthenticationStatus();

  public abstract boolean logonUser(byte[] paramArrayOfByte, String paramString1, String paramString2, SocketAddress paramSocketAddress)
    throws PasswordChangeException;

  public abstract boolean logonUser(byte[] paramArrayOfByte, String paramString, SocketAddress paramSocketAddress);

  public abstract boolean logonUser(byte[] paramArrayOfByte, String paramString, SocketAddress paramSocketAddress, SshPublicKey paramSshPublicKey, boolean paramBoolean);

  public abstract void logoffUser(byte[] paramArrayOfByte, String paramString, SocketAddress paramSocketAddress);

  public abstract String getHomeDirectory(byte[] paramArrayOfByte, String paramString);

  public abstract boolean changePassword(byte[] paramArrayOfByte, String paramString1, String paramString2, String paramString3)
    throws PasswordChangeException;

  public abstract String getUserGroup(byte[] paramArrayOfByte, String paramString)
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.platform.NativeAuthenticationProvider
 * JD-Core Version:    0.6.0
 */