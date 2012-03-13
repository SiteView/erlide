package com.maverick.ssh.components.jce;

import com.maverick.ssh.components.SshCipher;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AbstractJCECipher extends SshCipher
{
  Cipher g;
  String h;
  String j;
  int i;

  public AbstractJCECipher(String paramString1, String paramString2, int paramInt, String paramString3)
    throws IOException
  {
    super(paramString3);
    this.h = paramString1;
    this.i = paramInt;
    this.j = paramString2;
    try
    {
      this.g = (JCEProvider.getProviderForAlgorithm(paramString1) == null ? Cipher.getInstance(paramString1) : Cipher.getInstance(paramString1, JCEProvider.getProviderForAlgorithm(paramString1)));
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new IOException("Padding type not supported");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new IOException("Algorithm not supported:" + paramString1);
    }
    if (this.g == null)
      throw new IOException("Failed to create cipher engine for " + paramString1);
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    if (paramInt3 > 0)
    {
      byte[] arrayOfByte = this.g.update(paramArrayOfByte1, paramInt1, paramInt3);
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte2, paramInt2, paramInt3);
    }
  }

  public String getProvider()
  {
    return this.g.getProvider().getName();
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IOException
  {
    try
    {
      byte[] arrayOfByte = new byte[this.i];
      System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, arrayOfByte.length);
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte, this.j);
      this.g.init(paramInt == 0 ? 1 : 2, localSecretKeySpec, new IvParameterSpec(paramArrayOfByte1, 0, this.g.getBlockSize()));
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new IOException("Invalid encryption key");
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
      throw new IOException("Invalid algorithm parameter");
    }
  }

  public int getBlockSize()
  {
    return this.g.getBlockSize();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AbstractJCECipher
 * JD-Core Version:    0.6.0
 */