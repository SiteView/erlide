package com.sshtools.publickey;

import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.util.Base64;
import com.maverick.util.ByteArrayReader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

class F
  implements SshPrivateKeyFile
{
  byte[] F;

  F(byte[] paramArrayOfByte)
    throws IOException
  {
    if (!B(paramArrayOfByte))
      throw new IOException("Key is not formatted in the PuTTY key format!");
    this.F = paramArrayOfByte;
  }

  public boolean supportsPassphraseChange()
  {
    return false;
  }

  public String getType()
  {
    return "PuTTY";
  }

  public boolean isPassphraseProtected()
  {
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.F)));
    try
    {
      String str1 = localBufferedReader.readLine();
      if ((str1 != null) && ((str1.startsWith("PuTTY-User-Key-File-2:")) || (str1.equals("PuTTY-User-Key-File-1:"))))
      {
        str1 = localBufferedReader.readLine();
        if ((str1 != null) && (str1.startsWith("Encryption:")))
        {
          String str2 = str1.substring(str1.indexOf(":") + 1).trim();
          if (str2.equals("aes256-cbc"))
            return ComponentManager.getInstance().supportedSsh2CiphersCS().contains(str2);
        }
      }
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  public static boolean B(byte[] paramArrayOfByte)
  {
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(paramArrayOfByte)));
    try
    {
      String str = localBufferedReader.readLine();
      return (str != null) && ((str.startsWith("PuTTY-User-Key-File-2:")) || (str.equals("PuTTY-User-Key-File-1:")));
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public SshKeyPair toKeyPair(String paramString)
    throws IOException, InvalidPassphraseException
  {
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.F)));
    int i = 0;
    try
    {
      String str1 = localBufferedReader.readLine();
      if ((str1 != null) && ((str1.startsWith("PuTTY-User-Key-File-2:")) || (str1.equals("PuTTY-User-Key-File-1:"))))
      {
        int j = str1.startsWith("PuTTY-User-Key-File-2:") ? 2 : 1;
        String str2 = str1.substring(str1.indexOf(":") + 1).trim();
        str1 = localBufferedReader.readLine();
        if ((str1 != null) && (str1.startsWith("Encryption:")))
        {
          String str3 = str1.substring(str1.indexOf(":") + 1).trim();
          str1 = localBufferedReader.readLine();
          if ((str1 != null) && (str1.startsWith("Comment:")))
          {
            str1 = localBufferedReader.readLine();
            if ((str1 != null) && (str1.startsWith("Public-Lines:")))
              try
              {
                int k = Integer.parseInt(str1.substring(str1.indexOf(":") + 1).trim());
                String str4 = "";
                for (int m = 0; m < k; m++)
                {
                  str1 = localBufferedReader.readLine();
                  if (str1 != null)
                    str4 = str4 + str1;
                  else
                    throw new IOException("Corrupt public key data in PuTTY private key");
                }
                ByteArrayReader localByteArrayReader = new ByteArrayReader(Base64.decode(str4));
                str1 = localBufferedReader.readLine();
                if ((str1 != null) && (str1.startsWith("Private-Lines:")))
                {
                  int n = Integer.parseInt(str1.substring(str1.indexOf(":") + 1).trim());
                  String str5 = "";
                  for (int i1 = 0; i1 < n; i1++)
                  {
                    str1 = localBufferedReader.readLine();
                    if (str1 != null)
                      str5 = str5 + str1;
                    else
                      throw new IOException("Corrupt private key data in PuTTY private key");
                  }
                  byte[] arrayOfByte = Base64.decode(str5);
                  Object localObject2;
                  Object localObject3;
                  Object localObject4;
                  Object localObject5;
                  Object localObject6;
                  if (str3.equals("aes256-cbc"))
                  {
                    localObject1 = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance(str3);
                    localObject2 = new byte[40];
                    localObject3 = new byte[40];
                    localObject4 = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("SHA-1");
                    ((Digest)localObject4).putInt(0);
                    ((Digest)localObject4).putBytes(paramString.getBytes());
                    localObject5 = ((Digest)localObject4).doFinal();
                    ((Digest)localObject4).putInt(1);
                    ((Digest)localObject4).putBytes(paramString.getBytes());
                    localObject6 = ((Digest)localObject4).doFinal();
                    System.arraycopy(localObject5, 0, localObject3, 0, 20);
                    System.arraycopy(localObject6, 0, localObject3, 20, 20);
                    ((SshCipher)localObject1).init(1, localObject2, localObject3);
                    ((SshCipher)localObject1).transform(arrayOfByte);
                    i = 1;
                  }
                  Object localObject1 = new ByteArrayReader(arrayOfByte);
                  if (str2.equals("ssh-dss"))
                  {
                    localByteArrayReader.readString();
                    localObject2 = localByteArrayReader.readBigInteger();
                    localObject3 = localByteArrayReader.readBigInteger();
                    localObject4 = localByteArrayReader.readBigInteger();
                    localObject5 = localByteArrayReader.readBigInteger();
                    localObject6 = ((ByteArrayReader)localObject1).readBigInteger();
                    if (j == 1);
                    SshKeyPair localSshKeyPair = new SshKeyPair();
                    SshDsaPublicKey localSshDsaPublicKey = ComponentManager.getInstance().createDsaPublicKey((BigInteger)localObject2, (BigInteger)localObject3, (BigInteger)localObject4, (BigInteger)localObject5);
                    localSshKeyPair.setPublicKey(localSshDsaPublicKey);
                    localSshKeyPair.setPrivateKey(ComponentManager.getInstance().createDsaPrivateKey((BigInteger)localObject2, (BigInteger)localObject3, (BigInteger)localObject4, (BigInteger)localObject6, localSshDsaPublicKey.getY()));
                    return localSshKeyPair;
                  }
                  if (str2.equals("ssh-rsa"))
                  {
                    localByteArrayReader.readString();
                    localObject2 = localByteArrayReader.readBigInteger();
                    localObject3 = localByteArrayReader.readBigInteger();
                    localObject4 = ((ByteArrayReader)localObject1).readBigInteger();
                    localObject5 = new SshKeyPair();
                    ((SshKeyPair)localObject5).setPublicKey(ComponentManager.getInstance().createRsaPublicKey((BigInteger)localObject3, (BigInteger)localObject2, 2));
                    ((SshKeyPair)localObject5).setPrivateKey(ComponentManager.getInstance().createRsaPrivateKey((BigInteger)localObject3, (BigInteger)localObject4));
                    return localObject5;
                  }
                  throw new IOException("Unexpected key type " + str2);
                }
              }
              catch (NumberFormatException localNumberFormatException)
              {
              }
              catch (OutOfMemoryError localOutOfMemoryError)
              {
              }
          }
        }
      }
    }
    catch (Throwable localThrowable)
    {
      if (i == 0)
        throw new IOException("The PuTTY key could not be read! " + localThrowable.getMessage());
    }
    if (i != 0)
      throw new InvalidPassphraseException();
    throw new IOException("The PuTTY key could not be read! Invalid format");
  }

  public void changePassphrase(String paramString1, String paramString2)
    throws IOException
  {
    throw new IOException("Changing passphrase is not supported by the PuTTY key format engine");
  }

  public byte[] getFormattedKey()
    throws IOException
  {
    return this.F;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.F
 * JD-Core Version:    0.6.0
 */