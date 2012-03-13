package com.maverick.ssh.components.jce;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ArcFour extends AbstractJCECipher
{
  public ArcFour()
    throws IOException
  {
    super("ARCFOUR", "ARCFOUR", 16, "arcfour");
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IOException
  {
    try
    {
      this.W = (JCEProvider.getProviderForAlgorithm(this.X) == null ? Cipher.getInstance(this.X) : Cipher.getInstance(this.X, JCEProvider.getProviderForAlgorithm(this.X)));
      if (this.W == null)
        throw new IOException("Failed to create cipher engine for " + this.X);
      byte[] arrayOfByte = new byte[this.Y];
      System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, arrayOfByte.length);
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte, this.Z);
      this.W.init(paramInt == 0 ? 1 : 2, localSecretKeySpec);
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new IOException("Padding type not supported");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new IOException("Algorithm not supported:" + this.X);
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new IOException("Invalid encryption key");
    }
  }

  public int getBlockSize()
  {
    return 8;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.ArcFour
 * JD-Core Version:    0.6.0
 */