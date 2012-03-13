package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshKeyPair;
import java.io.IOException;

public class SshKeyPairGenerator
{
  public static final String SSH1_RSA = "rsa1";
  public static final String SSH2_RSA = "ssh-rsa";
  public static final String SSH2_DSA = "ssh-dss";

  public static SshKeyPair generateKeyPair(String paramString, int paramInt)
    throws IOException, SshException
  {
    if ((!"rsa1".equalsIgnoreCase(paramString)) && (!"ssh-rsa".equalsIgnoreCase(paramString)) && (!"ssh-dss".equalsIgnoreCase(paramString)))
      throw new IOException(paramString + " is not a supported key algorithm!");
    SshKeyPair localSshKeyPair = new SshKeyPair();
    if ("rsa1".equalsIgnoreCase(paramString))
      localSshKeyPair = ComponentManager.getInstance().generateRsaKeyPair(paramInt, 1);
    else if ("ssh-rsa".equalsIgnoreCase(paramString))
      localSshKeyPair = ComponentManager.getInstance().generateRsaKeyPair(paramInt, 2);
    else
      localSshKeyPair = ComponentManager.getInstance().generateDsaKeyPair(paramInt);
    return localSshKeyPair;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.SshKeyPairGenerator
 * JD-Core Version:    0.6.0
 */