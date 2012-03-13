package com.maverick.ssh.components.jce;

import com.maverick.ssh.SshException;
import com.maverick.ssh.SshKeyFingerprint;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh.components.SshRsaPublicKey;
import com.maverick.util.ByteArrayReader;
import com.maverick.util.ByteArrayWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;

public class Ssh2RsaPublicKey
  implements SshRsaPublicKey
{
  RSAPublicKey c;

  public Ssh2RsaPublicKey()
  {
  }

  public Ssh2RsaPublicKey(RSAPublicKey paramRSAPublicKey)
  {
    this.c = paramRSAPublicKey;
  }

  public Ssh2RsaPublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    KeyFactory localKeyFactory = JCEProvider.getProviderForAlgorithm("RSA") == null ? KeyFactory.getInstance("RSA") : KeyFactory.getInstance("RSA", JCEProvider.getProviderForAlgorithm("RSA"));
    RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(paramBigInteger1, paramBigInteger2);
    this.c = ((RSAPublicKey)localKeyFactory.generatePublic(localRSAPublicKeySpec));
  }

  public byte[] getEncoded()
    throws SshException
  {
    try
    {
      ByteArrayWriter localByteArrayWriter = new ByteArrayWriter();
      localByteArrayWriter.writeString(getAlgorithm());
      localByteArrayWriter.writeBigInteger(this.c.getPublicExponent());
      localByteArrayWriter.writeBigInteger(this.c.getModulus());
      return localByteArrayWriter.toByteArray();
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException("Failed to encoded key data", 5, localIOException);
  }

  public String getFingerprint()
    throws SshException
  {
    return SshKeyFingerprint.getFingerprint(getEncoded());
  }

  public int getBitLength()
  {
    return this.c.getModulus().bitLength();
  }

  public void init(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SshException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(paramArrayOfByte, paramInt1, paramInt2);
      String str = localByteArrayReader.readString();
      if (!str.equals(getAlgorithm()))
        throw new SshException("The encoded key is not RSA", 5);
      BigInteger localBigInteger1 = localByteArrayReader.readBigInteger();
      BigInteger localBigInteger2 = localByteArrayReader.readBigInteger();
      RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(localBigInteger2, localBigInteger1);
      try
      {
        KeyFactory localKeyFactory = JCEProvider.getProviderForAlgorithm("RSA") == null ? KeyFactory.getInstance("RSA") : KeyFactory.getInstance("RSA", JCEProvider.getProviderForAlgorithm("RSA"));
        this.c = ((RSAPublicKey)localKeyFactory.generatePublic(localRSAPublicKeySpec));
      }
      catch (Exception localException)
      {
        throw new SshException("Failed to obtain RSA key instance from JCE", 5, localException);
      }
    }
    catch (IOException localIOException)
    {
      throw new SshException("Failed to read encoded key data", 5);
    }
  }

  public String getAlgorithm()
  {
    return "ssh-rsa";
  }

  public boolean verifySignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SshException
  {
    try
    {
      if (paramArrayOfByte1.length != 128)
      {
        localObject = new ByteArrayReader(paramArrayOfByte1);
        byte[] arrayOfByte = ((ByteArrayReader)localObject).readBinaryString();
        String str = new String(arrayOfByte);
        paramArrayOfByte1 = ((ByteArrayReader)localObject).readBinaryString();
      }
      Object localObject = JCEProvider.getProviderForAlgorithm("SHA1WithRSA") == null ? Signature.getInstance("SHA1WithRSA") : Signature.getInstance("SHA1WithRSA", JCEProvider.getProviderForAlgorithm("SHA1WithRSA"));
      ((Signature)localObject).initVerify(this.c);
      ((Signature)localObject).update(paramArrayOfByte2);
      return ((Signature)localObject).verify(paramArrayOfByte1);
    }
    catch (Exception localException)
    {
    }
    throw new SshException(16, localException);
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

  public BigInteger doPublic(BigInteger paramBigInteger)
    throws SshException
  {
    try
    {
      Cipher localCipher = JCEProvider.getProviderForAlgorithm("RSA") == null ? Cipher.getInstance("RSA") : Cipher.getInstance("RSA", JCEProvider.getProviderForAlgorithm("RSA"));
      localCipher.init(1, this.c, JCEProvider.getSecureRandom());
      byte[] arrayOfByte = paramBigInteger.toByteArray();
      return new BigInteger(localCipher.doFinal(arrayOfByte, arrayOfByte[0] == 0 ? 1 : 0, arrayOfByte[0] == 0 ? arrayOfByte.length - 1 : arrayOfByte.length));
    }
    catch (Throwable localThrowable)
    {
      if (localThrowable.getMessage().indexOf("RSA") > -1)
        throw new SshException("JCE provider requires BouncyCastle provider for RSA/NONE/PKCS1Padding component. Add bcprov.jar to your classpath or configure an alternative provider for this algorithm", 5);
    }
    throw new SshException(localThrowable);
  }

  public BigInteger getModulus()
  {
    return this.c.getModulus();
  }

  public BigInteger getPublicExponent()
  {
    return this.c.getPublicExponent();
  }

  public int getVersion()
  {
    return 2;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh.components.jce.Ssh2RsaPublicKey
 * JD-Core Version:    0.6.0
 */