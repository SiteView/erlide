package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshPublicKey;
import java.io.IOException;

public class SECSHPublicKeyFile extends Base64EncodedFileFormat
  implements SshPublicKeyFile
{
  private static String q = "---- BEGIN SSH2 PUBLIC KEY ----";
  private static String s = "---- END SSH2 PUBLIC KEY ----";
  String p;
  byte[] r;

  SECSHPublicKeyFile(byte[] paramArrayOfByte)
    throws IOException
  {
    super(q, s);
    this.r = getKeyBlob(paramArrayOfByte);
    toPublicKey();
  }

  SECSHPublicKeyFile(SshPublicKey paramSshPublicKey, String paramString)
    throws IOException
  {
    super(q, s);
    try
    {
      this.p = paramSshPublicKey.getAlgorithm();
      this.r = paramSshPublicKey.getEncoded();
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
    return SshPublicKeyFileFactory.decodeSSH2PublicKey(this.r);
  }

  public byte[] getFormattedKey()
    throws IOException
  {
    return formatKey(this.r);
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

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.SECSHPublicKeyFile
 * JD-Core Version:    0.6.0
 */