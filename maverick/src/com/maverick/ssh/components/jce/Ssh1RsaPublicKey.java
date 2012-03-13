package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

public class Ssh1RsaPublicKey extends Ssh2RsaPublicKey
{
  public Ssh1RsaPublicKey(RSAPublicKey paramRSAPublicKey)
  {
    super(paramRSAPublicKey);
  }

  public Ssh1RsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    super(paramBigInteger1, paramBigInteger2);
  }

  public byte[] getEncoded()
    throws SshException
  {
    try
    {
      byte[] arrayOfByte1 = getModulus().toByteArray();
      byte[] arrayOfByte2 = getPublicExponent().toByteArray();
      int i = arrayOfByte1[0] == 0 ? 1 : 0;
      int j = arrayOfByte2[0] == 0 ? 1 : 0;
      byte[] arrayOfByte3 = new byte[arrayOfByte1.length + arrayOfByte2.length - i - j];
      System.arraycopy(arrayOfByte1, i, arrayOfByte3, 0, arrayOfByte1.length - i);
      System.arraycopy(arrayOfByte2, j, arrayOfByte3, arrayOfByte1.length - i, arrayOfByte2.length - j);
      return arrayOfByte3;
    }
    catch (Throwable localThrowable)
    {
      if ("Ssh1RsaPublicKey.getEncoded() caught an exception: " + localThrowable.getMessage() != null)
        tmpTernaryOp = localThrowable.getMessage();
    }
    throw new SshException(localThrowable.getClass().getName(), 5);
  }

  public String getAlgorithm()
  {
    return "rsa1";
  }

  public int getVersion()
  {
    return 1;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh1RsaPublicKey
 * JD-Core Version:    0.6.0
 */