package com.maverick.ssh.components.standalone;

import com.maverick.crypto.publickey.DsaPublicKey;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshKeyFingerprint;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.math.BigInteger;

public class Ssh2DsaPublicKey extends DsaPublicKey
  implements SshDsaPublicKey
{
  public Ssh2DsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    super(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4);
  }

  public String getAlgorithm()
  {
    return "ssh-dss";
  }

  public Ssh2DsaPublicKey()
  {
  }

  public byte[] getEncoded()
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(getAlgorithm());
      localByteArrayWriter.writeBigInteger(this.p);
      localByteArrayWriter.writeBigInteger(this.q);
      localByteArrayWriter.writeBigInteger(this.g);
      localByteArrayWriter.writeBigInteger(this.y);
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
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte, paramInt1, paramInt2);
      String str = localByteArrayReader.readString();
      if (!str.equals(getAlgorithm()))
        throw new SshException("Invalid public key header", 4);
      this.p = localByteArrayReader.readBigInteger();
      this.q = localByteArrayReader.readBigInteger();
      this.g = localByteArrayReader.readBigInteger();
      this.y = localByteArrayReader.readBigInteger();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException, 5);
    }
  }

  public boolean verifySignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    try
    {
      if (paramArrayOfByte1.length != 40)
      {
        ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte1, 0, paramArrayOfByte1.length);
        byte[] arrayOfByte = localByteArrayReader.readBinaryString();
        String str = new String(arrayOfByte);
        if (!str.equals(getAlgorithm()))
          return false;
        paramArrayOfByte1 = localByteArrayReader.readBinaryString();
      }
      return super.verifySignature(paramArrayOfByte1, paramArrayOfByte2);
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof Ssh2DsaPublicKey))
      try
      {
        return ((Ssh2DsaPublicKey)paramObject).getFingerprint().equals(getFingerprint());
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
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.standalone.Ssh2DsaPublicKey
 * JD-Core Version:    0.6.0
 */