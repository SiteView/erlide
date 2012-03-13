package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshPublicKey;
import java.io.IOException;

public class SECSHPublicKeyFile extends Base64EncodedFileFormat
  implements SshPublicKeyFile
{
  private static String P = "---- BEGIN SSH2 PUBLIC KEY ----";
  private static String R = "---- END SSH2 PUBLIC KEY ----";
  String O;
  byte[] Q;

  SECSHPublicKeyFile(byte[] paramArrayOfByte)
    throws IOException
  {
    super(P, R);
    this.Q = getKeyBlob(paramArrayOfByte);
    toPublicKey();
  }

  SECSHPublicKeyFile(SshPublicKey paramSshPublicKey, String paramString)
    throws IOException
  {
    super(P, R);
    try
    {
      this.O = paramSshPublicKey.getAlgorithm();
      this.Q = paramSshPublicKey.getEncoded();
      setComment(paramString);
      toPublicKey();
    }
    catch (SshException localSshException)
    {
      throw new IOException("Failed to encode public key");
    }
  }

  public String getComment()
  {
    return getHeaderValue("Comment");
  }

  public SshPublicKey toPublicKey()
    throws IOException
  {
    return SshPublicKeyFileFactory.decodeSSH2PublicKey(this.Q);
  }

  public byte[] getFormattedKey()
    throws IOException
  {
    return formatKey(this.Q);
  }

  public void setComment(String paramString)
  {
    setHeaderValue("Comment", (paramString.trim().startsWith("\"") ? "" : "\"") + paramString.trim() + (paramString.trim().endsWith("\"") ? "" : "\""));
  }

  public String toString()
  {
    try
    {
      return new String(getFormattedKey());
    }
    catch (IOException localIOException)
    {
    }
    return "Invalid encoding!";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.SECSHPublicKeyFile
 * JD-Core Version:    0.6.0
 */