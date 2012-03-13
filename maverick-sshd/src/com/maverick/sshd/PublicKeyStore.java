package com.maverick.sshd;

import com.maverick.ssh.components.SshPublicKey;
import com.maverick.sshd.platform.AuthenticationProvider;
import java.net.SocketAddress;

public abstract interface PublicKeyStore
{
  public abstract boolean isAuthorizedKey(SshPublicKey paramSshPublicKey, byte[] paramArrayOfByte, SocketAddress paramSocketAddress, AuthenticationProvider paramAuthenticationProvider);
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.PublicKeyStore
 * JD-Core Version:    0.6.0
 */