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

class D extends A
{
  private LineNumberReader E;
  private String F;
  private Hashtable H;
  private byte[] G;

  public D(Reader paramReader)
    throws IOException
  {
    this.E = new LineNumberReader(paramReader);
    C();
  }

  private void C()
    throws IOException
  {
    String str1;
    while ((str1 = this.E.readLine()) != null)
    {
      if ((!str1.startsWith("-----")) || (!str1.endsWith("-----")))
        continue;
      if (str1.startsWith("-----BEGIN "))
        this.F = str1.substring("-----BEGIN ".length(), str1.length() - "-----".length());
      else
        throw new IOException("Invalid PEM boundary at line " + this.E.getLineNumber() + ": " + str1);
    }
    this.H = new Hashtable();
    while ((str1 = this.E.readLine()) != null)
    {
      int i = str1.indexOf(':');
      if (i == -1)
        break;
      String str2 = str1.substring(0, i).trim();
      String str3;
      if (str1.endsWith("\\"))
      {
        str3 = str1.substring(i + 1, str1.length() - 1).trim();
        StringBuffer localStringBuffer2 = new StringBuffer(str3);
        while ((str1 = this.E.readLine()) != null)
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
        str3 = str1.substring(i + 1).trim();
        this.H.put(str2, str3);
      }
    }
    if (str1 == null)
      throw new IOException("The key format is invalid! OpenSSH formatted keys must begin with -----BEGIN RSA or -----BEGIN DSA");
    StringBuffer localStringBuffer1 = new StringBuffer(str1);
    while ((str1 = this.E.readLine()) != null)
    {
      if ((str1.startsWith("-----")) && (str1.endsWith("-----")))
      {
        if (str1.startsWith("-----END " + this.F))
          break;
        throw new IOException("Invalid PEM end boundary at line " + this.E.getLineNumber() + ": " + str1);
      }
      localStringBuffer1.append(str1);
    }
    this.G = Base64.decode(localStringBuffer1.toString());
  }

  public Hashtable B()
  {
    return this.H;
  }

  public String A()
  {
    return this.F;
  }

  public byte[] B(String paramString)
    throws IOException
  {
    try
    {
      String str1 = (String)this.H.get("DEK-Info");
      if (str1 != null)
      {
        int i = str1.indexOf(',');
        String str2 = str1.substring(0, i);
        if (!"DES-EDE3-CBC".equals(str2))
          throw new IOException("Unsupported passphrase algorithm: " + str2);
        String str3 = str1.substring(i + 1);
        byte[] arrayOfByte1 = new byte[str3.length() / 2];
        for (int j = 0; j < str3.length(); j += 2)
          arrayOfByte1[(j / 2)] = (byte)Integer.parseInt(str3.substring(j, j + 2), 16);
        byte[] arrayOfByte2 = A(paramString, arrayOfByte1, 24);
        SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance("3des-cbc");
        localSshCipher.init(1, arrayOfByte1, arrayOfByte2);
        byte[] arrayOfByte3 = new byte[this.G.length];
        localSshCipher.transform(this.G, 0, arrayOfByte3, 0, arrayOfByte3.length);
        return arrayOfByte3;
      }
      return this.G;
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.D
 * JD-Core Version:    0.6.0
 */