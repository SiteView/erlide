package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.util.Base64;
import java.io.IOException;

public class OpenSSHPublicKeyFile
  implements SshPublicKeyFile
{
  byte[] S;
  String T;

  OpenSSHPublicKeyFile(byte[] paramArrayOfByte)
    throws IOException
  {
    this.S = paramArrayOfByte;
    toPublicKey();
  }

  OpenSSHPublicKeyFile(SshPublicKey paramSshPublicKey, String paramString)
    throws IOException
  {
    try
    {
      String str = paramSshPublicKey.getAlgorithm() + " " + Base64.encodeBytes(paramSshPublicKey.getEncoded(), true);
      if (paramString != null)
        str = str + " " + paramString;
      this.S = str.getBytes();
    }
    catch (SshException localSshException)
    {
      throw new IOException("Failed to encode public key");
    }
  }

  public String toString()
  {
    return new String(this.S);
  }

  public byte[] getFormattedKey()
  {
    return this.S;
  }

  public SshPublicKey toPublicKey()
    throws IOException
  {
    String str1 = new String(this.S);
    int i = str1.indexOf(" ");
    if (i > 0)
    {
      String str2 = str1.substring(0, i);
      if ((str2.equals("ssh-dss")) || (str2.equals("ssh-rsa")))
      {
        int j = str1.indexOf(" ", i + 1);
        if (j != -1)
        {
          str3 = str1.substring(i + 1, j);
          if (str1.length() > j)
            this.T = str1.substring(j + 1).trim();
          return SshPublicKeyFileFactory.decodeSSH2PublicKey(str2, Base64.decode(str3));
        }
        String str3 = str1.substring(i + 1);
        return SshPublicKeyFileFactory.decodeSSH2PublicKey(str2, Base64.decode(str3));
      }
    }
    throw new IOException("Key format not supported!");
  }

  public String getComment()
  {
    return this.T;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.OpenSSHPublicKeyFile
 * JD-Core Version:    0.6.0
 */