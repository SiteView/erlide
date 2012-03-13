package com.maverick.ssh2;

import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshException;

public abstract interface AuthenticationClient extends SshAuthentication
{
  public abstract void authenticate(AuthenticationProtocol paramAuthenticationProtocol, String paramString)
    throws SshException, AuthenticationResult;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.AuthenticationClient
 * JD-Core Version:    0.6.0
 */