package com.sshtools.publickey;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshIOException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.ComponentManager;
import com.maverick.ssh.components.Digest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

class b
{
  protected static final char[] b = "0123456789ABCDEF".toCharArray();

  protected static byte[] b(String paramString, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    try
    {
      byte[] arrayOfByte1;
      try
      {
        arrayOfByte1 = paramString == null ? new byte[0] : paramString.getBytes("US-ASCII");
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new IOException("Mandatory US-ASCII character encoding is not supported by the VM");
      }
      Digest localDigest = (Digest)ComponentManager.getInstance().supportedDigests().getInstance("MD5");
      byte[] arrayOfByte2 = new byte[paramInt];
      int i = paramInt & 0xFFFFFFF0;
      if ((paramInt & 0xF) != 0)
        i += 16;
      byte[] arrayOfByte3 = new byte[i];
      int j = 0;
      while (j + 16 <= arrayOfByte3.length)
      {
        localDigest.putBytes(arrayOfByte1, 0, arrayOfByte1.length);
        localDigest.putBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
        byte[] arrayOfByte4 = localDigest.doFinal();
        System.arraycopy(arrayOfByte4, 0, arrayOfByte3, j, arrayOfByte4.length);
        j += arrayOfByte4.length;
        localDigest.putBytes(arrayOfByte4, 0, arrayOfByte4.length);
      }
      System.arraycopy(arrayOfByte3, 0, arrayOfByte2, 0, arrayOfByte2.length);
      return arrayOfByte2;
    }
    catch (SshException localSshException)
    {
    }
    throw new SshIOException(localSshException);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.b
 * JD-Core Version:    0.6.0
 */