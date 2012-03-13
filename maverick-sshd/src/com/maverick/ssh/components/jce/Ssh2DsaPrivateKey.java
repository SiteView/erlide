package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.components.SshDsaPrivateKey;
import com.maverick.ssh.components.SshDsaPublicKey;
import com.maverick.util.SimpleASNReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;

public class Ssh2DsaPrivateKey
  implements SshDsaPrivateKey
{
  protected DSAPrivateKey prv;
  private Ssh2DsaPublicKey E;

  public Ssh2DsaPrivateKey(DSAPrivateKey paramDSAPrivateKey, DSAPublicKey paramDSAPublicKey)
  {
    this.prv = paramDSAPrivateKey;
    this.E = new Ssh2DsaPublicKey(paramDSAPublicKey);
  }

  public Ssh2DsaPrivateKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5)
    throws SshException
  {
    try
    {
      KeyFactory localKeyFactory = JCEProvider.getProviderForAlgorithm("DSA") == null ? KeyFactory.getInstance("DSA") : KeyFactory.getInstance("DSA", JCEProvider.getProviderForAlgorithm("DSA"));
      DSAPrivateKeySpec localDSAPrivateKeySpec = new DSAPrivateKeySpec(paramBigInteger4, paramBigInteger1, paramBigInteger2, paramBigInteger3);
      this.prv = ((DSAPrivateKey)localKeyFactory.generatePrivate(localDSAPrivateKeySpec));
      this.E = new Ssh2DsaPublicKey(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger5);
    }
    catch (Throwable localThrowable)
    {
      throw new SshException(localThrowable);
    }
  }

  public byte[] sign(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      Signature localSignature = JCEProvider.getProviderForAlgorithm("SHA1WithDSA") == null ? Signature.getInstance("SHA1WithDSA") : Signature.getInstance("SHA1WithDSA", JCEProvider.getProviderForAlgorithm("SHA1WithDSA"));
      localSignature.initSign(this.prv);
      localSignature.update(paramArrayOfByte);
      byte[] arrayOfByte1 = localSignature.sign();
      byte[] arrayOfByte2 = new byte[40];
      SimpleASNReader localSimpleASNReader = new SimpleASNReader(arrayOfByte1);
      localSimpleASNReader.getByte();
      localSimpleASNReader.getLength();
      localSimpleASNReader.getByte();
      byte[] arrayOfByte3 = localSimpleASNReader.getData();
      localSimpleASNReader.getByte();
      byte[] arrayOfByte4 = localSimpleASNReader.getData();
      if (arrayOfByte3.length >= 20)
        System.arraycopy(arrayOfByte3, arrayOfByte3.length - 20, arrayOfByte2, 0, 20);
      else
        System.arraycopy(arrayOfByte3, 0, arrayOfByte2, 20 - arrayOfByte3.length, arrayOfByte3.length);
      if (arrayOfByte4.length >= 20)
        System.arraycopy(arrayOfByte4, arrayOfByte4.length - 20, arrayOfByte2, 20, 20);
      else
        System.arraycopy(arrayOfByte4, 0, arrayOfByte2, 20 + (20 - arrayOfByte4.length), arrayOfByte4.length);
      return arrayOfByte2;
    }
    catch (Exception localException)
    {
    }
    throw new IOException("Failed to sign data! " + localException.getMessage());
  }

  public String getAlgorithm()
  {
    return "ssh-dss";
  }

  public SshDsaPublicKey getPublicKey()
  {
    return this.E;
  }

  public BigInteger getX()
  {
    return this.prv.getX();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh2DsaPrivateKey
 * JD-Core Version:    0.6.0
 */