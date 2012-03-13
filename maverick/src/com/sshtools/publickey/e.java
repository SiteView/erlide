package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshCipher;
import com.maverick.util.Base64;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Hashtable;

class e extends b
{
  private LineNumberReader f;
  private String g;
  private Hashtable i;
  private byte[] h;

  public e(Reader paramReader)
    throws IOException
  {
    this.f = new LineNumberReader(paramReader);
    d();
  }

  private void d()
    throws IOException
  {
    String str1;
    while ((str1 = this.f.readLine()) != null)
    {
      if ((!str1.startsWith("-----")) || (!str1.endsWith("-----")))
        continue;
      if (str1.startsWith("-----BEGIN "))
        this.g = str1.substring("-----BEGIN ".length(), str1.length() - "-----".length());
      else
        throw new IOException("Invalid PEM boundary at line " + this.f.getLineNumber() + ": " + str1);
    }
    this.i = new Hashtable();
    while ((str1 = this.f.readLine()) != null)
    {
      int j = str1.indexOf(':');
      if (j == -1)
        break;
      String str2 = str1.substring(0, j).trim();
      String str3;
      if (str1.endsWith("\\"))
      {
        str3 = str1.substring(j + 1, str1.length() - 1).trim();
        StringBuffer localStringBuffer2 = new StringBuffer(str3);
        while ((str1 = this.f.readLine()) != null)
        {
          if (str1.endsWith("\\"))
          {
            localStringBuffer2.append(" ").append(str1.substring(0, str1.length() - 1).trim());
            continue;
          }
          localStringBuffer2.append(" ").append(str1.trim());
        }
      }
      else
      {
        str3 = str1.substring(j + 1).trim();
        this.i.put(str2, str3);
      }
    }
    if (str1 == null)
      throw new IOException("The key format is invalid! OpenSSH formatted keys must begin with -----BEGIN RSA or -----BEGIN DSA");
    StringBuffer localStringBuffer1 = new StringBuffer(str1);
    while ((str1 = this.f.readLine()) != null)
    {
      if ((str1.startsWith("-----")) && (str1.endsWith("-----")))
      {
        if (str1.startsWith("-----END " + this.g))
          break;
        throw new IOException("Invalid PEM end boundary at line " + this.f.getLineNumber() + ": " + str1);
      }
      localStringBuffer1.append(str1);
    }
    this.h = Base64.decode(localStringBuffer1.toString());
  }

  public Hashtable c()
  {
    return this.i;
  }

  public String b()
  {
    return this.g;
  }

  public byte[] c(String paramString)
    throws IOException
  {
    try
    {
      String str1 = (String)this.i.get("DEK-Info");
      if (str1 != null)
      {
        int j = str1.indexOf(',');
        String str2 = str1.substring(0, j);
        if (!"DES-EDE3-CBC".equals(str2))
          throw new IOException("Unsupported passphrase algorithm: " + str2);
        String str3 = str1.substring(j + 1);
        byte[] arrayOfByte1 = new byte[str3.length() / 2];
        for (int k = 0; k < str3.length(); k += 2)
          arrayOfByte1[(k / 2)] = (byte)Integer.parseInt(str3.substring(k, k + 2), 16);
        byte[] arrayOfByte2 = b(paramString, arrayOfByte1, 24);
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance("3des-cbc");
        localSshCipher.init(1, arrayOfByte1, arrayOfByte2);
        byte[] arrayOfByte3 = new byte[this.h.length];
        localSshCipher.transform(this.h, 0, arrayOfByte3, 0, arrayOfByte3.length);
        return arrayOfByte3;
      }
      return this.h;
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.e
 * JD-Core Version:    0.6.0
 */