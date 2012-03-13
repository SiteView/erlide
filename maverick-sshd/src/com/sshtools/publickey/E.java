package com.sshtools.publickey;

import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import java.io.IOException;
import java.math.BigInteger;
import java.util.StringTokenizer;

class E
  implements SshPublicKeyFile
{
  String A;

  public E(byte[] paramArrayOfByte)
    throws IOException
  {
    this.A = new String(paramArrayOfByte);
    toPublicKey();
  }

  public E(SshPublicKey paramSshPublicKey)
    throws IOException
  {
    if ((paramSshPublicKey instanceof SshRsaPublicKey))
    {
      this.A = (String.valueOf(((SshRsaPublicKey)paramSshPublicKey).getModulus().bitLength()) + " " + ((SshRsaPublicKey)paramSshPublicKey).getPublicExponent() + " " + ((SshRsaPublicKey)paramSshPublicKey).getModulus());
      toPublicKey();
    }
    else
    {
      throw new IOException("SSH1 public keys must be rsa");
    }
  }

  public byte[] getFormattedKey()
  {
    return this.A.getBytes();
  }

  public SshPublicKey toPublicKey()
    throws IOException
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(this.A.trim(), " ");
    try
    {
      Integer.parseInt((String)localStringTokenizer.nextElement());
      String str1 = (String)localStringTokenizer.nextElement();
      String str2 = (String)localStringTokenizer.nextElement();
      BigInteger localBigInteger1 = new BigInteger(str1);
      BigInteger localBigInteger2 = new BigInteger(str2);
      return ComponentManager.getInstance().createRsaPublicKey(localBigInteger2, localBigInteger1, 1);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new IOException("Invalid SSH1 public key format");
  }

  public String getComment()
  {
    return "";
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.E
 * JD-Core Version:    0.6.0
 */