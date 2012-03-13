package com.maverick.ssh.components.standalone;

import com.maverick.crypto.publickey.Rsa;
import com.maverick.crypto.publickey.RsaPublicKey;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshKeyFingerprint;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.math.BigInteger;

public class Ssh2RsaPublicKey extends RsaPublicKey
  implements SshRsaPublicKey
{
  public Ssh2RsaPublicKey()
  {
  }

  public Ssh2RsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(paramBigInteger1, paramBigInteger2);
  }

  public byte[] getEncoded()
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(getAlgorithm());
      localByteArrayWriter.writeBigInteger(getPublicExponent());
      localByteArrayWriter.writeBigInteger(getModulus());
      return localByteArrayWriter.toByteArray();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException, 5);
  }

  public String getFingerprint()
    throws SshException
  {
    return SshKeyFingerprint.getFingerprint(getEncoded());
  }

  public void init(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte);
      String str = localByteArrayReader.readString();
      if (!str.equals(getAlgorithm()))
        throw new SshException("Invalid ssh-rsa key", 4);
      setPublicExponent(localByteArrayReader.readBigInteger());
      setModulus(localByteArrayReader.readBigInteger());
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public String getAlgorithm()
  {
    return "ssh-rsa";
  }

  public boolean verifySignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte1, 0, paramArrayOfByte1.length);
      byte[] arrayOfByte = localByteArrayReader.readBinaryString();
      String str = new String(arrayOfByte);
      if (!str.equals(getAlgorithm()))
        return false;
      paramArrayOfByte1 = localByteArrayReader.readBinaryString();
      return super.verifySignature(paramArrayOfByte1, paramArrayOfByte2);
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof SshRsaPublicKey))
      try
      {
        return ((SshPublicKey)paramObject).getFingerprint().equals(getFingerprint());
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
    return 2;
  }

  public BigInteger doPublic(BigInteger paramBigInteger)
  {
    paramBigInteger = Rsa.padPKCS1(paramBigInteger, 2, (getModulus().bitLength() + 7) / 8);
    return Rsa.doPublic(paramBigInteger, getModulus(), getPublicExponent());
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Ssh2RsaPublicKey
 * JD-Core Version:    0.6.0
 */