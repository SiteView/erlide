package com.maverick.ssh.components.standalone;

import com.maverick.crypto.publickey.Rsa;
import com.maverick.crypto.publickey.RsaPublicKey;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshKeyFingerprint;
import com.maverick.ssh.components.SshRsaPublicKey;
import java.math.BigInteger;

public class Ssh1RsaPublicKey extends RsaPublicKey
  implements SshRsaPublicKey
{
  public Ssh1RsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
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

  public void init(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
  }

  public String getFingerprint()
    throws SshException
  {
    return SshKeyFingerprint.getFingerprint(getEncoded());
  }

  public String getAlgorithm()
  {
    return "rsa1";
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof Ssh1RsaPublicKey))
      try
      {
        return ((Ssh1RsaPublicKey)paramObject).getFingerprint().equals(getFingerprint());
      }
      catch (SshException localSshException)
      {
      }
    return false;
  }

  public int hashCode()
  {
    try
    {
      return getFingerprint().hashCode();
    }
    catch (SshException localSshException)
    {
    }
    return 0;
  }

  public int getVersion()
  {
    return 1;
  }

  public BigInteger doPublic(BigInteger paramBigInteger)
  {
    paramBigInteger = Rsa.padPKCS1(paramBigInteger, 2, (getModulus().bitLength() + 7) / 8);
    return Rsa.doPublic(paramBigInteger, getModulus(), getPublicExponent());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Ssh1RsaPublicKey
 * JD-Core Version:    0.6.0
 */