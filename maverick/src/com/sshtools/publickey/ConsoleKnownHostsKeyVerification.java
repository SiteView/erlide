package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshPublicKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ConsoleKnownHostsKeyVerification extends AbstractKnownHostsKeyVerification
{
  public ConsoleKnownHostsKeyVerification()
    throws SshException
  {
  }

  public ConsoleKnownHostsKeyVerification(String paramString)
    throws SshException
  {
    super(paramString);
  }

  public void onHostKeyMismatch(String paramString, SshPublicKey paramSshPublicKey1, SshPublicKey paramSshPublicKey2)
  {
    try
    {
      System.out.println("The host key supplied by " + paramString + "(" + paramSshPublicKey1.getAlgorithm() + ")" + " is: " + paramSshPublicKey2.getFingerprint());
      System.out.println("The current allowed key for " + paramString + " is: " + paramSshPublicKey1.getFingerprint());
      d(paramString, paramSshPublicKey2);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void onUnknownHost(String paramString, SshPublicKey paramSshPublicKey)
  {
    try
    {
      System.out.println("The host " + paramString + " is currently unknown to the system");
      System.out.println("The host key (" + paramSshPublicKey.getAlgorithm() + ") fingerprint is: " + paramSshPublicKey.getFingerprint());
      d(paramString, paramSshPublicKey);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  protected void onInvalidHostEntry(String paramString)
    throws SshException
  {
    System.out.println("Invalid host entry in " + getKnownHostsFile().getAbsolutePath());
    System.out.println(paramString);
  }

  private void d(String paramString, SshPublicKey paramSshPublicKey)
    throws SshException
  {
    String str1 = "";
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
    while ((!str1.equalsIgnoreCase("YES")) && (!str1.equalsIgnoreCase("NO")) && ((!str1.equalsIgnoreCase("ALWAYS")) || (!isHostFileWriteable())))
    {
      String str2 = isHostFileWriteable() ? "Yes|No|Always" : "Yes|No";
      if (!isHostFileWriteable())
        System.out.println("Always option disabled, host file is not writeable");
      System.out.print("Do you want to allow this host key? [" + str2 + "]: ");
      try
      {
        str1 = localBufferedReader.readLine();
      }
      catch (IOException localIOException)
      {
        throw new SshException("Failed to read response", 5);
      }
    }
    if (str1.equalsIgnoreCase("YES"))
      allowHost(paramString, paramSshPublicKey, false);
    if ((str1.equalsIgnoreCase("ALWAYS")) && (isHostFileWriteable()))
      allowHost(paramString, paramSshPublicKey, true);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.ConsoleKnownHostsKeyVerification
 * JD-Core Version:    0.6.0
 */