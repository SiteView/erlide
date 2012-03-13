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
  Cipher W;
  String X;
  String Z;
  int Y;

  public AbstractJCECipher(String paramString1, String paramString2, int paramInt, String paramString3)
    throws IOException
  {
    super(paramString3);
    this.X = paramString1;
    this.Y = paramInt;
    this.Z = paramString2;
    try
    {
      this.W = (JCEProvider.getProviderForAlgorithm(paramString1) == null ? Cipher.getInstance(paramString1) : Cipher.getInstance(paramString1, JCEProvider.getProviderForAlgorithm(paramString1)));
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new IOException("Padding type not supported");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new IOException("Algorithm not supported:" + paramString1);
    }
    if (this.W == null)
      throw new IOException("Failed to create cipher engine for " + paramString1);
  }

  public void transform(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
    throws IOException
  {
    if (paramInt3 > 0)
    {
      byte[] arrayOfByte = this.W.update(paramArrayOfByte1, paramInt1, paramInt3);
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte2, paramInt2, paramInt3);
    }
  }

  public String getProvider()
  {
    return this.W.getProvider().getName();
  }

  public void init(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws IOException
  {
    try
    {
      byte[] arrayOfByte = new byte[this.Y];
      System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, arrayOfByte.length);
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte, this.Z);
      this.W.init(paramInt == 0 ? 1 : 2, localSecretKeySpec, new IvParameterSpec(paramArrayOfByte1, 0, this.W.getBlockSize()));
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
    return this.W.getBlockSize();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.AbstractJCECipher
 * JD-Core Version:    0.6.0
 */