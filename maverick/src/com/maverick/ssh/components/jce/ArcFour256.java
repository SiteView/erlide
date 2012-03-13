package com.maverick.ssh.components.jce;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ArcFour256 extends AbstractJCECipher
{
  public ArcFour256()
    throws IOException
  {
    super("ARCFOUR", "ARCFOUR", 32, "arcfour256");
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IOException
  {
    try
    {
      this.g = (JCEProvider.getProviderForAlgorithm(this.h) == null ? Cipher.getInstance(this.h) : Cipher.getInstance(this.h, JCEProvider.getProviderForAlgorithm(this.h)));
      if (this.g == null)
        throw new IOException("Failed to create cipher engine for " + this.h);
      byte[] arrayOfByte1 = new byte[this.i];
      System.arraycopy(paramArrayOfByte2, 0, arrayOfByte1, 0, arrayOfByte1.length);
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte1, this.j);
      this.g.init(paramInt == 0 ? 1 : 2, localSecretKeySpec);
      byte[] arrayOfByte2 = new byte[1536];
      this.g.update(arrayOfByte2);
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new IOException("Padding type not supported");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new IOException("Algorithm not supported:" + this.h);
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

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.ArcFour256
 * JD-Core Version:    0.6.0
 */