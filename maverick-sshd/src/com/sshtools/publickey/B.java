package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.SshCipher;
import com.maverick.ssh.components.SshSecureRandomGenerator;
import com.maverick.util.Base64;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;

class B extends A
{
  private String B;
  private Hashtable D = new Hashtable();
  private byte[] C;

  public void A(Writer paramWriter)
  {
    PrintWriter localPrintWriter = new PrintWriter(paramWriter, true);
    localPrintWriter.println("-----BEGIN " + this.B + "-----");
    if (!this.D.isEmpty())
    {
      Enumeration localEnumeration = this.D.keys();
      while (localEnumeration.hasMoreElements())
      {
        String str1 = (String)localEnumeration.nextElement();
        String str2 = (String)this.D.get(str1);
        localPrintWriter.print(str1 + ": ");
        if (str1.length() + str2.length() + 2 > 75)
        {
          int i = Math.max(75 - str1.length() - 2, 0);
          localPrintWriter.println(str2.substring(0, i) + "\\");
          while (i < str2.length())
          {
            if (i + 75 >= str2.length())
              localPrintWriter.println(str2.substring(i));
            else
              localPrintWriter.println(str2.substring(i, i + 75) + "\\");
            i += 75;
          }
        }
        else
        {
          localPrintWriter.println(str2);
        }
      }
      localPrintWriter.println();
    }
    localPrintWriter.println(Base64.encodeBytes(this.C, false));
    localPrintWriter.println("-----END " + this.B + "-----");
  }

  public void A(byte[] paramArrayOfByte, String paramString)
    throws IOException
  {
    try
    {
      if ((paramString == null) || (paramString.length() == 0))
      {
        A(paramArrayOfByte);
        return;
      }
      byte[] arrayOfByte1 = new byte[8];
      ComponentManager.getInstance().getRND().nextBytes(arrayOfByte1);
      StringBuffer localStringBuffer = new StringBuffer(16);
      for (int i = 0; i < arrayOfByte1.length; i++)
      {
        localStringBuffer.append(A[((arrayOfByte1[i] & 0xFF) >> 4)]);
        localStringBuffer.append(A[(arrayOfByte1[i] & 0xF)]);
      }
      this.D.put("DEK-Info", "DES-EDE3-CBC," + localStringBuffer);
      this.D.put("Proc-Type", "4,ENCRYPTED");
      byte[] arrayOfByte2 = A(paramString, arrayOfByte1, 24);
      SshCipher localSshCipher = (SshCipher)ComponentManager.getInstance().supportedSsh2CiphersCS().getInstance("3des-cbc");
      localSshCipher.init(0, arrayOfByte1, arrayOfByte2);
      int j = localSshCipher.getBlockSize() - paramArrayOfByte.length % localSshCipher.getBlockSize();
      if (j > 0)
      {
        byte[] arrayOfByte3 = new byte[paramArrayOfByte.length + j];
        System.arraycopy(paramArrayOfByte, 0, arrayOfByte3, 0, paramArrayOfByte.length);
        for (int k = paramArrayOfByte.length; k < arrayOfByte3.length; k++)
          arrayOfByte3[k] = (byte)j;
        paramArrayOfByte = arrayOfByte3;
      }
      localSshCipher.transform(paramArrayOfByte, 0, paramArrayOfByte, 0, paramArrayOfByte.length);
      A(paramArrayOfByte);
    }
    catch (SshException localSshException)
    {
      throw new SshIOException(localSshException);
    }
  }

  public void A(byte[] paramArrayOfByte)
  {
    this.C = paramArrayOfByte;
  }

  public void A(String paramString)
  {
    this.B = paramString;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.B
 * JD-Core Version:    0.6.0
 */